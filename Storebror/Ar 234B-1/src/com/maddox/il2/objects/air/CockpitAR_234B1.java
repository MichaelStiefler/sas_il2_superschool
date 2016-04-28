package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.BaseGameVersion;
import com.maddox.sas1946.il2.util.CrossVersion;

public class CockpitAR_234B1 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitAR_234B1.this.fm != null) {
                CockpitAR_234B1.this.setTmp = CockpitAR_234B1.this.setOld;
                CockpitAR_234B1.this.setOld = CockpitAR_234B1.this.setNew;
                CockpitAR_234B1.this.setNew = CockpitAR_234B1.this.setTmp;
                CockpitAR_234B1.this.setNew.altimeter = CockpitAR_234B1.this.fm.getAltitude();
                CockpitAR_234B1.this.setNew.throttle1 = 0.92F * CockpitAR_234B1.this.setOld.throttle1 + 0.08F * CockpitAR_234B1.this.fm.EI.engines[0].getControlThrottle();
                CockpitAR_234B1.this.setNew.throttle2 = 0.92F * CockpitAR_234B1.this.setOld.throttle2 + 0.08F * CockpitAR_234B1.this.fm.EI.engines[1].getControlThrottle();
                CockpitAR_234B1.this.setNew.vspeed = (499F * CockpitAR_234B1.this.setOld.vspeed + CockpitAR_234B1.this.fm.getVertSpeed()) / 500F;
                
                CockpitAR_234B1.this.setNew.tankSelector1 = 0.92F * CockpitAR_234B1.this.setOld.tankSelector1 + 0.08F * ((AR_234B1)(CockpitAR_234B1.this.aircraft())).tankSelectors[0];
                CockpitAR_234B1.this.setNew.tankSelector2 = 0.92F * CockpitAR_234B1.this.setOld.tankSelector2 + 0.08F * ((AR_234B1)(CockpitAR_234B1.this.aircraft())).tankSelectors[1];
                CockpitAR_234B1.this.setNew.throttleLockLever = 0.92F * CockpitAR_234B1.this.setOld.throttleLockLever + 0.08F * (((AR_234B1)(CockpitAR_234B1.this.aircraft())).throttleLocked?1F:0F);
                
