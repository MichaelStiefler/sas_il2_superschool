package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbGP_AN_M43 extends Bomb
{

    static 
    {
        Class class1 = Bomb500lbGP_AN_M43.class;
        Property.set(class1, "mesh", "3do/arms/500lbGP_AN_M43/mono.sim");
        Property.set(class1, "radius", 84F);
        Property.set(class1, "power", 121.22F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.36F);
        Property.set(class1, "massa", 238.35F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_AN_M103.class
        })));
    }
}
