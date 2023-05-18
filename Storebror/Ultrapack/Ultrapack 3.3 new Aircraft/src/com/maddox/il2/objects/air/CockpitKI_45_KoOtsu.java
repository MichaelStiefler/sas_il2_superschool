package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class CockpitKI_45_KoOtsu extends CockpitKI_45 {

	public CockpitKI_45_KoOtsu() {
		super("3DO/Cockpit/Ki-45/hier_KoOtsu.him");
	}

	static {
		Property.set(CockpitKI_45_KoOtsu.class, "normZNs", new float[] { 0.7F, 0.7F, 0.8F, 0.7F });
	}
}
