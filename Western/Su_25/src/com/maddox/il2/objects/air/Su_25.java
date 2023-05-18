
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Su_25 extends Su_25X
    implements TypeX4Carrier, TypeGuidedMissileCarrier, TypeCountermeasure, TypeStormovikArmored, TypeLaserDesignator, TypeRadarWarningReceiver
{

    public Su_25()
    {
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        counterFlareList = new ArrayList();
        counterChaffList = new ArrayList();
        backfire = false;
        guidedMissileUtils = new GuidedMissileUtils(this);
        APmode1 = false;
        APmode2 = false;
        APmode3 = false;
        headPos = new float[3];
        headOr = new float[3];
        pilotHeadT = 0.0F;
        pilotHeadY = 0.0F;
        tX4Prev = 0L;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        removeChuteTimer = -1L;
        sppuList = new ArrayList();
        sppuDegree = 0.0F;
        sppuAutomationDoing = false;
        sppuReseting = false;
        sppuAutomationCarrierDegree = 0.0F;
        sppuAutomationFinishedTime = -1L;
        bHasSPPU = false;
        tLastLaserLockKeyInput = 0L;
        tangateLaserHead = 0;
        azimultLaserHead = 0;
        tangateLaserHeadOffset = 0;
        azimultLaserHeadOffset = 0;
        laserSpotPos = new Point3d();
        laserSpotPosSaveHold = new Point3d();

        if(Config.cur.ini.get("Mods", "RWRTextStop", 0) > 0) bRWR_Show_Text_Warning = false;
        rwrUtils = new RadarWarningReceiverUtils(this, RWR_GENERATION, RWR_MAX_DETECT, RWR_KEEP_SECONDS, RWR_RECEIVE_ELEVATION, RWR_DETECT_IRMIS, RWR_DETECT_ELEVATION, bRWR_Show_Text_Warning);
    }

    public RadarWarningReceiverUtils getRadarWarningReceiverUtils()
    {
        return rwrUtils;
    }

    public void myRadarSearchYou(Actor actor, String soundpreset)
    {
        rwrUtils.recordRadarSearched(actor, soundpreset);
    }

    public void myRadarLockYou(Actor actor, String soundpreset)
    {
        rwrUtils.recordRadarLocked(actor, soundpreset);
    }

    public void startCockpitSounds()
    {
        if(rwrUtils != null)
            rwrUtils.setSoundEnable(true);
    }

    public void stopCockpitSounds()
    {
        if(rwrUtils != null)
            rwrUtils.stopAllRWRSounds();
    }

    public void typeBomberAdjDistancePlus()
    {
        super.fSightCurForwardAngle += 0.2F;
        if(super.fSightCurForwardAngle > 6F)
            super.fSightCurForwardAngle = 6F;
    }

    public void typeBomberAdjDistanceMinus()
    {
        super.fSightCurForwardAngle -= 0.2F;
        if(super.fSightCurForwardAngle < -30F)
            super.fSightCurForwardAngle = -30F;
    }

    public void typeBomberAdjSideslipPlus()
    {
        super.fSightCurSideslip += 0.2F;
        if(super.fSightCurSideslip > 12F)
            super.fSightCurSideslip = 12F;
    }

    public void typeBomberAdjSideslipMinus()
    {
        super.fSightCurSideslip -= 0.2F;
        if(super.fSightCurSideslip < -12F)
            super.fSightCurSideslip = -12F;
    }

    private void checkAmmo()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof Pylon_SPPU22_gn16)
                        sppuList.add(FM.CT.Weapons[i][j]);
                    else if(FM.CT.Weapons[i][j].haveBullets())
                    {
                        if(FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16)
                            counterFlareList.add(FM.CT.Weapons[i][j]);
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunChaff_gn16)
                            counterChaffList.add(FM.CT.Weapons[i][j]);
                    }
                }
            }

        if(sppuList.size() > 0)
            bHasSPPU = true;
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
            if(Time.current() > lastChaffDeployed + 1000L)
            {
                ((RocketGunChaff_gn16)counterChaffList.get(0)).shots(1);
                hasChaff = true;
                lastChaffDeployed = Time.current();
                if(!((RocketGunChaff_gn16)counterChaffList.get(0)).haveBullets())
                    counterChaffList.remove(0);
            }
        }
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "AutopilotMode__AltitudeON");
                FM.AP.setStabAltitude(FM.getAltitude());
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "AutopilotMode__AltitudeOFF");
                FM.AP.setStabAltitude(false);
            }
        if(i == 21)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "AutopilotMode__DirectionON");
                FM.AP.setStabDirection(true);
                FM.CT.bHasRudderControl = false;
            } else
            if(APmode2)
            {
                APmode2 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "AutopilotMode__DirectionOFF");
                FM.AP.setStabDirection(false);
                FM.CT.bHasRudderControl = true;
            }
        if(i == 22)
            if(!APmode3)
            {
                APmode3 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "AutopilotMode__RouteON");
                FM.AP.setWayPoint(true);
            } else
            if(APmode3)
            {
                APmode3 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "AutopilotMode__RouteOFF");
                FM.AP.setWayPoint(false);
                FM.CT.AileronControl = 0.0F;
                FM.CT.ElevatorControl = 0.0F;
                FM.CT.RudderControl = 0.0F;
            }
        if(i == 23)
        {
            FM.CT.AileronControl = 0.0F;
            FM.CT.ElevatorControl = 0.0F;
            FM.CT.RudderControl = 0.0F;
            FM.AP.setWayPoint(false);
            FM.AP.setStabDirection(false);
            FM.AP.setStabAltitude(false);
            APmode1 = false;
            APmode2 = false;
            APmode3 = false;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "AutopilotMode__AllOff");
        }
        if(i == 25)
        {
            if(!bHasSPPU) return;

            if(sppuAutomationDoing)
            {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "SPPU__StopAutomation");
                sppuAutomationDoing = false;
                sppuAutomationCarrierDegree = 0.0F;
                typeX4CResetControls();
            }
            else
            {
                sppuAutomationCarrierDegree = FM.Or.getTangage();
                for(; sppuAutomationCarrierDegree > 180F; sppuAutomationCarrierDegree -= 360F) ;
                for(; sppuAutomationCarrierDegree < -180F; sppuAutomationCarrierDegree += 360F) ;
                if(sppuAutomationCarrierDegree < -3F && sppuAutomationCarrierDegree > -90F)
                {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "SPPU__StartAutomation");
                    sppuAutomationDoing = true;
                    sppuReseting = false;
                }
            }
        }
        if(i == 26 && getLaserOn())
        {
            if(holdLaser && tLastLaserLockKeyInput + 200L < Time.current())
            {
                holdLaser = false;
                holdFollowLaser = false;
                actorFollowing = null;
                tangateLaserHeadOffset = 0;
                azimultLaserHeadOffset = 0;
                HUD.log("Laser_Pos_Unlock");
                tLastLaserLockKeyInput = Time.current();
            }
            if(!holdLaser && tLastLaserLockKeyInput + 200L < Time.current())
            {
                holdLaser = true;
                holdFollowLaser = false;
                actorFollowing = null;
                laserSpotPosSaveHold.set(getLaserSpot());
                HUD.log("Laser_Pos_Lock");
                tLastLaserLockKeyInput = Time.current();
            }
        }
        if(i == 29)
        {
            setLaserOn(!bLaserOn);
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
        // superior the Laser spot of this LGBomb's owner than others'
        while(getLaserOn())
        {
            Point3d point3d = new Point3d();
            point3d = getLaserSpot();
            if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) < 1.0F)
                break;
            targetDistance = this.pos.getAbsPoint().distance(point3d);
            if(targetDistance > maxLGBombDistance)
                break;
            targetAngle = angleBetween(this, point3d);
            if(targetAngle > maxLGBombFOVfrom)
                break;

            laseron = true;
            break;
        }
        // seak other Laser designator spots when LGBomb's owner doesn't spot Laser
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
                    // Not target about objects behind of clouds from the LGBomb's seaker.
                    if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) < 1.0F)
                        continue;
                    targetDistance = this.pos.getAbsPoint().distance(point3d);
                    if(targetDistance > maxLGBombDistance)
                        continue;
                    targetAngle = angleBetween(this, point3d);
                    if(targetAngle > maxLGBombFOVfrom)
                        continue;

                    targetBait = 1 / targetAngle / (float) (targetDistance * targetDistance);
                    if(targetBait <= maxTargetBait)
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
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "LaserON");
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
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "LaserOFF");
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
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "LaserBomb_Engaged");
            }
            else
            {
                if(this == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "LaserBomb_Disengaged");
            }
        }

        return bLGBengaged = flag;
    }

