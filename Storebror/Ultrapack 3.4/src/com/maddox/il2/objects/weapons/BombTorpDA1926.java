package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpDA1926 extends Torpedo {
    static {
        Class localClass = BombTorpDA1926.class;
        Property.set(localClass, "mesh", "3DO/Arms/DA1926/mono.sim");
        Property.set(localClass, "radius", 12F);
        Property.set(localClass, "power", 144F);
        Property.set(localClass, "powerType", 0);
        Property.set(localClass, "kalibr", 0.45F);
        Property.set(localClass, "massa", 674F);
        Property.set(localClass, "sound", "weapon.torpedo");
        Property.set(localClass, "velocity", 18.1F);
        Property.set(localClass, "traveltime", 180.555F);
        Property.set(localClass, "startingspeed", 0.0F);
        Property.set(localClass, "impactAngleMin", 5.3F);
        Property.set(localClass, "impactAngleMax", 58F);
        Property.set(localClass, "impactSpeed", 92.3F);
        Property.set(localClass, "armingTime", 2.0F);
        Property.set(localClass, "dropAltitude", 80F);
        Property.set(localClass, "dropSpeed", 300F);
    }
}
