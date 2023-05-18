// USSR APU-60-1 pylon -- launcher rail for R-60 missiles.


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_APU60I_gn16 extends Pylon
{

    public Pylon_APU60I_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_APU60I_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_APU60I_gn16/mono.sim");
        Property.set(class1, "massa", 32.0F);
        Property.set(class1, "dragCx", 0.003F);  // stock Pylons is +0.035F
    }
}