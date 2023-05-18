package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombAO8M3 extends Bomb {

    static {
        Class class1 = BombAO8M3.class;
        Property.set(class1, "mesh", "3do/arms/ao-8m3/mono.sim");
        Property.set(class1, "power", 0.91F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.076F);
        Property.set(class1, "massa", 8F);
        Property.set(class1, "sound", "weapon.bomb_cassette");
//        Property.set(class1, "fuze", ((Object) (new Object[] {
//            com.maddox.il2.objects.weapons.Fuze_AM_A.class, com.maddox.il2.objects.weapons.Fuze_AVSh_2.class
//        })));
    }
}
