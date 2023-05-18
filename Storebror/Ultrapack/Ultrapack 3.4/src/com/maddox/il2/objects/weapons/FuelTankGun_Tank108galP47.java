package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTankGun_Tank108galP47 extends FuelTankGun {

    public FuelTankGun_Tank108galP47() {
    }

    static {
        Class class1 = FuelTankGun_Tank108galP47.class;
        Property.set(class1, "bulletClass", (Object) FuelTank_Tank108galP47.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
    }
}
