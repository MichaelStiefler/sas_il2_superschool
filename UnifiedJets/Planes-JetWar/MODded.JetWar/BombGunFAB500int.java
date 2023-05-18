package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFAB500int extends BombGun
{

    public BombGunFAB500int()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunFAB500int.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombFAB500.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.5F);
        Property.set(class1, "external", 0);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}