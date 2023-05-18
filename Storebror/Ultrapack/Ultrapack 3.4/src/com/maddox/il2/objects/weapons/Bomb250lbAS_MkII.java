package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250lbAS_MkII extends Bomb {

    static {
        Class class1 = Bomb250lbAS_MkII.class;
        Property.set(class1, "mesh", "3do/arms/250lbAS_MkII/mono.sim");
        Property.set(class1, "radius", 49F);
        Property.set(class1, "power", 57F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.29F);
        Property.set(class1, "massa", 110F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_No875B.class, Fuze_Pistol_No32.class })));
    }
}
