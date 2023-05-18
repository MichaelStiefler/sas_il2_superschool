package model;

import java.io.Serializable;
import java.util.HashMap;

public class DifficultySettings implements Serializable {
    private static final long        serialVersionUID = 1L;

    private String                   difficultyName   = "Default";
    private HashMap<String, Boolean> settings;

    public DifficultySettings(String name) {
        this.difficultyName = name;
        settings = new HashMap<String, Boolean>();
        settings.put("SeparateEStart", true);
        settings.put("ComplexEManagement", true);
        settings.put("TorqueGyroEffects", true);
        settings.put("FlutterEffect", true);
        settings.put("WindTurbulence", true);
        settings.put("StallsSpins", true);
        settings.put("Vulnerability", true);
        settings.put("BlackoutsRedouts", true);
        settings.put("RealisticGunnery", true);
        settings.put("LimitedAmmo", true);
        settings.put("LimitedFuel", true);
        settings.put("CockpitAlwaysOn", true);
        settings.put("NoOutsideViews", true);
        settings.put("HeadShake", true);
        settings.put("NoIcons", true);
        settings.put("NoPadlock", true);
        settings.put("Clouds", true);
        settings.put("NoInstantSuccess", true);
        settings.put("TakeoffLanding", true);
        settings.put("RealisticLandings", true);
        settings.put("NoMapIcons", true);
        settings.put("NoMinimapPath", true);
        settings.put("NoSpeedBar", true);
        settings.put("EngineOverheat", true);
        settings.put("GLimits", true);
        settings.put("Reliability", true);
        settings.put("RealisticPilotVulnerability", true);
        settings.put("NoPlayerIcon", true);
        settings.put("NoFogOfWarIcons", true);
        settings.put("RealisticNavigationInstruments", true);
//		settings.put("BombFuzes", true);
//		settings.put("FragileTorps", true);
//		settings.put("NoEnemyViews", true);
//		settings.put("NoFriendlyViews", true);
//		settings.put("NoSeaUnitViews", true);
//		settings.put("NoAircraftViews", true);
//		settings.put("NoOwnPlayerViews", true);
//		settings.put("RealisticRocketSpread", true);
//		settings.put("SharedKills", true);
//		settings.put("SharedKillsHistorical", true);
//		settings.put("No_GroundPadlock", true);

    }

    public String getDifficultyName() {
        return difficultyName;
    }

    public void setDifficultyName(String difficultyName) {
        this.difficultyName = difficultyName;
    }

    public HashMap<String, Boolean> getSettings() {
        return settings;
    }

    public void setSettings(HashMap<String, Boolean> settings) {
        this.settings = settings;
    }

    public void setSetting(String setting, boolean value) {
        if (settings.containsKey(setting)) {
            settings.put(setting, value);
        }
    }
}
