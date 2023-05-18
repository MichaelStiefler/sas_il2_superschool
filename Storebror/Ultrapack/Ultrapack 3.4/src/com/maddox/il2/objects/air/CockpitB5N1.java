package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.ReverbFXRoom;

public class CockpitB5N1 extends CockpitPilot {
    class Interpolater extends InterpolateRef {

        public boolean tick() {
            if ((CockpitB5N1.this.ac != null) && CockpitB5N1.this.ac.bChangedPit) {
                CockpitB5N1.this.reflectPlaneToModel();
                CockpitB5N1.this.ac.bChangedPit = false;
            }
            if (CockpitB5N1.this.fm != null) {
                CockpitB5N1.this.setTmp = CockpitB5N1.this.setOld;
                CockpitB5N1.this.setOld = CockpitB5N1.this.setNew;
                CockpitB5N1.this.setNew = CockpitB5N1.this.setTmp;
                CockpitB5N1.this.setNew.flaps = (0.9F * CockpitB5N1.this.setOld.flaps) + (0.1F * CockpitB5N1.this.fm.CT.FlapsControl);
                CockpitB5N1.this.setNew.hook = (0.9F * CockpitB5N1.this.setOld.hook) + (0.1F * CockpitB5N1.this.fm.CT.arrestorControl);
                CockpitB5N1.this.setNew.gear = (0.7F * CockpitB5N1.this.setOld.gear) + (0.3F * CockpitB5N1.this.fm.CT.GearControl);
                CockpitB5N1.this.setNew.throttle = (0.8F * CockpitB5N1.this.setOld.throttle) + (0.2F * CockpitB5N1.this.fm.CT.PowerControl);
                CockpitB5N1.this.setNew.prop = (0.8F * CockpitB5N1.this.setOld.prop) + (0.2F * CockpitB5N1.this.fm.EI.engines[0].getControlProp());
                CockpitB5N1.this.setNew.mix = (0.8F * CockpitB5N1.this.setOld.mix) + (0.2F * CockpitB5N1.this.fm.EI.engines[0].getControlMix());
                CockpitB5N1.this.setNew.altimeter = CockpitB5N1.this.fm.getAltitude();
                CockpitB5N1.this.setNew.azimuth.setDeg(CockpitB5N1.this.setOld.azimuth.getDeg(1.0F), CockpitB5N1.this.fm.Or.azimut());
                CockpitB5N1.this.setNew.vspeed = ((399F * CockpitB5N1.this.setOld.vspeed) + CockpitB5N1.this.fm.getVertSpeed()) / 400F;
                if (CockpitB5N1.this.gearLightsDamaged && (Math.random() > 0.5D)) {
                    CockpitB5N1.this.flickerLight = !CockpitB5N1.this.flickerLight;
                }
                CockpitB5N1.this.w.set(CockpitB5N1.this.fm.getW());
                CockpitB5N1.this.fm.Or.transform(CockpitB5N1.this.w);
                CockpitB5N1.this.setNew.turn = ((12F * CockpitB5N1.this.setOld.turn) + CockpitB5N1.this.w.z) / 13F;
                CockpitB5N1.this.setNew.manifold = (0.8F * CockpitB5N1.this.setOld.manifold) + (0.2F * (CockpitB5N1.this.fm.EI.engines[0].getManifoldPressure() - 1.0F) * 76F);
                float f = CockpitB5N1.this.waypointAzimuth();
                CockpitB5N1.this.setNew.waypointDeviation.setDeg(CockpitB5N1.this.setOld.waypointDeviation.getDeg(0.1F), f - CockpitB5N1.this.setOld.azimuth.getDeg(1.0F));
                if (CockpitB5N1.this.amountOfBombs == 1) {
                    if (!CockpitB5N1.this.bombs[0].haveBullets()) {
                        CockpitB5N1.this.bombLever2 = 1.0F;
                    }
                } else if (CockpitB5N1.this.amountOfBombs == 3) {
                    if (!CockpitB5N1.this.bombs[1].haveBullets()) {
                        CockpitB5N1.this.bombLever1 = 1.0F;
                    }
                    if (!CockpitB5N1.this.bombs[2].haveBullets()) {
                        CockpitB5N1.this.bombLever2 = 1.0F;
                    }
                    if (!CockpitB5N1.this.bombs[3].haveBullets()) {
                        CockpitB5N1.this.bombLever3 = 1.0F;
                    }
                } else if (CockpitB5N1.this.amountOfBombs == 6) {
                    if (!CockpitB5N1.this.bombs[4].haveBullets()) {
                        CockpitB5N1.this.bombLever1 = 0.5F;
                    }
                    if (!CockpitB5N1.this.bombs[5].haveBullets()) {
                        CockpitB5N1.this.bombLever1 = 1.0F;
                    }
                    if (!CockpitB5N1.this.bombs[6].haveBullets()) {
                        CockpitB5N1.this.bombLever2 = 0.5F;
                    }
                    if (!CockpitB5N1.this.bombs[7].haveBullets()) {
                        CockpitB5N1.this.bombLever2 = 1.0F;
                    }
                    if (!CockpitB5N1.this.bombs[8].haveBullets()) {
                        CockpitB5N1.this.bombLever3 = 0.5F;
                    }
                    if (!CockpitB5N1.this.bombs[9].haveBullets()) {
                        CockpitB5N1.this.bombLever3 = 1.0F;
                    }
                }
                if (CockpitB5N1.this.amountOfBombs != 0) {
                    if (CockpitB5N1.this.bombLever1cur < CockpitB5N1.this.bombLever1) {
                        CockpitB5N1.this.bombLever1cur += 0.1F;
                    }
                    if (CockpitB5N1.this.bombLever2cur < CockpitB5N1.this.bombLever2) {
                        CockpitB5N1.this.bombLever2cur += 0.1F;
                    }
                    if (CockpitB5N1.this.bombLever3cur < CockpitB5N1.this.bombLever3) {
                        CockpitB5N1.this.bombLever3cur += 0.1F;
                    }
                    if ((CockpitB5N1.this.bombLever1cur == 1.0F) && (CockpitB5N1.this.bombLever2cur == 1.0F) && (CockpitB5N1.this.bombLever3cur == 1.0F)) {
                        CockpitB5N1.this.amountOfBombs = 0;
                    }
                }
            }
            return true;
        }

