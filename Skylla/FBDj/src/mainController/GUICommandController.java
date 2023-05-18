package mainController;

import model.Aerodrome;
import model.GUICommand;
import model.IL2StaticObject;
import model.MissionCountObjective;
import model.MissionFile;
import model.MissionTargetObjective;
import model.SortieEvent;
import utility.Time;

public class GUICommandController {

    public static void processGUICommand(GUICommand guiCommand) {
        int newCount = 0;
        switch (guiCommand.getGuiCommand()) {
            case ADJTIMELEFT:
                long newTimeLeft = (Time.getTime() - MainController.ACTIVEMISSION.getStartTime() + ((Integer) guiCommand.getValue() * 60000));
                MainController.ACTIVEMISSION.getMissionParameters().setTimeLimit(newTimeLeft);
                ServerCommandController.serverCommandSend("chat Mission Time Remaining Reset to: " + guiCommand.getValue() + " Minutes TO ALL");
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                break;
            case ADJREDCNTOBJNEEDED:
                newCount = (Integer) guiCommand.getValue();
                MainController.ACTIVEMISSION.getMissionParameters().setRedCountObjectivesNeeded(newCount);
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                break;
            case ADJBLUECNTOBJNEEDED:
                newCount = (Integer) guiCommand.getValue();
                MainController.ACTIVEMISSION.getMissionParameters().setBlueCountObjectivesNeeded(newCount);
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                break;
            case ADJREDTGTOBJNEEDED:
                newCount = (Integer) guiCommand.getValue();
                MainController.ACTIVEMISSION.getMissionParameters().setRedTargetObjectivesNeeded(newCount);
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                break;
            case ADJBLUETGTOBJNEEDED:
                newCount = (Integer) guiCommand.getValue();
                MainController.ACTIVEMISSION.getMissionParameters().setBlueTargetObjectivesNeeded(newCount);
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                break;
            case ADJCOUNTOBJECTIVE:
                IL2StaticObject.ObjectType objectType = IL2DataLoadController.il2StaticObjectGetType(guiCommand.getName());
                MissionCountObjective countObjective = MissionCountObjectivesController.getMissionCountObjective(MainController.ACTIVEMISSION, guiCommand.getArmy(), objectType);
                if (countObjective != null) {
                    countObjective.setNumberToDestroy((Integer) guiCommand.getValue());
                }
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Army: " + guiCommand.getArmy() + " Objective: " + guiCommand.getName() + " New Value: " + guiCommand.getValue());
                break;
            case ADJTARGETOBJECTIVE:
                MissionTargetObjective targetObjective = (MissionTargetObjective) guiCommand.getChangeObject();
                targetObjective.setNumberToDestroy((Integer) guiCommand.getValue());
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Army: " + guiCommand.getArmy() + " Target: " + targetObjective.getMapGridLocation() + " New Value: " + guiCommand.getValue());
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                break;
            case ADJPLANELIMIT:
                Aerodrome aerodrome = (Aerodrome) guiCommand.getChangeObject();
                String plane = guiCommand.getName();
                String name = guiCommand.getIpAddress();

                int newLimit = (Integer) guiCommand.getValue();
                if (name.equals("In Use Limit")) {
                    aerodrome.getPlanes().get(plane).setPlanesInUseLimit(newLimit);
                } else {
                    aerodrome.getPlanes().get(plane).setPlaneTotalLimit(newLimit);
                }
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Plane: " + plane + " Column: " + name + " New Value: " + newLimit);
                break;
            case CHAT:
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                ServerCommandController.serverCommandSend(guiCommand.getValue().toString());
                break;
            case KICKPILOT:
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                PilotController.pilotKick(guiCommand.getName(), SortieEvent.EventType.PILOTKICKED);
                break;
            case KICKPILOTMARKINGS:
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                PilotController.pilotKick(guiCommand.getName(), SortieEvent.EventType.PILOTKICKEDMARKINGS);
                break;
            case KICKPILOTLANGUAGE:
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                PilotController.pilotKick(guiCommand.getName(), SortieEvent.EventType.PILOTKICKEDLANGUAGE);
                break;
            case ADDBAN:
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                PilotBanController.pilotBanAdd(guiCommand.getName(), guiCommand.getIpAddress(), "GUI", guiCommand.getDuration());
                break;
            case ADJBAN:
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                break;
            case REMOVEBAN:
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                break;
            case ADDADMIN:
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                break;
            case ADJADMIN:
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                break;
            case REMOVEADMIN:
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                break;
            case ADDRESERVEDNAME:
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                break;
            case ADJRESERVEDNAME:
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                break;
            case REMOVERESERVEDNAME:
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Value: " + guiCommand.getValue());
                break;
            case RUNTEMPMISSION:
                MissionFile tempMission = (MissionFile) guiCommand.getValue();
                MainController.MISSIONCONTROL.setTempMission(tempMission);
                if ((Boolean) guiCommand.getChangeObject() == true) {
                    MainController.MISSIONCONTROL.setMissionOver(true);
                }
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand." + guiCommand.getGuiCommand() + " Run Temp Mission( " + tempMission.getMissionName() + " ) Now ( " + (Boolean) guiCommand.getChangeObject() + " )");
                break;
            default:
                MainController.writeDebugLogFile(1, "GUICommandController.processGUICommand - Error Invalid guiCommand: " + guiCommand.getGuiCommand());
        }
    }
}
