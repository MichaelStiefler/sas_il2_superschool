package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb325lbDC_AN_Mk17 extends Bomb {

    static {
        Class class1 = Bomb325lbDC_AN_Mk17.class;
        Property.set(class1, "mesh", "3do/arms/325lbDC_AN_Mk17/mono.sim");
        Property.set(class1, "radius", 68F);
        Property.set(class1, "power", 101.05F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.38F);
        Property.set(class1, "massa", 147.4F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_AN_M103.class, Fuze_AN_MK_224.class })));
    }
}
