
package com.maddox.il2.objects.vehicles.planes;


public abstract class KC_10Astatic
{
    public static class KC_10A_boom extends PlaneGeneric
    {

        public KC_10A_boom()
        {
        }
    }

    public static class KC_10A_drogue extends PlaneGeneric
    {

        public KC_10A_drogue()
        {
        }
    }


    public static class KC_10A_transport extends PlaneGeneric
    {

        public KC_10A_transport()
        {
        }
    }

    public static class DC_10_10F extends PlaneGeneric
    {

        public DC_10_10F()
        {
        }
    }

    public static class DC_10_30F extends PlaneGeneric
    {

        public DC_10_30F()
        {
        }
    }

    public static class DC_10_40F extends PlaneGeneric
    {

        public DC_10_40F()
        {
        }
    }


    public KC_10Astatic()
    {
    }


    static 
    {
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.KC_10Astatic.KC_10A_boom.class);
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.KC_10Astatic.KC_10A_drogue.class);
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.KC_10Astatic.KC_10A_transport.class);
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.KC_10Astatic.DC_10_10F.class);
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.KC_10Astatic.DC_10_30F.class);
        new PlaneGeneric.SPAWN(com.maddox.il2.objects.vehicles.planes.KC_10Astatic.DC_10_40F.class);
    }
}