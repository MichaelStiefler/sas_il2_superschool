package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitP_43 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitP_43.this.fm != null) {
                CockpitP_43.this.setTmp = CockpitP_43.this.setOld;
                CockpitP_43.this.setOld = CockpitP_43.this.setNew;
                CockpitP_43.this.setNew = CockpitP_43.this.setTmp;
                CockpitP_43.this.setNew.throttle = ((10F * CockpitP_43.this.setOld.throttle) + CockpitP_43.this.fm.CT.PowerControl) / 11F;
                CockpitP_43.this.setNew.prop = ((8F * CockpitP_43.this.setOld.prop) + CockpitP_43.this.fm.EI.engines[0].getControlProp()) / 9F;
                CockpitP_43.this.setNew.altimeter = CockpitP_43.this.fm.getAltitude();
                if (Math.abs(CockpitP_43.this.fm.Or.getKren()) < 45F) {
                    CockpitP_43.this.setNew.azimuth = ((35F * CockpitP_43.this.setOld.azimuth) + -CockpitP_43.this.fm.Or.getYaw()) / 36F;
                }
                if ((CockpitP_43.this.setOld.azimuth > 270F) && (CockpitP_43.this.setNew.azimuth < 90F)) {
                    CockpitP_43.this.setOld.azimuth -= 360F;
                }
                if ((CockpitP_43.this.setOld.azimuth < 90F) && (CockpitP_43.this.setNew.azimuth > 270F)) {
                    CockpitP_43.this.setOld.azimuth += 360F;
                }
                CockpitP_43.this.setNew.waypointAzimuth = ((10F * CockpitP_43.this.setOld.waypointAzimuth) + (CockpitP_43.this.waypointAzimuth() - CockpitP_43.this.setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
                CockpitP_43.this.setNew.vspeed = ((199F * CockpitP_43.this.setOld.vspeed) + CockpitP_43.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
        }
    }

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

    public CockpitP_43() {
        super("3DO/Cockpit/P-43/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "Arrows_Segment", "Indicators_2", "Indicators_3", "Indicators_4", "Indicators_5" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.58F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, 50F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 0.0F, 50F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F);
        this.mesh.chunkSetAngles("Z_PedaL", 0.0F, -20F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_PedaR", 0.0F, 20F * this.fm.CT.getRudder(), 0.0F);
        this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl);
        this.mesh.chunkSetAngles("Stick", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 7F);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F), 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), CockpitP_43.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_Turn1", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 25F, -25F), 0.0F);
        this.mesh.chunkSetAngles("Z_Ball1", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -13F, 13F), 0.0F);
        this.mesh.chunkSetAngles("Z_AH1", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.03015F, -0.03015F);
        this.mesh.chunkSetLocate("Z_AH2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Climb1", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitP_43.variometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 504F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 600F, 0.0F, 267F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 1.71F, 0.0F, 343.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_OilPres", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 4F, 0.0F, -180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Carbair1", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -50F, 50F, -50F, 50F), 0.0F);
        float f1 = this.fm.EI.engines[0].getRPM();
        f1 = 2.5F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
        this.mesh.chunkSetAngles("Z_Suction1", 0.0F, this.cvt(f1, 0.0F, 10F, 0.0F, 306F), 0.0F);
        this.mesh.chunkSetAngles("Z_Coolant1", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 40F, 160F, -65F, 65F), 0.0F);
        this.mesh.chunkVisible("XLampGearUpL", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearUpR", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearDownL", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearDownR", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.rgear);
        this.resetYPRmodifier();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Interior"));
        this.mesh.materialReplace("Interior", mat);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
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
    private Point3d            tmpP;
    private Vector3d           tmpV;

}
