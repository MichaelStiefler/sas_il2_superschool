package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb650lbDC_AN_Mk29 extends Bomb {

    static {
        Class class1 = Bomb650lbDC_AN_Mk29.class;
        Property.set(class1, "mesh", "3do/arms/650lbDC_AN_Mk29/mono.sim");
        Property.set(class1, "radius", 103F);
        Property.set(class1, "power", 210.5F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.45F);
        Property.set(class1, "massa", 298F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_AN_M103.class, Fuze_AN_MK_224.class })));
    }
}
