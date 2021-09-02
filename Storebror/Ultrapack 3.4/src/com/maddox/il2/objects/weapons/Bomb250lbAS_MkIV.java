package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250lbAS_MkIV extends Bomb {

    static {
        Class class1 = Bomb250lbAS_MkIV.class;
        Property.set(class1, "mesh", "3do/arms/250lbAS_MkIV/mono.sim");
        Property.set(class1, "radius", 49F);
        Property.set(class1, "power", 57F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.29F);
        Property.set(class1, "massa", 110F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Pistol_No28.class, Fuze_Pistol_No30.class })));
    }
}
