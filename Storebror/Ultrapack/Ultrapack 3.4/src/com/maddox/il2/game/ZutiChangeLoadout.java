//***********************************
//This class is for client side players only
//***********************************
package com.maddox.il2.game;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiSupportMethods_AI;
import com.maddox.il2.game.order.ZutiSupportMethods_GameOrder;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.rts.Time;

public class ZutiChangeLoadout {
    private long      startTime       = 0;
    private float     maxTime         = 0;
    private int       loadoutId       = 0;
    private float     originalPenalty = 0;
    private Aircraft  playerAC        = null;
    private BornPlace bornPlace       = null;
    private String    selectedLoadout = "";

    public ZutiChangeLoadout(float penalty, int inLoadoutId, BornPlace bp) {
        this.bornPlace = bp;
        this.loadoutId = inLoadoutId;
        this.originalPenalty = penalty;
        this.playerAC = World.getPlayerAircraft();

        try {
            if (this.playerAC.FM.CT.wingControl > 0) {
                com.maddox.il2.game.HUD.log("mds.unfoldWings");
                return;
            }

            if (this.bornPlace != null) {
                this.maxTime = this.bornPlace.zutiLoadoutChangePenaltySeconds * this.originalPenalty;
                System.out.println("-== Loadout changes with HB specific RRR settings! ==-");
            } else this.maxTime = Mission.MDS_VARIABLES().zutiReload_LoadoutChangePenaltySeconds * this.originalPenalty;

            String currentAcName = com.maddox.rts.Property.stringValue(this.playerAC.getClass(), "keyName");

            this.selectedLoadout = ZutiSupportMethods.getSelectedLoadoutForAircraft(this.bornPlace, currentAcName, this.playerAC.FM.Loc.x, this.playerAC.FM.Loc.y, this.loadoutId, World.getPlayerArmy(), false);
            if (this.selectedLoadout.trim().length() < 1) {
                com.maddox.il2.game.HUD.log("mds.unknownLoadout");
                return;
            }

            // First, unload all ammo and return it to resources pool
            ZutiWeaponsManagement.returnRemainingAircraftResources(this.playerAC, null);

            HUD.log("mds.loadoutChanging", new java.lang.Object[] { new Integer(Math.round(this.maxTime)) });
            // Clear all ammo
            ZutiWeaponsManagement.unloadLoadedWeapons(this.playerAC);
            // Clear old pylons
            ZutiWeaponsManagement.removePylons(this.playerAC);
            // System.out.println("ZutiTimer_ChangeLoadout preparations complete!");

            ZutiWeaponsManagement.preloadLoadOptions(this.playerAC, this.loadoutId, this.selectedLoadout);

            if (this.playerAC instanceof NetAircraft) ZutiSupportMethods_Air.sendNetAircraftLoadoutChange(this.playerAC, this.loadoutId, this.selectedLoadout);

            this.maxTime *= 1000;

            this.startTime = Time.current();
        } catch (Exception ex) {}
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
            if (Time.current() - this.startTime > this.maxTime) {
                ZutiWeaponsManagement.changeLoadout(this.playerAC, this.selectedLoadout, true);

                // Start rearming this new laodout setup
                long bullets = ZutiSupportMethods_GameOrder.calculateBulletsToReload(this.playerAC);
                ZutiSupportMethods_NetSend.requestBullets(bullets);

                long rockets = ZutiSupportMethods_GameOrder.calculateRocketsToReload(this.playerAC);
                ZutiSupportMethods_NetSend.requestRockets(rockets);

                int[] bombs = ZutiSupportMethods_GameOrder.getBombsCount(this.playerAC);
                ZutiSupportMethods_NetSend.requestBombs(bombs);

                // Collect earned points
                ZutiSupportMethods_AI.collectPoints();
                this.stopTimer();

                return -1;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    private void stopTimer() {
        com.maddox.il2.game.HUD.log("mds.loadoutChanged");
        System.out.println("Loadout changed!");
    }

    public void cancelTimer() {
        com.maddox.il2.game.HUD.log("mds.loadoutAborted");
        System.out.println("Loadout changing aborted!!!");
    }
}