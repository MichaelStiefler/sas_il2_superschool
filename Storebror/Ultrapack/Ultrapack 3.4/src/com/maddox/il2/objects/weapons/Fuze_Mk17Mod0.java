package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Mk17Mod0 extends Fuze {
    static {
        Class localClass = Fuze_Mk17Mod0.class;
        Property.set(localClass, "type", 7);
        Property.set(localClass, "airTravelToArm", 15F);
        Property.set(localClass, "fixedDelay", new float[] { 0.0F, 10F, 15F, 25F, 30F, 50F, 60F, 75F, 100F, 150F, 200F, 300F, 400F, 500F });
    }
}
