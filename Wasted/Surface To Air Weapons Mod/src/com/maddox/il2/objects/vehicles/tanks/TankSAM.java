// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 05.10.2012 10:30:46
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Tank.java

package com.maddox.il2.objects.vehicles.tanks;

import com.maddox.il2.ai.ground.*;



// Referenced classes of package com.maddox.il2.objects.vehicles.tanks:
//            TankGeneric

public abstract class TankSAM
{

    public static class Strela_1M extends TankGeneric
        implements TgtTank, TgtFlak, TgtSAM
    {
    
        public Strela_1M()
        {
        }
    }
    
    public TankSAM()
    {
    }

    static 
    {
        new TankGeneric.SPAWN(com.maddox.il2.objects.vehicles.tanks.TankSAM.Strela_1M.class);
    }
}