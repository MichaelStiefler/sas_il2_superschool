package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombSC10 extends Bomb {

	static {
		Class class1 = BombSC10.class;
		Property.set(class1, "mesh", "3DO/Arms/SC10/mono.sim");
		Property.set(class1, "radius", 12F);
		Property.set(class1, "power", 0.9F);
		Property.set(class1, "powerType", 1);
		Property.set(class1, "kalibr", 0.086F);
		Property.set(class1, "massa", 10F);
		Property.set(class1, "sound", "weapon.bomb_mid");
	}
}
