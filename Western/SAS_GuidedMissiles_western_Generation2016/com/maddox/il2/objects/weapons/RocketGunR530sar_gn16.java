// Source File Name: RocketGunR530sar_gn16.java
// Author:           western0221
// Last Modified by: western0221 2017-12-08
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunR530sar_gn16 extends MissileGun implements RocketGunWithDelay {

	static {
		Class class1 = RocketGunR530sar_gn16.class;
		Property.set(class1, "bulletClass", (Object) MissileR530sar_gn16.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
