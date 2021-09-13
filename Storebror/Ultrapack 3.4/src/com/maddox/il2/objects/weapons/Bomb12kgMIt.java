package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb12kgMIt extends Bomb {
    static {
        Class class1 = Bomb12kgMIt.class;
        Property.set(class1, "mesh", "3DO/Arms/12kgFragMIt/mono.sim");
        Property.set(class1, "radius", 30F);
        Property.set(class1, "power", 7.5F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 12F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
