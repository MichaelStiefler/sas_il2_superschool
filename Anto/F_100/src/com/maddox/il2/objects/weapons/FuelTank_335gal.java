package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_335gal extends FuelTank
{

    public FuelTank_335gal()
    {
    }

    static 
    {
        Class var_class = com.maddox.il2.objects.weapons.FuelTank_335gal.class;
        Property.set(var_class, "mesh", "3DO/Arms/335Gal_Droptank/mono.sim");
        Property.set(var_class, "kalibr", 0.9F);
        Property.set(var_class, "massa", 1000F);
    }
}