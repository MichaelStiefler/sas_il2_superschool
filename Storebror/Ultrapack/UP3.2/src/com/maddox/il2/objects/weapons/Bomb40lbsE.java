package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb40lbsE extends Bomb {

	static {
		Class class1 = Bomb40lbsE.class;
		Property.set(class1, "mesh", "3DO/Arms/Bomb40lbsGP/mono.sim");
		Property.set(class1, "power", 12F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.12827F);
		Property.set(class1, "massa", 19F);
		Property.set(class1, "sound", "weapon.bomb_mid");
	}
}
