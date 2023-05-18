package com.maddox.il2.objects.weapons;

import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class MiG21WingPylon extends PylonSpitL
{

    public MiG21WingPylon()
    {
    }

    static 
    {
        Property.set(CLASS.THIS(), "mesh", "3DO/Arms/MiG21WingPylon/mono.sim");
    }
}