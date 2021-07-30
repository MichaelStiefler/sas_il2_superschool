package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb4000HCmkI extends Bomb {

    static {
        Class class1 = Bomb4000HCmkI.class;
        Property.set(class1, "mesh", "3DO/Arms/Bomb4000HCmkI/mono.sim");
        Property.set(class1, "radius", 400F);
        Property.set(class1, "power", 1380F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.76F);
        Property.set(class1, "massa", 1780F);
        Property.set(class1, "sound", "weapon.bomb_big");
    }
}
