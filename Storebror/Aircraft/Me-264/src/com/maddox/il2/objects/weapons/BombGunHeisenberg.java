package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunHeisenberg extends BombGun
{
    static 
    {
        Class class1 = BombGunHeisenberg.class;
        Property.set(class1, "bulletClass", (Object)BombHeisenberg.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.05F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
