package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb450lbAP_MkI extends Bomb
{

    static 
    {
        Class class1 = Bomb450lbAP_MkI.class;
        Property.set(class1, "mesh", "3do/arms/450lbAP_MkI/mono.sim");
        Property.set(class1, "radius", 27F);
        Property.set(class1, "power", 21F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.23F);
        Property.set(class1, "massa", 200F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_Pistol_No28.class, Fuze_Pistol_No30.class, Fuze_Pistol_No37.class
        })));
    }
}
