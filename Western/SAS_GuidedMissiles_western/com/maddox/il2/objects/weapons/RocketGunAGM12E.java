
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunAGM12E extends RocketGun
{

    public RocketGunAGM12E()
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

    static Class class$com$maddox$il2$objects$weapons$RocketGunAGM12E;
    static Class class$com$maddox$il2$objects$weapons$RocketAGM12E;
  
    static 
    {
   	Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketGunAGM12E == null
	       ? (class$com$maddox$il2$objects$weapons$RocketGunAGM12E
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketGunAGM12E"))
	       : class$com$maddox$il2$objects$weapons$RocketGunAGM12E);
        Class classbu
	    = (class$com$maddox$il2$objects$weapons$RocketAGM12E == null
	       ? (class$com$maddox$il2$objects$weapons$RocketAGM12E
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketAGM12E"))
	       : class$com$maddox$il2$objects$weapons$RocketAGM12E);
        Property.set(class1, "bulletClass", (Object) classbu);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.10F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}