package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Fuze_USnukeDelay extends Fuze {

    static {
        Class class1 = Fuze_USnukeDelay.class;
        Property.set(class1, "type", 1);
        Property.set(class1, "airTravelToArm", 152F);
        Property.set(class1, "minDelay", 15F);
        Property.set(class1, "maxDelay", 60F);
    }
}
