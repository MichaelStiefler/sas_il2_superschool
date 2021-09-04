package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb12M37Min extends Bomb {
    static {
        Class class1 = Bomb12M37Min.class;
        Property.set(class1, "mesh", "3do/arms/MinBomb12M37/mono.sim");
        Property.set(class1, "radius", 25F);
        Property.set(class1, "power", 8F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.1124F);
        Property.set(class1, "massa", 12F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
