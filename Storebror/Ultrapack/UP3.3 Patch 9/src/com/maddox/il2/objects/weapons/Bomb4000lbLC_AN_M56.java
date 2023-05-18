package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb4000lbLC_AN_M56 extends Bomb {

    static {
        Class class1 = Bomb4000lbLC_AN_M56.class;
        Property.set(class1, "mesh", "3do/arms/4000lbLC_AN_M56/mono.sim");
        Property.set(class1, "radius", 152F);
        Property.set(class1, "power", 1525F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.86F);
        Property.set(class1, "massa", 1907.4F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_AN_M103.class, Fuze_M162.class })));
    }
}
