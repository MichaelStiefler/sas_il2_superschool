package com.maddox.il2.objects.weapons;

import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class MiG21Pylon extends PylonSpitL
{

    public MiG21Pylon()
    {
    }

    static 
    {
        Property.set(CLASS.THIS(), "mesh", "3DO/Arms/MiG21Pylon/mono.sim");
    }
}