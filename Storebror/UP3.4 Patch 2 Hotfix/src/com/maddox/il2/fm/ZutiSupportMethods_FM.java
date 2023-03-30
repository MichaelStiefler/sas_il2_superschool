package com.maddox.il2.fm;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.builder.ZutiSupportMethods_Builder;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.engine.ZutiSupportMethods_Engine;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiAcWithReleasedOrdinance;
import com.maddox.il2.game.ZutiAirfieldPoint;
import com.maddox.il2.game.ZutiChangeLoadout;
import com.maddox.il2.game.ZutiInterpolator;
import com.maddox.il2.game.ZutiRearm_Bombs;
import com.maddox.il2.game.ZutiRearm_Cannons;
import com.maddox.il2.game.ZutiRearm_Rockets;
import com.maddox.il2.game.ZutiRearm_UnloadBombs;
import com.maddox.il2.game.ZutiRearm_UnloadBullets;
import com.maddox.il2.game.ZutiRearm_UnloadFuel;
import com.maddox.il2.game.ZutiRearm_UnloadRockets;
import com.maddox.il2.game.ZutiRefuelAircraft;
import com.maddox.il2.game.ZutiRepairAircraft;
import com.maddox.il2.game.ZutiRepairEngine;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.game.order.ZutiSupportMethods_GameOrder;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_Net;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.il2.objects.ships.ZutiSupportMethods_Ships;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class ZutiSupportMethods_FM {
    public static boolean                  UPDATE_DECK_TIMER          = false;
    public static long                     DECK_CLEAR_TIME            = 0L;
    public static long                     DECK_LAST_REFRESH          = 0L;
    private static boolean                 SHOW_DECK_CLEAR_MESSAGE    = true;
    public static boolean                  IS_PLAYER_VULNERABLE       = true;
    public static boolean                  UPDATE_VULNERABILITY_TIMER = false;
    public static long                     VULNERABILITY_LAST_REFRESH = 0L;

    private static ZutiRearm_Cannons       zutiRearm_Cannons          = null;
    private static ZutiRearm_Bombs         zutiRearm_Bombs            = null;
    private static ZutiRearm_Rockets       zutiRearm_Rockets          = null;
    private static ZutiChangeLoadout       zutiChangeLoadout          = null;
    private static ZutiRepairAircraft      zutirepairAircraft         = null;
    private static ZutiRefuelAircraft      zutiRefuelAircraft         = null;

    private static ZutiRearm_UnloadBullets zutiRearm_UnloadBullets    = null;
    private static ZutiRearm_UnloadBombs   zutiRearm_UnloadBombs      = null;
    private static ZutiRearm_UnloadRockets zutiRearm_UnloadRockets    = null;
    private static ZutiRearm_UnloadFuel    zutiRearm_UnloadFuel       = null;

    private static List                    zutiEngineRepairing        = null;
    private static ZutiAirfieldPoint       tempZAP                    = new ZutiAirfieldPoint(0D, 0D, 0D, 0D, 0D);

    /**
     * Reset class variables
     */
    public static void resetClassVariables() {
        UPDATE_DECK_TIMER = false;
        DECK_CLEAR_TIME = 0L;
        DECK_LAST_REFRESH = 0L;
        SHOW_DECK_CLEAR_MESSAGE = true;
        IS_PLAYER_VULNERABLE = true;
        UPDATE_VULNERABILITY_TIMER = false;
        VULNERABILITY_LAST_REFRESH = 0L;
    }

    public static void startUnloadingBombs() {
        zutiRearm_UnloadBombs = new ZutiRearm_UnloadBombs();
    }

    public static void startUnloadingBullets() {
        zutiRearm_UnloadBullets = new ZutiRearm_UnloadBullets();
    }

    public static void startUnloadingRockets() {
        zutiRearm_UnloadRockets = new ZutiRearm_UnloadRockets();
    }

    public static void startUnloadingFuel() {
        zutiRearm_UnloadFuel = new ZutiRearm_UnloadFuel();
    }

    public static void resetRRRTimers() {
        if (zutiRearm_Cannons != null && zutiRearm_Cannons.updateTimer() < 0) zutiRearm_Cannons = null;
        if (zutiRearm_Bombs != null && zutiRearm_Bombs.updateTimer() < 0) zutiRearm_Bombs = null;
        if (zutiRearm_Rockets != null && zutiRearm_Rockets.updateTimer() < 0) zutiRearm_Rockets = null;

        if (zutiChangeLoadout != null && zutiChangeLoadout.updateTimer() < 0) zutiChangeLoadout = null;

        if (zutirepairAircraft != null && zutirepairAircraft.updateTimer() < 0) zutirepairAircraft = null;

        if (zutiRefuelAircraft != null && zutiRefuelAircraft.updateTimer() < 0) zutiRefuelAircraft = null;

        if (zutiRearm_UnloadBombs != null && zutiRearm_UnloadBombs.updateTimer() < 0) zutiRearm_UnloadBombs = null;
        if (zutiRearm_UnloadBullets != null && zutiRearm_UnloadBullets.updateTimer() < 0) zutiRearm_UnloadBullets = null;
        if (zutiRearm_UnloadRockets != null && zutiRearm_UnloadRockets.updateTimer() < 0) zutiRearm_UnloadRockets = null;
        if (zutiRearm_UnloadFuel != null && zutiRearm_UnloadFuel.updateTimer() < 0) zutiRearm_UnloadFuel = null;

        zutiEngineRepairing = updateEngineRepairingProcesses();
    }

    private static List updateEngineRepairingProcesses() {
        if (zutiEngineRepairing == null) return null;

        boolean isStillActive = false;
        for (int i = 0; i < zutiEngineRepairing.size(); i++) {
            ZutiRepairEngine repair = (ZutiRepairEngine) zutiEngineRepairing.get(i);
            if (repair.updateTimer() > -1) isStillActive = true;
        }

        if (isStillActive) return zutiEngineRepairing;
        else {
            zutiEngineRepairing.clear();
            return null;
        }
    }

    /**
     * Method starts to execute all RRR processes.
     *
     * @param FM
     * @param tempZAP
     */
    public static void startAll(Aircraft aircraft) {
        if (ZutiSupportMethods.allowRRR(aircraft)) {
            if (!ZutiSupportMethods_FM.isFMOnFriendlySide(aircraft.FM, true)) return;

            long bullets = ZutiSupportMethods_GameOrder.calculateBulletsToReload(aircraft);
            ZutiSupportMethods_NetSend.requestBullets(bullets);

            int[] bombs = ZutiSupportMethods_GameOrder.getBombsCount(aircraft);
            ZutiSupportMethods_NetSend.requestBombs(bombs);

            long rockets = ZutiSupportMethods_GameOrder.calculateRocketsToReload(aircraft);
            ZutiSupportMethods_NetSend.requestRockets(rockets);

            // If user select "Start All", then refuel will be done just up to default fuel capacity
            float fuel = ZutiSupportMethods_GameOrder.calculateFuelToRefuel(aircraft, false);
            ZutiSupportMethods_NetSend.requestFuel(fuel);
        } else HUD.log("mds.checkChocksPosition");
    }

    /**
     * Method starts rearming process.
     *
     * @param FM
     * @param tempZAP
     */
    public static void startRearming_Cannons(Aircraft ac, long bullets) {
        if (ZutiSupportMethods.allowRRR(ac)) {
            ZutiAirfieldPoint currentZAP = ac.FM.Gears.zutiCurrentZAP;
            if (!ZutiSupportMethods_FM.isFMOnFriendlySide(ac.FM, true)) return;

            // TODO: Added by SAS~Storebror: If "Override default Rearm/Refuel/Repair settings" has been selected, it should actually override the general settings!
            boolean useBpRrrSettings = false;
            BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace(ac.pos.getAbsPoint().x, ac.pos.getAbsPoint().y, ac.getArmy());
            if (bp != null) {
                double distance = Math.sqrt((ac.pos.getAbsPoint().x - bp.place.x) * (ac.pos.getAbsPoint().x - bp.place.x) + (ac.pos.getAbsPoint().y - bp.place.y) * (ac.pos.getAbsPoint().y - bp.place.y));
                if (distance <= bp.r) useBpRrrSettings = bp.zutiOverrideDefaultRRRSettings;
            }
            boolean rearmOnlyIfAmmoPresent = useBpRrrSettings ? bp.zutiRearmOnlyIfAmmoBoxesExist : Mission.MDS_VARIABLES().zutiReload_ReloadOnlyIfAmmoBoxesExist;
            // boolean rearmOnlyIfAmmoPresent = Mission.MDS_VARIABLES().zutiReload_ReloadOnlyIfAmmoBoxesExist;
            // #####################################################################################################################################################

            float rearmTimePenalty = 0.0F;
            if (currentZAP != null) rearmTimePenalty = currentZAP.getRearmingTimePenalty(ac.FM.Loc);
            else rearmTimePenalty = tempZAP.getRearmingTimePenalty(ac.FM.Loc);

            // Start rearming process
            if (rearmOnlyIfAmmoPresent && rearmTimePenalty > 0) zutiRearm_Cannons = new ZutiRearm_Cannons(rearmTimePenalty, bullets);
            else if (!rearmOnlyIfAmmoPresent) zutiRearm_Cannons = new ZutiRearm_Cannons(1.0F, bullets);
            else HUD.log("mds.rearmNotPossible");
        } else HUD.log("mds.checkChocksPosition");
    }

    /**
     * Method starts rearming process.
     *
     * @param FM
     * @param tempZAP
     */
    public static void startRearming_Bombs(Aircraft ac, int[] bombs) {
        if (ZutiSupportMethods.allowRRR(ac)) {
            ZutiAirfieldPoint currentZAP = ac.FM.Gears.zutiCurrentZAP;
            if (!ZutiSupportMethods_FM.isFMOnFriendlySide(ac.FM, true)) return;

            // TODO: Added by SAS~Storebror: If "Override default Rearm/Refuel/Repair settings" has been selected, it should actually override the general settings!
            boolean useBpRrrSettings = false;
            BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace(ac.pos.getAbsPoint().x, ac.pos.getAbsPoint().y, ac.getArmy());
            if (bp != null) {
                double distance = Math.sqrt((ac.pos.getAbsPoint().x - bp.place.x) * (ac.pos.getAbsPoint().x - bp.place.x) + (ac.pos.getAbsPoint().y - bp.place.y) * (ac.pos.getAbsPoint().y - bp.place.y));
                if (distance <= bp.r) useBpRrrSettings = bp.zutiOverrideDefaultRRRSettings;
            }
            boolean rearmOnlyIfAmmoPresent = useBpRrrSettings ? bp.zutiRearmOnlyIfAmmoBoxesExist : Mission.MDS_VARIABLES().zutiReload_ReloadOnlyIfAmmoBoxesExist;
            // boolean rearmOnlyIfAmmoPresent = Mission.MDS_VARIABLES().zutiReload_ReloadOnlyIfAmmoBoxesExist;
            // #####################################################################################################################################################

            float rearmTimePenalty = 0.0F;
            if (currentZAP != null) rearmTimePenalty = currentZAP.getRearmingTimePenalty(ac.FM.Loc);
            else rearmTimePenalty = tempZAP.getRearmingTimePenalty(ac.FM.Loc);

            // Start rearming process
            if (rearmOnlyIfAmmoPresent && rearmTimePenalty > 0) zutiRearm_Bombs = new ZutiRearm_Bombs(rearmTimePenalty, bombs);
            else if (!rearmOnlyIfAmmoPresent) zutiRearm_Bombs = new ZutiRearm_Bombs(1.0F, bombs);
            else HUD.log("mds.rearmNotPossible");
        } else HUD.log("mds.checkChocksPosition");
    }

    /**
     * Method starts rearming process.
     *
     * @param FM
     * @param tempZAP
     */
    public static void startRearming_Rockets(Aircraft ac, long rockets) {
        if (ZutiSupportMethods.allowRRR(ac)) {
            ZutiAirfieldPoint currentZAP = ac.FM.Gears.zutiCurrentZAP;
            if (!ZutiSupportMethods_FM.isFMOnFriendlySide(ac.FM, true)) return;

            // TODO: Added by SAS~Storebror: If "Override default Rearm/Refuel/Repair settings" has been selected, it should actually override the general settings!
            boolean useBpRrrSettings = false;
            BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace(ac.pos.getAbsPoint().x, ac.pos.getAbsPoint().y, ac.getArmy());
            if (bp != null) {
                double distance = Math.sqrt((ac.pos.getAbsPoint().x - bp.place.x) * (ac.pos.getAbsPoint().x - bp.place.x) + (ac.pos.getAbsPoint().y - bp.place.y) * (ac.pos.getAbsPoint().y - bp.place.y));
                if (distance <= bp.r) useBpRrrSettings = bp.zutiOverrideDefaultRRRSettings;
            }
            boolean rearmOnlyIfAmmoPresent = useBpRrrSettings ? bp.zutiRearmOnlyIfAmmoBoxesExist : Mission.MDS_VARIABLES().zutiReload_ReloadOnlyIfAmmoBoxesExist;
            // boolean rearmOnlyIfAmmoPresent = Mission.MDS_VARIABLES().zutiReload_ReloadOnlyIfAmmoBoxesExist;
            // #####################################################################################################################################################

            float rearmTimePenalty = 0.0F;
            if (currentZAP != null) rearmTimePenalty = currentZAP.getRearmingTimePenalty(ac.FM.Loc);
            else rearmTimePenalty = tempZAP.getRearmingTimePenalty(ac.FM.Loc);

            // Start rearming process
            if (rearmOnlyIfAmmoPresent && rearmTimePenalty > 0) zutiRearm_Rockets = new ZutiRearm_Rockets(rearmTimePenalty, rockets);
            else if (!rearmOnlyIfAmmoPresent) zutiRearm_Rockets = new ZutiRearm_Rockets(1.0F, rockets);
            else HUD.log("mds.rearmNotPossible");
        } else HUD.log("mds.checkChocksPosition");
    }

    /**
     * Start process that changes loadout on giver flight model.
     *
     * @param FM
     * @param tempZAP
     * @param selectedLoadoutId
     */
    public static void startChangingLoadout(Aircraft ac, int selectedLoadoutId) {
        if (ZutiSupportMethods.allowRRR(ac)) {
            ZutiAirfieldPoint currentZAP = ac.FM.Gears.zutiCurrentZAP;
            if (!ZutiSupportMethods_FM.isFMOnFriendlySide(ac.FM, true)) return;

            // TODO: Added by SAS~Storebror: If "Override default Rearm/Refuel/Repair settings" has been selected, it should actually override the general settings!
            boolean useBpRrrSettings = false;
            BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace(ac.pos.getAbsPoint().x, ac.pos.getAbsPoint().y, ac.getArmy());
            if (bp != null) {
                double distance = Math.sqrt((ac.pos.getAbsPoint().x - bp.place.x) * (ac.pos.getAbsPoint().x - bp.place.x) + (ac.pos.getAbsPoint().y - bp.place.y) * (ac.pos.getAbsPoint().y - bp.place.y));
                if (distance <= bp.r) useBpRrrSettings = bp.zutiOverrideDefaultRRRSettings;
            }
            boolean rearmOnlyIfAmmoPresent = useBpRrrSettings ? bp.zutiRearmOnlyIfAmmoBoxesExist : Mission.MDS_VARIABLES().zutiReload_ReloadOnlyIfAmmoBoxesExist;
            // boolean rearmOnlyIfAmmoPresent = Mission.MDS_VARIABLES().zutiReload_ReloadOnlyIfAmmoBoxesExist;
            // #####################################################################################################################################################

            float rearmTimePenalty = 0.0F;
            if (currentZAP != null) rearmTimePenalty = currentZAP.getRearmingTimePenalty(ac.FM.Loc);
            else rearmTimePenalty = tempZAP.getRearmingTimePenalty(ac.FM.Loc);

            // Start rearming process
            if (rearmOnlyIfAmmoPresent && rearmTimePenalty > 0) zutiChangeLoadout = new ZutiChangeLoadout(rearmTimePenalty, selectedLoadoutId, ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(ac.FM));
            else if (!rearmOnlyIfAmmoPresent) zutiChangeLoadout = new ZutiChangeLoadout(1.0F, selectedLoadoutId, ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(ac.FM));
            else HUD.log("mds.changeLoadout");
        } else HUD.log("mds.checkChocksPosition");
    }

    /**
     * Method starts refueling process.
     *
     * @param FM
     * @param tempZAP
     * @param full
     */
    public static void startRefueling(Aircraft ac, float fuelToFill) {
        System.out.println("ZSM_FM: approved fuel amount = " + fuelToFill);
        if (ZutiSupportMethods.allowRRR(ac)) {
            ZutiAirfieldPoint currentZAP = ac.FM.Gears.zutiCurrentZAP;
            if (!ZutiSupportMethods_FM.isFMOnFriendlySide(ac.FM, true)) return;

            // TODO: Added by SAS~Storebror: If "Override default Rearm/Refuel/Repair settings" has been selected, it should actually override the general settings!
            boolean useBpRrrSettings = false;
            BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace(ac.pos.getAbs().getX(), ac.pos.getAbs().getY(), ac.getArmy());
            if (bp != null) {
                double distance = Math.sqrt((ac.pos.getAbsPoint().x - bp.place.x) * (ac.pos.getAbsPoint().x - bp.place.x) + (ac.pos.getAbsPoint().y - bp.place.y) * (ac.pos.getAbsPoint().y - bp.place.y));
                if (distance <= bp.r) useBpRrrSettings = bp.zutiOverrideDefaultRRRSettings;
            }
            boolean refuelOnlyIfFuelPresent = useBpRrrSettings ? bp.zutiRefuelOnlyIfFuelTanksExist : Mission.MDS_VARIABLES().zutiReload_ReloadOnlyIfFuelTanksExist;
            // boolean refuelOnlyIfFuelPresent = Mission.MDS_VARIABLES().zutiReload_ReloadOnlyIfFuelTanksExist;
            // #####################################################################################################################################################

            float refuelTimePenalty = 0.0F;
            if (currentZAP != null) refuelTimePenalty = currentZAP.getRefuelingTimePenalty(ac.FM.Loc);
            else refuelTimePenalty = tempZAP.getRefuelingTimePenalty(ac.FM.Loc);

            // Start refueling process
            if (refuelOnlyIfFuelPresent && refuelTimePenalty > 0) zutiRefuelAircraft = new ZutiRefuelAircraft(refuelTimePenalty, fuelToFill);
            else if (!refuelOnlyIfFuelPresent) zutiRefuelAircraft = new ZutiRefuelAircraft(1.0F, fuelToFill);
            else HUD.log("mds.refuelNotPossible");
        } else HUD.log("mds.checkChocksPosition");
    }

    /**
     * Method executes repairing procedures.
     *
     * @param FM
     * @param tempZAP
     */
    public static void startRepairing(Aircraft ac) {
        if (ZutiSupportMethods.allowRRR(ac)) {
            ZutiAirfieldPoint currentZAP = ac.FM.Gears.zutiCurrentZAP;
            if (!ZutiSupportMethods_FM.isFMOnFriendlySide(ac.FM, true)) return;

            // TODO: Added by SAS~Storebror: If "Override default Rearm/Refuel/Repair settings" has been selected, it should actually override the general settings!
            boolean useBpRrrSettings = false;
            BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace(ac.pos.getAbsPoint().x, ac.pos.getAbsPoint().y, ac.getArmy());
            if (bp != null) {
                double distance = Math.sqrt((ac.pos.getAbsPoint().x - bp.place.x) * (ac.pos.getAbsPoint().x - bp.place.x) + (ac.pos.getAbsPoint().y - bp.place.y) * (ac.pos.getAbsPoint().y - bp.place.y));
                if (distance <= bp.r) useBpRrrSettings = bp.zutiOverrideDefaultRRRSettings;
            }
            boolean repairOnlyIfWorkshopPresent = useBpRrrSettings ? bp.zutiRepairOnlyIfWorkshopExist : Mission.MDS_VARIABLES().zutiReload_ReloadOnlyIfFuelTanksExist;
            // boolean repairOnlyIfWorkshopPresent = Mission.MDS_VARIABLES().zutiReload_RepairOnlyIfWorkshopExist;
            // #####################################################################################################################################################

            float repairTimePenalty = 0.0F;
            if (currentZAP != null) repairTimePenalty = currentZAP.getRepairingTimePenalty(ac.FM.Loc);
            else repairTimePenalty = tempZAP.getRepairingTimePenalty(ac.FM.Loc);

            // Start repairing process
            boolean rearmingInProgress = zutiRearm_Cannons != null || zutiRearm_Bombs != null || zutiRearm_Rockets != null ? true : false;
            if (repairOnlyIfWorkshopPresent && repairTimePenalty > 0) zutirepairAircraft = new ZutiRepairAircraft(repairTimePenalty, rearmingInProgress);
            else if (!repairOnlyIfWorkshopPresent) zutirepairAircraft = new ZutiRepairAircraft(1.0F, rearmingInProgress);
            else HUD.log("mds.repairNotPossible");
        } else HUD.log("mds.checkChocksPosition");
    }

    /**
     * Method executes engine repair process. Multiple instances possible.
     *
     * @param FM
     * @param engines
     * @param engineId
     */
    public static void startEngineRepairing(Aircraft ac, int engines, int engineId) {
        if (ZutiSupportMethods.allowRRR(ac)) {
            ZutiAirfieldPoint currentZAP = ac.FM.Gears.zutiCurrentZAP;
            if (!ZutiSupportMethods_FM.isFMOnFriendlySide(ac.FM, true)) return;

            // TODO: Added by SAS~Storebror: If "Override default Rearm/Refuel/Repair settings" has been selected, it should actually override the general settings!
            boolean useBpRrrSettings = false;
            BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace(ac.pos.getAbsPoint().x, ac.pos.getAbsPoint().y, ac.getArmy());
            if (bp != null) {
                double distance = Math.sqrt((ac.pos.getAbsPoint().x - bp.place.x) * (ac.pos.getAbsPoint().x - bp.place.x) + (ac.pos.getAbsPoint().y - bp.place.y) * (ac.pos.getAbsPoint().y - bp.place.y));
                if (distance <= bp.r) useBpRrrSettings = bp.zutiOverrideDefaultRRRSettings;
            }
            boolean repairOnlyIfWorkshopPresent = useBpRrrSettings ? bp.zutiRepairOnlyIfWorkshopExist : Mission.MDS_VARIABLES().zutiReload_ReloadOnlyIfFuelTanksExist;
            // boolean repairOnlyIfWorkshopPresent = Mission.MDS_VARIABLES().zutiReload_RepairOnlyIfWorkshopExist;
            // #####################################################################################################################################################

            float repairTimePenalty = 0.0F;
            if (currentZAP != null) repairTimePenalty = currentZAP.getRepairingTimePenalty(ac.FM.Loc);
            else repairTimePenalty = tempZAP.getRepairingTimePenalty(ac.FM.Loc);

            // TODO: Start repairing process
            if (zutiEngineRepairing == null) zutiEngineRepairing = new ArrayList();

            if (repairOnlyIfWorkshopPresent && repairTimePenalty > 0) zutiEngineRepairing.add(new ZutiRepairEngine(repairTimePenalty, engines, engineId, ZutiSupportMethods_Air.getAircraftMotorBackup(ac, engineId)));
            else if (!repairOnlyIfWorkshopPresent) zutiEngineRepairing.add(new ZutiRepairEngine(1.0F, engines, engineId, ZutiSupportMethods_Air.getAircraftMotorBackup(ac, engineId)));
            else HUD.log("mds.repairNotPossible");
        } else HUD.log("mds.checkChocksPosition");
    }

    /**
     * Method checks if flight model location is on specified zap.
     *
     * @param FM
     * @return
     */
    public static boolean isFMOnZAP(FlightModel FM) {
        ZutiAirfieldPoint currentZAP = FM.Gears.zutiCurrentZAP;
        if (currentZAP != null) {
            double result = currentZAP.isInZAPArea(FM.Loc.x, FM.Loc.y);
            if (result > -1) return true;
        }

        // Bummer, we don't even have a "old" ZAP, yet :), or we have moved to another one
        List airports = ZutiSupportMethods_Engine.AIRFIELDS;
        int size = airports.size();
        for (int z = 0; z < size; z++) {
            ZutiAirfieldPoint point = (ZutiAirfieldPoint) airports.get(z);
            {
                double result = point.isInZAPArea(FM.Loc.x, FM.Loc.y);
                if (result > -1) {
                    // Set the ZAP ;)
                    currentZAP = point;
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Check if specified FM is on friendly side or not.
     *
     * @param flightmodel
     * @param showHUDMessage
     * @return
     */
    public static boolean isFMOnFriendlySide(FlightModel flightmodel, boolean showHUDMessage) {
        int pilotArmy = World.getPlayerArmy();

        // int areaArmy = com.maddox.il2.ai.Front.army(flightmodel.Loc.x, flightmodel.Loc.y);
        BornPlace bp = ZutiSupportMethods_Net.getNearestBornPlace_AnyArmy(flightmodel.Loc.x, flightmodel.Loc.y);
        if (bp == null) return true;

        // Friendly airfields can refuel you...
        if (pilotArmy == bp.army) return true;

        if (showHUDMessage) HUD.log("mds.playerOnEnemyHomebase");

        return false;
    }

    /**
     * Call this method when data was received that bombardier dropped bombs from AI aircraft! AI ONLY!!! Method was created to allow bombardiers on AI plane to drop bombs in pairs only and not all at once.
     */
    public static void executeDropBombs(FlightModel FM) {
        if (FM == null || FM.actor == null) return;

        for (int weaponIndex = 0; weaponIndex < FM.CT.Weapons[3].length; weaponIndex += 2)
            if (!(FM.CT.Weapons[3][weaponIndex] instanceof FuelTankGun) && FM.CT.Weapons[3][weaponIndex].haveBullets()) {
                if (FM.CT.bHasBayDoors && FM.CT.Weapons[3][weaponIndex].getHookName().startsWith("_BombSpawn")) {
                    if (FM.CT.BayDoorControl == 1.0F) {
                        FM.CT.Weapons[3][weaponIndex].shots(1);
                        // TODO:Added by |ZUTI|
                        ZutiSupportMethods_FM.executeOnbombDropped(FM.CT.zutiOwnerAircraftName, 3, weaponIndex, 1);
                    }
                } else {
                    FM.CT.Weapons[3][weaponIndex].shots(1);
                    // TODO:Added by |ZUTI|
                    ZutiSupportMethods_FM.executeOnbombDropped(FM.CT.zutiOwnerAircraftName, 3, weaponIndex, 1);
                    if (FM.CT.Weapons[3][weaponIndex].getHookName().startsWith("_BombSpawn")) FM.CT.BayDoorControl = 1.0F;
                }
                if (FM.CT.Weapons[3][weaponIndex] instanceof BombGun && !((BombGun) FM.CT.Weapons[3][weaponIndex]).isCassette() || FM.CT.Weapons[3][weaponIndex] instanceof RocketGun && !((RocketGun) FM.CT.Weapons[3][weaponIndex]).isCassette()) break;
            }
        for (int weaponIndex = 1; weaponIndex < FM.CT.Weapons[3].length; weaponIndex += 2)
            if (!(FM.CT.Weapons[3][weaponIndex] instanceof FuelTankGun) && FM.CT.Weapons[3][weaponIndex].haveBullets()) {
                if (FM.CT.bHasBayDoors && FM.CT.Weapons[3][weaponIndex].getHookName().startsWith("_BombSpawn")) {
                    if (FM.CT.BayDoorControl == 1.0F) {
                        FM.CT.Weapons[3][weaponIndex].shots(1);
                        // TODO:Added by |ZUTI|
                        ZutiSupportMethods_FM.executeOnbombDropped(FM.CT.zutiOwnerAircraftName, 3, weaponIndex, 1);
                    }
                } else {
                    FM.CT.Weapons[3][weaponIndex].shots(1);
                    // TODO:Added by |ZUTI|
                    ZutiSupportMethods_FM.executeOnbombDropped(FM.CT.zutiOwnerAircraftName, 3, weaponIndex, 1);
                }
                if (FM.CT.Weapons[3][weaponIndex] instanceof BombGun && !((BombGun) FM.CT.Weapons[3][weaponIndex]).isCassette() || FM.CT.Weapons[3][weaponIndex] instanceof RocketGun && !((RocketGun) FM.CT.Weapons[3][weaponIndex]).isCassette()) break;
            }
        FM.CT.WeaponControl[3] = false;
    }

    /**
     * Aircraft released bomb. Notify other users about this event.
     *
     * @param ownerAcName
     * @param weaponId
     * @param weaponBay
     * @param weaponRelease
     */
    public static void executeOnbombDropped(String ownerAcName, int weaponId, int weaponBay, int weaponRelease) {
        if (ownerAcName != null) {
            ZutiAcWithReleasedOrdinance aircraft = (ZutiAcWithReleasedOrdinance) ZutiAcWithReleasedOrdinance.AC_THAT_RELEASED_BOMBS.get(ownerAcName);
            if (aircraft != null) aircraft.setOrdinanceThatWasReleased(weaponId, weaponBay, weaponRelease);
            else {
                aircraft = new ZutiAcWithReleasedOrdinance();
                aircraft.setAcName(ownerAcName);
                aircraft.setOrdinanceThatWasReleased(weaponId, weaponBay, weaponRelease);
                ZutiAcWithReleasedOrdinance.AC_THAT_RELEASED_BOMBS.put(ownerAcName, aircraft);
            }

            // System.out.println("Dropped bomb from AC >" + zutiOwnerAircraftName + "<.");
        }
    }

    /**
     * Method creates complete backup of inputed Motor object and returns it's clone.
     *
     * @param inMotor
     * @return
     */
    public static Motor createEngineBackup(Motor inMotor) {
        Motor backupMotor = new Motor();

        backupMotor.bMagnetos = cloneBooleanArray(inMotor.bMagnetos);

        backupMotor.compressorAltitudes = cloneFloatArray(inMotor.compressorAltitudes);
        backupMotor.compressorPressure = cloneFloatArray(inMotor.compressorPressure);
        backupMotor.compressorAltMultipliers = cloneFloatArray(inMotor.compressorAltMultipliers);

        backupMotor.compressorRPM = cloneFloatArray(inMotor.compressorRPM);
        backupMotor.compressorATA = cloneFloatArray(inMotor.compressorATA);

        backupMotor.soundName = inMotor.soundName;
        backupMotor.startStopName = inMotor.startStopName;
        backupMotor.propName = inMotor.propName;
        backupMotor.number = inMotor.number;
        backupMotor.type = inMotor.type;
        backupMotor.cylinders = inMotor.cylinders;
        backupMotor.engineMass = inMotor.engineMass;
        backupMotor.wMin = inMotor.wMin;
        backupMotor.wNom = inMotor.wNom;
        backupMotor.wMax = inMotor.wMax;
        backupMotor.wWEP = inMotor.wWEP;
        backupMotor.wMaxAllowed = inMotor.wMaxAllowed;
        backupMotor.wNetPrev = inMotor.wNetPrev;
        backupMotor.engineMoment = inMotor.engineMoment;
        backupMotor.engineMomentMax = inMotor.engineMomentMax;
        backupMotor.engineBoostFactor = inMotor.engineBoostFactor;
        backupMotor.engineAfterburnerBoostFactor = inMotor.engineAfterburnerBoostFactor;
        backupMotor.engineDistAM = inMotor.engineDistAM;
        backupMotor.engineDistBM = inMotor.engineDistBM;
        backupMotor.engineDistCM = inMotor.engineDistCM;
        backupMotor.producedDistabilisation = inMotor.producedDistabilisation;
        backupMotor.bRan = inMotor.bRan;
        backupMotor.engineDamageAccum = inMotor.engineDamageAccum;
        backupMotor._1_wMaxAllowed = inMotor._1_wMaxAllowed;
        backupMotor._1_wMax = inMotor._1_wMax;
        backupMotor.RPMMin = inMotor.RPMMin;
        backupMotor.RPMNom = inMotor.RPMNom;
        backupMotor.RPMMax = inMotor.RPMMax;
        backupMotor.Vopt = inMotor.Vopt;
        backupMotor.pressureExtBar = inMotor.pressureExtBar;
        backupMotor.momForFuel = inMotor.momForFuel;
        backupMotor.addVflow = inMotor.addVflow;
        backupMotor.addVside = inMotor.addVside;
        backupMotor.propReductor = inMotor.propReductor;
        backupMotor.propAngleDeviceType = inMotor.propAngleDeviceType;
        backupMotor.propAngleDeviceMinParam = inMotor.propAngleDeviceMinParam;
        backupMotor.propAngleDeviceMaxParam = inMotor.propAngleDeviceMaxParam;
        backupMotor.propAngleDeviceAfterburnerParam = inMotor.propAngleDeviceAfterburnerParam;
        backupMotor.propDirection = inMotor.propDirection;
        backupMotor.propDiameter = inMotor.propDiameter;
        backupMotor.propMass = inMotor.propMass;
        backupMotor.propI = inMotor.propI;
        backupMotor.propSEquivalent = inMotor.propSEquivalent;
        backupMotor.propr = inMotor.propr;
        backupMotor.propPhiMin = inMotor.propPhiMin;
        backupMotor.propPhiMax = inMotor.propPhiMax;
        backupMotor.propPhi = inMotor.propPhi;
        backupMotor.propPhiW = inMotor.propPhiW;
        backupMotor.propAoA = inMotor.propAoA;
        backupMotor.propAoA0 = inMotor.propAoA0;
        backupMotor.propAoACrit = inMotor.propAoACrit;
        backupMotor.propAngleChangeSpeed = inMotor.propAngleChangeSpeed;
        backupMotor.propForce = inMotor.propForce;
        backupMotor.propMoment = inMotor.propMoment;
        backupMotor.propTarget = inMotor.propTarget;
        backupMotor.mixerType = inMotor.mixerType;
        backupMotor.mixerLowPressureBar = inMotor.mixerLowPressureBar;
        backupMotor.horsePowers = inMotor.horsePowers;
        backupMotor.thrustMax = inMotor.thrustMax;
        backupMotor.cylindersOperable = inMotor.cylindersOperable;
        backupMotor.engineI = inMotor.engineI;
        backupMotor.engineAcceleration = inMotor.engineAcceleration;
        backupMotor.bIsAutonomous = inMotor.bIsAutonomous;
        backupMotor.bIsMaster = inMotor.bIsMaster;
        backupMotor.bIsStuck = inMotor.bIsStuck;
        backupMotor.bIsInoperable = inMotor.bIsInoperable;
        backupMotor.bIsAngleDeviceOperational = inMotor.bIsAngleDeviceOperational;
        backupMotor.isPropAngleDeviceHydroOperable = inMotor.isPropAngleDeviceHydroOperable;
        backupMotor.engineCarburetorType = inMotor.engineCarburetorType;
        backupMotor.FuelConsumptionP0 = inMotor.FuelConsumptionP0;
        backupMotor.FuelConsumptionP05 = inMotor.FuelConsumptionP05;
        backupMotor.FuelConsumptionP1 = inMotor.FuelConsumptionP1;
        backupMotor.FuelConsumptionPMAX = inMotor.FuelConsumptionPMAX;
        backupMotor.compressorType = inMotor.compressorType;
        backupMotor.compressorMaxStep = inMotor.compressorMaxStep;
        backupMotor.compressorPMax = inMotor.compressorPMax;
        backupMotor.compressorManifoldPressure = inMotor.compressorManifoldPressure;
        backupMotor.compressorRPMtoP0 = inMotor.compressorRPMtoP0;
        backupMotor.compressorRPMtoCurvature = inMotor.compressorRPMtoCurvature;
        backupMotor.compressorRPMtoPMax = inMotor.compressorRPMtoPMax;
        backupMotor.compressorRPMtoWMaxATA = inMotor.compressorRPMtoWMaxATA;
        backupMotor.compressorSpeedManifold = inMotor.compressorSpeedManifold;
        backupMotor.nOfCompPoints = inMotor.nOfCompPoints;
        backupMotor.compressorStepFound = inMotor.compressorStepFound;
        backupMotor.compressorManifoldThreshold = inMotor.compressorManifoldThreshold;
        backupMotor.afterburnerCompressorFactor = inMotor.afterburnerCompressorFactor;
        backupMotor._1_P0 = inMotor._1_P0;
        backupMotor.compressor1stThrottle = inMotor.compressor1stThrottle;
        backupMotor.compressor2ndThrottle = inMotor.compressor2ndThrottle;
        backupMotor.compressorPAt0 = inMotor.compressorPAt0;
        backupMotor.afterburnerType = inMotor.afterburnerType;
        backupMotor.afterburnerChangeW = inMotor.afterburnerChangeW;
        backupMotor.stage = inMotor.stage;
        backupMotor.oldStage = inMotor.oldStage;
        backupMotor.timer = inMotor.timer;
        backupMotor.given = inMotor.given;
        backupMotor.rpm = inMotor.rpm;
        backupMotor.w = inMotor.w;
        backupMotor.aw = inMotor.aw;
        backupMotor.oldW = inMotor.oldW;
        backupMotor.readyness = inMotor.readyness;
        backupMotor.oldReadyness = inMotor.oldReadyness;
        backupMotor.radiatorReadyness = inMotor.radiatorReadyness;
        backupMotor.rearRush = inMotor.rearRush;
        backupMotor.tOilIn = inMotor.tOilIn;
        backupMotor.tOilOut = inMotor.tOilOut;
        backupMotor.tWaterOut = inMotor.tWaterOut;
        backupMotor.tCylinders = inMotor.tCylinders;
        backupMotor.tWaterCritMin = inMotor.tWaterCritMin;
        backupMotor.tWaterCritMax = inMotor.tWaterCritMax;
        backupMotor.tOilCritMin = inMotor.tOilCritMin;
        backupMotor.tOilCritMax = inMotor.tOilCritMax;
        backupMotor.tWaterMaxRPM = inMotor.tWaterMaxRPM;
        backupMotor.tOilOutMaxRPM = inMotor.tOilOutMaxRPM;
        backupMotor.tOilInMaxRPM = inMotor.tOilInMaxRPM;
        backupMotor.tChangeSpeed = inMotor.tChangeSpeed;
        backupMotor.timeOverheat = inMotor.timeOverheat;
        backupMotor.timeUnderheat = inMotor.timeUnderheat;
        backupMotor.timeCounter = inMotor.timeCounter;
        backupMotor.oilMass = inMotor.oilMass;
        backupMotor.waterMass = inMotor.waterMass;
        backupMotor.Ptermo = inMotor.Ptermo;
        backupMotor.R_air = inMotor.R_air;
        backupMotor.R_oil = inMotor.R_oil;
        backupMotor.R_water = inMotor.R_water;
        backupMotor.R_cyl_oil = inMotor.R_cyl_oil;
        backupMotor.R_cyl_water = inMotor.R_cyl_water;
        backupMotor.C_eng = inMotor.C_eng;
        backupMotor.C_oil = inMotor.C_oil;
        backupMotor.C_water = inMotor.C_water;
        backupMotor.bHasThrottleControl = inMotor.bHasThrottleControl;
        backupMotor.bHasAfterburnerControl = inMotor.bHasAfterburnerControl;
        backupMotor.bHasPropControl = inMotor.bHasPropControl;
        backupMotor.bHasRadiatorControl = inMotor.bHasRadiatorControl;
        backupMotor.bHasMixControl = inMotor.bHasMixControl;
        backupMotor.bHasMagnetoControl = inMotor.bHasMagnetoControl;
        backupMotor.bHasExtinguisherControl = inMotor.bHasExtinguisherControl;
        backupMotor.bHasCompressorControl = inMotor.bHasCompressorControl;
        backupMotor.bHasFeatherControl = inMotor.bHasFeatherControl;
        backupMotor.extinguishers = inMotor.extinguishers;
        backupMotor.controlThrottle = inMotor.controlThrottle;
        backupMotor.controlRadiator = inMotor.controlRadiator;
        backupMotor.controlAfterburner = inMotor.controlAfterburner;
        backupMotor.controlProp = inMotor.controlProp;
        backupMotor.bControlPropAuto = inMotor.bControlPropAuto;
        backupMotor.controlMix = inMotor.controlMix;
        backupMotor.controlMagneto = inMotor.controlMagneto;
        backupMotor.controlCompressor = inMotor.controlCompressor;
        backupMotor.controlFeather = inMotor.controlFeather;
        backupMotor.fastATA = inMotor.fastATA;
        backupMotor.updateStep = inMotor.updateStep;
        backupMotor.updateLast = inMotor.updateLast;
        backupMotor.fricCoeffT = inMotor.fricCoeffT;
        backupMotor.engineNoFuelHUDLogId = inMotor.engineNoFuelHUDLogId;

        return backupMotor;
    }

    /**
     * Method restores currentMotor to it's original (backed up) state.
     *
     * @param currentMotor
     * @param backupMotor
     */
    public static void restoreMotor(Motor currentMotor, Motor backupMotor) {
        currentMotor.bMagnetos = cloneBooleanArray(backupMotor.bMagnetos);

        currentMotor.compressorAltitudes = cloneFloatArray(backupMotor.compressorAltitudes);
        currentMotor.compressorPressure = cloneFloatArray(backupMotor.compressorPressure);
        currentMotor.compressorAltMultipliers = cloneFloatArray(backupMotor.compressorAltMultipliers);

        currentMotor.compressorRPM = cloneFloatArray(backupMotor.compressorRPM);
        currentMotor.compressorATA = cloneFloatArray(backupMotor.compressorATA);

        currentMotor.soundName = backupMotor.soundName;
        currentMotor.startStopName = backupMotor.startStopName;
        currentMotor.propName = backupMotor.propName;
        currentMotor.number = backupMotor.number;
        currentMotor.type = backupMotor.type;
        currentMotor.cylinders = backupMotor.cylinders;
        currentMotor.engineMass = backupMotor.engineMass;
        currentMotor.wMin = backupMotor.wMin;
        currentMotor.wNom = backupMotor.wNom;
        currentMotor.wMax = backupMotor.wMax;
        currentMotor.wWEP = backupMotor.wWEP;
        currentMotor.wMaxAllowed = backupMotor.wMaxAllowed;
        currentMotor.wNetPrev = backupMotor.wNetPrev;
        currentMotor.engineMoment = backupMotor.engineMoment;
        currentMotor.engineMomentMax = backupMotor.engineMomentMax;
        currentMotor.engineBoostFactor = backupMotor.engineBoostFactor;
        currentMotor.engineAfterburnerBoostFactor = backupMotor.engineAfterburnerBoostFactor;
        currentMotor.engineDistAM = backupMotor.engineDistAM;
        currentMotor.engineDistBM = backupMotor.engineDistBM;
        currentMotor.engineDistCM = backupMotor.engineDistCM;
        currentMotor.producedDistabilisation = backupMotor.producedDistabilisation;
        currentMotor.bRan = backupMotor.bRan;
        currentMotor.engineDamageAccum = backupMotor.engineDamageAccum;
        currentMotor._1_wMaxAllowed = backupMotor._1_wMaxAllowed;
        currentMotor._1_wMax = backupMotor._1_wMax;
        currentMotor.RPMMin = backupMotor.RPMMin;
        currentMotor.RPMNom = backupMotor.RPMNom;
        currentMotor.RPMMax = backupMotor.RPMMax;
        currentMotor.Vopt = backupMotor.Vopt;
        currentMotor.pressureExtBar = backupMotor.pressureExtBar;
        currentMotor.momForFuel = backupMotor.momForFuel;
        currentMotor.addVflow = backupMotor.addVflow;
        currentMotor.addVside = backupMotor.addVside;
        currentMotor.propReductor = backupMotor.propReductor;
        currentMotor.propAngleDeviceType = backupMotor.propAngleDeviceType;
        currentMotor.propAngleDeviceMinParam = backupMotor.propAngleDeviceMinParam;
        currentMotor.propAngleDeviceMaxParam = backupMotor.propAngleDeviceMaxParam;
        currentMotor.propAngleDeviceAfterburnerParam = backupMotor.propAngleDeviceAfterburnerParam;
        currentMotor.propDirection = backupMotor.propDirection;
        currentMotor.propDiameter = backupMotor.propDiameter;
        currentMotor.propMass = backupMotor.propMass;
        currentMotor.propI = backupMotor.propI;
        currentMotor.propSEquivalent = backupMotor.propSEquivalent;
        currentMotor.propr = backupMotor.propr;
        currentMotor.propPhiMin = backupMotor.propPhiMin;
        currentMotor.propPhiMax = backupMotor.propPhiMax;
        currentMotor.propPhi = backupMotor.propPhi;
        currentMotor.propPhiW = backupMotor.propPhiW;
        currentMotor.propAoA = backupMotor.propAoA;
        currentMotor.propAoA0 = backupMotor.propAoA0;
        currentMotor.propAoACrit = backupMotor.propAoACrit;
        currentMotor.propAngleChangeSpeed = backupMotor.propAngleChangeSpeed;
        currentMotor.propForce = backupMotor.propForce;
        currentMotor.propMoment = backupMotor.propMoment;
        currentMotor.propTarget = backupMotor.propTarget;
        currentMotor.mixerType = backupMotor.mixerType;
        currentMotor.mixerLowPressureBar = backupMotor.mixerLowPressureBar;
        currentMotor.horsePowers = backupMotor.horsePowers;
        currentMotor.thrustMax = backupMotor.thrustMax;
        currentMotor.cylindersOperable = backupMotor.cylindersOperable;
        currentMotor.engineI = backupMotor.engineI;
        currentMotor.engineAcceleration = backupMotor.engineAcceleration;
        currentMotor.bIsAutonomous = backupMotor.bIsAutonomous;
        currentMotor.bIsMaster = backupMotor.bIsMaster;
        currentMotor.bIsStuck = backupMotor.bIsStuck;
        currentMotor.bIsInoperable = backupMotor.bIsInoperable;
        currentMotor.bIsAngleDeviceOperational = backupMotor.bIsAngleDeviceOperational;
        currentMotor.isPropAngleDeviceHydroOperable = backupMotor.isPropAngleDeviceHydroOperable;
        currentMotor.engineCarburetorType = backupMotor.engineCarburetorType;
        currentMotor.FuelConsumptionP0 = backupMotor.FuelConsumptionP0;
        currentMotor.FuelConsumptionP05 = backupMotor.FuelConsumptionP05;
        currentMotor.FuelConsumptionP1 = backupMotor.FuelConsumptionP1;
        currentMotor.FuelConsumptionPMAX = backupMotor.FuelConsumptionPMAX;
        currentMotor.compressorType = backupMotor.compressorType;
        currentMotor.compressorMaxStep = backupMotor.compressorMaxStep;
        currentMotor.compressorPMax = backupMotor.compressorPMax;
        currentMotor.compressorManifoldPressure = backupMotor.compressorManifoldPressure;
        currentMotor.compressorRPMtoP0 = backupMotor.compressorRPMtoP0;
        currentMotor.compressorRPMtoCurvature = backupMotor.compressorRPMtoCurvature;
        currentMotor.compressorRPMtoPMax = backupMotor.compressorRPMtoPMax;
        currentMotor.compressorRPMtoWMaxATA = backupMotor.compressorRPMtoWMaxATA;
        currentMotor.compressorSpeedManifold = backupMotor.compressorSpeedManifold;
        currentMotor.nOfCompPoints = backupMotor.nOfCompPoints;
        currentMotor.compressorStepFound = backupMotor.compressorStepFound;
        currentMotor.compressorManifoldThreshold = backupMotor.compressorManifoldThreshold;
        currentMotor.afterburnerCompressorFactor = backupMotor.afterburnerCompressorFactor;
        currentMotor._1_P0 = backupMotor._1_P0;
        currentMotor.compressor1stThrottle = backupMotor.compressor1stThrottle;
        currentMotor.compressor2ndThrottle = backupMotor.compressor2ndThrottle;
        currentMotor.compressorPAt0 = backupMotor.compressorPAt0;
        currentMotor.afterburnerType = backupMotor.afterburnerType;
        currentMotor.afterburnerChangeW = backupMotor.afterburnerChangeW;
        currentMotor.stage = backupMotor.stage;
        currentMotor.oldStage = backupMotor.oldStage;
        currentMotor.timer = backupMotor.timer;
        currentMotor.given = backupMotor.given;
        currentMotor.rpm = backupMotor.rpm;
        currentMotor.w = backupMotor.w;
        currentMotor.aw = backupMotor.aw;
        currentMotor.oldW = backupMotor.oldW;
        currentMotor.readyness = backupMotor.readyness;
        currentMotor.oldReadyness = backupMotor.oldReadyness;
        currentMotor.radiatorReadyness = backupMotor.radiatorReadyness;
        currentMotor.rearRush = backupMotor.rearRush;
        currentMotor.tOilIn = backupMotor.tOilIn;
        currentMotor.tOilOut = backupMotor.tOilOut;
        currentMotor.tWaterOut = backupMotor.tWaterOut;
        currentMotor.tCylinders = backupMotor.tCylinders;
        currentMotor.tWaterCritMin = backupMotor.tWaterCritMin;
        currentMotor.tWaterCritMax = backupMotor.tWaterCritMax;
        currentMotor.tOilCritMin = backupMotor.tOilCritMin;
        currentMotor.tOilCritMax = backupMotor.tOilCritMax;
        currentMotor.tWaterMaxRPM = backupMotor.tWaterMaxRPM;
        currentMotor.tOilOutMaxRPM = backupMotor.tOilOutMaxRPM;
        currentMotor.tOilInMaxRPM = backupMotor.tOilInMaxRPM;
        currentMotor.tChangeSpeed = backupMotor.tChangeSpeed;
        currentMotor.timeOverheat = backupMotor.timeOverheat;
        currentMotor.timeUnderheat = backupMotor.timeUnderheat;
        currentMotor.timeCounter = backupMotor.timeCounter;
        currentMotor.oilMass = backupMotor.oilMass;
        currentMotor.waterMass = backupMotor.waterMass;
        currentMotor.Ptermo = backupMotor.Ptermo;
        currentMotor.R_air = backupMotor.R_air;
        currentMotor.R_oil = backupMotor.R_oil;
        currentMotor.R_water = backupMotor.R_water;
        currentMotor.R_cyl_oil = backupMotor.R_cyl_oil;
        currentMotor.R_cyl_water = backupMotor.R_cyl_water;
        currentMotor.C_eng = backupMotor.C_eng;
        currentMotor.C_oil = backupMotor.C_oil;
        currentMotor.C_water = backupMotor.C_water;
        currentMotor.bHasThrottleControl = backupMotor.bHasThrottleControl;
        currentMotor.bHasAfterburnerControl = backupMotor.bHasAfterburnerControl;
        currentMotor.bHasPropControl = backupMotor.bHasPropControl;
        currentMotor.bHasRadiatorControl = backupMotor.bHasRadiatorControl;
        currentMotor.bHasMixControl = backupMotor.bHasMixControl;
        currentMotor.bHasMagnetoControl = backupMotor.bHasMagnetoControl;
        currentMotor.bHasExtinguisherControl = backupMotor.bHasExtinguisherControl;
        currentMotor.bHasCompressorControl = backupMotor.bHasCompressorControl;
        currentMotor.bHasFeatherControl = backupMotor.bHasFeatherControl;
        currentMotor.extinguishers = backupMotor.extinguishers;
        currentMotor.controlThrottle = backupMotor.controlThrottle;
        currentMotor.controlRadiator = backupMotor.controlRadiator;
        currentMotor.controlAfterburner = backupMotor.controlAfterburner;
        currentMotor.controlProp = backupMotor.controlProp;
        currentMotor.bControlPropAuto = backupMotor.bControlPropAuto;
        currentMotor.controlMix = backupMotor.controlMix;
        currentMotor.controlMagneto = backupMotor.controlMagneto;
        currentMotor.controlCompressor = backupMotor.controlCompressor;
        currentMotor.controlFeather = backupMotor.controlFeather;
        currentMotor.fastATA = backupMotor.fastATA;
        currentMotor.updateStep = backupMotor.updateStep;
        currentMotor.updateLast = backupMotor.updateLast;
        currentMotor.fricCoeffT = backupMotor.fricCoeffT;
        currentMotor.engineNoFuelHUDLogId = backupMotor.engineNoFuelHUDLogId;
    }

    private static float[] cloneFloatArray(float[] inArray) {
        float[] array = new float[inArray.length];
        for (int i = 0; i < array.length; i++)
            array[i] = inArray[i];

        return array;
    }

    private static boolean[] cloneBooleanArray(boolean[] inArray) {
        boolean[] array = new boolean[inArray.length];
        for (int i = 0; i < array.length; i++)
            array[i] = inArray[i];

        return array;
    }

    /**
     * Method will adjust user aircraft by taking current aircraft values and reduce the specified amounts from them. Result is mirror image of what home base/player side has to offer. In short: METHOD INSTRUCTS HOW MUCH RESOURCES TO UNLOAD FROM AIRCRAFT!
     *
     * @param bullets
     * @param bombs
     * @param rockets
     * @param fuel
     * @param cargo
     */
    public static void updateSpawnResources(long bullets, int[] bombs, long rockets, float fuel, long cargo) {
        updateSpawnResources(bullets, bombs, rockets, fuel, cargo, World.getPlayerAircraft());
    }
    
    /**
     * Method will adjust given aircraft by taking current aircraft values and reduce the specified amounts from them. Result is mirror image of what home base/player side has to offer. In short: METHOD INSTRUCTS HOW MUCH RESOURCES TO UNLOAD FROM AIRCRAFT!
     *
     * @param bullets
     * @param bombs
     * @param rockets
     * @param fuel
     * @param cargo
     * @param aircraft
     */
    public static void updateSpawnResources(long bullets, int[] bombsIn, long rockets, float fuel, long cargo, Aircraft aircraft) {
        int[] bombs = ZutiSupportMethods.cloneIntegerArray(bombsIn);
        if (curDebugLevel() != 0) {
            StringBuffer sb = new StringBuffer();
            sb.append("{");
            for (int i=0; i<bombs.length; i++) {
                if (sb.length() > 1) sb.append(", ");
                sb.append(bombs[i]);
            }
            sb.append("}");
            printDebugMessage("updateSpawnResources(" + bullets + ", " + sb.toString() + ", " + rockets + ", " + fuel + ", " + cargo + "), aircraft=" + (aircraft==null?"null":aircraft.name()));
        }
        if (aircraft == null) return;

        // Adjust bullets, bombs, rockets
        BulletEmitter[][] weapons = aircraft.FM.CT.Weapons;
        for (int i = 0; i < weapons.length; i++) {
            if (weapons[i] == null) continue;

            try {
                for (int j = 0; j < weapons[i].length; j++)
                    if (weapons[i][j] instanceof GunGeneric) {
                        int currentBullets = ((GunGeneric) weapons[i][j]).countBullets();
                        // Adjust bullets counters accordingly
                        if (bullets > currentBullets) {
                            bullets -= currentBullets;
                            currentBullets = 0;

                            ((GunGeneric) weapons[i][j]).loadBullets(currentBullets);
                        } else if (bullets > 0) {
                            currentBullets -= bullets;
                            bullets = 0;

                            ((GunGeneric) weapons[i][j]).loadBullets(currentBullets);
                        }
                    } else if (weapons[i][j] instanceof RocketGun) {
                        int currentRockets = ((RocketGun) weapons[i][j]).countBullets();
                        // Adjust bullets counters accordingly
                        if (rockets > currentRockets) {
                            rockets -= currentRockets;
                            currentRockets = 0;

                            ((RocketGun) weapons[i][j]).loadBullets(currentRockets);
                        } else if (rockets > 0) {
                            currentRockets -= rockets;
                            rockets = 0;

                            ((RocketGun) weapons[i][j]).loadBullets(currentRockets);
                        }
                    } else if (weapons[i][j] instanceof BombGun) {
                        String className = Property.get(weapons[i][j].getClass(), "bulletClass").stringValue();
                        className = className.substring(className.indexOf("class ") + 6, className.length());

                        if (className.indexOf(ZutiSupportMethods_Builder.BOMB_CARGO_NAME) > -1) {
                            int currentCargo = ((BombGun) weapons[i][j]).countBullets();

                            if (cargo > currentCargo) {
                                // Reduce cargo count for current loaded amount
                                cargo -= currentCargo;
                                // Set currentCargo to 0 = means to unload all cargo
                                currentCargo = 0;

                                ((BombGun) weapons[i][j]).loadBullets(currentCargo);
                            } else if (cargo > 0) {
                                // Load only available cargo amount
                                currentCargo -= cargo;
                                // Set bomb counter to 0
                                cargo = 0;

                                ((BombGun) weapons[i][j]).loadBullets(currentCargo);
                            }
                            continue;
                        }

                        Class weaponClass = Class.forName(className);
                        float weaponPower = Property.floatValue(weaponClass, "power", -1F);

                        int currentBombs = ((BombGun) weapons[i][j]).countBullets();

                        if (weaponPower <= 250F) {
                            if (bombs[0] > currentBombs) {
                                // Reduce bombs count for current loaded amount
                                bombs[0] -= currentBombs;
                                // Set currentBombs to 0 = means to unload all bombs
                                currentBombs = 0;

                                ((BombGun) weapons[i][j]).loadBullets(currentBombs);
                            } else if (bombs[0] > 0) {
                                // Load only available bomb amount
                                currentBombs -= bombs[0];
                                // Set bomb counter to 0
                                bombs[0] = 0;

                                ((BombGun) weapons[i][j]).loadBullets(currentBombs);
                            }
                        } else if (weaponPower <= 500F) {
                            if (bombs[1] > currentBombs) {
                                // Reduce bombs count for current loaded amount
                                bombs[1] -= currentBombs;
                                // Set currentBombs to 0 = means to unload all bombs
                                currentBombs = 0;

                                ((BombGun) weapons[i][j]).loadBullets(currentBombs);
                            } else if (bombs[1] > 0) {
                                // Load only available bomb amount
                                currentBombs -= bombs[1];
                                // Set bomb counter to 0
                                bombs[1] = 0;

                                ((BombGun) weapons[i][j]).loadBullets(currentBombs);
                            }
                        } else if (weaponPower <= 1000F) {
                            if (bombs[2] > currentBombs) {
                                // Reduce bombs count for current loaded amount
                                bombs[2] -= currentBombs;
                                // Set currentBombs to 0 = means to unload all bombs
                                currentBombs = 0;

                                ((BombGun) weapons[i][j]).loadBullets(currentBombs);
                            } else if (bombs[2] > 0) {
                                // Load only available bomb amount
                                currentBombs -= bombs[2];
                                // Set bomb counter to 0
                                bombs[2] = 0;

                                ((BombGun) weapons[i][j]).loadBullets(currentBombs);
                            }
                        } else if (weaponPower <= 2000F) {
                            if (bombs[3] > currentBombs) {
                                // Reduce bombs count for current loaded amount
                                bombs[3] -= currentBombs;
                                // Set currentBombs to 0 = means to unload all bombs
                                currentBombs = 0;

                                ((BombGun) weapons[i][j]).loadBullets(currentBombs);
                            } else if (bombs[3] > 0) {
                                // Load only available bomb amount
                                currentBombs -= bombs[3];
                                // Set bomb counter to 0
                                bombs[3] = 0;

                                ((BombGun) weapons[i][j]).loadBullets(currentBombs);
                            }
                        } else if (weaponPower <= 5000F) {
                            if (bombs[4] > currentBombs) {
                                // Reduce bombs count for current loaded amount
                                bombs[4] -= currentBombs;
                                // Set currentBombs to 0 = means to unload all bombs
                                currentBombs = 0;

                                ((BombGun) weapons[i][j]).loadBullets(currentBombs);
                            } else if (bombs[4] > 0) {
                                // Load only available bomb amount
                                currentBombs -= bombs[4];
                                // Set bomb counter to 0
                                bombs[4] = 0;

                                ((BombGun) weapons[i][j]).loadBullets(currentBombs);
                            }
                        } else if (bombs[5] > currentBombs) {
                            // Reduce bombs count for current loaded amount
                            bombs[5] -= currentBombs;
                            // Set currentBombs to 0 = means to unload all bombs
                            currentBombs = 0;

                            ((BombGun) weapons[i][j]).loadBullets(currentBombs);
                        } else if (bombs[5] > 0) {
                            // Load only available bomb amount
                            currentBombs -= bombs[5];
                            // Set bomb counter to 0
                            bombs[5] = 0;

                            ((BombGun) weapons[i][j]).loadBullets(currentBombs);
                        }
                    }
            } catch (Exception ex) {}
        }

        // Reset fuel tanks statuses (external tanks!).
        aircraft.FM.M.onFuelTanksChanged();

        // Adjust fuel
        aircraft.FM.M.fuel -= fuel;
    }

    /**
     * Method releases players airfield stay point.
     */
    public static void updateDeckTimer() {
        if (UPDATE_DECK_TIMER) {
            long currentTime = Time.current();

            if (DECK_CLEAR_TIME <= 0L) {
                ZutiSupportMethods_NetSend.releaseCarrierSpawnPlace((NetUser) NetEnv.host());
                HUD.logCenter("mds.deck.clearedForce");
                UPDATE_DECK_TIMER = false;

                ZutiInterpolator.ZUTI_DESTROY_PLAYER_AC = true;
            } else if (currentTime - DECK_LAST_REFRESH > 2000) {
                DECK_LAST_REFRESH = currentTime;
                DECK_CLEAR_TIME -= 2000;

                if (ZutiSupportMethods_Ships.isAircraftOnDeck(World.getPlayerAircraft(), 15D)) {
                    if (SHOW_DECK_CLEAR_MESSAGE) {
                        HUD.logCenter("mds.deck.clear");
                        SHOW_DECK_CLEAR_MESSAGE = false;
                    } else {
                        HUD.logCenter(new Long(DECK_CLEAR_TIME / 1000).toString());
                        SHOW_DECK_CLEAR_MESSAGE = true;
                    }
                } else {
                    ZutiSupportMethods_NetSend.releaseCarrierSpawnPlace((NetUser) NetEnv.host());
                    HUD.logCenter("mds.deck.cleared");
                    UPDATE_DECK_TIMER = false;
                }
            }
        }
    }

    /**
     * Update timer in case player started on carrier deck.
     */
    public static void updateVulnerabilityTimer() {
        if (ZutiSupportMethods_FM.UPDATE_VULNERABILITY_TIMER) if (Time.current() - VULNERABILITY_LAST_REFRESH > 2000) {
            // System.out.println("CHECKING VULNERABILITY: " + Time.current());
            ZutiSupportMethods_FM.VULNERABILITY_LAST_REFRESH = Time.current();
            if (!ZutiSupportMethods_Ships.isAircraftOnDeck(World.getPlayerAircraft(), 50.0D)) {
                ZutiSupportMethods_FM.UPDATE_VULNERABILITY_TIMER = false;
                ZutiSupportMethods_FM.IS_PLAYER_VULNERABLE = true;
            }
        }
    }

    // Lutz mod
    public static void ZoneDef(java.lang.String s, int i, float[] Xcenter, float[] Ycenter, float[] LenghtZone, float[] WidthZone, float[] Zmin, float[] Zmax) {
        float af[] = new float[i + 1];
        float af1[] = new float[i + 1];
        float af2[] = new float[i + 1];
        float af3[] = new float[i + 1];
        float af4[] = new float[i + 1];
        float af5[] = new float[i + 1];
        if (i > 0) for (int j = 0; j < i; j++) {
            af[j] = Xcenter[j];
            af1[j] = Ycenter[j];
            af2[j] = LenghtZone[j];
            af3[j] = WidthZone[j];
            af4[j] = Zmin[j];
            af5[j] = Zmax[j];
        }
        af[i] = 0.0F;
        af1[i] = 0.0F;
        af2[i] = 500000F;
        af3[i] = 500000F;
        af4[i] = 0.0F;
        af5[i] = 20000F;
        java.util.StringTokenizer stringtokenizer = new StringTokenizer(s, " ");
        java.lang.String s1 = stringtokenizer.nextToken();
        if (s1 != null) af[i] = java.lang.Float.parseFloat(s1);
        s1 = stringtokenizer.nextToken();
        if (s1 != null) af1[i] = java.lang.Float.parseFloat(s1);
        s1 = stringtokenizer.nextToken();
        if (s1 != null) af2[i] = java.lang.Float.parseFloat(s1) / 2.0F;
        s1 = stringtokenizer.nextToken();
        if (s1 != null) af3[i] = java.lang.Float.parseFloat(s1) / 2.0F;
        s1 = stringtokenizer.nextToken();
        if (s1 != null) af4[i] = java.lang.Float.parseFloat(s1);
        s1 = stringtokenizer.nextToken();
        if (s1 != null) af5[i] = java.lang.Float.parseFloat(s1);
        Xcenter = new float[i + 1];
        Ycenter = new float[i + 1];
        LenghtZone = new float[i + 1];
        WidthZone = new float[i + 1];
        Zmin = new float[i + 1];
        Zmax = new float[i + 1];
        for (int k = 0; k <= i; k++) {
            Xcenter[k] = af[k];
            Ycenter[k] = af1[k];
            LenghtZone[k] = af2[k];
            WidthZone[k] = af3[k];
            Zmin[k] = af4[k];
            Zmax[k] = af5[k];
        }
    }
    
    private static int       debugLevel    = Integer.MIN_VALUE;
    private static final int DEBUG_DEFAULT = 0;

    private static int curDebugLevel() {
        if (debugLevel == Integer.MIN_VALUE) debugLevel = Config.cur.ini.get("Mods", "DEBUG_ZSM_FM", DEBUG_DEFAULT);
        return debugLevel;
    }

    public static void printDebugMessage(String theMessage) {
        if (curDebugLevel() == 0) return;
        System.out.println("ZutiSupportMethods_FM - " + theMessage);
    }
}