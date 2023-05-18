// US Army M279 , 2x AGM-114 Hellfire missile launcher

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_M279_gn16 extends Pylon
{

    public Pylon_M279_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_M279_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_M299M279_gn16/monoM279.sim");
        Property.set(class1, "massa", 43.0F);
        Property.set(class1, "dragCx", 0.032F);  // stock Pylons is +0.035F
    }
}