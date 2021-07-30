package com.maddox.il2.objects.ships;

import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Front.Marker;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Point_Stay;
import com.maddox.il2.builder.ZutiSupportMethods_Builder;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.ZutiStayPoint;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Time;

public class ZutiSupportMethods_Ships {
    /**
     * When ship moves it also needs to update its born place position.
     */
    public static void refreshShipsBornPlacePosition(BigshipGeneric ship) {
        if (ship.zutiBornPlace == null || ship.zutiIsClassBussy) return;

        ship.zutiIsClassBussy = true;
        /*
         * zutiReloadstayPointsCountDown += 30;
         *
         * if( zutiReloadstayPointsCountDown >= 30000 ) { //Reload stay points zutiAssignStayPointsToBp(); zutiReloadstayPointsCountDown = 0; //System.out.println(new Integer(zutiBornPlace.zutiBpStayPoints.size()).toString() +
         * " stay points reassigned to: " + this.toString()); }
         */
        if (ship.zutiGetDying() == 0) {
            com.maddox.JGP.Point3d point3d = ship.pos.getAbsPoint();
            ship.zutiBornPlace.place.set(point3d.x, point3d.y);

            if (ship.zutiBornPlace.zutiBpStayPoints != null) for (int i = 0; i < ship.zutiBornPlace.zutiBpStayPoints.size(); i++) {
                com.maddox.il2.game.ZutiStayPoint zutiPoint = (com.maddox.il2.game.ZutiStayPoint) ship.zutiBornPlace.zutiBpStayPoints.get(i);
                zutiPoint.PsVsShipRefresh(point3d.x, point3d.y, ship.initOr.getYaw());
                // System.out.println("Ship orientation: " + ship.initOr.toString() + ", ship position_ " + ship.pos.getAbsPoint().toString() + ", sp location: " + zutiPoint.pointStay.toString());
            }
        } else if (ship.zutiGetDying() > 0) {
            ZutiSupportMethods.removeBornPlace(ship.zutiBornPlace);
            ship.zutiBornPlace = null;
        }

        ship.zutiIsClassBussy = false;
    }

    /**
     * When ships move its front marker also needs to change position.
     *
     * @param died
     */
    public static void refreshShipsFrontMarkerPosition(BigshipGeneric ship, boolean died) {
        if (ship.zutiFrontMarkers == null) return;

        Marker marker = (Marker) ship.zutiFrontMarkers.get(0);

        if (marker == null) return;

        if (ship.zutiGetDying() == 0) {
            Point3d pos = ship.pos.getAbsPoint();
            marker.x = pos.x;
            marker.y = pos.y;

            com.maddox.il2.ai.Front.setMarkersChanged();
        } else {
            ZutiSupportMethods.removeFrontMarker(marker, 0);
            ship.zutiFrontMarkers.clear();
            ship.zutiFrontMarkers = null;
        }

        if (!ship.zutiHasFrontMarkerAssigned()) // System.out.println("Ships not carrying front marker: " + this.name());
            return;

        if (!died && Time.current() - ZutiSupportMethods.BASE_CAPRUTING_LAST_CHECK < ZutiSupportMethods.BASE_CAPTURING_INTERVAL) // System.out.println(Time.current() + " - " + ZutiSupportMethods.BASE_CAPRUTING_LAST_CHECK + " = " + (Time.current() -
                                                                                                                                 // ZutiSupportMethods.BASE_CAPRUTING_LAST_CHECK));
            return;

        // Check if new front position has overrun some home bases
        ZutiSupportMethods.checkIfAnyBornPlaceWasOverrun();

        // Set current time as last refresh time
        ZutiSupportMethods.BASE_CAPRUTING_LAST_CHECK = Time.current();
    }

