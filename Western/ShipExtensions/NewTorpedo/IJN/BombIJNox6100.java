package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombIJNox6100 extends IJNoxTorpedo
{
    static Class class$com$maddox$il2$objects$weapons$BombIJNox6100;

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
	    = (class$com$maddox$il2$objects$weapons$BombIJNox6100 == null
	       ? (class$com$maddox$il2$objects$weapons$BombIJNox6100
		  = class$("com.maddox.il2.objects.weapons.BombIJNox6100"))
	       : class$com$maddox$il2$objects$weapons$BombIJNox6100);
      
        Property.set(class1, "mesh", "3do/arms/45-12/mono.sim");

        Property.set(class1, "radius", 23.0F);
        Property.set(class1, "power", 576.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.610F);
        Property.set(class1, "massa", 2700.0F);
        Property.set(class1, "sound", "weapon.torpedo");

        Property.set(class1, "velocity", 24.69333F);
        Property.set(class1, "traveltime", 810.0F);
        Property.set(class1, "startingspeed", 40.0F);
    }
}
