package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIJN61 extends Torpedo
{
    static Class class$com$maddox$il2$objects$weapons$BombIJN61;

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
	    = (class$com$maddox$il2$objects$weapons$BombIJN61 == null
	       ? (class$com$maddox$il2$objects$weapons$BombIJN61
		  = class$("com.maddox.il2.objects.weapons.BombIJN61"))
	       : class$com$maddox$il2$objects$weapons$BombIJN61);
      
        Property.set(class1, "mesh", "3do/arms/45-12/mono.sim");

        Property.set(class1, "radius", 20.0F);
        Property.set(class1, "power", 390.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.533F);
        Property.set(class1, "massa", 2100.0F);
        Property.set(class1, "sound", "weapon.torpedo");

        Property.set(class1, "velocity", 20.66F);
        Property.set(class1, "traveltime", 387.22F);
        Property.set(class1, "startingspeed", 40.0F);
    }
}
