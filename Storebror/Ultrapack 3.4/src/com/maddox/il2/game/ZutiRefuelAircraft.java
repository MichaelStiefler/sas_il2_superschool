//***********************************
//This class is for client side players only
//***********************************
package com.maddox.il2.game;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiSupportMethods_AI;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Time;

public class ZutiRefuelAircraft {
    private long      startTime            = 0;
    private long      secondInterval       = 0;
    private float     refuelTime           = 0;
    private float     calculatedRefuelRate = 0.0F;
    private Aircraft  playerAC             = null;
    private BornPlace bornPlace            = null;

    private float     fuelAtStart          = 0.0F;
    private float     fuelToFill           = 0.0F;
    private float     loadedFuel           = 0.0F;

    public ZutiRefuelAircraft(float refuelPenalty, float fuelToFill) {
        this.fuelToFill = fuelToFill;
        this.playerAC = World.getPlayerAircraft();
        this.bornPlace = ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(this.playerAC.FM);

        this.calculateRearmingTimeConsumption(refuelPenalty);

        HUD.log("mds.refuelingTime", new java.lang.Object[] { new Integer(Math.round(this.refuelTime)) });

        this.startTime = Time.current();
        this.secondInterval = this.startTime;
        this.refuelTime = this.refuelTime * 1000;

        this.loadedFuel = 0F;
    }

    /**
     * if -1 is returned abort execution
     *
     * @return
     */
    public int updateTimer() {
        if (!ZutiSupportMethods.allowRRR(this.playerAC)) {
            this.cancelTimer();

            this.sendLogData();

            return -1;
        }

        try {
            // Refuel each second
            if (Time.current() - this.secondInterval > 1000) {
                this.zutiRefuelAircraft(this.calculatedRefuelRate);
                this.secondInterval = Time.current();

                if (Time.current() - this.startTime > this.refuelTime) {
                    ZutiSupportMethods_AI.collectPoints();
                    this.stopTimer();

                    this.sendLogData();

                    return -1;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    private void calculateRearmingTimeConsumption(float refuelTimePenalty) {
        int refuelingRate = Mission.MDS_VARIABLES().zutiReload_GallonsLitersPerSecond;

        if (this.bornPlace != null) {
            refuelingRate = this.bornPlace.zutiGallonsLitersPerSecond;
            System.out.println("-== Refueling with HB specific RRR settings! ==-");
        }

        this.fuelAtStart = this.playerAC.FM.M.fuel;
        System.out.println(" Current Fuel: " + this.fuelAtStart);
        System.out.println("  Target Fuel: " + this.fuelToFill);
        System.out.println("         Rate: " + refuelingRate + " L/s");
        System.out.println("Penalty Multi: x" + refuelTimePenalty);

        float refuelTimeNoPenalty = this.fuelToFill / refuelingRate;
        this.refuelTime = refuelTimeNoPenalty * refuelTimePenalty;
        this.calculatedRefuelRate = refuelTimeNoPenalty * refuelingRate / this.refuelTime;
        if (this.calculatedRefuelRate < 0) this.calculatedRefuelRate = 0;
        System.out.println("--------------------------------------");
        System.out.println("Calculated refuel rate: " + this.calculatedRefuelRate + " L/s");
        System.out.println("Calculated refuel time: " + Math.round(this.refuelTime) + "s.");
    }

    private void stopTimer() {
        HUD.log("mds.refuelingStatus", new java.lang.Object[] { new Integer(Math.round(this.loadedFuel)), new Integer(Math.round(this.playerAC.FM.M.fuel)) });
        System.out.println("Refueling Done. Loaded >" + this.loadedFuel + "kg< of new fuel. Total: " + this.playerAC.FM.M.fuel + "kg.");
    }

    public void cancelTimer() {
        float unusedFuel = this.fuelToFill - (this.playerAC.FM.M.fuel - this.fuelAtStart);
        System.out.println("Not used fuel: " + unusedFuel + "kg");

        // Return unused fuel
        ZutiSupportMethods_NetSend.returnRRRResources_Fuel(unusedFuel, this.playerAC.pos.getAbsPoint());

        if (World.getPlayerAircraft() != null) HUD.log("mds.refuelingStatus", new java.lang.Object[] { new Integer(Math.round(this.loadedFuel)), new Integer(Math.round(this.playerAC.FM.M.fuel)) });
        else HUD.log("mds.refuelingAborted");

        System.out.println("Refueling aborted. Loaded >" + this.loadedFuel + "kg< of new fuel. Total: " + this.playerAC.FM.M.fuel + "kg.");
    }

    private void zutiRefuelAircraft(float fuel) {
        if (this.playerAC.FM.M.fuel + fuel <= this.playerAC.FM.M.maxFuel) {
            this.playerAC.FM.M.fuel += fuel;

            this.loadedFuel += fuel;
        }

        // System.out.println("Fuel tanks status: " + playerAC.FM.M.fuel);
    }

    private void sendLogData() {
        String userData = ZutiSupportMethods.getAircraftCompleteName(World.getPlayerAircraft());
        String userLocation = ZutiSupportMethods.getPlayerLocation();
        ZutiSupportMethods_NetSend.logMessage((NetUser) NetEnv.host(), userData + " refueled aircraft to >" + this.playerAC.FM.M.fuel + "kg< at " + userLocation);
    }
}