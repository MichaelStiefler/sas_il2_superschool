package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpType95_G10N extends Torpedo {

    static {
        Class class1 = BombTorpType95_G10N.class;
        Property.set(class1, "mesh", "3DO/Arms/Type95_G10N/mono.sim");
        Property.set(class1, "radius", 40.0F);
        Property.set(class1, "power", 405F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.533F);
        Property.set(class1, "massa", 1665F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 24.18F);
        Property.set(class1, "traveltime", 310.17F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngle", 27.5F);
        Property.set(class1, "impactAngleMin", 9.2F);
        Property.set(class1, "impactAngleMax", 70.7F);
        Property.set(class1, "impactSpeed", 600F);
        Property.set(class1, "armingTime", 1F);
        Property.set(class1, "dropAltitude", 160F);
        Property.set(class1, "dropSpeed", 400F);
    }
}
