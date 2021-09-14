package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFR_500KgNo1 extends Bomb {
    static {
        Class class1 = BombFR_500KgNo1.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFr500KgNo1/mono.sim");
        Property.set(class1, "radius", 550F);
        Property.set(class1, "power", 304F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.545F);
        Property.set(class1, "massa", 536F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
