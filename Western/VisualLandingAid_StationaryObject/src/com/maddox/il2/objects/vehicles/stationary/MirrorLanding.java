
package com.maddox.il2.objects.vehicles.stationary;


public abstract class MirrorLanding
{
    public static class MirrorLanding1 extends MirrorLandingGeneric
    {

        public MirrorLanding1()
        {
        }
    }


    static 
    {
        new MirrorLandingGeneric.SPAWN(com.maddox.il2.objects.vehicles.stationary.MirrorLanding.MirrorLanding1.class);
    }
}
