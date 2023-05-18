// US QRC-160-2 (later AN/ALQ-72) ECM Pod , carried by F-105, F-4 or others

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_QRC160_2_gn16 extends Pylon
{

    public Pylon_QRC160_2_gn16()
    {
    }


    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_QRC160_2_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/QRC160_gn16/mono_2.sim");
        Property.set(class1, "massa", 98.0F);
        Property.set(class1, "dragCx", 0.015F);  // stock Pylons is +0.035F
    }
}