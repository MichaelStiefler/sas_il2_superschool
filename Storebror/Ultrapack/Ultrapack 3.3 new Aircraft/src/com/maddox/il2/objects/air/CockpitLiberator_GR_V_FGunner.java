package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;

public class CockpitLiberator_GR_V_FGunner extends CockpitGunner {

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		this.mesh.chunkSetAngles("TurretA", -12F, -orient.getYaw(), 0.0F);
		this.mesh.chunkSetAngles("TurretB", 0.0F, orient.getTangage(), 0.0F);
		this.mesh.chunkSetAngles("TurretC", -12F, -this.cvt(orient.getYaw(), -17F, 17F, -17F, 17F), 0.0F);
		this.mesh.chunkSetAngles("TurretD", 0.0F, this.cvt(orient.getTangage(), -10F, 15F, -10F, 15F), 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 0.0F, 0.0F);
		else {
			float f = orient.getYaw();
			float f_0_ = orient.getTangage();
			if (f < -23F) f = -23F;
			if (f > 23F) f = 23F;
			if (f_0_ > 15F) f_0_ = 15F;
			if (f_0_ < -25F) f_0_ = -25F;
			orient.setYPR(f, f_0_, 0.0F);
			orient.wrap();
		}
	}

	protected void interpTick() {
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		}
	}

	public void doGunFire(boolean bool) {
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
			else this.bGunFire = bool;
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		}
	}

	public CockpitLiberator_GR_V_FGunner() {
		super("3DO/Cockpit/B-25J-FGun/FGunnerB24D.him", "bf109");
		this.cockpitNightMats = new String[] { "textrbm9", "texture25" };
		this.setNightMats(false);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("XGlassDamage1", true);
		if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
		if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
		if ((this.fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("XGlassDamage3", true);
		if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("XHullDamage1", true);
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("XHullDamage2", true);
	}

	public void reflectWorldToInstruments(float f) {
		this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
		this.mesh.chunkSetAngles("zSpeed1", 0.0F, this.floatindex(this.cvt(this.fm.getSpeedKMH(), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
		this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt((float) this.fm.Loc.z, 0.0F, 9144F, 0.0F, 10800F), 0.0F);
		this.mesh.chunkSetAngles("zAlt2", 0.0F, this.cvt((float) this.fm.Loc.z, 0.0F, 9144F, 0.0F, 1080F), 0.0F);
		this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
		this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		this.mesh.chunkSetAngles("zSecond", 0.0F, this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
		this.mesh.chunkSetAngles("zCompass1", 0.0F, this.fm.Or.getAzimut(), 0.0F);
		WayPoint waypoint = this.fm.AP.way.curr();
		if (waypoint != null) {
			waypoint.getP(P1);
			V.sub(P1, this.fm.Loc);
			float f_2_ = (float) (57.295779513082323D * Math.atan2(V.x, V.y));
			this.mesh.chunkSetAngles("zCompass2", 0.0F, 90F + f_2_, 0.0F);
		}
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) this.setNightMats(true);
		else this.setNightMats(false);
	}

	private static final float speedometerScale[] = { 0.0F, 2.5F, 54F, 104F, 154.5F, 205.5F, 224F, 242F, 259.5F, 277.5F, 296.25F, 314F, 334F, 344.5F };
	private static Point3d     P1                 = new Point3d();
	private static Vector3d    V                  = new Vector3d();

	static {
		Property.set(CockpitLiberator_GR_V_FGunner.class, "aiTuretNum", 0);
		Property.set(CockpitLiberator_GR_V_FGunner.class, "weaponControlNum", 10);
		Property.set(CockpitLiberator_GR_V_FGunner.class, "astatePilotIndx", 2);
	}
}
