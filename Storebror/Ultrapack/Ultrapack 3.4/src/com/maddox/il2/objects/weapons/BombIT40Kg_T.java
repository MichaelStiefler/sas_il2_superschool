package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIT40Kg_T extends Bomb {
    static {
        Class class1 = BombIT40Kg_T.class;
        Property.set(class1, "mesh", "3do/arms/IT40Kg_T/mono.sim");
        Property.set(class1, "radius", 30F);
        Property.set(class1, "power", 20F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.25F);
        Property.set(class1, "massa", 40F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
