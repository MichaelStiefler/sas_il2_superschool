
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.trains.Wagon;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.planes.PlaneGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.Missile;
import com.maddox.il2.objects.weapons.MissileSAM;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapExt;
import java.io.IOException;
import java.lang.Math;
import java.lang.reflect.Field;
import java.util.*;


public class A_6 extends Scheme2
    implements TypeSupersonic, TypeFighter, TypeFighterAceMaker, TypeGSuit, TypeFastJet, TypeRadar, TypeBomber, TypeX4Carrier, TypeLaserDesignator, TypeStormovikArmored, /* TypeGroundRadar, */ TypeFuelDump, TypeRadarWarningReceiver
{

    public float getDragForce(float f, float f1, float f2, float f3)
    {
        throw new UnsupportedOperationException("getDragForce not supported anymore.");
    }

    public float getDragInGravity(float f, float f1, float f2, float f3, float f4, float f5)
    {
        throw new UnsupportedOperationException("getDragInGravity supported anymore.");
    }

    public float getForceInGravity(float f, float f1, float f2)
    {
        throw new UnsupportedOperationException("getForceInGravity supported anymore.");
    }

    public float getDegPerSec(float f, float f1)
    {
        throw new UnsupportedOperationException("getDegPerSec supported anymore.");
    }

    public float getGForce(float f, float f1)
    {
        throw new UnsupportedOperationException("getGForce supported anymore.");
    }

    public A_6()
    {
        elevatorsField = null;
        lLightHook = new Hook[8];
        SonicBoom = 0.0F;
        oldctl = -1F;
        curctl = -1F;
        oldthrl = -1F;
        curthrl = -1F;
        k14Mode = 2;
        k14WingspanType = 0;
        k14Distance = 200F;
        SpoilerBrakeControl = 0.0F;
        overrideBailout = false;
        ejectComplete = false;
        lightTime = 0.0F;
        ft = 0.0F;
        mn = 0.0F;
        ts = false;
        ictl = false;
        engineSurgeDamage = 0.0F;
        hasHydraulicPressure = true;
        radarmode = 0;
        targetnum = 0;
        lockrange = 0.04F;
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 3000F;
        fSightCurSpeed = 200F;
        fSightCurReadyness = 0.0F;
        missilesList = new ArrayList();
        lockmode = 0;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        radargunsight = 0;
        leftscreen = 0;
        Bingofuel = 1000;
        radarrange = 1;
        hold = false;
        holdFollow = false;
        actorFollowing = null;
        t1 = 0L;
        tangate = 0.0F;
        azimult = 0.0F;
        tf = 0L;
        APmode1 = false;
        APmode2 = false;
        radartogle = false;
        v = 0.0F;
        h = 0.0F;
        lockmode = 0;
        radargunsight = 0;
        leftscreen = 2;
        Bingofuel = 1000;
        radarrange = 1;
        Nvision = false;
        noFL = false;
        thrustMaxField = new Field[2];
        lTimeNextEject = 0L;
        obsLookTime = 0;
        obsLookAzimuth = 0.0F;
        obsLookElevation = 0.0F;
        obsAzimuth = 0.0F;
        obsElevation = 0.0F;
        obsAzimuthOld = 0.0F;
        obsElevationOld = 0.0F;
        obsMove = 0.0F;
        obsMoveTot = 0.0F;
        bObserverKilled = false;
        Flaps = false;
        antiColLight = new Eff3DActor[6];
        arrestor = 0.0F;
        engineOilPressurePSI = new float[2];
        laserSpotPos = new Point3d();
        laserTimer = -1L;
        counter = 0;
        freq = 800;
        Timer1 = Timer2 = freq;
        error = 0;
        bHasSK1Seat = true;
        if(Config.cur.ini.get("Mods", "RWRTextStop", 0) > 0) bRWR_Show_Text_Warning = false;
        if(this instanceof A_6Eswip || this instanceof EA_6BicapII)
            RWR_GENNUM = 0;
        else
            RWR_GENNUM = 1;
        if(RWR_GENNUM == 0)
            rwrUtils = new RadarWarningReceiverUtils(this, RWR_GENERATION70, RWR_MAX_DETECT70, RWR_KEEP_SECONDS70, RWR_RECEIVE_ELEVATION70, RWR_DETECT_IRMIS70, RWR_DETECT_ELEVATION70, bRWR_Show_Text_Warning);
        else if(RWR_GENNUM == 1)
            rwrUtils = new RadarWarningReceiverUtils(this, RWR_GENERATION80, RWR_MAX_DETECT80, RWR_KEEP_SECONDS80, RWR_RECEIVE_ELEVATION80, RWR_DETECT_IRMIS80, RWR_DETECT_ELEVATION80, bRWR_Show_Text_Warning);
    }

    private static final float toMeters(float f)
    {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f)
    {
        return 0.4470401F * f;
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log("Autopilot Mode: Altitude ON");
                FM.AP.setStabAltitude(1000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log("Autopilot Mode: Altitude OFF");
                FM.AP.setStabAltitude(false);
            }
        if(i == 21)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log("Autopilot Mode: Direction ON");
                FM.AP.setStabDirection(true);
                FM.CT.bHasRudderControl = false;
            } else
            if(APmode2)
            {
                APmode2 = false;
                HUD.log("Autopilot Mode: Direction OFF");
                FM.AP.setStabDirection(false);
                FM.CT.bHasRudderControl = true;
            }
        if(i == 26 && bHasLaser)
        {
            if(hold && t1 + 200L < Time.current() && FLIR)
            {
                hold = false;
                holdFollow = false;
                actorFollowing = null;
                HUD.log("Laser Pos Unlock");
                t1 = Time.current();
            }
            if(!hold && t1 + 200L < Time.current() && FLIR)
            {
                hold = true;
                holdFollow = false;
                actorFollowing = null;
                HUD.log("Laser Pos Lock");
                t1 = Time.current();
            }
            if(!FLIR)
                setLaserOn(false);
        }
        if(i == 27 && bHasLaser)
        {
            if(holdFollow && t1 + 200L < Time.current() && FLIR)
            {
                hold = false;
                holdFollow = false;
                actorFollowing = null;
                HUD.log("Laser Track Unlock");
                t1 = Time.current();
            }
            if(!holdFollow && t1 + 200L < Time.current() && FLIR)
            {
                hold = false;
                holdFollow = true;
                actorFollowing = null;
                HUD.log("Laser Track Lock");
                t1 = Time.current();
            }
        }
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
        if(FLIR)
        {
            azimult++;
            tf = Time.current();
        } else
        if(radartogle && lockmode == 0)
            h += 0.0035F;
    }

    public void typeBomberAdjDistanceMinus()
    {
        if(FLIR)
        {
            azimult--;
            tf = Time.current();
        } else
        if(radartogle && lockmode == 0)
            h -= 0.0035F;
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
        if(FLIR)
        {
            tangate++;
            tf = Time.current();
        } else
        if(radartogle && lockmode == 0)
            v += 0.0035F;
    }

    public void typeBomberAdjSideslipMinus()
    {
        if(FLIR)
        {
            tangate--;
            tf = Time.current();
        } else
        if(radartogle && lockmode == 0)
            v -= 0.0035F;
    }

    public void updatecontrollaser()
    {
        if(tf + 5L <= Time.current())
        {
            tangate = 0.0F;
            azimult = 0.0F;
        }

        if(!FLIR && laserTimer > 0L && Time.current() > laserTimer && getLaserOn())
        {
            setLaserOn(false);
        }

        if(bHasPaveway)
            checkgroundlaser();
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
            if(targetDistance > maxPavewayDistance)
                break;
            targetAngle = angleBetween(this, point3d);
            if(targetAngle > maxPavewayFOVfrom)
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
                    if(targetDistance > maxPavewayDistance)
                        continue;
                    targetAngle = angleBetween(this, point3d);
                    if(targetAngle > maxPavewayFOVfrom)
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

    private static float angleBetween(Actor actorFrom, Point3d pointTo) {
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

    public Point3d getLaserSpot()
    {
        return laserSpotPos;
    }

    public boolean setLaserSpot(Point3d p3d)
    {
        laserSpotPos = p3d;
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
                if(FM.actor == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: ON");
            }
            else
            {
                if(FM.actor == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: OFF");
                hold = false;
                holdFollow = false;
                actorFollowing = null;
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

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
        if(FLIR)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Engaged");
                FM.AP.setStabAltitude(2000F);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Released");
                FM.AP.setStabAltitude(false);
            }
    }

    public void typeBomberAdjAltitudeMinus()
    {
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
        radarrange++;
        if(radarrange > 5)
            radarrange = 5;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeBomberAdjSpeedMinus()
    {
        radarrange--;
        if(radarrange < 1)
            radarrange = 1;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeBomberUpdate(float f)
    {
        if((double)Math.abs(FM.Or.getKren()) > 4.5D)
        {
            fSightCurReadyness -= 0.0666666F * f;
            if(fSightCurReadyness < 0.0F)
                fSightCurReadyness = 0.0F;
        }
        if(fSightCurReadyness < 1.0F)
            fSightCurReadyness += 0.0333333F * f;
        else
        if(bSightAutomation)
        {
            fSightCurDistance -= toMetersPerSecond(fSightCurSpeed) * f;
            if(fSightCurDistance < 0.0F)
            {
                fSightCurDistance = 0.0F;
                typeBomberToggleAutomation();
            }
            fSightCurForwardAngle = (float)Math.toDegrees(Math.atan(fSightCurDistance / toMeters(fSightCurAltitude)));
            if((double)fSightCurDistance < (double)toMetersPerSecond(fSightCurSpeed) * Math.sqrt(toMeters(fSightCurAltitude) * 0.2038736F))
                bSightBombDump = true;
            if(bSightBombDump)
                if(FM.isTick(3, 0))
                {
                    if(FM.CT.Weapons[3] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets())
                    {
                        FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                } else
                {
                    FM.CT.WeaponControl[3] = false;
                }
        }
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte((bSightAutomation ? 1 : 0) | (bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(fSightCurDistance);
        netmsgguaranted.writeByte((int)fSightCurForwardAngle);
        netmsgguaranted.writeByte((int)((fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(fSightCurAltitude);
        netmsgguaranted.writeByte((int)(fSightCurSpeed / 2.5F));
        netmsgguaranted.writeByte((int)(fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        int i = netmsginput.readUnsignedByte();
        bSightAutomation = (i & 1) != 0;
        bSightBombDump = (i & 2) != 0;
        fSightCurDistance = netmsginput.readFloat();
        fSightCurForwardAngle = netmsginput.readUnsignedByte();
        fSightCurSideslip = -3F + (float)netmsginput.readUnsignedByte() / 33.33333F;
        fSightCurAltitude = netmsginput.readFloat();
        fSightCurSpeed = (float)netmsginput.readUnsignedByte() * 2.5F;
        fSightCurReadyness = (float)netmsginput.readUnsignedByte() / 200F;
    }

    private void laser(Point3d point3d)
    {
        point3d.z = World.land().HQ(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y);
        Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y, ((Tuple3d) (point3d)).z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
        eff3dactor.postDestroy(Time.current() + 1500L);
    }

    private void FLIR()
    {
        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if(((actor instanceof Aircraft) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric) || (actor instanceof TankGeneric)) && !(actor instanceof StationaryGeneric) && !(actor instanceof TypeLaserDesignator) && actor.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 20000D)
            {
                Point3d point3d = new Point3d();
                Orient orient = new Orient();
                actor.pos.getAbs(point3d, orient);
                Eff3DActor eff3dactor = Eff3DActor.New(actor, null, new Loc(), 1.0F, "effects/Explodes/Air/Zenitka/Germ_88mm/Glow.eff", 1.0F);
                eff3dactor.postDestroy(Time.current() + 1500L);
                LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
                lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
                if(actor instanceof Aircraft)
                    lightpointactor.light.setEmit(8F, 50F);
                else
                if(!(actor instanceof ArtilleryGeneric))
                    lightpointactor.light.setEmit(5F, 30F);
                else
                    lightpointactor.light.setEmit(3F, 10F);
                ((Actor) (eff3dactor)).draw.lightMap().put("light", lightpointactor);
            }
        }

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

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.AS.wantBeaconsNet(true);
        t1 = Time.current();
        FM.CT.toggleRocketHook();
        FM.CT.bHasBombSelect = true;
        FM.CT.bHasSideDoor = false;
        FM.CT.bHasAntiColLights = true;
        if(!noFL)
            FM.CT.bHasFormationLights = true;
        if(this instanceof A_6A || this instanceof A_6A_tanker || this instanceof KA_6D)
            bGen1st = true;
        else
            bGen1st = false;
        if(this instanceof A_6E || this instanceof A_6Eswip)
            bHasLaser = true;
        else
            bHasLaser = false;
        if(this instanceof EA_6B || this instanceof EA_6BicapII)
            bEA6B = true;
        else
            bEA6B = false;
        stockDragAirbrake = FM.Sq.dragAirbrakeCx;
        nonAileronTimer = Time.current();
        nonAileronTimerSet = false;
    //    if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))
    //    {
          //  FM.Sq.squareElevators += 0.5F;   //  FM.Sq.squareElevators += 2.0F;
          //  FM.Sq.liftStab += 0.5F;          //  FM.Sq.liftStab += 2.0F;
    //    }
        rwrUtils.onAircraftLoaded();
        if(RWR_GENNUM == 0)
            rwrUtils.setLockTone("aircraft.APR25AAA", "aircraft.APR25S75EbandLock", "aircraft.APR25S75EbandLock", "APR25AAA.wav", "APR25S75EbandLock.wav", "APR25S75EbandLock.wav");
        else if(RWR_GENNUM == 1)
            rwrUtils.setLockTone("aircraft.usRWRScan", "aircraft.usRWRLock", "aircraft.usRWRLaunchWarningMissileMissile", "aircraft.usRWRThreatNew");
    }

    public void missionStarting()
    {
        super.missionStarting();

        if((this instanceof EA_6B) || (this instanceof EA_6BicapII))
            OxygenMiliLitter = 30000F;
        else
            OxygenMiliLitter = 10000F;
        bOxygenUsing = false;
        LastOxygenUseTime = -1L;

        for(int i = 0; i < 2; i++)
        {
            if(FM.CT.GearControl > 0F)
                engineOilPressurePSI[i] = 0F;
            else
                engineOilPressurePSI[i] = 40F;
        }

        laserTimer = -1L;
        bLaserOn = false;
        FLIR = false;
    }

    public void checkSpolers(boolean forceFlag)
    {
        boolean SpoilerAsAileron = (FM.CT.getAileron() < -0.008F || FM.CT.getAileron() > 0.008F);

        if(!SpoilerAsAileron && oldSpoilerAsAileron && !nonAileronTimerSet)
        {
            nonAileronTimer = Time.current() + 1000L;
            nonAileronTimerSet = true;
        }

        if(FM.Gears.nOfGearsOnGr > 1 && !SpoilerAsAileron && Time.current() > nonAileronTimer && FM.CT.getWing() < 0.001F)
        {
            SpoilerBrakeControl = FM.CT.getAirBrake();
            FM.Sq.dragAirbrakeCx = stockDragAirbrake + 0.20F;
            nonAileronTimerSet = false;
        }
        else
        {
            SpoilerBrakeControl = 0.0F;
            FM.Sq.dragAirbrakeCx = stockDragAirbrake;
        }

        if(SpoilerBrakeControl != oldSpoilerBrakeControl || forceFlag)
        {
            hierMesh().chunkSetAngles("AroneLOut_D0", 0.0F, 55F * SpoilerBrakeControl, 0.0F);
            hierMesh().chunkSetAngles("AroneLIn_D0", 0.0F, 55F * SpoilerBrakeControl, 0.0F);
            hierMesh().chunkSetAngles("AroneROut_D0", 0.0F, -55F * SpoilerBrakeControl, 0.0F);
            hierMesh().chunkSetAngles("AroneRIn_D0", 0.0F, -55F * SpoilerBrakeControl, 0.0F);
        }
        oldSpoilerBrakeControl = SpoilerBrakeControl;
        oldSpoilerAsAileron = SpoilerAsAileron;
    }

    public void checkHydraulicStatus()
    {
        if(FM.EI.engines[0].getStage() < 6 && FM.EI.engines[1].getStage() < 6 && FM.Gears.nOfGearsOnGr > 0)
        {
            hasHydraulicPressure = false;
            FM.CT.bHasAileronControl = false;
            FM.CT.bHasElevatorControl = false;
            FM.CT.AirBrakeControl = 0.0F;
        } else
        if(!hasHydraulicPressure)
        {
            hasHydraulicPressure = true;
            FM.CT.bHasAileronControl = true;
            FM.CT.bHasElevatorControl = true;
            FM.CT.bHasAirBrakeControl = true;
        }
    }

    public void startCockpitSounds()
    {
        rwrUtils.setSoundEnable(true);
    }

    public void stopCockpitSounds()
    {
        rwrUtils.stopAllRWRSounds();
    }

    public void updateLLights()
    {
        super.pos.getRender(Actor._tmpLoc);
        if(lLight == null)
        {
            if(Actor._tmpLoc.getX() >= 1.0D)
            {
                lLight = new LightPointWorld[8];
                for(int i = 0; i < 8; i++)
                {
                    lLight[i] = new LightPointWorld();
                    lLight[i].setColor(1.0F, 1.0F, 1.0F);
                    lLight[i].setEmit(0.0F, 0.0F);
                    try
                    {
                        lLightHook[i] = new HookNamed(this, "_LandingLight0" + i);
                    }
                    catch(Exception exception) { }
                }

            }
        } else
        {
            for(int j = 0; j < 4; j++)
                if(FM.AS.astateLandingLightEffects[j] != null)
                {
                    for(int k = 0; k < 2; k++)
                    {
                        lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                        lLightHook[j + k * 4].computePos(this, Actor._tmpLoc, lLightLoc1);
                        lLightLoc1.get(lLightP1);
                        lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                        lLightHook[j + k * 4].computePos(this, Actor._tmpLoc, lLightLoc1);
                        lLightLoc1.get(lLightP2);
                        Engine.land();
                        if(Landscape.rayHitHQ(lLightP1, lLightP2, lLightPL))
                        {
                            lLightPL.z++;
                            lLightP2.interpolate(lLightP1, lLightPL, 0.95F);
                            lLight[j + k * 4].setPos(lLightP2);
                            float f = (float)lLightP1.distance(lLightPL);
                            float f1 = f * 0.5F + 60F;
                            float f2 = 0.7F - (0.8F * f * lightTime) / 2000F;
                            lLight[j + k * 4].setEmit(f2 / 2F, f1 / 2F);
                        } else
                        {
                            lLight[j + k * 4].setEmit(0.0F, 0.0F);
                        }
                    }
                } else
                if(lLight[j].getR() != 0.0F)
                    for(int k = 0; k < 2; k++)
                        lLight[j + k * 4].setEmit(0.0F, 0.0F);

        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(counter++ % 17 == 0)
            InertialNavigation();
        if(counter++ % 5 == 0)
            GroundRadar();
        if(counter++ % 5 == 0)
            NavyRadar();
        boolean bSideDoorOccupy = false;
        if(FM.CT.bHasSideDoor && FM.CT.bMoveSideDoor)
        {
            if(FM.CT.getCockpitDoor() > 0.0F && FM.CT.getCockpitDoor() < 1.0F)
                bSideDoorOccupy = true;
        }
        if(!bSideDoorOccupy)
            FM.CT.setActiveDoor( 1 );     // setActiveDoor <== not SIDE_DOOR
        if(FM.Gears.onGround() && !bSideDoorOccupy)
        {
            if(FM.CT.getCockpitDoor() > 0.95F)
            {
                hierMesh().chunkVisible("HMask1_D0", false);
                hierMesh().chunkVisible("HMask2_D0", false);
                if(bEA6B)
                {
                    hierMesh().chunkVisible("HMask3_D0", false);
                    hierMesh().chunkVisible("HMask4_D0", false);
                }
            }
            else
            {
                hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
                hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
                if(bEA6B)
                {
                    hierMesh().chunkVisible("HMask3_D0", hierMesh().isChunkVisible("Pilot3_D0"));
                    hierMesh().chunkVisible("HMask4_D0", hierMesh().isChunkVisible("Pilot4_D0"));
                }
            }

            if(FM.brakeShoe && FM.CT.getCockpitDoor() > 0.4F)
            {
                hierMesh().chunkSetAngles("StairsL_D0", 0.0F, -180.0F, 0.0F);
                hierMesh().chunkSetAngles("StairsR_D0", 0.0F, 180.0F, 0.0F);
            }
            else
            {
                hierMesh().chunkSetAngles("StairsL_D0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("StairsR_D0", 0.0F, 0.0F, 0.0F);
            }
        }
        if(FM.Gears.onGround() && FM.brakeShoe  && !bEA6B)
        {
            FM.CT.bHasSideDoor = true;
        }
        else
        {
            if(FM.CT.bHasSideDoor)
            {
                FM.CT.setActiveDoor( 2 );     // setActiveDoor <== SIDE_DOOR
                FM.CT.cockpitDoorControl = 0.0F;
                if(!bSideDoorOccupy)
                    FM.CT.bHasSideDoor = false;
            }
        }
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            Vector3d vector3d = FM.getVflow();
            mn = (float)vector3d.lengthSquared();
            mn = (float)Math.sqrt(mn);
            A_6 a_6 = this;
            float f1 = mn;
            World.cur().getClass();
            a_6.mn = f1 / Atmosphere.sonicSpeed((float)((Tuple3d) (FM.Loc)).z);
            if(mn >= 0.9F && (double)mn < 1.1000000000000001D)
                ts = true;
            else
                ts = false;
            ft = World.getTimeofDay() % 0.01F;
            if(ft == 0.0F)
                UpdateLightIntensity();
        }
        if(bHasLaser)
        {
            if(FLIR)
                FLIR();
            if(!FLIR)
                FM.AP.setStabAltitude(false);
        }
        if((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver))
            if(FM.AP.way.isLanding() && FM.getSpeed() > FM.VmaxFLAPS && FM.getSpeed() > FM.AP.way.curr().getV() * 1.4F)
            {
                if(FM.CT.AirBrakeControl != 1.0F)
                    FM.CT.AirBrakeControl = 1.0F;
            }
            else if(((Maneuver) FM).get_maneuver() == 25 && FM.AP.way.isLanding() && FM.getSpeed() < FM.VmaxFLAPS * 1.16F)
            {
                if(FM.getSpeed() > FM.VminFLAPS * 0.5F && FM.Gears.onGround())
                {
                    if(FM.CT.AirBrakeControl != 1.0F)
                        FM.CT.AirBrakeControl = 1.0F;
                }
                else if(FM.CT.AirBrakeControl != 0.0F)
                    FM.CT.AirBrakeControl = 0.0F;
            }
            else if(((Maneuver) FM).get_maneuver() == 66)
            {
                if(FM.CT.AirBrakeControl != 0.0F)
                    FM.CT.AirBrakeControl = 0.0F;
            }
            else if(((Maneuver) FM).get_maneuver() == 7)
            {
                if(FM.CT.AirBrakeControl != 1.0F)
                    FM.CT.AirBrakeControl = 1.0F;
            }
            else if(hasHydraulicPressure && FM.CT.AirBrakeControl != 0.0F)
                FM.CT.AirBrakeControl = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 0.91F || FM.EI.engines[1].getThrustOutput() > 0.91F)
            FM.CT.AirBrakeControl = 0.0F;

        formationlights();
        if(!FM.isPlayers())
            FM.CT.bAntiColLights = FM.AS.bNavLightsOn;

        if(FM.getAltitude() > 1500F && OxygenMiliLitter > 0F)
            checkOxygenUse();
        else
        {
            bOxygenUsing = false;
            LastOxygenUseTime = -1L;
        }
    }

    private boolean InertialNavigation()
    {
        Point3d point3d = new Point3d();
        pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if(aircraft.getSpeed(vector3d) > 20D && ((Actor) (aircraft)).pos.getAbsPoint().z >= 150D && (aircraft instanceof A_6))
        {
            pos.getAbs(point3d);
            if(Mission.cur() != null)
            {
                error++;
                if(error > 99)
                    error = 1;
            }
            int i = error;
            int j = i;
            Random random = new Random();
            int k = random.nextInt(100);
            if(k > 50)
                i -= i * 2;
            k = random.nextInt(100);
            if(k > 50)
                j -= j * 2;
            double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
            double d1 = (Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft)).pos.getAbsPoint().x) / 1000D / 10D;
            double d2 = (Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().y) / 1000D / 10D;
            char c = (char)(int)(65D + Math.floor((d1 / 676D - Math.floor(d1 / 676D)) * 26D));
            char c1 = (char)(int)(65D + Math.floor((d1 / 26D - Math.floor(d1 / 26D)) * 26D));
            new String();
            String s;
            if(d > 260D)
                s = "" + c + c1;
            else
                s = "" + c1;
            new String();
            int l = (int)Math.ceil(d2);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "INS: " + s + "-" + l);
        }
        return true;
    }
    
    
    private boolean GroundRadar()
    {
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
        {
            Actor actor = (Actor)entry.getValue();
            if(((actor instanceof ArtilleryGeneric) || (actor instanceof Wagon) || (actor instanceof TankGeneric) || (actor instanceof CarGeneric) || (actor instanceof PlaneGeneric)) && actor != World.getPlayerAircraft())
            {
                super.pos.getAbs(point3d);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
                new String();
                new String();
                int k = (int)(Math.floor(((Tuple3d) (actor.pos.getAbsPoint())).z * 0.10000000000000001D) * 10D);
                int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 6000D) * 10D);
                double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                new String();
                double d7 = d3 - d;
                double d8 = d4 - d1;
                float f = 57.32484F * (float)Math.atan2(d8, -d7);
                int i1 = (int)(Math.floor((int)f) - 90D);
                if(i1 < 0)
                    i1 += 360;
                int j1 = i1 - i;
                double d9 = d - d3;
                double d10 = d1 - d4;
                double d11 = Math.sqrt(d6 * d6);
                int k1 = (int)Math.ceil(Math.sqrt(d10 * d10 + d9 * d9));
                float f1 = 57.32484F * (float)Math.atan2(k1, d11);
                int l1 = (int)(Math.floor((int)f1) - 90D);
                if(l1 < 0)
                    l1 += 360;
                int i2 = l1 - j;
                k1 = (int)((double)k1 / 1000D);
                int j2 = (int)Math.ceil(k1);
                String s = "Contact, ";
                String s1 = "Unknown, ";
                byte byte0 = 9;
                if(k1 <= byte0 && i2 >= 210 && i2 <= 270 && Math.sqrt(j1 * j1) <= 20D)
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Ground units bearing " + i1 + "\260" + ", range " + j2 + " km");
            }
        }
        return true;
    }
        
        
    private boolean NavyRadar()
    {
        Point3d point3d = new Point3d();
        super.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (((Actor) (aircraft)).pos.getAbsPoint())).z;
        int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
        if(i < 0)
            i += 360;
        int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
        if(j < 0)
            j += 360;
        for(java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry))
        {
            Actor actor = (Actor)entry.getValue();
            if(((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && actor != World.getPlayerAircraft())
            //if(((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && actor.getArmy() != World.getPlayerArmy() && actor != World.getPlayerAircraft())
            {
                super.pos.getAbs(point3d);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
                new String();
                new String();
                int k = (int)(Math.floor(((Tuple3d) (actor.pos.getAbsPoint())).z * 0.10000000000000001D) * 10D);
                int l = (int)(Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D) * 10D);
                double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
                new String();
                double d7 = d3 - d;
                double d8 = d4 - d1;
                float f = 57.32484F * (float)Math.atan2(d8, -d7);
                int i1 = (int)(Math.floor((int)f) - 90D);
                if(i1 < 0)
                    i1 += 360;
                int j1 = i1 - i;
                double d9 = d - d3;
                double d10 = d1 - d4;
                double d11 = Math.sqrt(d6 * d6);
                int k1 = (int)Math.ceil(Math.sqrt(d10 * d10 + d9 * d9));
                float f1 = 57.32484F * (float)Math.atan2(k1, d11);
                int l1 = (int)(Math.floor((int)f1) - 90D);
                if(l1 < 0)
                    l1 += 360;
                int i2 = l1 - j;
                k1 = (int)((double)k1 / 1000D);
                int j2 = (int)Math.ceil(k1);
                String s = "Contact, ";
                String s1 = "Unknown, ";
                byte byte0 = 9;
                if((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric))
                    byte0 = 100;
                s1 = "surface unit, ";
                if(k1 <= byte0 && i2 >= 210 && i2 <= 270 && Math.sqrt(j1 * j1) <= 20D)
                    HUD.log(AircraftHotKeys.hudLogWeaponId,"                         " + s + s1 + " bearing " + i1 + "\260" + ", range " + j2 + " km");
            }
        }

        return true;
    }

        

    public void setTimer(int i)
    {
        Random random = new Random();
        Timer1 = (float)((double)random.nextInt(i) * 0.10000000000000001D);
        Timer2 = (float)((double)random.nextInt(i) * 0.10000000000000001D);
    }

    public void resetTimer(float f)
    {
        Timer1 = f;
        Timer2 = f;
    }


    private final void UpdateLightIntensity()
    {
        if(World.getTimeofDay() >= 6F && World.getTimeofDay() < 7F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 6F, 7F, 1.0F, 0.1F);
        else
        if(World.getTimeofDay() >= 18F && World.getTimeofDay() < 19F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 18F, 19F, 0.1F, 1.0F);
        else
        if(World.getTimeofDay() >= 7F && World.getTimeofDay() < 18F)
            lightTime = 0.1F;
        else
            lightTime = 1.0F;
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset()
    {
    }

    public void typeFighterAceMakerAdjDistancePlus()
    {
        k14Distance += 10F;
        if(k14Distance > 800F)
            k14Distance = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if(k14Distance < 200F)
            k14Distance = 200F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    }

    public void typeFighterAceMakerAdjSideslipPlus()
    {
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte(k14Mode);
        netmsgguaranted.writeByte(k14WingspanType);
        netmsgguaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        k14Mode = netmsginput.readByte();
        k14WingspanType = netmsginput.readByte();
        k14Distance = netmsginput.readFloat();
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("p1"))
                {
                    getEnergyPastArmor(13.350000381469727D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(8.77F, shot);
                else
                if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                }
            } else
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch(i)
                {
                case 1: // '\001'
                case 2: // '\002'
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(1.1F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3: // '\003'
                case 4: // '\004'
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;
                }
            } else
            if(s.startsWith("xxeng1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 20F)
                {
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 24000F)
                {
                    FM.AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                s.endsWith("exht");
            }
            if(s.startsWith("xxtank"))
            {
                int j = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(FM.AS.astateTankStates[j] == 0)
                    {
                        debuggunnery("Fuel Tank (" + j + "): Pierced..");
                        FM.AS.hitTank(shot.initiator, j, 1);
                        FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        FM.AS.hitTank(shot.initiator, j, 2);
                        debuggunnery("Fuel Tank (" + j + "): Hit..");
                    }
                }
            } else
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 1 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D2", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 1 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D2", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 1 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D2", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 1 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D2", shot.initiator);
                }
            } else
            if(s.startsWith("xxhyd"))
                FM.AS.setInternalDamage(shot.initiator, 3);
            else
            if(s.startsWith("xxpnm"))
                FM.AS.setInternalDamage(shot.initiator, 1);
        } else
        {
            if(s.startsWith("xcockpit"))
            {
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                getEnergyPastArmor(0.05F, shot);
            }
            if(s.startsWith("xcf"))
            {
                hitChunk("CF", shot);
                if(chunkDamageVisible("CF") > 1)
                {
                    hierMesh().chunkVisible("StairsL_D0", false);
                    hierMesh().chunkVisible("StairsL_D0", false);
                }
            }
            else
            if(s.startsWith("xnose"))
                hitChunk("Nose", shot);
            else
            if(s.startsWith("xtail"))
            {
                if(chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", shot);
            } else
            if(s.startsWith("xkeel"))
            {
                if(chunkDamageVisible("Keel1") < 2)
                    hitChunk("Keel1", shot);
            } else
            if(s.startsWith("xrudder"))
                hitChunk("Rudder1", shot);
            else
            if(s.startsWith("xstab"))
            {
                if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                    hitChunk("StabL", shot);
                if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
                    hitChunk("StabR", shot);
            } else
            if(s.startsWith("xvator"))
            {
                if(s.startsWith("xvatorl"))
                    hitChunk("VatorL", shot);
                if(s.startsWith("xvatorr"))
                    hitChunk("VatorR", shot);
            } else
            if(s.startsWith("xwing"))
            {
                if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                    hitChunk("WingLIn", shot);
                if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                    hitChunk("WingRIn", shot);
                if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                    hitChunk("WingLMid", shot);
                if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                    hitChunk("WingRMid", shot);
                if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                    hitChunk("WingLOut", shot);
                if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                    hitChunk("WingROut", shot);
            } else
            if(s.startsWith("xarone"))
            {
                if(s.startsWith("xaronel"))
                    hitChunk("AroneL", shot);
                if(s.startsWith("xaroner"))
                    hitChunk("AroneR", shot);
            } else
            if(s.startsWith("xgear"))
            {
                if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
                {
                    debuggunnery("Hydro System: Disabled..");
                    FM.AS.setInternalDamage(shot.initiator, 0);
                }
                if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
                {
                    debuggunnery("Undercarriage: Stuck..");
                    FM.AS.setInternalDamage(shot.initiator, 3);
                }
            } else
            if(s.startsWith("xpilot") || s.startsWith("xhead"))
            {
                byte byte0 = 0;
                int k;
                if(s.endsWith("a"))
                {
                    byte0 = 1;
                    k = s.charAt(6) - 49;
                } else
                if(s.endsWith("b"))
                {
                    byte0 = 2;
                    k = s.charAt(6) - 49;
                } else
                {
                    k = s.charAt(5) - 49;
                }
                hitFlesh(k, shot, byte0);
            }
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19: // '\023'
            FM.EI.engines[0].setEngineDies(actor);
            return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    private void bailout()
    {
        if(overrideBailout)
            if(FM.AS.astateBailoutStep >= 0 && FM.AS.astateBailoutStep < 2)
            {
                if(FM.getSpeedKMH() < 15F)
                {
                    FM.CT.cockpitDoorControl = 1.0F;
                    if(FM.CT.cockpitDoorControl > 0.5F && FM.CT.getCockpitDoor() > 0.5F)
                        FM.AS.astateBailoutStep = 11;
                } else
                {
                    FM.AS.astateBailoutStep = 2;
                }
            } else
            if(FM.AS.astateBailoutStep >= 2 && FM.AS.astateBailoutStep <= 3)
            {
                switch(FM.AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(FM.CT.cockpitDoorControl < 0.5F || FM.getSpeedKMH() > 15F)
                        doRemoveBlister1();
                    break;

                case 3: // '\003'
                    lTimeNextEject = Time.current() + 1000L;
                    if(FM.CT.cockpitDoorControl < 0.5F || FM.getSpeedKMH() > 15F)
                        doRemoveBlister2();
                    break;
                }
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                FM.AS.astateBailoutStep = (byte)(FM.AS.astateBailoutStep + 1);
                if(FM.AS.astateBailoutStep == 4)
                    FM.AS.astateBailoutStep = 11;
            } else
            if(FM.AS.astateBailoutStep >= 11 && FM.AS.astateBailoutStep <= 19)
            {
                byte byte0 = FM.AS.astateBailoutStep;
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                FM.AS.astateBailoutStep = (byte)(FM.AS.astateBailoutStep + 1);
                if((FM instanceof Maneuver) && ((Maneuver)FM).get_maneuver() != 44)
                {
                    World.cur();
                    if(FM.AS.actor != World.getPlayerAircraft())
                        ((Maneuver)FM).set_maneuver(44);
                }
                doRemoveBodyFromPlane(byte0 - 10);
                if(FM.getSpeedKMH() > 15F)
                {
                    if(byte0 == 11 || byte0 == 12 || ((byte0 == 13 || byte0 == 14)&& bEA6B))
                        doEjectCatapult(byte0 - 10);
                    lTimeNextEject = Time.current() + 1000L;
                    if((!bEA6B && byte0 > 11) || byte0 > 13) {
                        FM.AS.astateBailoutStep = 51;
                        overrideBailout = false;
                        FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                    EventLog.onBailedOut(this, byte0 - 11);
                    FM.AS.astatePilotStates[byte0 - 11] = 99;
                    return;
                }
                else
                {
                    if(FM.AS.astatePilotStates[byte0 - 11] < 99)
                    {
                        FM.AS.astatePilotStates[byte0 - 11] = 100;
                        if(FM.AS.isMaster())
                        {
                            try
                            {
                              Hook localHook = this.findHook("_ExternalBail0" + (byte0 - 10));

                              if(localHook != null)
                              {
                                  Loc localLoc = new Loc(0.0D, 0.0D, 0.0D, World.Rnd().nextFloat(-45.0F, 45.0F), 0.0F, 0.0F);

                                  localHook.computePos(this, this.pos.getAbs(), localLoc);

                                  new Paratrooper(this, this.getArmy(), byte0 - 11, localLoc, this.FM.Vwld);

                                  if((byte0 > 10) && (byte0 <= 19))
                                  {
                                      EventLog.onBailedOut(this, byte0 - 11);
                                  }
                              }
                            } catch (Exception localException) {
                            } finally {
                            }
                            if((FM.AS.astatePilotStates[byte0 - 11] == 19) && (this == World.getPlayerAircraft()) && (!World.isPlayerGunner()) && (FM.brakeShoe))
                            {
                                MsgDestroy.Post(Time.current() + 1000L, this);
                            }
                        }
                        FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        FM.CT.bHasAileronControl = false;
                        FM.CT.bHasRudderControl = false;
                        FM.CT.bHasElevatorControl = false;
                        if((!bEA6B && byte0 > 11) || byte0 > 13)
                        {
                            FM.AS.astateBailoutStep = 51;
                            overrideBailout = false;
                            FM.AS.bIsAboutToBailout = true;
                            ejectComplete = true;
                            if((this == World.getPlayerAircraft()) && (!World.isPlayerGunner()) && (FM.brakeShoe))
                            {
                                MsgDestroy.Post(Time.current() + 1000L, this);
                            }
                        }
                        return;
                    }
                }
                EventLog.type("astatePilotStates[" + (byte0 - 11) + "]=" + FM.AS.astatePilotStates[byte0 - 11]);
            }
    }

    private final void doRemoveBlister1()
    {
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1 && ((FlightModelMain) (super.FM)).AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            if(!bHasSK1Seat)
            {
                Wreckage localWreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
                localWreckage.collide(false);
                Vector3d localVector3d = new Vector3d();
                localVector3d.set(((FlightModelMain) (super.FM)).Vwld);
                localWreckage.setSpeed(localVector3d);
            }
        }
    }

    private final void doRemoveBlister2()
    {
        if(hierMesh().chunkFindCheck("Blister2_D0") != -1 && ((FlightModelMain) (super.FM)).AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister2_D0");
            if(!bHasSK1Seat)
            {
                Wreckage localWreckage = new Wreckage(this, hierMesh().chunkFind("Blister2_D0"));
                localWreckage.collide(false);
                Vector3d localVector3d = new Vector3d();
                localVector3d.set(((FlightModelMain) (super.FM)).Vwld);
                localWreckage.setSpeed(localVector3d);
            }
        }
    }

    public void doEjectCatapult(final int i)
    {
        new MsgAction(false, this) {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = bGen1st ? new Vector3d(0.0D, 0.0D, 40D - (double) i * 7D):
                                                  new Vector3d(0.0D, 0.0D, 80D - (double) i * 8D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat0" + i);
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(10, loc, vector3d, aircraft, true);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat" + i + "_D0", false);
        FM.setTakenMortalDamage(true, null);
        FM.CT.WeaponControl[0] = false;
        FM.CT.WeaponControl[1] = false;
        FM.CT.bHasAileronControl = false;
        FM.CT.bHasRudderControl = false;
        FM.CT.bHasElevatorControl = false;
    }

    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode != 1)
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
        {
            k14Distance = 200F;
            hunted = War.GetNearestEnemyAircraft(FM.actor, 2700F, 9);
        }
        if(hunted != null)
        {
            k14Distance = (float)FM.actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
            if(k14Distance > 800F)
                k14Distance = 800F;
            else
            if(k14Distance < 200F)
                k14Distance = 200F;
        }
    }

    public float getAirPressure(float f)
    {
        float f1 = 1.0F - (0.0065F * f) / 288.15F;
        float f2 = 5.255781F;
        return 101325F * (float)Math.pow(f1, f2);
    }

    public float getAirPressureFactor(float f)
    {
        return getAirPressure(f) / 101325F;
    }

    public float getAirDensity(float f)
    {
        return (getAirPressure(f) * 0.0289644F) / (8.31447F * (288.15F - 0.0065F * f));
    }

    public float getAirDensityFactor(float f)
    {
        return getAirDensity(f) / 1.225F;
    }

    public float getMachForAlt(float f)
    {
        f /= 1000F;
        int i = 0;
        for(i = 0; i < TypeSupersonic.fMachAltX.length - 1; i++)
            if(TypeSupersonic.fMachAltX[i] > f)
                break;

        if(i == 0)
        {
            return TypeSupersonic.fMachAltY[0];
        } else
        {
            float f1 = TypeSupersonic.fMachAltY[i - 1];
            float f2 = TypeSupersonic.fMachAltY[i] - f1;
            float f3 = TypeSupersonic.fMachAltX[i - 1];
            float f4 = TypeSupersonic.fMachAltX[i] - f3;
            float f5 = (f - f3) / f4;
            return f1 + f2 * f5;
        }
    }

    public float calculateMach()
    {
        return FM.getSpeedKMH() / getMachForAlt(FM.getAltitude());
    }

    public void soundbarier()
    {
        float f = getMachForAlt(FM.getAltitude()) - FM.getSpeedKMH();
        if(f < 0.5F)
            f = 0.5F;
        float f1 = FM.getSpeedKMH() - getMachForAlt(FM.getAltitude());
        if(f1 < 0.5F)
            f1 = 0.5F;
        if(calculateMach() <= 1.0F)
        {
            FM.VmaxAllowed = FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if(calculateMach() >= 1.0F)
        {
            FM.VmaxAllowed = FM.getSpeedKMH() + f1;
            isSonic = true;
        }
        if(FM.VmaxAllowed > 1500F)
            FM.VmaxAllowed = 1500F;
        if(isSonic && SonicBoom < 1.0F)
        {
            super.playSound("aircraft.SonicBoom", true);
            super.playSound("aircraft.SonicBoomInternal", true);
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
            if(Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(FM.getAltitude()))
                shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            SonicBoom = 1.0F;
        }
        if(calculateMach() > 1.01F || calculateMach() < 1.0F)
            Eff3DActor.finish(shockwave);
    }

    public void engineSurge(float f)
    {
        if(FM.AS.isMaster())
        {
            for(int i = 0; i < 1; i++)
                if(curthrl == -1F)
                {
                    curthrl = oldthrl = FM.EI.engines[i].getControlThrottle();
                } else
                {
                    curthrl = FM.EI.engines[i].getControlThrottle();
                    if(curthrl < 1.05F)
                    {
                        if((curthrl - oldthrl) / f > 20F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                        {
                            if(FM.actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.01D * (double)(FM.EI.engines[i].getRPM() / 1000F);
                            FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                                FM.AS.hitEngine(this, i, 100);
                            if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                                FM.EI.engines[i].setEngineDies(this);
                        }
                        if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6)
                        {
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage += 0.001D * (double)(FM.EI.engines[i].getRPM() / 1000F);
                            FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage);
                            if(World.Rnd().nextFloat() < 0.4F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                            {
                                if(FM.actor == World.getPlayerAircraft())
                                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                                FM.EI.engines[i].setEngineStops(this);
                            } else
                            if(FM.actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                        }
                    }
                    oldthrl = curthrl;
                }

        }
    }

    public void update(float f)
    {
        if(FM.getSpeedKMH() > 700F && FM.CT.bHasFlapsControl)
        {
            FM.CT.FlapsControl = 0.0F;
            FM.CT.bHasFlapsControl = false;
        } else
        {
            FM.CT.bHasFlapsControl = true;
        }
        if((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete)
        {
            overrideBailout = true;
            FM.AS.bIsAboutToBailout = false;
            if(Time.current() > lTimeNextEject)
                bailout();
        }
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            for(int i = 0; i < 2; i++)
            {
                if(FM.EI.engines[i].getThrustOutput() > 0.40F && FM.EI.engines[i].getStage() == 6)
                {
                    if(FM.EI.engines[i].getThrustOutput() > 0.97F)
                        FM.AS.setSootState(this, i, 5);
                    else
                    if(FM.EI.engines[i].getThrustOutput() > 0.91F && FM.EI.engines[i].getThrustOutput() < 0.971F)
                        FM.AS.setSootState(this, i, 4);
                    else
                    if(FM.EI.engines[i].getThrustOutput() > 0.84F && FM.EI.engines[i].getThrustOutput() < 0.911F)
                        FM.AS.setSootState(this, i, 3);
                    else
                    if(FM.EI.engines[i].getThrustOutput() > 0.58F && FM.EI.engines[i].getThrustOutput() < 0.841F)
                        FM.AS.setSootState(this, i, 2);
                    else
                        FM.AS.setSootState(this, i, 1);
                } else
                {
                    FM.AS.setSootState(this, i, 0);
                }
                setExhaustFlame(Math.round(Aircraft.cvt(FM.EI.engines[i].getThrustOutput(), 0.86F, 1.04F, 0.0F, 12F)), i);
            }
            if(FM instanceof RealFlightModel)
                umn();
        }
        if(obsMove < obsMoveTot && !bObserverKilled && !FM.AS.isPilotParatrooper(1))
        {
            if(obsMove < 0.2F || obsMove > obsMoveTot - 0.2F)
                obsMove += 0.29999999999999999D * (double)f;
            else
            if(obsMove < 0.1F || obsMove > obsMoveTot - 0.1F)
                obsMove += 0.15F;
            else
                obsMove += 1.2D * (double)f;
            obsLookAzimuth = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsAzimuthOld, obsAzimuth);
            obsLookElevation = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsElevationOld, obsElevation);
            hierMesh().chunkSetAngles("Head2_D0", 0.0F, obsLookAzimuth, obsLookElevation);
        }
        if(bHasLaser)
        {
            if(FLIR)
                laser(getLaserSpot());
            updatecontrollaser();
        }
        engineSurge(f);
        typeFighterAceMakerRangeFinder();
        checkHydraulicStatus();
        soundbarier();
        rwrUtils.update();
        backfire = rwrUtils.getBackfire();
        bRadarWarning = rwrUtils.getRadarLockedWarning();
        bMissileWarning = rwrUtils.getMissileWarning();
        if(FM.CT.getArrestor() > 0.2F)
            calculateArrestor();
        super.update(f);

        if(FM.AS.isMaster() && !FM.isPlayers())
            elevatorTrimAutotune();

        computeThrust();
        computeLift();
        Flaps();
        LimitMovings();
        checkSpolers(false);
        if(FM.getSpeedKMH() > 250F)
            FM.CT.cockpitDoorControl = 0.0F;
        if(FM.getSpeedKMH() > 300F)
            FM.CT.bHasCockpitDoorControl = false;
        else
            FM.CT.bHasCockpitDoorControl = true;
        anticollight();
        computeEngineOilPressure();
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head2_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            bObserverKilled = true;
            break;

        case 2: // '\002'
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Head3_D0", false);
            hierMesh().chunkVisible("HMask3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            break;

        case 3: // '\003'
            hierMesh().chunkVisible("Pilot4_D0", false);
            hierMesh().chunkVisible("Head4_D0", false);
            hierMesh().chunkVisible("HMask4_D0", false);
            hierMesh().chunkVisible("Pilot4_D1", true);
            break;

        default:
            return;
        }
    }

    private float clamp11(float f)
    {
        if(f < -1F)
            f = -1F;
        else
        if(f > 1.0F)
            f = 1.0F;
        return f;
    }

    public void netUpdateWayPoint()
    {
        super.netUpdateWayPoint();
        if(!(FM instanceof RealFlightModel))
            return;
        else
            return;
    }

    public void doSetSootState(int i, int j)
    {
        for(int k = 0; k < 2; k++)
        {
            if(FM.AS.astateSootEffects[i][k] != null)
                Eff3DActor.finish(FM.AS.astateSootEffects[i][k]);
            FM.AS.astateSootEffects[i][k] = null;
        }

        switch(j)
        {
        case 1: // '\001'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumWtTSPD.eff", -1F);
            break;

        case 2: // '\002'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.1F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;

        case 3: // '\003'
            if(bGen1st)
                FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.6F, "3DO/Effects/Aircraft/BlackMediumBk2TSPD.eff", -1F);
            else
                FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.6F, "3DO/Effects/Aircraft/BlackMediumBk1TSPD.eff", -1F);
            break;

        case 4: // '\004'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.3F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 5: // '\005'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.8F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            break;

        }
    }

    private final void umn()
    {
        Vector3d vector3d = FM.getVflow();
        mn = (float)vector3d.lengthSquared();
        mn = (float)Math.sqrt(mn);
        A_6 a_6 = this;
        float f = mn;
        World.cur().getClass();
        a_6.mn = f / Atmosphere.sonicSpeed((float)FM.Loc.z);
        if(mn >= lteb)
            ts = true;
        else
            ts = false;
    }

    public boolean ist()
    {
        return ts;
    }

    public float gmnr()
    {
        return mn;
    }

    public boolean inr()
    {
        return ictl;
    }

    public void typeRadarGainMinus()
    {
        radarrange++;
        if(radarrange > 4)
            radarrange = 4;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeRadarGainPlus()
    {
        radarrange--;
        if(radarrange < 1)
            radarrange = 1;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeRadarRangeMinus()
    {
        radarrange++;
        if(radarrange > 4)
            radarrange = 4;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeRadarRangePlus()
    {
        radarrange--;
        if(radarrange < 1)
            radarrange = 1;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "range" + radarrange);
    }

    public void typeRadarReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public void typeRadarReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public boolean typeRadarToggleMode()
    {
        radarmode++;
        if(radarmode > 2)
            radarmode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "radar mode" + radarmode);
        return false;
    }

    public boolean typeBomberToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        if(k14Mode == 0)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Bomb");
        } else
        if(k14Mode == 1)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Gunnery");
        } else
        if(k14Mode == 2 && FM.actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Navigation");
        return true;
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
    }

    public void typeX4CAdjAttitudeMinus()
    {
        deltaTangage = -0.1F;
    }

    public void typeX4CResetControls()
    {
        deltaAzimuth = deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth()
    {
        return deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage()
    {
        return deltaTangage;
    }

    public void moveCockpitDoor(float f)
    {
        if(FM.CT.bMoveSideDoor)
        {
            hierMesh().chunkSetAngles("Nose_D0", 0.0F, 0.0F, 45F * f);
        }
        else
        {
            if(bEA6B)
            {
                hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 0.0F, 36F * f);
                hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 0.0F, 36F * f);
            }
            else
            {
                resetYPRmodifier();
                if(f < 0.05F)
                {
                    Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 0.01F);
                    Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 0.01F);
                } else
                {
                    Aircraft.xyz[1] = Aircraft.cvt(f, 0.1F, 0.99F, 0.01F, 0.92F);
                    Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.04F, 0.0F, 0.01F);
                }
                hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
            }
            if(Config.isUSE_RENDER())
            {
                if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                    Main3D.cur3D().cockpits[0].onDoorMoved(f);
                setDoorSnd(f);
            }
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.16F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.11F, 0.0F, -94F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.11F, 0.0F, 94F), 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.11F, 0.85F, 0.0F, 85F), 0.0F);
        if(f2 < 0.31F)
            hiermesh.chunkSetAngles("GearC8_D0", 0.0F, Aircraft.cvt(f2, 0.11F, 0.31F, 0.0F, -50F), 0.0F);
        else if(f2 < 0.69F)
            hiermesh.chunkSetAngles("GearC8_D0", 0.0F, Aircraft.cvt(f2, 0.31F, 0.69F, -50F, -179F), 0.0F);
        else
            hiermesh.chunkSetAngles("GearC8_D0", 0.0F, Aircraft.cvt(f2, 0.75F, 0.85F, -179F, -140F), 0.0F);
        if(f2 < 0.31F)
            hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f2, 0.11F, 0.31F, 0.0F, 17.3F), 0.0F);
        else if(f2 < 0.69F)
            hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f2, 0.31F, 0.69F, 17.3F, 85F), 0.0F);
        else if(f2 < 0.75F)
            hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f2, 0.69F, 0.75F, 85F, 71F), 0.0F);
        else
            hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(f2, 0.75F, 0.85F, 71F, -2F), 0.0F);
        resetXYZYPR();
        Aircraft.xyz[0] = Aircraft.cvt(f2, 0.15F, 0.83F, 0.21F, 0.26F);
        hiermesh.chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
        float deg = (float)Math.toDegrees(Math.acos((double)((0.742F - Aircraft.xyz[0]) / 0.742F)));
        hiermesh.chunkSetAngles("GearC71_D0", 0.0F, -deg, 0.0F);
        hiermesh.chunkSetAngles("GearC72_D0", 0.0F, deg * 2F, 0.0F);
        hiermesh.chunkSetAngles("GearC51_D0", 0.0F, Aircraft.cvt(f2, 0.75F, 0.90F, 0.0F, -25F), 0.0F);

        resetXYZYPR();
        if(f < 0.5F)
        {
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.10F, 0.20F, 0.0F, 0.15F);
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.10F, 0.5F, 0.0F, -50F);
            Aircraft.ypr[2] = Aircraft.cvt(f, 0.05F, 0.14F, 0.0F, -8F);
        }
        else if(f < 0.75F)
        {
            Aircraft.xyz[1] = 0.15F;
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.5F, 0.75F, -50F, -85F);
            Aircraft.ypr[2] = Aircraft.cvt(f, 0.5F, 0.75F, -8F, -2.8F);
        }
        else
        {
            Aircraft.xyz[1] = 0.15F;
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.75F, 1F, -85F, -105F);
            Aircraft.ypr[2] = Aircraft.cvt(f, 0.75F, 1F, -2.8F, -0.8F);
        }
        hiermesh.chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearL3_D0", Aircraft.cvt(f, 0.10F, 0.20F, 0.0F, 98F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.65F, 1F, 0F, 16.32F));
        if(f < 0.6F)
            hiermesh.chunkSetAngles("GearL51_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.4F, 0.6F, 0F, -77.4F));
        else
            hiermesh.chunkSetAngles("GearL51_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.6F, 1.0F, -77.4F, -167.627F));
        hiermesh.chunkSetAngles("GearL22_D0", 0.0F, Aircraft.cvt(f, 0.1F, 1.0F, 0.0F, 4.25F), 0.0F);
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.1F, 1.0F, 0.0F, -0.222F);
        hiermesh.chunkSetLocate("GearL23_D0", Aircraft.xyz, Aircraft.ypr);
        resetXYZYPR();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.4F, 0.8F, 0.0F, 0.51F);
        hiermesh.chunkSetLocate("GearL4_D0", Aircraft.xyz, Aircraft.ypr);
        deg = (float)Math.toDegrees(Math.acos((double)((0.96F - 0.104F - Aircraft.xyz[1]) / 0.96F)));
        deg -= (float)Math.toDegrees(Math.acos((double)((0.96F - 0.104F) / 0.96F)));
        hiermesh.chunkSetAngles("GearL41_D0", 0.0F, 0.0F, -deg);
        hiermesh.chunkSetAngles("GearL42_D0", 0.0F, 0.0F, deg * 2F);
        hiermesh.chunkSetAngles("GearL15_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.4F, 0.6F, 0F, 70F));

        hiermesh.chunkSetAngles("GearL11_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, 30F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -90F));
        hiermesh.chunkSetAngles("GearL61_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.03F, 0.1F, 0.0F, 90F));
      
        resetXYZYPR();
        if(f1 < 0.5F)
        {
            Aircraft.xyz[1] = Aircraft.cvt(f1, 0.10F, 0.20F, 0.0F, -0.15F);
            Aircraft.ypr[1] = Aircraft.cvt(f1, 0.10F, 0.5F, 0.0F, -50F);
            Aircraft.ypr[2] = Aircraft.cvt(f1, 0.05F, 0.14F, 0.0F, 8F);
        }
        else if(f1 < 0.75F)
        {
            Aircraft.xyz[1] = -0.15F;
            Aircraft.ypr[1] = Aircraft.cvt(f1, 0.5F, 0.75F, -50F, -85F);
            Aircraft.ypr[2] = Aircraft.cvt(f1, 0.5F, 0.75F, 8F, 2.8F);
        }
        else
        {
            Aircraft.xyz[1] = -0.15F;
            Aircraft.ypr[1] = Aircraft.cvt(f1, 0.75F, 1F, -85F, -105F);
            Aircraft.ypr[2] = Aircraft.cvt(f1, 0.75F, 1F, 2.8F, 0.8F);
        }
        hiermesh.chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearR3_D0", Aircraft.cvt(f1, 0.10F, 0.20F, 0.0F, -98F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.65F, 1F, 0F, 16.32F));
        if(f1 < 0.6F)
            hiermesh.chunkSetAngles("GearR51_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.4F, 0.6F, 0F, -77.4F));
        else
            hiermesh.chunkSetAngles("GearR51_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.6F, 1.0F, -77.4F, -167.627F));
        hiermesh.chunkSetAngles("GearR22_D0", 0.0F, Aircraft.cvt(f1, 0.1F, 1.0F, 0.0F, 4.25F), 0.0F);
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(f1, 0.1F, 1.0F, 0.0F, -0.222F);
        hiermesh.chunkSetLocate("GearR23_D0", Aircraft.xyz, Aircraft.ypr);
        resetXYZYPR();
        Aircraft.xyz[1] = Aircraft.cvt(f1, 0.4F, 0.8F, 0.0F, 0.51F);
        hiermesh.chunkSetLocate("GearR4_D0", Aircraft.xyz, Aircraft.ypr);
        deg = (float)Math.toDegrees(Math.acos((double)((0.96F - 0.104F - Aircraft.xyz[1]) / 0.96F)));
        deg -= (float)Math.toDegrees(Math.acos((double)((0.96F - 0.104F) / 0.96F)));
        hiermesh.chunkSetAngles("GearR41_D0", 0.0F, 0.0F, -deg);
        hiermesh.chunkSetAngles("GearR42_D0", 0.0F, 0.0F, deg * 2F);
        hiermesh.chunkSetAngles("GearR15_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.4F, 0.6F, 0F, -70F));

        hiermesh.chunkSetAngles("GearR11_D0", 0.0F, Aircraft.cvt(f1, 0.03F, 0.15F, 0.0F, -30F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.01F, 0.1F, 0.0F, 90F));
        hiermesh.chunkSetAngles("GearR61_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.03F, 0.1F, 0.0F, -90F));
    }

    protected void moveGear(float f, float f1, float f2) {
        moveGear(hierMesh(), f, f1, f2);
    }

    // ************************************************************************************************
    // Gear code for backward compatibility, older base game versions don't indepently move their gears
    public static void moveGear(HierMesh hiermesh, float f) {
        moveGear(hiermesh, f, f, f); // re-route old style function calls to new code
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        if(FM.CT.getGearC() > 0.999F)
        {
            float f = FM.Gears.gWheelSinking[2];
            resetYPRmodifier();
            Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 0.19F, 0.0F, 0.19F) + 0.26F;
            hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
            float deg = (float)Math.toDegrees(Math.acos((double)((0.742F - Aircraft.xyz[0]) / 0.742F)));
            hierMesh().chunkSetAngles("GearC71_D0", 0.0F, -deg, 0.0F);
            hierMesh().chunkSetAngles("GearC72_D0", 0.0F, deg * 2F, 0.0F);
            hierMesh().chunkSetAngles("GearC8_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.19F, -140F, -128F), 0.0F);
        }

        if(FM.CT.getGearL() > 0.999F)
        {
            float f = FM.Gears.gWheelSinking[0];
            resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 0.19F, 0.0F, 0.19F) + 0.51F;
            float deg = (float)Math.toDegrees(Math.acos((double)((0.96F - 0.104F - Aircraft.xyz[1]) / 0.96F)));
            deg -= (float)Math.toDegrees(Math.acos((double)((0.96F - 0.104F) / 0.96F)));
            hierMesh().chunkSetLocate("GearL4_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetAngles("GearL41_D0", 0.0F, 0.0F, -deg);
            hierMesh().chunkSetAngles("GearL42_D0", 0.0F, 0.0F, deg * 2F);
        }

        if(FM.CT.getGearR() > 0.999F)
        {
            float f = FM.Gears.gWheelSinking[1];
            resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 0.19F, 0.0F, 0.19F) + 0.51F;
            float deg = (float)Math.toDegrees(Math.acos((double)((0.96F - 0.104F - Aircraft.xyz[1]) / 0.96F)));
            deg -= (float)Math.toDegrees(Math.acos((double)((0.96F - 0.104F) / 0.96F)));
            hierMesh().chunkSetLocate("GearR4_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetAngles("GearR41_D0", 0.0F, 0.0F, -deg);
            hierMesh().chunkSetAngles("GearR42_D0", 0.0F, 0.0F, deg * 2F);
        }
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, 35F * f, 0.0F);
    }

    public void moveSteering(float f)
    {
        if(FM.CT.GearControl > 0.5F && FM.Gears.onGround())
            hierMesh().chunkSetAngles("GearC5_D0", 0.0F, 0.0F, -1.0F * f);
        if(FM.CT.GearControl < 0.5F || FM.Gears.isCatapultArmed())
            hierMesh().chunkSetAngles("GearC5_D0", 0.0F, 0.0F, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 24F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -24F * f, 0.0F);
    }

    protected void moveAileron(float f)
    {
        if(FM.CT.getWing() > 0.001F)
        {
            hierMesh().chunkSetAngles("AroneLIn_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneRIn_D0", 0.0F, 0.0F, 0.0F);
            if(FM.CT.getWing() > 0.80F)
            {
                hierMesh().chunkSetAngles("AroneLOut_D0", 0.0F, 6.0F, 0.0F);
                hierMesh().chunkSetAngles("AroneROut_D0", 0.0F, -6.0F, 0.0F);
            }
            else
            {
                hierMesh().chunkSetAngles("AroneLOut_D0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("AroneROut_D0", 0.0F, 0.0F, 0.0F);
            }
            return;
        }
        if(f < 0F)
        {
            hierMesh().chunkSetAngles("AroneLOut_D0", 0.0F, -55F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneLIn_D0", 0.0F, -55F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneROut_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneRIn_D0", 0.0F, 0.0F, 0.0F);
        }
        else
        {
            hierMesh().chunkSetAngles("AroneLOut_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneLIn_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneROut_D0", 0.0F, -55F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneRIn_D0", 0.0F, -55F * f, 0.0F);
        }
    }

    protected void moveFlap(float f)
    {
        if(FM.CT.getWing() < 0.001F)
        {
            resetYPRmodifier();
            Aircraft.xyz[0] = 0.346F * f;
            Aircraft.xyz[1] = -0.044F * f;
            Aircraft.xyz[2] = -0.074F * f;
            Aircraft.ypr[1] = -40F * f;
            hierMesh().chunkSetLocate("FlapLIn_D0", Aircraft.xyz, Aircraft.ypr);
            resetYPRmodifier();
            Aircraft.xyz[0] = 0.346F * f;
            Aircraft.xyz[1] = 0.044F * f;
            Aircraft.xyz[2] = -0.074F * f;
            Aircraft.ypr[1] = -40F * f;
            hierMesh().chunkSetLocate("FlapRIn_D0", Aircraft.xyz, Aircraft.ypr);
            resetYPRmodifier();
            Aircraft.xyz[0] = 0.289F * f;
            Aircraft.xyz[2] = -0.096F * f;
            Aircraft.ypr[0] = -1.063F * f;
            Aircraft.ypr[1] = -40F * f;
            Aircraft.ypr[2] = -1.5F * f;
            hierMesh().chunkSetLocate("FlapLOut_D0", Aircraft.xyz, Aircraft.ypr);
            resetYPRmodifier();
            Aircraft.xyz[0] = 0.289F * f;
            Aircraft.xyz[2] = -0.096F * f;
            Aircraft.ypr[0] = 1.063F * f;
            Aircraft.ypr[1] = -40F * f;
            Aircraft.ypr[2] = 1.5F * f;
            hierMesh().chunkSetLocate("FlapROut_D0", Aircraft.xyz, Aircraft.ypr);

            float xyz_Lin[] = { 0.0F, 0.0F, 0.0F };
            float ypr_Lin[] = { 0.0F, 0.0F, 0.0F };
            float xyz_Lout[] = { 0.0F, 0.0F, 0.0F };
            float ypr_Lout[] = { 0.0F, 0.0F, 0.0F };
            float xyz_Rin[] = { 0.0F, 0.0F, 0.0F };
            float ypr_Rin[] = { 0.0F, 0.0F, 0.0F };
            float xyz_Rout[] = { 0.0F, 0.0F, 0.0F };
            float ypr_Rout[] = { 0.0F, 0.0F, 0.0F };
            xyz_Lin[0] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.141F);
            xyz_Lin[1] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, 0.078F);
            xyz_Lin[2] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.075F);
            ypr_Lin[1] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, 27.5F);
            xyz_Lout[0] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.059F);
            xyz_Lout[1] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.09F);
            xyz_Lout[2] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.061F);
            ypr_Lout[2] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -27.5F);
            xyz_Rin[0] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, 0.141F);
            xyz_Rin[1] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, 0.078F);
            xyz_Rin[2] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.075F);
            ypr_Rin[1] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -27.5F);
            xyz_Rout[0] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, 0.059F);
            xyz_Rout[1] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.09F);
            xyz_Rout[2] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -0.061F);
            ypr_Rout[2] = Aircraft.cvt(f, 0.01F, 0.23F, 0.0F, -27.5F);
            hierMesh().chunkSetLocate("SlatLIn_D0", xyz_Lin, ypr_Lin);
            hierMesh().chunkSetLocate("SlatLOut_D0", xyz_Lout, ypr_Lout);
            hierMesh().chunkSetLocate("SlatRIn_D0", xyz_Rin, ypr_Rin);
            hierMesh().chunkSetLocate("SlatROut_D0", xyz_Rout, ypr_Rout);
        }
        else
        {
            FM.CT.FlapsControl = 0.0F;
            resetYPRmodifier();
            hierMesh().chunkSetLocate("FlapLIn_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("FlapRIn_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("FlapLOut_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("FlapROut_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("SlatLIn_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("SlatRIn_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("SlatLOut_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("SlatROut_D0", Aircraft.xyz, Aircraft.ypr);
        }
    }

    protected void moveFan(float f)
    {
    }

    protected void moveAirBrake(float f)
    {
        if(FM.CT.getWing() > 0.001F)
        {
            hierMesh().chunkSetAngles("BrakeL1_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("BrakeL2_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("BrakeR1_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("BrakeR2_D0", 0.0F, 0.0F, 0.0F);
        }
        else
        {
            hierMesh().chunkSetAngles("BrakeL1_D0", 0.0F, 0.0F, -60F * f);
            hierMesh().chunkSetAngles("BrakeL2_D0", 0.0F, 0.0F, 60F * f);
            hierMesh().chunkSetAngles("BrakeR1_D0", 0.0F, 0.0F, -60F * f);
            hierMesh().chunkSetAngles("BrakeR2_D0", 0.0F, 0.0F, 60F * f);
        }
        if(bGen1st)
        {
            hierMesh().chunkSetAngles("BrakeBR_D0", 0.0F, 50F * f, 0.0F);
            hierMesh().chunkSetAngles("BrakeBL_D0", 0.0F, -50F * f, 0.0F);
        }
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 0.0F, 70F * f);
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 146F), 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -146F), 0.0F);
    }

    public void moveWingFold(float f)
    {
        if(f < 0.001F)
        {
            moveFlap(FM.CT.FlapsControl);
            moveAirBrake(FM.CT.AirBrakeControl);
            checkSpolers(true);
        } else
        {
            resetYPRmodifier();
            hierMesh().chunkSetLocate("FlapLIn_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("FlapRIn_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("FlapLOut_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("FlapROut_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("SlatLIn_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("SlatLOut_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("SlatRIn_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetLocate("SlatROut_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetAngles("BrakeL1_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("BrakeL2_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("BrakeR1_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("BrakeR2_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneLIn_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneRIn_D0", 0.0F, 0.0F, 0.0F);
            if(f > 0.80F)
            {
                hierMesh().chunkSetAngles("AroneLOut_D0", 0.0F, 6.0F, 0.0F);
                hierMesh().chunkSetAngles("AroneROut_D0", 0.0F, -6.0F, 0.0F);
            }
            else
            {
                hierMesh().chunkSetAngles("AroneLOut_D0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("AroneROut_D0", 0.0F, 0.0F, 0.0F);
            }
        }
        moveWingFold(hierMesh(), f);
    }

    protected void moveCatLaunchBar(float f)
    {
        hierMesh().chunkSetAngles("GearC51_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -87F), 0.0F);
    }

    public void setExhaustFlame(int i, int j)
    {
        if(j == 0)
            switch(i)
            {
            case 0: // '\0'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 1: // '\001'
                hierMesh().chunkVisible("ExhaustL1", true);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 2: // '\002'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", true);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 3: // '\003'
                hierMesh().chunkVisible("ExhaustL1", true);
                hierMesh().chunkVisible("ExhaustL2", true);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                // fall through

            case 4: // '\004'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", true);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 5: // '\005'
                hierMesh().chunkVisible("ExhaustL1", true);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", true);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 6: // '\006'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", true);
                hierMesh().chunkVisible("ExhaustL3", true);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 7: // '\007'
                hierMesh().chunkVisible("ExhaustL1", true);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", true);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 8: // '\b'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", true);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", true);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 9: // '\t'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", true);
                hierMesh().chunkVisible("ExhaustL4", true);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;

            case 10: // '\n'
                hierMesh().chunkVisible("ExhaustL1", true);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", true);
                break;

            case 11: // '\013'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", true);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", true);
                break;

            case 12: // '\f'
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", true);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", true);
                break;

            default:
                hierMesh().chunkVisible("ExhaustL1", false);
                hierMesh().chunkVisible("ExhaustL2", false);
                hierMesh().chunkVisible("ExhaustL3", false);
                hierMesh().chunkVisible("ExhaustL4", false);
                hierMesh().chunkVisible("ExhaustL5", false);
                break;
            }
        if(j == 1)
            switch(i)
            {
            case 0: // '\0'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 1: // '\001'
                hierMesh().chunkVisible("ExhaustR1", true);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 2: // '\002'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", true);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 3: // '\003'
                hierMesh().chunkVisible("ExhaustR1", true);
                hierMesh().chunkVisible("ExhaustR2", true);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                // fall through

            case 4: // '\004'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", true);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 5: // '\005'
                hierMesh().chunkVisible("ExhaustR1", true);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", true);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 6: // '\006'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", true);
                hierMesh().chunkVisible("ExhaustR3", true);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 7: // '\007'
                hierMesh().chunkVisible("ExhaustR1", true);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", true);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 8: // '\b'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", true);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", true);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 9: // '\t'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", true);
                hierMesh().chunkVisible("ExhaustR4", true);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;

            case 10: // '\n'
                hierMesh().chunkVisible("ExhaustR1", true);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", true);
                break;

            case 11: // '\013'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", true);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", true);
                break;

            case 12: // '\f'
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", true);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", true);
                break;

            default:
                hierMesh().chunkVisible("ExhaustR1", false);
                hierMesh().chunkVisible("ExhaustR2", false);
                hierMesh().chunkVisible("ExhaustR3", false);
                hierMesh().chunkVisible("ExhaustR4", false);
                hierMesh().chunkVisible("ExhaustR5", false);
                break;
            }
    }

    public void computeThrust()
    {
        if(FM.EI.engines[0].getThrustOutput() > 0.95F && (double)calculateMach() < 0.32000000000000001D && FM.EI.engines[0].getStage() > 5)
            FM.producedAF.x += 9000D;
        if(FM.EI.engines[1].getThrustOutput() > 0.95F && (double)calculateMach() < 0.32000000000000001D && FM.EI.engines[1].getStage() > 5)
            FM.producedAF.x += 9000D;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && FM.EI.engines[0].getStage() == 6 && FM.EI.engines[1].getThrustOutput() < 1.001F && FM.EI.engines[1].getStage() == 6)
            if((double)f > 13.5D)
            {
                f1 = 11F;
            } else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = ((358F * f3 - 7309F * f2) + 41826F * f) / 7560F;
            }
        FM.producedAF.x -= f1 * 1000F;
    }

    public void computeLift()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float x = calculateMach();
        if(x < 0.9F)
        {
            polares.lineCyCoeff = 0.08F;
        }
        else if(x < 1.25F)
        {
            float x2 = x * x;
            polares.lineCyCoeff = 0.114286F*x2 - 0.417143F*x + 0.362857F;
            // {{0.9, 0.08}, {1.0, 0.06}, {1.25, 0.02}} 
        }
        else
        {
            polares.lineCyCoeff = 0.02F;
        }
    }

    private boolean Flaps()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        if(Flaps)
        {
            if(FM.EI.engines[0].getThrustOutput() <= 0.95F || calculateMach() >= 0.36F || calculateMach() <= 0.10F)
            {
                polares.Cy0_1 -= 0.2D;
                Flaps = false;
            }
        }
        else
        {
            if(FM.EI.engines[0].getThrustOutput() > 0.95F && calculateMach() < 0.36F && calculateMach() > 0.10F)
            {
                polares.Cy0_1 += 0.2D;
                Flaps = true;
            }
        }
        return Flaps;
    }

    private void LimitMovings()
    {
/*        if(FM.CT.FlapsControl > 0.01F)
        {
            if(FM.CT.ElevatorControl < -0.125F) FM.CT.ElevatorControl = -0.125F;
            if(FM.CT.trimElevator < -0.12F) FM.CT.trimElevator = -0.12F;
            if(FM.CT.getTrimElevatorControl() < -0.12F) FM.CT.setTrimElevatorControl(-0.12F);
//            if(FM.CT.ElevatorControl < -0.0625F) FM.CT.ElevatorControl = -0.0625F;
//            if(FM.CT.trimElevator < -0.06F) FM.CT.trimElevator = -0.06F;
//            if(FM.CT.getTrimElevatorControl() < -0.06F) FM.CT.setTrimElevatorControl(-0.06F);
        }
        else if(FM.Gears.nOfGearsOnGr > 2)
        {
            if(FM.CT.ElevatorControl < -0.125F) FM.CT.ElevatorControl = -0.125F;
            if(FM.CT.trimElevator < -0.12F) FM.CT.trimElevator = -0.12F;
            if(FM.CT.getTrimElevatorControl() < -0.12F) FM.CT.setTrimElevatorControl(-0.12F);
//            if(FM.CT.ElevatorControl < -0.0625F) FM.CT.ElevatorControl = -0.0625F;
//            if(FM.CT.trimElevator < -0.06F) FM.CT.trimElevator = -0.06F;
//            if(FM.CT.getTrimElevatorControl() < -0.06F) FM.CT.setTrimElevatorControl(-0.06F);
            if(FM.CT.ElevatorControl > 0.39584F) FM.CT.ElevatorControl = 0.39584F;
            if(FM.CT.trimElevator > 0.38F) FM.CT.trimElevator = 0.38F;
            if(FM.CT.getTrimElevatorControl() > 0.38F) FM.CT.setTrimElevatorControl(0.38F);
        }
        else
        {
            if(FM.CT.RudderControl < -0.11429F) FM.CT.RudderControl = -0.11429F;
            if(FM.CT.trimRudder < -0.11F) FM.CT.trimRudder = -0.11F;
            if(FM.CT.getTrimRudderControl() < -0.11F) FM.CT.setTrimRudderControl(-0.11F);
            if(FM.CT.RudderControl > 0.11429F) FM.CT.RudderControl = 0.11429F;
            if(FM.CT.trimRudder > 0.11F) FM.CT.trimRudder = 0.11F;
            if(FM.CT.getTrimRudderControl() > 0.11F) FM.CT.setTrimRudderControl(0.11F);
            if(FM.CT.ElevatorControl < -0.125F) FM.CT.ElevatorControl = -0.125F;
            if(FM.CT.trimElevator < -0.12F) FM.CT.trimElevator = -0.12F;
            if(FM.CT.getTrimElevatorControl() < -0.12F) FM.CT.setTrimElevatorControl(-0.12F);
//            if(FM.CT.ElevatorControl < -0.0625F) FM.CT.ElevatorControl = -0.0625F;
//            if(FM.CT.trimElevator < -0.06F) FM.CT.trimElevator = -0.06F;
//            if(FM.CT.getTrimElevatorControl() < -0.06F) FM.CT.setTrimElevatorControl(-0.06F);
            if(FM.CT.ElevatorControl > 0.39584F) FM.CT.ElevatorControl = 0.39584F;
            if(FM.CT.trimElevator > 0.39F) FM.CT.trimElevator = 0.39F;
            if(FM.CT.getTrimElevatorControl() > 0.39F) FM.CT.setTrimElevatorControl(0.39F);
        }
*/
        if(FM.CT.getPowerControl() > 1.0F)
        {
            FM.CT.setPowerControl(1.0F);
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogPowerId, "Power", new Object[] { new Integer(100) });
        }
        if(FM.EI.engines[0].getControlThrottle() > 1.0F)
            FM.EI.engines[0].setControlThrottle(1.0F);
        if(FM.EI.engines[1].getControlThrottle() > 1.0F)
            FM.EI.engines[1].setControlThrottle(1.0F);
    }

    private void elevatorTrimAutotune()
    {
        if(FM.CT.FlapsControl > 0.1F)
            FM.CT.setTrimElevatorControl(0.05F);
        else
        {
            float f0 = Aircraft.cvt(FM.getSpeedKMH(), 400F, 900F, 0.100F, -0.024F);
            float f1 = Aircraft.cvt(FM.M.mass - FM.M.massEmpty, 16000F, 6000F, 0.030F, 0.0F);
            FM.CT.setTrimElevatorControl(f0 + f1);
        }
    }

    private void anticollight()
    {
        if(FM.CT.bAntiColLights)
        {
            for(int i = 0; i < 6; i++)
            {
                if(antiColLight[i] == null)
                {
                    try
                    {
                        if(bEA6B)
                            antiColLight[i] = Eff3DActor.New(this, findHook("_AntiColLight" + Integer.toString(i + 1)), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRedFlash2.eff", -1.0F, false);
                        else
                            antiColLight[i] = Eff3DActor.New(this, findHook("_AntiColLight" + Integer.toString(i + 1)), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRedFlash.eff", -1.0F, false);
                    } catch(Exception excp) {}
                }
            }
        }
        else
        {
            for(int i = 0; i < 6; i++)
              if(antiColLight[i] != null)
              {
                  Eff3DActor.finish(antiColLight[i]);
                  antiColLight[i] = null;
              }
        }
    }

    private void formationlights()
    {
        if(noFL)
            return;

        int ws = Mission.cur().curCloudsType();
        float we = Mission.cur().curCloudsHeight() + 500F;
        if((World.getTimeofDay() <= 6.5F || World.getTimeofDay() > 18F || (ws > 4 && FM.getAltitude()<we)) && !FM.isPlayers())
        {
            FM.CT.bFormationLights = true;
        }
        if(((World.getTimeofDay() > 6.5F && World.getTimeofDay() <= 18F && ws <= 4) || (World.getTimeofDay() > 6.5F && World.getTimeofDay() <= 18F && FM.getAltitude()>we)) && !FM.isPlayers())
        {
            FM.CT.bFormationLights = false;
        }
        hierMesh().chunkVisible("SSlightcf", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SSlightwL", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SSlightwR", FM.CT.bFormationLights);
    }

    private static void resetXYZYPR()
    {
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
    }

    private void hitChunkA6(String s, Shot shot, String stockPartName) {
        if(s.lastIndexOf("_") == -1) s = s + "_D" + chunkDamageVisible(s);
        float f = shot.powerToTNT();
        if(s.endsWith("_D0") && !s.startsWith("Gear")) {
            if(f > 0.01F) f = 1.0F + (f - 0.01F) / FM.Sq.getToughness(part(stockPartName));
            else f /= 0.01F;
        } else {
            f /= FM.Sq.getToughness(part(stockPartName));
        }
        f += FM.Sq.eAbsorber[part(stockPartName)];
        int i = (int) f;
        FM.Sq.eAbsorber[part(stockPartName)] = f - (float) i;
        if(i > 0) setDamager(shot.initiator, i);
        nextDMGLevels(i, 2, s, shot.initiator);
    }

    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
    }

    private void calculateArrestor()
    {
        if(FM.Gears.arrestorVAngle != 0.0F)
        {
            float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
            arrestor = 0.8F * arrestor + 0.2F * f1;
            moveArrestorHook(arrestor);
        } else
        {
            float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
            if(f2 < 0.0F && super.FM.getSpeedKMH() > 60F)
                Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if(f2 > 0.0F && FM.CT.getArrestor() < 0.95F)
                f2 = 0.0F;
            if(f2 > 0.2F)
                f2 = 0.2F;
            if(f2 > 0.0F)
                arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
            else
                arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
            if(arrestor < 0.0F)
                arrestor = 0.0F;
            else
            if(arrestor > 1.0F)
                arrestor = 1.0F;
            moveArrestorHook(arrestor);
        }
    }

    void checkOxygenUse()
    {
        if(OxygenMiliLitter <= 0F)
        {
            OxygenMiliLitter = 0F;
            bOxygenUsing = false;
            LastOxygenUseTime = -1L;
            return;
        }

        if(!bOxygenUsing)
        {
            bOxygenUsing = true;
            LastOxygenUseTime = Time.current();
            return;
        }

        long calculatetime = Time.current() - LastOxygenUseTime;
        OxygenMiliLitter -= OxygenConsumption * FM.crew * (float)calculatetime;
        if(OxygenMiliLitter < 0F)
            OxygenMiliLitter = 0F;
        bOxygenUsing = true;
        LastOxygenUseTime = Time.current();
    }

    private void computeEngineOilPressure()
    {
        float targetPress = 0F;

        for(int i= 0; i < 2; i++)
        {
            if(FM.EI.engines[i].getRPM() < 1000F)
                targetPress = cvt(FM.EI.engines[i].getRPM(), 0F, 1000F, 0F, 40F);
            else if(FM.EI.engines[i].getRPM() < 4000F)
                targetPress = cvt(FM.EI.engines[i].getRPM(), 1000F, 4000F, 40F, 50F);
            else
                targetPress = cvt(FM.EI.engines[i].getRPM(), 4000F, 7000F, 50F, 70F);

            if(targetPress > engineOilPressurePSI[i])
                engineOilPressurePSI[i] += Math.min((targetPress - engineOilPressurePSI[i]) * 0.1F, 0.1F);
            else
                engineOilPressurePSI[i] -= Math.min((engineOilPressurePSI[i]- targetPress) * 0.06F, 0.06F);
        }
    }

    public float Fuelamount;
    public long tvect;
    private int pk;
    public int radarrange;
    private long twait;
    private float oldctl;
    private float curctl;
    private float oldthrl;
    private float curthrl;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    public float SpoilerBrakeControl;
    private float oldSpoilerBrakeControl;
    private boolean oldSpoilerAsAileron;
    private long nonAileronTimer;
    private boolean nonAileronTimerSet;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private float lightTime;
    private float ft;
    private LightPointWorld lLight[];
    private Hook lLightHook[];
    private Loc lLightLoc1 = new Loc();
    private Point3d lLightP1 = new Point3d();
    private Point3d lLightP2 = new Point3d();
    private Point3d lLightPL = new Point3d();
    private boolean ictl;
    private static float mteb = 1.0F;
    private float mn;
    private static float uteb = 1.25F;
    private static float lteb = 0.92F;
    private float actl;
    private float rctl;
    private float ectl;
    private boolean ts;
    private float H1;
    public boolean bChangedPit = false;
    private float SonicBoom;
    private Eff3DActor shockwave;
    private boolean isSonic;
    public int LockState = 0;
    static Actor hunted = null;
    private float engineSurgeDamage;
    public boolean hasHydraulicPressure;
    private static final float NEG_G_TOLERANCE_FACTOR = 2.5F;
    private static final float NEG_G_TIME_FACTOR = 2.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 2.0F;
    private static final float POS_G_TOLERANCE_FACTOR = 5.5F;
    private static final float POS_G_TIME_FACTOR = 3F;
    private static final float POS_G_RECOVERY_FACTOR = 3F;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    public boolean FLIR;
    private ArrayList missilesList;
    private float deltaAzimuth;
    private float deltaTangage;
    public int lockmode;
    private boolean APmode1;
    private boolean APmode2;
    public boolean noFL;
    public float azimult;
    public float tangate;
    public long tf;
    public float v;
    public float h;
    public boolean hold;
    public boolean holdFollow;
    public Actor actorFollowing;
    private long t1;
    public boolean radartogle;
    public int radarmode;
    public int targetnum;
    public float lockrange;
    public int radargunsight;
    public int leftscreen;
    public int Bingofuel;
    public boolean Nvision;
    private Field elevatorsField;
    private Field thrustMaxField[];
    private long lTimeNextEject;
    boolean bObserverKilled;
    private int obsLookTime;
    private float obsLookAzimuth;
    private float obsLookElevation;
    private float obsAzimuth;
    private float obsElevation;
    private float obsAzimuthOld;
    private float obsElevationOld;
    private float obsMove;
    private float obsMoveTot;
    private boolean Flaps;
    private boolean bGen1st;
    private boolean bEA6B;
    private boolean bHasLaser;
    private float stockDragAirbrake;
    private Eff3DActor antiColLight[];
    public static float FlowRate = 10F;
    public static float FuelReserve = 1500F;
    private float arrestor;
    public float OxygenMiliLitter = 10000F;
    private boolean bOxygenUsing = false;
    private long LastOxygenUseTime = -1L;
    static private float OxygenConsumption = 0.000301932F;
    public float engineOilPressurePSI[];

    private Point3d laserSpotPos;
    private boolean bLaserOn = false;
    public long laserTimer;
    private boolean bLGBengaged = false;
    public boolean bHasPaveway = false;
    private static float maxPavewayFOVfrom = 45.0F;
    private static double maxPavewayDistance = 20000D;
    public float Timer1;
    public float Timer2;
    private int freq;
    private int counter;
    private int error;
    protected boolean bHasSK1Seat;

    private RadarWarningReceiverUtils rwrUtils;
    public float misslebrg;
    public float aircraftbrg;
    public boolean backfire;
    public boolean bRadarWarning;
    public boolean bMissileWarning;

    private int RWR_GENNUM = 0;
    private static final int RWR_GENERATION70 = 0;
    private static final int RWR_MAX_DETECT70 = 8;
    private static final int RWR_KEEP_SECONDS70 = 2;
    private static final double RWR_RECEIVE_ELEVATION70 = 45.0D;
    private static final boolean RWR_DETECT_IRMIS70 = false;
    private static final boolean RWR_DETECT_ELEVATION70 = false;

    private static final int RWR_GENERATION80 = 1;
    private static final int RWR_MAX_DETECT80 = 16;
    private static final int RWR_KEEP_SECONDS80 = 7;
    private static final double RWR_RECEIVE_ELEVATION80 = 45.0D;
    private static final boolean RWR_DETECT_IRMIS80 = false;
    private static final boolean RWR_DETECT_ELEVATION80 = false;
    private boolean bRWR_Show_Text_Warning = true;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.A_6.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}