package model;

public class AIWing {

    private String name;
    private int    army;
    private String planeClass;
    private String plane;
    private String fuel;
    private String weapons;
    private int    planeCount;
    private int    skillLevel;

    public AIWing(String name) {
        this.name = name;
        this.army = 0;
        this.planeClass = "Unknown";
        this.plane = "Unknown";
        this.fuel = "Unknown";
        this.weapons = "Unknown";
        this.planeCount = 0;
        this.skillLevel = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaneClass() {
        return planeClass;
    }

    public void setPlaneClass(String planeClass) {
        this.planeClass = planeClass;
    }

    public String getPlane() {
        return plane;
    }

    public void setPlane(String plane) {
        this.plane = plane;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getWeapons() {
        return weapons;
    }

    public void setWeapons(String weapons) {
        this.weapons = weapons;
    }

    public int getPlaneCount() {
        return planeCount;
    }

    public void setPlaneCount(int planeCount) {
        this.planeCount = planeCount;
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public int getArmy() {
        return army;
    }

    public void setArmy(int army) {
        this.army = army;
    }

}
