package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_Tank18C extends FuelTank {

	static {
		Class class1 = FuelTank_Tank18C.class;
		Property.set(class1, "mesh", "3DO/Arms/Tank18C/mono.sim");
		Property.set(class1, "kalibr", 0.7F);
		Property.set(class1, "massa", 1000F);
	}
}
