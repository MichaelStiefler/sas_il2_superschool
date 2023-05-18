package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGun5inchZuni extends RocketGun
{

    public RocketGun5inchZuni()
    {
    }

    public void setRocketTimeLife(float f)
    {
        timeLife = -1F;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGun5inchZuni.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.Rocket5inchZuni.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}