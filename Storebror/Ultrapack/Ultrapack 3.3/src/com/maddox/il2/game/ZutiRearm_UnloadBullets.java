//***********************************
//This class is for client side players only
//***********************************
package com.maddox.il2.game;

import com.maddox.il2.ai.World;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Time;

public class ZutiRearm_UnloadBullets {
    private long      startTime            = 0;
    private float     bulletsUnloadingTime = 0;
    private Aircraft  playerAC             = null;

    private BornPlace bornPlace            = null;

    public ZutiRearm_UnloadBullets() {
        this.playerAC = World.getPlayerAircraft();
        this.bornPlace = ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(this.playerAC.FM);

        this.calculateRearmingTimeConsumption();

        HUD.log("mds.unloadingBulletsTime", new java.lang.Object[] { new Integer(Math.round(this.bulletsUnloadingTime)) });

        this.bulletsUnloadingTime *= 1000;

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
            if (this.bulletsUnloadingTime > -1 && Time.current() - this.startTime > this.bulletsUnloadingTime) {
                ZutiWeaponsManagement.returnRemainingAircraftBullets(this.playerAC, true);

                String userData = ZutiSupportMethods.getAircraftCompleteName(World.getPlayerAircraft());
                String userLocation = ZutiSupportMethods.getPlayerLocation();
                ZutiSupportMethods_NetSend.logMessage((NetUser) NetEnv.host(), userData + " unloaded bullets at " + userLocation);

                this.bulletsUnloadingTime = -1;

                this.stopTimer();

                return -1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    private void calculateRearmingTimeConsumption() {
        // bulletsUnloadingTime = ZutiWeaponsManagement.returnRemainingAircraftBullets(playerAC, false);
        this.bulletsUnloadingTime = ZutiWeaponsManagement.returnNumberOfLoadedGuns(this.playerAC);

        if (this.bornPlace != null && this.bornPlace.zutiEnableResourcesManagement) this.bulletsUnloadingTime *= this.bornPlace.zutiOneMgCannonRearmSecond;
        else this.bulletsUnloadingTime *= Mission.MDS_VARIABLES().zutiReload_OneMgCannonRearmSecond;

        System.out.println("Calculated Bullets Unloading Time: " + this.bulletsUnloadingTime);
    }

    private void stopTimer() {
        com.maddox.il2.game.HUD.log("mds.unloadingBulletsDone");
        System.out.println("Unloading o bullets done!");
    }

    public void cancelTimer() {
        com.maddox.il2.game.HUD.log("mds.unloadingBulletsAborted");
        System.out.println("Unloading of bullets aborted!!!");
    }
}