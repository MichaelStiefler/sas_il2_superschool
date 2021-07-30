package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.Aircraft;

public class ZutiOrder_EngineRepair extends com.maddox.il2.game.order.Order {
    public ZutiOrder_EngineRepair(String name) {
        super(name);
    }

    public void setName(String name) {
        super.name = name;
    }

    public void setNameDE(String name) {
        super.name = this.nameDE;
    }

    public void run() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            if (!ZutiSupportMethods.allowRRR(aircraft)) {
                com.maddox.il2.game.HUD.log("mds.checkChocksPosition");
                return;
            }

            ZutiSupportMethods_NetSend.requestEngine(1, Integer.parseInt(this.name));

            // ZutiSupportMethods_FM.startEngineRepairing(World.getPlayerFM(), Integer.parseInt(name));
        } catch (Exception ex) {
            System.out.println("ZutiOrder_EngineRepair error, ID_01: " + ex.toString());
        }
    }
}