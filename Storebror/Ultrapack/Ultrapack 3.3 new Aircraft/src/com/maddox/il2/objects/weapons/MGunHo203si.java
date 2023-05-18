package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.GunProperties;

public class MGunHo203si extends MGunHo203s {

	public GunProperties createProperties() {
		GunProperties gunproperties = super.createProperties();
		gunproperties.shells = null;
		return gunproperties;
	}
}
