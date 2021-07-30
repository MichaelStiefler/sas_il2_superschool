package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunTorpMk13aTBD extends TorpedoGun
{

    public void setBombDelay(float f)
    {
        bombDelay = 0.0F;
        if(bomb != null)
            bomb.delayExplosion = bombDelay;
    }

    static 
    {
        Class class1 = BombGunTorpMk13aTBD.class;
        Property.set(class1, "bulletClass", (Object)BombTorpMk13aTBD.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.1F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun_torpedo");
    }
}
