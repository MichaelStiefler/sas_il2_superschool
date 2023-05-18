// US Army M299 , 4x AGM-114 Hellfire missile launcher

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_M299_gn16 extends Pylon
{

    public Pylon_M299_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_M299_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_M299M279_gn16/monoM299.sim");
        Property.set(class1, "massa", 65.8F);
        Property.set(class1, "dragCx", 0.044F);  // stock Pylons is +0.035F
    }
}