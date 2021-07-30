package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombMk53Charge extends Bomb {

    static {
        Class class1 = BombMk53Charge.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk53_Charge/mono.sim");
        Property.set(class1, "radius", 90F);
        Property.set(class1, "power", 90F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 148F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
