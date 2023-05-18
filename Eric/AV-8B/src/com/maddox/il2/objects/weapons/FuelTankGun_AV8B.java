// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 4/21/2012 9:33:23 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FuelTankGun_AV8B.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            FuelTankGun

public class FuelTankGun_AV8B extends FuelTankGun
{

    public FuelTankGun_AV8B()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTankGun_AV8B.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.FuelTank_AV8B.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
    }
}