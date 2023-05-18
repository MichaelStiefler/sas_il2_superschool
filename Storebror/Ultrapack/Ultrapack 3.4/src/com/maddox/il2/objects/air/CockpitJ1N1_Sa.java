package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitJ1N1_Sa extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitJ1N1_Sa.this.fm != null) {
                CockpitJ1N1_Sa.this.setTmp = CockpitJ1N1_Sa.this.setOld;
                CockpitJ1N1_Sa.this.setOld = CockpitJ1N1_Sa.this.setNew;
                CockpitJ1N1_Sa.this.setNew = CockpitJ1N1_Sa.this.setTmp;
                CockpitJ1N1_Sa.this.setNew.flap = (0.88F * CockpitJ1N1_Sa.this.setOld.flap) + (0.12F * CockpitJ1N1_Sa.this.fm.CT.FlapsControl);
                CockpitJ1N1_Sa.this.setNew.tlock = (0.7F * CockpitJ1N1_Sa.this.setOld.tlock) + (0.3F * (CockpitJ1N1_Sa.this.fm.Gears.bTailwheelLocked ? 1.0F : 0.0F));
                if (CockpitJ1N1_Sa.this.cockpitDimControl) {
                    if (CockpitJ1N1_Sa.this.setNew.dimPosition > 0.0F) {
                        CockpitJ1N1_Sa.this.setNew.dimPosition = CockpitJ1N1_Sa.this.setOld.dimPosition - 0.05F;
                    }
                } else if (CockpitJ1N1_Sa.this.setNew.dimPosition < 1.0F) {
                    CockpitJ1N1_Sa.this.setNew.dimPosition = CockpitJ1N1_Sa.this.setOld.dimPosition + 0.05F;
                }
                if ((CockpitJ1N1_Sa.this.fm.CT.GearControl < 0.5F) && (CockpitJ1N1_Sa.this.setNew.gear < 1.0F)) {
                    CockpitJ1N1_Sa.this.setNew.gear = CockpitJ1N1_Sa.this.setOld.gear + 0.02F;
                }
                if ((CockpitJ1N1_Sa.this.fm.CT.GearControl > 0.5F) && (CockpitJ1N1_Sa.this.setNew.gear > 0.0F)) {
                    CockpitJ1N1_Sa.this.setNew.gear = CockpitJ1N1_Sa.this.setOld.gear - 0.02F;
                }
                CockpitJ1N1_Sa.this.setNew.throttle = (0.9F * CockpitJ1N1_Sa.this.setOld.throttle) + (0.1F * CockpitJ1N1_Sa.this.fm.CT.PowerControl);
                CockpitJ1N1_Sa.this.setNew.manifold = (0.8F * CockpitJ1N1_Sa.this.setOld.manifold) + (0.2F * CockpitJ1N1_Sa.this.fm.EI.engines[0].getManifoldPressure());
                CockpitJ1N1_Sa.this.setNew.pitch = (0.9F * CockpitJ1N1_Sa.this.setOld.pitch) + (0.1F * CockpitJ1N1_Sa.this.fm.EI.engines[0].getControlProp());
                CockpitJ1N1_Sa.this.setNew.mix = (0.9F * CockpitJ1N1_Sa.this.setOld.mix) + (0.1F * CockpitJ1N1_Sa.this.fm.EI.engines[0].getControlMix());
                CockpitJ1N1_Sa.this.setNew.altimeter = CockpitJ1N1_Sa.this.fm.getAltitude();
                CockpitJ1N1_Sa.this.setNew.azimuth.setDeg(CockpitJ1N1_Sa.this.setOld.azimuth.getDeg(1.0F), CockpitJ1N1_Sa.this.fm.Or.azimut());
                CockpitJ1N1_Sa.this.setNew.vspeed = ((199F * CockpitJ1N1_Sa.this.setOld.vspeed) + CockpitJ1N1_Sa.this.fm.getVertSpeed()) / 200F;
                CockpitJ1N1_Sa.this.setNew.waypointAzimuth.setDeg(CockpitJ1N1_Sa.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitJ1N1_Sa.this.fm.Or.azimut() + 90F);
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      flap;
        float      throttle;
        float      pitch;
        float      mix;
        float      gear;
        float      tlock;
        float      altimeter;
        float      manifold;
        AnglesFork azimuth;
        float      vspeed;
        float      dimPosition;
        AnglesFork waypointAzimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0.0F;
        }
        waypoint.getP(this.tmpP);
        this.tmpV.sub(this.tmpP, this.fm.Loc);
        float f;
        for (f = (float) (Math.toDegrees(Math.atan2(-this.tmpV.y, this.tmpV.x))); f <= -180F; f += 180F) {
            ;
        }
        while (f > 180F) {
            f -= 180F;
        }
        return f;
    }

    public CockpitJ1N1_Sa() {
        super("3DO/Cockpit/J1N1-Sa/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "GP_I", "GP_II", "GP_III", "GP_IV", "GP_V", "GP_VI", "GP_VII", "gauges5", "GP1_d1", "GP1", "GP2_d1", "GP2", "GP3", "GP4_d1", "GP4", "GP5_d1", "GP5", "GP6_d1", "GP6", "GP7", "GP9", "throttle", "Volt_Tacho" });
        this.setNightMats(false);
        this.cockpitDimControl = !this.cockpitDimControl;
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("FLCS", -20F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)), 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 20F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.setNew.gear, 0.0F, 0.05F, 0.0F, -0.008F);
        if (this.setNew.gear > 0.85F) {
            Cockpit.xyz[1] = 0.0F;
        }
        this.mesh.chunkSetLocate("GearHandleKnob", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("GearLockHandle", this.cvt(this.setNew.gear, 0.1F, 0.25F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("GearLockSpring", 0.0F, 0.0F, this.cvt(this.setNew.gear, 0.1F, 0.25F, 0.0F, 45F));
        this.mesh.chunkSetAngles("GearHandle", this.cvt(this.setNew.gear, 0.5F, 1.0F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("FlapHandle", 0.0F, 0.0F, -60F * this.setNew.flap);
        this.resetYPRmodifier();
        if (Math.abs(this.setNew.flap - this.setOld.flap) > 0.001F) {
            Cockpit.xyz[2] = -0.008F;
        }
        this.mesh.chunkSetLocate("FlapHandleKnob", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("TWheelLockLvr", -30F * this.setNew.tlock, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ChargerLvr", 0.0F, 0.0F, 40F * this.fm.EI.engines[0].getControlCompressor());
        this.mesh.chunkSetAngles("TQHandle", 0.0F, 0.0F, 54.5454F * this.setNew.throttle);
        this.mesh.chunkSetAngles("PropPitchLvr", 0.0F, 0.0F, 60F * this.setNew.pitch);
        this.mesh.chunkSetAngles("MixLvr", 0.0F, 0.0F, 60F * this.cvt(this.setNew.mix, 1.0F, 1.2F, 0.5F, 1.0F));
        this.mesh.chunkSetAngles("PedalCrossBar", 0.0F, -15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Pedal_L", 15F * this.fm.CT.getBrake(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Pedal_R", 15F * this.fm.CT.getBrake(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RudderCable_L", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RudderCable_R", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("PriTrigger", 0.0F, 0.0F, this.fm.CT.saveWeaponControl[0] ? 15F : 0.0F);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[1]) {
            Cockpit.xyz[2] = -0.005F;
        }
        this.mesh.chunkSetLocate("SecTrigger", Cockpit.xyz, Cockpit.ypr);
        if (this.fm.CT.saveWeaponControl[2]) {
            Cockpit.xyz[2] = -0.005F;
        }
        this.mesh.chunkSetLocate("TreTrigger", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("CowlFlapLvr", 0.0F, 0.0F, 50F * this.fm.EI.engines[0].getControlRadiator());
        this.mesh.chunkSetAngles("OilCoolerLvr", -86F * this.fm.EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("FBoxSW_ANO", 0.0F, 0.0F, this.fm.AS.bNavLightsOn ? 50F : 0.0F);
        this.mesh.chunkSetAngles("FBoxSW_LandLt", 0.0F, 0.0F, this.fm.AS.bLandingLightOn ? 50F : 0.0F);
        this.mesh.chunkSetAngles("FBoxSW_Starter", 0.0F, 0.0F, this.fm.EI.engines[0].getStage() <= 0 ? 0.0F : 50F);
        this.mesh.chunkSetAngles("FBoxSW_UVLight", 0.0F, 0.0F, this.cockpitLightControl ? 50F : 0.0F);
        this.mesh.chunkSetAngles("GSDimmArm", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -55F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 0.05F);
        this.mesh.chunkSetLocate("GSDimmBase", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("NeedCylTemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 360F, 0.0F, 75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedCylTemp2", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 360F, 0.0F, 75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedExhTemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 324F, 0.0F, 75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedOilTemp", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedOilPress", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 10F, 0.0F, 30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedOilPress2", this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut), 0.0F, 10F, 0.0F, 30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedFuelPress", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.6F, 0.0F, 305F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedFuelPress2", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.6F, 0.0F, 305F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedVMPress", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 3F, 0.0F, 300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("IgnitionSwitch", this.fm.EI.engines[0].getStage() != 0 ? this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 60F) : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("IgnitionSwitch1", this.fm.EI.engines[1].getStage() != 0 ? this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 60F) : 0.0F, 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("NeedTurn", this.cvt(this.w.z, -0.23562F, 0.23562F, -25F, 25F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedBank", this.cvt(this.getBall(8D), -8F, 8F, 10F, -10F), 0.0F, 0.0F);
        if (((this.fm.AS.astateCockpitState & 0x40) == 0) || ((this.fm.AS.astateCockpitState & 0x10) == 0) || ((this.fm.AS.astateCockpitState & 4) == 0)) {
            this.mesh.chunkSetAngles("NeedAHCyl", 0.0F, -this.fm.Or.getKren(), 0.0F);
            this.mesh.chunkSetAngles("NeedAHBar", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -45F, 45F, 20F, -20F));
        }
        this.mesh.chunkSetAngles("NeedClimb", this.floatindex(this.cvt(this.setNew.vspeed, -30F, 30F, 0.5F, 6.5F), CockpitJ1N1_Sa.vsiNeedleScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedSpeed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1000F, 0.0F, 20F), CockpitJ1N1_Sa.speedometerScale), 0.0F, 0.0F);
        if (((this.fm.AS.astateCockpitState & 0x40) == 0) || ((this.fm.AS.astateCockpitState & 8) == 0) || ((this.fm.AS.astateCockpitState & 0x20) == 0)) {
            this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
            this.mesh.chunkSetAngles("Z_Compass3", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
            this.mesh.chunkSetAngles("Z_Compass5", -this.setNew.waypointAzimuth.getDeg(f * 0.1F) + 90F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("NeedAlt_Km", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedAlt_M", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedMin", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedHour", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedManPress", this.cvt(this.setNew.manifold, 0.4000511F, 1.799932F, -144F, 192F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedRPM", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 7F), CockpitJ1N1_Sa.revolutionsScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedFuel", this.cvt(this.fm.M.fuel, 0.0F, 525F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkVisible("FlareFuelLow", (this.fm.M.fuel < 52.5F) && ((this.fm.AS.astateCockpitState & 0x40) == 0));
        this.mesh.chunkVisible("FlareGearDn_A", this.fm.CT.getGear() > 0.99F);
        this.mesh.chunkVisible("FlareGearDn_B", this.fm.CT.getGear() > 0.99F);
        this.mesh.chunkVisible("FlareGearUp_A", this.fm.CT.getGear() < 0.01F);
        this.mesh.chunkVisible("FlareGearUp_B", this.fm.CT.getGear() < 0.01F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.materialReplace("GP_II", "DGP_II");
            this.mesh.chunkVisible("NeedManPress", false);
            this.mesh.chunkVisible("NeedRPM", false);
            this.mesh.chunkVisible("NeedFuel", false);
            this.retoggleLight();
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.materialReplace("GP_III", "DGP_III");
            this.mesh.chunkVisible("NeedAlt_Km", false);
            this.mesh.chunkVisible("NeedAlt_M", false);
            this.mesh.chunkVisible("NeedSpeed", false);
            this.retoggleLight();
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.materialReplace("GP_IV", "DGP_IV");
            this.mesh.chunkVisible("NeedCylTemp", false);
            this.mesh.chunkVisible("NeedCylTemp2", false);
            this.mesh.chunkVisible("NeedOilTemp", false);
            this.mesh.chunkVisible("NeedVAmmeter", false);
            this.mesh.materialReplace("GP_IV_night", "DGP_IV_night");
            this.retoggleLight();
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.materialReplace("GP_V", "DGP_V");
            this.mesh.chunkVisible("NeedExhTemp", false);
            this.mesh.chunkVisible("NeedOilPress", false);
            this.mesh.chunkVisible("NeedOilPress2", false);
            this.mesh.chunkVisible("NeedHour", false);
            this.mesh.chunkVisible("NeedMin", false);
            this.mesh.chunkVisible("NeedTurn", false);
            this.mesh.chunkVisible("NeedBank", false);
            this.retoggleLight();
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("GunSight_T3", false);
            this.mesh.chunkVisible("GS_Lenz", false);
            this.mesh.chunkVisible("GSGlassMain", false);
            this.mesh.chunkVisible("GSDimmArm", false);
            this.mesh.chunkVisible("GSDimmBase", false);
            this.mesh.chunkVisible("GSGlassDimm", false);
            this.mesh.chunkVisible("DGunSight_T3", true);
            this.mesh.chunkVisible("DGS_Lenz", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("DamageGlass2", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("DamageGlass1", true);
            this.mesh.chunkVisible("DamageGlass3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.materialReplace("GP_VI", "DGP_VI");
            this.mesh.chunkVisible("NeedFuelPress", false);
            this.mesh.chunkVisible("NeedFuelPress2", false);
            this.mesh.chunkVisible("NeedVMPress", false);
            this.mesh.chunkVisible("NeedClimb", false);
            this.mesh.materialReplace("GP_VI_night", "DGP_VI_night");
            this.retoggleLight();
            this.mesh.chunkVisible("DamageGlass4", true);
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
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

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private static final float vsiNeedleScale[]   = { -200F, -160F, -125F, -90F, 90F, 125F, 160F, 200F };
    private static final float speedometerScale[] = { 0.0F, 10F, 35F, 70F, 105F, 145F, 190F, 230F, 275F, 315F, 360F, 397.5F, 435F, 470F, 505F, 537.5F, 570F, 600F, 630F, 655F, 680F };
    private static final float revolutionsScale[] = { 0.0F, 20F, 75F, 125F, 180F, 220F, 285F, 335F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

    static {
        Property.set(CockpitJ1N1_Sa.class, "normZNs", new float[] { 1.07F, 0.56F, 0.89F, 0.56F });
    }

}
