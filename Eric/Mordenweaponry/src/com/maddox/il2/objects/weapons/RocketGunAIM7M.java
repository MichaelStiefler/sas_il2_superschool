package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAIM7M extends MissileGun implements RocketGunWithDelay {
	static {
		Class class1 = RocketGunAIM7M.class;
		Property.set(class1, "bulletClass", (Object)MissileAIM7M.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 5F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
