package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FTR extends FuelTank
{

    public FTR()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FTR.class;
        Property.set(class1, "mesh", "3DO/Arms/FTR/mono.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 150.8F);
    }
}