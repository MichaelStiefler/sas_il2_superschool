// US Mk-118 Bomblet class for Mk-20 RockeyeII Cluster bomb

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class BombletMk118 extends Bomb
{

    protected boolean haveSound()
    {
        return index % 64 == 0;
    }

    static 
    {
        Class class1 = BombletMk118.class;
        Property.set(class1, "mesh", "3do/arms/Mk118Bomlet/mono.sim");
        Property.set(class1, "radius", 2.0F);
        Property.set(class1, "power", 8.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.084F);
        Property.set(class1, "massa", 0.600F);
        Property.set(class1, "randomOrient", 1);
        Property.set(class1, "sound", "weapon.bomb_cassette");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_IT_Standard3.class
        })));
    }
}
