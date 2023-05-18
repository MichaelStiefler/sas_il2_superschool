package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb30lbsIncSBC extends Bomb {

    static {
        Class class1 = Bomb30lbsIncSBC.class;
        Property.set(class1, "mesh", "3DO/Arms/Bomb30lbsIncSBC/mono.sim");
        Property.set(class1, "power", 6.8F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.1524F);
        Property.set(class1, "massa", 11.36F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "randomOrient", 0);
    }
}
