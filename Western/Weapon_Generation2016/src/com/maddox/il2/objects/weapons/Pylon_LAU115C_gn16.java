// US LAU-115C pylon -- launcher rail for AIM-7 Sparrow missile.
// "C" has jettison kit on its top and higher height.

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU115C_gn16 extends Pylon
{

    public Pylon_LAU115C_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU115C_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU127_115_gn16/mono115C.sim");
        Property.set(class1, "massa", 54.43F);
        Property.set(class1, "dragCx", 0.0032F);  // stock Pylons is +0.035F
    }
}