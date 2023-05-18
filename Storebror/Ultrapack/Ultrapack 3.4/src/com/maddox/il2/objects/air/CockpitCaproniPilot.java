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
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;

public class CockpitCaproniPilot extends CockpitPilot {

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      throttle3;
        float      prop1;
        float      prop2;
        float      prop3;
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
            if (CockpitCaproniPilot.this.fm != null) {
                CockpitCaproniPilot.this.setTmp = CockpitCaproniPilot.this.setOld;
                CockpitCaproniPilot.this.setOld = CockpitCaproniPilot.this.setNew;
                CockpitCaproniPilot.this.setNew = CockpitCaproniPilot.this.setTmp;
                CockpitCaproniPilot.this.setNew.throttle1 = (0.85F * CockpitCaproniPilot.this.setOld.throttle1) + (CockpitCaproniPilot.this.fm.EI.engines[0].getControlThrottle() * 0.15F);
                CockpitCaproniPilot.this.setNew.prop1 = (0.85F * CockpitCaproniPilot.this.setOld.prop1) + (CockpitCaproniPilot.this.fm.EI.engines[0].getControlProp() * 0.15F);
                CockpitCaproniPilot.this.setNew.throttle2 = (0.85F * CockpitCaproniPilot.this.setOld.throttle2) + (CockpitCaproniPilot.this.fm.EI.engines[1].getControlThrottle() * 0.15F);
                CockpitCaproniPilot.this.setNew.prop2 = (0.85F * CockpitCaproniPilot.this.setOld.prop2) + (CockpitCaproniPilot.this.fm.EI.engines[1].getControlProp() * 0.15F);
                CockpitCaproniPilot.this.setNew.throttle3 = (0.85F * CockpitCaproniPilot.this.setOld.throttle3) + (CockpitCaproniPilot.this.fm.EI.engines[2].getControlThrottle() * 0.15F);
                CockpitCaproniPilot.this.setNew.prop3 = (0.85F * CockpitCaproniPilot.this.setOld.prop3) + (CockpitCaproniPilot.this.fm.EI.engines[2].getControlProp() * 0.15F);
                CockpitCaproniPilot.this.setNew.altimeter = CockpitCaproniPilot.this.fm.getAltitude();
                float f = CockpitCaproniPilot.this.waypointAzimuth();
                CockpitCaproniPilot.this.setNew.waypointAzimuth.setDeg(CockpitCaproniPilot.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitCaproniPilot.this.setOld.azimuth.getDeg(1.0F));
                CockpitCaproniPilot.this.setNew.azimuth.setDeg(CockpitCaproniPilot.this.setOld.azimuth.getDeg(1.0F), CockpitCaproniPilot.this.fm.Or.azimut());
                CockpitCaproniPilot.this.setNew.vspeed = (0.99F * CockpitCaproniPilot.this.setOld.vspeed) + (0.01F * CockpitCaproniPilot.this.fm.getVertSpeed());
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

    public CockpitCaproniPilot(String mesh, String name) {
        super(mesh, name);
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictBrake = 0.0F;
        this.pictFlap = 0.0F;
        this.pictManf1 = 1.0F;
        this.pictManf2 = 1.0F;
        this.pictManf3 = 1.0F;
        this.bNeedSetUp = true;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "01", "02", "03", "04", "05", "12", "20", "23", "24", "26", "27", "01_damage", "03_damage", "04_damage", "24_damage" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkVisible("XLampFuel1", this.fm.EI.engines[0].getRPM() < 300F);
        this.mesh.chunkVisible("XLampFuel2", this.fm.EI.engines[1].getRPM() < 300F);
        this.mesh.chunkSetAngles("Z_Columnbase", 12F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", 45F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column52R", 0.0F, 45F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)), 0.0F);
        this.mesh.chunkSetAngles("Z_ColumnSwitch", 20F * (this.pictBrake = (0.91F * this.pictBrake) + (0.09F * this.fm.CT.BrakeControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 62.72F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", 62.72F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ZThrottleR", 62.72F * this.interp(this.setNew.throttle3, this.setOld.throttle3, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", -20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 20F * this.fm.CT.getRudder(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zHour", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMinute", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSecond", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        float f1;
        if (Math.abs(this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.02F) {
            if ((this.fm.CT.FlapsControl - this.fm.CT.getFlap()) > 0.0F) {
                f1 = -0.0299F;
            } else {
                f1 = -0F;
            }
        } else {
            f1 = -0.0144F;
        }
        this.pictFlap = (0.8F * this.pictFlap) + (0.2F * f1);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.pictFlap;
        this.mesh.chunkSetLocate("Z_Flaps1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Trim1", -1000F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim2", 1000F * this.fm.CT.getTrimAileronControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Trim3", 90F * this.fm.CT.getTrimRudderControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 90F * this.setNew.prop1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", 90F * this.setNew.prop2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop3", 90F * this.setNew.prop3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("COMPASS_M", 90F + this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SHKALA_DIRECTOR", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", this.setNew.azimuth.getDeg(f) + this.setNew.waypointAzimuth.getDeg(f) + 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STREL_ALT_LONG", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STREL_ALT_SHORT", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 600F), 0.0F, 0.0F);
        this.pictManf1 = (0.91F * this.pictManf1) + (0.09F * this.fm.EI.engines[0].getManifoldPressure());
        f1 = this.pictManf1 - 1.0F;
        float f2 = 1.0F;
        if (f1 <= 0.0F) {
            f2 = -1F;
        }
        f1 = Math.abs(f1);
        this.mesh.chunkSetAngles("STRELKA_BOOST1", f2 * this.floatindex(this.cvt(f1, 0.0F, 1.792637F, 0.0F, 13F), CockpitCaproniPilot.boostScale), 0.0F, 0.0F);
        this.pictManf2 = (0.91F * this.pictManf2) + (0.09F * this.fm.EI.engines[1].getManifoldPressure());
        f1 = this.pictManf2 - 1.0F;
        f2 = 1.0F;
        if (f1 <= 0.0F) {
            f2 = -1F;
        }
        f1 = Math.abs(f1);
        this.mesh.chunkSetAngles("STRELKA_BOOST2", f2 * this.floatindex(this.cvt(f1, 0.0F, 1.792637F, 0.0F, 13F), CockpitCaproniPilot.boostScale), 0.0F, 0.0F);
        this.pictManf3 = (0.91F * this.pictManf3) + (0.09F * this.fm.EI.engines[2].getManifoldPressure());
        f1 = this.pictManf2 - 1.0F;
        f2 = 1.0F;
        if (f1 <= 0.0F) {
            f2 = -1F;
        }
        f1 = Math.abs(f1);
        this.mesh.chunkSetAngles("STRELKA_BOOST3", f2 * this.floatindex(this.cvt(f1, 0.0F, 1.792637F, 0.0F, 13F), CockpitCaproniPilot.boostScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_FUEL1", this.cvt(this.fm.M.fuel, 0.0F, 763F, 0.0F, 301F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELKA_FUEL2", this.cvt(this.fm.M.fuel, 0.0F, 763F, 0.0F, 301F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM_SHORT1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitCaproniPilot.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM_LONG1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 80F), CockpitCaproniPilot.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM_SHORT2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitCaproniPilot.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM_LONG2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4000F, 0.0F, 80F), CockpitCaproniPilot.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM_SHORT3", this.floatindex(this.cvt(this.fm.EI.engines[2].getRPM(), 0.0F, 4000F, 0.0F, 8F), CockpitCaproniPilot.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM_LONG3", this.floatindex(this.cvt(this.fm.EI.engines[2].getRPM(), 0.0F, 4000F, 0.0F, 80F), CockpitCaproniPilot.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMP_OIL1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 100F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMP_OIL2", this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 100F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMP_OIL3", this.cvt(this.fm.EI.engines[2].tOilOut, 0.0F, 100F, 0.0F, 266F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMP_RAD1", this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 14F), CockpitCaproniPilot.radScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMP_RAD2", this.floatindex(this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 140F, 0.0F, 14F), CockpitCaproniPilot.radScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TEMP_RAD3", this.floatindex(this.cvt(this.fm.EI.engines[2].tWaterOut, 0.0F, 140F, 0.0F, 14F), CockpitCaproniPilot.radScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELK_TURN_UP", this.cvt(this.getBall(8D), -8F, 8F, 31F, -31F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("STREL_TURN_DOWN", this.cvt(this.w.z, -0.23562F, 0.23562F, -50F, 50F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("STRELK_V_LONG", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 400F, 0.0F, 16F), CockpitCaproniPilot.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkVisible("STRELK_V_SHORT", false);
        this.mesh.chunkSetAngles("STRELKA_GOS", -this.fm.Or.getKren(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.02355F, -0.02355F);
        this.mesh.chunkSetLocate("STRELKA_GOR", Cockpit.xyz, Cockpit.ypr);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("STR_CLIMB", this.floatindex(this.cvt(this.setNew.vspeed, -20.32F, 20.32F, 0.0F, 8F), CockpitCaproniPilot.variometerScale), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_FlapPos", this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, 125F), 0.0F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            ;
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            ;
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            ;
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            ;
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            ;
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("STRELKA_FUEL2", false);
            this.mesh.chunkVisible("Z_RPM_SHORT1", false);
            this.mesh.chunkVisible("Z_RPM_LONG1", false);
            this.mesh.chunkVisible("Z_RPM_SHORT2", false);
            this.mesh.chunkVisible("Z_RPM_LONG2", false);
            this.mesh.chunkVisible("STRELKA_BOOST1", false);
            this.mesh.chunkVisible("Z_TEMP_OIL1", false);
            this.mesh.chunkVisible("Z_TEMP_OIL2", false);
            this.mesh.chunkVisible("Z_TEMP_RAD1", false);
            this.mesh.chunkVisible("STRELK_V_LONG", false);
            this.mesh.chunkVisible("STRELK_V_SHORT", false);
            this.mesh.chunkVisible("STRELKA_GOR", false);
            this.mesh.chunkVisible("STRELKA_GOS", false);
            this.mesh.chunkVisible("STREL_ALT_LONG", false);
            this.mesh.chunkVisible("STREL_ALT_SHORT", false);
            this.mesh.chunkVisible("STRELK_TURN_UP", false);
            this.mesh.chunkVisible("Z_FlapPos", false);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            ;
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            ;
        }
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
    private float              pictBrake;
    private float              pictFlap;
    private float              pictManf1;
    private float              pictManf2;
    private float              pictManf3;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 0.1F, 19F, 37.25F, 63.5F, 91.5F, 112F, 135.5F, 159.5F, 186.5F, 213F, 238F, 264F, 289F, 314.5F, 339.5F, 359.5F, 360F, 360F, 360F };
    private static final float radScale[]         = { 0.0F, 0.1F, 0.2F, 0.3F, 3.5F, 11F, 22F, 37.5F, 58.5F, 82.5F, 112.5F, 147F, 187F, 236F, 298.5F };
    private static final float boostScale[]       = { 0.0F, 21F, 39F, 56F, 90.5F, 109.5F, 129F, 146.5F, 163F, 179.5F, 196F, 212.5F, 231.5F, 250.5F };
    private static final float variometerScale[]  = { -158F, -111F, -65.5F, -32.5F, 0.0F, 32.5F, 65.5F, 111F, 158F };
    private static final float rpmScale[]         = { 0.0F, 11.25F, 54F, 111F, 171.5F, 229.5F, 282.5F, 334F, 342.5F, 342.5F };
    private Point3d            tmpP;
    private Vector3d           tmpV;
}
