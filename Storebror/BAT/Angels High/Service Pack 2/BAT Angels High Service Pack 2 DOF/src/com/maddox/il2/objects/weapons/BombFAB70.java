package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFAB70 extends Bomb {

    static {
        Class class1 = BombFAB70.class;
        Property.set(class1, "mesh", "3do/arms/FAB-70/mono.sim");
        Property.set(class1, "radius", 35F);
        Property.set(class1, "power", 36F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.3405F);
        Property.set(class1, "massa", 70F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
