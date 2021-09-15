package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFrN225kg_TypeM_Mle1938 extends Bomb {
    static {
        Class class1 = BombFrN225kg_TypeM_Mle1938.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFrN225kg_TypeM_Mle1938/mono.sim");
        Property.set(class1, "radius", 89F);
        Property.set(class1, "power", 162F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.4F);
        Property.set(class1, "massa", 228F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Culot_No8.class })));
    }
}
