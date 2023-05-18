package com.maddox.il2.objects.air;

import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitLate298 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitLate298.this.setTmp = CockpitLate298.this.setOld;
            CockpitLate298.this.setOld = CockpitLate298.this.setNew;
            CockpitLate298.this.setNew = CockpitLate298.this.setTmp;
            CockpitLate298.this.setNew.altimeter = CockpitLate298.this.fm.getAltitude();
            CockpitLate298.this.setNew.throttle = ((10F * CockpitLate298.this.setOld.throttle) + CockpitLate298.this.fm.CT.PowerControl) / 11F;
            CockpitLate298.this.setNew.mix = ((8F * CockpitLate298.this.setOld.mix) + CockpitLate298.this.fm.EI.engines[0].getControlMix()) / 9F;
            CockpitLate298.this.setNew.azimuth = CockpitLate298.this.fm.Or.getYaw();
            if ((CockpitLate298.this.setOld.azimuth > 270F) && (CockpitLate298.this.setNew.azimuth < 90F)) {
                CockpitLate298.this.setOld.azimuth -= 360F;
            }
            if ((CockpitLate298.this.setOld.azimuth < 90F) && (CockpitLate298.this.setNew.azimuth > 270F)) {
                CockpitLate298.this.setOld.azimuth += 360F;
            }
            CockpitLate298.this.setNew.waypointAzimuth = ((10F * CockpitLate298.this.setOld.waypointAzimuth) + (CockpitLate298.this.waypointAzimuth() - CockpitLate298.this.setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
            CockpitLate298.this.setNew.vspeed = ((199F * CockpitLate298.this.setOld.vspeed) + CockpitLate298.this.fm.getVertSpeed()) / 200F;
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float altimeter;
        float throttle;
        float azimuth;
        float waypointAzimuth;
        float mix;
        float vspeed;

        private Variables() {
        }
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) {
            return 0.0F;
        } else {
            waypoint.getP(Cockpit.P1);
            Cockpit.V.sub(Cockpit.P1, this.fm.Loc);
            return (float) (Math.toDegrees(Math.atan2(Cockpit.V.y, Cockpit.V.x)));
        }
    }

    public CockpitLate298() {
        super("3DO/Cockpit/Late298/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.cockpitNightMats = (new String[] { "Instruments", "Instruments2", "InstrumentsD" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Admission", 0.0F, 0.0F, this.floatindex(this.cvt(0.75F * this.fm.EI.engines[0].getManifoldPressure(), 0.0F, 1.5F, 0.0F, 15F), CockpitLate298.admissionScale));
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 700F, 0.0F, 14F), CockpitLate298.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 200F, 0.0F, 20F), CockpitLate298.speedometer2Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Variometer", 0.0F, 0.0F, -this.floatindex(this.cvt(this.setNew.vspeed, -12F, 20F, 0.0F, 16F), CockpitLate298.variometerScale));
        this.mesh.chunkSetAngles("Z_RPM", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 6F), CockpitLate298.rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelQuantity", this.floatindex(this.cvt(this.fm.M.fuel, 0.0F, 450F, 0.0F, 9F), CockpitLate298.fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilTemp", 0.0F, 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 14F), CockpitLate298.oiltempScale));
        this.mesh.chunkSetAngles("Z_WaterTemp", 0.0F, 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 14F), CockpitLate298.watertempScale));
        this.mesh.chunkSetAngles("Z_FuelPress", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPress", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut), 0.0F, 20F, 0.0F, 300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2a", -this.cvt(this.fm.Or.getTangage(), -15F, 15F, -15F, 15F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2b", 0.0F, this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Z_OxyButton", 0.0F, this.cvt(0.0F, 0.0F, 1.0F, 0.0F, -100F), 0.0F);
        this.mesh.chunkSetAngles("Z_OxyPress", 0.0F, this.cvt(1.0F, 0.0F, 1.0F, 0.0F, -111F), 0.0F);
        this.mesh.chunkSetAngles("Z_PompIndicator", 0.0F, this.cvt(1.0F, 0.0F, 1.0F, 0.0F, -33F), 0.0F);
        this.mesh.chunkSetAngles("Z_FlapsAirIndicator", 0.0F, this.cvt(1.0F, 0.0F, 1.0F, 0.0F, 27.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_BatteryIndicator", 0.0F, this.cvt(1.0F, 0.0F, 1.0F, 0.0F, 41F), 0.0F);
        this.mesh.chunkSetAngles("Z_LevierVolets", 40F * this.fm.CT.FlapsControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RoueTrim", 720F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Magnetos", -14F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Magnetos2", 14F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_CanopP", 1620F * this.fm.CT.getCockpitDoor(), 0.0F, 0.0F);
        if ((this.fm.AS.astateCockpitState & 8) == 0) {
            this.mesh.chunkSetAngles("Z_Throttle", -this.interp(this.setNew.throttle, this.setOld.throttle, f) * 70F, 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_TurnBank2", -this.cvt(this.getBall(6D), -12F, 12F, -9F, 9F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalL", this.fm.CT.getRudder() * 7F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalR", this.fm.CT.getRudder() * 7F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manche1", this.fm.CT.getAileron() * 6F, 0.0F, this.fm.CT.getElevator() * 0.0F);
        this.mesh.chunkSetAngles("Z_Manche2", this.fm.CT.getAileron() * 0.0F, 0.0F, this.fm.CT.getElevator() * 6F);
        this.mesh.chunkSetAngles("Z_MancheBrakes", this.cvt(this.fm.CT.BrakeControl, 0.0F, 1.0F, 0.0F, 8F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Boite", 0.0F, this.cvt(0.0F, 0.0F, 1.0F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Rechauffe2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 3000F, 10000F, 0.0F, 72F), 0.0F);
        this.mesh.chunkVisible("Z_BoiteInt", true);
        this.mesh.chunkVisible("Breakers", true);
        this.mesh.chunkVisible("Breakers2", true);
        this.mesh.chunkVisible("Z_Battery", true);
        this.mesh.chunkSetAngles("Z_BrakeLIndicator", this.cvt(this.fm.CT.BrakeControl, 0.0F, 1.0F, 0.0F, 76.6F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BrakeRIndicator", this.cvt(this.fm.CT.BrakeControl, 0.0F, 1.0F, 0.0F, -76.6F), 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_Z_RETICLE", true);
        if (this.fm.CT.BayDoorControl == 1.0F) {
            this.mesh.chunkVisible("Z_YellowLight2", true);
        } else {
            this.mesh.chunkVisible("Z_YellowLight2", false);
        }
        if ((this.fm.CT.getStepControl() >= 0.95F) || this.fm.CT.getStepControlAuto()) {
            this.mesh.chunkVisible("Z_PropGreenLight", true);
            this.mesh.chunkVisible("Z_PropRedLight", false);
        } else if ((this.fm.CT.getStepControl() < 0.95F) && !this.fm.CT.getStepControlAuto()) {
            this.mesh.chunkVisible("Z_PropGreenLight", false);
            this.mesh.chunkVisible("Z_PropRedLight", true);
        }
        if (this.fm.getAltitude() > 3000F) {
            this.mesh.chunkVisible("Z_RedLight", true);
        } else {
            this.mesh.chunkVisible("Z_RedLight", false);
        }
        this.mesh.chunkVisible("Z_Starter1", true);
        this.mesh.chunkVisible("Z_Starter2", true);
        this.mesh.chunkVisible("Z_Primer6", false);
        this.mesh.chunkVisible("Z_ComprSelect6", false);
        if ((this.fm.CT.getCockpitDoor() > 0.01F) && (this.fm.getSpeedKMH() > 300F)) {
            this.fm.CT.bHasCockpitDoorControl = false;
            this.mesh.chunkVisible("Top", false);
            this.mesh.chunkVisible("Top2", false);
            this.mesh.chunkVisible("Top3", false);
            this.mesh.chunkVisible("Z_Holes3", false);
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.0F, 1.0F, 0.0F, 0.627F);
        this.mesh.chunkSetLocate("Top", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getStepControl(), 0.0F, 1.0F, 0.0F, 0.011F);
        this.mesh.chunkSetLocate("Z_StepPropLever", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        float f1;
        if (this.aircraft().isFMTrackMirror()) {
            f1 = this.aircraft().fmTrack().getCockpitAzimuthSpeed();
        } else {
            f1 = -this.cvt(this.setNew.azimuth - this.setOld.azimuth, -1F, 1.0F, 0.04F, -0.04F);
            if (this.aircraft().fmTrack() != null) {
                this.aircraft().fmTrack().setCockpitAzimuthSpeed(f1);
            }
        }
        Cockpit.xyz[1] = f1;
        this.mesh.chunkSetLocate("Z_TurnBank1", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(-this.fm.CT.getRudder(), -1F, 1.0F, -0.025F, 0.025F);
        this.mesh.chunkSetLocate("Z_PedalL2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Z_PedalR2", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(-this.fm.CT.getRudder(), -1F, 1.0F, -0.015F, 0.015F);
        this.mesh.chunkSetLocate("Z_PedalL5", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetLocate("Z_PedalR5", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(0.0F, 0.0F, 1.0F, 0.0F, 0.015F);
        this.mesh.chunkSetLocate("Z_RobPompe", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(1.0F, 0.0F, 1.0F, 0.0F, 0.012F);
        this.mesh.chunkSetLocate("Z_FuelCutOff", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(1.0F, 0.0F, 1.0F, 0.0F, -0.075F);
        this.mesh.chunkSetLocate("Z_Starter1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(1.0F, 0.0F, 1.0F, 0.0F, -0.002F);
        this.mesh.chunkSetLocate("Z_Battery", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(1.0F, 0.0F, 1.0F, 0.0F, 0.02F);
        this.mesh.chunkSetLocate("Z_Robinets", Cockpit.xyz, Cockpit.ypr);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.setNightMats(true);
        } else {
            this.setNightMats(false);
        }
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Holes1", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("Z_Holes1", true);
            this.mesh.chunkVisible("Z_Holes2", true);
            this.mesh.chunkVisible("InstrumentsD", true);
            this.mesh.chunkVisible("Gauges2", false);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_Speedometer2", false);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Hour1", false);
            this.mesh.chunkVisible("Z_Minute1", false);
            this.mesh.chunkVisible("Z_Second1", false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private static final float speedometerScale[]  = { 0.0F, 12F, 22.5F, 48.5F, 83.1F, 125.5F, 173F, 227F, 285.5F, 347.5F, 414.3F, 482.7F, 551.5F, 607.5F, 690F };
    private static final float speedometer2Scale[] = { 0.0F, 4.2F, 7F, 11F, 15.5F, 19F, 24F, 39.5F, 54.5F, 70.5F, 87F, 104F, 120.5F, 140F, 160.5F, 171F, 216F, 240.5F, 272.5F, 302F, 331.5F };
    private static final float variometerScale[]   = { -108.1F, -89.8F, -72F, -53.5F, -35.5F, -17.5F, 0.0F, 18F, 36F, 54F, 72F, 90F, 108F, 126F, 143.5F, 162F, 180F };
    private static final float rpmScale[]          = { 0.0F, 30F, 90F, 149F, 210F, 270F, 330F };
    private static final float fuelScale[]         = { 0.0F, 35.5F, 71F, 107.5F, 145F, 182.5F, 218.5F, 254F, 284.5F, 298.5F };
    private static final float watertempScale[]    = { 0.0F, 4.5F, 15.2F, 26F, 34F, 43F, 65F, 88F, 111F, 133F, 156F, 202F, 248F, 294F, 340F };
    private static final float oiltempScale[]      = { 0.0F, 4.5F, 15.2F, 26F, 34F, 43F, 65F, 88F, 111F, 133F, 156F, 202F, 248F, 294F, 340F };
    private static final float admissionScale[]    = { 0.0F, 2.7F, 5.4F, 8F, 11F, 13.5F, 16.5F, 19F, 59F, 99F, 139F, 180F, 220.5F, 260F, 300F, 339F };

    static {
        Property.set(CockpitLate298.class, "normZN", 0.4F);
        Property.set(CockpitLate298.class, "gsZN", 0.4F);
    }

}
