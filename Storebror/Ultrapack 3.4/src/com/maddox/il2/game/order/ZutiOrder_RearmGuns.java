package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.game.ZutiRearm_Cannons;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;

class ZutiOrder_RearmGuns extends com.maddox.il2.game.order.Order {
    public ZutiOrder_RearmGuns() {
        super("mds.rearmGuns");
    }

    public void run() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            if (!ZutiSupportMethods.allowRRR(aircraft)) {
                com.maddox.il2.game.HUD.log("mds.checkChocksPosition");
                return;
            }

            long bullets = ZutiSupportMethods_GameOrder.calculateBulletsToReload(aircraft);
            System.out.println("  Number of guns    : " + ZutiRearm_Cannons.NUMBER_OF_GUNS);
            System.out.println("  Requesting bullets: " + bullets);

            ZutiSupportMethods_NetSend.requestBullets(bullets);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}