package com.maddox.il2.objects.effects;

import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ZutiSupportMethods_Engine;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiAirfieldPoint;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.ZutiSupportMethods_Net;
import com.maddox.rts.Time;

public class ZutiSupportMethods_Effects {
    /**
     * Dedicated crater object used for syncing purposes.
     *
     * @author Zuti
     *
     */
    public static class SyncingCrater {
        public long  expirationTime;
        public float size;
        public float x;
        public float y;
        public float z;
    }

    /**
     * Array that contains crater objects that need synchronization. Contains SyncingCrater objects!
     */
    private static List   CRATERS_FOR_SYNCING = new ArrayList();
    private static List   AIRPORTS            = null;
    private static Object EXPLOSION_AREA      = null;
    private static double AIRPORT_RADIUS      = 1000000.0D; // 1km*1km

    /**
     * Reset class variables
     */
    public static void resetClassVariables() {
        CRATERS_FOR_SYNCING.clear();
        AIRPORTS = null;
        EXPLOSION_AREA = null;
    }

    /**
     * Method returns crater visibility based on bomb size/weight.
     *
     * @param chargeMass
     * @return Time in seconds.
     */
    public static int getCraterTimeToLive(float chargeMass) {
        int result = 80;

        if (chargeMass <= 100) result = 80;
        else if (chargeMass <= 250) result = Mission.MDS_VARIABLES().zutiCraters_Bombs250_Visibility;
        else if (chargeMass <= 500) result = Mission.MDS_VARIABLES().zutiCraters_Bombs500_Visibility;
        else if (chargeMass <= 1000) result = Mission.MDS_VARIABLES().zutiCraters_Bombs1000_Visibility;
        else if (chargeMass <= 2000) result = Mission.MDS_VARIABLES().zutiCraters_Bombs2000_Visibility;
        else if (chargeMass <= 5000) result = Mission.MDS_VARIABLES().zutiCraters_Bombs5000_Visibility;
        else result = Mission.MDS_VARIABLES().zutiCraters_Bombs9999_Visibility;

        // System.out.println("ZSP - Crater time to live for bomb weight >" + bombWeight + "kg< is >" + result + "s<");

        return result;
    }

    /**
     * Sizes were taken/collected from Effects class.
     *
     * @param chargeMass
     * @return Size in feet.
     */
    public static float getCraterSize(float chargeMass) {
        float result = (float) (chargeMass * 0.0234);

        // 700feet wide crater was estimated for 1.2kt yield bomb (code name Uncle). FatMan is 1.3kt.
        if (result > 700.0F) result = 700.0F;
        // float result = (float)(chargeMass * 0.03);
        // System.out.println("ZSP - Crater size for bomb weight >" + bombWeight + "kg< is >" + result + "feet<.");

        return result;
    }

    /**
     * Is created crater set for syncing?
     *
     * @param chargeMass
     * @return True if bomb needs syncing. Else false.
     */
    public static boolean isSynced(float chargeMass) {
        if (chargeMass <= 100) return false;
        else if (chargeMass <= 250) return Mission.MDS_VARIABLES().zutiCraters_Bombs250_SyncOnline;
        else if (chargeMass <= 500) return Mission.MDS_VARIABLES().zutiCraters_Bombs500_SyncOnline;
        else if (chargeMass <= 1000) return Mission.MDS_VARIABLES().zutiCraters_Bombs1000_SyncOnline;
        else if (chargeMass <= 2000) return Mission.MDS_VARIABLES().zutiCraters_Bombs2000_SyncOnline;
        else if (chargeMass <= 5000) return Mission.MDS_VARIABLES().zutiCraters_Bombs5000_SyncOnline;
        else return Mission.MDS_VARIABLES().zutiCraters_Bombs9999_SyncOnline;
    }

    /**
     * Add crater to syncing list
     *
     * @param crater
     */
    public static void addCrater(SyncingCrater crater) {
        CRATERS_FOR_SYNCING.add(crater);
    }

    /**
     * Method returns list with only those craters that are alive.
     */
    public static List trimCratersList() {
        List expiredCraters = new ArrayList();
        long currentTime = Time.current();
        SyncingCrater crater = null;
        for (int i = 0; i < CRATERS_FOR_SYNCING.size(); i++) {
            crater = (SyncingCrater) CRATERS_FOR_SYNCING.get(i);
            if (crater.expirationTime < currentTime) expiredCraters.add(crater);
        }

        for (int i = 0; i < expiredCraters.size(); i++)
            CRATERS_FOR_SYNCING.remove(expiredCraters.get(i));

        return CRATERS_FOR_SYNCING;
    }

