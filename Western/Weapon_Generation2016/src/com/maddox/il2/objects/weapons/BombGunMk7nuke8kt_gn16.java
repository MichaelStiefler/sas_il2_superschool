
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class BombGunMk7nuke8kt_gn16 extends BombGun
{

    public BombGunMk7nuke8kt_gn16()
    {
    }

    public void extendTailfin(boolean bExtend)
    {
        if(bomb != null)
            ((BombMk7nuke8kt_gn16) bomb).extendTailfin(bExtend);
    }

    public boolean getTailfin()
    {
        if(bomb == null)
            return false;
        else
            return ((BombMk7nuke8kt_gn16) bomb).getTailfin();
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunMk7nuke8kt_gn16.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombMk7nuke8kt_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}