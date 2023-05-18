// Russian rocket launcher B-13L1 for 5x S-13, No faring - High drag - Light weight version for Mi-24/28, Ka-50/52

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_B13L1_gn16 extends Pylon
{

    public Pylon_B13L1_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_B13L1_gn16.class;
        Property.set(class1, "mesh", "3do/arms/Pylon_B13L1_gn16/monoL1.sim");
        Property.set(class1, "massa", 140.0F);
        Property.set(class1, "dragCx", 0.029F);  // stock Pylons is +0.035F
    }
}
