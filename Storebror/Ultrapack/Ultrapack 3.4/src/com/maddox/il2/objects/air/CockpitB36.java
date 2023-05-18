package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public abstract class CockpitB36 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitB36.this.fm != null) {
                CockpitB36.this.setTmp = CockpitB36.this.setOld;
                CockpitB36.this.setOld = CockpitB36.this.setNew;
                CockpitB36.this.setNew = CockpitB36.this.setTmp;
                for (int engineIndex=0; engineIndex < CockpitB36.this.fm.EI.engines.length; engineIndex++)
                    CockpitB36.this.setNew.throttle[engineIndex] = (0.85F * CockpitB36.this.setOld.throttle[engineIndex]) + (CockpitB36.this.fm.EI.engines[engineIndex].getControlThrottle() * 0.15F);
                CockpitB36.this.setNew.altimeter = CockpitB36.this.fm.getAltitude();
                float f = CockpitB36.this.waypointAzimuth();
                CockpitB36.this.setNew.azimuth.setDeg(CockpitB36.this.setOld.azimuth.getDeg(1.0F), CockpitB36.this.fm.Or.azimut());
                if (CockpitB36.this.useRealisticNavigationInstruments()) {
                    CockpitB36.this.setNew.waypointAzimuth.setDeg(f - 90F);
                    CockpitB36.this.setOld.waypointAzimuth.setDeg(f - 90F);
                    CockpitB36.this.setNew.radioCompassAzimuth.setDeg(CockpitB36.this.setOld.radioCompassAzimuth.getDeg(0.02F), CockpitB36.this.radioCompassAzimuthInvertMinus() - CockpitB36.this.setOld.azimuth.getDeg(1.0F) - 90F);
                    if (CockpitB36.this.fm.AS.listenLorenzBlindLanding && CockpitB36.this.fm.AS.isAAFIAS) {
                        CockpitB36.this.setNew.ilsLoc = ((10F * CockpitB36.this.setOld.ilsLoc) + CockpitB36.this.getBeaconDirection()) / 11F;
                        CockpitB36.this.setNew.ilsGS = ((10F * CockpitB36.this.setOld.ilsGS) + CockpitB36.this.getGlidePath()) / 11F;
                    } else {
                        CockpitB36.this.setNew.ilsLoc = 0.0F;
                        CockpitB36.this.setNew.ilsGS = 0.0F;
                    }
                } else {
                    CockpitB36.this.setNew.waypointAzimuth.setDeg(CockpitB36.this.setOld.waypointAzimuth.getDeg(0.1F), f);
                    CockpitB36.this.setNew.radioCompassAzimuth.setDeg(CockpitB36.this.setOld.radioCompassAzimuth.getDeg(0.1F), f - CockpitB36.this.setOld.azimuth.getDeg(0.1F) - 90F);
                }
                if (CockpitB36.this.b36X != null) {
                    CockpitB36.this.setNew.PDI = CockpitB36.this.b36X.fSightCurSideslip;
                }
                CockpitB36.this.setNew.vspeed = ((199F * CockpitB36.this.setOld.vspeed) + CockpitB36.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    class Variables {

        float      throttle[] = new float[10];
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float      vspeed;
        float      PDI;
        float      ilsLoc;
        float      ilsGS;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.radioCompassAzimuth = new AnglesFork();
        }

    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("CF_D" + this.aircraft().chunkDamageVisible("CF"), false);
            this.aircraft().hierMesh().chunkVisible("Pilot2_D" + this.aircraft().chunkDamageVisible("Pilot2"), false);
            this.aircraft().hierMesh().chunkVisible("Pilot3_D" + this.aircraft().chunkDamageVisible("Pilot3"), false);
            this.aircraft().hierMesh().chunkVisible("Head2_D0", false);
            this.aircraft().hierMesh().chunkVisible("HMask2_D0", false);
            this.aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
             return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("CF_D" + this.aircraft().chunkDamageVisible("CF"), true);
        this.aircraft().hierMesh().chunkVisible("Pilot2_D" + this.aircraft().chunkDamageVisible("Pilot2"), true);
        this.aircraft().hierMesh().chunkVisible("Pilot3_D" + this.aircraft().chunkDamageVisible("Pilot3"), false);
        this.aircraft().hierMesh().chunkVisible("Head2_D0", true);
        this.aircraft().hierMesh().chunkVisible("HMask2_D0", true);
        this.aircraft().hierMesh().chunkVisible("Cockpit_D0", true);
        super.doFocusLeave();
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitB36(String hier, String name) {
        super(hier, name);
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictFlap = 0.0F;
        this.pictManf1 = 1.0F;
        this.pictManf2 = 1.0F;
        this.pictManf3 = 1.0F;
        this.pictManf4 = 1.0F;
        this.pictManf5 = 1.0F;
        this.pictManf6 = 1.0F;
        this.b36X = null;
        this.cockpitNightMats = (new String[] { "gauges1", "gauges1dmg", "gauges2", "gauges2dmg", "gauges3", "gauges3dmg", "gauges4", "gauges4dmg", "gauges5", "gauges5dmg", "texture25" });
        this.setNightMats(false);
        if (this.aircraft() instanceof B_36X) {
            this.b36X = (B_36X) this.aircraft();
        }
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("ColumnL", 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 8F, 0.0F);
        this.mesh.chunkSetAngles("ColumnL2", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 68F, 0.0F);
        this.mesh.chunkSetAngles("ColumnR", 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 8F, 0.0F);
        this.mesh.chunkSetAngles("ColumnR2", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 68F, 0.0F);
        this.mesh.chunkSetAngles("rudderpedalR", 0.0F, -10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("rudderpedalL", 0.0F, 10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("rudderR01", 0.0F, -10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("rudderL01", 0.0F, 10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("mixture", 0.0F, -30F * this.fm.EI.engines[0].getControlMix(), 0.0F);
        for (int engineIndex=0; engineIndex < 6; engineIndex++)
            this.mesh.chunkSetAngles("zThrottle" + (engineIndex+1), 0.0F, -49F * this.interp(this.setNew.throttle[engineIndex], this.setOld.throttle[engineIndex], f), 0.0F);
        float f1;
        if (Math.abs(this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.02F) {
            if ((this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.0F) {
                f1 = -0.0299F;
            } else {
                f1 = -0F;
            }
        } else {
            f1 = -0.0144F;
        }
        this.pictFlap = (0.8F * this.pictFlap) + (0.2F * f1);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.pictFlap;
        this.mesh.chunkSetAngles("Zflap", this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, 125F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Zclock2R", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Zclock1R", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Zclock2L", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Zclock1L", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("ZAH1R", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("ZAH1L", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0365F, -0.0365F);
        this.mesh.chunkSetLocate("ZAH2R", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("ZAH2L", Cockpit.xyz, Cockpit.ypr);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("ZclimbL", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitB36.variometerScale), 0.0F);
        }
        this.mesh.chunkSetAngles("ZclimbR", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitB36.variometerScale), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("ZlageL01", 0.0F, this.cvt(this.getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
        this.mesh.chunkSetAngles("ZlageL", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        this.mesh.chunkSetAngles("ZlageR01", 0.0F, this.cvt(this.getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
        this.mesh.chunkSetAngles("ZlageR", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        this.mesh.chunkSetAngles("zpdi", 0.0F, this.cvt(this.setNew.PDI, -30F, 30F, -46.5F, 46.5F), 0.0F);
        this.mesh.chunkSetAngles("ZspeedL", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), CockpitB36.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("ZspeedR", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), CockpitB36.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zAlt2L", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1L", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("zAlt2Rc", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1R", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("zComp01", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zComp02", 0.0F, this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
        this.mesh.chunkSetAngles("zRComp", 0.0F, this.setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("zCompass2L", 0.0F, -0.5F * this.setNew.azimuth.getDeg(f), 0.0F);
            this.mesh.chunkSetAngles("zCompass2R", 0.0F, -0.5F * this.setNew.azimuth.getDeg(f), 0.0F);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("ZrpmL", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            this.mesh.chunkSetAngles("ZrpmR", 0.0F, this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            this.mesh.chunkSetAngles("ZrpmLC0", 0.0F, this.cvt(this.fm.EI.engines[2].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            this.mesh.chunkSetAngles("ZrpmRC0", 0.0F, this.cvt(this.fm.EI.engines[3].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            this.mesh.chunkSetAngles("ZrpmLC", 0.0F, this.cvt(this.fm.EI.engines[4].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            this.mesh.chunkSetAngles("ZrpmRC", 0.0F, this.cvt(this.fm.EI.engines[5].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            this.mesh.chunkSetAngles("ZpressLside1C", 0.0F, this.pictManf1 = (0.9F * this.pictManf1) + (0.1F * this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F)), 0.0F);
            this.mesh.chunkSetAngles("ZpressRside1C", 0.0F, this.pictManf2 = (0.9F * this.pictManf2) + (0.1F * this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F)), 0.0F);
            this.mesh.chunkSetAngles("ZpressLside2", 0.0F, this.pictManf3 = (0.9F * this.pictManf3) + (0.1F * this.cvt(this.fm.EI.engines[2].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F)), 0.0F);
            this.mesh.chunkSetAngles("ZpressLside1", 0.0F, this.pictManf4 = (0.9F * this.pictManf4) + (0.1F * this.cvt(this.fm.EI.engines[3].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F)), 0.0F);
            this.mesh.chunkSetAngles("ZpressRside1", 0.0F, this.pictManf5 = (0.9F * this.pictManf5) + (0.1F * this.cvt(this.fm.EI.engines[4].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F)), 0.0F);
            this.mesh.chunkSetAngles("ZpressRside2", 0.0F, this.pictManf6 = (0.9F * this.pictManf6) + (0.1F * this.cvt(this.fm.EI.engines[5].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F)), 0.0F);
        }
        this.mesh.chunkSetAngles("TrimwheelR", -104F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TrimwheelL", -104F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("BL_Vert", this.cvt(this.setNew.ilsLoc, -63F, 63F, -45F, 45F), 0.0F, 0.0F);
        if (this.setNew.ilsGS >= 0.0F) {
            this.mesh.chunkSetAngles("BL_Horiz", this.cvt(this.setNew.ilsGS, 0.0F, 0.5F, 0.0F, 40F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("BL_Horiz", this.cvt(this.setNew.ilsGS, -0.3F, 0.0F, -40F, 0.0F), 0.0F, 0.0F);
        }
        if (this.fm.Gears.cgear) {
            this.resetYPRmodifier();
        }
        if (this.fm.Gears.lgear) {
            this.resetYPRmodifier();
        }
        if (this.fm.Gears.rgear) {
            this.resetYPRmodifier();
        }
        this.mesh.chunkVisible("Z_GearRed1", (this.fm.CT.getGear() > 0.01F) && (this.fm.CT.getGear() < 0.99F));
        this.mesh.chunkVisible("Z_GearCGreen1", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.cgear);
        this.mesh.chunkVisible("Z_GearLGreen1", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_GearRGreen1", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage8", true);
            this.mesh.chunkVisible("gauges5L_dmg", true);
            this.mesh.chunkVisible("gauges5L", false);
            this.mesh.chunkVisible("gauges4L_dmg", true);
            this.mesh.chunkVisible("gauges4L", false);
            this.mesh.chunkVisible("zpdi", false);
            this.mesh.chunkVisible("zAlt2L", false);
            this.mesh.chunkVisible("zAlt1L", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage7", true);
            this.mesh.chunkVisible("gauges3L_dmg", true);
            this.mesh.chunkVisible("gauges3L", false);
            this.mesh.chunkVisible("gauges1L_dmg", true);
            this.mesh.chunkVisible("gauges1L", false);
            this.mesh.chunkVisible("stabiL_dmg", true);
            this.mesh.chunkVisible("stabiL", false);
            this.mesh.chunkVisible("ZclimbL", false);
            this.mesh.chunkVisible("ZspeedL", false);
            this.mesh.chunkVisible("ZrpmL", false);
            this.mesh.chunkVisible("ZrpmLR", false);
            this.mesh.chunkVisible("ZpressLside2", false);
            this.mesh.chunkVisible("ZpressLside1", false);
            this.mesh.chunkVisible("ZpressRside2", false);
            this.mesh.chunkVisible("ZpressRside1", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("gauges5R_dmg", true);
            this.mesh.chunkVisible("gauges5R", false);
            this.mesh.chunkVisible("gauges4R_dmg", true);
            this.mesh.chunkVisible("gauges4R", false);
            this.mesh.chunkVisible("stabiR_dmg", true);
            this.mesh.chunkVisible("stabiR", false);
            this.mesh.chunkVisible("zRPM1", false);
            this.mesh.chunkVisible("zBoost2", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage5", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage6", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("gauges2R_dmg", true);
            this.mesh.chunkVisible("gauges2R", false);
            this.mesh.chunkVisible("zFuel2", false);
            this.mesh.chunkVisible("zAlt2R", false);
            this.mesh.chunkVisible("zAlt1R", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
    }

    Variables                  setOld;
    Variables                  setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictFlap;
    private float              pictManf1;
    private float              pictManf2;
    private float              pictManf3;
    private float              pictManf4;
    private float              pictManf5;
    private float              pictManf6;
    private B_36X              b36X;
    private static final float speedometerScale[] = { 0.0F, 2.5F, 59F, 126F, 155.5F, 223.5F, 240F, 254.5F, 271F, 285F, 296.5F, 308.5F, 324F, 338.5F };
    private static final float variometerScale[]  = { -180F, -157F, -130F, -108F, -84F, -46.5F, 0.0F, 46.5F, 84F, 108F, 130F, 157F, 180F };
}
