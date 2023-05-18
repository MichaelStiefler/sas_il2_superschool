package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpType94_mod2 extends Torpedo {
    static {
        Class localClass = BombTorpType94_mod2.class;
        Property.set(localClass, "mesh", "3DO/Arms/Type94_mod2/mono.sim");
        Property.set(localClass, "radius", 20F);
        Property.set(localClass, "power", 150F);
        Property.set(localClass, "powerType", 0);
        Property.set(localClass, "kalibr", 0.45F);
        Property.set(localClass, "massa", 848F);
        Property.set(localClass, "sound", "weapon.torpedo");
        Property.set(localClass, "velocity", 24.4F);
        Property.set(localClass, "traveltime", 122.9508F);
        Property.set(localClass, "startingspeed", 0.0F);
        Property.set(localClass, "impactAngleMin", 16.3F);
        Property.set(localClass, "impactAngleMax", 56.8F);
        Property.set(localClass, "impactSpeed", 86.6F);
        Property.set(localClass, "armingTime", 2.0F);
        Property.set(localClass, "dropAltitude", 30F);
        Property.set(localClass, "dropSpeed", 268F);
    }
}
