//***********************************
//This class is for client side players only
//***********************************
package com.maddox.il2.game;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiSupportMethods_AI;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Time;

public class ZutiRepairAircraft {
    private long      startTime               = 1;
    private float     repairTime              = 0;
    private float     flapsRepairTime         = 0;
    private float     weaponsRepairTime       = 0;
    private float     fuelOilTanksRepairTime  = 0;
    private float     cockpitRepairTime       = 0;
    private float     controlCablesRepairTime = 0;
    private Aircraft  playerAC                = null;
    private BornPlace bornPlace               = null;

    public ZutiRepairAircraft(float repairPenalty, boolean rearmingInProgress) {
        this.playerAC = World.getPlayerAircraft();
        this.bornPlace = ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(this.playerAC.FM);

        this.calculateRepairingTimeConsumption(repairPenalty, rearmingInProgress);

        HUD.log("mds.repairingTime", new java.lang.Object[] { new Integer(Math.round(this.repairTime)) });

        this.flapsRepairTime *= 1000;
        this.weaponsRepairTime *= 1000;
        this.fuelOilTanksRepairTime *= 1000;
        this.controlCablesRepairTime *= 1000;
        this.cockpitRepairTime *= 1000;
        this.repairTime *= 1000;

        this.startTime = Time.current();
    }

