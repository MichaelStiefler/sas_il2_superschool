package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFrN75kg_TypeG2 extends Bomb {
    static {
        Class class1 = BombFrN75kg_TypeG2.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFrN75kg_TypeG2/mono.sim");
        Property.set(class1, "radius", 9.1F);
        Property.set(class1, "power", 46.5F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.27F);
        Property.set(class1, "massa", 73F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Culot_No7_G2.class })));
    }
}
