package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;
import com.maddox.sound.SoundFX;

public class CockpitJU_88A5Late_BGunner extends CockpitGunner {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (JU_88A5Late.bChangedPit) {
                CockpitJU_88A5Late_BGunner.this.reflectPlaneToModel();
                JU_88A5Late.bChangedPit = false;
            }
            if (CockpitJU_88A5Late_BGunner.this.fm != null) {
                CockpitJU_88A5Late_BGunner.this.setTmp = CockpitJU_88A5Late_BGunner.this.setOld;
                CockpitJU_88A5Late_BGunner.this.setOld = CockpitJU_88A5Late_BGunner.this.setNew;
                CockpitJU_88A5Late_BGunner.this.setNew = CockpitJU_88A5Late_BGunner.this.setTmp;
                CockpitJU_88A5Late_BGunner.this.setNew.throttle1 = 0.85F * CockpitJU_88A5Late_BGunner.this.setOld.throttle1 + CockpitJU_88A5Late_BGunner.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
                CockpitJU_88A5Late_BGunner.this.setNew.prop1 = 0.85F * CockpitJU_88A5Late_BGunner.this.setOld.prop1 + CockpitJU_88A5Late_BGunner.this.fm.EI.engines[0].getControlProp() * 0.15F;
                CockpitJU_88A5Late_BGunner.this.setNew.throttle2 = 0.85F * CockpitJU_88A5Late_BGunner.this.setOld.throttle2 + CockpitJU_88A5Late_BGunner.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
                CockpitJU_88A5Late_BGunner.this.setNew.prop2 = 0.85F * CockpitJU_88A5Late_BGunner.this.setOld.prop2 + CockpitJU_88A5Late_BGunner.this.fm.EI.engines[1].getControlProp() * 0.15F;
                CockpitJU_88A5Late_BGunner.this.setNew.altimeter = CockpitJU_88A5Late_BGunner.this.fm.getAltitude();
                float f = CockpitJU_88A5Late_BGunner.this.waypointAzimuth();
                CockpitJU_88A5Late_BGunner.this.setNew.waypointAzimuth.setDeg(CockpitJU_88A5Late_BGunner.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitJU_88A5Late_BGunner.this.setOld.azimuth.getDeg(1.0F));
                CockpitJU_88A5Late_BGunner.this.setNew.azimuth.setDeg(CockpitJU_88A5Late_BGunner.this.setOld.azimuth.getDeg(1.0F), CockpitJU_88A5Late_BGunner.this.fm.Or.azimut());
                CockpitJU_88A5Late_BGunner.this.setNew.vspeed = (199F * CockpitJU_88A5Late_BGunner.this.setOld.vspeed + CockpitJU_88A5Late_BGunner.this.fm.getVertSpeed()) / 200F;
                if (CockpitJU_88A5Late_BGunner.this.cockpitDimControl) {
                    if (CockpitJU_88A5Late_BGunner.this.setNew.dimPosition > 0.0F) CockpitJU_88A5Late_BGunner.this.setNew.dimPosition = CockpitJU_88A5Late_BGunner.this.setOld.dimPosition - 0.05F;
                } else if (CockpitJU_88A5Late_BGunner.this.setNew.dimPosition < 1.0F) CockpitJU_88A5Late_BGunner.this.setNew.dimPosition = CockpitJU_88A5Late_BGunner.this.setOld.dimPosition + 0.05F;
                float f1 = CockpitJU_88A5Late_BGunner.this.prevFuel - CockpitJU_88A5Late_BGunner.this.fm.M.fuel;
                CockpitJU_88A5Late_BGunner.this.prevFuel = CockpitJU_88A5Late_BGunner.this.fm.M.fuel;
                f1 /= 0.72F;
                f1 /= Time.tickLenFs();
                f1 *= 3600F;
                CockpitJU_88A5Late_BGunner.this.setNew.cons = 0.91F * CockpitJU_88A5Late_BGunner.this.setOld.cons + 0.09F * f1;
                if (CockpitJU_88A5Late_BGunner.this.buzzerFX != null)
                    if (CockpitJU_88A5Late_BGunner.this.fm.Loc.z < ((JU_88A5Late) CockpitJU_88A5Late_BGunner.this.aircraft()).fDiveRecoveryAlt && ((JU_88A5Late) ((Interpolate) CockpitJU_88A5Late_BGunner.this.fm).actor).diveMechStage == 1)
                        CockpitJU_88A5Late_BGunner.this.buzzerFX.play();
                    else if (CockpitJU_88A5Late_BGunner.this.buzzerFX.isPlaying()) CockpitJU_88A5Late_BGunner.this.buzzerFX.stop();
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      throttle1;
        float      prop1;
        float      throttle2;
        float      prop2;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      dimPosition;
        float      cons;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("fakeNose_D0", false);
            this.aircraft().hierMesh().chunkVisible("fakeNose_D1", false);
            this.aircraft().hierMesh().chunkVisible("fakeNose_D2", false);
            this.aircraft().hierMesh().chunkVisible("fakeNose_D3", false);
            this.aircraft().hierMesh().chunkVisible("Turret1C_D0", false);
            this.aircraft().hierMesh().chunkVisible("Wire_D0", false);
            this.aircraft().hierMesh().chunkVisible("Pilot4_D0", false);
            this.aircraft().hierMesh().chunkVisible("HMask4_D0", false);
            if (!((JU_88A5Late) this.aircraft()).topBlisterRemoved) {
                this.aircraft().hierMesh().chunkVisible("Turret2C_D0", false);
                this.aircraft().hierMesh().chunkVisible("Turret3C_D0", false);
                this.aircraft().hierMesh().chunkVisible("BlisterTop_D0", false);
                this.aircraft().hierMesh().chunkVisible("DummyBlister_D0", true);
            }
            if (!((JU_88A5Late) this.aircraft()).blisterRemoved) {
                this.aircraft().hierMesh().chunkVisible("Turret4C_D0", false);
                this.aircraft().hierMesh().chunkVisible("BlisterDown_D0", false);
            }
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        if (!this.isFocused()) return;
        this.aircraft().hierMesh().chunkVisible("fakeNose_D0", this.aircraft().hierMesh().isChunkVisible("Nose_D0"));
        this.aircraft().hierMesh().chunkVisible("fakeNose_D1", this.aircraft().hierMesh().isChunkVisible("Nose_D1"));
        this.aircraft().hierMesh().chunkVisible("fakeNose_D2", this.aircraft().hierMesh().isChunkVisible("Nose_D2"));
        this.aircraft().hierMesh().chunkVisible("fakeNose_D3", this.aircraft().hierMesh().isChunkVisible("Nose_D3"));
        this.aircraft().hierMesh().chunkVisible("Turret1C_D0", true);
        this.aircraft().hierMesh().chunkVisible("Wire_D0", true);
        this.aircraft().hierMesh().chunkVisible("Pilot4_D0", !this.aircraft().hierMesh().isChunkVisible("Pilot4_D1"));
        this.aircraft().hierMesh().chunkVisible("HMask4_D0", this.aircraft().hierMesh().isChunkVisible("HMask1_D0"));
        if (!((JU_88A5Late) this.aircraft()).topBlisterRemoved) {
            this.aircraft().hierMesh().chunkVisible("Turret2C_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret3C_D0", true);
            this.aircraft().hierMesh().chunkVisible("BlisterTop_D0", true);
            this.aircraft().hierMesh().chunkVisible("DummyBlister_D0", false);
        }
        if (!((JU_88A5Late) this.aircraft()).blisterRemoved) {
            this.aircraft().hierMesh().chunkVisible("Turret4C_D0", true);
            this.aircraft().hierMesh().chunkVisible("BlisterDown_D0", true);
        }
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        this.mesh.chunkSetAngles("zTurret4A", 0.0F, -f, 0.0F);
        this.mesh.chunkSetAngles("zTurret4B", 0.0F, f1, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -43F) f = -43F;
        if (f > 43F) f = 43F;
        if (f1 > 0.0F) f1 = 0.0F;
        if (f1 < -40F) f1 = -40F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
        if (this.bGunFire) {
            if (this.iCocking > 0) this.iCocking = 0;
            else this.iCocking = 1;
        } else this.iCocking = 0;
        this.resetYPRmodifier();
        Cockpit.xyz[0] = -0.07F * this.iCocking;
        this.mesh.chunkSetLocate("zLever4", Cockpit.xyz, Cockpit.ypr);
    }

