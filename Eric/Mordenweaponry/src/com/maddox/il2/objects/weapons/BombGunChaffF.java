package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGunChaffF extends BombGun {
	static {
		Class class1 = BombGunChaffF.class;
		Property.set(class1, "bulletClass", (Object) BombChaffF.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 6F);
		Property.set(class1, "external", 1);
		Property.set(class1, "sound", "weapon.bombgun");
	}
}