// Source File Name: RocketGunAIM9H.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01 , western0221 2016-07-14 changing classname into AIM9H
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunAIM9H_gn16 extends MissileGun implements RocketGunWithDelay {

	static {
		Class class1 = RocketGunAIM9H_gn16.class;
		Property.set(class1, "bulletClass", (Object) MissileAIM9H_gn16.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
