package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombCooper20lb extends Bomb {

	static {
		Class class1 = BombCooper20lb.class;
		Property.set(class1, "mesh", "3do/arms/Cooper_20lbsIII/mono.sim");
		Property.set(class1, "radius", 15F);
		Property.set(class1, "power", 4F);
		Property.set(class1, "powerType", 1);
		Property.set(class1, "kalibr", 0.15F);
		Property.set(class1, "massa", 8F);
		Property.set(class1, "sound", "weapon.bomb_mid");
	}
}
