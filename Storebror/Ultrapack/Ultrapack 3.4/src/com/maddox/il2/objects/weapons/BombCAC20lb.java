package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombCAC20lb extends Bomb {

    public BombCAC20lb() {
    }

    static {
        Class class1 = BombCAC20lb.class;
        Property.set(class1, "mesh", "3DO/Arms/20LbsBombCAC/mono.sim");
        Property.set(class1, "radius", 30F);
        Property.set(class1, "power", 25F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 8F);
        Property.set(class1, "sound", "weapon.bomb_mid");
    }
}
