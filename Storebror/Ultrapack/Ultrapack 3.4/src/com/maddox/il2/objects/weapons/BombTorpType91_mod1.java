package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpType91_mod1 extends Torpedo {
    static {
        Class localClass = BombTorpType91_mod1.class;
        Property.set(localClass, "mesh", "3DO/Arms/Type91_mod1/mono.sim");
        Property.set(localClass, "radius", 20F);
        Property.set(localClass, "power", 149.5F);
        Property.set(localClass, "powerType", 0);
        Property.set(localClass, "kalibr", 0.45F);
        Property.set(localClass, "massa", 784F);
        Property.set(localClass, "sound", "weapon.torpedo");
        Property.set(localClass, "velocity", 21.7F);
        Property.set(localClass, "traveltime", 92.1659F);
        Property.set(localClass, "startingspeed", 0.0F);
        Property.set(localClass, "impactAngleMin", 20.6F);
        Property.set(localClass, "impactAngleMax", 56.8F);
        Property.set(localClass, "impactSpeed", 68.9F);
        Property.set(localClass, "armingTime", 2.0F);
        Property.set(localClass, "dropAltitude", 100F);
        Property.set(localClass, "dropSpeed", 190F);
    }
}
