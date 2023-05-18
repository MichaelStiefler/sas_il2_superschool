package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class BF_109B1 extends BF_109_Early {

	static {
		Class class1 = BF_109B1.class;
		BF_109_Early.init(class1);
		Property.set(class1, "meshName", "3DO/Plane/BF_109B1/hier.him");
		Property.set(class1, "FlightModel", "FlightModels/Bf-109B-1SAS.fmd");
	}
}
