package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_150F7F extends FuelTank {

	public FuelTank_150F7F() {
	}

	static {
		Class class1 = FuelTank_150F7F.class;
		Property.set(class1, "mesh", "3DO/Arms/F7F150gal/mono.sim");
		Property.set(class1, "kalibr", 0.6F);
		Property.set(class1, "massa", 312F);
	}
}
