package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorpType91_mod2_QS extends BombParaTorp {
    static {
        Class localClass = BombTorpType91_mod2_QS.class;
        Property.set(localClass, "mesh", "3DO/Arms/Type91_mod2_QS/mono.sim");
        Property.set(localClass, "radius", 24F);
        Property.set(localClass, "power", 204.5F);
        Property.set(localClass, "powerType", 0);
        Property.set(localClass, "kalibr", 0.45F);
        Property.set(localClass, "massa", 838F);
        Property.set(localClass, "sound", "weapon.torpedo");
        Property.set(localClass, "velocity", 13.1F);
        Property.set(localClass, "traveltime", 282.4427F);
        Property.set(localClass, "startingspeed", 0.0F);
        Property.set(localClass, "impactAngleMin", 9.8F);
        Property.set(localClass, "impactAngleMax", 65.2F);
        Property.set(localClass, "impactSpeed", 142.3F);
        Property.set(localClass, "armingTime", 2.0F);
        Property.set(localClass, "dropAltitude", 200F);
        Property.set(localClass, "dropSpeed", 460F);
    }
}
