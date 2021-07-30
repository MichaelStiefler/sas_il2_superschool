package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGr500lbHE extends Bomb {
    static {
        Class class1 = BombGr500lbHE.class;
        Property.set(class1, "mesh", "3do/arms/Gr500lbHE/mono.sim");
        Property.set(class1, "radius", 84F);
        Property.set(class1, "power", 121.22F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.36F);
        Property.set(class1, "massa", 238.35F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
