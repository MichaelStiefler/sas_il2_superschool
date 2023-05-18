package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ZutiSupportMethods_AI;

class ZutiOrder_UnjamChocks extends com.maddox.il2.game.order.Order {
    public ZutiOrder_UnjamChocks() {
        super("mds.unjamChocks");
    }

    public void run() {
        try {
            World.getPlayerFM().brakeShoe = false;
            // Collect earned points
            ZutiSupportMethods_AI.collectPoints();
        } catch (Exception ex) {
            System.out.println("ZutiOrder_UnjamChocks error, ID_01: " + ex.toString());
        }
    }
}