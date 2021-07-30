package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;

class ZutiOrder_RefuelAircraft extends com.maddox.il2.game.order.Order {
    public ZutiOrder_RefuelAircraft() {
        super("mds.refuel");
    }

    public void run() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            if (!ZutiSupportMethods.allowRRR(aircraft)) {
                com.maddox.il2.game.HUD.log("mds.checkChocksPosition");
                return;
            }

            float fuel = ZutiSupportMethods_GameOrder.calculateFuelToRefuel(aircraft, true);
            System.out.println("  Requesting fuel: " + fuel);

            ZutiSupportMethods_NetSend.requestFuel(fuel);
        } catch (Exception ex) {
            System.out.println("ZutiOrder_RefuelAircraft error, ID_01: " + ex.toString());
            ex.printStackTrace();
        }
    }
}