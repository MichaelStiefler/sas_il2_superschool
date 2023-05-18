package model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Configuration implements Serializable {

    private static final long                  serialVersionUID = 1L;

    private String                             configName       = "default";
    private HashMap<String, ConfigurationItem> pingSettings;
    private HashMap<String, ConfigurationItem> iconSettings;
    private HashMap<String, ConfigurationItem> dynamicVariables;
    private HashMap<String, ConfigurationItem> staticVariables;
    private ArrayList<Integer>                 missionEndTimerInterval;
    private String                             serverVersion;

    public Configuration(String configName) {
        ConfigurationItem configItem;
        this.configName = configName;
        this.missionEndTimerInterval = new ArrayList<Integer>();
        this.missionEndTimerInterval.add(60);
        this.missionEndTimerInterval.add(30);
        this.missionEndTimerInterval.add(15);
        this.serverVersion = "Unknown";

        // Ping Settings
        pingSettings = new HashMap<String, ConfigurationItem>();
        configItem = new ConfigurationItem("maxPing", "Max Ping", ConfigurationItem.ConfigItemType.INTEGER, 500, 0, 2000);
        pingSettings.put("maxPing", configItem);
        configItem = new ConfigurationItem("pingDelay", "Ping Delay", ConfigurationItem.ConfigItemType.INTEGER, 10, 0, 100);
        pingSettings.put("pingDelay", configItem);
        configItem = new ConfigurationItem("warnings", "Number of Warnings", ConfigurationItem.ConfigItemType.INTEGER, 5, 0, 10);
        pingSettings.put("warnings", configItem);

        // Icon Settings
        iconSettings = new HashMap<String, ConfigurationItem>();
        configItem = new ConfigurationItem("fColor", "Friendly Color", ConfigurationItem.ConfigItemType.DOUBLE, 0.0, 0.0, 1000.0);
        iconSettings.put("fColor", configItem);
        configItem = new ConfigurationItem("fDot", "Friendly Dot", ConfigurationItem.ConfigItemType.DOUBLE, 0.0, 0.0, 1000.0);
        iconSettings.put("fDot", configItem);
        configItem = new ConfigurationItem("fRange", "Friendly Range", ConfigurationItem.ConfigItemType.DOUBLE, 0.0, 0.0, 1000.0);
        iconSettings.put("fRange", configItem);
        configItem = new ConfigurationItem("fType", "Friendly Type", ConfigurationItem.ConfigItemType.DOUBLE, 0.0, 0.0, 1000.0);
        iconSettings.put("fType", configItem);
        configItem = new ConfigurationItem("fId", "Friendly ID", ConfigurationItem.ConfigItemType.DOUBLE, 0.0, 0.0, 1000.0);
        iconSettings.put("fId", configItem);
        configItem = new ConfigurationItem("fName", "Friendly Name", ConfigurationItem.ConfigItemType.DOUBLE, 0.0, 0.0, 1000.0);
        iconSettings.put("fName", configItem);
        configItem = new ConfigurationItem("eColor", "Enemy Color", ConfigurationItem.ConfigItemType.DOUBLE, 0.0, 0.0, 1000.0);
        iconSettings.put("eColor", configItem);
        configItem = new ConfigurationItem("eDot", "Enemy Dot", ConfigurationItem.ConfigItemType.DOUBLE, 0.0, 0.0, 1000.0);
        iconSettings.put("eDot", configItem);
        configItem = new ConfigurationItem("eRange", "Enemy Range", ConfigurationItem.ConfigItemType.DOUBLE, 0.0, 0.0, 1000.0);
        iconSettings.put("eRange", configItem);
        configItem = new ConfigurationItem("eType", "Enemy Type", ConfigurationItem.ConfigItemType.DOUBLE, 0.0, 0.0, 1000.0);
        iconSettings.put("eType", configItem);
        configItem = new ConfigurationItem("eId", "Enemy ID", ConfigurationItem.ConfigItemType.DOUBLE, 0.0, 0.0, 1000.0);
        iconSettings.put("eId", configItem);
        configItem = new ConfigurationItem("eName", "Enemy Name", ConfigurationItem.ConfigItemType.DOUBLE, 0.0, 0.0, 1000.0);
        iconSettings.put("eName", configItem);

        // Static Variables
        staticVariables = new HashMap<String, ConfigurationItem>();
        configItem = new ConfigurationItem("enablePingKick", "Enable Ping Kick", ConfigurationItem.ConfigItemType.BOOLEAN, true);
        staticVariables.put("enablePingKick", configItem);
        configItem = new ConfigurationItem("serverDirectory", "IL2 Server Directory", ConfigurationItem.ConfigItemType.STRING, "?");
        staticVariables.put("serverDirectory", configItem);
        configItem = new ConfigurationItem("serverIP", "IL2 Server IP Address", ConfigurationItem.ConfigItemType.STRING, "0.0.0.0");
        staticVariables.put("serverIP", configItem);
        configItem = new ConfigurationItem("serverPort", "IL2 Server Port", ConfigurationItem.ConfigItemType.INTEGER, 21000, 0, 65535);
        staticVariables.put("serverPort", configItem);
        configItem = new ConfigurationItem("serverEventLog", "EventLog File (Including Path)", ConfigurationItem.ConfigItemType.STRING, "?");
        staticVariables.put("serverEventLog", configItem);
        configItem = new ConfigurationItem("statsIP", "Stats DB IP Address", ConfigurationItem.ConfigItemType.STRING, "0.0.0.0");
        staticVariables.put("statsIP", configItem);
        configItem = new ConfigurationItem("statsPort", "Stats DB Port", ConfigurationItem.ConfigItemType.INTEGER, 3306, 0, 65535);
        staticVariables.put("statsPort", configItem);
        configItem = new ConfigurationItem("statsDBName", "Stats DB Name", ConfigurationItem.ConfigItemType.STRING, "fbdjstats");
        staticVariables.put("statsDBName", configItem);
        configItem = new ConfigurationItem("statsDB01Username", "Stats DB Username", ConfigurationItem.ConfigItemType.STRING, "fbdadmin");
        staticVariables.put("statsDB01Username", configItem);
        configItem = new ConfigurationItem("statsDB02Password", "Stats DB Password", ConfigurationItem.ConfigItemType.STRING, "?");
        staticVariables.put("statsDB02Password", configItem);
        configItem = new ConfigurationItem("autoStart", "Auto Start", ConfigurationItem.ConfigItemType.BOOLEAN, false);
        staticVariables.put("autoStart", configItem);
        configItem = new ConfigurationItem("resumeMissionCycle", "Resume with Next Mission", ConfigurationItem.ConfigItemType.BOOLEAN, true);
        staticVariables.put("resumeMissionCycle", configItem);

        configItem = new ConfigurationItem("pilotPlaneLossWarningMessage", "Announce Pilot/Plane Loss Warning", ConfigurationItem.ConfigItemType.CHATRECIEPIENTS, ConfigurationItem.ChatReciepients.NONE);
        staticVariables.put("pilotPlaneLossWarningMessage", configItem);
        configItem = new ConfigurationItem("pilotPlaneLossWarningInterval", "Pilot/Plane Loss Warning Interval (separate by ,)", ConfigurationItem.ConfigItemType.STRING, "10,5,2,1");
        staticVariables.put("pilotPlaneLossWarningInterval", configItem);

        configItem = new ConfigurationItem("pilotPlaneLossesMessage", "Announce Pilot/Plane Losses", ConfigurationItem.ConfigItemType.CHATRECIEPIENTS, ConfigurationItem.ChatReciepients.NONE);
        staticVariables.put("pilotPlaneLossesMessage", configItem);
        configItem = new ConfigurationItem("pilotPlaneLossesLimit", "Interval for Pilot/Plane Losses", ConfigurationItem.ConfigItemType.INTEGER, 0, 0, 100);
        staticVariables.put("pilotPlaneLossesLimit", configItem);

        // Dynamic Variables
        dynamicVariables = new HashMap<String, ConfigurationItem>();
        configItem = new ConfigurationItem("fKillAirBan", "Friendly Kill Air Ban Limit", ConfigurationItem.ConfigItemType.INTEGER, 2, 0, 1000);
        dynamicVariables.put("fKillAirBan", configItem);
        configItem = new ConfigurationItem("fKillGroundBan", "Friendly Kill Ground Ban Limit", ConfigurationItem.ConfigItemType.INTEGER, 2, 0, 1000);
        dynamicVariables.put("fKillGroundBan", configItem);
        configItem = new ConfigurationItem("fKillBanZDuration", "Friendly Kill Ban Duration", ConfigurationItem.ConfigItemType.DOUBLE, 1.0, 0.0, 100000.0);
        dynamicVariables.put("fKillBanZDuration", configItem);
        configItem = new ConfigurationItem("fKillAirBanCount", "Count Friendly Air Kills on Ground", ConfigurationItem.ConfigItemType.BOOLEAN, true);
        dynamicVariables.put("fKillAirBanCount", configItem);
        configItem = new ConfigurationItem("fogOfWar", "Fog Of War", ConfigurationItem.ConfigItemType.BOOLEAN, true);
        dynamicVariables.put("fogOfWar", configItem);
        configItem = new ConfigurationItem("fKillMessage", "Announce Friendly Fire messages to", ConfigurationItem.ConfigItemType.CHATRECIEPIENTS, ConfigurationItem.ChatReciepients.ALL);
        dynamicVariables.put("fKillMessage", configItem);
        configItem = new ConfigurationItem("debugLevel", "Log Detail (0, 1, 2)", ConfigurationItem.ConfigItemType.INTEGER, 1, 0, 2);
        dynamicVariables.put("debugLevel", configItem);
        configItem = new ConfigurationItem("logChat", "Log Chat Messages", ConfigurationItem.ConfigItemType.BOOLEAN, true);
        dynamicVariables.put("logChat", configItem);
        configItem = new ConfigurationItem("logAdmin", "Log Admin Commands", ConfigurationItem.ConfigItemType.BOOLEAN, true);
        dynamicVariables.put("logAdmin", configItem);
        configItem = new ConfigurationItem("logIPAccess", "Log Connections", ConfigurationItem.ConfigItemType.BOOLEAN, true);
        dynamicVariables.put("logIPAccess", configItem);
        configItem = new ConfigurationItem("missionEndTimer", "Mission End Timer Interval(Seconds)", ConfigurationItem.ConfigItemType.STRING, "60,30,15");
        dynamicVariables.put("missionEndTimer", configItem);
        configItem = new ConfigurationItem("missionCycle", "Mission Cycle", ConfigurationItem.ConfigItemType.MISSIONCYCLE, "?");
        dynamicVariables.put("missionCycle", configItem);
        configItem = new ConfigurationItem("recordStats", "Auto Start Stats", ConfigurationItem.ConfigItemType.BOOLEAN, false);
        dynamicVariables.put("recordStats", configItem);
        configItem = new ConfigurationItem("autoKickTimer", "Plane Limit/Loadout Kick Timer (Seconds)", ConfigurationItem.ConfigItemType.INTEGER, 10, 0, 1000);
        dynamicVariables.put("autoKickTimer", configItem);

        configItem = new ConfigurationItem("deathKick", "Death Kick (0=Off)", ConfigurationItem.ConfigItemType.INTEGER, 0, 0, 1000);
        dynamicVariables.put("deathKick", configItem);
        configItem = new ConfigurationItem("deathKickTime", "Death Kick Duration (In minutes)", ConfigurationItem.ConfigItemType.INTEGER, 3, 0, 1000);
        dynamicVariables.put("deathKickTime", configItem);
        configItem = new ConfigurationItem("badLanguageKick", "Bad Language Kick(0=Off)", ConfigurationItem.ConfigItemType.INTEGER, 0, 0, 1000);
        dynamicVariables.put("badLanguageKick", configItem);
        configItem = new ConfigurationItem("badSpawnKick", "Autokick on Bad Spawn", ConfigurationItem.ConfigItemType.BOOLEAN, true);
        dynamicVariables.put("badSpawnKick", configItem);
        configItem = new ConfigurationItem("shotdownMsg", "Announce Shotdown Message", ConfigurationItem.ConfigItemType.CHATRECIEPIENTS, ConfigurationItem.ChatReciepients.NONE);
        dynamicVariables.put("shotdownMsg", configItem);
        configItem = new ConfigurationItem("logBaseAttack", "Log Attacks Near Aerodrome", ConfigurationItem.ConfigItemType.BOOLEAN, false);
        dynamicVariables.put("logBaseAttack", configItem);
        configItem = new ConfigurationItem("voteTime", "Mission Vote time 0=Off (In minutes)", ConfigurationItem.ConfigItemType.INTEGER, 0, 0, 100);
        dynamicVariables.put("voteTime", configItem);
        configItem = new ConfigurationItem("useWarningPoints", "Use Warning Points", ConfigurationItem.ConfigItemType.BOOLEAN, false);
        dynamicVariables.put("useWarningPoints", configItem);
        configItem = new ConfigurationItem("welcomeMessage", "Pilot Welcome Message", ConfigurationItem.ConfigItemType.STRING, "Welcome {pilotName} from {city}, {country}");
        dynamicVariables.put("welcomeMessage", configItem);
        configItem = new ConfigurationItem("sendWelcomeMessageTo", "Send Welcome Message to", ConfigurationItem.ConfigItemType.CHATRECIEPIENTS, ConfigurationItem.ChatReciepients.ALL);
        dynamicVariables.put("sendWelcomeMessageTo", configItem);

    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigDirectory() {
        return "." + File.separatorChar + "config" + File.separatorChar + configName + File.separatorChar;
    }

    public int getVoteTime() {
        return (Integer) dynamicVariables.get("voteTime").getValue();
    }

    public boolean getCountFAirKillonGround() {
        return (Boolean) dynamicVariables.get("fKillAirBanCount").getValue();
    }

    public int getFriendlyAirBan() {
        return (Integer) dynamicVariables.get("fKillAirBan").getValue();
    }

    public double getFKillBanDuration() {
        return (Double) dynamicVariables.get("fKillBanZDuration").getValue();
    }

    public void setFKillBanDuration(double killBanDuration) {
        dynamicVariables.get("fKillBanDuration").setValue(killBanDuration);
    }

    public int getFriendlyGroundBan() {
        return (Integer) dynamicVariables.get("fKillGroundBan").getValue();
    }

    public boolean isFogOfWar() {
        return (Boolean) dynamicVariables.get("fogOfWar").getValue();
    }

    public String getMissionEndTimer() {
        return (String) dynamicVariables.get("missionEndTimer").getValue();
    }

    public String getServerDirectory() {
        return (String) staticVariables.get("serverDirectory").getValue();
    }

    public String getServerIP() {
        return (String) staticVariables.get("serverIP").getValue();
    }

    public int getServerPort() {
        return (Integer) staticVariables.get("serverPort").getValue();
    }

    public String getEventLog() {
        return (String) staticVariables.get("serverEventLog").getValue();
    }

    public int getLogDebugLevel() {
        return (Integer) dynamicVariables.get("debugLevel").getValue();
    }

    public boolean isLogChat() {
        return (Boolean) dynamicVariables.get("logChat").getValue();
    }

    public boolean isLogAdmin() {
        return (Boolean) dynamicVariables.get("logAdmin").getValue();
    }

    public String getMissionCycle() {
        return (String) dynamicVariables.get("missionCycle").getValue();
    }

    public void setMissionCycle(String missionCycle) {
        dynamicVariables.get("missionCycle").setValue(missionCycle);
    }

    public boolean isRecordStats() {
        return (Boolean) dynamicVariables.get("recordStats").getValue();
    }

    public String getStatsIP() {
        return (String) staticVariables.get("statsIP").getValue();
    }

    public int getStatsPort() {
        return (Integer) staticVariables.get("statsPort").getValue();
    }

    public String getStatsDBName() {
        return (String) staticVariables.get("statsDBName").getValue();
    }

    public String getStatsUserName() {
        return (String) staticVariables.get("statsDB01Username").getValue();
    }

    public String getStatsUserPassword() {
        return (String) staticVariables.get("statsDB02Password").getValue();
    }

    public boolean isAutoStart() {
        return (Boolean) staticVariables.get("autoStart").getValue();
    }

    public ConfigurationItem.ChatReciepients getAnnounceFriendlyFire() {
        return (ConfigurationItem.ChatReciepients) dynamicVariables.get("fKillMessage").getValue();
    }

    public ConfigurationItem.ChatReciepients getAnnounceShotdownMsg() {
        return (ConfigurationItem.ChatReciepients) dynamicVariables.get("shotdownMsg").getValue();
    }

    public boolean isLogBaseAttack() {
        return (Boolean) dynamicVariables.get("logBaseAttack").getValue();
    }

    public boolean isLogIpAccess() {
        return (Boolean) dynamicVariables.get("logIPAccess").getValue();
    }

    public boolean enablePingKick() {
        return (Boolean) staticVariables.get("enablePingKick").getValue();
    }

    public int getMaxPing() {
        return (Integer) pingSettings.get("maxPing").getValue();
    }

    public int getPingDelay() {
        return (Integer) pingSettings.get("pingDelay").getValue();
    }

    public int getPingWarnings() {
        return (Integer) pingSettings.get("warnings").getValue();
    }

    public int getAutoKickTimer() {
        return (Integer) dynamicVariables.get("autoKickTimer").getValue();
    }

    public boolean getResumeMissionCycle() {
        return (Boolean) staticVariables.get("resumeMissionCycle").getValue();
    }

    public HashMap<String, ConfigurationItem> getPingSettings() {
        return pingSettings;
    }

    public void setPingSettings(HashMap<String, ConfigurationItem> pingSettings) {
        this.pingSettings = pingSettings;
    }

    public HashMap<String, ConfigurationItem> getIconSettings() {
        return iconSettings;
    }

    public void setIconSettings(HashMap<String, ConfigurationItem> iconSettings) {
        this.iconSettings = iconSettings;
    }

    public HashMap<String, ConfigurationItem> getDynamicVariables() {
        return dynamicVariables;
    }

    public void setDynamicVariables(HashMap<String, ConfigurationItem> dynamicVariables) {
        this.dynamicVariables = dynamicVariables;
    }

    public HashMap<String, ConfigurationItem> getStaticVariables() {
        return staticVariables;
    }

    public void setStaticVariables(HashMap<String, ConfigurationItem> staticVariables) {
        this.staticVariables = staticVariables;
    }

    public int getDeathKick() {
        return (Integer) dynamicVariables.get("deathKick").getValue();
    }

    public int getDeathKickTime() {
        return (Integer) dynamicVariables.get("deathKickTime").getValue();
    }

    public int getBadLanguageKick() {
        return (Integer) dynamicVariables.get("badLanguageKick").getValue();
    }

    public boolean isBadSpawnKick() {
        return (Boolean) dynamicVariables.get("badSpawnKick").getValue();
    }

    public String getAnnouncePlanePilotLosses() {
        Object test = staticVariables.get("pilotPlaneLossesMessage").getValue();
        return test.toString();
    }

    public int getPlanePilotLossLimit() {
        return (Integer) staticVariables.get("pilotPlaneLossesLimit").getValue();
    }

    public String getPlanePilotLossWarning() {
        Object test = staticVariables.get("pilotPlaneLossWarningMessage").getValue();
        return test.toString();
    }

    public String getPlanePilotLossWarningInterval() {
        return (String) staticVariables.get("pilotPlaneLossWarningInterval").getValue();
    }

    public ArrayList<Integer> getMissionEndTimerInterval() {
        return missionEndTimerInterval;
    }

    public void setMissionEndTimerInterval(ArrayList<Integer> missionEndTimerInterval) {
        this.missionEndTimerInterval = missionEndTimerInterval;
    }

    public boolean isWarningPoints() {
        return (Boolean) dynamicVariables.get("useWarningPoints").getValue();
    }

    public String getWelcomeMessage() {
        return (String) dynamicVariables.get("welcomeMessage").getValue();
    }

    public ConfigurationItem.ChatReciepients getSendWelcomeMessageTo() {
        return (ConfigurationItem.ChatReciepients) dynamicVariables.get("sendWelcomeMessageTo").getValue();
    }

}
