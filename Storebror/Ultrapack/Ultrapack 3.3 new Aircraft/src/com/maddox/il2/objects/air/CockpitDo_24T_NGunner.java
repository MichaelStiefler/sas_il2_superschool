package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class CockpitDo_24T_NGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = -orient.getYaw();
		float f1 = orient.getTangage();
		this.mesh.chunkSetAngles("Turret1A", 0.0F, f, 0.0F);
		this.mesh.chunkSetAngles("Turret1B", 0.0F, f1, 0.0F);
		this.mesh.chunkSetAngles("Turret1C", 0.0F, -f, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (this.isRealMode()) {
			float f = orient.getYaw();
			float f1 = orient.getTangage();
			if (f > 110F) f = 110F;
			if (f < -110F) f = -110F;
			if (f1 > 110F) f1 = 110F;
			if (f1 < -110F) f1 = -110F;
			orient.setYPR(f, f1, 0.0F);
			orient.wrap();
		}
	}

	protected void interpTick() {
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
			if (this.bGunFire) {
				if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN01");
				this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN01");
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

	public CockpitDo_24T_NGunner() {
		super("3DO/Cockpit/Do_24T_NGun/hier.him", "i16");
		this.setbNeedSetUp(true);
		this.hook1 = null;
		BulletEmitter abulletemitter[] = this.aircraft().FM.CT.Weapons[this.weaponControlNum()];
		if (abulletemitter != null) for (int i = 0; i < abulletemitter.length; i++)
			if (abulletemitter[i] instanceof Actor) ((Actor) abulletemitter[i]).visibilityAsBase(false);
	}

	public boolean isbNeedSetUp() {
		return this.bNeedSetUp;
	}

	public void setbNeedSetUp(boolean bNeedSetUp) {
		this.bNeedSetUp = bNeedSetUp;
	}

	private boolean bNeedSetUp;
	private Hook    hook1;

	static {
		Property.set(CockpitDo_24T_NGunner.class, "aiTuretNum", 0);
		Property.set(CockpitDo_24T_NGunner.class, "weaponControlNum", 10);
		Property.set(CockpitDo_24T_NGunner.class, "astatePilotIndx", 1);
	}
}
