package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb600lbAP_M62 extends Bomb {

    static {
        Class class1 = Bomb600lbAP_M62.class;
        Property.set(class1, "mesh", "3do/arms/600lbAP_M62/mono.sim");
        Property.set(class1, "radius", 23F);
        Property.set(class1, "power", 15.2F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.25F);
        Property.set(class1, "massa", 287.6F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_AN_M102A1.class })));
    }
}
