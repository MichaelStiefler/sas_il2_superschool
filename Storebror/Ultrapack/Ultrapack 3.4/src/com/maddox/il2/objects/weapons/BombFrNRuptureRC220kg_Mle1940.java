package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFrNRuptureRC220kg_Mle1940 extends Bomb {
    static {
        Class class1 = BombFrNRuptureRC220kg_Mle1940.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFrNRuptureRC220kg_Mle1940/mono.sim");
        Property.set(class1, "radius", 12F);
        Property.set(class1, "power", 5.1F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.24F);
        Property.set(class1, "massa", 218.2F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((new Object[] { Fuze_Culot_RC.class })));
    }
}
