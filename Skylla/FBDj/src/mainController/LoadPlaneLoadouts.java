package mainController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

class LoadPlaneLoadouts {
    static String plane             = null;
    static String weapon            = null;
    static String weaponDescription = null;

    private static void loadLoadouts() {
        Connection STATSCONN = null;
        String fileName = "weapons.properties";
        ArrayList<String> planeLoadoutData = FileController.fileRead(fileName);
        PreparedStatement stmt;
        int count = 0;

        try {
            String url = "jdbc:mysql://192.168.1.4:3306/fbdjstats";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            STATSCONN = DriverManager.getConnection(url, "fbdadmin", "fbdadmin");
        } catch (Exception ex) {
            System.out.println("error: " + ex);
        }

        try {
            for (String line : planeLoadoutData) {
                line = line.trim();
                if (line.length() < 1) {
                    // Do Nothing whitespace
                } else if (line.startsWith("#")) {
                    // Do Nothing Comment line
                } else {
                    int planeNdx = line.indexOf(".");
                    plane = line.substring(0, planeNdx);

                    weapon = line.substring(planeNdx + 1);
                    weaponDescription = line.substring(40);
                    weapon = weapon.split("  ")[0];
                    try {
                        // Insert new Mission
                        stmt = STATSCONN.prepareStatement("INSERT INTO planeLoadouts (planeName, weapons, weaponsDescription) VALUES ( ?,?,? )");
                        stmt.setString(1, plane);
                        stmt.setString(2, weapon);
                        stmt.setString(3, weaponDescription);
                        count += stmt.executeUpdate();

                    } catch (Exception ex) {
                        System.out.println("Error plane(" + plane + ") weapon(" + weapon + ") desc(" + weaponDescription + ") :" + ex);
                        System.out.println("Line: " + line);
                    }
                }
            }
            System.out.println(" Number of records written: " + count);
        } catch (Exception ex) {
            System.out.println("Error plane(" + plane + ") weapon(" + weapon + ") desc(" + weaponDescription + ") :" + ex);
            System.out.println("Error count(" + count + "): " + ex);
        }
    }

    public static void main(String[] args) {
        loadLoadouts();
    }
}