package com.maddox.il2.objects.weapons;

public class FuelTankGunCanberra extends com.maddox.il2.objects.weapons.FuelTankGun
{

    public FuelTankGunCanberra()
    {
    }

    static
    {
        java.lang.Class class1 = com.maddox.il2.objects.weapons.FuelTankGunCanberra.class;
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "bulletClass", ((java.lang.Object) (((java.lang.Object) (com.maddox.il2.objects.weapons.FuelTankCanberra.class)))));
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "bullets", 1);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "shotFreq", 0.25F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "external", 1);
    }
}