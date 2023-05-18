package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFrN150kg_TypeI2 extends Bomb {
    static {
        Class class1 = BombFrN150kg_TypeI2.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFrN150kg_TypeI2/mono.sim");
        Property.set(class1, "radius", 12.3F);
        Property.set(class1, "power", 113F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.35F);
        Property.set(class1, "massa", 150F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Culot_No7_I2.class })));
    }
}
