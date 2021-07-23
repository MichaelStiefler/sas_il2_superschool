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
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.BaseGameVersion;
import com.maddox.sas1946.il2.util.CrossVersion;

public class CockpitME_264_Bombardier extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (CockpitME_264_Bombardier.this.fm != null) {
				CockpitME_264_Bombardier.this.setTmp = CockpitME_264_Bombardier.this.setOld;
				CockpitME_264_Bombardier.this.setOld = CockpitME_264_Bombardier.this.setNew;
				CockpitME_264_Bombardier.this.setNew = CockpitME_264_Bombardier.this.setTmp;
				float f = CockpitME_264_Bombardier.this.waypointAzimuth();
	                CockpitME_264_Bombardier.this.setNew.beaconDirection = (10F
	                        * CockpitME_264_Bombardier.this.setOld.beaconDirection
	                        + CockpitME_264_Bombardier.this.getBeaconDirection()) / 11F;
	                CockpitME_264_Bombardier.this.setNew.beaconRange = (10F
	                        * CockpitME_264_Bombardier.this.setOld.beaconRange
	                        + CockpitME_264_Bombardier.this.getBeaconRange()) / 11F;
				    if (CockpitME_264_Bombardier.this.useRealisticNavigationInstruments()) {
	                    CockpitME_264_Bombardier.this.setNew.waypointAzimuth.setDeg(f - 90F);
	                    CockpitME_264_Bombardier.this.setOld.waypointAzimuth.setDeg(f - 90F);
	                    CockpitME_264_Bombardier.this.setNew.radioCompassAzimuth.setDeg(
	                            CockpitME_264_Bombardier.this.setOld.radioCompassAzimuth.getDeg(0.02F),
	                            CockpitME_264_Bombardier.this.radioCompassAzimuthInvertMinus()
	                                    - CockpitME_264_Bombardier.this.setOld.azimuth.getDeg(1.0F) - 90F);
	                } else {
	                    CockpitME_264_Bombardier.this.setNew.waypointAzimuth.setDeg(
	                            CockpitME_264_Bombardier.this.setOld.waypointAzimuth.getDeg(0.1F),
	                            f - CockpitME_264_Bombardier.this.setOld.azimuth.getDeg(1.0F));
				    }
				CockpitME_264_Bombardier.this.setNew.azimuth.setDeg(
						CockpitME_264_Bombardier.this.setOld.azimuth.getDeg(1.0F),
						CockpitME_264_Bombardier.this.fm.Or.azimut());
				CockpitME_264_Bombardier.this.setNew.pictAiler = 0.85F
						* CockpitME_264_Bombardier.this.setOld.pictAiler
						+ 0.15F * CockpitME_264_Bombardier.this
								.cvt(CockpitME_264_Bombardier.this.fm.CT.AileronControl, -1F, 1.0F, -1F, 1.0F);
				CockpitME_264_Bombardier.this.setNew.pictElev = 0.85F
						* CockpitME_264_Bombardier.this.setOld.pictElev
						+ 0.15F * CockpitME_264_Bombardier.this
								.cvt(CockpitME_264_Bombardier.this.fm.CT.ElevatorControl, -1F, 1.0F, -1F, 1.0F);
				CockpitME_264_Bombardier.this.setNew.elevTrim = 0.85F
						* CockpitME_264_Bombardier.this.setOld.elevTrim
						+ 0.15F * CockpitME_264_Bombardier.this.fm.CT.trimElevator;
				CockpitME_264_Bombardier.this.setNew.rudderTrim = 0.85F
						* CockpitME_264_Bombardier.this.setOld.rudderTrim
						+ 0.15F * CockpitME_264_Bombardier.this.fm.CT.trimRudder;
				CockpitME_264_Bombardier.this.setNew.ailTrim = 0.85F * CockpitME_264_Bombardier.this.setOld.ailTrim
						+ 0.15F * CockpitME_264_Bombardier.this.fm.CT.trimAileron;
				
