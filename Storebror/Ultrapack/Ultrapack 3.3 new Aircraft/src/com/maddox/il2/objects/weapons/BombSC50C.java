package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombSC50C extends Bomb {

	protected boolean haveSound() {
		return this.index % 16 == 0;
	}

	static {
		Class class1 = BombSC50C.class;
		Property.set(class1, "mesh", "3do/arms/sc-50/mono.sim");
		Property.set(class1, "radius", 25F);
		Property.set(class1, "power", 24.4F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.2F);
		Property.set(class1, "massa", 55.5F);
		Property.set(class1, "sound", "weapon.bomb_mid");
	}
}
