package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_Tank18W extends FuelTank {

	static {
		Class class1 = FuelTank_Tank18W.class;
		Property.set(class1, "mesh", "3DO/Arms/TankF4EF/mono.sim");
		Property.set(class1, "kalibr", 0.6F);
		Property.set(class1, "massa", 900F);
	}
}
