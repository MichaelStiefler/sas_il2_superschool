package model;

import java.io.Serializable;
import java.util.HashMap;

import mainController.MainController;

public class Aerodrome implements Serializable {
    private static final long           serialVersionUID = 1L;

    private int                         army;
    private double                      aerodromeRadius;
    private double                      aerodromeLocationX;
    private double                      aerodromeLocationY;
    private String                      aerodromeMapGrid;
    private boolean                     baseOverrun;
    private HashMap<String, PlaneLimit> planes;
    private HashMap<String, PlaneLimit> overrunBluePlanes;
    private HashMap<String, PlaneLimit> overrunRedPlanes;

    public Aerodrome(int army, double airportRadius, double airportLocationX, double airportLocationY) {
        this.army = army;
        this.aerodromeRadius = airportRadius;
        this.aerodromeLocationX = airportLocationX;
        this.aerodromeLocationY = airportLocationY;
        this.planes = new HashMap<String, PlaneLimit>();
        this.overrunBluePlanes = new HashMap<String, PlaneLimit>();
        this.overrunRedPlanes = new HashMap<String, PlaneLimit>();
    }

    public int getArmy() {
        return army;
    }

    public double getAerodromeRadius() {
        return aerodromeRadius;
    }

    public void setAerodromeRadius(double aerodromeRadius) {
        this.aerodromeRadius = aerodromeRadius;
    }

    public double getAerodromeLocationX() {
        return aerodromeLocationX;
    }

    public void setAerodromeLocationX(double aerodromeLocationX) {
        this.aerodromeLocationX = aerodromeLocationX;
    }

    public double getAerodromeLocationY() {
        return aerodromeLocationY;
    }

    public void setAerodromeLocationY(double aerodromeLocationY) {
        this.aerodromeLocationY = aerodromeLocationY;
    }

    public void setArmy(int army) {
        this.army = army;
    }

    public String getAerodromeMapGrid() {
        return aerodromeMapGrid;
    }

    public void setAerodromeMapGrid(String aerodromeMapGrid) {
        this.aerodromeMapGrid = aerodromeMapGrid;
    }

    public HashMap<String, PlaneLimit> getPlanes() {
        return planes;
    }

    public void addPlane(String planeName, int inUseLimit, int totalLimit) {
        PlaneLimit planeSet = new PlaneLimit(planeName, inUseLimit, totalLimit);
        planes.put(planeName, planeSet);
    }

    public void addBaseOverrunPlane(int army, String planeName, int inUseLimit, int totalLimit) {
        try {
            PlaneLimit planeSet = new PlaneLimit(planeName, inUseLimit, totalLimit);
            if (army == MainController.REDARMY) {
                if (overrunRedPlanes == null) {
                    overrunRedPlanes = new HashMap<String, PlaneLimit>();
                }
                overrunRedPlanes.put(planeName, planeSet);
            } else if (army == MainController.BLUEARMY) {
                if (overrunBluePlanes == null) {
                    overrunBluePlanes = new HashMap<String, PlaneLimit>();
                }
                overrunBluePlanes.put(planeName, planeSet);
            }
        } catch (Exception ex) {
            MainController.writeDebugLogFile(1, "Aerodrome.addBaseOverrunPlane - Error unhandled Exception Army(" + army + ")Plane(" + planeName + ")");
        }
    }

    public boolean isBaseOverrun() {
        return baseOverrun;
    }

    public void setBaseOverrun(boolean baseOverrun) {
        this.baseOverrun = baseOverrun;
    }

    public HashMap<String, PlaneLimit> getOverrunBluePlanes() {
        return overrunBluePlanes;
    }

    public void setOverrunBluePlanes(HashMap<String, PlaneLimit> overrunBluePlanes) {
        this.overrunBluePlanes = overrunBluePlanes;
    }

    public HashMap<String, PlaneLimit> getOverrunRedPlanes() {
        return overrunRedPlanes;
    }

    public void setOverrunRedPlanes(HashMap<String, PlaneLimit> overrunRedPlanes) {
        this.overrunRedPlanes = overrunRedPlanes;
    }

    public void setPlanes(HashMap<String, PlaneLimit> planes) {
        this.planes = planes;
    }

}
