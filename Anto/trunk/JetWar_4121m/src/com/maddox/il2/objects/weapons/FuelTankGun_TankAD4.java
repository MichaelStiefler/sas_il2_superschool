package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTankGun_TankAD4 extends FuelTankGun
{

    public FuelTankGun_TankAD4()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTankGun_TankAD4.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.FuelTank_TankAD4.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
    }
}