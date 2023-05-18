
package com.maddox.il2.objects.vehicles.stationary;


public abstract class Windsock
{
    public static class Windsock1 extends WindsockGeneric
    {

        public Windsock1()
        {
        }
    }


    public static class Windsock2 extends WindsockGeneric
    {

        public Windsock2()
        {
        }
    }


    public static class Windsock3 extends WindsockGeneric
    {

        public Windsock3()
        {
        }
    }


    static 
    {
        new WindsockGeneric.SPAWN(com.maddox.il2.objects.vehicles.stationary.Windsock.Windsock1.class);
        new WindsockGeneric.SPAWN(com.maddox.il2.objects.vehicles.stationary.Windsock.Windsock2.class);
        new WindsockGeneric.SPAWN(com.maddox.il2.objects.vehicles.stationary.Windsock.Windsock3.class);
    }
}