                float fCons = 0F;
                for (int engineIndex = 0; engineIndex < 4; engineIndex++) {
                    CockpitME_264_Bombardier.this.setNew.throttle[engineIndex] = 0.85F * CockpitME_264_Bombardier.this.setOld.throttle[engineIndex]
                            + CockpitME_264_Bombardier.this.fm.EI.engines[engineIndex].getControlThrottle() * 0.15F;
                    if (Math.toDegrees(CockpitME_264_Bombardier.this.fm.EI.engines[engineIndex].getPropPhi()) < 36D) {
                        CockpitME_264_Bombardier.this.setNew.prop[engineIndex] = 0.85F * CockpitME_264_Bombardier.this.setOld.prop[engineIndex]
                                + ((FlightModelMain) CockpitME_264_Bombardier.this.fm).EI.engines[engineIndex].getControlProp() * 0.15F;
                        CockpitME_264_Bombardier.this.setNew._prop[engineIndex] = CockpitME_264_Bombardier.this.cvt(CockpitME_264_Bombardier.this.setNew.prop[engineIndex],
                                0.0F, 1.0F, -22F, -63F);
                    } else {
                        CockpitME_264_Bombardier.this.setNew.prop[engineIndex] = 0.85F * CockpitME_264_Bombardier.this.setOld.prop[engineIndex];
                        CockpitME_264_Bombardier.this.setNew._prop[engineIndex] = CockpitME_264_Bombardier.this.cvt(CockpitME_264_Bombardier.this.setNew.prop[engineIndex],
                                0.0F, 1.0F, 0.0F, -63F);
                    }
                    CockpitME_264_Bombardier.this.setNew.pictManf[engineIndex] = 0.95F * CockpitME_264_Bombardier.this.setOld.pictManf[engineIndex]
                            + 0.05F * CockpitME_264_Bombardier.this.fm.EI.engines[engineIndex].getManifoldPressure();
                    fCons += CockpitME_264_Bombardier.this.fm.EI.engines[engineIndex].getEngineForce().x;
                }
				CockpitME_264_Bombardier.this.w.set(CockpitME_264_Bombardier.this.fm.getW());
				CockpitME_264_Bombardier.this.fm.Or.transform(CockpitME_264_Bombardier.this.w);
				CockpitME_264_Bombardier.this.setNew.turn = (12F * CockpitME_264_Bombardier.this.setOld.turn
						+ CockpitME_264_Bombardier.this.w.z) / 13F;
				CockpitME_264_Bombardier.this.setNew.altimeter = 0.85F
						* CockpitME_264_Bombardier.this.setOld.altimeter
						+ CockpitME_264_Bombardier.this.fm.getAltitude() * 0.15F;
				CockpitME_264_Bombardier.this.setNew.vspeed = (99F * CockpitME_264_Bombardier.this.setOld.vspeed
						+ CockpitME_264_Bombardier.this.fm.getVertSpeed()) / 100F;
				float f1 = CockpitME_264_Bombardier.this.fm.Or.getKren();
				float f2 = CockpitME_264_Bombardier.this.fm.Or.getTangage();
				if (f1 > 55F || f1 < -55F || f2 < -55F || f2 > 55F) {
					CockpitME_264_Bombardier.this.Pn.z = 250D;
				} else {
					CockpitME_264_Bombardier.this.Pn.set(CockpitME_264_Bombardier.this.fm.Loc);
					CockpitME_264_Bombardier.this.Pn.z = CockpitME_264_Bombardier.this.fm.getAltitude()
							- Engine.cur.land.HQ(((Tuple3d) CockpitME_264_Bombardier.this.Pn).x,
									((Tuple3d) CockpitME_264_Bombardier.this.Pn).y);
					double d = CockpitME_264_Bombardier.this.Pn.z * (float) Math.tan(Geom.DEG2RAD(f1));
					double d1 = CockpitME_264_Bombardier.this.Pn.z * (float) Math.tan(Geom.DEG2RAD(f2));
					CockpitME_264_Bombardier.this.Pn.z = (float) Math.sqrt(d * d + d1 * d1
							+ CockpitME_264_Bombardier.this.Pn.z * CockpitME_264_Bombardier.this.Pn.z);
					if (CockpitME_264_Bombardier.this.fm.CT.getGear() > 0.5F) {
						CockpitME_264_Bombardier.this.Pn.z = CockpitME_264_Bombardier.this
								.cvt((float) CockpitME_264_Bombardier.this.Pn.z, 0.0F, 150F, 0.0F, 250F);
					} else {
						CockpitME_264_Bombardier.this.Pn.z = CockpitME_264_Bombardier.this
								.cvt((float) CockpitME_264_Bombardier.this.Pn.z, 0.0F, 750F, 0.0F, 250F);
					}
				}
				CockpitME_264_Bombardier.this.setNew.AFN101 = 0.9F * CockpitME_264_Bombardier.this.setOld.AFN101
						+ 0.1F * (float) CockpitME_264_Bombardier.this.Pn.z;
				float f3 = CockpitME_264_Bombardier.this.setNew.prevFuel - CockpitME_264_Bombardier.this.fm.M.fuel;
				CockpitME_264_Bombardier.this.setNew.prevFuel = CockpitME_264_Bombardier.this.fm.M.fuel;
				f3 /= 0.72F;
				f3 /= Time.tickLenFs();
				f3 *= 3600F;
				CockpitME_264_Bombardier.this.setNew.cons = 0.9F * CockpitME_264_Bombardier.this.setOld.cons
						+ 0.1F * f3;
				float f6 = CockpitME_264_Bombardier.this.setNew.cons;
				CockpitME_264_Bombardier.this.setNew.consumption = 0.9F * CockpitME_264_Bombardier.this.setOld.consumption
                        + 0.1F * (f6 * fCons / 4F) / (fCons + 1.0F);
				float f7 = ((ME_264) CockpitME_264_Bombardier.this.aircraft()).fSightCurForwardAngle;
				float f8 = ((ME_264) CockpitME_264_Bombardier.this.aircraft()).fSightCurSideslip * 10F;
				CockpitME_264_Bombardier.this.mesh.chunkSetAngles("RollGyro", 0.0F, 0.0F, 0.0F);
				CockpitME_264_Bombardier.this.mesh.chunkSetAngles("BlackBox", -f8, 0.0F, f7);
				if (CockpitME_264_Bombardier.this.bEntered) {
                        HookPilot.cur().setSimpleAimOrient(CockpitME_264_Bombardier.this.aAim + f8,
                                CockpitME_264_Bombardier.this.tAim + f7, 0.0F);
				}
			}
			CockpitME_264_Bombardier.this.setNew.bombDoor = 0.9F * CockpitME_264_Bombardier.this.setOld.bombDoor
					+ 0.1F * CockpitME_264_Bombardier.this.fm.CT.getBayDoor();
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

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			HookPilot hookpilot = HookPilot.current;
			hookpilot.doAim(false);
    		hookpilot.setMinMax(155F, -89F, 89F);
    		Point3d point3d = new Point3d();
    		point3d.set(0.9495442D, 0.0022865D, -0.2855634D);
    		hookpilot.setTubeSight(point3d);
			return true;
		} else
			return false;
	}

	protected void doFocusLeave() {
		if (!this.isFocused())
			return;
		else {
			this.leave();
			super.doFocusLeave();
			return;
		}
	}

	    private void enter() {
	        saveFov = Main3D.FOVX;
	        CmdEnv.top().exec("fov 23.913");
	        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
	        HookPilot hookpilot = HookPilot.current;
	        if (hookpilot.isPadlock())
	            hookpilot.stopPadlock();
	        hookpilot.doAim(true);
	        hookpilot.setSimpleUse(true);
	        hookpilot.setSimpleAimOrient(aAim, tAim, 0.0F);
	        HotKeyEnv.enable("PanView", false);
	        HotKeyEnv.enable("SnapView", false);
	        bEntered = true;
	    }

	    private void leave() {
	        if (!bEntered)
	            return;
	        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
	        CmdEnv.top().exec("fov " + saveFov);
	        HookPilot hookpilot1 = HookPilot.current;
	        hookpilot1.doAim(false);
	        hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
	        hookpilot1.setSimpleUse(false);
	        boolean flag = HotKeyEnv.isEnabled("aircraftView");
	        HotKeyEnv.enable("PanView", flag);
	        HotKeyEnv.enable("SnapView", flag);
	        bEntered = false;
	    }

	public void destroy() {
		super.destroy();
		this.leave();
	}

	   public void doToggleAim(boolean flag) {
	        if (!isFocused())
	            return;
	        if (isToggleAim() == flag)
	            return;
	        if (flag)
	            enter();
	        else
                leave();
	    }

	public CockpitME_264_Bombardier() {
		super("3DO/Cockpit/Me-264-Bombardier/hier.him", "he111");
		this.setOld = new Variables();
		this.setNew = new Variables();
		this.w = new Vector3f();
		this.w = new Vector3f();
		this.Pn = new Point3d();
		this.bEntered = false;
		try {
			Loc loc = new Loc();
			HookNamed hooknamed = new HookNamed(this.mesh, "CAMERAAIM");
			hooknamed.computePos(this, this.pos.getAbs(), loc);
			this.aAim = loc.getOrient().getAzimut();
			this.tAim = loc.getOrient().getTangage();
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		this.cockpitNightMats = new String[] { "CompGrad", "Fl20274", "Fl20342na", "Fl20342", "Fl20516", "Fl20516_1",
				"Fl20556", "Fl20570", "Fl20572", "Fl20723_1000", "Fl20723_1185", "Fl20723_1850", "Fl20723_200na",
				"Fl20723_200", "Fl20723_640", "Fl20841", "Fl22231", "Fl22316", "Fl22320", "Fl22334b", "Fl22334c",
				"Fl22382", "Fl22412", "Fl22413", "Fl22561", "Fl23885na", "Fl23885", "Fl30489", "Fl30532", "Fl32336",
				"Gauge19", "Gauge20", "Ln27002", "Ln28330b", "Ln28330", "NeedlesnLights", "Nr92182B1na", "Voltmeters" };
        this.hidePilot = true;
		this.setNightMats(false);
		this.interpPut(new Interpolater(), null, Time.current(), null);
		this.setOld = new Variables();
		this.setNew = new Variables();
		CrossVersion.setPrintCompassHeading(this, true);
	}

	public void reflectWorldToInstruments(float f) {
		if (this.bEntered) {
			float f1 = 45F * (float) Math.tan(((ME_264) this.fm.actor).fSightCurForwardAngle * 0.01745329F);
			this.mesh.chunkSetAngles("zLotfe7C", f1, 0.0F, 0.0F);
			float f3 = ((ME_264) this.fm.actor).fSightCurForwardAngle;
			if (f3 > 80F) {
				f3 = 80F;
			}
			this.mesh.chunkSetAngles("zLotfeSignal", -45F * (float) Math.tan(f3 * 0.01745329F), 0.0F, 0.0F);
			this.mesh.chunkVisible("BlackBox", true);
			this.mesh.chunkVisible("Lotfe7C", true);
			this.mesh.chunkVisible("zLotfe7C", true);
			this.mesh.chunkVisible("zLotfeSignal", true);
		} else {
			this.mesh.chunkVisible("BlackBox", false);
			this.mesh.chunkVisible("Lotfe7C", false);
			this.mesh.chunkVisible("zLotfe7C", false);
			this.mesh.chunkVisible("zLotfeSignal", false);
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
			float f2 = this.fm.CT.getFlap();
			this.mesh.chunkVisible("F_FlapsUp", f2 < 0.1F);
			this.mesh.chunkVisible("F_FlapsUnf", f2 > 0.1F && f2 < 0.5F);
			this.mesh.chunkVisible("F_FlapsExt", f2 > 0.5F);
			float f4 = 0.0F;
			if (f2 > 0.5F) {
				f4 = 90F;
			} else if (f2 > 0.1F && f2 < 0.5F) {
				f4 = 45F;
			}
			this.mesh.chunkSetAngles("Z_Flaps", f4, 0.0F, 0.0F);
			this.mesh.chunkVisible("F_EngOverheatL", this.fm.AS.astateEngineStates[0] > 4);
			this.mesh.chunkVisible("F_EngOverheatR", this.fm.AS.astateEngineStates[1] > 4);
			boolean flag = false;
			if (!this.fm.CT.bHasFlapsControl) {
				flag = true;
			} else {
				float f5 = Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH());
				if (f2 > 0.21F && f5 > 270F && (f5 - 270F) * f2 > 8F) {
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
			float f6 = super.fm.CT.getRudder();
			this.mesh.chunkSetAngles("Z_RichagL", -35F * f6, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_Tiaga4L", 35F * f6, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_RichagR", 35F * f6, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_Koromislo1L", 0.0F, 0.0F, 22F * f6);
			this.mesh.chunkSetAngles("Z_Koromislo2L", 0.0F, 0.0F, 22F * f6);
			this.mesh.chunkSetAngles("Z_PedalBomL", 0.0F, 0.0F, -22F * f6);
			this.mesh.chunkSetAngles("Z_Koromislo1R", 0.0F, 0.0F, -22F * f6);
			this.mesh.chunkSetAngles("Z_Koromislo2R", 0.0F, 0.0F, -22F * f6);
			this.mesh.chunkSetAngles("Z_PedalBomR", 0.0F, 0.0F, 22F * f6);
			float f7 = this.fm.CT.getBrake();
			float f8 = this.fm.CT.getBrake();
			float f9 = 20F * f7;
			float f10 = 106.3657F - f9;
			double d = Math.cos(Geom.DEG2RAD(f10));
			float f11 = Geom.RAD2DEG((float) Math.acos((0.038666129112243652D - 0.18320585787296295D * d)
					/ Math.sqrt(0.035059455782175064D - 0.014167722314596176D * d)));
			float f12 = 180F - f10 - f11;
			float f13 = 62.8136F - f11;
			float f14 = f12 - 10.82074F;
			this.mesh.chunkSetAngles("Z_Kachalka2L", f9, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_Tiaga3L", f13, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_CilindrL", 0.0F, f14, 0.0F);
			float f15 = 20F * f8;
			this.mesh.chunkSetAngles("Z_Kachalka2R", f15, 0.0F, 0.0F);
			double d1 = Math.cos(Geom.DEG2RAD(142.0612F + f9));
			double d2 = Math.sqrt(0.0040516676381230354D - 0.003931721206754446D * d1);
			float f16 = Geom.RAD2DEG((float) Math.acos((0.039198901504278183D - 0.050150908529758453D * d1) / d2));
			float f17 = 21.38197F - f16;
			float f18 = 89.43449F + f17 + 35F * f6;
			double d3 = Math.sqrt(
					0.035435102880001068D + d2 * d2 - 0.37648427486419678D * d2 * (float) Math.cos(Geom.DEG2RAD(f18)));
			float f19 = Geom.RAD2DEG((float) Math.acos((0.042587004601955414D - d3 * d3) / 0.031839244067668915D));
			float f20 = 89.43554F - f19;
			this.mesh.chunkSetAngles("Z_Tiaga2L", f20, 0.0F, 0.0F);
			float f21 = (float) Math.acos((0.035435102880001068D + d3 * d3 - d2 * d2) / (0.37648427486419678D * d3));
			float f22 = (float) Math
					.acos((0.0071521135978400707D + d3 * d3 - 0.035434890538454056D) / (0.16914033889770508D * d3));
			float f23 = Geom.RAD2DEG(f21 + f22) - 90.56514F;
			this.mesh.chunkSetAngles("Z_LBrake", 0.0F, 0.0F, f23);
			double d4 = Math.cos(Geom.DEG2RAD(142.0612F + f15));
			double d5 = Math.sqrt(0.0040516676381230354D - 0.003931721206754446D * d4);
			float f24 = Geom.RAD2DEG((float) Math.acos((0.039198901504278183D - 0.050150908529758453D * d4) / d5));
			float f25 = 21.38197F - f24;
			float f26 = 89.43449F + f25 - 35F * f6;
			double d6 = Math.sqrt(
					0.035435102880001068D + d5 * d5 - 0.37648427486419678D * d5 * (float) Math.cos(Geom.DEG2RAD(f26)));
			float f27 = Geom.RAD2DEG((float) Math.acos((0.042587004601955414D - d6 * d6) / 0.031839244067668915D));
			float f28 = 89.43554F - f27;
			this.mesh.chunkSetAngles("Z_Tiaga2R", f28, 0.0F, 0.0F);
			float f29 = (float) Math.acos((0.035435102880001068D + d6 * d6 - d5 * d5) / (0.37648427486419678D * d6));
			float f30 = (float) Math
					.acos((0.0071521135978400707D + d6 * d6 - 0.035434890538454056D) / (0.16914033889770508D * d6));
			float f31 = Geom.RAD2DEG(f29 + f30) - 90.56514F;
			this.mesh.chunkSetAngles("Z_RBrake", 0.0F, 0.0F, f31);
			this.mesh.chunkSetAngles("Z_VatorTrim", this.cvt(this.setNew.elevTrim, -0.5F, 0.5F, -750F, 750F), 0.0F,
					0.0F);
			this.mesh.chunkSetAngles("Z_RudderTrim", -this.cvt(this.setNew.rudderTrim, -0.5F, 0.5F, -750F, 750F), 0.0F,
					0.0F);
			this.mesh.chunkSetAngles("Z_AilronTrim", -this.cvt(this.setNew.ailTrim, -0.5F, 0.5F, -750F, 750F), 0.0F,
					0.0F);
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
	        }

			this.mesh.chunkSetAngles("Z_BombRelease", this.cvt(this.setNew.bombDoor, 0.0F, 1.0F, 0.0F, 90F), 0.0F,
					0.0F);
			this.mesh.chunkSetAngles("Z_N_Clock3_H", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F,
					0.0F);
			this.mesh.chunkSetAngles("Z_N_Clock3_M", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F),
					0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Clock3_S",
					-this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_OxPress1", -200F, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_OxPress2", -200F, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_OxPress3", -200F, 0.0F, 0.0F);
			float f34 = this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 28F, -28F);
			this.mesh.chunkSetAngles("Z_N_Turn1", f34, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Turn2", f34, 0.0F, 0.0F);
			f34 = this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 17F, -17F);
			this.mesh.chunkSetAngles("Z_N_Turn3", f34, 0.0F, 0.0F);
			float f35 = -this.getBall(8D);
			this.mesh.chunkSetAngles("Z_N_Bank1", this.cvt(f35, -4F, 4F, -10.5F, 10.5F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Bank2", this.cvt(f35, -4F, 4F, -14F, 14F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Bank3", this.cvt(f35, -4F, 4F, -12F, 12F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Bank4", this.cvt(f35, -4F, 4F, -10F, 10F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_AirSpeed1", -this.floatindex(
					this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 750F, 0.0F, 15F),
					IAS_Scale), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_AirSpeed2", -this.floatindex(
					this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 750F, 0.0F, 15F),
					IAS_Scale), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_AH1", 0.0F, 0.0F, -this.fm.Or.getKren());
			this.mesh.chunkSetAngles("Z_N_AH2", -this.cvt(this.fm.Or.getTangage(), -45F, 45F, -8F, 8F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_Alt_km", -this.cvt(this.setNew.altimeter, 0.0F, 11000F, 0.0F, 330F), 0.0F,
					0.0F);
			this.mesh.chunkSetAngles("Z_N_Alti", -this.cvt(this.setNew.altimeter, 0.0F, 20000F, 0.0F, 7200F), 0.0F,
					0.0F);
			this.mesh.chunkSetAngles("Z_N_Alti2", -this.cvt(this.setNew.altimeter, 0.0F, 20000F, 0.0F, 720F), 0.0F,
					0.0F);
			this.mesh.chunkSetAngles("Z_N_Climb1", -this.cvt(this.setNew.vspeed, -15F, 15F, -136F, 136F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Climb2", -this.cvt(this.setNew.vspeed, -15F, 15F, -136F, 136F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_RadioAltim", -this.setNew.AFN101, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Ln28330b", this.cvt(this.fm.CT.getGear(), 0.4F, 0.6F, 40.2F, 0.0F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Nav1", this.cvt(this.setNew.beaconDirection, -45F, 45F, -16F, 16F), 0.0F,
					0.0F);
			this.mesh.chunkSetAngles("Z_N_Nav2", this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, -14.5F, 20F), 0.0F,
					0.0F);
			if (BaseGameVersion.is410orLater()) {
			    this.mesh.chunkVisible("F_N_AFN2_RED", this.isOnBlindLandingMarker());
			    this.mesh.chunkVisible("F_N_AFN2C_RED", this.isOnBlindLandingMarker());
			}
			this.mesh.chunkSetAngles("Z_N_Nav1C", this.cvt(this.setNew.beaconDirection, -45F, 45F, -16F, 16F), 0.0F,
					0.0F);
			this.mesh.chunkSetAngles("Z_N_Nav2C", this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, -14.5F, 20F), 0.0F,
					0.0F);
			float f36 = ((FlightModelMain) super.fm).EI.engines[0].getRPM();
			this.mesh.chunkSetAngles("Z_N_RPM1", -this.floatindex(this.cvt(f36, 400F, 3600F, 0.0F, 32F), Fl20274_Scale),
					0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_RPM2", -this.floatindex(this.cvt(f36, 400F, 3600F, 0.0F, 32F), Fl20274_Scale),
					0.0F, 0.0F);
			float f37 = ((FlightModelMain) super.fm).EI.engines[1].getRPM();
			this.mesh.chunkSetAngles("Z_N_RPM3", -this.floatindex(this.cvt(f37, 400F, 3600F, 0.0F, 32F), Fl20274_Scale),
					0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_RPM4", -this.floatindex(this.cvt(f37, 400F, 3600F, 0.0F, 32F), Fl20274_Scale),
					0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_FuelConsom",
					-this.cvt(0.5F * this.setNew.consumption, 0.0F, 500F, 0.0F, 255.5F), 0.0F, 0.0F);
			float f38 = this.fm.M.fuel;
			this.mesh.chunkSetAngles("Z_N_Fuel6", -this.cvt(f38, 3424.015F, 4268.9F, 0.0F, 72.63291F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Fuel2", -this.cvt(f38, 2505.015F, 3424.015F, 0.0F, 71.6875F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Fuel3", -this.cvt(f38, 2505.015F, 3424.015F, 0.0F, 71.6875F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Fuel7", -this.cvt(f38, 844.89F, 2505.015F, 0.0F, 81.76F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Fuel8", -this.cvt(f38, 844.89F, 2505.015F, 0.0F, 81.76F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_Fuel1", -this.cvt(f38, 0.0F, 844.89F, 0.0F, 72.63291F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_FuelQuant", -this.cvt(f38, 0.0F, this.fm.M.maxFuel, 37F, 84F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E1CoolTemp",
					-this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale),
					0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E2CoolTemp",
					-this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale),
					0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E3CoolTemp",
					-this.floatindex(this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale),
					0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E4CoolTemp",
					-this.floatindex(this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale),
					0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E1OilTemp",
					-this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale),
					0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E2OilTemp",
					-this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale),
					0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E3OilTemp",
					-this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale),
					0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E4OilTemp",
					-this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale),
					0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_AirPress1", -170F, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_AirPress2", -170F, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_AirPress3", -170F, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_AirPress4", this.fm.M.fuel <= 1.0F ? 0.0F : -83F, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E1_FuelPres",
					-this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E2_FuelPres",
					-this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E3_FuelPres",
					-this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E4_FuelPres",
					-this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E1_OilPress",
					this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E2_OilPress",
					this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E3_OilPress",
					this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_N_E4_OilPress",
					this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
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
			float f39 = -this.fm.turret[1].tu[0];
			float f40 = this.fm.turret[1].tu[1];
			float f41 = Geom.DEG2RAD(f39);
			float f42 = Geom.DEG2RAD(f40);
			float f43 = (float) Math.asin(Math.tan(f41) * Math.cos(f42) * Math.cos(f41));
			float f44 = (float) Math.atan(Math.tan(f42) / Math.cos(f41));
			float f45 = Geom.RAD2DEG(f43);
			float f46 = Geom.RAD2DEG(f44);
			this.mesh.chunkSetAngles("Z_Turret151A", 0.0F, f45, 0.0F);
			this.mesh.chunkSetAngles("Z_Turret151B", 0.0F, 0.0F, f46);
			f39 = this.fm.turret[2].tu[0];
			f40 = -this.fm.turret[2].tu[1];
			f41 = Geom.DEG2RAD(f39);
			f42 = Geom.DEG2RAD(f40);
			f43 = (float) Math.asin(Math.tan(f41) * Math.cos(f42) * Math.cos(f41));
			f44 = (float) Math.atan(Math.tan(f42) / Math.cos(f41));
			f45 = Geom.RAD2DEG(f43);
			f46 = Geom.RAD2DEG(f44);
			this.mesh.chunkSetAngles("Z_Turret131B", f45, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_Turret131A", 0.0F, 0.0F, f46);
			float f47;
			for (f47 = -this.fm.turret[3].tu[0]; f47 < -180F; f47 += 360F) {
				;
			}
			for (; f47 > 180F; f47 -= 360F) {
				;
			}
			float f48 = this.fm.turret[3].tu[1];
			this.mesh.chunkSetAngles("Z_TurretA", -f47, 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_TurretB", 0.0F, 0.0F, -f48);
			this.mesh.chunkSetAngles("Z_tLink", 0.0F, 0.0F,
					this.floatindex(this.cvt(f48, 0.0F, 90F, 0.0F, 18F), Turret_Z1));
			this.mesh.chunkSetAngles("Z_tHandle", 0.0F, 0.0F,
					-this.floatindex(this.cvt(f48, 0.0F, 90F, 0.0F, 18F), Turret_Z2));
			this.mesh.chunkSetAngles("Z_tReviVal", 0.0F, -f47 * 5F, 0.0F);
			this.CalculateRevi(f47, f48);
			this.resetYPRmodifier();
			Cockpit.xyz[2] = 0.185F * this.setNew.AirEnemy;
			this.mesh.chunkSetLocate("Z_TurretA_Seat", Cockpit.xyz, Cockpit.ypr);
		}
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
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) {
			this.setNightMats(true);
		} else {
			this.setNightMats(false);
		}
	}

	public void CalculateRevi(float f, float f1) {
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
	private float saveFov;
	private float aAim;
	private float tAim;
	private boolean bEntered;

	static {
		Class class1 = CockpitME_264_Bombardier.class;
		Property.set(class1, "astatePilotIndx", 1);
        Property.set(class1, "normZN", 1.2F);
        Property.set(class1, "gsZN", 1.2F);
	}
}
