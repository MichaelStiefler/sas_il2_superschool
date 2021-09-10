package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_TankKi44Underwing extends FuelTank {
    static {
        Class class1 = FuelTank_TankKi44Underwing.class;
        Property.set(class1, "mesh", "3DO/Arms/Ki-44_UnderwingTank/mono.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 115.3F);
    }
}
