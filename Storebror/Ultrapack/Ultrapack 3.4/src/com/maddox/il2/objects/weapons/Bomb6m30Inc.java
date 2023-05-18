package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb6m30Inc extends Bomb {
    static {
        Class class1 = Bomb6m30Inc.class;
        Property.set(class1, "mesh", "3DO/Arms/6kgInc_M1930/mono.sim");
        Property.set(class1, "radius", 15F);
        Property.set(class1, "power", 3F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.1102F);
        Property.set(class1, "massa", 6F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
