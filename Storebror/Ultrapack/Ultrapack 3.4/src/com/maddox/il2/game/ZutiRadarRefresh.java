package com.maddox.il2.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiTargetsSupportMethods;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.gui.GUI;
import com.maddox.il2.gui.GUIBriefing;
import com.maddox.il2.gui.ZutiSupportMethods_GUI;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.buildings.House;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.ships.ZutiTypeBigRadar;
import com.maddox.il2.objects.ships.ZutiTypeSmallRadar;
import com.maddox.rts.Time;

public class ZutiRadarRefresh {
    // ----------------------------------------------------------
    class ZutiRadar_CompareByRange implements Comparator {
        public int compare(Object o1, Object o2) {
            ZutiRadarObject oo1 = (ZutiRadarObject) o1;
            ZutiRadarObject oo2 = (ZutiRadarObject) o2;

            if (oo1.getRange() < oo2.getRange()) return 1;
            if (oo1.getRange() > oo2.getRange()) return -1;

            return 0;
        }
    }

    // ----------------------------------------------------------
    private static final int RADARS_COUNT_INTERVAL = 5000;
    private static final int RADARS_FIND_INTERVAL  = 30000;
    private static final int MODUS                 = 64;

    private float[]          tanAlfa               = { 0.577F,	// tan(30°)
            0.700F,	// tan(35°)
            0.839F,	// tan(40°)
            1.000F,	// tan(45°)
            1.192F,	// tan(50°)
            1.428F,	// tan(55°)
            1.732F,	// tan(60°)
            2.145F,	// tan(65°)
            2.747F,	// tan(70°)
            3.732F,	// tan(75°)
            5.671F	// tan(80°)
    };
    private List             radars;
    private long             countRadarsStartTime  = 0;
    private long             findRadarsStartTime   = 0;
    private long             startTime             = 0;
    private boolean          refreshIntervalSet    = false;
    private List             deadObjects           = null;

    // Use this static class in other classes to manage radars
    private static ZutiRadarRefresh ZUTI_RADARS = null;

    public ZutiRadarRefresh() {
        this.radars = new ArrayList();
        this.deadObjects = new ArrayList();

        this.refreshIntervalSet = Mission.MDS_VARIABLES().zutiRadar_RefreshInterval > 0;
    }

    /**
     * Sets current mission.
     */
    public static void setCurrentMission(Mission mission) {
        if (ZUTI_RADARS == null) ZUTI_RADARS = new ZutiRadarRefresh();

        if (mission != null) ZUTI_RADARS.refreshIntervalSet = Mission.MDS_VARIABLES().zutiRadar_RefreshInterval > 0;
    }

    /**
     * Reset radar refresh timers
     */
    public static void resetStartTimes() {
        if (ZUTI_RADARS == null) ZUTI_RADARS = new ZutiRadarRefresh();

        ZUTI_RADARS.startTime = 0;
        ZUTI_RADARS.countRadarsStartTime = 0;
        ZUTI_RADARS.findRadarsStartTime = 0;
    }

    /**
     * Executes these methods: - updatePadObjectsPositions(), - countRadarsForSides() and - executeVisibilityCheck().
     */
    public static void update() {
        if (ZUTI_RADARS == null) ZUTI_RADARS = new ZutiRadarRefresh();

        ZUTI_RADARS.findRadarsInterval();
        ZUTI_RADARS.countRadars();
        ZUTI_RADARS.updatePadObjectsPositions();
        ZUTI_RADARS.executeVisibilityCheck();
    }

    /**
     * Refresh pad objects positions. Executed always if radar refresh interval = 0, otherwise this method is executed as specified by radar refresh interval. Precondition is that pad must be visible/active.
     */
    private void updatePadObjectsPositions() {
        if (!GUIBriefing.ZUTI_IS_BRIEFING_ACTIVE && (GUI.pad == null || !GUI.pad.isActive())) return;

        /*
         * System.out.println("ZutiRadarRefresh - intervalSet = " + refreshIntervalSet); System.out.println("Current time: " + Time.current()); System.out.println("Start time: " + startTime); System.out.println("Result: " + (Time.current()-startTime) +
         * " > " + mission.zutiRadar_RefreshInterval); System.out.println("------------------------------");
         */

        if (!this.refreshIntervalSet || Time.current() - this.startTime > Mission.MDS_VARIABLES().zutiRadar_RefreshInterval) {
            this.refreshZutiPadObjectsPositions();
            this.refreshZutiTargetsPositions();

            // Refill objects array - check if new objects appeared
            ZutiSupportMethods_GUI.fillAirInterval(GUI.pad, false);

            this.startTime = Time.current();
        }
    }

