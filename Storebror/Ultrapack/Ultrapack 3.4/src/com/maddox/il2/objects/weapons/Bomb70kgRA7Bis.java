package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb70kgRA7Bis extends Bomb {
    static {
        Class class1 = Bomb70kgRA7Bis.class;
        Property.set(class1, "mesh", "3DO/Arms/RA-7Bis_70Kg/mono.sim");
        Property.set(class1, "radius", 70F);
        Property.set(class1, "power", 30F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.34F);
        Property.set(class1, "massa", 70F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
