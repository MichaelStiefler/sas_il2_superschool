package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

public class FuelTank_Boomer70gal extends FuelTank {

	public FuelTank_Boomer70gal() {
	}

	static {
		Class class1 = FuelTank_Boomer70gal.class;
		Property.set(class1, "mesh", "3DO/Arms/Boomer70gal_Droptank/mono.sim");
		Property.set(class1, "kalibr", 0.6F);
		Property.set(class1, "massa", 140F);
	}
}
