// US LAU-115A pylon -- launcher rail for AIM-7 Sparrow missile.
// "A" is no jettison kit and lower height.

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU115A_gn16 extends Pylon
{

    public Pylon_LAU115A_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU115A_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU127_115_gn16/mono115A.sim");
        Property.set(class1, "massa", 26.76F);
        Property.set(class1, "dragCx", 0.003F);  // stock Pylons is +0.035F
    }
}