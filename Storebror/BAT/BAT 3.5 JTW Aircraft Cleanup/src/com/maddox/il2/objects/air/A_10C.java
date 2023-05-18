package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.List;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.SoundFX;

public class A_10C extends A_10 implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeGuidedBombCarrier, TypeLaserDesignator {

    public A_10C() {
        this.bLaserOn = false;
        this.bLGBengaged = false;
        this.bHasPaveway = false;
        this.lLightLoc1 = new Loc();
        this.lLightP1 = new Point3d();
        this.lLightP2 = new Point3d();
        this.lLightPL = new Point3d();
        this.guidedMissileUtils = null;
        this.hasChaff = false;
        this.hasFlare = false;
        this.lastChaffDeployed = 0L;
        this.lastFlareDeployed = 0L;
        this.lastCommonThreatActive = 0L;
        this.intervalCommonThreat = 1000L;
        this.lastRadarLockThreatActive = 0L;
        this.intervalRadarLockThreat = 1000L;
        this.lastMissileLaunchThreatActive = 0L;
        this.intervalMissileLaunchThreat = 1000L;
        this.guidedMissileUtils = new GuidedMissileUtils(this);
        this.removeChuteTimer = -1L;
        this.engineSFX = null;
        this.engineSTimer = 0x98967f;
        this.Nvision = false;
        this.laserSpotPos = new Point3d();
        this.laserTimer = -1L;
        this.v = 0.0F;
        this.h = 0.0F;
        this.holdFollow = false;
        this.actorFollowing = null;
        this.hold = false;
        this.t1 = 0L;
        this.lockmode = 0;
        this.radarrange = 1;
        this.radartogle = false;
        this.bSightAutomation = false;
        this.bSightBombDump = false;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 3000F;
        this.fSightCurSpeed = 200F;
        this.fSightCurReadyness = 0.0F;
        this.FL = false;
        this.lLightHook = new Hook[4];
        this.lightTime = 0.0F;
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "A-10C_";
    }

    private static final float toMeters(float f) {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f) {
        return 0.4470401F * f;
    }

