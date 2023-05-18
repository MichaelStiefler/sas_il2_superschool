package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun75Napalm extends BombGun
{

    public BombGun75Napalm()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGun75Napalm.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.Bomb75Napalm.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}