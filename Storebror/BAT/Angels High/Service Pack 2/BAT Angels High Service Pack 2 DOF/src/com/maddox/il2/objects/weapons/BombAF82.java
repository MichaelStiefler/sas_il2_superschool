package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombAF82 extends Bomb {

    static {
        Class class1 = BombAF82.class;
        Property.set(class1, "mesh", "3do/arms/AF-82/mono.sim");
        Property.set(class1, "radius", 30F);
        Property.set(class1, "power", 36F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.28F);
        Property.set(class1, "massa", 83F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
