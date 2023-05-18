package mainController;

import model.StartupConfiguration;

class StartupConfigurationController {
    public static void startupConfigInitialize(String[] startupParameters) {
        String startupConfig = null;
        String firstParameter = null;
        if (startupParameters.length > 0) {
            firstParameter = startupParameters[0];
            if (firstParameter.startsWith("config=")) {
                startupConfig = firstParameter.split("=")[1];
            }
        }
        // If the config is not given as a parameter then set to default
        if (startupConfig == null) {
            startupConfig = "Default";
        }
        // Validate that the proper folders are in place for FBDj
        validateFolders();
        MainController.STARTUPCONFIG = new StartupConfiguration("./config/" + startupConfig + "/");
    }

    private static void validateFolders() {
        FileController.checkDirectory("./config");
    }
}
