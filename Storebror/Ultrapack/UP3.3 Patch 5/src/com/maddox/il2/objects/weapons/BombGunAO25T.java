package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunAO25T extends BombGun
{

    public void setBombDelay(float f)
    {
        bombDelay = 0.0F;
        if(bomb != null)
            bomb.delayExplosion = bombDelay;
    }

    static 
    {
        Class class1 = BombGunAO25T.class;
        Property.set(class1, "bulletClass", (Object)BombAO25T.class);
        Property.set(class1, "bullets", 30);
        Property.set(class1, "shotFreq", 10F);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun_AO10");
    }
}
