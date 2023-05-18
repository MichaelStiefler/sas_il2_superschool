package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_generic_1sec extends Fuze {

    static {
        Class class1 = Fuze_generic_1sec.class;
        Property.set(class1, "type", 0);
        Property.set(class1, "armingTime", 1000);
    }
}
