package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb325lbDC_AN_Mk41 extends Bomb
{

    static 
    {
        Class class1 = Bomb325lbDC_AN_Mk41.class;
        Property.set(class1, "mesh", "3do/arms/325lbDC_AN_Mk41/mono.sim");
        Property.set(class1, "radius", 68F);
        Property.set(class1, "power", 103F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.38F);
        Property.set(class1, "massa", 149.7F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_AN_M103.class, Fuze_AN_MK_224.class
        })));
    }
}
