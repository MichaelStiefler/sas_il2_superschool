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

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitB17E_FRGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("TurretRA", 0.0F, -orient.getYaw() + 36.0F, 0.0F);
		mesh.chunkSetAngles("TurretRB", 25F, orient.getTangage(), 0.0F);
		mesh.chunkSetAngles("TurretRC", 0.0F, -orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (isRealMode())
			if (!aiTurret().bIsOperable) {
				orient.setYPR(0.0F, 0.0F, 0.0F);
			} else {
				float f = orient.getYaw();
				float f1 = orient.getTangage();
				if (f < -45F)
					f = -45F;
				if (f > 45F)
					f = 45F;
				if (f1 > 32F)
					f1 = 32F;
				if (f1 < -40F)
					f1 = -40F;
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
				if (hook1 == null)
					hook1 = new HookNamed(aircraft(), "_MGUN06");
				doHitMasterAircraft(aircraft(), hook1, "_MGUN06");
			}
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

	public CockpitB17E_FRGunner() {
		super("3DO/Cockpit/B-25J-RGun/FRGunnerB17E.him", "he111_gunner");
		hook1 = null;
	}

	public void reflectWorldToInstruments(float f) {
	}

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 4) != 0)
			mesh.chunkVisible("XGlassDamage1", true);
		if ((fm.AS.astateCockpitState & 8) != 0) {
			mesh.chunkVisible("XGlassDamage1", true);
			mesh.chunkVisible("XHullDamage1", true);
		}
		if ((fm.AS.astateCockpitState & 0x10) != 0) {
			mesh.chunkVisible("XGlassDamage2", true);
			mesh.chunkVisible("XHullDamage2", true);
		}
		if ((fm.AS.astateCockpitState & 0x20) != 0) {
			mesh.chunkVisible("XGlassDamage2", true);
			mesh.chunkVisible("XHullDamage3", true);
		}
	}

	private Hook hook1;

	static {
		Property.set(CockpitB17E_FRGunner.class, "aiTuretNum", 2);
		Property.set(CockpitB17E_FRGunner.class, "weaponControlNum", 12);
		Property.set(CockpitB17E_FRGunner.class, "astatePilotIndx", 2);
	}
}
