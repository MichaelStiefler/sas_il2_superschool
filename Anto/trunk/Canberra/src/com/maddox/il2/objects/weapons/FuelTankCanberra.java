package com.maddox.il2.objects.weapons;

public class FuelTankCanberra extends com.maddox.il2.objects.weapons.FuelTank
{

    public FuelTankCanberra()
    {
    }

    static
    {
        java.lang.Class class1 = com.maddox.il2.objects.weapons.FuelTankCanberra.class;
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "mesh", "3DO/Arms/TankCanberra/mono.sim");
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "kalibr", 0.6F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "massa", 195F);
    }
}