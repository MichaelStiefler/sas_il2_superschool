package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb250lbAS_Mk4 extends Bomb {

	static {
		Class class1 = Bomb250lbAS_Mk4.class;
		Property.set(class1, "mesh", "3DO/Arms/250lbAS_Mk4/mono.sim");
		Property.set(class1, "radius", 45F);
		Property.set(class1, "power", 30F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.32F);
		Property.set(class1, "massa", 113F);
		Property.set(class1, "sound", "weapon.bomb_mid");
	}
}
