package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunCBU24 extends BombGun
{

    public BombGunCBU24()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunCBU24.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombCBU24.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}