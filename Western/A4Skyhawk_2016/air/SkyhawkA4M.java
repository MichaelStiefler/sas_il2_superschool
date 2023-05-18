
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import com.maddox.sas1946.il2.util.Reflection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.air:
//            Skyhawk, TypeTankerDrogue, TypeDockable, Aircraft, 
//            PaintSchemeFMPar05, TypeGuidedMissileCarrier, TypeCountermeasure, 
//            NetAircraft, Cockpit, TypeGSuit

public class SkyhawkA4M extends SkyhawkFuelReceiver
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeLaserDesignator
{

    public SkyhawkA4M()
    {
        bChangedPit = false;
        guidedMissileUtils = null;
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        counterFlareList = new ArrayList();
        counterChaffList = new ArrayList();
        guidedMissileUtils = new GuidedMissileUtils(this);
        bHasLAUcaps = false;
        removeChuteTimer = -1L;
        tLastLaserLockKeyInput = 0L;
        tangateLaserHead = 0;
        azimultLaserHead = 0;
        tangateLaserHeadOffset = 0;
        azimultLaserHeadOffset = 0;
        laserSpotPos = new Point3d();
        laserSpotPosSaveHold = new Point3d();
        lastUpdateTime = -1L;
        lastRareActionTime = -1L;
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 26 && getLaserOn())
        {
            if(holdLaser && tLastLaserLockKeyInput + 200L < Time.current())
            {
                holdLaser = false;
                holdFollowLaser = false;
                actorFollowing = null;
                tangateLaserHeadOffset = 0;
                azimultLaserHeadOffset = 0;
                HUD.log("Laser Pos Unlock");
                tLastLaserLockKeyInput = Time.current();
            }
            if(!holdLaser && tLastLaserLockKeyInput + 200L < Time.current())
            {
                holdLaser = true;
                holdFollowLaser = false;
                actorFollowing = null;
                laserSpotPosSaveHold.set(getLaserSpot());
                HUD.log("Laser Pos Lock");
                tLastLaserLockKeyInput = Time.current();
            }
        }
        if(i == 29)
        {
            setLaserOn(!bLaserOn);
        }
    }

    public void typeBomberAdjAltitudePlus()
    {
        if(bLaserOn)
        {
            if(holdLaser)
            {
                tangateLaserHeadOffset = 4;
            }
            else
            {
                tangateLaserHead += 5;
                if(tangateLaserHead > 500)
                    tangateLaserHead = 500;
            }
        }
    }

    public void typeBomberAdjAltitudeMinus()
    {
        if(bLaserOn)
        {
            if(holdLaser)
            {
                tangateLaserHeadOffset = -4;
            }
            else
            {
                tangateLaserHead -= 5;
                if(tangateLaserHead < -1000)
                    tangateLaserHead = -1000;
            }
        }
    }

    public void typeBomberAdjSideslipPlus()
    {
        if(bLaserOn)
        {
            if(holdLaser)
            {
                azimultLaserHeadOffset = 4;
            }
            else
            {
                azimultLaserHead += 5;
                if(azimultLaserHead > 1000)
                    azimultLaserHead = 1000;
            }
        }
    }

    public void typeBomberAdjSideslipMinus()
    {
        if(bLaserOn)
        {
            if(holdLaser)
            {
                azimultLaserHeadOffset = -4;
            }
            else
            {
                azimultLaserHead -= 5;
                if(azimultLaserHead < -1000)
                    azimultLaserHead = -1000;
            }
        }
    }

    public void laserUpdate()
    {
        if(Time.current() == tLastLaserUpdate)
            return;

        tLastLaserUpdate = Time.current();
        Orient orient2 = new Orient();
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d, orient2);
        float roll = orient2.getRoll();
        if(roll > 180F)
            roll -= 360F;
        if(roll < -180F)
            roll += 360F;
        float rollCounter = cvt(roll, -50F, 50F, 50F, -50F);

        if(holdLaser)
        {
            float fn = orient2.getPitch();
            float pitch = 0.0F;
            if(fn > 90F)
                pitch = fn - 360F;
            else
                pitch = fn;
            Point3d laser = new Point3d();
            laser.set(laserSpotPosSaveHold);
            laser.sub(point3d);
            double dx = laser.x;
            double dy = laser.y;
            double dz = laser.z;
            double radius = Math.abs(Math.sqrt(dx * dx + dy * dy));
            float t = (float)Math.toDegrees(Math.atan(dz / radius)) - pitch;
            float y = 0.0F;
            if(dx > 0.0D)
            {
                y = (float)Math.toDegrees(Math.atan(dy / dx)) - orient2.getYaw();
            }
            else
            {
                y = 180F + (float)Math.toDegrees(Math.atan(dy / dx)) - orient2.getYaw();
            }
            if(y > 180F)
                y -= 360F;
            if(y < -180F)
                y += 360F;
            azimultLaserHead = (int)(y / 0.03F) + azimultLaserHeadOffset;
            if(azimultLaserHead > 1000)
                azimultLaserHead = 1000;
            if(azimultLaserHead < -1000)
                azimultLaserHead = -1000;
            tangateLaserHead = (int)(t / 0.03F) + tangateLaserHeadOffset;
            if(tangateLaserHead > 500)
                tangateLaserHead = 500;
            if(tangateLaserHead < -1000)
                tangateLaserHead = -1000;
        }

        hierMesh().chunkSetAngles("LaserMshRoll_D0", 0.0F, 0.0F, rollCounter);
        hierMesh().chunkSetAngles("LaserMsh_D0", -(float)tangateLaserHead * 0.03F, -(float)azimultLaserHead * 0.03F, 0.0F);
        super.pos.setUpdateEnable(true);
        super.pos.getRender(Actor._tmpLoc);
        LaserHook = new HookNamed(this, "_Laser1");
        LaserLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        LaserHook.computePos(this, Actor._tmpLoc, LaserLoc1);
        LaserLoc1.get(LaserP1);
        LaserLoc1.set(30000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        LaserHook.computePos(this, Actor._tmpLoc, LaserLoc1);
        LaserLoc1.get(LaserP2);
        if(Landscape.rayHitHQ(LaserP1, LaserP2, LaserPL))
        {
            LaserPL.z -= 0.95D;
            LaserP2.interpolate(LaserP1, LaserPL, 1.0F);
            setLaserSpot(LaserP2);
            Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(LaserP2.x, LaserP2.y, LaserP2.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
            eff3dactor.postDestroy(Time.current() + 1500L);
        }
        if(azimultLaserHeadOffset != 0 || tangateLaserHeadOffset != 0)
        {
            azimultLaserHeadOffset = 0;
            tangateLaserHeadOffset = 0;
            laserSpotPosSaveHold.set(LaserP2);
        }
    }

    private void checkgroundlaser()
    {
        boolean laseron = false;
        double targetDistance = 0.0D;
        float targetAngle = 0.0F;
        float targetBait = 0.0F;
        float maxTargetBait = 0.0F;
        // superior the Laser spot of this Paveway's owner than others'
        while(getLaserOn())
        {
            Point3d point3d = new Point3d();
            point3d = getLaserSpot();
            if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) < 1.0F)
                break;
            targetDistance = this.pos.getAbsPoint().distance(point3d);
            if (targetDistance > maxPavewayDistance)
                break;
            targetAngle = angleBetween(this, point3d);
            if (targetAngle > maxPavewayFOVfrom)
                break;

            laseron = true;
            break;
        }
        // seak other Laser designator spots when Paveway's owner doesn't spot Laser
        if(!laseron)
        {
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                Actor actor = (Actor)list.get(j);
                if((actor instanceof TypeLaserDesignator) && ((TypeLaserDesignator) actor).getLaserOn() && actor.getArmy() == this.getArmy())
                {
                    Point3d point3d = new Point3d();
                    point3d = ((TypeLaserDesignator)actor).getLaserSpot();
                    // Not target about objects behind of clouds from the Paveway's seaker.
                    if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) < 1.0F)
                        continue;
                    targetDistance = this.pos.getAbsPoint().distance(point3d);
                    if (targetDistance > maxPavewayDistance)
                        continue;
                    targetAngle = angleBetween(this, point3d);
                    if (targetAngle > maxPavewayFOVfrom)
                        continue;

                    targetBait = 1 / targetAngle / (float) (targetDistance * targetDistance);
                    if (targetBait <= maxTargetBait)
                        continue;

                    maxTargetBait = targetBait;
                    laseron = true;
                }
            }
        }
        setLaserArmEngaged(laseron);
    }

    private static float angleBetween(Actor actorFrom, Point3d pointTo)
    {
        float angleRetVal = 180.1F;
        double angleDoubleTemp = 0.0D;
        Loc angleActorLoc = new Loc();
        Point3d angleActorPos = new Point3d();
        Vector3d angleTargRayDir = new Vector3d();
        Vector3d angleNoseDir = new Vector3d();
        actorFrom.pos.getAbs(angleActorLoc);
        angleActorLoc.get(angleActorPos);
        angleTargRayDir.sub(pointTo, angleActorPos);
        angleDoubleTemp = angleTargRayDir.length();
        angleTargRayDir.scale(1.0D / angleDoubleTemp);
        angleNoseDir.set(1.0D, 0.0D, 0.0D);
        angleActorLoc.transform(angleNoseDir);
        angleDoubleTemp = angleNoseDir.dot(angleTargRayDir);
        angleRetVal = Geom.RAD2DEG((float) Math.acos(angleDoubleTemp));
        return angleRetVal;
    }


