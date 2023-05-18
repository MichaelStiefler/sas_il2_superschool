package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombRRAB1Nose extends Bomb {

    static {
        Class class1 = BombRRAB1Nose.class;
        Property.set(class1, "mesh", "3DO/Arms/RRAB1/mono2.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.72F);
        Property.set(class1, "massa", 20F);
    }
}
