// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 9/13/2012 8:23:48 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FuelTank_Tank18C.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            FuelTank

public class FuelTank_Tank18C extends FuelTank
{

    public FuelTank_Tank18C()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_Tank18C.class;
        Property.set(class1, "mesh", "3DO/Arms/Tank18C/mono.sim");
        Property.set(class1, "kalibr", 0.7F);
        Property.set(class1, "massa", 1025F);
    }
}