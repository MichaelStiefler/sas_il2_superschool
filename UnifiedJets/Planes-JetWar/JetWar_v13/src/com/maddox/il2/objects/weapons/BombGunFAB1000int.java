package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFAB1000int extends BombGun
{

    public BombGunFAB1000int()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunFAB1000int.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombFAB1000.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.0F);
        Property.set(class1, "external", 0);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}