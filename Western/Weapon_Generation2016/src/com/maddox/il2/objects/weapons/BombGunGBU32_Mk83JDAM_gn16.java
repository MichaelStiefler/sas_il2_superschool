
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class BombGunGBU32_Mk83JDAM_gn16 extends BombGun
{

    public BombGunGBU32_Mk83JDAM_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunGBU32_Mk83JDAM_gn16.class;
        Property.set(class1, "bulletClass", (Object) BombGBU32_Mk83JDAM_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 2.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
