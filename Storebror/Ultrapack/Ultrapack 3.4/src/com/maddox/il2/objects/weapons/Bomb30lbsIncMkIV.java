package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb30lbsIncMkIV extends Bomb {
    static {
        Class class1 = Bomb30lbsIncMkIV.class;
        Property.set(class1, "mesh", "3DO/Arms/30bs_Inc_MkIV/mono.sim");
        Property.set(class1, "radius", 10F);
        Property.set(class1, "power", 6.5F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 12F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
