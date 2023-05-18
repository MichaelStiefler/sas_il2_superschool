package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class BombM33Sjunkbomb extends Bomb {

	static {
		Class class1 = BombM33Sjunkbomb.class;
		Property.set(class1, "mesh", "3DO/Arms/M33_Sjunkbomb/mono.sim");
		Property.set(class1, "radius", 16F);
		Property.set(class1, "power", 135F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.43F);
		Property.set(class1, "massa", 206F);
		Property.set(class1, "sound", "weapon.bomb_mid");
	}
}
