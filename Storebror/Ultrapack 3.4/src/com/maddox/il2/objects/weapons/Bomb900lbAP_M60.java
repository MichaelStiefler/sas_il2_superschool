package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb900lbAP_M60 extends Bomb {

    static {
        Class class1 = Bomb900lbAP_M60.class;
        Property.set(class1, "mesh", "3do/arms/900lbAP_M60/mono.sim");
        Property.set(class1, "radius", 26F);
        Property.set(class1, "power", 18.8F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.31F);
        Property.set(class1, "massa", 403.2F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_AN_M102A1.class })));
    }
}
