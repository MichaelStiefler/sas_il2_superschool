package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FTGunL extends FuelTankGun
{

    public FTGunL()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FTGunL.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.FTL.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
    }
}