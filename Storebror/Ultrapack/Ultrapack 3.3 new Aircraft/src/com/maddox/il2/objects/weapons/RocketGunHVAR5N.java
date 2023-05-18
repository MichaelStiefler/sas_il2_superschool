package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class RocketGunHVAR5N extends RocketGun {

	public void setRocketTimeLife(float paramFloat) {
		this.timeLife = -1F;
	}

	static {
		Class class1 = RocketGunHVAR5N.class;
		Property.set(class1, "bulletClass", (Object) RocketHVAR5N.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 1.0F);
		Property.set(class1, "sound", "weapon.rocketgun_132");
	}
}