    /**
     * if -1 is returned abort execution
     *
     * @return
     */
    public int updateTimer() {
        if (!ZutiSupportMethods.allowRRR(this.playerAC)) {
            this.cancelTimer();
            return -1;
        }

        try {
            if (this.flapsRepairTime > -1 && Time.current() - this.startTime > this.flapsRepairTime) {
                this.playerAC.FM.CT.bHasFlapsControl = true;
                this.flapsRepairTime = -1;
                com.maddox.il2.game.HUD.log("mds.repairedFlaps");
            }
            if (this.weaponsRepairTime > -1 && Time.current() - this.startTime > this.weaponsRepairTime) {
                this.fixJammedMgsCannons();
                this.weaponsRepairTime = -1;
                com.maddox.il2.game.HUD.log("mds.repairedWeapons");
            }
            if (this.fuelOilTanksRepairTime > -1 && Time.current() - this.startTime > this.fuelOilTanksRepairTime) {
                this.repairFuelOilTanks();
                this.fuelOilTanksRepairTime = -1;
                com.maddox.il2.game.HUD.log("mds.repairedTanks");
            }
            if (this.controlCablesRepairTime > -1 && Time.current() - this.startTime > this.controlCablesRepairTime) {
                this.repairControlCables();
                this.controlCablesRepairTime = -1;
                com.maddox.il2.game.HUD.log("mds.repairedCables");
            }
            if (this.cockpitRepairTime > -1 && Time.current() - this.startTime > this.cockpitRepairTime) {
                this.repairCockpit();
                if (this.cockpitRepairTime > 0) com.maddox.il2.game.HUD.log("mds.repairedCockpit");
                System.out.println("Cockpit(s) repaired!");
                this.cockpitRepairTime = -1;
            }
            if (Time.current() - this.startTime > this.repairTime) {
                this.playerAC.FM.AS.setCockpitState(this.playerAC, 0);

                // Collect earned points
                ZutiSupportMethods_AI.collectPoints();
                this.stopTimer();

                String userData = ZutiSupportMethods.getAircraftCompleteName(World.getPlayerAircraft());
                String userLocation = ZutiSupportMethods.getPlayerLocation();
                ZutiSupportMethods_NetSend.logMessage((NetUser) NetEnv.host(), userData + " repaired aircraft at " + userLocation);

                return -1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    private void calculateRepairingTimeConsumption(float repairTimePenalty, boolean rearmingInProgress) {
        this.calculateFlapsRepairTime();
        this.calculateWeaponsRepairTime(rearmingInProgress);
        this.calculateFuelOilTanksRepairTime();
        this.calculateControlCablesRepairTime();
        this.calculateCockpitRepairTime();

        System.out.println("    Flaps Repair Time: " + this.flapsRepairTime);
        System.out.println("     Guns Repair Time: " + this.weaponsRepairTime);
        System.out.println("F/O Tanks Repair Time: " + this.fuelOilTanksRepairTime);
        System.out.println("  CCables Repair Time: " + this.controlCablesRepairTime);
        System.out.println("  Cockpit Repair Time: " + this.cockpitRepairTime);
        System.out.println("    Repairing Penalty: " + repairTimePenalty);

        this.flapsRepairTime = this.flapsRepairTime * repairTimePenalty;
        this.weaponsRepairTime = this.weaponsRepairTime * repairTimePenalty;
        this.fuelOilTanksRepairTime = this.fuelOilTanksRepairTime * repairTimePenalty;
        this.controlCablesRepairTime = this.controlCablesRepairTime * repairTimePenalty;
        this.cockpitRepairTime = this.cockpitRepairTime * repairTimePenalty;

        this.setTotalRepairTime();

        System.out.println("--------------------------------------");
        System.out.println("Calculated Repair Time: " + this.repairTime);
    }

    private void setTotalRepairTime() {
        this.repairTime = this.flapsRepairTime;
        if (this.weaponsRepairTime > this.repairTime) this.repairTime = this.weaponsRepairTime;
        if (this.fuelOilTanksRepairTime > this.repairTime) this.repairTime = this.fuelOilTanksRepairTime;
        if (this.controlCablesRepairTime > this.repairTime) this.repairTime = this.controlCablesRepairTime;
        if (this.cockpitRepairTime > this.repairTime) this.repairTime = this.cockpitRepairTime;
    }

    private void calculateFlapsRepairTime() {
        this.flapsRepairTime = 0;

        if (!this.playerAC.FM.CT.bHasFlapsControl) {
            this.flapsRepairTime = Mission.MDS_VARIABLES().zutiReload_FlapsRepairSeconds;

            if (this.bornPlace != null) this.flapsRepairTime = this.bornPlace.zutiFlapsRepairSeconds;
        }
    }

    private void calculateWeaponsRepairTime(boolean rearmingInProgress) {
        this.weaponsRepairTime = 0;

        // If rearming is in process, rearming process already unjamed the guns
        if (rearmingInProgress) return;

        com.maddox.il2.ai.BulletEmitter[][] weapons = this.playerAC.FM.CT.Weapons;

        for (int i = 0; i < weapons.length; i++)
            try {
                for (int j = 0; j < weapons[i].length; j++)
                    if (weapons[i][j] instanceof GunGeneric) if (!((GunGeneric) weapons[i][j]).haveBullets()) this.weaponsRepairTime++;
            } catch (Exception ex) {}

        if (this.bornPlace == null) this.weaponsRepairTime = this.weaponsRepairTime * Mission.MDS_VARIABLES().zutiReload_OneWeaponRepairSeconds;
        else this.weaponsRepairTime = this.weaponsRepairTime * this.bornPlace.zutiOneWeaponRepairSeconds;
    }

    private void fixJammedMgsCannons() {
        com.maddox.il2.ai.BulletEmitter[][] weapons = this.playerAC.FM.CT.Weapons;
        Aircraft._WeaponSlot[] weaponSlots = Aircraft.getWeaponSlotsRegistered(this.playerAC.getClass(), ZutiSupportMethods_Air.getCurrentAircraftLoadoutName(this.playerAC));

        for (int i = 0; i < weapons.length; i++)
            try {
                for (int j = 0; j < weapons[i].length; j++)
                    if (weapons[i][j] instanceof GunGeneric) // fixing is not rearming - just load 5% of max allowed bullets per gun :D
                        if (!((GunGeneric) weapons[i][j]).haveBullets()) {
                            GunGeneric gun = (GunGeneric) weapons[i][j];
                            gun.loadBullets(0);
                            gun.loadBullets((int) (0.05 * ZutiWeaponsManagement.getBulletsForWeapon(weapons[i][j].getClass(), weaponSlots)));
                        }
            } catch (Exception ex) {}
    }

    private void calculateFuelOilTanksRepairTime() {
        FlightModel fm = this.playerAC.FM;
        this.fuelOilTanksRepairTime = 0;

        for (int i = 0; i < fm.AS.astateTankStates.length; i++)
            if (fm.AS.astateTankStates[i] > 0) if (this.bornPlace == null) this.fuelOilTanksRepairTime += Mission.MDS_VARIABLES().zutiReload_OneFuelOilTankRepairSeconds;
            else this.fuelOilTanksRepairTime += this.bornPlace.zutiOneFuelOilTankRepairSeconds;

        for (int i = 0; i < fm.AS.astateOilStates.length; i++)
            if (fm.AS.astateOilStates[i] > 0) if (this.bornPlace == null) this.fuelOilTanksRepairTime += Mission.MDS_VARIABLES().zutiReload_OneFuelOilTankRepairSeconds;
            else this.fuelOilTanksRepairTime += this.bornPlace.zutiOneFuelOilTankRepairSeconds;
    }

    private void calculateControlCablesRepairTime() {
        FlightModel fm = this.playerAC.FM;
        this.controlCablesRepairTime = 0;

        if (!fm.CT.bHasAileronControl) if (this.bornPlace == null) this.controlCablesRepairTime += Mission.MDS_VARIABLES().zutiReload_OneControlCableRepairSeconds;
        else this.controlCablesRepairTime += this.bornPlace.zutiOneControlCableRepairSeconds;
        if (!fm.CT.bHasElevatorControl) if (this.bornPlace == null) this.controlCablesRepairTime += Mission.MDS_VARIABLES().zutiReload_OneControlCableRepairSeconds;
        else this.controlCablesRepairTime += this.bornPlace.zutiOneControlCableRepairSeconds;
        if (!fm.CT.bHasRudderControl) if (this.bornPlace == null) this.controlCablesRepairTime += Mission.MDS_VARIABLES().zutiReload_OneControlCableRepairSeconds;
        else this.controlCablesRepairTime += this.bornPlace.zutiOneControlCableRepairSeconds;
    }

    private void calculateCockpitRepairTime() {
        FlightModel fm = this.playerAC.FM;
        this.cockpitRepairTime = 0;

        if (fm.AS.astateCockpitState != 0) if (this.bornPlace == null) this.cockpitRepairTime = Mission.MDS_VARIABLES().zutiReload_CockpitRepairSeconds;
        else this.cockpitRepairTime = this.bornPlace.zutiCockpitRepairSeconds;
    }

    private void repairFuelOilTanks() {
        FlightModel fm = this.playerAC.FM;

        for (int i = 0; i < fm.AS.astateTankStates.length; i++)
            fm.AS.repairTank(i);

        for (int i = 0; i < fm.AS.astateOilStates.length; i++)
            fm.AS.repairOil(i);
    }

    private void repairControlCables() {
        FlightModel fm = this.playerAC.FM;

        fm.CT.bHasAileronControl = true;
        fm.CT.bHasElevatorControl = true;
        fm.CT.bHasRudderControl = true;
    }

    private void repairCockpit() {
        for (int i = 0; i < com.maddox.il2.game.Main3D.cur3D().cockpits.length; i++)
            ZutiSupportMethods_Air.restoreCockpit(Main3D.cur3D().cockpits[i]);
    }

    private void stopTimer() {
        com.maddox.il2.game.HUD.log("mds.repairingDone");
        System.out.println("AC repairs done!");
    }

    public void cancelTimer() {
        // Return repair kit
        ZutiSupportMethods_NetSend.returnRRRResources_RepairKit(1, this.playerAC.pos.getAbsPoint());

        com.maddox.il2.game.HUD.log("mds.repairingAborted");
        System.out.println("AC repairs aborted!!!");
    }
}