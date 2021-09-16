package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFAB100SV extends Bomb {
    static {
        Class class1 = BombFAB100SV.class;
        Property.set(class1, "mesh", "3do/arms/FAB-100SV/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 48F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.28F);
        Property.set(class1, "massa", 113F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
