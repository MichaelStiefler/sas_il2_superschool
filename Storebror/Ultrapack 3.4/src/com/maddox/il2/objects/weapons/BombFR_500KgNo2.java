package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFR_500KgNo2 extends Bomb {
    static {
        Class class1 = BombFR_500KgNo2.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFr500KgNo2/mono.sim");
        Property.set(class1, "radius", 500F);
        Property.set(class1, "power", 270F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.5F);
        Property.set(class1, "massa", 520F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
