package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombM24Sjunkbomb extends Bomb {

    static {
        Class class1 = BombM24Sjunkbomb.class;
        Property.set(class1, "mesh", "3DO/Arms/M24_Sjunkbomb/mono.sim");
        Property.set(class1, "radius", 12F);
        Property.set(class1, "power", 100F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.43F);
        Property.set(class1, "massa", 135F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
