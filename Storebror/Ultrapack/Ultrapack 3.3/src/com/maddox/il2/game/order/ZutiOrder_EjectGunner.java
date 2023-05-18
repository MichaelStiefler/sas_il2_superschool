package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;

public class ZutiOrder_EjectGunner extends com.maddox.il2.game.order.Order {
    public ZutiOrder_EjectGunner(String name) {
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
            if (!World.isPlayerGunner()) ZutiSupportMethods_NetSend.ejectPlayer(ZutiSupportMethods.getNetUser(this.name));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}