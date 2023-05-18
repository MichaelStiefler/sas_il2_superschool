package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpTypeM_G10N extends Torpedo
{

    static 
    {
        Class class1 = BombTorpTypeM_G10N.class;
        Property.set(class1, "mesh", "3DO/Arms/TypeM_G10N/mono.sim");
        Property.set(class1, "radius", 63.5F);
        Property.set(class1, "power", 750F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.584F);
        Property.set(class1, "massa", 2070F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 25.8F);
        Property.set(class1, "traveltime", 135.66F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngle", 27.5F);
        Property.set(class1, "impactAngleMin", 9.2F);
        Property.set(class1, "impactAngleMax", 70.7F);
        Property.set(class1, "impactSpeed", 175F);
        Property.set(class1, "armingTime", 2.0F);
        Property.set(class1, "dropAltitude", 350F);
        Property.set(class1, "dropSpeed", 555F);
    }
}
