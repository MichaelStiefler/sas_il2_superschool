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

public class CockpitMARTIN_Maryland extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitMARTIN_Maryland.this.fm != null) {
                CockpitMARTIN_Maryland.this.setTmp = CockpitMARTIN_Maryland.this.setOld;
                CockpitMARTIN_Maryland.this.setOld = CockpitMARTIN_Maryland.this.setNew;
                CockpitMARTIN_Maryland.this.setNew = CockpitMARTIN_Maryland.this.setTmp;
                CockpitMARTIN_Maryland.this.setNew.throttle1 = (0.9F * CockpitMARTIN_Maryland.this.setOld.throttle1) + (0.1F * CockpitMARTIN_Maryland.this.fm.EI.engines[0].getControlThrottle());
                CockpitMARTIN_Maryland.this.setNew.prop1 = (0.9F * CockpitMARTIN_Maryland.this.setOld.prop1) + (0.1F * CockpitMARTIN_Maryland.this.fm.EI.engines[0].getControlProp());
                CockpitMARTIN_Maryland.this.setNew.mix1 = (0.8F * CockpitMARTIN_Maryland.this.setOld.mix1) + (0.2F * CockpitMARTIN_Maryland.this.fm.EI.engines[0].getControlMix());
                CockpitMARTIN_Maryland.this.setNew.throttle2 = (0.9F * CockpitMARTIN_Maryland.this.setOld.throttle2) + (0.1F * CockpitMARTIN_Maryland.this.fm.EI.engines[1].getControlThrottle());
                CockpitMARTIN_Maryland.this.setNew.prop2 = (0.9F * CockpitMARTIN_Maryland.this.setOld.prop2) + (0.1F * CockpitMARTIN_Maryland.this.fm.EI.engines[1].getControlProp());
                CockpitMARTIN_Maryland.this.setNew.mix2 = (0.8F * CockpitMARTIN_Maryland.this.setOld.mix2) + (0.2F * CockpitMARTIN_Maryland.this.fm.EI.engines[1].getControlMix());
                CockpitMARTIN_Maryland.this.setNew.man1 = (0.92F * CockpitMARTIN_Maryland.this.setOld.man1) + (0.08F * CockpitMARTIN_Maryland.this.fm.EI.engines[0].getManifoldPressure());
                CockpitMARTIN_Maryland.this.setNew.man2 = (0.92F * CockpitMARTIN_Maryland.this.setOld.man2) + (0.08F * CockpitMARTIN_Maryland.this.fm.EI.engines[1].getManifoldPressure());
                CockpitMARTIN_Maryland.this.setNew.altimeter = CockpitMARTIN_Maryland.this.fm.getAltitude();
                CockpitMARTIN_Maryland.this.setNew.azimuth.setDeg(CockpitMARTIN_Maryland.this.setOld.azimuth.getDeg(1.0F), CockpitMARTIN_Maryland.this.fm.Or.azimut());
                CockpitMARTIN_Maryland.this.setNew.vspeed = ((199F * CockpitMARTIN_Maryland.this.setOld.vspeed) + CockpitMARTIN_Maryland.this.fm.getVertSpeed()) / 200F;
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
        float      mix1;
        float      mix2;
        float      man1;
        float      man2;
        float      altimeter;
        AnglesFork azimuth;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
        }

    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0.0F;
        }
        waypoint.getP(this.tmpP);
        this.tmpV.sub(this.tmpP, this.fm.Loc);
        float f;
        for (f = (float) (57.295779513082323D * Math.atan2(-this.tmpV.y, this.tmpV.x)); f <= -180F; f += 180F) {
            ;
        }
        for (; f > 180F; f -= 180F) {
            ;
        }
        return f;
    }

    public CockpitMARTIN_Maryland() {
        super("3DO/Cockpit/M167/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bNeedSetUp = true;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "256-10_Gauges6", "256-16_Gauges7", "256-5_Gauges1", "256-6_Gauges2", "256-7_Gauges3", "256-8_Gauges4", "256-9_Gauges5", "256-18", "256-21", "256-22" });
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
        this.mesh.chunkSetAngles("Z_Throtle1", 0.0F, -70F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", 0.0F, -70F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 0.0F, -70F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", 0.0F, -70F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 0.0F, -47F * this.interp(this.setNew.mix1, this.setOld.mix1, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2", 0.0F, -47F * this.interp(this.setNew.mix2, this.setOld.mix2, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger1", 0.0F, 73F - (73F * this.fm.EI.engines[0].getControlCompressor()), 0.0F);
        this.mesh.chunkSetAngles("Z_Supercharger2", 0.0F, 73F - (73F * this.fm.EI.engines[1].getControlCompressor()), 0.0F);
        this.mesh.chunkSetAngles("Z_BombBay1", 0.0F, 40F * this.fm.CT.BayDoorControl, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = -0.1F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_LeftPedal1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = 0.1F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_RightPedal1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Column1", 0.0F, 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 8.8F);
        this.mesh.chunkSetAngles("Z_Column2", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 72.5F, 0.0F);
        this.mesh.chunkVisible("XLampGearUpL", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearUpR", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearDownL", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearDownR", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.rgear);
        this.mesh.chunkSetAngles("Z_Altimeter1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 804.6721F, 0.0F, 10F), CockpitMARTIN_Maryland.speedometerScale), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 22F, -22F), 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", 0.0F, this.cvt(this.getBall(8D), -8F, 8F, -16F, 16F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_Climb1", 0.0F, this.cvt(this.setNew.vspeed, -10.159F, 10.159F, -180F, 180F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_RPM1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 5000F, 0.0F, 305F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_RPM2", 0.0F, this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 5000F, 0.0F, 305F), 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel1", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 1390F, 0.0F, 70.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres1", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.3F, 0.0F, 301F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres2", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.3F, 0.0F, 301F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 87.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", 0.0F, this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 350F, 0.0F, 87.5F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.cvt(this.setNew.man1, 0.3386378F, 1.693189F, 0.0F, 285.5F), 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.cvt(this.setNew.man1, 0.3386378F, World.Rnd().nextFloat(1.0F, 2.0F), 0.0F, 285.5F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Pres2", 0.0F, this.cvt(this.setNew.man2, 0.3386378F, 1.693189F, 0.0F, 285.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", 0.0F, this.cvt(this.fm.EI.engines[0].tOilOut, 50F, 150F, 0.0F, 77F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oil2", 0.0F, this.cvt(this.fm.EI.engines[1].tOilOut, 50F, 150F, 0.0F, 77F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 302.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres2", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness()), 0.0F, 7.45F, 0.0F, 302.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_CarbIn1", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -15F, 55F, -35F, 35F), 0.0F);
        this.mesh.chunkSetAngles("Z_CarbIn2", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -15F, 55F, -35F, 35F), 0.0F);
        this.mesh.chunkSetAngles("Z_Hydro1", 0.0F, this.cvt(this.fm.Gears.isHydroOperable() ? 0.8F : 0.0F, 0.0F, 1.0F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_OxyPres1", 0.0F, 228F, 0.0F);
        this.mesh.chunkSetAngles("Z_OxyQ1", 0.0F, 279F, 0.0F);
        this.mesh.chunkSetAngles("Z_AH1", 0.0F, this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, -0.025F, 0.025F);
        this.mesh.chunkSetLocate("Z_AH2", Cockpit.xyz, Cockpit.ypr);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
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

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 16.5F, 79.5F, 143F, 206.5F, 229.5F, 251F, 272.5F, 294F, 316F, 339.5F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

}
