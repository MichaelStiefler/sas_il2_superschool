package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb2pudsR32 extends Bomb {
    static {
        Class class1 = Bomb2pudsR32.class;
        Property.set(class1, "mesh", "3DO/Arms/R32-2puds/mono.sim");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "power", 15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 32F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
