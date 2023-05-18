// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 21.08.2019 15:11:37
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ArtillerySAM.java

package com.maddox.il2.objects.vehicles.artillery;

import com.maddox.il2.ai.ground.*;

// Referenced classes of package com.maddox.il2.objects.vehicles.artillery:
//            ArtilleryGeneric, STank, AAA

public abstract class ArtillerySAM
{
    public static class Strela_1M extends ArtilleryGeneric
        implements TgtTank, TgtFlak, STank, TgtSAM
    {

        public Strela_1M()
        {
        }
    }

    public static class Strela_2M extends ArtilleryGeneric
        implements TgtFlak, AAA
    {

        public Strela_2M()
        {
        }
    }


    public ArtillerySAM()
    {
    }

    static 
    {
        new ArtilleryGeneric.SPAWN(Strela_2M.class);
        new ArtilleryGeneric.SPAWN(Strela_1M.class);
    }
}