    /**
     * Count how many radars each side has. Executed every 5s.
     */
    private void countRadars() {
        if (Time.current() - this.countRadarsStartTime > RADARS_COUNT_INTERVAL) {
            Mission.MDS_VARIABLES().zutiRadar_PlayerSideHasRadars = false;

            int size = this.radars.size();
            List deadRadars = new ArrayList();

            for (int i = 0; i < size; i++) {
                ZutiRadarObject radar = (ZutiRadarObject) this.radars.get(i);
                if (radar.isAlive()) {
                    Mission.MDS_VARIABLES().zutiRadar_PlayerSideHasRadars = true;
                    break;
                } else deadRadars.add(radar);
            }

            size = deadRadars.size();
            for (int i = 0; i < size; i++)
                this.radars.remove(deadRadars.get(i));

            // TODO: Storebror: Fix excessive log output on FoW Missions
//			System.out.println("ZutiRadarRefresh - Radars recounted...");
//			System.out.println("  Player side has radars:  " + Mission.MDS_VARIABLES().zutiRadar_PlayerSideHasRadars);
//			System.out.println("-------------------------------");
            // ---
            this.countRadarsStartTime = Time.current();
        }
    }

    /**
     * Set pad objects (actors and targets) visibility flags that either render them on the pad or don't. Executed always, if pad is active/shown.
     */
    private void executeVisibilityCheck() {
        if (!GUIBriefing.ZUTI_IS_BRIEFING_ACTIVE && (GUI.pad == null || !GUI.pad.isActive())) return;

        int playerArmy = ZutiSupportMethods.getPlayerArmy();

        double worldXOffset = GUI.pad.cameraMap2D.worldXOffset;
        double worldYOffset = GUI.pad.cameraMap2D.worldYOffset;
        double worldX1ffset = GUI.pad.cameraMap2D.worldXOffset + (GUI.pad.cameraMap2D.right - GUI.pad.cameraMap2D.left) / GUI.pad.cameraMap2D.worldScale;
        double worldY1ffset = GUI.pad.cameraMap2D.worldYOffset + (GUI.pad.cameraMap2D.top - GUI.pad.cameraMap2D.bottom) / GUI.pad.cameraMap2D.worldScale;

        this.runIfActive_RefreshPadObjects(worldXOffset, worldYOffset, worldX1ffset, worldY1ffset, playerArmy);
        this.runIfActive_RefreshTargets(worldXOffset, worldYOffset, worldX1ffset, worldY1ffset, playerArmy);
    }

