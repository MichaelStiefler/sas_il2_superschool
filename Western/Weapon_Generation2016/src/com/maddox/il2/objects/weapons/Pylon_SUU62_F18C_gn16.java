// US SUU-62 pylon with BRU-32 single bomb rack, as F-18C/D center pylon


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_SUU62_F18C_gn16 extends Pylon
{

    public Pylon_SUU62_F18C_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_SUU62_F18C_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_SUU62_F18C_gn16/mono.sim");
        Property.set(class1, "massa", 59.0F);
        Property.set(class1, "dragCx", 0.004F);  // stock Pylons is +0.035F
    }
}