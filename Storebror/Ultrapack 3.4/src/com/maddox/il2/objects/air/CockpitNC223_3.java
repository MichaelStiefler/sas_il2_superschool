package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitNC223_3 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitNC223_3.this.fm != null) {
                CockpitNC223_3.this.setTmp = CockpitNC223_3.this.setOld;
                CockpitNC223_3.this.setOld = CockpitNC223_3.this.setNew;
                CockpitNC223_3.this.setNew = CockpitNC223_3.this.setTmp;
                CockpitNC223_3.this.setNew.throttle1 = (0.9F * CockpitNC223_3.this.setOld.throttle1) + (0.1F * CockpitNC223_3.this.fm.EI.engines[0].getControlThrottle());
                CockpitNC223_3.this.setNew.throttle2 = (0.9F * CockpitNC223_3.this.setOld.throttle2) + (0.1F * CockpitNC223_3.this.fm.EI.engines[1].getControlThrottle());
                CockpitNC223_3.this.setNew.throttle3 = (0.9F * CockpitNC223_3.this.setOld.throttle3) + (0.1F * CockpitNC223_3.this.fm.EI.engines[2].getControlThrottle());
                CockpitNC223_3.this.setNew.throttle4 = (0.9F * CockpitNC223_3.this.setOld.throttle4) + (0.1F * CockpitNC223_3.this.fm.EI.engines[3].getControlThrottle());
                CockpitNC223_3.this.setNew.prop1 = (0.9F * CockpitNC223_3.this.setOld.prop1) + (0.1F * CockpitNC223_3.this.fm.EI.engines[0].getControlProp());
                CockpitNC223_3.this.setNew.prop2 = (0.9F * CockpitNC223_3.this.setOld.prop2) + (0.1F * CockpitNC223_3.this.fm.EI.engines[1].getControlProp());
                CockpitNC223_3.this.setNew.prop3 = (0.9F * CockpitNC223_3.this.setOld.prop3) + (0.1F * CockpitNC223_3.this.fm.EI.engines[2].getControlProp());
                CockpitNC223_3.this.setNew.prop4 = (0.9F * CockpitNC223_3.this.setOld.prop4) + (0.1F * CockpitNC223_3.this.fm.EI.engines[3].getControlProp());
                CockpitNC223_3.this.setNew.mix1 = (0.8F * CockpitNC223_3.this.setOld.mix1) + (0.2F * CockpitNC223_3.this.fm.EI.engines[0].getControlMix());
                CockpitNC223_3.this.setNew.mix2 = (0.8F * CockpitNC223_3.this.setOld.mix2) + (0.2F * CockpitNC223_3.this.fm.EI.engines[1].getControlMix());
                CockpitNC223_3.this.setNew.mix3 = (0.8F * CockpitNC223_3.this.setOld.mix3) + (0.2F * CockpitNC223_3.this.fm.EI.engines[2].getControlMix());
                CockpitNC223_3.this.setNew.mix4 = (0.8F * CockpitNC223_3.this.setOld.mix4) + (0.2F * CockpitNC223_3.this.fm.EI.engines[3].getControlMix());
                CockpitNC223_3.this.setNew.man1 = (0.92F * CockpitNC223_3.this.setOld.man1) + (0.08F * CockpitNC223_3.this.fm.EI.engines[0].getManifoldPressure());
                CockpitNC223_3.this.setNew.man2 = (0.92F * CockpitNC223_3.this.setOld.man2) + (0.08F * CockpitNC223_3.this.fm.EI.engines[1].getManifoldPressure());
                CockpitNC223_3.this.setNew.man3 = (0.92F * CockpitNC223_3.this.setOld.man3) + (0.08F * CockpitNC223_3.this.fm.EI.engines[2].getManifoldPressure());
                CockpitNC223_3.this.setNew.man4 = (0.92F * CockpitNC223_3.this.setOld.man4) + (0.08F * CockpitNC223_3.this.fm.EI.engines[3].getManifoldPressure());
                CockpitNC223_3.this.setNew.altimeter = CockpitNC223_3.this.fm.getAltitude();
                CockpitNC223_3.this.setNew.azimuth.setDeg(CockpitNC223_3.this.setOld.azimuth.getDeg(1.0F), CockpitNC223_3.this.fm.Or.azimut());
                CockpitNC223_3.this.setNew.vspeed = ((199F * CockpitNC223_3.this.setOld.vspeed) + CockpitNC223_3.this.fm.getVertSpeed()) / 200F;
                float f = CockpitNC223_3.this.waypointAzimuth();
                CockpitNC223_3.this.setNew.waypointAzimuth.setDeg(CockpitNC223_3.this.setOld.waypointAzimuth.getDeg(0.1F), (f - CockpitNC223_3.this.setOld.azimuth.getDeg(1.0F)) + World.Rnd().nextFloat(-10F, 10F));
                CockpitNC223_3.this.setNew.waypointDirection.setDeg(CockpitNC223_3.this.setOld.waypointDirection.getDeg(1.0F), f);
                CockpitNC223_3.this.setNew.inert = (0.999F * CockpitNC223_3.this.setOld.inert) + (0.001F * (CockpitNC223_3.this.fm.EI.engines[0].getStage() != 6 ? 0.0F : 0.867F));
                CockpitNC223_3.this.setNew.waypointAzimuth.setDeg(CockpitNC223_3.this.setOld.waypointAzimuth.getDeg(0.1F), (f - CockpitNC223_3.this.setOld.azimuth.getDeg(1.0F)) + 90F);
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      throttle2;
        float      throttle3;
        float      throttle4;
        float      prop1;
        float      prop2;
        float      prop3;
        float      prop4;
        float      mix1;
        float      mix2;
        float      mix3;
        float      mix4;
        float      man1;
        float      man2;
        float      man3;
        float      man4;
        float      altimeter;
        float      inert;
        AnglesFork azimuth;
        AnglesFork waypointDirection;
        AnglesFork waypointAzimuth;
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

    public CockpitNC223_3() {
        super("3DO/Cockpit/NC220/hier_3.him", "he111");
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
        this.mesh.chunkSetAngles("Z_ColumnR", 0.0F, 0.0F, -60.6F * this.pictAiler);
        this.mesh.chunkSetAngles("Z_Throtle1L", -64.5F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2L", -64.5F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle3L", -64.5F * this.interp(this.setNew.throttle3, this.setOld.throttle3, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle4L", -64.5F * this.interp(this.setNew.throttle4, this.setOld.throttle4, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1R", -64.5F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2R", -64.5F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle3R", -64.5F * this.interp(this.setNew.throttle3, this.setOld.throttle3, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle4R", -64.5F * this.interp(this.setNew.throttle4, this.setOld.throttle4, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1L", -58.25F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2L", -58.25F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop3L", -58.25F * this.interp(this.setNew.prop3, this.setOld.prop3, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop4L", -58.25F * this.interp(this.setNew.prop4, this.setOld.prop4, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1R", -58.25F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2R", -58.25F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop3R", -58.25F * this.interp(this.setNew.prop3, this.setOld.prop3, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop4R", -58.25F * this.interp(this.setNew.prop4, this.setOld.prop4, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1L", -48F * this.interp(this.setNew.mix1, this.setOld.mix1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2L", -48F * this.interp(this.setNew.mix2, this.setOld.mix2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture3L", -48F * this.interp(this.setNew.mix3, this.setOld.mix3, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture4L", -48F * this.interp(this.setNew.mix4, this.setOld.mix4, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1R", -48F * this.interp(this.setNew.mix1, this.setOld.mix1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2R", -48F * this.interp(this.setNew.mix2, this.setOld.mix2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture3R", -48F * this.interp(this.setNew.mix3, this.setOld.mix3, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture4R", -48F * this.interp(this.setNew.mix4, this.setOld.mix4, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPedal", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_RPedalStep1", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_RPedalStep2", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_LPedal", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_LPedalStep1", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_LPedalStep2", 0.0F, 0.0F, -22.2F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass4", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass5", -this.setNew.waypointAzimuth.getDeg(f * 0.1F) + 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AH1", 0.0F, 0.0F, this.fm.Or.getKren());
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, -0.03F, 0.03F);
        this.mesh.chunkSetLocate("Z_AH2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_AH3", 0.0F, 0.0F, this.fm.Or.getKren());
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, -0.03F, 0.03F);
        this.mesh.chunkSetLocate("Z_AH4", Cockpit.xyz, Cockpit.ypr);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        float f1 = this.getBall(7D);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(f1, -5F, 5F, 8.5F, -8.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.w.z, -0.23562F, 0.23562F, 22F, -22F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(f1, -7F, 7F, 16F, -16F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank4", this.cvt(this.w.z, -0.23562F, 0.23562F, 22F, -22F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank5", this.cvt(f1, -7F, 7F, 16F, -16F), 0.0F, 0.0F);
        float f2 = this.floatindex(this.cvt(0.539957F * Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 15F), CockpitNC223_3.speedometerScale);
        this.mesh.chunkSetAngles("Z_Speedometer1", f2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2", f2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 1440F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 1440F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp31", this.cvt(this.fm.EI.engines[3].tWaterOut, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp21", this.cvt(this.fm.EI.engines[2].tWaterOut, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp3", this.cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp32", this.cvt(this.fm.EI.engines[3].tOilIn, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp4", this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp42", this.cvt(this.fm.EI.engines[2].tOilIn, 0.0F, 350F, 0.0F, 90F), 0.0F, 0.0F);
        float f3 = (Atmosphere.temperature((float) this.fm.Loc.z) - 273.09F) + (44.4F * this.fm.EI.engines[0].getPowerOutput());
        if (f3 < 0.0F) {
            this.mesh.chunkSetAngles("Z_Temp5", this.cvt(f3, -40F, 0.0F, 0.0F, 45F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Temp5", this.cvt(f3, 0.0F, 60F, 45F, 90F), 0.0F, 0.0F);
        }
        float f4 = (Atmosphere.temperature((float) this.fm.Loc.z) - 273.09F) + (44.4F * this.fm.EI.engines[2].getPowerOutput());
        if (f4 < 0.0F) {
            this.mesh.chunkSetAngles("Z_Temp51", this.cvt(f4, -40F, 0.0F, 0.0F, 45F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Temp51", this.cvt(f4, 0.0F, 60F, 45F, 90F), 0.0F, 0.0F);
        }
        f3 = (Atmosphere.temperature((float) this.fm.Loc.z) - 273.09F) + (44.4F * this.fm.EI.engines[1].getPowerOutput());
        if (f3 < 0.0F) {
            this.mesh.chunkSetAngles("Z_Temp6", this.cvt(f3, -40F, 0.0F, 0.0F, 45F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Temp6", this.cvt(f3, 0.0F, 60F, 45F, 90F), 0.0F, 0.0F);
        }
        f4 = (Atmosphere.temperature((float) this.fm.Loc.z) - 273.09F) + (44.4F * this.fm.EI.engines[3].getPowerOutput());
        if (f4 < 0.0F) {
            this.mesh.chunkSetAngles("Z_Temp61", this.cvt(f4, -40F, 0.0F, 0.0F, 45F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_Temp61", this.cvt(f4, 0.0F, 60F, 45F, 90F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Temp7", this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 120F, 0.0F, 12F), CockpitNC223_3.oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp71", this.floatindex(this.cvt(this.fm.EI.engines[3].tOilOut, 0.0F, 120F, 0.0F, 12F), CockpitNC223_3.oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp8", this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 120F, 0.0F, 12F), CockpitNC223_3.oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp81", this.floatindex(this.cvt(this.fm.EI.engines[2].tOilOut, 0.0F, 120F, 0.0F, 12F), CockpitNC223_3.oilTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flap1", this.cvt(this.fm.CT.getFlap(), 0.0F, 0.75F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flap2", 57F * this.fm.CT.getFlap(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.setNew.vspeed, -15F, 15F, -0.055F, 0.055F);
        this.mesh.chunkSetAngles("Z_Climb2", this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold1", this.cvt(this.setNew.man1, 0.33339F, 1.66661F, -162F, 162F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold2", this.cvt(this.setNew.man2, 0.33339F, 1.66661F, -162F, 162F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold3", this.cvt(this.setNew.man3, 0.33339F, 1.66661F, -162F, 162F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold4", this.cvt(this.setNew.man4, 0.33339F, 1.66661F, -162F, 162F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil2", this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness()), 0.0F, 7.45F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil3", this.cvt(1.0F + (0.05F * this.fm.EI.engines[2].tOilOut * this.fm.EI.engines[2].getReadyness()), 0.0F, 7.45F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil4", this.cvt(1.0F + (0.05F * this.fm.EI.engines[3].tOilOut * this.fm.EI.engines[3].getReadyness()), 0.0F, 7.45F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuelpress1", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.5F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuelpress2", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.5F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuelpress3", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.5F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuelpress4", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 0.5F, 0.0F, 180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3500F, 0.0F, 7F), CockpitNC223_3.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", this.floatindex(this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 3500F, 0.0F, 7F), CockpitNC223_3.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM3", this.floatindex(this.cvt(this.fm.EI.engines[2].getRPM(), 0.0F, 3500F, 0.0F, 7F), CockpitNC223_3.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM4", this.floatindex(this.cvt(this.fm.EI.engines[3].getRPM(), 0.0F, 3500F, 0.0F, 7F), CockpitNC223_3.rpmScale), 0.0F, 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkVisible("Z_GearGreen2", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
            this.mesh.chunkVisible("Z_GearGreen3", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
            this.mesh.chunkVisible("Z_GearRed2", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.rgear);
            this.mesh.chunkVisible("Z_GearRed3", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.lgear);
        }
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
            this.mesh.chunkVisible("Z_TurnBank2", false);
            this.mesh.chunkVisible("Z_TurnBank3", false);
            this.mesh.chunkVisible("Z_TurnBank4", false);
            this.mesh.chunkVisible("Z_TurnBank5", false);
            this.mesh.chunkVisible("Z_Flap2", false);
            this.mesh.chunkVisible("Z_GearGreen2", false);
            this.mesh.chunkVisible("Z_GearGreen3", false);
            this.mesh.chunkVisible("Z_GearRed2", false);
            this.mesh.chunkVisible("Z_GearRed3", false);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("Z_RPM2", false);
            this.mesh.chunkVisible("Z_RPM3", false);
            this.mesh.chunkVisible("Z_RPM4", false);
            this.mesh.chunkVisible("Z_Oil1", false);
            this.mesh.chunkVisible("Z_Oil2", false);
            this.mesh.chunkVisible("Z_Oil3", false);
            this.mesh.chunkVisible("Z_Oil4", false);
            this.mesh.chunkVisible("Z_fuelpress1", false);
            this.mesh.chunkVisible("Z_fuelpress2", false);
            this.mesh.chunkVisible("Z_fuelpress3", false);
            this.mesh.chunkVisible("Z_fuelpress4", false);
            this.mesh.chunkVisible("Z_Temp1", false);
            this.mesh.chunkVisible("Z_Temp2", false);
            this.mesh.chunkVisible("Z_Temp3", false);
            this.mesh.chunkVisible("Z_Temp4", false);
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
