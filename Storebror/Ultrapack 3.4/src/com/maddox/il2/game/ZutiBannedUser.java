package com.maddox.il2.game;

import com.maddox.rts.Time;

public class ZutiBannedUser {
    private String name;
    private String IP;
    private long   duration;

    public ZutiBannedUser() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getIP() {
        return this.IP;
    }

    public void setIP(String value) {
        this.IP = value;
    }

    public void setDuration(long value) {
        this.duration = value;
    }

    public long getDuration() {
        return this.duration;
    }

    public boolean isMatch(String inName, String inIP) {
        if (this.name.trim().equalsIgnoreCase(inName.trim()) && this.IP.trim().equalsIgnoreCase(inIP.trim())) return true;

        return false;
    }

    public boolean isBanned() {
        if (Time.current() < this.duration) return true;

        return false;
    }
}