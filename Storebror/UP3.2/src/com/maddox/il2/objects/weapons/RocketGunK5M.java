// Source File Name: RocketGunK5M.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunK5M extends MissileGun implements MissileGunWithDelay {
	static {
		Class class1 = RocketGunK5M.class;
		Property.set(class1, "bulletClass", (Object) MissileK5M.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
