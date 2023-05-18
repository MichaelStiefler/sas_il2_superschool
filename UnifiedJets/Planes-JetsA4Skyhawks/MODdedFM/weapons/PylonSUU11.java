// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 02/11/2015 03:26:35 p.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PylonSUU11.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Pylon

public class PylonSUU11 extends Pylon
{

    public PylonSUU11()
    {
    }

    static Class _mthclass$(String s)
    {
        Class class1;
        try
        {
            class1 = Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
        return class1;
    }

//    static Class _mthclass$(String s)
//    {
//        try
//        {
//            return Class.forName(s);
//        }
//        catch(ClassNotFoundException classnotfoundexception)
//        {
//            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
//        }
//    }

    static 
    {
        Property.set(com.maddox.il2.objects.weapons.PylonSUU11.class, "mesh", "3DO/Arms/SUU11/mono.sim");
    }
}
