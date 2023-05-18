package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.RocketGunFlare;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Su_25 extends Su_25X implements TypeX4Carrier, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeStormovikArmored, TypeLaserSpotter {

    public Su_25() {
        this.LaserHook = new Hook[4];
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
        this.intervalMissileLaunchThreat = 1000L;
        this.guidedMissileUtils = new GuidedMissileUtils(this);
        this.radarmode = 0;
        this.APmode1 = false;
        this.APmode2 = false;
        this.APmode3 = false;
        this.backfireList = new ArrayList();
        this.laserOn = false;
        this.laserLock = false;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.removeChuteTimer = -1L;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle += 0.2F;
        if (this.fSightCurForwardAngle > 6F) {
            this.fSightCurForwardAngle = 6F;
        }
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle -= 0.2F;
        if (this.fSightCurForwardAngle < -30F) {
            this.fSightCurForwardAngle = -30F;
        }
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip += 0.2F;
        if (this.fSightCurSideslip > 12F) {
            this.fSightCurSideslip = 12F;
        }
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip -= 0.2F;
        if (this.fSightCurSideslip < -12F) {
            this.fSightCurSideslip = -12F;
        }
    }

    public void laserUpdate() {
        if (!this.laserLock) {
            this.hierMesh().chunkSetAngles("LaserMsh_D0", -this.fSightCurForwardAngle, -this.fSightCurSideslip, 0.0F);
            this.pos.setUpdateEnable(true);
            this.pos.getRender(Actor._tmpLoc);
            this.LaserHook[1] = new HookNamed(this, "_Laser1");
            Su_25.LaserLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            this.LaserHook[1].computePos(this, Actor._tmpLoc, Su_25.LaserLoc1);
            Su_25.LaserLoc1.get(Su_25.LaserP1);
            Su_25.LaserLoc1.set(30000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            this.LaserHook[1].computePos(this, Actor._tmpLoc, Su_25.LaserLoc1);
            Su_25.LaserLoc1.get(Su_25.LaserP2);
            Engine.land();
            if (Landscape.rayHitHQ(Su_25.LaserP1, Su_25.LaserP2, Su_25.LaserPL)) {
                Su_25.LaserPL.z -= 0.95D;
                Su_25.LaserP2.interpolate(Su_25.LaserP1, Su_25.LaserPL, 1.0F);
                TypeLaserSpotter.spot.set(Su_25.LaserP2);
                Eff3DActor.New(null, null, new Loc(((Tuple3d) (Su_25.LaserP2)).x, ((Tuple3d) (Su_25.LaserP2)).y, Su_25.LaserP2.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
            }
        } else if (this.laserLock) {
            Su_25.LaserP3.x = Su_25.LaserP2.x + (-(this.fSightCurForwardAngle * 6F));
            Su_25.LaserP3.y = Su_25.LaserP2.y + (this.fSightCurSideslip * 6F);
            Su_25.LaserP3.z = Su_25.LaserP2.z;
            TypeLaserSpotter.spot.set(Su_25.LaserP3);
            Eff3DActor.New(null, null, new Loc(((Tuple3d) (Su_25.LaserP3)).x, ((Tuple3d) (Su_25.LaserP3)).y, Su_25.LaserP3.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
        }
    }

    private void checkAmmo() {
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] != null) {
                for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                    if (this.FM.CT.Weapons[i][j].haveBullets() && (this.FM.CT.Weapons[i][j] instanceof RocketGunFlare)) {
                        this.backfireList.add(this.FM.CT.Weapons[i][j]);
                    }
                }

            }
        }

    }

    public void backFire() {
        if (this.backfireList.isEmpty()) {
            return;
        } else {
            ((RocketGunFlare) this.backfireList.remove(0)).shots(3);
            return;
        }
    }

    public void auxPressed(int i) {
        super.auxPressed(i);
        if (i == 20) {
            if (!this.APmode1) {
                this.APmode1 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude ON");
                this.FM.AP.setStabAltitude(1000F);
            } else if (this.APmode1) {
                this.APmode1 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude OFF");
                this.FM.AP.setStabAltitude(false);
            }
        }
        if (i == 21) {
            if (!this.APmode2) {
                this.APmode2 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction ON");
                this.FM.AP.setStabDirection(true);
                this.FM.CT.bHasRudderControl = false;
            } else if (this.APmode2) {
                this.APmode2 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction OFF");
                this.FM.AP.setStabDirection(false);
                this.FM.CT.bHasRudderControl = true;
            }
        }
        if (i == 22) {
            if (!this.APmode3) {
                this.APmode3 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route ON");
                this.FM.AP.setWayPoint(true);
            } else if (this.APmode3) {
                this.APmode3 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route OFF");
                this.FM.AP.setWayPoint(false);
                this.FM.CT.AileronControl = 0.0F;
                this.FM.CT.ElevatorControl = 0.0F;
                this.FM.CT.RudderControl = 0.0F;
            }
        }
        if (i == 23) {
            this.FM.CT.AileronControl = 0.0F;
            this.FM.CT.ElevatorControl = 0.0F;
            this.FM.CT.RudderControl = 0.0F;
            this.FM.AP.setWayPoint(false);
            this.FM.AP.setStabDirection(false);
            this.FM.AP.setStabAltitude(false);
            this.APmode1 = false;
            this.APmode2 = false;
            this.APmode3 = false;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: All Off");
        }
        if (i == 24) {
            if (!this.laserOn) {
                this.laserOn = true;
                this.laserLock = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: On");
                this.fSightCurSideslip = 0.0F;
                this.fSightCurForwardAngle = 0.0F;
            } else if (this.laserOn) {
                this.laserOn = false;
                this.laserLock = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: Off");
                this.fSightCurSideslip = 0.0F;
                this.fSightCurForwardAngle = 0.0F;
            }
        }
        if (i == 25) {
            if (!this.laserLock) {
                this.laserLock = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: Locked");
                this.fSightCurSideslip = 0.0F;
                this.fSightCurForwardAngle = 0.0F;
            } else if (this.laserLock) {
                this.laserLock = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: Unlocked");
                this.fSightCurSideslip = 0.0F;
                this.fSightCurForwardAngle = 0.0F;
            }
        }
    }

    public boolean typeRadarToggleMode() {
        return true;
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 0.1F;
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -0.1F;
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 0.1F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -0.1F;
    }

    public void typeX4CResetControls() {
        this.deltaAzimuth = this.deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
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

    public void onAircraftLoaded() {
        this.checkAmmo();
        super.onAircraftLoaded();
        this.FM.CT.bHasBombSelect = true;
        this.bHasSK1Seat = false;
        this.FM.CT.bHasDragChuteControl = true;
        this.bHasDeployedDragChute = false;
        this.guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f) {
        if (this.laserOn) {
            this.laserUpdate();
        }
        this.guidedMissileUtils.update();
        super.update(f);
        if ((this.FM.CT.DragChuteControl > 0.0F) && !this.bHasDeployedDragChute) {
            this.chute1 = new Chute(this);
            this.chute2 = new Chute(this);
            this.chute1.setMesh("3do/plane/ChuteSu_25/mono.sim");
            this.chute2.setMesh("3do/plane/ChuteSu_25/mono.sim");
            this.chute1.mesh().setScale(0.5F);
            this.chute2.mesh().setScale(0.5F);
            this.chute1.pos.setRel(new Point3d(-8D, 0.0D, 0.6D), new Orient(20F, 90F, 0.0F));
            this.chute2.pos.setRel(new Point3d(-8D, 0.0D, 0.6D), new Orient(-20F, 90F, 0.0F));
            this.bHasDeployedDragChute = true;
        } else if (this.bHasDeployedDragChute && this.FM.CT.bHasDragChuteControl) {
            if (((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() > 600F)) || (this.FM.CT.DragChuteControl < 1.0F)) {
                if ((this.chute1 != null) && (this.chute2 != null)) {
                    this.chute1.tangleChute(this);
                    this.chute2.tangleChute(this);
                    this.chute1.pos.setRel(new Point3d(-15D, 0.0D, 1.0D), new Orient(20F, 90F, 0.0F));
                    this.chute2.pos.setRel(new Point3d(-15D, 0.0D, 1.0D), new Orient(-20F, 90F, 0.0F));
                }
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 250L;
            } else if ((this.FM.CT.DragChuteControl == 1.0F) && (this.FM.getSpeedKMH() < 20F)) {
                if ((this.chute1 != null) && (this.chute2 != null)) {
                    this.chute1.tangleChute(this);
                    this.chute2.tangleChute(this);
                }
                this.chute1.pos.setRel(new Orient(10F, 100F, 0.0F));
                this.chute2.pos.setRel(new Orient(-10F, 100F, 0.0F));
                this.FM.CT.DragChuteControl = 0.0F;
                this.FM.CT.bHasDragChuteControl = false;
                this.FM.Sq.dragChuteCx = 0.0F;
                this.removeChuteTimer = Time.current() + 10000L;
            }
        }
        if ((this.removeChuteTimer > 0L) && !this.FM.CT.bHasDragChuteControl && (Time.current() > this.removeChuteTimer)) {
            this.chute1.destroy();
            this.chute2.destroy();
        }
        this.guidedMissileUtils.update();
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 60F * f, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 40D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(10, loc, vector3d, aircraft);
                }
            }

        };
        this.hierMesh().chunkVisible("Seat1_D0", false);
        this.FM.setTakenMortalDamage(true, null);
        this.FM.CT.WeaponControl[0] = false;
        this.FM.CT.WeaponControl[1] = false;
        this.FM.CT.bHasAileronControl = false;
        this.FM.CT.bHasRudderControl = false;
        this.FM.CT.bHasElevatorControl = false;
    }

    private static Point3d     LaserP3     = new Point3d();
    public boolean             laserOn;
    public boolean             laserLock;
    public static Orient       tmpOr       = new Orient();
    private Hook               LaserHook[];
    private static Loc         LaserLoc1   = new Loc();
    private static Point3d     LaserP1     = new Point3d();
    private static Point3d     LaserP2     = new Point3d();
    private static Point3d     LaserPL     = new Point3d();
    private float              deltaAzimuth;
    private float              deltaTangage;
    private ArrayList          backfireList;
    public boolean             APmode1;
    public boolean             APmode2;
    public boolean             APmode3;
    public int                 radarmode;
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
    private boolean            bHasDeployedDragChute;
    private Chute              chute1;
    private Chute              chute2;
    private long               removeChuteTimer;
    public static boolean      bChangedPit = false;
    public boolean             bToFire;

    static {
        Class class1 = Su_25.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Su-25");
        Property.set(class1, "meshName", "3DO/Plane/Su-25/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944.9F);
        Property.set(class1, "yearExpired", 1948.3F);
        Property.set(class1, "FlightModel", "FlightModels/SU-25.fmd:SU25FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSu_25.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 0, 0, 0, 0, 2, 2, 2, 2, 7, 7, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02", "_ExternalRock04", "_ExternalRock03", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalDev11", "_ExternalDev12", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_ExternalRock09", "_ExternalRock10", "_ExternalRock12", "_ExternalRock11", "_Flare01", "_Flare02", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalDev13", "_ExternalDev14", "_ExternalRock13", "_ExternalRock14",
                "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalDev15", "_ExternalDev16", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalDev17", "_ExternalDev18", "_ExternalDev19", "_ExternalDev20" });
    }
}
