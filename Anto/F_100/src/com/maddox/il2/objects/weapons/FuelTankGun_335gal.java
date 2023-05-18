package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTankGun_335gal extends FuelTankGun
{

    public FuelTankGun_335gal()
    {
    }


    static 
    {
        Class var_class = com.maddox.il2.objects.weapons.FuelTankGun_335gal.class;
        Property.set(var_class, "bulletClass", (Object) com.maddox.il2.objects.weapons.FuelTank_335gal.class);
        Property.set(var_class, "bullets", 1);
        Property.set(var_class, "shotFreq", 0.25F);
        Property.set(var_class, "external", 1);
    }
}