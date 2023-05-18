package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class PylonPB2L extends Pylon {

    static {
        Property.set(PylonPB2L.class, "mesh", "3DO/Arms/PB2-RAK_L/mono.sim");
        Property.set(PylonPB2L.class, "mass", 30F);
        Property.set(PylonPB2L.class, "drag", 0.001F);
    }
}
