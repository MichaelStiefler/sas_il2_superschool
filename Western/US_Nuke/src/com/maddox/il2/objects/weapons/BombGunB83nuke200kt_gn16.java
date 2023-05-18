
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class BombGunB83nuke200kt_gn16 extends BombGun
{

    public BombGunB83nuke200kt_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunB83nuke200kt_gn16.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombB83nuke200kt_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}