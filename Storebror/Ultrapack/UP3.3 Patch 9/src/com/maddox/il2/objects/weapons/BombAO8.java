package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombAO8 extends Bomb {

    protected boolean haveSound() {
        return (this.index % 16) == 0;
    }

    static {
        Class class1 = BombAO8.class;
        Property.set(class1, "mesh", "3do/arms/ao-8m3/mono.sim");
        Property.set(class1, "power", 0.91F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.076F);
        Property.set(class1, "massa", 8F);
        Property.set(class1, "randomOrient", 1);
        Property.set(class1, "sound", "weapon.bomb_cassette");
//        Property.set(class1, "fuze", ((Object) (new Object[] {
//            Fuze_AM_A.class, Fuze_AVSh_2.class
//        })));
    }
}
