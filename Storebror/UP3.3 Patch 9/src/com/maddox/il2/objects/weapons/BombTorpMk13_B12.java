package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpMk13_B12 extends Torpedo {

    static {
        Class class1 = BombTorpMk13_B12.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk13_Torpedo_B12/mono.sim");
        Property.set(class1, "radius", 90.8F);
        Property.set(class1, "power", 181.9F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.569F);
        Property.set(class1, "massa", 874.1F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 17.25F);
        Property.set(class1, "traveltime", 333.9536F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngleMin", 14F);
        Property.set(class1, "impactAngleMax", 36F);
        Property.set(class1, "impactSpeed", 74F);
        Property.set(class1, "armingTime", 3F);
        Property.set(class1, "dropAltitude", 30F);
        Property.set(class1, "dropSpeed", 205F);
    }
}
