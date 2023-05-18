package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250lbGP_MkIV_Rod extends Bomb {

    static {
        Class class1 = Bomb250lbGP_MkIV_Rod.class;
        Property.set(class1, "mesh", "3do/arms/250lbGP_MkIV_Rod/mono.sim");
        Property.set(class1, "radius", 80.4F);
        Property.set(class1, "power", 30.3F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.26F);
        Property.set(class1, "massa", 104.5F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Pistol_No27.class, Fuze_Pistol_No42.class })));
    }
}
