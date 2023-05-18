package mainController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import model.Configuration;
import model.ConfigurationItem;
import utility.FileLoggerFormat;

public class ConfigurationController {
    public static void configInitialize() {
        // Try startup config
        if ((MainController.CONFIG = (Configuration) FileController.fileReadSerialized(MainController.STARTUPCONFIG.getConfigDirectory() + MainController.CONFIGFILENAME)) == null) {
            FileController.fileWrite(MainController.FBDJROOTDIRECTORY, MainController.STARTSTOPLOGFILENAME, FileLoggerFormat.getLogString("ConfigurationController.configLoad: Could not find config file from startup config."), true);
            // Try default config
            if ((MainController.CONFIG = (Configuration) FileController.fileReadSerialized("./config/Default/" + MainController.CONFIGFILENAME)) == null) {
                // Create new default config
                MainController.CONFIG = new Configuration("Default");
                ConfigurationController.configWrite();
                MainController.writeDebugLogFile(1, "ConfigurationController.configLoad: Could not find default config file. Creating new default config.");
            } else {
                MainController.writeDebugLogFile(1, "ConfigurationController.configLoad: Default config Loaded ( " + MainController.STARTUPCONFIG.getConfigDirectory() + " )");
            }
        } else {
            MainController.writeDebugLogFile(1, "ConfigurationController.configLoad: Configuration Loaded ( " + MainController.STARTUPCONFIG.getConfigDirectory() + " )");
        }

        validateConfiguration(MainController.CONFIG);

    }

    public static void configWrite() {
        FileController.fileWriteSerialized(MainController.CONFIG.getConfigDirectory(), MainController.CONFIGFILENAME, MainController.CONFIG);
        MainController.writeDebugLogFile(2, "ConfigurationController.configWrite: Config written to file.");
    }

    public static ConfigurationItem getConfigItem(HashMap<String, ConfigurationItem> configHashMap, String itemName) {
        ConfigurationItem item = null;
        try {
            if (configHashMap.containsKey(itemName)) {
                item = configHashMap.get(itemName);
            } else {
                MainController.writeDebugLogFile(1, "ConfigurationController.getConfigItem - Error No Configuration Item (" + itemName + ") Found in configuration.");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ConfigurationController.getConfigItem - Error Unhandled Exception on Item (" + itemName + ")");
        }
        return item;
    }

    public static void setConfigItemValue(Configuration configName, String itemName, Object itemValue) {
        try {
            if (configName.getDynamicVariables().containsKey(itemName)) {
                configName.getDynamicVariables().get(itemName).setValue(itemValue);
            } else if (configName.getStaticVariables().containsKey(itemName)) {
                configName.getStaticVariables().get(itemName).setValue(itemValue);
            } else if (configName.getPingSettings().containsKey(itemName)) {
                configName.getPingSettings().get(itemName).setValue(itemValue);
            } else if (configName.getIconSettings().containsKey(itemName)) {
                configName.getIconSettings().get(itemName).setValue(itemValue);
            } else {
                MainController.writeDebugLogFile(1, "ConfigurationController.setConfigItem - Error No Configuration Item (" + itemName + ") Found in configuration.");
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ConfigurationController.setConfigItem - Error Unhandled Exception on Item (" + itemName + ") in Configuration (" + configName + ")");
        }
    }

    private static HashMap<String, ConfigurationItem> reconcileConfigHashMap(HashMap<String, ConfigurationItem> configHashMap, HashMap<String, ConfigurationItem> baseConfigHashMap) {
        // HashMap<String, ConfigurationItem> returnHashMap = configHashMap;
        ArrayList<String> removedItems = new ArrayList<String>();
        try {
            if (configHashMap.size() > baseConfigHashMap.size()) { // A configuration Item was deleted, lets remove it from the configHashMap
                Iterator<String> it = configHashMap.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    if (!baseConfigHashMap.containsKey(key)) {
                        removedItems.add(key);
                        MainController.writeDebugLogFile(1, "ConfigurationController.validateConfiguration - Deleted Configuration Item (" + configHashMap.get(key).getLabel() + ")");
                    }
                }
                if (removedItems.size() > 0) {
                    for (String itemName : removedItems) {
                        configHashMap.remove(itemName);
                    }
                }
            } else if (configHashMap.size() < baseConfigHashMap.size()) {  // A new Configuration Item has been added to base Config Hash Map
                Iterator<String> it = baseConfigHashMap.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    if (!configHashMap.containsKey(key)) {
                        ConfigurationItem newConfigItem = baseConfigHashMap.get(key);
                        configHashMap.put(key, newConfigItem);
                        MainController.writeDebugLogFile(1, "ConfigurationController.validateConfiguration - New Configuration Item Added (" + newConfigItem.getLabel() + ")");
                    }
                }
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "ConfigurationController.reconcileConfigItems - Error Unhandled exception: " + ex);
        }

        return configHashMap;

    }

    public static void validateConfiguration(Configuration configurationRead) {
        Configuration newConfiguration = new Configuration("currentConfig");

        if (newConfiguration.getPingSettings().size() != configurationRead.getPingSettings().size()) {
            MainController.writeDebugLogFile(1, "ConfigurationController.validateConfiguration - Ping Configuration Change");
            configurationRead.setPingSettings(reconcileConfigHashMap(configurationRead.getPingSettings(), newConfiguration.getPingSettings()));
        }

        if (newConfiguration.getIconSettings().size() != configurationRead.getIconSettings().size()) {
            MainController.writeDebugLogFile(1, "ConfigurationController.validateConfiguration - Icon Configuration Change");
            configurationRead.setIconSettings(reconcileConfigHashMap(configurationRead.getIconSettings(), newConfiguration.getIconSettings()));
        }

        if (newConfiguration.getDynamicVariables().size() != configurationRead.getDynamicVariables().size()) {
            MainController.writeDebugLogFile(1, "ConfigurationController.validateConfiguration - Dynamic Variable Configuration Change");
            configurationRead.setDynamicVariables(reconcileConfigHashMap(configurationRead.getDynamicVariables(), newConfiguration.getDynamicVariables()));
        }

        if (newConfiguration.getStaticVariables().size() != configurationRead.getStaticVariables().size()) {
            MainController.writeDebugLogFile(1, "ConfigurationController.validateConfiguration - Static Variable Configuration Change");
            configurationRead.setStaticVariables(reconcileConfigHashMap(configurationRead.getStaticVariables(), newConfiguration.getStaticVariables()));
        }
    }
}