package mainController;

import model.AIWing;
import model.IL2StaticObject;
import model.Mission;
import model.MissionCountObjective;

public class AIWingController {

    public static void aiPlaneLost(Mission mission, AIWing wing) {
        int objectiveArmy = 0;
        if (wing.getArmy() == MainController.BLUEARMY) {
            objectiveArmy = MainController.REDARMY;
        } else if (wing.getArmy() == MainController.REDARMY) {
            objectiveArmy = MainController.BLUEARMY;
        }

        MissionCountObjectivesController.adjustMissionLostCount(mission, objectiveArmy, IL2StaticObject.ObjectType.AIPLANE, 1);
    }

    public static void addAIPlaneObjective(Mission mission, AIWing wing) {
        int objectiveArmy = 0;

        if (wing.getArmy() == MainController.BLUEARMY) {
            objectiveArmy = MainController.REDARMY;
        } else if (wing.getArmy() == MainController.REDARMY) {
            objectiveArmy = MainController.BLUEARMY;
        }
        MissionCountObjective countObjective = mission.getMissionParameters().getCountObjective(objectiveArmy, IL2StaticObject.ObjectType.AIPLANE);
        if (countObjective != null) {
            countObjective.setMissionStartCount(countObjective.getMissionStartCount() + wing.getPlaneCount());
        } else {
            mission.getMissionParameters().addCountObjective(objectiveArmy, IL2StaticObject.ObjectType.AIPLANE, wing.getPlaneCount());
            mission.getMissionParameters().setCountObjectivesNeeded(objectiveArmy, mission.getMissionParameters().getCountObjectivesNeeded(wing.getArmy()) + 1);
        }
    }
}
