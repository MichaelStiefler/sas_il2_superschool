package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;

class ZutiOrder_RearmAll extends com.maddox.il2.game.order.Order {
    public ZutiOrder_RearmAll() {
        super("mds.rearmAll");
    }

    public void run() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            if (!ZutiSupportMethods.allowRRR(aircraft)) {
                com.maddox.il2.game.HUD.log("mds.checkChocksPosition");
                return;
            }

            long bullets = ZutiSupportMethods_GameOrder.calculateBulletsToReload(aircraft);
            ZutiSupportMethods_NetSend.requestBullets(bullets);

            int[] bombs = ZutiSupportMethods_GameOrder.getBombsCount(aircraft);
            ZutiSupportMethods_NetSend.requestBombs(bombs);

            long rockets = ZutiSupportMethods_GameOrder.calculateRocketsToReload(aircraft);
            ZutiSupportMethods_NetSend.requestRockets(rockets);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}