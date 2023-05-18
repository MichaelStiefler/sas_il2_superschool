package mainController;

import java.util.ArrayList;

import model.IL2StaticObject;
import model.Mission;
import model.MissionCountObjective;

class MissionCountObjectivesController {

    public static MissionCountObjective getMissionCountObjective(Mission mission, int army, IL2StaticObject.ObjectType objectType) {
        MissionCountObjective returnCountObjective = null;
        for (MissionCountObjective missionCountObjective : mission.getMissionParameters().getCountObjectives()) {
            if (missionCountObjective.getArmy() == army && missionCountObjective.getObjectType() == objectType) {
                returnCountObjective = missionCountObjective;
                break;
            }
        }
        return returnCountObjective;
    }

    public static long getCountPercentToComplete(MissionCountObjective missionCountObjective) {
        long pctToComplete = 0;
        double temppctToComplete = 0.0;
        if (missionCountObjective.getNumberToDestroy() > 0) {
            if (missionCountObjective.getMissionLostCount() < missionCountObjective.getNumberToDestroy()) {
                temppctToComplete = ((Double.valueOf(missionCountObjective.getNumberToDestroy() - missionCountObjective.getMissionLostCount()) / Double.valueOf(missionCountObjective.getNumberToDestroy())) * 100.0);
            }
        }
        pctToComplete = Math.round(temppctToComplete);
        return pctToComplete;
    }

    public static Boolean isCountObjectiveComplete(MissionCountObjective missionCountObjective) {
        Boolean objectiveComplete = false;
        String chatMessage;
        String whichArmy;
        if (missionCountObjective.getMissionLostCount() >= missionCountObjective.getNumberToDestroy() && missionCountObjective.getNumberToDestroy() > 0) {
            if (!missionCountObjective.getObjectiveMet()) {
                missionCountObjective.setObjectiveMet(true);
                if (missionCountObjective.getArmy() == MainController.REDARMY) {
                    chatMessage = "chat Red completed their " + missionCountObjective.getObjectType() + " Objectives TO ALL";
                    whichArmy = "Red";
                } else {
                    chatMessage = "chat Blue completed their " + missionCountObjective.getObjectType() + " Objectives TO ALL";
                    whichArmy = "Blue";
                }
                if (missionCountObjective.getObjectType() == IL2StaticObject.ObjectType.PILOT) {
                    chatMessage = "chat " + whichArmy + " Lost all their PILOTS TO ALL";
                } else if (missionCountObjective.getObjectType() == IL2StaticObject.ObjectType.PLANE) {
                    chatMessage = "chat " + whichArmy + " Lost all their PLANES TO ALL";
                }
                ServerCommandController.serverCommandSend(chatMessage);
                MainController.writeDebugLogFile(1, chatMessage);
            }
            objectiveComplete = true;
        }
        return objectiveComplete;
    }

