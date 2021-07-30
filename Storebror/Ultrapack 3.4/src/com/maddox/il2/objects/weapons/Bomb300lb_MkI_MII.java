package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb300lb_MkI_MII extends Bomb {

    static {
        Class class1 = Bomb300lb_MkI_MII.class;
        Property.set(class1, "mesh", "3do/arms/300lb_MkI_MII/mono.sim");
        Property.set(class1, "radius", 61F);
        Property.set(class1, "power", 67.2F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.31F);
        Property.set(class1, "massa", 129.7F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
