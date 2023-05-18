package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombFR_200Kg extends Bomb {

	static {
		Class class1 = BombFR_200Kg.class;
		Property.set(class1, "mesh", "3DO/Arms/BombFR200Kg/mono.sim");
		Property.set(class1, "radius", 150F);
		Property.set(class1, "power", 106.5F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.38F);
		Property.set(class1, "massa", 234F);
		Property.set(class1, "sound", "weapon.bomb_mid");
	}
}
