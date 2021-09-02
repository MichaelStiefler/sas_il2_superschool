package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbAS_MkIV extends Bomb {

    static {
        Class class1 = Bomb500lbAS_MkIV.class;
        Property.set(class1, "mesh", "3do/arms/500lbAS_MkIV/mono.sim");
        Property.set(class1, "radius", 79F);
        Property.set(class1, "power", 132F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.36F);
        Property.set(class1, "massa", 210F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Pistol_No28.class, Fuze_Pistol_No30.class })));
    }
}