    public static void adjustMissionLostCount(Mission mission, int army, IL2StaticObject.ObjectType objectType, int count) {
        MissionCountObjective missionCountObjective = MissionCountObjectivesController.getMissionCountObjective(mission, army, objectType);
        if (missionCountObjective != null) {
            missionCountObjective.setMissionLostCount(missionCountObjective.getMissionLostCount() + count);
        }
        if (objectType == IL2StaticObject.ObjectType.PILOT || objectType == IL2StaticObject.ObjectType.PLANE) {
            int redPlanesLeft = 0;
            int bluePlanesLeft = 0;
            int redPilotsLeft = 0;
            int bluePilotsLeft = 0;
            int pilotsRemaining = 0;
            int planesRemaining = 0;

            missionCountObjective = null;
            missionCountObjective = MissionCountObjectivesController.getMissionCountObjective(mission, MainController.REDARMY, IL2StaticObject.ObjectType.PLANE);
            if (missionCountObjective != null) {
                redPlanesLeft = missionCountObjective.getNumberToDestroy() - missionCountObjective.getMissionLostCount();
                if (redPlanesLeft < 0) {
                    redPlanesLeft = 0;
                }
            }
            missionCountObjective = null;
            missionCountObjective = MissionCountObjectivesController.getMissionCountObjective(mission, MainController.REDARMY, IL2StaticObject.ObjectType.PILOT);
            if (missionCountObjective != null) {
                redPilotsLeft = missionCountObjective.getNumberToDestroy() - missionCountObjective.getMissionLostCount();
                if (redPilotsLeft < 0) {
                    redPilotsLeft = 0;
                }
            }
            missionCountObjective = null;
            missionCountObjective = MissionCountObjectivesController.getMissionCountObjective(mission, MainController.BLUEARMY, IL2StaticObject.ObjectType.PLANE);
            if (missionCountObjective != null) {
                bluePlanesLeft = missionCountObjective.getNumberToDestroy() - missionCountObjective.getMissionLostCount();
                if (bluePlanesLeft < 0) {
                    bluePlanesLeft = 0;
                }
            }
            missionCountObjective = null;
            missionCountObjective = MissionCountObjectivesController.getMissionCountObjective(mission, MainController.BLUEARMY, IL2StaticObject.ObjectType.PILOT);
            if (missionCountObjective != null) {
                bluePilotsLeft = missionCountObjective.getNumberToDestroy() - missionCountObjective.getMissionLostCount();
                if (bluePilotsLeft < 0) {
                    bluePilotsLeft = 0;
                }
            }
            String armyName = "NONE";
            if (army == MainController.REDARMY) {
                armyName = "RED";
                pilotsRemaining = redPilotsLeft;
                planesRemaining = redPlanesLeft;
            } else if (army == MainController.BLUEARMY) {
                armyName = "BLUE";
                pilotsRemaining = bluePilotsLeft;
                planesRemaining = bluePlanesLeft;
            }

            String message = MainController.PLANEPILOTMESSAGES.SendUpdate(armyName, redPlanesLeft, redPilotsLeft, bluePlanesLeft, bluePilotsLeft);
            if (message.length() > 0) {
                ServerCommandController.serverCommandSend("chat " + message);
                MainController.writeDebugLogFile(2, "MissionCountObjectiveController.adjustMissionLostCount Plane/Pilot Message: " + message);
            }

            message = MainController.PLANEPILOTWARNING.CheckForPilotWarning(armyName, pilotsRemaining);
            if (message.length() > 0) {
                ServerCommandController.serverCommandSend("chat " + message);
                MainController.writeDebugLogFile(2, "MissionCountObjectiveController.adjustMissionLostCount Pilot Message: " + message);
            }

            message = MainController.PLANEPILOTWARNING.CheckForPlaneWarning(armyName, planesRemaining);
            if (message.length() > 0) {
                ServerCommandController.serverCommandSend("chat " + message);
                MainController.writeDebugLogFile(2, "MissionCountObjectiveController.adjustMissionLostCount Plane Message: " + message);
            }

        }

    }

    public static String displayRemainingPilotPlaneObjectives(Mission mission, int army) {
        String displayText = null;
        String armyText;
        int numLeft = 0;
        MissionCountObjective missionCountObjective = null;
        if (army == MainController.REDARMY) {
            armyText = "Red - ";
        } else {
            armyText = "Blue - ";
        }
        missionCountObjective = MissionCountObjectivesController.getMissionCountObjective(mission, army, IL2StaticObject.ObjectType.PLANE);
        if (missionCountObjective != null) {
            numLeft = missionCountObjective.getNumberToDestroy() - missionCountObjective.getMissionLostCount();
            if (numLeft > 0) {
                displayText = armyText + numLeft + " Planes";
            }
        }
        missionCountObjective = null;
        missionCountObjective = MissionCountObjectivesController.getMissionCountObjective(mission, army, IL2StaticObject.ObjectType.PILOT);
        if (missionCountObjective != null) {
            numLeft = missionCountObjective.getNumberToDestroy() - missionCountObjective.getMissionLostCount();
            if (numLeft > 0) {
                if (displayText == null) {
                    displayText = armyText + numLeft + " Pilots";
                } else {
                    displayText = displayText + "/" + numLeft + " Pilots";
                }
            }
        }
        if (displayText == null) {
            return "No limits on Planes/Pilots";
        } else {
            return displayText + " Remaining";
        }
    }

