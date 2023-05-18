package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.HUD;

class ZutiOrder_EnableTurrets extends com.maddox.il2.game.order.Order {
    public ZutiOrder_EnableTurrets() {
        super("mds.enableTurrets");
    }

    public void run() {
        try {
            FlightModel FM = World.getPlayerFM();
            if (FM == null) return;

            for (int i = 0; i < FM.turret.length; i++)
                FM.turret[i].zutiEnableTurret();
            HUD.log("mds.turretEnabled", new java.lang.Object[] { new Integer(FM.turret.length) });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}