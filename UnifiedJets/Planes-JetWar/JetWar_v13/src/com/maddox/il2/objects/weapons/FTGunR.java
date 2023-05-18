package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FTGunR extends FuelTankGun
{

    public FTGunR()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FTGunR.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.FTR.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
    }
}