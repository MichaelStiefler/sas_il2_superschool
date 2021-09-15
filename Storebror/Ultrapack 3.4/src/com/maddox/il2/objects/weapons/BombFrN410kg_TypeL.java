package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFrN410kg_TypeL extends Bomb {
    static {
        Class class1 = BombFrN410kg_TypeL.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFrN410kg_TypeL/mono.sim");
        Property.set(class1, "radius", 75F);
        Property.set(class1, "power", 120F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.4F);
        Property.set(class1, "massa", 410F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Ogive_L.class, Fuze_Culot_No7_L.class })));
    }
}