// ---- TypeLaserDesignator implements .... begin ----

    public Point3d getLaserSpot()
    {
        return laserSpotPos;
    }

    public boolean setLaserSpot(Point3d p3d)
    {
        laserSpotPos.set(p3d);
        return true;
    }

    public boolean getLaserOn()
    {
        return bLaserOn;
    }

    public boolean setLaserOn(boolean flag)
    {
        if(bLaserOn != flag)
        {
            if(bLaserOn == false)
            {
                if(this == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: ON");
                holdLaser = false;
                holdFollowLaser = false;
                actorFollowing = null;
                tangateLaserHead = 0;
                azimultLaserHead = 0;
                tangateLaserHeadOffset = 0;
                azimultLaserHeadOffset = 0;
            }
            else
            {
                if(this == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: OFF");
                holdLaser = false;
                holdFollowLaser = false;
                actorFollowing = null;
                tangateLaserHead = 0;
                azimultLaserHead = 0;
                tangateLaserHeadOffset = 0;
                azimultLaserHeadOffset = 0;
            }
        }

        return bLaserOn = flag;
    }

    public boolean getLaserArmEngaged()
    {
        return bLGBengaged;
    }

    public boolean setLaserArmEngaged(boolean flag)
    {
        if(bLGBengaged != flag)
        {
            if(bLGBengaged == false)
            {
                if(this == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser Bomb: Engaged");
            }
            else
            {
                if(this == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser Bomb: Disengaged");
            }
        }

        return bLGBengaged = flag;
    }

// ---- TypeLaserDesignator implements .... end ----


    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof Pylon_USTER_gn16)
                        ((Pylon_USTER_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_USMERfw_gn16)
                        ((Pylon_USMERfw_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_USMERmd_gn16)
                        ((Pylon_USMERmd_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_gn16)
                        ((Pylon_LAU10_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                        ((Pylon_LAU10_Cap_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU7_gn16)
                        ((Pylon_LAU7_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU118_gn16)
                        ((Pylon_LAU118_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof BombGunCBU24_gn16)
                        ((BombGunCBU24_gn16)FM.CT.Weapons[i][j]).matGray();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk_gn16)
                        ((FuelTankGun_TankSkyhawk_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkNF_gn16)
                        ((FuelTankGun_TankSkyhawkNF_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk400gal_gn16)
                        ((FuelTankGun_TankSkyhawk400gal_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_Mk4HIPEGpod_gn16)
                        ((Pylon_Mk4HIPEGpod_gn16)FM.CT.Weapons[i][j]).matHighvis();

                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                        bHasLAUcaps = true;
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16)
                        bHasLAUcaps = true;
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16)
                        bHasLAUcaps = true;

                    if(FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16)
                        counterFlareList.add(FM.CT.Weapons[i][j]);
                    else if(FM.CT.Weapons[i][j] instanceof RocketGunChaff_gn16)
                        counterChaffList.add(FM.CT.Weapons[i][j]);
                    else if(FM.CT.Weapons[i][j] instanceof BombGunGBU10_Mk84LGB_gn16 ||
                            FM.CT.Weapons[i][j] instanceof BombGunGBU12_Mk82LGB_gn16 ||
                            FM.CT.Weapons[i][j] instanceof BombGunGBU16_Mk83LGB_gn16)
                    {
                        bHasPaveway = true;
                        FM.bNoDiveBombing = true;
                    }
                }
            }
    }

    private void checkDeleteLAUcaps()
    {
        if(FM.CT.saveWeaponControl[2])
        {
            for(int i = 0; i < FM.CT.Weapons.length; i++)
                if(FM.CT.Weapons[i] != null)
                {
                    for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                        if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                            ((Pylon_LAU10_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                        else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU130_Cap_gn16)
                            ((Pylon_LAU130_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                        else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU131_Cap_gn16)
                            ((Pylon_LAU131_Cap_gn16)FM.CT.Weapons[i][j]).jettisonCap();
                }
            bHasLAUcaps = false;
        }
    }

    public void backFire()
    {
        if(counterFlareList.isEmpty())
            hasFlare = false;
        else
        {
            if(Time.current() > lastFlareDeployed + 700L)
            {
                ((RocketGunFlare_gn16)counterFlareList.get(0)).shots(1);
                hasFlare = true;
                lastFlareDeployed = Time.current();
                if(!((RocketGunFlare_gn16)counterFlareList.get(0)).haveBullets())
                    counterFlareList.remove(0);
            }
        }
        if(counterChaffList.isEmpty())
            hasChaff = false;
        else
        {
            if(Time.current() > lastChaffDeployed + 1300L)
            {
                ((RocketGunChaff_gn16)counterChaffList.get(0)).shots(1);
                hasChaff = true;
                lastChaffDeployed = Time.current();
                if(!((RocketGunChaff_gn16)counterChaffList.get(0)).haveBullets())
                    counterChaffList.remove(0);
            }
        }
    }

    public long getChaffDeployed()
    {
        if(hasChaff)
            return lastChaffDeployed;
        else
            return 0L;
    }

    public long getFlareDeployed()
    {
        if(hasFlare)
            return lastFlareDeployed;
        else
            return 0L;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        FM.Sq.dragChuteCx = 3.2F;
        bHasDeployedDragChute = false;
        FM.CT.bHasBombSelect = true;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);

        if(lastRareActionTime != Time.current())
        {
            if(bHasPaveway && FM.bNoDiveBombing && Time.current() - tLastLGBcheck > 30000L && (FM instanceof Maneuver))
            {
                if(!((Maneuver)FM).hasBombs())
                {
                    bHasPaveway = false;
                    FM.bNoDiveBombing = false;
                }
                tLastLGBcheck = Time.current();
            }
            lastRareActionTime = Time.current();
        }
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void update(float f)
    {
        if(lastUpdateTime != Time.current())
        {
            if(bHasLAUcaps)
                checkDeleteLAUcaps();
        }
        super.update(f);
        if(lastUpdateTime != Time.current())
        {
            guidedMissileUtils.update();
            if(super.backfire)
                backFire();
            if(bLaserOn)
                laserUpdate();
            if(bHasPaveway)
                checkgroundlaser();
            updateDragChute();

            if(((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode()) && (FM instanceof Maneuver)))
            {
                if(bHasPaveway && FM.AP.way.curr().Action == 3 && ((Maneuver)FM).hasBombs())
                {
                    if(getLaserArmEngaged())
                    {
                        ((Maneuver)FM).bombsOut = true;
                        FM.CT.WeaponControl[3] = true;
                        Voice.speakAttackByBombs(this);
                    }
                }
            }
            lastUpdateTime = Time.current();
        }
    }

    public void missionStarting()
    {
        super.missionStarting();

        checkChangeWeaponColors();

        bLaserOn = false;
        tLastLaserLockKeyInput = 0L;
        tangateLaserHead = 0;
        azimultLaserHead = 0;
        tangateLaserHeadOffset = 0;
        azimultLaserHeadOffset = 0;
        tLastLaserUpdate = -1L;
    }

    private void updateDragChute()
    {
        if(FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteA4M_US/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.8F);
            ((Actor) (chute)).pos.setRel(new Point3d(-8D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && FM.CT.bHasDragChuteControl)
            if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() > 600F || FM.CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !FM.CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
    }

    public boolean bChangedPit;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;
    private boolean bHasLAUcaps;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;

    private Hook LaserHook;
    private Loc LaserLoc1 = new Loc();
    private Point3d LaserP1 = new Point3d();
    private Point3d LaserP2 = new Point3d();
    private Point3d LaserPL = new Point3d();
    private long tLastLaserLockKeyInput;
    public int azimultLaserHead;
    public int tangateLaserHead;
    public int azimultLaserHeadOffset;
    public int tangateLaserHeadOffset;
    public boolean holdLaser;
    public boolean holdFollowLaser;
    public Actor actorFollowing;
    private Point3d laserSpotPos;
    private Point3d laserSpotPosSaveHold;
    private boolean bLaserOn = false;
    private boolean bLGBengaged = false;
    public boolean bHasPaveway = false;
    private long tLastLaserUpdate = -1L;
    private static float maxPavewayFOVfrom = 45.0F;
    private static double maxPavewayDistance = 20000D;
    private long tLastLGBcheck = -1L;
    private long lastUpdateTime;
    private long lastRareActionTime;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Skyhawk");
        Property.set(class1, "meshName", "3DO/Plane/SkyhawkA4M(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/a4m.fmd:SKYHAWKS");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitSkyhawkA4F.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 
            9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 
            9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 7, 7, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01",        "_CANNON02",        "_MGUN01",          "_MGUN02",          "_ExternalDev01",   "_ExternalDev02",   "_ExternalDev03",   "_ExternalDev04",   "_ExternalDev05",   "_ExternalDev01",
            "_ExternalDev02",   "_ExternalDev03",   "_ExternalDev04",   "_ExternalDev05",   "_ExternalBomb02",  "_ExternalBomb03",  "_ExternalBomb04",  "_ExternalBomb05",  "_ExternalBomb01",  "_ExternalBomb06",
            "_ExternalBomb07",  "_ExternalBomb08",  "_ExternalBomb09",  "_ExternalBomb10",  "_ExternalBomb11",  "_ExternalBomb12",  "_Bomb13",          "_ExternalBomb14",  "_Bomb15",          "_ExternalBomb16",
            "_Bomb17",          "_ExternalBomb18",  "_ExternalRock01",  "_ExternalRock01",  "_ExternalRock02",  "_ExternalRock02",  "_ExternalRock03",  "_ExternalRock03",  "_ExternalRock04",  "_ExternalRock04",
            "_ExternalDev06",   "_ExternalDev07",   "_ExternalDev08",   "_ExternalDev09",   "_ExternalDev06",   "_ExternalDev07",   "_ExternalDev08",   "_ExternalDev09",   "_ExternalDev10",   "_ExternalDev11",
            "_ExternalDev12",   "_ExternalDev10",   "_ExternalDev11",   "_ExternalDev12",   "_Rock05",          "_Rock06",          "_Rock07",          "_Rock08",          "_Rock09",          "_Rock10",
            "_Rock11",          "_Rock12",          "_Rock13",          "_Rock14",          "_Rock15",          "_Rock16",          "_Rock17",          "_Rock18",          "_Rock19",          "_Rock20",
            "_Rock21",          "_Rock22",          "_Rock23",          "_Rock24",          "_Rock25",          "_Rock26",          "_Rock27",          "_Rock28",          "_ExternalRock43",  "_ExternalRock43",
            "_ExternalRock29",  "_ExternalRock29",  "_ExternalRock30",  "_ExternalRock30",  "_ExternalRock31",  "_ExternalRock31",  "_ExternalRock32",  "_ExternalRock32",  "_ExternalRock33",  "_ExternalRock33",
            "_ExternalRock34",  "_ExternalRock34",  "_ExternalRock35",  "_ExternalRock35",  "_ExternalRock36",  "_ExternalRock36",  "_ExternalRock37",  "_ExternalRock37",  "_ExternalRock38",  "_ExternalRock38",
            "_ExternalRock39",  "_ExternalRock39",  "_ExternalRock40",  "_ExternalRock40",  "_Rock41",          "_Rock42",          "_Bomb19",          "_Bomb20",          "_ExternalBomb21",  "_ExternalBomb22",
            "_Bomb23",          "_ExternalBomb24",  "_ExternalBomb25",  "_Bomb26",          "_ExternalBomb27",  "_ExternalBomb28",  "_Flare01",         "_Flare02",         "_Chaff01"
        });
    }
}