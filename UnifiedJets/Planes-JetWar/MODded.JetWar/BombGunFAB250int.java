package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunFAB250int extends BombGun
{

    public BombGunFAB250int()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunFAB250int.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombFAB250.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 0);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}