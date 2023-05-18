package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            BombGun

public class BombGunSoldierAirborn extends BombGun
{

    public BombGunSoldierAirborn()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunSoldierAirborn.class;
        Property.set(class1, "bulletClass", (Object)com.maddox.il2.objects.weapons.BombParaSoldierAirborn.class);
        Property.set(class1, "bullets", 29);
        Property.set(class1, "shotFreq", 0.5F);
        Property.set(class1, "external", 0);
        Property.set(class1, "cassette", 1);
        Property.set(class1, "sound", (Object)null);
    }
}