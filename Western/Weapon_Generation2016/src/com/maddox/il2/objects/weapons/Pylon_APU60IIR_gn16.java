// USSR APU-60-2 pylon (for Right wings) -- launcher rail for 2x R-60 missiles.


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_APU60IIR_gn16 extends Pylon
{

    public Pylon_APU60IIR_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_APU60IIR_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_APU60II_gn16/monoR.sim");
        Property.set(class1, "massa", 34.0F);
        Property.set(class1, "dragCx", 0.007F);  // stock Pylons is +0.035F
    }
}