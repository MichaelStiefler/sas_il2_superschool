// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 6/4/2013 3:48:30 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BombletMK20.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb

public class BombletMK20 extends Bomb
{

    public BombletMK20()
    {
    }

    protected boolean haveSound()
    {
        return index % 16 == 0;
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
        Class class1 = com.maddox.il2.objects.weapons.BombletMK20.class;
        Property.set(class1, "mesh", "3do/arms/2KgBomblet/mono.sim");
        Property.set(class1, "radius", 4F);
        Property.set(class1, "power", 10F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.1F);
        Property.set(class1, "massa", 0.6F);
        Property.set(class1, "randomOrient", 1);
        Property.set(class1, "sound", "weapon.bomb_cassette");
    }
}