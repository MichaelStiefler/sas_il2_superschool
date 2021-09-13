package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIT24Kg_T extends Bomb {
    static {
        Class class1 = BombIT24Kg_T.class;
        Property.set(class1, "mesh", "3do/arms/IT24Kg_T/mono.sim");
        Property.set(class1, "radius", 15F);
        Property.set(class1, "power", 12F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.162F);
        Property.set(class1, "massa", 24F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
