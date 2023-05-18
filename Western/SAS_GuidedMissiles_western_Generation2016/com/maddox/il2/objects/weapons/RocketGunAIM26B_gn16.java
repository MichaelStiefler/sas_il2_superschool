// Source File Name: RocketGunAIM26B_gn16.java
// Author:           western0221
// Last Modified by: western0221 2017-11-11
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAIM26B_gn16 extends MissileGun {
	static {
		Class class1 = RocketGunAIM26B_gn16.class;
		Property.set(class1, "bulletClass", (Object) MissileAIM26B_gn16.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
