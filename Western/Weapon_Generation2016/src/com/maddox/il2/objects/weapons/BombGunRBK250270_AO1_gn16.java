// Last Modified by: western0221 2019-01-02

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            BombGun

public class BombGunRBK250270_AO1_gn16 extends BombGun
{

    public BombGunRBK250270_AO1_gn16()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunRBK250270_AO1_gn16.class;
        Property.set(class1, "bulletClass", (Object) BombRBK250270_AO1_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
