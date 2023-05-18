package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpType91_mod3 extends Torpedo {
    static {
        Class localClass = BombTorpType91_mod3.class;
        Property.set(localClass, "mesh", "3DO/Arms/Type91_mod3-4/mono.sim");
        Property.set(localClass, "radius", 26.5F);
        Property.set(localClass, "power", 235.5F);
        Property.set(localClass, "powerType", 0);
        Property.set(localClass, "kalibr", 0.45F);
        Property.set(localClass, "massa", 848F);
        Property.set(localClass, "sound", "weapon.torpedo");
        Property.set(localClass, "velocity", 21.1F);
        Property.set(localClass, "traveltime", 71.0901F);
        Property.set(localClass, "startingspeed", 0.0F);
        Property.set(localClass, "impactAngleMin", 10.9F);
        Property.set(localClass, "impactAngleMax", 65.2F);
        Property.set(localClass, "impactSpeed", 147.6F);
        Property.set(localClass, "armingTime", 2.0F);
        Property.set(localClass, "dropAltitude", 200F);
        Property.set(localClass, "dropSpeed", 481F);
    }
}
