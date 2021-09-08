package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFR_75KgG2 extends Bomb {
    static {
        Class class1 = BombFR_75KgG2.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFR75KgG2/mono.sim");
        Property.set(class1, "radius", 100F);
        Property.set(class1, "power", 46F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.22F);
        Property.set(class1, "massa", 73F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
