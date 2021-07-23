package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.TrueRandom;

public class Fuze_7_12 extends Fuze {
    static {
        Class class1 = Fuze_7_12.class;
        Property.set(class1, "type", 2);
        Property.set(class1, "airTravelToArm", 5F);
        Property.set(class1, "fixedDelay", new float[] { TrueRandom.nextInt(7, 12) });
    }
}
