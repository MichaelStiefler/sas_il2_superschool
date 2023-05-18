package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_TankAD4 extends FuelTank
{

    public FuelTank_TankAD4()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_TankAD4.class;
        Property.set(class1, "mesh", "3DO/Arms/AD4Droptank/mono.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 174.5F);
    }
}