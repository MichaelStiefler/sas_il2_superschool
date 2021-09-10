package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Time;

public class CockpitE7K extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitE7K.this.fm != null) {
                if (CockpitE7K.this.bNeedSetUp) {
                    CockpitE7K.this.reflectPlaneMats();
                    CockpitE7K.this.bNeedSetUp = false;
                }
                CockpitE7K.this.setTmp = CockpitE7K.this.setOld;
                CockpitE7K.this.setOld = CockpitE7K.this.setNew;
                CockpitE7K.this.setNew = CockpitE7K.this.setTmp;
                CockpitE7K.this.setNew.throttle = ((10F * CockpitE7K.this.setOld.throttle) + CockpitE7K.this.fm.CT.PowerControl) / 11F;
                CockpitE7K.this.setNew.prop = ((10F * CockpitE7K.this.setOld.prop) + CockpitE7K.this.fm.EI.engines[0].getControlProp()) / 11F;
                CockpitE7K.this.setNew.altimeter = CockpitE7K.this.fm.getAltitude();
                if (Math.abs(CockpitE7K.this.fm.Or.getKren()) < 30F) {
                    CockpitE7K.this.setNew.azimuth = ((35F * CockpitE7K.this.setOld.azimuth) + -CockpitE7K.this.fm.Or.getYaw()) / 36F;
                }
                if ((CockpitE7K.this.setOld.azimuth > 270F) && (CockpitE7K.this.setNew.azimuth < 90F)) {
                    CockpitE7K.this.setOld.azimuth -= 360F;
                }
                if ((CockpitE7K.this.setOld.azimuth < 90F) && (CockpitE7K.this.setNew.azimuth > 270F)) {
                    CockpitE7K.this.setOld.azimuth += 360F;
                }
                CockpitE7K.this.setNew.waypointAzimuth = ((10F * CockpitE7K.this.setOld.waypointAzimuth) + (CockpitE7K.this.waypointAzimuth() - CockpitE7K.this.setOld.azimuth) + World.Rnd().nextFloat(-10F, 10F)) / 11F;
                CockpitE7K.this.setNew.vspeed = ((199F * CockpitE7K.this.setOld.vspeed) + CockpitE7K.this.fm.getVertSpeed()) / 200F;
                boolean flag = false;
                if (CockpitE7K.this.setNew.gCrankAngle < (CockpitE7K.this.fm.CT.getGear() - 0.005F)) {
                    if (Math.abs(CockpitE7K.this.setNew.gCrankAngle - CockpitE7K.this.fm.CT.getGear()) < 0.33F) {
                        CockpitE7K.this.setNew.gCrankAngle += 0.0025F;
                        flag = true;
                    } else {
                        CockpitE7K.this.setNew.gCrankAngle = CockpitE7K.this.fm.CT.getGear();
                        CockpitE7K.this.setOld.gCrankAngle = CockpitE7K.this.fm.CT.getGear();
                    }
                }
                if (CockpitE7K.this.setNew.gCrankAngle > (CockpitE7K.this.fm.CT.getGear() + 0.005F)) {
                    if (Math.abs(CockpitE7K.this.setNew.gCrankAngle - CockpitE7K.this.fm.CT.getGear()) < 0.33F) {
                        CockpitE7K.this.setNew.gCrankAngle -= 0.0025F;
                        flag = true;
                    } else {
                        CockpitE7K.this.setNew.gCrankAngle = CockpitE7K.this.fm.CT.getGear();
                        CockpitE7K.this.setOld.gCrankAngle = CockpitE7K.this.fm.CT.getGear();
                    }
                }
                if (flag != this.sfxPlaying) {
                    if (flag) {
                        CockpitE7K.this.sfxStart(16);
                    } else {
                        CockpitE7K.this.sfxStop(16);
                    }
                    this.sfxPlaying = flag;
                }
            }
            return true;
        }

        boolean sfxPlaying;

        Interpolater() {
            this.sfxPlaying = false;
        }
    }

    private class Variables {

        float throttle;
        float prop;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;
        float gCrankAngle;

        private Variables() {
            this.gCrankAngle = 0.0F;
        }

    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        if (this.isFocused()) {
            this.leave();
            super.doFocusLeave();
        }
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 31");
        Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookpilot = HookPilot.current;
        if (hookpilot.isPadlock()) {
            hookpilot.stopPadlock();
        }
        hookpilot.doAim(true);
        hookpilot.setSimpleUse(true);
        hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
        this.mesh.chunkVisible("SuperReticle", true);
        this.mesh.chunkVisible("truba", false);
    }

    private void leave() {
        if (this.bEntered) {
            Main3D.cur3D().aircraftHotKeys.setEnableChangeFov(true);
            CmdEnv.top().exec("fov " + this.saveFov);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            hookpilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
            hookpilot.setSimpleUse(false);
            boolean flag = HotKeyEnv.isEnabled("aircraftView");
            HotKeyEnv.enable("PanView", flag);
            HotKeyEnv.enable("SnapView", flag);
            this.bEntered = false;
            this.mesh.chunkVisible("SuperReticle", false);
            this.mesh.chunkVisible("truba", true);
        }
    }

    public void destroy() {
        this.leave();
        super.destroy();
    }

    public void doToggleAim(boolean flag) {
        if (this.isFocused() && (this.isToggleAim() != flag)) {
            if (flag) {
                this.enter();
            } else {
                this.leave();
            }
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

    public CockpitE7K() {
//        super("3DO/Cockpit/I-16/CockpitE7K.him", "i16");
        super("3DO/Cockpit/E7K/hier.him", "i16");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.bEntered = false;
        this.cockpitNightMats = (new String[] { "prib_one", "prib_one_dd", "prib_two", "prib_two_dd", "prib_three", "prib_three_dd", "prib_four", "prib_four_dd", "shkala", "oxigen" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Stick", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Ped_Base", 0.0F, -this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalL", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("PedalR", 0.0F, this.fm.CT.getRudder() * 15F, 0.0F);
        this.mesh.chunkSetAngles("Fire1", 0.0F, -20F * (this.fm.CT.WeaponControl[0] ? 1.0F : 0.0F), 0.0F);
        this.mesh.chunkSetAngles("Fire2", 0.0F, -20F * (this.fm.CT.WeaponControl[1] ? 1.0F : 0.0F), 0.0F);
        this.mesh.chunkSetAngles("Thtl", 30F - (57F * this.interp(this.setNew.throttle, this.setOld.throttle, f)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Prop", (this.interp(this.setNew.prop, this.setOld.prop, f) * -57F) + 30F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Thtl_Rod", -30F + (57F * this.interp(this.setNew.throttle, this.setOld.throttle, f)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Prop_Rod", (this.interp(this.setNew.prop, this.setOld.prop, f) * 57F) - 30F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Gear_Crank", (15840F * this.interp(this.setNew.gCrankAngle, this.setOld.gCrankAngle, f)) % 360F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1a", 0.0F, this.cvt(this.fm.Or.getTangage(), -40F, 40F, -40F, 40F), 0.0F);
        this.mesh.chunkSetAngles("zAzimuth1b", 0.0F, -90F - this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("zGas1a", 0.0F, this.floatindex(this.cvt(this.fm.M.fuel, 0.0F, 190F, 0.0F, 14F), CockpitE7K.fuelQuantityScale), 0.0F);
        this.mesh.chunkSetAngles("zSpeed1a", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 550F, 0.0F, 11F), CockpitE7K.speedometerScale), 0.0F);
        if (((this.fm.AS.astateCockpitState & 4) == 0) && ((this.fm.AS.astateCockpitState & 0x10) == 0)) {
            this.w.set(this.fm.getW());
            this.fm.Or.transform(this.w);
            this.mesh.chunkSetAngles("zTurn1a", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F);
            this.mesh.chunkSetAngles("zSlide1a", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, 24F, -24F), 0.0F);
            this.mesh.chunkSetAngles("zTOilIn1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 125F, 0.0F, 275F), 0.0F);
            this.mesh.chunkSetAngles("zTOilOut1a", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 125F, 0.0F, 275F), 0.0F);
            this.mesh.chunkSetAngles("zPressAir1a", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 10F, 0.0F, 275F), 0.0F);
        }
        this.mesh.chunkSetAngles("zVariometer1a", 0.0F, this.cvt(this.setNew.vspeed, -30F, 30F, -180F, 180F), 0.0F);
        if (((this.fm.AS.astateCockpitState & 8) == 0) && ((this.fm.AS.astateCockpitState & 0x20) == 0)) {
            this.mesh.chunkSetAngles("zRPS1a", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 2400F, 0.0F, 13F), CockpitE7K.engineRPMScale), 0.0F);
            this.mesh.chunkSetAngles("zManifold1a", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.4F, 2.133F, 0.0F, 334.286F), 0.0F);
            this.mesh.chunkSetAngles("zTCil1a", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, -88F), 0.0F);
        }
        this.mesh.chunkSetAngles("zClock1a", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zClock1b", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zPressOil1a", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, 270F), 0.0F);
        this.mesh.chunkVisible("Z_Red1", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_Red2", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("Z_Green1", this.fm.CT.getGear() == 1.0F);
        this.mesh.chunkVisible("Z_Green2", this.fm.CT.getGear() == 1.0F);
    }

    public void reflectCockpitState() {
        if (((this.fm.AS.astateCockpitState & 4) != 0) || ((this.fm.AS.astateCockpitState & 0x10) != 0)) {
            this.mesh.chunkVisible("pribors1", false);
            this.mesh.chunkVisible("pribors1_dd", true);
            this.mesh.chunkVisible("zSpeed1a", false);
            this.mesh.chunkVisible("zAlt1a", false);
            this.mesh.chunkVisible("zAlt1b", false);
            this.mesh.chunkVisible("zPressOil1a", false);
            this.mesh.chunkVisible("zVariometer1a", false);
        }
        if (((this.fm.AS.astateCockpitState & 8) != 0) || ((this.fm.AS.astateCockpitState & 0x20) != 0)) {
            this.mesh.chunkVisible("pribors2", false);
            this.mesh.chunkVisible("pribors2_dd", true);
            this.mesh.chunkVisible("zAzimuth1a", false);
            this.mesh.chunkVisible("zAzimuth1b", false);
            this.mesh.chunkVisible("zManifold1a", false);
            this.mesh.chunkVisible("zGas1a", false);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Holes1_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("Z_Holes2_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
    }

    protected void reflectPlaneMats() {
        this.mesh.chunkVisible("ritedoor", false);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private boolean            bNeedSetUp;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[]  = { 0.0F, 0.0F, 18F, 44F, 74.5F, 106F, 136.3F, 169.5F, 207.5F, 245F, 287.5F, 330F };
    private static final float fuelQuantityScale[] = { 0.0F, 38.5F, 74.5F, 98.5F, 122F, 143F, 163F, 182.5F, 203F, 221F, 239.5F, 256F, 274F, 295F, 295F, 295F };
    private static final float engineRPMScale[]    = { 0.0F, 16F, 18F, 59.5F, 100.5F, 135.5F, 166.5F, 198.5F, 227F, 255F, 281.5F, 307F, 317F, 327F };
    private Point3d            tmpP;
    private Vector3d           tmpV;
    private float              saveFov;
    private boolean            bEntered;

}
