package mainController;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import model.Admin;
import model.BadLanguage;
import model.Configuration;
import model.IL2Map;
import model.IL2StaticObject;
import model.Mission;
import model.MissionControl;
import model.MissionFile;
import model.MultiVehicleObject;
import model.Pilot;
import model.PilotBan;
import model.PilotSortie;
import model.PlanePilotsStatusMessages;
import model.PlanePilotsStatusWarnings;
import model.QueueObj;
import model.ReservedName;
import model.StartupConfiguration;
import model.WarningPoints;
import model.Weapon;
import thread.EventLogReader;
import thread.ServerListen;
import utility.SocketConnection;

import com.maxmind.geoip.LookupService;

public class MainController implements Runnable {
    // Army
    public final static int                           REDARMY              = 1;
    public final static int                           BLUEARMY             = 2;
    // Config
    public static String                              FBDJROOTDIRECTORY    = "." + File.separatorChar;
    public static StartupConfiguration                STARTUPCONFIG;
    public static Configuration                       CONFIG;
    public static String                              CONFIGFILENAME       = "FBDjConfiguration.ser";
    static String                                     MISSIONPATH;

    public static WarningPoints                       WARNINGPOINTS;
    public static String                              WPFILENAME           = "FBDjWarningPoints.ser";

    // EventLog
    static EventLogReader                             EVENTLOG;
    // Server Connection
    public static SocketConnection                    SERVERCONN;
    public static ServerListen                        SERVERLISTEN;
    // Stats Connection
    static boolean                                    STATSON              = false;
    static Connection                                 STATSCONN;
    // Pilot
    static HashMap<String, Pilot>                     PILOTS;
    static HashMap<String, PilotSortie>               SORTIES;
    static HashMap<String, PilotSortie>               ENDINGSORTIES;
    // Admin
    static HashMap<String, Admin>                     ADMINS;
    static final String                               ADMINFILENAME        = "FBDjAdminList.ser";
    // Reserved Names
    static HashMap<String, ReservedName>              RESERVEDNAMES;
    static final String                               RESERVEDNAMEFILENAME = "FBDjReservedNames.ser";
    // Pilot Bans
    static HashMap<String, PilotBan>                  BANNEDPILOTS;
    static final String                               PILOTBANFILENAME     = "FBDjBanList.ser";
    // IL2 Map, Ground Objects, & Plane Loadouts
    public static HashMap<String, IL2Map>             IL2MAPS;
    public static HashMap<String, IL2StaticObject>    IL2STATICOBJECTS;
    public static HashMap<String, MultiVehicleObject> IL2MULTIOBJECTS;
    public static HashMap<String, ArrayList<Weapon>>  IL2PLANELOADOUTS;
    public static HashMap<String, Integer>            IL2REGIMENTS;
    public static HashMap<String, String>             IL2AIRCLASSES;

    // Mission
    static Mission                                    ACTIVEMISSION        = null;
    public static MissionControl                      MISSIONCONTROL;

    // Logging
    static final String                               DEBUGLOGFILENAME     = "FBDjLogFile.txt";
    static final String                               CHATLOGFILENAME      = "FBDjChatLog.txt";
    static final String                               ADMINLOGFILENAME     = "FBDjAdminLog.txt";
    static final String                               IPLOGFILENAME        = "FBDjIPAccessLog.txt";
    static final String                               STARTSTOPLOGFILENAME = "FBDjStartStopLog.txt";
    // Queues
    static ArrayList<QueueObj>                        QUEUEOBJECTS;
    static CommandParse                               COMMANDPARSE;
    static ArrayList<String>                          AUTHENTICATIONQUEUE;
    // GeoIP
    static LookupService                              IPLOOKUPSERVICE;
    static String                                     GEODBFILE            = "GeoLiteCity.dat";
    // Connected Flag
    public static boolean                             CONNECTED            = false;
    // Bad Language List
    static final String                               BADLANGUAGEFILENAME  = "FBDjBadLanguage.txt";
    public static BadLanguage                         BADLANGUAGE;
    public static PlanePilotsStatusMessages           PLANEPILOTMESSAGES;
    public static PlanePilotsStatusWarnings           PLANEPILOTWARNING;

