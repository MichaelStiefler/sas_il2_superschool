package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class Bomb1000lbsGPE extends Bomb {

	static {
		Class class1 = Bomb1000lbsGPE.class;
		Property.set(class1, "mesh", "3DO/Arms/Bomb1000lbsGPE/mono.sim");
		Property.set(class1, "power", 250F);
		Property.set(class1, "powerType", 0);
		Property.set(class1, "kalibr", 0.32F);
		Property.set(class1, "massa", 500F);
		Property.set(class1, "sound", "weapon.bomb_big");
	}
}
