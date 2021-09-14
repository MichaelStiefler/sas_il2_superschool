package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFR_10KgI extends Bomb {
    static {
        Class class1 = BombFR_10KgI.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFr10KgInc/mono.sim");
        Property.set(class1, "radius", 7.5F);
        Property.set(class1, "power", 25F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 10F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
