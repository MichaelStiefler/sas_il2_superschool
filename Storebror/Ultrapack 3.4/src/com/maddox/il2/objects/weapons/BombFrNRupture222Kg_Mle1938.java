package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFrNRupture222Kg_Mle1938 extends Bomb {
    static {
        Class class1 = BombFrNRupture222Kg_Mle1938.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFrNRupture222Kg_Mle1938/mono.sim");
        Property.set(class1, "radius", 29F);
        Property.set(class1, "power", 23F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.27F);
        Property.set(class1, "massa", 221.8F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Culot_RC.class })));
    }
}
