package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTankGun_PTB490 extends FuelTankGun
{

    public FuelTankGun_PTB490()
    {
    }

    static Class class$com$maddox$il2$objects$weapons$FuelTankGun_PTB490;

    static 
    {
        Class var_class = com.maddox.il2.objects.weapons.FuelTankGun_PTB490.class;
        Property.set(var_class, "bulletClass", (Object) com.maddox.il2.objects.weapons.FuelTank_PTB490.class);
        Property.set(var_class, "bullets", 1);
        Property.set(var_class, "shotFreq", 0.25F);
        Property.set(var_class, "external", 1);
    }
}