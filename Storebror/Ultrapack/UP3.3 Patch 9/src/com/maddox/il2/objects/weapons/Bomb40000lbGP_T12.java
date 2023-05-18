package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb40000lbGP_T12 extends Bomb {

    static {
        Class class1 = Bomb40000lbGP_T12.class;
        Property.set(class1, "mesh", "3do/arms/40000lbGP_T12/mono.sim");
        Property.set(class1, "radius", 840F);
        Property.set(class1, "power", 7980F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 1.37F);
        Property.set(class1, "massa", 19780F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Pistol_No58.class })));
    }
}
