package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitTBD1 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitTBD1.this.fm != null) {
                CockpitTBD1.this.setTmp = CockpitTBD1.this.setOld;
                CockpitTBD1.this.setOld = CockpitTBD1.this.setNew;
                CockpitTBD1.this.setNew = CockpitTBD1.this.setTmp;
                CockpitTBD1.this.setNew.throttle = (10F * CockpitTBD1.this.setOld.throttle + CockpitTBD1.this.fm.CT.PowerControl) / 11F;
                CockpitTBD1.this.setNew.altimeter = CockpitTBD1.this.fm.getAltitude();
                if (Math.abs(CockpitTBD1.this.fm.Or.getKren()) < 30F) CockpitTBD1.this.setNew.azimuth = (35F * CockpitTBD1.this.setOld.azimuth + -CockpitTBD1.this.fm.Or.getYaw()) / 36F;
                if (CockpitTBD1.this.setOld.azimuth > 270F && CockpitTBD1.this.setNew.azimuth < 90F) CockpitTBD1.this.setOld.azimuth -= 360F;
                if (CockpitTBD1.this.setOld.azimuth < 90F && CockpitTBD1.this.setNew.azimuth > 270F) CockpitTBD1.this.setOld.azimuth += 360F;
                CockpitTBD1.this.setNew.waypointAzimuth = (10F * CockpitTBD1.this.setOld.waypointAzimuth + (CockpitTBD1.this.waypointAzimuth() - CockpitTBD1.this.setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
                CockpitTBD1.this.setNew.vspeed = (199F * CockpitTBD1.this.setOld.vspeed + CockpitTBD1.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float throttle;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;

        private Variables() {
        }

    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Blister2_D0", false);
            this.aircraft().hierMesh().chunkVisible("Blister5_D0", false);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
            Point3d point3d = new Point3d();
            point3d.set(0.23D, 0.0D, -0.05D);
            hookpilot.setTubeSight(point3d);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) return;
        else {
            this.leave();
            this.aircraft().hierMesh().chunkVisible("Blister2_D0", true);
            this.aircraft().hierMesh().chunkVisible("Blister5_D0", true);
            super.doFocusLeave();
            return;
        }
    }

    private void enter() {
        this.saveFov = Main3D.FOVX;
        CmdEnv.top().exec("fov 31");
        (Main3D.cur3D()).aircraftHotKeys.setEnableChangeFov(false);
        HookPilot hookPilot = HookPilot.current;
        if (hookPilot.isPadlock())
          hookPilot.stopPadlock(); 
        hookPilot.doAim(true);
        hookPilot.setSimpleUse(true);
        hookPilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
        HotKeyEnv.enable("PanView", false);
        HotKeyEnv.enable("SnapView", false);
        this.bEntered = true;
        this.mesh.chunkVisible("SuperReticle", true);
    }

    private void leave() {
        if (!this.bEntered)
            return; 
          (Main3D.cur3D()).aircraftHotKeys.setEnableChangeFov(true);
          CmdEnv.top().exec("fov " + this.saveFov);
          HookPilot hookPilot = HookPilot.current;
          hookPilot.doAim(false);
          hookPilot.setSimpleAimOrient(0.0F, 0.0F, 0.0F);
          hookPilot.setSimpleUse(false);
          boolean bool = HotKeyEnv.isEnabled("aircraftView");
          HotKeyEnv.enable("PanView", bool);
          HotKeyEnv.enable("SnapView", bool);
          this.bEntered = false;
          this.mesh.chunkVisible("SuperReticle", false);
    }

    public void destroy() {
        this.leave();
        super.destroy();
    }

    public void doToggleAim(boolean flag) {
        if (!this.isFocused()) return;
        if (this.isToggleAim() == flag) return;
        if (flag) this.enter();
        else this.leave();
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

    public CockpitTBD1() {
        super("3DO/Cockpit/F2A-2/TBD.him", "bf109");
        this.gun = new Gun[1];
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.enteringAim = false;
        this.bEntered = false;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = new String[] { "F2ABoxes", "F2Acables", "F2Agauges", "F2Agauges1", "F2Agauges3", "F2AWindshields", "F2Azegary4" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.45F);
        this.limits6DoF = new float[] { 0.7F, 0.055F, -0.07F, 0.11F, 0.12F, -0.11F, 0.03F, -0.04F };
    }

    public void reflectWorldToInstruments(float f) {
        if (this.enteringAim) {
            HookPilot hookpilot = HookPilot.current;
            if (hookpilot.isAimReached()) {
                this.enteringAim = false;
                this.enter();
            } else if (!hookpilot.isAim()) this.enteringAim = false;
        }
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (this.gun[0] == null) this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN01");
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.725F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, 25F * (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl), 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, 0.0F, 10F * (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl));
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.pictAiler, -1F, 1.0F, -0.054F, 0.054F);
        this.mesh.chunkSetLocate("Z_ColumnR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Z_ColumnL", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, -0.0575F, 0.0575F);
        this.mesh.chunkSetLocate("Z_Pedal_L", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.CT.getRudder(), -1F, 1.0F, 0.0575F, -0.0575F);
        this.mesh.chunkSetLocate("Z_Pedal_R", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (this.fm.EI.engines[0].getStage() > 0 && this.fm.EI.engines[0].getStage() < 3) Cockpit.xyz[1] = 0.02825F;
        this.mesh.chunkSetLocate("Z_Starter", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Ign_Switch", 0.0F, -20F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle", 0.0F, 100F * (this.pictThtl = 0.9F * this.pictThtl + 0.1F * this.fm.EI.engines[0].getControlThrottle()), 0.0F);
        this.mesh.chunkSetAngles("Z_mixture", 0.0F, 91.66667F * (this.pictMix = 0.9F * this.pictMix + 0.1F * this.fm.EI.engines[0].getControlMix()), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.pictProp = 0.9F * this.pictProp + 0.1F * this.fm.EI.engines[0].getControlProp(), 0.0F, 1.0F, 0.0F, -0.035F);
        this.mesh.chunkSetLocate("Z_Pitch", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Booster_Lever", 0.0F, -5F * this.fm.EI.engines[0].getControlCompressor(), 0.0F);
        this.mesh.chunkSetAngles("Z_TL_lock", 0.0F, this.fm.Gears.bTailwheelLocked ? -30F : 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TL_wheel", 0.0F, 2240F * this.fm.CT.getArrestor(), 0.0F);
        if (this.fm.CT.FlapsControl == 0.0F && this.fm.CT.getFlap() != 0.0F) this.mesh.chunkSetAngles("Z_Flaps", 0.0F, 0.0F, 0.0F);
        else if (this.fm.CT.FlapsControl == 1.0F && this.fm.CT.getFlap() != 1.0F) this.mesh.chunkSetAngles("Z_Flaps", 0.0F, -70F, 0.0F);
        else this.mesh.chunkSetAngles("Z_Flaps", 0.0F, -35F, 0.0F);
        if (this.fm.CT.GearControl == 0.0F && this.fm.CT.getGear() != 0.0F) this.mesh.chunkSetAngles("Z_gearlever", 0.0F, 4F, 0.0F);
        else if (this.fm.CT.GearControl == 1.0F && this.fm.CT.getGear() != 1.0F) this.mesh.chunkSetAngles("Z_gearlever", 0.0F, -70F, 0.0F);
        else this.mesh.chunkSetAngles("Z_gearlever", 0.0F, -35F, 0.0F);
        this.mesh.chunkSetAngles("Z_Magn_Compas", 90F + this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass", 90F + this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.CT.getGear(), 0.0F, 1.0F, 0.0F, -0.135F);
        this.mesh.chunkSetLocate("Z_Gear", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, -0.04155F, 0.0211F);
        this.mesh.chunkSetLocate("Z_Flap", Cockpit.xyz, Cockpit.ypr);
        if (this.gun[0] != null && this.gun[0].haveBullets()) {
            this.mesh.chunkSetAngles("Z_Ammo_W1", 0.0F, -0.36F * this.gun[0].countBullets(), 0.0F);
            this.mesh.chunkSetAngles("Z_Ammo_W2", 0.0F, -3.6F * this.gun[0].countBullets(), 0.0F);
            this.mesh.chunkSetAngles("Z_Ammo_W3", 0.0F, -36F * this.gun[0].countBullets(), 0.0F);
            this.mesh.chunkSetAngles("Z_Ammo_W4", 0.0F, -0.36F * this.gun[0].countBullets(), 0.0F);
            this.mesh.chunkSetAngles("Z_Ammo_W5", 0.0F, -3.6F * this.gun[0].countBullets(), 0.0F);
            this.mesh.chunkSetAngles("Z_Ammo_W6", 0.0F, -36F * this.gun[0].countBullets(), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_TL_indi", 0.0F, 45F * this.fm.CT.getArrestor(), 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 1.693189F, 20F, 340F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Alt_Large", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Alt_Small", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 981.5598F, 0.0F, 53F), speedometerScale), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_Turn", this.cvt(this.w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Bank", this.cvt(this.getBall(8D), -8F, 8F, 12F, -12F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb", this.cvt(this.setNew.vspeed, -20F, 20F, -180F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Clock_Min", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Clock_H", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hor_Handle", -this.fm.Or.getKren(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.019F, -0.019F);
        this.mesh.chunkSetLocate("Z_Hor_Handle2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Temp_Handle", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 400F, 0.0F, 100F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp_Eng", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 170F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil_Eng", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilIn, 0.0F, 15F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel_Eng", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 10F * this.fm.EI.engines[0].getPowerOutput(), 0.0F, 20F, 0.0F, 180F), 0.0F, 0.0F);
        if (this.fm.EI.engines[0].getRPM() < 1000F) this.mesh.chunkSetAngles("Z_Tahometr_Eng", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 1000F, 0.0F, 90F), 0.0F, 0.0F);
        else this.mesh.chunkSetAngles("Z_Tahometr_Eng", this.cvt(this.fm.EI.engines[0].getRPM(), 1000F, 3500F, 90F, 540F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_carbmixtemp", this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 213.09F, 333.09F, -180F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel_1", this.cvt(this.fm.M.fuel, 0.0F, 504F, 0.0F, -272.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel_2", this.cvt(this.fm.M.fuel, 0.0F, 504F, 0.0F, -272.5F), 0.0F, 0.0F);
        if (this.fm.Gears.isHydroOperable()) this.mesh.chunkSetAngles("Z_hydropress", 133F, 0.0F, 0.0F);
        else this.mesh.chunkSetAngles("Z_hydropress", 1.0F, 0.0F, 0.0F);
        this.mesh.chunkVisible("XLampGear_1", !this.fm.Gears.lgear || !this.fm.Gears.lgear);
    }

    public void reflectCockpitState() {
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    private Gun                gun[];
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private Vector3f           w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictThtl;
    private float              pictMix;
    private float              pictProp;
    private boolean            bNeedSetUp;
    private boolean            enteringAim;
    private static final float speedometerScale[] = { 0.0F, 0.5F, 1.0F, 2.0F, 5.5F, 14F, 20F, 26F, 33.5F, 42F, 50.5F, 60.5F, 71.5F, 81.5F, 95.2F, 108.5F, 122.5F, 137F, 152F, 166.7F, 182F, 198F, 214.5F, 231F, 247.5F, 263.5F, 278.5F, 294F, 307F, 317F,
            330.5F, 343F, 355.5F, 367.5F, 379.5F, 391.5F, 404F, 417F, 430.5F, 444F, 458F, 473.5F, 487.5F, 503.5F, 519.5F, 535.5F, 552F, 569.5F, 586F, 602.5F, 619F, 631.5F, 643F, 648.5F };
    private float              saveFov;
    private boolean            bEntered;
    private Point3d            tmpP;
    private Vector3d           tmpV;

    static {
        Property.set(CockpitTBD1.class, "normZNs", new float[] { 1.26F, 1.12F, 1.15F, 1.12F });
    }
}
