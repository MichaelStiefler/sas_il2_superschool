package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_Ju88 extends FuelTank {

    public FuelTank_Ju88() {
    }

    static {
        Class class1 = FuelTank_Ju88.class;
        Property.set(class1, "mesh", "3do/arms/FuelTank_Ju88/mono.sim");
        Property.set(class1, "kalibr", 0.87F);
        Property.set(class1, "massa", 700F);
    }
}
