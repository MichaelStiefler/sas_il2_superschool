package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitVampire extends CockpitPilot {
    private class Variables {

        float      throttle;
        float      prop;
        float      mix;
        float      stage;
        float      altimeter;
        float      vspeed;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitVampire.this.fm != null) {
                CockpitVampire.this.setTmp = CockpitVampire.this.setOld;
                CockpitVampire.this.setOld = CockpitVampire.this.setNew;
                CockpitVampire.this.setNew = CockpitVampire.this.setTmp;
                CockpitVampire.this.setNew.throttle = (0.85F * CockpitVampire.this.setOld.throttle) + (CockpitVampire.this.fm.CT.PowerControl * 0.15F);
                CockpitVampire.this.setNew.prop = (0.85F * CockpitVampire.this.setOld.prop) + (CockpitVampire.this.fm.CT.getStepControl() * 0.15F);
                CockpitVampire.this.setNew.stage = (0.85F * CockpitVampire.this.setOld.stage) + (CockpitVampire.this.fm.EI.engines[0].getControlCompressor() * 0.15F);
                CockpitVampire.this.setNew.mix = (0.85F * CockpitVampire.this.setOld.mix) + (CockpitVampire.this.fm.EI.engines[0].getControlMix() * 0.15F);
                CockpitVampire.this.setNew.altimeter = CockpitVampire.this.fm.getAltitude();
                CockpitVampire.this.setNew.azimuth.setDeg(CockpitVampire.this.setOld.azimuth.getDeg(1.0F), CockpitVampire.this.fm.Or.azimut());
                CockpitVampire.this.setNew.vspeed = ((199F * CockpitVampire.this.setOld.vspeed) + CockpitVampire.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitVampire() {
        super("3DO/Cockpit/Vampire/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.cockpitNightMats = (new String[] { "GagePanel1", "GagePanel2", "GagePanel3", "GagePanel4", "GagePanel5", "GagePanel6", "GagePanel7", "Glass", "needles", "radio1" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Z_RightPedal", 15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -15F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 40F * this.setNew.throttle, 0.0F, 0.0F);
        this.resetYPRmodifier();
        float f1 = this.fm.EI.engines[0].getStage();
        if ((f1 > 0.0F) && (f1 < 7F)) {
            f1 = 0.0345F;
        } else {
            f1 = -0.05475F;
        }
        Cockpit.xyz[2] = f1;
        this.mesh.chunkSetLocate("Z_EngShutOff", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Column", (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 16F, 0.0F, -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 20F);
        if ((this.fm.CT.GearControl == 0.0F) && (this.fm.CT.getGear() != 0.0F)) {
            f1 = 40F;
        } else if ((this.fm.CT.GearControl == 1.0F) && (this.fm.CT.getGear() != 1.0F)) {
            f1 = 20F;
        } else {
            f1 = 0.0F;
        }
        this.mesh.chunkSetAngles("Z_Gear1", f1, 0.0F, 0.0F);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[0]) {
            Cockpit.xyz[2] = -0.0029F;
        }
        this.mesh.chunkSetLocate("Z_DropTank", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 1126.541F, 0.0F, 14F), CockpitVampire.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        f1 = this.fm.EI.engines[0].getPowerOutput();
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt((float) Math.sqrt(f1), 0.0F, 1.0F, -59.5F, 223F), 0.0F, 0.0F);
        f1 = this.cvt(this.fm.M.fuel, 0.0F, 1000F, 0.0F, 270F);
        if (f1 < 45F) {
            f1 = this.cvt(f1, 0.0F, 45F, -58F, 45F);
        }
        f1 += 58F;
        this.mesh.chunkSetAngles("Z_Fuel2", f1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 28F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 1100F, 0.0F, 322F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 289F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 150F, 0.0F, 116.75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 150F, 0.0F, 116.75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitVampire.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oxypres1", 142.5F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", 90F + this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", this.setNew.waypointAzimuth.getDeg(0.1F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.w.z, -0.23562F, 0.23562F, 22.5F, -22.5F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.getBall(7D), -7F, 7F, -15F, 15F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3a", this.fm.Or.getKren(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, 0.0F, this.cvt(this.fm.Or.getTangage(), -45F, 45F, 1.5F, -1.5F));
        this.mesh.chunkSetAngles("Z_Pres1", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, 0.0F, 320F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Ny1", this.cvt(this.fm.getOverload(), -4F, 12F, -80.5F, 241.5F), 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_GearGreen1", this.fm.CT.getGear() > 0.95F);
        this.mesh.chunkVisible("Z_GearRed1", (this.fm.CT.getGear() < 0.05F) || !this.fm.Gears.lgear || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("Z_LampFuelL", this.fm.M.fuel < 500F);
        this.mesh.chunkVisible("Z_LampFuelR", this.fm.M.fuel < 500F);
        this.mesh.chunkVisible("Z_LampFuelCf", this.fm.M.fuel < 125F);
        this.mesh.chunkVisible("Z_Trim1", Math.abs(this.fm.CT.getTrimElevatorControl()) < 0.05F);
        this.mesh.chunkVisible("Z_FireLamp", this.fm.AS.astateEngineStates[0] > 2);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("Pricel_D0", false);
            this.mesh.chunkVisible("Pricel_D1", true);
            this.mesh.chunkVisible("Pricel1_D0", false);
            this.mesh.chunkVisible("Pricel1_D1", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_RETICLE1", false);
            for (int i = 1; i < 7; i++) {
                this.mesh.chunkVisible("Z_Z_AIMMARK" + i, false);
            }

            this.mesh.chunkVisible("Z_Z_MASK", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XHullDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XHullDamage2", true);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
        this.retoggleLight();
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
    private static final float speedometerScale[] = { 0.0F, 42F, 65.5F, 88.5F, 111.3F, 134F, 156.5F, 181F, 205F, 227F, 249.4F, 271.7F, 294F, 316.5F, 339.5F };
    private static final float variometerScale[]  = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };
}
