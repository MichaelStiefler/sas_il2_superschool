package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFrNRupture150kg_Mle1938 extends Bomb {
    static {
        Class class1 = BombFrNRupture150kg_Mle1938.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFrNRupture150kg_Mle1938/mono.sim");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "power", 12.7F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.24F);
        Property.set(class1, "massa", 153.5F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Culot_RC.class })));
    }
}
