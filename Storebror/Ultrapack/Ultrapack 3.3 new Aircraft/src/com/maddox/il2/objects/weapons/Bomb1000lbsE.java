package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb1000lbsE extends Bomb {

	static {
		Class class1 = Bomb1000lbsE.class;
		Property.set(class1, "mesh", "3DO/Arms/1000LbsBombE/mono.sim");
		Property.set(class1, "radius", 138F);
		Property.set(class1, "power", 110F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.4572F);
		Property.set(class1, "massa", 453.6F);
		Property.set(class1, "sound", "weapon.bomb_big");
	}
}