    /**
     * Check if explosion is on special area. These areas are: Airports (1km radius from center of runway, if no home base is placed over it), home bases, friction areas.
     *
     * @param x
     * @param y
     * @return
     */
    public static boolean isExplosionOnSpecialArea(double x, double y) {
        // Precondition: if onlyAreaHits is disabled, we log craters everywhere
        if (!Mission.MDS_VARIABLES().zutiCraters_OnlyAreaHits) return true;

        // Then, check if explosion happened on previous special area
        if (checkIfExplosionIsOnLastKnownSpecialArea(x, y)) return true;

        // Second, check if explosion is on born place area
        BornPlace bp = ZutiSupportMethods_Net.isOnBornPlace(x, y);
        if (bp != null) {
            ZutiSupportMethods_Effects.EXPLOSION_AREA = bp;
            return true;
        }

        // Next, check if explosion is on friction area
        // -------------------------------------------------------------------------
        List objects = ZutiSupportMethods_Engine.AIRFIELDS;
        int size = objects.size();
        ZutiAirfieldPoint zap = null;
        for (int i = 0; i < size; i++) {
            zap = (ZutiAirfieldPoint) objects.get(i);
            if (zap.isInZAPArea(x, y) > -1) {
                ZutiSupportMethods_Effects.EXPLOSION_AREA = zap;
                return true;
            }
        }
        // -------------------------------------------------------------------------

        // Last, check if it is on airport area
        // -------------------------------------------------------------------------
        if (ZutiSupportMethods_Effects.AIRPORTS == null) {
            ZutiSupportMethods_Effects.AIRPORTS = new ArrayList();
            World.getAirports(ZutiSupportMethods_Effects.AIRPORTS);
        }

        size = ZutiSupportMethods_Effects.AIRPORTS.size();
        for (int i = 0; i < size; i++) {
            Airport airport = (Airport) ZutiSupportMethods_Effects.AIRPORTS.get(i);
            Point3d airportPoint = airport.pos.getAbsPoint();

            double tmpDistance = Math.pow(x - airportPoint.x, 2) + Math.pow(y - airportPoint.y, 2);
            if (tmpDistance < ZutiSupportMethods_Effects.AIRPORT_RADIUS) {
                ZutiSupportMethods_Effects.EXPLOSION_AREA = airport;
                return true;
            }
        }
        // -------------------------------------------------------------------------

        return false;
    }

    /**
     * Check if perhaps explosion happened on last known special area.
     *
     * @param x
     * @param y
     * @return
     */
    private static boolean checkIfExplosionIsOnLastKnownSpecialArea(double x, double y) {
        if (ZutiSupportMethods_Effects.EXPLOSION_AREA == null) return false;

        if (ZutiSupportMethods_Effects.EXPLOSION_AREA instanceof BornPlace) {
            BornPlace bp = (BornPlace) ZutiSupportMethods_Effects.EXPLOSION_AREA;
            double radius = bp.r * bp.r;
            double tmpDistance = Math.pow(x - bp.place.x, 2) + Math.pow(y - bp.place.y, 2);
            if (tmpDistance < radius) return true;
        } else if (ZutiSupportMethods_Effects.EXPLOSION_AREA instanceof ZutiAirfieldPoint) {
            ZutiAirfieldPoint zap = (ZutiAirfieldPoint) ZutiSupportMethods_Effects.EXPLOSION_AREA;
            if (zap.isInZAPArea(x, y) > -1) return true;
        } else if (ZutiSupportMethods_Effects.EXPLOSION_AREA instanceof Airport) {
            Airport airport = (Airport) ZutiSupportMethods_Effects.EXPLOSION_AREA;
            Point3d airportPoint = airport.pos.getAbsPoint();

            double tmpDistance = Math.pow(x - airportPoint.x, 2) + Math.pow(y - airportPoint.y, 2);
            if (tmpDistance < ZutiSupportMethods_Effects.AIRPORT_RADIUS) return true;
        }

        return false;
    }
}
