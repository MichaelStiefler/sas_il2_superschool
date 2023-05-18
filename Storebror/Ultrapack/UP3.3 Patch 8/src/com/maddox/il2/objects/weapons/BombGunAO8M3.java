package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunAO8M3 extends BombGun
{

    static 
    {
        Class class1 = BombGunAO8M3.class;
        Property.set(class1, "bulletClass", (Object)BombAO8M3.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun_AO10");
    }
}
