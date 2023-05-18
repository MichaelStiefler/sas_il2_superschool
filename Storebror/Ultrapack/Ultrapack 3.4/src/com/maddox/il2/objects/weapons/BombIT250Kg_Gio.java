package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIT250Kg_Gio extends Bomb {
    static {
        Class class1 = BombIT250Kg_Gio.class;
        Property.set(class1, "mesh", "3do/arms/IT250Kg_Gio/mono.sim");
        Property.set(class1, "radius", 90F);
        Property.set(class1, "power", 106.5F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.38F);
        Property.set(class1, "massa", 259F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
