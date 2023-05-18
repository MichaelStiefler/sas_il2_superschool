package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.rts.Property;

public class RocketGunAIM9L_gn16 extends MissileGun implements RocketGunWithDelay {

	static {
		Class class1 = RocketGunAIM9L_gn16.class;
		Property.set(class1, "bulletClass", (Object) MissileAIM9L_gn16.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 5.0F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}

}