// US BRU-33A/A Twin Bomb Ejecter Rack , CVER (canted vertical ejector rack) for F/A-18
// Carry upto 1000lbs / 14inch suspention lug bombs , Mk82 / 83 , PavewayII / III .
// Later Smart racks BRU-55 for F/A-18 or BRU-56 for F-16 are almost same shape and spec.
// BRU-55 / 56 can operate additional JDAM , JSOW

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_BRU33A_CVER_gn16 extends Pylon
{

    public Pylon_BRU33A_CVER_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_BRU33A_CVER_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_VER_BRU33A_gn16/mono.sim");
        Property.set(class1, "massa", 104.3F);
        Property.set(class1, "dragCx", 0.004F);  // stock Pylons is +0.035F
    }
}