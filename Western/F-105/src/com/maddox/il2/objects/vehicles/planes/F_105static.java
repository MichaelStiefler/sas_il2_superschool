
package com.maddox.il2.objects.vehicles.planes;


public abstract class F_105static
{
    public static class F_105D extends PlaneGeneric
    {

        public F_105D()
        {
        }
    }

    public static class F_105F extends PlaneGeneric
    {

        public F_105F()
        {
        }
    }


    public F_105static()
    {
    }


    static 
    {
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.F_105static.F_105D.class);
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.F_105static.F_105F.class);
    }
}