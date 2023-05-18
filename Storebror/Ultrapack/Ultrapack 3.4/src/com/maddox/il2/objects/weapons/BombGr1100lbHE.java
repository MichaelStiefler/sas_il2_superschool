package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGr1100lbHE extends Bomb {
    static {
        Class class1 = BombGr1100lbHE.class;
        Property.set(class1, "mesh", "3do/arms/Gr1100lbHE/mono.sim");
        Property.set(class1, "radius", 120F);
        Property.set(class1, "power", 280.3F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.49F);
        Property.set(class1, "massa", 517.5F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
