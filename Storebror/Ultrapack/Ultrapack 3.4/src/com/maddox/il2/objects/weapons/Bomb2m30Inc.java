package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb2m30Inc extends Bomb {
    static {
        Class class1 = Bomb2m30Inc.class;
        Property.set(class1, "mesh", "3DO/Arms/2kgInc_M1930/mono.sim");
        Property.set(class1, "radius", 5F);
        Property.set(class1, "power", 1F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.1102F);
        Property.set(class1, "massa", 2F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
