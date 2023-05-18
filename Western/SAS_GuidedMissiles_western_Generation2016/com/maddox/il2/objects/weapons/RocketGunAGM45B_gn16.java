package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAGM45B_gn16 extends MissileGun implements RocketGunWithDelay {
	static {
		Class class1 = RocketGunAGM45B_gn16.class;
		Property.set(class1, "bulletClass", (Object) MissileAGM45B_gn16.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
