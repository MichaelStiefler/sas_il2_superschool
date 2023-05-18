package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunMk81SnakeEye extends BombGun
{

    static 
    {
        Class class1 = BombGunMk81SnakeEye.class;
        Property.set(class1, "bulletClass", (Object)BombMk81SnakeEye.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
