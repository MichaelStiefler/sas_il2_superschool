
package com.maddox.il2.objects.vehicles.stationary;


public abstract class Papi
{
    public static class Papi1 extends PapiGeneric
    {

        public Papi1()
        {
        }
    }

    public static class Papi2wide extends PapiGeneric
    {

        public Papi2wide()
        {
        }
    }

    public static class Papi3two extends PapiGeneric
    {

        public Papi3two()
        {
        }
    }


    static 
    {
        new PapiGeneric.SPAWN(com.maddox.il2.objects.vehicles.stationary.Papi.Papi1.class);
        new PapiGeneric.SPAWN(com.maddox.il2.objects.vehicles.stationary.Papi.Papi2wide.class);
        new PapiGeneric.SPAWN(com.maddox.il2.objects.vehicles.stationary.Papi.Papi3two.class);
    }
}
