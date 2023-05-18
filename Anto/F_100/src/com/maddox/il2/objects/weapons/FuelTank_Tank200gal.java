package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_Tank200gal extends FuelTank
{

    public FuelTank_Tank200gal()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_Tank200gal.class;
        Property.set(class1, "mesh", "3DO/Arms/Tank200gal/mono.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 568F);
    }
}