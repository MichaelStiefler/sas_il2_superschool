package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGerG7aTorpedo extends GerTorpedo_a
{
    static Class class$com$maddox$il2$objects$weapons$BombGerG7aTorpedo;

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

  static
  {
    	Class class1
	    = (class$com$maddox$il2$objects$weapons$BombGerG7aTorpedo == null
	       ? (class$com$maddox$il2$objects$weapons$BombGerG7aTorpedo
		  = class$("com.maddox.il2.objects.weapons.BombGerG7aTorpedo"))
	       : class$com$maddox$il2$objects$weapons$BombGerG7aTorpedo);
      
        Property.set(class1, "mesh", "3do/arms/45-12/mono.sim");

        Property.set(class1, "radius", 18.0F);
        Property.set(class1, "power", 280.0F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.533F);
        Property.set(class1, "massa", 1490.0F);
        Property.set(class1, "sound", "weapon.torpedo");

        Property.set(class1, "velocity", 20.57777F);
        Property.set(class1, "traveltime", 388.0F);
        Property.set(class1, "startingspeed", 40.0F);
  }
}
