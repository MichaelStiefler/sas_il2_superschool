
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.rts.Property;


public class RocketFFARMk4_gn16 extends Rocket
{

    public RocketFFARMk4_gn16()
    {
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

    static Class class$com$maddox$il2$objects$weapons$RocketFFARMk4_gn16;

    static 
    {
    	Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketFFARMk4_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketFFARMk4_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketFFARMk4_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketFFARMk4_gn16);
        Property.set(class1, "mesh", "3DO/Arms/Mk4FFAR_275inch_gn16/Mk4FFAR.sim");
        Property.set(class1, "meshFly", "3DO/Arms/Mk4FFAR_275inch_gn16/Mk4FFARfly.sim");
        Property.set(class1, "sprite", "3DO/Effects/Rocket/firesprite.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Rocket/rocketsmokewhite.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_82");
        Property.set(class1, "radius", 12F);
        Property.set(class1, "timeLife", 999F);
        Property.set(class1, "timeFire", 1.5F);
        Property.set(class1, "force", 1300F);
        Property.set(class1, "power", 2F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.070F);
        Property.set(class1, "massa", 9.2F);
        Property.set(class1, "massaEnd", 5.2F);
        Property.set(class1, "spinningStraightFactor", 1.1F);
        Property.set(class1, "friendlyName", "FFAR-Mk4");
    }
}
