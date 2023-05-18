package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class BombGun3000lbsM118 extends BombGun
{

    public BombGun3000lbsM118()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGun3000lbsM118.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.Bomb3000lbsM118.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
