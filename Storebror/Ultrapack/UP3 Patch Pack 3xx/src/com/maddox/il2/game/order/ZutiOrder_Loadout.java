package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.fm.ZutiSupportMethods_FM;

public class ZutiOrder_Loadout extends com.maddox.il2.game.order.Order {
    public ZutiOrder_Loadout(String name) {
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
            // System.out.println("Executing: name=" + name + ", de=" + nameDE + ", ID:" + name );
            ZutiSupportMethods_FM.startChangingLoadout(World.getPlayerAircraft(), Integer.parseInt(this.name));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}