        Interpolater() {
        }
    }

    private class Variables {

        float      flaps;
        float      turn;
        float      hook;
        float      gear;
        float      throttle;
        float      prop;
        float      mix;
        float      altimeter;
        float      vspeed;
        float      manifold;
        AnglesFork azimuth;
        AnglesFork waypointDeviation;

        private Variables() {
            this.manifold = 0.0F;
            this.azimuth = new AnglesFork();
            this.waypointDeviation = new AnglesFork();
        }

    }

    public CockpitB5N1() {
        super("3DO/Cockpit/B5N2/hier.him", "bf109");
        this.bombs = new BombGun[10];
        this.setOld = new Variables();
        this.setNew = new Variables();
        this.w = new Vector3f();
        this.pictAiler = 0.0F;
        this.pictElev = 0.0F;
        this.bombLever1cur = 0.0F;
        this.bombLever2cur = 0.0F;
        this.bombLever3cur = 0.0F;
        this.bombLever1 = 0.0F;
        this.bombLever2 = 0.0F;
        this.bombLever3 = 0.0F;
        this.bNeedSetUp = true;
        this.amountOfBombs = 0;
        this.gearLightsDamaged = false;
        this.flickeringLight = "";
        this.flickerLight = false;
        this.bTorp = false;
        this.ac = null;
        this.cockpitNightMats = (new String[] { "bombergauges", "dgauges1", "dgauges2", "dgauges3", "dgauges4", "dgauges5", "dgauges6", "gauges1", "gauges2", "gauges3", "gauges4", "gauges5", "gauges6", "turnbankneedles", "gyro", "Voltammeter", "DVoltammeter" });
        this.setNightMats(false);
        this.interpPut(new Interpolater(), null, Time.current(), null);
        if (this.acoustics != null) {
            this.acoustics.globFX = new ReverbFXRoom(0.45F);
        }
        this.ac = (B5Nxyz) this.aircraft();
        HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK01");
        Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light1.light.setColor(126F, 232F, 245F);
        this.light1.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK01", this.light1);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK02");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light2.light.setColor(126F, 232F, 245F);
        this.light2.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK02", this.light2);
        hooknamed = new HookNamed(this.mesh, "LAMPHOOK03");
        loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
        this.light3 = new LightPointActor(new LightPoint(), loc.getPoint());
        this.light3.light.setColor(126F, 232F, 245F);
        this.light3.light.setEmit(0.0F, 0.0F);
        this.pos.base().draw.lightMap().put("LAMPHOOK03", this.light3);
        try {
            this.bombs[1] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb03");
            this.amountOfBombs++;
        } catch (Exception exception) {
        }
        try {
            this.bombs[2] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb04");
            this.amountOfBombs++;
        } catch (Exception exception1) {
        }
        try {
            this.bombs[3] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb05");
            this.amountOfBombs++;
        } catch (Exception exception2) {
        }
        try {
            this.bombs[4] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb06");
            this.amountOfBombs++;
        } catch (Exception exception3) {
        }
        try {
            this.bombs[5] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb07");
            this.amountOfBombs++;
        } catch (Exception exception4) {
        }
        try {
            this.bombs[6] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb08");
            this.amountOfBombs++;
        } catch (Exception exception5) {
        }
        try {
            this.bombs[7] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb09");
            this.amountOfBombs++;
        } catch (Exception exception6) {
        }
        try {
            this.bombs[8] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb10");
            this.amountOfBombs++;
        } catch (Exception exception7) {
        }
        try {
            this.bombs[9] = (BombGun) ((Aircraft) this.fm.actor).getBulletEmitterByHookName("_ExternalBomb11");
            this.amountOfBombs++;
        } catch (Exception exception8) {
        }
        if (this.amountOfBombs == 1) {
            this.bombLever1cur = 1.0F;
            this.bombLever3cur = 1.0F;
        }
        if (((NetAircraft) (this.aircraft())).thisWeaponsName.startsWith("1x91")) {
            this.bTorp = true;
        }
    }

    public void reflectWorldToInstruments(float f) {
        if (this.bNeedSetUp) {
            this.reflectPlaneMats();
            this.bNeedSetUp = false;
        }
        this.resetYPRmodifier();
        Cockpit.xyz[1] = this.cvt(this.fm.CT.getCockpitDoor(), 0.01F, 0.99F, 0.0F, 0.655F);
        this.mesh.chunkSetLocate("Z_Canopy", Cockpit.xyz, Cockpit.ypr);
        if (!this.gearLightsDamaged) {
            this.mesh.chunkVisible("XGreenL", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
            this.mesh.chunkVisible("XGreenR", (this.fm.CT.getGear() == 1.0F) && this.fm.Gears.lgear);
            this.mesh.chunkVisible("XRedL", this.fm.CT.getGear() == 0.0F);
            this.mesh.chunkVisible("XRedR", this.fm.CT.getGear() == 0.0F);
            this.mesh.chunkVisible("XGreenC", this.fm.CT.getArrestor() == 1.0F);
        } else {
            this.mesh.chunkVisible(this.flickeringLight, this.flickerLight);
        }
        this.mesh.chunkSetAngles("Z_Stick4", 0.0F, -(this.pictElev = (0.85F * this.pictElev) + (0.15F * this.fm.CT.ElevatorControl)) * 10F, 0.0F);
        float f1 = (this.pictAiler = (0.85F * this.pictAiler) + (0.15F * this.fm.CT.AileronControl)) * 15F;
        this.mesh.chunkSetAngles("Z_Stick1", 0.0F, f1, 0.0F);
        this.mesh.chunkSetAngles("Z_Stick3", 0.0F, -f1, 0.0F);
        this.mesh.chunkSetAngles("Z_Stick2", 0.0F, -f1, 0.0F);
        this.mesh.chunkSetAngles("Z_Rudder", 0.0F, -15F * this.fm.CT.getRudder(), 0.0F);
        this.mesh.chunkSetAngles("Z_ElevatorTrim", 0.0F, 70F * this.fm.CT.getTrimElevatorControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_RudderTrim", 0.0F, 40F * this.fm.CT.getTrimRudderControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_AileronTrim", 0.0F, 40F * this.fm.CT.getTrimAileronControl(), 0.0F);
        this.mesh.chunkSetAngles("Z_ArrestingHook", 0.0F, -80F * this.setNew.hook, 0.0F);
        this.mesh.chunkSetAngles("Z_LandingGear", 0.0F, 70F * this.setNew.gear, 0.0F);
        this.mesh.chunkSetAngles("Z_Flaps", 0.0F, 70F * this.setNew.flaps, 0.0F);
        this.mesh.chunkSetAngles("Z_CowlFlaps", 0.0F, -75F + (this.fm.EI.engines[0].getControlRadiator() * 75F), 0.0F);
        this.mesh.chunkSetAngles("Z_AutoThrottle", 0.0F, this.cvt(this.interp(this.setNew.throttle, this.setOld.throttle, f), 0.0F, 1.2F, 0.0F, 110F), 0.0F);
        this.mesh.chunkSetAngles("Z_AutoProp", 0.0F, this.cvt(this.interp(this.setNew.prop, this.setOld.prop, f), 0.0F, 1.0F, 0.0F, 110F), 0.0F);
        this.mesh.chunkSetAngles("Z_Mixture", 0.0F, this.cvt(this.interp(this.setNew.mix, this.setOld.mix, f), 0.0F, 1.2F, 0.0F, 85F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_airspeed", 0.0F, this.floatindex(this.cvt(0.539957F * Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 300F, 0.0F, 30F), CockpitB5N1.speedometerScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_RPM", 0.0F, this.cvt(this.fm.EI.engines[0].getRPM(), 500F, 3500F, 0.0F, 540F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Variometer", 0.0F, this.cvt(this.setNew.vspeed, -10F, 10F, -180F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Turn1", 0.0F, this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 28F, -28F), 0.0F);
        float f2 = this.getBall(4D);
        this.mesh.chunkSetAngles("Z_Need_ball", 0.0F, this.cvt(f2, -4F, 4F, -7.5F, 7.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_compass", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_gyro", 0.0F, this.setNew.azimuth.getDeg(f), 0.0F);
        this.mesh.chunkSetAngles("Z_horizon1a", 0.0F, -this.fm.Or.getKren(), 0.0F);
        this.mesh.chunkSetAngles("Z_horizon1b", 0.0F, this.cvt(-this.fm.Or.getTangage(), -45F, 45F, -22F, 22F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Cylheadtemp", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 69F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_oiltemp", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tOilIn, 30F, 110F, 0.0F, 8F), CockpitB5N1.oilTempScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Rfuel", 0.0F, this.cvt(this.fm.M.fuel / this.fm.M.maxFuel, 0.0F, 1.0F, 0.0F, 236F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Lfuel", 0.0F, this.cvt(this.fm.M.fuel / this.fm.M.maxFuel, 0.0F, 1.0F, 0.0F, 236F), 0.0F);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = -this.cvt(this.fm.Or.getTangage(), -15F, 15F, 0.043652F, -0.043652F);
        this.mesh.chunkSetLocate("Z_Inclinometer", Cockpit.xyz, Cockpit.ypr);
        this.resetYPRmodifier();
        Cockpit.xyz[1] = -this.cvt(this.fm.CT.getFlap(), 0.0F, 1.0F, 0.0F, -0.1175F);
        this.mesh.chunkSetLocate("Z_Flapindicator", Cockpit.xyz, Cockpit.ypr);
        this.mesh.chunkSetAngles("Z_Need_navigation", this.cvt(this.setNew.waypointDeviation.getDeg(f * 0.2F), -25F, 25F, -45F, 45F), 0.0F, 0.0F);
        this.mesh.chunkSetAngles("Z_BombRelease1", 0.0F, 50F * this.bombLever1cur, 0.0F);
        this.mesh.chunkSetAngles("Z_BombRelease2", 0.0F, 50F * this.bombLever2cur, 0.0F);
        this.mesh.chunkSetAngles("Z_BombRelease3", 0.0F, 50F * this.bombLever3cur, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_manifold", 0.0F, this.cvt(this.setNew.manifold, -50F, 30F, -202.5F, 112.5F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Alt1a", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30000F, 0.0F, 21600F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Alt1b", 0.0F, this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 30000F, 0.0F, 2160F), 0.0F);
        float f3 = this.fm.turret[0].tu[0];
        float f4 = this.fm.turret[0].tu[1];
        this.mesh.chunkSetAngles("Z_Gun1", 0.0F, f3, 0.0F);
        this.mesh.chunkSetAngles("Z_T92Mg", 0.0F, f4, 0.0F);
        this.mesh.chunkSetAngles("Z_MagnetoSwitch", 0.0F, this.cvt(this.fm.EI.engines[0].getControlMagnetos(), 0.0F, 3F, -46F, 46F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_Cylheadtemp", 0.0F, this.floatindex(this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 350F, 0.0F, 3F), CockpitB5N1.waterTempScale), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_exhausttemp", 0.0F, this.cvt(this.fm.EI.engines[0].tWaterOut, 50F, 350F, 0.0F, 68F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_oilpressb", 0.0F, 100F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_oilpressc", 0.0F, 100F, 0.0F);
        this.mesh.chunkSetAngles("Z_Need_pressfuel", 0.0F, this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 2.0F, 0.0F, 180F), 0.0F);
        this.mesh.chunkSetAngles("Z_Need_pressoil", 0.0F, this.cvt(1.0F + (0.05F * this.fm.EI.engines[0].tOilIn), 0.0F, 8.5F, 0.0F, -182F), 0.0F);
        if (this.bTorp) {
            this.mesh.chunkSetAngles("Z_TorpedoSight1", 0.0F, ((B5N1) this.aircraft()).fSightCurSideslip, 0.0F);
            this.mesh.chunkSetAngles("Z_TorpedoSight2", 0.0F, ((B5N1) this.aircraft()).fSightCurSideslip, 0.0F);
            this.mesh.chunkSetAngles("Z_TorpedoSight3", 0.0F, -this.cvt(((B5N1) this.aircraft()).fSightCurSideslip, -40F, 40F, -48F, 48F), 0.0F);
            this.mesh.chunkSetAngles("Z_TorpedoSight4", 0.0F, this.cvt(((B5N1) this.aircraft()).fSightCurSideslip, -40F, 40F, -48F, 48F), 0.0F);
        }
        this.mesh.chunkSetAngles("Z_Need_vacuum", 0.0F, 180F, 0.0F);
    }

    public void reflectCockpitState() {
        if ((this.fm.AS.astateCockpitState & 1) != 0) {
            this.mesh.chunkVisible("holesMiddle", true);
            this.mesh.chunkVisible("DGauge4", true);
            this.mesh.chunkVisible("Gauge4", false);
            this.mesh.chunkVisible("Z_Need_airspeed", false);
            this.mesh.chunkVisible("Z_Need_pressoil", false);
            this.mesh.chunkVisible("Z_Need_pressfuel", false);
            this.mesh.chunkVisible("Z_Need_oiltemp", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
            this.mesh.chunkVisible("DGauge1", true);
            this.mesh.chunkVisible("Gauge1", false);
            this.mesh.chunkVisible("Z_Need_Alt1b", false);
            this.mesh.chunkVisible("Z_Need_Alt1a", false);
            this.mesh.chunkVisible("Z_Need_RPM", false);
            this.mesh.chunkVisible("Z_Need_manifold", false);
            this.mesh.chunkVisible("Z_Need_Cylheadtemp", false);
            this.mesh.chunkVisible("Z_Need_carburator", false);
            this.mesh.chunkVisible("DGauge6", true);
            this.mesh.chunkVisible("Gauge6", false);
            this.mesh.chunkVisible("Z_Need_vacuum", false);
            this.mesh.chunkVisible("Z_Inclinometer", false);
            this.gearLightsDamaged = true;
            if (this.fm.CT.getGear() == 1.0F) {
                this.flickeringLight = "XGreenL";
            } else if (this.fm.getAltitude() > 2000F) {
                this.flickeringLight = "XRedL";
            } else if (this.fm.getAltitude() > 500F) {
                this.flickeringLight = "XGreenC";
            } else {
                this.flickeringLight = "XGreenR";
            }
            this.mesh.chunkVisible("XGreenL", false);
            this.mesh.chunkVisible("XGreenR", false);
            this.mesh.chunkVisible("XRedL", false);
            this.mesh.chunkVisible("XRedR", false);
            this.mesh.chunkVisible("XGreenC", false);
            this.mesh.chunkVisible("holesPit", true);
        }
        if ((this.fm.AS.astateCockpitState & 4) != 0) {
            this.mesh.chunkVisible("holesGunner", true);
        }
        if ((this.fm.AS.astateCockpitState & 8) != 0) {
            this.mesh.chunkVisible("holesCanopyNav", true);
            this.mesh.chunkVisible("DGauge3", true);
            this.mesh.chunkVisible("Gauge3", false);
            this.mesh.chunkVisible("Z_Need_Lfuel", false);
        }
        if ((this.fm.AS.astateCockpitState & 0x80) != 0) {
            this.mesh.chunkVisible("ZOil", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x10) != 0) {
            this.mesh.chunkVisible("holesCanopyNav", true);
        }
        if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
            this.mesh.chunkVisible("holesCanopy", true);
            this.mesh.chunkVisible("DGauge5", true);
            this.mesh.chunkVisible("Gauge5", false);
            this.mesh.chunkVisible("Z_Need_gyro", false);
            this.mesh.chunkVisible("Z_Need_exhausttemp", false);
            this.mesh.chunkVisible("Z_Need_oilpressc", false);
            this.mesh.chunkVisible("holesPit", true);
        }
        if ((this.fm.AS.astateCockpitState & 2) != 0) {
            this.mesh.chunkVisible("holesFront", true);
            this.mesh.chunkVisible("DGauge2", true);
            this.mesh.chunkVisible("Gauge2", false);
            this.mesh.chunkVisible("Z_Need_Turn1", false);
            this.mesh.chunkVisible("Z_Need_ball", false);
            this.mesh.chunkVisible("Z_Need_Variometer", false);
            this.mesh.chunkVisible("Z_Need_navigation", false);
        }
    }

    protected void reflectPlaneToModel() {
        this.aircraft().hierMesh().chunkVisible("Pilot2_D0", false);
        this.aircraft().hierMesh().chunkVisible("Pilot3_D0", false);
        this.aircraft().hierMesh().chunkVisible("Pilot2_D1", false);
        this.aircraft().hierMesh().chunkVisible("Pilot3_D1", false);
        this.aircraft().hierMesh().chunkVisible("HMask2_D0", false);
        this.aircraft().hierMesh().chunkVisible("HMask3_D0", false);
        this.mesh.chunkVisible("pilotNavigator_d0", this.aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
        this.mesh.chunkVisible("pilotNavigator_d1", !this.aircraft().hierMesh().isChunkVisible("Pilot2Col_D0"));
        this.mesh.chunkVisible("pilotgunner_d0", this.aircraft().hierMesh().isChunkVisible("Pilot3Col_D0"));
        this.mesh.chunkVisible("pilotgunner_d1", !this.aircraft().hierMesh().isChunkVisible("Pilot3Col_D0"));
    }

    protected void reflectPlaneMats() {
        HierMesh hiermesh = this.aircraft().hierMesh();
        Mat mat = hiermesh.material(hiermesh.materialFind("Pilot2"));
        this.mesh.materialReplace("Pilot2", mat);
    }

    public void toggleLight() {
        this.cockpitLightControl = !this.cockpitLightControl;
        if (this.cockpitLightControl) {
            this.light1.light.setEmit(0.003F, 0.3F);
            this.light2.light.setEmit(0.003F, 0.3F);
            this.light3.light.setEmit(0.003F, 0.3F);
            this.setNightMats(true);
        } else {
            this.light1.light.setEmit(0.0F, 0.0F);
            this.light2.light.setEmit(0.0F, 0.0F);
            this.light3.light.setEmit(0.0F, 0.0F);
            this.setNightMats(false);
        }
    }

    private Variables          setOld;
    private Variables          setNew;
    private Variables          setTmp;
    public Vector3f            w;
    private float              pictAiler;
    private float              pictElev;
    private float              bombLever1cur;
    private float              bombLever2cur;
    private float              bombLever3cur;
    private float              bombLever1;
    private float              bombLever2;
    private float              bombLever3;
    private boolean            bNeedSetUp;
    private LightPointActor    light1;
    private LightPointActor    light2;
    private LightPointActor    light3;
    private BombGun            bombs[];
    private int                amountOfBombs;
    private boolean            gearLightsDamaged;
    private String             flickeringLight;
    private boolean            flickerLight;
    private static final float speedometerScale[] = { 0.0F, 7F, 14F, 21F, 28F, 43.5F, 62F, 81F, 104.5F, 130F, 157F, 184.5F, 214F, 244.5F, 275.5F, 305F, 333F, 363F, 388F, 420F, 445F, 472F, 497F, 522F, 549F, 573F, 595F, 616F, 635F, 656F, 675F };
    private static final float oilTempScale[]     = { 0.0F, 19F, 43F, 68F, 98F, 134F, 175F, 221F, 264F };
    private static final float waterTempScale[]   = { 0.0F, 20F, 38F, 69F };
    private boolean            bTorp;
    private B5Nxyz             ac;

    static {
        Property.set(CockpitB5N1.class, "normZNs", new float[] { 1.17F, 1.0F, 1.0F, 1.0F });
    }

}
