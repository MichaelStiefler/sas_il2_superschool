package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
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
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitFokker_CXIW extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitFokker_CXIW.this.setTmp = CockpitFokker_CXIW.this.setOld;
            CockpitFokker_CXIW.this.setOld = CockpitFokker_CXIW.this.setNew;
            CockpitFokker_CXIW.this.setNew = CockpitFokker_CXIW.this.setTmp;
            if (((CockpitFokker_CXIW.this.fm.AS.astateCockpitState & 2) != 0) && (CockpitFokker_CXIW.this.setNew.stbyPosition < 1.0F)) {
                CockpitFokker_CXIW.this.setNew.stbyPosition = CockpitFokker_CXIW.this.setOld.stbyPosition + 0.0125F;
                CockpitFokker_CXIW.this.setOld.stbyPosition = CockpitFokker_CXIW.this.setNew.stbyPosition;
            }
            CockpitFokker_CXIW.this.setNew.altimeter = CockpitFokker_CXIW.this.fm.getAltitude();
            CockpitFokker_CXIW.this.setNew.azimuth.setDeg(CockpitFokker_CXIW.this.setOld.azimuth.getDeg(1.0F), CockpitFokker_CXIW.this.fm.Or.azimut());
            CockpitFokker_CXIW.this.setNew.throttle = ((10F * CockpitFokker_CXIW.this.setOld.throttle) + CockpitFokker_CXIW.this.fm.EI.engines[0].getControlThrottle()) / 11F;
            CockpitFokker_CXIW.this.setNew.mix = ((10F * CockpitFokker_CXIW.this.setOld.mix) + CockpitFokker_CXIW.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitFokker_CXIW.this.setNew.prop = CockpitFokker_CXIW.this.setOld.prop;
            if (CockpitFokker_CXIW.this.setNew.prop < (CockpitFokker_CXIW.this.fm.EI.engines[0].getControlProp() - 0.01F)) {
                CockpitFokker_CXIW.this.setNew.prop += 0.0025F;
            }
            if (CockpitFokker_CXIW.this.setNew.prop > (CockpitFokker_CXIW.this.fm.EI.engines[0].getControlProp() + 0.01F)) {
                CockpitFokker_CXIW.this.setNew.prop -= 0.0025F;
            }
            CockpitFokker_CXIW.this.w.set(CockpitFokker_CXIW.this.fm.getW());
            CockpitFokker_CXIW.this.fm.Or.transform(CockpitFokker_CXIW.this.w);
            CockpitFokker_CXIW.this.setNew.turn = ((12F * CockpitFokker_CXIW.this.setOld.turn) + CockpitFokker_CXIW.this.w.z) / 13F;
            CockpitFokker_CXIW.this.setNew.vspeed = ((299F * CockpitFokker_CXIW.this.setOld.vspeed) + CockpitFokker_CXIW.this.fm.getVertSpeed()) / 300F;
            CockpitFokker_CXIW.this.pictSupc = (0.8F * CockpitFokker_CXIW.this.pictSupc) + (0.2F * CockpitFokker_CXIW.this.fm.EI.engines[0].getControlCompressor());
            if (CockpitFokker_CXIW.this.cockpitDimControl) {
                if (CockpitFokker_CXIW.this.setNew.dimPos < 1.0F) {
                    CockpitFokker_CXIW.this.setNew.dimPos = CockpitFokker_CXIW.this.setOld.dimPos + 0.03F;
                }
            } else if (CockpitFokker_CXIW.this.setNew.dimPos > 0.0F) {
                CockpitFokker_CXIW.this.setNew.dimPos = CockpitFokker_CXIW.this.setOld.dimPos - 0.03F;
            }
            if (!CockpitFokker_CXIW.this.fm.Gears.bTailwheelLocked && (CockpitFokker_CXIW.this.tailWheelLock < 1.0F)) {
                CockpitFokker_CXIW.this.tailWheelLock = CockpitFokker_CXIW.this.tailWheelLock + 0.05F;
            } else if (CockpitFokker_CXIW.this.fm.Gears.bTailwheelLocked && (CockpitFokker_CXIW.this.tailWheelLock > 0.0F)) {
                CockpitFokker_CXIW.this.tailWheelLock = CockpitFokker_CXIW.this.tailWheelLock - 0.05F;
            }
            CockpitFokker_CXIW.this.updateCompass();
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      altimeter;
        AnglesFork azimuth;
        float      throttle;
        float      mix;
        float      prop;
        float      turn;
        float      vspeed;
        float      stbyPosition;
        float      dimPos;
        Point3d    planeLoc;
        Point3d    planeMove;
        Vector3d   compassPoint[];
        Vector3d   cP[];

        private Variables() {
            this.dimPos = 1.0F;
            this.azimuth = new AnglesFork();
            this.planeLoc = new Point3d();
            this.planeMove = new Point3d();
            this.compassPoint = new Vector3d[4];
            this.cP = new Vector3d[4];
            this.compassPoint[0] = new Vector3d(0.0D, Math.sqrt(1.0D - (CockpitFokker_CXIW.compassZ * CockpitFokker_CXIW.compassZ)), CockpitFokker_CXIW.compassZ);
            this.compassPoint[1] = new Vector3d(-Math.sqrt(1.0D - (CockpitFokker_CXIW.compassZ * CockpitFokker_CXIW.compassZ)), 0.0D, CockpitFokker_CXIW.compassZ);
            this.compassPoint[2] = new Vector3d(0.0D, -Math.sqrt(1.0D - (CockpitFokker_CXIW.compassZ * CockpitFokker_CXIW.compassZ)), CockpitFokker_CXIW.compassZ);
            this.compassPoint[3] = new Vector3d(Math.sqrt(1.0D - (CockpitFokker_CXIW.compassZ * CockpitFokker_CXIW.compassZ)), 0.0D, CockpitFokker_CXIW.compassZ);
            this.cP[0] = new Vector3d(0.0D, Math.sqrt(1.0D - (CockpitFokker_CXIW.compassZ * CockpitFokker_CXIW.compassZ)), CockpitFokker_CXIW.compassZ);
            this.cP[1] = new Vector3d(-Math.sqrt(1.0D - (CockpitFokker_CXIW.compassZ * CockpitFokker_CXIW.compassZ)), 0.0D, CockpitFokker_CXIW.compassZ);
            this.cP[2] = new Vector3d(0.0D, -Math.sqrt(1.0D - (CockpitFokker_CXIW.compassZ * CockpitFokker_CXIW.compassZ)), CockpitFokker_CXIW.compassZ);
            this.cP[3] = new Vector3d(Math.sqrt(1.0D - (CockpitFokker_CXIW.compassZ * CockpitFokker_CXIW.compassZ)), 0.0D, CockpitFokker_CXIW.compassZ);
        }

    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.25D, 0.0D, 0.0D);
            hookpilot.setTubeSight(point3d);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) {
            return;
        } else {
            this.leave();
            super.doFocusLeave();
            return;
        }
    }

    private void prepareToEnter() {
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(true);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        this.enteringAim = true;
    }

    private void enter() {
        HookPilot hookpilot = HookPilot.current;
        hookpilot.setSimpleUse(true);
        this.doSetSimpleUse(true);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
    }

    public void doSetSimpleUse(boolean flag) {
        super.doSetSimpleUse(flag);
        if (flag) {
            this.saveFov = Main3D.FOVX;
            CmdEnv.top().exec("fov 31");
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
            this.bEntered = true;
            this.mesh.chunkVisible("superretic", true);
            this.mesh.chunkVisible("Z_sight_cap_big", true);
        }
    }

    private void leave() {
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            return;
        }
        if (!this.bEntered) {
            return;
        } else {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot1 = HookPilot.current;
            hookpilot1.doAim(false);
            hookpilot1.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot1.setSimpleUse(false);
            this.doSetSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.bEntered = false;
            this.mesh.chunkVisible("superretic", false);
            this.mesh.chunkVisible("Z_sight_cap_big", false);
            return;
        }
    }

    public void destroy() {
        this.leave();
        super.destroy();
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused() || (this.isToggleAim() == flag)) {
            return;
        }
        if (flag) {
            this.prepareToEnter();
        } else {
            this.leave();
        }
    }

    public CockpitFokker_CXIW() {
        super("3DO/Cockpit/Fokker_CXIW/hierS.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictSupc = 0.0F;
        this.flaps = 0.0F;
        this.bEntered = false;
        this.hasRevi = false;
        this.tailWheelLock = 1.0F;
        this.flapsDirection = 0;
        this.flapsPump = 0.0F;
        this.rpmGeneratedPressure = 0.0F;
        this.oilPressure = 0.0F;
        this.compassFirst = 0;
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
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK03");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light3 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light3.light.setColor(126F, 232F, 245F);
        this.light3.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK03", this.light3);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK04");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light4 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light4.light.setColor(126F, 232F, 245F);
        this.light4.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK04", this.light4);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK05");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light5 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light5.light.setColor(126F, 232F, 245F);
        this.light5.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK05", this.light5);
        this.cockpitNightMats = (new String[] { "gyro", "gauge_speed", "gauge_alt", "gauge_fuel", "gauges_various_1", "gauges_various_2", "LABELS1", "gauges_various_3", "gauges_various_4", "gauges_various_3_dam", "gauge_alt_dam", "gauges_various_2_dam" });
        this.bNeedSetUp = true;
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (World.cur().isHakenAllowed()) {
            this.mesh.materialReplace("LABELS3", "LABELS3_HAKEN");
        }
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            if (hookpilot.isAimReached()) {
                this.enteringAim = false;
                this.enter();
            } else if (!hookpilot.isAim()) {
                this.enteringAim = false;
            }
        }
        float f1 = 0.0F;
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Z_reviIron", 90F * this.setNew.stbyPosition, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_manifold", this.cvt(this.pictManifold = (0.85F * this.pictManifold) + (0.15F * this.fm.EI.engines[0].getManifoldPressure() * 76F), 30F, 120F, 22F, 296F), 0.0F, 0.0F);
        f1 = -15F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl));
        this.mesh.chunkSetAngles("Z_stick_horiz_axis", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_aileron_rods", -f1 / 14F, 0.0F, 0.0F);
        f1 = (12F * (this.pictElev = (0.85F * this.pictElev) + (0.2F * this.fm.CT.ElevatorControl))) + 2.0F;
        this.mesh.chunkSetAngles("Z_Stick", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_elev_wire1", -f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_elev_wire2", -f1, 0.0F, 0.0F);
        f1 = this.fm.CT.getRudder();
        this.mesh.chunkSetAngles("Z_wheel_break_valve", -12F * f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_pedal_L", 24F * f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_pedal_R", -24F * f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_rudder_rod_L", -25F * f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_rudder_rod_R", 25F * f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle", -70F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture", -70F * this.interp(this.setNew.mix, this.setOld.mix, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_alt1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_alt2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 720F), 0.0F, 0.0F);
        float f2 = Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH());
        if (f2 < 80F) {
            this.mesh.chunkSetAngles("Z_Need_speed", this.cvt(f2, 0.0F, 80F, 0.0F, -11F), 0.0F, 0.0F);
        } else if (f2 < 120F) {
            this.mesh.chunkSetAngles("Z_Need_speed", this.cvt(f2, 80F, 120F, -11F, -40F), 0.0F, 0.0F);
        } else if (f2 < 160F) {
            this.mesh.chunkSetAngles("Z_Need_speed", this.cvt(f2, 120F, 160F, -40F, -78.5F), 0.0F, 0.0F);
        } else if (f2 < 200F) {
            this.mesh.chunkSetAngles("Z_Need_speed", this.cvt(f2, 160F, 200F, -78.5F, -130.5F), 0.0F, 0.0F);
        } else if (f2 < 360F) {
            this.mesh.chunkSetAngles("Z_Need_speed", this.cvt(f2, 200F, 360F, -130.5F, -329F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Need_speed", this.cvt(f2, 360F, 600F, -329F, -550F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Need_gyro", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_clock_hour", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, -720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_clock_minute", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_clock_sec", -this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_fuel", -this.cvt(this.fm.M.fuel, 0.0F, 300F, 0.0F, 52F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_oiltemp", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 329F), 0.0F, 0.0F);
        f1 = this.fm.EI.engines[0].getRPM();
        this.mesh.chunkSetAngles("Z_Need_rpm", this.cvt(f1, 440F, 3320F, 0.0F, -332F), 0.0F, 0.0F);
        if ((this.fm.Or.getKren() < -110F) || (this.fm.Or.getKren() > 110F)) {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure - 2.0F;
        } else if (f1 < this.rpmGeneratedPressure) {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure - ((this.rpmGeneratedPressure - f1) * 0.01F);
        } else {
            this.rpmGeneratedPressure = this.rpmGeneratedPressure + ((f1 - this.rpmGeneratedPressure) * 0.001F);
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
        this.mesh.chunkSetAngles("Z_Need_oilpressure", this.cvt(f4, 0.0F, 7F, 0.0F, 315F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_cylheadtemp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 110F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_fuelpressure", this.cvt(this.rpmGeneratedPressure, 0.0F, 1800F, 0.0F, 65F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_magneto", -30F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = -this.cvt(this.fm.Or.getTangage(), -20F, 20F, 0.04F, -0.04F);
        this.mesh.chunkSetLocate("Z_Need_red_liquid", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Need_Turn", -this.cvt(this.setNew.turn, -0.2F, 0.2F, -30F, 30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_bank", -this.cvt(this.getBall(8D), -8F, 8F, 14F, -14F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_variometer", -this.cvt(this.setNew.vspeed, -20F, 20F, 180F, -180F), 0.0F, 0.0F);
        if (this.fm.getAltitude() < 3000F) {
            this.mesh.chunkSetAngles("Z_Need_oxygeneflow", 0.0F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Need_oxygeneflow", 200F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Need_oxygenetank", 250F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_flaps_indicator", 0.7F * this.flaps, 0.0F, 0.0F);
        if (this.flapsDirection == 1) {
            this.mesh.chunkSetAngles("Z_flaps_valve", -33F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_flapsLeverKnob", 33F, 0.0F, 0.0F);
        } else if (this.flapsDirection == -1) {
            this.mesh.chunkSetAngles("Z_flaps_valve", 33F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_flapsLeverKnob", -33F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_flaps_valve", 0.0F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_flapsLeverKnob", 0.0F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_trim_indicator", 1.9F * -this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_trim_wheel", 600F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        if (this.fm.CT.bHasBrakeControl) {
            float f5 = this.fm.CT.getBrake();
            this.mesh.chunkSetAngles("Z_break_handle", (f5 * 12F) + 12F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Need_breakpressureR", this.cvt(f5 + (f5 * this.fm.CT.getRudder()), 0.0F, 1.5F, 0.0F, 148F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Need_breakpressureL", -this.cvt(f5 - (f5 * this.fm.CT.getRudder()), 0.0F, 1.5F, 0.0F, 148F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_Need_breakpressure1", -150F + (f5 * 20F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_flaps_pump", -this.flapsPump * 40F, 0.0F, 0.0F);
        if (this.fm.AS.bLandingLightOn) {
            this.mesh.chunkSetAngles("Z_switch_landing_light", -35F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_switch_landing_light", 0.0F, 0.0F, 0.0F);
        }
        if (this.fm.AS.bNavLightsOn) {
            this.mesh.chunkSetAngles("Z_switch_navigation_light", -35F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_switch_navigation_light", 0.0F, 0.0F, 0.0F);
        }
        if (this.cockpitLightControl) {
            this.mesh.chunkSetAngles("Z_switch_cockpit_light", -35F, 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_switch_cockpit_light", 0.0F, 0.0F, 0.0F);
        }
        if (this.tailWheelLock >= 1.0F) {
            this.mesh.chunkSetAngles("Z_tailwheel", this.tailWheelLock * 57F, 0.0F, 7F);
            this.mesh.chunkSetAngles("Z_tailwheel_lever_wire", this.tailWheelLock * 57F, 0.0F, 7F);
        } else {
            this.mesh.chunkSetAngles("Z_tailwheel", this.tailWheelLock * 57F, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_tailwheel_lever_wire", this.tailWheelLock * 57F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_wheelLockKnob", this.tailWheelLock * 57F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_extinguisher", this.fm.EI.engines[0].getExtinguishers() * 95F, 0.0F, 0.0F);
        if (this.hasRevi) {
            this.mesh.chunkSetAngles("Z_reviDimmer", -this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -90F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_reviDimmerLever", -this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, 0.004F), 0.0F, 0.0F);
        } else {
            float f6 = this.cvt(this.interp(this.setNew.dimPos, this.setOld.dimPos, f), 0.0F, 1.0F, 0.0F, -130F);
            this.mesh.chunkSetAngles("Z_sight_cap", f6, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_sight_cap_big", f6, 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_sight_cap_inside", f6, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_trigger", 1.6F * (this.fm.CT.saveWeaponControl[0] ? 1.0F : 0.0F), 0.0F, 0.0F);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("GlassDamageFront2", true);
            this.mesh.chunkVisible("HullDamageRear", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("GlassDamageFront", true);
            this.mesh.chunkVisible("HullDamageRear", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Gauges_d0", false);
            this.mesh.chunkVisible("Gauges_d1", true);
            this.mesh.chunkVisible("HullDamageFront", true);
            this.mesh.chunkVisible("Z_Need_manifold", false);
            this.mesh.chunkVisible("Z_Need_oilpressure", false);
            this.mesh.chunkVisible("Z_Need_rpm", false);
            this.mesh.chunkVisible("Z_Need_alt1", false);
            this.mesh.chunkVisible("Z_Need_alt2", false);
            this.mesh.chunkVisible("Z_Need_variometer", false);
            this.mesh.chunkVisible("Z_Need_clock_sec", false);
            this.mesh.chunkVisible("Z_Need_clock_minute", false);
            this.mesh.chunkVisible("Z_Need_clock_hour", false);
            this.mesh.chunkVisible("Z_Need_clock_timer", false);
            this.mesh.chunkVisible("Z_Need_cylheadtemp", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("GlassDamageLeft", true);
            this.mesh.chunkVisible("HullDamageLeft", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("GlassDamageLeft", true);
            this.mesh.chunkVisible("GlassDamageLeft2", true);
            this.mesh.chunkVisible("HullDamageLeft", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("GlassDamageRight", true);
            this.mesh.chunkVisible("HullDamageRight", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("GlassDamageRight", true);
            this.mesh.chunkVisible("HullDamageRight", true);
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.003F, 0.2F);
            this.light2.light.setEmit(0.004F, 0.2F);
            this.light3.light.setEmit(0.004F, 0.2F);
            this.light4.light.setEmit(0.001F, 0.1F);
            this.light5.light.setEmit(0.003F, 0.2F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.light3.light.setEmit(0.0F, 0.0F);
            this.light4.light.setEmit(0.0F, 0.0F);
            this.light5.light.setEmit(0.0F, 0.0F);
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
        this.segLen1 = 2D * Math.sqrt(1.0D - (CockpitFokker_CXIW.compassZ * CockpitFokker_CXIW.compassZ));
        this.segLen2 = this.segLen1 / Math.sqrt(2D);
        this.compassLimit = -1D * Math.sin(0.01745328888888889D * CockpitFokker_CXIW.compassLimitAngle);
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
        if (this.accel.length() > (-CockpitFokker_CXIW.compassZ * 0.7D)) {
            this.accel.scale((-CockpitFokker_CXIW.compassZ * 0.7D) / this.accel.length());
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
            Vector3d vector3d1 = new Vector3d();
            double d1 = vector3d.dot(this.compassSpeed[k]);
            vector3d1.set(vector3d);
            d1 *= 0.28D;
            vector3d1.scale(-d1);
            this.compassSpeed[k].add(vector3d1);
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
        Vector3d vector3d2 = new Vector3d(vector3d);
        vector3d2.normalize();
        vector3d2.scale(-CockpitFokker_CXIW.compassZ);
        vector3d2.sub(vector3d);
        for (int j1 = 0; j1 < 4; j1++) {
            this.setNew.compassPoint[j1].add(vector3d2);
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
        this.mesh.chunkSetAngles("Z_Need_compassBase", -(float) ((d2 * 180D) / Math.PI), -(float) ((d3 * 180D) / Math.PI), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_compass", 0.0F, (float) (90D - ((d4 * 180D) / Math.PI)), 0.0F);
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

    private Variables       setOld;
    private Variables       setNew;
    private Variables       setTmp;
    private Vector3f        w;
    private boolean         bNeedSetUp;
    private float           pictAiler;
    private float           pictElev;
    private float           pictSupc;
    private float           flaps;
    private float           pictManifold;
    private boolean         bEntered;
    private boolean         enteringAim;
    private float           saveFov;
    private boolean         hasRevi;
    private float           tailWheelLock;
    private int             flapsDirection;
    private float           flapsPump;
    private LightPointActor light1;
    private LightPointActor light2;
    private LightPointActor light3;
    private LightPointActor light4;
    private LightPointActor light5;
    private float           rpmGeneratedPressure;
    private float           oilPressure;
    private static double   compassZ          = -0.2D;
    private double          segLen1;
    private double          segLen2;
    private double          compassLimit;
    private static double   compassLimitAngle = 12D;
    private Vector3d        compassSpeed[];
    int                     compassFirst;
    private Vector3d        accel;
    private Vector3d        compassNorth;
    private Vector3d        compassSouth;
    private double          compassAcc;
    private double          compassSc;

}
