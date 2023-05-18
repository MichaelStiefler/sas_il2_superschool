
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_B13_gn16 extends Pylon
{

    public Pylon_B13_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_B13_gn16.class;
        Property.set(class1, "mesh", "3do/arms/Pylon_B13_gn16/mono.sim");
        Property.set(class1, "massa", 160.0F);
        Property.set(class1, "dragCx", 0.020F);  // stock Pylons is +0.035F
    }
}
