// This file is part of the SAS IL-2 Sturmovik 1946 4.12
// Flyable AI Aircraft Mod package.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Original file source: 1C/Maddox/TD
// Modified by: SAS - Special Aircraft Services
//              www.sas1946.com
//
// Last Edited by: SAS~Storebror
// Last Edited at: 2013/06/11

package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.Mission;
import com.maddox.rts.Property;

public class GLADIATOR2 extends GLADIATOR {

	public GLADIATOR2() {
	}

	public static String getSkinPrefix(String s, Regiment regiment) {
		if (regiment == null || regiment.country() == null)
			return "";
		if (regiment.country().equals("fi")) {
			int i = Mission.getMissionDate(true);
			if (i > 0) {
				if (i < 0x1282df2)
					return "winterwar_";
				if (i < 0x1282fd5)
					return "earlycontwar_";
				else
					return "contwar_";
			}
		}
		return "";
	}

	static {
		Class class1 = GLADIATOR2.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Gladiator");
		Property.set(class1, "meshName", "3DO/Plane/GladiatorMkII(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
		Property.set(class1, "yearService", 1939F);
		Property.set(class1, "yearExpired", 1943F);
		Property.set(class1, "FlightModel", "FlightModels/GladiatorMkII.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitGLADIATOR2.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1 });
		weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
		weaponsRegister(class1, "default", new String[] { "MGunBrowning303sipzl 600", "MGunBrowning303sipzl 600", "MGunBrowning303k 400", "MGunBrowning303k 400" });
		weaponsRegister(class1, "none", new String[] { null, null, null, null });
	}
}
