package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitSB2U extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitSB2U.this.fm != null) {
                CockpitSB2U.this.setTmp = CockpitSB2U.this.setOld;
                CockpitSB2U.this.setOld = CockpitSB2U.this.setNew;
                CockpitSB2U.this.setNew = CockpitSB2U.this.setTmp;
                CockpitSB2U.this.setNew.flaps = (0.9F * CockpitSB2U.this.setOld.flaps) + (0.1F * CockpitSB2U.this.fm.CT.FlapsControl);
                CockpitSB2U.this.setNew.gear = (0.7F * CockpitSB2U.this.setOld.gear) + (0.3F * CockpitSB2U.this.fm.CT.GearControl);
                CockpitSB2U.this.setNew.throttle = (0.8F * CockpitSB2U.this.setOld.throttle) + (0.2F * CockpitSB2U.this.fm.CT.PowerControl);
                CockpitSB2U.this.setNew.prop = (0.8F * CockpitSB2U.this.setOld.prop) + (0.2F * CockpitSB2U.this.fm.EI.engines[0].getControlProp());
                CockpitSB2U.this.setNew.mix = (0.8F * CockpitSB2U.this.setOld.mix) + (0.2F * CockpitSB2U.this.fm.EI.engines[0].getControlMix());
                CockpitSB2U.this.setNew.divebrake = (0.8F * CockpitSB2U.this.setOld.divebrake) + (0.2F * CockpitSB2U.this.fm.CT.AirBrakeControl);
                CockpitSB2U.this.setNew.man = (0.92F * CockpitSB2U.this.setOld.man) + (0.08F * CockpitSB2U.this.fm.EI.engines[0].getManifoldPressure());
                CockpitSB2U.this.setNew.altimeter = CockpitSB2U.this.fm.getAltitude();
                CockpitSB2U.this.setNew.azimuth.setDeg(CockpitSB2U.this.setOld.azimuth.getDeg(1.0F), CockpitSB2U.this.fm.Or.azimut());
                CockpitSB2U.this.setNew.vspeed = ((399F * CockpitSB2U.this.setOld.vspeed) + CockpitSB2U.this.fm.getVertSpeed()) / 400F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      flaps;
        float      gear;
        float      throttle;
        float      prop;
        float      mix;
        float      divebrake;
        float      altimeter;
        float      man;
        float      vspeed;
        AnglesFork azimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
        }
    }

    public CockpitSB2U() {
        super("3DO/Cockpit/SB2U/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bEntered = false;
        this.cockpitNightMats = (new String[] { "GagePanel1", "GagePanel1_D1", "GagePanel2", "GagePanel2_D1", "GagePanel3", "GagePanel3_D1", "GagePanel4", "GagePanel4_D1", "misc2" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void reflectWorldToInstruments(float f) {
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.7F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Trim1", 0.0F, 1444F * this.fm.CT.getTrimAileronControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2", 0.0F, 1444F * this.fm.CT.getTrimRudderControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim3", 0.0F, 1444F * this.fm.CT.getTrimElevatorControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", 0.0F, -77F * this.setNew.flaps, 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", 0.0F, -77F * this.setNew.gear, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, -40F * this.setNew.throttle, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 0.0F, -68F * this.setNew.prop, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 0.0F, -55F * this.setNew.mix, 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger1", 0.0F, -55F * this.fm.EI.engines[0].getControlCompressor(), 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, 20F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 20F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 8F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 8F, 0.0F);
        this.mesh.chunkSetAngles("Z_DiveBrake1", 0.0F, -77F * this.setNew.divebrake, 0.0F);
        this.mesh.chunkSetAngles("Z_CowlFlap1", 0.0F, -28F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        if ((this.fm.CT.Weapons[3] != null) && !this.fm.CT.Weapons[3][0].haveBullets()) {
            this.mesh.chunkSetAngles("Z_Bomb1", 0.0F, 35F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, -3600F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, -36000F), 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 925.9998F, 0.0F, 10F), CockpitSB2U.speedometerScale), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 22F, -22F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, this.cvt(this.getBall(6D), -6F, 6F, -16F, 16F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.025F, -0.025F);
        this.mesh.chunkSetLocate("Z_TurnBank4", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Climb1", 0.0F, this.setNew.vspeed >= 0.0F ? -this.floatindex(this.cvt(this.setNew.vspeed / 5.08F, 0.0F, 6F, 0.0F, 12F), CockpitSB2U.variometerScale) : this.floatindex(this.cvt(-this.setNew.vspeed / 5.08F, 0.0F, 6F, 0.0F, 12F), CockpitSB2U.variometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, -360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, -360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 400F, 0.0F, -87F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel2", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 400F, 0.0F, -87F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel3", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 400F, 0.0F, -87F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel4", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 400F, 0.0F, -87F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres1", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.3F, 0.0F, -180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, -74F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, -180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.cvt(this.setNew.man, 0.3386378F, 2.539784F, 0.0F, -344F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 200F, 0.0F, -180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, -301F), 0.0F);
        float f1 = this.fm.EI.engines[0].getRPM();
        f1 = 2.5F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
        this.mesh.chunkSetAngles("Z_Suction1", 0.0F, this.cvt(f1, 0.0F, 10F, 0.0F, -300F), 0.0F);
        if (this.fm.Gears.lgear) {
            this.resetYPRmodifier();
            Cockpit.xyz[0] = -0.133F * this.fm.CT.getGear();
            this.mesh.chunkSetLocate("Z_GearL1", Cockpit.xyz, Cockpit.ypr);
        }
        if (this.fm.Gears.rgear) {
            this.resetYPRmodifier();
            Cockpit.xyz[0] = -0.133F * this.fm.CT.getGear();
            this.mesh.chunkSetLocate("Z_GearR1", Cockpit.xyz, Cockpit.ypr);
        }
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -0.118F * this.fm.CT.getFlap();
        this.mesh.chunkSetLocate("Z_Flap1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_EnginePrim", 0.0F, this.fm.EI.engines[0].getStage() > 0 ? -36F : 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagSwitch", 0.0F, this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, -111F), 0.0F);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Mast", false);
            this.aircraft().hierMesh().chunkVisible("Wire", false);
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(false);
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
            this.aircraft().hierMesh().chunkVisible("Mast", true);
            this.aircraft().hierMesh().chunkVisible("Wire", true);
            super.doFocusLeave();
            return;
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
    }

    private void leave() {
        if (!this.bEntered) {
            return;
        } else {
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
            this.enter();
        } else {
            this.leave();
        }
    }

    public void reflectCockpitState() {
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
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 16.5F, 79.5F, 143F, 206.5F, 229.5F, 251F, 272.5F, 294F, 316F, 339.5F };
    private static final float variometerScale[]  = { 0.0F, 25F, 49.5F, 64F, 78.5F, 89.5F, 101F, 112F, 123F, 134.5F, 145.5F, 157F, 168F, 168F };
    private float              saveFov;
    private boolean            bEntered;

}
