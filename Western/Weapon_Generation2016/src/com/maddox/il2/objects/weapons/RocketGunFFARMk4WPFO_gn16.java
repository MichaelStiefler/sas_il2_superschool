
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class RocketGunFFARMk4WPFO_gn16 extends RocketGun
{

    public RocketGunFFARMk4WPFO_gn16()
    {
    }

    public void setRocketTimeLife(float f)
    {
        timeLife = -1F;
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    static Class class$com$maddox$il2$objects$weapons$RocketGunFFARMk4WPFO_gn16;
    static Class class$com$maddox$il2$objects$weapons$RocketFFARMk4WPFO_gn16;

    static 
    {
        Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketGunFFARMk4WPFO_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketGunFFARMk4WPFO_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketGunFFARMk4WPFO_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketGunFFARMk4WPFO_gn16);
        Class classbu
	    = (class$com$maddox$il2$objects$weapons$RocketFFARMk4WPFO_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketFFARMk4WPFO_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketFFARMk4WPFO_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketFFARMk4WPFO_gn16);
        Property.set(class1, "bulletClass", (Object) classbu);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 5F);
        Property.set(class1, "sound", "weapon.rocketgun_82");
    }
}
