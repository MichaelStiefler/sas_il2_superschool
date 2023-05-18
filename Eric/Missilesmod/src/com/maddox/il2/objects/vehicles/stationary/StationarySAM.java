package com.maddox.il2.objects.vehicles.stationary;

import com.maddox.il2.ai.ground.*;
import com.maddox.il2.objects.vehicles.artillery.*;

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
        new StationaryGeneric.SPAWN(com.maddox.il2.objects.vehicles.stationary.StationarySAM.S_75_radar.class);
    }
}