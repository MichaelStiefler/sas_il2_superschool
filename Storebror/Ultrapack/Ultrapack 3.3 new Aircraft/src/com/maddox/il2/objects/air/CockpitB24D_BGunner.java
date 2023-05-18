package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitB24D_BGunner extends CockpitGunner {

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		this.aircraft().hierMesh().chunkVisible("Turret1B_D0", this.aircraft().hierMesh().isChunkVisible("Turret1A_D0"));
		super.doFocusLeave();
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("Body", 180F, 0.0F, 180F);
		this.mesh.chunkSetAngles("BaseB24", 180F, 0.0F, 180F);
		this.mesh.chunkSetAngles("Turret1A", orient.getYaw(), 180F, 180F);
		this.mesh.chunkSetAngles("Turret1B", 180F, -orient.getTangage(), 180F);
	}

	public void clipAnglesGun(Orient orient) {
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		for (; f < -180F; f += 360F)
			;
		for (; f > 180F; f -= 360F)
			;
		for (; this.prevA0 < -180F; this.prevA0 += 360F)
			;
		for (; this.prevA0 > 180F; this.prevA0 -= 360F)
			;
		if (!this.isRealMode()) this.prevA0 = f;
		else {
			if (this.bNeedSetUp) {
				this.prevTime = Time.current() - 1L;
				this.bNeedSetUp = false;
			}
			if (f < -120F && this.prevA0 > 120F) f += 360F;
			else if (f > 120F && this.prevA0 < -120F) this.prevA0 += 360F;
			float f3 = f - this.prevA0;
			float f4 = 0.001F * (Time.current() - this.prevTime);
			float f5 = Math.abs(f3 / f4);
			if (f5 > 120F) if (f > this.prevA0) f = this.prevA0 + 120F * f4;
			else if (f < this.prevA0) f = this.prevA0 - 120F * f4;
			this.prevTime = Time.current();
			if (f1 > 0.0F) f1 = 0.0F;
			if (f1 < -85F) f1 = -85F;
			orient.setYPR(f, f1, 0.0F);
			orient.wrap();
			this.prevA0 = f;
		}
	}

	protected void interpTick() {
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
			if (this.bGunFire) {
				if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN07");
				this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN07");
				if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN08");
				this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN08");
			}
		}
	}

	public void doGunFire(boolean flag) {
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
			else this.bGunFire = flag;
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		}
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("Z_Holes1_D1", true);
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("Z_Holes2_D1", true);
	}

	public CockpitB24D_BGunner() {
		super("3DO/Cockpit/A-20G-TGun/BGunnerB24.him", "he111_gunner");
		this.bNeedSetUp = true;
		this.prevTime = -1L;
		this.prevA0 = 0.0F;
		this.hook1 = null;
		this.hook2 = null;
	}

	private boolean bNeedSetUp;
	private long    prevTime;
	private float   prevA0;
	private Hook    hook1;
	private Hook    hook2;

	static {
		Property.set(CockpitB24D_BGunner.class, "aiTuretNum", 2);
		Property.set(CockpitB24D_BGunner.class, "weaponControlNum", 12);
		Property.set(CockpitB24D_BGunner.class, "astatePilotIndx", 8);
	}
}
