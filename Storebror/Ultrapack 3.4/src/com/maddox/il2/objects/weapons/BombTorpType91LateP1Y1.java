package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpType91LateP1Y1 extends Torpedo {

    static {
        Class class1 = BombTorpType91LateP1Y1.class;
        Property.set(class1, "mesh", "3DO/Arms/Type_91_Late_p1y1/mono.sim");
        Property.set(class1, "radius", 90.8F);
        Property.set(class1, "power", 363.8F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.569F);
        Property.set(class1, "massa", 874.1F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 17.25F);
        Property.set(class1, "traveltime", 333.9536F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngle", 20F);
        Property.set(class1, "impactAngleMin", 13.5F);
        Property.set(class1, "impactAngleMax", 26.5F);
        Property.set(class1, "impactSpeed", 117F);
        Property.set(class1, "armingTime", 4F);
        Property.set(class1, "dropAltitude", 60F);
        Property.set(class1, "dropSpeed", 330F);
    }
}
