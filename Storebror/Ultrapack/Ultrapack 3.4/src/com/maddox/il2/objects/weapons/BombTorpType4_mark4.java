package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpType4_mark4 extends Torpedo {
    static {
        Class localClass = BombTorpType4_mark4.class;
        Property.set(localClass, "mesh", "3DO/Arms/Type4_mark4/mono.sim");
        Property.set(localClass, "radius", 39.5F);
        Property.set(localClass, "power", 417F);
        Property.set(localClass, "powerType", 0);
        Property.set(localClass, "kalibr", 0.45F);
        Property.set(localClass, "massa", 1105F);
        Property.set(localClass, "sound", "weapon.torpedo");
        Property.set(localClass, "velocity", 21.7F);
        Property.set(localClass, "traveltime", 69.1244F);
        Property.set(localClass, "startingspeed", 0.0F);
        Property.set(localClass, "impactAngleMin", 7.3F);
        Property.set(localClass, "impactAngleMax", 69.4F);
        Property.set(localClass, "impactSpeed", 219.9F);
        Property.set(localClass, "armingTime", 2.0F);
        Property.set(localClass, "dropAltitude", 305F);
        Property.set(localClass, "dropSpeed", 741F);
    }
}
