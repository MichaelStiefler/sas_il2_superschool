package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_Tank150galF8F extends FuelTank {

    static {
        Class var_class = FuelTank_Tank150galF8F.class;
        Property.set(var_class, "mesh", "3DO/Arms/F8F_Droptank/mono.sim");
        Property.set(var_class, "kalibr", 0.6F);
        Property.set(var_class, "massa", 445F);
    }
}
