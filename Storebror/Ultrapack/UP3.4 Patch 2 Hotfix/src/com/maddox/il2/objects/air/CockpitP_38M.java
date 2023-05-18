package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitP_38M extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitP_38M.this.bNeedSetUp) {
                CockpitP_38M.this.reflectPlaneMats();
                CockpitP_38M.this.bNeedSetUp = false;
            }
            if (((P_38) CockpitP_38M.this.aircraft()).bChangedPit) {
                CockpitP_38M.this.reflectPlaneToModel();
                ((P_38) CockpitP_38M.this.aircraft()).bChangedPit = false;
            }
            if (CockpitP_38M.this.fm != null) {
                CockpitP_38M.this.setTmp = CockpitP_38M.this.setOld;
                CockpitP_38M.this.setOld = CockpitP_38M.this.setNew;
                CockpitP_38M.this.setNew = CockpitP_38M.this.setTmp;
                CockpitP_38M.this.setNew.trim = (0.92F * CockpitP_38M.this.setOld.trim) + (0.08F * CockpitP_38M.this.fm.CT.getTrimElevatorControl());
                CockpitP_38M.this.setNew.throttle1 = (0.85F * CockpitP_38M.this.setOld.throttle1) + (CockpitP_38M.this.fm.EI.engines[0].getControlThrottle() * 0.15F);
                CockpitP_38M.this.setNew.throttle2 = (0.85F * CockpitP_38M.this.setOld.throttle2) + (CockpitP_38M.this.fm.EI.engines[1].getControlThrottle() * 0.15F);
                CockpitP_38M.this.setNew.prop1 = (0.85F * CockpitP_38M.this.setOld.prop1) + (CockpitP_38M.this.fm.EI.engines[0].getControlProp() * 0.15F);
                CockpitP_38M.this.setNew.prop2 = (0.85F * CockpitP_38M.this.setOld.prop2) + (CockpitP_38M.this.fm.EI.engines[1].getControlProp() * 0.15F);
                CockpitP_38M.this.setNew.mix1 = (0.85F * CockpitP_38M.this.setOld.mix1) + (CockpitP_38M.this.fm.EI.engines[0].getControlMix() * 0.15F);
                CockpitP_38M.this.setNew.mix2 = (0.85F * CockpitP_38M.this.setOld.mix2) + (CockpitP_38M.this.fm.EI.engines[1].getControlMix() * 0.15F);
                CockpitP_38M.this.setNew.altimeter = CockpitP_38M.this.fm.getAltitude();
                float f = CockpitP_38M.this.waypointAzimuth();
                CockpitP_38M.this.setNew.azimuth.setDeg(CockpitP_38M.this.setOld.azimuth.getDeg(1.0F), CockpitP_38M.this.fm.Or.azimut());
                CockpitP_38M.this.setNew.waypointAzimuth.setDeg(CockpitP_38M.this.setOld.waypointAzimuth.getDeg(1.0F), f);
                if (CockpitP_38M.this.useRealisticNavigationInstruments()) {
                    CockpitP_38M.this.setNew.waypointAzimuth.setDeg(f - 90F);
                    CockpitP_38M.this.setOld.waypointAzimuth.setDeg(f - 90F);
                } else {
                    CockpitP_38M.this.setNew.waypointAzimuth.setDeg(CockpitP_38M.this.setOld.waypointAzimuth.getDeg(0.1F), f);
                }
                CockpitP_38M.this.setNew.vspeed = ((199F * CockpitP_38M.this.setOld.vspeed) + CockpitP_38M.this.fm.getVertSpeed()) / 200F;
                float d = ((P_38M) CockpitP_38M.this.aircraft()).k14Distance;
                CockpitP_38M.this.setNew.k14w = (5F * CockpitP_38M.k14TargetWingspanScale[((P_38M) CockpitP_38M.this.aircraft()).k14WingspanType]) / d;
                CockpitP_38M.this.setNew.k14w = (0.9F * CockpitP_38M.this.setOld.k14w) + (0.1F * CockpitP_38M.this.setNew.k14w);
                CockpitP_38M.this.setNew.k14wingspan = (0.9F * CockpitP_38M.this.setOld.k14wingspan) + (0.1F * CockpitP_38M.k14TargetMarkScale[((P_38M) CockpitP_38M.this.aircraft()).k14WingspanType]);
                CockpitP_38M.this.setNew.k14mode = (0.8F * CockpitP_38M.this.setOld.k14mode) + (0.2F * ((P_38M) CockpitP_38M.this.aircraft()).k14Mode);
                Vector3d w = CockpitP_38M.this.aircraft().FM.getW();
                double t = 0.00125D * d;
                float x = (float) Math.toDegrees(t * w.z);
                float y = -(float) Math.toDegrees(t * w.y);
                float dy = CockpitP_38M.this.floatindex((d - 200F) * 0.04F, CockpitP_38M.k14BulletDrop) - CockpitP_38M.k14BulletDrop[0];
                y += (float) Math.toDegrees(Math.atan(dy / d));
                CockpitP_38M.this.setNew.k14x = (0.92F * CockpitP_38M.this.setOld.k14x) + (0.08F * x);
                CockpitP_38M.this.setNew.k14y = (0.92F * CockpitP_38M.this.setOld.k14y) + (0.08F * y);
                if (CockpitP_38M.this.setNew.k14x > 7F) {
                    CockpitP_38M.this.setNew.k14x = 7F;
                }
                if (CockpitP_38M.this.setNew.k14x < -7F) {
                    CockpitP_38M.this.setNew.k14x = -7F;
                }
                if (CockpitP_38M.this.setNew.k14y > 7F) {
                    CockpitP_38M.this.setNew.k14y = 7F;
                }
                if (CockpitP_38M.this.setNew.k14y < -7F) {
                    CockpitP_38M.this.setNew.k14y = -7F;
                }
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
        float      altimeter;
        float      vspeed;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      trim;
        float      k14wingspan;
        float      k14mode;
        float      k14x;
        float      k14y;
        float      k14w;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitP_38M() {
        super("3DO/Cockpit/P-38M/hierM.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictFlap = 0.0F;
        this.pictGear = 0.0F;
        this.hydrPressureAngle = 118F;
        this.bNeedSetUp = true;
        this.cockpitNightMats = (new String[] { "gauges1", "gauges1_dam", "gauges2", "gauges2_dam", "gauges3", "gauges3_dam", "gauges4", "gauges4_dam", "gauges5", "leftcontroles", "misc2", "swbox", "swbox2" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.printCompassHeading = true;
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("BMR_D0", false);
            return true;
        } else {
            return false;
        }
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("BMR_D0", this.aircraft().hierMesh().isChunkVisible("Blister1_D0"));
        super.doFocusLeave();
    }

    public void reflectWorldToInstruments(float delta) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (((P_38) this.aircraft()).bChangedPit) {
            this.reflectPlaneToModel();
            ((P_38) this.aircraft()).bChangedPit = false;
        }
        int i;
        TypeRadarAPSCarrier apsc = (TypeRadarAPSCarrier) this.aircraft();
        if (apsc.getRadarAPS().hasMessages()) {
            this.mesh.chunkSetAngles("RadarSWGun", 0.0F, 0.0F, 60F);
        } else {
            this.mesh.chunkSetAngles("RadarSWGun", 0.0F, 0.0F, 0.0F);
        }
        if (apsc.getRadarAPS().hasPower()) {
            this.mesh.chunkVisible("RadarScreen", true);
            this.mesh.chunkVisible("RadarScreenOFF", false);
            this.mesh.chunkSetAngles("RadarSWOn", 0.0F, 30F, 0.0F);
            apsc.getRadarAPS().radarScanCockpit(this.mesh, 10, 4);
        } else {
            this.mesh.chunkVisible("RadarScreen", false);
            this.mesh.chunkVisible("RadarScreenOFF", true);
            this.mesh.chunkSetAngles("RadarSWOn", 0.0F, 0.0F, 0.0F);
            apsc.getRadarAPS().clearRadarMarks(this.mesh, 10, 4);
        }
        i = ((P_38M) this.aircraft()).k14Mode;
        boolean bVis = i < 2;
        this.mesh.chunkVisible("Z_Z_RETICLE", bVis);
        bVis = i > 0;
        this.mesh.chunkVisible("Z_Z_RETICLE1", bVis);
        this.mesh.chunkSetAngles("Z_Z_RETICLE1", 0.0F, this.setNew.k14x, this.setNew.k14y);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.setNew.k14w;
        for (i = 1; i < 7; i++) {
            this.mesh.chunkVisible("Z_Z_AIMMARK" + i, bVis);
            this.mesh.chunkSetLocate("Z_Z_AIMMARK" + i, Cockpit.xyz, Cockpit.ypr);
        }

        this.mesh.chunkSetAngles("Z_Trim1", -1722F * this.setNew.trim, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps1", 60F * (this.pictFlap = (0.85F * this.pictFlap) + (0.15F * this.fm.CT.FlapsControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Gear1", 90F * (this.pictGear = (0.85F * this.pictGear) + (0.15F * this.fm.CT.GearControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", 93.1F * this.setNew.throttle1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", 93.1F * this.setNew.throttle2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", 95F * this.setNew.prop1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", 95F * this.setNew.prop2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 90F * this.setNew.mix1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2", 90F * this.setNew.mix2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RightPedal", 0.0F, 0.0F, 16F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_RPedalStep", 0.0F, 0.0F, (16F * this.fm.CT.getRudder()) + (30F * this.fm.CT.getBrake()));
        this.mesh.chunkSetAngles("Z_LeftPedal", 0.0F, 0.0F, -16F * this.fm.CT.getRudder());
        this.mesh.chunkSetAngles("Z_LPedalStep", 0.0F, 0.0F, (-16F * this.fm.CT.getRudder()) + (30F * this.fm.CT.getBrake()));
        this.mesh.chunkSetAngles("Z_Column", 0.0F, 0.0F, -(this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 70F);
        this.mesh.chunkSetAngles("Z_Columnbase", 0.0F, 0.0F, (this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[0]) {
            Cockpit.xyz[2] = 0.01065F;
        }
        this.mesh.chunkSetLocate("Z_Columnbutton1", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (this.fm.CT.saveWeaponControl[1]) {
            Cockpit.xyz[2] = 0.01065F;
        }
        this.mesh.chunkSetLocate("Z_Columnbutton2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, delta), 0.0F, 30480F, 0.0F, 36000F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, delta), 0.0F, 30480F, 0.0F, 3600F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()) * 0.621371F, 0.0F, 700F, 0.0F, 14F), CockpitP_38M.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", this.setNew.azimuth.getDeg(delta), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", this.setNew.azimuth.getDeg(delta), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", 90F + this.setNew.waypointAzimuth.getDeg(delta * 0.1F), 0.0F, 0.0F);
        float f = 0.0F;
        if (this.fm.AS.bLandingLightOn) {
            f -= 35F;
        }
        if (this.fm.AS.bNavLightsOn) {
            f -= 5F;
        }
        if (this.cockpitLightControl) {
            f -= 2.87F;
        }
        this.mesh.chunkSetAngles("Z_Amper1", this.cvt(f + this.cvt(this.fm.EI.engines[0].getRPM(), 150F, 2380F, 0.0F, 41.1F), -20F, 130F, -10F, 80F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Amper2", this.cvt(f + this.cvt(this.fm.EI.engines[1].getRPM(), 150F, 2380F, 0.0F, 41.1F), -20F, 130F, -10F, 80F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Carbair1", this.cvt((Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F) + (25F * this.fm.EI.engines[0].getPowerOutput()), -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Carbair2", this.cvt((Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F) + (25F * this.fm.EI.engines[1].getPowerOutput()), -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), CockpitP_38M.variometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Coolant1", this.cvt(this.fm.EI.engines[0].tWaterOut, -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Coolant2", this.cvt(this.fm.EI.engines[1].tWaterOut, -70F, 150F, -38.5F, 87.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tOilIn, -50F, 150F, 0.0F, 127.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[1].tOilIn, -50F, 150F, 0.0F, 127.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank1", -this.fm.Or.getKren(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.031F, -0.031F);
        this.mesh.chunkSetLocate("Z_TurnBank2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.M.fuel, 0.0F, 245.2F, -17F, 126F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel2", this.cvt(this.fm.M.fuel, 0.0F, 245.2F, -17F, 126F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel3", this.cvt(this.fm.M.fuel, 245.2F, 490.4F, -17F, 126F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Fuel4", this.cvt(this.fm.M.fuel, 245.2F, 490.4F, -17F, 126F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(this.getBall(7D), -7F, 7F, -16F, 16F), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank4", this.cvt(this.w.z, -0.23562F, 0.23562F, 29.5F, -29.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold1", this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.370465F, -4F, 342F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Manifold2", this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.3386378F, 2.370465F, -4F, 342F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", this.cvt((60F + (this.fm.EI.engines[0].tOilIn * 0.222F)) * this.fm.EI.engines[0].getReadyness(), 0.0F, 200F, -2.5F, 182F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil2", this.cvt((60F + (this.fm.EI.engines[1].tOilIn * 0.222F)) * this.fm.EI.engines[1].getReadyness(), 0.0F, 200F, -2.5F, 182F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuelpress1", this.cvt(this.fm.M.fuel <= 1.0F ? this.fm.M.fuel * 0.26F : 0.26F, 0.0F, 0.4F, -5F, 184.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_fuelpress2", this.cvt(this.fm.M.fuel <= 1.0F ? this.fm.M.fuel * 0.26F : 0.26F, 0.0F, 0.4F, -5F, 184.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 331F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4500F, 0.0F, 331F), 0.0F, 0.0F);
        float convergeHydrPress = 118F;
        if ((Math.abs(this.pictFlap - this.fm.CT.getFlap()) > 0.01D) || (Math.abs(this.pictGear - this.fm.CT.getGear()) > 0.01D)) {
            convergeHydrPress = 54F;
        }
        this.hydrPressureAngle = (0.95F * this.hydrPressureAngle) + (0.05F * convergeHydrPress);
        this.mesh.chunkSetAngles("Z_Pres1", this.hydrPressureAngle, 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_GearGreen1", (this.fm.CT.getGearL() > 0.99F) && (this.fm.CT.getGearR() > 0.99F));
        this.mesh.chunkVisible("Z_GearRed1", ((this.fm.CT.getGearL() < 0.01F) && (this.fm.CT.getGearR() < 0.01F)) || !this.fm.Gears.lgear || !this.fm.Gears.rgear);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("gauges1_dam", true);
            this.mesh.chunkVisible("gauges1", false);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Hour1", false);
            this.mesh.chunkVisible("Z_Minute1", false);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("Z_RPM2", false);
            this.mesh.chunkVisible("Z_Coolant1", false);
            this.mesh.chunkVisible("Z_Coolant2", false);
            this.mesh.chunkVisible("Z_Compass3", false);
        }
        this.fm.AS.getClass();
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("XHullDamage2", true);
            this.mesh.chunkVisible("XHullDamage3", true);
            this.mesh.chunkVisible("gauges2_dam", true);
            this.mesh.chunkVisible("gauges2", false);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_TurnBank1", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
            this.mesh.chunkVisible("Z_Climb1", false);
            this.mesh.chunkVisible("Z_Manifold1", false);
            this.mesh.chunkVisible("Z_Manifold2", false);
            this.mesh.chunkVisible("Z_Amper2", false);
            this.mesh.chunkVisible("gauges3_dam", true);
            this.mesh.chunkVisible("gauges3", false);
            this.mesh.chunkVisible("Z_Fuel1", false);
            this.mesh.chunkVisible("Z_Fuel2", false);
            this.mesh.chunkVisible("Z_Compass1", false);
            this.mesh.chunkVisible("Z_Compass2", false);
            this.mesh.chunkVisible("Z_TurnBank3", false);
            this.mesh.chunkVisible("Z_TurnBank4", false);
            this.mesh.chunkVisible("Z_Temp1", false);
            this.mesh.chunkVisible("Z_Oil1", false);
            this.mesh.chunkVisible("Z_fuelpress1", false);
            this.mesh.chunkVisible("Z_GearGreen1", false);
            this.mesh.chunkVisible("Z_GearRed1", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XHullDamage4", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("Z_OilSplats_D1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XHullDamage1", true);
            this.mesh.chunkVisible("XHullDamage3", true);
            this.mesh.chunkVisible("gauges2_dam", true);
            this.mesh.chunkVisible("gauges2", false);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_TurnBank1", false);
            this.mesh.chunkVisible("Z_TurnBank2", false);
            this.mesh.chunkVisible("Z_Climb1", false);
            this.mesh.chunkVisible("Z_Manifold1", false);
            this.mesh.chunkVisible("Z_Manifold2", false);
            this.mesh.chunkVisible("Z_Amper2", false);
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

    protected void reflectPlaneMats() {
        HierMesh h = this.aircraft().hierMesh();
        Mat m = h.material(h.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", m);
        m = h.material(h.materialFind("Gloss1D1o"));
        this.mesh.materialReplace("Gloss1D1o", m);
        m = h.material(h.materialFind("Gloss2D2o"));
        this.mesh.materialReplace("Gloss2D2o", m);
    }

    protected void reflectPlaneToModel() {
        HierMesh h = this.aircraft().hierMesh();
        this.mesh.chunkVisible("WingLIn_D0", h.isChunkVisible("WingLIn_D0"));
        this.mesh.chunkVisible("WingLIn_D1", h.isChunkVisible("WingLIn_D1"));
        this.mesh.chunkVisible("WingLIn_D2", h.isChunkVisible("WingLIn_D2"));
        this.mesh.chunkVisible("WingLIn_D3", h.isChunkVisible("WingLIn_D3"));
        this.mesh.chunkVisible("WingRIn_D0", h.isChunkVisible("WingRIn_D0"));
        this.mesh.chunkVisible("WingRIn_D1", h.isChunkVisible("WingRIn_D1"));
        this.mesh.chunkVisible("WingRIn_D2", h.isChunkVisible("WingRIn_D2"));
        this.mesh.chunkVisible("WingRIn_D3", h.isChunkVisible("WingRIn_D3"));
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictFlap;
    private float              pictGear;
    private float              hydrPressureAngle;
    private boolean            bNeedSetUp;
    private static final float speedometerScale[]       = { 0.0F, 8.5F, 41F, 84F, 134F, 186F, 233.5F, 246.75F, 260F, 273.25F, 286.5F, 300.25F, 314F, 328F, 342F };
    private static final float variometerScale[]        = { -170F, -147F, -124F, -101F, -78F, -48F, 0.0F, 48F, 78F, 101F, 124F, 147F, 170F };
    private static final float k14TargetMarkScale[]     = { -0F, -4.5F, -27.5F, -42.5F, -56.5F, -61.5F, -70F, -95F, -102.5F, -106F };
    private static final float k14TargetWingspanScale[] = { 9.9F, 10.52F, 13.8F, 16.34F, 19F, 20F, 22F, 29.25F, 30F, 32.85F };
    private static final float k14BulletDrop[]          = { 5.812F, 6.168F, 6.508F, 6.978F, 7.24F, 7.576F, 7.849F, 8.108F, 8.473F, 8.699F, 8.911F, 9.111F, 9.384F, 9.554F, 9.787F, 9.928F, 9.992F, 10.282F, 10.381F, 10.513F, 10.603F, 10.704F, 10.739F, 10.782F, 10.789F };
    static {
        Property.set(CockpitP_38M.class, "normZNs", new float[] { 1.18F, 0.83F, 0.95F, 0.83F });
    }

}
