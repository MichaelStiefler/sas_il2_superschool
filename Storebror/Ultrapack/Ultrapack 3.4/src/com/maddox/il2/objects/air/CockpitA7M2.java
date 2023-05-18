package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitA7M2 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitA7M2.this.fm != null) {
                CockpitA7M2.this.setTmp = CockpitA7M2.this.setOld;
                CockpitA7M2.this.setOld = CockpitA7M2.this.setNew;
                CockpitA7M2.this.setNew = CockpitA7M2.this.setTmp;
                CockpitA7M2.this.setNew.throttle = (0.85F * CockpitA7M2.this.setOld.throttle) + (CockpitA7M2.this.fm.CT.PowerControl * 0.15F);
                CockpitA7M2.this.setNew.prop = (0.85F * CockpitA7M2.this.setOld.prop) + (CockpitA7M2.this.fm.CT.getStepControl() * 0.15F);
                CockpitA7M2.this.setNew.altimeter = CockpitA7M2.this.fm.getAltitude();
                float f = CockpitA7M2.this.waypointAzimuth();
                CockpitA7M2.this.setNew.waypointAzimuth.setDeg(CockpitA7M2.this.setOld.waypointAzimuth.getDeg(0.1F), (f - CockpitA7M2.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                CockpitA7M2.this.setNew.azimuth.setDeg(CockpitA7M2.this.setOld.azimuth.getDeg(1.0F), CockpitA7M2.this.fm.Or.azimut());
                CockpitA7M2.this.setNew.vspeed = ((199F * CockpitA7M2.this.setOld.vspeed) + CockpitA7M2.this.fm.getVertSpeed()) / 200F;
                CockpitA7M2.this.setNew.manifold = (0.8F * CockpitA7M2.this.setOld.manifold) + (0.2F * CockpitA7M2.this.fm.EI.engines[0].getManifoldPressure());
                if (CockpitA7M2.this.cockpitDimControl) {
                    if (CockpitA7M2.this.setNew.dimPosition > 0.0F) {
                        CockpitA7M2.this.setNew.dimPosition = CockpitA7M2.this.setOld.dimPosition - 0.05F;
                    }
                } else if (CockpitA7M2.this.setNew.dimPosition < 1.0F) {
                    CockpitA7M2.this.setNew.dimPosition = CockpitA7M2.this.setOld.dimPosition + 0.05F;
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle;
        float      prop;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      manifold;
        float      dimPosition;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    public CockpitA7M2() {
        super("3DO/Cockpit/J2M5/hier_A7M2.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictMix = 0.0F;
        this.pictGear = 0.0F;
        this.pictMetl = 0.0F;
        this.pictTriE = 0.0F;
        this.pictTriR = 0.0F;
        this.pictSupc = 0.0F;
        this.pictBoox = 0.0F;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "Arrows_Segment", "D_g_ind_01", "D_g_ind_02", "D_g_ind_03", "D_g_ind_04", "D_g_ind_05", "D_g_ind_06", "D_g_ind_07", "g_ind_01", "g_ind_02", "g_ind_03", "g_ind_04", "g_ind_05", "g_ind_06", "g_ind_07" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Z_IronSight", 0.0F, -72F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.1F, 0.99F, 0.0F, 0.61F);
        this.mesh.chunkSetLocate("Canopy", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 0.07F);
        this.mesh.chunkSetLocate("Z_SightTint", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Trim1", 720F * (this.pictTriE = (0.92F * this.pictTriE) + (0.08F * this.fm.CT.getTrimElevatorControl())), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2", 160F * (this.pictTriR = (0.92F * this.pictTriR) + (0.08F * this.fm.CT.getTrimRudderControl())), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_CowlFlaps1", 175.5F * this.fm.EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
        float f1;
        if ((this.fm.CT.GearControl == 0.0F) && (this.fm.CT.getGear() != 0.0F)) {
            f1 = -32F;
        } else if ((this.fm.CT.GearControl == 1.0F) && (this.fm.CT.getGear() != 1.0F)) {
            f1 = 32F;
        } else {
            f1 = 0.0F;
        }
        this.mesh.chunkSetAngles("Z_Gear1", this.pictGear = (0.8F * this.pictGear) + (0.2F * f1), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 70.45F * this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 95F * this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 79F * (this.pictMix = (0.8F * this.pictMix) + (0.2F * this.fm.EI.engines[0].getControlMix())), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger1", -32F * (this.pictSupc = (0.91F * this.pictSupc) + (0.09F * this.fm.EI.engines[0].getControlCompressor())), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Boost", -32F * (this.pictBoox = (0.91F * this.pictBoox) + (0.09F * (this.fm.EI.engines[0].getControlAfterburner() ? 1.0F : 0.0F))), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Rudder", 16F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 40F * this.fm.CT.getBrake() * this.cvt(this.fm.CT.getRudder(), -0.5F, 1.0F, 0.0F, 1.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 40F * this.fm.CT.getBrake() * this.cvt(this.fm.CT.getRudder(), -1F, 0.5F, 1.0F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Columnbase", -18F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", -14F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trigger", this.fm.CT.saveWeaponControl[1] ? -16.5F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", -90F * this.fm.CT.FlapsControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilCooler", -175.5F * this.fm.EI.engines[0].getControlRadiator(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Magneto", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 76F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 92.59998F, 740.7998F, 0.0F, 7F), CockpitA7M2.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.fm.Or.getKren(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.02705F, -0.02705F);
        this.mesh.chunkSetLocate("Z_TurnBank1a", Cockpit.xyz, Cockpit.ypr);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.w.z, -0.23562F, 0.23562F, -21F, 21F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(this.getBall(8D), -8F, 8F, 12F, -12F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.cvt(this.setNew.vspeed, -10F, 10F, 180F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 500F, 3500F, 0.0F, -315F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.M.fuel, 50F, 403.2F, 0.0F, -240F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel2", this.cvt(this.fm.M.fuel, 0.0F, 43.2F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.32F, 0.0F, 1.0F, 0.0F, -278F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_HydPres1", this.fm.Gears.isHydroOperable() ? -116F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tWaterOut * this.fm.EI.engines[0].getPowerOutput() * 3.5F, 500F, 900F, 0.0F, -65F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", this.cvt(this.setNew.manifold, 0.200068F, 1.799932F, 164F, -164F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 30F, 110F, 0.0F, 4F), CockpitA7M2.oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Methanol", this.cvt(this.fm.M.nitro, 0.0F, 80F, 0.0F, -69F), 0.0F, 0.0F);
        f1 = 0.0F;
        if (this.fm.EI.engines[0].getControlAfterburner()) {
            f1 = 0.025F;
            if ((this.fm.EI.engines[0].getControlThrottle() > 1.0F) && (this.fm.M.nitro > 0.05F)) {
                f1 = 0.68F;
            }
        }
        this.pictMetl = (0.9F * this.pictMetl) + (0.1F * f1);
        this.mesh.chunkSetAngles("Z_Methanol_Pres", this.cvt(this.pictMetl, 0.0F, 1.0F, 0.0F, -276F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_CylHead_Temp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, -64F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FireExt_Quan", this.cvt(this.fm.EI.engines[0].getExtinguishers(), 0.0F, 11F, 0.0F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Ext_Air_Temp", -this.floatindex(this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 233.09F, 333.09F, 0.0F, 5F), CockpitA7M2.frAirTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim1a", 27F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2a", 27F * this.fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, -720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkVisible("XLampGearUpR", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearUpL", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearDownR", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearDownL", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0.0F;
        }
        waypoint.getP(this.tmpP);
        this.tmpV.sub(this.tmpP, this.fm.Loc);
        float f = (float) (Math.toDegrees(Math.atan2(-this.tmpV.y, this.tmpV.x)));
        while (f <= -180F) {
            f += 360F;
        }
        while (f > 180F) {
            f -= 360F;
        }
        return f;
    }

    public void reflectCockpitState() {
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
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
    private float              pictMix;
    private float              pictGear;
    private float              pictMetl;
    private float              pictTriE;
    private float              pictTriR;
    private float              pictSupc;
    private float              pictBoox;
    private static final float speedometerScale[] = { 0.0F, 109.5F, 220.5F, 337F, 433.5F, 513F, 605.5F, 301.5F };
    private static final float frAirTempScale[]   = { 0.0F, 27.5F, 49.5F, 66F, 82F, 100F };
    private static final float oilTempScale[]     = { 0.0F, 43.5F, 95.5F, 172F, 262F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

}
