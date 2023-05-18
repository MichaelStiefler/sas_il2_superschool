package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitPotez630 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitPotez630.this.fm != null) {
                CockpitPotez630.this.setTmp = CockpitPotez630.this.setOld;
                CockpitPotez630.this.setOld = CockpitPotez630.this.setNew;
                CockpitPotez630.this.setNew = CockpitPotez630.this.setTmp;
                CockpitPotez630.this.setNew.throttle1 = 0.85F * CockpitPotez630.this.setOld.throttle1 + CockpitPotez630.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
                CockpitPotez630.this.setNew.throttle2 = 0.85F * CockpitPotez630.this.setOld.throttle2 + CockpitPotez630.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
                CockpitPotez630.this.setNew.altimeter = CockpitPotez630.this.fm.getAltitude();
                float f = CockpitPotez630.this.waypointAzimuth();
                CockpitPotez630.this.setNew.waypointAzimuth.setDeg(CockpitPotez630.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitPotez630.this.setOld.azimuth.getDeg(1.0F) + World.Rnd().nextFloat(-10F, 10F));
                CockpitPotez630.this.setNew.azimuth.setDeg(CockpitPotez630.this.setOld.azimuth.getDeg(1.0F), CockpitPotez630.this.fm.Or.azimut());
                CockpitPotez630.this.setNew.vspeed = (199F * CockpitPotez630.this.setOld.vspeed + CockpitPotez630.this.fm.getVertSpeed()) / 200F;
                CockpitPotez630.this.setNew.manifold = 0.8F * CockpitPotez630.this.setOld.manifold + 0.2F * CockpitPotez630.this.fm.EI.engines[0].getManifoldPressure();
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      manifold;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    public CockpitPotez630() {
        super("3DO/Cockpit/POTEZ-630P/hier1.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictTriE = 0.0F;
        this.pictTriR = 0.0F;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = new String[] { "Arrows_Segment", "Arrows2", "g_ind_01", "g_ind_02", "g_ind_03", "g_ind_04", "g_ind_05", "g_ind_06", "g_ind_07" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.45F);
    }

    public void reflectWorldToInstruments(float f) {
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.55F);
        this.mesh.chunkSetLocate("canopy1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Trim1", 720F * (this.pictTriE = 0.92F * this.pictTriE + 0.08F * this.fm.CT.getTrimElevatorControl()), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2", 160F * (this.pictTriR = 0.92F * this.pictTriR + 0.08F * this.fm.CT.getTrimRudderControl()), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 0.0F, (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 8F, 0.0F);
        this.mesh.chunkSetAngles("Z_Arone", 0.0F, (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 25F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", -70.45F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", -70.45F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Rudder", 16F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 40F * this.fm.CT.getBrake() * this.cvt(this.fm.CT.getRudder(), -0.5F, 1.0F, 0.0F, 1.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 40F * this.fm.CT.getBrake() * this.cvt(this.fm.CT.getRudder(), -1F, 0.5F, 1.0F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", 120F * this.fm.CT.FlapsControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Magneto", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 76F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 92.59998F, 740.7998F, 0.0F, 7F), speedometerScale), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0275F, -0.0275F);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.fm.Or.getKren(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.02705F, -0.02705F);
        this.mesh.chunkSetLocate("Z_TurnBank1a", Cockpit.xyz, Cockpit.ypr);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.w.z, -0.23562F, 0.23562F, -21F, 21F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(this.getBall(8D), -8F, 8F, 12F, -12F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Ext_Air_Temp", -this.floatindex(this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 233.09F, 333.09F, 0.0F, 5F), frAirTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.cvt(this.setNew.vspeed, -10F, 10F, 180F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 500F, 3500F, 0.0F, -315F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", this.cvt(this.fm.EI.engines[1].getRPM(), 500F, 3500F, 0.0F, -315F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.M.fuel, 50F, 554F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.32F, 0.0F, 1.0F, 0.0F, -278F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_HydPres1", this.fm.Gears.isHydroOperable() ? -116F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tWaterOut * this.fm.EI.engines[0].getPowerOutput() * 3.5F, 500F, 900F, 0.0F, -65F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", this.cvt(this.setNew.manifold, 0.200068F, 1.799932F, 164F, -164F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 30F, 110F, 0.0F, 4F), oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness(), 0.0F, 7.45F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_CylHead_Temp", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, -64F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FireExt_Quan", this.cvt(this.fm.EI.engines[0].getExtinguishers(), 0.0F, 11F, 0.0F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim1a", 27F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2a", 27F * this.fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, -720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkVisible("XLampGearUpR", this.fm.CT.getGear() < 0.01F || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearUpL", this.fm.CT.getGear() < 0.01F || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearDownR", this.fm.CT.getGear() > 0.99F && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearDownL", this.fm.CT.getGear() > 0.99F && this.fm.Gears.lgear);
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

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("XGlassDamage1", true);
        this.mesh.chunkVisible("XGlassDamage2", true);
        if ((this.fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("XGlassDamage4", true);
        this.mesh.chunkVisible("XGlassDamage5", true);
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("XGlassDamage3", true);
        if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("XGlassDamage6", true);
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
    private float              pictTriE;
    private float              pictTriR;
    private static final float speedometerScale[] = { 0.0F, 109.5F, 220.5F, 337F, 433.5F, 513F, 605.5F, 301.5F };
    private static final float frAirTempScale[]   = { 0.0F, 27.5F, 49.5F, 66F, 82F, 100F };
    private static final float oilTempScale[]     = { 0.0F, 43.5F, 95.5F, 172F, 262F };
    private Point3d            tmpP;
    private Vector3d           tmpV;
}
