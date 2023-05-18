package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFAB250m46 extends Bomb
{

    public BombFAB250m46()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombFAB250m46.class;
        Property.set(class1, "mesh", "3do/arms/fab-250m-46/mono.sim");
        Property.set(class1, "radius", 80F);
        Property.set(class1, "power", 130F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.408F);
        Property.set(class1, "massa", 250F);
        Property.set(class1, "sound", "weapon.bomb_std");
		Property.set(class1, "fuze", new Object[] { Fuze_APUV.class, Fuze_APUV_M.class, Fuze_APUV_1.class, Fuze_AV_1du.class, Fuze_AV_1.class, Fuze_AV_87.class });
    }
}