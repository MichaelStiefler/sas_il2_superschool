package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb50KgR50 extends Bomb {
    static {
        Class class1 = Bomb50KgR50.class;
        Property.set(class1, "mesh", "3DO/Arms/R50-50KgFrag/mono.sim");
        Property.set(class1, "radius", 65F);
        Property.set(class1, "power", 20F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
