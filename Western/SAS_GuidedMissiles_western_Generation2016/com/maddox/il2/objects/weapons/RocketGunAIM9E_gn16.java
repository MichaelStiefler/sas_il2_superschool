// Source File Name: RocketGunAIM9E.java
// Author:           western0221
// Last Modified by: western0221 2016-07-14
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAIM9E_gn16 extends MissileGun implements RocketGunWithDelay {

	static {
		Class class1 = RocketGunAIM9E_gn16.class;
		Property.set(class1, "bulletClass", (Object) MissileAIM9E_gn16.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
