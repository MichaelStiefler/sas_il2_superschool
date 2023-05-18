package mainController;

import utility.FileLoggerFormat;

public class LogController {
    public static void writeChatLogFile(String data) {
        if (MainController.CONFIG.isLogChat()) {
            FileController.fileWrite(MainController.CONFIG.getConfigDirectory(), MainController.CHATLOGFILENAME, FileLoggerFormat.getLogString(data), true);
        }
    }

    public static void writeAdminLogFile(String data) {
        if (MainController.CONFIG.isLogAdmin()) {
            FileController.fileWrite(MainController.CONFIG.getConfigDirectory(), MainController.ADMINLOGFILENAME, FileLoggerFormat.getLogString(data), true);
        }
    }

    public static void writeDebugLogFile(int debugLevel, String data) {
        try {
            if (MainController.CONFIG.getLogDebugLevel() >= debugLevel) {
                FileController.fileWrite(MainController.CONFIG.getConfigDirectory(), MainController.DEBUGLOGFILENAME, FileLoggerFormat.getLogString(data), true);
            }
        } catch (NullPointerException e) {
            System.out.println("LogController.writeDebugLogFile Exception! Could not get debug level from Config. This happens when the config file is not found and the MainController instance of CONFIG has not been instantiated.");
        }
    }

    public static void writeIPLogFile(String data) {
        try {
            if (MainController.CONFIG.isLogIpAccess()) {
                FileController.fileWrite(MainController.CONFIG.getConfigDirectory(), MainController.IPLOGFILENAME, FileLoggerFormat.getLogString(data), true);
            }
        } catch (Exception ex) {
            System.out.println("LogController.writeDegugLogFile Exception! Could not write to the IPAccess Log: " + ex);
        }
    }
}
