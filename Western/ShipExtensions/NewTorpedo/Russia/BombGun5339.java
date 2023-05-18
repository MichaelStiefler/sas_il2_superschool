package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.rts.*;

public class BombGun5339 extends TorpedoApparatus
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

  static Class class$com$maddox$il2$objects$weapons$BombGun5339;
  static Class class$com$maddox$il2$objects$weapons$Bomb5339;

  static{
		Class class1
	    = (class$com$maddox$il2$objects$weapons$BombGun5339 == null
	       ? (class$com$maddox$il2$objects$weapons$BombGun5339
		  = class$("com.maddox.il2.objects.weapons.BombGun5339"))
	       : class$com$maddox$il2$objects$weapons$BombGun5339);
		Property.set(class1, "bulletClass",
		     ((Object)
		      (class$com$maddox$il2$objects$weapons$Bomb5339 == null
		       ? (class$com$maddox$il2$objects$weapons$Bomb5339
			  = class$("com.maddox.il2.objects.weapons.Bomb5339"))
		       : class$com$maddox$il2$objects$weapons$Bomb5339)));

        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 0.01F);
        Property.set(class1, "aimMinDist", 200F);
        Property.set(class1, "aimMaxDist", 8000F);
        Property.set(class1, "external", 0);
        Property.set(class1, "sound", "weapon.bombgun_torpedo");
  }
}