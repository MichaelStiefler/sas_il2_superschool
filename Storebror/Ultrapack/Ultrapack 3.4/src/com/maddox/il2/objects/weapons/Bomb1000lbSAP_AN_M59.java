package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb1000lbSAP_AN_M59 extends Bomb {

    static {
        Class class1 = Bomb1000lbSAP_AN_M59.class;
        Property.set(class1, "mesh", "3do/arms/1000lbSAP_AN_M59/mono.sim");
        Property.set(class1, "radius", 83F);
        Property.set(class1, "power", 145F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.38F);
        Property.set(class1, "massa", 451.3F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_M162.class, Fuze_AN_M102A2.class, Fuze_M114.class, Fuze_M117.class })));
    }
}
