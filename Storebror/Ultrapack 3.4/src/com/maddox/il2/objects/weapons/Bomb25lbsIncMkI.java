package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb25lbsIncMkI extends Bomb {
    static {
        Class class1 = Bomb25lbsIncMkI.class;
        Property.set(class1, "mesh", "3DO/Arms/25lbs_Inc_MkI/mono.sim");
        Property.set(class1, "radius", 10F);
        Property.set(class1, "power", 6.5F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 12F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
