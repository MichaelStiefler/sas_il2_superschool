// Source File Name: FuelTankGun
// Author:		   western0221
// Last Modified by: western0221 2017-Nov.-17
// add a function of --- No Jettison static external tank flag.

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//			BombGun, FuelTank

public class FuelTankGun extends BombGun {

	public FuelTankGun() {
		Class class1 = getClass();
		bNoJettison = (Property.intValue(class1, "noJettison", 0) == 1);
	}

	public void shots(int i) {
		if (i != 0)
			super.shots(i);
	}

	public FuelTank getFuelTank() {
		if (!(bomb instanceof FuelTank))
			return null;
		else
			return (FuelTank) bomb;
	}

	public boolean isNoJettison() {
		return bNoJettison;
	}

	private boolean bNoJettison = false;

	static {
		Class class1 = com.maddox.il2.objects.weapons.FuelTankGun.class;
		Property.set(class1, "sound", "weapon.bombgun_fueltank");
	}
}
