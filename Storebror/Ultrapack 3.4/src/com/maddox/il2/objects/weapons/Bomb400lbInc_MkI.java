package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb400lbInc_MkI extends Bomb {

    static {
        Class class1 = Bomb400lbInc_MkI.class;
        Property.set(class1, "mesh", "3do/arms/400lbInc_MkI/mono.sim");
        Property.set(class1, "radius", 72F);
        Property.set(class1, "power", 114F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.45F);
        Property.set(class1, "massa", 181.2F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Pistol_No60.class })));
    }
}
