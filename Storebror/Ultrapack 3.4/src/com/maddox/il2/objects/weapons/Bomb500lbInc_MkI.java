package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbInc_MkI extends Bomb {

    static {
        Class class1 = Bomb500lbInc_MkI.class;
        Property.set(class1, "mesh", "3do/arms/500lbInc_MkI/mono.sim");
        Property.set(class1, "radius", 49F);
        Property.set(class1, "power", 58F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.33F);
        Property.set(class1, "massa", 226.5F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Pistol_No30.class })));
    }
}
