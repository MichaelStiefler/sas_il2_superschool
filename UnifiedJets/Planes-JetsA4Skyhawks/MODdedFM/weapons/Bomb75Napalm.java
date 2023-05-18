// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 02/11/2015 03:24:42 p.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Bomb75Napalm.java

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb

public class Bomb75Napalm extends Bomb
{

    public Bomb75Napalm()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Bomb75Napalm.class;
        Property.set(class1, "mesh", "3DO/Arms/Tank75gal_Napalm/mono.sim");
        Property.set(class1, "radius", 77F);
        Property.set(class1, "power", 75F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.6F);
        Property.set(class1, "massa", 340F);
        Property.set(class1, "sound", "weapon.bomb_std");
    }
}
