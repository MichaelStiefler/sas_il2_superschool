// Last Modified by: western0221 2020-01-12

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb

public class BombPTAB1M extends Bomb
{

    protected boolean haveSound()
    {
        return index % 25 == 0;
    }

    static 
    {
        Class class1 = BombPTAB1M.class;
        Property.set(class1, "mesh", "3do/arms/AO1Sch_bomblet/mono.sim");
        Property.set(class1, "radius", 0.35F);
        Property.set(class1, "power", 2.8F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.07F);
        Property.set(class1, "massa", 1.0F);
        Property.set(class1, "sound", "weapon.bomb_cassette");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_AD_A.class
        })));
    }
}
