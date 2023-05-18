package viewController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import mainController.MainController;
import mainController.MySQLConnectionController;
import model.DifficultySettings;
import model.MissionCycle;
import model.MissionCycleEntry;
import model.MissionFile;
import model.UserMissionCycles;
import utility.FileRead;
import utility.FileWrite;
import view.MissionCyclePanel;

public class MissionCycleController {

    public static String addMissionCycle(String name) {
        String returnMessage = null;
        Iterator<String> it = MissionCyclePanel.getUserMissionCycles().getMissionCycleList().keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (key.equals(name)) {
                returnMessage = "Mission Cycle Already Exists";
                break;
            }
        }
        if (returnMessage == null) {
            MissionCycle newMissionCycle = new MissionCycle(name);
            MissionCyclePanel.getUserMissionCycles().getMissionCycleList().put(name, newMissionCycle);
        }
        return returnMessage;

    }

    public static void removeMissionCycle(String name) {
        if (MissionCyclePanel.getUserMissionCycles().getMissionCycleList().containsKey(name)) {
            MissionCyclePanel.getUserMissionCycles().getMissionCycleList().remove(name);
        }
    }

    public static void removeMissionFile(String missionCycle, String name) {
        MissionCycle tempMissionCycle = null;
        tempMissionCycle = MissionCyclePanel.getUserMissionCycles().getMissionCycleList().get(missionCycle);
        if (tempMissionCycle != null) {
//            for (MissionFile missionFile : tempMissionCycle.getMissionFiles())
//            {
//            	if (missionFile.getMissionName().equals(name))
//           	{
//            		tempMissionCycle.getMissionFiles().remove(missionFile);
//            		break;
//            	}
//            }
        }
    }

    public static String addDifficultySetup(String name) {
        String returnMessage = null;
        Iterator<String> it = MissionCyclePanel.getUserMissionCycles().getDifficultySettings().keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (key.equals(name)) {
                returnMessage = "Difficulty Setup Already Exists";
                break;
            }
        }
        if (returnMessage == null) {
            DifficultySettings newDifficultySetup = new DifficultySettings(name);
            MissionCyclePanel.getUserMissionCycles().getDifficultySettings().put(name, newDifficultySetup);
        }
        return returnMessage;

    }

    public static void removeDifficultySetup(String name) {
        if (MissionCyclePanel.getUserMissionCycles().getDifficultySettings().containsKey(name)) {
            MissionCyclePanel.getUserMissionCycles().getDifficultySettings().remove(name);
        }
    }

    public static void updateMissionFile(UserMissionCycles missionCycle, String directory, String missionName, String difficulty, String redWonMission, String blueWonMission) {
        MissionFile redMission = null;
        MissionFile blueMission = null;
        String redMissionName = "None";
        String blueMissionName = "None";
        redMission = missionCycle.getMissionFiles().get(redWonMission);
        if (redMission != null) {
            redMissionName = redMission.getMissionName();
        }
        blueMission = missionCycle.getMissionFiles().get(blueWonMission);
        if (blueMission != null) {
            blueMissionName = blueMission.getMissionName();
        }
        // Update Mission File Difficulty setting.
        MissionFile missionFile = null;
        missionFile = missionCycle.getMissionFiles().get(missionName);
        if (missionFile != null) {
            missionFile.setDifficulty(difficulty);
            missionFile.setRedWonMission(redMissionName);
            missionFile.setBlueWonMission(blueMissionName);
        } else {
            missionFile = new MissionFile(missionName); // FIXME: Storebror temporary Codefix
            missionFile.setMissionName(missionName);
            missionFile.setDirectory(directory);
            missionFile.setDifficulty(difficulty);
            missionFile.setRedWonMission(redMissionName);
            missionFile.setBlueWonMission(blueMissionName);
            missionCycle.getMissionFiles().put(missionName, missionFile);
        }
    }

    public static void reOrderMissionFileList(String missionCycle, ArrayList<MissionCycleEntry> missionCycleEntries) {
        if (MissionCyclePanel.getUserMissionCycles().getMissionCycleList().containsKey(missionCycle)) {
            MissionCyclePanel.getUserMissionCycles().getMissionCycleList().get(missionCycle).getMissionFiles().clear();
            for (MissionCycleEntry missionCycleEntry : missionCycleEntries) {
                MissionCyclePanel.getUserMissionCycles().getMissionCycleList().get(missionCycle).getMissionFiles().add(missionCycleEntry);
            }
        }
    }

    public static void updateDifficultySettings(String difficultySetup, String setting, boolean settingValue) {
        MissionCyclePanel.getUserMissionCycles().getDifficultySettings().get(difficultySetup).setSetting(setting, settingValue);
    }

    public static UserMissionCycles loadMissionCycleList() {
        UserMissionCycles userMissionCycles;
        String missionCycleFile = MainController.CONFIG.getConfigDirectory() + "MissionCycles.FBDj";
        // Load list (or create new if it doesn't exist)
        if ((userMissionCycles = (UserMissionCycles) FileRead.getFileSerialized(missionCycleFile)) == null) {
            userMissionCycles = new UserMissionCycles();
            // Since there was no default Mission Cycle File hense no default Difficulty setup add a 'default' one.
            DifficultySettings newDifficultySetup = new DifficultySettings("Default");
            userMissionCycles.getDifficultySettings().put("Default", newDifficultySetup);
            MainController.writeDebugLogFile(2, "MissionCycleController.loadMissionCycleList: could not find MissionCycle file! Creating new list.");
        } else {
            MainController.writeDebugLogFile(2, "MissionCycleController.loadMissionCycleList: UserMissionCycleList loaded from file.");
        }

        return userMissionCycles;
    }

    public static void saveMissionCycles(UserMissionCycles missionCycles) {
        String missionCycleFileDirectory = MainController.CONFIG.getConfigDirectory();
        FileWrite.writeFileSerialized(missionCycleFileDirectory, "MissionCycles.FBDj", missionCycles);
        MainController.writeDebugLogFile(2, "MissionCycleController.saveMissionCycles: MissionCycle list written to file.");
    }

    public static String validateMissionPath(String filePath) {
        String missionPath = null;
        String IL2missionDirectory = MainController.CONFIG.getServerDirectory() + "Missions" + File.separatorChar;
        missionPath = filePath.substring(IL2missionDirectory.length());
        File testFile = new File(IL2missionDirectory + missionPath);
        if (!testFile.isDirectory()) {
            JOptionPane.showMessageDialog(null, "Mission File Needs to be in the \n" + "IL2 Server Mission directory (" + IL2missionDirectory + ")", "Warning", JOptionPane.WARNING_MESSAGE);
        }
        return missionPath;
    }

    public static String getIL2serverMissionPath() {
        String IL2missionDirectory = MainController.CONFIG.getServerDirectory() + "Missions" + File.separatorChar;
        return IL2missionDirectory;
    }

    public static Object[] getMissionCycleList() {
        Object[] missionCycleList = null;
        @SuppressWarnings("rawtypes")
        DefaultListModel missionCycleModel = MissionCyclePanel.getMissionCycleListModel();
        missionCycleList = missionCycleModel.toArray();
        return missionCycleList;
    }

    public static void resetMissionCycle(String selectedCycle, boolean now, int selectedMission) {
        // If a mission was selected use that one, otherwise use the 1st mission in rotation
        if (selectedMission != -1) {
            MainController.MISSIONCONTROL.setRequestedMissionPointer(selectedMission);
        } else {
            MainController.MISSIONCONTROL.setRequestedMissionPointer(0);
        }
        if (selectedCycle != null) {
            MainController.CONFIG.setMissionCycle(selectedCycle);
            MainController.writeDebugLogFile(1, "MissionCycleController.resetMissionCycle Mission Cycle set to: " + selectedCycle);
        }
        MainController.MISSIONCONTROL.setResetMissionCycle(true);
        if (now) {
            MainController.MISSIONCONTROL.setMissionOver(true);
        }
    }

    public static Boolean writeMissionCycles() {
        Object[] tempMissionCycles;
        boolean writeOK = false;
        ArrayList<String> missionCycles = new ArrayList<String>();
        tempMissionCycles = MissionCycleController.getMissionCycleList();
        for (int i = 0; i < tempMissionCycles.length; i++) {
            missionCycles.add(tempMissionCycles[i].toString());
        }
        writeOK = MySQLConnectionController.writeMissionCycles(missionCycles);
        return writeOK;
    }

    public static ArrayList<MissionFile> getAvailableMissionFiles() {
        try {
            ArrayList<MissionFile> availableMissions = new ArrayList<MissionFile>(1000);

            File missionDirectory = new File(MainController.getMissionDirectory() + "FBDj" + File.separatorChar);
            String[] missionFileList = missionDirectory.list();
            if (missionFileList != null) {
                for (int i = 0; i < missionFileList.length; i++) {
                    int fileSeparator = missionFileList[i].lastIndexOf(".");
                    if (fileSeparator > 0) {
                        String fileType = missionFileList[i].substring(fileSeparator + 1);
                        if (fileType.equalsIgnoreCase("FBDj")) {
                            String missionName = missionFileList[i].substring(0, fileSeparator);
                            MissionFile newMission = new MissionFile(missionName);
                            availableMissions.add(newMission);
                        }
                    }
                }
            }
            return availableMissions;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionCycleController.getAvailableMissionFiles - Unhandled Exception: " + ex);
            return null;
        }
    }

    public static void verifyCurrentMissionFiles() {
        UserMissionCycles userMissionCycle = MissionCyclePanel.getUserMissionCycles();
        ArrayList<String> missionsToDelete = new ArrayList<String>();
        ArrayList<String> msg = new ArrayList<String>();

        try {
            HashMap<String, MissionFile> currentMissionFiles = new HashMap<String, MissionFile>();
            currentMissionFiles = userMissionCycle.getMissionFiles();
            Boolean validMission = false;
            Iterator<String> it = currentMissionFiles.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                validMission = MainController.validateMission(currentMissionFiles.get(key));
                if (!validMission) {
                    missionsToDelete.add(key);
                }
            }
            if (missionsToDelete.size() > 0) {
                for (String missionName : missionsToDelete) {
                    msg.add("Mission (" + missionName + ") No Longer Available\n");
                    deleteMission(missionName);
                }
                saveMissionCycles(userMissionCycle);
            }
            // Decided not to show this, as it would halt the auto start if mission names changed. It is already written in log file.
//    		if (msg.size() > 0)
//    		{
//    			JOptionPane.showMessageDialog(null, msg, "Message", JOptionPane.INFORMATION_MESSAGE);
//    		}

        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionCycleController.verifyCurrentMissionFiles - Unhandled Exception: " + ex);
        }
    }

    public static UserMissionCycles addNewMissions(UserMissionCycles userMissionCycle) {
        ArrayList<MissionFile> availableMissions = new ArrayList<MissionFile>();
        availableMissions = MissionCycleController.getAvailableMissionFiles();
        if (availableMissions != null) {
            for (MissionFile mission : availableMissions) {
                if (!userMissionCycle.getMissionFiles().containsKey(mission.getMissionName())) {
                    userMissionCycle.getMissionFiles().put(mission.getMissionName(), mission);
                }
            }
        }
        return userMissionCycle;
    }

    public static void deleteMission(String missionName) {
        try {
            UserMissionCycles userMissionCycle = MissionCyclePanel.getUserMissionCycles();
            MainController.writeDebugLogFile(1, "MissionCycleController.verifyCurrentMissionFiles - Mission (" + missionName + ") not available, removed from list");
            // 1st lets delete the mission from the available missions list
            if (userMissionCycle.getMissionFiles().containsKey(missionName)) {
                userMissionCycle.getMissionFiles().remove(missionName);
            } else {
                MainController.writeDebugLogFile(1, "MissionCycleController.deleteMission - Error Mission do Delete (" + missionName + ") not found");
            }
            // 2nd lets loop through the rest of the Available Missions and remove any links to this mission
            // in the Red/Blue Won fields
            Iterator<String> it = userMissionCycle.getMissionFiles().keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                if (userMissionCycle.getMissionFiles().get(key).getRedWonMission().equals(missionName)) {
                    userMissionCycle.getMissionFiles().get(key).setRedWonMission("None");
                }
                if (userMissionCycle.getMissionFiles().get(key).getBlueWonMission().equals(missionName)) {
                    userMissionCycle.getMissionFiles().get(key).setBlueWonMission("None");
                }
            }
            // 3rd lets loop through the Mission Cycle Lists and remove the mission from the mission cycle
            it = userMissionCycle.getMissionCycleList().keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                for (int i = 0; i < userMissionCycle.getMissionCycleList().get(key).getMissionFiles().size(); i++) {
                    if (userMissionCycle.getMissionCycleList().get(key).getMissionFiles().get(i).getMissionName().equals(missionName)) {
                        userMissionCycle.getMissionCycleList().get(key).getMissionFiles().remove(i);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionCycleController.deleteMission - Error Unhandled exception deleting mission (" + missionName + "): " + ex);
        }
    }

    public static void addMissionToCycle(String missionName, String missionCycleName) {
        try {
            UserMissionCycles userMissionCycle = MissionCyclePanel.getUserMissionCycles();
            MissionCycle missionCycle = null;
            missionCycle = userMissionCycle.getMissionCycleList().get(missionCycleName);
            if (missionCycle != null) {
                if (userMissionCycle.getMissionFiles().containsKey(missionName)) {
                    MissionCycleEntry missionEntry = new MissionCycleEntry(missionName);
                    missionCycle.getMissionFiles().add(missionEntry);
                } else {
                    MainController.writeDebugLogFile(1, "MissionCycleController.addMissionToCycle - Error Mission to Add (" + missionName + ") not in available Missions");
                }
            } else {
                MainController.writeDebugLogFile(1, "MissionCycleController.addMissionToCycle - Error Mission Cycle(" + missionCycleName + ") does not exist");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionCycleController.addMissionToCycle - Unhandled Exception MissionCycle(" + missionCycleName + ") Mission(" + missionName + ")");
        }
    }

    public static void removeMissionCycleEntry(String missionName, String missionCycleName) {
        try {
            MissionCycle missionCycle = null;

            UserMissionCycles userMissionCycle = MissionCyclePanel.getUserMissionCycles();
            missionCycle = userMissionCycle.getMissionCycleList().get(missionCycleName);
            if (missionCycle != null) {
                ArrayList<MissionCycleEntry> missionEntries = missionCycle.getMissionFiles();
                for (int i = 0; i < missionEntries.size(); i++) {
                    if (missionEntries.get(i).getMissionName().equals(missionName)) {
                        missionEntries.remove(i);
                        MainController.writeDebugLogFile(2, "MissionCycleController.removeMissionFromCycle - Mission (" + missionName + ") Removed from Mission Cycle (" + missionCycleName + ")");
                        break;
                    }
                }
            } else {
                MainController.writeDebugLogFile(1, "MissionCycleController.removeMissionFromCycle - Error Mission Cycle (" + missionCycleName + ") not found");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionCycleController.removeMissionFromCycle - Unhandled Exception MissionCycle (" + missionCycleName + ") Mission (" + missionName + "): " + ex);
        }
    }

    public static Boolean writeMissionFiles() {
        ArrayList<String> missionFiles = new ArrayList<String>();
        boolean writeOK = false;
        File missionDirectory = new File(MainController.getMissionDirectory() + "FBDj" + File.separatorChar);
        String[] missionFileList = missionDirectory.list();
        for (int i = 0; i < missionFileList.length; i++) {
            int fileSeparator = missionFileList[i].lastIndexOf(".");
            String fileType = missionFileList[i].substring(fileSeparator + 1);
            if (fileType.equalsIgnoreCase("FBDj")) {
                missionFiles.add(missionFileList[i].substring(0, fileSeparator));
            }
        }
        writeOK = MySQLConnectionController.writeMissionFiles(missionFiles);
        return writeOK;
    }

}
