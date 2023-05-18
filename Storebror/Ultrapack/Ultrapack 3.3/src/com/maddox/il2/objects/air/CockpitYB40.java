package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitYB40 extends CockpitPilot {
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
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float      vspeed;
        float      PDI;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.radioCompassAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitYB40.this.fm != null) {
                if (CockpitYB40.this.bNeedSetUp) {
                    CockpitYB40.this.reflectPlaneMats();
                    CockpitYB40.this.bNeedSetUp = false;
                }
                CockpitYB40.this.setTmp = CockpitYB40.this.setOld;
                CockpitYB40.this.setOld = CockpitYB40.this.setNew;
                CockpitYB40.this.setNew = CockpitYB40.this.setTmp;
                CockpitYB40.this.setNew.throttle1 = 0.85F * CockpitYB40.this.setOld.throttle1 + CockpitYB40.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
                CockpitYB40.this.setNew.throttle2 = 0.85F * CockpitYB40.this.setOld.throttle2 + CockpitYB40.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
                CockpitYB40.this.setNew.throttle3 = 0.85F * CockpitYB40.this.setOld.throttle3 + CockpitYB40.this.fm.EI.engines[2].getControlThrottle() * 0.15F;
                CockpitYB40.this.setNew.throttle4 = 0.85F * CockpitYB40.this.setOld.throttle4 + CockpitYB40.this.fm.EI.engines[3].getControlThrottle() * 0.15F;
                CockpitYB40.this.setNew.prop1 = 0.85F * CockpitYB40.this.setOld.prop1 + CockpitYB40.this.fm.EI.engines[0].getControlProp() * 0.15F;
                CockpitYB40.this.setNew.prop2 = 0.85F * CockpitYB40.this.setOld.prop2 + CockpitYB40.this.fm.EI.engines[1].getControlProp() * 0.15F;
                CockpitYB40.this.setNew.prop3 = 0.85F * CockpitYB40.this.setOld.prop3 + CockpitYB40.this.fm.EI.engines[2].getControlProp() * 0.15F;
                CockpitYB40.this.setNew.prop4 = 0.85F * CockpitYB40.this.setOld.prop4 + CockpitYB40.this.fm.EI.engines[3].getControlProp() * 0.15F;
                CockpitYB40.this.setNew.mix1 = 0.85F * CockpitYB40.this.setOld.mix1 + CockpitYB40.this.fm.EI.engines[0].getControlMix() * 0.15F;
                CockpitYB40.this.setNew.mix2 = 0.85F * CockpitYB40.this.setOld.mix1 + CockpitYB40.this.fm.EI.engines[1].getControlMix() * 0.15F;
                CockpitYB40.this.setNew.mix3 = 0.85F * CockpitYB40.this.setOld.mix1 + CockpitYB40.this.fm.EI.engines[2].getControlMix() * 0.15F;
                CockpitYB40.this.setNew.mix4 = 0.85F * CockpitYB40.this.setOld.mix1 + CockpitYB40.this.fm.EI.engines[3].getControlMix() * 0.15F;
                CockpitYB40.this.setNew.altimeter = CockpitYB40.this.fm.getAltitude();
                float f = CockpitYB40.this.waypointAzimuth();
                CockpitYB40.this.setNew.azimuth.setDeg(CockpitYB40.this.setOld.azimuth.getDeg(1.0F), CockpitYB40.this.fm.Or.azimut());
                CockpitYB40.this.setNew.waypointAzimuth.setDeg(f);
                CockpitYB40.this.setOld.waypointAzimuth.setDeg(f);
                if (CockpitYB40.this.b17 != null) CockpitYB40.this.setNew.vspeed = (199F * CockpitYB40.this.setOld.vspeed + CockpitYB40.this.fm.getVertSpeed()) / 200F;
                CockpitYB40.this.mesh.chunkSetAngles("Turret", 0.0F, CockpitYB40.this.aircraft().FM.turret[3].tu[0], 0.0F);
                CockpitYB40.this.mesh.chunkSetAngles("TurretB", 0.0F, CockpitYB40.this.aircraft().FM.turret[3].tu[1], 0.0F);
            }
            return true;
        }

        Interpolater() {
        }
    }

    public CockpitYB40() {
        super("3DO/Cockpit/YB-40-P/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.bNeedSetUp = true;
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictFlap = 0.0F;
        this.pictManf1 = 1.0F;
        this.pictManf2 = 1.0F;
        this.pictManf3 = 1.0F;
        this.pictManf4 = 1.0F;
        this.b17 = null;
        this.cockpitNightMats = new String[] { "Gauge_1", "Gauge_1DMG", "Gauge_2", "Gauge_2DMG", "Gauge_3", "Gauge_3DMG", "Gauge_4", "Gauge_5", "Gauge_5DMG", "Gauge_6", "Gauge_6DMG", "Gauge_7", "Gauges1", "Gauge_1DMG", "Gauges_2DMG", "Gauge_3DMG",
                "Gauges_5DMG", "Gauge_6DMG" };
        this.setNightMats(false);
        if (this.aircraft() instanceof YB_40abc) this.b17 = (YB_40abc) this.aircraft();
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.2F);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Column", 0.0F, (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 8F, 0.0F);
        this.mesh.chunkSetAngles("LYoke", 0.0F, (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 68F, 0.0F);
        this.mesh.chunkSetAngles("RYoke", 0.0F, (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 68F, 0.0F);
        this.mesh.chunkSetAngles("RPedal", 0.0F, 10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("LPedal", 0.0F, -10F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("zFlaps", 0.0F, 180F * (this.pictFlap = 0.75F * this.pictFlap + 0.25F * this.fm.CT.FlapsControl), 0.0F);
        this.mesh.chunkSetAngles("Radiator1", 0.0F, -40F * this.fm.EI.engines[0].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Radiator2", 0.0F, -40F * this.fm.EI.engines[1].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Radiator3", 0.0F, -40F * this.fm.EI.engines[2].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Radiator4", 0.0F, -40F * this.fm.EI.engines[3].getControlRadiator(), 0.0F);
        this.mesh.chunkSetAngles("Mix1", 0.0F, -40F * this.interp(this.setNew.mix1, this.setOld.mix1, f), 0.0F);
        this.mesh.chunkSetAngles("Mix2", 0.0F, -40F * this.interp(this.setNew.mix2, this.setOld.mix2, f), 0.0F);
        this.mesh.chunkSetAngles("Mix3", 0.0F, -40F * this.interp(this.setNew.mix3, this.setOld.mix3, f), 0.0F);
        this.mesh.chunkSetAngles("Mix4", 0.0F, -40F * this.interp(this.setNew.mix4, this.setOld.mix4, f), 0.0F);
        this.mesh.chunkSetAngles("Pitch1", 0.0F, -65F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F);
        this.mesh.chunkSetAngles("Pitch2", 0.0F, -65F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F);
        this.mesh.chunkSetAngles("Pitch3", 0.0F, -65F * this.interp(this.setNew.prop3, this.setOld.prop3, f), 0.0F);
        this.mesh.chunkSetAngles("Pitch4", 0.0F, -65F * this.interp(this.setNew.prop4, this.setOld.prop4, f), 0.0F);
        this.mesh.chunkSetAngles("Throttle1", 0.0F, -40F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F);
        this.mesh.chunkSetAngles("Throttle2", 0.0F, -40F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F);
        this.mesh.chunkSetAngles("Throttle3", 0.0F, -40F * this.interp(this.setNew.throttle3, this.setOld.throttle3, f), 0.0F);
        this.mesh.chunkSetAngles("Throttle4", 0.0F, -40F * this.interp(this.setNew.throttle4, this.setOld.throttle4, f), 0.0F);
        this.mesh.chunkSetAngles("ZclockH", 0.0F, this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F);
        this.mesh.chunkSetAngles("ZclockM", 0.0F, this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F);
        this.mesh.chunkSetAngles("ZAH1", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.0365F, -0.0365F);
        this.mesh.chunkSetLocate("ZAH2", Cockpit.xyz, Cockpit.ypr);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) this.mesh.chunkSetAngles("ZClimb", 0.0F, this.floatindex(this.cvt(this.setNew.vspeed, -30.48F, 30.48F, 0.0F, 12F), variometerScale), 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Zslip2", 0.0F, this.cvt(this.w.z, -0.23562F, 0.23562F, 23F, -23F), 0.0F);
        this.mesh.chunkSetAngles("Zslip", 0.0F, this.cvt(this.getBall(6D), -6F, 6F, -11.5F, 11.5F), 0.0F);
        this.mesh.chunkSetAngles("Zpdi", 0.0F, this.cvt(this.setNew.PDI, -30F, 30F, -46.5F, 46.5F), 0.0F);
        this.mesh.chunkSetAngles("Zspeed", 0.0F, this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 836.859F, 0.0F, 13F), speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("zAlt2", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 10800F), 0.0F);
        this.mesh.chunkSetAngles("zAlt1", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 9144F, 0.0F, 1080F), 0.0F);
        this.mesh.chunkSetAngles("zCompass1", 0.0F, -0.5F * this.setNew.azimuth.getDeg(f) + 5F, 0.0F);
        this.mesh.chunkSetAngles("zCompass2", 0.0F, -0.5F * this.setNew.azimuth.getDeg(f) + 5F, 0.0F);
        this.mesh.chunkSetAngles("ZRadioCompass", 0.0F, this.setNew.radioCompassAzimuth.getDeg(f * 0.02F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("ZWPCompass2", 0.0F, this.setNew.azimuth.getDeg(f) + 90F, 0.0F);
            this.mesh.chunkSetAngles("ZWPCompass", 0.0F, this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Zrpm1", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            this.mesh.chunkSetAngles("Zrpm2", 0.0F, this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            this.mesh.chunkSetAngles("Zrpm3", 0.0F, this.cvt(this.fm.EI.engines[2].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            this.mesh.chunkSetAngles("Zrpm4", 0.0F, this.cvt(this.fm.EI.engines[3].getRPM(), 0.0F, 4500F, 0.0F, 323F), 0.0F);
            this.mesh.chunkSetAngles("ZPress1", 0.0F, this.pictManf1 = 0.9F * this.pictManf1 + 0.1F * this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
            this.mesh.chunkSetAngles("ZPress2", 0.0F, this.pictManf2 = 0.9F * this.pictManf2 + 0.1F * this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
            this.mesh.chunkSetAngles("ZPress3", 0.0F, this.pictManf3 = 0.9F * this.pictManf3 + 0.1F * this.cvt(this.fm.EI.engines[2].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
            this.mesh.chunkSetAngles("ZPress4", 0.0F, this.pictManf4 = 0.9F * this.pictManf4 + 0.1F * this.cvt(this.fm.EI.engines[3].getManifoldPressure(), 0.3386378F, 2.539784F, 0.0F, 346.5F), 0.0F);
        }
        this.mesh.chunkSetAngles("ZFuelPress1", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("ZFuelPress2", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("ZFuelPress3", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("ZFuelPress4", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.78F : 0.0F, 0.0F, 3.25F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("ZCarbTemp1", 0.0F, -this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -50F, 150F, -25F, 75F), 0.0F);
        this.mesh.chunkSetAngles("ZCarbTemp2", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -50F, 150F, -25F, 75F), 0.0F);
        this.mesh.chunkSetAngles("ZCarbTemp3", 0.0F, -this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -50F, 150F, -25F, 75F), 0.0F);
        this.mesh.chunkSetAngles("ZCarbTemp4", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -50F, 150F, -25F, 75F), 0.0F);
        this.mesh.chunkSetAngles("ZCylTemp1", 0.0F, -this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 102.5F), 0.0F);
        this.mesh.chunkSetAngles("ZCylTemp2", 0.0F, this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 350F, 0.0F, 102.5F), 0.0F);
        this.mesh.chunkSetAngles("ZCylTemp3", 0.0F, -this.cvt(this.fm.EI.engines[2].tWaterOut, 0.0F, 350F, 0.0F, 102.5F), 0.0F);
        this.mesh.chunkSetAngles("ZCylTemp4", 0.0F, this.cvt(this.fm.EI.engines[3].tWaterOut, 0.0F, 350F, 0.0F, 102.5F), 0.0F);
        this.mesh.chunkSetAngles("zOilTemp1", 0.0F, this.cvt(this.fm.EI.engines[0].tOilIn, -70F, 150F, 34F, -76F), 0.0F);
        this.mesh.chunkSetAngles("zOilTemp2", 0.0F, -this.cvt(this.fm.EI.engines[1].tOilIn, -70F, 150F, 34F, -76F), 0.0F);
        this.mesh.chunkSetAngles("zOilTemp3", 0.0F, this.cvt(this.fm.EI.engines[2].tOilIn, -70F, 150F, 34F, -76F), 0.0F);
        this.mesh.chunkSetAngles("zOilTemp4", 0.0F, -this.cvt(this.fm.EI.engines[3].tOilIn, -70F, 150F, 34F, -76F), 0.0F);
        this.mesh.chunkSetAngles("ZOilpress1", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("ZOilpress2", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("ZOilpress3", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[2].tOilOut * this.fm.EI.engines[2].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("ZOilpress4", 0.0F, this.cvt(1.0F + 0.05F * this.fm.EI.engines[3].tOilOut * this.fm.EI.engines[3].getReadyness(), 0.0F, 13.49F, 0.0F, 270F), 0.0F);
        this.mesh.chunkSetAngles("ZFuel", 0.0F, this.cvt(this.fm.M.fuel, 0.0F, 7500F, 0.0F, 80F), 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) this.mesh.chunkSetAngles("ZFreeAir", 0.0F, this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -70F, 150F, -26.6F, 57F), 0.0F);
        float f1 = 0.5F * this.fm.EI.engines[0].getRPM() + 0.5F * this.fm.EI.engines[1].getRPM();
        f1 = 2.5F * (float) Math.sqrt(Math.sqrt(Math.sqrt(Math.sqrt(f1))));
        this.mesh.chunkSetAngles("ZSuction", 0.0F, this.cvt(f1, 0.0F, 10F, 0.0F, 297F), 0.0F);
        this.mesh.chunkSetAngles("PitchTrim", -160F * this.fm.CT.getTrimElevatorControl(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("RollTrim", 140F * this.fm.CT.getTrimAileronControl(), 0.0F, 0.0F);
        this.mesh.chunkVisible("Z_GearGreen", this.fm.CT.getGear() > 0.01F && this.fm.CT.getGear() > 0.99F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) != 0);
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("TopPanel2", false);
            this.mesh.chunkVisible("TopPanel2_DMG", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XGlassDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("FPanelCLeft", false);
            this.mesh.chunkVisible("FPanelLeft", false);
            this.mesh.chunkVisible("FPanelCLeft_DMG", true);
            this.mesh.chunkVisible("FPanelLeft_DMG", true);
            this.mesh.chunkVisible("ZSpeed", false);
            this.mesh.chunkVisible("ZAlt1", false);
            this.mesh.chunkVisible("ZAlt2", false);
            this.mesh.chunkVisible("ZClimb", false);
            this.mesh.chunkVisible("Zslip2", false);
            this.mesh.chunkVisible("ZAH1", false);
            this.mesh.chunkVisible("ZAH2", false);
            this.mesh.chunkVisible("BL_Vert", false);
            this.mesh.chunkVisible("BL_Horiz", false);
            this.mesh.chunkVisible("Zpdi", false);
            this.mesh.chunkVisible("ZRadioCompass", false);
            this.mesh.chunkVisible("ZWPCompass", false);
            this.mesh.chunkVisible("ZWPCompass2", false);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("FPanelCRight", false);
            this.mesh.chunkVisible("FPanelRight", false);
            this.mesh.chunkVisible("FPanelCRight_DMG", true);
            this.mesh.chunkVisible("FPanelRight_DMG", true);
            this.mesh.chunkVisible("XGlassDamage5", true);
            this.mesh.chunkVisible("Zrpm1", false);
            this.mesh.chunkVisible("Zrpm2", false);
            this.mesh.chunkVisible("Zrpm3", false);
            this.mesh.chunkVisible("Zrpm4", false);
            this.mesh.chunkVisible("Zpress1", false);
            this.mesh.chunkVisible("Zpress2", false);
            this.mesh.chunkVisible("Zpress3", false);
            this.mesh.chunkVisible("Zpress4", false);
            this.mesh.chunkVisible("ZFlaps", false);
            this.mesh.chunkVisible("ZFuelpress1", false);
            this.mesh.chunkVisible("ZFuelpress2", false);
            this.mesh.chunkVisible("ZFuelpress3", false);
            this.mesh.chunkVisible("ZFuelpress4", false);
            this.mesh.chunkVisible("ZOilpress1", false);
            this.mesh.chunkVisible("ZOilpress2", false);
            this.mesh.chunkVisible("ZOilpress3", false);
            this.mesh.chunkVisible("ZOilpress4", false);
            this.mesh.chunkVisible("ZCylTemp1", false);
            this.mesh.chunkVisible("ZCylTemp2", false);
            this.mesh.chunkVisible("ZCylTemp3", false);
            this.mesh.chunkVisible("ZCylTemp4", false);
            this.mesh.chunkVisible("ZCarbTemp1", false);
            this.mesh.chunkVisible("ZCarbTemp2", false);
            this.mesh.chunkVisible("ZCarbTemp3", false);
            this.mesh.chunkVisible("ZCarbTemp4", false);
            this.mesh.chunkVisible("ZOilTemp1", false);
            this.mesh.chunkVisible("ZOilTemp2", false);
            this.mesh.chunkVisible("ZOilTemp3", false);
            this.mesh.chunkVisible("ZOilTemp4", false);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("XGlassDamage1", true);
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("XGlassDamage6", true);
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("XGlassDamage7", true);
        this.retoggleLight();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
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

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Head5_D0", false);
            this.aircraft().hierMesh().chunkVisible("HMask5_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot5_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot5_D1", false);
            this.aircraft().hierMesh().chunkVisible("Turret4A_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret4B_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Pilot5_D0", true);
        this.aircraft().hierMesh().chunkVisible("Head5_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret4A_D0", true);
        this.aircraft().hierMesh().chunkVisible("Turret4B_D0", true);
        super.doFocusLeave();
    }

    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictFlap;
    private float              pictManf1;
    private float              pictManf2;
    private float              pictManf3;
    private float              pictManf4;
    private YB_40abc           b17;
    private static final float speedometerScale[] = { 0.0F, 2.5F, 59F, 126F, 155.5F, 223.5F, 240F, 254.5F, 271F, 285F, 296.5F, 308.5F, 324F, 338.5F };
    private static final float variometerScale[]  = { -180F, -157F, -130F, -108F, -84F, -46.5F, 0.0F, 46.5F, 84F, 108F, 130F, 157F, 180F };

    static {
        Property.set(CockpitYB40.class, "normZNs", new float[] { 0.75F, 0.75F, 1.27F, 1.27F });
    }
}
