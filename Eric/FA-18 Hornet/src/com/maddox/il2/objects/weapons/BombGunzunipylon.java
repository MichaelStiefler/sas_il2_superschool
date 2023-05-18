// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/24/2011 8:15:10 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BombGunMk82Snakeye.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            BombGunSC50

public class BombGunzunipylon extends BombGun
{

    public BombGunzunipylon()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunzunipylon.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.ZuniF18.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 6F);
        Property.set(class1, "external", 0);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}