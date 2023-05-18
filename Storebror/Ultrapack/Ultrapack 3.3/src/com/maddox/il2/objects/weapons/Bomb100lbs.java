package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb100lbs extends Bomb {

    static {
        Class class1 = Bomb100lbs.class;
        Property.set(class1, "mesh", "3DO/Arms/100LbsBomb/mono.sim");
        Property.set(class1, "radius", 75F);
        Property.set(class1, "power", 25F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
