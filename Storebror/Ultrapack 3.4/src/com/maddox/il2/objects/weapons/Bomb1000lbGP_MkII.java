package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb1000lbGP_MkII extends Bomb
{

    static 
    {
        Class class1 = Bomb1000lbGP_MkII.class;
        Property.set(class1, "mesh", "3do/arms/1000lbGP_MkII/mono.sim");
        Property.set(class1, "radius", 140F);
        Property.set(class1, "power", 162F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.41F);
        Property.set(class1, "massa", 486.7F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_Pistol_No27.class, Fuze_Pistol_No42.class, Fuze_Pistol_No44.class, Fuze_Pistol_No28.class, Fuze_Pistol_No30.class, Fuze_Pistol_No37.class, Fuze_No845.class
        })));
    }
}
