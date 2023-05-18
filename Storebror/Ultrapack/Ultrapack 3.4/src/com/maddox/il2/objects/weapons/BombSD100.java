package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombSD100 extends Bomb {

    static {
        Class class1 = BombSD100.class;
        Property.set(class1, "mesh", "3DO/Arms/SD100/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 45F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 100F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
