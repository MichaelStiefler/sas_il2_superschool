package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_AV_1du extends Fuze {
    static {
        Class class1 = Fuze_AV_1du.class;
        Property.set(class1, "type", 2);
        Property.set(class1, "airTravelToArm", 155F);
        Property.set(class1, "fixedDelay", new float[] { 10F, 22F });
        Property.set(class1, "dateStart", 0x12853c5);
        Property.set(class1, "dateEnd", 0x1310655);
    }
}
