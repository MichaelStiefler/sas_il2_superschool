package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_89B extends Fuze {
    static {
        Class class1 = Fuze_89B.class;
        Property.set(class1, "type", 7);
        Property.set(class1, "armingTime", 1000F);
        Property.set(class1, "fixedDelay", new float[] { 2.0F, 2.5F, 3F, 3.5F, 4F, 4.5F, 5F, 5.5F, 6F, 6.5F, 7F, 7.5F, 8F, 8.5F, 9F, 9.5F, 10F, 11F, 12F, 13F, 14F, 15F, 20F, 30F, 40F, 50F, 60F, 70F, 80F });
    }
}
