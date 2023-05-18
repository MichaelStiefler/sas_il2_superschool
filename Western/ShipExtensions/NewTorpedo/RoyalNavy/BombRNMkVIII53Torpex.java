package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombRNMkVIII53Torpex extends Torpedo
{
    static Class class$com$maddox$il2$objects$weapons$BombRNMkVIII53Torpex;

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
	    = (class$com$maddox$il2$objects$weapons$BombRNMkVIII53Torpex == null
	       ? (class$com$maddox$il2$objects$weapons$BombRNMkVIII53Torpex
		  = class$("com.maddox.il2.objects.weapons.BombRNMkVIII53Torpex"))
	       : class$com$maddox$il2$objects$weapons$BombRNMkVIII53Torpex);
      
        Property.set(class1, "mesh", "3do/arms/45-12/mono.sim");

        Property.set(class1, "radius", 16.2F);
        Property.set(class1, "power", 547.5F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.533F);
        Property.set(class1, "massa", 1693.0F);
        Property.set(class1, "sound", "weapon.torpedo");

        Property.set(class1, "velocity", 21.09222F);
        Property.set(class1, "traveltime", 303.43F);
        Property.set(class1, "startingspeed", 40.0F);
    }
}
