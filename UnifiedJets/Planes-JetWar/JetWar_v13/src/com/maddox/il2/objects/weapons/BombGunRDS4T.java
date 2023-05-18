package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunRDS4T extends BombGun
{

    public BombGunRDS4T()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunRDS4T.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombRDS4T.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.05F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
        Property.set(class1, "newEffect", 1);
    }
}