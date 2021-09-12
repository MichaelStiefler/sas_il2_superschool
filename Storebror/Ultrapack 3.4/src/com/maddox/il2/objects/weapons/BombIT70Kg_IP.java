package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIT70Kg_IP extends Bomb {
    static {
        Class class1 = BombIT70Kg_IP.class;
        Property.set(class1, "mesh", "3DO/Arms/IT70Kg_IP/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 24.5F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.252F);
        Property.set(class1, "massa", 62F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
