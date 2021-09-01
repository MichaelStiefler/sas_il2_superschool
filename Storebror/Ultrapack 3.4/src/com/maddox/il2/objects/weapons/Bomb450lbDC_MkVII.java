package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb450lbDC_MkVII extends Bomb
{

    static 
    {
        Class class1 = Bomb450lbDC_MkVII.class;
        Property.set(class1, "mesh", "3do/arms/450lbDC_MkVII/mono.sim");
        Property.set(class1, "radius", 79F);
        Property.set(class1, "power", 131.7F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.45F);
        Property.set(class1, "massa", 204F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        Property.set(class1, "fuze", ((Object) (new Object[] {
            Fuze_Pistol_MkX.class
        })));
    }
}
