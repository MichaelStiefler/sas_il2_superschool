package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Nosegun extends Pylon {

    static {
        Property.set(Nosegun.class, "mesh", "3DO/Arms/Nosegun/mono.sim");
    }
}
