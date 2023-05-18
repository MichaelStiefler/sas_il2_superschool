
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunAGM12B_gn16 extends RocketGun
{

    public RocketGunAGM12B_gn16()
    {
    }

    public void setRocketTimeLife(float f)
    {
        super.timeLife = 330F;
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

    static Class class$com$maddox$il2$objects$weapons$RocketGunAGM12B_gn16;
    static Class class$com$maddox$il2$objects$weapons$RocketAGM12B_gn16;
  
    static 
    {
   	Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketGunAGM12B_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketGunAGM12B_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketGunAGM12B_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketGunAGM12B_gn16);
        Class classbu
	    = (class$com$maddox$il2$objects$weapons$RocketAGM12B_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketAGM12B_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketAGM12B_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketAGM12B_gn16);
        Property.set(class1, "bulletClass", (Object) classbu);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.10F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}