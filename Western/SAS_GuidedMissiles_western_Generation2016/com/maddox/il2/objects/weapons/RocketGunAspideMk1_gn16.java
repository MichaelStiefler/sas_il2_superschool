package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAspideMk1_gn16 extends MissileGun implements RocketGunWithDelay {
	static {
		Class class1 = RocketGunAspideMk1_gn16.class;
		Property.set(class1, "bulletClass", (Object) MissileAspideMk1_gn16.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
