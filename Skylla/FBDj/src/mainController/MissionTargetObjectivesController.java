package mainController;

import java.util.ArrayList;

import model.Mission;
import model.MissionTargetObjective;

class MissionTargetObjectivesController {

    public static long getTargetPercentToComplete(MissionTargetObjective missionTargetObjective) {
        long pctToComplete = 0;
        double tempPctToComplete = 0.0;

        if (missionTargetObjective.getNumberToDestroy() > 0) {
            if (missionTargetObjective.getTargetsLost() < missionTargetObjective.getNumberToDestroy()) {
                tempPctToComplete = ((Double.valueOf(missionTargetObjective.getNumberToDestroy() - missionTargetObjective.getTargetsLost()) / Double.valueOf(missionTargetObjective.getNumberToDestroy())) * 100.0);
            }
        }
        pctToComplete = Math.round(tempPctToComplete);
        return pctToComplete;
    }

    public static Boolean isTargetObjectiveComplete(MissionTargetObjective missionTargetObjective) {
        Boolean objectiveComplete = false;
        String chatMessage;

        if (missionTargetObjective.getTargetsLost() >= missionTargetObjective.getNumberToDestroy() && missionTargetObjective.getNumberToDestroy() > 0) {
            if (!missionTargetObjective.getObjectiveMet()) {
                missionTargetObjective.setObjectiveMet(true);
                if (missionTargetObjective.getArmy() == MainController.REDARMY) {
                    chatMessage = "chat Red Met their " + missionTargetObjective.getMapGridLocation() + " Objective TO ALL";
                } else {
                    chatMessage = "chat Blue Met their " + missionTargetObjective.getMapGridLocation() + " Objective TO ALL";
                }
                ServerCommandController.serverCommandSend(chatMessage);
            }
            objectiveComplete = true;
        }
        return objectiveComplete;
    }

    public static ArrayList<String> displayRemainingTargetObjectives(Mission mission, int army) {
        ArrayList<String> tgtListText = new ArrayList<String>();
        String displayText;
        String strArmy;
        int numberObjectives = 0;
        try {
            if (army == MainController.REDARMY) {
                strArmy = "Red";
            } else {
                strArmy = "Blue";
            }
            displayText = strArmy + "-";

            for (MissionTargetObjective targetObjective : mission.getMissionParameters().getTargetObjectives()) {
                if (targetObjective.getArmy() == army) {
                    if (MissionTargetObjectivesController.getTargetPercentToComplete(targetObjective) > 0) {

                        numberObjectives++;
                        if (numberObjectives > 3) {
                            tgtListText.add(displayText);
                            displayText = null;
                            numberObjectives = 1;
                            displayText = strArmy + "-";
                        }
                        displayText = displayText + " (" + targetObjective.getMapGridLocation() + "/" + targetObjective.getTargetDesc() + "-" + MissionTargetObjectivesController.getTargetPercentToComplete(targetObjective) + "%)";

                    }
                }
            }
            displayText = displayText + " Remaining";
            if (numberObjectives > 0) {
                tgtListText.add(displayText);
            }

            if (tgtListText.size() > 0) {
                return tgtListText;
            } else {
//				String returnString = "No "+strArmy+" Target Objectives";
//				tgtListText.add(returnString);
//				return tgtListText;
                return null;
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionTargetObjectiveController.displayRemainingTargetObjectives - Error Unhandled Exception: " + ex);
            return tgtListText;
        }
    }

    public static ArrayList<String> displayTargetObjectives(Mission mission, int army) {
        ArrayList<String> targetObjectives = new ArrayList<String>();
        String armyText = "";
        String displayText = "";
        try {
            if (army == MainController.REDARMY) {
                armyText = "Red-(T";
            } else {
                armyText = "Blue-(T";
            }
            displayText = armyText + "id)-Objective-(Start/Destroyed/To Win Count)";
            targetObjectives.add(displayText);
            int i = 0;
            for (MissionTargetObjective targetObjective : mission.getMissionParameters().getTargetObjectives()) {
                if (targetObjective.getArmy() == army) {
                    displayText = armyText + i + ")-" + targetObjective.getMapGridLocation() + "-(" + targetObjective.getTotalTargets() + "/" + targetObjective.getTargetsLost() + "/" + targetObjective.getNumberToDestroy() + ")";
                    targetObjectives.add(displayText);
                }
                i++;
            }
            return targetObjectives;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionTargetObjectiveController.displayCountObjectives - Error Unhandled exception: " + ex);
            return targetObjectives;
        }
    }

    public static String updateTargetObjective(Mission mission, int army, int targetId, int newDestroyCount) {
//		ArrayList<String> targetObjectives = new ArrayList<String>();
        String armyText = "";
        String displayText = "";
        try {
            if (army == MainController.REDARMY) {
                armyText = "Red-(T";
            } else {
                armyText = "Blue-(T";
            }
            for (int i = 0; i < mission.getMissionParameters().getTargetObjectives().size(); i++) {
                MissionTargetObjective targetObjective = mission.getMissionParameters().getTargetObjectives().get(i);

                if (i == targetId && targetObjective.getArmy() == army) {
                    if (targetObjective.getTargetsLost() > targetObjective.getTotalTargets()) {
                        displayText = "Target Objective already Completed, cannot update";
                    } else if (newDestroyCount > targetObjective.getTotalTargets()) {
                        displayText = "Cannot set Objective count(" + newDestroyCount + ") > Start count(" + targetObjective.getTotalTargets() + ")";
                    } else {
                        targetObjective.setNumberToDestroy(newDestroyCount);
                        displayText = armyText + i + ")-" + targetObjective.getMapGridLocation() + "-(" + targetObjective.getTotalTargets() + "/" + targetObjective.getTargetsLost() + "/" + targetObjective.getNumberToDestroy() + ")";
                    }
                }
            }
            return displayText;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionTargetObjectiveController.updateTargetObjective - Error Unhandled exception: " + ex);
            return "Exception updating target value";
        }

    }
}
