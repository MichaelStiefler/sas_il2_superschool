package main;

import mainController.AdminController;
import mainController.IL2DataLoadController;
import mainController.MainController;
import mainController.PilotBanController;
import mainController.ReservedNameController;
import view.MainWindowApp2;

public class FBDj {
    // ************** SHUTDOWN *********************
    // Called directly from GUI
    public static void exitProgram() {
        // Will perform shutdown procedures so we can exit the program gracefully
    }

    // ************** MAIN *********************
    public static void main(String[] args) {

        // Required program start-up attributes
        MainController.startupConfigInitialize(args);

        // Config
        MainController.configInitialize();
        MainController.wpInitialize();

        MainController.writeDebugLogFile(1, "*******************************************************************");
        MainController.writeDebugLogFile(1, "*******************************************************************");

        MainController.writeDebugLogFile(1, "FBDj (2.0 FAC Edition) Started with Configuration ( " + MainController.CONFIG.getConfigName() + " )");

        // Load Data Files
        IL2DataLoadController.loadAllDataFiles();

        // Admin
        MainController.setAdmins(AdminController.adminsLoad());

        // ReservedName
        MainController.setReservedNames(ReservedNameController.reservedNamesLoad());

        // Pilot bans
        MainController.setBannedPilots(PilotBanController.pilotBansLoad());

        MainWindowApp2.main(null);
    }
}
