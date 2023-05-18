package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFR_200KgE extends Bomb {
    static {
        Class class1 = BombFR_200KgE.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFR200KgE/mono.sim");
        Property.set(class1, "radius", 350F);
        Property.set(class1, "power", 106.5F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.37F);
        Property.set(class1, "massa", 226F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
