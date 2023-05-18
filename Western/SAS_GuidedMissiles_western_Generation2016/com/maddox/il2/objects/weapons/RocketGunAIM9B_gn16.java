// Source File Name: RocketGunAIM9B.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAIM9B_gn16 extends MissileGun implements RocketGunWithDelay {
	static {
		Class class1 = RocketGunAIM9B_gn16.class;
		Property.set(class1, "bulletClass", (Object) MissileAIM9B_gn16.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
