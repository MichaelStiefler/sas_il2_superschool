package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbGP_MkV extends Bomb {

    static {
        Class class1 = Bomb500lbGP_MkV.class;
        Property.set(class1, "mesh", "3do/arms/500lbGP_MkV/mono.sim");
        Property.set(class1, "radius", 94F);
        Property.set(class1, "power", 65.6F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.33F);
        Property.set(class1, "massa", 213F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Pistol_No17.class })));
    }
}
