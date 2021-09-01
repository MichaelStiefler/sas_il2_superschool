package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb1000lbGP_AN_M65 extends BombProximity
{

    static 
    {
        Class class1 = Bomb1000lbGP_AN_M65.class;
        Property.set(class1, "mesh", "3do/arms/1000lbGP_AN_M65/mono.sim");
        Property.set(class1, "radius", 109F);
        Property.set(class1, "power", 253.3F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.48F);
        Property.set(class1, "massa", 449.46F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_AN_M103.class, Fuze_M117.class, Fuze_AN_MK_230.class, Fuze_MK_243.class, Fuze_T92.class
        })));
    }
}
