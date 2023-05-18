package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb100m37 extends Bomb {
    static {
        Class class1 = Bomb100m37.class;
        Property.set(class1, "mesh", "3do/arms/100kg_M1937/mono.sim");
        Property.set(class1, "radius", 77F);
        Property.set(class1, "power", 48F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.242F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
