package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFR_200KgD extends Bomb {
    static {
        Class class1 = BombFR_200KgD.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFR200KgD/mono.sim");
        Property.set(class1, "radius", 70F);
        Property.set(class1, "power", 50F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.37F);
        Property.set(class1, "massa", 216F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
