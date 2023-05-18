package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Polares;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.fm.Squares;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.trains.Wagon;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGun9M114;
import com.maddox.rts.MsgDestroy;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.sound.SoundFX;

public abstract class Mi24X extends Scheme2 implements TypeHelicopter, TypeStormovikArmored, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeLaserSpotter, TypeFighterAceMaker {

    public Mi24X() {
        this.counter = 0;
        this.vector3dop = new Vector3d();
        this.point3dop = new Point3d();
        this.localPoint3d1 = new Point3d();
        this.LaserHook = new Hook[4];
        this.bAPazimut = false;
        this.bAPkrentang = true;
        this.bAPalt = false;
        this.apALltPitch = 0.0F;
        this.apAzimut = 0.0F;
        this.apKren = 0.0F;
        this.apTang = 0.0F;
        this.forceTrim_x = 0.0D;
        this.forceTrim_y = 0.0D;
        this.forceTrim_z = 0.0D;
        this.getTrim = false;
        this.curAngleRotor = 0.0F;
        this.lastTimeFan = Time.current();
        this.rocketHookSelected = 2;
        this.suka = new Loc();
        this.obsLookTime = 0;
        this.obsLookAzimuth = 0.0F;
        this.obsLookElevation = 0.0F;
        this.obsAzimuth = 0.0F;
        this.obsElevation = 0.0F;
        this.obsAzimuthOld = 0.0F;
        this.obsElevationOld = 0.0F;
        this.obsMove = 0.0F;
        this.obsMoveTot = 0.0F;
        this.bObserverKilled = false;
        this.hasChaff = false;
        this.hasFlare = false;
        this.lastChaffDeployed = 0L;
        this.lastFlareDeployed = 0L;
        this.lastCommonThreatActive = 0L;
        this.intervalCommonThreat = 1000L;
        this.lastRadarLockThreatActive = 0L;
        this.intervalRadarLockThreat = 1000L;
        this.lastMissileLaunchThreatActive = 0L;
        this.intervalMissileLaunchThreat = 500L;
        this.guidedMissileUtils = new GuidedMissileUtils(this);
        this.curAngleRotor = 0.0F;
        this.lastTimeFan = Time.current();
        this.TailRotorDestroyed = false;
        this.laserOn = false;
        this.landing = false;
        this.counter = 0;
        this.repMod = 0;
        this.missileLaunchInterval = 0L;
        this.victim = null;
        this.missilesList = new ArrayList();
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200F;
        this.aPitch = 0.0F;
        this.aOldPitch = 0.0F;
        this.rotorRPM = 0.0D;
        this.tailRotorRPM = 0.0D;
        this.reductorRPM = 0.0D;
        this.engineRPM = 0.0D;
        this.sndTrim = this.newSound("cockpit.trimmer", false);
        this.sndProp = this.newSound("propeller.mi24", true);
        this.AltCheck = false;
        this.asTimer = Time.current() + 500L;
        this.aso2mode = 0;
        this.threatIsNear = false;
        this.hookDust = new Hook[4];
        this.hookDust[0] = this.findHook("_Dust00");
        this.hookDust[1] = this.findHook("_Dust01");
        this.hookDust[2] = this.findHook("_Dust02");
        this.hookDust[3] = this.findHook("_Dust03");
        this.dustLoc = new Loc();
        this.dustEff = new Eff3DActor[4];
        this.tV = new Vector3d[2];
        this.tV[0] = new Vector3d(0.0D, 0.0D, 0.0D);
        this.tV[1] = new Vector3d(0.0D, 0.0D, 0.0D);
        this.tP = new Point3d[5];
        this.tP[0] = new Point3d(0.0D, 0.0D, 0.0D);
        this.tP[1] = new Point3d(0.0D, 0.0D, 0.0D);
        this.tP[2] = new Point3d(0.0D, 0.0D, 0.0D);
        this.tP[3] = new Point3d(0.0D, 0.0D, 0.0D);
        this.tP[4] = new Point3d(0.0D, 0.0D, 0.0D);
        this.tOr = new Orientation(0.0F, 0.0F, 0.0F);
    }

    public void checkAirstart() {
        if (!this.AltCheck && ((this.FM.getAltitude() - Landscape.HQ_Air((float) this.FM.Loc.x, (float) this.FM.Loc.y)) > 20F)) {
            this.FM.EI.engines[0].doSetStage(6);
            this.FM.EI.engines[1].doSetStage(6);
            this.FM.EI.engines[0].setw(1570.797F);
            this.FM.EI.engines[1].setw(1570.797F);
            this.aPitch = 0.75F;
            this.engineRPM = 15000D;
            this.reductorRPM = 228D;
            this.rotorRPM = 228D;
            this.FM.CT.setPowerControl(1.0F);
            this.AltCheck = true;
        }
    }

