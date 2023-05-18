package mainController;

import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import model.Aerodrome;
import model.IL2StaticObject;
import model.Mission;
import model.Mission.MissionObjectiveType;
import model.MissionCountObjective;
import model.MissionObjectEvent;
import model.MissionTargetObjective;
import model.Pilot;
import model.PilotSortie;
import model.PlaneLoadoutRestriction;
import model.ScheduledEvent;
import utility.Time;
import view.MainWindowView2;

public class MySQLConnectionController {

    public static void connect() {
        try {
            if (MainController.STATSCONN == null) {
                String userName = MainController.CONFIG.getStatsUserName();
                String password = MainController.CONFIG.getStatsUserPassword();
                String statsIP = MainController.CONFIG.getStatsIP();
                int statsPort = MainController.CONFIG.getStatsPort();
                String statsDB = MainController.CONFIG.getStatsDBName();
                String url = "jdbc:mysql://" + statsIP + ":" + statsPort + "/" + statsDB + "?useUnicode=true&characterEncoding=UTF-8";
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                MainController.STATSCONN = DriverManager.getConnection(url, userName, password);
                MainController.writeDebugLogFile(1, "Stats Database Connection Established");
                MainController.setStatsOn(true);
                MainWindowView2.setStatusMessageTF("Stats Turned ON");
                initializeStats();
            } else {
                MainController.STATSCONN.isValid(10);
                MainWindowView2.setStatusMessageTF("Stats are already ON");
            }
        } catch (Exception e) {
            MainController.writeDebugLogFile(1, "Stats Database connection Failed error: " + e);
            MainWindowView2.setStatusMessageTF("Failed Stats Connection");
        }
    }

    public static void disconnect() {
        Statement stmt = null;
        String sql;
        @SuppressWarnings("unused")
        boolean result;

        if (MainController.STATSCONN != null) {
            try {
                sql = "DELETE FROM activePilots;";
                stmt = MainController.STATSCONN.createStatement();
                result = stmt.execute(sql);
                stmt.close();

                // Delete Active Mission Data
                sql = "DELETE FROM activeMission;";
                stmt = MainController.STATSCONN.createStatement();
                result = stmt.execute(sql);
                stmt.close();

                // Delete Active Mission Plane Limits
                sql = "DELETE FROM activeMissionPlaneLimits;";
                stmt = MainController.STATSCONN.createStatement();
                result = stmt.execute(sql);
                stmt.close();
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.disconnect - Unhandled exception deleting active mission/pilot Data");
            }
            try {
                MainController.STATSCONN.close();
                MainController.writeDebugLogFile(1, "Stats Database Connection Terminated");
                MainWindowView2.setStatusMessageTF("Stats Turned OFF");
                MainController.setStatsOn(false);
                MainController.STATSCONN = null;
            } catch (Exception e) { /* ignore close errors */
            }
        }
    }

    public static void reConnect() {
        if (MainController.isStatsOn()) {
            try {
                MainController.STATSCONN = null;
                connect();
                MainController.writeDebugLogFile(1, "MySQLConnectionController.reConnect - Database Connection Re-Established");
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.reConnect - Unhandled exception restoring Connection to database:" + ex);
            }
        }
    }

    public static void userConnect(Pilot pilot) {
        PreparedStatement stmt;
        @SuppressWarnings("unused")
        int count;

        if (MainController.isStatsOn()) {
            try {
                long connectTime = pilot.getConnectTime() / 1000;
                stmt = MainController.STATSCONN.prepareStatement("INSERT INTO connectionLog (pilotName, connectIPAddress, connectTime, connectStatus, goodConnection)" + "VALUES ( ?,?,?,?,? )");
                stmt.setString(1, pilot.getName());
                stmt.setString(2, pilot.getIPAddress());
                stmt.setLong(3, connectTime);
                stmt.setString(4, pilot.getConnectionComment());
                stmt.setBoolean(5, pilot.isValidConnection());
                count = stmt.executeUpdate();

            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.userConnect - Error Unhandled exception:" + ex);
            }
        }
    }

    public static void userDisconnect(String pilotName, String comment) {
        PreparedStatement stmt;
        @SuppressWarnings("unused")
        int count;

        if (MainController.isStatsOn()) {
            try {
                long disconnectTime = Time.getTime() / 1000;
                stmt = MainController.STATSCONN.prepareStatement("UPDATE connectionLog " + "SET disconnectTime = ?" + "    ,disconnectStatus = ?" + " WHERE pilotName = ?" + " AND disconnectTime IS NULL");
                stmt.setLong(1, disconnectTime);
                stmt.setString(2, comment);
                stmt.setString(3, pilotName);
                count = stmt.executeUpdate();

            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.userConnect - Error Unhandled exception:" + ex);
            }
        }

    }

    public static void newMission() {
        PreparedStatement stmt;
        String missionName;
        String mapName;
        String missionCycleName;
        boolean isBigMap;
        long startTime;
        MissionObjectiveType objectiveType;
        @SuppressWarnings("unused")
        int count;

        // Check to see if Stats are turned on
        if (MainController.isStatsOn()) {
            try {
                missionName = MainController.ACTIVEMISSION.getMissionName();
                mapName = MainController.IL2MAPS.get(MainController.ACTIVEMISSION.getMapName()).getDisplayName();
                isBigMap = MainController.ACTIVEMISSION.isBigMap();
                startTime = MainController.ACTIVEMISSION.getStartTime() / 1000;
                objectiveType = MainController.ACTIVEMISSION.getMissionParameters().getObjectiveType();
                missionCycleName = MainController.MISSIONCONTROL.getCurrentMissionCycle().getMissionCycleName();

                // Insert new Mission
                stmt = MainController.STATSCONN.prepareStatement("INSERT INTO missions (missionStartTime, missionName, mapName, mapSize, objectiveType, missionCycle) VALUES ( ?,?,?,?,?,? )");
                stmt.setLong(1, startTime);
                stmt.setString(2, missionName);
                stmt.setString(3, mapName);
                stmt.setBoolean(4, isBigMap);
                stmt.setString(5, objectiveType.toString());
                stmt.setString(6, missionCycleName);
                count = stmt.executeUpdate();

            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.newMission - Error writing mission record to database: " + ex);
            }

        }
    }

