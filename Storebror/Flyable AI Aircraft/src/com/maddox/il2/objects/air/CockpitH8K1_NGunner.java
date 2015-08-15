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

import com.maddox.il2.engine.Orient;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class CockpitH8K1_NGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = -orient.getYaw();
		float f1 = orient.getTangage();
		mesh.chunkSetAngles("Turret1A", 25F, -f, 0.0F);
		mesh.chunkSetAngles("Turret1B", 0.0F, f1, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -35F)
					f = -35F;
				if (f > 35F)
					f = 35F;
				if (f1 > 25F)
					f1 = 25F;
				if (f1 < -45F)
					f1 = -45F;
				orient.setYPR(f, f1, 0.0F);
				orient.wrap();
			}
	}

	protected void interpTick() {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
			if (bGunFire) {
				if (iCocking > 0)
					iCocking = 0;
				else
					iCocking = 1;
			} else {
				iCocking = 0;
			}
			resetYPRmodifier();
			Cockpit.xyz[1] = -0.07F * (float) iCocking;
			Cockpit.ypr[1] = 0.0F;
			mesh.chunkSetLocate("Turret1C", Cockpit.xyz, Cockpit.ypr);
		}
	}

	public void doGunFire(boolean flag) {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			else
				bGunFire = flag;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
		}
	}

	public CockpitH8K1_NGunner() {
		super("3DO/Cockpit/G4M1-11-NGun/NGunnerH8K1.him", "he111");
		iCocking = 0;
	}

	public void reflectCockpitState() {
	}

	private int iCocking;

	static {
		Property.set(CLASS.THIS(), "aiTuretNum", 0);
		Property.set(CLASS.THIS(), "weaponControlNum", 10);
		Property.set(CLASS.THIS(), "astatePilotIndx", 2);
	}
}