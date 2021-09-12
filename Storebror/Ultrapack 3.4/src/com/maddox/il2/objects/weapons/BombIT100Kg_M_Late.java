package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIT100Kg_M_Late extends Bomb {
    static {
        Class class1 = BombIT100Kg_M_Late.class;
        Property.set(class1, "mesh", "3do/arms/IT100Kg_M_Late/mono.sim");
        Property.set(class1, "radius", 30F);
        Property.set(class1, "power", 27.5F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.252F);
        Property.set(class1, "massa", 109F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
