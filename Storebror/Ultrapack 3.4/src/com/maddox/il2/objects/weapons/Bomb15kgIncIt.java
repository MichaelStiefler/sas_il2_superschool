package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb15kgIncIt extends Bomb {
    static {
        Class class1 = Bomb15kgIncIt.class;
        Property.set(class1, "mesh", "3DO/Arms/15kgIncIt/mono.sim");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "power", 8F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 15F);
        Property.set(class1, "sound", "weapon.bomb_phball");
    }
}
