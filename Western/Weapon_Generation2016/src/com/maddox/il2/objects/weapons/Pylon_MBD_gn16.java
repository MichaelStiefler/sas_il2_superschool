// Russian 4x 100kg bomb rack for Su-25 or other attacker jets.
// Last Modified by: western0221 2019-01-02

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_MBD_gn16 extends Pylon
{

    public Pylon_MBD_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_MBD_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_MBD_gn16/mono.sim");
        Property.set(class1, "massa", 60.5F);
        Property.set(class1, "dragCx", 0.004F);  // stock Pylons is +0.035F
    }
}