package mainController;

import java.util.HashMap;

import model.ReservedName;

public class ReservedNameController {
    public static void reservedNameAdd(String name, String password) {
        // Make sure the key doesn't already exist
        if (!MainController.RESERVEDNAMES.containsKey(name)) {
            ReservedName newReservedName = new ReservedName(name, password);
            MainController.RESERVEDNAMES.put(name, newReservedName);
            MainController.writeDebugLogFile(1, "ReservedName.addReservedName: " + name + ": Added to ReservedName list.");
        } else {
            MainController.writeDebugLogFile(1, "ReservedName.addReservedName: Cannot add ReservedName: " + name + ": ReservedName name already exists!");
        }

        // This is in an Exception handler because reserved names can be added before there is a pilots list.
        try {
            // Make pilot a ReservedName if on the server
            if (MainController.PILOTS.containsKey(name)) {
                MainController.PILOTS.get(name).setReservedName(true);
                MainController.writeDebugLogFile(1, "ReservedName.addReservedName: " + name + ": is now a ReservedName in-game.");
            }
        } catch (Exception ex) {}

        // Write to file
        ReservedNameController.reservedNamesWrite();
    }

    public static void reservedNameRemove(String name) {
        // Remove
        MainController.RESERVEDNAMES.remove(name);

        // Write to file
        ReservedNameController.reservedNamesWrite();

        MainController.writeDebugLogFile(1, "ReservedName.removeReservedName: " + name + ": removed from ReservedName.");
    }

    @SuppressWarnings("unchecked")
    public static HashMap<String, ReservedName> reservedNamesLoad() {
        HashMap<String, ReservedName> reservedName;

        // Load list (or create new if it doesn't exist)
        if ((reservedName = (HashMap<String, ReservedName>) FileController.fileReadSerialized(MainController.CONFIG.getConfigDirectory() + MainController.RESERVEDNAMEFILENAME)) == null) {
            reservedName = new HashMap<String, ReservedName>();
            MainController.writeDebugLogFile(1, "ReservedName.loadReservedName: could not find ReservedName file! Creating new list.");
        } else {
            int count = reservedName.size();
            MainController.writeDebugLogFile(1, "ReservedName.loadReservedName: Loaded (" + count + ") Reserved Names from file");
        }

        return reservedName;
    }

    public static void reservedNamesWrite() {
        FileController.fileWriteSerialized(MainController.CONFIG.getConfigDirectory(), MainController.RESERVEDNAMEFILENAME, MainController.RESERVEDNAMES);
        MainController.writeDebugLogFile(2, "ReservedName.writeReservedName: ReservedName list written to file.");
    }
}
