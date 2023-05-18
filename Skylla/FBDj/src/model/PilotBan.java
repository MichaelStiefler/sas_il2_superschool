package model;

import java.io.Serializable;

/*
 * Contains banned players list
 * Bans players
 */

public class PilotBan implements Serializable {
    private static final long serialVersionUID = 1L;

    private String            name;
    private String            ipAddress;
    private String            banner;               // Admin who banned this person
    private long              timestamp;
    private long              duration;

    public PilotBan(String name, String ipAddress, String banner, long timestamp, long banDuration) {
        this.name = name;
        this.ipAddress = ipAddress;
        this.banner = banner;
        this.timestamp = timestamp;
        this.duration = banDuration;
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getBanner() {
        return banner;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getDuration() {
        return duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
