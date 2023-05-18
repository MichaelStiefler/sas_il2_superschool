package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitLeo extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if (CockpitLeo.this.fm != null) {
                CockpitLeo.this.setTmp = CockpitLeo.this.setOld;
                CockpitLeo.this.setOld = CockpitLeo.this.setNew;
                CockpitLeo.this.setNew = CockpitLeo.this.setTmp;
                if (CockpitLeo.this.eBombs[0] == null) {
                    CockpitLeo.this.eBombs[0] = CockpitLeo.this.aircraft().getBulletEmitterByHookName("_BombSpawn06");
                    CockpitLeo.this.eBombs[1] = CockpitLeo.this.aircraft().getBulletEmitterByHookName("_BombSpawn10");
                    CockpitLeo.this.eBombs[2] = CockpitLeo.this.aircraft().getBulletEmitterByHookName("_BombSpawn18");
                    CockpitLeo.this.eBombs[3] = CockpitLeo.this.aircraft().getBulletEmitterByHookName("_BombSpawn28");
                    CockpitLeo.this.eBombs[4] = CockpitLeo.this.aircraft().getBulletEmitterByHookName("_BombSpawn07");
                    CockpitLeo.this.eBombs[5] = CockpitLeo.this.aircraft().getBulletEmitterByHookName("_BombSpawn08");
                    CockpitLeo.this.eBombs[6] = CockpitLeo.this.aircraft().getBulletEmitterByHookName("_BombSpawn58");
                    CockpitLeo.this.eBombs[7] = CockpitLeo.this.aircraft().getBulletEmitterByHookName("_BombSpawn68");
                    CockpitLeo.this.eBombs[8] = CockpitLeo.this.aircraft().getBulletEmitterByHookName("_BombSpawn78");
                }
                for (int i = 0; i < 9; i++) {
                    CockpitLeo.this.bBombs[i] = CockpitLeo.this.eBombs[i].haveBullets();
                }

                CockpitLeo.this.setNew.throttle1 = (0.9F * CockpitLeo.this.setOld.throttle1) + (0.1F * CockpitLeo.this.fm.EI.engines[0].getControlThrottle());
                CockpitLeo.this.setNew.prop1 = (0.9F * CockpitLeo.this.setOld.prop1) + (0.1F * CockpitLeo.this.fm.EI.engines[0].getControlProp());
                CockpitLeo.this.setNew.mix1 = (0.8F * CockpitLeo.this.setOld.mix1) + (0.2F * CockpitLeo.this.fm.EI.engines[0].getControlMix());
                CockpitLeo.this.setNew.man1 = (0.92F * CockpitLeo.this.setOld.man1) + (0.08F * CockpitLeo.this.fm.EI.engines[0].getManifoldPressure());
                CockpitLeo.this.setNew.throttle2 = (0.9F * CockpitLeo.this.setOld.throttle2) + (0.1F * CockpitLeo.this.fm.EI.engines[1].getControlThrottle());
                CockpitLeo.this.setNew.prop2 = (0.9F * CockpitLeo.this.setOld.prop2) + (0.1F * CockpitLeo.this.fm.EI.engines[1].getControlProp());
                CockpitLeo.this.setNew.mix2 = (0.8F * CockpitLeo.this.setOld.mix2) + (0.2F * CockpitLeo.this.fm.EI.engines[1].getControlMix());
                CockpitLeo.this.setNew.man2 = (0.92F * CockpitLeo.this.setOld.man2) + (0.08F * CockpitLeo.this.fm.EI.engines[1].getManifoldPressure());
                CockpitLeo.this.setNew.altimeter = CockpitLeo.this.fm.getAltitude();
                CockpitLeo.this.setNew.stage1 = (0.85F * CockpitLeo.this.setOld.stage1) + (CockpitLeo.this.fm.EI.engines[0].getControlCompressor() * 0.15F);
                CockpitLeo.this.setNew.stage2 = (0.85F * CockpitLeo.this.setOld.stage2) + (CockpitLeo.this.fm.EI.engines[1].getControlCompressor() * 0.15F);
                CockpitLeo.this.setNew.azimuth.setDeg(CockpitLeo.this.setOld.azimuth.getDeg(1.0F), CockpitLeo.this.fm.Or.azimut());
                CockpitLeo.this.setNew.vspeed = ((100F * CockpitLeo.this.setOld.vspeed) + CockpitLeo.this.fm.getVertSpeed()) / 101F;
                if (CockpitLeo.this.useRealisticNavigationInstruments()) {
                    CockpitLeo.this.setNew.waypointAzimuth.setDeg(CockpitLeo.this.setOld.waypointAzimuth.getDeg(1.0F), CockpitLeo.this.getBeaconDirection());
                } else {
                    CockpitLeo.this.setNew.waypointAzimuth.setDeg(CockpitLeo.this.setOld.waypointAzimuth.getDeg(0.1F), CockpitLeo.this.waypointAzimuth() - CockpitLeo.this.setOld.azimuth.getDeg(1.0F));
                }
                CockpitLeo.this.mesh.chunkSetAngles("Z_RTurret", CockpitLeo.this.aircraft().FM.turret[0].tu[0], 0.0F, 0.0F);
                CockpitLeo.this.mesh.chunkSetAngles("Z_RGun", CockpitLeo.this.aircraft().FM.turret[0].tu[1], 0.0F, 0.0F);
                CockpitLeo.this.setNew.inert = (0.999F * CockpitLeo.this.setOld.inert) + (0.001F * (CockpitLeo.this.fm.EI.engines[0].getStage() == 6 ? 0.867F : 0.0F));
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
        float      man1;
        float      man2;
        float      altimeter;
        AnglesFork azimuth;
        AnglesFork waypointAzimuth;
        float      vspeed;
        float      inert;
        float      stage1;
        float      stage2;

        private Variables() {
            this.azimuth = new AnglesFork();
            this.waypointAzimuth = new AnglesFork();
        }
    }

    protected float waypointAzimuth() {
        return this.waypointAzimuthInvertMinus(10F);
    }

    public CockpitLeo() {
        super("3DO/Cockpit/LeO-451/hier.him", "he111");
        this.bBombs = new boolean[9];
        this.eBombs = new BulletEmitter[9];
        this.bNeedSetUp = true;
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.cockpitNightMats = (new String[] { "GP_I", "GP_II", "GP_II_DM", "GP_III_DM", "GP_III", "GP_IV_DM", "GP_IV", "GP_V", "GP_VI", "GP_VII", "compass", "Eqpt_II", "Trans_II", "Trans_VI_Pilot", "Trans_VII_Pilot" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
    }

    protected boolean doFocusEnter() {
        return super.doFocusEnter();
    }

    protected void doFocusLeave() {
        super.doFocusLeave();
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
        this.mesh.materialReplace("Gloss1D0o", mat);
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        if (Math.abs(this.fm.CT.getTrimAileronControl() - this.fm.CT.trimAileron) > 1E-006F) {
            if ((this.fm.CT.getTrimAileronControl() - this.fm.CT.trimAileron) > 0.0F) {
                this.mesh.chunkSetAngles("Z_Trim1", -20F, 0.0F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("Z_Trim1", 20F, 0.0F, 0.0F);
            }
        } else {
            this.mesh.chunkSetAngles("Z_Trim1", 0.0F, 0.0F, 0.0F);
        }
        if (Math.abs(this.fm.CT.getTrimRudderControl() - this.fm.CT.trimRudder) > 1E-006F) {
            if ((this.fm.CT.getTrimRudderControl() - this.fm.CT.trimRudder) > 0.0F) {
                this.mesh.chunkSetAngles("Z_Trim2", -20F, 0.0F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("Z_Trim2", 20F, 0.0F, 0.0F);
            }
        } else {
            this.mesh.chunkSetAngles("Z_Trim2", 0.0F, 0.0F, 0.0F);
        }
        if (Math.abs(this.fm.CT.getTrimElevatorControl() - this.fm.CT.trimElevator) > 1E-006F) {
            if ((this.fm.CT.getTrimElevatorControl() - this.fm.CT.trimElevator) > 0.0F) {
                this.mesh.chunkSetAngles("Z_Trim3", -20F, 0.0F, 0.0F);
            } else {
                this.mesh.chunkSetAngles("Z_Trim3", 20F, 0.0F, 0.0F);
            }
        } else {
            this.mesh.chunkSetAngles("Z_Trim3", 0.0F, 0.0F, 0.0F);
        }
        this.resetYPRmodifier();
        if (this.fm.CT.FlapsControl > 0.5F) {
            Cockpit.xyz[2] = -0.0175F;
        }
        this.mesh.chunkSetLocate("Z_Flaps1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Gear1", this.fm.CT.GearControl > 0.5F ? -60F : 0.0F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle1", -31F * this.interp(this.setNew.throttle1, this.setOld.throttle1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Throtle2", -31F * this.interp(this.setNew.throttle2, this.setOld.throttle2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop1", -720F * this.interp(this.setNew.prop1, this.setOld.prop1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Prop2", -720F * this.interp(this.setNew.prop2, this.setOld.prop2, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture1", 41.67F * this.interp(this.setNew.mix1, this.setOld.mix1, f), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture2", 41.67F * this.interp(this.setNew.mix2, this.setOld.mix2, f), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = -0.095F * this.fm.CT.getRudder();
        this.mesh.chunkSetLocate("Z_RightPedal", Cockpit.xyz, Cockpit.ypr);
        Cockpit.xyz[1] = -Cockpit.xyz[1];
        this.mesh.chunkSetLocate("Z_LeftPedal", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Columnbase", -8F * (this.pictElev = (0.65F * this.pictElev) + (0.35F * this.fm.CT.ElevatorControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Column", -45F * (this.pictAiler = (0.65F * this.pictAiler) + (0.35F * this.fm.CT.AileronControl)), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Brake", 21.5F * this.fm.CT.BrakeControl, 0.0F, 0.0F);
        this.resetYPRmodifier();
        if (this.fm.CT.AirBrakeControl > 0.5F) {
            Cockpit.xyz[1] = -0.0175F;
        }
        this.mesh.chunkSetLocate("Z_AirBrake", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_MagnetoL", this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, 0.0F, -85F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoR", this.cvt(this.fm.EI.engines[1].getControlMagnetos(), 0.0F, 3F, 0.0F, 85F), 0.0F, 0.0F);
        this.resetYPRmodifier();
        if (this.fm.EI.engines[0].getControlRadiator() < 0.5F) {
            Cockpit.xyz[2] = -0.011F;
        }
        this.mesh.chunkSetLocate("Z_RadL", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        if (this.fm.EI.engines[1].getControlRadiator() < 0.5F) {
            Cockpit.xyz[2] = -0.011F;
        }
        this.mesh.chunkSetLocate("Z_RadR", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Altimeter1", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter2", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter3", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -720F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Altimeter4", this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 20000F, 0.0F, -7200F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer1", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitLeo.speedometerScale), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Speedometer2", -this.floatindex(this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 800F, 0.0F, 16F), CockpitLeo.speedometerScale), 0.0F, 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.Or.getTangage(), -45F, 45F, 0.018F, -0.018F);
        this.mesh.chunkSetLocate("Z_TurnBank1", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_TurnBank1Q", -this.fm.Or.getKren(), 0.0F, 0.0F);
        this.w.set(this.fm.getW());
        this.fm.Or.transform(this.w);
        this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.w.z, -0.23562F, 0.23562F, -27F, 27F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_TurnBank3", this.cvt(this.getBall(7D), -7F, 7F, 10F, -10F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Climb1", this.cvt(this.setNew.vspeed, -30F, 30F, 180F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass1", this.cvt(this.setNew.waypointAzimuth.getDeg(f * 0.2F), -90F, 90F, 55.5F, -55.5F), 0.0F, 0.0F);
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
        if (this.fm.M.fuel > 1.0F) {
            f1 = this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 570F, 0.0F, 0.26F);
        }
        this.mesh.chunkSetAngles("Z_FuelPres1", this.cvt(f1, 0.0F, 1.0F, 0.0F, -270F), 0.0F, 0.0F);
        f1 = 0.0F;
        if (this.fm.M.fuel > 1.0F) {
            f1 = this.cvt(this.fm.EI.engines[1].getRPM(), 0.0F, 570F, 0.0F, 0.26F);
        }
        this.mesh.chunkSetAngles("Z_FuelPres2", this.cvt(f1, 0.0F, 1.0F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Temp2", this.cvt(this.fm.EI.engines[1].tWaterOut, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pres1", this.cvt(this.setNew.man1, 0.399966F, 1.599864F, 0.0F, -300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Pres2", this.cvt(this.setNew.man2, 0.399966F, 1.599864F, 0.0F, -300F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil1", this.cvt(this.fm.EI.engines[0].tOilIn, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oil2", this.cvt(this.fm.EI.engines[1].tOilIn, 0.0F, 160F, 0.0F, -75F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres1", this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilOut * this.fm.EI.engines[0].getReadyness()), 0.0F, 7.45F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Oilpres2", this.cvt(1.0F + (0.05F * this.fm.EI.engines[1].tOilOut * this.fm.EI.engines[1].getReadyness()), 0.0F, 7.45F, 0.0F, -270F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_AirPres", -116F, 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_HPres", this.fm.Gears.isHydroOperable() ? -102F : 0.0F, 0.0F, 0.0F);
        f1 = Atmosphere.temperature((float) this.fm.Loc.z) - 273.09F;
        if (f1 < -40F) {
            this.mesh.chunkSetAngles("Z_AirTemp", this.cvt(f1, -40F, -20F, 52F, 35F), 0.0F, 0.0F);
        } else if (f1 < 0.0F) {
            this.mesh.chunkSetAngles("Z_AirTemp", this.cvt(f1, -20F, 0.0F, 35F, 0.0F), 0.0F, 0.0F);
        } else if (f1 < 20F) {
            this.mesh.chunkSetAngles("Z_AirTemp", this.cvt(f1, 0.0F, 20F, 0.0F, -18.5F), 0.0F, 0.0F);
        } else {
            this.mesh.chunkSetAngles("Z_AirTemp", this.cvt(f1, 20F, 50F, -18.5F, -37F), 0.0F, 0.0F);
        }
        this.mesh.chunkSetAngles("Z_FlapPos", this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, -180F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_Compass4", this.setNew.azimuth.getDeg(f), 0.0F, 0.0F);
        this.mesh.chunkVisible("XRGearUp", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLGearUp", (this.fm.CT.getGear() < 0.01F) || !this.fm.Gears.lgear);
        this.mesh.chunkVisible("XCGearUp", this.fm.CT.getGear() < 0.01F);
        this.mesh.chunkVisible("XRGearDn", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.rgear);
        this.mesh.chunkVisible("XLGearDn", (this.fm.CT.getGear() > 0.99F) && this.fm.Gears.lgear);
        this.mesh.chunkVisible("XCGearDn", this.fm.CT.getGear() > 0.99F);
        this.mesh.chunkVisible("XFlapsUp", this.fm.CT.getFlap() > 0.5F);
        this.mesh.chunkVisible("XOverG1", this.fm.getOverload() > 3F);
        this.mesh.chunkVisible("XOverG2", this.fm.getOverload() < -1F);
        this.mesh.chunkVisible("XARimCenter", Math.abs(this.fm.CT.getTrimAileronControl()) < 0.02F);
        this.mesh.chunkVisible("XERimCenter", Math.abs(this.fm.CT.getTrimElevatorControl()) < 0.02F);
        this.mesh.chunkVisible("XRRimCenter", Math.abs(this.fm.CT.getTrimRudderControl()) < 0.02F);
        this.mesh.chunkVisible("XBomb1", this.bBombs[0]);
        this.mesh.chunkVisible("XBomb2", this.bBombs[1]);
        this.mesh.chunkVisible("XBomb3", this.bBombs[2]);
        this.mesh.chunkVisible("XBomb4", this.bBombs[3]);
        this.mesh.chunkVisible("XBomb5", this.bBombs[4]);
        this.mesh.chunkVisible("XBomb6", this.bBombs[5]);
        this.mesh.chunkVisible("XBomb7", this.bBombs[6]);
        this.mesh.chunkVisible("XBomb8", this.bBombs[7]);
        this.mesh.chunkVisible("XBomb9", this.bBombs[8]);
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
        if (((this.fm.AS.astateCockpitState & 2) != 0) && ((this.fm.AS.astateCockpitState & 0x40) != 0)) {
            this.mesh.chunkVisible("Panel_D0", false);
            this.mesh.chunkVisible("Panel_D1", false);
            this.mesh.chunkVisible("Panel_D2", true);
            this.mesh.chunkVisible("Z_Altimeter1", false);
            this.mesh.chunkVisible("Z_Altimeter2", false);
            this.mesh.chunkVisible("Z_Speedometer1", false);
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

    private boolean            bNeedSetUp;
    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    private boolean            bBombs[];
    private BulletEmitter      eBombs[];
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private static final float speedometerScale[] = { 0.0F, 0.0F, 10.5F, 42.5F, 85F, 125F, 165.5F, 181F, 198F, 214.5F, 231F, 249F, 266.5F, 287.5F, 308F, 326.5F, 346F };
}
