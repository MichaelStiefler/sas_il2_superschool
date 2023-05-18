package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAS34_gn16 extends MissileGun implements RocketGunWithDelay {
	static {
		Class class1 = RocketGunAS34_gn16.class;
		Property.set(class1, "bulletClass", (Object) MissileAS34_gn16.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 1.0F);
		Property.set(class1, "sound", "weapon.bombgun");
	}
}
