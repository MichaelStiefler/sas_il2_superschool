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
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Message;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_177A3 extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (CockpitHE_177A3.this.fm != null) {
				CockpitHE_177A3.this.setTmp = CockpitHE_177A3.this.setOld;
				CockpitHE_177A3.this.setOld = CockpitHE_177A3.this.setNew;
				CockpitHE_177A3.this.setNew = CockpitHE_177A3.this.setTmp;
				CockpitHE_177A3.this.setNew.AirEnemy = 0.95F * CockpitHE_177A3.this.setOld.AirEnemy + 0.05F * (((HE_177A3) CockpitHE_177A3.this.fm.actor).bAirEnemy ? 1.0F : 0.0F);
				float f = CockpitHE_177A3.this.waypointAzimuth();
				if (CockpitHE_177A3.this.useRealisticNavigationInstruments()) {
					CockpitHE_177A3.this.setNew.waypointAzimuth.setDeg(f - 90F);
					CockpitHE_177A3.this.setOld.waypointAzimuth.setDeg(f - 90F);
					CockpitHE_177A3.this.setNew.radioCompassAzimuth.setDeg(CockpitHE_177A3.this.setOld.radioCompassAzimuth.getDeg(0.02F), CockpitHE_177A3.this.radioCompassAzimuthInvertMinus() - CockpitHE_177A3.this.setOld.azimuth.getDeg(1.0F) - 90F);
				} else CockpitHE_177A3.this.setNew.waypointAzimuth.setDeg(CockpitHE_177A3.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitHE_177A3.this.setOld.azimuth.getDeg(1.0F));
				CockpitHE_177A3.this.setNew.azimuth.setDeg(CockpitHE_177A3.this.setOld.azimuth.getDeg(1.0F), CockpitHE_177A3.this.fm.Or.azimut());
				CockpitHE_177A3.this.setNew.throttle1 = 0.85F * CockpitHE_177A3.this.setOld.throttle1 + CockpitHE_177A3.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
				CockpitHE_177A3.this.setNew.throttle2 = 0.85F * CockpitHE_177A3.this.setOld.throttle2 + CockpitHE_177A3.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
				CockpitHE_177A3.this.setNew.pictAiler = 0.85F * CockpitHE_177A3.this.setOld.pictAiler + 0.15F * CockpitHE_177A3.this.cvt(CockpitHE_177A3.this.fm.CT.AileronControl, -1F, 1.0F, -1F, 1.0F);
				CockpitHE_177A3.this.setNew.pictElev = 0.85F * CockpitHE_177A3.this.setOld.pictElev + 0.15F * CockpitHE_177A3.this.cvt(CockpitHE_177A3.this.fm.CT.ElevatorControl, -1F, 1.0F, -1F, 1.0F);
				CockpitHE_177A3.this.setNew.elevTrim = 0.85F * CockpitHE_177A3.this.setOld.elevTrim + 0.15F * CockpitHE_177A3.this.fm.CT.trimElevator;
				CockpitHE_177A3.this.setNew.rudderTrim = 0.85F * CockpitHE_177A3.this.setOld.rudderTrim + 0.15F * CockpitHE_177A3.this.fm.CT.trimRudder;
				CockpitHE_177A3.this.setNew.ailTrim = 0.85F * CockpitHE_177A3.this.setOld.ailTrim + 0.15F * CockpitHE_177A3.this.fm.CT.trimAileron;
				if (Math.toDegrees(CockpitHE_177A3.this.fm.EI.engines[0].getPropPhi()) < 36D) {
					CockpitHE_177A3.this.setNew.prop1 = 0.85F * CockpitHE_177A3.this.setOld.prop1 + ((FlightModelMain) CockpitHE_177A3.this.fm).EI.engines[0].getControlProp() * 0.15F;
					CockpitHE_177A3.this.setNew._prop1 = CockpitHE_177A3.this.cvt(CockpitHE_177A3.this.setNew.prop1, 0.0F, 1.0F, -22F, -63F);
				} else {
					CockpitHE_177A3.this.setNew.prop1 = 0.85F * CockpitHE_177A3.this.setOld.prop1;
					CockpitHE_177A3.this.setNew._prop1 = CockpitHE_177A3.this.cvt(CockpitHE_177A3.this.setNew.prop1, 0.0F, 1.0F, 0.0F, -63F);
				}
				if (Math.toDegrees(CockpitHE_177A3.this.fm.EI.engines[1].getPropPhi()) < 36D) {
					CockpitHE_177A3.this.setNew.prop2 = 0.85F * CockpitHE_177A3.this.setOld.prop2 + ((FlightModelMain) CockpitHE_177A3.this.fm).EI.engines[1].getControlProp() * 0.15F;
					CockpitHE_177A3.this.setNew._prop2 = CockpitHE_177A3.this.cvt(CockpitHE_177A3.this.setNew.prop2, 0.0F, 1.0F, -22F, -63F);
				} else {
					CockpitHE_177A3.this.setNew.prop2 = 0.85F * CockpitHE_177A3.this.setOld.prop2;
					CockpitHE_177A3.this.setNew._prop2 = CockpitHE_177A3.this.cvt(CockpitHE_177A3.this.setNew.prop2, 0.0F, 1.0F, 0.0F, -63F);
				}
				CockpitHE_177A3.this.w.set(CockpitHE_177A3.this.fm.getW());
				CockpitHE_177A3.this.fm.Or.transform(CockpitHE_177A3.this.w);
				CockpitHE_177A3.this.setNew.turn = (12F * CockpitHE_177A3.this.setOld.turn + CockpitHE_177A3.this.w.z) / 13F;
				CockpitHE_177A3.this.setNew.altimeter = 0.85F * CockpitHE_177A3.this.setOld.altimeter + CockpitHE_177A3.this.fm.getAltitude() * 0.15F;
				CockpitHE_177A3.this.setNew.vspeed = (99F * CockpitHE_177A3.this.setOld.vspeed + CockpitHE_177A3.this.fm.getVertSpeed()) / 100F;
				float f1 = CockpitHE_177A3.this.fm.Or.getKren();
				float f2 = CockpitHE_177A3.this.fm.Or.getTangage();
				if (f1 > 55F || f1 < -55F || f2 < -55F || f2 > 55F) CockpitHE_177A3.this.Pn.z = 250D;
				else {
					CockpitHE_177A3.this.Pn.set(CockpitHE_177A3.this.fm.Loc);
					CockpitHE_177A3.this.Pn.z = CockpitHE_177A3.this.fm.getAltitude() - Engine.cur.land.HQ(((Tuple3d) CockpitHE_177A3.this.Pn).x, ((Tuple3d) CockpitHE_177A3.this.Pn).y);
					double d = CockpitHE_177A3.this.Pn.z * (float) Math.tan(Geom.DEG2RAD(f1));
					double d1 = CockpitHE_177A3.this.Pn.z * (float) Math.tan(Geom.DEG2RAD(f2));
					CockpitHE_177A3.this.Pn.z = (float) Math.sqrt(d * d + d1 * d1 + CockpitHE_177A3.this.Pn.z * CockpitHE_177A3.this.Pn.z);
					if (CockpitHE_177A3.this.fm.CT.getGear() > 0.5F) CockpitHE_177A3.this.Pn.z = CockpitHE_177A3.this.cvt((float) CockpitHE_177A3.this.Pn.z, 0.0F, 150F, 0.0F, 250F);
					else CockpitHE_177A3.this.Pn.z = CockpitHE_177A3.this.cvt((float) CockpitHE_177A3.this.Pn.z, 0.0F, 750F, 0.0F, 250F);
				}
				CockpitHE_177A3.this.setNew.AFN101 = 0.9F * CockpitHE_177A3.this.setOld.AFN101 + 0.1F * (float) CockpitHE_177A3.this.Pn.z;
				CockpitHE_177A3.this.setNew.beaconDirection = (10F * CockpitHE_177A3.this.setOld.beaconDirection + CockpitHE_177A3.this.getBeaconDirection()) / 11F;
				CockpitHE_177A3.this.setNew.beaconRange = (10F * CockpitHE_177A3.this.setOld.beaconRange + CockpitHE_177A3.this.getBeaconRange()) / 11F;
				CockpitHE_177A3.this.setNew.pictManf1 = 0.9F * CockpitHE_177A3.this.setOld.pictManf1 + 0.1F * CockpitHE_177A3.this.fm.EI.engines[0].getManifoldPressure();
				CockpitHE_177A3.this.setNew.pictManf2 = 0.95F * CockpitHE_177A3.this.setOld.pictManf2 + 0.05F * CockpitHE_177A3.this.fm.EI.engines[0].getManifoldPressure();
				CockpitHE_177A3.this.setNew.pictManf3 = 0.9F * CockpitHE_177A3.this.setOld.pictManf3 + 0.1F * CockpitHE_177A3.this.fm.EI.engines[1].getManifoldPressure();
				CockpitHE_177A3.this.setNew.pictManf4 = 0.95F * CockpitHE_177A3.this.setOld.pictManf4 + 0.05F * CockpitHE_177A3.this.fm.EI.engines[1].getManifoldPressure();
				float f3 = CockpitHE_177A3.this.setNew.prevFuel - CockpitHE_177A3.this.fm.M.fuel;
				CockpitHE_177A3.this.setNew.prevFuel = CockpitHE_177A3.this.fm.M.fuel;
				f3 /= 0.72F;
				f3 /= Time.tickLenFs();
				f3 *= 3600F;
				CockpitHE_177A3.this.setNew.cons = 0.9F * CockpitHE_177A3.this.setOld.cons + 0.1F * f3;
				float f4 = CockpitHE_177A3.this.fm.EI.engines[0].getEngineForce().x;
				float f5 = CockpitHE_177A3.this.fm.EI.engines[1].getEngineForce().x;
				float f6 = CockpitHE_177A3.this.setNew.cons;
				CockpitHE_177A3.this.setNew.consumptionL = 0.9F * CockpitHE_177A3.this.setOld.consumptionL + 0.1F * (f6 * f4) / (f4 + f5 + 1.0F);
				CockpitHE_177A3.this.setNew.consumptionR = 0.9F * CockpitHE_177A3.this.setOld.consumptionR + 0.1F * (f6 * f5) / (f4 + f5 + 1.0F);
				CockpitHE_177A3.this.setNew.bombDoor = 0.9F * CockpitHE_177A3.this.setOld.bombDoor + 0.1F * CockpitHE_177A3.this.fm.CT.getBayDoor();
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
		float      elevTrim;
		float      rudderTrim;
		float      ailTrim;
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

	public CockpitHE_177A3() {
		super("3DO/Cockpit/He-177A-3/hier.him", "he111");
		this.setOld = new Variables();
		this.setNew = new Variables();
		this.w = new Vector3f();
		this.w = new Vector3f();
		this.Pn = new Point3d();
		this.cockpitNightMats = new String[] { "CompGrad", "Fl20274", "Fl20342na", "Fl20342", "Fl20516", "Fl20556", "Fl20570", "Fl20572", "Fl20723_1185", "Fl20723_1850", "Fl20723_200na", "Fl20723_200", "Fl20723_640", "Fl20841", "Fl22231", "Fl22316",
				"Fl22320", "Fl22334b", "Fl22334c", "Fl22382", "Fl22412", "Fl22413", "Fl22561", "Fl23885na", "Fl23885", "Fl30489", "Fl30532", "Fl32336", "Gauge19", "Gauge20", "Ln27002", "Ln28330b", "Ln28330", "NeedlesnLights", "Nr92182B1na",
				"Voltmeters" };
//		this.hidePilot = true;
		this.setNightMats(false);
		this.interpPut(new Interpolater(), (Object) null, Time.current(), (Message) null);
		this.limits6DoF = new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.1F, -0.07F, 0.03F, -0.03F };
	}

	public void reflectWorldToInstruments(float f) {
		this.mesh.chunkSetAngles("Z_Lotfe7", ((HE_177A3) this.fm.actor).fSightCurSideslip * -10F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Throttle1", 41.8F * this.setNew.throttle1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Throttle2", 41.8F * this.setNew.throttle1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Throttle3", 41.8F * this.setNew.throttle2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Throttle4", 41.8F * this.setNew.throttle2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_BMBThrot1", 70F * this.setNew.throttle1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_BMBThrot2", 70F * this.setNew.throttle1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_BMBThrot3", 70F * this.setNew.throttle2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_BMBThrot4", 70F * this.setNew.throttle2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng1Starter", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng2Starter", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng3Starter", this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng4Starter", this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng1Diseng", this.fm.EI.engines[0].getRPM() <= 10F ? 0.0F : 42F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng2Diseng", this.fm.EI.engines[0].getRPM() <= 10F ? 0.0F : 42F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng3Diseng", this.fm.EI.engines[1].getRPM() <= 10F ? 0.0F : 42F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng4Diseng", this.fm.EI.engines[1].getRPM() <= 10F ? 0.0F : 42F, 0.0F, 0.0F);
		this.mesh.chunkVisible("F_LGearsDown", this.fm.CT.getGear() == 1.0F && this.fm.Gears.lgear);
		this.mesh.chunkVisible("F_RGearsDown", this.fm.CT.getGear() == 1.0F && this.fm.Gears.rgear);
		this.mesh.chunkVisible("F_AllGearsDown", this.fm.CT.getGear() == 1.0F && this.fm.CT.getGear() == 1.0F && this.fm.Gears.lgear && this.fm.Gears.rgear);
		this.mesh.chunkVisible("F_TailGearsDown", this.fm.CT.getGear() == 1.0F && this.fm.Gears.cgear);
		this.mesh.chunkVisible("F_LGearsUp", this.fm.CT.getGear() == 0.0F);
		this.mesh.chunkVisible("F_RGearsUp", this.fm.CT.getGear() == 0.0F);
		this.mesh.chunkVisible("F_AllGearsUp", this.fm.CT.getGear() == 0.0F && this.fm.CT.getGear() == 0.0F);
		this.mesh.chunkVisible("F_TailGearsUp", this.fm.CT.getGear() == 0.0F);
		this.mesh.chunkSetAngles("Z_Gears", this.fm.CT.getGear() != 1.0F || this.fm.CT.getGear() != 1.0F || !this.fm.Gears.lgear || !this.fm.Gears.rgear ? 0.0F : 90F, 0.0F, 0.0F);
		float f1 = this.fm.CT.getFlap();
		this.mesh.chunkVisible("F_FlapsUp", f1 < 0.1F);
		this.mesh.chunkVisible("F_FlapsUnf", f1 > 0.1F && f1 < 0.5F);
		this.mesh.chunkVisible("F_FlapsExt", f1 > 0.5F);
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
		this.mesh.chunkSetAngles("Z_Tiaga4R", -35F * f4, 0.0F, 0.0F);
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
		float f14 = 106.3657F - f13;
		double d1 = Math.cos(Geom.DEG2RAD(f14));
		float f15 = Geom.RAD2DEG((float) Math.acos((0.038666129112243652D - 0.18320585787296295D * d1) / Math.sqrt(0.035059455782175064D - 0.014167722314596176D * d1)));
		float f16 = 180F - f14 - f15;
		float f17 = 62.8136F - f15;
		float f18 = f16 - 10.82074F;
		this.mesh.chunkSetAngles("Z_Kachalka2R", f13, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Tiaga3R", f17, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_CilindrR", 0.0F, f18, 0.0F);
		double d2 = Math.cos(Geom.DEG2RAD(142.0612F + f7));
		double d3 = Math.sqrt(0.0040516676381230354D - 0.003931721206754446D * d2);
		float f19 = Geom.RAD2DEG((float) Math.acos((0.039198901504278183D - 0.050150908529758453D * d2) / d3));
		float f20 = 21.38197F - f19;
		float f21 = 89.43449F + f20 + 35F * f4;
		double d4 = Math.sqrt(0.035435102880001068D + d3 * d3 - 0.37648427486419678D * d3 * (float) Math.cos(Geom.DEG2RAD(f21)));
		float f22 = Geom.RAD2DEG((float) Math.acos((0.042587004601955414D - d4 * d4) / 0.031839244067668915D));
		float f23 = 89.43554F - f22;
		this.mesh.chunkSetAngles("Z_Tiaga2L", f23, 0.0F, 0.0F);
		float f24 = (float) Math.acos((0.035435102880001068D + d4 * d4 - d3 * d3) / (0.37648427486419678D * d4));
		float f25 = (float) Math.acos((0.0071521135978400707D + d4 * d4 - 0.035434890538454056D) / (0.16914033889770508D * d4));
		float f26 = Geom.RAD2DEG(f24 + f25) - 90.56514F;
		this.mesh.chunkSetAngles("Z_LBrake", 0.0F, 0.0F, f26);
		double d5 = Math.cos(Geom.DEG2RAD(142.0612F + f13));
		double d6 = Math.sqrt(0.0040516676381230354D - 0.003931721206754446D * d5);
		float f27 = Geom.RAD2DEG((float) Math.acos((0.039198901504278183D - 0.050150908529758453D * d5) / d6));
		float f28 = 21.38197F - f27;
		float f29 = 89.43449F + f28 - 35F * f4;
		double d7 = Math.sqrt(0.035435102880001068D + d6 * d6 - 0.37648427486419678D * d6 * (float) Math.cos(Geom.DEG2RAD(f29)));
		float f30 = Geom.RAD2DEG((float) Math.acos((0.042587004601955414D - d7 * d7) / 0.031839244067668915D));
		float f31 = 89.43554F - f30;
		this.mesh.chunkSetAngles("Z_Tiaga2R", f31, 0.0F, 0.0F);
		float f32 = (float) Math.acos((0.035435102880001068D + d7 * d7 - d6 * d6) / (0.37648427486419678D * d7));
		float f33 = (float) Math.acos((0.0071521135978400707D + d7 * d7 - 0.035434890538454056D) / (0.16914033889770508D * d7));
		float f34 = Geom.RAD2DEG(f32 + f33) - 90.56514F;
		this.mesh.chunkSetAngles("Z_RBrake", 0.0F, 0.0F, f34);
		this.mesh.chunkSetAngles("Z_VatorTrim", this.cvt(this.setNew.elevTrim, -0.5F, 0.5F, -750F, 750F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_RudderTrim", -this.cvt(this.setNew.rudderTrim, -0.5F, 0.5F, -750F, 750F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_AilronTrim", -this.cvt(this.setNew.ailTrim, -0.5F, 0.5F, -750F, 750F), 0.0F, 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[0] = -this.cvt(this.setNew.elevTrim, -0.5F, 0.5F, -0.08425F, 0.08425F);
		this.mesh.chunkSetLocate("Z_VatorTrim2", Cockpit.xyz, Cockpit.ypr);
		this.resetYPRmodifier();
		Cockpit.xyz[0] = this.cvt(this.setNew.rudderTrim, -0.5F, 0.5F, -0.0722F, 0.0722F);
		this.mesh.chunkSetLocate("Z_RudderTrim2", Cockpit.xyz, Cockpit.ypr);
		this.resetYPRmodifier();
		Cockpit.xyz[0] = -this.cvt(this.setNew.ailTrim, -0.5F, 0.5F, -0.0722F, 0.0722F);
		this.mesh.chunkSetLocate("Z_AilronTrim2", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetAngles("Z_PropPitch1", -this.setNew._prop1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_PropPitch2", -this.setNew._prop2, 0.0F, 0.0F);
		float f35 = (float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin());
		f35 = (int) (f35 / 0.2F) * 0.2F;
		this.mesh.chunkSetAngles("Z_N_Clock1_M", f35 * 60F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Clock1_H", f35 * 5F, 0.0F, 0.0F);
		float f36 = (float) Math.toDegrees(this.fm.EI.engines[1].getPropPhi() - this.fm.EI.engines[1].getPropPhiMin());
		f36 = (int) (f36 / 0.2F) * 0.2F;
		this.mesh.chunkSetAngles("Z_N_Clock2_M", f36 * 60F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Clock2_H", f36 * 5F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_BombRelease", this.cvt(this.setNew.bombDoor, 0.0F, 1.0F, 0.0F, 90F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Clock3_H", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Clock3_M", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Clock3_S", -this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_OxPress1", -200F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_OxPress2", -200F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_OxPress3", -200F, 0.0F, 0.0F);
		float f37 = this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
		this.mesh.chunkSetAngles("Z_N_Turn1", f37, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Turn2", f37, 0.0F, 0.0F);
		f37 = this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 17F, -17F);
		this.mesh.chunkSetAngles("Z_N_Turn3", f37, 0.0F, 0.0F);
		float f38 = -this.getBall(8D);
		this.mesh.chunkSetAngles("Z_N_Bank1", this.cvt(f38, -4F, 4F, -10.5F, 10.5F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Bank2", this.cvt(f38, -4F, 4F, -14F, 14F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Bank3", this.cvt(f38, -4F, 4F, -12F, 12F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Bank4", this.cvt(f38, -4F, 4F, -10F, 10F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AirSpeed1", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 750F, 0.0F, 15F), IAS_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AirSpeed2", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 750F, 0.0F, 15F), IAS_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AH1", 0.0F, 0.0F, -this.fm.Or.getKren());
		this.mesh.chunkSetAngles("Z_N_AH2", -this.cvt(this.fm.Or.getTangage(), -45F, 45F, -8F, 8F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Alt_km", -this.cvt(this.setNew.altimeter, 0.0F, 11000F, 0.0F, 330F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Alti", -this.cvt(this.setNew.altimeter, 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Alti2", -this.cvt(this.setNew.altimeter, 0.0F, 20000F, 0.0F, 720F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Climb1", -this.cvt(this.setNew.vspeed, -15F, 15F, -136F, 136F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Climb2", -this.cvt(this.setNew.vspeed, -15F, 15F, -136F, 136F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_RadioAltim", -this.setNew.AFN101, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Ln28330b", this.cvt(this.fm.CT.getGear(), 0.4F, 0.6F, 40.2F, 0.0F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Nav1", this.cvt(this.setNew.beaconDirection, -45F, 45F, -16F, 16F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Nav2", this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, -14.5F, 20F), 0.0F, 0.0F);
		this.mesh.chunkVisible("F_N_AFN2_RED", this.isOnBlindLandingMarker());
		this.mesh.chunkSetAngles("Z_N_AiFuePress1", -this.cvt(this.setNew.pictManf1, 0.6F, 1.8F, 0.0F, 330F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AiFuePress2", -this.cvt(this.setNew.pictManf2, 0.6F, 1.8F, 0.0F, 330F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AiFuePress3", -this.cvt(this.setNew.pictManf3, 0.6F, 1.8F, 0.0F, 330F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AiFuePress4", -this.cvt(this.setNew.pictManf4, 0.6F, 1.8F, 0.0F, 330F), 0.0F, 0.0F);
		float f39 = ((FlightModelMain) this.fm).EI.engines[0].getRPM();
		this.mesh.chunkSetAngles("Z_N_RPM1", -this.floatindex(this.cvt(f39, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_RPM2", -this.floatindex(this.cvt(f39, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
		float f40 = ((FlightModelMain) this.fm).EI.engines[1].getRPM();
		this.mesh.chunkSetAngles("Z_N_RPM3", -this.floatindex(this.cvt(f40, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_RPM4", -this.floatindex(this.cvt(f40, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_FuelConsom", -this.cvt(0.5F * this.setNew.consumptionL, 0.0F, 500F, 0.0F, 255.5F), 0.0F, 0.0F);
		int i = ((HE_177A3) this.aircraft()).iRust;
		float f41 = this.fm.M.fuel;
		if (i == 2) {
			this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f41, 6825.781F, 9382.67F, 0.0F, 62.93919F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f41, 4268.9F, 6825.781F, 0.0F, 62.93919F), 0.0F, 0.0F);
		} else if (i == 1) {
			this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f41, 5395.4F, 7952.296F, 0.0F, 62.93919F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f41, 4268.9F, 5395.4F, 0.0F, 55.45946F), 0.0F, 0.0F);
		} else {
			this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f41, 5395.4F, 6521.92F, 0.0F, 55.45946F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f41, 4268.9F, 5395.4F, 0.0F, 55.45946F), 0.0F, 0.0F);
		}
		this.mesh.chunkSetAngles("Z_N_Fuel6", -this.cvt(f41, 3424.015F, 4268.9F, 0.0F, 72.63291F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Fuel2", -this.cvt(f41, 2505.015F, 3424.015F, 0.0F, 71.6875F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Fuel3", -this.cvt(f41, 2505.015F, 3424.015F, 0.0F, 71.6875F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Fuel1", -this.cvt(f41, 0.0F, 844.89F, 0.0F, 72.63291F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_FuelQuant", -this.cvt(f41, 0.0F, this.fm.M.maxFuel, 37F, 84F), 0.0F, 0.0F);
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
		float f42 = -this.fm.turret[1].tu[0];
		float f43 = this.fm.turret[1].tu[1];
		float f44 = Geom.DEG2RAD(f42);
		float f45 = Geom.DEG2RAD(f43);
		float f46 = (float) Math.asin(Math.tan(f44) * Math.cos(f45) * Math.cos(f44));
		float f47 = (float) Math.atan(Math.tan(f45) / Math.cos(f44));
		float f48 = Geom.RAD2DEG(f46);
		float f49 = Geom.RAD2DEG(f47);
		this.mesh.chunkSetAngles("Z_Turret151A", 0.0F, f48, 0.0F);
		this.mesh.chunkSetAngles("Z_Turret151B", 0.0F, 0.0F, f49);
		float f50;
		for (f50 = -this.fm.turret[3].tu[0]; f50 < -180F; f50 += 360F)
			;
		for (; f50 > 180F; f50 -= 360F)
			;
		float f51 = this.fm.turret[3].tu[1];
		this.mesh.chunkSetAngles("Z_TurretA", -f50, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_TurretB", 0.0F, 0.0F, -f51);
		this.mesh.chunkSetAngles("Z_tLink", 0.0F, 0.0F, this.floatindex(this.cvt(f51, 0.0F, 90F, 0.0F, 18F), Turret_Z1));
		this.mesh.chunkSetAngles("Z_tHandle", 0.0F, 0.0F, -this.floatindex(this.cvt(f51, 0.0F, 90F, 0.0F, 18F), Turret_Z2));
		this.mesh.chunkSetAngles("Z_tReviVal", 0.0F, -f50 * 5F, 0.0F);
		this.CalculateRevi(f50, f51);
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
		if ((this.fm.AS.astateCockpitState & 4) != 0) {
			this.mesh.chunkVisible("xGlass2", false);
			this.mesh.chunkVisible("xGlass2_dmg", true);
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

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			HierMesh hiermesh = this.aircraft().hierMesh();
			hiermesh.chunkVisible("Interior1_D0", false);
			return true;
		} else return false;
	}

	protected void doFocusLeave() {
		HierMesh hiermesh = this.aircraft().hierMesh();
		hiermesh.chunkVisible("Interior1_D0", true);
		super.doFocusLeave();
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

	static {
		Property.set(CockpitHE_177A3.class, "normZNs", new float[] { 1.82F, 1.8F, 1.85F, 1.8F });
	}

}
