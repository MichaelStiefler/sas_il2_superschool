package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_Tank110galP47 extends FuelTank {

    public FuelTank_Tank110galP47() {
    }

    static {
        Class class1 = FuelTank_Tank110galP47.class;
        Property.set(class1, "mesh", "3DO/Arms/Tank110galP47/mono.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 300.36F);
    }
}
