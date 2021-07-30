package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbInc_AN_M76 extends Bomb {

    static {
        Class class1 = Bomb500lbInc_AN_M76.class;
        Property.set(class1, "mesh", "3do/arms/500lbInc_AN_M76/mono.sim");
        Property.set(class1, "radius", 60F);
        Property.set(class1, "power", 81.7F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.36F);
        Property.set(class1, "massa", 215F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_AN_M103.class, Fuze_M161.class })));
    }
}
