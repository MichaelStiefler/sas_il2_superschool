package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunBLU_27Napalm extends BombGun
{

    public BombGunBLU_27Napalm()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunBLU_27Napalm.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombBLU_27Napalm.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}