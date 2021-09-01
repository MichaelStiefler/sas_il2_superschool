package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbGP_MkIII extends Bomb
{

    static 
    {
        Class class1 = Bomb500lbGP_MkIII.class;
        Property.set(class1, "mesh", "3do/arms/500lbGP_MkIII/mono.sim");
        Property.set(class1, "radius", 95F);
        Property.set(class1, "power", 65F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.33F);
        Property.set(class1, "massa", 213F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_Pistol_No19.class, Fuze_Pistol_No17.class, Fuze_Pistol_No22.class
        })));
    }
}
