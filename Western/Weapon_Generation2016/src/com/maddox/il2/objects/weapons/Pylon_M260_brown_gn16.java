// US Army M260 , light weight and no-farings launcher for 7x 2.75inch rocket -- Mk4 FFAR and HYDRA70 , brown color
//  -- Airforce and Navy, Marine Corps use another LAU-131 (LAU-32 , LAU-68)

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_M260_brown_gn16 extends Pylon
{

    public Pylon_M260_brown_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_M260_brown_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_M260brown_gn16/mono.sim");
        Property.set(class1, "massa", 15.9F);
        Property.set(class1, "dragCx", 0.024F);  // stock Pylons is +0.035F
    }
}