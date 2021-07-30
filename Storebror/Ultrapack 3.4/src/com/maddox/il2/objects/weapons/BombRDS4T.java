package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombRDS4T extends Bomb {

    static {
        Class class1 = BombRDS4T.class;
        Property.set(class1, "mesh", "3DO/Arms/RDS-4T/mono.sim");
        Property.set(class1, "radius", 3600F);
        Property.set(class1, "power", 2.458925E+013F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1.0F);
        Property.set(class1, "massa", 1200F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "newEffect", 1);
        Property.set(class1, "nuke", 1);
    }
}
