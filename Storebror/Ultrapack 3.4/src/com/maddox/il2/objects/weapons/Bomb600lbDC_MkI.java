package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb600lbDC_MkI extends Bomb {

    static {
        Class class1 = Bomb600lbDC_MkI.class;
        Property.set(class1, "mesh", "3do/arms/600lbDC_MkI/mono.sim");
        Property.set(class1, "radius", 99F);
        Property.set(class1, "power", 196.1F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.45F);
        Property.set(class1, "massa", 249.7F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_No862.class, Fuze_No875C.class, Fuze_No895_30.class })));
    }
}
