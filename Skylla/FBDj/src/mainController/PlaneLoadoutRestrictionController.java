package mainController;

import java.util.Timer;
import java.util.TimerTask;

import model.GUICommand;
import model.PlaneLoadoutRestriction;
import model.QueueObj;
import utility.UnicodeFormatter;

class PlaneLoadoutRestrictionController {

    public static Boolean planeLoadoutIsLoadoutOK(int army, String plane, String weapons, String pilotName) {
        Boolean weaponOk = true;
        for (PlaneLoadoutRestriction tempRestriction : MainController.ACTIVEMISSION.getMissionParameters().getPlaneLoadoutRestrictions()) {
            if (tempRestriction.getArmy() == army && tempRestriction.getPlane().equals(plane) && tempRestriction.getWeapon().equals(weapons)) {
                ServerCommandController.serverCommandSend("chat WARNING: Weapon Loadout (" + plane + "/" + weapons + ") NOT Allowed, CHANGE or be KICKED TO \"" + UnicodeFormatter.convertUnicodeToString(pilotName) + "\"");
                weaponOk = false;
                break;
            }
        }
        return weaponOk;
    }

    public static void weaponsCheck(String name, long sortieID) {
        final Timer timer = new Timer();

        class WeaponsCheck extends TimerTask {

            String name;
            long   sortieID;
            int    counter;
            long   waitTime = MainController.CONFIG.getAutoKickTimer() * 1000;

            // Constructor passes in Pilot name and counter
            public WeaponsCheck(String name, int counter, long sortieID) {
                this.name = name;
                this.counter = counter;
                this.sortieID = sortieID;
            }

            public void run() {
                try {
                    String asciiPilotName = "";
                    asciiPilotName = MainController.PILOTS.get(name).getAsciiTextName();
                    // Make sure the Sortie is still there.
                    if (MainController.SORTIES.containsKey(name)) {
                        // Make sure we are working with the same sortie
                        if (MainController.SORTIES.get(name).getSortieStartTime() == sortieID) {
                            // Check the loadout
                            if (!PlaneLoadoutRestrictionController.planeLoadoutIsLoadoutOK(MainController.SORTIES.get(name).getArmy(), MainController.SORTIES.get(name).getPlane(), MainController.SORTIES.get(name).getWeapons(), name)) {   // Reschedule this
                                                                                                                                                                                                                                            // check 2 more
                                                                                                                                                                                                                                            // times. If they
                                                                                                                                                                                                                                            // haven't fixed it
                                                                                                                                                                                                                                            // by then kick'em
                                if (counter < 3) {
                                    counter++;
                                    timer.schedule(new WeaponsCheck(name, counter, sortieID), waitTime);
                                } else {
                                    MainController.writeDebugLogFile(1, "Player ( " + name + " ) Kicked for Selecting Restricted Loadout ( " + MainController.SORTIES.get(name).getPlane() + "/" + MainController.SORTIES.get(name).getWeapons() + " )");
                                    GUICommand newGUICommand = new GUICommand(GUICommand.GUICommandType.KICKPILOT);
                                    newGUICommand.setName(name);
                                    MainController.doQueue(new QueueObj(QueueObj.Source.GUI, newGUICommand));
                                }
                            } else {
                                ServerCommandController.serverCommandSend("chat Weapons Loadout OK TO \"" + asciiPilotName + "\"");
                            }
                        }
                    }
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "PlaneLoadoutRestrictionController.weaponsCheck - Error Unhandled Exception in Weapons Check: " + ex);
                }
            }
        } // WeaponsCheck Class

        // When weaponsCheck method is called 1st time put it on the timer
        timer.schedule(new WeaponsCheck(name, 0, sortieID), 1);

    } // End weaponsCheck method

}