    public void auxPressed(int i) {
        super.auxPressed(i);
        if (i == 26) {
            if (this.hold && ((this.t1 + 200L) < Time.current()) && this.FLIR) {
                this.hold = false;
                HUD.log("Lazer Unlock");
                this.t1 = Time.current();
            }
            if (!this.hold && ((this.t1 + 200L) < Time.current()) && this.FLIR) {
                this.hold = true;
                HUD.log("Lazer Lock");
                this.t1 = Time.current();
            }
        }
        if (i == 27) {
            if (!this.ILS) {
                this.ILS = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ILS ON");
            } else {
                this.ILS = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ILS OFF");
            }
        }
        if (i == 28) {
            if (!this.FL) {
                this.FL = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "FL ON");
            } else {
                this.FL = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "FL OFF");
            }
        }
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
        if (this.FLIR) {
            this.azimult++;
            this.tf = Time.current();
        } else if (this.radartogle && (this.lockmode == 0)) {
            this.h += 0.0035F;
        }
    }

    public void typeBomberAdjDistanceMinus() {
        if (this.FLIR) {
            this.azimult--;
            this.tf = Time.current();
        } else if (this.radartogle && (this.lockmode == 0)) {
            this.h -= 0.0035F;
        }
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
        if (this.FLIR) {
            this.tangate++;
            this.tf = Time.current();
        } else if (this.radartogle && (this.lockmode == 0)) {
            this.v += 0.0035F;
        }
    }

    public void typeBomberAdjSideslipMinus() {
        if (this.FLIR) {
            this.tangate--;
            this.tf = Time.current();
        } else if (this.radartogle && (this.lockmode == 0)) {
            this.v -= 0.0035F;
        }
    }

    public void updatecontrollaser() {
        if ((this.tf + 5L) <= Time.current()) {
            this.tangate = 0.0F;
            this.azimult = 0.0F;
        }
        if (!this.FLIR && (this.laserTimer > 0L) && (Time.current() > this.laserTimer) && this.getLaserOn()) {
            this.setLaserOn(false);
        }
        if (this.bHasPaveway) {
            this.checkgroundlaser();
        }
    }

    private void checkgroundlaser() {
        boolean flag = false;
        float f5;
        for (f5 = 0.0F; this.getLaserOn();) {
            Point3d point3d = new Point3d();
            point3d = this.getLaserSpot();
            if ((Main.cur().clouds == null) || (Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) >= 1.0F)) {
                double d1 = this.pos.getAbsPoint().distance(point3d);
                if (d1 <= A_10C.maxPavewayDistance) {
                    float f1 = A_10C.angleBetween(this, point3d);
                    if (f1 <= A_10C.maxPavewayFOVfrom) {
                        flag = true;
                    }
                }
            }
            break;
        }

        if (!flag) {
            List list = Engine.targets();
            int i = list.size();
            for (int j = 0; j < i; j++) {
                Actor actor = (Actor) list.get(j);
                if ((actor instanceof TypeLaserDesignator) && ((TypeLaserDesignator) actor).getLaserOn() && (actor.getArmy() == this.getArmy())) {
                    Point3d point3d1 = new Point3d();
                    point3d1 = ((TypeLaserDesignator) actor).getLaserSpot();
                    if ((Main.cur().clouds == null) || (Main.cur().clouds.getVisibility(point3d1, this.pos.getAbsPoint()) >= 1.0F)) {
                        double d2 = this.pos.getAbsPoint().distance(point3d1);
                        if (d2 <= A_10C.maxPavewayDistance) {
                            float f2 = A_10C.angleBetween(this, point3d1);
                            if (f2 <= A_10C.maxPavewayFOVfrom) {
                                float f4 = 1.0F / f2 / (float) (d2 * d2);
                                if (f4 > f5) {
                                    f5 = f4;
                                    flag = true;
                                }
                            }
                        }
                    }
                }
            }

        }
        this.setLaserArmEngaged(flag);
    }

    private static float angleBetween(Actor actor, Point3d point3d) {
        float f = 180.1F;
        double d = 0.0D;
        Loc loc = new Loc();
        Point3d point3d1 = new Point3d();
        Vector3d vector3d = new Vector3d();
        Vector3d vector3d1 = new Vector3d();
        actor.pos.getAbs(loc);
        loc.get(point3d1);
        vector3d.sub(point3d, point3d1);
        d = vector3d.length();
        vector3d.scale(1.0D / d);
        vector3d1.set(1.0D, 0.0D, 0.0D);
        loc.transform(vector3d1);
        d = vector3d1.dot(vector3d);
        f = Geom.RAD2DEG((float) Math.acos(d));
        return f;
    }

    public Point3d getLaserSpot() {
        return this.laserSpotPos;
    }

    public boolean setLaserSpot(Point3d point3d) {
        this.laserSpotPos = point3d;
        return true;
    }

    public boolean getLaserOn() {
        return this.bLaserOn;
    }

    public boolean setLaserOn(boolean flag) {
        if (this.bLaserOn != flag) {
            if (!this.bLaserOn) {
                if (this.FM.actor == World.getPlayerAircraft()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: ON");
                }
            } else {
                if (this.FM.actor == World.getPlayerAircraft()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: OFF");
                }
                this.hold = false;
                this.holdFollow = false;
                this.actorFollowing = null;
            }
        }
        return this.bLaserOn = flag;
    }

    public boolean getLaserArmEngaged() {
        return this.bLGBengaged;
    }

    public boolean setLaserArmEngaged(boolean flag) {
        if (this.bLGBengaged != flag) {
            if (!this.bLGBengaged) {
                if (this == World.getPlayerAircraft()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser Bomb: Engaged");
                }
            } else if (this == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser Bomb: Disengaged");
            }
        }
        return this.bLGBengaged = flag;
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjAltitudePlus() {
        if (this.FLIR) {
            if (!this.APmode1) {
                this.APmode1 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Engaged");
                this.FM.AP.setStabAltitude(2000F);
            } else if (this.APmode1) {
                this.APmode1 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Released");
                this.FM.AP.setStabAltitude(false);
            }
        }
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberAdjSpeedPlus() {
        this.radarrange++;
        if (this.radarrange > 5) {
            this.radarrange = 5;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + this.radarrange);
    }

    public void typeBomberAdjSpeedMinus() {
        this.radarrange--;
        if (this.radarrange < 1) {
            this.radarrange = 1;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + this.radarrange);
    }

    public void typeBomberUpdate(float f) {
        if (Math.abs(this.FM.Or.getKren()) > 4.5D) {
            this.fSightCurReadyness -= 0.0666666F * f;
            if (this.fSightCurReadyness < 0.0F) {
                this.fSightCurReadyness = 0.0F;
            }
        }
        if (this.fSightCurReadyness < 1.0F) {
            this.fSightCurReadyness += 0.0333333F * f;
        } else if (this.bSightAutomation) {
            this.fSightCurDistance -= A_10C.toMetersPerSecond(this.fSightCurSpeed) * f;
            if (this.fSightCurDistance < 0.0F) {
                this.fSightCurDistance = 0.0F;
                this.typeBomberToggleAutomation();
            }
            this.fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(this.fSightCurDistance / A_10C.toMeters(this.fSightCurAltitude)));
            if (this.fSightCurDistance < (A_10C.toMetersPerSecond(this.fSightCurSpeed) * Math.sqrt(A_10C.toMeters(this.fSightCurAltitude) * (2F / 9.81F)))) {
                this.bSightBombDump = true;
            }
            if (this.bSightBombDump) {
                if (this.FM.isTick(3, 0)) {
                    if ((this.FM.CT.Weapons[3] != null) && (this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null) && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
                        this.FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else {
                    this.FM.CT.WeaponControl[3] = false;
                }
            }
        }
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte((this.bSightAutomation ? 1 : 0) | (this.bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(this.fSightCurDistance);
        netmsgguaranted.writeByte((int) this.fSightCurForwardAngle);
        netmsgguaranted.writeByte((int) ((this.fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeByte((int) (this.fSightCurSpeed / 2.5F));
        netmsgguaranted.writeByte((int) (this.fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        int i = netmsginput.readUnsignedByte();
        this.bSightAutomation = (i & 1) != 0;
        this.bSightBombDump = (i & 2) != 0;
        this.fSightCurDistance = netmsginput.readFloat();
        this.fSightCurForwardAngle = netmsginput.readUnsignedByte();
        this.fSightCurSideslip = -3F + (netmsginput.readUnsignedByte() / 33.33333F);
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readUnsignedByte() * 2.5F;
        this.fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
    }

    private void laser(Point3d point3d) {
        point3d.z = World.land().HQ(point3d.x, point3d.y);
        Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(point3d.x, point3d.y, point3d.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
        eff3dactor.postDestroy(Time.current() + 1500L);
    }

    private void FLIR() {
        List list = Engine.targets();
        int i = list.size();
        for (int j = 0; j < i; j++) {
            Actor actor = (Actor) list.get(j);
            if (((actor instanceof Aircraft) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric) || (actor instanceof TankGeneric)) && !(actor instanceof StationaryGeneric) && !(actor instanceof TypeLaserDesignator) && (actor.pos.getAbsPoint().distance(this.pos.getAbsPoint()) < 20000D)) {
                Point3d point3d = new Point3d();
                Orient orient = new Orient();
                actor.pos.getAbs(point3d, orient);
                Eff3DActor eff3dactor = Eff3DActor.New(actor, null, new Loc(), 1.0F, "effects/Explodes/Air/Zenitka/Germ_88mm/Glow.eff", 1.0F);
                eff3dactor.postDestroy(Time.current() + 1500L);
                LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
                lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
                if (actor instanceof Aircraft) {
                    lightpointactor.light.setEmit(8F, 50F);
                } else if (!(actor instanceof ArtilleryGeneric)) {
                    lightpointactor.light.setEmit(5F, 30F);
                } else {
                    lightpointactor.light.setEmit(3F, 10F);
                }
                eff3dactor.draw.lightMap().put("light", lightpointactor);
            }
        }

    }

    public void missionStarting() {
        super.missionStarting();
        this.laserTimer = -1L;
        this.bLaserOn = false;
        this.FLIR = false;
    }

    public boolean typeGuidedBombCisMasterAlive() {
        return this.isMasterAlive;
    }

    public void typeGuidedBombCsetMasterAlive(boolean flag) {
        this.isMasterAlive = flag;
    }

    public boolean typeGuidedBombCgetIsGuiding() {
        return this.isGuidingBomb;
    }

    public void typeGuidedBombCsetIsGuiding(boolean flag) {
        this.isGuidingBomb = flag;
    }

    public void updateLLights() {
        this.pos.getRender(Actor._tmpLoc);
        if (this.lLight == null) {
            if (Actor._tmpLoc.getX() >= 1.0D) {
                this.lLight = new LightPointWorld[4];
                for (int i = 0; i < 4; i++) {
                    this.lLight[i] = new LightPointWorld();
                    this.lLight[i].setColor(1.0F, 1.0F, 1.0F);
                    this.lLight[i].setEmit(0.0F, 0.0F);
                    try {
                        this.lLightHook[i] = new HookNamed(this, "_LandingLight0" + i);
                    } catch (Exception exception) {
                    }
                }

            }
        } else {
            for (int j = 0; j < 4; j++) {
                if (this.FM.AS.astateLandingLightEffects[j] != null) {
                    this.lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, this.lLightLoc1);
                    this.lLightLoc1.get(this.lLightP1);
                    this.lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    this.lLightHook[j].computePos(this, Actor._tmpLoc, this.lLightLoc1);
                    this.lLightLoc1.get(this.lLightP2);
                    Engine.land();
                    if (Landscape.rayHitHQ(this.lLightP1, this.lLightP2, this.lLightPL)) {
                        this.lLightPL.z++;
                        this.lLightP2.interpolate(this.lLightP1, this.lLightPL, 0.95F);
                        this.lLight[j].setPos(this.lLightP2);
                        float f = (float) this.lLightP1.distance(this.lLightPL);
                        float f1 = (f * 0.5F) + 60F;
                        float f2 = 0.7F - ((0.8F * f * this.lightTime) / 2000F);
                        this.lLight[j].setEmit(f2, f1);
                    } else {
                        this.lLight[j].setEmit(0.0F, 0.0F);
                    }
                } else if (this.lLight[j].getR() != 0.0F) {
                    this.lLight[j].setEmit(0.0F, 0.0F);
                }
            }

        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FLIR) {
            this.FLIR();
        }
        if (!this.FLIR) {
            this.FM.AP.setStabAltitude(false);
        }
    }

    public float getFuelReserve() {
        return A_10C.FuelReserve;
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 0.0F, 70F * f);
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
        long l = Time.current();
        if ((l - this.lastCommonThreatActive) > this.intervalCommonThreat) {
            this.lastCommonThreatActive = l;
            this.doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive() {
        long l = Time.current();
        if ((l - this.lastRadarLockThreatActive) > this.intervalRadarLockThreat) {
            this.lastRadarLockThreatActive = l;
            this.doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive() {
        long l = Time.current();
        if ((l - this.lastMissileLaunchThreatActive) > this.intervalMissileLaunchThreat) {
            this.lastMissileLaunchThreatActive = l;
            this.doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat() {
    }

    private void doDealRadarLockThreat() {
    }

    private void doDealMissileLaunchThreat() {
    }

    public static final double Rnd(double d, double d1) {
        return World.Rnd().nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1) {
        return World.Rnd().nextFloat(f, f1);
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this instanceof A_10C) {
            this.bHasLaser = true;
        }
        this.guidedMissileUtils.onAircraftLoaded();
        this.FM.Skill = 3;
        this.FM.CT.bHasDragChuteControl = true;
        this.bHasDeployedDragChute = false;
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        if (this.bHasLaser) {
            if (this.FLIR) {
                this.laser(this.getLaserSpot());
            }
            this.updatecontrollaser();
        }
        if (this.FM.CT.getArrestor() > 0.2F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f2 = (-33F * this.FM.Gears.arrestorVSink) / 57F;
                if ((f2 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f2 > 0.0F) && (this.FM.CT.getArrestor() < 0.95F)) {
                    f2 = 0.0F;
                }
                if (f2 > 0.2F) {
                    f2 = 0.2F;
                }
                if (f2 > 0.0F) {
                    this.arrestor = (0.7F * this.arrestor) + (0.3F * (this.arrestor + f2));
                } else {
                    this.arrestor = (0.3F * this.arrestor) + (0.7F * (this.arrestor + f2));
                }
                if (this.arrestor < 0.0F) {
                    this.arrestor = 0.0F;
                } else if (this.arrestor > 1.0F) {
                    this.arrestor = 1.0F;
                }
                this.moveArrestorHook(this.arrestor);
            }
        }
        super.update(f);
        if ((this.FM.CT.DragChuteControl > 0.0F) && !this.bHasDeployedDragChute) {
            this.chute = new Chute(this);
            this.chute.setMesh("3do/plane/ChuteF86/mono.sim");
            this.chute.collide(true);
            this.chute.mesh().setScale(0.8F);
            this.chute.pos.setRel(new Point3d(-8D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
            this.bHasDeployedDragChute = true;
        } else if (this.bHasDeployedDragChute && this.FM.CT.bHasDragChuteControl) {
            if (((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() > 600F)) || (this.FM.CT.DragChuteControl < 1.0F)) {
                if (this.chute != null) {
                    this.chute.tangleChute(this);
                    this.chute.pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 250L;
            } else if ((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() < 20F)) {
                if (this.chute != null) {
                    this.chute.tangleChute(this);
                }
                this.chute.pos.setRel(new Orient(0.0F, 100F, 0.0F));
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 10000L;
            }
        }
        if ((this.removeChuteTimer > 0L) && !this.FM.CT.bHasDragChuteControl && (Time.current() > this.removeChuteTimer)) {
            this.chute.destroy();
        }
        if (this.FM.getSpeed() > 5F) {
            this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 14.5F), 0.0F);
            this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -14.5F), 0.0F);
            this.hierMesh().chunkSetAngles("SlatL_Out", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -20.5F));
            this.hierMesh().chunkSetAngles("SlatR_Out", 0.0F, 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -20.5F));
        }
    }

    private GuidedMissileUtils guidedMissileUtils;
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
    public static float        FuelReserve        = 1500F;
    public boolean             bToFire;
    private boolean            bHasDeployedDragChute;
    private Chute              chute;
    private long               removeChuteTimer;
    private float              arrestor;
    protected SoundFX          engineSFX;
    protected int              engineSTimer;
    public boolean             bILS;
    public float               azimult;
    public float               tangate;
    public long                tf;
    private boolean            bLaserOn;
    public long                laserTimer;
    private Point3d            laserSpotPos;
    public float               v;
    public float               h;
    private boolean            bLGBengaged;
    public boolean             bHasPaveway;
    private static float       maxPavewayFOVfrom  = 45F;
    private static double      maxPavewayDistance = 20000D;
    public boolean             holdFollow;
    public Actor               actorFollowing;
    private boolean            bHasLaser;
    public boolean             Nvision;
    public boolean             hold;
    private long               t1;
    public int                 lockmode;
    public int                 radarrange;
    public boolean             radartogle;
    private boolean            bSightAutomation;
    private boolean            bSightBombDump;
    private float              fSightCurDistance;
    public float               fSightCurForwardAngle;
    public float               fSightCurSideslip;
    public float               fSightCurAltitude;
    public float               fSightCurSpeed;
    public float               fSightCurReadyness;
    public boolean             FLIR;
    public boolean             ILS;
    public boolean             FL;
    private boolean            isGuidingBomb;
    private boolean            isMasterAlive;
    private float              lightTime;
    private LightPointWorld    lLight[];
    private Hook               lLightHook[];
    private Loc                lLightLoc1;
    private Point3d            lLightP1;
    private Point3d            lLightP2;
    private Point3d            lLightPL;

    static {
        Class class1 = A_10C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-10C");
        Property.set(class1, "meshName", "3DO/Plane/A-10/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1966F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/A-10.fmd:A10FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitA_10.class, CockpitA_10Bombardier.class, CockpitA_10Laser.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 9, 9, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_Rock05", "_Rock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_Rock19", "_Rock20" });
    }
}
