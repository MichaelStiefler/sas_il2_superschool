package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIT500Kg_Gio extends Bomb {
    static {
        Class class1 = BombIT500Kg_Gio.class;
        Property.set(class1, "mesh", "3do/arms/IT500Kg_Gio/mono.sim");
        Property.set(class1, "radius", 275F);
        Property.set(class1, "power", 220F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.458F);
        Property.set(class1, "massa", 508F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