// ---- TypeLaserDesignator implements .... end ----

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void typeX4CAdjSidePlus()
    {
        deltaAzimuth = 0.1F;
    }

    public void typeX4CAdjSideMinus()
    {
        deltaAzimuth = -0.1F;
    }

    public void typeX4CAdjAttitudePlus()
    {
        deltaTangage = 0.1F;

        if(bHasSPPU && sppuDegree < 0.0F)
        {
            sppuDegree += 1.0F;
            if(sppuDegree > 0.0F)
                sppuDegree = 0.0F;
            if(sppuReseting && sppuDegree == 0.0F) sppuReseting = false;
            for(int i = 0; i < sppuList.size(); i++)
                ((Pylon_SPPU22_gn16)(sppuList.get(i))).setGunDegree(-sppuDegree);
            hierMesh().chunkSetAngles("SPPUgun00", 0.0F, sppuDegree, 0.0F);
            hierMesh().chunkSetAngles("SPPUgun01", 0.0F, sppuDegree, 0.0F);
            hierMesh().chunkSetAngles("SPPUgun02", 0.0F, sppuDegree, 0.0F);
            hierMesh().chunkSetAngles("SPPUgun03", 0.0F, sppuDegree, 0.0F);
        }
    }

    public void typeX4CAdjAttitudeMinus()
    {
        deltaTangage = -0.1F;

        if(bHasSPPU && sppuDegree > -30.0F)
        {
            if(sppuReseting) return;

            sppuDegree -= 1.0F;
            if(sppuDegree < -30.0F) sppuDegree = -30.0F;
            for(int i = 0; i < sppuList.size(); i++)
                ((Pylon_SPPU22_gn16)(sppuList.get(i))).setGunDegree(-sppuDegree);
            hierMesh().chunkSetAngles("SPPUgun00", 0.0F, sppuDegree, 0.0F);
            hierMesh().chunkSetAngles("SPPUgun01", 0.0F, sppuDegree, 0.0F);
            hierMesh().chunkSetAngles("SPPUgun02", 0.0F, sppuDegree, 0.0F);
            hierMesh().chunkSetAngles("SPPUgun03", 0.0F, sppuDegree, 0.0F);
        }
    }

    public void typeX4CResetControls()
    {
        deltaAzimuth = deltaTangage = 0.0F;

        if(bHasSPPU)
            sppuReseting = true;
    }

    public float typeX4CgetdeltaAzimuth()
    {
        return deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage()
    {
        return deltaTangage;
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
        checkAmmo();
        super.onAircraftLoaded();
        FM.CT.bHasBombSelect = true;
        super.bHasSK1Seat = false;
        FM.CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        guidedMissileUtils.onAircraftLoaded();

        rwrUtils.onAircraftLoaded();
        rwrUtils.setLockTone("aircraft.usRWRScan", "aircraft.Sirena2", "aircraft.Sirena2", "aircraft.usRWRThreatNew");
    }

    public void missionStarting()
    {
        super.missionStarting();
        FM.CT.toggleRocketHook();

        bLaserOn = false;
        tLastLaserLockKeyInput = 0L;
        tangateLaserHead = 0;
        azimultLaserHead = 0;
        tangateLaserHeadOffset = 0;
        azimultLaserHeadOffset = 0;
        tLastLaserUpdate = -1L;
    }

    public void update(float f)
    {
        if(bLaserOn)
            laserUpdate();
        if(bHasLGBomb)
            checkgroundlaser();
        guidedMissileUtils.update();
        rwrUtils.update();
        backfire = rwrUtils.getBackfire();
        bRadarWarning = rwrUtils.getRadarLockedWarning();
        bMissileWarning = rwrUtils.getMissileWarning();
        if(backfire)
            backFire();
        super.update(f);
        calculateDragChute();
        if(bHasSPPU)
        {
            if(sppuAutomationDoing)
                sppuAutomation();
            else if(sppuReseting)
                typeX4CAdjAttitudePlus();
        }
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 60F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void doEjectCatapult()
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 40D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(10, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat1_D0", false);
        FM.setTakenMortalDamage(true, null);
        FM.CT.WeaponControl[0] = false;
        FM.CT.WeaponControl[1] = false;
        FM.CT.bHasAileronControl = false;
        FM.CT.bHasRudderControl = false;
        FM.CT.bHasElevatorControl = false;
    }

    private void calculateDragChute()
    {
        if(FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute1 = new Chute(this);
            chute2 = new Chute(this);
            chute1.setMesh("3do/plane/ChuteSu_25/mono.sim");
            chute2.setMesh("3do/plane/ChuteSu_25/mono.sim");
            chute1.mesh().setScale(0.5F);
            chute2.mesh().setScale(0.5F);
            ((Actor) (chute1)).pos.setRel(new Point3d(-8D, 0.0D, 0.6D), new Orient(20F, 90F, 0.0F));
            ((Actor) (chute2)).pos.setRel(new Point3d(-8D, 0.0D, 0.6D), new Orient(-20F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && FM.CT.bHasDragChuteControl)
            if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() > 600F || FM.CT.DragChuteControl < 1.0F)
            {
                if(chute1 != null && chute2 != null)
                {
                    chute1.tangleChute(this);
                    chute2.tangleChute(this);
                    ((Actor) (chute1)).pos.setRel(new Point3d(-15D, 0.0D, 1.0D), new Orient(20F, 90F, 0.0F));
                    ((Actor) (chute2)).pos.setRel(new Point3d(-15D, 0.0D, 1.0D), new Orient(-20F, 90F, 0.0F));
                }
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() < 20F)
            {
                if(chute1 != null && chute2 != null)
                {
                    chute1.tangleChute(this);
                    chute2.tangleChute(this);
                }
                ((Actor) (chute1)).pos.setRel(new Orient(10F, 100F, 0.0F));
                ((Actor) (chute2)).pos.setRel(new Orient(-10F, 100F, 0.0F));
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !FM.CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
        {
            chute1.destroy();
            chute2.destroy();
        }
    }

    private void sppuAutomation()
    {
        if(sppuDegree > -30.0F) sppuAutomationFinishedTime = -1L;

        float nowPitch = FM.Or.getTangage();
        for(; nowPitch > 180F; nowPitch -= 360F) ;
        for(; nowPitch < -180F; nowPitch += 360F) ;

        float sppudiff = nowPitch - sppuAutomationCarrierDegree;
        if(Math.abs(sppudiff + sppuDegree) < 0.85F) return;

        if(sppudiff + sppuDegree > 0F)
            typeX4CAdjAttitudeMinus();
        else
            typeX4CAdjAttitudePlus();

        if(sppuDegree == -30.0F)
        {
            if(sppuAutomationFinishedTime == -1L)
                sppuAutomationFinishedTime = Time.current();
            else if(Time.current() - sppuAutomationFinishedTime > 10000L)
            {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "SPPU__StopAutomation");
                sppuAutomationDoing = false;
                sppuAutomationCarrierDegree = 0.0F;
                typeX4CResetControls();
                sppuAutomationFinishedTime = -1L;
            }
        }
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private float headPos[];
    private float headOr[];
    private float pilotHeadT;
    private float pilotHeadY;
    public static Orient tmpOr = new Orient();
    private static Vector3d Ve = new Vector3d();
    private long tX4Prev;
    private float deltaAzimuth;
    private float deltaTangage;
    public boolean APmode1;
    public boolean APmode2;
    public boolean APmode3;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;
    private boolean bHasDeployedDragChute;
    private Chute chute1;
    private Chute chute2;
    private long removeChuteTimer;
    private ArrayList sppuList;
    private float sppuDegree;
    private boolean bHasSPPU;
    private boolean sppuAutomationDoing;
    private boolean sppuReseting;
    private float sppuAutomationCarrierDegree;
    private long sppuAutomationFinishedTime;
//    public static boolean bChangedPit = false;   // already prepaired in Su_25X.class

    //By western0221, Radar Warning Receiver
    private RadarWarningReceiverUtils rwrUtils;
    public boolean bRadarWarning;
    public boolean bMissileWarning;
    public boolean backfire;

    private static final int RWR_GENERATION = 1;
    private static final int RWR_MAX_DETECT = 16;
    private static final int RWR_KEEP_SECONDS = 6;
    private static final double RWR_RECEIVE_ELEVATION = 45.0D;
    private static final boolean RWR_DETECT_IRMIS = false;
    private static final boolean RWR_DETECT_ELEVATION = true;
    private boolean bRWR_Show_Text_Warning = true;

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
    public boolean bHasLGBomb = false;
    private long tLastLaserUpdate = -1L;
    private static float maxLGBombFOVfrom = 45.0F;
    private static double maxLGBombDistance = 20000D;
    private long tLastLGBcheck = -1L;

    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(byte bt)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[bt];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGSH_30_2k", 250);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(6, "RocketGunR60M_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(6, "RocketGunR60M_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            // merging 2x ASO-2V chaff / flare dispensers (each 32x) on 1 hook.
            // In 1986 , Su-25 became to have total 256x rounds chaff+flare with 8x ASO-2V.
            a_lweaponslot[110] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 64);
            a_lweaponslot[111] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 64);
            a_lweaponslot[112] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 64);
            a_lweaponslot[113] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 64);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : Default loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] GenerateKh29Config(byte bt)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = GenerateDefaultConfig(bt);
        try
        {
            a_lweaponslot[58] = new Aircraft._WeaponSlot(5, "RocketGunKh29L_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunKh29L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : Kh29L loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] GenerateKh25ML2Config(byte bt)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = GenerateDefaultConfig(bt);
        try
        {
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(5, "RocketGunKh25ML_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(5, "RocketGunKh25ML_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : 2x Kh25ML loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] GenerateKh25ML4Config(byte bt)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = GenerateKh25ML2Config(bt);
        try
        {
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(5, "RocketGunKh25ML_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(5, "RocketGunKh25ML_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : 4x Kh25ML loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] GenerateKh25MR2Config(byte bt)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = GenerateDefaultConfig(bt);
        try
        {
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(5, "RocketGunKh25MR_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(5, "RocketGunKh25MR_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : 2x Kh25MR loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] GenerateKh25MR4Config(byte bt)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = GenerateKh25MR2Config(bt);
        try
        {
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(5, "RocketGunKh25MR_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(5, "RocketGunKh25MR_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : 4x Kh25MR loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] GenerateKMGUptConfig(byte bt)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = GenerateDefaultConfig(bt);
        try
        {
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : KMGU2(PTAB) loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] GenerateKMGUaoConfig(byte bt)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = GenerateDefaultConfig(bt);
        try
        {
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : KMGU2(AO) loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] InsertFAB100ooutConfig(Aircraft._WeaponSlot[] a_lweaponslot)
    {
        try
        {
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : Insert 8x o-out FAB-100M46 loadout method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] InsertFAB100outConfig(Aircraft._WeaponSlot[] a_lweaponslot)
    {
        try
        {
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : Insert 8x out FAB-100M46 loadout method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] InsertFAB100midConfig(Aircraft._WeaponSlot[] a_lweaponslot)
    {
        try
        {
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : Insert 8x out FAB-100M46 loadout method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] InsertSAB100ooutConfig(Aircraft._WeaponSlot[] a_lweaponslot)
    {
        try
        {
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunSAB100_90_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunSAB100_90_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunSAB100_90_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunSAB100_90_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunSAB100_90_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunSAB100_90_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunSAB100_90_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunSAB100_90_gn16", 1);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : Insert 8x o-out FAB-100M46 loadout method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] InsertUB32ooutConfig(Aircraft._WeaponSlot[] a_lweaponslot)
    {
        try
        {
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : Insert 2x o-out UB-32 loadout method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] InsertB8ooutConfig(Aircraft._WeaponSlot[] a_lweaponslot)
    {
        try
        {
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : Insert 2x o-out B-8 loadout method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    static
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Su-25");
        Property.set(class1, "meshName", "3DO/Plane/Su-25/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1980.1F);
        Property.set(class1, "yearExpired", 2040.3F);
        Property.set(class1, "FlightModel", "FlightModels/SU-25.fmd:SU25FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitSu_25.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 1, 1, 1, 2, 2, 2, 2,
            9, 9, 9, 9, 9, 9, 9, 9, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            7, 7, 8, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01",       "_CANNON02",       "_MGUN00",         "_MGUN01",         "_MGUN02",         "_MGUN03",         "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02",
            "_ExternalDev01",  "_ExternalDev02",  "_ExternalDev03",  "_ExternalDev04",  "_ExternalDev05",  "_ExternalDev06",  "_ExternalDev07",  "_ExternalDev08",  "_ExternalBomb01", "_ExternalBomb02",
            "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_Bomb09",         "_Bomb10",         "_Bomb11",         "_Bomb12",
            "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_Bomb17",         "_Bomb18",         "_Bomb19",         "_Bomb20",         "_ExternalBomb21", "_ExternalBomb22",
            "_ExternalBomb23", "_ExternalBomb24", "_Bomb25",         "_Bomb26",         "_Bomb27",         "_Bomb28",         "_ExternalBomb29", "_ExternalBomb30", "_ExternalBomb31", "_ExternalBomb32",
            "_Bomb33",         "_Bomb34",         "_Bomb35",         "_Bomb36",         "_ExternalBomb37", "_ExternalBomb38", "_ExternalBomb39", "_ExternalBomb40", "_ExternalRock03", "_ExternalRock03",
            "_ExternalRock04", "_ExternalRock04", "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalRock07", "_ExternalRock07", "_ExternalRock08", "_ExternalRock08",
            "_ExternalRock09", "_ExternalRock09", "_ExternalRock10", "_ExternalRock10", "_ExternalRock11", "_ExternalRock11", "_ExternalRock12", "_ExternalRock12", "_ExternalRock13", "_ExternalRock13",
            "_ExternalRock14", "_ExternalRock14", "_ExternalRock15", "_ExternalRock15", "_ExternalRock16", "_ExternalRock16", "_ExternalRock17", "_ExternalRock17", "_ExternalRock18", "_ExternalRock18",
            "_ExternalRock19", "_ExternalRock19", "_ExternalRock20", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26",
            "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36",
            "_Flare01",        "_Flare02",        "_Chaff01",        "_Chaff02"
        });
        String s = "";

        ArrayList arraylist = new ArrayList();
        Property.set(class1, "weaponsList", arraylist);
        HashMapInt hashmapint = new HashMapInt();
        Property.set(class1, "weaponsMap", hashmapint);
        byte byte0 = 114;
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];

        try
        {
            s = "default";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xFAB250+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xFAB500+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xRBK250(PTAB2.5)+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xRBK250(AO1)+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xRBK500(PTAB1)+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xRBK500(AO2.5)+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xB13+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xS24+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xS25+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "32xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xFAB250+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB500+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK250(PTAB2.5)+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK250(AO1)+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK500(PTAB1)+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK500(AO2.5)+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xB8+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xB13+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS24+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS25+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24xFAB100+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xFAB250+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB500+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK250(PTAB2.5)+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK250(AO1)+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK500(PTAB1)+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK500(AO2.5)+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xB8+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xB13+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS24+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB250+8xFAB100+2xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB500+8xFAB100+2xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK250(PTAB2.5)+8xFAB100+2xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK500(AO2.5)+8xFAB100+2xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB8+4xFAB250+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xB8+2xFAB500+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB8+4xRBK250(PTAB2.5)+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xB8+2xRBK500(AO2.5)+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xB13+8xFAB100+2xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xB13+2xB8+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xS24+2xB8+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xS25+2xB8+8xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xFAB100+2xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xFAB250+2xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xFAB500+2xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK250(PTAB2.5)+2xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK250(AO1)+2xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK500(PTAB1)+2xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK500(AO2.5)+2xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xB8+2xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xB13+2xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS24+4xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xFAB250+2xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xFAB500+2xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK250(PTAB2.5)+2xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK250(AO1)+2xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK500(PTAB1)+2xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK500(AO2.5)+2xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xB13+2xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS24+4xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS25+2xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xFAB250+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xFAB500+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK250(PTAB2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK250(AO1)+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK500(PTAB1)+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xRBK500(AO2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xB13+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xS24+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS25+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB250+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB500+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK250(PTAB2.5)+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK250(AO1)+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRBK500(PTAB1)+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRBK500(AO2.5)+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xB8+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xB13+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS24+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xS25+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB250+8xFAB100+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB500+8xFAB100+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRBK250(PTAB2.5)+8xFAB100+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xRBK500(AO2.5)+8xFAB100+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB8+2xFAB250+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB8+2xFAB500+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB8+2xRBK250(PTAB2.5)+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB8+2xRBK500(AO2.5)+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB13+8xFAB100+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB13+2xB8+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB250+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB500+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK250(PTAB2.5)+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK250(AO1)+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK500(PTAB1)+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK500(AO2.5)+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xB8+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(2, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xB13+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xS24+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB250+2xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB500+2xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK250(PTAB2.5)+2xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK250(AO1)+2xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK500(PTAB1)+2xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xRBK500(AO2.5)+2xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xB13+2xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xS24+4xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xS25+2xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+16xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+4xFAB250+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+4xRBK250(PTAB2.5)+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+4xRBK250(AO1)+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+4xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+4xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+4xB13+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+4xS24+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+8xFAB100+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+2xUB32+2xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+2xFAB250+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+2xRBK250(PTAB2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+2xRBK250(AO1)+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+2xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+2xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSPPU22+2xB13+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+24xFAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+6xFAB250+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+6xRBK250(PTAB2.5)+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+6xRBK250(AO1)+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+6xUB32+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+6xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+6xB13+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+6xS24+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+16xFAB100+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xFAB250+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xUB32+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xB8+8xSAB100+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertSAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+16xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xFAB250+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xRBK250(PTAB2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xRBK250(AO1)+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xB13+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xS24+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+16xFAB100+2xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xFAB250+2xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xRBK250(PTAB2.5)+2xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xRBK250(AO1)+2xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xB13+2xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xSPPU22+4xS24+2xB8+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(1, "MGunGSH23kit", 260);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_SPPU22_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xKMGU2(AO2.5)+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKMGU2(AO2.5)+8xFAB100+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKMGU2(AO2.5)+2xFAB250+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKMGU2(AO2.5)+4xUB32+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKMGU2(AO2.5)+4xB8+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKMGU2(AO2.5)+2xB13+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKMGU2(AO2.5)+2xS24+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+16xFAB100+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+6xFAB250+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+4xFAB500+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+6xUB32+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+6xB8+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+6xB13+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+6xS24+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+6xS25+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+4xFAB250+2xUB32+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+4xFAB250+2xB8+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+4xUB32+8xFAB100+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+4xB8+8xFAB100+2xR60";
            a_lweaponslot = GenerateKMGUaoConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKMGU2(AO2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+4xFAB250+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+4xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+4xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+2xB13+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(AO2.5)+2xS24+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunAO2_5_3", 96);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xKMGU2(PTAB2.5)+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKMGU2(PTAB2.5)+8xFAB100+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKMGU2(PTAB2.5)+2xFAB250+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKMGU2(PTAB2.5)+4xUB32+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKMGU2(PTAB2.5)+4xB8+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKMGU2(PTAB2.5)+2xB13+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKMGU2(PTAB2.5)+2xS24+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+16xFAB100+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+6xFAB250+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+4xFAB500+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+6xUB32+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+6xB8+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+6xB13+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+6xS24+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+6xS25+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+4xFAB250+2xUB32+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+4xFAB250+2xB8+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+4xUB32+8xFAB100+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+4xB8+8xFAB100+2xR60";
            a_lweaponslot = GenerateKMGUptConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKMGU2(PTAB2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+4xFAB250+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+4xUB32+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertUB32ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+4xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+2xB13+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKMGU2(PTAB2.5)+2xS24+2xPTB800+2xR60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_KMGU2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunPTAB25", 96);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+16xFAB100+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+6xFAB250+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+4xFAB500+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+6xRBK250(PTAB2.5)+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+6xRBK250(AO1)+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+4xRBK500(PTAB1)+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+4xRBK500(AO2.5)+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+6xB8+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+6xB13+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(4, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(4, "RocketGunS13_gn16", 5);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(4, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(4, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(4, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(4, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+4xS24+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(4, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(4, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(4, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(4, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+4xS25+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(4, "RocketGunS25_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(4, "RocketGunS25_gn16", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(4, "RocketGunS25_gn16", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(4, "RocketGunS25_gn16", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+8xFAB100+2xB8+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+4xFAB250+2xB8+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+2xFAB500+2xB8+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+4xRBK250(PTAB2.5)+2xB8+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+4xRBK250(AO1)+2xB8+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+2xRBK500(PTAB1)+2xB8+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+2xRBK500(AO2.5)+2xB8+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot = InsertFAB100midConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+4xFAB250+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+2xFAB500+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+4xRBK250(PTAB2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+4xRBK250(AO1)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+2xRBK500(PTAB1)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+2xRBK500(AO2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+4xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+4xB13+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(4, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(4, "RocketGunS13_gn16", 5);
            a_lweaponslot[98] = new Aircraft._WeaponSlot(4, "RocketGunS13_gn16", 5);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(4, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+2xS24+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(4, "RocketGunS24_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(4, "RocketGunS24_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+2xS25+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(4, "RocketGunS25_gn16", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(4, "RocketGunS25_gn16", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+2xFAB250+2xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+2xRBK250(PTAB2.5)+2xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh29L+2xRBK250(AO1)+2xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh29Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+16xFAB100+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+4xFAB250+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+4xFAB500+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+4xRBK250(PTAB2.5)+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+4xRBK250(AO1)+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+4xRBK500(PTAB1)+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+4xRBK500(AO2.5)+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+4xB8+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+4xB13+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+4xS24+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+4xS25+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+8xFAB100+2xB8+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xFAB250+2xB8+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xRBK250(PTAB2.5)+2xB8+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xRBK250(AO1)+2xB8+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xFAB250+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xFAB500+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xRBK250(PTAB2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xRBK250(AO1)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xRBK500(PTAB1)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xRBK500(AO2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xB13+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xS24+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25ML+2xS25+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML4Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+24xFAB100+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+6xFAB250+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+6xFAB500+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+6xRBK250(PTAB2.5)+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+6xRBK250(AO1)+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+6xRBK500(PTAB1)+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+6xRBK500(AO2.5)+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+6xB8+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+6xB13+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+6xS24+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+6xS25+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+16xFAB100+2xB8+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+4xFAB250+2xB8+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+4xRBK250(PTAB2.5)+2xB8+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+4xRBK250(AO1)+2xB8+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+16xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+4xFAB250+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+2xFAB500+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+4xRBK250(PTAB2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+4xRBK250(AO1)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+2xRBK500(PTAB1)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+2xRBK500(AO2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+4xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+4xB13+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+4xS24+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25ML+4xS25+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25ML2Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+16xFAB100+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+4xFAB250+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+4xFAB500+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+4xRBK250(PTAB2.5)+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+4xRBK250(AO1)+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+4xRBK500(PTAB1)+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+4xRBK500(AO2.5)+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+4xB8+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+4xB13+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+4xS24+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+4xS25+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+8xFAB100+2xB8+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xFAB250+2xB8+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xRBK250(PTAB2.5)+2xB8+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xRBK250(AO1)+2xB8+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+8xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xFAB250+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xFAB500+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xRBK250(PTAB2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xRBK250(AO1)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xRBK500(PTAB1)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xRBK500(AO2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xB13+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xS24+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xKh25MR+2xS25+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR4Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+24xFAB100+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+6xFAB250+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+6xFAB500+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+6xRBK250(PTAB2.5)+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+6xRBK250(AO1)+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+6xRBK500(PTAB1)+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+6xRBK500(AO2.5)+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+6xB8+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[108] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[109] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+6xB13+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+6xS24+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+6xS25+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+16xFAB100+2xB8+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_MBD_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+4xFAB250+2xB8+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+4xRBK250(PTAB2.5)+2xB8+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+4xRBK250(AO1)+2xB8+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+16xFAB100+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot = InsertFAB100ooutConfig(a_lweaponslot);
            a_lweaponslot = InsertFAB100outConfig(a_lweaponslot);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+4xFAB250+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+2xFAB500+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+4xRBK250(PTAB2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250_PTAB25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+4xRBK250(AO1)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK250270_AO1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+2xRBK500(PTAB1)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_PTAB1M_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+2xRBK500(AO2.5)+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunRBK500_AO25_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+4xB8+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot = InsertB8ooutConfig(a_lweaponslot);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B8M1_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(4, "RocketGunS8KOM_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+4xB13+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_B13_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunS13_gn16", 5);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+4xS24+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_APU68UM2_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunS24_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xKh25MR+4xS25+2xPTB800+2xR60";
            a_lweaponslot = GenerateKh25MR2Config(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_O25_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunS25_gn16", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Ferry(4xPTB800+2xR60)";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "FuelTankGun_Su25PTB800L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - Su_25 : " + s);
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
