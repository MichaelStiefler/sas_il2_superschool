package com.maddox.il2.objects.air;

import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.BombGunCBU24_gn16;
import com.maddox.il2.objects.weapons.BombGunGBU10_Mk84LGB_gn16;
import com.maddox.il2.objects.weapons.BombGunGBU12_Mk82LGB_gn16;
import com.maddox.il2.objects.weapons.BombGunGBU16_Mk83LGB_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawk400gal_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawkNF_gn16;
import com.maddox.il2.objects.weapons.FuelTankGun_TankSkyhawk_gn16;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.Pylon_LAU10_Cap_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU10_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU118_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU130_Cap_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU131_Cap_gn16;
import com.maddox.il2.objects.weapons.Pylon_LAU7_gn16;
import com.maddox.il2.objects.weapons.Pylon_Mk4HIPEGpod_gn16;
import com.maddox.il2.objects.weapons.Pylon_USMERfw_gn16;
import com.maddox.il2.objects.weapons.Pylon_USMERmd_gn16;
import com.maddox.il2.objects.weapons.Pylon_USTER_gn16;
import com.maddox.il2.objects.weapons.RocketGunChaff_gn16;
import com.maddox.il2.objects.weapons.RocketGunFlare_gn16;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class SkyhawkA4M extends SkyhawkFuelReceiver implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeLaserDesignator {

    public SkyhawkA4M() {
        this.LaserLoc1 = new Loc();
        this.LaserP1 = new Point3d();
        this.LaserP2 = new Point3d();
        this.LaserPL = new Point3d();
        this.bLaserOn = false;
        this.bLGBengaged = false;
        this.bHasPaveway = false;
        this.tLastLaserUpdate = -1L;
        this.tLastLGBcheck = -1L;
        this.bChangedPit = false;
        this.guidedMissileUtils = null;
        this.hasChaff = false;
        this.hasFlare = false;
        this.lastChaffDeployed = 0L;
        this.lastFlareDeployed = 0L;
        this.counterFlareList = new ArrayList();
        this.counterChaffList = new ArrayList();
        this.guidedMissileUtils = new GuidedMissileUtils(this);
        this.bHasLAUcaps = false;
        this.removeChuteTimer = -1L;
        this.tLastLaserLockKeyInput = 0L;
        this.tangateLaserHead = 0;
        this.azimultLaserHead = 0;
        this.tangateLaserHeadOffset = 0;
        this.azimultLaserHeadOffset = 0;
        this.laserSpotPos = new Point3d();
        this.laserSpotPosSaveHold = new Point3d();
    }

    public void auxPressed(int i) {
        super.auxPressed(i);
        if ((i == 26) && this.getLaserOn()) {
            if (this.holdLaser && ((this.tLastLaserLockKeyInput + 200L) < Time.current())) {
                this.holdLaser = false;
                this.holdFollowLaser = false;
                this.actorFollowing = null;
                this.tangateLaserHeadOffset = 0;
                this.azimultLaserHeadOffset = 0;
                HUD.log("Laser Pos Unlock");
                this.tLastLaserLockKeyInput = Time.current();
            }
            if (!this.holdLaser && ((this.tLastLaserLockKeyInput + 200L) < Time.current())) {
                this.holdLaser = true;
                this.holdFollowLaser = false;
                this.actorFollowing = null;
                this.laserSpotPosSaveHold.set(this.getLaserSpot());
                HUD.log("Laser Pos Lock");
                this.tLastLaserLockKeyInput = Time.current();
            }
        }
        if (i == 29) {
            this.setLaserOn(!this.bLaserOn);
        }
    }

    public void typeBomberAdjAltitudePlus() {
        if (this.bLaserOn) {
            if (this.holdLaser) {
                this.tangateLaserHeadOffset = 4;
            } else {
                this.tangateLaserHead += 5;
                if (this.tangateLaserHead > 500) {
                    this.tangateLaserHead = 500;
                }
            }
        }
    }

    public void typeBomberAdjAltitudeMinus() {
        if (this.bLaserOn) {
            if (this.holdLaser) {
                this.tangateLaserHeadOffset = -4;
            } else {
                this.tangateLaserHead -= 5;
                if (this.tangateLaserHead < -1000) {
                    this.tangateLaserHead = -1000;
                }
            }
        }
    }

    public void typeBomberAdjSideslipPlus() {
        if (this.bLaserOn) {
            if (this.holdLaser) {
                this.azimultLaserHeadOffset = 4;
            } else {
                this.azimultLaserHead += 5;
                if (this.azimultLaserHead > 1000) {
                    this.azimultLaserHead = 1000;
                }
            }
        }
    }

    public void typeBomberAdjSideslipMinus() {
        if (this.bLaserOn) {
            if (this.holdLaser) {
                this.azimultLaserHeadOffset = -4;
            } else {
                this.azimultLaserHead -= 5;
                if (this.azimultLaserHead < -1000) {
                    this.azimultLaserHead = -1000;
                }
            }
        }
    }

    public void laserUpdate() {
        if (Time.current() == this.tLastLaserUpdate) {
            return;
        }
        this.tLastLaserUpdate = Time.current();
        Orient orient = new Orient();
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d, orient);
        float f = orient.getRoll();
        if (f > 180F) {
            f -= 360F;
        }
        if (f < -180F) {
            f += 360F;
        }
        float f1 = Aircraft.cvt(f, -50F, 50F, 50F, -50F);
        if (this.holdLaser) {
            float f2 = orient.getPitch();
            float f3 = 0.0F;
            if (f2 > 90F) {
                f3 = f2 - 360F;
            } else {
                f3 = f2;
            }
            Point3d point3d1 = new Point3d();
            point3d1.set(this.laserSpotPosSaveHold);
            point3d1.sub(point3d);
            double d = point3d1.x;
            double d1 = point3d1.y;
            double d2 = point3d1.z;
            double d3 = Math.abs(Math.sqrt((d * d) + (d1 * d1)));
            float f4 = (float) Math.toDegrees(Math.atan(d2 / d3)) - f3;
            float f5 = 0.0F;
            if (d > 0.0D) {
                f5 = (float) Math.toDegrees(Math.atan(d1 / d)) - orient.getYaw();
            } else {
                f5 = (180F + (float) Math.toDegrees(Math.atan(d1 / d))) - orient.getYaw();
            }
            if (f5 > 180F) {
                f5 -= 360F;
            }
            if (f5 < -180F) {
                f5 += 360F;
            }
            this.azimultLaserHead = (int) (f5 / 0.03F) + this.azimultLaserHeadOffset;
            if (this.azimultLaserHead > 1000) {
                this.azimultLaserHead = 1000;
            }
            if (this.azimultLaserHead < -1000) {
                this.azimultLaserHead = -1000;
            }
            this.tangateLaserHead = (int) (f4 / 0.03F) + this.tangateLaserHeadOffset;
            if (this.tangateLaserHead > 500) {
                this.tangateLaserHead = 500;
            }
            if (this.tangateLaserHead < -1000) {
                this.tangateLaserHead = -1000;
            }
        }
        this.hierMesh().chunkSetAngles("LaserMshRoll_D0", 0.0F, 0.0F, f1);
        this.hierMesh().chunkSetAngles("LaserMsh_D0", -(float) this.tangateLaserHead * 0.03F, -(float) this.azimultLaserHead * 0.03F, 0.0F);
        this.pos.setUpdateEnable(true);
        this.pos.getRender(Actor._tmpLoc);
        this.LaserHook = new HookNamed(this, "_Laser1");
        this.LaserLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        this.LaserHook.computePos(this, Actor._tmpLoc, this.LaserLoc1);
        this.LaserLoc1.get(this.LaserP1);
        this.LaserLoc1.set(30000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        this.LaserHook.computePos(this, Actor._tmpLoc, this.LaserLoc1);
        this.LaserLoc1.get(this.LaserP2);
        if (Landscape.rayHitHQ(this.LaserP1, this.LaserP2, this.LaserPL)) {
            this.LaserPL.z -= 0.95D;
            this.LaserP2.interpolate(this.LaserP1, this.LaserPL, 1.0F);
            this.setLaserSpot(this.LaserP2);
            Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(this.LaserP2.x, this.LaserP2.y, this.LaserP2.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
            eff3dactor.postDestroy(Time.current() + 1500L);
        }
        if ((this.azimultLaserHeadOffset != 0) || (this.tangateLaserHeadOffset != 0)) {
            this.azimultLaserHeadOffset = 0;
            this.tangateLaserHeadOffset = 0;
            this.laserSpotPosSaveHold.set(this.LaserP2);
        }
    }

    private void checkgroundlaser() {
        boolean flag = false;
        float f5 = 0.0F;
        if (this.getLaserOn()) {
            Point3d point3d = new Point3d();
            point3d = this.getLaserSpot();
            if ((Main.cur().clouds == null) || (Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) >= 1.0F)) {
                double d1 = this.pos.getAbsPoint().distance(point3d);
                if (d1 <= SkyhawkA4M.maxPavewayDistance) {
                    float f1 = SkyhawkA4M.angleBetween(this, point3d);
                    if (f1 <= SkyhawkA4M.maxPavewayFOVfrom) {
                        flag = true;
                    }
                }
            }
        }
        if (!flag) {
            List list = Engine.targets();
            int i = list.size();
            for (int j = 0; j < i; j++) {
                Actor actor = (Actor) list.get(j);
                if (!(actor instanceof TypeLaserDesignator) || !((TypeLaserDesignator) actor).getLaserOn() || (actor.getArmy() != this.getArmy())) {
                    continue;
                }
                Point3d point3d1 = new Point3d();
                point3d1 = ((TypeLaserDesignator) actor).getLaserSpot();
                if ((Main.cur().clouds != null) && (Main.cur().clouds.getVisibility(point3d1, this.pos.getAbsPoint()) < 1.0F)) {
                    continue;
                }
                double d2 = this.pos.getAbsPoint().distance(point3d1);
                if (d2 > SkyhawkA4M.maxPavewayDistance) {
                    continue;
                }
                float f2 = SkyhawkA4M.angleBetween(this, point3d1);
                if (f2 > SkyhawkA4M.maxPavewayFOVfrom) {
                    continue;
                }
                float f4 = 1.0F / f2 / (float) (d2 * d2);
                if (f4 > f5) {
                    f5 = f4;
                    flag = true;
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
        this.laserSpotPos.set(point3d);
        return true;
    }

    public boolean getLaserOn() {
        return this.bLaserOn;
    }

    public boolean setLaserOn(boolean flag) {
        if (this.bLaserOn != flag) {
            if (!this.bLaserOn) {
                if (this == World.getPlayerAircraft()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: ON");
                }
                this.holdLaser = false;
                this.holdFollowLaser = false;
                this.actorFollowing = null;
                this.tangateLaserHead = 0;
                this.azimultLaserHead = 0;
                this.tangateLaserHeadOffset = 0;
                this.azimultLaserHeadOffset = 0;
            } else {
                if (this == World.getPlayerAircraft()) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: OFF");
                }
                this.holdLaser = false;
                this.holdFollowLaser = false;
                this.actorFollowing = null;
                this.tangateLaserHead = 0;
                this.azimultLaserHead = 0;
                this.tangateLaserHeadOffset = 0;
                this.azimultLaserHeadOffset = 0;
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

    private void checkChangeWeaponColors() {
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] == null) {
                continue;
            }
            for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                if (this.FM.CT.Weapons[i][j] instanceof Pylon_USTER_gn16) {
                    ((Pylon_USTER_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_USMERfw_gn16) {
                    ((Pylon_USMERfw_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_USMERmd_gn16) {
                    ((Pylon_USMERmd_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU10_gn16) {
                    ((Pylon_LAU10_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16) {
                    ((Pylon_LAU10_Cap_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU7_gn16) {
                    ((Pylon_LAU7_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU118_gn16) {
                    ((Pylon_LAU118_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                } else if (this.FM.CT.Weapons[i][j] instanceof BombGunCBU24_gn16) {
                    ((BombGunCBU24_gn16) this.FM.CT.Weapons[i][j]).matGray();
                } else if (this.FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk_gn16) {
                    ((FuelTankGun_TankSkyhawk_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                } else if (this.FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkNF_gn16) {
                    ((FuelTankGun_TankSkyhawkNF_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                } else if (this.FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk400gal_gn16) {
                    ((FuelTankGun_TankSkyhawk400gal_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_Mk4HIPEGpod_gn16) {
                    ((Pylon_Mk4HIPEGpod_gn16) this.FM.CT.Weapons[i][j]).matHighvis();
                }
                if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16) {
                    this.bHasLAUcaps = true;
                } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16) {
                    this.bHasLAUcaps = true;
                } else if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16) {
                    this.bHasLAUcaps = true;
                }
                if (this.FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16) {
                    this.counterFlareList.add(this.FM.CT.Weapons[i][j]);
                    continue;
                }
                if (this.FM.CT.Weapons[i][j] instanceof RocketGunChaff_gn16) {
                    this.counterChaffList.add(this.FM.CT.Weapons[i][j]);
                    continue;
                }
                if ((this.FM.CT.Weapons[i][j] instanceof BombGunGBU10_Mk84LGB_gn16) || (this.FM.CT.Weapons[i][j] instanceof BombGunGBU12_Mk82LGB_gn16) || (this.FM.CT.Weapons[i][j] instanceof BombGunGBU16_Mk83LGB_gn16)) {
                    this.bHasPaveway = true;
                    this.FM.bNoDiveBombing = true;
                }
            }

        }

    }

    private void checkDeleteLAUcaps() {
        if (this.FM.CT.saveWeaponControl[2]) {
            for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
                if (this.FM.CT.Weapons[i] == null) {
                    continue;
                }
                for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                    if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16) {
                        ((Pylon_LAU10_Cap_gn16) this.FM.CT.Weapons[i][j]).jettisonCap();
                        continue;
                    }
                    if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16) {
                        ((Pylon_LAU130_Cap_gn16) this.FM.CT.Weapons[i][j]).jettisonCap();
                        continue;
                    }
                    if (this.FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16) {
                        ((Pylon_LAU131_Cap_gn16) this.FM.CT.Weapons[i][j]).jettisonCap();
                    }
                }

            }

            this.bHasLAUcaps = false;
        }
    }

    public void backFire() {
        if (this.counterFlareList.isEmpty()) {
            this.hasFlare = false;
        } else if (Time.current() > (this.lastFlareDeployed + 700L)) {
            ((RocketGunFlare_gn16) this.counterFlareList.get(0)).shots(1);
            this.hasFlare = true;
            this.lastFlareDeployed = Time.current();
            if (!((RocketGunFlare_gn16) this.counterFlareList.get(0)).haveBullets()) {
                this.counterFlareList.remove(0);
            }
        }
        if (this.counterChaffList.isEmpty()) {
            this.hasChaff = false;
        } else if (Time.current() > (this.lastChaffDeployed + 1300L)) {
            ((RocketGunChaff_gn16) this.counterChaffList.get(0)).shots(1);
            this.hasChaff = true;
            this.lastChaffDeployed = Time.current();
            if (!((RocketGunChaff_gn16) this.counterChaffList.get(0)).haveBullets()) {
                this.counterChaffList.remove(0);
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

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
        this.FM.Sq.dragChuteCx = 3.2F;
        this.bHasDeployedDragChute = false;
        this.FM.CT.bHasBombSelect = true;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.bHasPaveway && this.FM.bNoDiveBombing && ((Time.current() - this.tLastLGBcheck) > 30000L) && (this.FM instanceof Maneuver)) {
            if (!((Maneuver) this.FM).hasBombs()) {
                this.bHasPaveway = false;
                this.FM.bNoDiveBombing = false;
            }
            this.tLastLGBcheck = Time.current();
        }
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void update(float f) {
        if (this.bHasLAUcaps) {
            this.checkDeleteLAUcaps();
        }
        super.update(f);
        this.guidedMissileUtils.update();
        if (this.backfire) {
            this.backFire();
        }
        if (this.bLaserOn) {
            this.laserUpdate();
        }
        if (this.bHasPaveway) {
            this.checkgroundlaser();
        }
        this.updateDragChute();
        if ((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) && (this.FM instanceof Maneuver) && this.bHasPaveway && (this.FM.AP.way.curr().Action == 3) && ((Maneuver) this.FM).hasBombs() && this.getLaserArmEngaged()) {
            ((Maneuver) this.FM).bombsOut = true;
            this.FM.CT.WeaponControl[3] = true;
            Voice.speakAttackByBombs(this);
        }
    }

    public void missionStarting() {
        super.missionStarting();
        this.checkChangeWeaponColors();
        this.bLaserOn = false;
        this.tLastLaserLockKeyInput = 0L;
        this.tangateLaserHead = 0;
        this.azimultLaserHead = 0;
        this.tangateLaserHeadOffset = 0;
        this.azimultLaserHeadOffset = 0;
        this.tLastLaserUpdate = -1L;
    }

    private void updateDragChute() {
        if ((this.FM.CT.DragChuteControl > 0.0F) && !this.bHasDeployedDragChute) {
            this.chute = new Chute(this);
            this.chute.setMesh("3do/plane/ChuteA4M_US/mono.sim");
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
    }

    public boolean             bChangedPit;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean            hasChaff;
    private boolean            hasFlare;
    private long               lastChaffDeployed;
    private long               lastFlareDeployed;
    private ArrayList          counterFlareList;
    private ArrayList          counterChaffList;
    private boolean            bHasLAUcaps;
    private boolean            bHasDeployedDragChute;
    private Chute              chute;
    private long               removeChuteTimer;
    private Hook               LaserHook;
    private Loc                LaserLoc1;
    private Point3d            LaserP1;
    private Point3d            LaserP2;
    private Point3d            LaserPL;
    private long               tLastLaserLockKeyInput;
    public int                 azimultLaserHead;
    public int                 tangateLaserHead;
    public int                 azimultLaserHeadOffset;
    public int                 tangateLaserHeadOffset;
    public boolean             holdLaser;
    public boolean             holdFollowLaser;
    public Actor               actorFollowing;
    private Point3d            laserSpotPos;
    private Point3d            laserSpotPosSaveHold;
    private boolean            bLaserOn;
    private boolean            bLGBengaged;
    public boolean             bHasPaveway;
    private long               tLastLaserUpdate;
    private static float       maxPavewayFOVfrom  = 45F;
    private static double      maxPavewayDistance = 20000D;
    private long               tLastLGBcheck;

    static {
        Class class1 = SkyhawkA4M.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Skyhawk");
        Property.set(class1, "meshName", "3DO/Plane/SkyhawkA4M(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/a4m.fmd:SKYHAWKS");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSkyhawkA4F.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 7, 7, 8 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb01", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_Bomb13", "_ExternalBomb14", "_Bomb15", "_ExternalBomb16", "_Bomb17", "_ExternalBomb18", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_Rock05",
                "_Rock06", "_Rock07", "_Rock08", "_Rock09", "_Rock10", "_Rock11", "_Rock12", "_Rock13", "_Rock14", "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22", "_Rock23", "_Rock24", "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_ExternalRock43", "_ExternalRock43", "_ExternalRock29", "_ExternalRock29", "_ExternalRock30", "_ExternalRock30", "_ExternalRock31", "_ExternalRock31", "_ExternalRock32", "_ExternalRock32", "_ExternalRock33", "_ExternalRock33", "_ExternalRock34", "_ExternalRock34", "_ExternalRock35", "_ExternalRock35", "_ExternalRock36", "_ExternalRock36", "_ExternalRock37", "_ExternalRock37", "_ExternalRock38", "_ExternalRock38", "_ExternalRock39", "_ExternalRock39", "_ExternalRock40", "_ExternalRock40", "_Rock41", "_Rock42", "_Bomb19", "_Bomb20", "_ExternalBomb21", "_ExternalBomb22", "_Bomb23", "_ExternalBomb24", "_ExternalBomb25", "_Bomb26", "_ExternalBomb27", "_ExternalBomb28", "_Flare01", "_Flare02", "_Chaff01" });
    }
}
