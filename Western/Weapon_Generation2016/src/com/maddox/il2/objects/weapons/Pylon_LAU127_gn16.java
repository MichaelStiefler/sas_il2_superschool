// US LAU-127 pylon -- common launcher rail for AIM-120 AMRAAM and AIM-9 Sidewinder missiles.


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU127_gn16 extends Pylon
{

    public Pylon_LAU127_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU127_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU127_115_gn16/mono.sim");
        Property.set(class1, "massa", 43.1F);
        Property.set(class1, "dragCx", 0.003F);  // stock Pylons is +0.035F
    }
}