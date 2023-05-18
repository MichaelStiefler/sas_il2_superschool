package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTankGun_150F7F extends FuelTankGun {

    public FuelTankGun_150F7F() {
    }

    static {
        Class class1 = FuelTankGun_150F7F.class;
        Property.set(class1, "bulletClass", (Object) FuelTank_150F7F.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
    }
}
