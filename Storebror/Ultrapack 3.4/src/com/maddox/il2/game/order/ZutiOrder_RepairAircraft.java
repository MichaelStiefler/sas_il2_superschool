package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;

class ZutiOrder_RepairAircraft extends com.maddox.il2.game.order.Order {
    public ZutiOrder_RepairAircraft() {
        super("mds.repair");
    }

    public void run() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            if (!ZutiSupportMethods.allowRRR(aircraft)) {
                com.maddox.il2.game.HUD.log("mds.checkChocksPosition");
                return;
            }

            ZutiSupportMethods_NetSend.requestRepairKit(1);
        } catch (Exception ex) {
            System.out.println("ZutiOrder_RepairAircraft error, ID_01: " + ex.toString());
        }
    }
}