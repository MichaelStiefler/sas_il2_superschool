package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTankGun_TankFirefly44 extends FuelTankGun
{

    static 
    {
        Class class1 = FuelTankGun_TankFirefly44.class;
        Property.set(class1, "bulletClass", (Object)FuelTank_TankFirefly44.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
    }
}
