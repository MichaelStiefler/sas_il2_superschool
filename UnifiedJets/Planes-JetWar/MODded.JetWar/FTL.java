package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FTL extends FuelTank
{

    public FTL()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FTL.class;
        Property.set(class1, "mesh", "3DO/Arms/FTL/mono.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 150.8F);
    }
}