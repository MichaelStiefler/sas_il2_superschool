// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 9/13/2012 6:52:29 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FuelTankGun_TankF4EF.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            FuelTankGun

public class FuelTankGun_Tank18C extends FuelTankGun
{

    public FuelTankGun_Tank18C()
    {
    }

    
    

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTankGun_Tank18C.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.FuelTank_Tank18C.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
    }
}