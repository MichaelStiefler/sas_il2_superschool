package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAIM7Eps extends MissileGun implements RocketGunWithDelay {

	static {
		Class class1 = RocketGunAIM7Eps.class;
		Property.set(class1, "bulletClass", (Object) MissileAIM7Eps.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
