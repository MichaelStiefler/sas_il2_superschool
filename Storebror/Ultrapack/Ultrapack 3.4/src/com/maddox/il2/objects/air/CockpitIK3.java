package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitIK3 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitIK3.this.bNeedSetUp) {
                CockpitIK3.this.reflectPlaneMats();
                CockpitIK3.this.bNeedSetUp = false;
            }
            if (CockpitIK3.this.fm != null) {
                CockpitIK3.this.setTmp = CockpitIK3.this.setOld;
                CockpitIK3.this.setOld = CockpitIK3.this.setNew;
                CockpitIK3.this.setNew = CockpitIK3.this.setTmp;
                CockpitIK3.this.setNew.mixture = ((32F * CockpitIK3.this.setOld.mixture) + (5F * CockpitIK3.this.fm.EI.engines[0].getControlMix())) / 38F;
                CockpitIK3.this.setNew.throttle = ((10F * CockpitIK3.this.setOld.throttle) + CockpitIK3.this.fm.CT.PowerControl) / 11F;
                CockpitIK3.this.setNew.propPitch = (0.9F * CockpitIK3.this.setOld.propPitch) + (0.1F * CockpitIK3.this.fm.EI.engines[0].getControlProp());
                CockpitIK3.this.setNew.azimuth.setDeg(CockpitIK3.this.setOld.azimuth.getDeg(1.0F), CockpitIK3.this.fm.Or.azimut());
                CockpitIK3.this.setNew.waypointAzimuth = ((10F * CockpitIK3.this.setOld.waypointAzimuth) + CockpitIK3.this.waypointAzimuth()) / 11F;
                CockpitIK3.this.setNew.vspeed = ((199F * CockpitIK3.this.setOld.vspeed) + CockpitIK3.this.fm.getVertSpeed()) / 200F;
                CockpitIK3.this.setNew.altimeter = CockpitIK3.this.fm.getAltitude();
                if (((CockpitIK3.this.setOld.gearLever != 0.0F) && (CockpitIK3.this.gears == CockpitIK3.this.fm.CT.getGear())) || !CockpitIK3.this.fm.Gears.bIsHydroOperable) {
                    CockpitIK3.this.setNew.gearLever = 0.8F * CockpitIK3.this.setOld.gearLever;
                } else if (CockpitIK3.this.gears < CockpitIK3.this.fm.CT.getGear()) {
                    CockpitIK3.this.gears = CockpitIK3.this.fm.CT.getGear();
                    CockpitIK3.this.setNew.gearLever = (0.8F * CockpitIK3.this.setOld.gearLever) + 9F;
                } else if (CockpitIK3.this.gears > CockpitIK3.this.fm.CT.getGear()) {
                    CockpitIK3.this.gears = CockpitIK3.this.fm.CT.getGear();
                    CockpitIK3.this.setNew.gearLever = (0.8F * CockpitIK3.this.setOld.gearLever) - 9F;
                }
                CockpitIK3.this.setNew.radiator = (0.8F * CockpitIK3.this.setOld.radiator) + (0.2F * CockpitIK3.this.fm.CT.getRadiator());
                IK_3xyz ik_3xyz = (IK_3xyz) CockpitIK3.this.aircraft();
                if ((ik_3xyz.emergencyPressure == 0.0F) || (CockpitIK3.this.fm.CT.GearControl == 1.0F)) {
                    CockpitIK3.this.setNew.emGearLever = 0.8F * CockpitIK3.this.setOld.emGearLever;
                    CockpitIK3.this.setNew.emPressLever = 0.9F * CockpitIK3.this.setOld.emPressLever;
                } else {
                    CockpitIK3.this.setNew.emGearLever = (0.8F * CockpitIK3.this.setOld.emGearLever) + 0.2F;
                    if (ik_3xyz.emergencyPressure > CockpitIK3.this.oldEmPressure) {
                        CockpitIK3.this.setNew.emPressLever = (0.9F * CockpitIK3.this.setOld.emPressLever) + 0.1F;
                    } else {
                        CockpitIK3.this.setNew.emPressLever = 0.9F * CockpitIK3.this.setOld.emPressLever;
                    }
                    CockpitIK3.this.oldEmPressure = ik_3xyz.emergencyPressure;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        float      vspeed;
        AnglesFork azimuth;
        float      waypointAzimuth;
        float      mixture;
        float      throttle;
        float      propPitch;
        float      gearLever;
        float      radiator;
        float      emGearLever;
        float      emPressLever;

        private Variables() {
            this.azimuth = new AnglesFork();
        }
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0.0F;
        } else {
            waypoint.getP(this.tmpP);
            this.tmpV.sub(this.tmpP, this.fm.Loc);
            return (float) (Math.toDegrees(Math.atan2(-this.tmpV.y, this.tmpV.x)));
        }
    }

    public CockpitIK3() {
        super("3DO/Cockpit/IK-3/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.starterPressure = 0.0F;
        this.mainHydrPressure = 0.0F;
        this.missionStartTime = 0.0F;
        this.gears = 0.0F;
        this.oldEmPressure = 0.0F;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "Instr1", "Instr2", "Instr3", "Instr4", "Instr5", "Instr6", "Instr1_d", "Instr2_d", "Instr3_d", "Instr4_d", "Instr5_d" });
        this.setNightMats(false);
        this.missionStartTime = World.getTimeofDay();
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Z_Flaps", this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 720F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_VTrim", this.cvt(this.fm.CT.getTrimElevatorControl(), -0.5F, 0.5F, -90F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_HTrim", this.cvt(this.fm.CT.getTrimRudderControl(), -0.5F, 0.5F, 90F, -90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mix", -30F * this.interp(this.setNew.mixture, this.setOld.mixture, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle", -113F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PPitch", -113F * this.interp(this.setNew.propPitch, this.setOld.propPitch, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TWLock", this.fm.Gears.bTailwheelLocked ? -10F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_M1Sw", this.cvt(this.fm.EI.engines[0].getControlMagnetos() & 1, 0.0F, 1.0F, 0.0F, 45F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_M2Sw", this.cvt((this.fm.EI.engines[0].getControlMagnetos() >> 1) & 1, 0.0F, 1.0F, 0.0F, 45F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdFuelP", this.cvt(this.fm.M.fuel, 0.0F, 1.0F, 0.0F, -150F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdFuel", this.floatindex(this.cvt(this.fm.M.fuel, 0.0F, 205F, 0.0F, 3F), CockpitIK3.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdManPres", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.7F, 1.4F, 0.0F, -323F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdOilP", this.cvt(Math.max(4F, this.fm.EI.engines[0].tOilIn * 0.084F) * this.fm.EI.engines[0].getReadyness(), 0.0F, 10F, 0.0F, -300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdSpd", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 700F, 0.0F, 14F), CockpitIK3.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdAHB", this.cvt(this.getBall(8D), -8F, 8F, 10F, -10F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.025F, -0.025F);
        this.mesh.chunkSetLocate("Z_NdAHP", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_NdAHR", this.fm.Or.getKren(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdCmpsB", this.setNew.azimuth.getDeg(1.0F) + 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdCmpsC", this.setNew.waypointAzimuth + 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdVario", this.cvt(this.setNew.vspeed, -20F, 20F, 180F, -180F), 0.0F, 0.0F);
        float f1 = 0.0F;
        if ((this.fm.EI.engines[0].getStage() > 0) && (this.fm.EI.engines[0].getStage() < 3)) {
            f1 = 72F + World.Rnd().nextFloat(-5F, 0.0F);
        }
        float f2 = 214F;
        float f3 = 0.05F;
        if (this.fm.EI.engines[0].getStage() == 0) {
            f3 = 0.004F;
            f2 = 0.0F;
        }
        this.starterPressure = (0.95F * this.starterPressure) + (0.05F * f1);
        this.mainHydrPressure = ((1.0F - f3) * this.mainHydrPressure) + (f3 * f2);
        this.mesh.chunkSetAngles("Z_NdApres", -this.starterPressure, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdHP1", -this.mainHydrPressure, 0.0F, 0.0F);
        IK_3xyz ik_3xyz = (IK_3xyz) this.aircraft();
        this.mesh.chunkSetAngles("Z_NdHP1E", this.cvt(ik_3xyz.emergencyPressure, 0.0F, 1.4F, 0.0F, -301F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdHP3", this.cvt(this.fm.CT.getBrake(), 0.0F, 1.0F, 0.0F, -149F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdCA", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, -300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdMHour", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, -720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdMMin", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdMSec", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -21600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdMHour1", this.cvt(World.getTimeofDay() - this.missionStartTime, 0.0F, 24F, 0.0F, -720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdMMin1", this.cvt((World.getTimeofDay() - this.missionStartTime) % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdWatrT", this.cvt(this.fm.EI.engines[0].tWaterOut, 60F, 140F, 0.0F, -289F), 0.0F, 0.0F);
        if (this.fm.Gears.lgear) {
            this.mesh.chunkSetAngles("Z_NdLGr", this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, -90F), 0.0F, 0.0F);
        }
        if (this.fm.Gears.rgear) {
            this.mesh.chunkSetAngles("Z_NdRGr", this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, -90F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_NdFlap", this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, -65F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdPitch", this.cvt(this.fm.CT.getStepControl(), 0.0F, 1.0F, 0.0F, -90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdAltBig", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdAltSmall", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, -3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdRPM", this.cvt(this.fm.EI.engines[0].getRPM(), 500F, 3200F, 0.0F, -331F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NdOilT", this.cvt(this.fm.EI.engines[0].tOilOut, 60F, 140F, 0.0F, -289F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gear", this.setNew.gearLever, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_EmGear", this.cvt(this.setNew.emGearLever, 0.0F, 1.0F, 0.0F, -80F), 0.0F, 0.0F);
        if (ik_3xyz.emergencyPressure > 0.0F) {
            this.mesh.chunkSetAngles("Z_NdHP2", this.cvt(ik_3xyz.emergencyPressure, 0.0F, 1.0F, 0.0F, -149F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_NdHP2", this.cvt(Math.abs(this.setNew.gearLever), 0.0F, 45F, 0.0F, -149F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Rad1", this.cvt(this.setNew.radiator, 0.0F, 1.0F, 0.0F, 60F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Rad2", this.cvt(this.setNew.radiator, 0.0F, 1.0F, 0.0F, 60F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Sw1", this.cockpitLightControl ? 90F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Sw2", this.fm.AS.bNavLightsOn ? 90F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pump", this.cvt(this.setNew.emPressLever, 0.0F, 1.0F, 0.0F, 40F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trigger", !this.fm.CT.WeaponControl[0] && !this.fm.CT.WeaponControl[1] ? 0.0F : -5.5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pitch", -15F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ElevRod", -15F * this.pictElev, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Ailer1", -20F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Ailer2", -70F * this.pictAiler, 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = -0.009F * this.fm.CT.getBrake();
        this.mesh.chunkSetLocate("Z_Brake", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_CnLockL", this.cvt(this.fm.CT.getCockpitDoor(), 0.0F, 0.05F, 0.0F, -60F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_CnLockR", this.cvt(this.fm.CT.getCockpitDoor(), 0.0F, 0.05F, 0.0F, -60F), 0.0F, 0.0F);
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 0.65F);
        this.mesh.chunkSetLocate("Z_Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_PedalL", this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -6F, 6F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RCableL", this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -6F, 6F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalR", this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -6F, 6F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RCableR", this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -6F, 6F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Rudder", this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -6F, 6F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PRod", this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -6F, 6F), 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XHullDamage2", true);
            this.mesh.materialReplace("Instr1", "Instr1_d");
            this.mesh.materialReplace("Instr1_night", "Instr1_d_night");
            this.mesh.chunkVisible("Z_NdFuel", false);
            this.mesh.chunkVisible("Z_NdManPres", false);
            this.mesh.chunkVisible("Z_NdFuelP", false);
            this.mesh.chunkVisible("Z_NdOilP", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("XOil1", true);
            this.mesh.chunkVisible("XOil2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.materialReplace("Instr3", "Instr3_d");
            this.mesh.materialReplace("Instr3_night", "Instr3_d_night");
            this.mesh.chunkVisible("Z_NdSpd", false);
            this.mesh.chunkVisible("Z_NdVario", false);
            this.mesh.chunkVisible("Z_NdHP1E", false);
            this.mesh.chunkVisible("Z_NdHP3", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("XHullDamage2", true);
            this.mesh.materialReplace("Instr2", "Instr2_d");
            this.mesh.materialReplace("Instr2_night", "Instr2_d_night");
            this.mesh.chunkVisible("Z_NdOilT", false);
            this.mesh.chunkVisible("Z_NdRPM", false);
            this.mesh.chunkVisible("Z_NdAltBig", false);
            this.mesh.chunkVisible("Z_NdAltSmall", false);
            this.mesh.chunkVisible("Z_NdHP1", false);
            this.mesh.materialReplace("Instr4", "Instr4_d");
            this.mesh.materialReplace("Instr4_night", "Instr4_d_night");
            this.mesh.chunkVisible("Z_NdApres", false);
            this.mesh.chunkVisible("Z_NdHP2", false);
            this.mesh.chunkVisible("Z_NdMHour", false);
            this.mesh.chunkVisible("Z_NdMMin", false);
            this.mesh.chunkVisible("Z_NdMSec", false);
            this.mesh.chunkVisible("Z_NdMHour1", false);
            this.mesh.chunkVisible("Z_NdMMin1", false);
            this.mesh.chunkVisible("Z_NdLGr", false);
            this.mesh.chunkVisible("Z_NdRGr", false);
            this.mesh.chunkVisible("Z_NdFlap", false);
            this.mesh.chunkVisible("Z_NdPitch", false);
            this.mesh.materialReplace("Instr5", "Instr5_d");
            this.mesh.materialReplace("Instr5_night", "Instr5_d_night");
            this.mesh.chunkVisible("Z_NdCA", false);
            this.mesh.chunkVisible("Z_NdWatrT", false);
            this.mesh.chunkVisible("Z_NdAHB", false);
            this.mesh.chunkVisible("Z_NdAHR", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
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
    private boolean            bNeedSetUp;
    private float              starterPressure;
    private float              mainHydrPressure;
    private float              missionStartTime;
    private float              gears;
    private float              oldEmPressure;
    private static final float speedometerScale[] = { 0.0F, -12F, -40F, -71F, -101F, -116.75F, -132.5F, -148.25F, -164F, -196.5F, -228.5F, -255.5F, -282.5F, -304F, -326F };
    private static final float fuelScale[]        = { 0.0F, -128F, -217F, -313F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

    static {
        Property.set(CockpitIK3.class, "normZNs", new float[] { 0.5F, 0.5F, 1.3F, 0.5F });
    }

}
