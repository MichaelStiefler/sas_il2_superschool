package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIT800Kg_T extends Bomb {
    static {
        Class class1 = BombIT800Kg_T.class;
        Property.set(class1, "mesh", "3do/arms/IT800Kg_T/mono.sim");
        Property.set(class1, "radius", 500F);
        Property.set(class1, "power", 357F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.458F);
        Property.set(class1, "massa", 822F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
