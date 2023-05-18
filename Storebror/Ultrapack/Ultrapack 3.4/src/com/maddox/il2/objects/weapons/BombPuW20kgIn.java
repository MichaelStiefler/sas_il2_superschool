package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombPuW20kgIn extends Bomb {
    static {
        Class class1 = BombPuW20kgIn.class;
        Property.set(class1, "mesh", "3DO/Arms/PuW-20KgInc/mono.sim");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "power", 10F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 20F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
