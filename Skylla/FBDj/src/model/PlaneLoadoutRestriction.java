package model;

import java.io.Serializable;

/*
 * Stores Restricted Plane Weapon Loadouts.
 */
public class PlaneLoadoutRestriction implements Serializable {
    private static final long serialVersionUID = 1L;

    private int               army;
    private String            plane;
    private String            weapon;

    public PlaneLoadoutRestriction(int army, String plane, String weapon) {
        this.army = army;
        this.plane = plane;
        this.weapon = weapon;
    }

    public int getArmy() {
        return army;
    }

    public String getPlane() {
        return plane;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }
}
