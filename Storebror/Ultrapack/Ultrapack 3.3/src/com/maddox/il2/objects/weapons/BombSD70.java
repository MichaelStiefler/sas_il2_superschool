package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombSD70 extends Bomb {
    static {
        Class class1 = BombSD70.class;
        Property.set(class1, "mesh", "3do/arms/SD70/mono.sim");
        Property.set(class1, "radius", 58F);
        Property.set(class1, "power", 32F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.22F);
        Property.set(class1, "massa", 70F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
