package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb300kg_VMNo2 extends Bomb {

    static {
        Class class1 = Bomb300kg_VMNo2.class;
        Property.set(class1, "mesh", "3do/arms/300kg_VMNo2/mono.sim");
        Property.set(class1, "radius", 200F);
        Property.set(class1, "power", 145F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.4F);
        Property.set(class1, "massa", 300F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
