// USSR B-8 M1, launcher for 20x S-8 80mm rocket, pointed tip for high speed jets

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_B8M1_gn16 extends Pylon
{

    public Pylon_B8M1_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_B8M1_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_B8M1_gn16/mono.sim");
        Property.set(class1, "massa", 160.0F);
        Property.set(class1, "dragCx", 0.025F);  // stock Pylons is +0.035F
    }
}