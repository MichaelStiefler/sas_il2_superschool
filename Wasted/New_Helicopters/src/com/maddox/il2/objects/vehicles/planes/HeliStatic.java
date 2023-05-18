// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 26.12.2012 18:39:53
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   JetEraStatic.java

package com.maddox.il2.objects.vehicles.planes;


// Referenced classes of package com.maddox.il2.objects.vehicles.planes:
//            PlaneGeneric

public abstract class HeliStatic
{
    public static class MI8MT extends PlaneGeneric
    {

        public MI8MT()
        {
        }
    }
    
    public static class Mi24V extends PlaneGeneric
    {

        public Mi24V()
        {
        }
    }

    public HeliStatic()
    {
    }

    static 
    {
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.HeliStatic.MI8MT.class);
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.HeliStatic.Mi24V.class);
    }
}