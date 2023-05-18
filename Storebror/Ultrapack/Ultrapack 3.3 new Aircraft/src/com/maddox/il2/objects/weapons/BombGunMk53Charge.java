package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunMk53Charge extends BombGun {
	static {
		Class class1 = BombGunMk53Charge.class;
		Property.set(class1, "bulletClass", (Object) BombMk53Charge.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 3F);
		Property.set(class1, "external", 1);
		Property.set(class1, "sound", "weapon.bombgun");
	}
}
