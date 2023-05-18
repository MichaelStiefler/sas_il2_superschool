package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitP_35 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitP_35.this.fm != null) {
                CockpitP_35.this.setTmp = CockpitP_35.this.setOld;
                CockpitP_35.this.setOld = CockpitP_35.this.setNew;
                CockpitP_35.this.setNew = CockpitP_35.this.setTmp;
                CockpitP_35.this.setNew.throttle = (10F * CockpitP_35.this.setOld.throttle + CockpitP_35.this.fm.CT.PowerControl) / 11F;
                CockpitP_35.this.setNew.prop = (8F * CockpitP_35.this.setOld.prop + CockpitP_35.this.fm.CT.getStepControl()) / 9F;
                CockpitP_35.this.setNew.altimeter = CockpitP_35.this.fm.getAltitude();
                CockpitP_35.this.setNew.azimuth.setDeg(CockpitP_35.this.setOld.azimuth.getDeg(1.0F), CockpitP_35.this.fm.Or.azimut() - 90F);
                CockpitP_35.this.setNew.waypointAzimuth = (10F * CockpitP_35.this.setOld.waypointAzimuth + (CockpitP_35.this.waypointAzimuth() - CockpitP_35.this.setOld.azimuth.getDeg(1.0F)) + World.cur().rnd.nextFloat(-30F, 30F)) / 11F;
                CockpitP_35.this.setNew.vspeed = (199F * CockpitP_35.this.setOld.vspeed + CockpitP_35.this.fm.getVertSpeed()) / 200F;
                float f = 30F;
                if (CockpitP_35.this.flapsLever != 0.0F && CockpitP_35.this.flaps == CockpitP_35.this.fm.CT.getFlap()) {
                    CockpitP_35.this.flapsLever = CockpitP_35.this.flapsLever * 0.8F;
                    if (Math.abs(CockpitP_35.this.flapsLever) < 0.1F) CockpitP_35.this.flapsLever = 0.0F;
                } else if (CockpitP_35.this.flaps < CockpitP_35.this.fm.CT.getFlap()) {
                    CockpitP_35.this.flaps = CockpitP_35.this.fm.CT.getFlap();
                    CockpitP_35.this.flapsLever = CockpitP_35.this.flapsLever + 2.0F;
                    if (CockpitP_35.this.flapsLever > f) CockpitP_35.this.flapsLever = f;
                } else if (CockpitP_35.this.flaps > CockpitP_35.this.fm.CT.getFlap()) {
                    CockpitP_35.this.flaps = CockpitP_35.this.fm.CT.getFlap();
                    CockpitP_35.this.flapsLever = CockpitP_35.this.flapsLever - 2.0F;
                    if (CockpitP_35.this.flapsLever < -f) CockpitP_35.this.flapsLever = -f;
                }
                f = 14.5F;
                if (CockpitP_35.this.gearsLever != 0.0F && CockpitP_35.this.gears == CockpitP_35.this.fm.CT.getGear()) {
                    CockpitP_35.this.gearsLever = CockpitP_35.this.gearsLever * 0.8F;
                    if (Math.abs(CockpitP_35.this.gearsLever) < 0.1F) CockpitP_35.this.gearsLever = 0.0F;
                } else if (CockpitP_35.this.gears < CockpitP_35.this.fm.CT.getGear()) {
                    CockpitP_35.this.gears = CockpitP_35.this.fm.CT.getGear();
                    CockpitP_35.this.gearsLever = CockpitP_35.this.gearsLever + 2.0F;
                    if (CockpitP_35.this.gearsLever > f) CockpitP_35.this.gearsLever = f;
                } else if (CockpitP_35.this.gears > CockpitP_35.this.fm.CT.getGear()) {
                    CockpitP_35.this.gears = CockpitP_35.this.fm.CT.getGear();
                    CockpitP_35.this.gearsLever = CockpitP_35.this.gearsLever - 2.0F;
                    if (CockpitP_35.this.gearsLever < -f) CockpitP_35.this.gearsLever = -f;
                }
                CockpitP_35.this.fuelPerct = CockpitP_35.this.fm.M.fuel / CockpitP_35.this.fm.M.maxFuel * 100F;
                byte byte0;
                float f1;
                if (CockpitP_35.this.fuelPerct > CockpitP_35.fuelTankLevels[0]) {
                    if (CockpitP_35.this.fuelPerct > CockpitP_35.fuelTankLevels[1]) byte0 = 2;
                    else byte0 = 1;
                    f1 = CockpitP_35.this.fuelPerct - CockpitP_35.fuelTankLevels[byte0 - 1];
                } else {
                    byte0 = 0;
                    f1 = CockpitP_35.this.fuelPerct;
                }
                float f2 = 17F;
                if (byte0 != 0) {
                    if (f1 < 7F) f2 = f1 + 10F;
                } else if (f1 < 17F) f2 = f1;
                if (CockpitP_35.this.fuelSwitchAngle > CockpitP_35.fuelSwitchAngles[byte0]) {
                    CockpitP_35.this.fuelSwitchAngle -= 4;
                    f2 = 0.0F;
                }
                if (CockpitP_35.this.fuelSwitchAngle < CockpitP_35.fuelSwitchAngles[byte0]) {
                    CockpitP_35.this.fuelSwitchAngle += 4;
                    f2 = 0.0F;
                }
                CockpitP_35.this.setNew.fuelPSI = (15F * CockpitP_35.this.setOld.fuelPSI + f2) / 16F;
            }
            if (CockpitP_35.this.gearsLever != 0.0F || CockpitP_35.this.flapsLever != 0.0F) {
                CockpitP_35.this.amps[1] = 60F;
                CockpitP_35.this.amps[2] = 0.6F;
            } else {
                if (CockpitP_35.this.amps[0] < 12F) CockpitP_35.this.amps[2] = 0.02F;
                if (CockpitP_35.this.fm.EI.engines[0].getStage() != 0) CockpitP_35.this.amps[1] = 10F;
                else CockpitP_35.this.amps[1] = 0.0F;
            }
            if (CockpitP_35.this.amps[0] != CockpitP_35.this.amps[1]) {
                if (CockpitP_35.this.amps[0] > CockpitP_35.this.amps[1]) {
                    CockpitP_35.this.amps[0] -= CockpitP_35.this.amps[2];
                    if (CockpitP_35.this.amps[0] < CockpitP_35.this.amps[1]) CockpitP_35.this.amps[0] = CockpitP_35.this.amps[1];
                } else {
                    CockpitP_35.this.amps[0] += CockpitP_35.this.amps[2];
                    if (CockpitP_35.this.amps[0] > CockpitP_35.this.amps[1]) CockpitP_35.this.amps[0] = CockpitP_35.this.amps[1];
                }
                if ((CockpitP_35.this.fm.AS.astateCockpitState & 2) != 0) CockpitP_35.this.setNew.reviPos = (9F * CockpitP_35.this.setOld.reviPos + 1.0F) / 10F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      prop;
        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;
        float      waypointAzimuth;
        float      fuelPSI;
        float      reviPos;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

        Variables(Variables variables) {
            this();
        }
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) return 0.0F;
        else {
            waypoint.getP(this.tmpP);
            this.tmpV.sub(this.tmpP, this.fm.Loc);
            return (float) (57.295779513082323D * Math.atan2(-this.tmpV.y, this.tmpV.x));
        }
    }

    public CockpitP_35() {
        super("3DO/Cockpit/Hawk-75A3/hier.him", "bf109");
        this.setOld = new Variables(null);
        this.setNew = new Variables(null);
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.reversedThrottle = false;
        this.leftGun = null;
        this.rightGun = null;
        this.flapsLever = 0.0F;
        this.flaps = 0.0F;
        this.gearsLever = 0.0F;
        this.gears = 0.0F;
        this.fuelSwitchAngle = 0;
        this.fuelPerct = 100F;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = new String[] { "Arrows_Segment", "Indicators_2", "Indicators_2_Dam", "Indicators_3", "Indicators_3_Dam", "Indicators_4", "Indicators_4_Dam", "Indicators_5", "Indicators_5_Dam", "Indicators_6", "Indicators_7", "Plastics" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.45F);
        Aircraft aircraft = this.aircraft();
        try {
            if (!(aircraft.getGunByHookName("_MGUN01") instanceof GunEmpty)) {
                this.leftGun = aircraft.getGunByHookName("_MGUN02");
                this.rightGun = aircraft.getGunByHookName("_MGUN01");
            }
        } catch (Exception exception) {}
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.9F, 0.0F, 0.7F);
        this.mesh.chunkSetLocate("Z_canopy", Cockpit.xyz, Cockpit.ypr);
        if (this.fm.CT.getCockpitDoor() > 0.5D) this.mesh.chunkSetAngles("Z_canopy_lever", 0.0F, -this.cvt(this.fm.CT.getCockpitDoor(), 0.9F, 1.0F, 7F, 0.0F), 0.0F);
        else this.mesh.chunkSetAngles("Z_canopy_lever", 0.0F, -this.cvt(this.fm.CT.getCockpitDoor(), 0.0F, 0.1F, 0.0F, 7F), 0.0F);
        this.mesh.chunkSetAngles("Z_canopy_open_rod", Cockpit.xyz[1] * 2570F, 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Zairspeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 20F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Zturn_and_bank", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 28F, -28F), 0.0F);
        this.mesh.chunkSetAngles("Zturn_and_bank2", 0.0F, this.cvt(this.getBall(7D), -7F, 7F, -13F, 13F), 0.0F);
        this.mesh.chunkSetAngles("Z_Ball", 0.0F, this.cvt(this.getBall(7D), -7F, 7F, -12F, 12F), 0.0F);
        this.mesh.chunkSetAngles("Zclimb", 0.0F, this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Zclock_hour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Zclock_minute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Zaltimeter_01", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Zaltimeter_02", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 7200F), 0.0F);
        this.mesh.chunkSetAngles("ZTempOil", 0.0F, this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 100F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("ZTempCyl", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 400F, 0.0F, 62F), 0.0F);
        this.mesh.chunkSetAngles("Zmanifold_pressure", 0.0F, this.cvt(this.pictManifold = 0.85F * this.pictManifold + 0.15F * this.fm.EI.engines[0].getManifoldPressure() * 76F, 30F, 120F, 38F, 322.4F), 0.0F);
        float f1 = this.fm.EI.engines[0].getRPM();
        if (f1 > 600F) this.mesh.chunkSetAngles("Zrpm", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 600F, 3000F, 16.5F, 343.2F), 0.0F);
        else this.mesh.chunkSetAngles("Zrpm", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 600F, 0.0F, 16.5F), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.interp(this.setNew.prop, this.setOld.prop, f);
        this.mesh.chunkSetAngles("Z_Prop_Pitch", 0.0F, 65F * Cockpit.xyz[1], 0.0F);
        Cockpit.xyz[1] = Cockpit.xyz[1] * 0.02F - 0.02F;
        this.mesh.chunkSetLocate("Z_Prop_Pitch_strut", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.fm.EI.engines[0].getControlMix(), 0.0F, 1.2F, 0.0F, 1.0F);
        this.mesh.chunkSetAngles("Z_Mixture", 0.0F, 68F * Cockpit.xyz[1], 0.0F);
        Cockpit.xyz[1] = Cockpit.xyz[1] * 0.02F - 0.02F;
        this.mesh.chunkSetLocate("Z_Mixture_strut", Cockpit.xyz, Cockpit.ypr);
        if (this.reversedThrottle) Cockpit.xyz[1] = 1.0F - this.interp(this.setNew.throttle, this.setOld.throttle, f);
        else Cockpit.xyz[1] = this.interp(this.setNew.throttle, this.setOld.throttle, f);
        this.mesh.chunkSetAngles("Z_Throttle", 0.0F, this.cvt(Cockpit.xyz[1], 0.0F, 1.1F, -3F, 60F), 0.0F);
        Cockpit.xyz[1] = Cockpit.xyz[1] * 0.02F - 0.021F;
        this.mesh.chunkSetLocate("Z_Throttle_strut", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Flaps_lever", 0.0F, this.flapsLever, 0.0F);
        this.mesh.chunkSetAngles("Z_gear_lever", 0.0F, this.gearsLever + 14.5F, 0.0F);
        this.mesh.chunkSetAngles("Z_Rudder_Trim", 0.0F, 722F * this.fm.CT.getTrimRudderControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Elevator_Trim", 0.0F, -722F * this.fm.CT.getTrimElevatorControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Aileron_Rod", 0.0F, 0.0F, (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 20F);
        this.mesh.chunkSetAngles("Z_Control_Column", 0.0F, -(this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 16F, 0.0F);
        this.mesh.chunkSetAngles("Z_Elevator_Rod", 0.0F, this.pictElev * 16F, 0.0F);
        this.mesh.chunkSetAngles("Z_CowlFlaps", 0.0F, 720F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_Frame_L", 0.0F, 12F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_Frame_R", 0.0F, -12F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_Rudderwire_L", 0.0F, 0.0F, 12F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_Rudderwire_R", 0.0F, 0.0F, -12F * this.fm.CT.getRudder());
        if (this.leftGun != null) {
            int i = this.leftGun.countBullets();
            int k = (int) (i * 0.01F);
            int i1 = (int) (i * 0.1F) - 10 * k;
            int k1 = i - (k * 100 + i1 * 10);
            this.bullets[0] = 0.8F * this.bullets[0] + 0.2F * k;
            this.bullets[1] = 0.8F * this.bullets[1] + 0.2F * i1;
            this.bullets[2] = 0.8F * this.bullets[2] + 0.2F * k1;
            this.mesh.chunkSetAngles("Z_Lammo00", 0.0F, -this.cvt(this.bullets[0], 0.0F, 9F, 0.0F, 324F), 0.0F);
            this.mesh.chunkSetAngles("Z_Lammo01", 0.0F, -this.cvt(this.bullets[1], 0.0F, 9F, 0.0F, 324F), 0.0F);
            this.mesh.chunkSetAngles("Z_Lammo02", 0.0F, -this.cvt(this.bullets[2], 0.0F, 9F, 0.0F, 324F), 0.0F);
        }
        if (this.rightGun != null) {
            int j = this.rightGun.countBullets();
            int l = (int) (j * 0.01F);
            int j1 = (int) (j * 0.1F) - 10 * l;
            int l1 = j - (l * 100 + j1 * 10);
            this.bullets[3] = 0.8F * this.bullets[3] + 0.2F * l;
            this.bullets[4] = 0.8F * this.bullets[4] + 0.2F * j1;
            this.bullets[5] = 0.8F * this.bullets[5] + 0.2F * l1;
            this.mesh.chunkSetAngles("Z_Rammo00", 0.0F, -this.cvt(this.bullets[3], 0.0F, 9F, 0.0F, 324F), 0.0F);
            this.mesh.chunkSetAngles("Z_Rammo01", 0.0F, -this.cvt(this.bullets[4], 0.0F, 9F, 0.0F, 324F), 0.0F);
            this.mesh.chunkSetAngles("Z_Rammo02", 0.0F, -this.cvt(this.bullets[5], 0.0F, 9F, 0.0F, 324F), 0.0F);
        }
        if (this.fm.Gears.lgear) this.mesh.chunkSetAngles("ZGearL", 0.0F, this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 76F), 0.0F);
        if (this.fm.Gears.rgear) this.mesh.chunkSetAngles("ZGearR", 0.0F, this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, -76F), 0.0F);
        if (this.fm.Gears.cgear) this.mesh.chunkSetAngles("ZGearC", 0.0F, this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, 90F), 0.0F);
        this.mesh.chunkSetAngles("Zflaps", 0.0F, this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, -80F), 0.0F);
        this.mesh.chunkSetAngles("Zcompass", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Zcompass02", 0.0F, -this.setNew.azimuth.getDeg(f) - 8F, 0.0F);
        this.mesh.chunkSetAngles("Zflaps", 0.0F, -76.5F * this.fm.CT.getFlap(), 0.0F);
        if (this.fuelPerct > fuelTankLevels[0]) {
            this.mesh.chunkSetAngles("Zfuel", 0.0F, 295.5F, 0.0F);
            this.mesh.chunkSetAngles("ZfuelWing", 0.0F, this.cvt(this.fuelPerct, fuelTankLevels[0], 100F, 13.5F, 285.5F), 0.0F);
        } else {
            this.mesh.chunkSetAngles("Zfuel", 0.0F, this.cvt(this.fuelPerct, 0.0F, fuelTankLevels[0], -3.5F, 295.5F), 0.0F);
            this.mesh.chunkSetAngles("ZfuelWing", 0.0F, 13.5F, 0.0F);
        }
        if (this.setNew.fuelPSI < 12F) this.mesh.chunkVisible("XFlareRed_01", true);
        else this.mesh.chunkVisible("XFlareRed_01", false);
        this.mesh.chunkSetAngles("Z_Fuel_Selector", 0.0F, this.fuelSwitchAngle, 0.0F);
        this.mesh.chunkSetAngles("ZFuelPres", 0.0F, this.cvt(this.setNew.fuelPSI, 0.0F, 25F, 0.0F, -180F), 0.0F);
        this.mesh.chunkSetAngles("Zfuel_press", 0.0F, this.cvt(this.setNew.fuelPSI, 0.0F, 30F, 0.0F, 293.5F), 0.0F);
        this.mesh.chunkSetAngles("ZoilPres", 0.0F, this.cvt((60F + this.fm.EI.engines[0].tOilIn * 0.222F) * this.fm.EI.engines[0].getReadyness(), 0.0F, 200F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Zoxy", 0.0F, 173F, 0.0F);
        this.mesh.chunkSetAngles("Z_Amp01", 0.0F, 0.0F, this.cvt(this.amps[0], 0.0F, 150F, -9.5F, -108.5F));
        this.mesh.chunkSetAngles("Z_cockpit_lights", 0.0F, this.cockpitLightControl ? -20F : 20F, 0.0F);
        this.mesh.chunkSetAngles("Z_Ignition_switch", 0.0F, this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F);
        this.mesh.chunkSetAngles("Z_nav_lights", 0.0F, this.fm.AS.bNavLightsOn ? -20F : 20F, 0.0F);
        this.mesh.chunkSetAngles("Z_landing_lights", 0.0F, this.fm.AS.bLandingLightOn ? -20F : 20F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_L", 0.0F, 25F * this.fm.CT.BrakeControl, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal_R", 0.0F, 25F * this.fm.CT.BrakeControl, 0.0F);
        this.mesh.chunkSetAngles("Z_REVI3_IRON", 0.0F, this.cvt(this.interp(this.setNew.reviPos, this.setOld.reviPos, f), 0.0F, 1.0F, 0.0F, 90F), 0.0F);
        this.mesh.chunkSetAngles("ZCarbAir", 0.0F, this.floatindex(this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -40F, 50F, 0.0F, 4F), airTempScale), 0.0F);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        if (this.aircraft().getRegiment() != null) {
            if (!this.aircraft().getRegiment().country().equals("fi")) {
                this.mesh.chunkVisible("Finnish_writings01", false);
                this.mesh.chunkVisible("Finnish_writings02", false);
                this.mesh.chunkVisible("Finnish_writings03", false);
            }
            if (this.aircraft().getRegiment().country().equals("fr")) this.reversedThrottle = true;
        }
        if (((NetAircraft) this.aircraft()).thisWeaponsName.startsWith("P35A")) {
            this.mesh.chunkVisible("2xcal303", false);
            this.mesh.chunkVisible("1xcal50cal303", false);
            this.mesh.chunkVisible("2xcal50", true);
        } else {
            this.mesh.chunkVisible("2xcal303", false);
            this.mesh.chunkVisible("1xcal50cal303", true);
            this.mesh.chunkVisible("2xcal50", false);
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("X_GlassDamage5", true);
            this.mesh.chunkVisible("reticle", false);
            this.mesh.chunkVisible("reticlemask", false);
            this.mesh.chunkVisible("REVI3_RAMKA", false);
            this.mesh.chunkVisible("REVI3_STEKLO", false);
            this.mesh.chunkVisible("REVI3_RAMKA_damage", true);
            this.mesh.chunkVisible("REVI3_STEKLO_damage", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("X_GlassDamage4", true);
            this.mesh.chunkVisible("X_GlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Gauges01", false);
            this.mesh.chunkVisible("D_Gauges01", true);
            this.mesh.chunkVisible("X_HullDamage1", true);
            this.mesh.chunkVisible("X_HullDamage4", true);
            this.mesh.chunkVisible("Zairspeed", false);
            this.mesh.chunkVisible("Zmanifold_pressure", false);
            this.mesh.chunkVisible("Zclimb", false);
            this.mesh.chunkVisible("ZCarbAir", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("Gauges02", false);
            this.mesh.chunkVisible("D_Gauges02", true);
            this.mesh.chunkVisible("X_HullDamage2", true);
            this.mesh.chunkVisible("X_HullDamage4", true);
            this.mesh.chunkVisible("Zturn_and_bank", false);
            this.mesh.chunkVisible("Zturn_and_bank2", false);
            this.mesh.chunkVisible("Zaltimeter_01", false);
            this.mesh.chunkVisible("Zaltimeter_02", false);
            this.mesh.chunkVisible("ZTempOil", false);
            this.mesh.chunkVisible("ZoilPres", false);
            this.mesh.chunkVisible("ZFuelPres", false);
            this.mesh.chunkVisible("Zfuel", false);
            this.mesh.chunkVisible("Zfuel_press", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("X_GlassDamage3", true);
            this.mesh.chunkVisible("X_HullDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) this.mesh.chunkVisible("ZOil", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("X_GlassDamage2", true);
            this.mesh.chunkVisible("X_HullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("Gauges03", false);
            this.mesh.chunkVisible("D_Gauges03", true);
            this.mesh.chunkVisible("Zrpm", false);
            this.mesh.chunkVisible("ZTempCyl", false);
            this.mesh.chunkVisible("Z_Ball", false);
            this.mesh.chunkVisible("Zclock_hour", false);
            this.mesh.chunkVisible("Zclock_minute", false);
            this.mesh.chunkVisible("Zcompass02", false);
            this.mesh.chunkVisible("X_GlassDamage1", true);
            this.mesh.chunkVisible("X_HullDamage3", true);
            this.mesh.chunkVisible("X_HullDamage4", true);
        }
        this.retoggleLight();
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
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
    private float              pictManifold;
    private boolean            bNeedSetUp;
    private boolean            reversedThrottle;
    private Gun                leftGun;
    private Gun                rightGun;
    private float              flapsLever;
    private float              flaps;
    private float              gearsLever;
    private float              gears;
    private float              bullets[]          = { 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F };
    private float              amps[]             = { 0.0F, 0.0F, 0.0F };
    private static final int   fuelSwitchAngles[] = { 0, -72, -152 };
    private static final int   fuelTankLevels[]   = { 35, 75, 100 };
    private int                fuelSwitchAngle;
    private float              fuelPerct;
    private static final float speedometerScale[] = { 0.0F, 7F, 14.2F, 32F, 57.16F, 87.45F, 120.9F, 158.3F, 193F, 230.4F, 267.2F, 303.7F, 340F, 377F, 5F, 412.7F, 448.7F, 484.2F, 518F, 553.6F, 587F, 622F };
    private static final float airTempScale[]     = { -2.5F, 42F, 135F, 224F, 298.5F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

    static {
        Property.set(CockpitP_35.class, "normZN", 1.06F);
    }

}
