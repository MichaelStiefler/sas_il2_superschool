package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb70KgCatalana extends Bomb {
    static {
        Class class1 = Bomb70KgCatalana.class;
        Property.set(class1, "mesh", "3do/arms/Catalana_70Kg/mono.sim");
        Property.set(class1, "radius", 35F);
        Property.set(class1, "power", 36F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.2505F);
        Property.set(class1, "massa", 70F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