    public void rotorSound() {
        try {
            this.sndProp.setParent(this.getRootFX());
            this.sndProp.setPosition(this.FM.EI.engines[0].getEnginePos());
            this.sndProp.setControl(100, (float) this.rotorRPM);
            this.sndProp.setControl(108, this.aPitch);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void trimSound() {
        try {
            this.sndTrim.cancel();
            this.sndTrim.setParent(this.getRootFX());
            this.sndTrim.play(this.pos.getAbsPoint());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dustEmit() {
        for (int j = 0; j < this.FM.EI.getNum(); j++) {
            if (this.FM.Gears.clpEngineEff[j][0] != null) {
                Eff3DActor.finish(this.FM.Gears.clpEngineEff[j][0]);
                this.FM.Gears.clpEngineEff[j][0] = null;
            }
            if (this.FM.Gears.clpEngineEff[j][1] != null) {
                Eff3DActor.finish(this.FM.Gears.clpEngineEff[j][1]);
                this.FM.Gears.clpEngineEff[j][1] = null;
            }
        }

        float alt = this.FM.getAltitude() - Landscape.HQ_Air((float) this.FM.Loc.x, (float) this.FM.Loc.y);
        float f = (float) (Aircraft.cvt(alt, 0.0F, 20F, TrueRandom.nextFloat(0.4F, 0.9F), 0.01F) * (this.rotorRPM / 240D));
        if ((alt < 20F) && (this.rotorRPM > 80D)) {
            this.dustLoc.set(0.0D, alt + 3F, 0.0D, 0.0F, 0.0F, 0.0F);
            this.hookDust[0].computePos(this, this.pos.getCurrent(), this.dustLoc);
            this.dustLoc.getPoint().z = Landscape.HQ_Air((float) this.dustLoc.getPoint().x, (float) this.dustLoc.getPoint().y);
            if (Engine.cur.land.isWater(this.dustLoc.getPoint().x, this.dustLoc.getPoint().y)) {
                this.dustEff[0] = Eff3DActor.New(this.dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust2.eff", TrueRandom.nextFloat(0.2F));
            } else {
                this.dustEff[0] = Eff3DActor.New(this.dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage == 1 ? "2" : "1") + ".eff", TrueRandom.nextFloat(0.2F));
            }
            this.hookDust[1].computePos(this, this.pos.getCurrent(), this.dustLoc);
            this.dustLoc.getPoint().z = Landscape.HQ_Air((float) this.dustLoc.getPoint().x, (float) this.dustLoc.getPoint().y);
            if (Engine.cur.land.isWater(this.dustLoc.getPoint().x, this.dustLoc.getPoint().y)) {
                this.dustEff[1] = Eff3DActor.New(this.dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust2.eff", TrueRandom.nextFloat(0.2F));
            } else {
                this.dustEff[1] = Eff3DActor.New(this.dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage == 1 ? "2" : "1") + ".eff", TrueRandom.nextFloat(0.2F));
            }
            this.hookDust[2].computePos(this, this.pos.getCurrent(), this.dustLoc);
            this.dustLoc.getPoint().z = Landscape.HQ_Air((float) this.dustLoc.getPoint().x, (float) this.dustLoc.getPoint().y);
            if (Engine.cur.land.isWater(this.dustLoc.getPoint().x, this.dustLoc.getPoint().y)) {
                this.dustEff[2] = Eff3DActor.New(this.dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust2.eff", TrueRandom.nextFloat(0.2F));
            } else {
                this.dustEff[2] = Eff3DActor.New(this.dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage == 1 ? "2" : "1") + ".eff", TrueRandom.nextFloat(0.2F));
            }
            this.hookDust[3].computePos(this, this.pos.getCurrent(), this.dustLoc);
            this.dustLoc.getPoint().z = Landscape.HQ_Air((float) this.dustLoc.getPoint().x, (float) this.dustLoc.getPoint().y);
            if (Engine.cur.land.isWater(this.dustLoc.getPoint().x, this.dustLoc.getPoint().y)) {
                this.dustEff[3] = Eff3DActor.New(this.dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust2.eff", TrueRandom.nextFloat(0.2F));
            } else {
                this.dustEff[3] = Eff3DActor.New(this.dustLoc, f, "3DO/Effects/Aircraft/GrayGroundDust" + (World.cur().camouflage == 1 ? "2" : "1") + ".eff", TrueRandom.nextFloat(0.2F));
            }
        } else {
            Eff3DActor.finish(this.dustEff[0]);
            Eff3DActor.finish(this.dustEff[1]);
            Eff3DActor.finish(this.dustEff[2]);
            Eff3DActor.finish(this.dustEff[3]);
        }
    }

    public void laserUpdate() {
        if (this.laserOn) {
            this.pos.getRender(Actor._tmpLoc);
            this.LaserHook[1] = new HookNamed(this, "_Laser1");
            Mi24X.LaserLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            this.LaserHook[1].computePos(this, Actor._tmpLoc, Mi24X.LaserLoc1);
            Mi24X.LaserLoc1.get(Mi24X.LaserP1);
            Mi24X.LaserLoc1.set(6000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            this.LaserHook[1].computePos(this, Actor._tmpLoc, Mi24X.LaserLoc1);
            Mi24X.LaserLoc1.get(Mi24X.LaserP2);
            Engine.land();
            if (Landscape.rayHitHQ(Mi24X.LaserP1, Mi24X.LaserP2, Mi24X.LaserPL)) {
                Mi24X.LaserPL.z -= 0.95D;
                Mi24X.LaserP2.interpolate(Mi24X.LaserP1, Mi24X.LaserPL, 1.0F);
                TypeLaserSpotter.spot.set(Mi24X.LaserP2);
            }
        }
    }

    public long getChaffDeployed() {
        if (this.hasChaff) {
            return this.lastChaffDeployed;
        } else {
            return 0L;
        }
    }

    public long getFlareDeployed() {
        if (this.hasFlare) {
            return this.lastFlareDeployed;
        } else {
            return 0L;
        }
    }

    public void setCommonThreatActive() {
        this.intervalCommonThreat = this.aso2mode * 4000;
        long curTime = Time.current();
        if ((curTime - this.lastCommonThreatActive) > this.intervalCommonThreat) {
            this.lastCommonThreatActive = curTime;
            this.doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive() {
        long curTime = Time.current();
        if ((curTime - this.lastRadarLockThreatActive) > this.intervalRadarLockThreat) {
            this.lastRadarLockThreatActive = curTime;
            this.doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive() {
        if (this.bShotFlare) {
            long curTime = Time.current();
            if ((curTime - this.lastMissileLaunchThreatActive) > this.intervalMissileLaunchThreat) {
                this.lastMissileLaunchThreatActive = curTime;
                this.doDealMissileLaunchThreat();
                this.lastFlareDeployed++;
                if (this.lastFlareDeployed > 2L) {
                    this.lastFlareDeployed = 0L;
                    this.bShotFlare = false;
                }
            }
        }
    }

    private void doDealCommonThreat() {
        this.hasFlare = this.FM.CT.Weapons[7][0].haveBullets() && this.FM.CT.Weapons[7][1].haveBullets();
        if (this.hasFlare) {
            this.FM.CT.Weapons[7][0].shots(1);
            this.FM.CT.Weapons[7][1].shots(1);
        }
    }

    private void doDealRadarLockThreat() {
    }

    private void doDealMissileLaunchThreat() {
        this.hasFlare = this.FM.CT.Weapons[7][0].haveBullets() && this.FM.CT.Weapons[7][1].haveBullets();
        if (this.hasFlare) {
            this.FM.CT.Weapons[7][0].shots(1);
            this.FM.CT.Weapons[7][1].shots(1);
        }
    }

    private void OperatorLookout() {
        double d = Main3D.cur3D().land2D.worldOfsX() + this.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + this.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + this.pos.getAbsPoint().z;
        int i = (int) (-(this.pos.getAbsOrient().getYaw() - 90D));
        if (i < 0) {
            i += 360;
        }
        float f1 = World.getTimeofDay();
        boolean flag = false;
        if (((f1 >= 0.0F) && (f1 <= 5F)) || ((f1 >= 21F) && (f1 <= 24F))) {
            flag = true;
        }
        int i1 = TrueRandom.nextInt(100);
        int i2 = 5 - this.FM.Skill;
        int i3 = i2 + TrueRandom.nextInt(i2);
        this.counter++;
        if (this.counter >= (7 + i3)) {
            this.counter = 0;
        }
        if (!this.FM.Gears.onGround()) {
            List list = Engine.missiles();
            int j1 = list.size();
            for (int k1 = 0; k1 < j1; k1++) {
                Actor missile = (Actor) list.get(k1);
                if ((missile.getSpeed(this.vector3dop) > 500D) && (missile.getArmy() != this.getArmy())) {
                    double d3 = Main3D.cur3D().land2D.worldOfsX() + missile.pos.getAbsPoint().x;
                    double d4 = Main3D.cur3D().land2D.worldOfsY() + missile.pos.getAbsPoint().y;
                    double d5 = Main3D.cur3D().land2D.worldOfsY() + missile.pos.getAbsPoint().z;
                    double d8 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                    String s = "";
                    if ((d2 - d5 - 500D) >= 0.0D) {
                        s = " LOW";
                    }
                    if (((d2 - d5) + 500D) < 0.0D) {
                        s = " HIGH";
                    }
                    double d9 = d3 - d;
                    double d10 = d4 - d1;
                    float f = 57.32484F * (float) Math.atan2(d10, -d9);
                    int j = (int) (Math.floor((int) f) - 90D);
                    if (j < 0) {
                        j += 360;
                    }
                    int k = j - i;
                    if (k < 0) {
                        k += 360;
                    }
                    int l = (int) (Math.ceil((k + 15) / 30D) - 1.0D);
                    if (l < 1) {
                        l = 12;
                    }
                    double d11 = d - d3;
                    double d12 = d1 - d4;
                    double d13 = Math.ceil(Math.sqrt((d12 * d12) + (d11 * d11)) / 10D) * 10D;
                    if (!flag && (d13 <= 3000D) && (d13 >= 20D) && (Math.sqrt(d8 * d8) <= 2000D)) {
                        if (!this.isAI) {
                            HUD.logCenter("MISSILE AT " + l + " O'CLOCK" + s + "!");
                            Voice.speakDanger(this, 4);
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: ASO-2 Engaged!");
                        }
                        if (this.isAI) {
                            ((Maneuver) this.FM).set_maneuver(100);
                        }
                        this.bShotFlare = true;
                    }
                    if (flag && (d13 <= 1000D) && (d13 >= 20D) && (Math.sqrt(d8 * d8) <= 500D)) {
                        if (!this.isAI) {
                            HUD.logCenter("MISSILE AT " + l + " O'CLOCK" + s + "!");
                            Voice.speakDanger(this, 4);
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: ASO-2 Engaged!");
                        }
                        if (this.isAI) {
                            ((Maneuver) this.FM).set_maneuver(100);
                        }
                        this.bShotFlare = true;
                    }
                }
            }

        }
        List targets = Engine.targets();
        int v = targets.size();
        for (int b = 0; b < v; b++) {
            Actor threat = (Actor) targets.get(b);
            double d3 = Main3D.cur3D().land2D.worldOfsX() + threat.pos.getAbsPoint().x;
            double d4 = Main3D.cur3D().land2D.worldOfsY() + threat.pos.getAbsPoint().y;
            double d5 = Main3D.cur3D().land2D.worldOfsY() + threat.pos.getAbsPoint().z;
            if ((this.repMod == 7) && (this.counter == 0) && (threat instanceof Aircraft) && (threat.getArmy() != this.getArmy()) && !this.FM.Gears.onGround()) {
                double d8 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                String s = "";
                if ((d2 - d5 - 500D) >= 0.0D) {
                    s = " low";
                }
                if (((d2 - d5) + 500D) < 0.0D) {
                    s = " high";
                }
                double d9 = d3 - d;
                double d10 = d4 - d1;
                float f = 57.32484F * (float) Math.atan2(d10, -d9);
                int j = (int) (Math.floor((int) f) - 90D);
                if (j < 0) {
                    j += 360;
                }
                int k = j - i;
                if (k < 0) {
                    k += 360;
                }
                int l = (int) (Math.ceil((k + 15) / 30D) - 1.0D);
                if (l < 1) {
                    l = 12;
                }
                double d11 = d - d3;
                double d12 = d1 - d4;
                double d13 = Math.ceil(Math.sqrt((d12 * d12) + (d11 * d11)) / 10D) * 10D;
                String s1 = "Aircraft ";
                if (threat instanceof TypeFighter) {
                    s1 = "Fighters ";
                }
                if (threat instanceof TypeBomber) {
                    s1 = "Bombers ";
                }
                if (threat instanceof TypeHelicopter) {
                    s1 = "Helicopters ";
                }
                if (!flag && (i1 <= (50 + i2)) && (d13 <= 4000D) && (d13 >= 500D) && (Math.sqrt(d8 * d8) <= 2000D) && !this.isAI) {
                    HUD.training(s1 + "at " + l + " o'clock" + s + "!");
                }
                if (flag && (i1 <= (5 + i2)) && (d13 <= 1000D) && (d13 >= 100D) && (Math.sqrt(d8 * d8) <= 500D) && !this.isAI) {
                    HUD.training(s1 + "at " + l + " o'clock" + s + "!");
                }
            }
            if (((!this.isAI && (this.repMod >= 1) && (this.repMod <= 6) && (this.counter == 0)) || this.isAI) && Actor.isAlive(threat) && ((threat instanceof TankGeneric) || (threat instanceof Wagon) || (threat instanceof ArtilleryGeneric) || (threat instanceof BigshipGeneric) || (threat instanceof ShipGeneric) || (threat instanceof CarGeneric)) && (threat.getArmy() != this.getArmy())) {
                double d8 = (d2 - d5) * 2D;
                if (d8 > 6000D) {
                    d8 = 6000D;
                }
                if (flag) {
                    d8 = 1500D - (d2 - d5);
                }
                if (!flag && (d8 < (2000 + (this.FM.Skill * 100)))) {
                    d8 = (this.FM.Skill * 100) + 2000;
                }
                String s = "units";
                if (this.repMod == 1) {
                    if (threat instanceof TankGeneric) {
                        s = "armor";
                    }
                    if (threat instanceof ArtilleryGeneric) {
                        s = "guns";
                    }
                    if (threat instanceof CarGeneric) {
                        s = "vehicles";
                    }
                    if (threat instanceof Wagon) {
                        s = "train";
                    }
                    if ((threat instanceof BigshipGeneric) || (threat instanceof ShipGeneric)) {
                        s = "ship";
                        d8 *= 2D;
                    }
                } else if (this.repMod == 2) {
                    if (threat instanceof TankGeneric) {
                        s = "armor";
                    }
                } else if (this.repMod == 3) {
                    if (threat instanceof ArtilleryGeneric) {
                        s = "guns";
                    }
                } else if (this.repMod == 4) {
                    if (threat instanceof CarGeneric) {
                        s = "vehicles";
                    }
                } else if (this.repMod == 5) {
                    if (threat instanceof Wagon) {
                        s = "train";
                    }
                } else if ((this.repMod == 6) && ((threat instanceof BigshipGeneric) || (threat instanceof ShipGeneric))) {
                    s = "ship";
                    d8 *= 2D;
                }
                double d9 = d3 - d;
                double d10 = d4 - d1;
                float f11 = 57.32484F * (float) Math.atan2(d10, -d9);
                double d11 = Math.floor((int) f11) - 90D;
                if (d11 < 0.0D) {
                    d11 += 360D;
                }
                int k = (int) (d11 - i);
                if (k < 0) {
                    k += 360;
                }
                int l = (int) (Math.ceil((k + 15) / 30D) - 1.0D);
                if (l < 1) {
                    l = 12;
                }
                double d12 = d - d3;
                double d13 = d1 - d4;
                double d14 = Math.ceil(Math.sqrt((d13 * d13) + (d12 * d12)));
                float angle = GuidedMissileUtils.angleActorBetween(this, threat);
                float distance = (float) GuidedMissileUtils.distanceBetween(this, threat);
                int i4 = (int) (Math.ceil(distance / 100D) * 100D);
                if (d14 <= d8) {
                    if (!this.isAI) {
                        HUD.training("Enemy " + s + " at " + l + " o'clock for " + i4 + " meters");
                        if (!this.bManualFire && (l == 12) && (angle < (10F + (this.FM.Skill * 10F))) && (distance > (1400F - (this.FM.Skill * 100))) && (distance < (2000F + (this.FM.Skill * 1000))) && (Math.abs(this.FM.getOverload()) < (1 + this.FM.Skill))) {
                            HUD.training("Tracking " + s + " range " + i4 + " meters");
                            this.victim = threat;
                            Reflection.setInt(this.guidedMissileUtils, "engageMode", 1);
                            this.doOperatorLaunchMissile();
                        }
                    } else if ((l == 12) && (angle < (10F + (this.FM.Skill * 10F))) && (distance > (1400F - (this.FM.Skill * 100))) && (distance < (2000F + (this.FM.Skill * 1000)))) {
                        this.victim = threat;
                        this.doLaunchMissileAI();
                        this.threatIsNear = true;
                    }
                }
            }
        }

    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    protected void moveElevator(float f) {
    }

    protected void moveAileron(float f) {
    }

    protected void moveFlap(float f1) {
    }

    protected void moveRudder(float f1) {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f_1_ = Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -90F);
        float f_2_ = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -85F);
        float f_3_ = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, 85F);
        float f_4_ = f > 0.5F ? Aircraft.cvt(f, 0.8F, 1.0F, 70F, 0.0F) : Aircraft.cvt(f, 0.1F, 0.5F, 0.0F, 70F);
        float f_5_ = f > 0.5F ? Aircraft.cvt(f, 0.8F, 1.0F, -70F, 0.0F) : Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -70F);
        Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -90F);
        float f_7_ = Aircraft.cvt(f, 0.1F, 0.8F, -7F, 90F);
        float f_8_ = Aircraft.cvt(f, 0.2F, 0.9F, 7F, -90F);
        float f_9_ = Aircraft.cvt(f, 0.1F, 0.9F, 0.0F, -140F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, f_1_);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 0.0F, f_9_);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, f_2_, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, f_3_, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f_4_, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f_5_, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", f_8_, 7F, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", f_7_, -7F, 0.0F);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("GearL22_D0", 0.0F, this.FM.Gears.gWheelSinking[0] * -90F, 0.0F);
        this.hierMesh().chunkSetAngles("GearR22_D0", 0.0F, this.FM.Gears.gWheelSinking[1] * 90F, 0.0F);
    }

    protected void moveGear(float f) {
        Mi24X.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        float f_1_ = Aircraft.cvt(1.0F, 0.1F, 0.9F, 0.0F, -90F);
        if (this.FM.CT.GearControl > 0.1F) {
            if (f < -45F) {
                this.hierMesh().chunkSetAngles("GearC2_D0", -45F, 0.0F, f_1_);
            } else if (f > 45F) {
                this.hierMesh().chunkSetAngles("GearC2_D0", 45F, 0.0F, f_1_);
            } else {
                this.hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, f_1_);
            }
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, 0.0F, f_1_);
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.95F, 0.0F, 0.9F);
        this.hierMesh().chunkSetAngles("Door1_D0", -90F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Door2_D0", 0.0F, -65F * f, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -120F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 120F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 120F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -120F * f, 0.0F);
    }

    protected void moveFan(float f) {
        if (this.isAI) {
            if ((this.FM.EI.engines[0].getStage() > 5) && (this.FM.EI.engines[1].getStage() > 5)) {
                this.hierMesh().chunkVisible("Prop1_D0", false);
                this.hierMesh().chunkVisible("Prop2_D0", false);
                this.hierMesh().chunkVisible("PropRot1_D0", true);
                this.hierMesh().chunkVisible("PropRot2_D0", true);
            } else {
                this.hierMesh().chunkVisible("Prop1_D0", true);
                this.hierMesh().chunkVisible("Prop2_D0", true);
                this.hierMesh().chunkVisible("PropRot1_D0", false);
                this.hierMesh().chunkVisible("PropRot2_D0", false);
            }
        } else if (this.rotorRPM > 100D) {
            this.hierMesh().chunkVisible("Prop1_D0", false);
            this.hierMesh().chunkVisible("Prop2_D0", false);
            this.hierMesh().chunkVisible("PropRot1_D0", true);
            this.hierMesh().chunkVisible("PropRot2_D0", true);
        } else {
            this.hierMesh().chunkVisible("Prop1_D0", true);
            this.hierMesh().chunkVisible("Prop2_D0", true);
            this.hierMesh().chunkVisible("PropRot1_D0", false);
            this.hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if (this.hierMesh().isChunkVisible("Prop1_D1")) {
            this.hierMesh().chunkVisible("Prop1_D0", false);
            this.hierMesh().chunkVisible("PropRot1_D0", false);
        }
        if (this.hierMesh().isChunkVisible("Prop2_D1")) {
            this.hierMesh().chunkVisible("Prop2_D0", false);
            this.hierMesh().chunkVisible("PropRot2_D0", false);
        }
        if (this.hierMesh().isChunkVisible("Tail1_CAP")) {
            this.hierMesh().chunkVisible("Prop2_D0", false);
            this.hierMesh().chunkVisible("PropRot2_D0", false);
            this.hierMesh().chunkVisible("Prop2_D1", false);
        }
        long curTime = Time.current();
        this.diffAngleRotor = (float) ((6D * this.rotorRPM * (curTime - this.lastTimeFan)) / 1000D);
        this.curAngleRotor += this.diffAngleRotor;
        this.diffAngleTailRotor = (float) ((6D * this.tailRotorRPM * (curTime - this.lastTimeFan)) / 1000D);
        this.curAngleTailRotor += this.diffAngleTailRotor;
        this.lastTimeFan = curTime;
        this.hierMesh().chunkSetAngles("Prop1_D0", -this.curAngleRotor % 360F, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Prop2_D0", 0.0F, 0.0F, -this.curAngleTailRotor % 360F);
        if (this.TailRotorDestroyed) {
            this.hierMesh().chunkVisible("Prop2_D0", false);
            this.hierMesh().chunkVisible("PropRot2_D0", false);
            this.hierMesh().chunkVisible("Prop2_D1", true);
        }
    }

    private void tiltRotor(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -this.FM.CT.getElevator() / 10F);
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -this.FM.CT.getAileron() / 10F, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        boolean flag1 = this instanceof Mi24X;
        if (s.startsWith("xx")) {
            if (s.startsWith("xxarmor")) {
                this.debuggunnery("Armor: Hit..");
                if (s.startsWith("xxarmorp")) {
                    int i = s.charAt(8) - 48;
                    switch (i) {
                        default:
                            break;

                        case 1:
                            this.getEnergyPastArmor(7.07D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                            shot.powerType = 0;
                            break;

                        case 2:
                        case 3:
                            this.getEnergyPastArmor(5.05D / (Math.abs(Aircraft.v1.y) + 0.0001D), shot);
                            shot.powerType = 0;
                            if ((shot.power <= 0.0F) && (Math.abs(Aircraft.v1.x) > 0.866D)) {
                                this.doRicochet(shot);
                            }
                            break;

                        case 4:
                            if (point3d.x > -1.35D) {
                                this.getEnergyPastArmor(5.05D / (Math.abs(Aircraft.v1.z) + 0.0001D), shot);
                                shot.powerType = 0;
                                if ((shot.power <= 0.0F) && (Math.abs(Aircraft.v1.x) > 0.866D)) {
                                    this.doRicochet(shot);
                                }
                            } else {
                                this.getEnergyPastArmor(5.05D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                            }
                            break;

                        case 5:
                        case 6:
                            this.getEnergyPastArmor(20.2D / (Math.abs(Aircraft.v1.y) + 0.0001D), shot);
                            if (shot.power > 0.0F) {
                                break;
                            }
                            if (Math.abs(Aircraft.v1.x) > 0.866D) {
                                this.doRicochet(shot);
                            } else {
                                this.doRicochetBack(shot);
                            }
                            break;

                        case 7:
                            this.getEnergyPastArmor(20.2D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                            if (shot.power <= 0.0F) {
                                this.doRicochetBack(shot);
                            }
                            break;
                    }
                }
                if (s.startsWith("xxarmorc1")) {
                    this.getEnergyPastArmor(7.07D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                }
                if (s.startsWith("xxarmort1")) {
                    this.getEnergyPastArmor(6.06D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                }
                return;
            }
            if (s.startsWith("xxspar")) {
                this.debuggunnery("Spar Construction: Hit..");
                if (s.startsWith("xxspark") && (this.chunkDamageVisible("Keel1") > 1) && (World.Rnd().nextFloat() > (Math.abs(Aircraft.v1.x) + 0.1D)) && (this.getEnergyPastArmor(3.4D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: Keel Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if (s.startsWith("xxsparlm")) {
                    if (flag1) {
                        if ((this.chunkDamageVisible("WingLMid") > 0) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                            this.debuggunnery("Spar Construction: WingLMid Spar Hit and Holed..");
                            this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
                        }
                    } else if ((this.chunkDamageVisible("WingLMid") > 2) && (World.Rnd().nextFloat() > (Math.abs(Aircraft.v1.x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F)) {
                        this.debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                        this.nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                    }
                }
                if (s.startsWith("xxsparrm")) {
                    if (flag1) {
                        if ((this.chunkDamageVisible("WingRMid") > 0) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                            this.debuggunnery("Spar Construction: WingRMid Spar Hit and Holed..");
                            this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
                        }
                    } else if ((this.chunkDamageVisible("WingRMid") > 2) && (World.Rnd().nextFloat() > (Math.abs(Aircraft.v1.x) + 0.11999999731779099D)) && (this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F)) {
                        this.debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                        this.nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                    }
                }
                if (s.startsWith("xxsparlo")) {
                    if (flag1) {
                        if ((this.chunkDamageVisible("WingLOut") > 0) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                            this.debuggunnery("Spar Construction: WingLOut Spar Hit and Holed..");
                            this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
                        }
                    } else if ((this.chunkDamageVisible("WingLOut") > 2) && (World.Rnd().nextFloat() > (Math.abs(Aircraft.v1.x) + 0.12D)) && (this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F)) {
                        this.debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                        this.nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                    }
                }
                if (s.startsWith("xxsparro")) {
                    if (flag1) {
                        if ((this.chunkDamageVisible("WingROut") > 0) && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                            this.debuggunnery("Spar Construction: WingROut Spar Hit and Holed..");
                            this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
                        }
                    } else if ((this.chunkDamageVisible("WingROut") > 2) && (World.Rnd().nextFloat() > (Math.abs(Aircraft.v1.x) + 0.12D)) && (this.getEnergyPastArmor(6.96D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F)) {
                        this.debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                        this.nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                    }
                }
                if (s.startsWith("xxsparsl") && (this.chunkDamageVisible("StabL") > 1) && (this.getEnergyPastArmor(6.5D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if (s.startsWith("xxsparsr") && (this.chunkDamageVisible("StabR") > 1) && (this.getEnergyPastArmor(6.5D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot) > 0.0F)) {
                    this.debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if (s.startsWith("xxspart") && (this.chunkDamageVisible("Tail1") > 2) && (this.getEnergyPastArmor(3.86F / (float) Math.sqrt((Aircraft.v1.y * Aircraft.v1.y) + (Aircraft.v1.z * Aircraft.v1.z)), shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    this.debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            }
            if (s.startsWith("xxlock")) {
                this.debuggunnery("Lock Construction: Hit..");
                if (s.startsWith("xxlockr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if (s.startsWith("xxlockvl") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
                }
                if (s.startsWith("xxlockvr") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
                }
                if (s.startsWith("xxlockal") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator);
                }
                if (s.startsWith("xxlockar") && (this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
                    this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
                }
            }
            if (s.startsWith("xxeng")) {
                this.debuggunnery("Engine Module: Hit..");
                if (s.endsWith("prop")) {
                    if ((this.getEnergyPastArmor(3.6F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.8F)) {
                        if (World.Rnd().nextFloat() < 0.5F) {
                            this.debuggunnery("Engine Module: Prop Governor Hit, Disabled..");
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        } else {
                            this.debuggunnery("Engine Module: Prop Governor Hit, Oil Pipes Damaged..");
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        }
                    }
                } else if (s.endsWith("gear")) {
                    if ((this.getEnergyPastArmor(4.6F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                        this.debuggunnery("Engine Module: Reductor Hit, Bullet Jams Reductor Gear..");
                        this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                    }
                } else if (s.endsWith("supc")) {
                    if (this.getEnergyPastArmor(0.01F, shot) > 0.0F) {
                        this.debuggunnery("Engine Module: Supercharger Disabled..");
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                    }
                } else if (s.endsWith("feed")) {
                    if ((this.getEnergyPastArmor(3.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            this.FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if (World.Rnd().nextFloat() < 0.05F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else if (s.endsWith("fue1")) {
                    if (this.getEnergyPastArmor(0.89F, shot) > 0.0F) {
                        this.debuggunnery("Engine Module: Fuel Feed Line Pierced, Engine Fires..");
                        this.FM.AS.hitEngine(shot.initiator, 0, 100);
                    }
                } else if (s.endsWith("case")) {
                    if (this.getEnergyPastArmor(2.2F, shot) > 0.0F) {
                        if (World.Rnd().nextFloat() < (shot.power / 175000F)) {
                            this.debuggunnery("Engine Module: Crank Case Hit, Bullet Jams Ball Bearings..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < (shot.power / 50000F)) {
                            this.debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    this.debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    this.getEnergyPastArmor(22.5F, shot);
                } else if (s.endsWith("cyl1")) {
                    if ((this.getEnergyPastArmor(1.3F, shot) > 0.0F) && (World.Rnd().nextFloat() < (this.FM.EI.engines[0].getCylindersRatio() * 1.75F))) {
                        this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
                        this.debuggunnery("Engine Module: Cylinders Assembly Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Operating..");
                        if (World.Rnd().nextFloat() < (shot.power / 48000F)) {
                            this.debuggunnery("Engine Module: Cylinders Assembly Hit, Engine Fires..");
                            this.FM.AS.hitEngine(shot.initiator, 0, 3);
                        }
                        if (World.Rnd().nextFloat() < 0.01F) {
                            this.debuggunnery("Engine Module: Cylinders Assembly Hit, Bullet Jams Piston Head..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        this.getEnergyPastArmor(22.5F, shot);
                    }
                    if ((Math.abs(point3d.y) < 0.138D) && (this.getEnergyPastArmor(3.2F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.5F)) {
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            this.FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if (World.Rnd().nextFloat() < 0.05F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if (World.Rnd().nextFloat() < 0.1F) {
                            this.debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else if (s.startsWith("xxeng1mag")) {
                    int j = s.charAt(9) - 49;
                    this.debuggunnery("Engine Module: Magneto " + j + " Hit, Magneto " + j + " Disabled..");
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
                } else if (s.startsWith("xxeng1oil") && (this.getEnergyPastArmor(0.5F, shot) > 0.0F)) {
                    this.debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
                    this.FM.AS.hitOil(shot.initiator, 0);
                }
            }
            if (s.startsWith("xxw1")) {
                if (this.FM.AS.astateEngineStates[0] == 0) {
                    this.debuggunnery("Engine Module: Water Radiator Pierced..");
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 0, 1);
                } else if (this.FM.AS.astateEngineStates[0] == 1) {
                    this.debuggunnery("Engine Module: Water Radiator Pierced..");
                    this.FM.AS.hitEngine(shot.initiator, 0, 1);
                    this.FM.AS.doSetEngineState(shot.initiator, 0, 2);
                }
            }
            if (s.startsWith("xxtank")) {
                int k = s.charAt(6) - 49;
                if ((this.getEnergyPastArmor(0.12F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
                    if (this.FM.AS.astateTankStates[k] == 0) {
                        this.debuggunnery("Fuel System: Fuel Tank " + k + " Pierced..");
                        this.FM.AS.hitTank(shot.initiator, 2, 1);
                        this.FM.AS.doSetTankState(shot.initiator, k, 1);
                    } else if (this.FM.AS.astateTankStates[k] == 1) {
                        this.debuggunnery("Fuel System: Fuel Tank " + k + " Pierced..");
                        this.FM.AS.hitTank(shot.initiator, 2, 1);
                        this.FM.AS.doSetTankState(shot.initiator, k, 2);
                    }
                    if ((shot.powerType == 3) && (World.Rnd().nextFloat() < 0.5F)) {
                        this.FM.AS.hitTank(shot.initiator, 2, 2);
                        this.debuggunnery("Fuel System: Fuel Tank " + k + " Pierced, State Shifted..");
                    }
                }
            }
            if (s.startsWith("xxmgun")) {
                if (s.endsWith("01")) {
                    this.debuggunnery("Armament System: Left Machine Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.endsWith("02")) {
                    this.debuggunnery("Armament System: Right Machine Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
            }
            if (s.startsWith("xxcannon")) {
                if (s.endsWith("01") && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                    this.debuggunnery("Armament System: Left Cannon: Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                if (s.endsWith("02") && (this.getEnergyPastArmor(0.25F, shot) > 0.0F)) {
                    this.debuggunnery("Armament System: Right Cannon: Disabled..");
                    this.FM.AS.setJamBullets(1, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if (s.startsWith("xxammo")) {
                if (s.startsWith("xxammol1") && (World.Rnd().nextFloat() < 0.023F)) {
                    this.debuggunnery("Armament System: Left Cannon: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(1, 0);
                }
                if (s.startsWith("xxammor1") && (World.Rnd().nextFloat() < 0.023F)) {
                    this.debuggunnery("Armament System: Right Cannon: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(1, 1);
                }
                if (s.startsWith("xxammol2") && (World.Rnd().nextFloat() < 0.023F)) {
                    this.debuggunnery("Armament System: Left Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if (s.startsWith("xxammor2") && (World.Rnd().nextFloat() < 0.023F)) {
                    this.debuggunnery("Armament System: Right Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 12.6F), shot);
            }
            if (s.startsWith("xxbomb") && (World.Rnd().nextFloat() < 0.00345F) && (this.FM.CT.Weapons[3] != null) && this.FM.CT.Weapons[3][0].haveBullets()) {
                this.debuggunnery("Armament System: Bomb Payload Detonated..");
                this.FM.AS.hitTank(shot.initiator, 0, 10);
                this.FM.AS.hitTank(shot.initiator, 1, 10);
                this.nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            if (s.startsWith("xxpnm") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)) {
                this.debuggunnery("Pneumo System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 1);
            }
            if (s.startsWith("xxhyd") && (this.getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if (s.startsWith("xxins")) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
            return;
        }
        if (s.startsWith("xcockpit") || s.startsWith("xblister")) {
            if (point3d.z > 0.473D) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            } else if (point3d.y > 0.0D) {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            } else {
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
            }
        }
        if (s.startsWith("xcf")) {
            if (point3d.x < -1.94D) {
                if (this.chunkDamageVisible("Tail1") < 3) {
                    this.hitChunk("Tail1", shot);
                }
            } else {
                if (point3d.x <= 1.342D) {
                    if ((point3d.z < -0.591D) || ((point3d.z > 0.408D) && (point3d.x > 0.0D))) {
                        this.getEnergyPastArmor(5.05D / (Math.abs(Aircraft.v1.z) + 0.0001D), shot);
                        if ((shot.power <= 0.0F) && (Math.abs(Aircraft.v1.x) > 0.866D)) {
                            this.doRicochet(shot);
                        }
                    } else {
                        this.getEnergyPastArmor(5.05D / (Math.abs(Aircraft.v1.y) + 0.0001D), shot);
                        if ((shot.power <= 0.0F) && (Math.abs(Aircraft.v1.x) > 0.866D)) {
                            this.doRicochet(shot);
                        }
                    }
                }
                if (this.chunkDamageVisible("CF") < 3) {
                    this.hitChunk("CF", shot);
                }
            }
        } else if (s.startsWith("xoil")) {
            if (point3d.z < -0.981D) {
                this.getEnergyPastArmor(5.05D / (Math.abs(Aircraft.v1.z) + 0.0001D), shot);
                if (shot.power <= 0.0F) {
                    this.doRicochet(shot);
                }
            } else if ((point3d.x > 0.537D) || (point3d.x < -0.1D)) {
                this.getEnergyPastArmor(0.2D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                if (shot.power <= 0.0F) {
                    this.doRicochetBack(shot);
                }
            } else {
                this.getEnergyPastArmor(5.05D / (Math.abs(Aircraft.v1.y) + 0.0001D), shot);
                if (shot.power <= 0.0F) {
                    this.doRicochet(shot);
                }
            }
            if (this.chunkDamageVisible("CF") < 3) {
                this.hitChunk("CF", shot);
            }
        } else if (s.startsWith("xeng")) {
            if (point3d.z > 0.159D) {
                this.getEnergyPastArmor((1.25F * World.Rnd().nextFloat(0.95F, 1.12F)) / (Math.abs(Aircraft.v1.z) + 0.0001D), shot);
            } else if ((point3d.x > 1.335D) && (point3d.x < 2.386D) && (point3d.z > -0.06D) && (point3d.z < 0.064D)) {
                this.getEnergyPastArmor(0.5D / (Math.abs(Aircraft.v1.y) + 0.0001D), shot);
            } else if ((point3d.x > 2.53D) && (point3d.x < 2.992D) && (point3d.z > -0.235D) && (point3d.z < 0.011D)) {
                this.getEnergyPastArmor(4.04D / (Math.abs(Aircraft.v1.y) + 0.0001D), shot);
            } else if ((point3d.x > 2.559D) && (point3d.z < -0.595D)) {
                this.getEnergyPastArmor(4.04D / (Math.abs(Aircraft.v1.z) + 0.0001D), shot);
            } else if ((point3d.x > 1.849D) && (point3d.x < 2.251D) && (point3d.z < -0.71D)) {
                this.getEnergyPastArmor(4.04D / (Math.abs(Aircraft.v1.z) + 0.0001D), shot);
            } else if (point3d.x > 3.003D) {
                this.getEnergyPastArmor(World.Rnd().nextFloat(2.3F, 3.2F), shot);
            } else if (point3d.z < -0.606D) {
                this.getEnergyPastArmor(5.05D / (Math.abs(Aircraft.v1.z) + 0.0001D), shot);
            } else {
                this.getEnergyPastArmor(5.05D / (Math.abs(Aircraft.v1.y) + 0.0001D), shot);
            }
            if ((Math.abs(Aircraft.v1.x) > 0.866D) && ((shot.power <= 0.0F) || (World.Rnd().nextFloat() < 0.1F))) {
                this.doRicochet(shot);
            }
            if (this.chunkDamageVisible("Engine1") < 2) {
                this.hitChunk("Engine1", shot);
            }
        } else if (s.startsWith("xtail")) {
            this.hitChunk("Tail1", shot);
        } else if (s.startsWith("xkeel")) {
            if (this.chunkDamageVisible("Keel1") < 2) {
                this.hitChunk("Keel1", shot);
                this.TailRotorDamage();
            }
        } else if (s.startsWith("xrudder") && (this.getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)) {
            if (this.chunkDamageVisible("Rudder1") < 1) {
                this.hitChunk("Rudder1", shot);
            }
        } else if (s.startsWith("xstab")) {
            if (s.startsWith("xstabl") && (this.chunkDamageVisible("StabL") < 2)) {
                this.hitChunk("StabL", shot);
            }
            if (s.startsWith("xstabr") && (this.chunkDamageVisible("StabR") < 1)) {
                this.hitChunk("StabR", shot);
            }
        } else if (s.startsWith("xvator")) {
            if (s.startsWith("xvatorl") && (this.chunkDamageVisible("VatorL") < 1)) {
                this.hitChunk("VatorL", shot);
            }
            if (s.startsWith("xvatorr") && (this.chunkDamageVisible("VatorR") < 1)) {
                this.hitChunk("VatorR", shot);
            }
        } else if (s.startsWith("xwing")) {
            if (s.startsWith("xwinglin") && (this.chunkDamageVisible("WingLIn") < 3)) {
                this.hitChunk("WingLIn", shot);
            }
            if (s.startsWith("xwingrin") && (this.chunkDamageVisible("WingRIn") < 3)) {
                this.hitChunk("WingRIn", shot);
            }
            if (s.startsWith("xwinglmid") && (this.chunkDamageVisible("WingLMid") < 3)) {
                this.hitChunk("WingLMid", shot);
            }
            if (s.startsWith("xwingrmid") && (this.chunkDamageVisible("WingRMid") < 3)) {
                this.hitChunk("WingRMid", shot);
            }
            if (s.startsWith("xwinglout") && (this.chunkDamageVisible("WingLOut") < 3)) {
                this.hitChunk("WingLOut", shot);
            }
            if (s.startsWith("xwingrout") && (this.chunkDamageVisible("WingROut") < 3)) {
                this.hitChunk("WingROut", shot);
            }
        } else if (s.startsWith("xarone")) {
            if (s.startsWith("xaronel")) {
                this.hitChunk("AroneL", shot);
            }
            if (s.startsWith("xaroner")) {
                this.hitChunk("AroneR", shot);
            }
        } else if (s.startsWith("xgear")) {
            if (s.endsWith("1") && (World.Rnd().nextFloat() < 0.05F)) {
                this.debuggunnery("Hydro System: Disabled..");
                this.FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if ((s.endsWith("2a") || s.endsWith("2b")) && (World.Rnd().nextFloat() < 0.1F) && (this.getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)) {
                this.debuggunnery("Undercarriage: Stuck..");
                this.FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else if (s.startsWith("xturret")) {
            if (this.getEnergyPastArmor(0.25F, shot) > 0.0F) {
                this.debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                this.FM.AS.setJamBullets(10, 0);
                this.FM.AS.setJamBullets(10, 1);
                this.getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 26.35F), shot);
            }
        } else if (s.startsWith("xhelm")) {
            this.getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 3.56F), shot);
            if (shot.power <= 0.0F) {
                this.doRicochetBack(shot);
            }
        } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int l;
            if (s.endsWith("a")) {
                byte0 = 1;
                l = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                l = s.charAt(6) - 49;
            } else {
                l = s.charAt(5) - 49;
            }
            this.hitFlesh(l, shot, byte0);
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                // fall through

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Head2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.bObserverKilled = true;
                // fall through

            default:
                return;
        }
    }

    private void TailRotorDamage() {
        int rnd = TrueRandom.nextInt(50);
        if (!this.TailRotorDestroyed && (rnd == 1)) {
            if (World.getPlayerAircraft() == this) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Tail Rotor: Damaged!");
            }
            this.TailRotorDestroyed = true;
        }
    }

    public void blyat(double v, double a, double d, float f) {
        v /= 3.6D;
        if (v < this.tV[0].x) {
            this.tV[0].x -= (this.tV[0].x - v) / d;
        } else {
            this.tV[0].x += (v - this.tV[0].x) / d;
        }
        if (a < this.FM.Loc.z) {
            this.tV[0].z = -((this.tV[0].x * (a - this.FM.Loc.z)) / d);
        } else {
            this.tV[0].z = (this.tV[0].x * (a - this.FM.Loc.z)) / d;
        }
        this.tV[1].interpolate(this.tV[0], 0.5F * f);
        Vector3d v3d = new Vector3d(this.tV[1]);
        this.pos.getAbsOrient().transform(v3d);
        this.FM.Vwld.set(v3d);
        this.FM.Vwld.z = this.tV[1].z;
        this.FM.Loc.x += this.FM.Vwld.x * f;
        this.FM.Loc.y += this.FM.Vwld.y * f;
        this.FM.Loc.z += this.FM.Vwld.z * f;
    }

    private void stability(float f) {
        if (this.TailRotorDestroyed) {
            this.FM.producedAM.z += 100000D;
        }
        Vector3f eVect = new Vector3f();
        eVect.x = 1.0F;
        eVect.y = 0.0F;
        eVect.z = 0.0F;
        this.FM.EI.engines[0].setVector(eVect);
        this.FM.EI.engines[1].setVector(eVect);
        this.pos.getAbs(this.localPoint3d1);
        Vector3d localVector3d = new Vector3d(this.FM.Vwld);
        float avT = (this.FM.EI.engines[0].getControlThrottle() + this.FM.EI.engines[1].getControlThrottle()) / 2.0F;
        float alt = this.FM.getAltitude() - Landscape.HQ_Air((float) this.FM.Loc.x, (float) this.FM.Loc.y);
        this.FM.SensYaw = 0.5F;
        this.FM.SensPitch = 0.5F;
        this.FM.SensRoll = 0.5F;
        if ((((Maneuver) this.FM).get_maneuver() == 26) && (alt < 10F)) {
            this.takeOff = true;
        }
        if (this.takeOff) {
            switch (this.takeOffStep) {
                default:
                    break;

                case 0:
                    if ((this.FM.CT.PowerControl > 0.9D) && (this.FM.EI.engines[0].getStage() == 6) && (this.FM.EI.engines[1].getStage() == 6) && (this.FM.CT.cockpitDoorControl < 0.9D)) {
                        Vector3d tmpV1 = new Vector3d();
                        tmpV1.set(1.0D, 0.0D, 0.0D);
                        this.FM.Or.transform(tmpV1);
                        tmpV1.scale(20D);
                        this.tP[0].set(this.FM.Loc);
                        this.tP[0].add(tmpV1);
                        this.tP[0].z += 10D;
                        tmpV1.set(1.0D, 0.0D, 0.0D);
                        this.FM.Or.transform(tmpV1);
                        tmpV1.scale(70D);
                        this.tP[1].set(this.FM.Loc);
                        this.tP[1].add(tmpV1);
                        this.tP[1].z += 20D;
                        tmpV1.set(1.0D, 0.0D, 0.0D);
                        this.FM.Or.transform(tmpV1);
                        tmpV1.scale(500D);
                        this.tP[2].set(this.FM.Loc);
                        this.tP[2].add(tmpV1);
                        this.tP[2].z += 30D;
                        this.tOr.set(this.FM.Or);
                        this.takeOffStep++;
                    }
                    break;

                case 1:
                    Reflection.setBoolean(this.FM, "callSuperUpdate", false);
                    ((Maneuver) this.FM).set_maneuver(66);
                    ((Maneuver) this.FM).setSpeedMode(11);
                    this.FM.Vwld.x = 0.0D;
                    this.FM.Vwld.y = 0.0D;
                    this.FM.Vwld.z = 0.4D;
                    this.FM.Loc.x += this.FM.Vwld.x * f;
                    this.FM.Loc.y += this.FM.Vwld.y * f;
                    this.FM.Loc.z += this.FM.Vwld.z * f;
                    this.tOr.setYPR(this.tOr.getYaw(), 0.0F, this.tOr.getRoll());
                    this.FM.Or.interpolate(this.tOr, 0.2F * f);
                    if (alt > 3.5F) {
                        this.takeOffStep++;
                    }
                    break;

                case 2:
                    Reflection.setBoolean(this.FM, "callSuperUpdate", false);
                    ((Maneuver) this.FM).set_maneuver(66);
                    ((Maneuver) this.FM).setSpeedMode(11);
                    this.tOr.setYPR(this.tOr.getYaw(), -4F, this.tOr.getRoll());
                    this.FM.Or.interpolate(this.tOr, 0.2F * f);
                    this.blyat(7D, this.tP[0].z, this.pos.getAbsPoint().distance(this.tP[0]), f);
                    if ((this.pos.getAbsPoint().distance(this.tP[0]) < 3D) || (alt > 10F)) {
                        this.takeOffStep++;
                    }
                    break;

                case 3:
                    Reflection.setBoolean(this.FM, "callSuperUpdate", false);
                    ((Maneuver) this.FM).set_maneuver(66);
                    ((Maneuver) this.FM).setSpeedMode(11);
                    this.tOr.setYPR(this.tOr.getYaw(), -7F, this.tOr.getRoll());
                    this.FM.Or.interpolate(this.tOr, 0.2F * f);
                    this.blyat(40D, this.tP[1].z, this.pos.getAbsPoint().distance(this.tP[1]), f);
                    if ((this.pos.getAbsPoint().distance(this.tP[1]) < 5D) || (alt > 20F)) {
                        this.takeOffStep++;
                    }
                    break;

                case 4:
                    Reflection.setBoolean(this.FM, "callSuperUpdate", false);
                    ((Maneuver) this.FM).set_maneuver(66);
                    ((Maneuver) this.FM).setSpeedMode(11);
                    this.tOr.setYPR(this.tOr.getYaw(), -4F, this.tOr.getRoll());
                    this.FM.Or.interpolate(this.tOr, 0.1F * f);
                    this.blyat(200D, this.tP[2].z, this.pos.getAbsPoint().distance(this.tP[2]), f);
                    if ((this.pos.getAbsPoint().distance(this.tP[2]) < 20D) || (this.FM.getSpeedKMH() > 195F)) {
                        Reflection.setBoolean(this.FM, "callSuperUpdate", true);
                        ((Maneuver) this.FM).unblock();
                        this.takeOff = false;
                    }
                    break;
            }
        }
        if (((Maneuver) this.FM).get_maneuver() == 25) {
            this.landing = true;
        }
        if (this.landing) {
            if ((alt <= 17F) && (alt > 7F)) {
                this.FM.setCapableOfTaxiing(false);
                ((Maneuver) this.FM).set_maneuver(66);
                this.FM.CT.ElevatorControl = 0.8F;
                localVector3d.x *= 0.995D;
                localVector3d.y *= 0.995D;
                localVector3d.z *= 0.1D;
            }
            if (alt <= 7F) {
                ((Maneuver) this.FM).setSpeedMode(8);
                localVector3d.x *= 0.97D;
                localVector3d.y *= 0.97D;
                localVector3d.z *= 0.4D;
                if (this.FM.Gears.nOfGearsOnGr > 2) {
                    ((Maneuver) this.FM).set_maneuver(66);
                    this.FM.CT.BrakeControl = 1.0F;
                    this.FM.EI.setEngineStops();
                    MsgDestroy.Post(Time.current() + 12000L, this);
                }
            }
            this.setSpeed(localVector3d);
        }
        if ((this.FM.getSpeedKMH() > 0.1F) && !this.takeOff && !this.landing && (((Maneuver) this.FM).get_maneuver() != 44) && (((Maneuver) this.FM).get_maneuver() != 49) && !this.FM.isReadyToDie() && !this.FM.isTakenMortalDamage()) {
            this.FM.producedAF.z += (avT * Aircraft.cvt(alt, 30F, 50F, 30000F, 0.0F)) + (avT * 20000F);
            this.FM.producedAF.x += (avT * Aircraft.cvt(alt, 30F, 50F, 5000F, 0.0F)) + (avT * 5000F);
            if (this.FM.getSpeedKMH() >= 310F) {
                this.FM.Sq.dragParasiteCx += 0.25F;
            }
            this.FM.Vwld.z *= this.FM.getVertSpeed() <= 0.0D ? 1.0D : Aircraft.cvt(this.FM.getVertSpeed(), 1.0F, 10F, 1.0F, 0.95F);
        }
        this.engineRPM = Math.sqrt((Math.pow(this.FM.EI.engines[0].getRPM(), 2D) + Math.pow(this.FM.EI.engines[1].getRPM(), 2D)) / 2D);
        this.reductorRPM = this.engineRPM * 0.016D;
        this.tailRotorRPM = this.engineRPM * 0.07413D;
        this.rotorRPM = this.reductorRPM;
    }

    public void human() {
        if (this.FM.EI.getCurControl(0) && !this.FM.EI.getCurControl(1)) {
            this.FM.EI.engines[1].setControlProp(this.FM.EI.engines[0].getControlProp());
        }
        if (this.FM.EI.getCurControl(1) && !this.FM.EI.getCurControl(0)) {
            this.FM.EI.engines[0].setControlProp(this.FM.EI.engines[1].getControlProp());
        }
        if (this.bAPkrentang) {
            if ((this.FM.CT.getElevator() > 0.07F) || (this.FM.CT.getElevator() < -0.07F)) {
                this.apTang = -this.FM.Or.getTangage();
            }
            if ((this.FM.CT.getAileron() > 0.07F) || (this.FM.CT.getAileron() < -0.07F)) {
                this.apKren = -this.FM.Or.getKren();
            }
            this.FM.CT.setTrimElevatorControl(Aircraft.cvt(-this.FM.Or.getTangage(), this.apTang - 7F, this.apTang + 7F, -0.2F, 0.2F));
            this.FM.CT.setTrimAileronControl(Aircraft.cvt(-this.FM.Or.getKren(), this.apKren - 15F, this.apKren + 15F, -0.2F, 0.2F));
        } else {
            this.FM.CT.setTrimElevatorControl(0.0F);
            this.FM.CT.setTrimAileronControl(0.0F);
        }
        float aileT = this.FM.CT.getAileron() + (float) this.forceTrim_x;
        float aile = aileT + this.FM.CT.getTrimAileronControl();
        if (aile > 1.0F) {
            aile = 1.0F;
        }
        if (aile < -1F) {
            aile = -1F;
        }
        float elevT = this.FM.CT.getElevator() + (float) this.forceTrim_y;
        float elev = elevT + this.FM.CT.getTrimElevatorControl();
        if (elev > 1.0F) {
            elev = 1.0F;
        }
        if (elev < -1F) {
            elev = -1F;
        }
        if (this.bAPazimut) {
            if ((this.FM.CT.getRudder() > 0.07F) || (this.FM.CT.getRudder() < -0.07F)) {
                this.apAzimut = -this.FM.Or.getAzimut();
            }
            this.FM.CT.setTrimRudderControl(Aircraft.cvt(-this.FM.Or.getAzimut(), this.apAzimut - 10F, this.apAzimut + 10F, -0.2F, 0.2F));
        } else {
            this.FM.CT.setTrimRudderControl(0.0F);
        }
        float ruddT = this.FM.CT.getRudder() + (float) this.forceTrim_z;
        float rudd = ruddT + this.FM.CT.getTrimRudderControl();
        if (rudd > 1.0F) {
            rudd = 1.0F;
        }
        if (rudd < -1F) {
            rudd = -1F;
        }
        if (this.getTrim) {
            this.forceTrim_x = aileT;
            this.forceTrim_y = elevT;
            this.forceTrim_z = ruddT;
            this.getTrim = false;
        }
        double Wx = this.FM.getW().x;
        double Wy = this.FM.getW().y;
        double Wz = this.FM.getW().z;
        if (this.bAPalt) {
            this.apALltPitch = Aircraft.cvt(this.FM.getAltitude(), this.apAlt - 20F, this.apAlt + 20F, 0.2F, -0.2F);
        } else {
            this.apALltPitch = 0.0F;
        }
        float inPitch = (this.FM.EI.engines[0].getControlProp() + this.FM.EI.engines[1].getControlProp()) / 2.0F;
        float aPitchT = this.aOldPitch;
        if (aPitchT < inPitch) {
            aPitchT = (float) (aPitchT + (0.007D - (this.rotorRPM / 120000D)));
        }
        if (aPitchT > inPitch) {
            aPitchT = (float) (aPitchT - (0.007D - (this.rotorRPM / 120000D)));
        }
        this.aOldPitch = aPitchT;
        this.aPitch = aPitchT + this.apALltPitch;
        if (this.aPitch > 1.0F) {
            this.aPitch = 1.0F;
        }
        Vector3d vFlow = this.FM.getVflow();
        double sinkRate = vFlow.z;
        float falt = this.FM.getAltitude() - Landscape.HQ_Air((float) this.FM.Loc.x, (float) this.FM.Loc.y);
        float airDensity = Atmosphere.density((float) this.FM.Loc.z);
        double rotorSurface = 20D;
        double rotorSurface_cyclic = 10D;
        double tailRotorSurface = 1.35D;
        double rotorCy = 1.3D;
        double rotorCx = 0.02D;
        double rotorLineCx = 0.00075D * (this.aPitch * this.aPitch * 12F * 12F);
        double tailRotorLineCx = 0.000752D * (rudd * rudd * 10F * 10F);
        double spdFlowCoef_0 = Aircraft.cvt((float) vFlow.x, 5.5F, 22.2F, 1.0F, 1.3F);
        double altFlowCoef_1 = Aircraft.cvt(falt, 2.0F, 12F, Aircraft.cvt((float) vFlow.x, 5.5F, 22.2F, 1.2F, 1.0F), 1.0F);
        double rotorCyDyn_0 = 0.0198D * altFlowCoef_1 * spdFlowCoef_0 * airDensity;
        double rotorCyDyn_line = 0.0765D * altFlowCoef_1 * spdFlowCoef_0 * airDensity;
        if ((sinkRate < 0.0D) && (Math.abs(vFlow.x) < Math.abs(sinkRate)) && (sinkRate < 3D)) {
            ((RealFlightModel) this.FM).producedShakeLevel += 0.005D * Math.abs(sinkRate);
            rotorCyDyn_0 -= Math.abs(sinkRate) * 0.001D;
            rotorCyDyn_line -= Math.abs(sinkRate) * 0.001D;
        }
        double rotorDiameter = 17.3D;
        double tailRotorDiameter = 3D;
        float fAOA = this.FM.getAOA();
        float fuselageCxS = 5F - Aircraft.cvt((float) vFlow.x, 41.6F, 83.3F, 0.0F, 4F);
        float fuselageCyS = 20F;
        double load = (this.FM.M.getFullMass() - this.FM.M.massEmpty) * airDensity;
        double bladeAOACx = this.aPitch <= (0.7D - (load / 27000D)) ? 0.0D : (load / 50000D) * ((this.aPitch - (0.7D - (load / 27000D))) / (0.3D + (load / 27000D))) * (this.rotorRPM / 240D);
        double bladeCx = this.FM.Or.getTangage() <= 0.0D ? -(((vFlow.x + vFlow.z) * Math.abs(this.FM.Or.getTangage()) * 0.0001D) / 3D) : (vFlow.x + vFlow.z) * Math.abs(this.FM.Or.getTangage()) * 0.0001D;
        float flowRPM = Aircraft.cvt((float) vFlow.x, 0.0F, 27F, 0.0F, (float) (200D - (Math.abs(fAOA) * 3D) - (sinkRate <= 0.0D ? 0.0D : sinkRate * 12D)));
        this.engineRPM = Math.sqrt((Math.pow(this.FM.EI.engines[0].getRPM(), 2D) + Math.pow(this.FM.EI.engines[1].getRPM(), 2D)) / 2D);
        this.reductorRPM = (this.engineRPM * 0.016D) <= flowRPM ? flowRPM : (this.engineRPM * 0.016D) <= 228D ? this.engineRPM * 0.016D : 228D;
        double tempRPM = this.reductorRPM * ((1.0D + bladeCx) - bladeAOACx);
        this.rotorRPM += this.rotorRPM >= tempRPM ? -(0.005D + (this.reductorRPM * 0.0007D)) : 0.005D + (this.reductorRPM * 0.0007D);
        if ((this.rotorRPM > this.reductorRPM) && (this.rotorRPM < 240D)) {
            this.rotorRPM = this.reductorRPM;
        }
        double hubDirection_x = Math.toRadians(0.0D);
        double hubDirection_y = Math.toRadians(5D);
        double rotorHeight = 2D;
        double rotorSpeed = (Math.PI * rotorDiameter * this.rotorRPM) / 120D;
        double autoPitch = this.PitchAuto(-Wy);
        double autoRoll = this.RollAuto(Wx);
        double d_hubDirection_x = Math.toRadians(-(aile + autoRoll) * 2D);
        double d_hubDirection_y = Math.toRadians((elev + autoPitch) * 5D);
        double rotorLift_dyn = 0.5D * (rotorCyDyn_0 + (rotorCyDyn_line * 12D * this.aPitch)) * rotorSurface * airDensity * rotorSpeed * rotorSpeed;
        double rotorLift_moment_z = 0.5D * (rotorCx + (rotorLineCx * 1.2D * this.aPitch)) * rotorSurface * airDensity * rotorSpeed * rotorSpeed;
        double rotorLift_moment_y = -(rotorDiameter / 2D) * 0.5D * 0.5D * (rotorCyDyn_line * 5D * (elev + autoPitch)) * rotorSurface_cyclic * airDensity * rotorSpeed * rotorSpeed;
        double rotorLift_moment_x = (rotorDiameter / 2D) * 0.5D * 0.5D * (rotorCyDyn_line * 3D * (aile + autoRoll)) * rotorSurface_cyclic * airDensity * rotorSpeed * rotorSpeed;
        rotorLift_moment_y += rotorLift_dyn * (rotorHeight * Math.sin(d_hubDirection_y));
        rotorLift_moment_x += rotorLift_dyn * (rotorHeight * Math.sin(d_hubDirection_x));
        this.tailRotorRPM = this.engineRPM * 0.07413D;
        double tailRotorSpeed = (Math.PI * tailRotorDiameter * this.tailRotorRPM) / 120D;
        double tailRotorLift_dyn = 0.5D * (rotorCyDyn_line * 10D * rudd) * airDensity * tailRotorSpeed * tailRotorSpeed;
        double tailRotorLift_moment_y = 0.5D * (tailRotorDiameter / 2D) * 0.5D * (rotorCx + tailRotorLineCx) * tailRotorSurface * airDensity * tailRotorSpeed * tailRotorSpeed;
        double tailRotorLift_moment_z = tailRotorLift_dyn * 10D;
        double rotateSpeed_z = Wz * (rotorDiameter / 2D) * 0.5D;
        double rotateSpeed_y = Wy * (rotorDiameter / 2D) * 0.5D;
        double rotateSpeed_x = Wx * (rotorDiameter / 2D) * 0.5D;
        double balanceMoment_x = (rotorDiameter / 2D) * 0.66D * rotateSpeed_x * rotateSpeed_x * rotorSurface * airDensity * rotorCy * 0.5D;
        if (rotateSpeed_x < 0.0D) {
            balanceMoment_x = 0.0D - balanceMoment_x;
        }
        double balanceMoment_y = (rotorDiameter / 2D) * 0.66D * rotateSpeed_y * rotateSpeed_y * rotorSurface * airDensity * rotorCy * 0.5D;
        if (rotateSpeed_y < 0.0D) {
            balanceMoment_y = 0.0D - balanceMoment_y;
        }
        double balanceMoment_z = 10D * rotateSpeed_z * rotateSpeed_z * tailRotorSurface * airDensity * rotorCy * 0.5D;
        if (rotateSpeed_z < 0.0D) {
            balanceMoment_z = 0.0D - balanceMoment_z;
        }
        float antiSinkForce;
        if (sinkRate >= 0.0D) {
            antiSinkForce = -(float) (((rotorCy * rotorSurface) + fuselageCyS) * airDensity * sinkRate * sinkRate);
        } else {
            antiSinkForce = (float) (((rotorCy * rotorSurface) + fuselageCyS) * airDensity * sinkRate * sinkRate);
        }
        float headOnForce;
        double dragMoment_y;
        if (vFlow.x >= 0.0D) {
            headOnForce = -(float) ((((rotorCx + rotorLineCx) * rotorSurface) + fuselageCxS) * airDensity * vFlow.x * vFlow.x);
            dragMoment_y = -2D * ((rotorCx + rotorLineCx) * rotorSurface * airDensity * vFlow.x * vFlow.x);
        } else {
            headOnForce = (float) ((((rotorCx + rotorLineCx) * rotorSurface) + fuselageCxS) * airDensity * vFlow.x * vFlow.x);
            dragMoment_y = 2D * ((rotorCx + rotorLineCx) * rotorSurface * airDensity * vFlow.x * vFlow.x);
        }
        float sideForce;
        float tailRotorMoment;
        double dragMoment_x;
        if (vFlow.y >= 0.0D) {
            sideForce = -(float) ((((rotorCx + rotorLineCx) * rotorSurface) + fuselageCyS) * airDensity * vFlow.y * vFlow.y);
            tailRotorMoment = -(float) (rotorCy * tailRotorSurface * airDensity * vFlow.y * vFlow.y) * 10F;
            dragMoment_x = 2D * ((rotorCx + rotorLineCx) * rotorSurface * airDensity * vFlow.y * vFlow.y);
        } else {
            sideForce = (float) ((((rotorCx + rotorLineCx) * rotorSurface) + fuselageCyS) * airDensity * vFlow.y * vFlow.y);
            tailRotorMoment = (float) (rotorCy * tailRotorSurface * airDensity * vFlow.y * vFlow.y) * 10F;
            dragMoment_x = -2D * ((rotorCx + rotorLineCx) * rotorSurface * airDensity * vFlow.y * vFlow.y);
        }
        double rotorLift_3D_x = Math.sin(hubDirection_y - d_hubDirection_y) * Math.cos(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
        double rotorLift_3D_y = Math.sin(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
        double rotorLift_3D_z = Math.cos(hubDirection_y - d_hubDirection_y) * Math.cos(hubDirection_x + d_hubDirection_x) * rotorLift_dyn;
        float antiLiftForce;
        if (sinkRate >= 1.0D) {
            antiLiftForce = (float) (0.5D * rotorCy * rotorSurface * airDensity * sinkRate * sinkRate) * 20F;
        } else {
            antiLiftForce = 0.0F;
        }
        this.FM.producedAF.x += headOnForce + rotorLift_3D_x;
        this.FM.producedAF.y += sideForce + rotorLift_3D_y;
        this.FM.producedAF.z += (antiSinkForce + rotorLift_3D_z) - antiLiftForce;
        this.FM.producedAM.x += (dragMoment_x - balanceMoment_x) + rotorLift_moment_x;
        this.FM.producedAM.y += ((dragMoment_y + tailRotorLift_moment_y) - balanceMoment_y) + rotorLift_moment_y;
        this.FM.producedAM.z += this.TailRotorDestroyed ? (this.rotorRPM * 50000D) + (this.aPitch * 50000F) : (this.FM.producedAM.z += (tailRotorMoment - tailRotorLift_moment_z - balanceMoment_z) + rotorLift_moment_z);
        this.FM.Vwld.z *= sinkRate <= 0.0D ? 1.0D : Aircraft.cvt((float) sinkRate, 1.0F, 10F, 1.0F, 0.95F);
        this.FM.Sq.dragParasiteCx += fAOA <= 0.0F ? 0.0F : fAOA / 5F;
        rotateSpeed_z = 0.0D;
        rotateSpeed_y = 0.0D;
        rotateSpeed_x = 0.0D;
        headOnForce = 0.0F;
        sideForce = 0.0F;
        if (this.rotorRPM > 0.0D) {
            float shakeMe = 0.0F;
            float shakeRPM = (float) ((this.rotorRPM / 2.4D) * 0.01D);
            if ((vFlow.x < 22.2D) && (vFlow.x > 11.1D)) {
                shakeMe = Aircraft.cvt((float) vFlow.x, 11.1F, 22.2F, 0.03F, (shakeRPM * 0.01F) + (this.aPitch * shakeRPM * 0.015F));
            } else if (vFlow.x < 11.1D) {
                shakeMe = Aircraft.cvt((float) vFlow.x, 5.5F, 11.1F, (shakeRPM * 0.01F) + (this.aPitch * shakeRPM * 0.015F), 0.03F);
            }
            ((RealFlightModel) this.FM).producedShakeLevel += shakeMe;
        }
    }

    public double PitchAuto(double p) {
        p = -(p * 4D);
        if (p >= 0.2D) {
            p = 0.2D;
        }
        if (p <= -0.2D) {
            p = -0.2D;
        }
        return p;
    }

    public double RollAuto(double k) {
        k = -(k * 4D);
        if (k >= 0.2D) {
            k = 0.2D;
        }
        if (k <= -0.2D) {
            k = -0.2D;
        }
        return k;
    }
    
    public void auxPressed(int i) {
        super.auxPressed(i);
        if (i == 20) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Trimmer: Set");
            this.getTrim = true;
            this.trimSound();
        }
        if (i == 21) {
            this.forceTrim_x = 0.0D;
            this.forceTrim_y = 0.0D;
            this.forceTrim_z = 0.0D;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Trimmer: Reset");
            this.trimSound();
        }
        if (i == 22) {
            this.repMod++;
            if (this.repMod > 7) {
                this.repMod = 0;
            }
            if (this.repMod == 0) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: None");
            }
            if (this.repMod == 1) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For All Ground Targets");
            }
            if (this.repMod == 2) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For Armor");
            }
            if (this.repMod == 3) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For Guns");
            }
            if (this.repMod == 4) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For Vehicles");
            }
            if (this.repMod == 5) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For Train");
            }
            if (this.repMod == 6) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For Ship");
            }
            if (this.repMod == 7) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Operator: Watch For All Air Targets");
            }
        }
        if (i == 23) {
            if (!this.bManualFire) {
                this.bManualFire = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "9K113 Fire Control: Pilot");
            } else if (this.bManualFire) {
                this.bManualFire = false;
                this.laserOn = false;
                Reflection.setInt(this.guidedMissileUtils, "engageMode", 1);
                HUD.log(AircraftHotKeys.hudLogWeaponId, "9K113 Fire Control: Operator");
            }
        }
        if ((i == 24) && this.bManualFire) {
            if (!this.laserOn) {
                this.laserOn = true;
                this.laserLock = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Raduga-Sh: On");
            } else if (this.laserOn) {
                this.laserOn = false;
                this.laserLock = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Raduga-Sh: Off");
            }
        }
        if (i == 26) {
            this.aso2mode++;
            if (this.aso2mode > 2) {
                this.aso2mode = 0;
            }
            if (this.aso2mode == 0) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASO-2V: Standby");
                this.intervalCommonThreat = 0L;
            }
            if (this.aso2mode == 1) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASO-2V: 4 sec. interval");
            }
            if (this.aso2mode == 2) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASO-2V: 8 sec. interval");
            }
        }
        if (i == 27) {
            if (!this.bAPazimut) {
                this.bAPazimut = true;
                this.apAzimut = -this.FM.Or.getAzimut();
                HUD.log(AircraftHotKeys.hudLogWeaponId, "VUAP-1: Heading: On");
            } else if (this.bAPazimut) {
                this.bAPazimut = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "VUAP-1: Heading: Off");
            }
        }
        if (i == 28) {
            if (!this.bAPalt) {
                this.bAPalt = true;
                this.apAlt = this.FM.getAltitude();
                HUD.log(AircraftHotKeys.hudLogWeaponId, "VUAP-1: Altitude: On");
            } else if (this.bAPalt) {
                this.bAPalt = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "VUAP-1: Altitude: Off");
            }
        }
        if (i == 29) {
            if (!this.bAPkrentang) {
                this.bAPkrentang = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "VUAP-1: Pitch + Roll: On");
            } else if (this.bAPkrentang) {
                this.bAPkrentang = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "VUAP-1: Pitch + Roll: Off");
            }
        }
    }

    private void checkAmmo() {
        this.missilesList.clear();
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] != null) {
                for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                    if (this.FM.CT.Weapons[i][j].haveBullets() && (this.FM.CT.Weapons[i][j] instanceof RocketGun9M114)) {
                        this.missilesList.add(this.FM.CT.Weapons[i][j]);
                    }
                }

            }
        }

    }

    private void doOperatorLaunchMissile() {
        this.victim.pos.getAbs(this.point3dop);
        TypeLaserSpotter.spot.set(this.point3dop);
        boolean isReady = (this.FM.getAOA() < (this.FM.Skill + 3)) && (this.FM.getOverload() < (this.FM.Skill + 1));
        if (this.missilesList.isEmpty()) {
            return;
        }
        if (Actor.isAlive(this.victim) && (Time.current() > this.missileLaunchInterval) && isReady) {
            this.guidedMissileUtils.update();
            this.missileLaunchInterval = Time.current() + 20000L + ((4 - this.FM.Skill) * 1000L);
            ((RocketGun9M114) this.missilesList.remove(0)).shots(1);
            HUD.training("Missile Gone!");
            return;
        } else {
            return;
        }
    }

    private void doLaunchMissileAI() {
        if (this.isAI && !this.missilesList.isEmpty() && (Time.current() > this.missileLaunchInterval) && ((((Maneuver) this.FM).get_maneuver() == 7) || (((Maneuver) this.FM).get_maneuver() == 43)) && (((Maneuver) this.FM).target_ground != null)) {
            ((RocketGun9M114) this.missilesList.remove(0)).shots(1);
            this.missileLaunchInterval = Time.current() + 20000L + ((4 - this.FM.Skill) * 1000L);
            Voice.speakAttackByRockets(this);
        }
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 3) {
            this.k14Mode = 0;
        }
        if (this.k14Mode == 0) {
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: Grid");
            }
        } else if (this.k14Mode == 1) {
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: S-8");
            }
        } else if (this.k14Mode == 2) {
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: S-13");
            }
        } else if ((this.k14Mode == 3) && (((Interpolate) (this.FM)).actor == World.getPlayerAircraft())) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-17V: Unguided Bomb");
        }
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset() {
    }

    public void typeFighterAceMakerAdjDistancePlus() {
    }

    public void typeFighterAceMakerAdjDistanceMinus() {
    }

    public void typeFighterAceMakerAdjSideslipReset() {
    }

    public void typeFighterAceMakerAdjSideslipPlus() {
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
    }

    public void typeFighterAceMakerRangeFinder() {
        if (this.k14Mode == 1) {
            Vector3d vTmp1 = new Vector3d();
            Point3d pTmp1 = new Point3d();
            Point3d pTmp2 = new Point3d();
            vTmp1.set(1.0D, 0.0D, 0.0D);
            this.FM.Or.transform(vTmp1);
            vTmp1.scale(6000D);
            pTmp1.set(this.FM.Loc);
            pTmp1.add(vTmp1);
            Engine.land();
            if (Landscape.rayHitHQ(this.FM.Loc, pTmp1, pTmp2)) {
                this.k14Distance = (float) this.pos.getAbsPoint().distance(pTmp2);
            }
        }
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte(this.k14Mode);
        netmsgguaranted.writeByte(this.k14WingspanType);
        netmsgguaranted.writeFloat(this.k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.k14Mode = netmsginput.readByte();
        this.k14WingspanType = netmsginput.readByte();
        this.k14Distance = netmsginput.readFloat();
    }

    public void update(float f) {
        this.typeFighterAceMakerRangeFinder();
        if (Time.current() < this.asTimer) {
            this.checkAirstart();
        }
        if (this.isAI) {
            this.stability(f);
        } else {
            this.human();
        }
        this.rotorSound();
        this.dustEmit();
        this.setMissileLaunchThreatActive();
        this.laserUpdate();
        this.tiltRotor(f);
        this.guidedMissileUtils.update();
        if ((this.obsMove < this.obsMoveTot) && !this.bObserverKilled && !this.FM.AS.isPilotParatrooper(1)) {
            if ((this.obsMove < 0.2F) || (this.obsMove > (this.obsMoveTot - 0.2F))) {
                this.obsMove += 0.3D * f;
            } else if ((this.obsMove < 0.1F) || (this.obsMove > (this.obsMoveTot - 0.1F))) {
                this.obsMove += 0.15F;
            } else {
                this.obsMove += 1.2D * f;
            }
            this.obsLookAzimuth = Aircraft.cvt(this.obsMove, 0.0F, this.obsMoveTot, this.obsAzimuthOld, this.obsAzimuth);
            this.obsLookElevation = Aircraft.cvt(this.obsMove, 0.0F, this.obsMoveTot, this.obsElevationOld, this.obsElevation);
            this.hierMesh().chunkSetAngles("Head2_D0", 0.0F, this.obsLookAzimuth, this.obsLookElevation);
        }
        super.update(f);
    }

    public void rareAction(float f, boolean flag) {
        if (this.aso2mode > 0) {
            this.setCommonThreatActive();
        }
        if (this.isAI && !this.FM.Gears.onGround()) {
            Pilot pilot = (Pilot) this.FM;
            if ((((Maneuver) this.FM).get_maneuver() == 7) || (((Maneuver) this.FM).get_maneuver() == 43) || (((Maneuver) this.FM).target_ground != null)) {
                this.aso2mode = 1;
            } else if (this.threatIsNear) {
                this.aso2mode = 2;
            } else if ((pilot != null) && this.isInPlayerWing() && (((pilot.Leader != null) && (pilot.Leader instanceof RealFlightModel)) || ((RealFlightModel) pilot.Leader).isRealMode())) {
                this.aso2mode = ((Mi24X) World.getPlayerAircraft()).aso2mode;
            }
        }
        this.OperatorLookout();
        super.rareAction(f, flag);
        if (this.FM.AS.bNavLightsOn) {
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            this.pos.getAbs(point3d, orient);
            Mi24X.l.set(point3d, orient);
            Eff3DActor eff3dactor = Eff3DActor.New(this, this.findHook("_RedLight"), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", 1.0F);
            eff3dactor.postDestroy(Time.current() + 500L);
            LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
            lightpointactor.light.setColor(1.0F, 0.3F, 0.3F);
            lightpointactor.light.setEmit(1.0F, 3F);
            eff3dactor.draw.lightMap().put("light", lightpointactor);
        }
        if (!this.bObserverKilled) {
            if (this.obsLookTime == 0) {
                this.obsLookTime = 2 + World.Rnd().nextInt(1, 3);
                this.obsMoveTot = 1.0F + (World.Rnd().nextFloat() * 1.5F);
                this.obsMove = 0.0F;
                this.obsAzimuthOld = this.obsAzimuth;
                this.obsElevationOld = this.obsElevation;
                if (World.Rnd().nextFloat() > 0.8D) {
                    this.obsAzimuth = 0.0F;
                    this.obsElevation = 0.0F;
                } else {
                    this.obsAzimuth = (World.Rnd().nextFloat() * 140F) - 70F;
                    this.obsElevation = (World.Rnd().nextFloat() * 50F) - 20F;
                }
            } else {
                this.obsLookTime--;
            }
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.checkAmmo();
        this.guidedMissileUtils.onAircraftLoaded();
        World.cur().diffCur.Engine_Overheat = false;
        if (((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) || !(this.FM instanceof Pilot)) {
            this.isAI = false;
        } else {
            this.isAI = true;
            this.repMod = 1;
            Squares squares = (Squares) Reflection.getValue(this.FM, "Sq");
            squares.squareWing = 66F;
            squares.squareAilerons = 1.0F;
            squares.squareFlaps = 0.81F;
            squares.liftStab = 5.2F;
            squares.squareElevators = 2.8F;
            squares.liftKeel = 0.1F;
            squares.squareRudders = 2.5F;
            squares.liftWingLIn = squares.liftWingRIn = 10F;
            squares.liftWingLMid = squares.liftWingRMid = 10F;
            squares.liftWingLOut = squares.liftWingROut = 10F;
            squares.dragFuselageCx = 0.06F;
            squares.dragAirbrakeCx = 1.0F;
            Vector3f eVect = new Vector3f();
            eVect.x = 1.0F;
            eVect.y = 0.0F;
            eVect.z = 0.0F;
            Point3d ePos = new Point3d();
            ePos.x = 1.5D;
            ePos.y = 0.0D;
            ePos.z = 0.0D;
            Point3d ePropPos = new Point3d();
            ePropPos.x = 1.0D;
            ePropPos.y = 0.0D;
            ePropPos.z = 0.0D;
            this.FM.EI.engines[0].setVector(eVect);
            this.FM.EI.engines[0].setPos(ePos);
            this.FM.EI.engines[0].setPropPos(ePropPos);
            Reflection.setFloat(this.FM.EI.engines[0], "tChangeSpeed", 1E-006F);
            Reflection.setFloat(this.FM.EI.engines[0], "thrustMax", 7000F);
            Reflection.setFloat(this.FM.EI.engines[0], "engineAcceleration", 1.0F);
            Reflection.setFloat(this.FM.EI.engines[0], "propReductor", 1.0F);
            this.FM.EI.engines[1].setVector(eVect);
            this.FM.EI.engines[1].setPos(ePos);
            this.FM.EI.engines[1].setPropPos(ePropPos);
            Reflection.setFloat(this.FM.EI.engines[1], "tChangeSpeed", 1E-006F);
            Reflection.setFloat(this.FM.EI.engines[1], "thrustMax", 7000F);
            Reflection.setFloat(this.FM.EI.engines[1], "engineAcceleration", 1.0F);
            Reflection.setFloat(this.FM.EI.engines[1], "propReductor", 1.0F);
            Polares polares = (Polares) Reflection.getValue(this.FM, "Wing");
            polares.AOA_crit = 10F;
            Reflection.setFloat(this.FM, "Vmin", 20F);
            Reflection.setFloat(this.FM, "Vmax", 280F);
            Reflection.setFloat(this.FM, "VmaxAllowed", 300F);
            Reflection.setFloat(this.FM, "VmaxH", 280F);
            Reflection.setFloat(this.FM, "HofVmax", 7900F);
            Reflection.setFloat(this.FM, "VminFLAPS", 20F);
            Reflection.setFloat(this.FM, "VmaxFLAPS", 280F);
            polares.Cy0_max = 7.15F;
            polares.FlapsMult = 1.0F;
            polares.FlapsAngSh = 4F;
            polares.lineCyCoeff = 0.19F;
            polares.AOAMinCx_Shift = 0.0F;
            polares.Cy0_0 = 0.55F;
            polares.AOACritH_0 = 10F;
            polares.AOACritL_0 = -6F;
            polares.CyCritH_0 = 2.0F;
            polares.CyCritL_0 = -0.7648107F;
            polares.parabCxCoeff_0 = 0.0005F;
            polares.CxMin_0 = 0.0125F;
            polares.Cy0_1 = 7.53F;
            polares.AOACritH_1 = 10F;
            polares.AOACritL_1 = -6F;
            polares.CyCritH_1 = 8.279165F;
            polares.CyCritL_1 = -0.7F;
            polares.CxMin_1 = 0.15F;
            polares.parabCxCoeff_1 = 7.5E-005F;
            polares.parabAngle = 5F;
            polares.declineCoeff = 0.008F;
            polares.maxDistAng = 40F;
            this.FM.setGCenter(0.0F);
            this.FM.CT.bHasBrakeControl = true;
            this.FM.CT.bHasAirBrakeControl = false;
        }
    }

    private Orientation        tOr;
    private Vector3d           tV[];
    private Point3d            tP[];
    private int                takeOffStep;
    private Eff3DActor         dustEff[];
    private Hook               hookDust[];
    private Loc                dustLoc;
    public boolean             threatIsNear;
    public int                 aso2mode;
    public int                 k14Mode;
    public int                 k14WingspanType;
    public float               k14Distance;
    private boolean            bAPazimut;
    private boolean            bAPalt;
    private boolean            bAPkrentang;
    private float              apALltPitch;
    private float              apAlt;
    private float              apKren;
    private float              apTang;
    private float              apAzimut;
    private long               missileLaunchInterval;
    private ArrayList          missilesList;
    public Actor               victim;
    private boolean            bManualFire;
    private boolean            bShotFlare;
    private int                repMod;
    private int                counter;
    private Vector3d           vector3dop;
    private Point3d            point3dop;
    private Point3d            localPoint3d1;
    private boolean            landing;
    private boolean            takeOff;
    private boolean            TailRotorDestroyed;
    public static Orient       LaserOr     = new Orient();
    public boolean             laserOn;
    public boolean             laserLock;
    public boolean             FLIR;
    public BulletEmitter       Weapons[][];
    public int                 rocketHookSelected;
    private static Loc         l           = new Loc();
    private int                obsLookTime;
    private float              obsLookAzimuth;
    private float              obsLookElevation;
    private float              obsAzimuth;
    private float              obsElevation;
    private float              obsAzimuthOld;
    private float              obsElevationOld;
    private float              obsMove;
    private float              obsMoveTot;
    boolean                    bObserverKilled;
    public static boolean      bChangedPit = false;
    public Loc                 suka;
    private GuidedMissileUtils guidedMissileUtils;
    public double              forceTrim_x;
    public double              forceTrim_y;
    public double              forceTrim_z;
    public boolean             getTrim;
    private Hook               LaserHook[];
    private static Loc         LaserLoc1   = new Loc();
    private static Point3d     LaserP1     = new Point3d();
    private static Point3d     LaserP2     = new Point3d();
    private static Point3d     LaserPL     = new Point3d();
    private boolean            hasChaff;
    private boolean            hasFlare;
    private long               lastChaffDeployed;
    private long               lastFlareDeployed;
    private long               lastCommonThreatActive;
    private long               intervalCommonThreat;
    private long               lastRadarLockThreatActive;
    private long               intervalRadarLockThreat;
    private long               lastMissileLaunchThreatActive;
    private long               intervalMissileLaunchThreat;
    private float              curAngleRotor;
    private float              diffAngleRotor;
    private float              curAngleTailRotor;
    private float              diffAngleTailRotor;
    private long               lastTimeFan;
    public float               aPitch;
    public float               aOldPitch;
    public double              engineRPM;
    public double              reductorRPM;
    public double              rotorRPM;
    public double              tailRotorRPM;
    protected SoundFX          sndProp;
    protected SoundFX          sndTrim;
    public boolean             isAI;
    private long               asTimer;
    private boolean            AltCheck;
    static {
        Property.set(Mi24X.class, "originCountry", PaintScheme.countryRussia);
    }
}
