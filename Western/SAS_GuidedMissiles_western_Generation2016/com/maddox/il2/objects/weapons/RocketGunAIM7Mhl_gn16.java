package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAIM7Mhl_gn16 extends MissileGun implements RocketGunWithDelay {
	static {
		Class class1 = RocketGunAIM7Mhl_gn16.class;
		Property.set(class1, "bulletClass", (Object) MissileAIM7Mhl_gn16.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 5F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
