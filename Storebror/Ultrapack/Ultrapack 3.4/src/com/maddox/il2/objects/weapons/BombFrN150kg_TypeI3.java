package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFrN150kg_TypeI3 extends Bomb {
    static {
        Class class1 = BombFrN150kg_TypeI3.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFrN150kg_TypeI3/mono.sim");
        Property.set(class1, "radius", 11.9F);
        Property.set(class1, "power", 102F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.36F);
        Property.set(class1, "massa", 150F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Culot_No8.class })));
    }
}
