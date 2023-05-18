// Last Modified by: western0221 2019-01-09

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            BombGun, Bomb

public class BombGunPTAB1M extends BombGun
{

    public BombGunPTAB1M()
    {
    }

    public void setBombDelay(float f)
    {
        bombDelay = 0.0F;
        if(bomb != null)
            bomb.delayExplosion = bombDelay;
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunPTAB1M.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombPTAB1M.class);
        Property.set(class1, "bullets", 124);
        Property.set(class1, "shotFreq", 32F);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", "weapon.bombgun_AO10");
    }
}
