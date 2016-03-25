// aero dynamics "Fairing" part of "Pylon_LAU10" (Launcher of 4x 5inch Zuni rocket).


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU10_LowvisCap_gn16 extends Pylon
{

    public Pylon_LAU10_LowvisCap_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU10_LowvisCap_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU10_gn16/monocap.sim");
        Property.set(class1, "massa", 8F);
        Property.set(class1, "dragCx", 0.011F);  // stock Pylons is +0.035F
        Property.set(class1, "bMinusDrag", 1);  // working inverting dragCx value into minus drag as -0.01F
    }
}