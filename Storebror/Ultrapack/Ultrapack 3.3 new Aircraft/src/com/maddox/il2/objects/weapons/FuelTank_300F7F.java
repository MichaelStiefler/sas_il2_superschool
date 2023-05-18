package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_300F7F extends FuelTank {

	public FuelTank_300F7F() {
	}

	static {
		Class class1 = FuelTank_300F7F.class;
		Property.set(class1, "mesh", "3DO/Arms/F7F300gal/mono.sim");
		Property.set(class1, "kalibr", 0.6F);
		Property.set(class1, "massa", 624F);
	}
}
