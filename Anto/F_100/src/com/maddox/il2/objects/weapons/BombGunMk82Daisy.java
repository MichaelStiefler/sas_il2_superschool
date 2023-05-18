// Source File Name:   BombGunMk82.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunMk82Daisy extends BombGun
{

    public BombGunMk82Daisy()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunMk82Daisy.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombMk82Daisy.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}