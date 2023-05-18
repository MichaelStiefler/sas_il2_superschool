// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:19:52
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketGunNull.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunNull extends RocketGun
{

    public RocketGunNull()
    {
    }

    public void shots(int i)
    {
        bullets(0);
    }
    
    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunNull.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.RocketNull.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "sound", "weapon.bombgun_phball");
    }
}