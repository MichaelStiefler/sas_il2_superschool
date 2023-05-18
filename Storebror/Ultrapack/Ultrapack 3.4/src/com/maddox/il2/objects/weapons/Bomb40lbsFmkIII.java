package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb40lbsFmkIII extends Bomb {
    static {
        Class class1 = Bomb40lbsFmkIII.class;
        Property.set(class1, "mesh", "3DO/Arms/40lbs_F_MkIII/mono.sim");
        Property.set(class1, "radius", 35F);
        Property.set(class1, "power", 20F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.15F);
        Property.set(class1, "massa", 18F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
