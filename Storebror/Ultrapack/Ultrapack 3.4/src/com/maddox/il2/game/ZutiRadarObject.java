package com.maddox.il2.game;

import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Property;

public class ZutiRadarObject {
    public static final String ZUTI_RADAR_OBJECT_NAME = "Radar";
    private int                type;
    private double             range;
    private int                minHeight;
    private int                maxHeight;
    private Point3d            position;
    private Actor              owner;

    /**
     * Constructor. Sets also army depending on owner army. For type 1 radars, army has to be set separately.
     *
     * @param owner
     * @param type: 1 = house/ground radar; 2 = ship radar; 3 = scout radar
     */
    public ZutiRadarObject(Actor owner, int type) {
        this.owner = owner;
        this.type = type;
        this.position = owner.pos.getAbsPoint();
    }

    /**
     * @return 1 = hose/ground radar; 2 = ship radar; 3 = scout radar
     */
    public int getType() {
        return this.type;
    }

    /**
     * @return range in kilometers. For type 3 objects (scouts), range is calculated by multiplying object height with selected alpha value. Returned value is ^2 because of easier comparison later on (no sqrt needed).
     */
    public double getRange() {
        if (this.type == 3) // Scouts have different ground range. Because this result is in meters, divide by 1000.
            return Math.pow(this.position.z * this.range / 1000, 2);
        else return this.range;
    }

    /**
     * @param range value in kilometers. For type 3 objects (scouts) this represents selected alpha value.
     */
    public void setRange(double range) {
        if (this.type == 3) // Scouts have different ground range...
            this.range = range;
        else this.range = Math.pow(range, 2);
    }

    /**
     * @return minimum height in meters. For type 3 objects (scouts) this represents delta height in which scouts can scan the air. It is calculated by taking current owner height and subtracting delta height.
     */
    public int getMinHeight() {
        if (this.type == 3) // Scouts...
            return (int) (this.position.z - this.minHeight);
        else return this.minHeight;
    }

    /**
     * @param minHeight value in meters. For type 3 objects (scouts) this represents delta height in which scouts can scan the air.
     */
    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    /**
     * @return maximum height in meters. For type 3 objects (scouts) this represents delta height in which scouts can scan the air. It is calculated by taking current owner height and adding delta height.
     */
    public int getMaxHeight() {
        if (this.type == 3) // Scouts...
            return (int) (this.position.z - this.maxHeight);
        else return this.maxHeight;
    }

    /**
     * @param maxHeight value in meters
     */
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    /**
     * Get radar position
     *
     * @return 3d point
     */
    public Point3d getPosition() {
        return this.position;
    }

    /**
     * Get radar status
     *
     * @return true if radar is not destroyed, else false.
     */
    public boolean isAlive() {
        if (this.owner == null) return false;
        else return !this.owner.getDiedFlag();
    }

    private Actor getOwner() {
        return this.owner;
    }

    /**
     * Returns radar base actor name.
     *
     * @return
     */
    public String getName() {
        if (this.owner != null) return this.owner.name() + "@" + this.hashCode();

        return "NONAME@" + this.hashCode();
    }

    public boolean equals(Object o) {
        if (!(o instanceof ZutiRadarObject)) return false;

        ZutiRadarObject inKeyObject = (ZutiRadarObject) o;
        if (this.owner.equals(inKeyObject.getOwner())) return true;

        return false;
    }

    public int hashCode() {
        return this.owner.hashCode();
    }

    /**
     * Call this method if you want to know if radar covers imputed coordinates or not.
     *
     * @param point
     * @return
     */
    public boolean isInRange(Point3d point, boolean isGroundUnit, boolean isTarget) {
        double range = this.getRange();
        double tmpDistance = (Math.pow(this.position.x - point.x, 2) + Math.pow(this.position.y - point.y, 2)) / 1000000; // 1000000 = 1000m * 1000m -> we need kilometers
        // System.out.println("ZutiRadarObject - tmpDistance = " + tmpDistance + " vs range " + range + ", Height: " + point.z + " vs MIN: " + getMinHeight() + ", MAX " + getMaxHeight());

        if (isGroundUnit && !isTarget && tmpDistance <= range) return true;

        if (tmpDistance <= range && point.z >= this.getMinHeight() && point.z <= this.getMaxHeight()) return true;

        return false;
    }

    /**
     * Check actor against players army scout classes
     *
     * @param actor
     * @param playerArmy
     * @return true if actor is scout, else false
     */
    public static boolean isPlayerArmyScout(Actor actor, int playerArmy) {
        if (actor == null || !(actor instanceof SndAircraft) || actor.getArmy() != playerArmy) return false;

        List scouts = new ArrayList();
        if (playerArmy == 1) scouts = Mission.MDS_VARIABLES().scoutsRed;
        else if (playerArmy == 2) scouts = Mission.MDS_VARIABLES().scoutsBlue;

        String currentAcName = Property.stringValue(((Aircraft) actor).getClass(), "keyName");
        int size = scouts.size();
        for (int i = 0; i < size; i++) {
            String str1 = (String) scouts.get(i);
            if (currentAcName.indexOf(str1) > -1) return true;
        }

        return false;
    }
}