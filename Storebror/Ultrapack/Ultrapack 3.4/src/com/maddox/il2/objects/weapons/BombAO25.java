package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombAO25 extends Bomb {
    protected boolean haveSound() {
        return this.index % 20 == 0;
    }

    static {
        Class class1 = BombAO25.class;
        Property.set(class1, "mesh", "3do/arms/ao25/mono.sim");
        Property.set(class1, "power", 0.1F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.14F);
        Property.set(class1, "massa", 1.5F);
        Property.set(class1, "sound", "weapon.bomb_cassette");
    }
}
