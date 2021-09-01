package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb500lbSAP_MkV extends Bomb
{

    static 
    {
        Class class1 = Bomb500lbSAP_MkV.class;
        Property.set(class1, "mesh", "3do/arms/500lbSAP_MkV/mono.sim");
        Property.set(class1, "radius", 40F);
        Property.set(class1, "power", 40F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.34F);
        Property.set(class1, "massa", 220F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_Pistol_No28.class, Fuze_Pistol_No30.class, Fuze_Pistol_No37.class
        })));
    }
}
