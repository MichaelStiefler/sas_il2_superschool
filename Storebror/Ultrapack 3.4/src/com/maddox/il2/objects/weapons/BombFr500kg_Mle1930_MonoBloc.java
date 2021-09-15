package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFr500kg_Mle1930_MonoBloc extends Bomb {
    static {
        Class class1 = BombFr500kg_Mle1930_MonoBloc.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFr500kg_Mle1930_MonoBloc/mono.sim");
        Property.set(class1, "radius", 119F);
        Property.set(class1, "power", 270F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.49F);
        Property.set(class1, "massa", 570F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
