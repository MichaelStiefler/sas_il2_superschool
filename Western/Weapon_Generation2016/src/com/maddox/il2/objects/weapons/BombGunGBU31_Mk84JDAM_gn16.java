
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class BombGunGBU31_Mk84JDAM_gn16 extends BombGun
{

    public BombGunGBU31_Mk84JDAM_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunGBU31_Mk84JDAM_gn16.class;
        Property.set(class1, "bulletClass", (Object) BombGBU31_Mk84JDAM_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
