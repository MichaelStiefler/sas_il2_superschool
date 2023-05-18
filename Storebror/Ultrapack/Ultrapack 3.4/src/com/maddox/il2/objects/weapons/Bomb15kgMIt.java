package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb15kgMIt extends Bomb {
    static {
        Class class1 = Bomb15kgMIt.class;
        Property.set(class1, "mesh", "3DO/Arms/15kgFragMIt/mono.sim");
        Property.set(class1, "radius", 30F);
        Property.set(class1, "power", 7.5F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 15F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
