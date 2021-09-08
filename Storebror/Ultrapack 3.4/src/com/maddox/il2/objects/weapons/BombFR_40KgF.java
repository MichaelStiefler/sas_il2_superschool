package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFR_40KgF extends Bomb {
    static {
        Class class1 = BombFR_40KgF.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFr40kgFrag/mono.sim");
        Property.set(class1, "radius", 60F);
        Property.set(class1, "power", 25F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 40F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
