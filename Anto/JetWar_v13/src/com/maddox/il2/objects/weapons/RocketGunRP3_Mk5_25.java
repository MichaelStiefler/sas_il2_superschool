package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunRP3_Mk5_25 extends RocketGun
{

    public RocketGunRP3_Mk5_25()
    {
    }

    public void setRocketTimeLife(float f)
    {
        timeLife = -1F;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunRP3_Mk5_25.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketRP3_Mk5_25.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}