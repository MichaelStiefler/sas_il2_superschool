//***********************************
//This class is for client side players only
//***********************************
package com.maddox.il2.game;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiSupportMethods_AI;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Time;

public class ZutiRearm_Rockets {
    private long      startTime        = 0;
    private float     rocketsRearmTime = 0;
    private Aircraft  playerAC         = null;

    private long      rockets;
    private BornPlace bornPlace        = null;

    public ZutiRearm_Rockets(float rearmPenalty, long rockets) {
        this.rockets = rockets;
        this.playerAC = World.getPlayerAircraft();
        this.bornPlace = ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(this.playerAC.FM);
        // TODO: +++ RRR Bug hunting
        ZutiWeaponsManagement.printDebugMessage(this.playerAC, "ZutiRearm_Rockets(" + rearmPenalty + ", " + rockets + ")");
        // --- RRR Bug hunting

        if (this.playerAC.FM.CT.wingControl > 0) {
            HUD.log("mds.unfoldWings");
            return;
        }

        this.calculateRearmingTimeConsumption(rearmPenalty);

        HUD.log("mds.rearmingRocketsTime", new Object[] { new Integer(Math.round(this.rocketsRearmTime)) });

        // TODO: +++ RRR Bug hunting
        ZutiWeaponsManagement.printDebugMessage(this.playerAC, "ZutiRearm_Rockets rocketsRearmTime = " + this.rocketsRearmTime);
        // --- RRR Bug hunting

        this.rocketsRearmTime *= 1000;

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
            if (this.rocketsRearmTime > -1 && Time.current() - this.startTime > this.rocketsRearmTime) {
                // TODO: +++ RRR Bug hunting
                ZutiWeaponsManagement.printDebugMessage(this.playerAC, "ZutiRearm_Rockets updateTimer Checkpoint 1");
                // --- RRR Bug hunting
                ZutiWeaponsManagement.rearmRockets(this.playerAC, this.rockets);

                // TODO: +++ RRR Bug hunting
                ZutiWeaponsManagement.printDebugMessage(this.playerAC, "ZutiRearm_Rockets updateTimer Checkpoint 2");
                // --- RRR Bug hunting
                if (this.playerAC instanceof NetAircraft) {
                    // TODO: +++ RRR Bug hunting
                    ZutiWeaponsManagement.printDebugMessage(this.playerAC, "ZutiRearm_Rockets sendNetAircraftRearmOrdinance(playerAC, 1, " + this.rockets + ", null)");
                    // --- RRR Bug hunting
                    ZutiSupportMethods_Air.sendNetAircraftRearmOrdinance(this.playerAC, 1, this.rockets, null);
                }

                String userData = ZutiSupportMethods.getAircraftCompleteName(World.getPlayerAircraft());
                String userLocation = ZutiSupportMethods.getPlayerLocation();
                // TODO: +++ RRR Bug hunting
                ZutiWeaponsManagement.printDebugMessage(this.playerAC, "ZutiSupportMethods_NetSend.logMessage((NetUser)NetEnv.host(), " + userData + " rearmed rockets at " + userLocation + ")");
                // --- RRR Bug hunting
                ZutiSupportMethods_NetSend.logMessage((NetUser) NetEnv.host(), userData + " rearmed rockets at " + userLocation);

                this.rocketsRearmTime = -1;

                // Collect earned points
                ZutiSupportMethods_AI.collectPoints();
                // Reset processing of cargo drops since player rearmed the plane and had to land first (and survive :D)
                ZutiWeaponsManagement.ZUTI_PROCESS_CARGO_DROPS = true;

                this.stopTimer();

                return -1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    private void calculateRearmingTimeConsumption(float rearmPenalty) {
        /*
         * BulletEmitter[][] weapons = World.getPlayerAircraft().FM.CT.Weapons;
         *
         * for( int i=0; i<weapons.length; i++ ) { try { for( int j=0; j<weapons[i].length; j++ ) { //Covers rockets if( weapons[i][j] instanceof RocketGun ) { if( bornPlace == null ) rocketsRearmTime +=
         * Mission.MDS_VARIABLES().zutiReload_OneRocketRearmSeconds; else rocketsRearmTime += bornPlace.zutiOneRocketRearmSeconds; } } } catch(Exception ex){} }
         */

        if (this.bornPlace == null) this.rocketsRearmTime = Mission.MDS_VARIABLES().zutiReload_OneRocketRearmSeconds * this.rockets;
        else this.rocketsRearmTime = this.bornPlace.zutiOneRocketRearmSeconds * this.rockets;

        System.out.println("Rockets Rearm time: " + this.rocketsRearmTime);
        System.out.println("  Rearming Penalty: " + rearmPenalty);

        this.rocketsRearmTime = this.rocketsRearmTime * rearmPenalty;

        System.out.println("--------------------------------------");
        System.out.println("Calculated Rearm Time: " + this.rocketsRearmTime);
    }

    private int countLoadedRockets() {
        int counter = 0;
        BulletEmitter[][] weapons = World.getPlayerAircraft().FM.CT.Weapons;

        for (int i = 0; i < weapons.length; i++)
            try {
                for (int j = 0; j < weapons[i].length; j++)
                    // Covers bombs, fuel tanks, torpedoes
                    if (weapons[i][j] instanceof RocketGun) counter += ((RocketGun) weapons[i][j]).countBullets();
            } catch (Exception ex) {}

        return counter;
    }

    private void stopTimer() {
        int loadedRockets = this.countLoadedRockets();
        HUD.log("mds.rearmingRocketsDone", new Object[] { new Integer(loadedRockets) });
        System.out.println("Rearming done! Loaded >" + loadedRockets + "< rockets.");
    }

    public void cancelTimer() {
        // Return unused fuel
        ZutiSupportMethods_NetSend.returnRRRResources_Rockets(this.rockets, this.playerAC.pos.getAbsPoint());

        HUD.log("mds.rearmingRocketsAborted");
        System.out.println("Rearming aborted!!!");
    }
}