package com.maddox.il2.objects.vehicles.planes;

public abstract class MiG21Static extends Plane
{
    public static class MIG_21FL extends PlaneGeneric
    {

        public MIG_21FL()
        {
        }
    }

    public static class MIG_21PFM extends PlaneGeneric
    {

        public MIG_21PFM()
        {
        }
    }

    public static class MIG_21PF extends PlaneGeneric
    {

        public MIG_21PF()
        {
        }
    }


    public MiG21Static()
    {
    }

    static 
    {
        new PlaneGeneric.SPAWN(MIG_21PF.class);
        new PlaneGeneric.SPAWN(MIG_21PFM.class);
        new PlaneGeneric.SPAWN(MIG_21FL.class);
    }
}