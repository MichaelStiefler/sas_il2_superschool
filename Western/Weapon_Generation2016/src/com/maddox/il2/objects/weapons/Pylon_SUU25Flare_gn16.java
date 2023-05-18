// US SUU-25 dispenser for night illumination flare bomb LUU-2 (5inch flare bomb with a parachute)
// One SUU-25 carries 8x LUU-2
// LUU-2 of backward deployed version is provided as RocketParaFlareLUU2disp_gn16.class (not Bomb class)


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_SUU25Flare_gn16 extends Pylon
{

    public Pylon_SUU25Flare_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_SUU25Flare_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_SUU25_Flare_gn16/mono.sim");
        Property.set(class1, "massa", 117.9F);
        Property.set(class1, "dragCx", 0.023F);  // stock Pylons is +0.035F
    }
}