package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomblet2KgI extends Bomb {
    static {
        Class class1 = Bomblet2KgI.class;
        Property.set(class1, "mesh", "3DO/Arms/2KgIncIt/mono.sim");
        Property.set(class1, "radius", 2F);
        Property.set(class1, "power", 2F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.17F);
        Property.set(class1, "massa", 4F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
