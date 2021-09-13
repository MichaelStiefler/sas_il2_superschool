package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_Tank240 extends FuelTank {
    static {
        Class class1 = FuelTank_Tank240.class;
        Property.set(class1, "mesh", "3DO/Arms/Tank240/mono.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 184F);
    }
}
