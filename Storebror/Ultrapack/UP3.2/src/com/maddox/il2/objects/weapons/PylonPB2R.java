package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class PylonPB2R extends Pylon
{

    static
    {
        Property.set(PylonPB2R.class, "mesh", "3DO/Arms/PB2-RAK_R/mono.sim");
        Property.set(PylonPB2R.class, "mass", 30F);
        Property.set(PylonPB2R.class, "drag", 0.001F);
    }
}
