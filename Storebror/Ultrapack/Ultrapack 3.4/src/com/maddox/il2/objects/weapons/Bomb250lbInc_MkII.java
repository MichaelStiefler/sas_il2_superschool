package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250lbInc_MkII extends Bomb {

    static {
        Class class1 = Bomb250lbInc_MkII.class;
        Property.set(class1, "mesh", "3do/arms/250lbInc_MkII/mono.sim");
        Property.set(class1, "radius", 48F);
        Property.set(class1, "power", 55F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.3F);
        Property.set(class1, "massa", 87.4F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_No36.class })));
    }
}
