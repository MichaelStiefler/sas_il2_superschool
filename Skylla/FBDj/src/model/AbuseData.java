package model;

import org.ini4j.Ini;
import org.json.JSONObject;

import mainController.LogController;
import mainController.MainController;

public class AbuseData extends JsonData {
    private double trustLevel = 0.0;
    private double abuseMin = 0.0;
    private double abuseMax = 1.0;
    private double weight = 1.0;
    
    public double getTrustLevel() {
        return trustLevel;
    }

    public double getWeight() {
        return weight;
    }
    
    public AbuseData(Ini.Section section, String ipAddress) {
        super(section, ipAddress);
        MainController.writeDebugLogFile(2, "AbuseData <Init>+");
        this.abuseMin = Double.parseDouble(section.get("abuse_min", "0"));
        this.abuseMax = Double.parseDouble(section.get("abuse_max", "1"));
        this.weight = Double.parseDouble(section.get("weight", "1"));
        JSONObject json = this.json;
        if (section.containsKey("abuse_data")) json = json.getJSONObject(section.get("abuse_data"));
        double temp = json.optDouble(section.get("abuse_field"), 0.0D);
        if (temp < abuseMin) temp = abuseMin;
        if (temp > abuseMax) temp = abuseMax;
        this.trustLevel = 1.0D - ((temp - abuseMin) / (abuseMax - abuseMin));
        LogController.writeIPLogFile(ipAddress + " " + section.getName() + " Trust Level: " + this.trustLevel);
        MainController.writeDebugLogFile(2, "AbuseData <Init>-");
    }

}
