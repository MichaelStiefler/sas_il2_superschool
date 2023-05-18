package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombGun300lb_MkI_MII extends BombGun {

	static {
		Class class1 = BombGun300lb_MkI_MII.class;
		Property.set(class1, "bulletClass", (Object) Bomb300lb_MkI_MII.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 3F);
		Property.set(class1, "external", 1);
		Property.set(class1, "sound", "weapon.bombgun");
	}
}
