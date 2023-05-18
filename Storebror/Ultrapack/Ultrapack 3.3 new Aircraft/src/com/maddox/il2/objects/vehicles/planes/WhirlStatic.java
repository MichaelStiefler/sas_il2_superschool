// This file is part of the SAS IL-2 Sturmovik 1946
// Westland Whirlwind Mod.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Mod Creator:          tfs101
// Original file source: SAS~Epervier
// Modified by:          SAS - Special Aircraft Services
//                       www.sas1946.com
//
// Last Edited by:       SAS~Storebror
// Last Edited at:       2013/09/29

package com.maddox.il2.objects.vehicles.planes;

public abstract class WhirlStatic extends Plane {
	public static class WhirlwindA extends PlaneGeneric {}

	static {
		new PlaneGeneric.SPAWN(WhirlwindA.class);
	}

}
