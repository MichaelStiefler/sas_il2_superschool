package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

/*
 * Dorsal Gunner for Sunderland Mk.II by Barnesy/CWatson/Freemodding
 * Latest edit: 2016-03-11 by SAS~Storebror
 */
public class CockpitSunderlandTGunner extends CockpitGunner {

	protected void interpTick() {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable || this.suppressGunFire) this.bGunFire = false;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) {
			if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN02");
			this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN02");
			if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN03");
			this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN03");
		}
		// Check if tail damage state has changed
		int curDamageState = ((Sunderlandxyz) this.aircraft()).getTail1DamageVisible();
		if (curDamageState != this.lastDamageState) {
			// If so, hide new damage state tail mesh (tail shall not be visible as long as turret is manned)
			// and keep track of damage state so it can be set visible again when player leaves turret
			this.aircraft().hierMesh().chunkVisible("Tail1_D" + this.lastDamageState, false);
			curDamageState = this.lastDamageState;
		}
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);

		/*
		 * Add rotation offset when turret is elevated. This is just an estimated value, it's necessary because plane's gun hooks are not aligned orthogonally to the world's coordinate system
		 */
		this.mesh.chunkSetAngles("Turret1A", -orient.getYaw() - orient.getTangage() * 0.03F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Turret2B", 0.0F, orient.getTangage() + 1F, 0.0F);
		this.mesh.chunkSetAngles("Turret1B", 0.0F, orient.getTangage() + 1F, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		while (f < -180F)
			f += 360F;
		while (f > 180F)
			f -= 360F;
		if (this.isRealMode()) {
			while (this.prevA0 < -180F)
				this.prevA0 += 360F;
			while (this.prevA0 > 180F)
				this.prevA0 -= 360F;
		} else {
			this.prevA0 = f;
			return;
		}
		if (this.bNeedSetUp) {
			this.prevTime = Time.current() - 1L;
			this.bNeedSetUp = false;
			this.reflectPlaneMats();
		}
		if (f < -120F && this.prevA0 > 120F) f += 360F;
		else if (f > 120F && this.prevA0 < -120F) this.prevA0 += 360F;
		float f3 = f - this.prevA0;
		float f4 = 0.001F * (Time.current() - this.prevTime);
		float f5 = Math.abs(f3 / f4);
		if (f5 > 120F) if (f > this.prevA0) f = this.prevA0 + 120F * f4;
		else if (f < this.prevA0) f = this.prevA0 - 120F * f4;
		this.prevTime = Time.current();

		/*
		 * limit turret movement to FN turret physical limits and avoid shooting own plane
		 */
		this.suppressGunFire = false;
		if (f >= 3F && f < 11F) {
			if (f1 < 27F) this.suppressGunFire = true; // Don't shoot at own plane!
		} else if (f >= 11F && f < 38F) {
			if (f1 < -6F) this.suppressGunFire = true; // Don't shoot at own plane!
		} else if (f >= 102F && f < 173F) {
			if (f1 < -1F) this.suppressGunFire = true; // Don't shoot at own plane!
		} else if (f >= 173F || f < -168F) {
			if (f1 < 6F) this.suppressGunFire = true; // Don't shoot at own plane!
		} else if (f >= -168F && f < -91F) {
			if (f1 < 1.5F) this.suppressGunFire = true; // Don't shoot at own plane!
		} else if (f >= -25F && f < 3F) if (f1 < -6F) this.suppressGunFire = true; // Don't shoot at own plane!
		if (f1 > 75F) f1 = 75F;
		if (f1 < -20F) f1 = -20F;

		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
		this.prevA0 = f;
	}

	public void doGunFire(boolean flag) {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable || this.suppressGunFire) this.bGunFire = false;
		else this.bGunFire = flag;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			/*
			 * When entering turret position, make sure to hide external 3D model of the turret, and hide the gunner, they shall not be seen from inside the turret!
			 */
			this.aircraft().hierMesh().chunkVisible("Turret2A_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret2A1_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
			this.aircraft().hierMesh().chunkVisible("Pilot5_D0", false);
			this.aircraft().hierMesh().chunkVisible("Head5_D0", false);
			this.aircraft().hierMesh().chunkVisible("HMASK5_D0", false);

			// When entering turret, save current tail damage state for visibility toggle
			((Sunderlandxyz) this.aircraft()).updateDamageState();
			this.lastDamageState = ((Sunderlandxyz) this.aircraft()).getTail1DamageVisible();
			this.aircraft().hierMesh().chunkVisible("Tail1_D" + this.lastDamageState, false);

			// Hide away the radar antenna and wires
			this.aircraft().hierMesh().chunkVisible("Wire_D0", false);
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		if (!this.isFocused()) return;
		else {
			/*
			 * When leaving this turret, regain visibility of the turret's external 3D model, show gunner if alive and show the plane's tail in it's appropriate damage state again!
			 */
			this.aircraft().hierMesh().chunkVisible("Turret2A_D0", true);
			this.aircraft().hierMesh().chunkVisible("Turret2A1_D0", true);
			this.aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
			this.aircraft().hierMesh().chunkVisible("Pilot5_D0", !this.aircraft().hierMesh().isChunkVisible("Pilot5_D1"));
			this.aircraft().hierMesh().chunkVisible("Head5_D0", !this.aircraft().hierMesh().isChunkVisible("Pilot5_D1"));
			this.aircraft().hierMesh().chunkVisible("Tail1_D" + this.lastDamageState, true);

			// Show radar antenna and wires again if they're part of the external 3D model in the current weapon slot
			if (!this.aircraft().thisWeaponsName.toLowerCase().startsWith("rescue")) this.aircraft().hierMesh().chunkVisible("Wire_D0", true);
			super.doFocusLeave();
		}
	}

	public void reflectWorldToInstruments(float f) {
		if (this.bNeedSetUp) {
			this.prevTime = Time.current() - 1L;
			this.bNeedSetUp = false;
			this.reflectPlaneMats();
		}
	}

	protected void reflectPlaneMats() {
		HierMesh hiermesh = this.aircraft().hierMesh();
		Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		this.mesh.materialReplace("Gloss1D0o", mat);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("Z_Holes1_D1", true);
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("Z_Holes2_D1", true);
	}

	public CockpitSunderlandTGunner() {
		super("3DO/Cockpit/SunderlandTGun/hier.him", "bf109");
		this.bNeedSetUp = true;
		this.prevTime = -1L;
		this.prevA0 = 0.0F;
		this.hook1 = null;
		this.hook2 = null;
		this.suppressGunFire = false;
	}

	private int     lastDamageState;
	private boolean bNeedSetUp;
	private long    prevTime;
	private float   prevA0;
	private Hook    hook1;
	private Hook    hook2;
	private boolean suppressGunFire;

	static {
		Property.set(CockpitSunderlandTGunner.class, "aiTuretNum", 1);
		Property.set(CockpitSunderlandTGunner.class, "weaponControlNum", 11);
		Property.set(CockpitSunderlandTGunner.class, "astatePilotIndx", 4);
	}
}
