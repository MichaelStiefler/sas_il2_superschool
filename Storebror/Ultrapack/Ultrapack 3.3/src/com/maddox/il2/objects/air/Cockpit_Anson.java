package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class Cockpit_Anson extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (Cockpit_Anson.this.fm != null) {
                Cockpit_Anson.this.setTmp = Cockpit_Anson.this.setOld;
                Cockpit_Anson.this.setOld = Cockpit_Anson.this.setNew;
                Cockpit_Anson.this.setNew = Cockpit_Anson.this.setTmp;
                Cockpit_Anson.this.setNew.throttle1 = 0.85F * Cockpit_Anson.this.setOld.throttle1 + Cockpit_Anson.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
                Cockpit_Anson.this.setNew.throttle2 = 0.85F * Cockpit_Anson.this.setOld.throttle2 + Cockpit_Anson.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
                Cockpit_Anson.this.setNew.prop1 = 0.85F * Cockpit_Anson.this.setOld.prop1 + Cockpit_Anson.this.fm.EI.engines[0].getControlProp() * 0.15F;
                Cockpit_Anson.this.setNew.prop2 = 0.85F * Cockpit_Anson.this.setOld.prop2 + Cockpit_Anson.this.fm.EI.engines[1].getControlProp() * 0.15F;
                Cockpit_Anson.this.setNew.altimeter = Cockpit_Anson.this.fm.getAltitude();
                Cockpit_Anson.this.setNew.azimuth.setDeg(Cockpit_Anson.this.setOld.azimuth.getDeg(1.0F), Cockpit_Anson.this.fm.Or.azimut());
                float f = Cockpit_Anson.this.waypointAzimuth();
                Cockpit_Anson.this.setNew.waypointDeviation.setDeg(Cockpit_Anson.this.setOld.waypointDeviation.getDeg(0.1F), f - Cockpit_Anson.this.setOld.azimuth.getDeg(1.0F));
                Cockpit_Anson.this.setNew.waypointAzimuth.setDeg(Cockpit_Anson.this.setOld.waypointAzimuth.getDeg(0.1F), f - Cockpit_Anson.this.setOld.azimuth.getDeg(1.0F) - 90F);
                if ((Cockpit_Anson.this.fm.AS.astateCockpitState & 0x40) == 0) Cockpit_Anson.this.setNew.vspeed = (199F * Cockpit_Anson.this.setOld.vspeed + Cockpit_Anson.this.fm.getVertSpeed()) / 200F;
                else Cockpit_Anson.this.setNew.vspeed = (1990F * Cockpit_Anson.this.setOld.vspeed + Cockpit_Anson.this.fm.getVertSpeed()) / 2000F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      prop1;
        float      prop2;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        AnglesFork waypointDeviation;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.waypointDeviation = new AnglesFork();
        }

        Variables(Variables variables) {
            this();
        }
    }

    public Cockpit_Anson() {
        super("3DO/Cockpit/Anson-P/hier.him", "bf109");
        this.setOld = new Variables(null);
        this.setNew = new Variables(null);
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictFlap = 0.0F;
        this.pictGear = 0.0F;
        this.cockpitNightMats = new String[] { "prib_one_fin", "prib_two", "prib_three", "panel", "gauges2", "prib_one_fin_damage", "prib_two_damage", "prib_three_damage", "gauges2_damage", "PEICES1", "PEICES2", "PEICES3" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    public void reflectWorldToInstruments(float f) {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("Z_RDF", 0.0F, 90F + this.setNew.waypointAzimuth.getDeg(f * 0.02F), 0.0F);
        this.mesh.chunkVisible("XLampGearUpL", this.fm.CT.getGear() == 0.0F && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearUpR", this.fm.CT.getGear() == 0.0F && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearDownL", this.fm.CT.getGear() == 1.0F && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearDownR", this.fm.CT.getGear() == 1.0F && this.fm.Gears.rgear);
        this.mesh.chunkSetAngles("Z_Trim1", 0.0F, 161F * this.fm.CT.getTrimAileronControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2", 0.0F, 332F * this.fm.CT.getTrimRudderControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim3", 0.0F, 722F * this.fm.CT.getTrimElevatorControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", 0.0F, -75.5F * (this.pictFlap = 0.85F * this.pictFlap + 0.15F * this.fm.CT.FlapsControl), 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", 0.0F, -75.5F * (this.pictGear = 0.85F * this.pictGear + 0.15F * this.fm.CT.GearControl), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, 90F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", 0.0F, 90F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 0.0F, 100F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", 0.0F, 100F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 0.0F, 90F * this.fm.EI.engines[0].getControlMix(), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2", 0.0F, 90F * this.fm.EI.engines[1].getControlMix(), 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, -10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 8F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 57F, 0.0F);
        this.mesh.chunkSetAngles("Z_Brake", 0.0F, -25F * this.fm.CT.getBrake(), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeed()), 0.0F, 180.0555F, 0.0F, 35F), speedometerScaleFAF), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, -48F, 48F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, 35F, -35F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0275F, -0.0275F);
        this.mesh.chunkSetLocate("Z_TurnBank4", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Climb1", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), variometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 800F, 3500F, 0.0F, 8F), rpmScale), 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 800F, 3500F, 0.0F, 8F), rpmScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", 0.0F, 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 800F, 0.0F, 77F));
        this.mesh.chunkSetAngles("Z_Fuel2", 0.0F, 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 800F, 0.0F, 77F));
        this.mesh.chunkSetAngles("Z_Oil1", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 100F, 0.0F, 274F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil2", 0.0F, this.cvt(this.fm.EI.engines[1].tOilOut, 40F, 100F, 0.0F, 274F), 0.0F);
        float f1 = 0.5F * this.fm.EI.engines[0].getRPM() + 0.5F * this.fm.EI.engines[1].getRPM();
        f1 = 2.5F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
        this.mesh.chunkSetAngles("Z_Suction", 0.0F, this.cvt(f1, 0.0F, 10F, 0.0F, 302F), 0.0F);
        this.mesh.chunkSetAngles("Z_AirTemp", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -17.8F, 60F, 0.0F, -109.5F), 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("XHullDamage3", true);
            this.mesh.chunkVisible("XHullDamage2", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("XGlassDamage3", true);
        this.mesh.chunkVisible("XGlassDamage2", true);
        if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("XHullDamage2", true);
        this.mesh.chunkVisible("XGlassDamage1", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
        this.mesh.chunkVisible("XGlassDamage3", true);
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("XHullDamage3", true);
        this.mesh.chunkVisible("XGlassDamage1", true);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictFlap;
    private float              pictGear;
    private static final float speedometerScaleFAF[] = { 0.0F, 0.0F, 1.0F, 3F, 7.5F, 34.5F, 46F, 63F, 76F, 94F, 112.5F, 131F, 150F, 168.5F, 187F, 203F, 222F, 242.5F, 258.5F, 277F, 297F, 315.5F, 340F, 360F, 376.5F, 392F, 407F, 423.5F, 442F, 459F, 476F,
            492.5F, 513F, 534.5F, 552F, 569.5F };
    private static final float variometerScale[]     = { -158F, -110F, -63.5F, -29F, 0.0F, 29F, 63.5F, 110F, 158F };
    private static final float rpmScale[]            = { 0.0F, 10F, 75F, 126.5F, 179.5F, 232F, 284.5F, 313.5F };
    static {
        Property.set(Cockpit_Anson.class, "normZNs", new float[] { 0.55F, 0.55F, 0.75F, 0.75F });
    }
}
