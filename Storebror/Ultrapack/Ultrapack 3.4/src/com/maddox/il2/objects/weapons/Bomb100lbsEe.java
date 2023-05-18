package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb100lbsEe extends Bomb {
    static {
        Class class1 = Bomb100lbsEe.class;
        Property.set(class1, "mesh", "3DO/Arms/100LbsBombEe/mono.sim");
        Property.set(class1, "radius", 25F);
        Property.set(class1, "power", 25F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 45F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
