package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpLTF5A extends Torpedo {

    static {
        Class class1 = BombTorpLTF5A.class;
        Property.set(class1, "mesh", "3DO/Arms/F5-F5A/mono.sim");
        Property.set(class1, "radius", 12F);
        Property.set(class1, "power", 200F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.45F);
        Property.set(class1, "massa", 685F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 16.9F);
        Property.set(class1, "traveltime", 118.3432F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngleMin", 18.6F);
        Property.set(class1, "impactAngleMax", 44F);
        Property.set(class1, "impactSpeed", 77.5F);
        Property.set(class1, "armingTime", 2.0F);
        Property.set(class1, "dropAltitude", 40F);
        Property.set(class1, "dropSpeed", 260F);
    }
}
