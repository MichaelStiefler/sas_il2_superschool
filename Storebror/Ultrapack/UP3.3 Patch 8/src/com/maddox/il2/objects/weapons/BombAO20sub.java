package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombAO20sub extends Bomb
{

    protected boolean haveSound()
    {
        return index % 16 == 0;
    }

    static 
    {
        Class class1 = BombAO20sub.class;
        Property.set(class1, "mesh", "3do/arms/ao-10/mono.sim");
        Property.set(class1, "power", 10.01F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.24F);
        Property.set(class1, "massa", 19.56F);
        Property.set(class1, "sound", "weapon.bomb_cassette");
//        Property.set(class1, "fuze", ((Object) (new Object[] {
//            com.maddox.il2.objects.weapons.Fuze_AM_A.class
//        })));
    }
}
