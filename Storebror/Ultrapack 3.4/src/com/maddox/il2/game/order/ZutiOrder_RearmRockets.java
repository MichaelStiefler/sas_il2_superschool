package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;

class ZutiOrder_RearmRockets extends com.maddox.il2.game.order.Order {
    public ZutiOrder_RearmRockets() {
        super("mds.rearmRockets");
    }

    public void run() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            if (!ZutiSupportMethods.allowRRR(aircraft)) {
                com.maddox.il2.game.HUD.log("mds.checkChocksPosition");
                return;
            }

            long rockets = ZutiSupportMethods_GameOrder.calculateRocketsToReload(aircraft);
            System.out.println("  Requesting rockets: " + rockets);

            ZutiSupportMethods_NetSend.requestRockets(rockets);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}