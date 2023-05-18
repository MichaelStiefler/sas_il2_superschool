package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunSD1000 extends BombGun
{
    static 
    {
        Class class1 = BombGunSD1000.class;
        Property.set(class1, "bulletClass", (Object)BombSD1000.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
