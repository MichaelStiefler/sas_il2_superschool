
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class BombGunSAB250_200_gn16 extends BombGun
{

    public BombGunSAB250_200_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunSAB250_200_gn16.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombSAB250_200_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 3F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun_AO10");
    }
}
