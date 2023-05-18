package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb120lbsEe extends Bomb {

    static {
        Class class1 = Bomb120lbsEe.class;
        Property.set(class1, "mesh", "3DO/Arms/120LbsBombEe/mono.sim");
        Property.set(class1, "radius", 40F);
        Property.set(class1, "power", 30F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.21F);
        Property.set(class1, "massa", 54F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
