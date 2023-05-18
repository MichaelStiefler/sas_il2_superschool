// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 15/07/2011 10:39:12 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BombGunMk82.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            BombGun

public class BombGunMk82SnakeEye extends BombGun
{

    public BombGunMk82SnakeEye()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunMk82SnakeEye.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombMk82SnakeEye.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}