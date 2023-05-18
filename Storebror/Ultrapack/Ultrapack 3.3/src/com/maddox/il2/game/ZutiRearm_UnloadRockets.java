//***********************************
//This class is for client side players only
//***********************************
package com.maddox.il2.game;

import com.maddox.il2.ai.World;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Time;

public class ZutiRearm_UnloadRockets {
    private long      startTime            = 0;
    private float     rocketsUnloadingTime = 0;
    private Aircraft  playerAC             = null;

    private BornPlace bornPlace            = null;

    public ZutiRearm_UnloadRockets() {
        this.playerAC = World.getPlayerAircraft();
        this.bornPlace = ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(this.playerAC.FM);

        this.calculateRearmingTimeConsumption();

        HUD.log("mds.unloadingRocketsTime", new java.lang.Object[] { new Integer(Math.round(this.rocketsUnloadingTime)) });

        this.rocketsUnloadingTime *= 1000;

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
            if (this.rocketsUnloadingTime > -1 && Time.current() - this.startTime > this.rocketsUnloadingTime) {
                ZutiWeaponsManagement.returnRemainingAircraftRockets(this.playerAC, true);

                if (this.playerAC instanceof NetAircraft) ZutiSupportMethods_Air.sendNetAircraftRearmOrdinance(this.playerAC, 1, 0, null);

                String userData = ZutiSupportMethods.getAircraftCompleteName(World.getPlayerAircraft());
                String userLocation = ZutiSupportMethods.getPlayerLocation();
                ZutiSupportMethods_NetSend.logMessage((NetUser) NetEnv.host(), userData + " unloaded rockets at " + userLocation);

                this.rocketsUnloadingTime = -1;

                this.stopTimer();

                return -1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    private void calculateRearmingTimeConsumption() {
        this.rocketsUnloadingTime = ZutiWeaponsManagement.returnRemainingAircraftRockets(this.playerAC, false);

        if (this.bornPlace != null && this.bornPlace.zutiEnableResourcesManagement) this.rocketsUnloadingTime *= this.bornPlace.zutiOneRocketRearmSeconds;
        else this.rocketsUnloadingTime *= Mission.MDS_VARIABLES().zutiReload_OneRocketRearmSeconds;

        System.out.println("Calculated Rockets Unloading Time: " + this.rocketsUnloadingTime);
    }

    private void stopTimer() {
        com.maddox.il2.game.HUD.log("mds.unloadingRocketsDone");
        System.out.println("Unloading o rockets done!");
    }

    public void cancelTimer() {
        com.maddox.il2.game.HUD.log("mds.unloadingRocketsAborted");
        System.out.println("Unloading of rockets aborted!!!");
    }
}