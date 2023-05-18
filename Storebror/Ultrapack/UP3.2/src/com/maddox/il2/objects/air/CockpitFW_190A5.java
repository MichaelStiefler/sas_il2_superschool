//
// Decompiled by Procyon v0.5.29
//

package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitFW_190A5 extends CockpitPilot {
	private Gun[]                gun;
	private Variables            setOld;
	private Variables            setNew;
	private Variables            setTmp;
	private LightPointActor      light1;
	private LightPointActor      light2;
	private BulletEmitter[]      bomb;
	private long                 t1;
	private float                pictAiler;
	private float                pictElev;
	public Vector3f              w;
	private static final float[] speedometerScale;
	private static final float[] rpmScale;
	private static final float[] fuelScale;
	private static final float[] vsiNeedleScale;
	private static final float[] oilTempScale;

	// ------------------------------------------------------------------------
	/*
	 * skylla: Schusszaehler fix: for more info please see class CockpitFW_190A4
	 *
	 * @author: SAS~Skylla
	 *
	 * @params: int bullets: number of bullets still in the gun; float offset: offset at which the ammocounter shows zero bullets left (-0.018f for outer, -0.045f for inner) int counterrange: number of bullets the counter can display (100 for outer, 500
	 * for inner)
	 */
	private static int mg17lbullets = 0;
	private static int mg17rbullets = 0;

	private static float ammoCounter(int bullets, float offset, int counterrange) {
		float f = (0.026f - offset) / counterrange;
		bullets = Math.min(bullets, counterrange);
		return f * bullets + offset;
	}
	// ------------------------------------------------------------------------

	protected float waypointAzimuth() {
		return this.waypointAzimuthInvertMinus(30.0f);
	}

	public CockpitFW_190A5() {
		super("3DO/Cockpit/FW-190A-5/hier.him", "bf109");
		this.gun = new Gun[] { null, null, null, null, null, null };
		this.bomb = new BulletEmitter[] { null, null, null, null };
		this.setOld = new Variables();
		this.setNew = new Variables();
		this.pictAiler = 0.0f;
		this.pictElev = 0.0f;
		this.w = new Vector3f();
		this.setNew.dimPosition = 1.0f;
		final HookNamed hookNamed = new HookNamed(this.mesh, "LIGHTHOOK_L");
		final Loc loc = new Loc(0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f);
		hookNamed.computePos(this, new Loc(0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f), loc);
		this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
		this.light1.light.setColor(126.0f, 232.0f, 245.0f);
		this.light1.light.setEmit(0.0f, 0.0f);
		this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
		final HookNamed hookNamed2 = new HookNamed(this.mesh, "LIGHTHOOK_R");
		final Loc loc2 = new Loc(0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f);
		hookNamed2.computePos(this, new Loc(0.0, 0.0, 0.0, 0.0f, 0.0f, 0.0f), loc2);
		this.light2 = new LightPointActor(new LightPoint(), loc2.getPoint());
		this.light2.light.setColor(126.0f, 232.0f, 245.0f);
		this.light2.light.setEmit(0.0f, 0.0f);
		this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
		this.cockpitNightMats = new String[] { "A4GP1", "A4GP2", "A5GP3", "A5GP3Km", "A5GP4", "A4GP5", "A4GP6" };
		this.setNightMats(false);
		this.cockpitDimControl = !this.cockpitDimControl;
		this.interpPut(new Interpolater(), null, Time.current(), null);
		AircraftLH.printCompassHeading = true;
	}

	public void reflectWorldToInstruments(final float f_54_) {
		if (this.gun[0] == null) {
			this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN01");
			this.gun[1] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN02");
			this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON03");
			this.gun[3] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON04");
			this.gun[4] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON01");
			this.gun[5] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON02");
		}
		if (this.bomb[0] == null) {
			for (int i = 0; i < this.bomb.length; ++i) {
				this.bomb[i] = GunEmpty.get();
			}
			if (((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb05") != GunEmpty.get()) {
				this.bomb[1] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb05");
				this.bomb[2] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb05");
			} else if (((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev02") != GunEmpty.get()) {
				this.bomb[1] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev02");
				this.bomb[2] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev02");
			}
			if (((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalRock01") != GunEmpty.get()) {
				this.bomb[0] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalRock01");
				this.bomb[3] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalRock02");
			} else if (((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb03") != GunEmpty.get()) {
				this.bomb[0] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb03");
				this.bomb[3] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb04");
			} else if (((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev05") != GunEmpty.get()) {
				this.bomb[0] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev05");
				this.bomb[3] = ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalDev06");
			}
			this.t1 = Time.current();
		}
		if ((this.fm.AS.astateCockpitState & 0x20) == 0x0 && (this.fm.AS.astateCockpitState & 0x80) == 0x0) {
			this.mesh.chunkSetAngles("NeedleALT", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f_54_), 0.0f, 10000.0f, 0.0f, 3600.0f), 0.0f, 0.0f);
			this.mesh.chunkSetAngles("NeedleALTKm", 0.0f, 0.0f, this.cvt(this.setNew.altimeter, 0.0f, 10000.0f, 0.0f, -180.0f));
		}
		this.mesh.chunkSetAngles("NeedleManPress", -this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6f, 1.8f, 0.0f, 336.0f), 0.0f, 0.0f);
		this.mesh.chunkSetAngles("NeedleKMH", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0f, 900.0f, 0.0f, 9.0f), CockpitFW_190A5.speedometerScale), 0.0f, 0.0f);
		if ((this.fm.AS.astateCockpitState & 0x40) == 0x0 && (this.fm.AS.astateCockpitState & 0x8) == 0x0 && (this.fm.AS.astateCockpitState & 0x10) == 0x0) {
			this.mesh.chunkSetAngles("NeedleRPM", -this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0f, 4000.0f, 0.0f, 8.0f), CockpitFW_190A5.rpmScale), 0.0f, 0.0f);
		}
		this.mesh.chunkSetAngles("NeedleFuel", this.floatindex(this.cvt(this.fm.M.fuel / 0.72f, 0.0f, 400.0f, 0.0f, 4.0f), CockpitFW_190A5.fuelScale), 0.0f, 0.0f);
		this.mesh.chunkSetAngles("NeedleOilTemp", this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0f, 120.0f, 0.0f, 3.0f), CockpitFW_190A5.oilTempScale), 0.0f, 0.0f);
		if ((this.fm.AS.astateCockpitState & 0x2) == 0x0 && (this.fm.AS.astateCockpitState & 0x1) == 0x0 && (this.fm.AS.astateCockpitState & 0x4) == 0x0) {
			this.mesh.chunkSetAngles("NeedleFuelPress", this.cvt(this.fm.M.fuel <= 1.0f ? 0.0f : 0.26f, 0.0f, 3.0f, 0.0f, 135.0f), 0.0f, 0.0f);
			this.mesh.chunkSetAngles("NeedleOilPress", -this.cvt(1.0f + 0.05f * this.fm.EI.engines[0].tOilOut, 0.0f, 15.0f, 0.0f, 135.0f), 0.0f, 0.0f);
		}
		if ((this.fm.AS.astateCockpitState & 0x40) == 0x0 && (this.fm.AS.astateCockpitState & 0x8) == 0x0 && (this.fm.AS.astateCockpitState & 0x10) == 0x0) {
			this.mesh.chunkSetAngles("NeedleAHTurn", this.cvt(this.setNew.turn, -0.23562f, 0.23562f, -50.0f, 50.0f), 0.0f, 0.0f);
			this.mesh.chunkSetAngles("NeedleAHBank", this.cvt(this.getBall(7.0), -7.0f, 7.0f, 11.0f, -11.0f), 0.0f, 0.0f);
			this.mesh.chunkSetAngles("NeedleAHCyl", 0.0f, 0.0f, this.fm.Or.getKren());
			this.mesh.chunkSetAngles("NeedleAHBar", 0.0f, 0.0f, this.cvt(this.fm.Or.getTangage(), -45.0f, 45.0f, 12.0f, -12.0f));
		}
		if ((this.fm.AS.astateCockpitState & 0x20) == 0x0 && (this.fm.AS.astateCockpitState & 0x80) == 0x0) {
			this.mesh.chunkSetAngles("NeedleCD", this.setNew.vspeed < 0.0f ? this.floatindex(this.cvt(-this.setNew.vspeed, 0.0f, 30.0f, 0.0f, 6.0f), CockpitFW_190A5.vsiNeedleScale)
					: -this.floatindex(this.cvt(this.setNew.vspeed, 0.0f, 30.0f, 0.0f, 6.0f), CockpitFW_190A5.vsiNeedleScale), 0.0f, 0.0f);
		}
		if ((this.fm.AS.astateCockpitState & 0x40) == 0x0 && (this.fm.AS.astateCockpitState & 0x8) == 0x0 && (this.fm.AS.astateCockpitState & 0x10) == 0x0) {
			if (this.useRealisticNavigationInstruments()) {
				this.mesh.chunkSetAngles("RepeaterPlane", -this.setNew.azimuth.getDeg(f_54_) + this.setNew.waypointAzimuth.getDeg(f_54_), 0.0f, 0.0f);
				this.mesh.chunkSetAngles("RepeaterOuter", this.setNew.waypointAzimuth.getDeg(f_54_), 0.0f, 0.0f);
			} else {
				this.mesh.chunkSetAngles("RepeaterOuter", this.setNew.azimuth.getDeg(f_54_), 0.0f, 0.0f);
				this.mesh.chunkSetAngles("RepeaterPlane", -this.setNew.waypointAzimuth.getDeg(f_54_ * 0.1f), 0.0f, 0.0f);
			}
		}
		if ((this.fm.AS.astateCockpitState & 0x20) == 0x0 && (this.fm.AS.astateCockpitState & 0x80) == 0x0) {
			this.mesh.chunkSetAngles("NeedleHBSmall", -105.0f + (float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 5.0f, 0.0f, 0.0f);
			this.mesh.chunkSetAngles("NeedleHBLarge", -270.0f + (float) Math.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 60.0f, 0.0f, 0.0f);
		}
		this.mesh.chunkSetAngles("NeedleTrimmung", this.fm.CT.getTrimElevatorControl() * 25.0f, 0.0f, 0.0f);
		this.mesh.chunkSetAngles("NeedleHClock", -this.cvt(World.getTimeofDay(), 0.0f, 24.0f, 0.0f, 720.0f), 0.0f, 0.0f);
		this.mesh.chunkSetAngles("NeedleMClock", -this.cvt(World.getTimeofDay() % 1.0f, 0.0f, 1.0f, 0.0f, 360.0f), 0.0f, 0.0f);
		this.mesh.chunkSetAngles("NeedleSClock", -this.cvt(World.getTimeofDay() % 1.0f * 60.0f % 1.0f, 0.0f, 1.0f, 0.0f, 360.0f), 0.0f, 0.0f);
		this.resetYPRmodifier();

		// ------------------------------------------------------------------------
		// skylla: Schusszaehler Fix: for more info please see class CockpitFW_190A4
		if (this.gun[0] != null) {
			Cockpit.xyz[0] = this.cvt(this.gun[0].countBullets(), 0.0f, 500.0f, -0.044f, 0.0f);
			this.mesh.chunkVisible("XLampMG17_1", !this.gun[0].haveBullets());
			if (this.gun[0].haveBullets() && this.gun[0].isShots() && this.gun[0].countBullets() != mg17lbullets) {
				this.mesh.chunkVisible("XLampMG17_1", true);
				mg17lbullets = this.gun[0].countBullets();
			}
		}
		if (this.gun[1] != null) {
			this.mesh.chunkVisible("XLampMG17_2", !this.gun[1].haveBullets());
			if (this.gun[1].haveBullets() && this.gun[1].isShots() && this.gun[1].countBullets() != mg17rbullets) {
				this.mesh.chunkVisible("XLampMG17_2", true);
				mg17rbullets = this.gun[1].countBullets();
			}
		}
		if (this.gun[4] != null) {
			Cockpit.xyz[0] = this.cvt(this.gun[4].countBullets(), 0.0f, 500.0f, -0.045f, 0.0f);
			// Cockpit.xyz[0] = ammoCounter(this.gun[4].countBullets(), -0.045f, 500);
			this.mesh.chunkSetLocate("RC_MG17_L", Cockpit.xyz, Cockpit.ypr);
		}
		if (this.gun[5] != null) {
			Cockpit.xyz[0] = this.cvt(this.gun[5].countBullets(), 0.0f, 500.0f, -0.045f, 0.0f);
			// Cockpit.xyz[0] = ammoCounter(this.gun[5].countBullets(), -0.045f, 500);
			this.mesh.chunkSetLocate("RC_MG17_R", Cockpit.xyz, Cockpit.ypr);
		}
		if (this.gun[2] != null) {
			Cockpit.xyz[0] = ammoCounter(this.gun[2].countBullets(), -0.018f, 100); // <- new code with
																					 // CockpitFW_190A4.ammoCounter();
																					 // see Cockpit class of FW-190A4 for
																					 // more details
			this.mesh.chunkSetLocate("RC_MG151_L", Cockpit.xyz, Cockpit.ypr);
		}
		if (this.gun[3] != null) {
			Cockpit.xyz[0] = ammoCounter(this.gun[3].countBullets(), -0.018f, 100); // <- new code with
																					 // CockpitFW_190A4.ammoCounter();
																					 // see Cockpit class of FW-190A4 for
																					 // more details
			this.mesh.chunkSetLocate("RC_MG151_R", Cockpit.xyz, Cockpit.ypr);
		}
		this.resetYPRmodifier();
		/*
		 * old code: if (this.gun[0] != null) { Cockpit.xyz[0] = this.cvt(this.gun[0].countBullets(), 0.0f, 500.0f, -0.044f, 0.0f); this.mesh.chunkSetLocate("RC_MG17_L", Cockpit.xyz, Cockpit.ypr); this.mesh.chunkVisible("XLampMG17_1",
		 * !this.gun[0].haveBullets()); } if (this.gun[1] != null) { Cockpit.xyz[0] = this.cvt(this.gun[1].countBullets(), 0.0f, 500.0f, -0.044f, 0.0f); this.mesh.chunkSetLocate("RC_MG17_R", Cockpit.xyz, Cockpit.ypr); this.mesh.chunkVisible("XLampMG17_2",
		 * !this.gun[1].haveBullets()); } if (this.gun[4] != null) { Cockpit.xyz[0] = this.cvt(this.gun[4].countBullets(), 0.0f, 200.0f, -0.017f, 0.0f); this.mesh.chunkSetLocate("RC_MG151_L", Cockpit.xyz, Cockpit.ypr); } if (this.gun[5] != null) {
		 * Cockpit.xyz[0] = this.cvt(this.gun[5].countBullets(), 0.0f, 200.0f, -0.017f, 0.0f); this.mesh.chunkSetLocate("RC_MG151_R", Cockpit.xyz, Cockpit.ypr); }
		 */
		// ------------------------------------------------------------------------
		if (this.t1 < Time.current()) {
			this.t1 = Time.current() + 500L;
			this.mesh.chunkVisible("XLampBombL", this.bomb[0].haveBullets());
			this.mesh.chunkVisible("XLampBombCL", this.bomb[1].haveBullets());
			this.mesh.chunkVisible("XLampBombCR", this.bomb[2].haveBullets());
			this.mesh.chunkVisible("XLampBombR", this.bomb[3].haveBullets());
		}
		this.mesh.chunkSetAngles("IgnitionSwitch", 24.0f * this.fm.EI.engines[0].getControlMagnetos(), 0.0f, 0.0f);
		Cockpit.xyz[2] = this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f_54_), 0.0f, 1.0f, 0.0f, 0.058f);
		this.mesh.chunkSetLocate("Revi16Tinter", Cockpit.xyz, Cockpit.ypr);
		final HierMesh mesh = this.mesh;
		final String s = "Stick";
		final float n = 0.0f;
		final float pictAiler = 0.85f * this.pictAiler + 0.15f * this.fm.CT.AileronControl;
		this.pictAiler = pictAiler;
		mesh.chunkSetAngles(s, n, pictAiler * 20.0f, (this.pictElev = 0.85f * this.pictElev + 0.15f * this.fm.CT.ElevatorControl) * 20.0f);
		this.resetYPRmodifier();
		Cockpit.xyz[2] = this.fm.CT.WeaponControl[1] ? -0.004f : 0.0f;
		this.mesh.chunkSetLocate("SecTrigger", Cockpit.xyz, Cockpit.ypr);
		Cockpit.ypr[0] = this.interp(this.setNew.throttle, this.setOld.throttle, f_54_) * 34.0f * 0.91f;
		Cockpit.xyz[2] = Cockpit.ypr[0] <= 7.0f ? 0.0f : -0.006f;
		this.mesh.chunkSetLocate("ThrottleQuad", Cockpit.xyz, Cockpit.ypr);
		this.mesh.chunkSetAngles("RPedalBase", 0.0f, 0.0f, this.fm.CT.getRudder() * 15.0f);
		this.mesh.chunkSetAngles("RPedalStrut", 0.0f, 0.0f, -this.fm.CT.getRudder() * 15.0f);
		this.mesh.chunkSetAngles("RPedal", 0.0f, 0.0f, -this.fm.CT.getRudder() * 15.0f - this.fm.CT.getBrake() * 15.0f);
		this.mesh.chunkSetAngles("LPedalBase", 0.0f, 0.0f, -this.fm.CT.getRudder() * 15.0f);
		this.mesh.chunkSetAngles("LPedalStrut", 0.0f, 0.0f, this.fm.CT.getRudder() * 15.0f);
		this.mesh.chunkSetAngles("LPedal", 0.0f, 0.0f, this.fm.CT.getRudder() * 15.0f - this.fm.CT.getBrake() * 15.0f);
		this.mesh.chunkVisible("XLampTankSwitch", this.fm.M.fuel > 144.0f);
		this.mesh.chunkVisible("XLampFuelLow", this.fm.M.fuel < 43.2f);
		this.mesh.chunkVisible("XLampFlapLPos_3", this.fm.CT.getFlap() > 0.9f);
		this.mesh.chunkVisible("XLampFlapLPos_2", this.fm.CT.getFlap() > 0.1f && this.fm.CT.getFlap() < 0.5f);
		this.mesh.chunkVisible("XLampFlapLPos_1", this.fm.CT.getFlap() < 0.1f);
		this.mesh.chunkVisible("XLampFlapRPos_3", this.fm.CT.getFlap() > 0.9f);
		this.mesh.chunkVisible("XLampFlapRPos_2", this.fm.CT.getFlap() > 0.1f && this.fm.CT.getFlap() < 0.5f);
		this.mesh.chunkVisible("XLampFlapRPos_1", this.fm.CT.getFlap() < 0.1f);
		this.mesh.chunkVisible("XLampGearL_1", this.fm.CT.getGear() < 0.05f);
		this.mesh.chunkVisible("XLampGearL_2", this.fm.CT.getGear() > 0.95f && this.fm.Gears.lgear);
		this.mesh.chunkVisible("XLampGearR_1", this.fm.CT.getGear() < 0.05f);
		this.mesh.chunkVisible("XLampGearR_2", this.fm.CT.getGear() > 0.95f && this.fm.Gears.rgear);
		this.mesh.chunkVisible("XLampGearC_1", this.fm.CT.getGear() < 0.05f);
		this.mesh.chunkVisible("XLampGearC_2", this.fm.CT.getGear() > 0.95f);
		this.mesh.chunkSetAngles("NeedleNahe1", this.cvt(this.setNew.beaconDirection, -45.0f, 45.0f, 20.0f, -20.0f), 0.0f, 0.0f);
		this.mesh.chunkSetAngles("NeedleNahe2", this.cvt(this.setNew.beaconRange, 0.0f, 1.0f, -20.0f, 20.0f), 0.0f, 0.0f);
		this.mesh.chunkVisible("AFN2_RED", this.isOnBlindLandingMarker());
	}

	public void toggleDim() {
		this.cockpitDimControl = !this.cockpitDimControl;
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) {
			this.light1.light.setEmit(0.005f, 0.75f);
			this.light2.light.setEmit(0.005f, 0.75f);
			this.setNightMats(true);
		} else {
			this.light1.light.setEmit(0.0f, 0.0f);
			this.light2.light.setEmit(0.0f, 0.0f);
			this.setNightMats(false);
		}
	}

	private void retoggleLight() {
		if (this.cockpitLightControl) {
			this.setNightMats(false);
			this.setNightMats(true);
		} else {
			this.setNightMats(true);
			this.setNightMats(false);
		}
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 0x2) != 0x0 || (this.fm.AS.astateCockpitState & 0x1) != 0x0 || (this.fm.AS.astateCockpitState & 0x4) != 0x0) {
			if ((this.fm.AS.astateCockpitState & 0x2) != 0x0) {
				this.mesh.chunkVisible("Revi16", false);
				this.mesh.chunkVisible("Revi16Tinter", false);
				this.mesh.chunkVisible("Z_Z_MASK", false);
				this.mesh.chunkVisible("Z_Z_RETICLE", false);
				this.mesh.chunkVisible("DRevi16", true);
			}
			this.mesh.chunkVisible("XGlassDamage1", true);
			this.mesh.chunkVisible("HullDamage1", true);
			this.mesh.materialReplace("A4GP1", "DA5GP1");
			this.mesh.materialReplace("A4GP1_night", "DA5GP1_night");
			this.mesh.chunkVisible("NeedleManPress", false);
		}
		if ((this.fm.AS.astateCockpitState & 0x40) != 0x0 || (this.fm.AS.astateCockpitState & 0x8) != 0x0 || (this.fm.AS.astateCockpitState & 0x10) != 0x0) {
			this.mesh.chunkVisible("XGlassDamage2", true);
			this.mesh.chunkVisible("XGlassDamage4", true);
			this.mesh.chunkVisible("HullDamage3", true);
			this.mesh.materialReplace("A4GP2", "DA5GP2");
			this.mesh.materialReplace("A4GP2_night", "DA5GP2_night");
			this.mesh.chunkVisible("NeedleAHBank", false);
			this.mesh.chunkVisible("NeedleOilTemp", false);
			this.mesh.materialReplace("A5GP4", "DA5GP4");
			this.mesh.materialReplace("A5GP4_night", "DA5GP4_night");
			this.mesh.chunkVisible("NeedleFuel", false);
			this.resetYPRmodifier();
		}
		if ((this.fm.AS.astateCockpitState & 0x20) != 0x0 || (this.fm.AS.astateCockpitState & 0x80) != 0x0) {
			this.mesh.chunkVisible("XGlassDamage3", true);
			this.mesh.chunkVisible("HullDamage2", true);
			this.mesh.materialReplace("A5GP3", "DA5GP3");
			this.mesh.materialReplace("A5GP3_night", "DA5GP3_night");
			this.mesh.chunkVisible("NeedleALT", false);
			this.mesh.chunkVisible("NeedleKMH", false);
		}
		this.retoggleLight();
	}

	static {
		speedometerScale = new float[] { 0.0f, 18.5f, 67.0f, 117.0f, 164.0f, 215.0f, 267.0f, 320.0f, 379.0f, 427.0f, 428.0f };
		rpmScale = new float[] { 0.0f, 11.25f, 53.0f, 108.0f, 170.0f, 229.0f, 282.0f, 334.0f, 342.5f, 342.5f };
		fuelScale = new float[] { 0.0f, 16.0f, 35.0f, 52.5f, 72.0f, 72.0f };
		vsiNeedleScale = new float[] { 0.0f, 48.0f, 82.0f, 96.5f, 111.0f, 120.5f, 130.0f, 130.0f };
		oilTempScale = new float[] { 0.0f, 23.0f, 52.0f, 81.0f, 81.0f };
		Property.set(CockpitFW_190A5.class, "normZN", 0.72f);
		Property.set(CockpitFW_190A5.class, "gsZN", 0.66f);
	}

	class Interpolater extends InterpolateRef {
		public boolean tick() {
			if (CockpitFW_190A5.this.fm != null) {
				CockpitFW_190A5.this.setTmp = CockpitFW_190A5.this.setOld;
				CockpitFW_190A5.this.setOld = CockpitFW_190A5.this.setNew;
				CockpitFW_190A5.this.setNew = CockpitFW_190A5.this.setTmp;
				CockpitFW_190A5.this.setNew.altimeter = CockpitFW_190A5.this.fm.getAltitude();
				if (CockpitFW_190A5.this.cockpitDimControl) {
					if (CockpitFW_190A5.this.setNew.dimPosition > 0.0f) { CockpitFW_190A5.this.setNew.dimPosition = CockpitFW_190A5.this.setOld.dimPosition - 0.05f; }
				} else if (CockpitFW_190A5.this.setNew.dimPosition < 1.0f) { CockpitFW_190A5.this.setNew.dimPosition = CockpitFW_190A5.this.setOld.dimPosition + 0.05f; }
				CockpitFW_190A5.this.setNew.throttle = (10.0f * CockpitFW_190A5.this.setOld.throttle + CockpitFW_190A5.this.fm.CT.PowerControl) / 11.0f;
				CockpitFW_190A5.this.setNew.vspeed = (499.0f * CockpitFW_190A5.this.setOld.vspeed + CockpitFW_190A5.this.fm.getVertSpeed()) / 500.0f;
				final float waypointAzimuth = CockpitFW_190A5.this.waypointAzimuth();
				if (CockpitFW_190A5.this.useRealisticNavigationInstruments()) {
					CockpitFW_190A5.this.setNew.waypointAzimuth.setDeg(waypointAzimuth - 90.0f);
					CockpitFW_190A5.this.setOld.waypointAzimuth.setDeg(waypointAzimuth - 90.0f);
				} else {
					CockpitFW_190A5.this.setNew.waypointAzimuth.setDeg(CockpitFW_190A5.this.setOld.waypointAzimuth.getDeg(0.1f), waypointAzimuth - CockpitFW_190A5.this.setOld.azimuth.getDeg(1.0f));
				}
				CockpitFW_190A5.this.setNew.azimuth.setDeg(CockpitFW_190A5.this.setOld.azimuth.getDeg(1.0f), CockpitFW_190A5.this.fm.Or.azimut());
				CockpitFW_190A5.this.w.set(CockpitFW_190A5.this.fm.getW());
				CockpitFW_190A5.this.fm.Or.transform(CockpitFW_190A5.this.w);
				CockpitFW_190A5.this.setNew.turn = (12.0f * CockpitFW_190A5.this.setOld.turn + CockpitFW_190A5.this.w.z) / 13.0f;
				CockpitFW_190A5.this.setNew.beaconDirection = (10.0f * CockpitFW_190A5.this.setOld.beaconDirection + CockpitFW_190A5.this.getBeaconDirection()) / 11.0f;
				CockpitFW_190A5.this.setNew.beaconRange = (10.0f * CockpitFW_190A5.this.setOld.beaconRange + CockpitFW_190A5.this.getBeaconRange()) / 11.0f;
			}
			return true;
		}
	}

	private class Variables {
		float      altimeter;
		float      throttle;
		float      dimPosition;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		float      beaconDirection;
		float      beaconRange;
		float      turn;
		float      vspeed;

		private Variables() {
			this.azimuth = new AnglesFork();
			this.waypointAzimuth = new AnglesFork();
		}
	}
}
