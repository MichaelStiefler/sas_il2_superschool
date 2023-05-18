package com.maddox.il2.objects.air;

import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitD520 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            CockpitD520.this.setTmp = CockpitD520.this.setOld;
            CockpitD520.this.setOld = CockpitD520.this.setNew;
            CockpitD520.this.setNew = CockpitD520.this.setTmp;
            CockpitD520.this.setNew.altimeter = CockpitD520.this.fm.getAltitude();
            if (CockpitD520.this.fm.CT.GearControl == 1.0F) {
                if (CockpitD520.this.setNew.stbyPosition1 > 0.0F) CockpitD520.this.setNew.stbyPosition1 = CockpitD520.this.setOld.stbyPosition1 - 0.5F;
            } else if (CockpitD520.this.setNew.stbyPosition1 < 1.0F) CockpitD520.this.setNew.stbyPosition1 = CockpitD520.this.setOld.stbyPosition1 + 0.5F;
            if (!CockpitD520.this.cockpitLightControl) {
                if (CockpitD520.this.setNew.stbyPosition2 > 0.0F) CockpitD520.this.setNew.stbyPosition2 = CockpitD520.this.setOld.stbyPosition2 - 0.05F;
            } else if (CockpitD520.this.setNew.stbyPosition2 < 1.0F) CockpitD520.this.setNew.stbyPosition2 = CockpitD520.this.setOld.stbyPosition2 + 0.05F;
            if (!CockpitD520.this.fm.AS.bNavLightsOn) {
                if (CockpitD520.this.setNew.stbyPosition3 > 0.0F) CockpitD520.this.setNew.stbyPosition3 = CockpitD520.this.setOld.stbyPosition3 - 0.5F;
            } else if (CockpitD520.this.setNew.stbyPosition3 < 1.0F) CockpitD520.this.setNew.stbyPosition3 = CockpitD520.this.setOld.stbyPosition3 + 0.5F;
            if (!CockpitD520.this.fm.AS.bLandingLightOn) {
                if (CockpitD520.this.setNew.stbyPosition4 > 0.0F) CockpitD520.this.setNew.stbyPosition4 = CockpitD520.this.setOld.stbyPosition4 - 0.5F;
            } else if (CockpitD520.this.setNew.stbyPosition4 < 1.0F) CockpitD520.this.setNew.stbyPosition4 = CockpitD520.this.setOld.stbyPosition4 + 0.5F;
            if (!CockpitD520.this.fm.CT.getStepControlAuto()) {
                if (CockpitD520.this.setNew.stbyPosition5 > 0.0F) CockpitD520.this.setNew.stbyPosition5 = CockpitD520.this.setOld.stbyPosition5 - 0.2F;
            } else if (CockpitD520.this.setNew.stbyPosition5 < 1.0F) CockpitD520.this.setNew.stbyPosition5 = CockpitD520.this.setOld.stbyPosition5 + 0.2F;
            if (CockpitD520.this.fm.CT.BayDoorControl == 0.0F) {
                if (CockpitD520.this.setNew.stbyPosition8 > 0.0F) CockpitD520.this.setNew.stbyPosition8 = CockpitD520.this.setOld.stbyPosition8 - 0.5F;
            } else if (CockpitD520.this.setNew.stbyPosition8 < 1.0F) CockpitD520.this.setNew.stbyPosition8 = CockpitD520.this.setOld.stbyPosition8 + 0.5F;
            if (!CockpitD520.this.fm.CT.WeaponControl[0] && !CockpitD520.this.fm.CT.WeaponControl[1]) {
                if (CockpitD520.this.setNew.stbyPosition12 > 0.0F) CockpitD520.this.setNew.stbyPosition12 = CockpitD520.this.setOld.stbyPosition12 - 0.5F;
            } else if (CockpitD520.this.setNew.stbyPosition12 < 1.0F) CockpitD520.this.setNew.stbyPosition12 = CockpitD520.this.setOld.stbyPosition12 + 0.5F;
            if (CockpitD520.this.fm.CT.GearControl == 0.0F) {
                if (CockpitD520.this.setNew.stbyPosition13 > 0.0F) CockpitD520.this.setNew.stbyPosition13 = CockpitD520.this.setOld.stbyPosition13 - 0.05F;
            } else if (CockpitD520.this.setNew.stbyPosition13 < 1.0F) CockpitD520.this.setNew.stbyPosition13 = CockpitD520.this.setOld.stbyPosition13 + 0.05F;
            if (CockpitD520.this.fm.CT.FlapsControl <= 0.1F || CockpitD520.this.fm.CT.FlapsControl == 1.0F) {
                if (CockpitD520.this.setNew.stbyPosition14 > 0.0F) CockpitD520.this.setNew.stbyPosition14 = CockpitD520.this.setOld.stbyPosition14 - 0.05F;
            } else if (CockpitD520.this.setNew.stbyPosition14 < 1.0F) CockpitD520.this.setNew.stbyPosition14 = CockpitD520.this.setOld.stbyPosition14 + 0.05F;
            if (CockpitD520.this.fm.CT.getGear() > 0.1F && CockpitD520.this.fm.CT.getGear() < 0.9F) {
                if (CockpitD520.this.setNew.stbyPosition15 > 0.0F) CockpitD520.this.setNew.stbyPosition15 = CockpitD520.this.setOld.stbyPosition15 - 0.05F;
            } else if (CockpitD520.this.setNew.stbyPosition15 < 1.0F) CockpitD520.this.setNew.stbyPosition15 = CockpitD520.this.setOld.stbyPosition15 + 0.05F;
            if (!CockpitD520.this.CompresseOk) {
                if (CockpitD520.this.setNew.stbyPosition35 > 0.0F) CockpitD520.this.setNew.stbyPosition35 = CockpitD520.this.setOld.stbyPosition35 - 0.005F;
            } else if (CockpitD520.this.setNew.stbyPosition35 < 1.0F) CockpitD520.this.setNew.stbyPosition35 = CockpitD520.this.setOld.stbyPosition35 + 0.005F;
            if (!CockpitD520.this.CompresseOk) {
                if (CockpitD520.this.setNew.stbyPosition36 > 0.0F) CockpitD520.this.setNew.stbyPosition36 = CockpitD520.this.setOld.stbyPosition36 - 0.005F;
            } else if (CockpitD520.this.setNew.stbyPosition36 < 1.0F) CockpitD520.this.setNew.stbyPosition36 = CockpitD520.this.setOld.stbyPosition36 + 0.005F;
            if (!CockpitD520.this.StarterState2) {
                if (CockpitD520.this.setNew.stbyPosition50 > 0.0F) CockpitD520.this.setNew.stbyPosition50 = CockpitD520.this.setOld.stbyPosition50 - 0.1F;
            } else if (CockpitD520.this.setNew.stbyPosition50 < 1.0F) CockpitD520.this.setNew.stbyPosition50 = CockpitD520.this.setOld.stbyPosition50 + 0.1F;
            if (!CockpitD520.this.StarterState1) {
                if (CockpitD520.this.setNew.stbyPosition51 > 0.0F) CockpitD520.this.setNew.stbyPosition51 = CockpitD520.this.setOld.stbyPosition51 - 0.1F;
            } else if (CockpitD520.this.setNew.stbyPosition51 < 1.0F) CockpitD520.this.setNew.stbyPosition51 = CockpitD520.this.setOld.stbyPosition51 + 0.1F;
            if (CockpitD520.this.fm.EI.isSelectionHasControlExtinguisher()) {
                if (CockpitD520.this.setNew.stbyPosition59 > 0.0F) CockpitD520.this.setNew.stbyPosition59 = CockpitD520.this.setOld.stbyPosition59 - 0.1F;
            } else if (CockpitD520.this.setNew.stbyPosition59 < 1.0F) CockpitD520.this.setNew.stbyPosition59 = CockpitD520.this.setOld.stbyPosition59 + 0.1F;
            if (!CockpitD520.this.cockpitDimControl) {
                if (CockpitD520.this.setNew.dimPosition > 0.0F) CockpitD520.this.setNew.dimPosition = CockpitD520.this.setOld.dimPosition - 0.05F;
            } else if (CockpitD520.this.setNew.dimPosition < 1.0F) CockpitD520.this.setNew.dimPosition = CockpitD520.this.setOld.dimPosition + 0.05F;
            CockpitD520.this.setNew.throttle = (10F * CockpitD520.this.setOld.throttle + CockpitD520.this.fm.CT.PowerControl) / 11F;
            CockpitD520.this.setNew.mix = (8F * CockpitD520.this.setOld.mix + CockpitD520.this.fm.EI.engines[0].getControlMix()) / 9F;
            CockpitD520.this.setNew.azimuth = CockpitD520.this.fm.Or.getYaw();
            if (CockpitD520.this.setOld.azimuth > 270F && CockpitD520.this.setNew.azimuth < 90F) CockpitD520.this.setOld.azimuth -= 360F;
            if (CockpitD520.this.setOld.azimuth < 90F && CockpitD520.this.setNew.azimuth > 270F) CockpitD520.this.setOld.azimuth += 360F;
            CockpitD520.this.setNew.waypointAzimuth = (10F * CockpitD520.this.setOld.waypointAzimuth + (CockpitD520.this.waypointAzimuth() - CockpitD520.this.setOld.azimuth) + World.Rnd().nextFloat(-30F, 30F)) / 11F;
            CockpitD520.this.setNew.vspeed = (199F * CockpitD520.this.setOld.vspeed + CockpitD520.this.fm.getVertSpeed()) / 200F;
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float altimeter;
        float throttle;
        float dimPosition;
        float azimuth;
        float waypointAzimuth;
        float mix;
        float vspeed;
        float stbyPosition1;
        float stbyPosition2;
        float stbyPosition3;
        float stbyPosition4;
        float stbyPosition5;
        float stbyPosition8;
        float stbyPosition12;
        float stbyPosition13;
        float stbyPosition14;
        float stbyPosition15;
        float stbyPosition35;
        float stbyPosition36;
        float stbyPosition50;
        float stbyPosition51;
        float stbyPosition59;

        private Variables() {
        }

    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Cockpit_D0", false);
            this.aircraft().hierMesh().chunkVisible("CockpitInterior", false);
            this.aircraft().hierMesh().chunkVisible("CockpitSeat", false);
            this.aircraft().hierMesh().chunkVisible("FGlass", false);
            this.aircraft().hierMesh().chunkVisible("RGlass", false);
            this.aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Blister1_D1", false);
            this.aircraft().hierMesh().chunkVisible("InternalViewEngine", true);
            this.aircraft().hierMesh().chunkVisible("CF_D0Interior", false);
            this.aircraft().hierMesh().chunkVisible("Pilot1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Head1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Tail1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Motor", false);
            this.aircraft().hierMesh().chunkVisible("Motor2", false);
            this.aircraft().hierMesh().chunkVisible("Pipes", false);
            this.aircraft().hierMesh().chunkVisible("Radiator", false);
            this.aircraft().hierMesh().chunkVisible("Radiatorb", false);
            this.aircraft().hierMesh().chunkVisible("HMask1_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Cockpit_D0", true);
        this.aircraft().hierMesh().chunkVisible("CockpitInterior", true);
        this.aircraft().hierMesh().chunkVisible("CockpitSeat", true);
        this.aircraft().hierMesh().chunkVisible("FGlass", true);
        this.aircraft().hierMesh().chunkVisible("RGlass", true);
        this.aircraft().hierMesh().chunkVisible("InternalViewEngine", false);
        this.aircraft().hierMesh().chunkVisible("CF_D0Interior", true);
        this.aircraft().hierMesh().chunkVisible("Pilot1_D0", true);
        this.aircraft().hierMesh().chunkVisible("Head1_D0", true);
        this.aircraft().hierMesh().chunkVisible("Tail1_D0", true);
        this.aircraft().hierMesh().chunkVisible("Motor", true);
        this.aircraft().hierMesh().chunkVisible("Motor2", true);
        this.aircraft().hierMesh().chunkVisible("Pipes", true);
        this.aircraft().hierMesh().chunkVisible("Radiator", true);
        this.aircraft().hierMesh().chunkVisible("Radiatorb", true);
        if (!((D520) this.aircraft()).CockpitDamaged2 && !((D520) this.aircraft()).CanopyEjectState) this.aircraft().hierMesh().chunkVisible("Blister1_D0", true);
        else this.aircraft().hierMesh().chunkVisible("Blister1_D0", false);
        if (((D520) this.aircraft()).CockpitDamaged2 && !((D520) this.aircraft()).CanopyEjectState) this.aircraft().hierMesh().chunkVisible("Blister1_D1", true);
        else this.aircraft().hierMesh().chunkVisible("Blister1_D1", false);
        super.doFocusLeave();
    }

    protected float waypointAzimuth() {
        WayPoint waypoint = this.fm.AP.way.curr();
        if (waypoint == null) return 0.0F;
        else {
            waypoint.getP(Cockpit.P1);
            Cockpit.V.sub(Cockpit.P1, this.fm.Loc);
            return (float) (57.295779513082323D * Math.atan2(Cockpit.V.y, Cockpit.V.x));
        }
    }

    public CockpitD520() {
        super("3DO/Cockpit/D-520/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.CompresseOk = false;
        this.StarterState1 = false;
        this.StarterState2 = false;
        this.setNew.dimPosition = 1.0F;
        this.cockpitNightMats = new String[] { "Instruments", "Instruments2", "InstrumentsD" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Admission", 0.0F, 0.0F, this.floatindex(this.cvt(0.75F * this.fm.EI.engines[0].getManifoldPressure(), 0.0F, 1.5F, 0.0F, 15F), admissionScale));
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 700F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 200F, 0.0F, 20F), speedometer2Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Variometer", 0.0F, 0.0F, -this.floatindex(this.cvt(this.setNew.vspeed, -12F, 20F, 0.0F, 16F), variometerScale));
        this.mesh.chunkSetAngles("Z_RPM", this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 3000F, 0.0F, 6F), rpmScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FuelQuantity", this.floatindex(this.cvt(this.fm.M.fuel, 0.0F, 450F, 0.0F, 9F), fuelScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilTemp", 0.0F, 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 140F, 0.0F, 14F), oiltempScale));
        this.mesh.chunkSetAngles("Z_WaterTemp", 0.0F, 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 140F, 0.0F, 14F), watertempScale));
        this.mesh.chunkSetAngles("Z_FuelPress", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.26F, 0.0F, 3F, 0.0F, 300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_OilPress", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 20F, 0.0F, 300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", 0.0F, this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2a", -this.cvt(this.fm.Or.getTangage(), -15F, 15F, -15F, 15F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2b", 0.0F, this.interp(this.setNew.azimuth, this.setOld.azimuth, f), 0.0F);
        this.mesh.chunkSetAngles("Z_GearContact", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition1, this.setOld.stbyPosition1, f), 0.0F, 1.0F, 0.0F, -50F), 0.0F);
        this.mesh.chunkSetAngles("Z_LightButton", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition2, this.setOld.stbyPosition2, f), 0.0F, 1.0F, 0.0F, 311.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_NavLightsContact", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition3, this.setOld.stbyPosition3, f), 0.0F, 1.0F, 0.0F, -50F), 0.0F);
        this.mesh.chunkSetAngles("Z_LandLightContact", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition4, this.setOld.stbyPosition4, f), 0.0F, 1.0F, 0.0F, -50F), 0.0F);
        this.mesh.chunkSetAngles("Z_OxyButton", 0.0F, this.cvt(0.0F, 0.0F, 1.0F, 0.0F, -100F), 0.0F);
        this.mesh.chunkSetAngles("Z_OxyPress", 0.0F, this.cvt(1.0F, 0.0F, 1.0F, 0.0F, -111F), 0.0F);
        this.mesh.chunkSetAngles("Z_MasterArm", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition8, this.setOld.stbyPosition8, f), 0.0F, 1.0F, 0.0F, -30F), 0.0F);
        this.mesh.chunkSetAngles("Z_GearAirIndicator", 0.0F, this.cvt(1.0F, 0.0F, 1.0F, 0.0F, 33F), 0.0F);
        this.mesh.chunkSetAngles("Z_PompIndicator", 0.0F, this.cvt(1.0F, 0.0F, 1.0F, 0.0F, -33F), 0.0F);
        this.mesh.chunkSetAngles("Z_FlapsAirIndicator", 0.0F, this.cvt(1.0F, 0.0F, 1.0F, 0.0F, 27.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_BatteryIndicator", 0.0F, this.cvt(1.0F, 0.0F, 1.0F, 0.0F, 41F), 0.0F);
        this.mesh.chunkSetAngles("Z_ColliButton", 0.0F, this.cvt(1.0F, 0.0F, 1.0F, 0.0F, 321.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_LevierTrain", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition13, this.setOld.stbyPosition13, f), 0.0F, 1.0F, 0.0F, 40F), 0.0F);
        this.mesh.chunkSetAngles("Z_LevierVolets", 40F * this.fm.CT.FlapsControl, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RoueTrim", 720F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Magnetos", -14F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Magnetos2", 14F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 0.0F);
        if (!((D520) this.aircraft()).CockpitDamaged5) this.mesh.chunkSetAngles("Z_GTeintee", 0.0F, this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 87F), 0.0F);
        this.mesh.chunkSetAngles("Z_ColliViseur", 0.0F, this.cvt(0.0F, 0.0F, 1.0F, 0.0F, 44.22F), 0.0F);
        this.mesh.chunkSetAngles("Z_ComprNeedle1", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition35, this.setOld.stbyPosition35, f), 0.0F, 1.0F, 0.0F, -31.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_ComprNeedle2", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition36, this.setOld.stbyPosition36, f), 0.0F, 1.0F, 0.0F, -31.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_CanopP", 1620F * this.fm.CT.getCockpitDoor(), 0.0F, 0.0F);
        if ((this.fm.AS.astateCockpitState & 8) == 0) this.mesh.chunkSetAngles("Z_Throttle", -this.interp(this.setNew.throttle, this.setOld.throttle, f) * 70F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank2", -this.cvt(this.getBall(6D), -12F, 12F, -9F, 9F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Second1", this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalL", this.fm.CT.getRudder() * 7F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PedalR", this.fm.CT.getRudder() * 7F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manche1", this.fm.CT.getAileron() * 6F, 0.0F, this.fm.CT.getElevator() * 0.0F);
        this.mesh.chunkSetAngles("Z_Manche2", this.fm.CT.getAileron() * 0.0F, 0.0F, this.fm.CT.getElevator() * 6F);
        this.mesh.chunkSetAngles("Z_MancheBrakes", this.cvt(this.fm.CT.BrakeControl, 0.0F, 1.0F, 0.0F, 8F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Boite", 0.0F, this.cvt(0.0F, 0.0F, 1.0F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Starter3", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition50, this.setOld.stbyPosition50, f), 0.0F, 1.0F, 0.0F, -90F), 0.0F);
        this.mesh.chunkSetAngles("Z_Starter2", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition51, this.setOld.stbyPosition51, f), 0.0F, 1.0F, 0.0F, 120F), 0.0F);
        this.mesh.chunkSetAngles("Z_Instincteur", 0.0F, this.cvt(this.interp(this.setNew.stbyPosition59, this.setOld.stbyPosition59, f), 0.0F, 1.0F, 0.0F, -90F), 0.0F);
        this.mesh.chunkSetAngles("Z_Rechauffe2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 3000F, 10000F, 0.0F, 72F), 0.0F);
        this.mesh.chunkVisible("Z_BoiteInt", true);
        this.mesh.chunkVisible("Breakers", true);
        this.mesh.chunkVisible("Breakers2", true);
        this.mesh.chunkVisible("Z_Battery", true);
        this.mesh.chunkSetAngles("Z_BrakeLIndicator", this.cvt(this.fm.CT.BrakeControl, 0.0F, 1.0F, 0.0F, 76.6F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BrakeRIndicator", this.cvt(this.fm.CT.BrakeControl, 0.0F, 1.0F, 0.0F, -76.6F), 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_Z_RETICLE", true);
        if (this.fm.CT.BayDoorControl == 1.0F) this.mesh.chunkVisible("Z_YellowLight2", true);
        else this.mesh.chunkVisible("Z_YellowLight2", false);
        if (this.fm.CT.getStepControl() >= 0.95F || this.fm.CT.getStepControlAuto()) {
            this.mesh.chunkVisible("Z_PropGreenLight", true);
            this.mesh.chunkVisible("Z_PropRedLight", false);
        } else if (this.fm.CT.getStepControl() < 0.95F && !this.fm.CT.getStepControlAuto()) {
            this.mesh.chunkVisible("Z_PropGreenLight", false);
            this.mesh.chunkVisible("Z_PropRedLight", true);
        }
        if (this.fm.CT.getGear() == 0.0F) {
            this.mesh.chunkVisible("Z_GearLRed1", true);
            this.mesh.chunkVisible("Z_GearRRed1", true);
        } else {
            this.mesh.chunkVisible("Z_GearLRed1", false);
            this.mesh.chunkVisible("Z_GearRRed1", false);
        }
        if (this.fm.CT.getGear() == 1.0F) {
            this.mesh.chunkVisible("Z_GearLGreen1", true);
            this.mesh.chunkVisible("Z_GearRGreen1", true);
        } else {
            this.mesh.chunkVisible("Z_GearLGreen1", false);
            this.mesh.chunkVisible("Z_GearRGreen1", false);
        }
        if (this.fm.getAltitude() > 3000F) this.mesh.chunkVisible("Z_RedLight", true);
        else this.mesh.chunkVisible("Z_RedLight", false);
        this.mesh.chunkVisible("Z_Starter1", true);
        this.mesh.chunkVisible("Z_Starter2", true);
        this.mesh.chunkVisible("Z_Primer6", false);
        this.mesh.chunkVisible("Z_ComprSelect6", false);
        if (((D520) this.aircraft()).CanopyEjectState) {
            this.fm.CT.bHasCockpitDoorControl = false;
            this.mesh.chunkVisible("Top", false);
            this.mesh.chunkVisible("Top2", false);
            this.mesh.chunkVisible("Top3", false);
            this.mesh.chunkVisible("Z_Holes3", false);
        }
        if (this.fm.CT.getCockpitDoor() == 0.0F && !((D520) this.aircraft()).CanopyEjectState) this.mesh.chunkVisible("Top4", true);
        else this.mesh.chunkVisible("Top4", false);
//        if(this.fm.CT.getCockpitDoor() > 0.01F && this.fm.getSpeedKMH() > 300F)
//        {
//            this.fm.CT.bHasCockpitDoorControl = false;
//            this.mesh.chunkVisible("Top", false);
//            this.mesh.chunkVisible("Top2", false);
//            this.mesh.chunkVisible("Top3", false);
//            this.mesh.chunkVisible("Z_Holes3", false);
//        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.0F, 1.0F, 0.0F, 0.627F);
        this.mesh.chunkSetLocate("Top", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.stbyPosition5, this.setOld.stbyPosition5, f), 0.0F, 1.0F, 0.0F, -0.011F);
        this.mesh.chunkSetLocate("Z_AutoPropLever", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.stbyPosition12, this.setOld.stbyPosition12, f), 0.0F, 1.0F, 0.0F, -0.005F);
        this.mesh.chunkSetLocate("Z_MancheBTir", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getStepControl(), 0.0F, 1.0F, 0.0F, 0.011F);
        this.mesh.chunkSetLocate("Z_StepPropLever", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        float f1;
        if (this.aircraft().isFMTrackMirror()) f1 = this.aircraft().fmTrack().getCockpitAzimuthSpeed();
        else {
            f1 = -this.cvt(this.setNew.azimuth - this.setOld.azimuth, -1F, 1.0F, 0.04F, -0.04F);
            if (this.aircraft().fmTrack() != null) this.aircraft().fmTrack().setCockpitAzimuthSpeed(f1);
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
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.stbyPosition14, this.setOld.stbyPosition14, f), 0.0F, 1.0F, 0.0F, 0.019F);
        this.mesh.chunkSetLocate("Z_RobVolets", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = this.cvt(this.interp(this.setNew.stbyPosition15, this.setOld.stbyPosition15, f), 0.0F, 1.0F, 0.0F, -0.019F);
        this.mesh.chunkSetLocate("Z_RobTrain", Cockpit.xyz, Cockpit.ypr);
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
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    public void reflectCockpitState() {
        if (((D520) this.aircraft()).CockpitDamaged2 || (this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("Z_Holes3", true);
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("Z_Holes1", true);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
        }
        if (((D520) this.aircraft()).CockpitDamaged1 || (this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("Z_Holes1", true);
        if (((D520) this.aircraft()).CockpitDamaged3) this.mesh.chunkVisible("Z_Holes2", true);
        if (((D520) this.aircraft()).CockpitDamaged4 || (this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("InstrumentsD", true);
            this.mesh.chunkVisible("Gauges2", false);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_Speedometer2", false);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Hour1", false);
            this.mesh.chunkVisible("Z_Minute1", false);
            this.mesh.chunkVisible("Z_Second1", false);
            this.mesh.chunkVisible("Z_Holes3", true);
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
        if (((D520) this.aircraft()).CockpitDamaged5) {
            this.mesh.chunkVisible("ColliDMG", true);
            this.mesh.chunkVisible("Z_ColliViseurDMG", true);
            this.mesh.chunkVisible("Colli3", false);
            this.mesh.chunkVisible("Z_ColliViseur", false);
            this.mesh.chunkVisible("Z_Z_RETICLE", false);
            this.mesh.chunkVisible("Z_Z_MASK", false);
        }
        if (((D520) this.aircraft()).OilHit) this.mesh.chunkVisible("Z_OilSplats", true);
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private boolean            CompresseOk;
    private boolean            StarterState1;
    private boolean            StarterState2;
    private static final float speedometerScale[]  = { 0.0F, 12F, 22.5F, 48.5F, 83.1F, 125.5F, 173F, 227F, 285.5F, 347.5F, 414.3F, 482.7F, 551.5F, 607.5F, 690F };
    private static final float speedometer2Scale[] = { 0.0F, 4.2F, 7F, 11F, 15.5F, 19F, 24F, 39.5F, 54.5F, 70.5F, 87F, 104F, 120.5F, 140F, 160.5F, 171F, 216F, 240.5F, 272.5F, 302F, 331.5F };
    private static final float variometerScale[]   = { -108.1F, -89.8F, -72F, -53.5F, -35.5F, -17.5F, 0.0F, 18F, 36F, 54F, 72F, 90F, 108F, 126F, 143.5F, 162F, 180F };
    private static final float rpmScale[]          = { 0.0F, 30F, 90F, 149F, 210F, 270F, 330F };
    private static final float fuelScale[]         = { 0.0F, 35.5F, 71F, 107.5F, 145F, 182.5F, 218.5F, 254F, 284.5F, 298.5F };
    private static final float watertempScale[]    = { 0.0F, 4.5F, 15.2F, 26F, 34F, 43F, 65F, 88F, 111F, 133F, 156F, 202F, 248F, 294F, 340F };
    private static final float oiltempScale[]      = { 0.0F, 4.5F, 15.2F, 26F, 34F, 43F, 65F, 88F, 111F, 133F, 156F, 202F, 248F, 294F, 340F };
    private static final float admissionScale[]    = { 0.0F, 2.7F, 5.4F, 8F, 11F, 13.5F, 16.5F, 19F, 59F, 99F, 139F, 180F, 220.5F, 260F, 300F, 339F };

    static {
        Property.set(CockpitD520.class, "normZN", 0.4F);
        Property.set(CockpitD520.class, "gsZN", 0.4F);
    }
}
