package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

/*
 * Tail Gunner for Sunderland Mk.II by Barnesy/CWatson/Freemodding
 * Latest edit: 2016-03-15 by SAS~Storebror
 */
public class CockpitSunderlandAGunner extends CockpitGunner {

	protected void interpTick() {
		if (!this.isRealMode()) return;
		if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) {
			if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN04");
			this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN04");
			if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN05");
			this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN05");
			if (this.hook3 == null) this.hook3 = new HookNamed(this.aircraft(), "_MGUN06");
			this.doHitMasterAircraft(this.aircraft(), this.hook3, "_MGUN06");
			if (this.hook4 == null) this.hook4 = new HookNamed(this.aircraft(), "_MGUN07");
			this.doHitMasterAircraft(this.aircraft(), this.hook4, "_MGUN07");
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

		super.moveGun(new Orient(orient.getYaw(), -orient.getTangage(), orient.getKren()));
		/*
		 * Add rotation offset when turret is elevated and elevation offset when turret is rotated. These are just estimated values, they're necessary because plane's gun hooks are not aligned orthogonally to the world's coordinate system
		 */
		this.mesh.chunkSetAngles("TurretA", 0.0F, -orient.getYaw() * this.cvt(orient.getTangage(), -45F, 60F, 1.035F, 0.96F), 0.0F);
		this.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage() + Math.abs(orient.getYaw()) / 35F, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 2.3F, 0.0F);
		else {
			float f = orient.getYaw();
			float f1 = orient.getTangage();
			/*
			 * limit turret movement to FN turret physical limits
			 */
			if (f < -60F) f = -60F;
			if (f > 60F) f = 60F;
			if (f1 > 60F) f1 = 60F;
			if (f1 < -45F) f1 = -45F;
			orient.setYPR(f, f1, 0.0F);
			orient.wrap();
		}
	}

	public void doGunFire(boolean flag) {
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
			else this.bGunFire = flag;
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		}
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			/*
			 * When entering turret position, make sure to hide external 3D model of the turret, hide the gunner and hide the plane's tail, they shall not be seen from inside the turret!
			 */
			this.aircraft().hierMesh().chunkVisible("Turret3A_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret3A1_D0", false);
			this.aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
			this.aircraft().hierMesh().chunkVisible("Pilot6_D0", false);
			this.aircraft().hierMesh().chunkVisible("Head6_D0", false);
			this.aircraft().hierMesh().chunkVisible("HMASK6_D0", false);

			// When entering turret, save current tail damage state for visibility toggle
			((Sunderlandxyz) this.aircraft()).updateDamageState();
			this.lastDamageState = ((Sunderlandxyz) this.aircraft()).getTail1DamageVisible();
			this.aircraft().hierMesh().chunkVisible("Tail1_D" + this.lastDamageState, false);
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		if (!this.isFocused()) return;
		else {
			/*
			 * When leaving this turret, regain visibility of the turret's external 3D model, show gunner if alive and show the plane's tail in it's appropriate damage state again!
			 */
			this.aircraft().hierMesh().chunkVisible("Turret3A_D0", true);
			this.aircraft().hierMesh().chunkVisible("Turret3A1_D0", true);
			this.aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
			this.aircraft().hierMesh().chunkVisible("Pilot6_D0", !this.aircraft().hierMesh().isChunkVisible("Pilot6_D1"));
			this.aircraft().hierMesh().chunkVisible("Head6_D0", !this.aircraft().hierMesh().isChunkVisible("Pilot6_D1"));
			this.aircraft().hierMesh().chunkVisible("Tail1_D" + this.lastDamageState, true);
			super.doFocusLeave();
			return;
		}
	}

	protected void reflectPlaneMats() {
		// Apply plane skin to turret frame parts
		HierMesh hiermesh = this.aircraft().hierMesh();
		Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		this.mesh.materialReplace("Gloss1D0o", mat);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("XGlassDamage1", true);
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
	}

	public CockpitSunderlandAGunner() {
		super("3DO/Cockpit/SunderlandAGun/hier.him", "bf109");
		this.hook1 = null;
		this.hook2 = null;
		this.hook3 = null;
		this.hook4 = null;
	}

	private Hook hook1;
	private Hook hook2;
	private Hook hook3;
	private Hook hook4;
	private int  lastDamageState;

	static {
		Property.set(CockpitSunderlandAGunner.class, "aiTuretNum", 2);
		Property.set(CockpitSunderlandAGunner.class, "weaponControlNum", 12);
		Property.set(CockpitSunderlandAGunner.class, "astatePilotIndx", 5);
	}
}
