package viewController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;

import mainController.ConfigurationController;
import mainController.IL2DataLoadController;
import mainController.MainController;
import model.Configuration;
import model.ConfigurationItem;
import view.ConfigurationPanel;

public class ViewConfigurationController {

    public static Configuration getConfiguration() {
        return MainController.getConfiguration();
    }

    public static String getConfigDirectory() {
        return MainController.STARTUPCONFIG.getConfigDirectory();
    }

    public static void updateConfigurationItem(Configuration config, HashMap<String, ConfigurationItem> configHashMap, String name, Object value) {
        ConfigurationItem item = null;

        item = ConfigurationController.getConfigItem(configHashMap, name);
        if (item != null) {
            item.setValue(value);
        }

        if (name.equals("missionEndTimer")) {
            try {
                String strMissionEndTimerInterval = (String) value;
                String[] intervals = strMissionEndTimerInterval.split(",");
                ArrayList<Integer> missionEndNotificationInterval = new ArrayList<Integer>();
                for (int i = 0; i < intervals.length; i++) {
                    missionEndNotificationInterval.add(Integer.valueOf(intervals[i].trim()));
                }
                Collections.sort(missionEndNotificationInterval, Collections.reverseOrder());
                config.setMissionEndTimerInterval(missionEndNotificationInterval);
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "ViewConfigurationController.applyDynamicChanges - Error Unhandled excpetion decoding Mission End Timer Interval (" + item.getValue() + " ex:" + ex);
            }
        }
    }  // End updateConfigurationItem

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void updateMissionCycleList(DefaultListModel missionCycleList) {
        JComboBox missionCycleComboBox = ConfigurationPanel.getMissionCycleComboBox();
        if (missionCycleComboBox != null) {
            missionCycleComboBox.removeAllItems();
            for (Object newItem : missionCycleList.toArray()) {
                missionCycleComboBox.addItem(newItem);
            }
        }
    }

    public static void updateConfigName(Configuration config, String newName) {
        config.setConfigName(newName);
    }

    public static void setCurrentConfiguration(String configName) {
        MainController.STARTUPCONFIG.setConfigDirectory("./config/" + configName + "/");
    }

    public static void reloadDataFiles() {
        IL2DataLoadController.loadAllDataFiles();
    }

    public static void applyDynamicChanges(Configuration config) {
        Iterator<String> itemKey = config.getDynamicVariables().keySet().iterator();
        ConfigurationItem item = null;

        while (itemKey.hasNext()) {
            String key = itemKey.next();
            item = ConfigurationController.getConfigItem(MainController.CONFIG.getDynamicVariables(), key);
            if (item != null && !item.getValue().equals(config.getDynamicVariables().get(key).getValue())) {
                System.out.println("key(" + key + ") value(" + item.getValue() + ")");
                item.setValue(config.getDynamicVariables().get(key).getValue());
                System.out.println("key(" + key + ") New value(" + item.getValue() + ")");
                // If the Dynamic Variable changed is the Mission Cycle then reset the mission Cycle
                // for after the current Mission
                if (key.equals("missionCycle")) {
                    MissionCycleController.resetMissionCycle((String) item.getValue(), false, 0);
                } else if (key.equals("missionEndTimer")) {
                    try {
                        String strMissionEndTimerInterval = (String) item.getValue();
                        String[] intervals = strMissionEndTimerInterval.split(",");
                        ArrayList<Integer> missionEndNotificationInterval = new ArrayList<Integer>();
                        for (int i = 0; i < intervals.length; i++) {
                            missionEndNotificationInterval.add(Integer.valueOf(intervals[i].trim()));
                        }
                        Collections.sort(missionEndNotificationInterval, Collections.reverseOrder());
                        MainController.CONFIG.setMissionEndTimerInterval(missionEndNotificationInterval);
                    } catch (Exception ex) {
                        MainController.writeDebugLogFile(1, "ViewConfigurationController.applyDynamicChanges - Error Unhandled excpetion decoding Mission End Timer Interval (" + item.getValue() + " ex:" + ex);
                    }
                }
            }
            item = null;
        }
    }

}
