package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb800lbAP_M61 extends Bomb {

    static {
        Class class1 = Bomb800lbAP_M61.class;
        Property.set(class1, "mesh", "3do/arms/800lbAP_M61/mono.sim");
        Property.set(class1, "radius", 23F);
        Property.set(class1, "power", 14.82F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.31F);
        Property.set(class1, "massa", 387F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_AN_M102A1.class })));
    }
}
