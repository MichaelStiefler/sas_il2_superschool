package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIT250Kg_T extends Bomb {
    static {
        Class class1 = BombIT250Kg_T.class;
        Property.set(class1, "mesh", "3do/arms/IT250Kg_T/mono.sim");
        Property.set(class1, "radius", 150F);
        Property.set(class1, "power", 126F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.446F);
        Property.set(class1, "massa", 286F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
