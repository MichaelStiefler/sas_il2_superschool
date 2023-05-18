package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb20lbsFmkII extends Bomb {
    static {
        Class class1 = Bomb20lbsFmkII.class;
        Property.set(class1, "mesh", "3DO/Arms/20lbs_F_MkII/mono.sim");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "power", 10F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.1F);
        Property.set(class1, "massa", 9F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
