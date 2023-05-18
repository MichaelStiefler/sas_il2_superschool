// Last Modified by: western0221 2020-01-12

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb

public class BombAO1Sch_10x extends Bomb
{

    protected boolean haveSound()
    {
        return index % 5 == 0;
    }

    static 
    {
        Class class1 = BombAO1Sch_10x.class;
        Property.set(class1, "mesh", "3do/arms/AO1Sch_bomblet/mono_10x.sim");
        Property.set(class1, "radius", 27.0F);
        Property.set(class1, "power", 1.0F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.07F);
        Property.set(class1, "massa", 1.3F);
        Property.set(class1, "sound", "weapon.bomb_cassette");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_AD_A.class
        })));
    }
}
