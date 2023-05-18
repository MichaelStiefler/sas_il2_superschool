package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFr140kg_Mle1925_mod1932 extends Bomb {
    static {
        Class class1 = BombFr140kg_Mle1925_mod1932.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFr140kg_Mle1925_mod1932/mono.sim");
        Property.set(class1, "radius", 51F);
        Property.set(class1, "power", 63F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.258F);
        Property.set(class1, "massa", 145F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
