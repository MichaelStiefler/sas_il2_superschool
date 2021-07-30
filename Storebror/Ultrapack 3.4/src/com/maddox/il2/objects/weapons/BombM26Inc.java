package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombM26Inc extends Bomb {

    static {
        Class class1 = BombM26Inc.class;
        Property.set(class1, "mesh", "3DO/Arms/M26Incendiary/mono.sim");
        Property.set(class1, "radius", 35F);
        Property.set(class1, "power", 50F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 260F);
        Property.set(class1, "sound", "weapon.bomb_phball");
    }
}
