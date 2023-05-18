package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb

public class BombAO2_5_3_4x extends Bomb
{

    protected boolean haveSound()
    {
        return super.index % 8 == 0;
    }

    static 
    {
        Class class1 = BombAO2_5_3_4x.class;
        Property.set(class1, "mesh", "3do/arms/AO-2_5-3/mono_4x.sim");
        Property.set(class1, "radius", 16F);
        Property.set(class1, "power", 0.48F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.1F);
        Property.set(class1, "massa", 2.0F);
        Property.set(class1, "randomOrient", 1);
        Property.set(class1, "sound", "weapon.bomb_cassette");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_AM_A.class, Fuze_AVSh_2.class
        })));
    }
}