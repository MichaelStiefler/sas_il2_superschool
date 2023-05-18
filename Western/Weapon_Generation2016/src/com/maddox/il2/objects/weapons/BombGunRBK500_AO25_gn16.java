// Last Modified by: western0221 2019-01-09

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            BombGun

public class BombGunRBK500_AO25_gn16 extends BombGun
{

    public BombGunRBK500_AO25_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunRBK500_AO25_gn16.class;
        Property.set(class1, "bulletClass", (Object) BombRBK500_AO25_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
