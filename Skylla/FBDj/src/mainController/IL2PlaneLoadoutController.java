package mainController;

import java.util.ArrayList;

import model.Weapon;

public class IL2PlaneLoadoutController {

    public static void addPlaneWeapon(String planeName, String weapon, String weaponDescription) {

        Weapon newWeapon = new Weapon(weapon, weaponDescription);
        if (MainController.IL2PLANELOADOUTS.containsKey(planeName)) {
            MainController.IL2PLANELOADOUTS.get(planeName).add(newWeapon);
        } else {
            ArrayList<Weapon> weaponList = new ArrayList<Weapon>();
            weaponList.add(newWeapon);
            MainController.IL2PLANELOADOUTS.put(planeName, weaponList);
        }
    }

    public static ArrayList<Weapon> getPlaneWeapons(String plane) {

        ArrayList<Weapon> weapons = null;
        weapons = MainController.IL2PLANELOADOUTS.get(plane);
        return weapons;

    }

    public static void initialize(String il2PlaneLoadoutsFileName) {
        loadPlaneLoadouts(il2PlaneLoadoutsFileName);
    }

    private static void loadPlaneLoadouts(String il2PlaneLoadoutsFileName) {
        String planeName = null;
        String weapon = null;
        String weaponDescription = null;
        int weaponCount = 0;

        // The line below needs to change to read from the config class to get the
        // FBDj path for the file

        ArrayList<String> il2WeaponsFile = new ArrayList<String>();

        // Read IL2 Plane Weapons File
        il2WeaponsFile = FileController.fileRead(il2PlaneLoadoutsFileName);

        // Loop through all lines of the file
        for (String line : il2WeaponsFile) {
            try {
                line = line.trim();
                if (line.length() > 0) {
                    planeName = line.split("\",\"")[0];
                    weapon = line.split("\",\"")[1];
                    weaponDescription = line.split("\",\"")[2];
                    planeName = planeName.substring(1);
                    weaponDescription = weaponDescription.substring(0, (weaponDescription.length() - 1));
                    addPlaneWeapon(planeName, weapon, weaponDescription);
                    weaponCount++;
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "IL2PlaneLoadoutController.loadPlaneLoadouts Error loading weapon: " + ex);
                MainController.writeDebugLogFile(1, " *** Plane (" + planeName + " ) Weapon(" + weapon + ") Desc(" + weaponDescription + ")");
            }
        }
        MainController.writeDebugLogFile(1, "IL2PlaneLoadoutController.loadPlaneLoadouts Loaded data for (" + weaponCount + ") Weapons");
    } // loadWeapons

}
