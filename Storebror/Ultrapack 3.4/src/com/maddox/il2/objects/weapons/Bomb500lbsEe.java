package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbsEe extends Bomb {
    static {
        Class class1 = Bomb500lbsEe.class;
        Property.set(class1, "mesh", "3DO/Arms/500LbsBombEe/mono.sim");
        Property.set(class1, "radius", 100F);
        Property.set(class1, "power", 75F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.34F);
        Property.set(class1, "massa", 226F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
