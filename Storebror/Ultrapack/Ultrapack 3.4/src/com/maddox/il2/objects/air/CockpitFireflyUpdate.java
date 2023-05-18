package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitFireflyUpdate extends CockpitPilot {
    private class Variables {

        float throttle;
        float prop;
        float altimeter;
        float azimuth;
        float vspeed;
        float waypointAzimuth;

        private Variables() {
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitFireflyUpdate.this.fm != null) {
                CockpitFireflyUpdate.this.setTmp = CockpitFireflyUpdate.this.setOld;
                CockpitFireflyUpdate.this.setOld = CockpitFireflyUpdate.this.setNew;
                CockpitFireflyUpdate.this.setNew = CockpitFireflyUpdate.this.setTmp;
                CockpitFireflyUpdate.this.setNew.throttle = ((10F * CockpitFireflyUpdate.this.setOld.throttle) + CockpitFireflyUpdate.this.fm.CT.PowerControl) / 11F;
                CockpitFireflyUpdate.this.setNew.prop = ((10F * CockpitFireflyUpdate.this.setOld.prop) + CockpitFireflyUpdate.this.fm.EI.engines[0].getControlProp()) / 11F;
                CockpitFireflyUpdate.this.setNew.altimeter = CockpitFireflyUpdate.this.fm.getAltitude();
                if (Math.abs(CockpitFireflyUpdate.this.fm.Or.getKren()) < 30F) {
                    CockpitFireflyUpdate.this.setNew.azimuth = ((35F * CockpitFireflyUpdate.this.setOld.azimuth) + CockpitFireflyUpdate.this.fm.Or.azimut()) / 36F;
                }
                if ((CockpitFireflyUpdate.this.setOld.azimuth > 270F) && (CockpitFireflyUpdate.this.setNew.azimuth < 90F)) {
                    CockpitFireflyUpdate.this.setOld.azimuth -= 360F;
                }
                if ((CockpitFireflyUpdate.this.setOld.azimuth < 90F) && (CockpitFireflyUpdate.this.setNew.azimuth > 270F)) {
                    CockpitFireflyUpdate.this.setOld.azimuth += 360F;
                }
                CockpitFireflyUpdate.this.setNew.waypointAzimuth = ((10F * CockpitFireflyUpdate.this.setOld.waypointAzimuth) + (CockpitFireflyUpdate.this.waypointAzimuth() - CockpitFireflyUpdate.this.setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
                CockpitFireflyUpdate.this.setNew.vspeed = ((199F * CockpitFireflyUpdate.this.setOld.vspeed) + CockpitFireflyUpdate.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitFireflyUpdate() {
        super("3DO/Cockpit/Firefly-P/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.cockpitNightMats = (new String[] { "Dials", "Compa", "BORT4", "BORT5" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.2F);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.5F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.arrestorControl, 0.01F, 0.99F, 0.0F, 0.03F);
        this.mesh.chunkVisible("XLampArrest", this.fm.CT.getArrestor() > 0.9F);
        this.mesh.chunkVisible("XLampGearUpL", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearUpR", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearUpC", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.cgear);
        this.mesh.chunkVisible("XLampGearDownL", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearDownR", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.rgear);
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("Z_BasePedal", 20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 0.0F, -20F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, 0.0F, -20F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_Columnbase", 16F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 45F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.pictAiler, -1F, 1.0F, -0.027F, 0.027F);
        this.mesh.chunkSetLocate("Z_Shlang01", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = -Cockpit.xyz[2];
        this.mesh.chunkSetLocate("Z_Shlang02", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("RUD", 0.0F, -75F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F);
        this.mesh.chunkSetAngles("PROP", 0.0F, -70F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F);
        this.mesh.chunkSetAngles("DIRECTOR", 0.0F, this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("COMPASS", 0.0F, this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("STRELKA_ALT1", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F));
        this.mesh.chunkSetAngles("STRELKA_ALT2", 0.0F, 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F));
        this.mesh.chunkSetAngles("STRELKA_BOOST", 0.0F, 0.0F, -this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.7242097F, 1.620528F, -111.5F, 221F));
        this.mesh.chunkSetAngles("STRELKA_FUEL", this.cvt(this.fm.M.fuel / 0.725F, 0.0F, 600F, 0.0F, 200F), 0.0F, 0.0F);
//        this.mesh.chunkSetAngles("STRELKA_RPM", 0.0F, 0.0F, -floatindex(cvt(fm.EI.engines[0].getRPM(), 1000F, 5000F, 2.0F, 10F), rpmScale));
        this.mesh.chunkSetAngles("STRELKA_RPM", 0.0F, 0.0F, -this.cvt(this.fm.EI.engines[0].getRPM() % 1000F, 0F, 1000F, 0F, 360F));
        this.mesh.chunkSetAngles("STRELKA_RPM2", 0.0F, 0.0F, -this.cvt(this.fm.EI.engines[0].getRPM() / 1000F, 0F, 10F, 0F, 360F));
        this.mesh.chunkSetAngles("STRELKA_OIL", 0.0F, 0.0F, -this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 100F, 0.0F, 270F));
        this.resetYPRmodifier();
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("STRELKA_DOWN", 0.0F, 0.0F, -this.cvt(((Tuple3f) (this.w)).z, -0.23562F, 0.23562F, -48F, 48F));
        this.mesh.chunkSetAngles("STRELKA_UP", 0.0F, 0.0F, -this.cvt(this.getBall(8D), -8F, 8F, 35F, -35F));
        this.mesh.chunkSetAngles("STRELKA_VL", 0.0F, 0.0F, -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeed()), 0.0F, 180.0555F, 0.0F, 35F), CockpitFireflyUpdate.speedometerScale));
        this.mesh.chunkSetAngles("STRELKA_VY", 0.0F, 0.0F, -this.floatindex(this.cvt(this.setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), CockpitFireflyUpdate.variometerScale));
        this.resetYPRmodifier();
    }

    protected void reflectPlaneMats() {
        this.mesh.materialReplace("Gloss1D0o", this.aircraft().hierMesh().material(this.aircraft().hierMesh().materialFind("Gloss1D0o")));
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
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
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 1.0F, 3F, 7.5F, 34.5F, 46F, 63F, 76F, 94F, 112.5F, 131F, 150F, 168.5F, 187F, 203F, 222F, 242.5F, 258.5F, 277F, 297F, 315.5F, 340F, 360F, 376.5F, 392F, 407F, 423.5F, 442F, 459F, 476F, 492.5F, 513F, 534.5F, 552F, 569.5F };
//    private static final float rpmScale[] = {
//        0.0F, 0.0F, 0.0F, 22F, 58F, 103.5F, 152.5F, 193.5F, 245F, 281.5F,
//        311.5F
//    };
    private static final float variometerScale[]  = { -158F, -110F, -63.5F, -29F, 0.0F, 29F, 63.5F, 110F, 158F };
}
