package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGr600lbHE extends Bomb {
    static {
        Class class1 = BombGr600lbHE.class;
        Property.set(class1, "mesh", "3do/arms/Gr600lbHE/mono.sim");
        Property.set(class1, "radius", 88F);
        Property.set(class1, "power", 152.4F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.37F);
        Property.set(class1, "massa", 281.7F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
