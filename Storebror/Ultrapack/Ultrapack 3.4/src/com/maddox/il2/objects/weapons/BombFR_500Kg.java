package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFR_500Kg extends Bomb {
    static {
        Class class1 = BombFR_500Kg.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFr500Kg/mono.sim");
        Property.set(class1, "radius", 170F);
        Property.set(class1, "power", 216F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.38F);
        Property.set(class1, "massa", 572F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
