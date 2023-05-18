// USSR B-8 V20, launcher for 20x S-8 80mm rocket, high drag - light weight for helicopters

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_B8V20_gn16 extends Pylon
{

    public Pylon_B8V20_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_B8V20_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_B8V20_gn16/mono.sim");
        Property.set(class1, "massa", 123.0F);
        Property.set(class1, "dragCx", 0.034F);  // stock Pylons is +0.035F
    }
}