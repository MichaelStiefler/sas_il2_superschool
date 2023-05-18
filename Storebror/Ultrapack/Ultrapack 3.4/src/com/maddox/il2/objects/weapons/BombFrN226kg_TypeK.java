package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFrN226kg_TypeK extends Bomb {
    static {
        Class class1 = BombFrN226kg_TypeK.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFrN226kg_TypeK/mono.sim");
        Property.set(class1, "radius", 70F);
        Property.set(class1, "power", 107F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.37F);
        Property.set(class1, "massa", 226F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Ogive_No10.class, Fuze_Culot_No7_K.class })));
    }
}
