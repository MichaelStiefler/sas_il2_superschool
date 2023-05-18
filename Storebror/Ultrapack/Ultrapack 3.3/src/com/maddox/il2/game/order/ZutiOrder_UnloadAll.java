package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.fm.ZutiSupportMethods_FM;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.objects.air.Aircraft;

class ZutiOrder_UnloadAll extends com.maddox.il2.game.order.Order {
    public ZutiOrder_UnloadAll() {
        super("mds.unloadAll");
    }

    public void run() {
        try {
            Aircraft aircraft = World.getPlayerAircraft();
            if (!ZutiSupportMethods.allowRRR(aircraft)) {
                com.maddox.il2.game.HUD.log("mds.checkChocksPosition");
                return;
            }

            if (aircraft != null && aircraft.isAlive()) {
                ZutiSupportMethods_FM.startUnloadingBombs();
                ZutiSupportMethods_FM.startUnloadingBullets();
                ZutiSupportMethods_FM.startUnloadingRockets();
                ZutiSupportMethods_FM.startUnloadingFuel();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}