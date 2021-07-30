package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.fm.ZutiSupportMethods_FM;

class ZutiOrder_StartAll extends com.maddox.il2.game.order.Order {
    public ZutiOrder_StartAll() {
        super("mds.RRR1");
    }

    public void run() {
        try {
            ZutiSupportMethods_FM.startAll(World.getPlayerAircraft());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}