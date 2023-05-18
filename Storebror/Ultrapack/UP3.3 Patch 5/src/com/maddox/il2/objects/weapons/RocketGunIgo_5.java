package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunIgo_5 extends RocketBombGun
{

    public void setRocketTimeLife(float f)
    {
        timeLife = 30F;
    }

    static 
    {
        Class class1 = RocketGunIgo_5.class;
        Property.set(class1, "bulletClass", (Object)RocketIgo_5.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
