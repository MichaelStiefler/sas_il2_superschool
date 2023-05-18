package model;

import java.util.ArrayList;
import java.util.HashMap;

public class IL2PlaneLoadout {
    private static HashMap<String, ArrayList<Weapon>> planeLoadouts;

    public static HashMap<String, ArrayList<Weapon>> getPlaneLoadouts() {
        return planeLoadouts;
    }

    public static void setPlaneLoadouts(HashMap<String, ArrayList<Weapon>> planeLoadouts) {
        IL2PlaneLoadout.planeLoadouts = planeLoadouts;
    }

}
