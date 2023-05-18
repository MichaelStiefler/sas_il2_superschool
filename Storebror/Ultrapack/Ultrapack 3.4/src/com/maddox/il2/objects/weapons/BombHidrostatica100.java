package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombHidrostatica100 extends Bomb {
    static {
        Class class1 = BombHidrostatica100.class;
        Property.set(class1, "mesh", "3DO/Arms/Hidrostatica_100Kg/mono.sim");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "power", 20F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.35F);
        Property.set(class1, "massa", 100F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