    public static String displayRemainingCountObjectives(Mission mission, int army) {
        String displayText = "";
        int numberObjectives = 0;
        if (army == MainController.REDARMY) {
            displayText = "Red-(";
        } else {
            displayText = "Blue-(";
        }

        for (MissionCountObjective countObjective : mission.getMissionParameters().getCountObjectives()) {
            if (countObjective.getObjectType() != IL2StaticObject.ObjectType.PILOT && countObjective.getObjectType() != IL2StaticObject.ObjectType.PLANE) {
                if (countObjective.getArmy() == army) {
                    if (MissionCountObjectivesController.getCountPercentToComplete(countObjective) > 0) {
                        if (displayText.endsWith("(")) {
                            displayText = displayText + countObjective.getObjectType() + "-" + MissionCountObjectivesController.getCountPercentToComplete(countObjective) + "%";
                        } else {
                            displayText = displayText + "/" + countObjective.getObjectType() + "-" + MissionCountObjectivesController.getCountPercentToComplete(countObjective) + "%";
                        }
                        numberObjectives++;
                    }
                }
            }
        }
        displayText = displayText + ") Remaining";
        if (numberObjectives > 0) {
            return displayText;
        } else {
            return null;
        }
    }

//	public static void clearMissionCountObjectives()
//	{
//		MainController.MISSIONCOUNTOBJECTIVES.clear();
//	}
    public static ArrayList<String> displayCountObjectives(Mission mission, int army) {
        ArrayList<String> countObjectives = new ArrayList<String>();
        String armyText = "";
        String displayText = "";
        try {
            if (army == MainController.REDARMY) {
                armyText = "Red-(C";
            } else {
                armyText = "Blue-(C";
            }
            displayText = armyText + "id)-Objective-(Start/Destroyed/To Win Count)";
            countObjectives.add(displayText);
            int i = 0;
            for (MissionCountObjective countObjective : mission.getMissionParameters().getCountObjectives()) {
                if (countObjective.getArmy() == army) {
                    displayText = armyText + i + ")-" + countObjective.getObjectType() + "-(" + countObjective.getMissionStartCount() + "/" + countObjective.getMissionLostCount() + "/" + countObjective.getNumberToDestroy() + ")";
                    countObjectives.add(displayText);
                }
                i++;
            }
            return countObjectives;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MissionCountObjectiveController.displayCountObjectivs - Error Unhandled exception: " + ex);
            return countObjectives;
        }
    }

    public static String updateTargetObjective(Mission mission, int army, int targetId, int newDestroyCount) {
        String armyText = "";
        String displayText = "";
        try {
            if (army == MainController.REDARMY) {
                armyText = "Red-(T";
            } else {
                armyText = "Blue-(T";
            }
            for (int i = 0; i < mission.getMissionParameters().getCountObjectives().size(); i++) {
                MissionCountObjective targetObjective = mission.getMissionParameters().getCountObjectives().get(i);
                if (i == targetId && targetObjective.getArmy() == army) {
                    if (targetObjective.getMissionLostCount() > targetObjective.getNumberToDestroy()) {
                        displayText = "Target Objective already Completed, cannot update";
                    } else if (newDestroyCount > targetObjective.getMissionStartCount() && targetObjective.getObjectType() != IL2StaticObject.ObjectType.PILOT && targetObjective.getObjectType() != IL2StaticObject.ObjectType.PLANE) {
                        displayText = "Cannot set Objective count(" + newDestroyCount + ") > Start count(" + targetObjective.getMissionStartCount() + ")";
                    } else {
                        targetObjective.setNumberToDestroy(newDestroyCount);
                        displayText = armyText + i + ")-" + targetObjective.getObjectType() + "-(" + targetObjective.getMissionStartCount() + "/" + targetObjective.getMissionLostCount() + "/" + targetObjective.getNumberToDestroy() + ")";
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
