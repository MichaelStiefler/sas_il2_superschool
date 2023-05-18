package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFR_100Kg extends Bomb {
    static {
        Class class1 = BombFR_100Kg.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFR100kg/mono.sim");
        Property.set(class1, "radius", 110F);
        Property.set(class1, "power", 49.5F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 120F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
