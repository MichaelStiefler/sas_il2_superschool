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

public class ZutiRearm_UnloadFuel {
    private long      startTime            = 0;
    private long      secondInterval       = 0;
    private float     refuelTime           = 0;
    private float     calculatedRefuelRate = 0.0F;
    private Aircraft  playerAC             = null;
    private BornPlace bornPlace            = null;

    private float     unloadedFuel         = 0.0F;

    public ZutiRearm_UnloadFuel() {
        this.playerAC = World.getPlayerAircraft();
        this.bornPlace = ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(this.playerAC.FM);

        this.calculateRearmingTimeConsumption();

        HUD.log("mds.unloadingFuelTime", new java.lang.Object[] { new Integer(Math.round(this.refuelTime)) });

        this.startTime = Time.current();
        this.secondInterval = this.startTime;
        this.refuelTime = this.refuelTime * 1000;
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
            if (Time.current() - this.secondInterval > 1000) {
                this.zutiUnloadFuel(this.calculatedRefuelRate);
                this.secondInterval = Time.current();

                if (Time.current() - this.startTime > this.refuelTime) {
                    ZutiSupportMethods_AI.collectPoints();
                    this.stopTimer();

                    return -1;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    private void calculateRearmingTimeConsumption() {
        this.calculatedRefuelRate = Mission.MDS_VARIABLES().zutiReload_GallonsLitersPerSecond;

        if (this.bornPlace != null && this.bornPlace.zutiEnableResourcesManagement) {
            this.calculatedRefuelRate = this.bornPlace.zutiGallonsLitersPerSecond;
            System.out.println("-== Refueling with HB specific RRR settings! ==-");
        }

        this.refuelTime = this.playerAC.FM.M.fuel / this.calculatedRefuelRate;
        if (this.calculatedRefuelRate < 0) this.calculatedRefuelRate = 0;

        System.out.println("Calculated unloading fuel time: " + Math.round(this.refuelTime) + "s.");
    }

    private void zutiUnloadFuel(float fuel) {
        if (this.playerAC.FM.M.fuel - fuel >= 0.0F) {
            this.playerAC.FM.M.fuel -= fuel;
            this.unloadedFuel += fuel;
        }
    }

    private void stopTimer() {
        com.maddox.il2.game.HUD.log("mds.unloadingFuelDone");
        System.out.println("Unloading o fuel done!");

        this.sync();
    }

    public void cancelTimer() {
        com.maddox.il2.game.HUD.log("mds.unloadingFuelAborted");
        System.out.println("Unloading of fuel aborted!!!");

        this.sync();
    }

    private void sync() {
        String userData = ZutiSupportMethods.getAircraftCompleteName(World.getPlayerAircraft());
        String userLocation = ZutiSupportMethods.getPlayerLocation();
        ZutiSupportMethods_NetSend.logMessage((NetUser) NetEnv.host(), userData + " unloaded fuel at " + userLocation);

        ZutiWeaponsManagement.returnRemainingAircraftFuel(this.playerAC, this.unloadedFuel);
    }
}