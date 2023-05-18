// Last Modified by: western0221 2018-12-30

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunKh25MR_gn16 extends RocketGun
{

    public RocketGunKh25MR_gn16()
    {
    }

    public void setRocketTimeLife(float f)
    {
        super.timeLife = 30F;
    }

    static Class _mthclass$(String s)
    {
        Class c;
        try{
            c = Class.forName(s);
        } catch ( ClassNotFoundException e ){
            throw new NoClassDefFoundError(e.getMessage());
        }
        return c;
    }

    static Class class$com$maddox$il2$objects$weapons$RocketGunKh25MR_gn16;
    static Class class$com$maddox$il2$objects$weapons$RocketKh25MR_gn16;
  
    static 
    {
   	Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketGunKh25MR_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketGunKh25MR_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketGunKh25MR_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketGunKh25MR_gn16);
        Class classbu
	    = (class$com$maddox$il2$objects$weapons$RocketKh25MR_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketKh25MR_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketKh25MR_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketKh25MR_gn16);
        Property.set(class1, "bulletClass", (Object) classbu);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.25F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}