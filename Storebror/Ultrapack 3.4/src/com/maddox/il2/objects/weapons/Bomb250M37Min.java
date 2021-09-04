package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250M37Min extends Bomb {
    static {
        Class class1 = Bomb250M37Min.class;
        Property.set(class1, "mesh", "3do/arms/MinBomb250M37/mono.sim");
        Property.set(class1, "radius", 77F);
        Property.set(class1, "power", 130F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.4236F);
        Property.set(class1, "massa", 250F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
