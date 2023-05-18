package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class P_47D27 extends P_47ModPack {

	public float getEyeLevelCorrection() {
		return 0.05F;
	}

	public static String getSkinPrefix(String s, Regiment regiment) {
		if (regiment == null || regiment.country() == null) return "";
		if (regiment.country().equals("us")) {
			int i = Mission.getMissionDate(true);
			if (i > 0 && i < 19440606) return "PreInvasion_";
		}
		return "";
	}

	public void update(float f) {
		super.update(f);
		this.bubbleTopTailSway();
	}

	static {
		Class class1 = P_47D27.class;
		new NetAircraft.SPAWN(class1);

		Property.set(class1, "iconFar_shortClassName", "P-47");
		Property.set(class1, "meshName", "3DO/Plane/P-47D-27(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "meshName_us", "3DO/Plane/P-47D-27(USA)/hier.him");
		Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
		Property.set(class1, "noseart", 1);
		Property.set(class1, "yearService", 1944.0F);
		Property.set(class1, "yearExpired", 1947.5F);
		Property.set(class1, "FlightModel", useStockFlightModels() ? "FlightModels/P-47D-27.fmd" : "FlightModels/P-47D-27.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitP_47D27.class });
		Property.set(class1, "LOSElevation", 1.1104F);

		weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9 });
		weaponHooksRegister(class1,
				new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04",
						"_ExternalRock04", "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03",
						"_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03" });
	}
}
