package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIT50Kg_T_Late extends Bomb {
    static {
        Class class1 = BombIT50Kg_T_Late.class;
        Property.set(class1, "mesh", "3do/arms/IT50Kg_T_Late/mono.sim");
        Property.set(class1, "radius", 40F);
        Property.set(class1, "power", 29.2F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.252F);
        Property.set(class1, "massa", 59.3F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
