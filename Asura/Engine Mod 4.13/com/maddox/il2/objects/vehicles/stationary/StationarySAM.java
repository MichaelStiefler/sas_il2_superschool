// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:16:55
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   StationarySAM.java

package com.maddox.il2.objects.vehicles.stationary;

import com.maddox.il2.ai.ground.TypeSAM;

// Referenced classes of package com.maddox.il2.objects.vehicles.stationary:
//            StationaryGeneric

public abstract class StationarySAM
{
    public static class S_75_radar extends StationaryGeneric
        implements TypeSAM
    {

        public S_75_radar()
        {
        }
    }


    public StationarySAM()
    {
    }

    static 
    {
        new StationaryGeneric.SPAWN(S_75_radar.class);
    }
}