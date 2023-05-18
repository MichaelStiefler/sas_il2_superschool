// Twin US LAU-127 launcher rail for AIM-9 / AIM-120 on both sides of LAU-115C pylon.
// "C" has jettison kit on its top and higher height.

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_TwinLAU127_115C_gn16 extends Pylon
{

    public Pylon_TwinLAU127_115C_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_TwinLAU127_115C_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU127_115_gn16/monoTw127_115C.sim");
        Property.set(class1, "massa", 140.6F);
        Property.set(class1, "dragCx", 0.0060F);  // stock Pylons is +0.035F
    }
}