package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombM41A1 extends Bomb {

    static {
        Class class1 = BombM41A1.class;
        Property.set(class1, "mesh", "3DO/Arms/M41A1Frag/mono.sim");
        Property.set(class1, "power", 4F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.2F);
        Property.set(class1, "massa", 8F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
