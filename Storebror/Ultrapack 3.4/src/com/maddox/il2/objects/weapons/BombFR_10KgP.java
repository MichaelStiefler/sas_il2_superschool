package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFR_10KgP extends Bomb {

    static {
        Class class1 = BombFR_10KgP.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFr10KgPA/mono.sim");
        Property.set(class1, "radius", 25F);
        Property.set(class1, "power", 6.5F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 10F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