    private void runIfActive_RefreshPadObjects(double worldXOffset, double worldYOffset, double worldX1ffset, double worldY1ffset, int playerArmy) {
        try {
            this.deadObjects.clear();

            Iterator iterator = GUI.pad.zutiPadObjects.keySet().iterator();
            ZutiPadObject zo = null;
            ZutiRadarObject radar = null;
            Point3d position = null;
            boolean playerSideHasRadars = Mission.MDS_VARIABLES().zutiRadar_PlayerSideHasRadars;

            while (iterator.hasNext()) {
                zo = (ZutiPadObject) GUI.pad.zutiPadObjects.get(iterator.next());
                if (!zo.isAlive() || zo.getOwner() == null) {
                    this.deadObjects.add(zo);
                    continue;
                }

                // If radar is in simple mode, just set flags
                if (!ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE) {
                    if (playerSideHasRadars) zo.setVisibleForPlayerArmy(true);
                    else zo.setVisibleForPlayerArmy(false);
                    continue;
                }
                if (zo.isPlayerArmyScout() && Mission.MDS_VARIABLES().zutiRadar_ScoutsAsRadar) {
                    zo.setVisibleForPlayerArmy(true);
                    continue;
                }

                position = zo.getPosition();

                if (!(position.x < worldXOffset) && !(position.x > worldX1ffset) && !(position.y < worldYOffset) && !(position.y > worldY1ffset)) {
                    // Speed optimization
                    if (zo.hashCode() % MODUS != Time.current() % MODUS) continue;

                    // Target is inside pad area and his time for processing is here
                    zo.setVisibleForPlayerArmy(false);
                    // Conventional radars can only show non-ground objects! For ground objects, scouts are used to identify
                    // Go through all radars on the map and check if it is visible by that radar. As soon as it it, break.
                    for (int i = 0; i < this.radars.size(); i++) {
                        radar = (ZutiRadarObject) this.radars.get(i);

                        // Ground units can only be detected by scouts
                        if (zo.isGroundUnit() && radar.getType() != 3) continue;

                        if (!zo.isVisibleForPlayerArmy() && radar.isInRange(position, zo.isGroundUnit(), false)) {
                            zo.setVisibleForPlayerArmy(true);
                            // System.out.println("ZutiRadarRefresh - Is actor " + zo.getName() + ", visible: " + zo.isVisibleForPlayerArmy());
                            break;
                        }
                    }
                    // System.out.println("Actor: " + zo.getOwner().toString() + ", height: " + zo.getPosition().z);
                    // System.out.println("----------------------------------------------------");
                }
            }

            // remove dead objects
            for (int i = 0; i < this.deadObjects.size(); i++)
                GUI.pad.zutiPadObjects.remove(this.deadObjects.get(i));

            this.deadObjects.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void runIfActive_RefreshTargets(double worldXOffset, double worldYOffset, double worldX1ffset, double worldY1ffset, int playerArmy) {
        try {
            if (ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS != null && Mission.MDS_VARIABLES().zutiIcons_ShowTargets) {
                this.deadObjects.clear();

                Point3d targetPosition = null;
                GUIBriefing.TargetPoint tp = null;
                ZutiRadarObject radar = null;
                boolean playerSideHasRadars = Mission.MDS_VARIABLES().zutiRadar_PlayerSideHasRadars;

                Iterator iterator = ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS.iterator();
                while (iterator.hasNext()) {
                    tp = (GUIBriefing.TargetPoint) iterator.next();

                    if (tp.actor != null && !tp.actor.isAlive()) {
                        this.deadObjects.add(tp);

                        // System.out.println("ZutiRadarRefresh - Added new targetpoint to deadTargets array!");
                        continue;
                    }

                    // If our targets are stationary by nature (bridges, ground, inspect), always render!
                    if (tp.type != 0 && tp.type != 4 && tp.type != 5) {
                        tp.setVisibleForPlayerArmy(true);
                        continue;
                    }

                    // If radar is in simple mode, just set flags
                    if (!ZutiMDSVariables.ZUTI_RADAR_IN_ADV_MODE) {
                        if (playerSideHasRadars) tp.setVisibleForPlayerArmy(true);
                        else tp.setVisibleForPlayerArmy(false);

                        continue;
                    }

                    // Targets can have actors position or their own position, depending
                    // on target type
                    targetPosition = new Point3d(tp.x, tp.y, tp.z);
                    if (tp.actor != null) targetPosition = tp.actor.pos.getAbsPoint();

                    if (!(targetPosition.x < worldXOffset) && !(targetPosition.x > worldX1ffset) && !(targetPosition.y < worldYOffset) && !(targetPosition.y > worldY1ffset) || GUIBriefing.ZUTI_IS_BRIEFING_ACTIVE) {
                        // Speed optimization
                        long modusResult = Time.current() % MODUS;
                        if (tp.actor != null && tp.hashCode() % MODUS != modusResult) continue;
                        else if (tp.hashCode() % MODUS != modusResult) continue;

                        tp.setVisibleForPlayerArmy(false);

                        // Target is inside pad area and his time for processing is here
                        // System.out.println("Checking visibility for target: " + tp.actor.toString());

                        if (tp.actor != null && tp.getIsAlive()) for (int j = 0; j < this.radars.size(); j++) {
                            radar = (ZutiRadarObject) this.radars.get(j);
                            // System.out.println(" Is if visible by radar " + radar.getName() + ": " + radar.isInRange(targetPosition, tp.isGroundUnit()));
                            if (!tp.isVisibleForPlayerArmy() && radar.isInRange(targetPosition, tp.isGroundUnit(), true)) {
                                tp.setVisibleForPlayerArmy(true);

                                break;
                            }
                        }

                        // System.out.println("----------------------------------------------------");
                    }
                }

                // remove dead objects
                GUIBriefing.TargetPoint deadTp = null;
                for (int i = 0; i < this.deadObjects.size(); i++) {
                    deadTp = (GUIBriefing.TargetPoint) this.deadObjects.get(i);

                    if (deadTp.isBaseActorWing) {
                        ZutiSupportMethods_GUI.assignTargetActor(deadTp);
                        if (deadTp.actor == null || deadTp.actor.getDiedFlag()) ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS.remove(deadTp);
                    } else ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS.remove(deadTp);

                    // System.out.println("ZutiRadarRefresh - Targetpoint removed!");
                }

                this.deadObjects.clear();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void refreshZutiPadObjectsPositions() {
        if (GUI.pad == null) return;

        // System.out.println("Pad objects: " + GUI.pad.zutiPadObjects.size());
        Iterator iterator = GUI.pad.zutiPadObjects.keySet().iterator();
        ZutiPadObject zpo = null;

        while (iterator.hasNext()) {
            zpo = (ZutiPadObject) GUI.pad.zutiPadObjects.get(iterator.next());
            zpo.refreshPosition();
        }
    }

    private void refreshZutiTargetsPositions() {
        if (ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS == null || !Mission.MDS_VARIABLES().zutiIcons_ShowTargets) return;

        // System.out.println("Targets: " + GUIBriefing.ZUTI_TARGETS.size());
        Iterator iterator = ZutiTargetsSupportMethods.ZUTI_TARGETPOINTS.iterator();
        GUIBriefing.TargetPoint zpo = null;

        while (iterator.hasNext()) {
            zpo = (GUIBriefing.TargetPoint) iterator.next();
            zpo.refreshPosition();

            // System.out.println("Target position refreshed to: " + zpo.x + ", " + zpo.y + ", name: " + zpo.nameTarget);
        }
    }

    /**
     * Fill radars list by going through all of the objects that are part of missions. After radars are found, list containing them is sorted by range.
     *
     * @param bp
     * @param radars
     */
    public static void findRadars(int playerArmy) {
        if (ZUTI_RADARS == null) ZUTI_RADARS = new ZutiRadarRefresh();

        ZUTI_RADARS.clear();

        ZUTI_RADARS.findLandBasedRadars(playerArmy);
        // TODO: Limit Log output // by Storebror
//		System.out.println("ZutiRadarRefresh_New - radars after searching houses: " + ZUTI_RADARS.radars.size());

        ZUTI_RADARS.findSeaBasedRadars(playerArmy);
        // TODO: Limit Log output // by Storebror
//		System.out.println("ZutiRadarRefresh_New - radars after searching ships: " + ZUTI_RADARS.radars.size());

        ZUTI_RADARS.findAirborneRadars(playerArmy);
        // TODO: Limit Log output // by Storebror
//		System.out.println("ZutiRadarRefresh_New - radars after searching aircrafts: " + ZUTI_RADARS.radars.size());

        ZUTI_RADARS.sortRadars();
    }

    private void findRadarsInterval() {
        /*
         * System.out.println("Checking for radars... findRadarsStartTime = " + findRadarsStartTime); System.out.println("current time: " + Time.current()); System.out.println("Interval = " + RADARS_FIND_INTERVAL); System.out.println("RESULT = " +
         * (Time.current()-findRadarsStartTime) );
         */

        // If findRadarsStartTime is 0 = just reset or loaded, so, find new radar objects.
        if (this.findRadarsStartTime == 0 || Time.current() - this.findRadarsStartTime > RADARS_FIND_INTERVAL) {
            findRadars(ZutiSupportMethods.getPlayerArmy());
            this.findRadarsStartTime = Time.current();
        }
    }

    private void findLandBasedRadars(int playerArmy) {
        if (World.cur().houseManager == null) return;

        House[] houses = World.cur().houseManager.zutiGetHouses();

        if (houses == null) return;

        int lenght = houses.length;
        if (lenght <= 0) return;

        if (World.cur() == null) return;

        ArrayList bornPlaces = World.cur().bornPlaces;
        if (bornPlaces == null) return;

        int size = bornPlaces.size();
        if (size <= 0) return;

        ZutiRadarObject radar = null;
        House houseRadar = null;
        BornPlace bp = null;
        Point3d point3d = null;
        double distance = 0.0D;

        for (int i = 0; i < lenght; i++)
            try {
                for (int ii = 0; ii < size; ii++) {
                    bp = (BornPlace) bornPlaces.get(ii);

                    if (bp.army != playerArmy) continue;

                    houseRadar = houses[i];

                    if (houseRadar != null && houseRadar.name() != null && houseRadar.name().indexOf(ZutiRadarObject.ZUTI_RADAR_OBJECT_NAME) > -1) {
                        double d = bp.r * bp.r;
                        // Changed that this will convert only static actors to appropriate army
                        point3d = houseRadar.pos.getAbsPoint();
                        // System.out.println("Radar point: " + new Double(point3d.x).toString() + ", " + new Double(point3d.y).toString());
                        distance = (point3d.x - bp.place.x) * (point3d.x - bp.place.x) + (point3d.y - bp.place.y) * (point3d.y - bp.place.y);
                        if (distance <= d) {
                            radar = new ZutiRadarObject(houseRadar, 1);
                            radar.setRange(bp.zutiRadarRange);
                            radar.setMinHeight(bp.zutiRadarHeight_MIN);
                            radar.setMaxHeight(bp.zutiRadarHeight_MAX);

                            this.radars.add(radar);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

    private void findSeaBasedRadars(int playerArmy) {
        if (!Mission.MDS_VARIABLES().zutiRadar_ShipsAsRadar || Main.cur().mission == null) return;

        ArrayList actors = Main.cur().mission.actors;
        if (actors == null) return;

        int size = actors.size();
        if (size <= 0) return;

        boolean bigRadar = Mission.MDS_VARIABLES().zutiRadar_EnableBigShip_Radar;
        boolean smallRadar = Mission.MDS_VARIABLES().zutiRadar_EnableSmallShip_Radar;

        ZutiRadarObject radar = null;

        for (int i = 0; i < size; i++) {
            Actor actor = (Actor) actors.get(i);

            if (actor == null || actor.getArmy() != playerArmy || actor.getDiedFlag() || !(actor instanceof BigshipGeneric || actor instanceof ShipGeneric)) continue;

            boolean recognisedAsRadar = false;

            if (bigRadar) if (actor instanceof ZutiTypeBigRadar) {
                radar = new ZutiRadarObject(actor, 2);
                radar.setRange(Mission.MDS_VARIABLES().zutiRadar_ShipRadar_MaxRange);
                radar.setMinHeight(Mission.MDS_VARIABLES().zutiRadar_ShipRadar_MinHeight);
                radar.setMaxHeight(Mission.MDS_VARIABLES().zutiRadar_ShipRadar_MaxHeight);

                this.radars.add(radar);
                recognisedAsRadar = true;
                break;
            }

            if (smallRadar && !recognisedAsRadar) if (actor instanceof ZutiTypeSmallRadar) {
                radar = new ZutiRadarObject(actor, 2);
                radar.setRange(Mission.MDS_VARIABLES().zutiRadar_ShipSmallRadar_MaxRange);
                radar.setMinHeight(Mission.MDS_VARIABLES().zutiRadar_ShipSmallRadar_MinHeight);
                radar.setMaxHeight(Mission.MDS_VARIABLES().zutiRadar_ShipSmallRadar_MaxHeight);

                this.radars.add(radar);
            }
        }
    }

    private void findAirborneRadars(int playerArmy) {
        if (!Mission.MDS_VARIABLES().zutiRadar_ScoutsAsRadar) return;

        // ArrayList actors = mission.actors;
        List actors = Engine.targets();
        if (actors == null) return;

        int size = actors.size();
        if (size <= 0) return;

        Actor actor = null;
        ZutiRadarObject radar = null;
        float alphaValue = this.tanAlfa[Mission.MDS_VARIABLES().zutiRadar_ScoutGroundObjects_Alpha - 1];
        for (int i = 0; i < size; i++) {
            actor = (Actor) actors.get(i);

            if (actor == null || actor.getArmy() != playerArmy || actor.getDiedFlag()) continue;

            if (actor instanceof Aircraft && ZutiRadarObject.isPlayerArmyScout(actor, playerArmy)) {
                radar = new ZutiRadarObject(actor, 3);
                radar.setRange(alphaValue);
                radar.setMinHeight(Mission.MDS_VARIABLES().zutiRadar_ScoutRadar_DeltaHeight);
                radar.setMaxHeight(radar.getMinHeight());

                this.radars.add(radar);
            }
        }
    }

    /**
     * Sorts radars based on their range
     */
    private void sortRadars() {
        Collections.sort(this.radars, new ZutiRadar_CompareByRange());

        // TODO: Storebror: Fix excessive log output on FoW Missions
//		System.out.println("Sorted array of radars by their range");
        // ---
        String name = null;
        for (int i = 0; i < this.radars.size(); i++) {
            ZutiRadarObject ro = (ZutiRadarObject) this.radars.get(i);
            name = ro.getName();
            if (name.indexOf(".") > 0) name = name.substring(name.lastIndexOf(".") + 1, name.length());

            // TODO: Storebror: Fix excessive log output on FoW Missions
//			if( ro.getType() != 3 )
//				System.out.println("  Radar name: " + name + ", type: " + ro.getType() + ", range: " + Math.sqrt(ro.getRange()) + "km" );
//			else
//				System.out.println("  Radar name: " + name + ", type: " + ro.getType() + ", range: " + ro.getRange() + "km" );
            // ---
        }
        // TODO: Storebror: Fix excessive log output on FoW Missions
//		System.out.println("--------------------------------------");
        // ---
    }

    /**
     * Clear current radars list
     */
    private void clear() {
        this.radars.clear();
    }
}