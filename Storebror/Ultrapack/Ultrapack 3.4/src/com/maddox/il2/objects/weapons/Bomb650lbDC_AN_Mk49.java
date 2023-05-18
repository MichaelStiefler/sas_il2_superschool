package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb650lbDC_AN_Mk49 extends Bomb {

    static {
        Class class1 = Bomb650lbDC_AN_Mk49.class;
        Property.set(class1, "mesh", "3do/arms/650lbDC_AN_Mk49/mono.sim");
        Property.set(class1, "radius", 104F);
        Property.set(class1, "power", 214F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.45F);
        Property.set(class1, "massa", 308.9F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_AN_M103.class, Fuze_AN_MK_230.class })));
    }
}
