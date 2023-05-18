package com.maddox.il2.game;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.ZutiSupportMethods_FM;
import com.maddox.il2.objects.buildings.House;

public class ZutiAirfieldPoint {
    private double    x1         = 0.0D;
    private double    y1         = 0.0D;
    private double    x2         = 0.0D;
    private double    y2         = 0.0D;
    private double    friction   = 3.8D;
    private ArrayList rrrObjects = new ArrayList();

    public ZutiAirfieldPoint(double x1, double y1, double x2, double y2, double friction) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        this.friction = friction;
    }

    public double isInZAPArea(double in_x, double in_y) {
        if (in_x >= this.x1 && in_x <= this.x2 && in_y <= this.y1 && in_y >= this.y2) return this.friction;

        return -1.0D;
    }

    // Called from: HouseManager
    public void addRRRObject(House fuelTank) {
        Point3d point3d = fuelTank.pos.getAbsPoint();
        if (point3d.x >= this.x1 && point3d.x <= this.x2 && point3d.y <= this.y1 && point3d.y >= this.y2) this.rrrObjects.add(fuelTank);
    }

    // Called from: Gears
    public float getRefuelingTimePenalty(Point3d point3d) {
        float closestFuelTank = -1.0F;

        // Only check for specialized objects if gears are on ZAP
        if (ZutiSupportMethods_FM.isFMOnZAP(World.getPlayerFM())) for (int i = 0; i < this.rrrObjects.size(); i++) {
            House fuelTank = (House) this.rrrObjects.get(i);

            if (!fuelTank.getDiedFlag() && fuelTank.name() != null && ZutiSupportMethods.isFuelTankObject(fuelTank.name())) {
                Point3d tankPoint = fuelTank.pos.getAbsPoint();
                int tmpDistance = (int) Math.sqrt(Math.pow(point3d.x - tankPoint.x, 2) + Math.pow(point3d.y - tankPoint.y, 2));

                if (tmpDistance < closestFuelTank || closestFuelTank == -1) closestFuelTank = tmpDistance;
            }
        }

        if (closestFuelTank == -1) {
            // Check if we are on a carrier : return 1.1F
            if (isLocatedOnCarrier(point3d)) return 1.1F;

            // If it's not on the carrier, check if it is ono simulated runway...
            if (isLocatedTestRunway(point3d)) return 1.5F;
        }

        System.out.println("Closest fuel tank is " + closestFuelTank + "m away.");

        if (closestFuelTank == -1.0F) return closestFuelTank;
        if (closestFuelTank < 200.1F) return 1.0F;
        if (closestFuelTank < 400.1F) return 1.2F;
        if (closestFuelTank < 600.1F) return 1.4F;
        if (closestFuelTank < 800.1F) return 1.6F;
        if (closestFuelTank < 1000.1F) return 1.8F;

        // If pilot parked at a distance greater than 250m... add max time penalty
        return 2.0F;
    }

    // Called from: Gears
    public float getRearmingTimePenalty(Point3d point3d) {
        float closestAmmoBox = -1.0F;

        // Only check for specialized objects if gears are on ZAP
        if (ZutiSupportMethods_FM.isFMOnZAP(World.getPlayerFM())) for (int i = 0; i < this.rrrObjects.size(); i++) {
            House ammoBox = (House) this.rrrObjects.get(i);
            if (!ammoBox.getDiedFlag() && ammoBox.name() != null && ZutiSupportMethods.isAmmoBoxObject(ammoBox.name())) {
                Point3d tankPoint = ammoBox.pos.getAbsPoint();
                int tmpDistance = (int) Math.sqrt(Math.pow(point3d.x - tankPoint.x, 2) + Math.pow(point3d.y - tankPoint.y, 2));

                if (tmpDistance < closestAmmoBox || closestAmmoBox == -1) closestAmmoBox = tmpDistance;
            }
        }

        if (closestAmmoBox == -1) {
            // Check if we are on a carrier : return 1.1F
            if (isLocatedOnCarrier(point3d)) return 1.1F;

            // If it's not on the carrier, check if it is ono simulated runway...
            if (isLocatedTestRunway(point3d)) return 1.5F;
        }

        System.out.println("Closest ammo box is " + closestAmmoBox + "m away.");

        if (closestAmmoBox == -1.0F) return closestAmmoBox;
        if (closestAmmoBox < 200.1F) return 1.0F;
        if (closestAmmoBox < 400.1F) return 1.2F;
        if (closestAmmoBox < 600.1F) return 1.4F;
        if (closestAmmoBox < 800.1F) return 1.6F;
        if (closestAmmoBox < 1000.1F) return 1.8F;

        // If pilot parked at a distance greater than 250m... add max time penalty
        return 2.0F;
    }

    // Called from: Gears
    public float getRepairingTimePenalty(Point3d point3d) {
        float closestWorkshop = -1.0F;

        // Only check for specialized objects if gears are on ZAP
        if (ZutiSupportMethods_FM.isFMOnZAP(World.getPlayerFM())) for (int i = 0; i < this.rrrObjects.size(); i++) {
            House workshop = (House) this.rrrObjects.get(i);
            if (!workshop.getDiedFlag() && workshop.name() != null && ZutiSupportMethods.isWorkshopObject(workshop.name())) {
                Point3d workshopPoint = workshop.pos.getAbsPoint();
                int tmpDistance = (int) Math.sqrt(Math.pow(point3d.x - workshopPoint.x, 2) + Math.pow(point3d.y - workshopPoint.y, 2));

                if (tmpDistance < closestWorkshop || closestWorkshop == -1) closestWorkshop = tmpDistance;
            }
        }

        if (closestWorkshop == -1) {
            // Check if we are on a carrier : return 1.1F
            if (isLocatedOnCarrier(point3d)) return 1.1F;

            // If it's not on the carrier, check if it is ono simulated runway...
            if (isLocatedTestRunway(point3d)) return 1.5F;
        }

        System.out.println("Closest workshop is " + closestWorkshop + "m away.");

        if (closestWorkshop == -1.0F) return closestWorkshop;
        if (closestWorkshop < 200.1F) return 1.0F;
        if (closestWorkshop < 400.1F) return 1.2F;
        if (closestWorkshop < 600.1F) return 1.4F;
        if (closestWorkshop < 800.1F) return 1.6F;
        if (closestWorkshop < 1000.1F) return 1.8F;

        // If pilot parked at a distance greater than 250m... add max time penalty
        return 2.0F;
    }

    public static boolean isLocatedOnCarrier(Point3d point3d) {
        for (int i = 0; i < Main.cur().mission.actors.size(); i++) {
            Actor actor = (Actor) Main.cur().mission.actors.get(i);

            if (actor instanceof com.maddox.il2.objects.ships.BigshipGeneric && (actor.toString().indexOf("CV") > 0 || actor.toString().indexOf("Carrier") > 0) && actor.name().indexOf("Chief") > 0) {
                // ok, check if the location is near enough (radius of 150m max!)
                double d = 150 * 150;
                double x = actor.pos.getAbsPoint().x;
                double y = actor.pos.getAbsPoint().y;
                double d_22_ = (x - point3d.x) * (x - point3d.x) + (y - point3d.y) * (y - point3d.y);
                if (d_22_ <= d) // System.out.println("Point stay position: " + point_stay.toString());
                    return true;
            }
        }

        return false;
    }

    /**
     * Check if aircraft is test runway.
     *
     * @param point3d
     * @return
     */
    public static boolean isLocatedTestRunway(Point3d point3d) {
        for (int i = 0; i < Main.cur().mission.actors.size(); i++) {
            Actor actor = (Actor) Main.cur().mission.actors.get(i);
            // System.out.println(actor.toString());

            if (actor instanceof com.maddox.il2.objects.ships.BigshipGeneric && actor.toString().indexOf("Ship$Rwy") > 0) {
                // ok, check if the location is near enough (radius of 150m max!)
                double d = 1000 * 1000;
                double x = actor.pos.getAbsPoint().x;
                double y = actor.pos.getAbsPoint().y;
                double d_22_ = (x - point3d.x) * (x - point3d.x) + (y - point3d.y) * (y - point3d.y);
                if (d_22_ <= d) // System.out.println("Point stay position: " + point_stay.toString());
                    return true;
            }
        }
        return false;
    }
}