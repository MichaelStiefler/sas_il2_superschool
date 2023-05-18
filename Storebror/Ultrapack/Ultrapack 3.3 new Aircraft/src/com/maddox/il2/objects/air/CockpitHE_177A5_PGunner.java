package com.maddox.il2.objects.air;

import com.maddox.JGP.Geom;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_177A5_PGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("Body", 0.0F, 0.0F, -1F);
		float f = orient.getYaw();
		f -= 1.5F * Math.sin(Geom.DEG2RAD(f));
		float f1 = orient.getTangage();
		f1 -= 1.5F * Math.cos(Geom.DEG2RAD(f));
		this.mesh.chunkSetAngles("Turret1A", -f, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Turret1B", 0.0F, f1, 0.0F);
		this.aircraft().hierMesh().chunkSetAngles("Pilot4_D0", -f, 0.0F, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		float f = orient.getYaw();
		float f1 = orient.getTangage() + GUN_OFFSET_TANGAGE;
		while (f < -180F)
			f += 360F;
		while (f > 180F)
			f -= 360F;
		while (this.prevA0 < -180F)
			this.prevA0 += 360F;
		while (this.prevA0 > 180F)
			this.prevA0 -= 360F;
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
			float minf1 = 0F;
			float maxf1 = 87F;
			if (f1 > maxf1) f1 = maxf1;
			if (f1 < minf1) f1 = minf1;
			float f2 = Math.abs(f);
			if (f2 < 10F) minf1 = Math.max(minf1, 15F * (float) Math.cos(Math.PI * f2 / 20F));
			else if (f2 > 65F && f2 < 85F) minf1 = Math.max(minf1, 5F * (float) Math.cos(Math.PI * (f2 - 75F) / 20F));
			else if (f2 > 170F) minf1 = Math.max(minf1, 5F * (float) Math.cos(Math.PI * (180 - f2) / 20F));
			if (f1 < minf1) {
				this.tangageOffset += minf1 - f1;
				f1 = minf1;
			} else if (this.tangageOffset < f1 - minf1) { // Offset is smaller than difference between current f1 and minf1
				f1 -= this.tangageOffset;
				this.tangageOffset = 0F;
			} else { // Offset is larger than difference between current f1 and minf1
				this.tangageOffset -= f1 - minf1;
				f1 = minf1;
			}

			orient.setYPR(f, f1 - GUN_OFFSET_TANGAGE, 0.0F);
			orient.wrap();
			this.prevA0 = f;
		}
	}

	protected void interpTick() {
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
			if (this.bGunFire) {
				if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN04");
				this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN04");
				if (this.hook2 == null) this.hook2 = new HookNamed(this.aircraft(), "_MGUN05");
				this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN05");
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

	public CockpitHE_177A5_PGunner() {
		super("3DO/Cockpit/He-177A-5-PGun/hier.him", "he111_gunner");
		this.bNeedSetUp = true;
		this.prevTime = -1L;
		this.prevA0 = 0.0F;
		this.hook1 = null;
		this.hook2 = null;
		this.tangageOffset = 0F;
	}

	private boolean              bNeedSetUp;
	private long                 prevTime;
	private float                prevA0;
	private Hook                 hook1;
	private Hook                 hook2;
	private float                tangageOffset;
	protected static final float GUN_OFFSET_TANGAGE = 15.0F;

	static {
		Class class1 = CockpitHE_177A5_PGunner.class;
		Property.set(class1, "aiTuretNum", 3);
		Property.set(class1, "weaponControlNum", 13);
		Property.set(class1, "astatePilotIndx", 3);
	}
}
