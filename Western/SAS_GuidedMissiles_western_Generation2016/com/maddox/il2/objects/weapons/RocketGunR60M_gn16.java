// Source File Name: RocketGunR60M_gn16.java
// Author:           western0221
// Last Modified by: western0221 2018-12-30
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunR60M_gn16 extends MissileGun implements RocketGunWithDelay {

	static {
		Class class1 = RocketGunR60M_gn16.class;
		Property.set(class1, "bulletClass", (Object) MissileR60M_gn16.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
