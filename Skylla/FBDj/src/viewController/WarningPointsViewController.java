package viewController;

import java.util.HashMap;

import mainController.MainController;
import mainController.WarningPointsController;
import model.WarningPoints;
import model.WarningPointsItem;
import utility.FileWrite;

public class WarningPointsViewController {

    public static WarningPoints getWarningPoints() {
        return MainController.getWarningPoints();
    }

    public static void updateItem(WarningPoints wp, HashMap<String, WarningPointsItem> wpHashMap, String name, Object value) {
        WarningPointsItem item = null;

        item = WarningPointsController.getWPItem(wpHashMap, name);
        if (item != null) {
            item.setValue(value);
        }

    }

    public static Boolean writeWarningPoints(WarningPoints wp) {
        try {
            String configDirectory = MainController.STARTUPCONFIG.getConfigDirectory();

            configDirectory = MainController.FBDJROOTDIRECTORY + "/config/" + MainController.CONFIG.getConfigName() + "/";
            FileWrite.writeFileSerialized(configDirectory, MainController.WPFILENAME, wp);
            MainController.WARNINGPOINTS = wp;
            return true;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "WarningPointsViewController.writeWarningPoints - Error Unhandled exception: " + ex);
            return false;
        }
    }

}
