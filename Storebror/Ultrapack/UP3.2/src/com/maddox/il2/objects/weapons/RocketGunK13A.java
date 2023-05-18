// Source File Name: RocketGunK13A.java
// Author:           Storebror
// Last Modified by: Storebror 2011-11-19
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunK13A extends MissileGun implements MissileGunWithDelay {
	static {
		Class class1 = RocketGunK13A.class;
		Property.set(class1, "bulletClass", (Object) MissileK13A.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
//	    Property.set(class1, "dateOfUse", 19610101);
	}
}
