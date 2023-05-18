package viewController;

import view.MainWindowView2;

public class MainWindowController {

    public static void setStatusMessage(String message) {
        MainWindowView2.setStatusMessageTF(message);
    }

    public static void displayMessage(String message, String messageType) {
        if (messageType.equals("Error")) {
            MainWindowView2.missionStatusPanel.displayErrorMessage(message);
        }
    }

}
