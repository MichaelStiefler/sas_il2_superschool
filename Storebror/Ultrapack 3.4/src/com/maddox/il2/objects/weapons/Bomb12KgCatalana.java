package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb12KgCatalana extends Bomb {
    static {
        Class class1 = Bomb12KgCatalana.class;
        Property.set(class1, "mesh", "3do/arms/Catalana_12Kg/mono.sim");
        Property.set(class1, "radius", 12F);
        Property.set(class1, "power", 7F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.15F);
        Property.set(class1, "massa", 12.5F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
