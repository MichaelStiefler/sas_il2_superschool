package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb25kgR25 extends Bomb {
    static {
        Class class1 = Bomb25kgR25.class;
        Property.set(class1, "mesh", "3DO/Arms/R25-25Kg/mono.sim");
        Property.set(class1, "radius", 30F);
        Property.set(class1, "power", 15F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 25F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
