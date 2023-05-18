package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombTorp450mm extends Torpedo {
    static {
        Class class1 = BombTorp450mm.class;
        Property.set(class1, "mesh", "3DO/Arms/Torpedo_450mm/mono.sim");
        Property.set(class1, "radius", 90F);
        Property.set(class1, "power", 176F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.45F);
        Property.set(class1, "massa", 702F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 17.25F);
        Property.set(class1, "traveltime", 333.9536F);
        Property.set(class1, "startingspeed", 0.0F);
    }
}