    public CockpitJU_88A5Late_BGunner() {
        super("3DO/Cockpit/Ju-88A-4Late-BGun/hier-A5.him", "he111");
        this.bNeedSetUp = true;
        this.iCocking = 0;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictManf1 = 1.0F;
        this.pictManf2 = 1.0F;
        this.prevFuel = 0.0F;
        this.cockpitNightMats = new String[] { "88a4_I_Set2", "88a4_I_Set3", "88a4_I_Set4", "88a4_I_Set6", "88a4_SlidingGlass", "88gardinen", "Peil1", "Pedal", "skala", "Peil2", "lofte7_02" };
        this.setNightMats(false);
        this.setNew.dimPosition = this.setOld.dimPosition = 1.0F;
        this.cockpitDimControl = !this.cockpitDimControl;
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) this.acoustics.globFX = new ReverbFXRoom(0.45F);
        this.buzzerFX = this.aircraft().newSound("models.buzzthru", false);
    }

    public void toggleDim() {
        this.cockpitDimControl = !this.cockpitDimControl;
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkSetAngles("Z_Trim1", this.cvt(this.fm.CT.getTrimElevatorControl(), -0.5F, 0.5F, -750F, 750F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_ReviTinter", this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 130F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zColumn1", 7F * (this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zColumn2", 52.2F * (this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[2] = -0.1F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("zPedalL", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[2] = 0.1F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("zPedalR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("zTurret1A", 0.0F, this.fm.turret[0].tu[0], 0.0F);
        this.mesh.chunkSetAngles("zTurret1B", 0.0F, this.fm.turret[0].tu[1], 0.0F);
        this.mesh.chunkSetAngles("zTurret2A", 0.0F, this.fm.turret[1].tu[0], 0.0F);
        this.mesh.chunkSetAngles("zTurret2B", 0.0F, this.fm.turret[1].tu[1], 0.0F);
        this.mesh.chunkSetAngles("zTurret4A", 0.0F, this.fm.turret[3].tu[0], 0.0F);
        this.mesh.chunkSetAngles("zTurret4B", 0.0F, this.fm.turret[3].tu[1], 0.0F);
        this.mesh.chunkSetAngles("zHour2", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMinute2", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 6000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt3", ((JU_88A5Late) this.aircraft()).fDiveRecoveryAlt * 360F / 6000F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 50F, 750F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.cvt(this.setNew.vspeed, -15F, 15F, -151F, 151F), 0.0F, 0.0F);
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
        this.mesh.chunkSetAngles("zFuelPress", this.cvt(this.setNew.cons, 100F, 500F, 0.0F, 240F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", -this.setNew.azimuth.getDeg(f) - 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", this.setNew.waypointAzimuth.getDeg(f * 0.1F) - 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass3", this.setNew.waypointAzimuth.getDeg(f * 0.1F) - 90F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass4", -this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass5", 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass7", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass8", this.setNew.waypointAzimuth.getDeg(f * 0.1F) + 90F, 0.0F, 0.0F);
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
        if (JU_88Axx.bChangedPit) {
            this.aircraft().hierMesh().chunkVisible("fakeNose_D0", false);
            this.aircraft().hierMesh().chunkVisible("fakeNose_D1", false);
            this.aircraft().hierMesh().chunkVisible("fakeNose_D2", false);
            this.aircraft().hierMesh().chunkVisible("fakeNose_D3", false);
            JU_88Axx.bChangedPit = false;
        }
    }

    public void reflectCockpitState() {
//        if ((this.fm.AS.astateCockpitState & 0x80) == 0);
        if ((this.fm.AS.astateCockpitState & 2) != 0) this.mesh.chunkVisible("XGlassDamage1", true);
        if ((this.fm.AS.astateCockpitState & 1) != 0) this.mesh.chunkVisible("XGlassDamage5", true);
//        if ((this.fm.AS.astateCockpitState & 0x40) == 0);
        if ((this.fm.AS.astateCockpitState & 4) != 0) this.mesh.chunkVisible("XGlassDamage3", true);
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

    protected void reflectPlaneToModel() {
        if (this.isFocused()) {
            this.aircraft().hierMesh().chunkVisible("fakeNose_D0", false);
            this.aircraft().hierMesh().chunkVisible("fakeNose_D1", false);
            this.aircraft().hierMesh().chunkVisible("fakeNose_D2", false);
            this.aircraft().hierMesh().chunkVisible("fakeNose_D3", false);
        }
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
        mat = hiermesh.material(hiermesh.materialFind("Matt1D0o"));
        this.mesh.materialReplace("Matt1D0o", mat);
    }

    private int                iCocking;
    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              pictManf1;
    private float              pictManf2;
    private float              prevFuel;
    protected SoundFX          buzzerFX;
    private static final float speedometerScale[] = { 0.0F, 16F, 35.5F, 60.5F, 88F, 112.5F, 136F, 159.5F, 186.5F, 211.5F, 240F, 268F, 295.5F, 321F, 347F };

    static {
        Property.set(CockpitJU_88A5Late_BGunner.class, "aiTuretNum", 3);
        Property.set(CockpitJU_88A5Late_BGunner.class, "weaponControlNum", 13);
        Property.set(CockpitJU_88A5Late_BGunner.class, "astatePilotIndx", 3);
        Property.set(CockpitJU_88A5Late_BGunner.class, "normZN", 0.2F);
        Property.set(CockpitJU_88A5Late_BGunner.class, "gsZN", 0.2F);
    }
}
