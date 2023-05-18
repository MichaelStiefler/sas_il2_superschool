// USSR rack attached under pylons for Sturm Anti-tank missiles' tube launchers.


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_rackSturm_gn16 extends Pylon
{

    public Pylon_rackSturm_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_rackSturm_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_rackSturm_gn16/mono.sim");
        Property.set(class1, "massa", 28.0F);
        Property.set(class1, "dragCx", 0.019F);  // stock Pylons is +0.035F
    }
}