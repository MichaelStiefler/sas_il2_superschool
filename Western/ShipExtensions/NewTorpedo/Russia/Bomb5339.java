package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb5339 extends Torpedo
{
    static Class class$com$maddox$il2$objects$weapons$Bomb5339;

    static Class class$(String s)
    {
        Class c;
        try{
            c = Class.forName(s);
        } catch ( ClassNotFoundException e ){
            throw new NoClassDefFoundError(e.getMessage());
        }
        return c;
    }

	static {
    	Class class1
	    = (class$com$maddox$il2$objects$weapons$Bomb5339 == null
	       ? (class$com$maddox$il2$objects$weapons$Bomb5339
		  = class$("com.maddox.il2.objects.weapons.Bomb5339"))
	       : class$com$maddox$il2$objects$weapons$Bomb5339);
      
        Property.set(class1, "mesh", "3do/arms/45-12/mono.sim");

        Property.set(class1, "radius", 20.0F);
        Property.set(class1, "power", 317.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.533F);
        Property.set(class1, "massa", 1750.0F);
        Property.set(class1, "sound", "weapon.torpedo");

        Property.set(class1, "velocity", 20.66F);
        Property.set(class1, "traveltime", 387.22F);
        Property.set(class1, "startingspeed", 40.0F);
    }
}
