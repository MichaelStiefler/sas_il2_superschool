// Source File Name: RocketGunR55f.java
// Author:           Storebror
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunR55f extends MissileGun implements RocketGunWithDelay {

	static {
		Class class1 = RocketGunR55f.class;
		Property.set(class1, "bulletClass", (Object) MissileR55f.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
