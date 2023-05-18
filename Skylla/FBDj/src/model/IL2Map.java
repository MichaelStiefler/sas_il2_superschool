package model;

/*
 * Stores maps and their sizes
 */

public class IL2Map {
    private String  name;
    private String  displayName;
    private boolean bigMap;
    private long    xOffset = 0;
    private long    yOffset = 0;

    public IL2Map(String name, boolean bigMap, String displayName) {
        this.name = name;
        this.displayName = displayName;
        this.bigMap = bigMap;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    public boolean isBigMap() {
        return bigMap;
    }

    public long getXOffset() {
        return xOffset;
    }

    public void setXOffset(long offset) {
        xOffset = offset;
    }

    public long getYOffset() {
        return yOffset;
    }

    public void setYOffset(long offset) {
        yOffset = offset;
    }

}
