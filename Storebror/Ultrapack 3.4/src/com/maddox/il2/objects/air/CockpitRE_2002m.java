package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitRE_2002m extends CockpitPilot {
    private class Variables {

        float    altimeter;
        float    throttle;
        float    mix;
        float    prop;
        float    turn;
        float    vspeed;
        float    stbyPosition;
        Point3d  planeLoc;
        Point3d  planeMove;
        Vector3d compassPoint[];
        Vector3d cP[];

        private Variables() {
            this.planeLoc = new Point3d();
            this.planeMove = new Point3d();
            this.compassPoint = new Vector3d[4];
            this.cP = new Vector3d[4];
            this.compassPoint[0] = new Vector3d(0.0D, Math.sqrt(1.0D - (CockpitRE_2002m.compassZ * CockpitRE_2002m.compassZ)), CockpitRE_2002m.compassZ);
            this.compassPoint[1] = new Vector3d(-Math.sqrt(1.0D - (CockpitRE_2002m.compassZ * CockpitRE_2002m.compassZ)), 0.0D, CockpitRE_2002m.compassZ);
            this.compassPoint[2] = new Vector3d(0.0D, -Math.sqrt(1.0D - (CockpitRE_2002m.compassZ * CockpitRE_2002m.compassZ)), CockpitRE_2002m.compassZ);
            this.compassPoint[3] = new Vector3d(Math.sqrt(1.0D - (CockpitRE_2002m.compassZ * CockpitRE_2002m.compassZ)), 0.0D, CockpitRE_2002m.compassZ);
            this.cP[0] = new Vector3d(0.0D, Math.sqrt(1.0D - (CockpitRE_2002m.compassZ * CockpitRE_2002m.compassZ)), CockpitRE_2002m.compassZ);
            this.cP[1] = new Vector3d(-Math.sqrt(1.0D - (CockpitRE_2002m.compassZ * CockpitRE_2002m.compassZ)), 0.0D, CockpitRE_2002m.compassZ);
            this.cP[2] = new Vector3d(0.0D, -Math.sqrt(1.0D - (CockpitRE_2002m.compassZ * CockpitRE_2002m.compassZ)), CockpitRE_2002m.compassZ);
            this.cP[3] = new Vector3d(Math.sqrt(1.0D - (CockpitRE_2002m.compassZ * CockpitRE_2002m.compassZ)), 0.0D, CockpitRE_2002m.compassZ);
        }
    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitRE_2002m.this.bNeedSetUp) {
                CockpitRE_2002m.this.reflectPlaneMats();
                CockpitRE_2002m.this.bNeedSetUp = false;
            }
            CockpitRE_2002m.this.setTmp = CockpitRE_2002m.this.setOld;
            CockpitRE_2002m.this.setOld = CockpitRE_2002m.this.setNew;
            CockpitRE_2002m.this.setNew = CockpitRE_2002m.this.setTmp;
            if (((CockpitRE_2002m.this.fm.AS.astateCockpitState & 2) != 0) && (CockpitRE_2002m.this.setNew.stbyPosition < 1.0F)) {
                CockpitRE_2002m.this.delay--;
                if (CockpitRE_2002m.this.delay <= 0) {
                    CockpitRE_2002m.this.setNew.stbyPosition = CockpitRE_2002m.this.setOld.stbyPosition + 0.03F;
                    CockpitRE_2002m.this.setOld.stbyPosition = CockpitRE_2002m.this.setNew.stbyPosition;
                    CockpitRE_2002m.this.sightDamaged = true;
                }
            }
            if (CockpitRE_2002m.this.fm.CT.getRadiator() < CockpitRE_2002m.this.currentRadiator) {
                CockpitRE_2002m.this.radiatorDelta = -1;
            } else if (CockpitRE_2002m.this.fm.CT.getRadiator() > CockpitRE_2002m.this.currentRadiator) {
                CockpitRE_2002m.this.radiatorDelta = 1;
            }
            if ((CockpitRE_2002m.this.radiator > 1.0F) || (CockpitRE_2002m.this.radiator < -1F)) {
                CockpitRE_2002m.this.radiatorDelta = 0;
            }
            if (CockpitRE_2002m.this.radiatorDelta != 0) {
                CockpitRE_2002m.this.radiator += CockpitRE_2002m.this.radiatorDelta * 0.2F;
            } else if (CockpitRE_2002m.this.currentRadiator == CockpitRE_2002m.this.fm.CT.getRadiator()) {
                CockpitRE_2002m.this.radiator *= 0.5F;
            }
            CockpitRE_2002m.this.currentRadiator = CockpitRE_2002m.this.fm.CT.getRadiator();
            CockpitRE_2002m.this.setNew.altimeter = CockpitRE_2002m.this.fm.getAltitude();
            CockpitRE_2002m.this.setNew.throttle = ((10F * CockpitRE_2002m.this.setOld.throttle) + CockpitRE_2002m.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitRE_2002m.this.setNew.mix = ((10F * CockpitRE_2002m.this.setOld.mix) + CockpitRE_2002m.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitRE_2002m.this.setNew.prop = ((10F * CockpitRE_2002m.this.setOld.prop) + CockpitRE_2002m.this.fm.EI.engines[0].getControlProp()) / 11F;
            CockpitRE_2002m.this.pictGear = (0.85F * CockpitRE_2002m.this.pictGear) + (0.15F * CockpitRE_2002m.this.fm.CT.GearControl);
            CockpitRE_2002m.this.w.set(CockpitRE_2002m.this.fm.getW());
            CockpitRE_2002m.this.fm.Or.transform(CockpitRE_2002m.this.w);
            CockpitRE_2002m.this.setNew.turn = ((12F * CockpitRE_2002m.this.setOld.turn) + CockpitRE_2002m.this.w.z) / 13F;
            CockpitRE_2002m.this.setNew.vspeed = ((299F * CockpitRE_2002m.this.setOld.vspeed) + CockpitRE_2002m.this.fm.getVertSpeed()) / 300F;
            if (CockpitRE_2002m.this.flaps == CockpitRE_2002m.this.fm.CT.getFlap()) {
                CockpitRE_2002m.this.flapsDirection = 0;
                CockpitRE_2002m.this.sfxStop(16);
            } else if (CockpitRE_2002m.this.flaps < CockpitRE_2002m.this.fm.CT.getFlap()) {
                if (CockpitRE_2002m.this.flapsDirection == 0) {
                    CockpitRE_2002m.this.sfxStart(16);
                }
                CockpitRE_2002m.this.flaps = CockpitRE_2002m.this.fm.CT.getFlap();
                CockpitRE_2002m.this.flapsDirection = 1;
                CockpitRE_2002m.this.flapsLeverAngle += CockpitRE_2002m.this.flapsIncrement;
            } else if (CockpitRE_2002m.this.flaps > CockpitRE_2002m.this.fm.CT.getFlap()) {
                if (CockpitRE_2002m.this.flapsDirection == 0) {
                    CockpitRE_2002m.this.sfxStart(16);
                }
                CockpitRE_2002m.this.flaps = CockpitRE_2002m.this.fm.CT.getFlap();
                CockpitRE_2002m.this.flapsDirection = -1;
                CockpitRE_2002m.this.flapsLeverAngle -= CockpitRE_2002m.this.flapsIncrement;
            }
            if (!CockpitRE_2002m.this.fm.Gears.bTailwheelLocked && (CockpitRE_2002m.this.tailWheelLock < 1.0F)) {
                CockpitRE_2002m.this.tailWheelLock += 0.1F;
            } else if (CockpitRE_2002m.this.fm.Gears.bTailwheelLocked && (CockpitRE_2002m.this.tailWheelLock > 0.0F)) {
                CockpitRE_2002m.this.tailWheelLock -= 0.1F;
            }
            CockpitRE_2002m.this.updateCompass();
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitRE_2002m() {
        super("3DO/Cockpit/Re-2002m/RE2002m.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.flaps = 0.0F;
        this.pictGear = 0.0F;
        this.tailWheelLock = 1.0F;
        this.flapsDirection = 0;
        this.flapsLeverAngle = 0.0F;
        this.flapsIncrement = 10F;
        this.rpmGeneratedPressure = 0.0F;
        this.oilPressure = 0.0F;
        this.radiator = 0.0F;
        this.currentRadiator = 0.0F;
        this.radiatorDelta = 0;
        this.sightDamaged = false;
        this.compassFirst = 0;
        this.delay = 80;
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK01");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(126F, 232F, 245F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK01", this.light1);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK02");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(126F, 232F, 245F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK02", this.light2);
        this.cockpitNightMats = (new String[] { "D_strum1", "D_strum2", "D_strum3", "D_strum4", "equip01", "equip04", "equip05", "gunsight", "panel1", "stick", "strum1", "strum2", "strum3", "strum4" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void reflectWorldToInstruments(float f) {
        float f1 = 0.0F;
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        f1 = this.pictManifold = (0.85F * this.pictManifold) + (0.15F * this.fm.EI.engines[0].getManifoldPressure() * 76F);
        if (f1 < 76F) {
            this.mesh.chunkSetAngles("Z_need_airpress", -this.cvt(f1, 40F, 76F, 12F, 210F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_need_airpress", -this.cvt(f1, 76F, 100F, 210F, 328F), 0.0F, 0.0F);
        }
        f1 = -15F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl));
        this.mesh.chunkSetAngles("Z_stick03", f1, 0.0F, 0.0F);
        f1 = -14F * (this.pictElev = (0.85F * this.pictElev) + (0.2F * this.fm.CT.ElevatorControl));
        this.mesh.chunkSetAngles("Z_stick01", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_stick02", 0.0F, 0.0F, f1);
        this.mesh.chunkSetAngles("Z_gunsight_rim", 50F * this.setNew.stbyPosition, 0.0F, 0.0F);
        f1 = this.fm.CT.getRudder();
        this.mesh.chunkSetAngles("Z_rudder_01", -15F * f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_rudder_02", 15F * f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_rudder_03", 15F * f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_rudder_04", -15F * f1, 0.0F, 0.0F);
        f1 = this.interp(this.setNew.throttle, this.setOld.throttle, f);
        this.mesh.chunkSetAngles("Z_throttle_lvr", this.cvt(f1, 0.0F, 1.0F, 0.0F, 50F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_wep_lvr", this.cvt(f1, 1.0F, 1.1F, 0.0F, 40F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_mix_lvr", 40F * this.interp(this.setNew.mix, this.setOld.mix, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_pitch_lvr", 45F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_alt_01", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, -3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_alt_02", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, -360F), 0.0F, 0.0F);
        float f2 = Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH());
        if (f2 < 100F) {
            f1 = this.cvt(f2, 0.0F, 100F, 0.0F, 41F);
        } else if (f2 < 200F) {
            f1 = this.cvt(f2, 100F, 200F, 41F, 110F);
        } else if (f2 < 300F) {
            f1 = this.cvt(f2, 200F, 300F, 110F, 144F);
        } else if (f2 < 400F) {
            f1 = this.cvt(f2, 300F, 400F, 144F, 212F);
        } else if (f2 < 500F) {
            f1 = this.cvt(f2, 400F, 500F, 212F, 292F);
        } else {
            f1 = this.cvt(f2, 500F, 550F, 292F, 328F);
        }
        this.mesh.chunkSetAngles("Z_need_speed_02", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_speed_01", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_clock01", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, -720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_clock02", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_oiltemp_01", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 30F, 150F, 0.0F, 12F), CockpitRE_2002m.oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_oiltemp_02", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilIn, 30F, 150F, 0.0F, 12F), CockpitRE_2002m.oilTempScale), 0.0F, 0.0F);
        f1 = this.fm.EI.engines[0].getRPM();
        this.mesh.chunkSetAngles("Z_need_RPM", this.cvt(f1, 0.0F, 4000F, 0.0F, -322F), 0.0F, 0.0F);
        if ((this.fm.Or.getKren() < -110F) || (this.fm.Or.getKren() > 110F)) {
            this.rpmGeneratedPressure -= 2.0F;
        } else if (f1 < this.rpmGeneratedPressure) {
            this.rpmGeneratedPressure -= (this.rpmGeneratedPressure - f1) * 0.01F;
        } else {
            this.rpmGeneratedPressure += (f1 - this.rpmGeneratedPressure) * 0.001F;
        }
        if (this.rpmGeneratedPressure < 800F) {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 0.0F, 800F, 0.0F, 4F);
        } else if (this.rpmGeneratedPressure < 1800F) {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 800F, 1800F, 4F, 5F);
        } else {
            this.oilPressure = this.cvt(this.rpmGeneratedPressure, 1800F, 2750F, 5F, 5.8F);
        }
        float f3 = 0.0F;
        if (this.fm.EI.engines[0].tOilOut > 90F) {
            f3 = this.cvt(this.fm.EI.engines[0].tOilOut, 90F, 110F, 1.1F, 1.5F);
        } else if (this.fm.EI.engines[0].tOilOut < 50F) {
            f3 = this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 50F, 2.0F, 0.9F);
        } else {
            f3 = this.cvt(this.fm.EI.engines[0].tOilOut, 50F, 90F, 0.9F, 1.1F);
        }
        float f4 = f3 * this.fm.EI.engines[0].getReadyness() * this.oilPressure;
        this.mesh.chunkSetAngles("Z_need_oilpress", this.cvt(f4, 0.0F, 15F, 0.0F, -300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_cyltemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, -77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_fuelpress", this.cvt(this.rpmGeneratedPressure, 0.0F, 1800F, 0.0F, -200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_magneto_switch", -38F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_turnbank01", this.cvt(this.setNew.turn, -0.2F, 0.2F, -20F, 20F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_turnbank02", this.cvt(this.getBall(8D), -8F, 8F, 7.5F, -7.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_need_climb", this.cvt(this.setNew.vspeed, -25F, 25F, 180F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuel_01", this.cvt(this.fm.M.fuel, this.fm.M.maxFuel * 0.28F, this.fm.M.maxFuel, 0.0F, 210F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuel_02", this.cvt(this.fm.M.fuel, 0.0F, this.fm.M.maxFuel * 0.28F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_flaps_indicator", 146F * this.fm.CT.getFlap(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_elev_trim", 360F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_elev_indicator", -180F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_rudder_trim", -180F * this.fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = -0.03F * this.tailWheelLock;
        this.mesh.chunkSetLocate("Z_tailwheel_lock", Cockpit.xyz, Cockpit.ypr);
        if (this.fm.CT.bHasBrakeControl) {
            float f5 = this.fm.CT.getBrake();
            this.mesh.chunkSetAngles("Z_need_brakes_airpress02", -this.cvt(f5 + (f5 * this.fm.CT.getRudder()), 0.0F, 1.5F, 0.0F, 135F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_need_brakes_airpress01", this.cvt(f5 - (f5 * this.fm.CT.getRudder()), 0.0F, 1.5F, 0.0F, 135F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_need_brakes_airpress03", -150F + (f5 * 20F), 0.0F, 0.0F);
            this.resetYPRmodifier();
            Cockpit.xyz[2] = -0.01F * f5;
            this.mesh.chunkSetLocate("Z_brakes", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.fm.EI.engines[0].getControlFeather() == 1) {
            this.mesh.chunkSetAngles("Z_pitch_vario", -66F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_gear_lvr", 78F * this.pictGear, 0.0F, 0.0F);
        if (this.fm.Gears.lgear) {
            this.mesh.chunkSetAngles("Z_gear01", 90F - (90F * this.fm.CT.getGear()), 0.0F, 0.0F);
        }
        if (this.fm.Gears.rgear) {
            this.mesh.chunkSetAngles("Z_gear02", -90F + (90F * this.fm.CT.getGear()), 0.0F, 0.0F);
        }
        this.mesh.chunkVisible("Z_gearlamp01", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_gearlamp02", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("Z_gearlamp03", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_gearlamp04", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.rgear);
        this.mesh.chunkSetAngles("Z_flaps", -this.flapsLeverAngle, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_radiatorflaps", this.radiator * -8F, 0.0F, 0.0F);
        this.resetYPRmodifier();
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("gunsight_lense", false);
            this.mesh.chunkVisible("D_gunsight_lense", true);
            this.mesh.chunkVisible("reticle", false);
            this.mesh.chunkVisible("reticlemask", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassHoles_04", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Gauges01", false);
            this.mesh.chunkVisible("D_Gauges01", true);
            this.mesh.chunkVisible("Z_need_clock01", false);
            this.mesh.chunkVisible("Z_need_clock02", false);
            this.mesh.chunkVisible("Z_need_alt_01", false);
            this.mesh.chunkVisible("Z_need_alt_02", false);
            this.mesh.chunkVisible("Z_need_climb", false);
            this.mesh.chunkVisible("Z_need_speed_01", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassHoles_05", true);
            this.mesh.chunkVisible("XGlassHoles_03", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XHoles_01", true);
            this.mesh.chunkVisible("Gauges02", false);
            this.mesh.chunkVisible("D_Gauges02", true);
            this.mesh.chunkVisible("Z_need_speed_02", false);
            this.mesh.chunkVisible("Z_need_brakes_airpress03", false);
            this.mesh.chunkVisible("Z_need_brakes_airpress01", false);
            this.mesh.chunkVisible("Z_need_brakes_airpress02", false);
            this.mesh.chunkVisible("Z_need_airpress", false);
            this.mesh.chunkVisible("Z_need_fuelpress", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XHoles_02", true);
            this.mesh.chunkVisible("Gauges03", false);
            this.mesh.chunkVisible("D_Gauges03", true);
            this.mesh.chunkVisible("Z_need_oiltemp_02", false);
            this.mesh.chunkVisible("Z_need_oiltemp_01", false);
            this.mesh.chunkVisible("Z_need_oilpress", false);
            this.mesh.chunkVisible("Z_need_RPM", false);
            this.mesh.chunkVisible("Z_need_turnbank01", false);
            this.mesh.chunkVisible("Z_need_turnbank02", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassHoles_01", true);
            this.mesh.chunkVisible("XGlassHoles_02", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("ZOil", true);
        }
    }

    protected void reflectPlaneToModel() {
    }

    public void doToggleAim(boolean flag) {
        super.doToggleAim(flag);
        if (flag && this.sightDamaged) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(true);
            hookpilot.setAim(new Point3d(-0.5D, -0.01778D, 0.88D));
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.003F, 0.6F);
            this.light2.light.setEmit(0.003F, 0.6F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    private void initCompass() {
        this.accel = new Vector3d();
        this.compassSpeed = new Vector3d[4];
        this.compassSpeed[0] = new Vector3d(0.0D, 0.0D, 0.0D);
        this.compassSpeed[1] = new Vector3d(0.0D, 0.0D, 0.0D);
        this.compassSpeed[2] = new Vector3d(0.0D, 0.0D, 0.0D);
        this.compassSpeed[3] = new Vector3d(0.0D, 0.0D, 0.0D);
        float af[] = { 87F, 77.5F, 65.3F, 41.5F, -0.3F, -43.5F, -62.9F, -64F, -66.3F, -75.8F };
        float af1[] = { 55.8F, 51.5F, 47F, 40.1F, 33.8F, 33.7F, 32.7F, 35.1F, 46.6F, 61F };
        float f = this.cvt(Engine.land().config.declin, -90F, 90F, 9F, 0.0F);
        float f1 = this.floatindex(f, af);
        this.compassNorth = new Vector3d(0.0D, Math.cos(0.017452777777777779D * f1), -Math.sin(0.017452777777777779D * f1));
        this.compassSouth = new Vector3d(0.0D, -Math.cos(0.017452777777777779D * f1), Math.sin(0.017452777777777779D * f1));
        float f2 = this.floatindex(f, af1);
        this.compassNorth.scale((f2 / 600F) * Time.tickLenFs());
        this.compassSouth.scale((f2 / 600F) * Time.tickLenFs());
        this.segLen1 = 2D * Math.sqrt(1.0D - (CockpitRE_2002m.compassZ * CockpitRE_2002m.compassZ));
        this.segLen2 = this.segLen1 / Math.sqrt(2D);
        this.compassLimit = -1D * Math.sin(0.01745328888888889D * CockpitRE_2002m.compassLimitAngle);
        this.compassLimit *= this.compassLimit;
        this.compassAcc = 4.66666666D * Time.tickLenFs();
        this.compassSc = 0.101936799D / Time.tickLenFs() / Time.tickLenFs();
    }

    private void updateCompass() {
        if (this.compassFirst == 0) {
            this.initCompass();
            this.fm.getLoc(this.setOld.planeLoc);
        }
        this.fm.getLoc(this.setNew.planeLoc);
        this.setNew.planeMove.set(this.setNew.planeLoc);
        this.setNew.planeMove.sub(this.setOld.planeLoc);
        this.accel.set(this.setNew.planeMove);
        this.accel.sub(this.setOld.planeMove);
        this.accel.scale(this.compassSc);
        this.accel.x = -this.accel.x;
        this.accel.y = -this.accel.y;
        this.accel.z = -this.accel.z - 1.0D;
        this.accel.scale(this.compassAcc);
        if (this.accel.length() > (-CockpitRE_2002m.compassZ * 0.7D)) {
            this.accel.scale((-CockpitRE_2002m.compassZ * 0.7D) / this.accel.length());
        }
        for (int i = 0; i < 4; i++) {
            this.compassSpeed[i].set(this.setOld.compassPoint[i]);
            this.compassSpeed[i].sub(this.setNew.compassPoint[i]);
        }

        for (int j = 0; j < 4; j++) {
            double d = this.compassSpeed[j].length();
            d = 0.985D / (1.0D + (d * d * 15D));
            this.compassSpeed[j].scale(d);
        }

        Vector3d vector3d = new Vector3d();
        vector3d.set(this.setOld.compassPoint[0]);
        vector3d.add(this.setOld.compassPoint[1]);
        vector3d.add(this.setOld.compassPoint[2]);
        vector3d.add(this.setOld.compassPoint[3]);
        vector3d.normalize();
        for (int k = 0; k < 4; k++) {
            Vector3d vector3d2 = new Vector3d();
            double d1 = vector3d.dot(this.compassSpeed[k]);
            vector3d2.set(vector3d);
            d1 *= 0.28D;
            vector3d2.scale(-d1);
            this.compassSpeed[k].add(vector3d2);
        }

        for (int l = 0; l < 4; l++) {
            this.compassSpeed[l].add(this.accel);
        }

        this.compassSpeed[0].add(this.compassNorth);
        this.compassSpeed[2].add(this.compassSouth);
        for (int i1 = 0; i1 < 4; i1++) {
            this.setNew.compassPoint[i1].set(this.setOld.compassPoint[i1]);
            this.setNew.compassPoint[i1].add(this.compassSpeed[i1]);
        }

        vector3d.set(this.setNew.compassPoint[0]);
        vector3d.add(this.setNew.compassPoint[1]);
        vector3d.add(this.setNew.compassPoint[2]);
        vector3d.add(this.setNew.compassPoint[3]);
        vector3d.scale(0.25D);
        Vector3d vector3d1 = new Vector3d(vector3d);
        vector3d1.normalize();
        vector3d1.scale(-CockpitRE_2002m.compassZ);
        vector3d1.sub(vector3d);
        for (int j1 = 0; j1 < 4; j1++) {
            this.setNew.compassPoint[j1].add(vector3d1);
        }

        for (int k1 = 0; k1 < 4; k1++) {
            this.setNew.compassPoint[k1].normalize();
        }

        for (int l1 = 0; l1 < 2; l1++) {
            this.compassDist(this.setNew.compassPoint[0], this.setNew.compassPoint[2], this.segLen1);
            this.compassDist(this.setNew.compassPoint[1], this.setNew.compassPoint[3], this.segLen1);
            this.compassDist(this.setNew.compassPoint[0], this.setNew.compassPoint[1], this.segLen2);
            this.compassDist(this.setNew.compassPoint[2], this.setNew.compassPoint[3], this.segLen2);
            this.compassDist(this.setNew.compassPoint[1], this.setNew.compassPoint[2], this.segLen2);
            this.compassDist(this.setNew.compassPoint[3], this.setNew.compassPoint[0], this.segLen2);
            for (int i2 = 0; i2 < 4; i2++) {
                this.setNew.compassPoint[i2].normalize();
            }

            this.compassDist(this.setNew.compassPoint[3], this.setNew.compassPoint[0], this.segLen2);
            this.compassDist(this.setNew.compassPoint[1], this.setNew.compassPoint[2], this.segLen2);
            this.compassDist(this.setNew.compassPoint[2], this.setNew.compassPoint[3], this.segLen2);
            this.compassDist(this.setNew.compassPoint[0], this.setNew.compassPoint[1], this.segLen2);
            this.compassDist(this.setNew.compassPoint[1], this.setNew.compassPoint[3], this.segLen1);
            this.compassDist(this.setNew.compassPoint[0], this.setNew.compassPoint[2], this.segLen1);
            for (int j2 = 0; j2 < 4; j2++) {
                this.setNew.compassPoint[j2].normalize();
            }

        }

        Orientation orientation = new Orientation();
        this.fm.getOrient(orientation);
        for (int k2 = 0; k2 < 4; k2++) {
            this.setNew.cP[k2].set(this.setNew.compassPoint[k2]);
            orientation.transformInv(this.setNew.cP[k2]);
        }

        Vector3d vector3d3 = new Vector3d();
        vector3d3.set(this.setNew.cP[0]);
        vector3d3.add(this.setNew.cP[1]);
        vector3d3.add(this.setNew.cP[2]);
        vector3d3.add(this.setNew.cP[3]);
        vector3d3.scale(0.25D);
        Vector3d vector3d4 = new Vector3d();
        vector3d4.set(vector3d3);
        vector3d4.normalize();
        float f = (float) ((vector3d4.x * vector3d4.x) + (vector3d4.y * vector3d4.y));
        if ((f > this.compassLimit) || (vector3d3.z > 0.0D)) {
            for (int l2 = 0; l2 < 4; l2++) {
                this.setNew.cP[l2].set(this.setOld.cP[l2]);
                this.setNew.compassPoint[l2].set(this.setOld.cP[l2]);
                orientation.transform(this.setNew.compassPoint[l2]);
            }

            vector3d3.set(this.setNew.cP[0]);
            vector3d3.add(this.setNew.cP[1]);
            vector3d3.add(this.setNew.cP[2]);
            vector3d3.add(this.setNew.cP[3]);
            vector3d3.scale(0.25D);
        }
        vector3d4.set(this.setNew.cP[0]);
        vector3d4.sub(vector3d3);
        double d2 = -Math.atan2(vector3d3.y, -vector3d3.z);
        this.vectorRot2(vector3d3, d2);
        this.vectorRot2(vector3d4, d2);
        double d3 = Math.atan2(vector3d3.x, -vector3d3.z);
        this.vectorRot1(vector3d4, -d3);
        double d4 = Math.atan2(vector3d4.y, vector3d4.x);
        this.mesh.chunkSetAngles("compass_base", 0.0F, -(float) ((d2 * 180D) / 3.1415926D), (float) ((d3 * 180D) / 3.1415926D));
        this.mesh.chunkSetAngles("compass_header", -(float) (90D - ((d4 * 180D) / 3.1415926D)), 0.0F, 0.0F);
        this.compassFirst++;
    }

    private void vectorRot1(Vector3d vector3d, double d) {
        double d1 = Math.sin(d);
        double d2 = Math.cos(d);
        double d3 = (vector3d.x * d2) - (vector3d.z * d1);
        vector3d.z = (vector3d.x * d1) + (vector3d.z * d2);
        vector3d.x = d3;
    }

    private void vectorRot2(Vector3d vector3d, double d) {
        double d1 = Math.sin(d);
        double d2 = Math.cos(d);
        double d3 = (vector3d.y * d2) - (vector3d.z * d1);
        vector3d.z = (vector3d.y * d1) + (vector3d.z * d2);
        vector3d.y = d3;
    }

    private void compassDist(Vector3d vector3d, Vector3d vector3d1, double d) {
        Vector3d vector3d2 = new Vector3d();
        vector3d2.set(vector3d);
        vector3d2.sub(vector3d1);
        double d1 = vector3d2.length();
        if (d1 < 0.000001D) {
            d1 = 0.000001D;
        }
        d1 = (d - d1) / d1 / 2D;
        vector3d2.scale(d1);
        vector3d.add(vector3d2);
        vector3d1.sub(vector3d2);
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private Vector3f           w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private float              flaps;
    private float              pictManifold;
    private float              pictGear;
    private float              tailWheelLock;
    private int                flapsDirection;
    private float              flapsLeverAngle;
    private float              flapsIncrement;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private float              rpmGeneratedPressure;
    private float              oilPressure;
    private static final float oilTempScale[]    = { 0.0F, 10.4F, 20.5F, 31F, 45F, 59F, 79F, 98F, 124F, 153F, 191F, 238F, 297F };
    private float              radiator;
    private float              currentRadiator;
    private int                radiatorDelta;
    private boolean            sightDamaged;
    private static double      compassZ          = -0.2D;
    private double             segLen1;
    private double             segLen2;
    private double             compassLimit;
    private static double      compassLimitAngle = 12D;
    private Vector3d           compassSpeed[];
    int                        compassFirst;
    private Vector3d           accel;
    private Vector3d           compassNorth;
    private Vector3d           compassSouth;
    private double             compassAcc;
    private double             compassSc;
    private int                delay;

}
