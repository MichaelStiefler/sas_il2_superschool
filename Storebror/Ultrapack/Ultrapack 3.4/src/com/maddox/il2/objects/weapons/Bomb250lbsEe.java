package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250lbsEe extends Bomb {
    static {
        Class class1 = Bomb250lbsEe.class;
        Property.set(class1, "mesh", "3DO/Arms/250LbsBombEe/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 50F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.35F);
        Property.set(class1, "massa", 113F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