    /**
     * This method searches born places and assigns nearest carrier to it.
     */
    public static void zutiAssignShipToBornPlaces() {
        List bornplaces = World.cur().bornPlaces;
        if (bornplaces == null || bornplaces.size() == 0) return;

        List bornPlacesToRemove = new ArrayList();

        BornPlace bp = null;
        for (int i = 0; i < bornplaces.size(); i++) {
            bp = (BornPlace) bornplaces.get(i);
            if (bp == null) continue;

            BigshipGeneric carrier = getNearestFriendlyAircraftCarrier(bp.place.x, bp.place.y, bp.r);
            if (carrier != null) {
                if (bp.zutiIsStandAloneBornPlace) {
                    bornPlacesToRemove.add(bp);
                    System.out.println("You are probably trying to load stand alone home base from mission to a carrier! Not possible.");
                    System.out.println("  Ignoring home base at x=" + (int) bp.place.x + ", y=" + (int) bp.place.y);
                    continue;
                }
                carrier.zutiBornPlace = bp;
                bp.zutiAlreadyAssigned = true;
                ZutiSupportMethods_Ships.assignShipsStayPointsToBornPlace(carrier);
            }
        }

        for (int i = 0; i < bornPlacesToRemove.size(); i++)
            World.cur().bornPlaces.remove(bornPlacesToRemove.get(i));
    }

