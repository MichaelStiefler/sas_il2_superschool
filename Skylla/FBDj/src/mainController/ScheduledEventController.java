package mainController;

import java.io.File;

import model.MissionFile;
import model.ScheduledEvent;

public class ScheduledEventController {

    public static void checkForEvent() {

        ScheduledEvent event = null;
        // Look for a Scheduled Mission First
        event = MySQLConnectionController.missionToExecute();
        if (event != null) {
            // Scheduled Mission is found so run it
            executeScheduledMission(event);
        } else {
            // No scheduled Mission Found, check for Mission Cycle Change
            // 1st well see if there is one scheduled for today
            event = MySQLConnectionController.missionCycleToExecute("Once");
            if (event != null) {
                // Mission Cycle Change
                setMissionCycle(event);
            } else {
                // No scheduled Mission Change for today, look for daily changes
                event = MySQLConnectionController.missionCycleToExecute("Daily");
                if (event != null) {
                    // Mission Cycle Change
                    setMissionCycle(event);
                }
            }
            // If no Scheduled Mission Cycle changes are present, remove any that are left in Mission Control
            // So it can revert back to the Default Mission Cycle
            if (event == null && MainController.MISSIONCONTROL.getMissionCycleEvent() != null) {
                setMissionCycle(null);
            }
        }

    }

    public static void executeScheduledMission(ScheduledEvent event) {
        String newMission = event.getEventName();
        // Set the Mission file and FBDj files with path. (Files need to be in the FBDj missions directory)
        File missionFile = new File(MainController.CONFIG.getServerDirectory() + "Missions/FBDj/" + newMission + ".mis");
        File missionParameterFile = new File(MainController.CONFIG.getServerDirectory() + "Missions/FBDj/" + newMission + ".fbdj");
        if (missionFile.canRead() && missionParameterFile.canRead()) {
            MissionFile tempMission = new MissionFile(newMission);
            MainController.MISSIONCONTROL.setTempMission(tempMission);
            MainController.MISSIONCONTROL.setMissionOver(true);
            ServerCommandController.serverCommandSend("chat Mission Will Change To ( " + newMission + ") TO ALL");
            // Update Calendar events to show mission has been executed
            MySQLConnectionController.updateCalendarEventMissionExecuted(event.getEventId());
            // Reset mission fields
            MainController.writeDebugLogFile(1, "ScheduledEventController.executeScheduledMission - Starting Scheduled Mission ( " + newMission + " ) in path ( " + MainController.CONFIG.getServerDirectory() + "Missions/FBDj/ )");

        } else {
            MainController.writeDebugLogFile(1, "ScheduledEventController.executeScheduledMission - Unable to open Scheduled Mission ( " + newMission + " ) in path ( " + MainController.CONFIG.getServerDirectory() + "Missions/FBDj/ )");
        }
    }

    public static void setMissionCycle(ScheduledEvent event) {
        MainController.MISSIONCONTROL.setMissionCycleEvent(event);
        MainController.MISSIONCONTROL.setResetMissionCycle(true);
    }

}
