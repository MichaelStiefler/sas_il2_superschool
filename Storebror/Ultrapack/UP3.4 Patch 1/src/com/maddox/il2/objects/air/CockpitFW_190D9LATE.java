package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
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
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitFW_190D9LATE extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitFW_190D9LATE.this.fm != null) {
                CockpitFW_190D9LATE.this.setTmp = CockpitFW_190D9LATE.this.setOld;
                CockpitFW_190D9LATE.this.setOld = CockpitFW_190D9LATE.this.setNew;
                CockpitFW_190D9LATE.this.setNew = CockpitFW_190D9LATE.this.setTmp;
                CockpitFW_190D9LATE.this.setNew.altimeter = CockpitFW_190D9LATE.this.fm.getAltitude();
                if (CockpitFW_190D9LATE.this.cockpitDimControl) {
                    if (CockpitFW_190D9LATE.this.setNew.dimPosition > 0.0F) {
                        CockpitFW_190D9LATE.this.setNew.dimPosition = CockpitFW_190D9LATE.this.setOld.dimPosition - 0.05F;
                    }
                } else if (CockpitFW_190D9LATE.this.setNew.dimPosition < 1.0F) {
                    CockpitFW_190D9LATE.this.setNew.dimPosition = CockpitFW_190D9LATE.this.setOld.dimPosition + 0.05F;
                }
                CockpitFW_190D9LATE.this.setNew.throttle = ((10F * CockpitFW_190D9LATE.this.setOld.throttle) + CockpitFW_190D9LATE.this.fm.CT.PowerControl) / 11F;
                CockpitFW_190D9LATE.this.setNew.vspeed = ((499F * CockpitFW_190D9LATE.this.setOld.vspeed) + CockpitFW_190D9LATE.this.fm.getVertSpeed()) / 500F;
                float f = CockpitFW_190D9LATE.this.waypointAzimuth();
                if (CockpitFW_190D9LATE.this.useRealisticNavigationInstruments()) {
                    CockpitFW_190D9LATE.this.setNew.waypointAzimuth.setDeg(f - 90F);
                    CockpitFW_190D9LATE.this.setOld.waypointAzimuth.setDeg(f - 90F);
                } else {
                    CockpitFW_190D9LATE.this.setNew.waypointAzimuth.setDeg(CockpitFW_190D9LATE.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitFW_190D9LATE.this.setOld.azimuth.getDeg(1.0F));
                }
                CockpitFW_190D9LATE.this.setNew.azimuth.setDeg(CockpitFW_190D9LATE.this.setOld.azimuth.getDeg(1.0F), CockpitFW_190D9LATE.this.fm.Or.azimut());
                CockpitFW_190D9LATE.this.w.set(CockpitFW_190D9LATE.this.fm.getW());
                CockpitFW_190D9LATE.this.fm.Or.transform(CockpitFW_190D9LATE.this.w);
                CockpitFW_190D9LATE.this.setNew.turn = ((12F * CockpitFW_190D9LATE.this.setOld.turn) + CockpitFW_190D9LATE.this.w.z) / 13F;
                CockpitFW_190D9LATE.this.setNew.beaconDirection = ((10F * CockpitFW_190D9LATE.this.setOld.beaconDirection) + CockpitFW_190D9LATE.this.getBeaconDirection()) / 11F;
                CockpitFW_190D9LATE.this.setNew.beaconRange = ((10F * CockpitFW_190D9LATE.this.setOld.beaconRange) + CockpitFW_190D9LATE.this.getBeaconRange()) / 11F;
                float f1 = ((FW_190D9LATE) CockpitFW_190D9LATE.this.aircraft()).k14Distance;
                CockpitFW_190D9LATE.this.setNew.k14w = (5F * CockpitFW_190D9LATE.k14TargetWingspanScale[((FW_190D9LATE) CockpitFW_190D9LATE.this.aircraft()).k14WingspanType]) / f1;
                CockpitFW_190D9LATE.this.setNew.k14w = (0.9F * CockpitFW_190D9LATE.this.setOld.k14w) + (0.1F * CockpitFW_190D9LATE.this.setNew.k14w);
                CockpitFW_190D9LATE.this.setNew.k14wingspan = (0.9F * CockpitFW_190D9LATE.this.setOld.k14wingspan) + (0.1F * CockpitFW_190D9LATE.k14TargetMarkScale[((FW_190D9LATE) CockpitFW_190D9LATE.this.aircraft()).k14WingspanType]);
                CockpitFW_190D9LATE.this.setNew.k14mode = (0.8F * CockpitFW_190D9LATE.this.setOld.k14mode) + (0.2F * ((FW_190D9LATE) CockpitFW_190D9LATE.this.aircraft()).k14Mode);
                Vector3d vector3d = CockpitFW_190D9LATE.this.aircraft().FM.getW();
                double d = 0.00125D * f1;
                float f2 = (float) Math.toDegrees(d * vector3d.z);
                float f3 = -(float) Math.toDegrees(d * vector3d.y);
                float f4 = CockpitFW_190D9LATE.this.floatindex((f1 - 200F) * 0.04F, CockpitFW_190D9LATE.k14BulletDrop) - CockpitFW_190D9LATE.k14BulletDrop[0];
                f3 += (float) Math.toDegrees(Math.atan(f4 / f1));
                CockpitFW_190D9LATE.this.setNew.k14x = (0.92F * CockpitFW_190D9LATE.this.setOld.k14x) + (0.08F * f2);
                CockpitFW_190D9LATE.this.setNew.k14y = (0.92F * CockpitFW_190D9LATE.this.setOld.k14y) + (0.08F * f3);
                if (CockpitFW_190D9LATE.this.setNew.k14x > 7F) {
                    CockpitFW_190D9LATE.this.setNew.k14x = 7F;
                }
                if (CockpitFW_190D9LATE.this.setNew.k14x < -7F) {
                    CockpitFW_190D9LATE.this.setNew.k14x = -7F;
                }
                if (CockpitFW_190D9LATE.this.setNew.k14y > 7F) {
                    CockpitFW_190D9LATE.this.setNew.k14y = 7F;
                }
                if (CockpitFW_190D9LATE.this.setNew.k14y < -7F) {
                    CockpitFW_190D9LATE.this.setNew.k14y = -7F;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        float      throttle;
        float      dimPosition;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      turn;
        float      beaconDirection;
        float      beaconRange;
        float      vspeed;
        float      k14wingspan;
        float      k14mode;
        float      k14x;
        float      k14y;
        float      k14w;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(30F);
    }

    public CockpitFW_190D9LATE() {
        super("3DO/Cockpit/FW-190D-9Late/hier.him", "bf109");
        this.gun = new Gun[4];
        this.bomb = new BulletEmitter[4];
        AircraftLH.printCompassHeading = true;
        ((AircraftLH) this.aircraft()).bWantBeaconKeys = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.w = new Vector3f();
        this.setNew.dimPosition = 1.0F;
        this.bNeedSetUp = true;
        HookNamed hooknamed = new HookNamed(this.mesh, "LIGHTHOOK_L");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(126F, 232F, 245F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
        hooknamed = new HookNamed(this.mesh, "LIGHTHOOK_R");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(126F, 232F, 245F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
        this.cockpitNightMats = (new String[] { "D9GP1", "D9GP2", "D9GP3", "A5GP3Km", "D9GP4_MW50", "D9GP5", "A4GP6", "D9Trans2", "DD9GP1", "D9GP2", "DD9GP3", "DD9GP4", "DD9Trans2", "D9EQpt5" });
        this.setNightMats(false);
        this.cockpitDimControl = !this.cockpitDimControl;
        this.interpPut(new Interpolater(), null, Time.current(), null);
        AircraftLH.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float f) {
        int i = ((FW_190D9LATE) this.aircraft()).k14Mode;
        boolean flag = i < 2;
        this.mesh.chunkVisible("Z_Z_RETICLE", flag);
        flag = i > 0;
        this.mesh.chunkVisible("Z_Z_RETICLE1", flag);
        this.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, this.setNew.k14x, this.setNew.k14y);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.setNew.k14w;
        for (int j = 1; j < 7; j++) {
            this.mesh.chunkVisible("Z_Z_AIMMARK" + j, flag);
            this.mesh.chunkSetLocate("Z_Z_AIMMARK" + j, Cockpit.xyz, Cockpit.ypr);
        }

        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN01");
            this.gun[1] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN02");
            this.gun[2] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON03");
            this.gun[3] = ((Aircraft) this.fm.actor).getGunByHookName("_CANNON04");
        }
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (this.bomb[0] == null) {
            for (int k = 0; k < this.bomb.length; k++) {
                this.bomb[k] = GunEmpty.get();
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
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.5F);
        Cockpit.xyz[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 0.01F);
        Cockpit.ypr[2] = this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, -2F);
        this.mesh.chunkSetLocate("CanopyTop", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("EjectatorHandle", 2520F * this.fm.CT.getCockpitDoor(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("CanopyFrameL", -this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 2.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("CanopyFrameR", this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 2.0F), 0.0F, 0.0F);
        if (((this.fm.AS.astateCockpitState & 0x20) == 0) && ((this.fm.AS.astateCockpitState & 0x80) == 0)) {
            this.mesh.chunkSetAngles("NeedleALT", -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("NeedleALTKm", 0.0F, 0.0F, this.cvt(this.setNew.altimeter, 0.0F, 10000F, 0.0F, -180F));
        }
        this.mesh.chunkSetAngles("NeedleManPress", -this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 336F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleKMH", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 900F, 0.0F, 9F), CockpitFW_190D9LATE.speedometerScale), 0.0F, 0.0F);
        if (((this.fm.AS.astateCockpitState & 0x40) == 0) && ((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x10) == 0)) {
            this.mesh.chunkSetAngles("NeedleRPM", -this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitFW_190D9LATE.rpmScale), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("NeedleFuel", this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 400F, 0.0F, 4F), CockpitFW_190D9LATE.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleWaterTemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 60F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleOilTemp", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 60F), 0.0F, 0.0F);
        if (((this.fm.AS.astateCockpitState & 2) == 0) && ((this.fm.AS.astateCockpitState & 1) == 0) && ((this.fm.AS.astateCockpitState & 4) == 0)) {
            this.mesh.chunkSetAngles("NeedleFuelPress", this.cvt(this.fm.EI.engines[0].getRPM() / 1500F, 0.0F, 3F, 0.0F, 135F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("NeedleOilPress", -this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut) + (this.fm.EI.engines[0].getRPM() / 1500F), 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
        }
        if (((this.fm.AS.astateCockpitState & 0x40) == 0) && ((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x10) == 0)) {
            this.mesh.chunkSetAngles("NeedleAHTurn", 0.0F, this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 25F, -25F), 0.0F);
            this.mesh.chunkSetAngles("NeedleAHBank", 0.0F, -this.cvt(this.getBall(7D), -7F, 7F, 11F, -11F), 0.0F);
            this.mesh.chunkSetAngles("NeedleAHCyl", 0.0F, 0.0F, this.fm.Or.getKren());
            this.mesh.chunkSetAngles("NeedleAHBar", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -45F, 45F, 12F, -12F));
        }
        if (((this.fm.AS.astateCockpitState & 0x20) == 0) && ((this.fm.AS.astateCockpitState & 0x80) == 0)) {
            this.mesh.chunkSetAngles("NeedleCD", this.setNew.vspeed < 0.0F ? this.floatindex(this.cvt(-this.setNew.vspeed, 0.0F, 30F, 0.0F, 6F), CockpitFW_190D9LATE.vsiNeedleScale) : -this.floatindex(this.cvt(this.setNew.vspeed, 0.0F, 30F, 0.0F, 6F), CockpitFW_190D9LATE.vsiNeedleScale), 0.0F, 0.0F);
        }
        if (((this.fm.AS.astateCockpitState & 0x40) == 0) && ((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x10) == 0)) {
            if (this.useRealisticNavigationInstruments()) {
                this.mesh.chunkSetAngles("RepeaterPlane", -this.setNew.azimuth.getDeg(f) + this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
                this.mesh.chunkSetAngles("RepeaterOuter", this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("RepeaterOuter", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
                this.mesh.chunkSetAngles("RepeaterPlane", -this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
            }
        }
        if (((this.fm.AS.astateCockpitState & 0x40) == 0) && ((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x10) == 0)) {
            this.mesh.chunkSetAngles("NeedleTrimmung", this.fm.CT.getTrimElevatorControl() * 25F, 0.0F, 0.0F);
        }
        if (((this.fm.AS.astateCockpitState & 0x20) == 0) && ((this.fm.AS.astateCockpitState & 0x80) == 0)) {
            this.mesh.chunkSetAngles("NeedleHClock", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("NeedleMClock", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("NeedleSClock", -this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        }
        float f1 = this.fm.M.nitro / this.fm.M.maxNitro;
        f1 = (float) Math.sqrt(f1);
        if (this.fm.EI.engines[0].getControlAfterburner()) {
            this.mesh.chunkSetAngles("NeedleMW50Press", this.cvt(f1, 0.0F, 0.5F, 0.0F, 230F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("NeedleMW50Press", this.cvt(f1, 0.0F, 0.5F, 0.0F, 0.0F), 0.0F, 0.0F);
        }
        this.resetYPRmodifier();
        if (this.gun[0] != null) {
            Cockpit.xyz[0] = this.cvt(this.gun[0].countBullets(), 0.0F, 500F, -0.044F, 0.0F);
            this.mesh.chunkSetLocate("RC_MG17_L", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkVisible("XLampMG17_1", this.gun[0].haveBullets());
        }
        if (this.gun[1] != null) {
            Cockpit.xyz[0] = this.cvt(this.gun[1].countBullets(), 0.0F, 500F, -0.044F, 0.0F);
            this.mesh.chunkSetLocate("RC_MG17_R", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkVisible("XLampMG17_2", this.gun[1].haveBullets());
        }
        if (this.gun[2] != null) {
            Cockpit.xyz[0] = this.cvt(this.gun[2].countBullets(), 0.0F, 200F, -0.017F, 0.0F);
            this.mesh.chunkSetLocate("RC_MG151_L", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.gun[3] != null) {
            Cockpit.xyz[0] = this.cvt(this.gun[3].countBullets(), 0.0F, 200F, -0.017F, 0.0F);
            this.mesh.chunkSetLocate("RC_MG151_R", Cockpit.xyz, Cockpit.ypr);
            this.mesh.chunkVisible("XLampMG151_1", this.gun[3].haveBullets());
        }
        this.mesh.chunkSetAngles("IgnitionSwitch", 24F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Revi16Tinter", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -45F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("EZ42Filter", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -85F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("EZ42FLever", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("EZ42Range", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("EZ42Size", this.cvt(this.interp(-this.setNew.k14wingspan / 106F, -this.setOld.k14wingspan / 106F, f), 0.0F, 1.0F, 36.5F, -57.2F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Stick", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 20F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 20F);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[1]) {
            Cockpit.xyz[2] = -0.004F;
        }
        this.mesh.chunkSetLocate("SecTrigger", Cockpit.xyz, Cockpit.ypr);
        float f3 = 0.0F;
        int i1 = 0;
        if (this.fm.CT.saveWeaponControl[0] || this.fm.CT.saveWeaponControl[1] || this.fm.CT.saveWeaponControl[3]) {
            if (this.fm.CT.saveWeaponControl[0] || this.fm.CT.saveWeaponControl[1]) {
                f3 = 20F;
            }
            this.tClap = Time.tickCounter() + World.Rnd().nextInt(500, 1000);
            if (this.fm.CT.saveWeaponControl[0] && !this.fm.CT.saveWeaponControl[1]) {
            } else if (!this.fm.CT.saveWeaponControl[0] && this.fm.CT.saveWeaponControl[1]) {
            } else {
            }
        }
        if (Time.tickCounter() < this.tClap) {
            i1 = 1;
        }
        this.mesh.chunkSetAngles("SecTrigger2", -(240F + f3) * (this.pictClap = (0.85F * this.pictClap) + (0.15F * i1)), 0.0F, 0.0F);
        Cockpit.ypr[0] = this.interp(this.setNew.throttle, this.setOld.throttle, f) * 34F * 0.91F;
        Cockpit.xyz[2] = Cockpit.ypr[0] > 7F ? -0.006F : 0.0F;
        this.mesh.chunkSetLocate("ThrottleQuad", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("RPedalBase", 0.0F, 0.0F, this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("RPedalStrut", 0.0F, 0.0F, -this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("RPedal", 0.0F, 0.0F, (-this.fm.CT.getRudder() * 15F) - (this.fm.CT.getBrake() * 15F));
        this.mesh.chunkSetAngles("LPedalBase", 0.0F, 0.0F, -this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("LPedalStrut", 0.0F, 0.0F, this.fm.CT.getRudder() * 15F);
        this.mesh.chunkSetAngles("LPedal", 0.0F, 0.0F, (this.fm.CT.getRudder() * 15F) - (this.fm.CT.getBrake() * 15F));
        this.mesh.chunkVisible("XLampTankSwitch", this.fm.EI.engines[0].getStage() > 0.0F);
        this.mesh.chunkVisible("XLampFuelLow", this.fm.M.fuel < 39.4F);
        this.mesh.chunkVisible("XLampGearL_1", this.fm.CT.getGear() < 0.05F);
        this.mesh.chunkVisible("XLampGearL_2", (this.fm.CT.getGear() > 0.95F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearR_1", this.fm.CT.getGear() < 0.05F);
        this.mesh.chunkVisible("XLampGearR_2", (this.fm.CT.getGear() > 0.95F) && this.fm.Gears.rgear);
        this.mesh.chunkSetAngles("NeedleNahe1", this.cvt(this.setNew.beaconDirection, -45F, 45F, 20F, -20F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("NeedleNahe2", this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, -20F, 20F), 0.0F, 0.0F);
        this.mesh.chunkVisible("AFN2_RED", this.isOnBlindLandingMarker());
        if (this.fm.EI.engines[0].getControlAfterburner()) {
            this.mesh.chunkSetAngles("SwitchMW50", 0.0F, -23F, 0.0F);
            this.mesh.chunkVisible("XLampMW50", true);
        } else {
            this.mesh.chunkSetAngles("SwitchMW50", 0.0F, 23F, 0.0F);
            this.mesh.chunkVisible("XLampMW50", false);
        }
        float f4 = Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F;
        if (f4 < -16F) {
            this.mesh.chunkVisible("XLampPitot", true);
        } else if (f4 > -12F) {
            this.mesh.chunkVisible("XLampPitot", false);
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.0012F, 0.75F);
            this.light2.light.setEmit(0.0012F, 0.75F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
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
        if (((this.fm.AS.astateCockpitState & 2) != 0) || ((this.fm.AS.astateCockpitState & 1) != 0) || ((this.fm.AS.astateCockpitState & 4) != 0)) {
            if ((this.fm.AS.astateCockpitState & 2) != 0) {
                this.mesh.chunkVisible("EZ42", false);
                this.mesh.chunkVisible("Revi16Tinter", false);
                this.mesh.chunkVisible("EZ42Filter", false);
                this.mesh.chunkVisible("EZ42FLever", false);
                this.mesh.chunkVisible("EZ42Range", false);
                this.mesh.chunkVisible("EZ42Size", false);
                this.mesh.chunkVisible("Z_Z_MASK", false);
                this.mesh.chunkVisible("Z_Z_RETICLE", false);
                this.mesh.chunkVisible("DEZ42", true);
            }
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassCanopyDamage1", true);
            this.mesh.chunkVisible("HullDamage1", true);
            this.mesh.materialReplace("D9GP1", "DD9GP1");
            this.mesh.materialReplace("D9GP1_night", "DD9GP1_night");
            this.mesh.chunkVisible("NeedleManPress", false);
            this.mesh.chunkVisible("NeedleRPM", false);
            this.mesh.chunkVisible("RepeaterOuter", false);
            this.mesh.chunkVisible("RepeaterPlane", false);
        }
        if (((this.fm.AS.astateCockpitState & 0x40) != 0) || ((this.fm.AS.astateCockpitState & 8) != 0) || ((this.fm.AS.astateCockpitState & 0x10) != 0)) {
            this.mesh.chunkVisible("XGlassCanopyDamage2", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("HullDamage3", true);
            this.mesh.materialReplace("D9GP2", "DD9GP2");
            this.mesh.materialReplace("D9GP2_night", "DD9GP2_night");
            this.mesh.chunkVisible("NeedleAHCyl", false);
            this.mesh.chunkVisible("NeedleAHBank", false);
            this.mesh.chunkVisible("NeedleAHBar", false);
            this.mesh.chunkVisible("NeedleAHTurn", false);
            this.mesh.chunkVisible("NeedleFuelPress", false);
            this.mesh.chunkVisible("NeedleOilPress", false);
            this.mesh.materialReplace("D9GP4_MW50", "DD9GP4_MW50");
            this.mesh.materialReplace("D9GP4_MW50_night", "DD9GP4_MW50_night");
            this.mesh.chunkVisible("NeedleFuel", false);
            this.resetYPRmodifier();
            Cockpit.xyz[0] = -0.001F;
            Cockpit.xyz[1] = 0.008F;
            Cockpit.xyz[2] = 0.025F;
            Cockpit.ypr[0] = 3F;
            Cockpit.ypr[1] = -6F;
            Cockpit.ypr[2] = 1.5F;
            this.mesh.chunkSetLocate("IPCentral", Cockpit.xyz, Cockpit.ypr);
        }
        if (((this.fm.AS.astateCockpitState & 0x20) != 0) || ((this.fm.AS.astateCockpitState & 0x80) != 0)) {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("HullDamage2", true);
            this.mesh.materialReplace("D9GP3", "DD9GP3");
            this.mesh.materialReplace("D9GP3_night", "DD9GP3_night");
            this.mesh.chunkVisible("NeedleKMH", false);
            this.mesh.chunkVisible("NeedleCD", false);
            this.mesh.chunkVisible("NeedleAlt", false);
            this.mesh.chunkVisible("NeedleAltKM", false);
            this.mesh.materialReplace("D9Trans2", "DD9Trans2");
            this.mesh.materialReplace("D9Trans2_night", "DD9Trans2_night");
            this.mesh.chunkVisible("NeedleWaterTemp", false);
            this.mesh.chunkVisible("NeedleOilTemp", false);
        }
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    private Gun                gun[];
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private boolean            bNeedSetUp;
    private BulletEmitter      bomb[];
    private float              pictAiler;
    private float              pictElev;
    public Vector3f            w;
    private static final float speedometerScale[]       = { 0.0F, 18.5F, 67F, 117F, 164F, 215F, 267F, 320F, 379F, 427F, 428F };
    private static final float rpmScale[]               = { 0.0F, 11.25F, 53F, 108F, 170F, 229F, 282F, 334F, 342.5F, 342.5F };
    private static final float fuelScale[]              = { 0.0F, 16F, 35F, 52.5F, 72F, 72F };
    private static final float vsiNeedleScale[]         = { 0.0F, 48F, 82F, 96.5F, 111F, 120.5F, 130F, 130F };
    private static final float k14TargetMarkScale[]     = { -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F };
    private static final float k14TargetWingspanScale[] = { 9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F };
    private static final float k14BulletDrop[]          = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 10.603F, 10.704F, 10.739F, 10.782F, 10.789F };
    private int                tClap;
    private float              pictClap;
    static {
        Property.set(CockpitFW_190D9LATE.class, "normZN", 0.72F);
        Property.set(CockpitFW_190D9LATE.class, "gsZN", 0.66F);
    }

}
