// US SUU-63 pylon with BRU-32 single bomb rack, as F-18C/D wing pylon


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_SUU63_F18W_gn16 extends Pylon
{

    public Pylon_SUU63_F18W_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_SUU63_F18W_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_SUU63_F18W_gn16/mono.sim");
        Property.set(class1, "massa", 125.6F);
        Property.set(class1, "dragCx", 0.007F);  // stock Pylons is +0.035F
    }
}