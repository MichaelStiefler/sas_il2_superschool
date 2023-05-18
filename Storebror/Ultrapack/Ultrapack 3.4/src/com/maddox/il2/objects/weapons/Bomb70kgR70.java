package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb70kgR70 extends Bomb {
    static {
        Class class1 = Bomb70kgR70.class;
        Property.set(class1, "mesh", "3do/arms/R70-70Kg/mono.sim");
        Property.set(class1, "radius", 35F);
        Property.set(class1, "power", 36F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.3405F);
        Property.set(class1, "massa", 70F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