    // TODO: Added by Storebror to avoid flooding the Stats with missions where no sorties have been flown.
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public static boolean hasSorties(Mission mission) {
        PreparedStatement stmt;
        ResultSet rs;
        long startTime = 0;

        if (!MainController.isStatsOn())
            return true;
        try {
            // See if Sorties exist for the given Mission.
            startTime = mission.getStartTime() / 1000;
            stmt = MainController.STATSCONN.prepareStatement("SELECT COUNT(*) FROM sorties WHERE missionStartTime = ?;");
            stmt.setLong(1, startTime);
            rs = stmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    // Sorties exist
                    return true;
                }
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MySQLConnectionController.hasSorties - Error checking if Sorties exist for the given Mission: " + ex);
        }
        return false;
    }

    public static void removeUnflownMission(Mission mission) {
        Statement stmt = null;
        long startTime = 0;
        String sql = "";

        // Check to see if Stats are turned on
        if (!MainController.isStatsOn())
            return;
        try {
            startTime = mission.getStartTime() / 1000;
            stmt = MainController.STATSCONN.createStatement();
            sql = "DELETE FROM missioncountobjectives WHERE missionStartTime=" + startTime;
            stmt.executeUpdate(sql);
            sql = "DELETE FROM missionobjectevents WHERE missionStartTime=" + startTime;
            stmt.executeUpdate(sql);
            sql = "DELETE FROM missionplanelimits WHERE missionStartTime=" + startTime;
            stmt.executeUpdate(sql);
            sql = "DELETE FROM missiontargetobjectives WHERE missionStartTime=" + startTime;
            stmt.executeUpdate(sql);
            sql = "DELETE FROM missions WHERE missionStartTime=" + startTime;
            stmt.executeUpdate(sql);
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MySQLConnectionController.removeUnflownMission - Error removing Mission Data from database: " + ex);
            MainController.writeDebugLogFile(1, " *** SQL: " + sql);
        }
    }
    // ----------------------------------------------------------------------------------------------------
    
    public static void endMission(Mission mission) {
        Statement stmt = null;
        long startTime = 0;
        int winner;
        long endTime;
        long timeLimit;
        String sql = "";
//		int count = 0;
        PreparedStatement s;

        // Check to see if Stats are turned on
        if (MainController.isStatsOn()) {
            try {
                endTime = mission.getEndTime() / 1000;
                timeLimit = mission.getMissionParameters().getTimeLimit();
                if ((endTime - startTime) < timeLimit) {
                    timeLimit = (endTime - startTime);
                }
                winner = mission.getWinner();
                startTime = mission.getStartTime() / 1000;

                sql = "UPDATE missions SET missionEndTime=" + endTime + ", winner=" + winner + ", timeLimit=" + timeLimit + " WHERE missionStartTime=" + startTime;
                stmt = MainController.STATSCONN.createStatement();
//				count = stmt.executeUpdate(sql);
                stmt.executeUpdate(sql);
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.endMission - Error updating mission(end) record to database: " + ex);
            }

            try {
                for (MissionCountObjective missionCountObjective : mission.getMissionParameters().getCountObjectives()) {
                    int army = missionCountObjective.getArmy();
                    IL2StaticObject.ObjectType objectType = missionCountObjective.getObjectType();
                    int startCount = missionCountObjective.getMissionStartCount();
                    int lostCount = missionCountObjective.getMissionLostCount();
                    int numberToDestroy = missionCountObjective.getNumberToDestroy();

                    sql = "INSERT INTO missionCountObjectives (missionStartTime, army, objectType, startCount, lostCount, numberToDestroy) " + "VALUES (" + startTime + "," + army + ",\"" + objectType + "\"," + startCount + "," + lostCount + ","
                            + numberToDestroy + ")";
//					count = stmt.executeUpdate(sql);
                    stmt.executeUpdate(sql);
                } // End for MissionCountObjectives
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.endMission - Error Inserting Mission Count Objective record to database: " + ex);
                MainController.writeDebugLogFile(1, " *** SQL: " + sql);
            }

            if (mission.getMissionParameters().getObjectiveType() != MissionObjectiveType.COUNT) {
                try {
                    for (MissionTargetObjective missionTargetObjective : mission.getMissionParameters().getTargetObjectives()) {
                        int army = missionTargetObjective.getArmy();
                        int startCount = missionTargetObjective.getTotalTargets();
                        int lostCount = missionTargetObjective.getTargetsLost();
                        int numberToDestroy = missionTargetObjective.getNumberToDestroy();
                        double locationX = missionTargetObjective.getLocationX();
                        double locationY = missionTargetObjective.getLocationY();
                        double radius = missionTargetObjective.getTargetRadius();
                        String targetDesc = missionTargetObjective.getTargetDesc();
                        String mapGrid = CoordinatesController.getCoordinates(mission.isBigMap(), locationX, locationY);
                        s = MainController.STATSCONN
                                .prepareStatement("INSERT INTO missionTargetObjectives (missionStartTime, army, locationX, locationY, radius, mapGrid, targetDesc, startCount, lostCount, numberToDestroy) " + "VALUES ( ?,?,?,?,?,?,?,?,?,?)");
                        s.setLong(1, startTime);
                        s.setInt(2, army);
                        s.setDouble(3, locationX);
                        s.setDouble(4, locationY);
                        s.setDouble(5, radius);
                        s.setString(6, mapGrid);
                        s.setString(7, targetDesc);
                        s.setInt(8, startCount);
                        s.setInt(9, lostCount);
                        s.setInt(10, numberToDestroy);
//						count = s.executeUpdate();
                        s.executeUpdate();

                    } // End for MissionCountObjectives
                } catch (Exception ex) {
                    MainController.writeDebugLogFile(1, "MySQLConnectionController.endMission - Error Inserting Mission Target Objective record to database: " + ex);
                }
            }

            try {

                for (Aerodrome planeLimit : mission.getMissionParameters().getAerodromes()) {
                    Iterator<String> it = planeLimit.getPlanes().keySet().iterator();
                    while (it.hasNext()) {
                        String key = it.next();

                        s = MainController.STATSCONN
                                .prepareStatement("INSERT INTO missionPlaneLimits (missionStartTime, army, aerodromeMapGrid, aerodromeLocationX," + "aerodromeLocationY, plane, inUseLimit, totalLimit, lostCount) VALUES (?,?,?,?,?,?,?,?,?)");
                        s.setLong(1, startTime);
                        s.setInt(2, planeLimit.getArmy());
                        s.setString(3, planeLimit.getAerodromeMapGrid());
                        s.setDouble(4, planeLimit.getAerodromeLocationX());
                        s.setDouble(5, planeLimit.getAerodromeLocationY());
                        s.setString(6, key);
                        s.setInt(7, planeLimit.getPlanes().get(key).getPlanesInUseLimit());
                        s.setInt(8, planeLimit.getPlanes().get(key).getPlaneTotalLimit());
                        s.setInt(9, planeLimit.getPlanes().get(key).getPlanesLost());
//						count = s.executeUpdate();
                        s.executeUpdate();

                    }
                } // End for MissionPlaneLimits
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.endMission - Error Inserting Mission Plane Limit record to database: " + ex);
            }

            try {
                for (PlaneLoadoutRestriction loadoutRestriction : mission.getMissionParameters().getPlaneLoadoutRestrictions()) {
                    int army = loadoutRestriction.getArmy();
                    String plane = loadoutRestriction.getPlane();
                    String weapon = loadoutRestriction.getWeapon();

                    sql = "INSERT INTO missionLoadoutRestrictions (missionStartTime, army, plane, restrictedLoadout) " + "VALUES (" + startTime + "," + army + ",\"" + plane + "\",\"" + weapon + "\")";
//					count = stmt.executeUpdate(sql);
                    stmt.executeUpdate(sql);
                } // End for MissionPlaneLoadoutRestrictions
                stmt.close();
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.endMission - Error Inserting Mission Loadout Restriction record to database: " + ex);
            }
        }
    }

    public static int getPilotId(String name) {
        int pilotId = 0;
        //PreparedStatement s;
        int rowCount = 0;
        String country = null;
        String ipAddress = null;
        // Check to see if stats are turned on
        if (MainController.isStatsOn()) {
            try {
                // Get pilotId from database if the pilot exists.
                PreparedStatement s1 = MainController.STATSCONN.prepareStatement("SELECT pilotId, country, ipAddress FROM pilots WHERE pilotName = ?");
                s1.setString(1, name);
                ResultSet rs1 = s1.executeQuery();
                while (rs1.next()) {
                    pilotId = rs1.getInt("pilotId");
                    country = rs1.getString("country");
                    ipAddress = rs1.getString("ipAddress");
                    rowCount++;
                } // end while loop
                rs1.close();
                s1.close();

                // If pilot did not exist, insert new pilot into database and get the new pilotId
                if (rowCount < 1) {
                    ipAddress = MainController.PILOTS.get(name).getIPAddress();
                    country = MainController.PILOTS.get(name).getCountry();
                    if (country == null) {
                        country = "Unknown";
                    }
                    PreparedStatement s2 = MainController.STATSCONN.prepareStatement("INSERT INTO pilots (pilotName, ipaddress, country) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
                    s2.setString(1, name);
                    s2.setString(2, ipAddress);
                    s2.setString(3, country);
                    rowCount = s2.executeUpdate();
                    ResultSet rs2 = s2.getGeneratedKeys();

                    if (rs2.next()) {
                        pilotId = rs2.getInt(1);
                    } else {
                        MainController.writeDebugLogFile(1, "MySQLConnectionController.newPilot - Error retrieving pilotId after Insert");
                    }
                    rs2.close();
                    s2.close();
                } else { // Pilot found in database Update country and IPaddress if necessary
                    if ((country == null || !country.equals(MainController.PILOTS.get(name).getCountry())) || (ipAddress == null || !ipAddress.equals(MainController.PILOTS.get(name).getIPAddress()))) {
                        PreparedStatement s3 = MainController.STATSCONN.prepareStatement("UPDATE pilots SET ipaddress = ?, country = ? WHERE pilotId = ?");
                        s3.setString(1, MainController.PILOTS.get(name).getIPAddress());
                        s3.setString(2, MainController.PILOTS.get(name).getCountry());
                        s3.setInt(3, pilotId);
                        rowCount = s3.executeUpdate();
                        s3.close();
                    }
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.newPilot - Error getting Pilot ID from database for pilot( " + name + " ): " + ex);
            }
            if (pilotId == 0) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.newPilot - Error No pilotId retrieved from database for pilot( " + name + " )");
            }
        } // End if stats
        return pilotId;
    }

    public static HashMap<String, Integer> getIL2StaticData(String name) {
        PreparedStatement s;
        ResultSet rs;
        HashMap<String, Integer> il2Data = new HashMap<String, Integer>();
        int il2ObjectId = 0;
        il2Data.put("objectId", 0);
        il2Data.put("pointValue", 0);
//		il2Data.put("baseValue",0);
        if (MainController.isStatsOn()) {
            try {
                s = MainController.STATSCONN.prepareStatement(
                        // "SELECT il2ObjectID, pointValue, baseValue FROM il2Objects WHERE il2ObjectName = ? LIMIT 1");
                        "SELECT il2ObjectID, pointValue FROM il2Objects WHERE il2ObjectName = ? LIMIT 1");
                s.setString(1, name);
                rs = s.executeQuery();

                if (rs.next()) {
                    il2ObjectId = rs.getInt(1);
                    il2Data.put("objectId", il2ObjectId);
                    il2Data.put("pointValue", rs.getInt(2));
//					il2Data.put("baseValue", rs.getInt(3));
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectController.getIL2StaticObjectID - Error Selecting IL2 Static Object ( " + name + " ): " + ex);
            }
            if (il2ObjectId == 0) {
                MainController.writeDebugLogFile(1, "MySQLConnectController.getIL2StaticObjectID - No IL2 Static Object ( " + name + " ) found in Database ");

            }
        } // Stats are ON

        return il2Data;
    }

    public static String getLastMissionPlayed(String missionCycleName) {
        // Get the mission name from Stats for the last mission played
        PreparedStatement s;
        ResultSet rs;
        String lastMissionPlayed = "";
        if (MainController.isStatsOn()) {
            try {
                s = MainController.STATSCONN.prepareStatement("SELECT missionName " + " FROM missions " + " WHERE missionCycle = ? " + " ORDER BY missionStartTime DESC LIMIT 1");
                s.setString(1, missionCycleName);
                rs = s.executeQuery();

                if (rs.next()) {
                    lastMissionPlayed = rs.getString(1);
                }
                rs.close();
                s.close();
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectController.getLastMissionPlayed - Error Selecting Mission Name: " + ex);
            }
        } // Stats are ON

        return lastMissionPlayed;
    }

    public static ArrayList<String> getPlaneInfo(String planeName, String weapons) {
        ArrayList<String> planeInfo = new ArrayList<String>(2);
        planeInfo.add("Unknown");
        planeInfo.add("Unknown");

        PreparedStatement s;
        ResultSet rs;
        String sortieType = "Unknown";
        String primaryAirForce = "Unknown";

        if (MainController.isStatsOn()) {
            try {
                s = MainController.STATSCONN.prepareStatement("SELECT sortieType, primaryAirForce " + " FROM planeLoadouts " + " WHERE planeName = ? " + "   AND weapons = ? " + " LIMIT 1");
                s.setString(1, planeName);
                s.setString(2, weapons);
                rs = s.executeQuery();

                if (rs.next()) {
                    sortieType = rs.getString(1);
                    primaryAirForce = rs.getString(2);
                    planeInfo.clear();
                    planeInfo.add(sortieType);
                    planeInfo.add(primaryAirForce);
                }
                rs.close();
                s.close();
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectController.getLastMissionPlayed - Error Selecting Mission Name: " + ex);
            }
        } // Stats are ON
        return planeInfo;
    }

    public static void writeSortie(String name, PilotSortie sortie) {
        ResultSet rs;
        PreparedStatement s;
        long sortieId = 0;

        if (sortie != null && MainController.isStatsOn() && PilotSortieController.isSortieValid(name, sortie)) {

            try {
                if (sortie.getPilotStatus().toString().contains("KIA")) {
                    if (sortie.getBonusPoints() > 0) {
                        sortie.setBonusPoints(Math.round(sortie.getBonusPoints() * 0.10));
                    }
                } else if (sortie.getPilotStatus().toString().equals("CAPTURED")) {
                    if (sortie.getBonusPoints() > 0) {
                        sortie.setBonusPoints(Math.round(sortie.getBonusPoints() * 0.20));
                    }
                } else if (sortie.getPilotStatus().toString().equals("BAILEDOUT")) {
                    if (sortie.getBonusPoints() > 0) {
                        sortie.setBonusPoints(Math.round(sortie.getBonusPoints() * 0.50));
                    }
                }

                long totalScore = MainController.PILOTS.get(name).getScore() - sortie.getScore() + sortie.getBonusPoints();

                String sortieType;
                String airForce;
                ArrayList<String> planeInfo = getPlaneInfo(sortie.getPlane(), sortie.getWeapons());
                sortieType = planeInfo.get(0);
                airForce = planeInfo.get(1);

                s = MainController.STATSCONN.prepareStatement("INSERT INTO sorties (pilotId, missionStartTime, sortieStartTime, sortieEndTime, army, " + "aerodrome, plane, weapons, fuel, score, " + "eAir, eAirConfirmed, fAir, FiBull, HiBull,"
                        + " HiABull, FiRock, HiRock, FiBomb, HiBomb, " + "planeMarkings, pilotStatus, planeStatus, bailedOut, sortieType, airForce) VALUES ( " + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + " ?, ?, ?, ?, ?, ? )",
                        Statement.RETURN_GENERATED_KEYS);
                s.setInt(1, MainController.PILOTS.get(name).getPilotId());
                s.setLong(2, sortie.getMissionStartTime() / 1000);
                s.setLong(3, sortie.getSortieStartTime() / 1000);
                s.setLong(4, Time.getTime() / 1000); // Sortie End Time
                s.setInt(5, sortie.getArmy());
                if (sortie.getAerodrome() != null) {
                    s.setString(6, sortie.getAerodrome().getAerodromeMapGrid());
                } else {
                    s.setString(6, "Unknown");
                }
                s.setString(7, sortie.getPlane());
                s.setString(8, sortie.getWeapons());
                s.setString(9, sortie.getFuel());
                s.setLong(10, totalScore);
                s.setInt(11, (MainController.PILOTS.get(name).getEAir() - sortie.getEAir()));
                s.setInt(12, (MainController.PILOTS.get(name).getEAirConfirmed() - sortie.getEAirConfirmed()));
                s.setInt(13, (MainController.PILOTS.get(name).getFAir() - sortie.getFAir()));
                s.setInt(14, (MainController.PILOTS.get(name).getFiBull() - sortie.getFiBull()));
                s.setInt(15, (MainController.PILOTS.get(name).getHiBull() - sortie.getHiBull()));
                s.setInt(16, (MainController.PILOTS.get(name).getHiABull() - sortie.getHiABull()));
                s.setInt(17, (MainController.PILOTS.get(name).getFiRock() - sortie.getFiRock()));
                s.setInt(18, (MainController.PILOTS.get(name).getHiRock() - sortie.getHiRock()));
                s.setInt(19, (MainController.PILOTS.get(name).getFiBomb() - sortie.getFiBomb()));
                s.setInt(20, (MainController.PILOTS.get(name).getHiBomb() - sortie.getHiBomb()));
                s.setString(21, sortie.getPlaneMarkings());
                s.setString(22, sortie.getPilotStatus().toString());
                s.setString(23, sortie.getPlaneStatus().toString());
                s.setBoolean(24, sortie.isBailedOut());
                s.setString(25, sortieType);
                s.setString(26, airForce);

//				int rowCount = s.executeUpdate();
                s.executeUpdate();
                rs = s.getGeneratedKeys();
                if (rs.next()) {
                    sortieId = rs.getLong(1);
                    MainController.writeDebugLogFile(2, "MySQLConnectionController.writeSortie: Wrote sortie Id(" + sortieId + ")");
                    MySQLConnectionController.writeSortieEvents(sortie, sortieId);
                } else {
                    MainController.writeDebugLogFile(1, "MySQLConnectionController.writeSortie - Error retrieving sortieId after Insert");
                }
                rs.close();
                s.close();
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.writeSortie - Error Inserting Sortie Record for pilot(" + name + "): " + ex);
            }
        } // If Stats
        else {
//			MainController.writeDebugLogFile(1, "MySQLConnectionController.writeSortie - Error Sortie value Null for pilot(" + name + "): " );
        }
    }

    public static void writeSortieEvents(PilotSortie sortie, long sortieId) {
//		ResultSet rs;
        PreparedStatement s;
        int counter = 0;
        double locationX = 0.0;
        double locationY = 0.0;
        if (MainController.isStatsOn()) {

            try {

                Iterator<Long> it = sortie.getSortieEvents().keySet().iterator();

                while (it.hasNext()) {
                    long key = it.next();
                    // Sometimes the Location data is sent as NaN "Not a Number" so set to 0 before writing to DB
                    if (Double.isNaN(sortie.getSortieEvents().get(key).getLocationX())) {
                        locationX = 0.0;
                    } else {
                        locationX = sortie.getSortieEvents().get(key).getLocationX();
                    }
                    if (Double.isNaN(sortie.getSortieEvents().get(key).getLocationY())) {
                        locationY = 0.0;
                    } else {
                        locationY = sortie.getSortieEvents().get(key).getLocationY();
                    }
//					s = MainController.STATSCONN
//							.prepareStatement("INSERT INTO sortieEvents (sortieId, eventTime, eventId, pilotId, opponentId, opponentName, "
//									        + "opponentObjectType, opponentSortieStartTime, opponentArmy, damage, damageValue, locationX, locationY, targetGrid ) VALUES ( "
//									+ " ?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    s = MainController.STATSCONN.prepareStatement("INSERT INTO sortieEvents (sortieId, eventTime, eventId, pilotId, opponentId, opponentName, "
                            + "opponentObjectType, opponentSortieStartTime, opponentArmy, locationX, locationY, targetGrid ) VALUES ( " + " ?,?,?,?,?,?,?,?,?,?,?,?)");
                    s.setLong(1, sortieId);
                    s.setLong(2, key);
                    s.setString(3, sortie.getSortieEvents().get(key).getEventType().toString());
                    s.setInt(4, sortie.getPilotId());
                    s.setInt(5, sortie.getSortieEvents().get(key).getOpponentId());
                    s.setString(6, sortie.getSortieEvents().get(key).getOpponentName());
                    s.setString(7, sortie.getSortieEvents().get(key).getOpponentObjectType().toString());
                    s.setLong(8, sortie.getSortieEvents().get(key).getOpponentSortieStartTime() / 1000);
                    s.setInt(9, sortie.getSortieEvents().get(key).getOpponentArmy());
//					s.setString(10, sortie.getSortieEvents().get(key).getDamage());
//					s.setDouble(11, sortie.getSortieEvents().get(key).getDamageValue());
//					s.setDouble(12, locationX);
//					s.setDouble(13, locationY);
//					s.setString(14, sortie.getSortieEvents().get(key).getTargetGrid());
                    s.setDouble(10, locationX);
                    s.setDouble(11, locationY);
                    s.setString(12, sortie.getSortieEvents().get(key).getTargetGrid());
//					int rowCount = s.executeUpdate();
                    s.executeUpdate();
                    s.close();
                    counter++;
                }
                MainController.writeDebugLogFile(2, "MySQLConnectionController.writeSortieEvents: Wrote (" + counter + ") sortie events");

            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.writeSortieEvents - Error Inserting Sortie Event Record for Sortie Id( " + sortieId + " ): " + ex);
            }
        } // If Stats
    }

    public static void writeMissionObjectEvent(MissionObjectEvent missionObjectEvent) {
        PreparedStatement s;

        if (MainController.isStatsOn()) {
            try {
                s = MainController.STATSCONN.prepareStatement("INSERT INTO missionObjectEvents (missionStartTime, missionObjectName, " + "IL2ObjectId, IL2ObjectName, IL2ObjectType, army, eventTime, eventId, opponentId, opponentName, opponentObjectType, "
                        + "opponentSortieStartTime, opponentArmy, locationX, locationY, targetGrid ) VALUES ( " + " ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                s.setLong(1, MainController.ACTIVEMISSION.getStartTime() / 1000);
                s.setString(2, missionObjectEvent.getMissionObjectName());
                s.setInt(3, missionObjectEvent.getIL2ObjectId());
                s.setString(4, missionObjectEvent.getIL2ObjectName());
                s.setString(5, missionObjectEvent.getIL2ObjectType().toString());
                s.setInt(6, missionObjectEvent.getArmy());
                s.setLong(7, missionObjectEvent.getEventTime() / 1000);
                s.setString(8, missionObjectEvent.getEventType().toString());
                s.setInt(9, missionObjectEvent.getOpponentId());
                s.setString(10, missionObjectEvent.getOpponentName());
                s.setString(11, missionObjectEvent.getOpponentObjectType().toString());
                s.setLong(12, missionObjectEvent.getOpponentSortieStartTime() / 1000);
                s.setInt(13, missionObjectEvent.getOpponentArmy());
                s.setDouble(14, missionObjectEvent.getLocationX());
                s.setDouble(15, missionObjectEvent.getLocationY());
                s.setString(16, missionObjectEvent.getTargetGrid());
//				int rowCount = s.executeUpdate();
                s.executeUpdate();
                s.close();
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.writeMissionObjectEvent - Error Writing MissionObjectEvent for Mission Object ID(" + missionObjectEvent.getIL2ObjectId() + "): " + ex);
            }
        }
    } // End writeMissionObjectEvent

    public static void writeActivePilots() {
//		ResultSet rs;
        PreparedStatement pStmt;
        Statement stmt = null;
        String sql;
        String plane = "Not Selected";
        String markings = "Not Selected";
        int army = 0;
//		int count;
        String pilotState = null;
//		boolean result;

        if (MainController.isStatsOn()) {

            try {
                sql = "DELETE FROM activePilots;";
                stmt = MainController.STATSCONN.createStatement();
//				result = stmt.execute(sql);
                stmt.execute(sql);
                stmt.close();

            } catch (Exception ex) {}
            try {

                Iterator<String> pilot = MainController.PILOTS.keySet().iterator();

                while (pilot.hasNext()) {
                    String key = pilot.next();
                    if (MainController.SORTIES.containsKey(key)) {
                        plane = MainController.SORTIES.get(key).getPlane();
                        markings = MainController.SORTIES.get(key).getPlaneMarkings();
                        army = MainController.SORTIES.get(key).getArmy();
                        pilotState = MainController.SORTIES.get(key).getPilotStatus() + "/" + MainController.SORTIES.get(key).getPlaneStatus();

                    } else {
                        plane = "Not Selected";
                        markings = "Not Selected";
                        army = 0;
                        pilotState = MainController.PILOTS.get(key).getState().toString();
                    }

//					System.out.println("Name: "+key);
//					for (int i=0;i<key.length();i++)
//					{
//						System.out.printf("%c\n", key.codePointAt(i));
//					}
                    if (!key.equals("UnknownFBDjPilot")) {
                        pStmt = MainController.STATSCONN.prepareStatement("INSERT INTO activePilots (pilotName, army, aircraft, " + "markings, state, country) VALUES ( ?,?,?,?,?,? )");
                        pStmt.setBytes(1, MainController.PILOTS.get(key).getName().getBytes("utf8"));
                        pStmt.setInt(2, army);
                        pStmt.setString(3, plane);
                        pStmt.setString(4, markings);
                        pStmt.setString(5, pilotState);
                        pStmt.setString(6, MainController.PILOTS.get(key).getCountry());
//				    	count = pStmt.executeUpdate();
                        pStmt.executeUpdate();
                        pStmt.close();
                    }
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.writeActivePilots - Error Writing Active Pilot List: " + ex);
            }
        }

    }

    public static void writeActiveMission(Mission mission) {
        PreparedStatement pStmt;
        Statement stmt = null;
        String sql;
        ArrayList<String> redPlaneLimits = AerodromeController.planeLimitGetPlanesAvailable(MainController.ACTIVEMISSION, MainController.REDARMY);
        ArrayList<String> bluePlaneLimits = AerodromeController.planeLimitGetPlanesAvailable(MainController.ACTIVEMISSION, MainController.BLUEARMY);

        @SuppressWarnings("unused")
        int count;
//		boolean result;
        String objectiveText = null;

        if (MainController.isStatsOn()) {

            try {
                // Delete Active Mission Data
                sql = "DELETE FROM activeMission;";
                stmt = MainController.STATSCONN.createStatement();
//				result = stmt.execute(sql);
                stmt.execute(sql);
                stmt.close();

                // Delete Active Mission Plane Limits
                sql = "DELETE FROM activeMissionPlaneLimits;";
                stmt = MainController.STATSCONN.createStatement();
//				result = stmt.execute(sql);
                stmt.execute(sql);
                stmt.close();

                pStmt = MainController.STATSCONN.prepareStatement(
                        "INSERT INTO activeMission (missionName, missionStartTime, timeLeft, rPilotsPlanesLeft, bPilotsPlanesLeft, " + " rObjectivesLeft, bObjectivesLeft, rTargetObjectivesLeft, bTargetObjectivesLeft ) VALUES ( ?,?,?,?,?,?,?,?,? )");
                pStmt.setString(1, mission.getMissionName());
                pStmt.setLong(2, mission.getStartTime() / 1000);
                pStmt.setLong(3, MissionController.getMissionTimeLeft() / 1000);
                pStmt.setString(4, MissionCountObjectivesController.displayRemainingPilotPlaneObjectives(mission, MainController.REDARMY));
                pStmt.setString(5, MissionCountObjectivesController.displayRemainingPilotPlaneObjectives(mission, MainController.BLUEARMY));
                // Get Red Objectives left
                objectiveText = MissionCountObjectivesController.displayRemainingCountObjectives(mission, MainController.REDARMY);
                pStmt.setString(6, objectiveText);
                objectiveText = "";
                objectiveText = MissionCountObjectivesController.displayRemainingCountObjectives(mission, MainController.BLUEARMY);
                pStmt.setString(7, objectiveText);
                objectiveText = "";
                ArrayList<String> tgtText = MissionTargetObjectivesController.displayRemainingTargetObjectives(mission, MainController.REDARMY);
                if (tgtText != null) {
                    for (int i = 0; i < tgtText.size(); i++) {
                        objectiveText = objectiveText + tgtText.get(i);
                    }
                    tgtText.clear();
                } else {
                    objectiveText = "No RED Target Objectives Remaining";
                }

                pStmt.setString(8, objectiveText);
                objectiveText = "";
                tgtText = MissionTargetObjectivesController.displayRemainingTargetObjectives(mission, MainController.BLUEARMY);
                if (tgtText != null) {
                    for (int i = 0; i < tgtText.size(); i++) {
                        objectiveText = objectiveText + tgtText.get(i);
                    }
                } else {
                    objectiveText = "No BLUE Target Objectives Remaining";
                }
                pStmt.setString(9, objectiveText);
                count = pStmt.executeUpdate();
                pStmt.close();

                // Write Out Red Plane Limits
                for (String planeLimit : redPlaneLimits) {
                    pStmt = MainController.STATSCONN.prepareStatement("INSERT INTO activeMissionPlaneLimits(army, planeLimit) " + " VALUES (?, ?)");
                    pStmt.setInt(1, MainController.REDARMY);
                    pStmt.setString(2, planeLimit);
                    count = pStmt.executeUpdate();
                    pStmt.close();
                }

                // Write out Blue Plane Limits
                for (String planeLimit : bluePlaneLimits) {
                    pStmt = MainController.STATSCONN.prepareStatement("INSERT INTO activeMissionPlaneLimits(army, planeLimit) " + " VALUES (?, ?)");
                    pStmt.setInt(1, MainController.BLUEARMY);
                    pStmt.setString(2, planeLimit);
                    count = pStmt.executeUpdate();
                    pStmt.close();
                }
            }

            catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.writeActiveMission - Error Writing Active Mission Data: " + ex);
                reConnect();
            }
        }
    }

    public static void writeChatMessage(CommandParse.ChatType chatType, String name, String chatMessage) {
        PreparedStatement s;
        int army = 0;

        if (MainController.isStatsOn()) {
            try {
                if (MainController.PILOTS.containsKey(name)) {
                    army = MainController.PILOTS.get(name).getArmy();
                }
                s = MainController.STATSCONN.prepareStatement("INSERT INTO gameChat ( timeStamp, author, army, chat, chatType ) " + "VALUES (?,?,?,?,?)");
                s.setLong(1, (Time.getTime() / 1000));
                s.setString(2, name);
                s.setInt(3, army);
                s.setBytes(4, chatMessage.getBytes("utf8"));
                s.setString(5, chatType.toString());
//				int rowCount = s.executeUpdate();
                s.executeUpdate();
                s.close();
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.writeChatMessage - Error Inserting Data: " + ex);
            }
        }
    }

    public static void validateSorties() {

        final Timer timer = new Timer();

        class ValidateSorties extends TimerTask {

            ResultSet                  rs;
            ResultSet                  sortieEventsRS;
            PreparedStatement          pStmt;
            Statement                  stmt              = null;
            String                     sql;
//			int army = 0;
            int                        count;
//			boolean result;
            long                       currentTime;
            ArrayList<ArrayList<Long>> updateSortieIds   = new ArrayList<ArrayList<Long>>();
            double                     numberOfPilots    = 1.0;
            double                     adjustedKillValue = 0.0;

            public ValidateSorties(long currentTime) {
                this.currentTime = currentTime;
            }

            public void run() {
                if (MainController.isStatsOn()) {
                    long startTime = Time.getTime();

                    // Update SortieEvents and set the Opponent Sortie Id based on the OpponentId and OpponentSortieStartTime
                    try {
                        // Call Stored Procedure in Database
                        CallableStatement cStmt = MainController.STATSCONN.prepareCall("{call updateOpponentSortieId()}");
//	                    boolean hadResults = cStmt.execute();
                        cStmt.execute();
                    } catch (Exception ex) {
                        MainController.writeDebugLogFile(1, "MySQLConnectionController.validateSorties - Error Updating opponentSortieId: " + ex);
                    }

                    try {
                        // Call Stored Procedure in Database
                        CallableStatement cStmt = MainController.STATSCONN.prepareCall("{call updateSortieGroundKills()}");
//	                    boolean hadResults = cStmt.execute();
                        cStmt.execute();
                    } catch (Exception ex) {
                        MainController.writeDebugLogFile(1, "MySQLConnectionController.validateSorties - Error Updating Ground Kills: " + ex);
                    }

                    // Kill Sharing logic
                    try {
                        // Select all Sorties where the Plane was Shotdown by the enemy
                        sql = "SELECT a.sortieId, opponentId, a.pilotId, a.eventTime, a.opponentSortieId " + "FROM sortieEvents a, sorties b " + "WHERE a.sortieId = b.sortieId " + "AND b.validated = 0 " + "AND a.opponentArmy != b.army "
                                + "AND a.eventId = 'SHOTDOWNBY';";
                        stmt = MainController.STATSCONN.createStatement();
//						result = stmt.execute(sql);
                        stmt.execute(sql);
                        rs = stmt.getResultSet();
                        // Loop through all sorties returned
                        while (rs.next()) {
                            long victimSortieId = rs.getLong(1);
                            long attackerSortieId = rs.getLong(5);
                            int workingOpponentId = rs.getInt(2);
                            int workingPilotId = rs.getInt(3);
                            long workingEventTime = rs.getLong(4);

                            pStmt = MainController.STATSCONN.prepareStatement("SELECT eventTime, eventId, opponentId, opponentSortieId " + "  FROM sortieEvents " + " WHERE sortieId = ?" + " ORDER BY eventTime;");
                            pStmt.setLong(1, victimSortieId);
                            sortieEventsRS = pStmt.executeQuery();
                            updateSortieIds.clear();
                            numberOfPilots = 1.0;
                            // Loop through all the Sortie events from the Sortie to see if any other pilots
                            // participated in the kill
                            while (sortieEventsRS.next()) {
                                // Look for Sortie Events where the Plane was damaged or pilot was killed
                                // by someone other than the one who got the kill
                                if ((sortieEventsRS.getString(2).equals("PLANEDAMAGEDBY") && sortieEventsRS.getInt(3) != workingOpponentId) || (sortieEventsRS.getString(2).equals("PILOTKILLEDBY") && sortieEventsRS.getInt(3) != workingOpponentId)) {
                                    ArrayList<Long> tempList = new ArrayList<Long>();
                                    // Event Time
                                    tempList.add(sortieEventsRS.getLong(1));
                                    // OpponentId
                                    tempList.add(sortieEventsRS.getLong(3));
                                    // OpponentSortieId
                                    tempList.add(sortieEventsRS.getLong(4));
                                    updateSortieIds.add(tempList);
                                    numberOfPilots++;
                                }
                            }
                            pStmt.close();

                            if (numberOfPilots > 1) {
                                // Get the Adjusted Kill value based on the Number of pilots involved
                                adjustedKillValue = 1 / numberOfPilots;
                                MainController.writeDebugLogFile(2, "MySQLConnectionController.validateSorties (" + numberOfPilots + ") Pilots share the kill in Sortie (" + victimSortieId + ")");

                                // Loop through all pilots who shared in the kill
                                for (int i = 0; i < updateSortieIds.size(); i++) {
                                    count = 0;
                                    // Update their sortieevents record to show it has been adjusted.
                                    pStmt = MainController.STATSCONN.prepareStatement("UPDATE sortieEvents " + "SET adjusted = 1 " + " WHERE sortieId = ? "
//											+ "   AND eventId = ? "
                                            + "   AND opponentId = ?" + "   AND eventTime = ?");
                                    // SortieId
                                    pStmt.setLong(1, updateSortieIds.get(i).get(2));
//									pStmt.setString(2, "PLANEDAMAGED");
                                    pStmt.setInt(2, workingPilotId);
                                    // Event Time
                                    pStmt.setLong(3, updateSortieIds.get(i).get(0));
                                    count = pStmt.executeUpdate();
                                    if (count >= 1) {
                                        MainController.writeDebugLogFile(2, "Updated (" + count + ") sortieEvents (" + updateSortieIds.get(i).get(0) + ") and marked as Adjusted");
                                    }
                                    pStmt.close();
                                    count = 0;
                                    // Update the sorties record with the adjustedKillValue.
                                    pStmt = MainController.STATSCONN.prepareStatement("UPDATE sorties " + "SET eAirAdjusted = eAirAdjusted + ? " + " WHERE sortieId = ? ");
                                    pStmt.setDouble(1, adjustedKillValue);
                                    pStmt.setLong(2, updateSortieIds.get(i).get(2));
                                    count = pStmt.executeUpdate();
                                    if (count == 1) {
                                        MainController.writeDebugLogFile(2, "Adjusted Sortie (" + updateSortieIds.get(i).get(2) + ") with AdjustedKillValue (" + adjustedKillValue + ")");
                                    }
                                    pStmt.close();
                                } // End for loop
                                count = 0;
                                // Update the sortie record of the Pilot who got the kill to adjust the kill value.
                                // To properly adjust the kill I take the kill away (-1) then add the adjustedkill value.
                                pStmt = MainController.STATSCONN.prepareStatement("UPDATE sorties " + "SET eAirAdjusted = eAirAdjusted + ? " + " WHERE sortieId = ? ");
                                pStmt.setDouble(1, (-1 + adjustedKillValue));
                                pStmt.setLong(2, attackerSortieId);
                                count = pStmt.executeUpdate();
                                if (count >= 1) {
                                    MainController.writeDebugLogFile(2, "Adjusted Sortie (" + attackerSortieId + ") with AdjustedKillValue (" + (-1 + adjustedKillValue) + ")");
                                }
                                pStmt.close();
                                count = 0;
                                // Update the sortieevents record of the pilot who got the kill to show it has been adjusted.
                                pStmt = MainController.STATSCONN.prepareStatement("UPDATE sortieEvents " + "SET adjusted = 1 " + " WHERE sortieId = ? " + "   AND eventId = ? " + "   AND eventTime = ? " + "   AND opponentId = ?;");
                                pStmt.setLong(1, attackerSortieId);
                                pStmt.setString(2, "SHOTDOWN");
                                pStmt.setLong(3, workingEventTime);
                                pStmt.setInt(4, workingPilotId);
                                count = pStmt.executeUpdate();
                                if (count >= 1) {
                                    MainController.writeDebugLogFile(2, "Updated (" + count + ") sortieEvents and marked SHOTDOWN PILOT as Adjusted");
                                }
                                pStmt.close();
                            }
                        }
                        stmt.close();
                        count = 0;
                        // Update all sorties who start time is less then the time passed in (Mission end time?).
                        pStmt = MainController.STATSCONN.prepareStatement("UPDATE sorties " + "SET validated = 1 " + " WHERE sortieStartTime <  ? " + "   AND validated = 0;");
                        pStmt.setLong(1, currentTime);
                        count = pStmt.executeUpdate();
//						if (count >= 1)
//						{
//							MainController.writeDebugLogFile(2, "Updated ("+count+") Sortie's to Validated");
//						}
                        pStmt.close();

                    } catch (Exception ex) {
                        MainController.writeDebugLogFile(1, "MySQLConnectionController.validateSorties - Error Updating Confirmed Kills: " + ex);
                    }

                    try {
                        // Update Pilot Shotdowns where the Pilot was PK'd then hit refly before the shotdown event occured
                        CallableStatement cStmt = MainController.STATSCONN.prepareCall("{call updatePilotKills()}");
//	                    boolean hadResults = cStmt.execute();
                        cStmt.execute();

                    } catch (Exception ex) {
                        MainController.writeDebugLogFile(1, "MySQLConnectionController.validateSorties - Error Updating PilotKills: " + ex);
                    }

                    MainController.writeDebugLogFile(2, "MySQLConnectionController.validateSorties - Completed in: " + Time.getTimeDuration(startTime) + " Milliseconds");

                }
            } // End Run Method

        } // End ValidateSorties Class
        long runTime = Time.getTime();
        timer.schedule(new ValidateSorties(runTime), 30000);
    } // End validateSorties method

    public static void initializeStats() {
        PreparedStatement stmt;
        ResultSet rs;

        if (MainController.CONNECTED) {
            try {
                // See if the Mission Start record was written.
                stmt = MainController.STATSCONN.prepareStatement("SELECT COUNT(*) FROM missions " + " WHERE missionStartTime = ?;");
                stmt.setLong(1, MainController.ACTIVEMISSION.getStartTime());
                rs = stmt.executeQuery();

                if (rs.next()) {
                    if (rs.getInt(1) == 0) {
                        // Add Mission to Database
                        newMission();
                    }
                    // Mission Already exists in database do nothing
                }

                // Loop through Pilots and make sure everyone has a valid Stats Id
                Iterator<String> pilot = MainController.PILOTS.keySet().iterator();

                while (pilot.hasNext()) {
                    String key = pilot.next();
                    MainController.PILOTS.get(key).setPilotId(MySQLConnectionController.getPilotId(key));
                }

            } catch (Exception ex) {}
        }

        try {
            HashMap<String, Integer> il2Data = new HashMap<String, Integer>();
            // Loop through IL2 Static Objects and make sure all have a valid Stats Id
            Iterator<String> il2Object = MainController.IL2STATICOBJECTS.keySet().iterator();

            while (il2Object.hasNext()) {
                String key = il2Object.next();
                il2Data = MySQLConnectionController.getIL2StaticData(key);
                int extraPoints = il2Data.get("pointValue");
                MainController.IL2STATICOBJECTS.get(key).setIl2ObjectID(il2Data.get("objectId"));
                MainController.IL2STATICOBJECTS.get(key).setPointValue(extraPoints);
//				MainController.IL2STATICOBJECTS.get(key).setBaseValue(il2Data.get("baseValue"));
                if (extraPoints > 0)
                    MainController.writeDebugLogFile(2, "MySQLConnectionController.initializeStats - Object (" + key + ") extra Points: " + extraPoints);
                // MainController.writeDebugLogFile(2, "MySQLConnectionController.initializeStats - Object ("+key+") base Value: "+il2Data.get("baseValue"));

            }
        } catch (Exception ex) {}
    }

    public static Boolean writeMissionCycles(ArrayList<String> missionCycles) {
        PreparedStatement stmt;
//		int count;

        // Check to see if Stats are turned on
        if (MainController.isStatsOn()) {
            try {
                // Delete current Mission Cycle Data
                stmt = MainController.STATSCONN.prepareStatement("DELETE FROM missionCycles");
//				count = stmt.executeUpdate();
                stmt.executeUpdate();

                for (String missionCycle : missionCycles) {
                    // Insert new Mission Cycle
                    stmt = MainController.STATSCONN.prepareStatement("INSERT INTO missionCycles (missionCycleName) VALUES ( ? )");
                    stmt.setString(1, missionCycle);
//					count = stmt.executeUpdate();
                    stmt.executeUpdate();
                }

            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.writeMissionCycles - Error writing to database: " + ex);
                return false;
            }

        }
        return true;
    }

    public static boolean writeMissionFiles(ArrayList<String> missionFiles) {
        PreparedStatement stmt;
//		int count;

        // Check to see if Stats are turned on
        if (MainController.isStatsOn()) {
            try {
                // Delete current Mission Files Data
                stmt = MainController.STATSCONN.prepareStatement("DELETE FROM missionFiles");
//				count = stmt.executeUpdate();
                stmt.executeUpdate();

                for (String missionFile : missionFiles) {
                    // Insert new Mission File
                    stmt = MainController.STATSCONN.prepareStatement("INSERT INTO missionFiles (missionName) VALUES ( ? )");
                    stmt.setString(1, missionFile);
//					count = stmt.executeUpdate();
                    stmt.executeUpdate();
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.writeMissionFiles - Error writing to database: " + ex);
                return false;
            }

        }
        return true;
    }

    public static ScheduledEvent missionToExecute() {
        PreparedStatement stmt;
        ScheduledEvent newEvent = null;
        String missionName = null;
        int eventId = 0;
        // Check to see if Stats are turned on
        if (MainController.isStatsOn()) {
            try {
                stmt = MainController.STATSCONN.prepareStatement("SELECT id, eventname FROM calendarEvents " + "WHERE eventStart < CURRENT_TIMESTAMP() " + "  AND eventType = 'Mission to Run' " + "  AND eventExecuted = 0 "
                        + "  AND ( datediff(eventStart, curdate()) = 0 ) " + "ORDER BY eventStart " + " LIMIT 1");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    eventId = rs.getInt("id");
                    missionName = rs.getString("eventname");
                    newEvent = new ScheduledEvent(eventId, ScheduledEvent.EventType.MissionToRun, missionName);
                    MainController.writeDebugLogFile(1, "MySQLConnectionController.missionToExecute - Scheduled Mission ( " + missionName + " ) Found");

                } // end while loop
                rs.close();
                stmt.close();
                // Delete current Mission Files Data

            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.writeMissionFiles - Error writing to database: " + ex);
                return null;
            }

        }
        if (newEvent != null) {
            return newEvent;
        }
        return null;
    }

    public static ScheduledEvent missionCycleToExecute(String frequency) {
        PreparedStatement stmt;
        ScheduledEvent newEvent = null;
        String missionCycleName = null;
        int eventId = 0;

        // FIXME: Just for Debugging the Cycle Change!
        MainController.writeDebugLogFile(2, "MySQLConnectionController.missionCycleToExecute( " + frequency + " )");

        // Check to see if Stats are turned on
        if (MainController.isStatsOn()) {
            try {
                // Check for Mission Cycle Change event occuring within the next 15 minutes
                if (frequency.equals("Daily")) {
                    MainController.writeDebugLogFile(2, "... Stats are ON, checking Daily Events ...");
                    stmt = MainController.STATSCONN.prepareStatement("SELECT id, eventName FROM calendarEvents " + " WHERE eventType = 'MissionCycle Change' " + "   AND TIME_TO_SEC(eventStart) < TIME_TO_SEC(ADDTIME(CURTIME(), '00:15'))"
                            + "   AND TIME_TO_SEC(eventEnd) > TIME_TO_SEC(ADDTIME(CURTIME(), '-00:15'))" + "   AND frequency = 'Daily' " + "  ORDER BY TIME(eventStart) DESC " + "  LIMIT 1");
                } else {
                    MainController.writeDebugLogFile(2, "... Stats are ON, checking One Time Events ...");
                    stmt = MainController.STATSCONN.prepareStatement("SELECT id, eventName FROM calendarEvents " + " WHERE eventType = 'MissionCycle Change' " + "   AND TIME_TO_SEC(eventstart) < TIME_TO_SEC(ADDTIME(CURTIME(), '00:15'))"
                            + "   AND TIME_TO_SEC(eventEnd) > TIME_TO_SEC(ADDTIME(CURTIME(), '-00:15'))" + "   AND datediff(eventStart, curdate()) = 0 " + "   AND frequency = 'Once' " + "  ORDER BY TIME(eventStart) DESC " + "  LIMIT 1");

                }
                ResultSet rs = stmt.executeQuery();

                // FIXME: Just for Debugging the Cycle Change!
                int rowcount = 0;
                if (rs.last()) {
                    rowcount = rs.getRow();
                    rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
                }
                MainController.writeDebugLogFile(2, "... " + rowcount + " new Events available.");

                while (rs.next()) {
                    eventId = rs.getInt("id");
                    missionCycleName = rs.getString("eventName");
                    newEvent = new ScheduledEvent(eventId, ScheduledEvent.EventType.MissionCycleChange, missionCycleName);

                    // FIXME: Just for Debugging the Cycle Change!
                    MainController.writeDebugLogFile(2, "Scheduled NEW Mission Cycle Change( " + missionCycleName + " ) Found frequency ( " + frequency + " )");

                    // Only print out mission cycle change if it's a new mission cycle change
                    if (MainController.MISSIONCONTROL.getMissionCycleEvent() == null || !MainController.MISSIONCONTROL.getMissionCycleEvent().getEventName().equals(missionCycleName)) {
                        MainController.writeDebugLogFile(1, "MySQLConnectionController.missionCycleToExecute - Scheduled Mission Cycle Change( " + missionCycleName + " ) Found frequency ( " + frequency + " )");
                    }
                } // end while loop
                rs.close();
                stmt.close();
                // Delete current Mission Files Data

            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.writeMissionFiles - Error writing to database: " + ex);
                return null;
            }

        }
        if (newEvent != null) {
            return newEvent;
        }
        return null;
    }

    public static void updateCalendarEventLastMissionRan(int eventId, String lastMissionRan) {
        PreparedStatement stmt;
//		int count;
        // Check to see if Stats are turned on
        if (MainController.isStatsOn()) {
            try {

                stmt = MainController.STATSCONN.prepareStatement("UPDATE calendarEvents SET lastMissionRan='?' " + "WHERE eventId = '?' ");
                stmt.setString(1, lastMissionRan);
                stmt.setInt(2, eventId);
//				count = stmt.executeUpdate();
                stmt.executeUpdate();

            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.updateCalendarEventLastMissionRan - Error updating record: " + ex);
            }
        }
    }

    public static void updateCalendarEventMissionExecuted(int eventId) {
        PreparedStatement stmt;
//		int count;
        // Check to see if Stats are turned on
        if (MainController.isStatsOn()) {
            try {

                stmt = MainController.STATSCONN.prepareStatement("UPDATE calendarEvents SET eventExecuted=1 " + "WHERE id = ? ");
                stmt.setInt(1, eventId);
//				count = stmt.executeUpdate();
                stmt.executeUpdate();
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.updateCalendarEventMissionExecuted - Error updating record: " + ex);
            }
        }
    }

    public static void updateBaseAttackLog(String pilotName, String victimName, String event, String aerodromeMapGrid, long missionStartTime, String missionName) {
        PreparedStatement stmt;
//		int count;
        // Check to see if Stats are turned on
        if (MainController.isStatsOn()) {
            try {
                stmt = MainController.STATSCONN.prepareStatement("INSERT INTO baseAttackLog (attackTime, missionName, missionStartTime, " + " aerodromeMapGrid, pilot, event, victim) VALUES (NOW(), ?, ?, ?, ?, ?, ? )");
                stmt.setString(1, missionName);
                stmt.setLong(2, missionStartTime / 1000);
                stmt.setString(3, aerodromeMapGrid);
                stmt.setString(4, pilotName);
                stmt.setString(5, event);
                stmt.setString(6, victimName);

                MainController.writeDebugLogFile(2, "MySQLConnectionController.updateBaseAttackLog - New Record (" + stmt.toString() + ")");

//				count = stmt.executeUpdate();
                stmt.executeUpdate();
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.updateBaseAttackLog - Error inserting record: " + ex);
            }
        }
    }

    public static long getLastLanguageUpdate() {
        ResultSet rs = null;
        PreparedStatement stmt;
        long lastUpdateTime = 0;
        if (MainController.isStatsOn()) {
            try {
                stmt = MainController.STATSCONN.prepareStatement("SELECT MAX(UNIX_TIMESTAMP(ts)) AS ts FROM badWords");
                rs = stmt.executeQuery();
                while (rs.next()) {
                    lastUpdateTime = rs.getLong("ts");
                } // end while loop
                rs.close();
                stmt.close();
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.getLastLanguageUpdate - Error Retrieving timestamp: " + ex);
            }
        }
        return lastUpdateTime;
    }

    public static HashMap<String, String> getBadWordList() {
        ResultSet rs = null;
        PreparedStatement stmt;
        HashMap<String, String> badWordList = null;
        String badWord = null;
        // Check to see if Stats are turned on
        if (MainController.isStatsOn()) {
            try {
                stmt = MainController.STATSCONN.prepareStatement("SELECT word FROM badWords");
                rs = stmt.executeQuery();
                badWordList = new HashMap<String, String>();
                while (rs.next()) {
                    badWord = rs.getString("word");
                    badWord = badWord.toUpperCase();
                    badWordList.put(badWord, badWord);
                } // end while loop
                rs.close();
                stmt.close();
                if (badWordList.size() == 0) {
                    MainController.writeDebugLogFile(1, "MySQLConnectionController.getBadWordList - Loaded 0 Words from Database");
                    badWordList = null;
                } else {
                    MainController.writeDebugLogFile(1, "MySQLConnectionController.getBadWordList - Loaded ( " + badWordList.size() + " ) Words from Database");
                }
            } catch (Exception ex) {
                MainController.writeDebugLogFile(1, "MySQLConnectionController.getBadWordList - Error Retrieving words: " + ex);
            }
        }
        return badWordList;
    }

}
