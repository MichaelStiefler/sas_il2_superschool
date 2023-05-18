package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb25kg_VNo2 extends Bomb {
    static {
        Class class1 = Bomb25kg_VNo2.class;
        Property.set(class1, "mesh", "3DO/Arms/25kg_VNo2/mono.sim");
        Property.set(class1, "radius", 30F);
        Property.set(class1, "power", 15F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.25F);
        Property.set(class1, "massa", 25F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
