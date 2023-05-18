
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunAGM12C extends RocketGun
{

    public RocketGunAGM12C()
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

    static Class class$com$maddox$il2$objects$weapons$RocketGunAGM12C;
    static Class class$com$maddox$il2$objects$weapons$RocketAGM12C;
  
    static 
    {
   	Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketGunAGM12C == null
	       ? (class$com$maddox$il2$objects$weapons$RocketGunAGM12C
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketGunAGM12C"))
	       : class$com$maddox$il2$objects$weapons$RocketGunAGM12C);
        Class classbu
	    = (class$com$maddox$il2$objects$weapons$RocketAGM12C == null
	       ? (class$com$maddox$il2$objects$weapons$RocketAGM12C
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketAGM12C"))
	       : class$com$maddox$il2$objects$weapons$RocketAGM12C);
        Property.set(class1, "bulletClass", (Object) classbu);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.10F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}