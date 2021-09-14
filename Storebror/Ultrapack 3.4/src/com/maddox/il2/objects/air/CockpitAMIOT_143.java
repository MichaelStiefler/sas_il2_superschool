package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitAMIOT_143 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitAMIOT_143.this.fm != null) {
                CockpitAMIOT_143.this.setTmp = CockpitAMIOT_143.this.setOld;
                CockpitAMIOT_143.this.setOld = CockpitAMIOT_143.this.setNew;
                CockpitAMIOT_143.this.setNew = CockpitAMIOT_143.this.setTmp;
                CockpitAMIOT_143.this.setNew.throttle1 = (0.9F * CockpitAMIOT_143.this.setOld.throttle1) + (0.1F * CockpitAMIOT_143.this.fm.EI.engines[0].getControlThrottle());
                CockpitAMIOT_143.this.setNew.prop1 = (0.9F * CockpitAMIOT_143.this.setOld.prop1) + (0.1F * CockpitAMIOT_143.this.fm.EI.engines[0].getControlProp());
                CockpitAMIOT_143.this.setNew.mix1 = (0.8F * CockpitAMIOT_143.this.setOld.mix1) + (0.2F * CockpitAMIOT_143.this.fm.EI.engines[0].getControlMix());
                CockpitAMIOT_143.this.setNew.throttle2 = (0.9F * CockpitAMIOT_143.this.setOld.throttle2) + (0.1F * CockpitAMIOT_143.this.fm.EI.engines[1].getControlThrottle());
                CockpitAMIOT_143.this.setNew.prop2 = (0.9F * CockpitAMIOT_143.this.setOld.prop2) + (0.1F * CockpitAMIOT_143.this.fm.EI.engines[1].getControlProp());
                CockpitAMIOT_143.this.setNew.mix2 = (0.8F * CockpitAMIOT_143.this.setOld.mix2) + (0.2F * CockpitAMIOT_143.this.fm.EI.engines[1].getControlMix());
                CockpitAMIOT_143.this.setNew.man1 = (0.92F * CockpitAMIOT_143.this.setOld.man1) + (0.08F * CockpitAMIOT_143.this.fm.EI.engines[0].getManifoldPressure());
                CockpitAMIOT_143.this.setNew.man2 = (0.92F * CockpitAMIOT_143.this.setOld.man2) + (0.08F * CockpitAMIOT_143.this.fm.EI.engines[1].getManifoldPressure());
                CockpitAMIOT_143.this.setNew.altimeter = CockpitAMIOT_143.this.fm.getAltitude();
                CockpitAMIOT_143.this.setNew.azimuth.setDeg(CockpitAMIOT_143.this.setOld.azimuth.getDeg(1.0F), CockpitAMIOT_143.this.fm.Or.azimut());
                CockpitAMIOT_143.this.setNew.vspeed = ((199F * CockpitAMIOT_143.this.setOld.vspeed) + CockpitAMIOT_143.this.fm.getVertSpeed()) / 200F;
                float f = CockpitAMIOT_143.this.waypointAzimuth();
                CockpitAMIOT_143.this.setNew.waypointAzimuth.setDeg(CockpitAMIOT_143.this.setOld.waypointAzimuth.getDeg(0.1F), (f - CockpitAMIOT_143.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                CockpitAMIOT_143.this.setNew.waypointDirection.setDeg(CockpitAMIOT_143.this.setOld.waypointDirection.getDeg(1.0F), f);
                CockpitAMIOT_143.this.setNew.inert = (0.999F * CockpitAMIOT_143.this.setOld.inert) + (0.001F * (CockpitAMIOT_143.this.fm.EI.engines[0].getStage() != 6 ? 0.0F : 0.867F));
                CockpitAMIOT_143.this.setNew.waypointAzimuth.setDeg(CockpitAMIOT_143.this.setOld.waypointAzimuth.getDeg(0.1F), (f - CockpitAMIOT_143.this.setOld.azimuth.getDeg(1.0F)) + 90F);
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
        float      inert;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork waypointDirection;
        float      vspeed;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.waypointDirection = new AnglesFork();
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

    public CockpitAMIOT_143() {
        super("3DO/Cockpit/Amiot-143/hierAmiot.him", "he111");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.tmpP = new Point3d();
        this.tmpV = new Vector3d();
        this.cockpitNightMats = (new String[] { "gauges5", "GP1_d1", "GP1", "GP2_d1", "GP2", "GP3", "GP4_d1", "GP4", "GP5_d1", "GP5", "GP6_d1", "GP6", "GP7", "GP9", "throttle", "Volt_Tacho" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, 0.0F, 10F * (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)));
        this.mesh.chunkSetAngles("Z_ColumnL", 0.0F, 0.0F, -60.6F * (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)));
        this.mesh.chunkSetAngles("Z_Throtle1", -40F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", -40F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", -75.5F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", -75.5F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", -62.9F * this.interp(this.setNew.mix1, this.setOld.mix1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2", -62.9F * this.interp(this.setNew.mix2, this.setOld.mix2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LPedal", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_LPedalStep1", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_LPedalStep2", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass4", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass5", -this.setNew.waypointAzimuth.getDeg(f * 0.1F) + 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AH1", 0.0F, 0.0F, this.fm.Or.getKren());
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, -0.03F, 0.03F);
        this.mesh.chunkSetLocate("Z_AH2", Cockpit.xyz, Cockpit.ypr);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        float f1 = this.getBall(7D);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(f1, -5F, 5F, 8.5F, -8.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.w.z, -0.23562F, 0.23562F, 22F, -22F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(f1, -7F, 7F, 16F, -16F), 0.0F, 0.0F);
        float f2 = this.floatindex(this.cvt(0.539957F * Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 15F), CockpitAMIOT_143.speedometerScale);
        this.mesh.chunkSetAngles("Z_Speedometer1", f2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 1440F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp3", this.cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp4", this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp7", this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 120F, 0.0F, 12F), CockpitAMIOT_143.oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp8", this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 12F), CockpitAMIOT_143.oilTempScale), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.setNew.vspeed, -15F, 15F, -0.055F, 0.055F);
        this.mesh.chunkSetLocate("Z_Climb1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Climb2", this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold1", this.cvt(this.setNew.man1, 0.33339F, 1.66661F, -162F, 162F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold2", this.cvt(this.setNew.man2, 0.33339F, 1.66661F, -162F, 162F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil2", this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness()), 0.0F, 7.45F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuelpress1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.5F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuelpress2", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 0.5F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 7F), CockpitAMIOT_143.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 3500F, 0.0F, 7F), CockpitAMIOT_143.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ColumnL", 0.0F, (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 68F, 0.0F);
        this.mesh.chunkSetAngles("Z_BrkpresR", 0.0F, -7F * this.fm.CT.getBrake(), 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XHullDamage5", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XGlassDamage5", true);
            this.mesh.chunkVisible("XHullDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("Z_Fuel1", false);
            this.mesh.chunkVisible("Z_Fuel2", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
            this.mesh.chunkVisible("Z_TurnBank3", false);
            this.mesh.chunkVisible("Z_TurnBank4", false);
            this.mesh.chunkVisible("Z_TurnBank5", false);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("Z_RPM2", false);
            this.mesh.chunkVisible("Z_Oil1", false);
            this.mesh.chunkVisible("Z_Oil2", false);
            this.mesh.chunkVisible("Z_fuelpress1", false);
            this.mesh.chunkVisible("Z_fuelpress2", false);
            this.mesh.chunkVisible("Z_Temp1", false);
            this.mesh.chunkVisible("Z_Temp2", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("XHullDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XHullDamage3", true);
            this.mesh.chunkVisible("XHullDamage5", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage5", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        this.retoggleLight();
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
    private static final float speedometerScale[] = { 0.0F, 6.5F, 30F, 66F, 105F, 158.5F, 212F, 272.5F, 333F, 384F, 432.5F, 479.5F, 526.5F, 573.5F, 624.5F, 674F };
    private static final float oilTempScale[]     = { 0.0F, 4F, 17.5F, 38F, 63F, 90.5F, 115F, 141.3F, 180F, 221.7F, 269.5F, 311F, 357F };
    private static final float rpmScale[]         = { 0.0F, 10F, 75F, 126.5F, 179.5F, 232F, 284.5F, 336F };
    private Point3d            tmpP;
    private Vector3d           tmpV;

}
