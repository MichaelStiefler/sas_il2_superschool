package mainController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import model.Admin;
import model.Pilot;

public class AdminController {
    public static void adminAdd(String name, String password) {
        // Make sure the key doesn't already exist
        if (!MainController.ADMINS.containsKey(name)) {
            Admin newAdmin = new Admin(name, password);
            MainController.ADMINS.put(name, newAdmin);
        } else {
            MainController.writeDebugLogFile(1, "AdminController.addAdmin: Cannot add admin: " + name + ": Admin name already exists!");
        }

        // Write to file
        AdminController.adminsWrite();
    }

    public static void adminRemove(String name) {
        // Remove
        MainController.ADMINS.remove(name);

        // Write to file
        AdminController.adminsWrite();

        MainController.writeDebugLogFile(1, "AdminController.removeAdmin: " + name + ": removed from admin.");
    }

    public static void adminPasswordChange(String name, String newPassword) {
        MainController.ADMINS.get(name).setPassword(newPassword);
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, Admin> adminsLoad() {
        HashMap<String, Admin> admins;

        // Load list (or create new if it doesn't exist)
        if ((admins = (HashMap<String, Admin>) FileController.fileReadSerialized(MainController.CONFIG.getConfigDirectory() + MainController.ADMINFILENAME)) == null) {
            admins = new HashMap<String, Admin>();
            MainController.writeDebugLogFile(1, "AdminController.loadAdmins: No Admin File Found! Creating new Admin File");
        } else {
            int adminCount = admins.size();
            MainController.writeDebugLogFile(1, "AdminController.loadAdmins: Loaded (" + adminCount + ") Admins from file");
        }

        return admins;
    }

    public static void adminsWrite() {
        FileController.fileWriteSerialized(MainController.CONFIG.getConfigDirectory(), MainController.ADMINFILENAME, MainController.ADMINS);
        MainController.writeDebugLogFile(2, "AdminController.writeAdmins: admin list written to file.");
    }

    public static ArrayList<String> getConnectedAdmins() {
        ArrayList<String> admins = new ArrayList<String>();
        try {
            Iterator<String> it = MainController.PILOTS.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                Pilot pilot = MainController.PILOTS.get(key);
                if (pilot.isAdmin()) {
                    admins.add(pilot.getAsciiTextName());
                }
            }
            return admins;
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "AdminController.getConnectedAdmins - Error Unhandled exception: " + ex);
            return null;
        }
    }

}
