package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFr100kg_Rasant extends Bomb {
    static {
        Class class1 = BombFr100kg_Rasant.class;
        Property.set(class1, "mesh", "3DO/Arms/BombFr100kg_Rasant/mono.sim");
        Property.set(class1, "radius", 45F);
        Property.set(class1, "power", 49.5F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.275F);
        Property.set(class1, "massa", 117F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
