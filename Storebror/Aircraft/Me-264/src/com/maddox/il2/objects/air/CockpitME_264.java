package com.maddox.il2.objects.air;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Message;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.BaseGameVersion;

public class CockpitME_264 extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (CockpitME_264.this.fm != null) {
				CockpitME_264.this.setTmp = CockpitME_264.this.setOld;
				CockpitME_264.this.setOld = CockpitME_264.this.setNew;
				CockpitME_264.this.setNew = CockpitME_264.this.setTmp;
				float f = CockpitME_264.this.waypointAzimuth();
				if (BaseGameVersion.is410orLater()) {
	                CockpitME_264.this.setNew.beaconDirection = (10F * CockpitME_264.this.setOld.beaconDirection
	                        + CockpitME_264.this.getBeaconDirection()) / 11F;
	                CockpitME_264.this.setNew.beaconRange = (10F * CockpitME_264.this.setOld.beaconRange
	                        + CockpitME_264.this.getBeaconRange()) / 11F;
				    if (CockpitME_264.this.useRealisticNavigationInstruments()) {
	                    CockpitME_264.this.setNew.waypointAzimuth.setDeg(f - 90F);
	                    CockpitME_264.this.setOld.waypointAzimuth.setDeg(f - 90F);
	                    CockpitME_264.this.setNew.radioCompassAzimuth.setDeg(
	                            CockpitME_264.this.setOld.radioCompassAzimuth.getDeg(0.02F),
	                            CockpitME_264.this.radioCompassAzimuthInvertMinus()
	                                    - CockpitME_264.this.setOld.azimuth.getDeg(1.0F) - 90F);
	                } else {
	                    CockpitME_264.this.setNew.waypointAzimuth.setDeg(
	                            CockpitME_264.this.setOld.waypointAzimuth.getDeg(0.1F),
	                            f - CockpitME_264.this.setOld.azimuth.getDeg(1.0F));
	                }
				} else {
					CockpitME_264.this.setNew.waypointAzimuth.setDeg(
							CockpitME_264.this.setOld.waypointAzimuth.getDeg(0.1F),
							f - CockpitME_264.this.setOld.azimuth.getDeg(1.0F));
				}
				CockpitME_264.this.setNew.azimuth.setDeg(CockpitME_264.this.setOld.azimuth.getDeg(1.0F),
						CockpitME_264.this.fm.Or.azimut());
				CockpitME_264.this.setNew.pictAiler = 0.85F * CockpitME_264.this.setOld.pictAiler + 0.15F
						* CockpitME_264.this.cvt(CockpitME_264.this.fm.CT.AileronControl, -1F, 1.0F, -1F, 1.0F);
				CockpitME_264.this.setNew.pictElev = 0.85F * CockpitME_264.this.setOld.pictElev + 0.15F
						* CockpitME_264.this.cvt(CockpitME_264.this.fm.CT.ElevatorControl, -1F, 1.0F, -1F, 1.0F);
				CockpitME_264.this.setNew.elevTrim = 0.85F * CockpitME_264.this.setOld.elevTrim
						+ 0.15F * CockpitME_264.this.fm.CT.trimElevator;
				CockpitME_264.this.setNew.rudderTrim = 0.85F * CockpitME_264.this.setOld.rudderTrim
						+ 0.15F * CockpitME_264.this.fm.CT.trimRudder;
				CockpitME_264.this.setNew.ailTrim = 0.85F * CockpitME_264.this.setOld.ailTrim
						+ 0.15F * CockpitME_264.this.fm.CT.trimAileron;
				
