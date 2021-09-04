package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombPuW100MSp extends Bomb {
    static {
        Class class1 = BombPuW100MSp.class;
        Property.set(class1, "mesh", "3DO/Arms/PuW-100MSp/mono.sim");
        Property.set(class1, "radius", 35F);
        Property.set(class1, "power", 75F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 100F);
        Property.set(class1, "randomOrient", 0);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
