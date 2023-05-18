
package com.maddox.il2.objects.vehicles.stationary;


public abstract class Vasi
{
    public static class VasiColor extends VasiGeneric
    {

        public VasiColor()
        {
        }
    }

    public static class Vasi2bar extends VasiGeneric
    {

        public Vasi2bar()
        {
        }
    }

    public static class Vasi3bar extends VasiGeneric
    {

        public Vasi3bar()
        {
        }
    }


    static 
    {
        new VasiGeneric.SPAWN(com.maddox.il2.objects.vehicles.stationary.Vasi.VasiColor.class);
        new VasiGeneric.SPAWN(com.maddox.il2.objects.vehicles.stationary.Vasi.Vasi2bar.class);
        new VasiGeneric.SPAWN(com.maddox.il2.objects.vehicles.stationary.Vasi.Vasi3bar.class);
    }
}
