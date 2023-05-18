package com.maddox.il2.objects.weapons;

import com.maddox.il2.objects.air.HE_177_MOD;
import com.maddox.rts.Property;

public class FuelTankGun_Tank790_He177_internal extends FuelTankGun {
	public void shots(int paramInt) {
		if (paramInt != 0) {
			if (((HE_177_MOD) this.actor).FM.CT.getBayDoor() < 0.9F) return;
			super.shots(paramInt);
		}
	}

	static {
		Class class1 = FuelTankGun_Tank790_He177_internal.class;
		Property.set(class1, "bulletClass", (Object) FuelTank_Tank790_He177_internal.class);
		Property.set(class1, "bullets", 1);
		Property.set(class1, "shotFreq", 0.25F);
		Property.set(class1, "external", 1);
	}
}