                float fCons = 0F;
				for (int engineIndex = 0; engineIndex < 4; engineIndex++) {
	                CockpitME_264.this.setNew.throttle[engineIndex] = 0.85F * CockpitME_264.this.setOld.throttle[engineIndex]
	                        + CockpitME_264.this.fm.EI.engines[engineIndex].getControlThrottle() * 0.15F;
	                if (Math.toDegrees(CockpitME_264.this.fm.EI.engines[engineIndex].getPropPhi()) < 36D) {
	                    CockpitME_264.this.setNew.prop[engineIndex] = 0.85F * CockpitME_264.this.setOld.prop[engineIndex]
	                            + ((FlightModelMain) CockpitME_264.this.fm).EI.engines[engineIndex].getControlProp() * 0.15F;
	                    CockpitME_264.this.setNew._prop[engineIndex] = CockpitME_264.this.cvt(CockpitME_264.this.setNew.prop[engineIndex],
	                            0.0F, 1.0F, -22F, -63F);
	                } else {
	                    CockpitME_264.this.setNew.prop[engineIndex] = 0.85F * CockpitME_264.this.setOld.prop[engineIndex];
	                    CockpitME_264.this.setNew._prop[engineIndex] = CockpitME_264.this.cvt(CockpitME_264.this.setNew.prop[engineIndex],
	                            0.0F, 1.0F, 0.0F, -63F);
	                }
	                CockpitME_264.this.setNew.pictManf[engineIndex] = 0.95F * CockpitME_264.this.setOld.pictManf[engineIndex]
	                        + 0.05F * CockpitME_264.this.fm.EI.engines[engineIndex].getManifoldPressure();
	                fCons += CockpitME_264.this.fm.EI.engines[engineIndex].getEngineForce().x;
				}
				CockpitME_264.this.w.set(CockpitME_264.this.fm.getW());
				CockpitME_264.this.fm.Or.transform(CockpitME_264.this.w);
				CockpitME_264.this.setNew.turn = (12F * CockpitME_264.this.setOld.turn + CockpitME_264.this.w.z)
						/ 13F;
				CockpitME_264.this.setNew.altimeter = 0.85F * CockpitME_264.this.setOld.altimeter
						+ CockpitME_264.this.fm.getAltitude() * 0.15F;
				CockpitME_264.this.setNew.vspeed = (99F * CockpitME_264.this.setOld.vspeed
						+ CockpitME_264.this.fm.getVertSpeed()) / 100F;
				float f1 = CockpitME_264.this.fm.Or.getKren();
				float f2 = CockpitME_264.this.fm.Or.getTangage();
				if (f1 > 55F || f1 < -55F || f2 < -55F || f2 > 55F) {
					CockpitME_264.this.Pn.z = 250D;
				} else {
					CockpitME_264.this.Pn.set(CockpitME_264.this.fm.Loc);
					CockpitME_264.this.Pn.z = CockpitME_264.this.fm.getAltitude() - Engine.cur.land
							.HQ(((Tuple3d) CockpitME_264.this.Pn).x, ((Tuple3d) CockpitME_264.this.Pn).y);
					double d = CockpitME_264.this.Pn.z * (float) Math.tan(Geom.DEG2RAD(f1));
					double d1 = CockpitME_264.this.Pn.z * (float) Math.tan(Geom.DEG2RAD(f2));
					CockpitME_264.this.Pn.z = (float) Math
							.sqrt(d * d + d1 * d1 + CockpitME_264.this.Pn.z * CockpitME_264.this.Pn.z);
					if (CockpitME_264.this.fm.CT.getGear() > 0.5F) {
						CockpitME_264.this.Pn.z = CockpitME_264.this.cvt((float) CockpitME_264.this.Pn.z, 0.0F,
								150F, 0.0F, 250F);
					} else {
						CockpitME_264.this.Pn.z = CockpitME_264.this.cvt((float) CockpitME_264.this.Pn.z, 0.0F,
								750F, 0.0F, 250F);
					}
				}
				CockpitME_264.this.setNew.AFN101 = 0.9F * CockpitME_264.this.setOld.AFN101
						+ 0.1F * (float) CockpitME_264.this.Pn.z;
				float f3 = CockpitME_264.this.setNew.prevFuel - CockpitME_264.this.fm.M.fuel;
				CockpitME_264.this.setNew.prevFuel = CockpitME_264.this.fm.M.fuel;
				f3 /= 0.72F;
				f3 /= Time.tickLenFs();
				f3 *= 3600F;
				CockpitME_264.this.setNew.cons = 0.9F * CockpitME_264.this.setOld.cons + 0.1F * f3;
				float f6 = CockpitME_264.this.setNew.cons;
                CockpitME_264.this.setNew.consumption = 0.9F * CockpitME_264.this.setOld.consumption
                        + 0.1F * (f6 * fCons / 4F) / (fCons + 1.0F);
				CockpitME_264.this.setNew.bombDoor = 0.9F * CockpitME_264.this.setOld.bombDoor
						+ 0.1F * CockpitME_264.this.fm.CT.getBayDoor();
			}
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

	    float throttle[];
		float pictAiler;
		float pictElev;
		float elevTrim;
		float rudderTrim;
		float ailTrim;
		float prop[];
		float _prop[];
		float turn;
		float altimeter;
		float vspeed;
		float AFN101;
		float beaconDirection;
		float beaconRange;
        float pictManf[];
		float prevFuel;
		float cons;
		float consumption;
		float bombDoor;
		float AirEnemy;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		AnglesFork radioCompassAzimuth;

		private Variables() {
			this.azimuth = new AnglesFork();
			this.waypointAzimuth = new AnglesFork();
			this.radioCompassAzimuth = new AnglesFork();
			this.prop = new float[4];
            this._prop = new float[4];
            this.throttle = new float[4];
            this.pictManf = new float[4];
		}

	}

    protected float waypointAzimuth() {
        if (BaseGameVersion.is410orLater())
            return super.waypointAzimuthInvertMinus(30F);
        WayPoint waypoint = fm.AP.way.curr();
        if (waypoint == null)
            return 0.0F;
        Point3d tmpP = new Point3d();
        Vector3d tmpV = new Vector3d();
        waypoint.getP(tmpP);
        tmpV.sub(tmpP, fm.Loc);
        float fWP = (float) (180.0D / Math.PI * Math.atan2(tmpV.x, tmpV.y));
        fWP += World.Rnd().nextFloat(-20F, 20F);
        while (fWP < -180F)
            fWP += 180F;
        while (fWP > 180F)
            fWP -= 180F;
        return fWP;
    }

	public CockpitME_264() {
		super("3DO/Cockpit/Me-264/hier.him", "he111");
		this.setOld = new Variables();
		this.setNew = new Variables();
		this.w = new Vector3f();
		this.w = new Vector3f();
		this.Pn = new Point3d();
		this.cockpitNightMats = new String[] { "CompGrad", "Fl20274", "Fl20342na", "Fl20342", "Fl20516", "Fl20556",
				"Fl20570", "Fl20572", "Fl20723_1185", "Fl20723_1850", "Fl20723_200na", "Fl20723_200", "Fl20723_640",
				"Fl20841", "Fl22231", "Fl22316", "Fl22320", "Fl22334b", "Fl22334c", "Fl22382", "Fl22412", "Fl22413",
				"Fl22561", "Fl23885na", "Fl23885", "Fl30489", "Fl30532", "Fl32336", "Gauge19", "Gauge20", "Ln27002",
				"Ln28330b", "Ln28330", "NeedlesnLights", "Nr92182B1na", "Voltmeters" };
		if (BaseGameVersion.is411orLater()) {
            this.hidePilot = true;
            this.limits6DoF = new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.1F, -0.07F, 0.03F, -0.03F };
		}
		this.setNightMats(false);
		this.interpPut(new Interpolater(), (Object) null, Time.current(), (Message) null);
	}

	public void reflectWorldToInstruments(float f) {
		this.mesh.chunkSetAngles("Z_Lotfe7", ((ME_264) this.fm.actor).fSightCurSideslip * -10F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng1Starter",
				this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng2Starter",
				this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng3Starter",
				this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng4Starter",
				this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng1Diseng", this.fm.EI.engines[0].getRPM() <= 10F ? 0.0F : 42F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng2Diseng", this.fm.EI.engines[0].getRPM() <= 10F ? 0.0F : 42F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng3Diseng", this.fm.EI.engines[1].getRPM() <= 10F ? 0.0F : 42F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Eng4Diseng", this.fm.EI.engines[1].getRPM() <= 10F ? 0.0F : 42F, 0.0F, 0.0F);
		float gearL = this.fm.CT.getGear();
        float gearR = this.fm.CT.getGear();
        float gearC = this.fm.CT.getGear();
        if (BaseGameVersion.is412orLater()) {
            gearL = this.fm.CT.getGearL();
            gearR = this.fm.CT.getGearR();
            gearC = this.fm.CT.getGearC();
        }
		
		this.mesh.chunkVisible("F_LGearsDown", gearL == 1.0F && this.fm.Gears.lgear);
		this.mesh.chunkVisible("F_RGearsDown", gearR == 1.0F && this.fm.Gears.rgear);
		this.mesh.chunkVisible("F_AllGearsDown", gearL == 1.0F && gearR == 1.0F
				&& this.fm.Gears.lgear && this.fm.Gears.rgear);
		this.mesh.chunkVisible("F_TailGearsDown", gearC == 1.0F && this.fm.Gears.cgear);
		this.mesh.chunkVisible("F_LGearsUp", gearL == 0.0F);
		this.mesh.chunkVisible("F_RGearsUp", gearR == 0.0F);
		this.mesh.chunkVisible("F_AllGearsUp", gearR == 0.0F && gearL == 0.0F);
		this.mesh.chunkVisible("F_TailGearsUp", gearC == 0.0F);
		this.mesh.chunkSetAngles("Z_Gears", gearL != 1.0F || gearR != 1.0F
				|| !this.fm.Gears.lgear || !this.fm.Gears.rgear ? 0.0F : 90F, 0.0F, 0.0F);
		float f1 = this.fm.CT.getFlap();
		this.mesh.chunkVisible("F_FlapsUp", f1 < 0.1F);
		this.mesh.chunkVisible("F_FlapsUnf", f1 > 0.1F && f1 < 0.5F);
		this.mesh.chunkVisible("F_FlapsExt", f1 > 0.5F);
		float f2 = 0.0F;
		if (f1 > 0.5F) {
			f2 = 90F;
		} else if (f1 > 0.1F && f1 < 0.5F) {
			f2 = 45F;
		}
		this.mesh.chunkSetAngles("Z_Flaps", f2, 0.0F, 0.0F);
		this.mesh.chunkVisible("F_EngOverheatL", this.fm.AS.astateEngineStates[0] > 4 || this.fm.AS.astateEngineStates[1] > 4);
		this.mesh.chunkVisible("F_EngOverheatR", this.fm.AS.astateEngineStates[2] > 4 || this.fm.AS.astateEngineStates[3] > 4);
		boolean flag = false;
		if (!this.fm.CT.bHasFlapsControl) {
			flag = true;
		} else {
			float f3 = Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH());
			if (f1 > 0.21F && f3 > 270F && (f3 - 270F) * f1 > 8F) {
				flag = true;
			}
		}
		this.mesh.chunkVisible("F_Fl32558", flag);
		if (this.setNew.pictElev < 0.0F) {
			this.mesh.chunkSetAngles("Z_CColumn", 0.0F, 15F * -this.setNew.pictElev, 0.0F);
		} else {
			this.mesh.chunkSetAngles("Z_CColumn", 0.0F, 15F * -this.setNew.pictElev, 0.0F);
		}
		this.mesh.chunkSetAngles("Z_Handle", 93F * this.setNew.pictAiler, 0.0F, 0.0F);
		float f4 = super.fm.CT.getRudder();
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
		float f9 = Geom.RAD2DEG((float) Math.acos((0.038666129112243652D - 0.18320585787296295D * d)
				/ Math.sqrt(0.035059455782175064D - 0.014167722314596176D * d)));
		float f10 = 180F - f8 - f9;
		float f11 = 62.8136F - f9;
		float f12 = f10 - 10.82074F;
		this.mesh.chunkSetAngles("Z_Kachalka2L", f7, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Tiaga3L", f11, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_CilindrL", 0.0F, f12, 0.0F);
		float f13 = 20F * f6;
		float f14 = 106.3657F - f13;
		double d1 = Math.cos(Geom.DEG2RAD(f14));
		float f15 = Geom.RAD2DEG((float) Math.acos((0.038666129112243652D - 0.18320585787296295D * d1)
				/ Math.sqrt(0.035059455782175064D - 0.014167722314596176D * d1)));
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
		double d4 = Math.sqrt(
				0.035435102880001068D + d3 * d3 - 0.37648427486419678D * d3 * (float) Math.cos(Geom.DEG2RAD(f21)));
		float f22 = Geom.RAD2DEG((float) Math.acos((0.042587004601955414D - d4 * d4) / 0.031839244067668915D));
		float f23 = 89.43554F - f22;
		this.mesh.chunkSetAngles("Z_Tiaga2L", f23, 0.0F, 0.0F);
		float f24 = (float) Math.acos((0.035435102880001068D + d4 * d4 - d3 * d3) / (0.37648427486419678D * d4));
		float f25 = (float) Math
				.acos((0.0071521135978400707D + d4 * d4 - 0.035434890538454056D) / (0.16914033889770508D * d4));
		float f26 = Geom.RAD2DEG(f24 + f25) - 90.56514F;
		this.mesh.chunkSetAngles("Z_LBrake", 0.0F, 0.0F, f26);
		double d5 = Math.cos(Geom.DEG2RAD(142.0612F + f13));
		double d6 = Math.sqrt(0.0040516676381230354D - 0.003931721206754446D * d5);
		float f27 = Geom.RAD2DEG((float) Math.acos((0.039198901504278183D - 0.050150908529758453D * d5) / d6));
		float f28 = 21.38197F - f27;
		float f29 = 89.43449F + f28 - 35F * f4;
		double d7 = Math.sqrt(
				0.035435102880001068D + d6 * d6 - 0.37648427486419678D * d6 * (float) Math.cos(Geom.DEG2RAD(f29)));
		float f30 = Geom.RAD2DEG((float) Math.acos((0.042587004601955414D - d7 * d7) / 0.031839244067668915D));
		float f31 = 89.43554F - f30;
		this.mesh.chunkSetAngles("Z_Tiaga2R", f31, 0.0F, 0.0F);
		float f32 = (float) Math.acos((0.035435102880001068D + d7 * d7 - d6 * d6) / (0.37648427486419678D * d7));
		float f33 = (float) Math
				.acos((0.0071521135978400707D + d7 * d7 - 0.035434890538454056D) / (0.16914033889770508D * d7));
		float f34 = Geom.RAD2DEG(f32 + f33) - 90.56514F;
		this.mesh.chunkSetAngles("Z_RBrake", 0.0F, 0.0F, f34);
		this.mesh.chunkSetAngles("Z_VatorTrim", this.cvt(this.setNew.elevTrim, -0.5F, 0.5F, -750F, 750F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_RudderTrim", -this.cvt(this.setNew.rudderTrim, -0.5F, 0.5F, -750F, 750F), 0.0F,
				0.0F);
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
		for (int engineIndex = 1; engineIndex < 5; engineIndex++) {
	        this.mesh.chunkSetAngles("Z_Throttle" + engineIndex, 41.8F * this.setNew.throttle[engineIndex-1], 0.0F, 0.0F);
	        this.mesh.chunkSetAngles("Z_BMBThrot" + engineIndex, 70F * this.setNew.throttle[engineIndex-1], 0.0F, 0.0F);
		    this.mesh.chunkSetAngles("Z_PropPitch" + engineIndex, -this.setNew._prop[engineIndex-1], 0.0F, 0.0F);
		    float fPhi = (float) Math.toDegrees(this.fm.EI.engines[engineIndex-1].getPropPhi() - this.fm.EI.engines[engineIndex-1].getPropPhiMin());
		    fPhi = (int) (fPhi / 0.2F) * 0.2F;
	        this.mesh.chunkSetAngles("Z_N_ClockP" + engineIndex + "_M", fPhi * 60F, 0.0F, 0.0F);
	        this.mesh.chunkSetAngles("Z_N_ClockP" + engineIndex + "_H", fPhi * 5F, 0.0F, 0.0F);
	        this.mesh.chunkSetAngles("Z_N_AiFuePress" + engineIndex, -this.cvt(this.setNew.pictManf[engineIndex-1], 0.6F, 1.8F, 0.0F, 330F), 0.0F,
	                0.0F);
	        this.mesh.chunkSetAngles("Z_N_RPM" + engineIndex, -this.floatindex(this.cvt(this.fm.EI.engines[engineIndex-1].getRPM(), 400F, 3600F, 0.0F, 32F), Fl20274_Scale),
	                0.0F, 0.0F);
	        this.mesh.chunkSetAngles("Z_N_E" + engineIndex + "CoolTemp",
	                -this.floatindex(this.cvt(this.fm.EI.engines[engineIndex-1].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F,
	                0.0F);
	        this.mesh.chunkSetAngles("Z_N_E" + engineIndex + "OilTemp",
	                -this.floatindex(this.cvt(this.fm.EI.engines[engineIndex-1].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F,
	                0.0F);
    	    this.mesh.chunkSetAngles("Z_N_E" + engineIndex + "_OilPress",
    	            this.cvt(1.0F + 0.05F * this.fm.EI.engines[engineIndex-1].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
		}
		this.mesh.chunkSetAngles("Z_BombRelease", this.cvt(this.setNew.bombDoor, 0.0F, 1.0F, 0.0F, 90F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Clock3_H", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Clock3_M", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F,
				0.0F);
		this.mesh.chunkSetAngles("Z_N_Clock3_S",
				-this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
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
		this.mesh.chunkSetAngles("Z_N_AirSpeed1",
				-this.floatindex(
						this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 750F, 0.0F, 15F),
						IAS_Scale),
				0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AirSpeed2",
				-this.floatindex(
						this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 750F, 0.0F, 15F),
						IAS_Scale),
				0.0F, 0.0F);
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
		if (BaseGameVersion.is410orLater()) this.mesh.chunkVisible("F_N_AFN2_RED", this.isOnBlindLandingMarker());
		this.mesh.chunkSetAngles("Z_N_FuelConsom", -this.cvt(0.5F * this.setNew.consumption, 0.0F, 500F, 0.0F, 255.5F),
				0.0F, 0.0F);
//		int i = ((ME_264) this.aircraft()).iRust;
		float f41 = this.fm.M.fuel;
		this.mesh.chunkSetAngles("Z_N_Fuel6", -this.cvt(f41, 3424.015F, 4268.9F, 0.0F, 72.63291F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Fuel2", -this.cvt(f41, 2505.015F, 3424.015F, 0.0F, 71.6875F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Fuel3", -this.cvt(f41, 2505.015F, 3424.015F, 0.0F, 71.6875F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_Fuel1", -this.cvt(f41, 0.0F, 844.89F, 0.0F, 72.63291F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_FuelQuant", -this.cvt(f41, 0.0F, this.fm.M.maxFuel, 37F, 84F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AirPress1", -170F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AirPress2", -170F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_N_AirPress3", -170F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_LandingLight", this.fm.AS.bLandingLightOn ? 90F : 0.0F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_NavLights", this.fm.AS.bNavLightsOn ? 90F : 0.0F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_CockpitLight", this.cockpitLightControl ? 90F : 0.0F, 0.0F, 0.0F);
		if (BaseGameVersion.is410orLater() && this.useRealisticNavigationInstruments()) {
			this.mesh.chunkSetAngles("Fl22334b",
					-(this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f)), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Fl22334c", this.setNew.waypointAzimuth.getDeg(f) + 90F, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Fl22338b",
					-(this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f)), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Fl22338c", this.setNew.waypointAzimuth.getDeg(f) + 90F, 0.0F, 0.0F);
		} else {
			this.mesh.chunkSetAngles("Fl22334b", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Fl22334c", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Fl22338b", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Fl22338c", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
		}
		this.mesh.chunkSetAngles("Z_Course2a", this.setNew.azimuth.getDeg(f) + 90F, 0.0F, 0.0F);
		float f50;
		for (f50 = -this.fm.turret[3].tu[0]; f50 < -180F; f50 += 360F) {
			;
		}
		for (; f50 > 180F; f50 -= 360F) {
			;
		}
		float f51 = this.fm.turret[3].tu[1];
		this.mesh.chunkSetAngles("Z_TurretA", -f50, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_TurretB", 0.0F, 0.0F, -f51);
		this.mesh.chunkSetAngles("Z_tLink", 0.0F, 0.0F,
				this.floatindex(this.cvt(f51, 0.0F, 90F, 0.0F, 18F), Turret_Z1));
		this.mesh.chunkSetAngles("Z_tHandle", 0.0F, 0.0F,
				-this.floatindex(this.cvt(f51, 0.0F, 90F, 0.0F, 18F), Turret_Z2));
		this.mesh.chunkSetAngles("Z_tReviVal", 0.0F, -f50 * 5F, 0.0F);
		this.resetYPRmodifier();
		Cockpit.xyz[2] = 0.185F * this.setNew.AirEnemy;
		this.mesh.chunkSetLocate("Z_TurretA_Seat", Cockpit.xyz, Cockpit.ypr);
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 2) != 0) {
			this.mesh.chunkVisible("xGlassDm1", true);
		}
		if ((this.fm.AS.astateCockpitState & 1) != 0) {
			this.mesh.chunkVisible("xHullDm1", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
			this.mesh.chunkVisible("xGlass1", false);
			this.mesh.chunkVisible("xGlass1_dmg", true);
		}
		if ((this.fm.AS.astateCockpitState & 4) != 0) {
			this.mesh.chunkVisible("xGlass2", false);
			this.mesh.chunkVisible("xGlass2_dmg", true);
		}
		if ((this.fm.AS.astateCockpitState & 8) != 0) {
			this.mesh.chunkVisible("xGlassDm2", true);
		}
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
		super.cockpitLightControl = !super.cockpitLightControl;
		if (super.cockpitLightControl) {
			this.setNightMats(true);
		} else {
			this.setNightMats(false);
		}
	}

	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	public Vector3f w;
	private Point3d Pn;
	private static final float Turret_Z1[] = { 0.0F, 4.235F, 8.64F, 13.21F, 17.92F, 22.79F, 27.82F, 32.99F, 38.29F,
			43.75F, 49.36F, 55.12F, 61.05F, 67.13F, 73.4F, 79.87F, 86.55F, 93.45F, 100.61F };
	private static final float Turret_Z2[] = { 0.0F, 2.125F, 4.32F, 6.58F, 8.87F, 11.188F, 13.53F, 15.88F, 18.21F,
			20.52F, 22.78F, 24.98F, 27.105F, 29.13F, 31.03F, 32.76F, 34.33F, 35.68F, 36.78F };
	private static final float IAS_Scale[] = { 0.0F, 8F, 18.4F, 41.26F, 67.26F, 94.36F, 119.58F, 141F, 166.1F, 190.43F,
			216.05F, 241.18F, 267.5F, 293.8F, 318.19F, 341.7F };
	private static final float Fl20342_Scale[] = { 0.0F, 5.5F, 11F, 17.5F, 25F, 33F, 41.5F, 50.5F, 59.5F, 67F, 73.5F,
			80.25F, 85.5F, 90F };
	private static final float Fl20274_Scale[] = { 0.0F, 4.25F, 8F, 12F, 17F, 23F, 28.5F, 34.5F, 42F, 51F, 58.5F, 69F,
			79F, 91F, 103F, 114.5F, 128F, 140F, 152.5F, 164.5F, 175F, 186.5F, 195.5F, 205F, 214F, 222.5F, 230.5F, 239F,
			246.5F, 253.5F, 260.5F, 265F, 269.5F };

	static {
	    Class class1 = CockpitME_264.class;
        Property.set(class1, "normZN", 1.2F);
        Property.set(class1, "gsZN", 1.2F);
	}

}
