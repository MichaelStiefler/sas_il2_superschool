
package com.maddox.il2.objects.vehicles.planes;


public abstract class F_14static
{
    public static class F_14A extends PlaneGeneric
    {

        public F_14A()
        {
        }
    }

    public static class F_14B extends PlaneGeneric
    {

        public F_14B()
        {
        }
    }

    public static class F_14D extends PlaneGeneric
    {

        public F_14D()
        {
        }
    }


    public F_14static()
    {
    }


    static 
    {
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.F_14static.F_14A.class);
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.F_14static.F_14B.class);
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.F_14static.F_14D.class);
    }
}