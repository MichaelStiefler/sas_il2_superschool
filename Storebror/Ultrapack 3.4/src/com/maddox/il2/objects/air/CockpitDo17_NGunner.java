package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;

public class CockpitDo17_NGunner extends CockpitGunner {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitDo17_NGunner.this.fm != null) {
                CockpitDo17_NGunner.this.setTmp = CockpitDo17_NGunner.this.setOld;
                CockpitDo17_NGunner.this.setOld = CockpitDo17_NGunner.this.setNew;
                CockpitDo17_NGunner.this.setNew = CockpitDo17_NGunner.this.setTmp;
                CockpitDo17_NGunner.this.setNew.throttlel = 0.85F * CockpitDo17_NGunner.this.setOld.throttlel + CockpitDo17_NGunner.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
                CockpitDo17_NGunner.this.setNew.propL = 0.85F * CockpitDo17_NGunner.this.setOld.propL + CockpitDo17_NGunner.this.fm.EI.engines[0].getControlProp() * 0.15F;
                CockpitDo17_NGunner.this.setNew.throttler = 0.85F * CockpitDo17_NGunner.this.setOld.throttler + CockpitDo17_NGunner.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
                CockpitDo17_NGunner.this.setNew.propR = 0.85F * CockpitDo17_NGunner.this.setOld.propR + CockpitDo17_NGunner.this.fm.EI.engines[1].getControlProp() * 0.15F;
                float f1 = CockpitDo17_NGunner.this.waypointAzimuth();
                if (CockpitDo17_NGunner.this.useRealisticNavigationInstruments()) {
                    CockpitDo17_NGunner.this.setNew.waypointAzimuth.setDeg(f1 - 90F);
                    CockpitDo17_NGunner.this.setOld.waypointAzimuth.setDeg(f1 - 90F);
                    CockpitDo17_NGunner.this.setNew.radioCompassAzimuth.setDeg(CockpitDo17_NGunner.this.setOld.radioCompassAzimuth.getDeg(0.02F),
                            CockpitDo17_NGunner.this.radioCompassAzimuthInvertMinus() - CockpitDo17_NGunner.this.setOld.azimuth.getDeg(1.0F) - 90F);
                } else CockpitDo17_NGunner.this.setNew.waypointAzimuth.setDeg(CockpitDo17_NGunner.this.setOld.waypointAzimuth.getDeg(0.1F), f1 - CockpitDo17_NGunner.this.setOld.azimuth.getDeg(1.0F));
                CockpitDo17_NGunner.this.setNew.azimuth.setDeg(CockpitDo17_NGunner.this.setOld.azimuth.getDeg(1.0F), CockpitDo17_NGunner.this.fm.Or.azimut());
                CockpitDo17_NGunner.this.setNew.vspeed = (199F * CockpitDo17_NGunner.this.setOld.vspeed + CockpitDo17_NGunner.this.fm.getVertSpeed()) / 200F;
                if (CockpitDo17_NGunner.this.cockpitDimControl) {
                    if (CockpitDo17_NGunner.this.setNew.dimPosition > 0.0F) CockpitDo17_NGunner.this.setNew.dimPosition = CockpitDo17_NGunner.this.setOld.dimPosition - 0.05F;
                } else if (CockpitDo17_NGunner.this.setNew.dimPosition < 1.0F) CockpitDo17_NGunner.this.setNew.dimPosition = CockpitDo17_NGunner.this.setOld.dimPosition + 0.05F;
                CockpitDo17_NGunner.this.setNew.beaconDirection = (10F * CockpitDo17_NGunner.this.setOld.beaconDirection + CockpitDo17_NGunner.this.getBeaconDirection()) / 11F;
                CockpitDo17_NGunner.this.setNew.beaconRange = (10F * CockpitDo17_NGunner.this.setOld.beaconRange + CockpitDo17_NGunner.this.getBeaconRange()) / 11F;
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttlel;
        float      throttler;
        float      propL;
        float      propR;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        AnglesFork radioCompassAzimuth;
        float      vspeed;
        float      dimPosition;
        float      beaconDirection;
        float      beaconRange;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
            this.radioCompassAzimuth = new AnglesFork();
        }
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
            this.bBeaconKeysEnabled = ((AircraftLH) this.aircraft()).bWantBeaconKeys;
            ((AircraftLH) this.aircraft()).bWantBeaconKeys = true;
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) return;
        else {
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
            ((AircraftLH) this.aircraft()).bWantBeaconKeys = this.bBeaconKeysEnabled;
            super.doFocusLeave();
            return;
        }
    }

    public void moveGun(Orient paramOrient) {
        super.moveGun(paramOrient);
        float f1 = -paramOrient.getYaw();
        float f2 = paramOrient.getTangage();
        this.mesh.chunkSetAngles("zTurret1A", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("zTurret1B", 0.0F, f2, 0.0F);
        if (f1 > 10F) f1 = 10F;
        if (f1 < -10F) f1 = -10F;
        if (f2 < -10F) f2 = -10F;
        if (f2 > 15F) f2 = 15F;
        if (f1 <= 10F && f1 >= 0.0F && f2 < Aircraft.cvt(f1, 0.0F, 10F, -10F, 0.0F)) f2 = Aircraft.cvt(f1, 0.0F, 10F, -10F, 0.0F);
        this.mesh.chunkSetAngles("CameraRodA", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("CameraRodB", 0.0F, f2, 0.0F);
    }

    public void clipAnglesGun(Orient paramOrient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            paramOrient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f1 = paramOrient.getYaw();
        float f2 = paramOrient.getTangage();
        if (f1 < -30F) f1 = -30F;
        if (f1 > 35F) f1 = 35F;
        if (f2 > 35F) f2 = 35F;
        if (f2 < -10F) f2 = -10F;
        paramOrient.setYPR(f1, f2, 0.0F);
        paramOrient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public void doGunFire(boolean paramBoolean) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = paramBoolean;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public CockpitDo17_NGunner() {
        super("3DO/Cockpit/Ju-88A-4-NGun/hier.him", "he111");
        this.bNeedSetUp = true;
        this.pictFlap = 0.0F;
        this.pictGear = 0.0F;
        this.pictManf1 = 1.0F;
        this.pictManf2 = 1.0F;
        this.w = new Vector3f();
        this.cockpitNightMats = new String[] { "88a4_I_Set1", "88a4_I_Set2", "88a4_I_Set3", "88a4_I_Set4", "88a4_I_Set5", "88a4_I_Set6", "88a4_SlidingGlass", "88gardinen", "lofte7_02", "Peil1", "Pedal", "skala", "alt4" };
        this.setNightMats(false);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        AircraftLH.printCompassHeading = true;
        this.mesh.chunkSetAngles("Z_Trim1", this.cvt(this.fm.CT.getTrimElevatorControl(), -0.5F, 0.5F, -750F, 750F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zColumn1", 7F * (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zColumn2", 52.2F * (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = -0.1F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("zPedalL", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = 0.1F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("zPedalR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zTurret1A", 0.0F, this.fm.turret[0].tu[0], 0.0F);
        this.mesh.chunkSetAngles("zTurret1B", 0.0F, this.fm.turret[0].tu[1], 0.0F);
        this.mesh.chunkSetAngles("zTurret3A", 0.0F, this.fm.turret[2].tu[0], 0.0F);
        this.mesh.chunkSetAngles("zTurret3B", 0.0F, this.fm.turret[2].tu[1], 0.0F);
        this.resetYPRmodifier();
        float tmp368_367 = 0.85F * this.pictFlap + 0.00948F * this.fm.CT.FlapsControl;
        this.pictFlap = tmp368_367;
        Cockpit.xyz[2] = tmp368_367;
        this.mesh.chunkSetLocate("zFlaps1", Cockpit.xyz, Cockpit.ypr);
        float tmp414_413 = 0.85F * this.pictGear + 0.007095F * this.fm.CT.GearControl;
        this.pictGear = tmp414_413;
        Cockpit.xyz[2] = tmp414_413;
        this.mesh.chunkSetLocate("zGear1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("TempMeter", this.floatindex(this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 213.09F, 293.09F, 0.0F, 8F), frAirTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Radiator_Sw1", this.cvt(this.fm.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.0F, -120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Radiator_Sw2", this.cvt(this.fm.EI.engines[1].getControlRadiator(), 0.0F, 1.0F, 0.0F, -120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSw1", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 100F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSw2", this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 100F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 50F, 750F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed2", this.floatindex(this.cvt(this.fm.getSpeedKMH(), 50F, 750F, 0.0F, 14F), speedometerScale2), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zWaterTemp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 64F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zWaterTemp2", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 120F, 0.0F, 64F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilPress1", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness(), 0.0F, 7.47F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zOilPress2", this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness(), 0.0F, 7.47F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuelPress1", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.32F, 0.0F, 0.5F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuelPress2", this.cvt(this.fm.M.fuel <= 1.0F ? 0.0F : 0.32F, 0.0F, 0.5F, 0.0F, 90F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 600F, 3600F, 0.0F, 331F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zRPM2", this.cvt(this.fm.EI.engines[1].getRPM(), 600F, 3600F, 0.0F, 331F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ATA1", this.pictManf1 = 0.9F * this.pictManf1 + 0.1F * this.cvt(this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 325.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("ATA2", this.pictManf2 = 0.9F * this.pictManf2 + 0.1F * this.cvt(this.fm.EI.engines[1].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 325.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zHour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMinute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel1", this.cvt(this.fm.M.fuel, 0.0F, 1008F, 0.0F, 77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel2", this.cvt(this.fm.M.fuel, 0.0F, 1008F, 0.0F, 77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zHORIZ1", -this.fm.Or.getKren(), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.045F, -0.045F);
        this.mesh.chunkSetLocate("zHORIZ2", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = 0.02125F * this.fm.CT.getTrimElevatorControl();
        this.mesh.chunkSetLocate("zTRIM1", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = 0.02125F * this.fm.CT.getTrimAileronControl();
        this.mesh.chunkSetLocate("zTRIM2", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = 0.02125F * this.fm.CT.getTrimRudderControl();
        this.mesh.chunkSetLocate("zTRIM3", Cockpit.xyz, Cockpit.ypr);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.w.z, -0.23562F, 0.23562F, 25F, -25F), 0.0F, 0.0F);
        this.mesh.chunkVisible("XLampGearUpL", this.fm.CT.getGear() < 0.01F || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearDownL", this.fm.CT.getGear() > 0.99F && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XLampGearUpR", this.fm.CT.getGear() < 0.01F || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearDownR", this.fm.CT.getGear() > 0.99F && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLampGearUpC", this.fm.CT.getGear() < 0.01F);
        this.mesh.chunkVisible("XLampGearDownC", this.fm.CT.getGear() > 0.99F);
        this.mesh.chunkVisible("XLampFlap1", this.fm.CT.getFlap() < 0.1F);
        this.mesh.chunkVisible("XLampFlap2", this.fm.CT.getFlap() > 0.1F && this.fm.CT.getFlap() < 0.5F);
        this.mesh.chunkVisible("XLampFlap3", this.fm.CT.getFlap() > 0.5F);
        this.mesh.chunkVisible("XLamp1", false);
        this.mesh.chunkVisible("XLamp2", true);
        this.mesh.chunkVisible("XLamp3", true);
        this.mesh.chunkVisible("XLamp4", false);
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(20F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 0x80) == 0 || (this.fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("XGlassDamage1", true);
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("XGlassDamage5", true);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0 || (this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("XGlassDamage3", true);
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XGlassDamage5", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) this.mesh.chunkVisible("XGlassDamage2", true);
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) this.mesh.chunkVisible("XGlassDamage6", true);
        this.retoggleLight();
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

    protected void reflectPlaneMats() {
        HierMesh localHierMesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat localMat = localHierMesh.material(localHierMesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", localMat);
    }

    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private boolean            bBeaconKeysEnabled;
    private float              pictAiler;
    private float              pictElev;
    private float              pictFlap;
    private float              pictGear;
    private float              pictManf1;
    private float              pictManf2;
    private static final float speedometerScale[]  = { 0.0F, 16F, 35.5F, 60.5F, 88F, 112.5F, 136F, 159.5F, 186.5F, 211.5F, 240F, 268F, 295.5F, 321F, 347F };
    private static final float speedometerScale2[] = { 0.0F, 23.5F, 47.5F, 72F, 95.5F, 120F, 144.5F, 168.5F, 193F, 217F, 241F, 265F, 288F, 311.5F, 335.5F };
    private static final float frAirTempScale[]    = { 76.5F, 68F, 57F, 44.5F, 29.5F, 14.5F, 1.5F, -10F, -19F };
    public Vector3f            w;

    static {
        Property.set(CockpitDo17_NGunner.class, "aiTuretNum", 0);
        Property.set(CockpitDo17_NGunner.class, "weaponControlNum", 10);
        Property.set(CockpitDo17_NGunner.class, "astatePilotIndx", 1);
        Property.set(CockpitDo17_NGunner.class, "normZN", 0.75F);
        Property.set(CockpitDo17_NGunner.class, "gsZN", 0.75F);
    }

}
