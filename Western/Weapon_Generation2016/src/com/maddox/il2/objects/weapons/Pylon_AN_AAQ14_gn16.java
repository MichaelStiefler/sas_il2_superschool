// US Laser targeting pod AN/AAQ-14 or AN/AAQ-25 (same 3D shape), a part of "LANTIRN".

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_AN_AAQ14_gn16 extends Pylon
{

    public Pylon_AN_AAQ14_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_AN_AAQ14_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/AN_AAQ14_gn16/mono.sim");
        Property.set(class1, "massa", 240.7F);
        Property.set(class1, "dragCx", 0.023F);
    }
}