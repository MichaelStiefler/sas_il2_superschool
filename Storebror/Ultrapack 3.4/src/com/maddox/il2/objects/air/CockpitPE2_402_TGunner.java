package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Message;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitPE2_402_TGunner extends CockpitGunner {
    private class Variables {

        float      man1;
        float      man2;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      inert;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }

    }

    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitPE2_402_TGunner.this.fm != null) {
                CockpitPE2_402_TGunner.this.setTmp = CockpitPE2_402_TGunner.this.setOld;
                CockpitPE2_402_TGunner.this.setOld = CockpitPE2_402_TGunner.this.setNew;
                CockpitPE2_402_TGunner.this.setNew = CockpitPE2_402_TGunner.this.setTmp;
                if (CockpitPE2_402_TGunner.this.fm.CT.Weapons[2] != null) {
                    CockpitPE2_402_TGunner.this.bHaveDAG10 = true;
                    if (CockpitPE2_402_TGunner.this.fm.CT.Weapons[2].length > 0) CockpitPE2_402_TGunner.this.bDAG10 = CockpitPE2_402_TGunner.this.fm.CT.Weapons[2][0].haveBullets();
                    else CockpitPE2_402_TGunner.this.bDAG10 = false;
                }
                if (CockpitPE2_402_TGunner.this.fm.CT.Weapons[3] != null) if (((PE_2NEW) CockpitPE2_402_TGunner.this.aircraft()).BombLoadType == 1) {
                    CockpitPE2_402_TGunner.this.bBombs[5] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][2].haveBullets();
                    CockpitPE2_402_TGunner.this.bBombs[4] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][3].haveBullets();
                    CockpitPE2_402_TGunner.this.bBombs[7] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][4].haveBullets();
                    CockpitPE2_402_TGunner.this.bBombs[6] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][5].haveBullets();
                } else if (((PE_2NEW) CockpitPE2_402_TGunner.this.aircraft()).BombLoadType == 2) {
                    CockpitPE2_402_TGunner.this.bBombs[1] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][0].haveBullets();
                    CockpitPE2_402_TGunner.this.bBombs[0] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][1].haveBullets();
                } else if (((PE_2NEW) CockpitPE2_402_TGunner.this.aircraft()).BombLoadType == 3) {
                    CockpitPE2_402_TGunner.this.bBombs[1] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][0].haveBullets();
                    CockpitPE2_402_TGunner.this.bBombs[0] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][1].haveBullets();
                    CockpitPE2_402_TGunner.this.bBombs[7] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][2].haveBullets();
                    CockpitPE2_402_TGunner.this.bBombs[6] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][3].haveBullets();
                } else if (((PE_2NEW) CockpitPE2_402_TGunner.this.aircraft()).BombLoadType == 4) {
                    CockpitPE2_402_TGunner.this.bBombs[1] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][0].haveBullets();
                    CockpitPE2_402_TGunner.this.bBombs[0] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][1].haveBullets();
                    CockpitPE2_402_TGunner.this.bBombs[3] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][2].haveBullets();
                    CockpitPE2_402_TGunner.this.bBombs[2] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][3].haveBullets();
                } else if (((PE_2NEW) CockpitPE2_402_TGunner.this.aircraft()).BombLoadType == 5) {
                    CockpitPE2_402_TGunner.this.bBombs[5] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][2].haveBullets();
                    CockpitPE2_402_TGunner.this.bBombs[4] = CockpitPE2_402_TGunner.this.fm.CT.Weapons[3][3].haveBullets();
                }
                CockpitPE2_402_TGunner.this.setNew.man1 = 0.92F * CockpitPE2_402_TGunner.this.setOld.man1 + 0.08F * CockpitPE2_402_TGunner.this.fm.EI.engines[0].getManifoldPressure();
                CockpitPE2_402_TGunner.this.setNew.man2 = 0.92F * CockpitPE2_402_TGunner.this.setOld.man2 + 0.08F * CockpitPE2_402_TGunner.this.fm.EI.engines[1].getManifoldPressure();
                CockpitPE2_402_TGunner.this.setNew.altimeter = CockpitPE2_402_TGunner.this.fm.getAltitude();
                CockpitPE2_402_TGunner.this.setNew.azimuth.setDeg(CockpitPE2_402_TGunner.this.setOld.azimuth.getDeg(1.0F), CockpitPE2_402_TGunner.this.fm.Or.azimut());
                CockpitPE2_402_TGunner.this.setNew.vspeed = (100F * CockpitPE2_402_TGunner.this.setOld.vspeed + CockpitPE2_402_TGunner.this.fm.getVertSpeed()) / 101F;
                if (CockpitPE2_402_TGunner.this.useRealisticNavigationInstruments())
                    CockpitPE2_402_TGunner.this.setNew.waypointAzimuth.setDeg(CockpitPE2_402_TGunner.this.setOld.waypointAzimuth.getDeg(0.1F), CockpitPE2_402_TGunner.this.getBeaconDirection());
                else CockpitPE2_402_TGunner.this.setNew.waypointAzimuth.setDeg(CockpitPE2_402_TGunner.this.setOld.waypointAzimuth.getDeg(0.1F), CockpitPE2_402_TGunner.this.waypointAzimuth() - CockpitPE2_402_TGunner.this.setOld.azimuth.getDeg(1.0F));
                CockpitPE2_402_TGunner.this.setNew.inert = 0.999F * CockpitPE2_402_TGunner.this.setOld.inert + 0.001F * (CockpitPE2_402_TGunner.this.fm.EI.engines[0].getStage() == 6 ? 0.867F : 0.0F);
            }
            return true;
        }

        Interpolater() {
        }
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
        HierMesh hiermesh = this.aircraft().hierMesh();
        com.maddox.il2.engine.Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.mesh.chunkVisible("WireL_D0", this.aircraft().hierMesh().isChunkVisible("WireL_D0"));
        this.mesh.chunkVisible("WireR_D0", this.aircraft().hierMesh().isChunkVisible("WireR_D0"));
        this.mesh.chunkSetAngles("Z_Gear1", this.fm.CT.GearControl > 0.5F ? -60F : 0.0F, 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = -0.095F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_RightPedal", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Columnbase", -8F * (this.pictElev = 0.65F * this.pictElev + 0.35F * this.fm.CT.ElevatorControl), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", -45F * (this.pictAiler = 0.65F * this.pictAiler + 0.35F * this.fm.CT.AileronControl), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Brake", 21.5F * this.fm.CT.BrakeControl, 0.0F, 0.0F);
        this.resetYPRmodifier();
        this.resetYPRmodifier();
        if (this.fm.EI.engines[0].getControlRadiator() > 0.5F) Cockpit.xyz[1] = 0.011F;
        this.mesh.chunkSetLocate("Z_RadL", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (this.fm.EI.engines[1].getControlRadiator() > 0.5F) Cockpit.xyz[1] = 0.011F;
        this.mesh.chunkSetLocate("Z_RadR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter3", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter4", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), speedometerScale), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.018F, -0.018F);
        this.mesh.chunkSetLocate("Z_TurnBank1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_TurnBank1Q", -this.fm.Or.getKren(), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.w.z, -0.23562F, 0.23562F, -27F, 27F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.cvt(this.setNew.vspeed, -30F, 30F, 180F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", this.cvt(this.setNew.waypointAzimuth.getDeg(f), -90F, 90F, -55.5F, 55.5F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass2", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM1", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, -360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_RPM2", this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 10000F, 0.0F, -3600F), 0.0F, 0.0F);
        if ((this.fm.AS.astateCockpitState & 0x40) == 0) {
            this.mesh.chunkSetAngles("Z_RPM3", this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 10000F, 0.0F, -360F), 0.0F, 0.0F);
            this.mesh.chunkSetAngles("Z_RPM4", this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 10000F, 0.0F, -3600F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Fuel1", this.cvt(this.fm.M.fuel, 0.0F, 360F, 0.0F, -198F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_InertGas", this.cvt(this.setNew.inert, 0.0F, 1.0F, 0.0F, -300F), 0.0F, 0.0F);
        float f1 = 0.0F;
        if (this.fm.M.fuel > 1.0F) f1 = this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 570F, 0.0F, 0.26F);
        this.mesh.chunkSetAngles("Z_FuelPres1", this.cvt(f1, 0.0F, 1.0F, 0.0F, -270F), 0.0F, 0.0F);
        f1 = 0.0F;
        if (this.fm.M.fuel > 1.0F) f1 = this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 570F, 0.0F, 0.26F);
        this.mesh.chunkSetAngles("Z_FuelPres2", this.cvt(f1, 0.0F, 1.0F, 0.0F, -270F), 0.0F, 0.0F);
        this.PrivateDevices();
        this.mesh.chunkSetAngles("Z_Pres1", this.cvt(this.setNew.man1, 0.399966F, 1.599864F, 0.0F, -300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pres2", this.cvt(this.setNew.man2, 0.399966F, 1.599864F, 0.0F, -300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil2", this.cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness(), 0.0F, 7.45F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres2", this.cvt(1.0F + 0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness(), 0.0F, 7.45F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AirPres", -116F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_HPres", this.fm.Gears.isHydroOperable() ? -102F : 0.0F, 0.0F, 0.0F);
        f1 = Atmosphere.temperature((float) this.fm.Loc.z) - 273.09F;
        if (f1 < -40F) this.mesh.chunkSetAngles("Z_AirTemp", this.cvt(f1, -70F, -40F, 52F, 35F), 0.0F, 0.0F);
        else if (f1 < 0.0F) this.mesh.chunkSetAngles("Z_AirTemp", this.cvt(f1, -40F, 0.0F, 35F, 0.0F), 0.0F, 0.0F);
        else if (f1 < 20F) this.mesh.chunkSetAngles("Z_AirTemp", this.cvt(f1, 0.0F, 20F, 0.0F, -18.5F), 0.0F, 0.0F);
        else this.mesh.chunkSetAngles("Z_AirTemp", this.cvt(f1, 20F, 50F, -18.5F, -37F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_FlapPos", this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass4", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkVisible("XFlapsUp", this.fm.CT.getFlap() > 0.5F);
        this.mesh.chunkVisible("XOverG1", this.fm.getOverload() > 3F);
        this.mesh.chunkVisible("XOverG2", this.fm.getOverload() < -1F);
        this.mesh.chunkVisible("XERimCenter", Math.abs(this.fm.CT.getTrimElevatorControl()) < 0.02F);
        this.mesh.chunkVisible("XBomb1", this.bBombs[0]);
        this.mesh.chunkVisible("XBomb2", this.bBombs[1]);
        this.mesh.chunkVisible("XBomb3", this.bBombs[2]);
        this.mesh.chunkVisible("XBomb4", this.bBombs[3]);
        this.mesh.chunkVisible("XBomb5", this.bBombs[4]);
        this.mesh.chunkVisible("XBomb6", this.bBombs[5]);
        this.mesh.chunkVisible("XBomb7", this.bBombs[6]);
        this.mesh.chunkVisible("XBomb8", this.bBombs[7]);
        this.mesh.chunkSetAngles("Z_arrow_hour", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_arrow_min", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_arrow_sec", -this.cvt(World.getTimeofDay() % 1.0F * 60F % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SW_5", 0.0F, this.fm.AS.bNavLightsOn ? 60F : 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SW_6", 0.0F, this.fm.AS.bNavLightsOn ? 60F : 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SW_7", 0.0F, this.fm.AS.bLandingLightOn ? 60F : 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SW_4", 0.0F, this.cockpitLightControl ? 60F : 0.0F, 0.0F);
        this.mesh.chunkSetAngles("SW_8", 0.0F, this.cockpitLightControl ? 60F : 0.0F, 0.0F);
        if (this.bHaveDAG10) this.mesh.chunkVisible("XDAG10", this.bDAG10);
        this.mesh.chunkSetAngles("Z_Mixture1", 41.67F * this.fm.EI.engines[0].getControlMix(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2", 41.67F * this.fm.EI.engines[1].getControlMix(), 0.0F, 0.0F);
    }

    public void PrivateDevices() {
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
    }

    protected boolean doFocusEnter() {
        if (super.doFocusEnter()) {
            this.aircraft().hierMesh().chunkVisible("NDetails_D0", false);
            this.aircraft().hierMesh().chunkVisible("Blister1_D0", false);
            this.aircraft().hierMesh().chunkVisible("QBlister2_D0", false);
            return true;
        } else return false;
    }

    protected void doFocusLeave() {
        this.aircraft().hierMesh().chunkVisible("NDetails_D0", true);
        this.aircraft().hierMesh().chunkVisible("Blister1_D0", true);
        this.aircraft().hierMesh().chunkVisible("QBlister2_D0", true);
        super.doFocusLeave();
    }

    public void moveGun(Orient orient) {
        super.moveGun(orient);
        this.mesh.chunkSetAngles("TurrelA", -orient.getYaw(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurrelB", -orient.getTangage(), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("TurrelC", -this.cvt(orient.getTangage(), -4F, 45F, -4F, 45F), 0.0F, 0.0F);
    }

    public void clipAnglesGun(Orient orient) {
        if (!this.isRealMode()) return;
        if (!this.aiTurret().bIsOperable) {
            orient.setYPR(0.0F, 0.0F, 0.0F);
            return;
        }
        float f = orient.getYaw();
        float f1 = orient.getTangage();
        if (f < -110F) f = -110F;
        if (f > 90F) f = 90F;
        if (f1 > 50F) f1 = 50F;
        if (f1 < -6F) f1 = -6F;
        orient.setYPR(f, f1, 0.0F);
        orient.wrap();
        this.StopFire = false;
        if (f1 < -3.1F) if (f > -9F && f < 10.9F) this.StopFire = true;
        else if (f > -83.7F && f < -70.5F) this.StopFire = true;
        else if (f > 70.2F && f < 85.2F) this.StopFire = true;
    }

    protected void interpTick() {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) this.bGunFire = false;
        if (this.bGunFire && !this.StopFire) {
            this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
            if (this.hook1 == null) this.hook1 = new HookNamed(this.aircraft(), "_MGUN01");
            this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN01");
        } else this.fm.CT.WeaponControl[this.weaponControlNum()] = false;
    }

    public void doGunFire(boolean flag) {
        if (!this.isRealMode()) return;
        if (this.emitter == null || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable || this.StopFire) this.bGunFire = false;
        else this.bGunFire = flag;
        this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("XGlassDamage2", true);
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", true);
            this.mesh.chunkVisible("Z_Fuel1", false);
            this.mesh.chunkVisible("Z_Pres1", false);
            this.mesh.chunkVisible("Z_Altimeter3", false);
            this.mesh.chunkVisible("Z_Altimeter4", false);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("XGlassDamage3", true);
            this.mesh.chunkVisible("XHullDamage1", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage5", true);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("XGlassDamage4", true);
            this.mesh.chunkVisible("XHullDamage2", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("XGlassDamage1", true);
            this.mesh.chunkVisible("XGlassDamage6", true);
            this.mesh.chunkVisible("XHullDamage3", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0 && (this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", false);
            this.mesh.chunkVisible("Panel_D2", true);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Speedometer1", false);
            this.mesh.chunkVisible("Z_Speedometer2", false);
            this.mesh.chunkVisible("Z_AirTemp", false);
            this.mesh.chunkVisible("Z_Pres2", false);
            this.mesh.chunkVisible("Z_RPM1", false);
            this.mesh.chunkVisible("Z_RPM2", false);
            this.mesh.chunkVisible("Z_InertGas", false);
            this.mesh.chunkVisible("Z_FuelPres2", false);
            this.mesh.chunkVisible("Z_Oilpres1", false);
            this.mesh.chunkVisible("Z_Oilpres2", false);
        }
        this.retoggleLight();
    }

    public CockpitPE2_402_TGunner() {
        this("3DO/Cockpit/Pe-2series402-TGun/hier.him");
    }

    public CockpitPE2_402_TGunner(String s) {
        super(s, "he111_gunner");
        this.bDAG10 = false;
        this.bHaveDAG10 = false;
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.hook1 = null;
        this.cockpitNightMats = new String[] { "GP_I", "GP_II", "GP_II_DM", "GP_III_DM", "GP_III", "GP_IV_DM", "GP_IV", "GP_V", "GP_VI", "GP_VII", "Eqpt_II", "Trans_II", "Trans_VI_Pilot", "Trans_VII_Pilot", "acho", "acho_arrow", "GP_VIII" };
        this.setNightMats(false);
        this.interpPut(new Interpolater(), (Object) null, Time.current(), (Message) null);
        this.StopFire = false;
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private boolean            bBombs[]           = { false, false, false, false, false, false, false, false };
    private boolean            bDAG10;
    private boolean            bHaveDAG10;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 10.5F, 42.5F, 85F, 125F, 165.5F, 181F, 198F, 214.5F, 231F, 249F, 266.5F, 287.5F, 308F, 326.5F, 346F };
    private Hook               hook1;
    private boolean            StopFire;

    static {
        Property.set(CockpitPE2_402_TGunner.class, "aiTuretNum", 0);
        Property.set(CockpitPE2_402_TGunner.class, "weaponControlNum", 10);
        Property.set(CockpitPE2_402_TGunner.class, "astatePilotIndx", 1);
    }
}
