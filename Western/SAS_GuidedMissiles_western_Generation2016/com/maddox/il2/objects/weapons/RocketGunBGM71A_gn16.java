package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunBGM71A_gn16 extends MissileGun implements RocketGunWithDelay {

	static {
		Class class1 = RocketGunBGM71A_gn16.class;
		Property.set(class1, "bulletClass", (Object) MissileBGM71A_gn16.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 4.0F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}