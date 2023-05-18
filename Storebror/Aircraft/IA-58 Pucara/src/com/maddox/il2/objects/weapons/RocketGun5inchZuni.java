package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGun5inchZuni extends RocketGun
{

    public void setRocketTimeLife(float f)
    {
        this.timeLife = -1F;
    }

    static 
    {
        Class class1 = RocketGun5inchZuni.class;
        Property.set(class1, "bulletClass", (Object)Rocket5inchZuni.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}
