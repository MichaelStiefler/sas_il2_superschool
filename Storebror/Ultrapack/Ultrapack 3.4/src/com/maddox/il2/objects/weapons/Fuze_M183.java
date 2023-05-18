package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.TrueRandom;

public class Fuze_M183 extends Fuze {
    static {
        Class class1 = Fuze_M183.class;
        Property.set(class1, "type", 2);
        Property.set(class1, "airTravelToArm", 135F);
        Property.set(class1, "fixedDelay", new float[] { TrueRandom.nextInt(4, 5), TrueRandom.nextInt(8, 15) });
    }
}
