package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CockpitPE2_265 extends CockpitPE2_402 {

	public CockpitPE2_265() {
		super("3DO/Cockpit/Pe-2series402/hier-265.him");
	}

	static {
		Property.set(CockpitPE2_265.class, "normZNs", new float[] { 1.45F, 0.89F, 1.1F, 0.89F });
	}
}
