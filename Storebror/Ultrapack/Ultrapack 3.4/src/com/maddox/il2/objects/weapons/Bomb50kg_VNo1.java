package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb50kg_VNo1 extends Bomb {
    static {
        Class class1 = Bomb50kg_VNo1.class;
        Property.set(class1, "mesh", "3DO/Arms/50kg_VNo1/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 25F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
