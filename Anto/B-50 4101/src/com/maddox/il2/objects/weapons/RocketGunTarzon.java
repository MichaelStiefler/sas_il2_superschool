package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunTarzon extends RocketBombGun
{

    public RocketGunTarzon()
    {
    }

    public void setRocketTimeLife(float f)
    {
        timeLife = 30F;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunTarzon.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketTarzon.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.0F);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}