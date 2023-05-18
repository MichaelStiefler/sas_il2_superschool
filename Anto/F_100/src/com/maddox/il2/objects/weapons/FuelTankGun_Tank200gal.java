package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class FuelTankGun_Tank200gal extends FuelTankGun
{

    public FuelTankGun_Tank200gal()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTankGun_Tank200gal.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.FuelTank_Tank200gal.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
    }
}