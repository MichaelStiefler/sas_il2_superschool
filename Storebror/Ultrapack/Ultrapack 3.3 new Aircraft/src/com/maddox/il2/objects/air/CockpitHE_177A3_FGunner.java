package com.maddox.il2.objects.air;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Message;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_177A3_FGunner extends CockpitGunner {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (CockpitHE_177A3_FGunner.this.fm != null) {
				CockpitHE_177A3_FGunner.this.setTmp = CockpitHE_177A3_FGunner.this.setOld;
				CockpitHE_177A3_FGunner.this.setOld = CockpitHE_177A3_FGunner.this.setNew;
				CockpitHE_177A3_FGunner.this.setNew = CockpitHE_177A3_FGunner.this.setTmp;
				CockpitHE_177A3_FGunner.this.setNew.AirEnemy = 0.95F * CockpitHE_177A3_FGunner.this.setOld.AirEnemy + 0.05F * (((HE_177A3) CockpitHE_177A3_FGunner.this.fm.actor).bAirEnemy ? 1.0F : 0.0F);
				float f = CockpitHE_177A3_FGunner.this.waypointAzimuth();
				if (CockpitHE_177A3_FGunner.this.useRealisticNavigationInstruments()) {
					CockpitHE_177A3_FGunner.this.setNew.waypointAzimuth.setDeg(f - 90F);
					CockpitHE_177A3_FGunner.this.setOld.waypointAzimuth.setDeg(f - 90F);
					CockpitHE_177A3_FGunner.this.setNew.radioCompassAzimuth.setDeg(CockpitHE_177A3_FGunner.this.setOld.radioCompassAzimuth.getDeg(0.02F),
							CockpitHE_177A3_FGunner.this.radioCompassAzimuthInvertMinus() - CockpitHE_177A3_FGunner.this.setOld.azimuth.getDeg(1.0F) - 90F);
				} else CockpitHE_177A3_FGunner.this.setNew.waypointAzimuth.setDeg(CockpitHE_177A3_FGunner.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitHE_177A3_FGunner.this.setOld.azimuth.getDeg(1.0F));
				CockpitHE_177A3_FGunner.this.setNew.azimuth.setDeg(CockpitHE_177A3_FGunner.this.setOld.azimuth.getDeg(1.0F), CockpitHE_177A3_FGunner.this.fm.Or.azimut());
				CockpitHE_177A3_FGunner.this.setNew.throttle1 = 0.85F * CockpitHE_177A3_FGunner.this.setOld.throttle1 + CockpitHE_177A3_FGunner.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
				CockpitHE_177A3_FGunner.this.setNew.throttle2 = 0.85F * CockpitHE_177A3_FGunner.this.setOld.throttle2 + CockpitHE_177A3_FGunner.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
				CockpitHE_177A3_FGunner.this.setNew.pictAiler = 0.85F * CockpitHE_177A3_FGunner.this.setOld.pictAiler + 0.15F * CockpitHE_177A3_FGunner.this.cvt(CockpitHE_177A3_FGunner.this.fm.CT.AileronControl, -1F, 1.0F, -1F, 1.0F);
				CockpitHE_177A3_FGunner.this.setNew.pictElev = 0.85F * CockpitHE_177A3_FGunner.this.setOld.pictElev + 0.15F * CockpitHE_177A3_FGunner.this.cvt(CockpitHE_177A3_FGunner.this.fm.CT.ElevatorControl, -1F, 1.0F, -1F, 1.0F);
				if (Math.toDegrees(CockpitHE_177A3_FGunner.this.fm.EI.engines[0].getPropPhi()) < 36D) {
					CockpitHE_177A3_FGunner.this.setNew.prop1 = 0.85F * CockpitHE_177A3_FGunner.this.setOld.prop1 + ((FlightModelMain) CockpitHE_177A3_FGunner.this.fm).EI.engines[0].getControlProp() * 0.15F;
					CockpitHE_177A3_FGunner.this.setNew._prop1 = CockpitHE_177A3_FGunner.this.cvt(CockpitHE_177A3_FGunner.this.setNew.prop1, 0.0F, 1.0F, -22F, -63F);
				} else {
					CockpitHE_177A3_FGunner.this.setNew.prop1 = 0.85F * CockpitHE_177A3_FGunner.this.setOld.prop1;
					CockpitHE_177A3_FGunner.this.setNew._prop1 = CockpitHE_177A3_FGunner.this.cvt(CockpitHE_177A3_FGunner.this.setNew.prop1, 0.0F, 1.0F, 0.0F, -63F);
				}
				if (Math.toDegrees(CockpitHE_177A3_FGunner.this.fm.EI.engines[1].getPropPhi()) < 36D) {
					CockpitHE_177A3_FGunner.this.setNew.prop2 = 0.85F * CockpitHE_177A3_FGunner.this.setOld.prop2 + ((FlightModelMain) CockpitHE_177A3_FGunner.this.fm).EI.engines[1].getControlProp() * 0.15F;
					CockpitHE_177A3_FGunner.this.setNew._prop2 = CockpitHE_177A3_FGunner.this.cvt(CockpitHE_177A3_FGunner.this.setNew.prop2, 0.0F, 1.0F, -22F, -63F);
				} else {
					CockpitHE_177A3_FGunner.this.setNew.prop2 = 0.85F * CockpitHE_177A3_FGunner.this.setOld.prop2;
					CockpitHE_177A3_FGunner.this.setNew._prop2 = CockpitHE_177A3_FGunner.this.cvt(CockpitHE_177A3_FGunner.this.setNew.prop2, 0.0F, 1.0F, 0.0F, -63F);
				}
				CockpitHE_177A3_FGunner.this.w.set(CockpitHE_177A3_FGunner.this.fm.getW());
				CockpitHE_177A3_FGunner.this.fm.Or.transform(CockpitHE_177A3_FGunner.this.w);
				CockpitHE_177A3_FGunner.this.setNew.turn = (12F * CockpitHE_177A3_FGunner.this.setOld.turn + CockpitHE_177A3_FGunner.this.w.z) / 13F;
				CockpitHE_177A3_FGunner.this.setNew.altimeter = 0.85F * CockpitHE_177A3_FGunner.this.setOld.altimeter + CockpitHE_177A3_FGunner.this.fm.getAltitude() * 0.15F;
				CockpitHE_177A3_FGunner.this.setNew.vspeed = (99F * CockpitHE_177A3_FGunner.this.setOld.vspeed + CockpitHE_177A3_FGunner.this.fm.getVertSpeed()) / 100F;
				float f1 = CockpitHE_177A3_FGunner.this.fm.Or.getKren();
				float f2 = CockpitHE_177A3_FGunner.this.fm.Or.getTangage();
				if (f1 > 55F || f1 < -55F || f2 < -55F || f2 > 55F) CockpitHE_177A3_FGunner.this.Pn.z = 250D;
				else {
					CockpitHE_177A3_FGunner.this.Pn.set(CockpitHE_177A3_FGunner.this.fm.Loc);
					CockpitHE_177A3_FGunner.this.Pn.z = CockpitHE_177A3_FGunner.this.fm.getAltitude() - Engine.cur.land.HQ(((Tuple3d) CockpitHE_177A3_FGunner.this.Pn).x, ((Tuple3d) CockpitHE_177A3_FGunner.this.Pn).y);
					double d = CockpitHE_177A3_FGunner.this.Pn.z * (float) Math.tan(Geom.DEG2RAD(f1));
					double d1 = CockpitHE_177A3_FGunner.this.Pn.z * (float) Math.tan(Geom.DEG2RAD(f2));
					CockpitHE_177A3_FGunner.this.Pn.z = (float) Math.sqrt(d * d + d1 * d1 + CockpitHE_177A3_FGunner.this.Pn.z * CockpitHE_177A3_FGunner.this.Pn.z);
					if (CockpitHE_177A3_FGunner.this.fm.CT.getGear() > 0.5F) CockpitHE_177A3_FGunner.this.Pn.z = CockpitHE_177A3_FGunner.this.cvt((float) CockpitHE_177A3_FGunner.this.Pn.z, 0.0F, 150F, 0.0F, 250F);
					else CockpitHE_177A3_FGunner.this.Pn.z = CockpitHE_177A3_FGunner.this.cvt((float) CockpitHE_177A3_FGunner.this.Pn.z, 0.0F, 750F, 0.0F, 250F);
				}
				CockpitHE_177A3_FGunner.this.setNew.AFN101 = 0.9F * CockpitHE_177A3_FGunner.this.setOld.AFN101 + 0.1F * (float) CockpitHE_177A3_FGunner.this.Pn.z;
				CockpitHE_177A3_FGunner.this.setNew.beaconDirection = (10F * CockpitHE_177A3_FGunner.this.setOld.beaconDirection + CockpitHE_177A3_FGunner.this.getBeaconDirection()) / 11F;
				CockpitHE_177A3_FGunner.this.setNew.beaconRange = (10F * CockpitHE_177A3_FGunner.this.setOld.beaconRange + CockpitHE_177A3_FGunner.this.getBeaconRange()) / 11F;
				CockpitHE_177A3_FGunner.this.setNew.pictManf1 = 0.9F * CockpitHE_177A3_FGunner.this.setOld.pictManf1 + 0.1F * CockpitHE_177A3_FGunner.this.cvt(CockpitHE_177A3_FGunner.this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
				CockpitHE_177A3_FGunner.this.setNew.pictManf2 = 0.8F * CockpitHE_177A3_FGunner.this.setOld.pictManf2 + 0.2F * CockpitHE_177A3_FGunner.this.cvt(CockpitHE_177A3_FGunner.this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
				CockpitHE_177A3_FGunner.this.setNew.pictManf3 = 0.9F * CockpitHE_177A3_FGunner.this.setOld.pictManf3 + 0.1F * CockpitHE_177A3_FGunner.this.cvt(CockpitHE_177A3_FGunner.this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
				CockpitHE_177A3_FGunner.this.setNew.pictManf4 = 0.8F * CockpitHE_177A3_FGunner.this.setOld.pictManf4 + 0.2F * CockpitHE_177A3_FGunner.this.cvt(CockpitHE_177A3_FGunner.this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
				float f3 = CockpitHE_177A3_FGunner.this.setNew.prevFuel - CockpitHE_177A3_FGunner.this.fm.M.fuel;
				CockpitHE_177A3_FGunner.this.setNew.prevFuel = CockpitHE_177A3_FGunner.this.fm.M.fuel;
				f3 /= 0.72F;
				f3 /= Time.tickLenFs();
				f3 *= 3600F;
				CockpitHE_177A3_FGunner.this.setNew.cons = 0.9F * CockpitHE_177A3_FGunner.this.setOld.cons + 0.1F * f3;
				float f4 = CockpitHE_177A3_FGunner.this.fm.EI.engines[0].getEngineForce().x;
				float f5 = CockpitHE_177A3_FGunner.this.fm.EI.engines[1].getEngineForce().x;
				float f6 = CockpitHE_177A3_FGunner.this.setNew.cons;
				CockpitHE_177A3_FGunner.this.setNew.consumptionL = 0.9F * CockpitHE_177A3_FGunner.this.setOld.consumptionL + 0.1F * (f6 * f4) / (f4 + f5 + 1.0F);
				CockpitHE_177A3_FGunner.this.setNew.consumptionR = 0.9F * CockpitHE_177A3_FGunner.this.setOld.consumptionR + 0.1F * (f6 * f5) / (f4 + f5 + 1.0F);
				CockpitHE_177A3_FGunner.this.setNew.bombDoor = 0.9F * CockpitHE_177A3_FGunner.this.setOld.bombDoor + 0.1F * CockpitHE_177A3_FGunner.this.fm.CT.getBayDoor();
			}
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float      throttle1;
		float      throttle2;
		float      pictAiler;
		float      pictElev;
		float      prop1;
		float      _prop1;
		float      prop2;
		float      _prop2;
		float      turn;
		float      altimeter;
		float      vspeed;
		float      AFN101;
		float      beaconDirection;
		float      beaconRange;
		float      pictManf1;
		float      pictManf2;
		float      pictManf3;
		float      pictManf4;
		float      prevFuel;
		float      cons;
		float      consumptionL;
		float      consumptionR;
		float      bombDoor;
		float      AirEnemy;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		AnglesFork radioCompassAzimuth;

		private Variables() {
			this.azimuth = new AnglesFork();
			this.waypointAzimuth = new AnglesFork();
			this.radioCompassAzimuth = new AnglesFork();
		}

	}

	protected float waypointAzimuth() {
		return this.waypointAzimuthInvertMinus(30F);
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			this.bEntered = true;
			HierMesh hiermesh = this.aircraft().hierMesh();
			hiermesh.chunkVisible("Interior1_D0", false);
			this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", false);
			this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", false);
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		if (this.bEntered) this.bEntered = false;
		HierMesh hiermesh = this.aircraft().hierMesh();
		hiermesh.chunkVisible("Interior1_D0", true);
		this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot4_D0"));
		this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot4_D1"));
		this.aircraft().hierMesh().chunkVisible("HMask4_D0", this.aircraft().hierMesh().isChunkVisible("Pilot4_D0"));
		super.doFocusLeave();
	}

	public void moveGun(Orient orient) {
		super.moveGun(orient);
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		float f2 = Geom.DEG2RAD(f);
		float f3 = Geom.DEG2RAD(f1);
		float f4 = (float) Math.asin(Math.tan(f2) * Math.cos(f3) * Math.cos(f2));
		float f5 = (float) Math.atan(Math.tan(f3) / Math.cos(f2));
		float f6 = Geom.RAD2DEG(f4);
		float f7 = Geom.RAD2DEG(f5);
		this.mesh.chunkSetAngles("Z_Turret151A", 0.0F, f6, 0.0F);
		this.mesh.chunkSetAngles("Z_Turret151B", 0.0F, 0.0F, f7);
	}

	public void clipAnglesGun(Orient orient) {
		if (this.isRealMode()) if (!this.aiTurret().bIsOperable) orient.setYPR(0.0F, 0.0F, 0.0F);
		else {
			float f = orient.getYaw();
			float f1 = orient.getTangage();
			if (f >= 0.0F) {
				if (f1 >= -1.7F) {
					if (f > 15F) f = 15F;
					if (f1 > 7F) f1 = 7F;
				} else if (f1 < -1.7F && f1 > -14.39F) {
					if (f > this.cvt(f1, -14.39F, -1.7F, 5.5F, 15F)) f = this.cvt(f1, -14.39F, -1.7F, 5.5F, 15F);
				} else {
					if (f > 5.5F) f = 5.5F;
					if (f1 < -40F) f1 = -40F;
				}
			} else if (f1 >= -20.29F) {
				if (f < -15F) f = -15F;
				if (f1 > 7F) f1 = 7F;
			} else if (f1 > -22.9F && f1 < -20.29F) {
				if (f < this.cvt(f1, -22.9F, -20.29F, -6.5F, -15F)) f = this.cvt(f1, -22.9F, -20.29F, -6.5F, -15F);
			} else {
				if (f < -6.5F) f = -6.5F;
				if (f1 < -40F) f1 = -40F;
			}
			orient.setYPR(f, f1, 0.0F);
			orient.wrap();
		}
	}

	protected void interpTick() {
		if (this.bNeedSetUp) {
			this.reflectPlaneMats();
			this.bNeedSetUp = false;
		}
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		}
	}

	public void doGunFire(boolean flag) {
		if (this.isRealMode()) {
			if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
			else this.bGunFire = flag;
			this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		}
	}

	public void reflectWorldToInstruments(float f) {
		if (this.gun != null) {
			this.resetYPRmodifier();
			this.countBullets = this.gun.countBullets();
			Cockpit.xyz[0] = -this.cvt(this.countBullets, 0.0F, 1000F, 0.054F, 0.0F);
			this.mesh.chunkSetLocate("Z_LSK2A", Cockpit.xyz, Cockpit.ypr);
			this.resetYPRmodifier();
		}
		this.mesh.chunkSetAngles("Z_Lotfe7", ((HE_177A3) this.fm.actor).fSightCurSideslip * -10F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_BMBThrot1", 70F * this.setNew.throttle1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_BMBThrot2", 70F * this.setNew.throttle1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_BMBThrot3", 70F * this.setNew.throttle2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_BMBThrot4", 70F * this.setNew.throttle2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Gears", this.fm.CT.getGear() != 1.0F || this.fm.CT.getGear() != 1.0F || !this.fm.Gears.lgear || !this.fm.Gears.rgear ? 0.0F : 90F, 0.0F, 0.0F);
		float f1 = this.fm.CT.getFlap();
		float f2 = 0.0F;
		if (f1 > 0.5F) f2 = 90F;
		else if (f1 > 0.1F && f1 < 0.5F) f2 = 45F;
		this.mesh.chunkSetAngles("Z_Flaps", f2, 0.0F, 0.0F);
		this.mesh.chunkVisible("F_EngOverheatL", this.fm.AS.astateEngineStates[0] > 4);
		this.mesh.chunkVisible("F_EngOverheatR", this.fm.AS.astateEngineStates[1] > 4);
		boolean flag = false;
		if (!this.fm.CT.bHasFlapsControl) flag = true;
		else {
			float f3 = Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH());
			if (f1 > 0.21F && f3 > 270F && (f3 - 270F) * f1 > 8F) flag = true;
		}
		this.mesh.chunkVisible("F_Fl32558", flag);
		if (this.setNew.pictElev < 0.0F) this.mesh.chunkSetAngles("Z_CColumn", 0.0F, 15F * -this.setNew.pictElev, 0.0F);
		else this.mesh.chunkSetAngles("Z_CColumn", 0.0F, 15F * -this.setNew.pictElev, 0.0F);
		this.mesh.chunkSetAngles("Z_Handle", 93F * this.setNew.pictAiler, 0.0F, 0.0F);
		float f4 = this.fm.CT.getRudder();
		this.mesh.chunkSetAngles("Z_RichagL", -35F * f4, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Tiaga4L", 35F * f4, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_RichagR", 35F * f4, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Koromislo1L", 0.0F, 0.0F, 22F * f4);
		this.mesh.chunkSetAngles("Z_Koromislo2L", 0.0F, 0.0F, 22F * f4);
		this.mesh.chunkSetAngles("Z_PedalBomL", 0.0F, 0.0F, -22F * f4);
		this.mesh.chunkSetAngles("Z_Koromislo1R", 0.0F, 0.0F, -22F * f4);
		this.mesh.chunkSetAngles("Z_Koromislo2R", 0.0F, 0.0F, -22F * f4);
		this.mesh.chunkSetAngles("Z_PedalBomR", 0.0F, 0.0F, 22F * f4);
		float f5 = this.fm.CT.getBrake();
		float f6 = this.fm.CT.getBrake();
		float f7 = 20F * f5;
		float f8 = 106.3657F - f7;
		double d = Math.cos(Geom.DEG2RAD(f8));
		float f9 = Geom.RAD2DEG((float) Math.acos((0.038666129112243652D - 0.18320585787296295D * d) / Math.sqrt(0.035059455782175064D - 0.014167722314596176D * d)));
		float f10 = 180F - f8 - f9;
		float f11 = 62.8136F - f9;
		float f12 = f10 - 10.82074F;
		this.mesh.chunkSetAngles("Z_Kachalka2L", f7, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Tiaga3L", f11, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_CilindrL", 0.0F, f12, 0.0F);
		float f13 = 20F * f6;
		double d1 = Math.cos(Geom.DEG2RAD(142.0612F + f7));
		double d2 = Math.sqrt(0.0040516676381230354D - 0.003931721206754446D * d1);
		float f14 = Geom.RAD2DEG((float) Math.acos((0.039198901504278183D - 0.050150908529758453D * d1) / d2));
		float f15 = 21.38197F - f14;
		float f16 = 89.43449F + f15 + 35F * f4;
		double d3 = Math.sqrt(0.035435102880001068D + d2 * d2 - 0.37648427486419678D * d2 * (float) Math.cos(Geom.DEG2RAD(f16)));
		float f17 = Geom.RAD2DEG((float) Math.acos((0.042587004601955414D - d3 * d3) / 0.031839244067668915D));
		float f18 = 89.43554F - f17;
		this.mesh.chunkSetAngles("Z_Tiaga2L", f18, 0.0F, 0.0F);
		float f19 = (float) Math.acos((0.035435102880001068D + d3 * d3 - d2 * d2) / (0.37648427486419678D * d3));
		float f20 = (float) Math.acos((0.0071521135978400707D + d3 * d3 - 0.035434890538454056D) / (0.16914033889770508D * d3));
		float f21 = Geom.RAD2DEG(f19 + f20) - 90.56514F;
		this.mesh.chunkSetAngles("Z_LBrake", 0.0F, 0.0F, f21);
		double d4 = Math.cos(Geom.DEG2RAD(142.0612F + f13));
		double d5 = Math.sqrt(0.0040516676381230354D - 0.003931721206754446D * d4);
		float f22 = Geom.RAD2DEG((float) Math.acos((0.039198901504278183D - 0.050150908529758453D * d4) / d5));
		float f23 = 21.38197F - f22;
		float f24 = 89.43449F + f23 - 35F * f4;
		double d6 = Math.sqrt(0.035435102880001068D + d5 * d5 - 0.37648427486419678D * d5 * (float) Math.cos(Geom.DEG2RAD(f24)));
		float f25 = Geom.RAD2DEG((float) Math.acos((0.042587004601955414D - d6 * d6) / 0.031839244067668915D));
		float f26 = 89.43554F - f25;
		this.mesh.chunkSetAngles("Z_Tiaga2R", f26, 0.0F, 0.0F);
		float f27 = (float) Math.acos((0.035435102880001068D + d6 * d6 - d5 * d5) / (0.37648427486419678D * d6));
		float f28 = (float) Math.acos((0.0071521135978400707D + d6 * d6 - 0.035434890538454056D) / (0.16914033889770508D * d6));
		float f29 = Geom.RAD2DEG(f27 + f28) - 90.56514F;
		this.mesh.chunkSetAngles("Z_RBrake", 0.0F, 0.0F, f29);
		this.mesh.chunkSetAngles("Z_PropPitch1", -this.setNew._prop1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_PropPitch2", -this.setNew._prop2, 0.0F, 0.0F);
		float f30 = (float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin());
		f30 = (int) (f30 / 0.2F) * 0.2F;
		this.mesh.chunkSetAngles("Z_N_Clock1_M", f30 * 60F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Clock1_H", f30 * 5F, 0.0F, 0.0F);
		float f31 = (float) Math.toDegrees(this.fm.EI.engines[1].getPropPhi() - this.fm.EI.engines[1].getPropPhiMin());
		f31 = (int) (f31 / 0.2F) * 0.2F;
		this.mesh.chunkSetAngles("Z_N_Clock2_M", f31 * 60F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Clock2_H", f31 * 5F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_BombRelease", this.cvt(this.setNew.bombDoor, 0.0F, 1.0F, 0.0F, 90F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Clock3_H", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Clock3_M", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Clock3_S", -this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_OxPress2", -200F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_OxPress3", -200F, 0.0F, 0.0F);
		float f32 = this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
		this.mesh.chunkSetAngles("Z_N_Turn2", f32, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AirSpeed2", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 750F, 0.0F, 15F), IAS_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Alti2", -this.cvt(this.setNew.altimeter, 0.0F, 20000F, 0.0F, 720F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Climb1", -this.cvt(this.setNew.vspeed, -15F, 15F, -136F, 136F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Climb2", -this.cvt(this.setNew.vspeed, -15F, 15F, -136F, 136F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_RadioAltim", -this.setNew.AFN101, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Ln28330b", this.cvt(this.fm.CT.getGear(), 0.4F, 0.6F, 40.2F, 0.0F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Nav1", this.cvt(this.setNew.beaconDirection, -45F, 45F, -16F, 16F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Nav2", this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, -14.5F, 20F), 0.0F, 0.0F);
		this.mesh.chunkVisible("F_N_AFN2_RED", this.isOnBlindLandingMarker());
		this.mesh.chunkSetAngles("Z_N_Nav1C", this.cvt(this.setNew.beaconDirection, -45F, 45F, -16F, 16F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Nav2C", this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, -14.5F, 20F), 0.0F, 0.0F);
		this.mesh.chunkVisible("F_N_AFN2C_RED", this.isOnBlindLandingMarker());
		this.mesh.chunkSetAngles("Z_N_AiFuePress1", -this.setNew.pictManf1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AiFuePress2", -this.setNew.pictManf2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AiFuePress3", -this.setNew.pictManf3, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AiFuePress4", -this.setNew.pictManf4, 0.0F, 0.0F);
		float f33 = ((FlightModelMain) this.fm).EI.engines[0].getRPM();
		this.mesh.chunkSetAngles("Z_N_RPM1", -this.floatindex(this.cvt(f33, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_RPM2", -this.floatindex(this.cvt(f33, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
		float f34 = ((FlightModelMain) this.fm).EI.engines[1].getRPM();
		this.mesh.chunkSetAngles("Z_N_RPM3", -this.floatindex(this.cvt(f34, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_RPM4", -this.floatindex(this.cvt(f34, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_FuelConsom", -this.cvt(0.5F * this.setNew.consumptionL, 0.0F, 500F, 0.0F, 255.5F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_FuelQuant", -this.cvt(this.fm.M.fuel, 0.0F, this.fm.M.maxFuel, 37F, 84F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E1CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E2CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E3CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E4CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E1OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E2OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E3OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E4OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AirPress1", -170F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AirPress2", -170F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AirPress3", -170F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E1_FuelPres", -this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E2_FuelPres", -this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E3_FuelPres", -this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E4_FuelPres", -this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E1_OilPress", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E2_OilPress", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E3_OilPress", this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_E4_OilPress", this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_LandingLight", this.fm.AS.bLandingLightOn ? 90F : 0.0F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_NavLights", this.fm.AS.bNavLightsOn ? 90F : 0.0F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_CockpitLight", this.cockpitLightControl ? 90F : 0.0F, 0.0F, 0.0F);
		if (this.useRealisticNavigationInstruments()) {
			this.mesh.chunkSetAngles("Fl22334b", -(this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f)), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Fl22334c", this.setNew.waypointAzimuth.getDeg(f) + 90F, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Fl22338b", -(this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f)), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Fl22338c", this.setNew.waypointAzimuth.getDeg(f) + 90F, 0.0F, 0.0F);
		} else {
			this.mesh.chunkSetAngles("Fl22334b", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Fl22334c", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Fl22338b", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Fl22338c", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
		}
		this.mesh.chunkSetAngles("Z_Course2a", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
//        mesh.chunkSetAngles("Z_Course2b", ((HE_177A3)aircraft()).getCourse(), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_MG81B", 0.0F, this.fm.turret[0].tu[0], 0.0F);
		this.mesh.chunkSetAngles("Z_MG81A", 0.0F, this.fm.turret[0].tu[1], 0.0F);
		float f35;
		for (f35 = -this.fm.turret[3].tu[0]; f35 < -180F; f35 += 360F)
			;
		for (; f35 > 180F; f35 -= 360F)
			;
		float f36 = this.fm.turret[3].tu[1];
		this.mesh.chunkSetAngles("Z_TurretA", -f35, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_TurretB", 0.0F, 0.0F, -f36);
		this.mesh.chunkSetAngles("Z_tLink", 0.0F, 0.0F, this.floatindex(this.cvt(f36, 0.0F, 90F, 0.0F, 18F), Turret_Z1));
		this.mesh.chunkSetAngles("Z_tHandle", 0.0F, 0.0F, -this.floatindex(this.cvt(f36, 0.0F, 90F, 0.0F, 18F), Turret_Z2));
		this.mesh.chunkSetAngles("Z_tReviVal", 0.0F, -f35 * 5F, 0.0F);
		this.CalculateRevi(f35, f36);
		this.resetYPRmodifier();
		Cockpit.xyz[2] = 0.185F * this.setNew.AirEnemy;
		this.mesh.chunkSetLocate("Z_TurretA_Seat", Cockpit.xyz, Cockpit.ypr);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("xGlassDm1", true);
		if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("xHullDm1", true);
		if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
			this.mesh.chunkVisible("xGlass1", false);
			this.mesh.chunkVisible("xGlass1_dmg", true);
		}
		if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("xGlassDm2", true);
		if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
			this.mesh.chunkVisible("xGlass3", false);
			this.mesh.chunkVisible("xGlass3_dmg", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
			this.mesh.chunkVisible("xGlass4", false);
			this.mesh.chunkVisible("xGlass4_dmg", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
			this.mesh.chunkVisible("xGlass5", false);
			this.mesh.chunkVisible("xGlass5_dmg", true);
		}
		if (!this.cockpitLightControl) {
			this.setNightMats(true);
			this.setNightMats(false);
		}
	}

	protected void reflectPlaneMats() {
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) this.setNightMats(true);
		else this.setNightMats(false);
	}

	public CockpitHE_177A3_FGunner() {
		super("3DO/Cockpit/He-177A-3-FGun/hier.him", "he111_gunner");
		this.setOld = new Variables();
		this.setNew = new Variables();
		this.w = new Vector3f();
		this.bNeedSetUp = true;
		this.bEntered = false;
		this.gun = null;
		this.countBullets = 0;
		this.w = new Vector3f();
		this.Pn = new Point3d();
		this.cockpitNightMats = new String[] { "CompGrad", "Fl20274", "Fl20342na", "Fl20342", "Fl20516", "Fl20556", "Fl20570", "Fl20572", "Fl20723_200na", "Fl20723_200", "Fl20841", "Fl22231", "Fl22316", "Fl22334b", "Fl22334c", "Fl22382", "Fl22412",
				"Fl22561", "Fl23885na", "Fl23885", "Fl30489", "Fl30532", "Fl32336", "Gauge20", "Ln27002", "Ln28330b", "Ln28330", "NeedlesnLights", "Nr92182B1na", "Voltmeters" };
//		this.hidePilot = true;
		if (this.gun == null) {
			this.gun = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN03");
			this.countBullets = this.gun.countBullets();
		}
		this.setNightMats(false);
		this.interpPut(new Interpolater(), (Object) null, Time.current(), (Message) null);
	}

	public void CalculateRevi(float f, float f1) {
		((HE_177A3) this.fm.actor).CalculateRevi(-f, f1);
		float f2 = ((HE_177A3) this.fm.actor).getB1_HeadTangage();
		float f3 = ((HE_177A3) this.fm.actor).getB1_HeadYaw();
		float f4 = ((HE_177A3) this.fm.actor).getB1_HeadTangage1();
		float f5 = ((HE_177A3) this.fm.actor).getB1_ReviYaw();
		float f6 = ((HE_177A3) this.fm.actor).getB1_ReviX();
		float f7 = ((HE_177A3) this.fm.actor).getB1_ReviY();
		this.mesh.chunkSetAngles("Z_TurretB2", 0.0F, 0.0F, f2);
		this.mesh.chunkSetAngles("Z_Turret_Revi", 0.0F, f3, 0.0F);
		this.mesh.chunkSetAngles("Z_tRevi1", -f3, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_tRevi2", f3, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_tRevi4", f, 0.0F, 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[0] = -f7;
		this.mesh.chunkSetLocate("Z_tRevi5", Cockpit.xyz, Cockpit.ypr);
		this.resetYPRmodifier();
		Cockpit.xyz[1] = f6;
		this.mesh.chunkSetLocate("Z_tRevi3", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetAngles("Z_tRevi6", 0.0F, 0.0F, f4);
		this.mesh.chunkSetAngles("Z_tRevi7", f5, 0.0F, 0.0F);
	}

	private Variables          setOld;
	private Variables          setNew;
	private Variables          setTmp;
	public Vector3f            w;
	private Point3d            Pn;
	private static final float Turret_Z1[]     = { 0.0F, 4.235F, 8.64F, 13.21F, 17.92F, 22.79F, 27.82F, 32.99F, 38.29F, 43.75F, 49.36F, 55.12F, 61.05F, 67.13F, 73.4F, 79.87F, 86.55F, 93.45F, 100.61F };
	private static final float Turret_Z2[]     = { 0.0F, 2.125F, 4.32F, 6.58F, 8.87F, 11.188F, 13.53F, 15.88F, 18.21F, 20.52F, 22.78F, 24.98F, 27.105F, 29.13F, 31.03F, 32.76F, 34.33F, 35.68F, 36.78F };
	private static final float IAS_Scale[]     = { 0.0F, 8F, 18.4F, 41.26F, 67.26F, 94.36F, 119.58F, 141F, 166.1F, 190.43F, 216.05F, 241.18F, 267.5F, 293.8F, 318.19F, 341.7F };
	private static final float Fl20342_Scale[] = { 0.0F, 5.5F, 11F, 17.5F, 25F, 33F, 41.5F, 50.5F, 59.5F, 67F, 73.5F, 80.25F, 85.5F, 90F };
	private static final float Fl20274_Scale[] = { 0.0F, 4.25F, 8F, 12F, 17F, 23F, 28.5F, 34.5F, 42F, 51F, 58.5F, 69F, 79F, 91F, 103F, 114.5F, 128F, 140F, 152.5F, 164.5F, 175F, 186.5F, 195.5F, 205F, 214F, 222.5F, 230.5F, 239F, 246.5F, 253.5F, 260.5F,
			265F, 269.5F };
	final float                constAB         = 0.03866613F;
	final float                constAC         = 0.1832059F;
	final float                constOA         = 0.0391989F;
	final float                constOB         = 0.05015091F;
	final float                constAC2        = 0.1882421F;
	final float                constBD2        = 0.1882416F;
	final float                constCD2        = 0.08457017F;
	private boolean            bNeedSetUp;
	private boolean            bEntered;
	private Gun                gun;
	private int                countBullets;

	static {
		Class class1 = CockpitHE_177A3_FGunner.class;
		Property.set(class1, "aiTuretNum", 1);
		Property.set(class1, "weaponControlNum", 11);
		Property.set(class1, "astatePilotIndx", 3);
		Property.set(class1, "normZN", 3.2F);
	}
}
