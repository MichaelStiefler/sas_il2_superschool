package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_Pistol_No27 extends Fuze
{

    static 
    {
        Class localClass = Fuze_Pistol_No27.class;
        Property.set(localClass, "type", 1);
        Property.set(localClass, "airTravelToArm", 1.0F);
        Property.set(localClass, "fixedDelay", new float[] {
            0.0F, 0.025F, 0.04F, 0.12F, 0.5F, 1.0F, 2.0F, 11F
        });
    }
}
