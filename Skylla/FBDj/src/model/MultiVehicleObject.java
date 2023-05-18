package model;

import java.util.ArrayList;

public class MultiVehicleObject {

    private String                     name;
    private ArrayList<IL2StaticObject> vehicleList;

    public MultiVehicleObject(String name) {
        this.name = name;
        this.vehicleList = new ArrayList<IL2StaticObject>(10);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<IL2StaticObject> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(ArrayList<IL2StaticObject> vehicleList) {
        this.vehicleList = vehicleList;
    }

}
