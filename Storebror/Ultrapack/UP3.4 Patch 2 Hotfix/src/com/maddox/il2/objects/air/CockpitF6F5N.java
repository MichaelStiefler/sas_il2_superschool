package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitF6F5N extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitF6F5N.this.fm != null) {
                CockpitF6F5N.this.setTmp = CockpitF6F5N.this.setOld;
                CockpitF6F5N.this.setOld = CockpitF6F5N.this.setNew;
                CockpitF6F5N.this.setNew = CockpitF6F5N.this.setTmp;
                CockpitF6F5N.this.setNew.throttle = (0.85F * CockpitF6F5N.this.setOld.throttle) + (CockpitF6F5N.this.fm.CT.PowerControl * 0.15F);
                CockpitF6F5N.this.setNew.prop = (0.85F * CockpitF6F5N.this.setOld.prop) + (CockpitF6F5N.this.fm.CT.getStepControl() * 0.15F);
                CockpitF6F5N.this.setNew.mix = (0.85F * CockpitF6F5N.this.setOld.mix) + (CockpitF6F5N.this.fm.EI.engines[0].getControlMix() * 0.15F);
                CockpitF6F5N.this.setNew.altimeter = CockpitF6F5N.this.fm.getAltitude();
                float f = CockpitF6F5N.this.waypointAzimuth(10F);
                CockpitF6F5N.this.setNew.azimuth.setDeg(CockpitF6F5N.this.setOld.azimuth.getDeg(1.0F), CockpitF6F5N.this.fm.Or.azimut());
                CockpitF6F5N.this.setNew.waypointAzimuth.setDeg(CockpitF6F5N.this.setOld.waypointAzimuth.getDeg(0.1F), f);
                CockpitF6F5N.this.setNew.vspeed = ((199F * CockpitF6F5N.this.setOld.vspeed) + CockpitF6F5N.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      prop;
        float      mix;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    protected void setCameraOffset() {
        this.cameraCenter.add(0.035D, 0.0D, 0.0D);
    }

    public CockpitF6F5N() {
        super("3DO/Cockpit/F6F-5N/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.cockpitNightMats = (new String[] { "Image11_Damage", "Image11", "Image12_Damage", "Image12", "Image14", "Image27_Damage", "Image27", "Image09" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
        this.printCompassHeading = true;
    }

    public void reflectWorldToInstruments(float delta) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.reflectPlaneToModel();
            this.bNeedSetUp = false;
        }
        if (F6F.bChangedPit) {
            this.reflectPlaneToModel();
            F6F.bChangedPit = false;
        }

        TypeRadarAPSCarrier apsc = (TypeRadarAPSCarrier) this.aircraft();
        if (apsc.getRadarAPS().hasMessages()) {
            this.mesh.chunkSetAngles("RadarSWGun", 0.0F, 0.0F, 60F);
        } else {
            this.mesh.chunkSetAngles("RadarSWGun", 0.0F, 0.0F, 0.0F);
        }
        if (apsc.getRadarAPS().hasPower()) {
            this.mesh.chunkVisible("RadarScreen", true);
            this.mesh.chunkVisible("RadarScreenOFF", false);
            this.mesh.chunkSetAngles("RadarSWOn", 0.0F, 30F, 0.0F);
            apsc.getRadarAPS().radarScanCockpit(this.mesh, 10, 4);
        } else {
            this.mesh.chunkVisible("RadarScreen", false);
            this.mesh.chunkVisible("RadarScreenOFF", true);
            this.mesh.chunkSetAngles("RadarSWOn", 0.0F, 0.0F, 0.0F);
            apsc.getRadarAPS().clearRadarMarks(this.mesh, 10, 4);
        }

        float f = this.fm.CT.getWing();
        this.mesh.chunkSetAngles("WingLMid_D0", 100F * f, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("WingRMid_D0", -100F * f, 0.0F, 0.0F);
        f = -50F * this.fm.CT.getFlap();
        this.mesh.chunkSetAngles("Flap01_D0", 0.0F, f, 0.0F);
        this.mesh.chunkSetAngles("Flap02_D0", 0.0F, f, 0.0F);
        this.mesh.chunkSetAngles("Flap03_D0", 0.0F, f, 0.0F);
        this.mesh.chunkSetAngles("Flap04_D0", 0.0F, f, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.625F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Trim2", 180F * this.fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim3", 180F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", 90F * this.fm.CT.FlapsControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", 70F * this.fm.CT.GearControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, 50F * this.interp(this.setNew.throttle, this.setOld.throttle, delta), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 0.0F, 82F * this.interp(this.setNew.prop, this.setOld.prop, delta), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 0.0F, 50F * this.fm.EI.engines[0].getControlMix(), 0.0F);
        this.mesh.chunkSetAngles("Pedal_L", 0.0F, -17F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Pedal1_L", 0.0F, (17F * this.fm.CT.getRudder()) - (this.fm.CT.getBrake() * 15F), 0.0F);
        this.mesh.chunkSetAngles("Pedal2_L", 0.0F, (17F * this.fm.CT.getRudder()) - (this.fm.CT.getBrake() * 15F), 0.0F);
        this.mesh.chunkSetAngles("Pedal_R", 0.0F, 17F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Pedal1_R", 0.0F, (-17F * this.fm.CT.getRudder()) - (this.fm.CT.getBrake() * 15F), 0.0F);
        this.mesh.chunkSetAngles("Pedal2_R", 0.0F, (-17F * this.fm.CT.getRudder()) - (this.fm.CT.getBrake() * 15F), 0.0F);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 7F, 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger1", 0.0F, 25F - (25F * this.fm.EI.engines[0].getControlCompressor()), 0.0F);
        this.mesh.chunkSetAngles("Z_CowlFlap1", 0.0F, -70F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Z_OilCooler1", 0.0F, -70F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, delta), 0.0F, 9144F, 0.0F, 1080F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, delta), 0.0F, 9144F, 0.0F, 10800F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 963.0397F, 0.0F, 13F), CockpitF6F5N.speedometerScale), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.w.z, -0.23562F, 0.23562F, 30F, -30F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.getBall(7D), -7F, 7F, 16F, -16F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", -this.fm.Or.getKren(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.025F, -0.025F);
        this.mesh.chunkSetLocate("Z_TurnBank4", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitF6F5N.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", this.setNew.azimuth.getDeg(delta), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 270F + this.setNew.waypointAzimuth.getDeg(delta * 0.1F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", this.setNew.azimuth.getDeg(delta), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 278F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.EI.engines[0].tWaterOut, 70F, 350F, 0.0F, 76.15F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 100F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold1", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 343.75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oxy1", 120F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Coolant1", this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 160F, 0.0F, 120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPres1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 295F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AmmoCtr1", this.cvt(this.aircraft().getGunByHookName("_MGUN01").countBullets(), -500F, 1200F, -103F, 235.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AmmoCtr2", this.cvt(this.aircraft().getGunByHookName("_MGUN02").countBullets(), -500F, 1200F, -103F, 235.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres", this.cvt(this.fm.M.fuel, 0.0F, 700F, -5F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPres", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FlapInd1", this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, 60F), 0.0F, 0.0F);
        if (this.fm.Gears.lgear) {
            this.mesh.chunkSetAngles("Z_GearInd1", 0.0F, this.cvt(this.fm.CT.getGearL(), 0.0F, 1.0F, 0.0F, 65F), 0.0F);
        }
        if (this.fm.Gears.rgear) {
            this.mesh.chunkSetAngles("Z_GearInd2", 0.0F, this.cvt(this.fm.CT.getGearR(), 0.0F, 1.0F, 0.0F, -65F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_GearInd3", 0.0F, this.cvt(this.fm.CT.getGearC(), 0.0F, 1.0F, 0.0F, 80F), 0.0F);
        this.mesh.chunkSetAngles("Z_AirPres", this.cvt(12.4F, 0.0F, 20F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_HydroPres", this.cvt(this.fm.Gears.isHydroOperable() ? 7.5F : 0.0F, 0.0F, 20F, 0.0F, 180F), 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("Pricel_D0", false);
            this.mesh.chunkVisible("Pricel_D1", true);
        }
        this.fm.AS.getClass();
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("Z_Hour1", false);
            this.mesh.chunkVisible("Z_Minute1", false);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_Climb1", false);
            this.mesh.chunkVisible("Z_Fuel1", false);
            this.mesh.chunkVisible("Z_Temp1", false);
            this.mesh.chunkVisible("Z_OilPres", false);
            this.mesh.chunkVisible("Z_FuelPres", false);
        }
        this.fm.AS.getClass();
        this.fm.AS.getClass();
        this.fm.AS.getClass();
        this.fm.AS.getClass();
    }

    protected void reflectPlaneMats() {
        HierMesh h = this.aircraft().hierMesh();
        Mat m = h.material(h.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", m);
        m = h.material(h.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", m);
        m = h.material(h.materialFind("Gloss1D2o"));
        this.mesh.materialReplace("Gloss1D2o", m);
        m = h.material(h.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", m);
        m = h.material(h.materialFind("Matt1D1o"));
        this.mesh.materialReplace("Matt1D1o", m);
    }

    protected void reflectPlaneToModel() {
        HierMesh h = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLIn_D0", h.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", h.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", h.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingLIn_D3", h.isChunkVisible("WingLIn_D3"));
        this.mesh.chunkVisible("WingRIn_D0", h.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", h.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", h.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("WingRIn_D3", h.isChunkVisible("WingRIn_D3"));
        this.mesh.chunkVisible("WingLMid_D0", h.isChunkVisible("WingLMid_D0"));
        this.mesh.chunkVisible("WingLMid_D1", h.isChunkVisible("WingLMid_D1"));
        this.mesh.chunkVisible("WingLMid_D2", h.isChunkVisible("WingLMid_D2"));
        this.mesh.chunkVisible("WingLMid_D3", h.isChunkVisible("WingLMid_D3"));
        this.mesh.chunkVisible("WingRMid_D0", h.isChunkVisible("WingRMid_D0"));
        this.mesh.chunkVisible("WingRMid_D1", h.isChunkVisible("WingRMid_D1"));
        this.mesh.chunkVisible("WingRMid_D2", h.isChunkVisible("WingRMid_D2"));
        this.mesh.chunkVisible("WingRMid_D3", h.isChunkVisible("WingRMid_D3"));
        this.mesh.chunkVisible("Flap01_D0", h.isChunkVisible("Flap01_D0"));
        this.mesh.chunkVisible("Flap02_D0", h.isChunkVisible("Flap02_D0"));
        this.mesh.chunkVisible("Flap03_D0", h.isChunkVisible("Flap03_D0"));
        this.mesh.chunkVisible("Flap04_D0", h.isChunkVisible("Flap04_D0"));
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
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 17F, 56.5F, 107.5F, 157F, 204F, 220.5F, 238.5F, 256.5F, 274.5F, 293F, 311F, 330F, 342F };
    private static final float variometerScale[]  = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };

    static {
        Property.set(CockpitF6F5N.class, "normZNs", new float[] { 0.7F, 0.7F, 0.94F, 0.7F });
    }

}
