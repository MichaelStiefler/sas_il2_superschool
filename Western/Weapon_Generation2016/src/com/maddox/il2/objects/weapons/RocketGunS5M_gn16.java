// Last Modified by: western0221 2018-12-30

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunS5M_gn16 extends RocketGun
{

    public RocketGunS5M_gn16()
    {
    }

    public void setConvDistance(float f, float f1)
    {
        super.setConvDistance(f, f1 + 2.81F);
    }


    static Class _mthclass$(String s)
    {
        Class class1;
        try
        {
            class1 = Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
        return class1;
    }

    static Class class$com$maddox$il2$objects$weapons$RocketGunS5M_gn16;
    static Class class$com$maddox$il2$objects$weapons$RocketS5M_gn16;

    static 
    {
        Class class1
        = (class$com$maddox$il2$objects$weapons$RocketGunS5M_gn16 == null
           ? (class$com$maddox$il2$objects$weapons$RocketGunS5M_gn16
          = _mthclass$("com.maddox.il2.objects.weapons.RocketGunS5M_gn16"))
           : class$com$maddox$il2$objects$weapons$RocketGunS5M_gn16);
        Class classbu
        = (class$com$maddox$il2$objects$weapons$RocketS5M_gn16 == null
           ? (class$com$maddox$il2$objects$weapons$RocketS5M_gn16
          = _mthclass$("com.maddox.il2.objects.weapons.RocketS5M_gn16"))
           : class$com$maddox$il2$objects$weapons$RocketS5M_gn16);
        Property.set(class1, "bulletClass", (Object) classbu);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5.33F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}
