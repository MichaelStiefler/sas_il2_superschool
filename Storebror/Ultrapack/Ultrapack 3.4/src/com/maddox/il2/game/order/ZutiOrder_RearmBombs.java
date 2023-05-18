package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;

class ZutiOrder_RearmBombs extends com.maddox.il2.game.order.Order {
    public ZutiOrder_RearmBombs() {
        super("mds.rearmBombs");
    }

    public void run() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            if (!ZutiSupportMethods.allowRRR(aircraft)) {
                com.maddox.il2.game.HUD.log("mds.checkChocksPosition");
                return;
            }

            int[] bombs = ZutiSupportMethods_GameOrder.getBombsCount(aircraft);
            ZutiSupportMethods_NetSend.requestBombs(bombs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}