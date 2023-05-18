package model;

public class Location {

    double locationX        = 0.0;
    double locationY        = 0.0;
    String mapGridReference = "Unknown";

    public String getMapGridReference() {
        return mapGridReference;
    }

    public void setMapGridReference(String mapGridReference) {
        this.mapGridReference = mapGridReference;
    }

    public double getLocationX() {
        return locationX;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }

}
