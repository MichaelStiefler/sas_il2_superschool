package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIT50Kg_T_Early extends Bomb {
    static {
        Class class1 = BombIT50Kg_T_Early.class;
        Property.set(class1, "mesh", "3do/arms/IT50Kg_T_Early/mono.sim");
        Property.set(class1, "radius", 35F);
        Property.set(class1, "power", 25F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.252F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
