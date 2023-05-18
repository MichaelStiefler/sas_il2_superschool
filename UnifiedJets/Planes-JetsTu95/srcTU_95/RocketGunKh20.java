package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunKh20 extends MissileGun implements RocketGunWithDelay {

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

  static Class class$com$maddox$il2$objects$weapons$RocketGunKh20;
  static Class class$com$maddox$il2$objects$weapons$MissileKh20;

	static {
    	Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketGunKh20 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketGunKh20
		  = class$("com.maddox.il2.objects.weapons.RocketGunKh20"))
	       : class$com$maddox$il2$objects$weapons$RocketGunKh20);
		Property.set(class1, "bulletClass", (Object)
		      (class$com$maddox$il2$objects$weapons$MissileKh20 == null
		       ? (class$com$maddox$il2$objects$weapons$MissileKh20
			  = class$("com.maddox.il2.objects.weapons.MissileKh20"))
		       : class$com$maddox$il2$objects$weapons$MissileKh20)
                             );
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 2.0F);
		Property.set(class1, "sound", "weapon.bombgun");
		Property.set(class1, "dateOfUse", 0x1298D71);
	}



}
