package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFrNRuptureRC438kg_Mle1938 extends Bomb {
    static {
        Class class1 = BombFrNRuptureRC438kg_Mle1938.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFrNRuptureRC438kg_Mle1938/mono.sim");
        Property.set(class1, "radius", 19F);
        Property.set(class1, "power", 11.6F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.31F);
        Property.set(class1, "massa", 437.4F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Culot_RC.class })));
    }
}
