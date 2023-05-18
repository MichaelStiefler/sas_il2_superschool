package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFrN491kg_TypeL1_Vichy extends Bomb {
    static {
        Class class1 = BombFrN491kg_TypeL1_Vichy.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFrN491kg_TypeL1_Vichy/mono.sim");
        Property.set(class1, "radius", 70F);
        Property.set(class1, "power", 108F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.4F);
        Property.set(class1, "massa", 491F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Ogive_L.class, Fuze_Culot_No7_L.class })));
    }
}
