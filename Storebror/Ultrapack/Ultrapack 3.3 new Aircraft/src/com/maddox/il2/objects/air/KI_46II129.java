package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class KI_46II129 extends KI_46 implements TypeFighter {
	public KI_46II129() {
		this.bChangedPit = true;
	}

	protected void nextDMGLevel(String var1, int var2, Actor var3) {
		super.nextDMGLevel(var1, var2, var3);
		if (this.FM.isPlayers()) this.bChangedPit = true;
	}

	protected void nextCUTLevel(String var1, int var2, Actor var3) {
		super.nextCUTLevel(var1, var2, var3);
		if (this.FM.isPlayers()) this.bChangedPit = true;
	}

	public boolean bChangedPit;

	static {
		Class localClass = KI_46II129.class;
		new NetAircraft.SPAWN(localClass);
		Property.set(localClass, "iconFar_shortClassName", "Ki-46 II");
		Property.set(localClass, "meshName", "3DO/Plane/Ki46II_Dinah/hier.him");
		Property.set(localClass, "PaintScheme", new PaintSchemeFMPar01());
		Property.set(localClass, "yearService", 1941F);
		Property.set(localClass, "yearExpired", 1945F);
		Property.set(localClass, "FlightModel", "FlightModels/Ki-46-IIIKai.fmd");
		Property.set(localClass, "cockpitClass", new Class[] { CockpitKI_46_OTSU.class });
		Aircraft.weaponTriggersRegister(localClass, new int[2]);
		Aircraft.weaponHooksRegister(localClass, new String[] { "_MGUN01", "_MGUN02" });
	}
}
