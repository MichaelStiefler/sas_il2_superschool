package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250lbDC_MkXI_Tail extends Bomb {

    static {
        Class class1 = Bomb250lbDC_MkXI_Tail.class;
        Property.set(class1, "mesh", "3do/arms/250lbDC_MkXI_T/mono.sim");
        Property.set(class1, "radius", 59F);
        Property.set(class1, "power", 79.3F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.3F);
        Property.set(class1, "massa", 120.2F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Pistol_MkXIX.class, Fuze_Pistol_MkXX.class })));
    }
}
