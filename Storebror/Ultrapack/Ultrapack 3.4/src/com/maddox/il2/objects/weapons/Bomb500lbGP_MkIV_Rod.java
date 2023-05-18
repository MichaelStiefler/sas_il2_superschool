package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbGP_MkIV_Rod extends Bomb {

    static {
        Class class1 = Bomb500lbGP_MkIV_Rod.class;
        Property.set(class1, "mesh", "3do/arms/500lbGP_MkIV_Rod/mono.sim");
        Property.set(class1, "radius", 96F);
        Property.set(class1, "power", 69F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.33F);
        Property.set(class1, "massa", 222.5F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Pistol_No27.class, Fuze_Pistol_No42.class })));
    }
}
