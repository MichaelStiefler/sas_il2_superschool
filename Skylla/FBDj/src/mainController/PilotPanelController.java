package mainController;

import java.util.Iterator;

import view.MainWindowView2;

public class PilotPanelController {

    public static void addPilot(String name) {
        String plane = "";
        String markings = "";
        if (MainController.SORTIES.containsKey(name)) {
            plane = MainController.SORTIES.get(name).getPlane();
            markings = MainController.SORTIES.get(name).getPlaneMarkings();
        } else {
            plane = "None";
            markings = "None";
        }
        MainWindowView2.pilotsPanel.addPilot(MainController.PILOTS.get(name).getArmy(), MainController.PILOTS.get(name).getName(), MainController.PILOTS.get(name).isAdmin(), MainController.PILOTS.get(name).getIPAddress(), MainController.PILOTS.get(name)
                .getState().toString(), MainController.PILOTS.get(name).getCountry(), MainController.PILOTS.get(name).getPing(), plane, markings);
    }

    public static void updatePilot(String name) {
        try {
            MainWindowView2.pilotsPanel.updatePilot(MainController.PILOTS.get(name).getName(), MainController.PILOTS.get(name).isAdmin(), MainController.SORTIES.get(name).getArmy(), MainController.SORTIES.get(name).getPlane(),
                    MainController.SORTIES.get(name).getPlaneMarkings(), MainController.PILOTS.get(name).getState().toString());
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotPanelController.updatePilot - Error: " + ex);
        }
    }

    public static void refreshPilotPanel() {
        try {
            MainWindowView2.pilotsPanel.clearPilotPanel();
            Iterator<String> it = MainController.PILOTS.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                addPilot(key);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "PilotPanelController.refreshPilotPanel - Error: " + ex);
        }
    }

    public static void removePilot(String name) {
        MainWindowView2.pilotsPanel.removePilot(name);
    }
}
