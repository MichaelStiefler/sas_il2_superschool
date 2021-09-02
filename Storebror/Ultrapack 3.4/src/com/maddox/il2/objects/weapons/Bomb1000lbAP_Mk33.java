package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb1000lbAP_Mk33 extends Bomb {

    static {
        Class class1 = Bomb1000lbAP_Mk33.class;
        Property.set(class1, "mesh", "3do/arms/1000lbAP_Mk33/mono.sim");
        Property.set(class1, "radius", 52F);
        Property.set(class1, "power", 63.5F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.31F);
        Property.set(class1, "massa", 465F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_AN_MK_228.class })));
    }
}
