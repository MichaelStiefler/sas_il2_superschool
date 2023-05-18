package mainController;

import java.util.Iterator;

import model.Aerodrome;
import model.Mission;
import model.MissionCountObjective;
import model.MissionTargetObjective;
import model.PlaneLoadoutRestriction;
import utility.Time;
import view.MainWindowView2;
import viewController.MainWindowController;

public class MissionStatusPanelController {

    public static void initializeData(Mission currentMission) {
        try {
            if (currentMission == null) {
                currentMission = MainController.ACTIVEMISSION;
            }
            if (!MainWindowView2.missionStatusPanel.isDataChanged()) {
                removeMissionData();
                MainWindowView2.missionStatusPanel.setMissionStatusMissionNameLabel(currentMission.getMissionName());
                MainWindowView2.missionStatusPanel.setMissionStatusStartTimeValueLabel(Time.getTime(currentMission.getStartTime()));
                MainWindowView2.missionStatusPanel.setMissionStatusTimeLeftTF(Long.toString(MissionController.getMissionTimeLeft() / 60000));
                MainWindowView2.missionStatusPanel.setMissionObjectiveTypeTF(currentMission.getMissionParameters().getObjectiveType().toString());
                MainWindowView2.missionStatusPanel.setRedCountObjectivesNeededTF(currentMission.getMissionParameters().getRedCountObjectivesNeeded());
                MainWindowView2.missionStatusPanel.setBlueCountObjectivesNeededTF(currentMission.getMissionParameters().getBlueCountObjectivesNeeded());
                MainWindowView2.missionStatusPanel.setRedTargetObjectivesNeededTF(currentMission.getMissionParameters().getRedTargetObjectivesNeeded());
                MainWindowView2.missionStatusPanel.setBlueTargetObjectivesNeededTF(currentMission.getMissionParameters().getBlueTargetObjectivesNeeded());

                for (MissionCountObjective missionObjective : currentMission.getMissionParameters().getCountObjectives()) {
                    MainWindowView2.missionStatusPanel.addObjectiveTableRow("COUNT", missionObjective.getArmy(), missionObjective.getObjectType().toString(), "", missionObjective.getMissionStartCount(), missionObjective.getMissionLostCount(),
                            missionObjective.getNumberToDestroy());
                }
                for (MissionTargetObjective missionObjective : currentMission.getMissionParameters().getTargetObjectives()) {
                    MainWindowView2.missionStatusPanel.addObjectiveTableRow("TARGET", missionObjective.getArmy(), missionObjective.getMapGridLocation(), missionObjective.getTargetDesc(), missionObjective.getTotalTargets(),
                            missionObjective.getTargetsLost(), missionObjective.getNumberToDestroy());
                }

                for (PlaneLoadoutRestriction loadoutRestriction : currentMission.getMissionParameters().getPlaneLoadoutRestrictions()) {
                    MainWindowView2.missionStatusPanel.addLoadoutRestrictionRow(loadoutRestriction.getArmy(), loadoutRestriction.getPlane(), loadoutRestriction.getWeapon());
                }

                for (Aerodrome aerodrome : currentMission.getMissionParameters().getAerodromes()) {
                    String plane;
                    String strAerodrome;
                    Iterator<String> it = aerodrome.getPlanes().keySet().iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        int army = aerodrome.getArmy();
                        if (army == MainController.REDARMY) {
                            strAerodrome = "Red(";
                        } else {
                            strAerodrome = "Blue(";
                        }
                        strAerodrome += aerodrome.getAerodromeMapGrid() + ")";
                        plane = aerodrome.getPlanes().get(key).getPlaneName();
                        MainWindowView2.missionStatusPanel.addPlaneLimitRow(strAerodrome, plane, aerodrome.getPlanes().get(key).getPlanesLost(), aerodrome.getPlanes().get(key).getPlanesInUse(), aerodrome.getPlanes().get(key).getPlanesInUseLimit(),
                                aerodrome.getPlanes().get(key).getPlaneTotalLimit(), aerodrome);
                    }
                }
                MainWindowController.setStatusMessage("Running Mission ( " + currentMission.getMissionName() + " )");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionStatusPanelController.initializeData - Error Unhandled Exception: " + ex);
        }
    }

    public static void removeMissionData() {
        MainWindowView2.missionStatusPanel.removeObjectiveTableData();
    }

    public static void displayMessage(String message, String messageType) {
        if (messageType.equals("Error")) {
            MainWindowView2.missionStatusPanel.displayErrorMessage(message);
        }
    }
}