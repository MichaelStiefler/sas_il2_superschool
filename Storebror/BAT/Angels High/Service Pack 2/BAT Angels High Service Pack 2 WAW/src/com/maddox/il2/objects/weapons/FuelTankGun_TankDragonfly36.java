package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTankGun_TankDragonfly36 extends FuelTankGun {
    static {
        Class class1 = FuelTankGun_TankDragonfly36.class;
        Property.set(class1, "bulletClass", (Object) FuelTank_TankDragonfly36.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
    }
}
