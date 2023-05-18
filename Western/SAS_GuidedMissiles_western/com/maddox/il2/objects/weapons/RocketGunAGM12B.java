
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketGunAGM12B extends RocketGun
{

    public RocketGunAGM12B()
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

    static Class class$com$maddox$il2$objects$weapons$RocketGunAGM12B;
    static Class class$com$maddox$il2$objects$weapons$RocketAGM12B;
  
    static 
    {
   	Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketGunAGM12B == null
	       ? (class$com$maddox$il2$objects$weapons$RocketGunAGM12B
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketGunAGM12B"))
	       : class$com$maddox$il2$objects$weapons$RocketGunAGM12B);
        Class classbu
	    = (class$com$maddox$il2$objects$weapons$RocketAGM12B == null
	       ? (class$com$maddox$il2$objects$weapons$RocketAGM12B
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketAGM12B"))
	       : class$com$maddox$il2$objects$weapons$RocketAGM12B);
        Property.set(class1, "bulletClass", (Object) classbu);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.10F);
        Property.set(class1, "sound", "weapon.rocketgun_132");
    }
}