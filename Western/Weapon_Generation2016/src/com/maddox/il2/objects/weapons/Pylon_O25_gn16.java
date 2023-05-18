// Last Modified by: western0221 2018-12-30

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_O25_gn16 extends Pylon
{

    public Pylon_O25_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_O25_gn16.class;
        Property.set(class1, "mesh", "3do/arms/Pylon_APU68UM2_gn16/O25.sim");
        Property.set(class1, "massa", 60.0F);
        Property.set(class1, "dragCx", 0.025F);  // stock Pylons is +0.035F
    }
}
