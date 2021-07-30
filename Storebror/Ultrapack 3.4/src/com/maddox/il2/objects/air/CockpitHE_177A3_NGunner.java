package com.maddox.il2.objects.air;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitHE_177A3_NGunner extends CockpitGunner {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitHE_177A3_NGunner.this.fm != null) {
                CockpitHE_177A3_NGunner.this.setTmp = CockpitHE_177A3_NGunner.this.setOld;
                CockpitHE_177A3_NGunner.this.setOld = CockpitHE_177A3_NGunner.this.setNew;
                CockpitHE_177A3_NGunner.this.setNew = CockpitHE_177A3_NGunner.this.setTmp;
                CockpitHE_177A3_NGunner.this.setNew.AirEnemy = 0.95F * CockpitHE_177A3_NGunner.this.setOld.AirEnemy + 0.05F * (((HE_177A3) CockpitHE_177A3_NGunner.this.fm.actor).bAirEnemy ? 1.0F : 0.0F);
                CockpitHE_177A3_NGunner.this.setNew.throttle1 = 0.85F * CockpitHE_177A3_NGunner.this.setOld.throttle1 + CockpitHE_177A3_NGunner.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
                CockpitHE_177A3_NGunner.this.setNew.throttle2 = 0.85F * CockpitHE_177A3_NGunner.this.setOld.throttle2 + CockpitHE_177A3_NGunner.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
                CockpitHE_177A3_NGunner.this.setNew.pictAiler = 0.85F * CockpitHE_177A3_NGunner.this.setOld.pictAiler + 0.15F * CockpitHE_177A3_NGunner.this.cvt(CockpitHE_177A3_NGunner.this.fm.CT.AileronControl, -1F, 1.0F, -1F, 1.0F);
                CockpitHE_177A3_NGunner.this.setNew.pictElev = 0.85F * CockpitHE_177A3_NGunner.this.setOld.pictElev + 0.15F * CockpitHE_177A3_NGunner.this.cvt(CockpitHE_177A3_NGunner.this.fm.CT.ElevatorControl, -1F, 1.0F, -1F, 1.0F);
                CockpitHE_177A3_NGunner.this.setNew.elevTrim = 0.85F * CockpitHE_177A3_NGunner.this.setOld.elevTrim + 0.15F * CockpitHE_177A3_NGunner.this.fm.CT.trimElevator;
                CockpitHE_177A3_NGunner.this.setNew.rudderTrim = 0.85F * CockpitHE_177A3_NGunner.this.setOld.rudderTrim + 0.15F * CockpitHE_177A3_NGunner.this.fm.CT.trimRudder;
                CockpitHE_177A3_NGunner.this.setNew.ailTrim = 0.85F * CockpitHE_177A3_NGunner.this.setOld.ailTrim + 0.15F * CockpitHE_177A3_NGunner.this.fm.CT.trimAileron;
                if (Math.toDegrees(CockpitHE_177A3_NGunner.this.fm.EI.engines[0].getPropPhi()) < 36D) {
                    CockpitHE_177A3_NGunner.this.setNew.prop1 = 0.85F * CockpitHE_177A3_NGunner.this.setOld.prop1 + ((FlightModelMain) CockpitHE_177A3_NGunner.this.fm).EI.engines[0].getControlProp() * 0.15F;
                    CockpitHE_177A3_NGunner.this.setNew._prop1 = CockpitHE_177A3_NGunner.this.cvt(CockpitHE_177A3_NGunner.this.setNew.prop1, 0.0F, 1.0F, -22F, -63F);
                } else {
                    CockpitHE_177A3_NGunner.this.setNew.prop1 = 0.85F * CockpitHE_177A3_NGunner.this.setOld.prop1;
                    CockpitHE_177A3_NGunner.this.setNew._prop1 = CockpitHE_177A3_NGunner.this.cvt(CockpitHE_177A3_NGunner.this.setNew.prop1, 0.0F, 1.0F, 0.0F, -63F);
                }
                if (Math.toDegrees(CockpitHE_177A3_NGunner.this.fm.EI.engines[1].getPropPhi()) < 36D) {
                    CockpitHE_177A3_NGunner.this.setNew.prop2 = 0.85F * CockpitHE_177A3_NGunner.this.setOld.prop2 + ((FlightModelMain) CockpitHE_177A3_NGunner.this.fm).EI.engines[1].getControlProp() * 0.15F;
                    CockpitHE_177A3_NGunner.this.setNew._prop2 = CockpitHE_177A3_NGunner.this.cvt(CockpitHE_177A3_NGunner.this.setNew.prop2, 0.0F, 1.0F, -22F, -63F);
                } else {
                    CockpitHE_177A3_NGunner.this.setNew.prop2 = 0.85F * CockpitHE_177A3_NGunner.this.setOld.prop2;
                    CockpitHE_177A3_NGunner.this.setNew._prop2 = CockpitHE_177A3_NGunner.this.cvt(CockpitHE_177A3_NGunner.this.setNew.prop2, 0.0F, 1.0F, 0.0F, -63F);
                }
                CockpitHE_177A3_NGunner.this.setNew.altimeter = 0.85F * CockpitHE_177A3_NGunner.this.setOld.altimeter + CockpitHE_177A3_NGunner.this.fm.getAltitude() * 0.15F;
                CockpitHE_177A3_NGunner.this.setNew.vspeed = (199F * CockpitHE_177A3_NGunner.this.setOld.vspeed + CockpitHE_177A3_NGunner.this.fm.getVertSpeed()) / 200F;
                CockpitHE_177A3_NGunner.this.setNew.beaconDirection = (10F * CockpitHE_177A3_NGunner.this.setOld.beaconDirection + CockpitHE_177A3_NGunner.this.getBeaconDirection()) / 11F;
                CockpitHE_177A3_NGunner.this.setNew.beaconRange = (10F * CockpitHE_177A3_NGunner.this.setOld.beaconRange + CockpitHE_177A3_NGunner.this.getBeaconRange()) / 11F;
                CockpitHE_177A3_NGunner.this.setNew.pictManf1 = 0.9F * CockpitHE_177A3_NGunner.this.setOld.pictManf1 + 0.1F * CockpitHE_177A3_NGunner.this.cvt(CockpitHE_177A3_NGunner.this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
                CockpitHE_177A3_NGunner.this.setNew.pictManf2 = 0.8F * CockpitHE_177A3_NGunner.this.setOld.pictManf2 + 0.2F * CockpitHE_177A3_NGunner.this.cvt(CockpitHE_177A3_NGunner.this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
                CockpitHE_177A3_NGunner.this.setNew.pictManf3 = 0.9F * CockpitHE_177A3_NGunner.this.setOld.pictManf3 + 0.1F * CockpitHE_177A3_NGunner.this.cvt(CockpitHE_177A3_NGunner.this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
                CockpitHE_177A3_NGunner.this.setNew.pictManf4 = 0.8F * CockpitHE_177A3_NGunner.this.setOld.pictManf4 + 0.2F * CockpitHE_177A3_NGunner.this.cvt(CockpitHE_177A3_NGunner.this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 330F);
                float f = CockpitHE_177A3_NGunner.this.setNew.prevFuel - CockpitHE_177A3_NGunner.this.fm.M.fuel;
                CockpitHE_177A3_NGunner.this.setNew.prevFuel = CockpitHE_177A3_NGunner.this.fm.M.fuel;
                f /= 0.72F;
                f /= Time.tickLenFs();
                f *= 3600F;
                CockpitHE_177A3_NGunner.this.setNew.cons = 0.9F * CockpitHE_177A3_NGunner.this.setOld.cons + 0.1F * f;
                float f1 = CockpitHE_177A3_NGunner.this.fm.EI.engines[0].getEngineForce().x;
                float f2 = CockpitHE_177A3_NGunner.this.fm.EI.engines[1].getEngineForce().x;
                float f3 = CockpitHE_177A3_NGunner.this.setNew.cons;
                CockpitHE_177A3_NGunner.this.setNew.consumptionL = 0.9F * CockpitHE_177A3_NGunner.this.setOld.consumptionL + 0.1F * (f3 * f1) / (f1 + f2 + 1.0F);
                CockpitHE_177A3_NGunner.this.setNew.consumptionR = 0.9F * CockpitHE_177A3_NGunner.this.setOld.consumptionR + 0.1F * (f3 * f2) / (f1 + f2 + 1.0F);
                CockpitHE_177A3_NGunner.this.setNew.bombDoor = 0.9F * CockpitHE_177A3_NGunner.this.setOld.bombDoor + 0.1F * CockpitHE_177A3_NGunner.this.fm.CT.getBayDoor();
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float throttle1;
        float throttle2;
        float pictAiler;
        float pictElev;
        float elevTrim;
        float rudderTrim;
        float ailTrim;
        float prop1;
        float _prop1;
        float prop2;
        float _prop2;
        float altimeter;
        float vspeed;
        float beaconDirection;
        float beaconRange;
        float pictManf1;
        float pictManf2;
        float pictManf3;
        float pictManf4;
        float prevFuel;
        float cons;
        float consumptionL;
        float consumptionR;
        float bombDoor;
        float AirEnemy;

        private Variables() {
        }

    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Interior1_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", false);
            this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", false);
            if (this.aircraft().thisWeaponsName.endsWith("Schlong")) this.mesh.chunkVisible("Schlong", true);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("Interior1_D0", true);
        this.aircraft().hierMesh().chunkVisible("Pilot4_FAK", this.aircraft().hierMesh().isChunkVisible("Pilot4_D0"));
        this.aircraft().hierMesh().chunkVisible("Pilot4_FAL", this.aircraft().hierMesh().isChunkVisible("Pilot4_D1"));
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = -orient.getYaw();
        float f1 = orient.getTangage();
        f = this.clipCirgleGun(f, f1, 60F, 20F, -20F, 40F, -40F);
        this.mesh.chunkSetAngles("Z_MG81B", 0.0F, f, 0.0F);
        this.mesh.chunkSetAngles("Z_MG81A", 0.0F, f1, 0.0F);
        if (f > 15F) f = 15F;
        if (f < -15F) f = -15F;
        if (f1 > 30F) f1 = 30F;
        this.mesh.chunkSetAngles("CameraRodB", 0.0F, f, 0.0F);
        this.mesh.chunkSetAngles("CameraRodA", 0.0F, f1, 0.0F);
    }

    public float clipCirgleGun(float f, float f1, float f2, float f3, float f4, float f5, float f6) {
        float f7 = f;
        float f13 = 90F / (f2 - f3);
        float f14 = 90F / (f3 - f4);
        if (f1 > f3) {
            if (f7 > 0.0F) {
                float f9 = f5 * (float) Math.cos(Geom.DEG2RAD(f13 * (f1 - f3)));
                if (f7 > f9) f7 = f9;
            } else {
                float f10 = f6 * (float) Math.cos(Geom.DEG2RAD(f13 * (f1 - f3)));
                if (f7 < f10) f7 = f10;
            }
        } else if (f7 > 0.0F) {
            float f11 = f5 * (float) Math.cos(Geom.DEG2RAD(f14 * (f1 - f3)));
            if (f7 > f11) f7 = f11;
        } else {
            float f12 = f6 * (float) Math.cos(Geom.DEG2RAD(f14 * (f1 - f3)));
            if (f7 < f12) f7 = f12;
        }
        return f7;
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -40F) f = -40F;
        if (f > 40F) f = 40F;
        if (f1 > 60F) f1 = 60F;
        if (f1 < -20F) f1 = -20F;
        f = this.clipCirgleGun(f, f1, 60F, 20F, -20F, 40F, -40F);
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (((HE_177A3) this.aircraft()).bElectroSound && ((HE_177A3) this.aircraft()).bSightAutomation) {
            this.sfxStart(16);
            ((HE_177A3) this.aircraft()).bElectroSound = false;
        } else if (((HE_177A3) this.aircraft()).bElectroSound && !((HE_177A3) this.aircraft()).bSightAutomation) {
            this.sfxStop(16);
            ((HE_177A3) this.aircraft()).bElectroSound = false;
        }
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public CockpitHE_177A3_NGunner() {
        super("3DO/Cockpit/He-177A-3-NGun/hier.him", "bf109");
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.w = new Vector3f();
//        gunLeverMoveAxis = -1;
//        magazines = -2;
        this.cockpitNightMats = new String[] { "Fl20274", "Fl20342na", "Fl20342", "Fl20516", "Fl20516_1", "Fl20556", "Fl20570", "Fl20572", "Fl20723_1000", "Fl20723_1185", "Fl20723_1850", "Fl20723_200na", "Fl20723_200", "Fl20723_640", "Fl22231", "Fl22316",
                "Fl22382", "Fl30489", "Fl30532", "Fl32336", "Ln27002", "NeedlesnLights", "Nr92182B1na", "Voltmeters" };
//		this.hidePilot = true;
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        this.setOld = new Variables();
        this.setNew = new Variables();
        AircraftLH.printCompassHeading = true;
        this.normZN = 2.8F;
        this.gsZN = 2.8F;
    }

    public void reflectWorldToInstruments(float f) {
        this.mesh.chunkSetAngles("Z_Lotfe7", ((HE_177A3) this.fm.actor).fSightCurSideslip * -10F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle1", 41.8F * this.setNew.throttle1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle2", 41.8F * this.setNew.throttle1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle3", 41.8F * this.setNew.throttle2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throttle4", 41.8F * this.setNew.throttle2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BMBThrot1", 70F * this.setNew.throttle1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BMBThrot2", 70F * this.setNew.throttle1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BMBThrot3", 70F * this.setNew.throttle2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BMBThrot4", 70F * this.setNew.throttle2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Eng1Starter", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Eng2Starter", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Eng3Starter", this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Eng4Starter", this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 101F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Eng1Diseng", this.fm.EI.engines[0].getRPM() <= 10F ? 0.0F : 42F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Eng2Diseng", this.fm.EI.engines[0].getRPM() <= 10F ? 0.0F : 42F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Eng3Diseng", this.fm.EI.engines[1].getRPM() <= 10F ? 0.0F : 42F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Eng4Diseng", this.fm.EI.engines[1].getRPM() <= 10F ? 0.0F : 42F, 0.0F, 0.0F);
        this.mesh.chunkVisible("F_LGearsDown", this.fm.CT.getGear() == 1.0F && this.fm.Gears.lgear);
        this.mesh.chunkVisible("F_RGearsDown", this.fm.CT.getGear() == 1.0F && this.fm.Gears.rgear);
        this.mesh.chunkVisible("F_AllGearsDown", this.fm.CT.getGear() == 1.0F && this.fm.CT.getGear() == 1.0F && this.fm.Gears.lgear && this.fm.Gears.rgear);
        this.mesh.chunkVisible("F_TailGearsDown", this.fm.CT.getGear() == 1.0F && this.fm.Gears.cgear);
        this.mesh.chunkVisible("F_LGearsUp", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("F_RGearsUp", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("F_AllGearsUp", this.fm.CT.getGear() == 0.0F && this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkVisible("F_TailGearsUp", this.fm.CT.getGear() == 0.0F);
        this.mesh.chunkSetAngles("Z_Gears", this.fm.CT.getGear() != 1.0F || this.fm.CT.getGear() != 1.0F || !this.fm.Gears.lgear || !this.fm.Gears.rgear ? 0.0F : 90F, 0.0F, 0.0F);
        float f1 = this.fm.CT.getFlap();
        this.mesh.chunkVisible("F_FlapsUp", f1 < 0.1F);
        this.mesh.chunkVisible("F_FlapsUnf", f1 > 0.1F && f1 < 0.5F);
        this.mesh.chunkVisible("F_FlapsExt", f1 > 0.5F);
        float f2 = 0.0F;
        if (f1 > 0.5F) f2 = 90F;
        else if (f1 > 0.1F && f1 < 0.5F) f2 = 45F;
        this.mesh.chunkSetAngles("Z_Flaps", f2, 0.0F, 0.0F);
        this.mesh.chunkVisible("F_EngOverheatL", this.fm.AS.astateEngineStates[0] > 4);
        this.mesh.chunkVisible("F_EngOverheatR", this.fm.AS.astateEngineStates[1] > 4);
        if (this.setNew.pictElev < 0.0F) this.mesh.chunkSetAngles("Z_CColumn", 0.0F, 15F * -this.setNew.pictElev, 0.0F);
        else this.mesh.chunkSetAngles("Z_CColumn", 0.0F, 15F * -this.setNew.pictElev, 0.0F);
        this.mesh.chunkSetAngles("Z_Handle", 93F * this.setNew.pictAiler, 0.0F, 0.0F);
        float f3 = this.fm.CT.getRudder();
        this.mesh.chunkSetAngles("Z_RichagL", -35F * f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Tiaga4L", 35F * f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RichagR", 35F * f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Koromislo1L", 0.0F, 0.0F, 22F * f3);
        this.mesh.chunkSetAngles("Z_Koromislo2L", 0.0F, 0.0F, 22F * f3);
        this.mesh.chunkSetAngles("Z_PedalBomL", 0.0F, 0.0F, -22F * f3);
        this.mesh.chunkSetAngles("Z_Koromislo1R", 0.0F, 0.0F, -22F * f3);
        this.mesh.chunkSetAngles("Z_Koromislo2R", 0.0F, 0.0F, -22F * f3);
        this.mesh.chunkSetAngles("Z_PedalBomR", 0.0F, 0.0F, 22F * f3);
        float f4 = this.fm.CT.getBrake();
        float f5 = this.fm.CT.getBrake();
        float f6 = 20F * f4;
        float f7 = 106.3657F - f6;
        double d = Math.cos(Geom.DEG2RAD(f7));
        float f8 = Geom.RAD2DEG((float) Math.acos((0.038666129112243652D - 0.18320585787296295D * d) / Math.sqrt(0.035059455782175064D - 0.014167722314596176D * d)));
        float f9 = 180F - f7 - f8;
        float f10 = 62.8136F - f8;
        float f11 = f9 - 10.82074F;
        this.mesh.chunkSetAngles("Z_Kachalka2L", f6, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Tiaga3L", f10, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_CilindrL", 0.0F, f11, 0.0F);
        float f12 = 20F * f5;
        double d1 = Math.cos(Geom.DEG2RAD(142.0612F + f6));
        double d2 = Math.sqrt(0.0040516676381230354D - 0.003931721206754446D * d1);
        float f13 = Geom.RAD2DEG((float) Math.acos((0.039198901504278183D - 0.050150908529758453D * d1) / d2));
        float f14 = 21.38197F - f13;
        float f15 = 89.43449F + f14 + 35F * f3;
        double d3 = Math.sqrt(0.035435102880001068D + d2 * d2 - 0.37648427486419678D * d2 * (float) Math.cos(Geom.DEG2RAD(f15)));
        float f16 = Geom.RAD2DEG((float) Math.acos((0.042587004601955414D - d3 * d3) / 0.031839244067668915D));
        float f17 = 89.43554F - f16;
        this.mesh.chunkSetAngles("Z_Tiaga2L", f17, 0.0F, 0.0F);
        float f18 = (float) Math.acos((0.035435102880001068D + d3 * d3 - d2 * d2) / (0.37648427486419678D * d3));
        float f19 = (float) Math.acos((0.0071521135978400707D + d3 * d3 - 0.035434890538454056D) / (0.16914033889770508D * d3));
        float f20 = Geom.RAD2DEG(f18 + f19) - 90.56514F;
        this.mesh.chunkSetAngles("Z_LBrake", 0.0F, 0.0F, f20);
        double d4 = Math.cos(Geom.DEG2RAD(142.0612F + f12));
        double d5 = Math.sqrt(0.0040516676381230354D - 0.003931721206754446D * d4);
        float f21 = Geom.RAD2DEG((float) Math.acos((0.039198901504278183D - 0.050150908529758453D * d4) / d5));
        float f22 = 21.38197F - f21;
        float f23 = 89.43449F + f22 - 35F * f3;
        double d6 = Math.sqrt(0.035435102880001068D + d5 * d5 - 0.37648427486419678D * d5 * (float) Math.cos(Geom.DEG2RAD(f23)));
        float f24 = (float) Math.acos((0.035435102880001068D + d6 * d6 - d5 * d5) / (0.37648427486419678D * d6));
        float f25 = (float) Math.acos((0.0071521135978400707D + d6 * d6 - 0.035434890538454056D) / (0.16914033889770508D * d6));
        float f26 = Geom.RAD2DEG(f24 + f25) - 90.56514F;
        this.mesh.chunkSetAngles("Z_RBrake", 0.0F, 0.0F, f26);
        this.mesh.chunkSetAngles("Z_VatorTrim", this.cvt(this.setNew.elevTrim, -0.5F, 0.5F, -750F, 750F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RudderTrim", -this.cvt(this.setNew.rudderTrim, -0.5F, 0.5F, -750F, 750F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AilronTrim", -this.cvt(this.setNew.ailTrim, -0.5F, 0.5F, -750F, 750F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -this.cvt(this.setNew.elevTrim, -0.5F, 0.5F, -0.08425F, 0.08425F);
        this.mesh.chunkSetLocate("Z_VatorTrim2", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = this.cvt(this.setNew.rudderTrim, -0.5F, 0.5F, -0.0722F, 0.0722F);
        this.mesh.chunkSetLocate("Z_RudderTrim2", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -this.cvt(this.setNew.ailTrim, -0.5F, 0.5F, -0.0722F, 0.0722F);
        this.mesh.chunkSetLocate("Z_AilronTrim2", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_PropPitch1", -this.setNew._prop1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_PropPitch2", -this.setNew._prop2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BombRelease", this.cvt(this.setNew.bombDoor, 0.0F, 1.0F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_OxPress1", -200F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_OxPress2", -200F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_OxPress3", -200F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AirSpeed2", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 750F, 0.0F, 15F), IAS_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Alti2", -this.cvt(this.setNew.altimeter, 0.0F, 20000F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Climb2", -this.cvt(this.setNew.vspeed, -15F, 15F, -136F, 136F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Nav1C", this.cvt(this.setNew.beaconDirection, -45F, 45F, -16F, 16F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Nav2C", this.cvt(this.setNew.beaconRange, 0.0F, 1.0F, -14.5F, 20F), 0.0F, 0.0F);
        this.mesh.chunkVisible("F_N_AFN2C_RED", this.isOnBlindLandingMarker());
        this.mesh.chunkSetAngles("Z_N_AiFuePress1", -this.setNew.pictManf1, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AiFuePress2", -this.setNew.pictManf2, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AiFuePress3", -this.setNew.pictManf3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AiFuePress4", -this.setNew.pictManf4, 0.0F, 0.0F);
        float f27 = ((FlightModelMain) this.fm).EI.engines[0].getRPM();
        this.mesh.chunkSetAngles("Z_N_RPM1", -this.floatindex(this.cvt(f27, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_RPM2", -this.floatindex(this.cvt(f27, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
        float f28 = ((FlightModelMain) this.fm).EI.engines[1].getRPM();
        this.mesh.chunkSetAngles("Z_N_RPM3", -this.floatindex(this.cvt(f28, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_RPM4", -this.floatindex(this.cvt(f28, 400F, 3600F, 0.0F, 32F), Fl20274_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_FuelConsom", -this.cvt(0.5F * this.setNew.consumptionL, 0.0F, 500F, 0.0F, 255.5F), 0.0F, 0.0F);
//		int i = ((HE_177A3) this.aircraft()).iRust;
        float f29 = this.fm.M.fuel;
//		if (i == 2) {
//			this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f29, 6825.781F, 9382.67F, 0.0F, 62.93919F), 0.0F, 0.0F);
//			this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f29, 4268.9F, 6825.781F, 0.0F, 62.93919F), 0.0F, 0.0F);
//		} else if (i == 1) {
//			this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f29, 5395.4F, 7952.296F, 0.0F, 62.93919F), 0.0F, 0.0F);
//			this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f29, 4268.9F, 5395.4F, 0.0F, 55.45946F), 0.0F, 0.0F);
//		} else {
//			this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f29, 5395.4F, 6521.92F, 0.0F, 55.45946F), 0.0F, 0.0F);
//			this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f29, 4268.9F, 5395.4F, 0.0F, 55.45946F), 0.0F, 0.0F);
//		}
        switch (((HE_177A3) this.aircraft()).iRust) {
            case 3:
                this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f29, 6825.781F, 10813.045F, 0.0F, 62.93919F), 0.0F, 0.0F);
                this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f29, 4268.9F, 6825.781F, 0.0F, 62.93919F), 0.0F, 0.0F);
                break;
            case 2:
                this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f29, 6825.781F, 9382.67F, 0.0F, 62.93919F), 0.0F, 0.0F);
                this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f29, 4268.9F, 6825.781F, 0.0F, 62.93919F), 0.0F, 0.0F);
                break;
            case 1:
                this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f29, 5395.4F, 7952.295F, 0.0F, 62.93919F), 0.0F, 0.0F);
                this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f29, 4268.9F, 5395.4F, 0.0F, 55.45946F), 0.0F, 0.0F);
                break;
            default:
                this.mesh.chunkSetAngles("Z_N_Fuel4", -this.cvt(f29, 5395.4F, 6521.92F, 0.0F, 55.45946F), 0.0F, 0.0F);
                this.mesh.chunkSetAngles("Z_N_Fuel5", -this.cvt(f29, 4268.9F, 5395.4F, 0.0F, 55.45946F), 0.0F, 0.0F);
                break;
        }
        this.mesh.chunkSetAngles("Z_N_Fuel6", -this.cvt(f29, 3424.015F, 4268.9F, 0.0F, 72.63291F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Fuel2", -this.cvt(f29, 2505.015F, 3424.015F, 0.0F, 71.6875F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Fuel3", -this.cvt(f29, 2505.015F, 3424.015F, 0.0F, 71.6875F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Fuel7", -this.cvt(f29, 844.89F, 2505.015F, 0.0F, 81.76F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Fuel8", -this.cvt(f29, 844.89F, 2505.015F, 0.0F, 81.76F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_Fuel1", -this.cvt(f29, 0.0F, 844.89F, 0.0F, 72.63291F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_FuelQuant", -this.cvt(f29, 0.0F, this.fm.M.maxFuel, 37F, 84F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E1CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E2CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E3CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E4CoolTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E1OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E2OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E3OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E4OilTemp", -this.floatindex(this.cvt(this.fm.EI.engines[1].tOilOut, 0.0F, 130F, 0.0F, 13F), Fl20342_Scale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AirPress1", -170F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AirPress2", -170F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AirPress3", -170F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_AirPress4", this.fm.M.fuel <= 1.0F ? 0.0F : -83F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E1_FuelPres", -this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E2_FuelPres", -this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E3_FuelPres", -this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E4_FuelPres", -this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.77F, 0.0F, 3F, 0.0F, 178F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E1_OilPress", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E2_OilPress", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E3_OilPress", this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_N_E4_OilPress", this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut, 0.0F, 10F, 0.0F, 183F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_LandingLight", this.fm.AS.bLandingLightOn ? 90F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_NavLights", this.fm.AS.bNavLightsOn ? 90F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_CockpitLight", this.cockpitLightControl ? 90F : 0.0F, 0.0F, 0.0F);
        float f30 = -this.fm.turret[1].tu[0];
        float f31 = this.fm.turret[1].tu[1];
        float f32 = Geom.DEG2RAD(f30);
        float f33 = Geom.DEG2RAD(f31);
        float f34 = (float) Math.asin(Math.tan(f32) * Math.cos(f33) * Math.cos(f32));
        float f35 = (float) Math.atan(Math.tan(f33) / Math.cos(f32));
        float f36 = Geom.RAD2DEG(f34);
        float f37 = Geom.RAD2DEG(f35);
        this.mesh.chunkSetAngles("Z_Turret151A", 0.0F, f36, 0.0F);
        this.mesh.chunkSetAngles("Z_Turret151B", 0.0F, 0.0F, f37);
        f30 = this.fm.turret[2].tu[0];
        f31 = -this.fm.turret[2].tu[1];
        f32 = Geom.DEG2RAD(f30);
        f33 = Geom.DEG2RAD(f31);
        f34 = (float) Math.asin(Math.tan(f32) * Math.cos(f33) * Math.cos(f32));
        f35 = (float) Math.atan(Math.tan(f33) / Math.cos(f32));
        f36 = Geom.RAD2DEG(f34);
        f37 = Geom.RAD2DEG(f35);
        this.mesh.chunkSetAngles("Z_Turret131B", f36, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Turret131A", 0.0F, 0.0F, f37);
        float f38;
        for (f38 = -this.fm.turret[3].tu[0]; f38 < -180F; f38 += 360F)
            ;
        for (; f38 > 180F; f38 -= 360F)
            ;
        float f39 = this.fm.turret[3].tu[1];
        this.mesh.chunkSetAngles("Z_TurretA", -f38, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurretB", 0.0F, 0.0F, -f39);
        this.mesh.chunkSetAngles("Z_tLink", 0.0F, 0.0F, this.floatindex(this.cvt(f39, 0.0F, 90F, 0.0F, 18F), Turret_Z1));
        this.mesh.chunkSetAngles("Z_tHandle", 0.0F, 0.0F, -this.floatindex(this.cvt(f39, 0.0F, 90F, 0.0F, 18F), Turret_Z2));
        this.mesh.chunkSetAngles("Z_tReviVal", 0.0F, -f38 * 5F, 0.0F);
        this.CalculateRevi(f38, f39);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = 0.185F * this.setNew.AirEnemy;
        this.mesh.chunkSetLocate("Z_TurretA_Seat", Cockpit.xyz, Cockpit.ypr);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("xGlassDm1", true);
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("xHullDm1", true);
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("xGlass2", false);
            this.mesh.chunkVisible("xGlass2_dmg", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) this.mesh.chunkVisible("xGlassDm2", true);
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("xGlass3", false);
            this.mesh.chunkVisible("xGlass3_dmg", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("xGlass4", false);
            this.mesh.chunkVisible("xGlass4_dmg", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("xGlass5", false);
            this.mesh.chunkVisible("xGlass5_dmg", true);
        }
        if (!this.cockpitLightControl) {
            this.setNightMats(true);
            this.setNightMats(false);
        }
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) this.setNightMats(true);
        else this.setNightMats(false);
    }

    public void CalculateRevi(float f, float f1) {
        ((HE_177A3) this.fm.actor).CalculateRevi(-f, f1);
        float f2 = ((HE_177A3) this.fm.actor).getB1_HeadTangage();
        float f3 = ((HE_177A3) this.fm.actor).getB1_HeadYaw();
        float f4 = ((HE_177A3) this.fm.actor).getB1_HeadTangage1();
        float f5 = ((HE_177A3) this.fm.actor).getB1_ReviYaw();
        float f6 = ((HE_177A3) this.fm.actor).getB1_ReviX();
        float f7 = ((HE_177A3) this.fm.actor).getB1_ReviY();
        this.mesh.chunkSetAngles("Z_TurretB2", 0.0F, 0.0F, f2);
        this.mesh.chunkSetAngles("Z_Turret_Revi", 0.0F, f3, 0.0F);
        this.mesh.chunkSetAngles("Z_tRevi1", -f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_tRevi2", f3, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_tRevi4", f, 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -f7;
        this.mesh.chunkSetLocate("Z_tRevi5", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = f6;
        this.mesh.chunkSetLocate("Z_tRevi3", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_tRevi6", 0.0F, 0.0F, f4);
        this.mesh.chunkSetAngles("Z_tRevi7", f5, 0.0F, 0.0F);
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private static final float Turret_Z1[]     = { 0.0F, 4.235F, 8.64F, 13.21F, 17.92F, 22.79F, 27.82F, 32.99F, 38.29F, 43.75F, 49.36F, 55.12F, 61.05F, 67.13F, 73.4F, 79.87F, 86.55F, 93.45F, 100.61F };
    private static final float Turret_Z2[]     = { 0.0F, 2.125F, 4.32F, 6.58F, 8.87F, 11.188F, 13.53F, 15.88F, 18.21F, 20.52F, 22.78F, 24.98F, 27.105F, 29.13F, 31.03F, 32.76F, 34.33F, 35.68F, 36.78F };
    private static final float IAS_Scale[]     = { 0.0F, 8F, 18.4F, 41.26F, 67.26F, 94.36F, 119.58F, 141F, 166.1F, 190.43F, 216.05F, 241.18F, 267.5F, 293.8F, 318.19F, 341.7F };
    private static final float Fl20342_Scale[] = { 0.0F, 5.5F, 11F, 17.5F, 25F, 33F, 41.5F, 50.5F, 59.5F, 67F, 73.5F, 80.25F, 85.5F, 90F };
    private static final float Fl20274_Scale[] = { 0.0F, 4.25F, 8F, 12F, 17F, 23F, 28.5F, 34.5F, 42F, 51F, 58.5F, 69F, 79F, 91F, 103F, 114.5F, 128F, 140F, 152.5F, 164.5F, 175F, 186.5F, 195.5F, 205F, 214F, 222.5F, 230.5F, 239F, 246.5F, 253.5F, 260.5F,
            265F, 269.5F };
    final float                constAB         = 0.03866613F;
    final float                constAC         = 0.1832059F;
    final float                constOA         = 0.0391989F;
    final float                constOB         = 0.05015091F;
    final float                constAC2        = 0.1882421F;
    final float                constBD2        = 0.1882416F;
    final float                constCD2        = 0.08457017F;

    static {
        Class class1 = CockpitHE_177A3_NGunner.class;
        Property.set(class1, "aiTuretNum", 0);
        Property.set(class1, "weaponControlNum", 10);
        Property.set(class1, "astatePilotIndx", 1);
        Property.set(class1, "normZN", 2.8F);
    }
}
