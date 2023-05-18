package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFAB50SV extends Bomb {
    static {
        Class class1 = BombFAB50SV.class;
        Property.set(class1, "mesh", "3do/arms/FAB-50SV/mono.sim");
        Property.set(class1, "radius", 25F);
        Property.set(class1, "power", 24F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.24F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
