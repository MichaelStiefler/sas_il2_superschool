// TODO: Edited by SAS~Storebror 0n 2019-12-26: Increase explosive power to balance reduction of spawned bomblets to reasonable figures, code cleanup.

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombletBLU26_10x extends Bomb
{

    protected boolean haveSound()
    {
        return index % 8 == 0;
    }

    static 
    {
        Class class1 = BombletBLU26_10x.class;
        Property.set(class1, "mesh", "3do/arms/BLU26Bomlet/mono_10x.sim");
        Property.set(class1, "power", 1.0F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.064F);
        Property.set(class1, "massa", 0.435F);
        Property.set(class1, "randomOrient", 1);
        Property.set(class1, "sound", "weapon.bomb_cassette");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_IT_Standard3.class
        })));
    }
}
