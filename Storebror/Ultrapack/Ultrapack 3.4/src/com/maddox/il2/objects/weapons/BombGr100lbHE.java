package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGr100lbHE extends Bomb {
    static {
        Class class1 = BombGr100lbHE.class;
        Property.set(class1, "mesh", "3do/arms/Gr100lbHE/mono.sim");
        Property.set(class1, "radius", 38F);
        Property.set(class1, "power", 24.5F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.205F);
        Property.set(class1, "massa", 48.5F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
