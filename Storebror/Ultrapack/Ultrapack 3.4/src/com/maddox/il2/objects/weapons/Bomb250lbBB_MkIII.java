package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250lbBB_MkIII extends Bomb {

    static {
        Class class1 = Bomb250lbBB_MkIII.class;
        Property.set(class1, "mesh", "3do/arms/250lbBB_MkIII/mono.sim");
        Property.set(class1, "radius", 47F);
        Property.set(class1, "power", 52.9F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.36F);
        Property.set(class1, "massa", 132F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_No850.class })));
    }
}
