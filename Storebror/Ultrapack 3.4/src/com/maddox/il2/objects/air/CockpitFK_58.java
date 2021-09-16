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
import com.maddox.sound.ReverbFXRoom;

public class CockpitFK_58 extends CockpitPilot {
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
            if (CockpitFK_58.this.fm != null) {
                CockpitFK_58.this.setTmp = CockpitFK_58.this.setOld;
                CockpitFK_58.this.setOld = CockpitFK_58.this.setNew;
                CockpitFK_58.this.setNew = CockpitFK_58.this.setTmp;
                CockpitFK_58.this.setNew.throttle = ((10F * CockpitFK_58.this.setOld.throttle) + CockpitFK_58.this.fm.CT.PowerControl) / 11F;
                CockpitFK_58.this.setNew.prop = ((8F * CockpitFK_58.this.setOld.prop) + CockpitFK_58.this.fm.CT.getStepControl()) / 9F;
                CockpitFK_58.this.setNew.altimeter = CockpitFK_58.this.fm.getAltitude();
                if (Math.abs(CockpitFK_58.this.fm.Or.getKren()) < 45F) {
                    CockpitFK_58.this.setNew.azimuth = ((35F * CockpitFK_58.this.setOld.azimuth) + -CockpitFK_58.this.fm.Or.getYaw()) / 36F;
                }
                if ((CockpitFK_58.this.setOld.azimuth > 270F) && (CockpitFK_58.this.setNew.azimuth < 90F)) {
                    CockpitFK_58.this.setOld.azimuth -= 360F;
                }
                if ((CockpitFK_58.this.setOld.azimuth < 90F) && (CockpitFK_58.this.setNew.azimuth > 270F)) {
                    CockpitFK_58.this.setOld.azimuth += 360F;
                }
                CockpitFK_58.this.setNew.waypointAzimuth = ((10F * CockpitFK_58.this.setOld.waypointAzimuth) + (CockpitFK_58.this.waypointAzimuth() - CockpitFK_58.this.setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
                CockpitFK_58.this.setNew.vspeed = ((199F * CockpitFK_58.this.setOld.vspeed) + CockpitFK_58.this.fm.getVertSpeed()) / 200F;
            }
            return true;
        }

        Interpolater() {
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

    public CockpitFK_58() {
        super("3DO/Cockpit/FK-58-P/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "Arrows_Segment", "Indicators_2", "Indicators_3", "Indicators_4", "Indicators_5", "Indicators_6", "Indicators_7" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.63F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkVisible("XLampCoolant1", this.fm.EI.engines[0].tOilOut > this.fm.EI.engines[0].tOilCritMax);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -10800F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, -this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, -1080F), 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), CockpitFK_58.speedometerScale), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 25F, -25F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, this.cvt(this.getBall(7D), -7F, 7F, -13F, 13F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank9", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.03015F, -0.03015F);
        this.mesh.chunkSetLocate("Z_TurnBank3", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Climb1", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitFK_58.variometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 504F), 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 400F, 0.0F, 267F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 1.693189F, 0.0F, 343.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Carbair1", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -50F, 50F, -50F, 50F), 0.0F);
        float f1 = this.fm.EI.engines[0].getRPM();
        f1 = 2.5F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
        this.mesh.chunkSetAngles("Z_Suction1", 0.0F, this.cvt(f1, 0.0F, 10F, 0.0F, 306F), 0.0F);
        this.mesh.chunkSetAngles("Z_Coolant1", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 40F, 160F, -65F, 65F), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2", 0.0F, 722F * this.fm.CT.getTrimRudderControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_Trim3", 0.0F, -722F * this.fm.CT.getTrimElevatorControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_gear_lever", 45F * this.fm.CT.GearControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_flaps_lever", 45F * this.fm.CT.FlapsControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Rad_lever", 0.0F, -75F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Z_Rad_rod", 0.0F, 75F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, 66.81F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 0.0F, 66.5F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal1", 0.0F, -20F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal2", 0.0F, 20F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal1", 0.0F, 20F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal2", 0.0F, -20F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 20F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 16F, 0.0F);
        this.mesh.chunkSetAngles("Z_ColumnCable1", 0.0F, this.pictElev * 16F, 0.0F);
        this.mesh.chunkVisible("XLampGearUpL", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearUpR", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearDownL", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearDownR", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.rgear);
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("Z_Ignition", 0.0F, this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 90F), 0.0F);
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
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
        this.mesh.chunkVisible("XGlassDamage2", true);
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
