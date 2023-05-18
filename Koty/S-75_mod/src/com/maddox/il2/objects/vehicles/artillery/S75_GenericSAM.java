// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 21.08.2019 15:10:05
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   StationaryGenericSAM.java

package com.maddox.il2.objects.vehicles.artillery;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Interpolate;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.vehicles.stationary:
//            StationaryGeneric

public abstract class S75_GenericSAM extends ArtilleryGeneric
{
    class Move extends Interpolate
    {

        public boolean tick()
        {
            Target();
            return true;
        }

        Move()
        {
        }
    }


    public void Target()
    {
    }

    public S75_GenericSAM()
    {
        if(!interpEnd("move"))
            interpPut(new Move(), "move", Time.current(), null);
    }
}