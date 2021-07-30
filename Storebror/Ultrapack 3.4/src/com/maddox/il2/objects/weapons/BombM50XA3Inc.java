package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombM50XA3Inc extends Bomb {

    static {
        Class class1 = BombM50XA3Inc.class;
        Property.set(class1, "mesh", "3DO/Arms/SD-2A/mono.sim");
        Property.set(class1, "radius", 1.0F);
        Property.set(class1, "power", 0.44F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.0406F);
        Property.set(class1, "massa", 1.6F);
        Property.set(class1, "sound", "weapon.bomb_cassette");
    }
}
