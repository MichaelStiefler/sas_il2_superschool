
package com.maddox.il2.objects.vehicles.planes;


public abstract class F_4Estatic
{
    public static class F_4B extends PlaneGeneric
    {

        public F_4B()
        {
        }
    }

    public static class F_4D extends PlaneGeneric
    {

        public F_4D()
        {
        }
    }

    public static class F_4E extends PlaneGeneric
    {

        public F_4E()
        {
        }
    }

    public static class F_4J extends PlaneGeneric
    {

        public F_4J()
        {
        }
    }


    public F_4Estatic()
    {
    }


    static 
    {
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.F_4Estatic.F_4B.class);
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.F_4Estatic.F_4D.class);
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.F_4Estatic.F_4E.class);
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.F_4Estatic.F_4J.class);
    }
}