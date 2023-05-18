package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunHeisenbergPara extends BombGun
{
    static 
    {
        Class class1 = BombGunHeisenbergPara.class;
        Property.set(class1, "bulletClass", (Object)BombHeisenbergPara.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.05F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
