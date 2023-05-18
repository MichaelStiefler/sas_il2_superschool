package model;

import java.io.Serializable;
import java.util.HashMap;

public class WarningPoints implements Serializable {

    private static final long                  serialVersionUID = 1L;

    private HashMap<String, WarningPointsItem> warningPoints;
    private HashMap<String, WarningPointsItem> warningLevels;

    public WarningPoints() {
        WarningPointsItem wpItem;

        warningPoints = new HashMap<String, WarningPointsItem>();

        wpItem = new WarningPointsItem("damageFriendly", "Damage Friendly", WarningPointsItem.ItemType.INTEGER, 10, 0, 10000);
        warningPoints.put("damageFriendly", wpItem);
        wpItem = new WarningPointsItem("shotdownFriendly", "Shotdown Friendly Plane", WarningPointsItem.ItemType.INTEGER, 50, 0, 10000);
        warningPoints.put("shotdownFriendly", wpItem);
        wpItem = new WarningPointsItem("damageFriendlyGround", "Damage Friendly Ground", WarningPointsItem.ItemType.INTEGER, 10, 0, 10000);
        warningPoints.put("damageFriendlyGround", wpItem);
        wpItem = new WarningPointsItem("destroyedFriendlyGround", "Destroy Friendly Ground", WarningPointsItem.ItemType.INTEGER, 30, 0, 10000);
        warningPoints.put("destroyedFriendlyGround", wpItem);
        wpItem = new WarningPointsItem("badwordUsage", "Bad Word Usage", WarningPointsItem.ItemType.INTEGER, 30, 0, 10000);
        warningPoints.put("badwordUsage", wpItem);
        wpItem = new WarningPointsItem("attackingWithinSpawnBaseRadius", "Attack within Spawn Base Radius", WarningPointsItem.ItemType.INTEGER, 40, 0, 10000);
        warningPoints.put("attackingWithinSpawnBaseRadius", wpItem);

        warningLevels = new HashMap<String, WarningPointsItem>();
        wpItem = new WarningPointsItem("kickLevel", "Kick Level", WarningPointsItem.ItemType.INTEGER, 60, 0, 10000);
        warningLevels.put("kickLevel", wpItem);
        wpItem = new WarningPointsItem("banLevel", "Ban Level", WarningPointsItem.ItemType.INTEGER, 100, 0, 10000);
        warningLevels.put("banLevel", wpItem);
        wpItem = new WarningPointsItem("banDuration", "Ban Duration", WarningPointsItem.ItemType.DOUBLE, 1.0, 0.0, 1000.0);
        warningLevels.put("banDuration", wpItem);
        wpItem = new WarningPointsItem("pointLoss", "Daily Point Loss", WarningPointsItem.ItemType.INTEGER, 20, 0, 10000);
        warningLevels.put("pointLoss", wpItem);

    }

    public HashMap<String, WarningPointsItem> getWarningPoints() {
        return warningPoints;
    }

    public void setWarningPoints(HashMap<String, WarningPointsItem> warningPoints) {
        this.warningPoints = warningPoints;
    }

    public HashMap<String, WarningPointsItem> getWarningLevels() {
        return warningLevels;
    }

    public void setWarningLevels(HashMap<String, WarningPointsItem> warningLevels) {
        this.warningLevels = warningLevels;
    }

}
