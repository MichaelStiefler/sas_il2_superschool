package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpFIDO extends Torpedo {

    static {
        Class class1 = BombTorpFIDO.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk24FIDO/mono.sim");
        Property.set(class1, "radius", 90.8F);
        Property.set(class1, "power", 135.8F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.569F);
        Property.set(class1, "massa", 308.1F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 15F);
        Property.set(class1, "traveltime", 333.9536F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngleMin", 14F);
        Property.set(class1, "impactAngleMax", 24F);
        Property.set(class1, "impactSpeed", 74F);
        Property.set(class1, "armingTime", 3F);
        Property.set(class1, "dropAltitude", 90F);
        Property.set(class1, "dropSpeed", 220F);
    }
}
