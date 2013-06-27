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

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitG4M2E_TGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
			aircraft().hierMesh().chunkVisible("Turret3A_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
		aircraft().hierMesh().chunkVisible("Turret3A_D0", true);
		super.doFocusLeave();
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("Turret1A", -orient.getYaw(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage(), 0.0F);
		mesh.chunkSetAngles("Turret1C", -orient.getYaw(), 0.0F, 0.0F);
		mesh.chunkSetAngles("Turret1D", 0.0F, orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		for (; f < -180F; f += 360F)
			;
		for (; f > 180F; f -= 360F)
			;
		for (; prevA0 < -180F; prevA0 += 360F)
			;
		for (; prevA0 > 180F; prevA0 -= 360F)
			;
		if (!isRealMode()) {
			prevA0 = f;
		} else {
			if (bNeedSetUp) {
				prevTime = Time.current() - 1L;
				bNeedSetUp = false;
			}
			if (f < -120F && prevA0 > 120F)
				f += 360F;
			else if (f > 120F && prevA0 < -120F)
				prevA0 += 360F;
			float f3 = f - prevA0;
			float f4 = 0.001F * (float) (Time.current() - prevTime);
			float f5 = Math.abs(f3 / f4);
			if (f5 > 120F)
				if (f > prevA0)
					f = prevA0 + 120F * f4;
				else if (f < prevA0)
					f = prevA0 - 120F * f4;
			prevTime = Time.current();
			if (f1 > 73F)
				f1 = 73F;
			if (f1 < 0.0F)
				f1 = 0.0F;
			orient.setYPR(f, f1, 0.0F);
			orient.wrap();
			prevA0 = f;
		}
	}

	protected void interpTick() {
		if (isRealMode()) {
			if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
				bGunFire = false;
			fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
			if (bGunFire) {
				if (hook1 == null)
					hook1 = new HookNamed(aircraft(), "_CANNON01");
				doHitMasterAircraft(aircraft(), hook1, "_CANNON01");
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

	public void reflectCockpitState() {
		if ((fm.AS.astateCockpitState & 4) != 0)
			mesh.chunkVisible("Z_Holes1_D1", true);
		if ((fm.AS.astateCockpitState & 0x10) != 0)
			mesh.chunkVisible("Z_Holes2_D1", true);
	}

	public CockpitG4M2E_TGunner() {
		super("3DO/Cockpit/G4M2E-TGun/TGunnerG4M2E.him", "he111_gunner");
		bNeedSetUp = true;
		prevTime = -1L;
		prevA0 = 0.0F;
		hook1 = null;
	}

	private boolean bNeedSetUp;
	private long prevTime;
	private float prevA0;
	private Hook hook1;
	static Class class$com$maddox$il2$objects$air$CockpitG4M2E_TGunner;

	static {
		Property.set(CLASS.THIS(), "aiTuretNum", 2);
		Property.set(CLASS.THIS(), "weaponControlNum", 12);
		Property.set(CLASS.THIS(), "astatePilotIndx", 3);
	}
}