    /**
     * Method returns aircraft carrier that is nearest to given coordinates.
     *
     * @param x
     * @param y
     * @return
     */
    private static BigshipGeneric getNearestFriendlyAircraftCarrier(double x, double y, float r) {
        ArrayList actors = Main.cur().mission.actors;
        int size = actors.size();
        double min = r * r;
        BigshipGeneric nearest = null;
        Actor actor = null;
        Point3d point3d = null;

        try {
            for (int i = 0; i < size; i++) {
                actor = (Actor) actors.get(i);

                if (actor instanceof ZutiTypeAircraftCarrier) {
                    point3d = actor.pos.getAbsPoint();

                    double tmpDistance = Math.pow(x - point3d.x, 2) + Math.pow(y - point3d.y, 2);
                    if (tmpDistance < min) {
                        min = tmpDistance;
                        nearest = (BigshipGeneric) actor;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return nearest;
    }

    /**
     * Assigns stay points to born place so that they can be refreshed as carrier moves
     */
    private static void assignShipsStayPointsToBornPlace(BigshipGeneric ship) {
        if (!(ship instanceof ZutiTypeAircraftCarrier)) return;

        if (ship.zutiBornPlace == null) return;

        // System.out.println("Checking spawn places for carrier >" + ship.toString() + "<");
        double x = ship.pos.getAbsPoint().x;
        double y = ship.pos.getAbsPoint().y;

        ship.zutiBornPlace.zutiBpStayPoints = new ArrayList();
        // Search radios as small as possible to dismiss the danger of capturing foreign born places
        double d = 150 * 150;
        Point_Stay[][] point_stays = World.cur().airdrome.stay;
        // i will be setting stay points on my own from now on... screw it
        ArrayList foundPoints = new ArrayList();

        for (int i_21_ = 0; i_21_ < point_stays.length; i_21_++)
            if (point_stays[i_21_] != null) {
                Point_Stay point_stay = point_stays[i_21_][point_stays[i_21_].length - 1];
                double d_22_ = (point_stay.x - x) * (point_stay.x - x) + (point_stay.y - y) * (point_stay.y - y);
                if (d_22_ <= d) foundPoints.add(point_stay);
            }

        // Return points starting from the last one that is the farthest from ships center
        int startIndex = foundPoints.size();
        // If by any means stupid mission builders fiddled with .mis file by hand and wanted to cheat... fix it
        if (ship.zutiBornPlace.zutiCarrierSpawnPlaces > startIndex) ship.zutiBornPlace.zutiCarrierSpawnPlaces = startIndex;
        // Get the mission selected spawn points for given bp on this carrier
        startIndex = startIndex - ship.zutiBornPlace.zutiCarrierSpawnPlaces;
        // System.out.println(" Number of set/enabled spawn points: " + ship.zutiBornPlace.zutiCarrierSpawnPlaces);
        // System.out.println(" Array start index is: " + startIndex);

        // Set points that we don't need for specific carrier to location that do not exist
        for (int i = 0; i < startIndex; i++) {
            Point_Stay point_stay = (Point_Stay) foundPoints.get(i);
            point_stay.set(-999999.99F, -999999.99F);
        }
        // Now, remove those points from global list
        ZutiSupportMethods_Ships.removeRedundantSpawnPlaces();

        if (startIndex < 0) {
            System.out.println("Ship >" + ship.name() + "< has no valid spawn places!");
            return;
        }

        // For stationary carriers do now mess with spawn place positions. Just reduce their numbers above.
        if (ship.name().endsWith("Static")) return;

        // Assign points that we will use to specified carrier
        for (int i = startIndex; i < foundPoints.size(); i++)
            try {
                ZutiStayPoint zutiSp = new ZutiStayPoint();
                zutiSp.pointStay = (Point_Stay) foundPoints.get(i);
                zutiSp.PsVsShip(x, y, (double) ship.initOr.getYaw(), i, (ZutiTypeAircraftCarrier) ship);

                if (ship.zutiBornPlace == null) return;

                ship.zutiBornPlace.zutiBpStayPoints.add(zutiSp);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        // Set number of stay points for this home base, since carriers can have that different than land based ones
        ship.zutiBornPlace.zutiNumberOfSpawnPlaces = foundPoints.size() - startIndex;
        // System.out.println(" Nr. of available stay points is: " + ship.zutiBornPlace.zutiNumberOfSpawnPlaces);
        // System.out.println("=================================================");
    }

    /**
     * Method will remove those spawn places that have x and y positions set to -999999.99F
     */
    private static void removeRedundantSpawnPlaces() {
        List allowedSpawnPlaces = new ArrayList();
        Point_Stay[][] points = World.cur().airdrome.stay;
        Point_Stay pointStay = null;
        for (int i = 0; i < points.length; i++) {
            pointStay = points[i][0];

            if (pointStay.zutiLocation != null) {
                // My spawn place - allow it always
                allowedSpawnPlaces.add(points[i]);
                continue;
            }

            if (pointStay.x != -999999.99F && pointStay.y != -999999.99F) // this spawn place can exist
                allowedSpawnPlaces.add(points[i]);
        }

        // Now reset original list
        Point_Stay[][] newStayPointsList = new Point_Stay[allowedSpawnPlaces.size()][];
        for (int i = 0; i < allowedSpawnPlaces.size(); i++)
            newStayPointsList[i] = (Point_Stay[]) allowedSpawnPlaces.get(i);

        World.cur().airdrome.stay = newStayPointsList;
    }

    /**
     * Method appends born place to big ship generic based on ships and born place location
     */
    public static void assignBornPlaceToShip(BigshipGeneric ship) {
        double x = ship.pos.getAbsPoint().x;
        double y = ship.pos.getAbsPoint().y;
        double min = ZutiSupportMethods_Builder.BORN_PLACE_RADIUS_CARRIERS;

        BornPlace bp = null;

        List arraylist = World.cur().bornPlaces;
        for (int i = 0; i < arraylist.size(); i++) {
            BornPlace bornPlace = (BornPlace) arraylist.get(i);
            if (bornPlace.zutiAlreadyAssigned) continue;
            if (bornPlace.army != ship.getArmy()) continue;

            double tmpDistance = Math.pow(bornPlace.place.x - x, 2) + Math.pow(bornPlace.place.y - y, 2);
            // System.out.println("Tank: " + tank.toString() + ", " + new Double(tmpDistance).toString() + " vs " + new Double(min).toString());
            if (tmpDistance < min) {
                min = tmpDistance;
                bp = bornPlace;
            }
        }
        if (min < ZutiSupportMethods_Builder.BORN_PLACE_RADIUS_CARRIERS) {
            ship.zutiBornPlace = bp;
            bp.zutiAlreadyAssigned = true;
            ZutiSupportMethods_Ships.assignShipsStayPointsToBornPlace(ship);
        }
    }

    /**
     * Method returns true if aircraft is positioned on carrier deck.
     *
     * @param aircraft
     * @param speedDifference: difference between carrier and AC speed
     * @return
     */
    public static boolean isAircraftOnDeck(Aircraft aircraft, double speedDifference) {
        if (aircraft == null || aircraft.FM == null || aircraft.FM.brakeShoeLastCarrier == null) return false;

        if (aircraft.FM.brakeShoeLastCarrier instanceof BigshipGeneric && aircraft.FM.Gears.isUnderDeck()) {
            BigshipGeneric bigshipgeneric = (BigshipGeneric) World.getPlayerFM().brakeShoeLastCarrier;
            Vector3d shipSpeedVector = new Vector3d();
            bigshipgeneric.getSpeed(shipSpeedVector);
            Vector3d playerSpeedVector = new Vector3d();
            aircraft.FM.getSpeed(playerSpeedVector);
            if (Math.abs(playerSpeedVector.length() - shipSpeedVector.length()) < speedDifference) return true;
        }
        return false;
    }
}
