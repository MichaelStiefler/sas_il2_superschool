package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitCa_309 extends CockpitPilot {
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
        float      radioalt;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.waypointDeviation = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitCa_309.this.fm != null) {
                CockpitCa_309.this.setTmp = CockpitCa_309.this.setOld;
                CockpitCa_309.this.setOld = CockpitCa_309.this.setNew;
                CockpitCa_309.this.setNew = CockpitCa_309.this.setTmp;
                CockpitCa_309.this.reflectPlaneToModel();
                CockpitCa_309.this.setNew.throttle1 = 0.85F * CockpitCa_309.this.setOld.throttle1 + CockpitCa_309.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
                CockpitCa_309.this.setNew.throttle2 = 0.85F * CockpitCa_309.this.setOld.throttle2 + CockpitCa_309.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
                CockpitCa_309.this.setNew.prop1 = 0.85F * CockpitCa_309.this.setOld.prop1 + CockpitCa_309.this.fm.EI.engines[0].getControlProp() * 0.15F;
                CockpitCa_309.this.setNew.prop2 = 0.85F * CockpitCa_309.this.setOld.prop2 + CockpitCa_309.this.fm.EI.engines[1].getControlProp() * 0.15F;
                CockpitCa_309.this.setNew.altimeter = CockpitCa_309.this.fm.getAltitude();
                float f = CockpitCa_309.this.waypointAzimuth();
                CockpitCa_309.this.setNew.azimuth.setDeg(CockpitCa_309.this.setOld.azimuth.getDeg(1.0F), CockpitCa_309.this.fm.Or.azimut());
                CockpitCa_309.this.setNew.waypointAzimuth.setDeg(CockpitCa_309.this.setOld.waypointAzimuth.getDeg(1.0F), f);
                CockpitCa_309.this.setNew.waypointDeviation.setDeg(CockpitCa_309.this.setOld.waypointDeviation.getDeg(0.1F), f - CockpitCa_309.this.setOld.azimuth.getDeg(1.0F) + World.Rnd().nextFloat(-10F, 10F));
                if ((CockpitCa_309.this.fm.AS.astateCockpitState & 0x40) == 0) CockpitCa_309.this.setNew.vspeed = (199F * CockpitCa_309.this.setOld.vspeed + CockpitCa_309.this.fm.getVertSpeed()) / 200F;
                else CockpitCa_309.this.setNew.vspeed = (1990F * CockpitCa_309.this.setOld.vspeed + CockpitCa_309.this.fm.getVertSpeed()) / 2000F;
                CockpitCa_309.this.setNew.radioalt = 0.9F * CockpitCa_309.this.setOld.radioalt
                        + 0.1F * (CockpitCa_309.this.fm.getAltitude() - Landscape.HQ((float) CockpitCa_309.this.fm.Loc.x, (float) CockpitCa_309.this.fm.Loc.y) + World.Rnd().nextFloat(-10F, 10F));
            }
            return true;
        }

        Interpolater() {
        }
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) return 0.0F;
        waypoint.getP(this.tmpP);
        this.tmpV.sub(this.tmpP, this.fm.Loc);
        float f;
        for (f = (float) (57.295779513082323D * Math.atan2(-this.tmpV.y, this.tmpV.x)); f <= -180F; f += 180F)
            ;
        for (; f > 180F; f -= 180F)
            ;
        return f;
    }

    public CockpitCa_309() {
        super("3DO/Cockpit/Ca-309/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictFlap = 0.0F;
        this.pictGear = 0.0F;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = new String[] { "prib_one_fin", "prib_two", "prib_three", "prib_four", "gauges2", "prib_one_fin_damage", "prib_two_damage", "prib_three_damage", "prib_four_damage", "gauges2_damage", "PEICES1", "PEICES2" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
    }

    protected void reflectPlaneToModel() {
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("CF_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("CF_D0", true);
        super.doFocusLeave();
    }

    public void reflectWorldToInstruments(float f) {
        this.reflectPlaneMats();
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("Z_Trim1", 0.0F, 161F * this.fm.CT.getTrimAileronControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2", 0.0F, 332F * this.fm.CT.getTrimRudderControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim3", 0.0F, 722F * this.fm.CT.getTrimElevatorControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", 0.0F, -75.5F * (this.pictFlap = 0.85F * this.pictFlap + 0.15F * this.fm.CT.FlapsControl), 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", 0.0F, -75.5F * (this.pictGear = 0.85F * this.pictGear + 0.15F * this.fm.CT.GearControl), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, 90F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 0.0F, 100F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", 0.0F, 100F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 0.0F, 90F * this.fm.EI.engines[0].getControlMix(), 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, -10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 8F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 57F, 0.0F);
        this.mesh.chunkSetAngles("Z_Brake", 0.0F, -25F * this.fm.CT.getBrake(), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeed()), 0.0F, 143.0528F, 0.0F, 32F), speedometerScaleFAF), 0.0F);
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
        this.mesh.chunkSetAngles("Z_RPM1", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 1000F, 5000F, 0.0F, 8F), rpmScale), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) this.mesh.chunkSetAngles("Z_RPM2", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 1000F, 5000F, 0.0F, 8F), rpmScale), 0.0F);
        else this.mesh.chunkSetAngles("Z_RPM2", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 500F, 9800F, 0.0F, 8F), rpmScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 40F, 100F, 0.0F, 274F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) this.mesh.chunkSetAngles("Z_Oil2", 0.0F, this.cvt(this.fm.EI.engines[1].tOilOut, 40F, 100F, 0.0F, 274F), 0.0F);
        if (this.fm.getOverload() < 0.0F) {
            this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, this.cvt(1.0F + 0.05F * (this.fm.EI.engines[0].tOilOut / 10F) + this.fm.EI.engines[0].getRPM() / 800F + this.fm.getOverload() / 1.8F, 0.0F, 12.59F, 0.0F, 277F), 0.0F);
            this.mesh.chunkSetAngles("Z_Oilpres2", 0.0F, this.cvt(1.0F + 0.05F * (this.fm.EI.engines[1].tOilOut / 10F) + this.fm.EI.engines[1].getRPM() / 820F + this.fm.getOverload() / 1.8F, 0.0F, 12.59F, 0.0F, 277F), 0.0F);
        } else this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, this.cvt(1.0F + 0.05F * (this.fm.EI.engines[0].tOilOut / 10F) + this.fm.EI.engines[0].getRPM() / 800F + this.fm.getOverload() / 3.8F, 0.0F, 12.59F, 0.0F, 277F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres2", 0.0F, this.cvt(1.0F + 0.05F * (this.fm.EI.engines[1].tOilOut / 10F) + this.fm.EI.engines[1].getRPM() / 820F + this.fm.getOverload() / 3.8F, 0.0F, 12.59F, 0.0F, 277F), 0.0F);
        float f1 = 0.5F * this.fm.EI.engines[0].getRPM() + 0.5F * this.fm.EI.engines[1].getRPM();
        f1 = 2.5F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
        this.mesh.chunkSetAngles("Z_Suction", 0.0F, this.cvt(f1, 0.0F, 10F, 0.0F, 302F), 0.0F);
        this.mesh.chunkSetAngles("Z_AirTemp", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -17.8F, 60F, 0.0F, -109.5F), 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats1_D1", true);
            this.mesh.chunkVisible("Z_OilSplats2_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("Z_Fuel2", false);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Hour1", false);
            this.mesh.chunkVisible("Z_Minute1", false);
            this.mesh.chunkVisible("Z_Oilpres1", false);
            this.mesh.chunkVisible("Z_TurnBank1", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("XGlassDamage3", true);
        if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("XHullDamage2", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("XHullDamage3", true);
        this.retoggleLight();
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
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
    private float              pictFlap;
    private float              pictGear;
    private static final float speedometerScaleFAF[] = { 0.0F, 0.0F, 2.0F, 6F, 21F, 40F, 56F, 72.5F, 89.5F, 114F, 135.5F, 157F, 182.5F, 205F, 227.5F, 246.5F, 265.5F, 286F, 306F, 326F, 345.5F, 363F, 385F, 401F, 414.5F, 436.5F, 457F, 479F, 496.5F, 515F,
            539F, 559.5F, 576.5F };
    private static final float variometerScale[]     = { -158F, -110F, -63.5F, -29F, 0.0F, 29F, 63.5F, 110F, 158F };
    private static final float rpmScale[]            = { 0.0F, 20F, 54F, 99F, 151.5F, 195.5F, 249.25F, 284.5F, 313.75F };
    private Point3d            tmpP;
    private Vector3d           tmpV;
}
