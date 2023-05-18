// US FLIR and Terrain radar Navigftion pod AN/AAQ-13 , a part of "LANTIRN".
// It needs special a tilted pylon or spacer kit for hooking.

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_AN_AAQ13_gn16 extends Pylon
{

    public Pylon_AN_AAQ13_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_AN_AAQ13_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/AN_AAQ13_gn16/mono.sim");
        Property.set(class1, "massa", 205.5F);
        Property.set(class1, "dragCx", 0.023F);
    }
}