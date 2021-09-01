package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250lbGP_AN_M57 extends BombProximity
{

    static 
    {
        Class class1 = Bomb250lbGP_AN_M57.class;
        Property.set(class1, "mesh", "3do/arms/250lbGP_AN_M57/mono.sim");
        Property.set(class1, "radius", 60F);
        Property.set(class1, "power", 58.572F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.28F);
        Property.set(class1, "massa", 118.04F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_AN_M103.class, Fuze_M160.class, Fuze_M112.class, Fuze_M115.class, Fuze_T91.class
        })));
    }
}
