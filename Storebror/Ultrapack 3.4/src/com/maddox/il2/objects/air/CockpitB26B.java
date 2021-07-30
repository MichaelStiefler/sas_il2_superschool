package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitB26B extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitB26B.this.fm != null) {
                CockpitB26B.this.setTmp = CockpitB26B.this.setOld;
                CockpitB26B.this.setOld = CockpitB26B.this.setNew;
                CockpitB26B.this.setNew = CockpitB26B.this.setTmp;
                CockpitB26B.this.setNew.throttle1 = 0.85F * CockpitB26B.this.setOld.throttle1 + CockpitB26B.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
                CockpitB26B.this.setNew.throttle2 = 0.85F * CockpitB26B.this.setOld.throttle2 + CockpitB26B.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
                CockpitB26B.this.setNew.prop1 = 0.85F * CockpitB26B.this.setOld.prop1 + CockpitB26B.this.fm.EI.engines[0].getControlProp() * 0.15F;
                CockpitB26B.this.setNew.prop2 = 0.85F * CockpitB26B.this.setOld.prop2 + CockpitB26B.this.fm.EI.engines[1].getControlProp() * 0.15F;
                CockpitB26B.this.setNew.altimeter = CockpitB26B.this.fm.getAltitude();
                float f = CockpitB26B.this.waypointAzimuth();
                CockpitB26B.this.setNew.azimuth.setDeg(CockpitB26B.this.setOld.azimuth.getDeg(1.0F), CockpitB26B.this.fm.Or.azimut());
                if (CockpitB26B.this.useRealisticNavigationInstruments()) {
                    CockpitB26B.this.setNew.waypointAzimuth.setDeg(f - 90F);
                    CockpitB26B.this.setOld.waypointAzimuth.setDeg(f - 90F);
                    CockpitB26B.this.setNew.radioCompassAzimuth.setDeg(CockpitB26B.this.setOld.radioCompassAzimuth.getDeg(0.02F), CockpitB26B.this.radioCompassAzimuthInvertMinus() - CockpitB26B.this.setOld.azimuth.getDeg(1.0F) - 90F);
                    if (CockpitB26B.this.fm.AS.listenLorenzBlindLanding && CockpitB26B.this.fm.AS.isAAFIAS) {
                        CockpitB26B.this.setNew.ilsLoc = (10F * CockpitB26B.this.setOld.ilsLoc + CockpitB26B.this.getBeaconDirection()) / 11F;
                        CockpitB26B.this.setNew.ilsGS = (10F * CockpitB26B.this.setOld.ilsGS + CockpitB26B.this.getGlidePath()) / 11F;
                    } else {
                        CockpitB26B.this.setNew.ilsLoc = 0.0F;
                        CockpitB26B.this.setNew.ilsGS = 0.0F;
                    }
                } else {
                    CockpitB26B.this.setNew.waypointAzimuth.setDeg(CockpitB26B.this.setOld.waypointAzimuth.getDeg(0.1F), f);
                    CockpitB26B.this.setNew.radioCompassAzimuth.setDeg(CockpitB26B.this.setOld.radioCompassAzimuth.getDeg(0.1F), f - CockpitB26B.this.setOld.azimuth.getDeg(0.1F) - 90F);
                }
                if (CockpitB26B.this.B26B != null) CockpitB26B.this.setNew.PDI = CockpitB26B.this.B26B.fSightCurSideslip;
                CockpitB26B.this.setNew.vspeed = (199F * CockpitB26B.this.setOld.vspeed + CockpitB26B.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      prop1;
        float      prop2;
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

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitB26B() {
        super("3DO/Cockpit/B-26B/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictFlap = 0.0F;
        this.pictManf1 = 1.0F;
        this.pictManf2 = 1.0F;
        this.B26B = null;
        this.cockpitNightMats = new String[] { "texture01_dmg", "texture01", "texture02_dmg", "texture02", "texture03_dmg", "texture03", "texture04_dmg", "texture04", "texture05_dmg", "texture05", "texture06_dmg", "texture06", "texture21_dmg",
                "texture21", "texture25" };
        this.setNightMats(false);
        if (this.aircraft() instanceof B_26B) this.B26B = (B_26B) this.aircraft();
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Z_Column", 0.0F, -(this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 8F, 0.0F);
        this.mesh.chunkSetAngles("Z_AroneL", 0.0F, -(this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 68F, 0.0F);
        this.mesh.chunkSetAngles("Z_AroneR", 0.0F, -(this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 68F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, -10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_RPedalStep", 0.0F, -10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LPedalStep", 0.0F, 10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("zFlaps1", 0.0F, 38F * (this.pictFlap = 0.75F * this.pictFlap + 0.25F * this.fm.CT.FlapsControl), 0.0F);
        this.mesh.chunkSetAngles("zOilFlap1", 0.0F, -35F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("zOilFlap2", 0.0F, -35F * this.fm.EI.engines[1].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("zMix1", 0.0F, -45.8F * this.fm.EI.engines[0].getControlMix(), 0.0F);
        this.mesh.chunkSetAngles("zMix2", 0.0F, -45.8F * this.fm.EI.engines[1].getControlMix(), 0.0F);
        this.mesh.chunkSetAngles("zPitch1", 0.0F, -54F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F);
        this.mesh.chunkSetAngles("zPitch2", 0.0F, -54F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F);
        this.mesh.chunkSetAngles("zThrottle1", 0.0F, -49F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F);
        this.mesh.chunkSetAngles("zThrottle2", 0.0F, -49F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F);
        this.mesh.chunkSetAngles("zCompressor1", 0.0F, this.cvt(this.fm.EI.engines[0].getControlCompressor(), 0.0F, 1.0F, 0.0F, -50F), 0.0F);
        this.mesh.chunkSetAngles("zCompressor2", 0.0F, this.cvt(this.fm.EI.engines[1].getControlCompressor(), 0.0F, 1.0F, 0.0F, -50F), 0.0F);
        if (this.fm.Gears.cgear) {
            this.resetYPRmodifier();
            Cockpit.xyz[1] = this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 0.0135F);
            this.mesh.chunkSetLocate("Z_Gearc1", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.fm.Gears.lgear) {
            this.resetYPRmodifier();
            Cockpit.xyz[1] = this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 0.0135F);
            this.mesh.chunkSetLocate("Z_GearL1", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.fm.Gears.rgear) {
            this.resetYPRmodifier();
            Cockpit.xyz[1] = this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 0.0135F);
            this.mesh.chunkSetLocate("Z_GearR1", Cockpit.xyz, Cockpit.ypr);
        }
        this.mesh.chunkSetAngles("zFlaps2", 0.0F, 90F * this.fm.CT.getFlap(), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSecond", 0.0F, this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zAH1", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0365F, -0.0365F);
        this.mesh.chunkSetLocate("zAH2", Cockpit.xyz, Cockpit.ypr);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) this.mesh.chunkSetAngles("Z_Climb1", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("zTurnBank", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        this.mesh.chunkSetAngles("zBall", 0.0F, this.cvt(this.getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
        this.mesh.chunkSetAngles("zPDI", 0.0F, this.cvt(this.setNew.PDI, -30F, 30F, -46.5F, 46.5F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("zAlt2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("zCompass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zCompass2", 0.0F, -0.5F * this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zCompass3", 0.0F, this.setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("zMagnetic", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
            this.mesh.chunkSetAngles("zNavP", 0.0F, this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("zRPM1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            this.mesh.chunkSetAngles("zRPM2", 0.0F, this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.pictManf1 = 0.9F * this.pictManf1 + 0.1F * this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
            this.mesh.chunkSetAngles("Z_Pres2", 0.0F, this.pictManf2 = 0.9F * this.pictManf2 + 0.1F * this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_FuelPres1", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres2", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("Z_Carbair1", 0.0F, -this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -50F, 150F, -25F, 75F), 0.0F);
        this.mesh.chunkSetAngles("Z_Carbair2", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -50F, 150F, -25F, 75F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, -this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 100F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", 0.0F, this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 350F, 0.0F, 100F), 0.0F);
        this.mesh.chunkSetAngles("zOilTemp1", 0.0F, -this.cvt(this.fm.EI.engines[0].tOilOut, -50F, 150F, -25F, 75F), 0.0F);
        this.mesh.chunkSetAngles("zOilTemp2", 0.0F, this.cvt(this.fm.EI.engines[1].tOilOut, -50F, 150F, -25F, 75F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres2", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("Z_Brkpres1", 0.0F, 118F * this.fm.CT.getBrake(), 0.0F);
        this.mesh.chunkSetAngles("zFuel1", 0.0F, -this.cvt(this.fm.M.fuel, 0.0F, 2332F, 0.0F, 95F), 0.0F);
        this.mesh.chunkSetAngles("zFuel2", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 2332F, 0.0F, 95F), 0.0F);
        this.mesh.chunkSetAngles("zFuel3", 0.0F, -this.cvt(this.fm.M.fuel, 0.0F, 1916F, 0.0F, 90.5F), 0.0F);
        this.mesh.chunkSetAngles("zFuel4", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 1916F, 0.0F, 90.5F), 0.0F);
        this.mesh.chunkSetAngles("zFuel5", 0.0F, -this.cvt(this.fm.M.fuel, 1916F, 3084F, 0.0F, 102.5F), 0.0F);
        this.mesh.chunkSetAngles("zFuel6", 0.0F, this.cvt(this.fm.M.fuel, 1916F, 3084F, 0.0F, 102.5F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) this.mesh.chunkSetAngles("zFreeAir", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -70F, 150F, -26.6F, 57F), 0.0F);
        this.mesh.chunkSetAngles("zHydPres", 0.0F, this.fm.Gears.bIsHydroOperable ? 165.5F : 0.0F, 0.0F);
        float f_0_ = 0.5F * this.fm.EI.engines[0].getRPM() + 0.5F * this.fm.EI.engines[1].getRPM();
        f_0_ = 2.5F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f_0_))));
        this.mesh.chunkSetAngles("zSuction", 0.0F, this.cvt(f_0_, 0.0F, 10F, 0.0F, 297F), 0.0F);
        if (this.fm.AS.isAAFIAS) this.mesh.chunkVisible("BL_Light", this.isOnBlindLandingMarker());
        this.mesh.chunkVisible("Z_GearRed1", this.fm.CT.getGear() > 0.01F && this.fm.CT.getGear() < 0.99F);
        this.mesh.chunkVisible("Z_GearCGreen1", this.fm.CT.getGear() > 0.99F && this.fm.Gears.cgear);
        this.mesh.chunkVisible("Z_GearLGreen1", this.fm.CT.getGear() > 0.99F && this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_GearRGreen1", this.fm.CT.getGear() > 0.99F && this.fm.Gears.rgear);
    }

    public void reflectCockpitState() {
        this.fm.AS.getClass();
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Pricel_D0", false);
            this.mesh.chunkVisible("Pricel_D1", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage", true);
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictFlap;
    private float              pictManf1;
    private float              pictManf2;
    private B_26B              B26B;
    private static final float speedometerScale[] = { 0.0F, 2.5F, 59F, 126F, 155.5F, 223.5F, 240F, 254.5F, 271F, 285F, 296.5F, 308.5F, 324F, 338.5F };
    private static final float variometerScale[]  = { -180F, -157F, -130F, -108F, -84F, -46.5F, 0.0F, 46.5F, 84F, 108F, 130F, 157F, 180F };
}
