package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFAB250m46 extends BombGun
{

    public BombGunFAB250m46()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunFAB250m46.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombFAB250m46.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}