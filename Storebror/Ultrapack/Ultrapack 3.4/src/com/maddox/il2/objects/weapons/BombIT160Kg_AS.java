package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIT160Kg_AS extends Bomb {
    static {
        Class class1 = BombIT160Kg_AS.class;
        Property.set(class1, "mesh", "3DO/Arms/IT160Kg_AS/mono.sim");
        Property.set(class1, "radius", 16F);
        Property.set(class1, "power", 25F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.337F);
        Property.set(class1, "massa", 160F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
