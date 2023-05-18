// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 02/11/2015 03:24:54 p.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BombGun75Napalm.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            BombGun

public class BombGun75Napalm extends BombGun
{

    public BombGun75Napalm()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGun75Napalm.class;
        Property.set(class1, "bulletClass", com.maddox.il2.objects.weapons.Bomb75Napalm.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
