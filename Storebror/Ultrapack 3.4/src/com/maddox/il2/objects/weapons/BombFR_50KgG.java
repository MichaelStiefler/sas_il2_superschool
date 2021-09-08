package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFR_50KgG extends Bomb {
    static {
        Class class1 = BombFR_50KgG.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFr50KgGAM/mono.sim");
        Property.set(class1, "radius", 50F);
        Property.set(class1, "power", 25F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 50F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