    // *************** CONFIGURATION ***************
    public static void startupConfigInitialize(String[] startupParameters) {
        StartupConfigurationController.startupConfigInitialize(startupParameters);
    }

    public static void configInitialize() {
        ConfigurationController.configInitialize();
    }

    public static Configuration getConfiguration() {
        return MainController.CONFIG;
    }

    public static void wpInitialize() {
        WarningPointsController.initialize();
    }

    public static WarningPoints getWarningPoints() {
        return MainController.WARNINGPOINTS;
    }

    public static void setWarningPoints(WarningPoints warningPoints) {
        MainController.WARNINGPOINTS = warningPoints;
    }

    public static void setConfiguration(Configuration newConfig) {
        MainController.CONFIG = newConfig;
    }

    public static String getMissionDirectory() {
        return MainController.CONFIG.getServerDirectory() + "Missions" + File.separatorChar;
    }

    // *************** LOGGING ***************
    public static synchronized void writeDebugLogFile(int debugLevel, String data) {
        LogController.writeDebugLogFile(debugLevel, data);
    }

    public static synchronized void writeIPAccessLogFile(String name, String ipAddress) {
        String data = ipAddress + " " + name;
        LogController.writeIPLogFile(data);
    }

    // ************** CONNECT *********************
    // Called directly from GUI
    public void run() {
        ConnectController.connect();
    }

    // Called directly from GUI
    public static void disconnect() {
        ConnectController.disconnect();
    }

    // *************** STATS ************************
    // Called directly from GUI
    public static void statsConnect() {
        MySQLConnectionController.connect();
    }

    // Called Directly from GUI
    public static void statsDisconnect() {
        MySQLConnectionController.disconnect();
    }

    // *************** Queues ************************
    // Called from GUI, EventLog, and Socket Threads
    public static synchronized ArrayList<QueueObj> doQueue(QueueObj newObj) {
        return QueueController.doQueue(newObj);
    }

    // **************** Mission Utilities *****************
    public static Boolean validateMission(MissionFile mission) {
        return MissionController.validateMission(mission);
    }

    // *************** Admins ************************
    // All Admins are changed through the GUI
    public static HashMap<String, Admin> getAdmins() {
        return ADMINS;
    }

    public static void setAdmins(HashMap<String, Admin> admins) {
        ADMINS = admins;
    }

    public static HashMap<String, PilotBan> getBannedPilots() {
        return BANNEDPILOTS;
    }

    public static void setBannedPilots(HashMap<String, PilotBan> bannedPilots) {
        BANNEDPILOTS = bannedPilots;
    }

    public static HashMap<String, ReservedName> getReservedNames() {
        return RESERVEDNAMES;
    }

    public static void setReservedNames(HashMap<String, ReservedName> reservedNames) {
        RESERVEDNAMES = reservedNames;
    }

    public static boolean isStatsOn() {
        return STATSON;
    }

    public static void setStatsOn(boolean statsOn) {
        MainController.STATSON = statsOn;
    }

    // ************ Plane Loadouts *******************************
    public static Weapon getWeapon(String plane, String weaponDescription) {
        ArrayList<Weapon> planeLoadouts = null;

        try {
            if (MainController.IL2PLANELOADOUTS.containsKey(plane)) {
                planeLoadouts = MainController.IL2PLANELOADOUTS.get(plane);
                for (Weapon weapon : planeLoadouts) {
                    if (weapon.getWeaponDescription().equals(weaponDescription)) {
                        return weapon;
                    }
                }
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "MainController.getWeapon - Unhandled Exception finding weapon for Plane(" + plane + ") Weapon Desc(" + weaponDescription + ")");
        }
        return null;
    }
}
