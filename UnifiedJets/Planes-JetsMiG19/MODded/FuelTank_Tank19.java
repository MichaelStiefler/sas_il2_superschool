// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 12/10/2015 03:53:32 a.m.
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FuelTank_Tank19.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            FuelTank

public class FuelTank_Tank19 extends FuelTank
{

    public FuelTank_Tank19()
    {
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_Tank19.class;
        Property.set(class1, "mesh", "3DO/Arms/tank19/mono.sim");
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 184F);
    }
}
