// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 02/11/2015 03:26:06 p.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BombMk83.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb

public class BombMk83 extends Bomb
{

    public BombMk83()
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
        Class class1 = com.maddox.il2.objects.weapons.BombMk83.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk83/mono.sim");
        Property.set(class1, "radius", 100F);
        Property.set(class1, "power", 250F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.32F);
        Property.set(class1, "massa", 454F);
        Property.set(class1, "sound", "weapon.bomb_big");
    }
}
