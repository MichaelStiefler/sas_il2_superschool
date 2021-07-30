package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpType94_mod2_G10N extends Torpedo {

    static {
        Class class1 = BombTorpType94_mod2_G10N.class;
        Property.set(class1, "mesh", "3DO/Arms/Type94_mod2_G10N/mono.sim");
        Property.set(class1, "radius", 20F);
        Property.set(class1, "power", 150F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.45F);
        Property.set(class1, "massa", 848F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 24.4F);
        Property.set(class1, "traveltime", 122.9508F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngle", 27.5F);
        Property.set(class1, "impactAngleMin", 16.3F);
        Property.set(class1, "impactAngleMax", 56.8F);
        Property.set(class1, "impactSpeed", 86.6F);
        Property.set(class1, "armingTime", 2.0F);
        Property.set(class1, "dropAltitude", 30F);
        Property.set(class1, "dropSpeed", 268F);
    }
}
