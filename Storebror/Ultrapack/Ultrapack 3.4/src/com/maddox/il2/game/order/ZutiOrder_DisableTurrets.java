package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.HUD;

class ZutiOrder_DisableTurrets extends com.maddox.il2.game.order.Order {
    public ZutiOrder_DisableTurrets() {
        super("mds.disableTurrets");
    }

    public void run() {
        try {
            FlightModel FM = World.getPlayerFM();
            if (FM == null) return;

            for (int i = 0; i < FM.turret.length; i++)
                FM.turret[i].zutiDisableTurret();
            HUD.log("mds.turretDisabled", new java.lang.Object[] { new Integer(FM.turret.length) });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}