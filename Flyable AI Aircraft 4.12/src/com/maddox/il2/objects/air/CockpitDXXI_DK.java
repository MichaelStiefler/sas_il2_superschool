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
// Last Edited at: 2013/01/22

package com.maddox.il2.objects.air;

public class CockpitDXXI_DK extends CockpitDXXI {
	public CockpitDXXI_DK() {
		super();
		ac = (DXXI_DK) aircraft();
	}

	protected boolean getChangedPit() {
		return ((DXXI_DK) aircraft()).bChangedPit;
	}

	protected void setChangedPit(boolean changedPit) {
		((DXXI_DK) aircraft()).bChangedPit = changedPit;
	}

	protected float getGyroDelta() {
		return ((DXXI_DK) ac).gyroDelta;
	}
}