package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_TankLysander150gal extends FuelTank {

    static {
        Class class1 = FuelTank_TankLysander150gal.class;
        Property.set(class1, "mesh", "3DO/Arms/LysanderTank150/mono.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 405F);
    }
}
