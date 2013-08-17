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
// Last Edited at: 2013/02/01

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class CockpitG4M2E_LGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			aircraft().hierMesh().chunkVisible("RearAXX_D0", false);
			if (curMat == null) {
				curMat = aircraft().hierMesh().material(aircraft().hierMesh().materialFind("Pilot2"));
				newMat = (Mat) curMat.Clone();
				newMat.setLayer(0);
				newMat.set((short) 0, false);
			}
			aircraft().hierMesh().materialReplace("Pilot2", newMat);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		if (!isFocused()) {
			return;
		} else {
			aircraft().hierMesh().materialReplace("Pilot2", curMat);
			aircraft().hierMesh().chunkVisible("RearAXX_D0", aircraft().isChunkAnyDamageVisible("CF_D"));
			super.doFocusLeave();
			return;
		}
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		mesh.chunkSetAngles("Turret5A", 0.0F, orient.getYaw() + 5.0F, 0.0F);
		mesh.chunkSetAngles("Turret5B", 0.0F, orient.getTangage(), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!isRealMode())
			return;
		if (!aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		if (f < -30F)
			f = -30F;
		if (f > 60F)
			f = 60F;
		if (f1 < -45F)
			f1 = -45F;
		if (f1 > 45F)
			f1 = 45F;
		if (f < 0.0F) {
			if (f1 < cvt(f, -30F, 0.0F, -6F, -23F))
				f1 = cvt(f, -30F, 0.0F, -6F, -23F);
			if (f1 > cvt(f, -30F, 0.0F, 22F, 33F))
				f1 = cvt(f, -30F, 0.0F, 22F, 33F);
		} else if (f < 30F) {
			if (f1 < cvt(f, 0.0F, 30F, -23F, -16F))
				f1 = cvt(f, 0.0F, 30F, -23F, -16F);
			if (f1 > cvt(f, 0.0F, 10F, 33F, 45F))
				f1 = cvt(f, 0.0F, 10F, 33F, 45F);
		} else if (f1 < cvt(f, 30F, 60F, -16F, -10F))
			f1 = cvt(f, 30F, 60F, -16F, -10F);
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		mesh.chunkSetAngles("Turret4A", 0.0F, -aircraft().FM.turret[3].tu[0], 0.0F);
		mesh.chunkSetAngles("Turret4B", 0.0F, aircraft().FM.turret[3].tu[1], 0.0F);
		if (!isRealMode())
			return;
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
		xyz[1] = -0.07F * (float) iCocking;
		mesh.chunkSetLocate("Turret5C", xyz, ypr);
	}

	public void doGunFire(boolean flag) {
		if (!isRealMode())
			return;
		if (emitter == null || !emitter.haveBullets() || !aiTurret().bIsOperable)
			bGunFire = false;
		else
			bGunFire = flag;
		fm.CT.WeaponControl[weaponControlNum()] = bGunFire;
	}

	public CockpitG4M2E_LGunner() {
		super("3DO/Cockpit/G4M1-11-LGun/LGunnerG4M2E.him", "he111_gunner");
		curMat = null;
		iCocking = 0;
	}

	public void reflectCockpitState() {
	}

	Mat curMat;
	Mat newMat;
	private int iCocking;

	static {
		Property.set(CLASS.THIS(), "aiTuretNum", 3);
		Property.set(CLASS.THIS(), "weaponControlNum", 13);
		Property.set(CLASS.THIS(), "astatePilotIndx", 5);
	}
}