                if (BaseGameVersion.is410orLater()) {
                    float f = CockpitAR_234B1.this.waypointAzimuth();
                    if (CockpitAR_234B1.this.useRealisticNavigationInstruments()) {
                        CockpitAR_234B1.this.setNew.waypointAzimuth.setDeg(f - 90F);
                        CockpitAR_234B1.this.setOld.waypointAzimuth.setDeg(f - 90F);
                    } else {
                        CockpitAR_234B1.this.setNew.waypointAzimuth.setDeg(CockpitAR_234B1.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitAR_234B1.this.setOld.azimuth.getDeg(1.0F));
                    }
                    CockpitAR_234B1.this.setNew.azimuth.setDeg(CockpitAR_234B1.this.setOld.azimuth.getDeg(1.0F), CockpitAR_234B1.this.fm.Or.azimut());
                    CockpitAR_234B1.this.w.set(CockpitAR_234B1.this.fm.getW());
                    CockpitAR_234B1.this.fm.Or.transform(CockpitAR_234B1.this.w);
                    CockpitAR_234B1.this.setNew.turn = (12F * CockpitAR_234B1.this.setOld.turn + CockpitAR_234B1.this.w.z) / 13F;
                    CockpitAR_234B1.this.setNew.beaconDirection = (10F * CockpitAR_234B1.this.setOld.beaconDirection + CockpitAR_234B1.this.getBeaconDirection()) / 11F;
                    CockpitAR_234B1.this.setNew.beaconRange = (10F * CockpitAR_234B1.this.setOld.beaconRange + CockpitAR_234B1.this.getBeaconRange()) / 11F;
                } else {
                    CockpitAR_234B1.this.setNew.azimuth.setDeg(CockpitAR_234B1.this.fm.Or.getYaw());
                    if (CockpitAR_234B1.this.setOld.azimuth.getDstDeg() > 270F && CockpitAR_234B1.this.setNew.azimuth.getDstDeg() < 90F)
                        CockpitAR_234B1.this.setOld.azimuth.setDstDeg(CockpitAR_234B1.this.setOld.azimuth.getDstDeg() - 360F);
                    if (CockpitAR_234B1.this.setOld.azimuth.getDstDeg() < 90F && CockpitAR_234B1.this.setNew.azimuth.getDstDeg() > 270F)
                        CockpitAR_234B1.this.setOld.azimuth.setDstDeg(CockpitAR_234B1.this.setOld.azimuth.getDstDeg() + 360F);
                    CockpitAR_234B1.this.setNew.waypointAzimuth.setDstDeg((10F * CockpitAR_234B1.this.setOld.waypointAzimuth.getDstDeg() + (CockpitAR_234B1.this.waypointAzimuth() - CockpitAR_234B1.this.setOld.azimuth.getDstDeg()) + World.Rnd().nextFloat(-30F, 30F)) / 11F);
                }
            }
            return true;
        }

        Interpolater() {}
    }

    private class Variables {

        float      altimeter;
        float      throttle1;
        float      throttle2;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      turn;
        float      beaconDirection;
        float      beaconRange;
        float      vspeed;
        
        float      tankSelector1;
        float      tankSelector2;
        float      throttleLockLever;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        if (BaseGameVersion.is410orLater())
            return this.waypointAzimuthInvertMinus(30F);
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0F;
        } else {
            waypoint.getP(P1);
            V.sub(P1, this.fm.Loc);
            return (float) (180D / Math.PI * Math.atan2(V.x, V.y));
        }
    }

    public CockpitAR_234B1() {
        super("3DO/Cockpit/Ar-234B-1/hier.him", "bf109");
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictGear = 0.0F;
        this.w = new Vector3f();
        this.cockpitNightMats = (new String[] { "D_gauges_1_TR", "D_gauges_2_TR", "D_gauges_3_TR", "D_gauges_4_TR", "D_gauges_6_TR", "gauges_1_TR", "gauges_2_TR", "gauges_3_TR", "gauges_4_TR", "gauges_5_TR", "gauges_6_TR", "gauges_7_TR", "gauges_8_TR",
                "gauges_9_TR" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        CrossVersion.setPrintCompassHeading(this, true);
        CrossVersion.setLimits6DoF(this, new float[] { 0.7F, 0.055F, -0.07F, 0.08F, 0.1F, -0.09F, 0.03F, -0.03F });
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        xyz[1] = this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -0.03F, 0.03F);
        if (BaseGameVersion.is413orLater())
            ypr[0] = this.fm.CT.getBrakeL() * 15F;
        else
            ypr[0] = this.fm.CT.getBrake() * 15F;
        this.mesh.chunkSetLocate("PedalL", xyz, ypr);
        xyz[1] = -xyz[1];
        if (BaseGameVersion.is413orLater())
            ypr[0] = this.fm.CT.getBrakeR() * 15F;
        else
            ypr[0] = this.fm.CT.getBrake() * 15F;
        this.mesh.chunkSetLocate("PedalR", xyz, ypr);
        ypr[1] = 0.0F;
        this.pictGear = 0.8F * this.pictGear + 0.2F * this.fm.CT.GearControl;
        this.mesh.chunkSetAngles("ruchkaShassi", this.cvt(this.pictGear, 0.0F, 1.0F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ruchkaGaza1", this.cvt(this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 1.0F, 0.0F, 60F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ruchkaGaza2", this.cvt(this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 1.0F, 0.0F, 60F), 0.0F, 0.0F);
        
        this.mesh.chunkSetAngles("ruchkaSopla", this.cvt(this.interp(this.setNew.throttleLockLever, this.setOld.throttleLockLever, f), 0.0F, 1.0F, 0F, 50F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ruchkaFuel1", this.cvt(this.interp(this.setNew.tankSelector1, this.setOld.tankSelector1, f), 0.0F, 2.0F, 0F, -36F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ruchkaFuel2", this.cvt(this.interp(this.setNew.tankSelector2, this.setOld.tankSelector2, f), 0.0F, 2.0F, 0F, -36F), 0.0F, 0.0F);
        
        this.mesh.chunkSetAngles("ETrim", -30F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RTrim", -300F * this.fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("os_V", -15F * (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Srul", 60F * (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl), 0.0F, 0.0F);
        this.resetYPRmodifier();
        if (this.cockpitLightControl)
            xyz[2] = 0.00365F;
        this.mesh.chunkSetLocate("Z_lightSW", xyz, ypr);
        xyz[2] = this.fm.CT.saveWeaponControl[0] ? 0.0059F : 0.0F;
        this.mesh.chunkSetLocate("r_one", xyz, ypr);
        xyz[2] = this.fm.CT.saveWeaponControl[3] ? 0.0059F : 0.0F;
        this.mesh.chunkSetLocate("r_two", xyz, ypr);
        this.resetYPRmodifier();
        xyz[1] = this.cvt(-this.fm.CT.trimElevator, -0.5F, 0.5F, -0.0475F, 0.0475F);
        this.mesh.chunkSetLocate("Need_ETrim", xyz, ypr);
        this.resetYPRmodifier();
        xyz[0] = this.cvt(-this.fm.CT.trimRudder, -0.5F, 0.5F, -0.029F, 0.029F);
        this.mesh.chunkSetLocate("Need_RTrim", xyz, ypr);
        this.mesh.chunkSetAngles("zClock1a", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zClock1b", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zClock1c", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAirTemp", this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 213.09F, 313.09F, -30F, 20F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zHydropress", this.fm.Gears.isHydroOperable() ? 120F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMCompc", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -22.2F, 22.2F, -22.2F, 22.2F));
        this.mesh.chunkSetAngles("zMCompa", this.cvt(this.fm.Or.getKren(), -22.2F, 22.2F, -22.2F, 22.2F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMCompb", -90F - this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 900F, 0.0F, 9F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1a", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1b", this.cvt(this.setNew.altimeter, 0.0F, 14000F, 0.0F, 315F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM1", 22.5F + this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM() * 10F * 0.25F, 2000F, 14000F, 2.0F, 14F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM2", 22.5F + this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM() * 10F * 0.25F, 2000F, 14000F, 2.0F, 14F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zGasPrs1a", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : this.fm.EI.engines[0].getPowerOutput(), 0.0F, 1F, 0.0F, 270F) * this.cvt(this.fm.EI.engines[0].tOilOut, 110F, 130F, 1F, 1.1F) * this.cvt(this.fm.EI.engines[0].tWaterOut, 630F, 650F, 1F, 1.1F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zGasPrs1b", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : this.fm.EI.engines[0].getPowerOutput(), 0.0F, 1F, 0.0F, 130F) * this.cvt(this.fm.EI.engines[0].tOilOut, 110F, 130F, 1F, 1.15F) * this.cvt(this.fm.EI.engines[0].tWaterOut, 630F, 650F, 1F, 1.15F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zGasPrs2a", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : this.fm.EI.engines[1].getPowerOutput(), 0.0F, 1F, 0.0F, 270F) * this.cvt(this.fm.EI.engines[1].tOilOut, 110F, 130F, 1F, 1.1F) * this.cvt(this.fm.EI.engines[1].tWaterOut, 630F, 650F, 1F, 1.1F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zGasPrs2b", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : this.fm.EI.engines[1].getPowerOutput(), 0.0F, 1F, 0.0F, -130F) * this.cvt(this.fm.EI.engines[1].tOilOut, 110F, 130F, 1F, 1.15F) * this.cvt(this.fm.EI.engines[1].tWaterOut, 630F, 650F, 1F, 1.15F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilPrs1a", this.fm.EI.engines[0].tOilOut < 110F ? this.cvt(this.fm.EI.engines[0].tOilOut, 20F, 110F, 0.0F, 135F) : this.cvt(this.fm.EI.engines[0].tOilOut, 110F, 138F, 135F, 270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilPrs2a", this.fm.EI.engines[1].tOilOut < 110F ? this.cvt(this.fm.EI.engines[1].tOilOut, 20F, 110F, 0.0F, 135F) : this.cvt(this.fm.EI.engines[1].tOilOut, 110F, 138F, 135F, 270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuelPrs1a", this.fm.M.fuel <= 1.0F ? 0F : this.cvt(this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 1.0F, 0.0F, 135F) * this.cvt(this.fm.EI.engines[0].tOilOut, 110F, 138F, 1F, 2F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuelPrs2a", this.fm.M.fuel <= 1.0F ? 0F : this.cvt(this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 1.0F, 0.0F, 135F) * this.cvt(this.fm.EI.engines[1].tOilOut, 110F, 138F, 1F, 2F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zGasTemp1a", this.cvt(this.fm.EI.engines[0].getThrustOutput() * this.cvt(this.fm.EI.engines[0].tWaterOut, 630F, 650F, 1F, 2F), 0F, 2F, 0.0F, 61F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zGasTemp2a", this.cvt(this.fm.EI.engines[1].getThrustOutput() * this.cvt(this.fm.EI.engines[1].tWaterOut, 630F, 650F, 1F, 2F), 0F, 2F, 0.0F, 61F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zHorizon1b", -this.fm.Or.getKren(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.025F, -0.025F);
        this.mesh.chunkSetLocate("zHorizon1a", xyz, ypr);
        this.mesh.chunkSetAngles("zSlide1a", this.cvt(this.getBall(6D), -6F, 6F, -15F, 15F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRoll1a", this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 26.5F, -26.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zVariometer1a", this.setNew.vspeed < 0.0F ? -this.floatindex(this.cvt(-this.setNew.vspeed, 0.0F, 30F, 0.0F, 6F), vsiNeedleScale) : this.floatindex(this.cvt(this.setNew.vspeed, 0.0F, 30F, 0.0F, 6F), vsiNeedleScale), 0.0F, 0.0F);
        if (BaseGameVersion.is410orLater() && this.useRealisticNavigationInstruments()) {
            this.mesh.chunkSetAngles("zAzimuth1b", this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zAzimuth1a", -this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("zAzimuth1a", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("zAzimuth1b", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Course1a", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Course1b", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Air1", 135F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuelQ1", this.cvt(this.fm.M.fuel, 0.0F, 2400F, 0.0F, 60F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuelQ2", this.cvt(this.fm.M.fuel, 0.0F, 4000F, 0.0F, 60F), 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_Red1", this.fm.getSpeedKMH() < 160F);
        this.mesh.chunkVisible("Z_Red2", this.fm.M.fuel < 600F);
        this.mesh.chunkVisible("Z_Red3", this.fm.M.fuel < 250F);
        this.mesh.chunkVisible("Z_Red4", this.fm.CT.getFlap() < 0.1F);
        if (BaseGameVersion.is412orLater()) {
            this.mesh.chunkVisible("Z_Red5", this.fm.CT.getGearL() < 0.01F);
            this.mesh.chunkVisible("Z_Red6", this.fm.CT.getGearC() < 0.01F);
            this.mesh.chunkVisible("Z_Red7", this.fm.CT.getGearR() < 0.01F);
            this.mesh.chunkVisible("Z_Green2", this.fm.CT.getGearL() > 0.99F && this.fm.Gears.lgear);
            this.mesh.chunkVisible("Z_Green3", this.fm.CT.getGearC() > 0.99F && this.fm.Gears.cgear);
            this.mesh.chunkVisible("Z_Green4", this.fm.CT.getGearR() > 0.99F && this.fm.Gears.rgear);
        } else {
            this.mesh.chunkVisible("Z_Red5", this.fm.CT.getGear() < 0.01F);
            this.mesh.chunkVisible("Z_Red6", this.fm.CT.getGear() < 0.01F);
            this.mesh.chunkVisible("Z_Red7", this.fm.CT.getGear() < 0.01F);
            this.mesh.chunkVisible("Z_Green2", this.fm.CT.getGear() > 0.99F && this.fm.Gears.lgear);
            this.mesh.chunkVisible("Z_Green3", this.fm.CT.getGear() > 0.99F && this.fm.Gears.cgear);
            this.mesh.chunkVisible("Z_Green4", this.fm.CT.getGear() > 0.99F && this.fm.Gears.rgear);
        }
        this.mesh.chunkVisible("Z_Red8", this.fm.CT.getFlap() < 0.1F);
        this.mesh.chunkVisible("Z_Green1", this.fm.CT.getFlap() > 0.665F);
        this.mesh.chunkVisible("Z_Green5", this.fm.CT.getFlap() > 0.665F);
        this.mesh.chunkVisible("Z_White1", this.fm.CT.getFlap() > 0.265F);
        this.mesh.chunkVisible("Z_White2", this.fm.CT.getFlap() > 0.265F);
        this.mesh.chunkSetAngles("Z_Course1b", this.cvt(this.setNew.beaconDirection, -45F, 45F, -20F, 20F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Course1a", this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, 20F, -20F), 0.0F, 0.0F);
        this.mesh.chunkVisible("AFN2_RED", BaseGameVersion.is410orLater() && this.isOnBlindLandingMarker());
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl)
            this.setNightMats(true);
        else
            this.setNightMats(false);
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
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
            this.mesh.chunkVisible("Z_Holes2_D1", true);
            this.mesh.chunkVisible("Z_Holes3_D1", true);
            this.mesh.chunkVisible("Z_Holes4_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) == 0)
            ;
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("pribors2_d1", true);
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("zAlt1a", false);
            this.mesh.chunkVisible("zAlt1b", false);
            this.mesh.chunkVisible("zHorizon1a", false);
            this.mesh.chunkVisible("zHorizon1b", false);
            this.mesh.chunkVisible("zSlide1a", false);
            this.mesh.chunkVisible("zRoll1a", false);
            this.mesh.chunkVisible("zRPM1", false);
            this.mesh.chunkVisible("zVariometer1a", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Z_Holes5_D1", true);
            this.mesh.chunkVisible("pribors1_d1", true);
            this.mesh.chunkVisible("pribors1", false);
            this.mesh.chunkVisible("zGasPrs1b", false);
            this.mesh.chunkVisible("zGasPrs2b", false);
            this.mesh.chunkVisible("zSpeed1a", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("pribors4_d1", true);
            this.mesh.chunkVisible("pribors4", false);
            this.mesh.chunkVisible("zGasPrs2a", false);
            this.mesh.chunkVisible("zOilPrs1a", false);
            this.mesh.chunkVisible("zOilPrs2a", false);
            this.mesh.chunkVisible("zFuelPrs1a", false);
            this.mesh.chunkVisible("zFuelPrs2a", false);
            this.mesh.chunkVisible("zGasTemp1a", false);
            this.mesh.chunkVisible("zGasTemp2a", false);
            this.mesh.chunkVisible("zFuelQ1", false);
            this.mesh.chunkVisible("zFuelQ2", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) == 0)
            ;
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("Z_Holes6_D1", true);
            this.mesh.chunkVisible("pribors3_d1", true);
            this.mesh.chunkVisible("pribors3", false);
            this.mesh.chunkVisible("Z_Course1a", false);
            this.mesh.chunkVisible("Z_Course1b", false);
            this.mesh.chunkVisible("zRPM2", false);
            this.mesh.chunkVisible("zGasPrs1a", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) == 0)
            ;
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void doToggleAim(boolean paramBoolean) {}

    public boolean isToggleAim() {
        return false;
    }

    public boolean isToggleUp() {
        return false;
    }    
    
    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private float              pictAiler;
    private float              pictElev;
    private float              pictGear;
    public Vector3f            w;
    private static final float speedometerScale[] = { 0.0F, 18.5F, 67F, 117F, 164F, 215F, 267F, 320F, 379F, 427F, 428F };
    private static final float rpmScale[]         = { 0.0F, 0.0F, 0.0F, 16.5F, 34.5F, 55F, 77.5F, 104F, 133.5F, 162.5F, 192F, 224F, 254F, 255.5F, 260F };
    private static final float vsiNeedleScale[]   = { 0.0F, 48F, 82F, 96.5F, 111F, 120.5F, 130F, 130F };

    public float[]             limits6DoF         = new float[] { 0.7F, 0.055F, -0.07F, 0.08F, 0.1F, -0.09F, 0.03F, -0.03F };

    static {
        Property.set(CLASS.THIS(), "normZN", 1.12F);
    }

}
