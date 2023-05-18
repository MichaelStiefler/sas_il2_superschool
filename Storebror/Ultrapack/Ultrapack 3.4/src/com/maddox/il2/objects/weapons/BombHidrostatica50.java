package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombHidrostatica50 extends Bomb {
    static {
        Class class1 = BombHidrostatica50.class;
        Property.set(class1, "mesh", "3DO/Arms/Hidrostatica_50Kg/mono.sim");
        Property.set(class1, "radius", 12F);
        Property.set(class1, "power", 12F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.25F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
