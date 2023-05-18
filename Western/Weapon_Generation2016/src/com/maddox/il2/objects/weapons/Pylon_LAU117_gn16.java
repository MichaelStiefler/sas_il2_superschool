// US LAU-117 pylon -- launcher rail for single AGM-65 Marverick missile.

/*
* Base color is low visibility dark gray for 1980s and later.
* No High visibility light gray texture is prepared.
 */


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU117_gn16 extends Pylon
{

    public Pylon_LAU117_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU117_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU117_gn16/mono.sim");
        Property.set(class1, "massa", 61.3F);
        Property.set(class1, "dragCx", 0.008F);  // stock Pylons is +0.035F
    }
}