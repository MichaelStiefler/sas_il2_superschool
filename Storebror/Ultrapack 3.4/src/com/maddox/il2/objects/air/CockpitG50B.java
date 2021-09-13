package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Time;

public class CockpitG50B extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitG50B.this.setTmp = CockpitG50B.this.setOld;
            CockpitG50B.this.setOld = CockpitG50B.this.setNew;
            CockpitG50B.this.setNew = CockpitG50B.this.setTmp;
            if (((CockpitG50B.this.fm.AS.astateCockpitState & 2) != 0) && (CockpitG50B.this.setNew.stbyPosition < 1.0F)) {
                CockpitG50B.this.delay--;
                if (CockpitG50B.this.delay <= 0) {
                    CockpitG50B.this.setNew.stbyPosition = CockpitG50B.this.setOld.stbyPosition + 0.03F;
                    CockpitG50B.this.setOld.stbyPosition = CockpitG50B.this.setNew.stbyPosition;
                    CockpitG50B.this.sightDamaged = true;
                }
            }
            CockpitG50B.this.setNew.throttle = ((10F * CockpitG50B.this.setOld.throttle) + CockpitG50B.this.fm.CT.PowerControl) / 11F;
            CockpitG50B.this.setNew.prop = ((10F * CockpitG50B.this.setOld.prop) + CockpitG50B.this.fm.EI.engines[0].getControlProp()) / 11F;
            CockpitG50B.this.setNew.mix = ((10F * CockpitG50B.this.setOld.mix) + CockpitG50B.this.fm.EI.engines[0].getControlMix()) / 11F;
            CockpitG50B.this.setNew.vspeed = ((199F * CockpitG50B.this.setOld.vspeed) + CockpitG50B.this.fm.getVertSpeed()) / 200F;
            CockpitG50B.this.setNew.azimuth = CockpitG50B.this.fm.Or.getYaw();
            if ((CockpitG50B.this.setOld.azimuth > 270F) && (CockpitG50B.this.setNew.azimuth < 90F)) {
                CockpitG50B.this.setOld.azimuth -= 360F;
            }
            if ((CockpitG50B.this.setOld.azimuth < 90F) && (CockpitG50B.this.setNew.azimuth > 270F)) {
                CockpitG50B.this.setOld.azimuth += 360F;
            }
            CockpitG50B.this.setNew.waypointAzimuth = ((10F * CockpitG50B.this.setOld.waypointAzimuth) + (CockpitG50B.this.waypointAzimuth(30F) - CockpitG50B.this.setOld.azimuth)) / 11F;
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float throttle;
        float prop;
        float mix;
        float azimuth;
        float waypointAzimuth;
        float vspeed;
        float stbyPosition;

        private Variables() {
        }
    }

    public CockpitG50B() {
        super("3DO/Cockpit/G50B/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.gun = new Gun[2];
        this.delay = 80;
        this.sightDamaged = false;
        this.cockpitNightMats = (new String[] { "texture07", "texture08", "texture09", "texture10", "texture11", "texture12", "texture15" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.gun[0] == null) {
            this.gun[0] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN01");
            this.gun[1] = ((Aircraft) this.fm.actor).getGunByHookName("_MGUN02");
        }
        this.mesh.chunkVisible("Z_GearRedL", (this.fm.CT.getGear() == 0.0F) || this.fm.Gears.isAnyDamaged());
        this.mesh.chunkVisible("Z_GearRedR", (this.fm.CT.getGear() == 0.0F) || this.fm.Gears.isAnyDamaged());
        this.mesh.chunkVisible("Z_GearGreenL", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_GearGreenR", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.rgear);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal1", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal2", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal3", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pedal4", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gear", 50F * (this.pictGear = (0.85F * this.pictGear) + (0.15F * this.fm.CT.GearControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flap", 50F * (this.pictFlap = (0.85F * this.pictFlap) + (0.15F * this.fm.CT.FlapsControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", -this.cvt(this.fm.getAltitude(), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", -this.cvt(this.fm.getAltitude(), 0.0F, 20000F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", -60F - this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", 0.0F, 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F));
        this.mesh.chunkSetAngles("Z_Minute1", 0.0F, 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F));
        this.mesh.chunkSetAngles("Z_Secund1", 0.0F, 0.0F, this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F));
        this.mesh.chunkSetAngles("Z_Speedometer1", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 600F, 0.0F, 12F), CockpitG50B.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 600F, 0.0F, 12F), CockpitG50B.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 70F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 70F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 70F * this.interp(this.setNew.mix, this.setOld.mix, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", -this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 316F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.getBall(8D), -8F, 8F, 9F, -9F), 0.0F, 0.0F);
        float f_0_;
        if (this.aircraft().isFMTrackMirror()) {
            f_0_ = this.aircraft().fmTrack().getCockpitAzimuthSpeed();
        } else {
            f_0_ = this.cvt((this.setNew.azimuth - this.setOld.azimuth) / Time.tickLenFs(), -9F, 9F, -24F, 24F);
            if (this.aircraft().fmTrack() != null) {
                this.aircraft().fmTrack().setCockpitAzimuthSpeed(f_0_);
            }
        }
        this.mesh.chunkSetAngles("Z_TurnBank2", f_0_, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.cvt(this.setNew.vspeed, -25F, 25F, 180F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.6F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 15F, 0.0F, -300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oiltemp1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 150F, 0.0F, -300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, -74F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prescockp1", -this.floatindex(this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.533288F, 1.33322F, 0.0F, 6F), CockpitG50B.manifoldScale), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.gun[0].countBullets(), 0.0F, 150F, 0.0F, 0.06025F);
        this.mesh.chunkSetLocate("Z_AmmoCounter1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = this.cvt(this.gun[1].countBullets(), 0.0F, 150F, 0.0F, 0.06025F);
        this.mesh.chunkSetLocate("Z_AmmoCounter2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_gunsight_rim", 50F * this.setNew.stbyPosition, 0.0F, 0.0F);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    public void doToggleAim(boolean bool) {
        super.doToggleAim(bool);
        if (bool && this.sightDamaged) {
            HookPilot hookpilot = HookPilot.current;
            hookpilot.doAim(true);
            hookpilot.setAim(new Point3d(-1.4D, 0.0D, 0.9583200216293335D));
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Front", false);
            this.mesh.chunkVisible("Front_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("gunsight_lense", false);
            this.mesh.chunkVisible("D_gunsight_lense", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        this.fm.AS.getClass();
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        this.fm.AS.getClass();
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        this.fm.AS.getClass();
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private float              pictAiler;
    private float              pictElev;
    private float              pictFlap;
    private float              pictGear;
    private Gun                gun[];
    private int                delay;
    private boolean            sightDamaged;
    private static final float speedometerScale[] = { 0.0F, 6F, 27.5F, 66F, 108F, 146.5F, 183.1F, 217.5F, 251F, 281.5F, 310.5F, 388F, 394F };
    private static final float manifoldScale[]    = { 0.0F, 56F, 111F, 166.5F, 220.5F, 276F, 327.5F };

}
