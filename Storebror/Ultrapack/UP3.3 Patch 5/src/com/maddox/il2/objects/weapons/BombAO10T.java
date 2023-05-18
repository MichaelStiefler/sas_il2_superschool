package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombAO10T extends Bomb
{

    protected boolean haveSound()
    {
        return index % 16 == 0;
    }

    static 
    {
        Class class1 = BombAO10T.class;
        Property.set(class1, "mesh", "3do/arms/ao-10/mono.sim");
        Property.set(class1, "power", 1.01F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.14F);
        Property.set(class1, "massa", 9.56F);
        Property.set(class1, "randomOrient", 1);
        Property.set(class1, "sound", "weapon.bomb_cassette");
//        Property.set(class1, "fuze", ((Object) (new Object[] {
//            Fuze_AM_A.class, Fuze_AVSh_2.class
//        })));
    }
}
