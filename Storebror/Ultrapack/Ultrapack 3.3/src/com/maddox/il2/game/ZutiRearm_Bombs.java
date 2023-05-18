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
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Time;

public class ZutiRearm_Bombs {
    private long      startTime      = 0;
    private float     bombsRearmTime = 0;
    private Aircraft  playerAC       = null;

    private int[]     bombs          = null;
    private BornPlace bornPlace      = null;

    public ZutiRearm_Bombs(float rearmPenalty, int[] bombs) {
        this.bombs = bombs;
        this.playerAC = World.getPlayerAircraft();
        this.bornPlace = ZutiSupportMethods.isPilotLandedOnBPWithOwnRRRSettings(this.playerAC.FM);
        // TODO: +++ RRR Bug hunting
        String bombsArray = "";
        if (bombs == null) bombsArray = "null";
        else {
            bombsArray = "{ ";
            for (int i = 0; i < bombs.length; i++) {
                if (i > 0) bombsArray += ", ";
                bombsArray += bombs[i];
            }
            bombsArray += " }";
        }
        ZutiWeaponsManagement.printDebugMessage(this.playerAC, "ZutiRearm_Bombs(" + rearmPenalty + ", bombs = " + bombsArray + ")");
        // --- RRR Bug hunting

        if (this.playerAC.FM.CT.wingControl > 0) {
            HUD.log("mds.unfoldWings");
            return;
        }

        this.calculateRearmingTimeConsumption(rearmPenalty);

        HUD.log("mds.rearmingBombsTime", new Object[] { new Integer(Math.round(this.bombsRearmTime)) });

        // TODO: +++ RRR Bug hunting
        ZutiWeaponsManagement.printDebugMessage(this.playerAC, "ZutiRearm_Bombs bombsRearmTime = " + this.bombsRearmTime);
        // --- RRR Bug hunting

        this.bombsRearmTime *= 1000;
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
            if (this.bombsRearmTime > -1 && Time.current() - this.startTime > this.bombsRearmTime) {
                // TODO: +++ RRR Bug hunting
                ZutiWeaponsManagement.printDebugMessage(this.playerAC, "ZutiRearm_Bombs updateTimer Checkpoint 1");
//				ZutiWeaponsManagement.rearmBombsFTanksTorpedoes(playerAC, this.bombs);
                ZutiWeaponsManagement.rearmBombsFTanksTorpedoes(this.playerAC, (int[]) this.bombs.clone()); // Objects like arrays are passed as reference, this would reduce the number of bombs to reload before the reload command is sent to other clients!
                ZutiWeaponsManagement.printDebugMessage(this.playerAC, "ZutiRearm_Bombs updateTimer Checkpoint 2");
                // --- RRR Bug hunting

                if (this.playerAC instanceof NetAircraft) {
                    // TODO: +++ RRR Bug hunting
                    String bombsArray = "";
                    if (this.bombs == null) bombsArray = "null";
                    else {
                        bombsArray = "{ ";
                        for (int i = 0; i < this.bombs.length; i++) {
                            if (i > 0) bombsArray += ", ";
                            bombsArray += this.bombs[i];
                        }
                        bombsArray += " }";
                    }
                    ZutiWeaponsManagement.printDebugMessage(this.playerAC, "ZutiRearm_Bombs sendNetAircraftRearmOrdinance(playerAC, 2, -1, " + bombsArray + ")");
                    // --- RRR Bug hunting
                    ZutiSupportMethods_Air.sendNetAircraftRearmOrdinance(this.playerAC, 2, -1, this.bombs);
                }

                String userData = ZutiSupportMethods.getAircraftCompleteName(World.getPlayerAircraft());
                String userLocation = ZutiSupportMethods.getPlayerLocation();
                // TODO: +++ RRR Bug hunting
                ZutiWeaponsManagement.printDebugMessage(this.playerAC, "ZutiSupportMethods_NetSend.logMessage((NetUser)NetEnv.host(), " + userData + " rearmed bombs at " + userLocation + ")");
                // --- RRR Bug hunting
                ZutiSupportMethods_NetSend.logMessage((NetUser) NetEnv.host(), userData + " rearmed bombs at " + userLocation);

                this.bombsRearmTime = -1;

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
         * BulletEmitter[][] weapons = World.getPlayerAircraft().FM.CT.Weapons; Property bombsCount = null;
         *
         * for( int i=0; i<weapons.length; i++ ) { try { for( int j=0; j<weapons[i].length; j++ ) { //Covers bombs, fuel tanks, torpedoes if( weapons[i][j] instanceof BombGun ) { bombsCount = Property.get(weapons[i][j], "_count"); if( bombsCount != null
         * && bombsCount.intValue() < 1 ) bombsCount.set(1);
         *
         * if( bornPlace == null ) bombsRearmTime += (Mission.MDS_VARIABLES().zutiReload_OneBombFTankTorpedoeRearmSeconds * bombsCount.longValue()); else bombsRearmTime += (bornPlace.zutiOneBombFTankTorpedoeRearmSeconds * bombsCount.longValue()); } } }
         * catch(Exception ex){} }
         */
        int bombsCount = 0;
        for (int i = 0; i < this.bombs.length; i++)
            bombsCount += this.bombs[i];

        if (this.bornPlace == null) this.bombsRearmTime = Mission.MDS_VARIABLES().zutiReload_OneBombFTankTorpedoeRearmSeconds * bombsCount;
        else this.bombsRearmTime = this.bornPlace.zutiOneBombFTankTorpedoeRearmSeconds * bombsCount;

        System.out.println("Received bombs by weight: ");
        System.out.println("   250kg: " + this.bombs[0]);
        System.out.println("   500kg: " + this.bombs[1]);
        System.out.println("  1000kg: " + this.bombs[2]);
        System.out.println("  2000kg: " + this.bombs[3]);
        System.out.println("  5000kg: " + this.bombs[4]);
        System.out.println("    more: " + this.bombs[5]);
        System.out.println("  Bombs Rearm time: " + this.bombsRearmTime);
        System.out.println("  Rearming Penalty: " + rearmPenalty);

        this.bombsRearmTime = this.bombsRearmTime * rearmPenalty;

        System.out.println("--------------------------------------");
        System.out.println("Calculated Rearm Time: " + this.bombsRearmTime);
    }

    private int countLoadedBombs() {
        int counter = 0;
        BulletEmitter[][] weapons = World.getPlayerAircraft().FM.CT.Weapons;

        for (int i = 0; i < weapons.length; i++)
            try {
                for (int j = 0; j < weapons[i].length; j++)
                    // Covers bombs, fuel tanks, torpedoes
                    if (weapons[i][j] instanceof BombGun) counter += ((BombGun) weapons[i][j]).countBullets();
            } catch (Exception ex) {}

        return counter;
    }

    private void stopTimer() {
        // Return unused bombs
        ZutiSupportMethods_NetSend.returnRRRResources_Bombs(this.bombs, this.playerAC.pos.getAbsPoint());

        int loadedBombs = this.countLoadedBombs();
        HUD.log("mds.rearmingBombsDone", new Object[] { new Integer(loadedBombs) });
        System.out.println("Bomb Rearming done! Loaded >" + loadedBombs + "< bombs.");
    }

    public void cancelTimer() {
        // Return unused bombs
        ZutiSupportMethods_NetSend.returnRRRResources_Bombs(this.bombs, this.playerAC.pos.getAbsPoint());

        HUD.log("mds.rearmingBombsAborted");
        System.out.println("Bomb Rearming aborted!!!");
    }
}