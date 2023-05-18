package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;
import com.maddox.sound.SoundFX;

public class CockpitJU_88A4Late_LGunner extends CockpitGunner {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (JU_88A4Late.bChangedPit) {
                CockpitJU_88A4Late_LGunner.this.reflectPlaneToModel();
                JU_88A4Late.bChangedPit = false;
            }
            if (CockpitJU_88A4Late_LGunner.this.fm != null) {
                CockpitJU_88A4Late_LGunner.this.setTmp = CockpitJU_88A4Late_LGunner.this.setOld;
                CockpitJU_88A4Late_LGunner.this.setOld = CockpitJU_88A4Late_LGunner.this.setNew;
                CockpitJU_88A4Late_LGunner.this.setNew = CockpitJU_88A4Late_LGunner.this.setTmp;
                CockpitJU_88A4Late_LGunner.this.setNew.throttle1 = 0.85F * CockpitJU_88A4Late_LGunner.this.setOld.throttle1 + CockpitJU_88A4Late_LGunner.this.fm.EI.engines[0].getControlThrottle() * 0.15F;
                CockpitJU_88A4Late_LGunner.this.setNew.prop1 = 0.85F * CockpitJU_88A4Late_LGunner.this.setOld.prop1 + CockpitJU_88A4Late_LGunner.this.fm.EI.engines[0].getControlProp() * 0.15F;
                CockpitJU_88A4Late_LGunner.this.setNew.throttle2 = 0.85F * CockpitJU_88A4Late_LGunner.this.setOld.throttle2 + CockpitJU_88A4Late_LGunner.this.fm.EI.engines[1].getControlThrottle() * 0.15F;
                CockpitJU_88A4Late_LGunner.this.setNew.prop2 = 0.85F * CockpitJU_88A4Late_LGunner.this.setOld.prop2 + CockpitJU_88A4Late_LGunner.this.fm.EI.engines[1].getControlProp() * 0.15F;
                CockpitJU_88A4Late_LGunner.this.setNew.altimeter = CockpitJU_88A4Late_LGunner.this.fm.getAltitude();
                float f = CockpitJU_88A4Late_LGunner.this.waypointAzimuth();
                CockpitJU_88A4Late_LGunner.this.setNew.waypointAzimuth.setDeg(CockpitJU_88A4Late_LGunner.this.setOld.waypointAzimuth.getDeg(0.1F), f - CockpitJU_88A4Late_LGunner.this.setOld.azimuth.getDeg(1.0F));
                CockpitJU_88A4Late_LGunner.this.setNew.azimuth.setDeg(CockpitJU_88A4Late_LGunner.this.setOld.azimuth.getDeg(1.0F), CockpitJU_88A4Late_LGunner.this.fm.Or.azimut());
                CockpitJU_88A4Late_LGunner.this.setNew.vspeed = (199F * CockpitJU_88A4Late_LGunner.this.setOld.vspeed + CockpitJU_88A4Late_LGunner.this.fm.getVertSpeed()) / 200F;
                if (CockpitJU_88A4Late_LGunner.this.cockpitDimControl) {
                    if (CockpitJU_88A4Late_LGunner.this.setNew.dimPosition > 0.0F) CockpitJU_88A4Late_LGunner.this.setNew.dimPosition = CockpitJU_88A4Late_LGunner.this.setOld.dimPosition - 0.05F;
                } else if (CockpitJU_88A4Late_LGunner.this.setNew.dimPosition < 1.0F) CockpitJU_88A4Late_LGunner.this.setNew.dimPosition = CockpitJU_88A4Late_LGunner.this.setOld.dimPosition + 0.05F;
                float f1 = CockpitJU_88A4Late_LGunner.this.prevFuel - CockpitJU_88A4Late_LGunner.this.fm.M.fuel;
                CockpitJU_88A4Late_LGunner.this.prevFuel = CockpitJU_88A4Late_LGunner.this.fm.M.fuel;
                f1 /= 0.72F;
                f1 /= Time.tickLenFs();
                f1 *= 3600F;
                CockpitJU_88A4Late_LGunner.this.setNew.cons = 0.91F * CockpitJU_88A4Late_LGunner.this.setOld.cons + 0.09F * f1;
                if (CockpitJU_88A4Late_LGunner.this.buzzerFX != null)
                    if (CockpitJU_88A4Late_LGunner.this.fm.Loc.z < ((JU_88A4Late) CockpitJU_88A4Late_LGunner.this.aircraft()).fDiveRecoveryAlt && ((JU_88A4Late) ((Interpolate) CockpitJU_88A4Late_LGunner.this.fm).actor).diveMechStage == 1)
                        CockpitJU_88A4Late_LGunner.this.buzzerFX.play();
                    else if (CockpitJU_88A4Late_LGunner.this.buzzerFX.isPlaying()) CockpitJU_88A4Late_LGunner.this.buzzerFX.stop();
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
            this.aircraft().hierMesh().chunkVisible("Turret1B_D0", false);
            this.aircraft().hierMesh().chunkVisible("Wire_D0", false);
            if (!((JU_88A4Late) this.aircraft()).topBlisterRemoved) {
                this.aircraft().hierMesh().chunkVisible("Turret2B_D0", false);
                this.aircraft().hierMesh().chunkVisible("Turret3B_D0", false);
                this.aircraft().hierMesh().chunkVisible("BlisterTop_D0", false);
                this.aircraft().hierMesh().chunkVisible("DummyBlister_D0", true);
            }
            if (!((JU_88A4Late) this.aircraft()).blisterRemoved) {
                this.aircraft().hierMesh().chunkVisible("Turret4B_D0", false);
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
        this.aircraft().hierMesh().chunkVisible("Turret1B_D0", true);
        this.aircraft().hierMesh().chunkVisible("Wire_D0", true);
        if (!((JU_88A4Late) this.aircraft()).topBlisterRemoved) {
            this.aircraft().hierMesh().chunkVisible("Turret2B_D0", true);
            this.aircraft().hierMesh().chunkVisible("Turret3B_D0", true);
            this.aircraft().hierMesh().chunkVisible("BlisterTop_D0", true);
            this.aircraft().hierMesh().chunkVisible("DummyBlister_D0", false);
        }
        if (!((JU_88A4Late) this.aircraft()).blisterRemoved) {
            this.aircraft().hierMesh().chunkVisible("Turret4B_D0", true);
            this.aircraft().hierMesh().chunkVisible("BlisterDown_D0", true);
        }
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        float f2 = f1;
        this.mesh.chunkSetAngles("zTurret2A", 0.0F, -f, 0.0F);
        this.mesh.chunkSetAngles("zTurret2B", 0.0F, f1, 0.0F);
        if (f < -10F) f = -10F;
        if (f > 35F) f = 35F;
        if (f1 < -10F) f1 = -10F;
        if (f1 > 20F) f1 = 20F;
        if (f < 0.0F && f1 < -4.5F) f1 = -4.5F;
        this.mesh.chunkSetAngles("CameraRodA", 0.0F, -f, 0.0F);
        this.mesh.chunkSetAngles("CameraRodB", 0.0F, f1, 0.0F);
        if (f2 > 20F) this.mesh.chunkSetAngles("CameraRodC", 0.0F, f2 - f1, 0.0F);
        else this.mesh.chunkSetAngles("CameraRodC", 0.0F, 0.0F, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f > 45F) f = 45F;
        if (f < -25F) f = -25F;
        if (f1 > 60F) f1 = 60F;
        if (f1 < -10F) f1 = -10F;
        if (f > 3F) {
            if (f1 < this.cvt(f, 3F, 8.4F, -1.72F, -10F)) f1 = this.cvt(f, 3F, 8.4F, -1.72F, -10F);
        } else if (f > -5.3F) {
            if (f1 < this.cvt(f, -5.3F, 3F, -1.72F, -1.72F)) f1 = this.cvt(f, -5.3F, 3F, -1.72F, -1.72F);
        } else if (f1 < this.cvt(f, -25F, -5.3F, -3F, -1.72F)) f1 = this.cvt(f, -25F, -5.3F, -3F, -1.72F);
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
    }

    protected void interpTick() {
        if (this.isRealMode()) {
            if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.bGunFire) {
                if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN02");
                this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN02");
            }
        }
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public CockpitJU_88A4Late_LGunner() {
        super("3DO/Cockpit/Ju-88A-4Late-TGun/hier-LGun.him", "he111");
        this.hook1 = null;
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.pictManf1 = 1.0F;
        this.pictManf2 = 1.0F;
        this.prevFuel = 0.0F;
        this.cockpitNightMats = new String[] { "88a4_I_Set1", "88a4_I_Set2", "88a4_I_Set3", "88a4_I_Set4", "88a4_I_Set5", "88a4_I_Set6", "88a4_SlidingGlass", "88gardinen", "lofte7_02", "Peil1", "Peil2", "Pedal", "skala" };
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
        this.mesh.chunkSetAngles("z_Object20", this.cvt(((JU_88A4Late) this.aircraft()).fSightCurSpeed, 400F, 800F, 87F, -63.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TempMeter", this.floatindex(this.cvt(Atmosphere.temperature((float) this.fm.Loc.z), 213.09F, 293.09F, 0.0F, 8F), frAirTempScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Radiator_Sw1", this.cvt(this.fm.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 0.0F, -120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Radiator_Sw2", this.cvt(this.fm.EI.engines[1].getControlRadiator(), 0.0F, 1.0F, 0.0F, -120F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSw1", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 100F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSw2", this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 100F, 0.0F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zHour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMinute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zHour2", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zMinute2", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, 7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 6000F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zAlt3", ((JU_88A4Late) this.aircraft()).fDiveRecoveryAlt * 360F / 6000F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed", this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 50F, 750F, 0.0F, 14F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zSpeed2", this.floatindex(this.cvt(this.fm.getSpeedKMH(), 50F, 750F, 0.0F, 14F), speedometerScale2), 0.0F, 0.0F);
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
        this.mesh.chunkSetAngles("zFuel1", this.cvt(this.fm.M.fuel, 0.0F, 1008F, 0.0F, 77F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("zFuel2", this.cvt(this.fm.M.fuel, 0.0F, 1008F, 0.0F, 77F), 0.0F, 0.0F);
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

    private Hook               hook1;
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
    private static final float speedometerScale[]  = { 0.0F, 16F, 35.5F, 60.5F, 88F, 112.5F, 136F, 159.5F, 186.5F, 211.5F, 240F, 268F, 295.5F, 321F, 347F };
    private static final float speedometerScale2[] = { 0.0F, 23.5F, 47.5F, 72F, 95.5F, 120F, 144.5F, 168.5F, 193F, 217F, 241F, 265F, 288F, 311.5F, 335.5F };
    private static final float frAirTempScale[]    = { 76.5F, 68F, 57F, 44.5F, 29.5F, 14.5F, 1.5F, -10F, -19F };

    static {
        Property.set(CockpitJU_88A4Late_LGunner.class, "aiTuretNum", 1);
        Property.set(CockpitJU_88A4Late_LGunner.class, "weaponControlNum", 11);
        Property.set(CockpitJU_88A4Late_LGunner.class, "astatePilotIndx", 2);
        Property.set(CockpitJU_88A4Late_LGunner.class, "normZN", 0.75F);
        Property.set(CockpitJU_88A4Late_LGunner.class, "gsZN", 0.75F);
    }
}
