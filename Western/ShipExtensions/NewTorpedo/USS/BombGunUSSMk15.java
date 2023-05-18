package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.rts.*;

public class BombGunUSSMk15 extends USSTorpedoApparatus
{

    public BulletEmitter detach(int i)
    {
        return null;
    }

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

  static Class class$com$maddox$il2$objects$weapons$BombGunUSSMk15;
  static Class class$com$maddox$il2$objects$weapons$BombUSStorpMk15;

  static{
		Class class1
	    = (class$com$maddox$il2$objects$weapons$BombGunUSSMk15 == null
	       ? (class$com$maddox$il2$objects$weapons$BombGunUSSMk15
		  = class$("com.maddox.il2.objects.weapons.BombGunUSSMk15"))
	       : class$com$maddox$il2$objects$weapons$BombGunUSSMk15);
		Property.set(class1, "bulletClass",
		     ((Object)
		      (class$com$maddox$il2$objects$weapons$BombUSStorpMk15 == null
		       ? (class$com$maddox$il2$objects$weapons$BombUSStorpMk15
			  = class$("com.maddox.il2.objects.weapons.BombUSStorpMk15"))
		       : class$com$maddox$il2$objects$weapons$BombUSStorpMk15)));

        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.01F);
        Property.set(class1, "aimMinDist", 200F);
        Property.set(class1, "aimMaxDist", 5500F);
        Property.set(class1, "external", 0);
        Property.set(class1, "sound", "weapon.bombgun_torpedo");
  }
}