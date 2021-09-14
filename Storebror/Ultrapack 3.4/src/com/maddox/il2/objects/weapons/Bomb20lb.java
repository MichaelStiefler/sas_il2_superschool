package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb20lb extends Bomb {

    static {
        Class class1 = Bomb20lb.class;
        Property.set(class1, "mesh", "3DO/Arms/Bomb20lb/mono.sim");
        Property.set(class1, "radius", 30F);
        Property.set(class1, "power", 25F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 100F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
