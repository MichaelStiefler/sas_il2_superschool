
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.rts.Property;

public class Rocket5inchZuniMk71_gn16 extends Rocket
{

    public Rocket5inchZuniMk71_gn16()
    {
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

    static Class class$com$maddox$il2$objects$weapons$Rocket5inchZuniMk71_gn16;

    static 
    {
    	Class class1
	    = (class$com$maddox$il2$objects$weapons$Rocket5inchZuniMk71_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$Rocket5inchZuniMk71_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.Rocket5inchZuniMk71_gn16"))
	       : class$com$maddox$il2$objects$weapons$Rocket5inchZuniMk71_gn16);
        Property.set(class1, "mesh", "3DO/Arms/Zuni_5inch_gn16/Mk71Mk24close.sim");
        Property.set(class1, "meshFly", "3DO/Arms/Zuni_5inch_gn16/Mk71Mk24fly.sim");
        Property.set(class1, "sprite", "3DO/Effects/Rocket/firesprite.eff");
        Property.set(class1, "flame", "3DO/Effects/Rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Rocket/rocketsmokewhite.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 60F);
        Property.set(class1, "timeLife", 999.999F);
        Property.set(class1, "timeFire", 1.5F);
        Property.set(class1, "force", 1700F);
        Property.set(class1, "power", 19F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.128F);
        Property.set(class1, "massa", 56.8F);
        Property.set(class1, "massaEnd", 39.1F);
        Property.set(class1, "spinningStraightFactor", 1.5F);
        Property.set(class1, "friendlyName", "Zuni");
    }
}
