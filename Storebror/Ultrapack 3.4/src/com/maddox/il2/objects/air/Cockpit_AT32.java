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

public class Cockpit_AT32 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (Cockpit_AT32.this.fm != null) {
                Cockpit_AT32.this.setTmp = Cockpit_AT32.this.setOld;
                Cockpit_AT32.this.setOld = Cockpit_AT32.this.setNew;
                Cockpit_AT32.this.setNew = Cockpit_AT32.this.setTmp;
                Cockpit_AT32.this.setNew.throttle1 = (0.85F * Cockpit_AT32.this.setOld.throttle1) + (Cockpit_AT32.this.fm.EI.engines[0].getControlThrottle() * 0.15F);
                Cockpit_AT32.this.setNew.throttle2 = (0.85F * Cockpit_AT32.this.setOld.throttle2) + (Cockpit_AT32.this.fm.EI.engines[1].getControlThrottle() * 0.15F);
                Cockpit_AT32.this.setNew.prop1 = (0.85F * Cockpit_AT32.this.setOld.prop1) + (Cockpit_AT32.this.fm.EI.engines[0].getControlProp() * 0.15F);
                Cockpit_AT32.this.setNew.prop2 = (0.85F * Cockpit_AT32.this.setOld.prop2) + (Cockpit_AT32.this.fm.EI.engines[1].getControlProp() * 0.15F);
                Cockpit_AT32.this.setNew.altimeter = Cockpit_AT32.this.fm.getAltitude();
                float f = Cockpit_AT32.this.waypointAzimuth();
                Cockpit_AT32.this.setNew.azimuth.setDeg(Cockpit_AT32.this.setOld.azimuth.getDeg(1.0F), Cockpit_AT32.this.fm.Or.azimut());
                Cockpit_AT32.this.setNew.waypointAzimuth.setDeg(Cockpit_AT32.this.setOld.waypointAzimuth.getDeg(1.0F), f);
                Cockpit_AT32.this.setNew.waypointDeviation.setDeg(Cockpit_AT32.this.setOld.waypointDeviation.getDeg(0.1F), (f - Cockpit_AT32.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                Cockpit_AT32.this.setNew.vspeed = ((199F * Cockpit_AT32.this.setOld.vspeed) + Cockpit_AT32.this.fm.getVertSpeed()) / 200F;
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

    public Cockpit_AT32() {
        super("3DO/Cockpit/AT32-P/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictManf1 = 1.0F;
        this.pictManf2 = 1.0F;
        this.bNeedSetUp = true;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "texture01", "texture02", "texture03", "texture04", "texture06", "texture21", "texture25" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        this.mesh.chunkSetAngles("Z_Column", 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 8F, 0.0F);
        this.mesh.chunkSetAngles("Z_Sturva", 0.0F, -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 68F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, -10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_Brake", 0.0F, 118F * this.fm.CT.getBrake(), 0.0F);
        this.mesh.chunkSetAngles("zMix1", 0.0F, -45.8F * this.fm.EI.engines[0].getControlMix(), 0.0F);
        this.mesh.chunkSetAngles("zMix2", 0.0F, -45.8F * this.fm.EI.engines[1].getControlMix(), 0.0F);
        this.mesh.chunkSetAngles("zPitch1", 0.0F, -54F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F);
        this.mesh.chunkSetAngles("zPitch2", 0.0F, -54F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F);
        this.mesh.chunkSetAngles("zThrottle1", 0.0F, -49F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F);
        this.mesh.chunkSetAngles("zThrottle2", 0.0F, -49F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F);
        this.mesh.chunkSetAngles("zHour", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("zMinute", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zSecond", 0.0F, this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("zAH1", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0365F, -0.0365F);
        this.mesh.chunkSetLocate("zAH2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Climb", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), Cockpit_AT32.variometerScale), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("zTurnBank", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        this.mesh.chunkSetAngles("zBall", 0.0F, this.cvt(this.getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
        this.mesh.chunkSetAngles("zSpeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), Cockpit_AT32.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("zAlt2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("zCompass1", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zCompass2", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("zRPM1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
        this.mesh.chunkSetAngles("zRPM2", 0.0F, this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", 0.0F, this.pictManf1 = (0.9F * this.pictManf1) + (0.1F * this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F)), 0.0F);
        this.mesh.chunkSetAngles("Z_Pres2", 0.0F, this.pictManf2 = (0.9F * this.pictManf2) + (0.1F * this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F)), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres1", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("Z_FuelPres2", 0.0F, this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.78F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness()), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres2", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("zFuel1", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 700F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("zFuel2", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 700F, 0.0F, 180F), 0.0F);
        float f1 = (0.5F * this.fm.EI.engines[0].getRPM()) + (0.5F * this.fm.EI.engines[1].getRPM());
        f1 = 2.5F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
        this.mesh.chunkSetAngles("zSuction", 0.0F, this.cvt(f1, 0.0F, 10F, 0.0F, 297F), 0.0F);
        this.mesh.chunkVisible("Z_GearLGreen", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_GearCGreen", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.cgear);
        this.mesh.chunkVisible("Z_GearRGreen", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("Z_GearLRed", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("Z_GearCRed", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.cgear);
        this.mesh.chunkVisible("Z_GearRRed", (this.fm.CT.getGear() == 0.0F) && this.fm.Gears.rgear);
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

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictManf1;
    private float              pictManf2;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[] = { 0.0F, 2.5F, 59F, 126F, 155.5F, 223.5F, 240F, 254.5F, 271F, 285F, 296.5F, 308.5F, 324F, 338.5F };
    private static final float variometerScale[]  = { -180F, -157F, -130F, -108F, -84F, -46.5F, 0.0F, 46.5F, 84F, 108F, 130F, 157F, 180F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

}
