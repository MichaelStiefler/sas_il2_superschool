package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpType91New extends Torpedo {
    static {
        Class class1 = BombTorpType91New.class;
        Property.set(class1, "mesh", "3DO/Arms/Type91_new/mono.sim");
        Property.set(class1, "radius", 90.8F);
        Property.set(class1, "power", 181.9F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.569F);
        Property.set(class1, "massa", 874.1F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 17.25F);
        Property.set(class1, "traveltime", 333.9536F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngleMin", 15F);
        Property.set(class1, "impactAngleMax", 25F);
        Property.set(class1, "impactSpeed", 86F);
        Property.set(class1, "armingTime", 3F);
        Property.set(class1, "dropAltitude", 30F);
        Property.set(class1, "dropSpeed", 240F);
    }
}
