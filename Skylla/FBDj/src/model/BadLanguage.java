package model;

import java.util.HashMap;

public class BadLanguage {

    private HashMap<String, String> badWordList;
    private long                    lastUpdateTime;
    private String                  fileName;

    public BadLanguage(String fileName) {
        this.badWordList = null;
        this.lastUpdateTime = 0;
        this.fileName = fileName;
    }

    public HashMap<String, String> getBadWordList() {
        return badWordList;
    }

    public void setBadWordList(HashMap<String, String> badWordList) {
        this.badWordList = badWordList;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
