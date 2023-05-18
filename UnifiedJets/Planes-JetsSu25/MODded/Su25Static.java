// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 09/10/2015 07:34:15 a.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Su25Static.java

package com.maddox.il2.objects.vehicles.planes;


// Referenced classes of package com.maddox.il2.objects.vehicles.planes:
//            PlaneGeneric

public abstract class Su25Static
{
    public static class Su_25 extends PlaneGeneric
    {

        public Su_25()
        {
        }
    }


    public Su25Static()
    {
    }

    static 
    {
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.Su25Static$Su_25.class);
    }
}
