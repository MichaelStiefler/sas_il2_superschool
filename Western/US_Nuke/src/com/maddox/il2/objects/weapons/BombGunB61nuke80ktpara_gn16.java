
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class BombGunB61nuke80ktpara_gn16 extends BombGun
{

    public BombGunB61nuke80ktpara_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunB61nuke80ktpara_gn16.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombB61nuke80ktpara_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}