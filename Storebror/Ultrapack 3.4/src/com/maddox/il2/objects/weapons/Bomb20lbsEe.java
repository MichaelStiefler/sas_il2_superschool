package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb20lbsEe extends Bomb {
    static {
        Class class1 = Bomb20lbsEe.class;
        Property.set(class1, "mesh", "3do/arms/20lbsBombEe/mono.sim");
        Property.set(class1, "radius", 17F);
        Property.set(class1, "power", 5F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.15F);
        Property.set(class1, "massa", 8F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
