// USSR Sturm-V Anti-Tank missile 2x launcher for Mi-24V

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_Sturm_gn16 extends Pylon
{

    public Pylon_Sturm_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_Sturm_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_Sturm_gn16/mono.sim");
        Property.set(class1, "massa", 35.0F);
        Property.set(class1, "dragCx", 0.012F);  // stock Pylons is +0.035F
    }
}