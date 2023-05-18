package mainController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import model.WarningPoints;
import model.WarningPointsItem;
import utility.FileLoggerFormat;

public class WarningPointsController {

    public static void initialize() {
        if ((MainController.WARNINGPOINTS = (WarningPoints) FileController.fileReadSerialized(MainController.STARTUPCONFIG.getConfigDirectory() + MainController.WPFILENAME)) == null) {
            FileController.fileWrite(MainController.FBDJROOTDIRECTORY, MainController.STARTSTOPLOGFILENAME, FileLoggerFormat.getLogString("WarningPointsController.initialize: Could not find Warning Points file from startup config."), true);
            // Try default config
            if ((MainController.WARNINGPOINTS = (WarningPoints) FileController.fileReadSerialized("./config/Default/" + MainController.WPFILENAME)) == null) {
                // Create new default config
                MainController.WARNINGPOINTS = new WarningPoints();
                WarningPointsController.write();
                MainController.writeDebugLogFile(1, "ConfigurationController.configLoad: Could not find default config file. Creating new default config.");
            } else {
                MainController.writeDebugLogFile(1, "ConfigurationController.configLoad: Default config Loaded ( " + MainController.STARTUPCONFIG.getConfigDirectory() + " )");
            }
        } else {
            MainController.writeDebugLogFile(1, "ConfigurationController.configLoad: Configuration Loaded ( " + MainController.STARTUPCONFIG.getConfigDirectory() + " )");
        }

        validateWarningPoints(MainController.WARNINGPOINTS);

    }

    public static void write() {
        FileController.fileWriteSerialized(MainController.CONFIG.getConfigDirectory(), MainController.WPFILENAME, MainController.WARNINGPOINTS);
        MainController.writeDebugLogFile(2, "WarningPointsController.write: Warning Points written to file.");
    }

    public static WarningPointsItem getWPItem(HashMap<String, WarningPointsItem> wpHashMap, String itemName) {
        WarningPointsItem item = null;
        try {
            if (wpHashMap.containsKey(itemName)) {
                item = wpHashMap.get(itemName);
            } else {
                MainController.writeDebugLogFile(1, "WarningPointsController.getWPItem - Error No Warning Points Item (" + itemName + ") Found in configuration.");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "WarningPointsController.getWPItem - Error Unhandled Exception on Item (" + itemName + ")");
        }
        return item;
    }

    public static void setConfigItemValue(WarningPoints wpName, String itemName, Object itemValue) {
        try {
            if (wpName.getWarningPoints().containsKey(itemName)) {
                wpName.getWarningPoints().get(itemName).setValue(itemValue);
            } else if (wpName.getWarningLevels().containsKey(itemName)) {
                wpName.getWarningLevels().get(itemName).setValue(itemValue);
            } else {
                MainController.writeDebugLogFile(1, "ConfigurationController.setConfigItem - Error No Configuration Item (" + itemName + ") Found in configuration.");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ConfigurationController.setConfigItem - Error Unhandled Exception on Item (" + itemName + ") in Configuration (" + wpName + ")");
        }
    }

    private static HashMap<String, WarningPointsItem> reconcileWPHashMap(HashMap<String, WarningPointsItem> wpHashMap, HashMap<String, WarningPointsItem> baseWPHashMap) {
//		HashMap<String, WarningPointsItem> returnHashMap = wpHashMap;
        ArrayList<String> removedItems = new ArrayList<String>();
        try {
            if (wpHashMap.size() > baseWPHashMap.size()) { // A configuration Item was deleted, lets remove it from the configHashMap
                Iterator<String> it = wpHashMap.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    if (!baseWPHashMap.containsKey(key)) {
                        removedItems.add(key);
                        MainController.writeDebugLogFile(1, "ConfigurationController.validateConfiguration - Deleted Configuration Item (" + wpHashMap.get(key).getLabel() + ")");
                    }
                }
                if (removedItems.size() > 0) {
                    for (String itemName : removedItems) {
                        wpHashMap.remove(itemName);
                    }
                }
            } else if (wpHashMap.size() < baseWPHashMap.size()) {  // A new Configuration Item has been added to base Config Hash Map
                Iterator<String> it = baseWPHashMap.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    if (!wpHashMap.containsKey(key)) {
                        WarningPointsItem newWPItem = baseWPHashMap.get(key);
                        wpHashMap.put(key, newWPItem);
                        MainController.writeDebugLogFile(1, "ConfigurationController.validateConfiguration - New Configuration Item Added (" + newWPItem.getLabel() + ")");
                    }
                }
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ConfigurationController.reconcileConfigItems - Error Unhandled exception: " + ex);
        }

        return wpHashMap;

    }

    public static void validateWarningPoints(WarningPoints wpRead) {
        WarningPoints newWP = new WarningPoints();

        if (newWP.getWarningPoints().size() != wpRead.getWarningPoints().size()) {
            MainController.writeDebugLogFile(1, "ConfigurationController.validateConfiguration - Ping Configuration Change");
            wpRead.setWarningPoints(reconcileWPHashMap(wpRead.getWarningPoints(), newWP.getWarningPoints()));
        }

        if (newWP.getWarningLevels().size() != wpRead.getWarningLevels().size()) {
            MainController.writeDebugLogFile(1, "ConfigurationController.validateConfiguration - Icon Configuration Change");
            wpRead.setWarningLevels(reconcileWPHashMap(wpRead.getWarningLevels(), newWP.getWarningLevels()));
        }
    }

}
