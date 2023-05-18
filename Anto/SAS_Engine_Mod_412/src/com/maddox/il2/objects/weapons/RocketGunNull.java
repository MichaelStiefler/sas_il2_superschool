// Source File Name: RocketGunNull.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-03
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunNull extends RocketGun
{

    public RocketGunNull()
    {
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.RocketGunNull.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.RocketNull.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "sound", "weapon.bombgun_phball");
    }
}
