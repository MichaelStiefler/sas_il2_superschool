package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_SAB extends Fuze_SABBarometric {
    static {
        Class localClass = Fuze_SAB.class;
        Property.set(localClass, "type", 8);
        Property.set(localClass, "airTravelToArm", 10F);
        Property.set(localClass, "fixedDelay", new float[] { 3000F, 500F, 700F, 800F, 900F, 1000F, 1100F, 1200F, 1300F, 1400F, 1500F, 1600F, 1700F, 1800F, 1900F, 2000F, 2100F, 2200F, 2300F, 2400F, 2500F, 2600F, 2700F, 2800F, 2900F, 3000F, 3200F, 3400F, 3600F, 3800F, 4000F, 4200F, 4400F, 4600F, 4800F, 5000F, 5500F, 6000F, 7000F, 8000F });
    }
}
