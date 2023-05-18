package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunCooper20lb extends BombGun {

	public BombGunCooper20lb() {
	}

	static {
		Class class1 = BombGunCooper20lb.class;
		Property.set(class1, "bulletClass", (Object) BombCooper20lb.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 3F);
		Property.set(class1, "external", 1);
		Property.set(class1, "sound", "weapon.bombgun");
	}
}
