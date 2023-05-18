
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapExt;
import com.maddox.sas1946.il2.util.Reflection;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;


public class F_14 extends Scheme2
    implements TypeFighter, TypeFighterAceMaker, TypeBNZFighter, TypeBomber, TypeSupersonic, TypeFastJet, TypeLaserDesignator, TypeGSuit, TypeFuelDump, TypeRadarWarningReceiver, TypeGuidedMissileCarrier, TypeCountermeasure, TypeRadar
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

    public F_14()
    {
        guidedMissileUtils = new GuidedMissileUtils(this);
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        counterFlareList = new ArrayList();
        counterChaffList = new ArrayList();
        lLightHook = new Hook[4];
        SonicBoom = 0.0F;
        lastUpdateTime = -1L;
        k14Mode = 2;
        k14WingspanType = 0;
        k14Distance = 200F;
        overrideBailout = false;
        ejectComplete = false;
        lightTime = 0.0F;
        ft = 0.0F;
        hasHydraulicPressure = true;
        isGeneratorAlive = true;
        super.bWantBeaconKeys = true;
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
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 3000F;
        fSightCurSpeed = 200F;
        fSightCurReadyness = 0.0F;
        arrestor = 0.0F;
        trimauto = false;
        t1 = 0L;
        tangate = 0.0F;
        azimult = 0.0F;
        tf = 0L;
        bDLCengaged = false;
        iDLCcommand = 0;
        hydroPress = 0F;
        bCheckedHydro1st = false;
        lTimeHydroChecked = -1L;
        engineOilPressurePSI = new float[2];
        lTimeOilPressChecked = -1L;
        unitsAoA = 0.0F;
        dragPylons = 0.0F;
        APmode1 = false;
        APmode2 = false;
        APmode3 = false;
        radartoggle = false;
        radarvrt = 0.0F;
        radarhol = 0.0F;
        lockmode = 0;
        radargunsight = 0;
        leftscreen = 2;
        Bingofuel = 1000;
        radarrange = 1;
        Nvision = false;
        bHasUnderTank = false;
        bTakeoffFlapAssist = false;
        bSpawnedAsAI = false;
        bLandedAI = false;
        bTakeoffAIAssist = false;
        bStableAIGround = false;
        autoEng = false;
        curFlaps = 0.0F;
        desiredPosition = 0.0F;
        tVarWingInput = -1L;
        oldVarWingControlSwitch = 0;
        varWingProgram = 0F;
        bFlapsOutFixed = false;
        bFlapsInFixed = false;
        stockCy0_0 = 0.13F;
        stockCy0_1 = 0.85F;
        stockCxMin_0 = 0.032F;
        stockCxMin_1 = 0.10F;
        stockCyCritH_0 = 1.4F;
        stockGCenter = 0.0F;
        stockDefaultElevatorTrim = 0.0F;
        stockparabCxCoeff_0 = 0.0004F;
        stockparabCxCoeff_1 = 0.0005F;
        stockSqWing = 54.5F;
        stockSqWingOut = 13.25F;
        stockSqFlaps = 13.5F;
        stockDragAirbrake = 0.18F;
        SpoilerBrakeControl = 0.0F;
        oldSpoilerBrakeControl = 0.0F;
        antiColLight = new Eff3DActor[3];
        oldAntiColLight = false;
        lEnteredTaxiMode = -1L;
        FLIR = false;
        bLaserOn = false;
        laserSpotPos = new Point3d();
        bLGBengaged = false;
        laserTimer = -1L;
        bHasLaser = false;
        bHasPaveway = false;
        bHadLGweapons = false;
        bAILaserOn = false;
        lAILaserTimer = -1L;
        lAIGAskipTimer = -1L;
        lastLGBcheck = -1L;
        hold = false;
        holdFollow = false;
        actorFollowing = null;
        freq = 800;
        Timer1 = Timer2 = freq;
        if(Config.cur.ini.get("Mods", "RWRTextStop", 0) > 0) bRWR_Show_Text_Warning = false;
        if(this instanceof F_14D)
        {
            RWR_GENERATION = 1;
            RWR_KEEP_SECONDS = 7;
        }
        rwrUtils = new RadarWarningReceiverUtils(this, RWR_GENERATION, RWR_MAX_DETECT, RWR_KEEP_SECONDS, RWR_RECEIVE_ELEVATION, RWR_DETECT_IRMIS, RWR_DETECT_ELEVATION, bRWR_Show_Text_Warning);
    }

    private static final float toMeters(float f)
    {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f)
    {
        return 0.4470401F * f;
    }

    private void checkAmmo()
    {
        counterFlareList.clear();
        counterChaffList.clear();
        bHasLaser = false;
        bHasPaveway = false;
        bHadLGweapons = false;
        FM.bNoDiveBombing = false;

        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                    if(FM.CT.Weapons[i][j].haveBullets())
                    {
                        if(FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16)
                            counterFlareList.add(FM.CT.Weapons[i][j]);
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunChaff_gn16)
                            counterChaffList.add(FM.CT.Weapons[i][j]);
                        else if(FM.CT.Weapons[i][j] instanceof BombGunGBU10_Mk84LGB_gn16 ||
                                FM.CT.Weapons[i][j] instanceof BombGunGBU12_Mk82LGB_gn16 ||
                                FM.CT.Weapons[i][j] instanceof BombGunGBU16_Mk83LGB_gn16)
                        {
                            bHasPaveway = true;
                            bHadLGweapons = true;
                            FM.bNoDiveBombing = true;
                        }
                        else if(FM.CT.Weapons[i][j] instanceof BombGunGBU38_Mk82JDAM_gn16 ||
                                FM.CT.Weapons[i][j] instanceof BombGunGBU32_Mk83JDAM_gn16 ||
                                FM.CT.Weapons[i][j] instanceof BombGunGBU31_Mk84JDAM_gn16)
                            FM.bNoDiveBombing = true;
                    }
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_AN_AAQ14_gn16)
                        bHasLaser = true;
            }
    }

    private void checkGuidedBombRest()
    {
        if(!((Maneuver)FM).hasBombs())
        {
            bHasPaveway = false;
            FM.bNoDiveBombing = false;
        }
        else if(bHasPaveway || FM.bNoDiveBombing)
        {
            boolean bTempNoGBU = true;
            boolean bTempNoJDAM = true;
            for(int i = 3; i < 4 && bTempNoGBU && bTempNoJDAM; i++)
                if(FM.CT.Weapons[i] != null)
                {
                    for(int j = 0; j < FM.CT.Weapons[i].length && bTempNoGBU && bTempNoJDAM; j++)
                        if(FM.CT.Weapons[i][j].haveBullets())
                        {
                            if(FM.CT.Weapons[i][j] instanceof BombGunGBU10_Mk84LGB_gn16 ||
                               FM.CT.Weapons[i][j] instanceof BombGunGBU12_Mk82LGB_gn16 ||
                               FM.CT.Weapons[i][j] instanceof BombGunGBU16_Mk83LGB_gn16)
                                bTempNoGBU = false;
                            else if(FM.CT.Weapons[i][j] instanceof BombGunGBU38_Mk82JDAM_gn16 ||
                                    FM.CT.Weapons[i][j] instanceof BombGunGBU32_Mk83JDAM_gn16 ||
                                    FM.CT.Weapons[i][j] instanceof BombGunGBU31_Mk84JDAM_gn16)
                                bTempNoJDAM = false;
                        }
                }
            if(bTempNoGBU)
                bHasPaveway = false;
            if(bTempNoGBU && bTempNoJDAM)
                FM.bNoDiveBombing = false;
        }
    }

    private void backFire()
    {
        if(counterFlareList.isEmpty())
            hasFlare = false;
        else
        {
            if(Time.current() > lastFlareDeployed + 1200L)
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
            if(Time.current() > lastChaffDeployed + 1500L)
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

    public float checkfuel(int i)
    {
        FuelTank afueltank[] = FM.CT.getFuelTanks();
        if(afueltank.length == 0 || FM.M.bFuelTanksDropped)
        {
            return 0.0F;
        }
        else
        {
            Fuelamount = afueltank[i].checkFuel();
            return Fuelamount;
        }
    }

    private void checkDroptanks()
    {
        FuelTank afueltank[] = FM.CT.getFuelTanks();
        if(afueltank.length == 2)
            bHasUnderTank = true;
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
/*        if(i == 20)
            if(!radartoggle)
            {
                radartoggle = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar ON");
                radarmode = 0;
            }
            else
            {
                radartoggle = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar OFF");
            }
        if(i == 21)
            if(!Nvision)
            {
                Nvision = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Nvision ON");
            }
            else
            {
                Nvision = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Nvision OFF");
            }
        if(i == 22)
        {
            lockmode++;
            if(lockmode > 1)
                lockmode = 0;
        }
        if(i == 23)
        {
            radargunsight++;
            if(radargunsight > 3)
                radargunsight = 0;
            if(radargunsight == 0)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: funnel");
            if(radargunsight == 1)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Radar ranging");
            if(radargunsight == 2)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Unguided Rocket");
            if(radargunsight == 3)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Ground");
        }
        if(i == 24)
        {
            leftscreen++;
            if(leftscreen > 2)
                leftscreen = 0;
            if(leftscreen == 0)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: Fuel");
            else
            if(leftscreen == 1)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: FPAS");
            else
            if(leftscreen == 2)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: Engine");
        } */
        if(i == 25)
        {
            Bingofuel += 500;
            if(Bingofuel > 11000)
                Bingofuel = 1000;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Bingofuel " + Bingofuel);
        }
        if(i == 26)
        {
            if(hold && Time.current() > t1 + 200L && FLIR)
            {
                hold = false;
                holdFollow = false;
                HUD.log("Laser Pos Unlock");
                t1 = Time.current();
            }
            if(!hold && Time.current() > t1 + 200L && FLIR)
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
        if(i == 27)
        {
            if(holdFollow && Time.current() > t1 + 200L && FLIR)
            {
                hold = false;
                holdFollow = false;
                actorFollowing = null;
                HUD.log("Laser Track Unlock");
                t1 = Time.current();
            }
            if(!holdFollow && Time.current() > t1 + 200L && FLIR)
            {
                hold = false;
                holdFollow = true;
                actorFollowing = null;
                HUD.log("Laser Track Lock");
                t1 = Time.current();
            }
        }
        if(i == 29)
            if(!bDLCengaged)
            {
                if(FM.CT.getFlap() > 0.715F && FM.EI.engines[0].getControlThrottle() < 0.97F && FM.EI.engines[1].getControlThrottle() < 0.97F)
                {
                    bDLCengaged = true;
                    iDLCcommand = 0;
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "DLC engaged");
                }
            }
            else
            {
                bDLCengaged = false;
                iDLCcommand = 0;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "DLC stowed");
            }
        if(i == 30)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log("Autopilot Mode: Direction ON");
                FM.AP.setStabDirection(true);
                FM.CT.bHasRudderControl = false;
            }
            else if(APmode2)
            {
                APmode2 = false;
                HUD.log("Autopilot Mode: Direction OFF");
                FM.AP.setStabDirection(false);
                FM.CT.bHasRudderControl = true;
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
        }
        else if(radartoggle && lockmode == 0)
            radarhol += 0.0035F;
    }

    public void typeBomberAdjDistanceMinus()
    {
        if(FLIR)
        {
            azimult--;
            tf = Time.current();
        }
        else if(radartoggle && lockmode == 0)
            radarhol -= 0.0035F;
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
        }
        else if(radartoggle && lockmode == 0)
            radarvrt += 0.0035F;
    }

    public void typeBomberAdjSideslipMinus()
    {
        if(FLIR)
        {
            tangate--;
            tf = Time.current();
        }
        else if(radartoggle && lockmode == 0)
            radarvrt -= 0.0035F;
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
        if(bDLCengaged)
        {
            iDLCcommand = 0;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "DLC neutral");
        }
    }

    public void typeBomberAdjAltitudePlus()
    {
        if(bDLCengaged)
        {
            iDLCcommand++;
            if(iDLCcommand >= 10)
            {
                iDLCcommand = 10;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "DLC plus Max");
            }
            if(iDLCcommand == 0)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "DLC neutral");
        }
        else if(FLIR)
            if(!APmode3)
            {
                APmode3 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Engaged");
                ((FlightModelMain) (FM)).AP.setStabAltitude((float)FM.Loc.z);
            }
            else if(APmode3)
            {
                APmode3 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Released");
                ((FlightModelMain) (FM)).AP.setStabAltitude(false);
            }
    }

    public void typeBomberAdjAltitudeMinus()
    {
        if(bDLCengaged)
        {
            iDLCcommand--;
            if(iDLCcommand <= -10)
            {
                iDLCcommand = -10;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "DLC minus Max");
            }
            if(iDLCcommand == 0)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "DLC neutral");
        }
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
        radarrange++;
        if(radarrange > 5)
            radarrange = 5;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar range " + radarrange);
    }

    public void typeBomberAdjSpeedMinus()
    {
        radarrange--;
        if(radarrange < 1)
            radarrange = 1;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar range " + radarrange);
    }

    public void typeBomberUpdate(float f)
    {
        if(Math.abs(FM.Or.getKren()) > 4.5F)
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
            if(fSightCurDistance < toMetersPerSecond(fSightCurSpeed) * (float)Math.sqrt(toMeters(fSightCurAltitude) * 0.2038736F))
                bSightBombDump = true;
            if(bSightBombDump)
                if(FM.isTick(3, 0))
                {
                    if(FM.CT.Weapons[3] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets())
                    {
                        FM.CT.WeaponControl[3] = true;
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
                    }
                }
                else
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
        point3d.z = World.land().HQ(point3d.x, point3d.y);
        Eff3DActor eff3dactor = Eff3DActor.New(null, null, new Loc(point3d.x, point3d.y, point3d.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
        eff3dactor.postDestroy(Time.current() + 1500L);
    }

    private void FLIR()
    {
        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if(!(actor instanceof Aircraft) && !(actor instanceof ArtilleryGeneric) && !(actor instanceof CarGeneric) && !(actor instanceof TankGeneric) || (actor instanceof StationaryGeneric) || (actor instanceof TypeLaserDesignator) || actor.pos.getAbsPoint().distance(pos.getAbsPoint()) >= 20000D)
                continue;
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            actor.pos.getAbs(point3d, orient);
//            l.set(point3d, orient);
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
            eff3dactor.draw.lightMap().put("light", lightpointactor);
        }

    }

    public boolean typeDiveBomberToggleAutomation()
    {
        return true;
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
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

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        FM.AS.wantBeaconsNet(true);
        FM.Skill = 3;
        FM.turret[0].bIsAIControlled = false;
        FM.CT.bHasBombSelect = true;
        t1 = Time.current();
        FM.CT.bHasAntiColLights = true;
        FM.CT.bHasFormationLights = true;
        if(FM instanceof RealFlightModel)
            Reflection.invokeMethod(FM, "init_G_Limits");
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        stockCy0_0 = polares.Cy0_0;
        stockCy0_1 = polares.Cy0_1;
        stockCxMin_0 = polares.CxMin_0;
        stockCxMin_1 = polares.CxMin_1;
        stockCyCritH_0 = polares.CyCritH_0;
        stockGCenter = Reflection.getFloat(FM, "GCenter");
        stockDefaultElevatorTrim = Reflection.getFloat(FM, "DefaultElevatorTrim");
        stockparabCxCoeff_0 = polares.parabCxCoeff_0;
        stockparabCxCoeff_1 = polares.parabCxCoeff_1;
        stockSqWing = FM.Sq.squareWing;
        stockSqWingOut = FM.Sq.liftWingLOut;
        stockSqFlaps = FM.Sq.squareFlaps;
        stockDragAirbrake = FM.Sq.dragAirbrakeCx;
        dragPylons = 0.0000F;
        if(thisWeaponsName.startsWith("Fighter_4xAIM54"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon2L", true);
            hierMesh().chunkVisible("Pylon2R", true);
            dragPylons = 0.001F;
        }
        if(thisWeaponsName.startsWith("Fighter_6xAIM54"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon2L", true);
            hierMesh().chunkVisible("Pylon2R", true);
            dragPylons = 0.001F;
        }
        if(thisWeaponsName.startsWith("Fighter_2xAIM54"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            dragPylons = 0.0007F;
        }
        if(thisWeaponsName.startsWith("GAttack_2xMk"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            dragPylons = 0.0007F;
        }
        if(thisWeaponsName.startsWith("GAttackFLIR_ALQ"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            dragPylons = 0.0007F;
        }
        if(thisWeaponsName.startsWith("Recon_ALQ"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            dragPylons = 0.0007F;
        }
        if(thisWeaponsName.startsWith("GAttackFLIR_2xGBU"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            dragPylons = 0.0007F;
        }
        if(thisWeaponsName.startsWith("GAttack_2xCBU"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            dragPylons = 0.0007F;
        }
        if(thisWeaponsName.startsWith("GAttack_4xMk"))
         {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon2L", true);
            hierMesh().chunkVisible("Pylon2R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            hierMesh().chunkVisible("Pylon4L", true);
            hierMesh().chunkVisible("Pylon4R", true);
            dragPylons = 0.001F;
        }
        if(thisWeaponsName.startsWith("GAttack_4xCBU"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon2L", true);
            hierMesh().chunkVisible("Pylon2R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            hierMesh().chunkVisible("Pylon4L", true);
            hierMesh().chunkVisible("Pylon4R", true);
            dragPylons = 0.001F;
        }
        if(thisWeaponsName.startsWith("GAttackFLIR_4xGBU"))
        {
            hierMesh().chunkVisible("Pylon1L", true);
            hierMesh().chunkVisible("Pylon1R", true);
            hierMesh().chunkVisible("Pylon2L", true);
            hierMesh().chunkVisible("Pylon2R", true);
            hierMesh().chunkVisible("Pylon3L", true);
            hierMesh().chunkVisible("Pylon3R", true);
            hierMesh().chunkVisible("Pylon4L", true);
            hierMesh().chunkVisible("Pylon4R", true);
            dragPylons = 0.001F;
        }
        rwrUtils.onAircraftLoaded();
        if(this instanceof F_14D)
            rwrUtils.setLockTone("", "aircraft.usRWRLock", "aircraft.usRWRLaunchWarningMissileMissile", "", "usRWRLock.wav", "usRWRLaunchWarningMissileMissile.wav");
        else
            rwrUtils.setLockTone("aircraft.APR25AAA", "aircraft.APR25S75EbandLock", "aircraft.APR25S75EbandLock", "APR25AAA.wav", "APR25S75EbandLock.wav", "APR25S75EbandLock.wav");
    }

    public void missionStarting()
    {
        super.missionStarting();
        checkDroptanks();
        checkAmmo();
        if(!(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))
            bSpawnedAsAI = true;

        FM.CT.toggleRocketHook();

        tVarWingInput = -1L;
        laserTimer = -1L;
        bLaserOn = false;
        FLIR = false;

        for(int i = 0; i < 2; i++)
        {
            oldthrl[i] = -1.0F;
            curthrl[i] = -1.0F;
            engineSurgeDamage[i] = 0.0F;
            exNozzleBroken[i] = false;
        }
        for(int i = 0; i < 2; i++)
        {
            if(FM.CT.GearControl > 0F)
                engineOilPressurePSI[i] = 0F;
            else
                engineOilPressurePSI[i] = 47F;
        }
        bCheckedHydro1st = false;
        if(FM.CT.GearControl > 0F)
        {
            isGeneratorAlive = false;
            hydroPress = 0F;
        }
        else
        {
            isGeneratorAlive = true;
            hydroPress = 293F;
        }
    }

    public void updateLLights()
    {
        super.pos.getRender(Actor._tmpLoc);
        if(lLight == null)
        {
            if(Actor._tmpLoc.getX() >= 1.0D)
            {
                lLight = new LightPointWorld[4];
                for(int i = 0; i < 4; i++)
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
        }
        else
        {
            for(int j = 0; j < 4; j++)
            {
                if(FM.AS.astateLandingLightEffects[j] != null)
                {
                    lLightLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP1);
                    lLightLoc1.set(2000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    lLightHook[j].computePos(this, Actor._tmpLoc, lLightLoc1);
                    lLightLoc1.get(lLightP2);
                    if(Landscape.rayHitHQ(lLightP1, lLightP2, lLightPL))
                    {
                        lLightPL.z++;
                        lLightP2.interpolate(lLightP1, lLightPL, 0.95F);
                        lLight[j].setPos(lLightP2);
                        float f = (float)lLightP1.distance(lLightPL);
                        float f1 = f * 0.5F + 60F;
                        float f2 = 0.7F - (0.8F * f * lightTime) / 2000F;
                        lLight[j].setEmit(f2, f1);
                    }
                    else
                    {
                        lLight[j].setEmit(0.0F, 0.0F);
                    }
                    continue;
                }
                if(lLight[j].getR() != 0.0F)
                    lLight[j].setEmit(0.0F, 0.0F);
            }

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
        if(FM.crew > 1 && !bObserverKilled)
            if(obsLookTime == 0)
            {
                obsLookTime = 2 + World.Rnd().nextInt(1, 3);
                obsMoveTot = 1.0F + World.Rnd().nextFloat() * 1.5F;
                obsMove = 0.0F;
                obsAzimuthOld = obsAzimuth;
                obsElevationOld = obsElevation;
                if(World.Rnd().nextFloat() > 0.80F)
                {
                    obsAzimuth = 0.0F;
                    obsElevation = 0.0F;
                }
                else
                {
                    obsAzimuth = World.Rnd().nextFloat() * 140F - 70F;
                    obsElevation = World.Rnd().nextFloat() * 50F - 20F;
                }
            }
            else
            {
                obsLookTime--;
            }
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            ft = World.getTimeofDay() % 0.01F;
            if(ft == 0.0F)
                UpdateLightIntensity();
        }
        if(FM.Gears.onGround() && FM.CT.getCockpitDoor() == 1.0F)
        {
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
        }
        else
        {
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
            hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
        }
        if(bHasLaser && FM.actor == World.getPlayerAircraft())
        {
            if(FLIR)
                FLIR();
        }
        if(bHasLaser && FM.actor != World.getPlayerAircraft() && FM instanceof Maneuver)
            AILaserControl();

        if((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver))
        {
            if(FM.AP.way.curr().Action == 3)
            {
                if(!((Maneuver)super.FM).hasBombs())
                {
                    if((lAIGAskipTimer > 0L && Time.current() > lAIGAskipTimer) || !bHadLGweapons || !bHasLaser)
                    {
                        FM.AP.way.next();
                        FM.bSkipGroundAttack = true;
                        ((Maneuver)FM).target_ground = null;
                        ((Maneuver)FM).set_maneuver(0);
                    }
                    else if(lAIGAskipTimer == -1L)
                        lAIGAskipTimer = Time.current() + 20000L + (long)Math.min(FM.getAltitude(), 10000F) * 5L;
                }
            }
            AiAirbrakeOperation();
            FM.CT.bAntiColLights = FM.AS.bNavLightsOn;
        }

        formationlights();
        if(FM.CT.bAntiColLights != oldAntiColLight)
            anticollights();

        if((bHasPaveway || FM.bNoDiveBombing) && Time.current() > lastLGBcheck + 30000L && (FM instanceof Maneuver))
        {
            checkGuidedBombRest();
            lastLGBcheck = Time.current();
        }
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

    public boolean typeBomberToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        if(k14Mode == 0)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Bomb");
        }
        else if(k14Mode == 1)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Gunnery");
        }
        else if(k14Mode == 2 && FM.actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Navigation");
        return true;
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
        if(k14Distance > 1500F)
            k14Distance = 1500F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if(k14Distance < 20F)
            k14Distance = 20F;
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

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        default:
            break;

        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1: // '\001'
            if(FM.crew > 1)
            {
                hierMesh().chunkVisible("Pilot2_D0", false);
                hierMesh().chunkVisible("Head2_D0", false);
                hierMesh().chunkVisible("HMask2_D0", false);
                hierMesh().chunkVisible("Pilot2_D1", true);
                bObserverKilled = true;
            }
            break;
        }
    }


    public void doEjectCatapultStudent()
    {
        new MsgAction(false, this)
        {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 30D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat02");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat2_D0", false);
    }

    public void doEjectCatapultInstructor()
    {
        new MsgAction(false, this)
        {

            public void doAction(Object obj)
            {
                Aircraft aircraft = (Aircraft)obj;
                if(Actor.isValid(aircraft))
                {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 30D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    ((Actor) (aircraft)).pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).x;
                    vector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).y;
                    vector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).Vwld)).z;
                    new EjectionSeat(1, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat1_D0", false);
        super.FM.setTakenMortalDamage(true, null);
        FM.CT.WeaponControl[0] = false;
        FM.CT.WeaponControl[1] = false;
        FM.CT.bHasAileronControl = false;
        FM.CT.bHasRudderControl = false;
        FM.CT.bHasElevatorControl = false;
        FM.CT.bHasVarWingControl = false;
    }

    private void checkHydraulicStatus()
    {
        if(lTimeHydroChecked == Time.current())
            return;

        float f = 0.03F;
        if(!bCheckedHydro1st && Time.current() > 900L)
        {
            if(FM.CT.getGear() == 0.0F && FM.getSpeedKMH() > 120F)
                hydroPress = 294F;
            else
                hydroPress = 0F;
            bCheckedHydro1st = true;
        }

        if(FM.EI.engines[0].getStage() < 6 && FM.EI.engines[1].getStage() < 6 && FM.getSpeedKMH() < 160F)
        {
            isGeneratorAlive = false;
            hydroPress -= 1.2F * f;
            if(hydroPress < 0F)
                hydroPress = 0F;
        }
        else
        {
            isGeneratorAlive = true;
            if(hydroPress < 260F)
                hydroPress += (40F + World.Rnd().nextFloat()) * f;
            else
                hydroPress += (((300F - hydroPress) * f) + World.Rnd().nextFloat());
            if(FM.EI.engines[0].getRPM() > 4000F)
                hydroPress += 2.0F * f;
            if(FM.EI.engines[1].getRPM() > 4000F)
                hydroPress += 2.0F * f;
        }

        if(hydroPress < 200F)
        {
            hasHydraulicPressure = false;
            FM.CT.bHasFlapsControl = false;
            FM.CT.bHasAileronControl = false;
            FM.CT.bHasElevatorControl = false;
            FM.CT.bHasRudderControl = false;
            FM.CT.bHasAirBrakeControl = false;
            updateControlsVisuals();
        }
        else if(!hasHydraulicPressure)
        {
            hasHydraulicPressure = true;
            FM.CT.bHasFlapsControl = true;
            FM.CT.bHasAileronControl = true;
            if(FM.CT.getWing() > 0.3F)
            {
                FM.CT.ElevatorControl = 0.0F;
                FM.CT.setTrimElevatorControl(0.0F);
                FM.CT.bHasElevatorControl = false;
                updateControlsVisuals();
            }
            else
                FM.CT.bHasElevatorControl = true;
            FM.CT.bHasRudderControl = true;
            FM.CT.bHasAirBrakeControl = true;
        }

        lTimeHydroChecked = Time.current();
    }

    public void moveCockpitDoor(float f)
    {
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 0.0F, 25F * f);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 90F * f2, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.11F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 0.0F, Aircraft.cvt(f2, 0.01F, 0.72F, 0.0F, -25F));
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, -60F * f2, 0.0F);
        resetXYZYPR();
        double lenH = 0.362D;  // Length of GearC71b : High
        double lenL = 0.282D;  // Length of GearC71d : Low
        double lenS = 0.408D;  // Length of GearC71c : Side
        Aircraft.xyz[0] = Aircraft.cvt(f2, 0.15F, 0.83F, 0.0F, 0.04F);
        hiermesh.chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
        double contraH = ((double)Aircraft.xyz[0] + 0.178D) * lenH / (lenH + lenL);
        double contraL = ((double)Aircraft.xyz[0] + 0.178D) * lenL / (lenH + lenL);
        float fH = (float)Math.toDegrees(Math.acos((lenH - contraH) / lenH)) * 0.7F;
        float fL = (float)Math.toDegrees(Math.acos((lenL - contraL) / lenL)) * 1.3F;
        hiermesh.chunkSetAngles("GearC71b_D0", 0.0F, fH -35F, 0.0F);
        hiermesh.chunkSetAngles("GearC71c_D0", 0.0F, -0.54F * fH + 19.7F, 0.0F);
        hiermesh.chunkSetAngles("GearC71d_D0", 0.0F, -fL - fH + 84F, 0.0F);
        hiermesh.chunkSetAngles("GearC51_D0", 0.0F, Aircraft.cvt(f2, 0.4F, 0.8F, 0.0F, 125F), 0.0F);
        if(f2 < 0.7F)
            hiermesh.chunkSetAngles("GearC22_D0", 0.0F, 0.0F, 0.0F);

        resetXYZYPR();
        if(f < 0.5F)
        {
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.3F, 0.48F, 0.0F, 0.15F);
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.1F, 0.5F, 0.0F, -50F);
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
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.75F, 1.0F, -85F, -105F);
            Aircraft.ypr[2] = Aircraft.cvt(f, 0.75F, 1.0F, -2.8F, -0.8F);
        }
        hiermesh.chunkSetLocate("GearL2_D0", Aircraft.xyz, Aircraft.ypr);
//        hiermesh.chunkSetAngles("GearL3_D0", Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, 90F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, 98F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.65F, 1.0F, 0.0F, 16.32F));
        if(f < 0.6F)
            hiermesh.chunkSetAngles("GearL51_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.4F, 0.6F, 0.0F, -77.4F));
        else
            hiermesh.chunkSetAngles("GearL51_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.6F, 1.0F, -77.4F, -167.627F));
        hiermesh.chunkSetAngles("GearL22_D0", 0.0F, Aircraft.cvt(f, 0.1F, 1.0F, 0.0F, 4.25F), 0.0F);
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.1F, 1.0F, 0.0F, -0.222F);
        hiermesh.chunkSetLocate("GearL23_D0", Aircraft.xyz, Aircraft.ypr);
        resetXYZYPR();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.4F, 0.8F, 0.0F, 0.51F);
        hiermesh.chunkSetLocate("GearL4_D0", Aircraft.xyz, Aircraft.ypr);
        float f3 = (float)Math.toDegrees(Math.acos((0.96F - 0.104F - Aircraft.xyz[1]) / 0.96F));
        f3 -= (float)Math.toDegrees(Math.acos((0.96F - 0.104F) / 0.96F));
        hiermesh.chunkSetAngles("GearL41_D0", 0.0F, 0.0F, -f3);
        hiermesh.chunkSetAngles("GearL42_D0", 0.0F, 0.0F, f3 * 2.0F);
       // hiermesh.chunkSetAngles("GearL15_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.4F, 0.6F, 0.0F, 70F));
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0",  0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, 95F), 0.0F);
        hiermesh.chunkSetAngles("GearL11_D0",  0.0F, Aircraft.cvt(f, 0.03F, 0.1F, 0.0F, -75F), 0.0F);

        resetXYZYPR();
        if(f1 < 0.5F)
        {
            Aircraft.xyz[1] = Aircraft.cvt(f1, 0.3F, 0.48F, 0.0F, -0.15F);
            Aircraft.ypr[1] = Aircraft.cvt(f1, 0.1F, 0.5F, 0.0F, -50F);
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
            Aircraft.ypr[1] = Aircraft.cvt(f1, 0.75F, 1.0F, -85F, -105F);
            Aircraft.ypr[2] = Aircraft.cvt(f1, 0.75F, 1.0F, 2.8F, 0.8F);
        }
        hiermesh.chunkSetLocate("GearR2_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearR3_D0", Aircraft.cvt(f1, 0.1F, 0.2F, 0.0F, -98F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.65F, 1.0F, 0.0F, 16.32F));
        if(f1 < 0.6F)
            hiermesh.chunkSetAngles("GearR51_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.4F, 0.6F, 0.0F, -77.4F));
        else
            hiermesh.chunkSetAngles("GearR51_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.6F, 1.0F, -77.4F, -167.627F));
        hiermesh.chunkSetAngles("GearR22_D0", 0.0F, Aircraft.cvt(f1, 0.1F, 1.0F, 0.0F, 4.25F), 0.0F);
        resetXYZYPR();
        Aircraft.xyz[2] = Aircraft.cvt(f1, 0.1F, 1.0F, 0.0F, -0.222F);
        hiermesh.chunkSetLocate("GearR23_D0", Aircraft.xyz, Aircraft.ypr);
        resetXYZYPR();
        Aircraft.xyz[1] = Aircraft.cvt(f1, 0.4F, 0.8F, 0.0F, 0.51F);
        hiermesh.chunkSetLocate("GearR4_D0", Aircraft.xyz, Aircraft.ypr);
        f3 = (float)Math.toDegrees(Math.acos((0.96F - 0.104F - Aircraft.xyz[1]) / 0.96F));
        f3 -= (float)Math.toDegrees(Math.acos((0.96F - 0.104F) / 0.96F));
        hiermesh.chunkSetAngles("GearR41_D0", 0.0F, 0.0F, -f3);
        hiermesh.chunkSetAngles("GearR42_D0", 0.0F, 0.0F, f3 * 2.0F);
       // hiermesh.chunkSetAngles("GearR15_D0", 0.0F, 0.0F, Aircraft.cvt(f1, 0.4F, 0.6F, 0.0F, -70F));
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.1F, 0.0F, 95F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0",  0.0F, Aircraft.cvt(f, 0.01F, 0.1F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearR11_D0", 0.0F, Aircraft.cvt(f1, 0.03F, 0.1F, 0.0F, 75F), 0.0F );
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        moveGear(hiermesh, f, f, f);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        if(FM.CT.getGearC() > 0.999F)
        {
            double lenH = 0.362D;  // Length of GearC71b : High
            double lenL = 0.282D;  // Length of GearC71d : Low
            double lenS = 0.408D;  // Length of GearC71c : Side
            float f = FM.Gears.gWheelSinking[2];
            resetYPRmodifier();
            Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 0.22F, 0.0F, 0.22F) + 0.065F;
//            Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 0.22F, 0.0F, 0.22F) + 0.04F;
            hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
            double contraH = ((double)Aircraft.xyz[0] + 0.178D) * lenH / (lenH + lenL);
            double contraL = ((double)Aircraft.xyz[0] + 0.178D) * lenL / (lenH + lenL);
            float fH = (float)Math.toDegrees(Math.acos((lenH - contraH) / lenH)) * 0.7F;
            float fL = (float)Math.toDegrees(Math.acos((lenL - contraL) / lenL)) * 1.3F;
            hierMesh().chunkSetAngles("GearC71b_D0", 0.0F, fH - 35F, 0.0F);
            hierMesh().chunkSetAngles("GearC71c_D0", 0.0F, -0.54F * fH + 19.7F, 0.0F);
            hierMesh().chunkSetAngles("GearC71d_D0", 0.0F, -fL - fH + 84F, 0.0F);
//            hierMesh().chunkSetAngles("GearC8_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.19F, -140F, -128F), 0.0F);
        }
        if(FM.CT.getGearL() > 0.999F)
        {
            float f1 = FM.Gears.gWheelSinking[0];
            resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f1, 0.0F, 0.19F, 0.0F, 0.19F) + 0.51F;
            float f4 = (float)Math.toDegrees(Math.acos((0.8559999F - Aircraft.xyz[1]) / 0.96F));
            f4 -= (float)Math.toDegrees(Math.acos(0.89166665077209473D));
            hierMesh().chunkSetLocate("GearL4_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetAngles("GearL41_D0", 0.0F, 0.0F, -f4);
            hierMesh().chunkSetAngles("GearL42_D0", 0.0F, 0.0F, f4 * 2.0F);
        }
        if(FM.CT.getGearR() > 0.999F)
        {
            float f2 = FM.Gears.gWheelSinking[1];
            resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f2, 0.0F, 0.19F, 0.0F, 0.19F) + 0.51F;
            float f5 = (float)Math.toDegrees(Math.acos((0.8559999F - Aircraft.xyz[1]) / 0.96F));
            f5 -= (float)Math.toDegrees(Math.acos(0.89166665077209473D));
            hierMesh().chunkSetLocate("GearR4_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetAngles("GearR41_D0", 0.0F, 0.0F, -f5);
            hierMesh().chunkSetAngles("GearR42_D0", 0.0F, 0.0F, f5 * 2.0F);
        }
    }

    private static void resetXYZYPR()
    {
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
    }

    public void moveSteering(float f)
    {
        if(FM.CT.GearControl < 0.7F || FM.Gears.isCatapultArmed())
            hierMesh().chunkSetAngles("GearC22_D0", 0.0F, 0.0F, 0.0F);
        else if(FM.CT.GearControl > 0.7F && FM.Gears.onGround())
            hierMesh().chunkSetAngles("GearC22_D0", 0.0F, 0.0F, -1.0F * f);
    }

    protected void moveCatLaunchBar(float f)
    {
        hierMesh().chunkSetAngles("GearC51_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 125F, 35F), 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, 20F * f, 0.0F);
        hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, 20F * f, 0.0F);
    }


    protected void moveElevator(float f)
    {
        updateControlsVisuals();
    }


    protected void moveAileron(float f)
    {
        updateControlsVisuals();
        updateAileronVisuals();
    }

    private final void updateControlsVisuals()
    {
        if(!FM.CT.bHasElevatorControl || FM.CT.getVarWing() > 0.8F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, 0.0F);
        }
        else
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, Math.max(-33F, Math.min(10F, -33F * FM.CT.getElevator() + 20F * FM.CT.getAileron())), 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, Math.max(-33F, Math.min(10F, -33F * FM.CT.getElevator() - 20F * FM.CT.getAileron())), 0.0F);
        }
    }


    protected void moveFlap(float f)
    {
        if(f < 0.286F)
        {  // Combat Flap area 0 ~ 10 deg.
            resetYPRmodifier();
            Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.05F, 0.0F, -0.086F);
            Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.05F, 0.0F, -0.01F);
            Aircraft.ypr[0] = Aircraft.cvt(f, 0.01F, 0.05F, 0.0F, 0.036F);
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.01F, 0.05F, 0.0F, 7F);
            hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
            resetYPRmodifier();
            Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.05F, 0.0F, 0.086F);
            Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.05F, 0.0F, -0.01F);
            Aircraft.ypr[0] = Aircraft.cvt(f, 0.01F, 0.05F, 0.0F, -0.036F);
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.01F, 0.05F, 0.0F, -7F);
            hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
        }
        else
        {  // Full Flap area 10 ~ 35 deg.
            resetYPRmodifier();
            Aircraft.xyz[0] = Aircraft.cvt(f, 0.3F, 0.5F, -0.086F, -0.17F);
            Aircraft.xyz[2] = Aircraft.cvt(f, 0.3F, 0.5F, -0.01F, -0.048F);
            Aircraft.ypr[0] = Aircraft.cvt(f, 0.3F, 0.5F, 0.036F, 1F);
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.3F, 0.5F, 7F, 17F);
            hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
            resetYPRmodifier();
            Aircraft.xyz[0] = Aircraft.cvt(f, 0.3F, 0.5F, 0.086F, 0.17F);
            Aircraft.xyz[2] = Aircraft.cvt(f, 0.3F, 0.5F, -0.01F, -0.048F);
            Aircraft.ypr[0] = Aircraft.cvt(f, 0.3F, 0.5F, -0.036F, -1F);
            Aircraft.ypr[1] = Aircraft.cvt(f, 0.3F, 0.5F, -7F, -17F);
            hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
        }

        if(bFlapsOutFixed || FM.CT.getVarWing() > 0.6F)
        {
            hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Flap01u_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Flap02u_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("WingLCoveDoorO_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("WingRCoveDoorO_D0", 0.0F, 0.0F, 0.0F);
        }
        else
        {
            hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -35 * f, 0.0F);
            hierMesh().chunkSetAngles("Flap01u_D0", 0.0F, 16 * Aircraft.cvt(f, 0.40F, 0.75F, 0.0F, 1.0F), 0.0F);
            hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -35 * f, 0.0F);
            hierMesh().chunkSetAngles("Flap02u_D0", 0.0F, 16 * Aircraft.cvt(f, 0.40F, 0.75F, 0.0F, 1.0F), 0.0F);
            hierMesh().chunkSetAngles("WingLCoveDoorO_D0", 0.0F, 0.0F, -45 * Aircraft.cvt(f, 0.40F, 0.50F, 0.0F, 1.0F));
            hierMesh().chunkSetAngles("WingRCoveDoorO_D0", 0.0F, 0.0F, -45 * Aircraft.cvt(f, 0.40F, 0.50F, 0.0F, 1.0F));
        }

        if(bFlapsInFixed || FM.CT.getVarWing() > 0.6F)
        {
            hierMesh().chunkSetAngles("Flap03_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Flap03u_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Flap04_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("Flap04u_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("WingLCoveDoorI_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("WingRCoveDoorI_D0", 0.0F, 0.0F, 0.0F);
        }
        else
        {
            hierMesh().chunkSetAngles("Flap03_D0", 0.0F, -35 * f, 0.0F);
            hierMesh().chunkSetAngles("Flap03u_D0", 0.0F, 16 * Aircraft.cvt(f, 0.40F, 0.75F, 0.0F, 1.0F), 0.0F);
            hierMesh().chunkSetAngles("Flap04_D0", 0.0F, -35 * f, 0.0F);
            hierMesh().chunkSetAngles("Flap04u_D0", 0.0F, 16 * Aircraft.cvt(f, 0.40F, 0.75F, 0.0F, 1.0F), 0.0F);
            hierMesh().chunkSetAngles("WingLCoveDoorI_D0", 0.0F, 0.0F, -45 * Aircraft.cvt(f, 0.40F, 0.50F, 0.0F, 1.0F));
            hierMesh().chunkSetAngles("WingRCoveDoorI_D0", 0.0F, 0.0F, -45 * Aircraft.cvt(f, 0.40F, 0.50F, 0.0F, 1.0F));
        }

        updateAileronVisuals();
    }

    private final void updateAileronVisuals()
    {
        float spodegLI = 0.0F;
        float spodegLO = 0.0F;
        float spodegRI = 0.0F;
        float spodegRO = 0.0F;
        float spodegDLC = -4.5F;  // spoiler -4.5 deg. when DLC is not engaged in flap deployed over 25 deg.

        boolean bflapDep = FM.CT.getFlap() > 0.29F;      // over 10 deg. of main flaps down.
        boolean bflapDepMax = FM.CT.getFlap() > 0.715F;  // over 25 deg. of main flaps down. "Direct Lift Control" engage possible in the Power Approach mode.

        if(bDLCengaged && (!bflapDepMax || FM.EI.engines[0].getControlThrottle() > 0.97F || FM.EI.engines[1].getControlThrottle() > 0.97F))
        {
            bDLCengaged = false;
            iDLCcommand = 0;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "DLC stowed");
        }

        if(bDLCengaged)
        {
            if(iDLCcommand > 0)
            {
                spodegDLC = 17.5F + 3.75F * (float)iDLCcommand;  // 17.5 deg. ~ 55 deg.
            }
            else if(iDLCcommand < 0)
            {
                spodegDLC = 17.5F + 2.2F * (float)iDLCcommand;   // 17.5 deg. ~ -4.5 deg.
            }
            else
            {
                spodegDLC = 17.5F;
            }
        }

        if(FM.CT.getAileron() < 0F)
        {
            if(bflapDep && !bFlapsOutFixed && FM.CT.getVarWing() < 0.6F)
            {
                spodegLI = Math.max(-59.5F * FM.CT.getAileron() - 4.5F, spodegDLC);
                spodegRI = -spodegDLC;
                spodegLO = Math.max(-59.5F * FM.CT.getAileron() - 4.5F, -4.5F);
                spodegRO = 4.5F;
            }
            else
            {
                spodegLI = -55F * FM.CT.getAileron();
                spodegRI = 0.0F;
                spodegLO = -55F * FM.CT.getAileron();
                spodegRO = 0.0F;
            }
        }
        else
        {
            if(bflapDep && !bFlapsOutFixed && FM.CT.getVarWing() < 0.6F)
            {
                spodegLI = spodegDLC;
                spodegRI = Math.min(-59.5F * FM.CT.getAileron() + 4.5F, -spodegDLC);
                spodegLO = -4.5F;
                spodegRO = Math.min(-59.5F * FM.CT.getAileron() + 4.5F, 4.5F);
            }
            else
            {
                spodegLI = 0.0F;
                spodegRI = -55F * FM.CT.getAileron();
                spodegLO = 0.0F;
                spodegRO = -55F * FM.CT.getAileron();
            }
        }

        if(bflapDep)
        {
            spodegLI = Math.max(59.5F * SpoilerBrakeControl - 4.5F, spodegLI);
            spodegRI = Math.min(-59.5F * SpoilerBrakeControl + 4.5F, spodegRI);
            spodegLO = Math.max(59.5F * SpoilerBrakeControl - 4.5F, spodegLO);
            spodegRO = Math.min(-59.5F * SpoilerBrakeControl + 4.5F, spodegRO);
        }
        else
        {
            spodegLI = Math.max(55F * SpoilerBrakeControl, spodegLI);
            spodegRI = Math.min(-55F * SpoilerBrakeControl, spodegRI);
            spodegLO = Math.max(55F * SpoilerBrakeControl, spodegLO);
            spodegRO = Math.min(-55F * SpoilerBrakeControl, spodegRO);
        }

        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, spodegLI, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, spodegRI, 0.0F);
        hierMesh().chunkSetAngles("AroneL2_D0", 0.0F, spodegLO, 0.0F);
        hierMesh().chunkSetAngles("AroneR2_D0", 0.0F, spodegRO, 0.0F);
    }

    public void checkSpoilers()
    {
        if(FM.Gears.nOfGearsOnGr > 1 && FM.EI.engines[0].getControlThrottle() < 0.20F && FM.EI.engines[1].getControlThrottle() < 0.20F)
        {
            SpoilerBrakeControl = FM.CT.getAirBrake();
            FM.Sq.dragAirbrakeCx = stockDragAirbrake * 2.0F;
        }
        else
        {
            SpoilerBrakeControl = 0.0F;
            FM.Sq.dragAirbrakeCx = stockDragAirbrake;
        }

        if(SpoilerBrakeControl != oldSpoilerBrakeControl)
            updateAileronVisuals();
        oldSpoilerBrakeControl = SpoilerBrakeControl;
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -70F * f, 0.0F);
        arrestor = f;
    }

    private void calculateArrestor()
    {
        if(FM.Gears.arrestorVAngle != 0.0F)
        {
            float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
            arrestor = 0.8F * arrestor + 0.2F * f1;
            moveArrestorHook(arrestor);
        }
        else
        {
            float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
            if(f2 < 0.0F && FM.getSpeedKMH() > 60F)
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

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        FM.CT.VarWingControl = Math.max(FM.CT.getVarWing(), FM.CT.getWing());
        if(FM.CT.getWing() > 0.3F || !hasHydraulicPressure)
        {
            FM.CT.ElevatorControl = 0.0F;
            FM.CT.setTrimElevatorControl(0.0F);
            FM.CT.bHasElevatorControl = false;
            updateControlsVisuals();
        }
        else
            FM.CT.bHasElevatorControl = true;
        computeVarWing();
    }

    public void moveWingFold(float f)
    {
        moveWingFold(hierMesh(), f);
    }

    private void moveInletRamps()
    {
        hierMesh().chunkSetAngles("IntakeDoor_Lf", 0.0F, 0.0F, cvt(calculateMach(), 1.1F, 2.2F, 0.0F, 13.2F));
        hierMesh().chunkSetAngles("IntakeDoor_Rf", 0.0F, 0.0F, cvt(calculateMach(), 1.1F, 2.2F, 0.0F, 13.2F));
        float fRamp3rd = 0.0F;
        if(calculateMach() < 1.2F)
            fRamp3rd = cvt(calculateMach(), 0.9F, 1.2F, 0.0F, -4.22F);
        else if(calculateMach() < 1.6F)
            fRamp3rd = cvt(calculateMach(), 1.2F, 1.4F, -4.22F, -9.85F);
        else if(calculateMach() < 2.0F)
            fRamp3rd = cvt(calculateMach(), 1.6F, 1.9F, -9.85F, -16.87F);
        else
            fRamp3rd = cvt(calculateMach(), 2.0F, 2.3F, -16.87F, -22.5F);
        hierMesh().chunkSetAngles("IntakeDoor_Lr", 0.0F, 0.0F, fRamp3rd);
        hierMesh().chunkSetAngles("IntakeDoor_Rr", 0.0F, 0.0F, fRamp3rd);
    }

    public static void forceParkingStyle(HierMesh hiermesh)
    {
        float f1 = 55F * 0.8F;  // var-wing parking position.
        hiermesh.chunkSetAngles("WingPivotL", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("WingPivotR", 0.0F, f1, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        int i = part(s);
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
                }
                else if(s.endsWith("p2"))
                    getEnergyPastArmor(8.770001F, shot);
                else if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                }
            }
            else if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int j = s.charAt(10) - 48;
                switch(j)
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
            }
            else if(s.startsWith("xxeng"))
            {
                int ien = 0;
                if(s.startsWith("xxeng1")) ien = 0;
                else if(s.startsWith("xxeng2")) ien = 1;
                debuggunnery("Engine Module " + (ien + 1) + " : Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[ien].getCylindersRatio() * 20F)
                {
                    FM.EI.engines[ien].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debuggunnery("Engine Module " + (ien + 1) + " : Engine Cams Hit, " + FM.EI.engines[ien].getCylindersOperable() + "/" + FM.EI.engines[ien].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        FM.AS.hitEngine(shot.initiator, ien, 2);
                        debuggunnery("Engine Module " + (ien + 1) + " : Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
                    {
                        FM.AS.hitEngine(shot.initiator, ien, 1);
                        debuggunnery("Engine Module " + (ien + 1) + " : Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 24000F)
                {
                    FM.AS.hitEngine(shot.initiator, ien, 3);
                    debuggunnery("Engine Module " + (ien + 1) + " : Hit - Engine Fires..");
                }
                if(s.endsWith("exht") && World.Rnd().nextFloat() < shot.power / 30000F)
                {
                    exNozzleBroken[ien] = true;
                    debuggunnery("Engine Module " + (ien + 1) + " : Hit - Exhaust nozzle broken..");
                }
            }
            else if(s.startsWith("xxCannon1") || s.startsWith("xxammo1"))
            {
                if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Cannon Disabled..");
                    FM.AS.setJamBullets(0, 0);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            }
            else if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(FM.AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel Tank (" + l + "): Pierced..");
                        FM.AS.hitTank(shot.initiator, l, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        FM.AS.hitTank(shot.initiator, l, 2);
                        debuggunnery("Fuel Tank (" + l + "): Hit..");
                    }
                }
            }
            else if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
            }
            else if(s.startsWith("xxhyd"))
                FM.AS.setInternalDamage(shot.initiator, 3);
            else if(s.startsWith("xxpnm"))
                FM.AS.setInternalDamage(shot.initiator, 1);
        }
        else
        {
            if(s.startsWith("xcockpit"))
            {
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                getEnergyPastArmor(0.05F, shot);
            }
            if(s.startsWith("xcf"))
                hitChunk("CF", shot);
            else
            if(s.startsWith("xnose"))
                hitChunk("Nose", shot);
            else
            if(s.startsWith("xtail"))
            {
                if(chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", shot);
            }
            else if(s.startsWith("xkeel"))
            {
                if(s.endsWith("1") && chunkDamageVisible("Keel1") < 2)
                    hitChunk("Keel1", shot);
                if(s.endsWith("2") && chunkDamageVisible("Keel2") < 2)
                    hitChunk("Keel2", shot);
            }
            else if(s.startsWith("xrudder"))
            {
                if(s.endsWith("1"))
                    hitChunk("Rudder1", shot);
                if(s.endsWith("2"))
                    hitChunk("Rudder2", shot);
            }
            else if(s.startsWith("xstab"))
            {
                if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                    hitChunk("StabL", shot);
                if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
                    hitChunk("StabR", shot);
            }
            else if(s.startsWith("xvator"))
            {
                if(s.startsWith("xvatorl"))
                    hitChunk("VatorL", shot);
                if(s.startsWith("xvatorr"))
                    hitChunk("VatorR", shot);
            }
            else if(s.startsWith("xwing"))
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
            }
            else if(s.startsWith("xarone"))
            {
                if(s.startsWith("xaronel"))
                    hitChunk("AroneL", shot);
                if(s.startsWith("xaroner"))
                    hitChunk("AroneR", shot);
            }
            else if(s.startsWith("xflap"))
            {
                if(s.endsWith("01"))
                    hitChunk("Flap01", shot);
                if(s.endsWith("02"))
                    hitChunk("Flap02", shot);
                if(s.endsWith("03"))
                    hitChunk("Flap03", shot);
                if(s.endsWith("04"))
                    hitChunk("Flap04", shot);
            }
            else if(s.startsWith("xgear"))
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
            }
            else if(s.startsWith("xpilot") || s.startsWith("xhead"))
            {
                byte byte0 = 0;
                int i1;
                if(s.endsWith("a"))
                {
                    byte0 = 1;
                    i1 = s.charAt(6) - 49;
                }
                else if(s.endsWith("b"))
                {
                    byte0 = 2;
                    i1 = s.charAt(6) - 49;
                }
                else
                {
                    i1 = s.charAt(5) - 49;
                }
                hitFlesh(i1, shot, byte0);
            }
        }

        hierMesh().chunkVisible("AroneL2_D0", hierMesh().isChunkVisible("AroneL_D0"));
        hierMesh().chunkVisible("AroneL2_D1", hierMesh().isChunkVisible("AroneL_D1"));
        hierMesh().chunkVisible("AroneR2_D0", hierMesh().isChunkVisible("AroneR_D0"));
        hierMesh().chunkVisible("AroneR2_D1", hierMesh().isChunkVisible("AroneR_D1"));
        hierMesh().chunkVisible("WingLCoveDoorI_D0", hierMesh().isChunkVisible("WingLOut_D0"));
        hierMesh().chunkVisible("WingLCoveDoorI_D2", hierMesh().isChunkVisible("WingLOut_D1") || hierMesh().isChunkVisible("WingLOut_D2"));
        hierMesh().chunkVisible("WingLCoveDoorO_D0", hierMesh().isChunkVisible("WingLOut_D0"));
        hierMesh().chunkVisible("WingLCoveDoorO_D2", hierMesh().isChunkVisible("WingLOut_D1") || hierMesh().isChunkVisible("WingLOut_D2"));
        hierMesh().chunkVisible("WingRCoveDoorI_D0", hierMesh().isChunkVisible("WingROut_D0"));
        hierMesh().chunkVisible("WingRCoveDoorI_D2", hierMesh().isChunkVisible("WingROut_D1") || hierMesh().isChunkVisible("WingROut_D2"));
        hierMesh().chunkVisible("WingRCoveDoorO_D0", hierMesh().isChunkVisible("WingROut_D0"));
        hierMesh().chunkVisible("WingRCoveDoorO_D2", hierMesh().isChunkVisible("WingROut_D1") || hierMesh().isChunkVisible("WingROut_D2"));
        hierMesh().chunkVisible("SlatL_D0", hierMesh().isChunkVisible("WingLOut_D0"));
        hierMesh().chunkVisible("SlatL_D2", hierMesh().isChunkVisible("WingLOut_D1") || hierMesh().isChunkVisible("WingLOut_D2"));
        hierMesh().chunkVisible("SlatR_D0", hierMesh().isChunkVisible("WingROut_D0"));
        hierMesh().chunkVisible("SlatR_D2", hierMesh().isChunkVisible("WingROut_D1") || hierMesh().isChunkVisible("WingROut_D2"));
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 13: // '\r'
            FM.Gears.cgear = false;
            float f = World.Rnd().nextFloat(0.0F, 1.0F);
            if(f < 0.1F)
            {
                FM.AS.hitEngine(this, 0, 100);
                if(World.Rnd().nextFloat(0.0F, 1.0F) < 0.49F)
                    FM.EI.engines[0].setEngineDies(actor);
                FM.EI.engines[1].setEngineDies(actor);
            }
            else if(f > 0.55F)
                FM.EI.engines[0].setEngineDies(actor);
            FM.EI.engines[1].setEngineDies(actor);
            return super.cutFM(i, j, actor);

        case 19: // '\023'
            FM.EI.engines[0].setEngineDies(actor);
            FM.EI.engines[1].setEngineDies(actor);
            return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

/*
    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode == 2)
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
        {
            k14Distance = 500F;
            hunted = War.GetNearestEnemyAircraft(FM.actor, 2700F, 9);
        }
        if(hunted != null)
        {
            k14Distance = (float)FM.actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
            if(k14Distance > 1500F)
                k14Distance = 1500F;
            else
            if(k14Distance < 20F)
                k14Distance = 20F;
        }
    } */

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
        for(i = 0; i < TypeSupersonic.fMachAltX.length - 1 && TypeSupersonic.fMachAltX[i] <= f; i++);
        if(i == 0)
        {
            return TypeSupersonic.fMachAltY[0];
        }
        else
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
        else
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
            for(int i = 0; i < 2; i++)
            {
                if(curthrl[1] == -1F)
                {
                    curthrl[i] = oldthrl[i] = FM.EI.engines[i].getControlThrottle();
                    continue;
                }
                curthrl[i] = FM.EI.engines[i].getControlThrottle();
                if(curthrl[i] < 1.05F)
                {
                    if((curthrl[i] - oldthrl[i]) / f > 35F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                    {
                        if(FM.actor == World.getPlayerAircraft())
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Fans Surge!!!");
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage[i] += 0.01F * (FM.EI.engines[i].getRPM() / 1000F);
                        FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage[i]);
                        if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                            FM.AS.hitEngine(this, i, 100);
                        if(World.Rnd().nextFloat() < 0.05F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                            FM.EI.engines[i].setEngineDies(this);
                    }
                    if((curthrl[i] - oldthrl[i]) / f < -35F && (curthrl[i] - oldthrl[i]) / f > -100F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6)
                    {
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage[i] += 0.001F * (FM.EI.engines[i].getRPM() / 1000F);
                        FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage[i]);
                        if(World.Rnd().nextFloat() < 0.4F && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
                        {
                            if(FM.actor == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                            FM.EI.engines[i].setEngineStops(this);
                        }
                        else if(FM.actor == World.getPlayerAircraft())
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Fans Surge!!!");
                    }
                }
                oldthrl[i] = curthrl[i];
            }

        }
    }

    private void gearlimit()
    {
        float f = FM.getSpeedKMH() - 650F;
        if(f < 0.0F)
            f = 0.0F;
        FM.CT.dvGear = 0.2F - f / 500F;
        if(FM.CT.dvGear < 0.0F)
            FM.CT.dvGear = 0.0F;
    }


    public void update(float f)
    {
        if(lastUpdateTime != Time.current())
        {
            guidedMissileUtils.update();
            if((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && FM.getSpeedKMH() > 15F)
            {
                overrideBailout = true;
                FM.AS.bIsAboutToBailout = false;
                if(FM.crew > 1)
                {
                    if(Time.current() > lTimeNextEject)
                        bailout();
                }
                else
                {
                    bailout();
                }
            }
            if(FM.AS.isMaster() && Config.isUSE_RENDER())
            {
                for(int en = 0; en < 2; en++)
                {
                    if(FM.EI.engines[en].getThrustOutput() > 0.5F && FM.EI.engines[en].getStage() == 6)
                    {
                        if(FM.EI.engines[en].getThrustOutput() > 1.001F)
                            FM.AS.setSootState(this, en, 5);
                        else if(FM.EI.engines[en].getThrustOutput() > 0.96F)
                            FM.AS.setSootState(this, en, 4);
                        else if(FM.EI.engines[en].getThrustOutput() > 0.88F)
                            FM.AS.setSootState(this, en, 3);
                        else if(FM.EI.engines[en].getThrustOutput() > 0.7F)
                            FM.AS.setSootState(this, en, 2);
                        else
                            FM.AS.setSootState(this, en, 1);
                    }
                    else
                    {
                        FM.AS.setSootState(this, en, 0);
                    }
                    setExhaustFlame(Math.round(Aircraft.cvt(FM.EI.engines[en].getThrustOutput(), 0.76F, 0.94F, 0.0F, 12F)), en);
                }
            }
            if(FLIR)
                laser(getLaserSpot());
            updatecontrollaser();
//        typeFighterAceMakerRangeFinder();
            soundbarier();
            rwrUtils.update();
            backfire = rwrUtils.getBackfire();
            bRadarWarning = rwrUtils.getRadarLockedWarning();
            bMissileWarning = rwrUtils.getMissileWarning();
            if(backfire)
                backFire();
            checkHydraulicStatus();
            computeEngineOilPressure();
            computeLift();
            computeVarWing();
            computeFlapsFixing();
            if(FM.isTick(44, 1))
                computeSupersonicLimiter();
            computeAoAunits();
            FlapAssistTakeoff();
            if(FM.crew > 1 && obsMove < obsMoveTot && !bObserverKilled && !FM.AS.isPilotParatrooper(1))
            {
                if(obsMove < 0.2F || obsMove > obsMoveTot - 0.2F)
                    obsMove += 0.3F * 0.03F;
                else
                if(obsMove < 0.1F || obsMove > obsMoveTot - 0.1F)
                    obsMove += 0.15F;
                else
                    obsMove += 1.2F * 0.03F;
                obsLookAzimuth = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsAzimuthOld, obsAzimuth);
                obsLookElevation = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsElevationOld, obsElevation);
                hierMesh().chunkSetAngles("Head2_D0", 0.0F, obsLookAzimuth, obsLookElevation);
            }
            gearlimit();
            moveInletRamps();
            checkSpoilers();
        }
        engineSurge(f);
        computeCombatFlaps(f);

        super.update(f);

        if(lastUpdateTime != Time.current())
        {
            if(this instanceof F_14A)
            {
                int step;

                if(!exNozzleBroken[0])
                {
                    fNozzleOpenL = FM.EI.engines[0].getPowerOutput() <= 0.92F ? cvt(FM.EI.engines[0].getPowerOutput(), 0.0F, 0.92F, 0F, 0.9999F) : cvt(FM.EI.engines[0].getPowerOutput(), 0.92F, 1.1F, 0.9999F, 0F);
                    step = (int)Math.floor(fNozzleOpenL * 10F);
                    for(int i = 0; i < 10; i++)
                        hierMesh().chunkVisible("ExhaustL" + i, i == step);
                }

                if(!exNozzleBroken[1])
                {
                    fNozzleOpenR = FM.EI.engines[1].getPowerOutput() <= 0.92F ? cvt(FM.EI.engines[1].getPowerOutput(), 0.0F, 0.92F, 0F, 0.9999F) : cvt(FM.EI.engines[1].getPowerOutput(), 0.92F, 1.1F, 0.9999F, 0F);
                    step = (int)Math.floor(fNozzleOpenR * 10F);
                    for(int i = 0; i < 10; i++)
                        hierMesh().chunkVisible("ExhaustR" + i, i == step);
                }
            }
            if((this instanceof F_14B) || (this instanceof F_14D))
            {
                float deg;

                if(!exNozzleBroken[0])
                {
                    fNozzleOpenL = FM.EI.engines[0].getPowerOutput() <= 0.92F ? cvt(FM.EI.engines[0].getPowerOutput(), 0.0F, 0.92F, 0.0F, 1.0F) : cvt(FM.EI.engines[0].getPowerOutput(), 0.92F, 1.1F, 1.0F, 0.0F);
                    deg = -9F * (1F - fNozzleOpenL);
                    for(int i = 1; i < 33; i++)
                        hierMesh().chunkSetAngles("Eflap" + i, deg, 0.0F, 0.0F);
                }

                if(!exNozzleBroken[1])
                {
                    fNozzleOpenR = FM.EI.engines[1].getPowerOutput() <= 0.92F ? cvt(FM.EI.engines[1].getPowerOutput(), 0.0F, 0.92F, 0.0F, 1.0F) : cvt(FM.EI.engines[1].getPowerOutput(), 0.92F, 1.1F, 1.0F, 0.0F);
                    deg = -9F * (1F - fNozzleOpenR);
                    for(int j = 33; j > 32 && j < 65; j++)
                        hierMesh().chunkSetAngles("Eflap" + j, deg, 0.0F, 0.0F);
                }
            }

            if(FM.getSpeedKMH() > 300F)
                FM.CT.cockpitDoorControl = 0.0F;

            if(FM.CT.getArrestor() > 0.2F)
                calculateArrestor();

            float f1 = cvt(FM.getSpeedKMH(), 500F, 1000F, 0.999F, 0.601F);
            if(FM.getSpeed() > 7F && World.Rnd().nextFloat() < getAirDensityFactor(FM.getAltitude()))
            {
                if(FM.getOverload() > 5.7F)
                {
                    pull01 = Eff3DActor.New(this, findHook("_Pull01"), null, f1, "3DO/Effects/Aircraft/Pullingvapor.eff", -1F);
                    pull02 = Eff3DActor.New(this, findHook("_Pull02"), null, f1, "3DO/Effects/Aircraft/Pullingvapor.eff", -1F);
                    pull03 = Eff3DActor.New(this, findHook("_Pull03"), null, f1, "3DO/Effects/Aircraft/Pullingvapor.eff", -1F);
                    pull04 = Eff3DActor.New(this, findHook("_Pull04"), null, f1, "3DO/Effects/Aircraft/Pullingvapor.eff", -1F);
                }
                else
                {
                    Eff3DActor.finish(pull01);
                    Eff3DActor.finish(pull02);
                    Eff3DActor.finish(pull03);
                    Eff3DActor.finish(pull04);
                }
            }
            // flap effect plus only for players without Autopilot
            if(!FM.Gears.onGround() && FM.getSpeedKMH() < 550F && (FM instanceof RealFlightModel) && ((RealFlightModel) FM).isRealMode())
            {
                if(FM.CT.getFlap() > 0.99F)
                {
                    float zp = cvt(FM.getAOA(), 0.3F, 10.0F, 0.001F, 4.0F);
                    if(FM.getSpeedKMH() < 200F)
                        zp *= cvt(FM.getSpeedKMH(), 160F, 200F, 0F, 10000F);
                    else
                        zp *= cvt(FM.getSpeedKMH(), 200F, 360F, 10000F, 16000F);
                    FM.producedAF.z += (double)zp;
                    if(bDLCengaged)
                    {
                        if(iDLCcommand > 0)
                            FM.producedAF.z -= (double)((float)iDLCcommand * FM.getSpeedKMH() * 1.2F + 100F);
                        else
                            FM.producedAF.z += (double)((float)iDLCcommand * FM.getSpeedKMH() * 0.8F - 100F);
                    }
                }
            }
        }
        lastUpdateTime = Time.current();
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

    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
    }

    private void AILaserControl()
    {
        Point3d point3d = new Point3d();
        Point3d point3dtemp = new Point3d();
        boolean bLaserReach = false;
        if(((Maneuver)FM).target_ground != null)
        {
            point3d = ((Maneuver)FM).target_ground.pos.getAbsPoint();
            bLaserReach = (this.pos.getAbsPoint().distance(point3d) < 20000D &&
                (Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) > 0.98F) &&
                !Landscape.rayHitHQ(this.pos.getAbsPoint(), point3d, point3dtemp));
        }
        if(bHasPaveway && FM.AP.way.curr().Action == 3 && ((Maneuver)FM).target_ground != null && !bAILaserOn)
        {
            if(bLaserReach)
            {
                bAILaserOn = true;
                setLaserOn(true);
                lAILaserTimer = -1L;
            }
        }
        else if(bAILaserOn)
        {
            if(((Maneuver)FM).target_ground == null || !bLaserReach)
            {
                bAILaserOn = false;
                setLaserOn(false);
                lAILaserTimer = -1L;
            }
            else if(lAILaserTimer > 0L && Time.current() > lAILaserTimer)
            {
                bAILaserOn = false;
                setLaserOn(false);
                lAILaserTimer = -1L;
            }
            else if(lAILaserTimer == -1L)
                lAILaserTimer = Time.current() + 20000L + (long)Math.min(FM.getAltitude(), 4000F) * 5L;
        }
        if(bAILaserOn && ((Maneuver)FM).target_ground != null && ((Maneuver)FM).target_ground.pos != null && Actor.isValid(((Maneuver)FM).target_ground))
        {
            setLaserSpot(point3d);
        }
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
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 0.4F, "3DO/Effects/Aircraft/BlackMediumWtTSPD.eff", -1F);
            break;

        case 2: // '\002'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 0.8F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 3: // '\003'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;

        case 4: // '\004'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 4F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.5F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;

        case 5: // '\005'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 3F, "3DO/Effects/Aircraft/AfterBurnerF100D.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.5F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;
        }
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("BrakeL_D0", 0.0F, -40F * f, 0.0F);
        hierMesh().chunkSetAngles("BrakeR_D0", 0.0F, 40F * f, 0.0F);
    }

    public void setExhaustFlame(int i, int j)
    {
        String pfl = "";
        if(j == 1)
            pfl = "b";

        switch(i)
        {
            case 0: // '\0'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 1: // '\001'
                hierMesh().chunkVisible("Exhaust1" + pfl, true);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 2: // '\002'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, true);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 3: // '\003'
                hierMesh().chunkVisible("Exhaust1" + pfl, true);
                hierMesh().chunkVisible("Exhaust2" + pfl, true);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;
                //By western:  Old code was "fall through", but why ?
                //             "fall through" makes do nothing about above 5 lines.

            case 4: // '\004'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, true);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 5: // '\005'
                hierMesh().chunkVisible("Exhaust1" + pfl, true);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, true);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 6: // '\006'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, true);
                hierMesh().chunkVisible("Exhaust3" + pfl, true);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 7: // '\007'
                hierMesh().chunkVisible("Exhaust1" + pfl, true);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, true);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 8: // '\b'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, true);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, true);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 9: // '\t'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, true);
                hierMesh().chunkVisible("Exhaust4" + pfl, true);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;

            case 10: // '\n'
                hierMesh().chunkVisible("Exhaust1" + pfl, true);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, true);
                break;

            case 11: // '\013'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, true);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, true);
                break;

            case 12: // '\f'
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, true);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, true);
                break;

            default:
                hierMesh().chunkVisible("Exhaust1" + pfl, false);
                hierMesh().chunkVisible("Exhaust2" + pfl, false);
                hierMesh().chunkVisible("Exhaust3" + pfl, false);
                hierMesh().chunkVisible("Exhaust4" + pfl, false);
                hierMesh().chunkVisible("Exhaust5" + pfl, false);
                break;
        }
    }

    private void bailout()
    {
        if(overrideBailout)
            if(FM.AS.astateBailoutStep >= 0 && FM.AS.astateBailoutStep < 2)
            {
                if(FM.CT.cockpitDoorControl > 0.5F && FM.CT.getCockpitDoor() > 0.5F)
                    FM.AS.astateBailoutStep = 11;
                else
                    FM.AS.astateBailoutStep = 2;
            }
            else if(FM.AS.astateBailoutStep >= 2 && FM.AS.astateBailoutStep <= 3)
            {
                switch(FM.AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(FM.CT.cockpitDoorControl < 0.5F)
                        doRemoveBlisters();
                    break;

                case 3: // '\003'
                    lTimeNextEject = Time.current() + 1000L;
                    break;
                }
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                FM.AS.astateBailoutStep = (byte)(FM.AS.astateBailoutStep + 1);
                if(FM.AS.astateBailoutStep == 4)
                    FM.AS.astateBailoutStep = 11;
            }
            else if(FM.AS.astateBailoutStep >= 11 && FM.AS.astateBailoutStep <= 19)
            {
                byte byte0 = FM.AS.astateBailoutStep;
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                FM.AS.astateBailoutStep = (byte)(FM.AS.astateBailoutStep + 1);
                if((super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_maneuver() != 44)
                {
                    World.cur();
                    if(FM.AS.actor != World.getPlayerAircraft())
                        ((Maneuver)super.FM).set_maneuver(44);
                }
                if(FM.AS.astatePilotStates[byte0 - 11] < 99)
                {
                    if(byte0 == 11)
                    {
                        doRemoveBodyFromPlane(2);
                        doEjectCatapultStudent();
                        lTimeNextEject = Time.current() + 1000L;
                    }
                    else if(byte0 == 12)
                    {
                        doRemoveBodyFromPlane(1);
                        doEjectCatapultInstructor();
                        FM.AS.astateBailoutStep = 51;
                        super.FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        FM.CT.bHasVarWingControl = false;
                        FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                    FM.AS.astatePilotStates[byte0 - 11] = 99;
                }
                else
                {
                    EventLog.type("astatePilotStates[" + (byte0 - 11) + "]=" + FM.AS.astatePilotStates[byte0 - 11]);
                }
            }
    }

    public void computeCombatFlaps(float f)
    {
        // Auto combat flaps is only F-14B and F-14D
        if(this instanceof F_14A)
            return;

        if(FM.CT.FlapsControl <= 0.30F && FM.CT.FlapsControl >= 0.20F)
        {
            if(!autoEng)
            {
                autoEng = true;
                curFlaps = FM.CT.getFlap();
                if(this == World.getPlayerAircraft())
                    HUD.log("FlapsCombat");
            }
            desiredPosition = 0.2857F * Math.max(Aircraft.cvt(FM.getAOA(), 6.0F, 15F, 0.0F, 1.0F), 1.0F - Aircraft.cvt(FM.getSpeedKMH(), 350F, 500F, 0.0F, 1.0F));
            curFlaps = flapsMovement(f, desiredPosition, curFlaps, 999F, 0.25F);
            FM.CT.forceFlaps(curFlaps);
            if(Math.abs(desiredPosition - curFlaps) >= 0.02F)
                sfxFlaps(true);
            else
                sfxFlaps(false);
        }
        else
        {
            autoEng = false;
        }
    }

    private float flapsMovement(float f, float f1, float f2, float f3, float f4)
    {
        float f5 = (float)Math.exp(-f / f3);
        float f6 = f1 + (f2 - f1) * f5;
        if(f6 < f1)
        {
            f6 += f4 * f;
            if(f6 > f1)
                f6 = f1;
        }
        else if(f6 > f1)
        {
            f6 -= f4 * f;
            if(f6 < f1)
                f6 = f1;
        }
        return f6;
    }

    private void computeFlapsFixing()
    {
        // when flap position is above 10 degrees, force 10 degrees Combat mode automatically.
        if(FM.CT.FlapsControl > 0.30F && FM.getSpeedKMH() > 550F)
        {
            if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() && (FM instanceof Pilot))
            {
                Main3D.cur3D().aircraftHotKeys.setFlapIndex(1);
                HUD.log("FlapsDegree", new Object[] { new Float(((float)Math.floor((double)FM.CT.FlapsControl * FM.CT.FlapStageMax * 100D + 0.06D) / 100F)) });
            }
            FM.CT.FlapsControl = 0.2857F;
        }

    // Only Leading Edge Slats working in any speed and any VG-wing degree
        if(FM.getSpeedKMH() > 740F && !bFlapsOutFixed)
        {
            if(!bStableAIGround)
            {
                Polares polares = (Polares)Reflection.getValue(FM, "Wing");
                polares.Cy0_1 = (0.8F * stockCy0_0 + 0.2F * stockCy0_1) * (1.0F - 0.5F * SpoilerBrakeControl);
                polares.CxMin_1 = 0.8F * stockCxMin_0 + 0.2F * stockCxMin_1;
                polares.parabCxCoeff_1 = 0.8F * stockparabCxCoeff_0 + 0.2F * stockparabCxCoeff_1;
                FM.Sq.squareFlaps = 0.35F * stockSqFlaps;
            }
            bFlapsOutFixed = true;
            bFlapsInFixed = true;
            bTakeoffFlapAssist = false;
        }
        else if(FM.getSpeedKMH() <= 740F && bFlapsOutFixed)
        {
            if(!bStableAIGround)
            {
                Polares polares = (Polares)Reflection.getValue(FM, "Wing");
                polares.Cy0_1 = stockCy0_1 * (1.0F - 0.5F * SpoilerBrakeControl);
                polares.CxMin_1 = stockCxMin_1;
                polares.parabCxCoeff_1 = stockparabCxCoeff_1;
                FM.Sq.squareFlaps = stockSqFlaps;
            }
            bFlapsOutFixed = false;
        }

        if(bFlapsOutFixed)
            bFlapsInFixed = true;
        else
        {
            if(FM.CT.getVarWing() > 0.02F && !bFlapsInFixed)
            {
                if(!bStableAIGround)
                {
                    Polares polares = (Polares)Reflection.getValue(FM, "Wing");
                    polares.Cy0_1 = (0.14F * stockCy0_0 + 0.86F * stockCy0_1)  * (1.0F - 0.5F * SpoilerBrakeControl);
                    polares.CxMin_1 = 0.14F * stockCxMin_0 + 0.86F * stockCxMin_1;
                    polares.parabCxCoeff_1 = 0.14F * stockparabCxCoeff_0 + 0.86F * stockparabCxCoeff_1;
                    FM.Sq.squareFlaps = 0.90F * stockSqFlaps;
                }
                bFlapsInFixed = true;
                bTakeoffFlapAssist = false;
            }
            else if(FM.CT.getVarWing() <= 0.02F && !bTakeoffFlapAssist && bFlapsInFixed)
            {
                if(!bStableAIGround)
                {
                    Polares polares = (Polares)Reflection.getValue(FM, "Wing");
                    polares.Cy0_1 = stockCy0_1 * (1.0F - 0.5F * SpoilerBrakeControl);
                    polares.CxMin_1 = stockCxMin_1;
                    polares.parabCxCoeff_1 = stockparabCxCoeff_1;
                    FM.Sq.squareFlaps = stockSqFlaps;
                }
                bFlapsInFixed = false;
            }
            else if(!bFlapsInFixed)
            {
                if(!bStableAIGround)
                {
                    Polares polares = (Polares)Reflection.getValue(FM, "Wing");
                    polares.Cy0_1 = stockCy0_1 * (1.0F - 0.5F * SpoilerBrakeControl);
                }
            }
        }

        moveFlap(FM.CT.getFlap());
    }


    public void computeLift()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float x = calculateMach();
        if(x < 0.7F)
        {
            polares.lineCyCoeff = floatindex(cvt(x, 0.0F, 0.7F, 0.0F, 14.0F), linecycoeffScale0007);
            //{{0,0.15},{0.3,0.14},{0.5,0.13},{0.7,0.08}}
        }
        else if(x < 0.9F)
        {
            polares.lineCyCoeff = 0.08F;
        }
        else if(x < 2.3F)
        {
            tmpx = (double)x;
            tmpx2 = tmpx * tmpx;  // faster than using Math.pow(double, double)
            tmpx3 = tmpx2 * tmpx;
            tmpx4 = tmpx3 * tmpx;
            polares.lineCyCoeff = (float)(0.034863946D * tmpx4 - 0.27117347D * tmpx3 + 0.78577381D * tmpx2 - 1.025247449D * tmpx + 0.541057143D);
    // {{0.9, 0.08}, {1.3, 0.04},{1.6, 0.03},{2.1, 0.02}, {2.3, 0.016}}
        }
        else
        {
            polares.lineCyCoeff = 0.016F;
        }
    }


    public void computeSupersonicLimiter()
    {
        float x = FM.getAltitude() / 1000F;
        float Drag = 0.0F;
        if(((FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6)
            || (FM.EI.engines[1].getThrustOutput() > 1.001F && FM.EI.engines[1].getStage() == 6))
           && calculateMach() > 1.1F)
        {
            if(x > 4F)
            {
                Drag = 0.0F;
            }
            else
            {
                Drag = 0.0198F - 0.00495F * x;
        //{{0,0.0198},{4, 0.0}}
                Drag *= cvt(calculateMach(), 1.1F, 1.12F, 0.0F, 1.0F);
            }
        }
        FM.Sq.dragParasiteCx += Drag + dragPylons;
    }

    protected void moveVarWing(float f)
    {
        float f1 = 55F * f;
        hierMesh().chunkSetAngles("WingPivotL", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("WingPivotR", 0.0F, f1, 0.0F);
    }

    public void computeVarWing()
    {
        if(FM == null || !(FM instanceof Pilot) || !FM.CT.bHasVarWingControl)
            return;
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");

        if((!(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode()) && (FM instanceof Maneuver))
        {
            //When AI goes 'GAttack' waypoint, set VarWingControlSwitch = "BOMB". Others "AUTO".
            if(calculateMach() > 0.6F &&(FM.AP.way.curr().Action == 3 || (((Maneuver)FM).Group != null && ((Maneuver)FM).Group.grTask == 4) || ((Maneuver)FM).get_task() == 4))
            {
                FM.CT.VarWingControlSwitch = 4;
                FM.CT.setTrimElevatorControl(0.50F);
            }
            else if(FM.AP.way.isLanding() && (((Maneuver)FM).get_maneuver() == 21 || ((Maneuver)FM).get_maneuver() == 2))
            {
                FM.CT.VarWingControlSwitch = 1;
                FM.CT.setTrimElevatorControl(0.60F);
            }
            else if(FM.AP.way.isLanding() && ((Maneuver)FM).get_maneuver() == 25)
            {
                if(((Maneuver)FM).getTaxiMode())
                {
                    if(lEnteredTaxiMode == -1L)
                    {
                        FM.CT.VarWingControlSwitch = 1;
                        FM.CT.setTrimElevatorControl(0.0F);
                        lEnteredTaxiMode = Time.current();
                    }
                    else if(Time.current() > lEnteredTaxiMode + 10000L)
                        FM.CT.VarWingControlSwitch = 3;
                    else
                        FM.CT.VarWingControlSwitch = 1;
                }
                else
                {
                    FM.CT.VarWingControlSwitch = 1;
                    FM.CT.setTrimElevatorControl(0.10F);
                }
            }
            else if(bLandedAI && ((Maneuver)FM).get_maneuver() == 1)
                FM.CT.VarWingControlSwitch = 3;
            else if(((Maneuver)FM).get_maneuver() == 26)
            {
                FM.CT.VarWingControlSwitch = 1;
                FM.CT.setTrimElevatorControl(0.0F);
            }
            else
            {
                FM.CT.VarWingControlSwitch = 0;
                FM.CT.setTrimElevatorControl(0.0F);
            }
        }

        float target_value = 0.0F;

        // VarWing degree decision part

        // calculate Flight Computer Program value
        if(calculateMach() < 0.435F)
            varWingProgram = 0F;   // 20 deg.
        else if(calculateMach() < 0.55F)
            varWingProgram = 0.036364F;   // 22 deg.
        else if(calculateMach() < 0.97F)
        {
            if(FM.getAltitude() > 6090F)   // over 20000 feet
            {
                if(calculateMach() < 0.64F)
                    varWingProgram = 0.036364F;   // 22 deg.
                else if(calculateMach() < 0.7F)
                    varWingProgram = cvt(calculateMach(), 0.64F, 0.7F, 0.036364F, 0.0964F);   // 22 ~ 25.88 deg.
                else if(calculateMach() < 0.738F)
                    varWingProgram = cvt(calculateMach(), 0.7F, 0.738F, 0.0964F, 0.1396F);   // 25.88 ~ 28.53 deg.
                else if(calculateMach() < 0.776F)
                    varWingProgram = cvt(calculateMach(), 0.738F, 0.776F, 0.1396F, 0.2045F);   // 28.53 ~ 32.5 deg.
                else if(calculateMach() < 0.8F)
                    varWingProgram = cvt(calculateMach(), 0.7776F, 0.8F, 0.2045F, 0.3F);   // 32.5 ~ 37.869 deg.
                else if(calculateMach() < 0.87222F)
                    varWingProgram = cvt(calculateMach(), 0.8F, 0.87222F, 0.3F, 0.49F);   // 37.869 ~ 50 deg.
                else
                    varWingProgram = cvt(calculateMach(), 0.87222F, 0.97F, 0.49F, 0.6555F);   // 50 ~ 60 deg.
            }
            else   // below 20000 feet
            {
                if(calculateMach() < 0.6F)
                    varWingProgram = cvt(calculateMach(), 0.55F, 0.6F, 0.036364F, 0.0964F);   // 22 ~ 25.88 deg.
                else if(calculateMach() < 0.66F)
                    varWingProgram = cvt(calculateMach(), 0.6F, 0.66F, 0.0964F, 0.1636F);   // 25.88 ~ 30 deg.
                else if(calculateMach() < 0.75F)
                    varWingProgram = cvt(calculateMach(), 0.66F, 0.75F, 0.1636F, 0.2636F);   // 30 ~ 36.11 deg.
                else if(calculateMach() < 0.8F)
                    varWingProgram = cvt(calculateMach(), 0.75F, 0.8F, 0.2636F, 0.3272F);   // 36.11 ~ 40 deg.
                else if(calculateMach() < 0.87222F)
                    varWingProgram = cvt(calculateMach(), 0.8F, 0.87222F, 0.3272F, 0.49F);   // 40 ~ 50 deg.
                else
                    varWingProgram = cvt(calculateMach(), 0.87222F, 0.97F, 0.49F, 0.6555F);   // 50 ~ 60 deg.
            }
        }
        else if(calculateMach() < 1.1F)
            varWingProgram = cvt(calculateMach(), 0.97F, 1.1F, 0.6555F, 0.7373F);   // 60 ~ 65 deg.
        else
            varWingProgram = cvt(calculateMach(), 1.1F, 1.2F, 0.7373F, 0.8F);   // 65 ~ 68 deg.

        switch(FM.CT.VarWingControlSwitch)
        {
        default:
        case 0:   // AUTO
            target_value = varWingProgram;
            oldVarWingControlSwitch = 0;
            break;
        case 1:   // Extend
            if(oldVarWingControlSwitch != 1)
                tVarWingInput = Time.current();
            if(Time.current() > tVarWingInput + 400L)
            {
                target_value = FM.CT.VarWingControl - 0.005F;
                if(target_value < 0.0F)
                    target_value = 0.0F;
            }
            else
                target_value = FM.CT.VarWingControl;
            oldVarWingControlSwitch = 1;
            break;
        case 2:   // Keep
            target_value = FM.CT.VarWingControl;
            if(calculateMach() < 0.97F && target_value > 0.65549F)
                target_value = 0.65549F;
            oldVarWingControlSwitch = 2;
            break;
        case 3:   // Retract
            if(oldVarWingControlSwitch != 3)
                tVarWingInput = Time.current();
            if(Time.current() > tVarWingInput + 400L)
            {
                target_value = FM.CT.VarWingControl + 0.005F;
                if(FM.CT.getWing() < 0.8F && target_value > 0.8F)
                    target_value = 0.8F;
                if(calculateMach() < 0.97F && FM.CT.getWing() < 0.65549F && target_value > 0.65549F)
                    target_value = 0.65549F;
            }
            else
                target_value = FM.CT.VarWingControl;
            oldVarWingControlSwitch = 3;
            break;
        case 4:   // BOMB; historically fixed 55 degrees, but 48 degrees to avoid flight character too drastic change.
            target_value = 0.4827F;  // 0.6034F;
            oldVarWingControlSwitch = 4;
            break;
        }
        if(target_value < varWingExtendLimitter())
            target_value = varWingExtendLimitter();
        if(!hasHydraulicPressure)
            target_value = 0.8F;  // ground parking
        FM.CT.VarWingControl = Math.max(target_value, FM.CT.getWing());

        // VarWing flight charactoristic part
        if(!bStableAIGround)
        {
            float fsweep = 0F;
            if(FM.CT.getVarWing() < 0.24F)
                fsweep = cvt(FM.CT.getVarWing(), 0.0F, 0.24F, 0.0F, 1.0F);
            else if(FM.CT.getVarWing() < 0.4F)
                fsweep = cvt(FM.CT.getVarWing(), 0.24F, 0.4F, 1.0F, 2.0F);
            else if(FM.CT.getVarWing() < 0.64F)
                fsweep = cvt(FM.CT.getVarWing(), 0.4F, 0.64F, 2.0F, 3.0F);
            else
                fsweep = cvt(FM.CT.getVarWing(), 0.64F, 0.8F, 3.0F, 4.0F);
            Reflection.setFloat(FM, "GCenter", stockGCenter + floatindex(fsweep, gcenterScale));
            Reflection.setFloat(FM, "DefaultElevatorTrim", stockDefaultElevatorTrim + floatindex(fsweep, defaultelevtrimScale));
            polares.CxMin_0 = floatindex(fsweep, cxmin0Scale);
            polares.Cy0_0 = floatindex(fsweep, cy00Scale) * (1.0F - 0.5F * SpoilerBrakeControl);
            polares.CyCritH_0 = floatindex(fsweep, cycrith0Scale);
            FM.Sq.squareWing = stockSqWing - floatindex(fsweep, wingsquredecScale) * 2F;
            FM.Sq.liftWingLOut = FM.Sq.liftWingROut = stockSqWingOut - floatindex(fsweep, wingsquredecScale);
            FM.Wingspan = floatindex(fsweep, wingspanScale);
        }
    }

    private float varWingExtendLimitter()
    {
        float limit = 0F;
        if(calculateMach() < 0.435F)
            limit = 0F;   // 20 deg.
        else if(calculateMach() < 0.75F)
            limit = 0.036364F;   // 22 deg.
        else if(calculateMach() < 0.8F)
            limit = cvt(calculateMach(), 0.75F, 0.8F, 0.036364F, 0.3F);   // 22 ~ 37.869 deg.
        else if(calculateMach() < 0.87222F)
            limit = cvt(calculateMach(), 0.8F, 0.87222F, 0.3F, 0.49F);   // 37.869 ~ 50 deg.
        else if(calculateMach() < 0.97F)
            limit = cvt(calculateMach(), 0.87222F, 0.97F, 0.49F, 0.6555F);   // 50 ~ 60 deg.
        else if(calculateMach() < 1.1F)
            limit = cvt(calculateMach(), 0.97F, 1.1F, 0.6555F, 0.7373F);   // 60 ~ 65 deg.
        else
            limit = cvt(calculateMach(), 1.1F, 1.2F, 0.7373F, 0.8F);   // 65 ~ 68 deg.

        return limit;
    }


    protected void computeF110GE400_AB()
    {
        for(int en = 0; en < 2; en++)
        {
            if(FM.EI.engines[en].getThrustOutput() > 1.001F && FM.EI.engines[en].getStage() > 5)
                FM.producedAF.x += (40600D * (exNozzleBroken[en] ? 0.90D : 1.0D));
            if(FM.EI.engines[en].getThrustOutput() > 1.001F && FM.EI.engines[en].getStage() > 5 && calculateMach() > 0.85F)
                FM.producedAF.x += (12000D * (exNozzleBroken[en] ? 0.90D : 1.0D));
            if(FM.EI.engines[en].getThrustOutput() > 1.001F && FM.EI.engines[en].getStage() > 5 && calculateMach() > 1.3F)
                FM.producedAF.x -= 34000D;
        }
        float x = FM.getAltitude() / 1000F;
        float thrustDegradation = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6
         && FM.EI.engines[1].getThrustOutput() > 1.001F && FM.EI.engines[1].getStage() == 6)
            if(x > 19.0F)
            {
                thrustDegradation = 25.0F;
            }
            else
            {
                tmpx = (double)x;
                tmpx2 = tmpx * tmpx;  // faster than using Math.pow(double, double)
                tmpx3 = tmpx2 * tmpx;
                tmpx4 = tmpx3 * tmpx;
                tmpx5 = tmpx4 * tmpx;
                tmpx6 = tmpx5 * tmpx;
                thrustDegradation = (float)(-0.0001198866D * tmpx6 + 0.0057088098D * tmpx5 - 0.0926952325D * tmpx4 + 0.6216011457D * tmpx3 - 2.00471265D * tmpx2 + 3.6770779891D * tmpx);
        //{{0,0},{2,3},{5,4},{7,0},{12,-30},{17,7},{19,25}}
            }
        if(exNozzleBroken[0]) thrustDegradation += 0.3F;
        if(exNozzleBroken[1]) thrustDegradation += 0.3F;
        FM.producedAF.x -=  thrustDegradation * 1000F;
    }


    private void FlapAssistTakeoff()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");

        if(bSpawnedAsAI)
        {
            if(FM.Gears.onGround() && FM.getSpeedKMH() < 160F && !bStableAIGround)
            {
                polares.Cy0_0 = stockCy0_0 * 0.01F;
                polares.Cy0_1 = stockCy0_1 * 0.1F;
                Reflection.setFloat(FM, "GCenter", stockGCenter);
                FM.Sq.liftStab -= 3.0F;
                FM.Sq.squareElevators -= 6.1F;
                bStableAIGround = true;
            }
            if(FM.getSpeedKMH() > 160F && bStableAIGround)
            {
                polares.Cy0_0 = stockCy0_0;
                polares.Cy0_1 = stockCy0_1;
                Reflection.setFloat(FM, "GCenter", stockGCenter);
                FM.Sq.liftStab += 3.0F;
                FM.Sq.squareElevators += 6.1F;
                bStableAIGround = false;
            }

            if(FM.Gears.nearGround() && FM.getSpeedKMH() > 220F && !bTakeoffAIAssist)
            {
                FM.Sq.liftStab += 1.5F;
                FM.Sq.squareElevators += 3.0F;
                bTakeoffAIAssist = true;
            }
            else if(bTakeoffAIAssist)
            {
                FM.Sq.liftStab -= 1.5F;
                FM.Sq.squareElevators -= 3.0F;
                bTakeoffAIAssist = false;
            }
        }

        if(bFlapsOutFixed)
            return;

        if(FM.EI.engines[0].getThrustOutput() > 0.97F && FM.EI.engines[1].getThrustOutput() > 0.97F && (calculateMach() < 0.32F || FM.Gears.onGround()) && !bTakeoffFlapAssist && !bStableAIGround)
        {
            polares.Cy0_1 += (bSpawnedAsAI ? 1.8D : 0.8D);
            bTakeoffFlapAssist = true;
        }
        if(bTakeoffFlapAssist && !FM.Gears.nearGround())
        {
            if((FM.EI.engines[0].getThrustOutput() < 0.97F && FM.EI.engines[1].getThrustOutput() < 0.97F) || calculateMach() >= 0.32F)
            {
                polares.Cy0_1 -= (bSpawnedAsAI ? 1.8D : 0.8D);
                bTakeoffFlapAssist = false;
            }
        }
        return;
    }

    private void AiAirbrakeOperation()
    {
        if(((Maneuver)super.FM).get_maneuver() == 21)
        {
            if(FM.AP.way.isLanding() && FM.AP.way.Cur() > 0)
            {
                if((FM.getSpeed() > FM.VmaxFLAPS && FM.getSpeed() > FM.AP.way.curr().Speed * 1.4F && FM.getAltitude() > FM.AP.way.curr().z() + 20F
                   && FM.EI.engines[0].getControlThrottle() < 0.85F && FM.EI.engines[1].getControlThrottle() < 0.85F)
                   || FM.getSpeed() > 144.6F)
                {
                    if(FM.CT.AirBrakeControl != 1.0F)
                        FM.CT.AirBrakeControl = 1.0F;
                }
                else if(FM.getSpeed() < 141.0F)
                    if(FM.CT.AirBrakeControl != 0.0F)
                        FM.CT.AirBrakeControl = 0.0F;
            }
            else
            {
                if(FM.getSpeed() > FM.VmaxFLAPS && FM.getSpeed() > FM.AP.way.curr().Speed * 1.4F && FM.getAltitude() > FM.AP.way.curr().z() + 20F
                   && FM.EI.engines[0].getControlThrottle() < 0.85F && FM.EI.engines[1].getControlThrottle() < 0.85F)
                {
                    if(FM.CT.AirBrakeControl != 1.0F)
                        FM.CT.AirBrakeControl = 1.0F;
                }
                if(FM.getSpeed() < FM.AP.way.curr().Speed * 1.05F || FM.getAltitude() < FM.AP.way.curr().z()
                   || FM.EI.engines[0].getControlThrottle() > 0.96F || FM.EI.engines[1].getControlThrottle() > 0.96F)
                    if(FM.CT.AirBrakeControl != 0.0F)
                        FM.CT.AirBrakeControl = 0.0F;
            }
        }
        else if(FM.Gears.onGround() && ((Maneuver)FM).get_maneuver() == 25 && FM.AP.way.isLanding() && !FM.AP.way.isLandingOnShip())
        {
            if(FM.getSpeedKMH() > 120F)
                FM.CT.AirBrakeControl = 1.0F;
            else
                FM.CT.AirBrakeControl = 0.0F;
            bLandedAI = true;
        }
        else if(((Maneuver)super.FM).get_maneuver() == 66)
        {
            if(FM.CT.AirBrakeControl != 0.0F)
                FM.CT.AirBrakeControl = 0.0F;
        }
        else if(((Maneuver)super.FM).get_maneuver() == 7)
        {
            if(FM.CT.AirBrakeControl != 1.0F)
                FM.CT.AirBrakeControl = 1.0F;
        }
        else if(hasHydraulicPressure && FM.CT.AirBrakeControl != 0.0F)
            FM.CT.AirBrakeControl = 0.0F;
    }

    private final void doRemoveBlisters()
    {
        for(int i = 1; i < 10; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && FM.AS.getPilotHealth(i - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(FM.Vwld);
                wreckage.setSpeed(vector3d);
            }

    }

    private void anticollights()
    {
        if(FM.CT.bAntiColLights)
        {
            char postfix = (char)('A' + World.Rnd().nextInt(0, 5));
            for(int i = 0; i < 3; i++)
            {
                if(antiColLight[i] == null)
                {
                    try
                    {
                        antiColLight[i] = Eff3DActor.New(this, findHook("_AntiColLight" + Integer.toString(i + 1)), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRedFlash_" + String.valueOf(postfix) + ".eff", -1.0F, false);
                    } catch(Exception excp) {}
                }
            }
        }
        else
        {
            for(int i = 0; i < 3; i++)
              if(antiColLight[i] != null)
              {
                  Eff3DActor.finish(antiColLight[i]);
                  antiColLight[i] = null;
              }
        }
        oldAntiColLight = FM.CT.bAntiColLights;
    }

    private void formationlights()
    {
        Mission.cur();
        int ws = Mission.curCloudsType();
        Mission.cur();
        float we = Mission.curCloudsHeight() + 500F;
        if((World.getTimeofDay() <= 6.5F || World.getTimeofDay() > 18F || ws > 4 && FM.getAltitude() < we) && !FM.isPlayers())
            FM.CT.bFormationLights = true;
        if((World.getTimeofDay() > 6.5F && World.getTimeofDay() <= 18F && ws <= 4 || World.getTimeofDay() > 6.5F && World.getTimeofDay() <= 18F && FM.getAltitude() > we) && !FM.isPlayers())
            FM.CT.bFormationLights = false;
        hierMesh().chunkVisible("SlightNose", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightTail", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightKeelL", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightKeelR", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightWTipL", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightWTipR", FM.CT.bFormationLights);
    }

    private void computeEngineOilPressure()
    {
        if(lTimeOilPressChecked == Time.current())
            return;

        float targetPress = 0F;

        for(int i= 0; i < 2; i++)
        {
            if(FM.EI.engines[i].getRPM() < 1000F)
                targetPress = cvt(FM.EI.engines[i].getRPM(), 0F, 1000F, 0F, 45F);
            else if(FM.EI.engines[i].getRPM() < 4000F)
                targetPress = cvt(FM.EI.engines[i].getRPM(), 1000F, 4000F, 45F, 55F);
            else
                targetPress = cvt(FM.EI.engines[i].getRPM(), 4000F, 7000F, 55F, 100F);

            if(targetPress > engineOilPressurePSI[i])
                engineOilPressurePSI[i] += Math.min((targetPress - engineOilPressurePSI[i]) * 0.1F, 0.1F);
            else
                engineOilPressurePSI[i] -= Math.min((engineOilPressurePSI[i]- targetPress) * 0.06F, 0.06F);
        }

        lTimeOilPressChecked = Time.current();
    }

    private void computeAoAunits()
    {
        // over 250 knots
        if(FM.getSpeed() > 128.1F || FM.CT.getGear() == 0.0F || FM.CT.getFlap() < 0.29F)
            unitsAoA = FM.getAOA() * 1.84F + 4.28F;
        else
        {
            tmpx = (double)FM.getSpeed();
            if(tmpx < 0.4D) tmpx = 0.4D;
            tmpx2 = tmpx * tmpx;  // faster than using Math.pow(double, double)
            tmpx3 = tmpx2 * tmpx;
            tmpx4 = tmpx3 * tmpx;
            tmpx5 = tmpx4 * tmpx;
            tmpx6 = tmpx5 * tmpx;
            float fmass = (FM.M.mass - 18143.68F) / 4535.92F;  // index-ize with 40000, 50000, 60000, 70000 lbs.
            if(fmass < 0.0F) fmass = 0.0F;
            if(fmass > 3.0F) fmass = 3.0F;
            int i0 = (int)Math.floor(fmass);
            int j0 = 0;
            if(bDLCengaged) j0 = 1;

            if(fmass == 0.0F || fmass == 3.0F)
            {
                unitsAoA = (float)(caoa6[j0][i0] * tmpx6 + caoa5[j0][i0] * tmpx5 + caoa4[j0][i0] * tmpx4 + caoa3[j0][i0] * tmpx3 + caoa2[j0][i0] * tmpx2 + caoa1[j0][i0] * tmpx + caoa0[j0][i0]);
            }
            else
            {
                float u0 = (float)(caoa6[j0][i0] * tmpx6 + caoa5[j0][i0] * tmpx5 + caoa4[j0][i0] * tmpx4 + caoa3[j0][i0] * tmpx3 + caoa2[j0][i0] * tmpx2 + caoa1[j0][i0] * tmpx + caoa0[j0][i0]);
                float u1 = (float)(caoa6[j0][i0 + 1] * tmpx6 + caoa5[j0][i0 + 1] * tmpx5 + caoa4[j0][i0 + 1] * tmpx4 + caoa3[j0][i0 + 1] * tmpx3 + caoa2[j0][i0 + 1] * tmpx2 + caoa1[j0][i0 + 1] * tmpx + caoa0[j0][i0 + 1]);
                if(u0 > 35.0F) u0 = 35.0F;
                if(u0 < 0.0F) u0 = 0.0F;
                if(u1 > 35.0F) u1 = 35.0F;
                if(u1 < 0.0F) u1 = 0.0F;
                unitsAoA = cvt(fmass, (float)i0, (float)(i0 + 1), u0, u1);
            }
            if(unitsAoA > 25.5F) unitsAoA = 25.5F;
            else if(unitsAoA < 0.0F) unitsAoA = 0.0F;
        }
    }

    public float Fuelamount;
    public boolean radartoggle;
    public int radarmode;
    public int targetnum;
    public float lockrange;
    public int lockmode;
    public int radargunsight;
    public int radarrange;
    public int leftscreen;
    public float azimult;
    public float tangate;
    public long tf;
    public float radarvrt;
    public float radarhol;
    public int Bingofuel;
    public boolean Nvision;
    private boolean APmode1;
    private boolean APmode2;
    private boolean APmode3;
    public boolean bDLCengaged;
    private int iDLCcommand;  // from -10 ~ +10
    private float oldthrl[] = { -1.0F, -1.0F };
    private float curthrl[] = { -1.0F, -1.0F };
    private float engineSurgeDamage[] = { 0.0F, 0.0F };
    public boolean exNozzleBroken[] = { false, false };
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    public int LockState = 0;
    public Actor hunted = null;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private long lTimeNextEject;
    private float lightTime;
    private float ft;
    private LightPointWorld lLight[];
    private Hook lLightHook[];
    private Loc lLightLoc1 = new Loc();
    private Point3d lLightP1 = new Point3d();
    private Point3d lLightP2 = new Point3d();
    private Point3d lLightPL = new Point3d();
    public boolean bChangedPit = false;
    private float SonicBoom;
    private Eff3DActor shockwave;
    private boolean isSonic;
    private int obsLookTime;
    private float obsLookAzimuth;
    private float obsLookElevation;
    private float obsAzimuth;
    private float obsElevation;
    private float obsAzimuthOld;
    private float obsElevationOld;
    private float obsMove;
    private float obsMoveTot;
    boolean bObserverKilled;
//    private static Loc l = new Loc();
    private Eff3DActor pull01;
    private Eff3DActor pull02;
    private Eff3DActor pull03;
    private Eff3DActor pull04;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    public boolean trimauto;
    private long t1;
    public float fNozzleOpenL;
    public float fNozzleOpenR;
    public float hydroPress;
    private boolean bCheckedHydro1st;
    public boolean hasHydraulicPressure;
    private long lTimeHydroChecked;
    public boolean isGeneratorAlive;
    public float engineOilPressurePSI[];
    private long lTimeOilPressChecked;

    private long lastUpdateTime;
    private float dragPylons;
    public boolean bHasUnderTank;
    private boolean bSpawnedAsAI;
    private boolean bLandedAI;
    public int freq;
    public float Timer1;
    public float Timer2;
    private long tVarWingInput;
    private int oldVarWingControlSwitch;
    public float varWingProgram;
    private boolean bTakeoffFlapAssist;
    private boolean bTakeoffAIAssist;
    private boolean bStableAIGround;
    private float arrestor;
    private boolean autoEng;
    private float curFlaps;
    private float desiredPosition;
    private boolean bFlapsOutFixed;
    private boolean bFlapsInFixed;
    private Eff3DActor antiColLight[];
    private boolean oldAntiColLight;

    public float unitsAoA;
    private static double caoa6[][] = {{ 4.80782167719908E-009D, 3.99576141267209E-009D, 5.41273413671136E-009D, 7.48344287205741E-009D }
                                      ,{ 1.93936081032443E-009D, 3.64870244307957E-009D, 5.27154966598838E-009D, 5.036772132305634E-009D }};
    private static double caoa5[][] = {{ -2.61589037982822E-006D, -2.33151730910257E-006D, -3.23180238379053E-006D, -4.591E-006D }
                                      ,{ -1.10262747258236E-006D, -2.10463221728266E-006D, -3.16153368019415E-006D, -3.15144808172798E-006D }};
    private static double caoa4[][] = {{ 0.000586227D, 0.0005614348D, 0.000797895D, 0.0011658065D }
                                      ,{ 0.0002587275D, 0.0005014314D, 0.0007835203D, 0.000816535D }};
    private static double caoa3[][] = {{ -0.069293325D, -0.0714401185D, -0.104293672D, -0.1568768777D }
                                      ,{ -0.0321065658D, -0.0632099656D, -0.1027380313D, -0.112168896D }};
    private static double caoa2[][] = {{ 4.5627552178D, 5.0717691878D, 7.6182287471D, 11.8052442765D }
                                      ,{ 2.2278825993D, 4.4536001235D, 7.5234941351D, 8.6224239708D }};
    private static double caoa1[][] = {{ -159.262272854D, -191.0108768886D, -295.457957995D, -471.7157625482 }
                                      ,{ -82.4482547981D, -166.8932406462D, -292.3519584234D, -352.2766119171D }};
    private static double caoa0[][] = {{ 2322.219702527D, 3002.684851714D, 4777.6553315341D, 7847.7906941935D }
                                      ,{ 1290.4449614683D, 2622.9919181958D, 4736.6939598851D, 6004.7605937287D }};
    private static double tmpx = 0.0D;  // "static" for saving memory
    private static double tmpx2 = 0.0D;
    private static double tmpx3 = 0.0D;
    private static double tmpx4 = 0.0D;
    private static double tmpx5 = 0.0D;
    private static double tmpx6 = 0.0D;

    private float stockCy0_0;
    private float stockCy0_1;
    private float stockCxMin_0;
    private float stockCxMin_1;
    private float stockCyCritH_0;
    private float stockGCenter;
    private float stockDefaultElevatorTrim;
    private float stockparabCxCoeff_0;
    private float stockparabCxCoeff_1;
    private float stockSqWing;
    private float stockSqWingOut;
    private float stockSqFlaps;
    private float stockDragAirbrake;
    private float SpoilerBrakeControl;
    private float oldSpoilerBrakeControl;
    private long lEnteredTaxiMode;

    // TypeFuelDump setup values
    public static float FlowRate = 10F;
    public static float FuelReserve = 1500F;

    // TypeGSuit setup values
    private static final float NEG_G_TOLERANCE_FACTOR = 3.5F;
    private static final float NEG_G_TIME_FACTOR = 2.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 7F;
    private static final float POS_G_TIME_FACTOR = 3F;
    private static final float POS_G_RECOVERY_FACTOR = 5F;

    // TypeRadarWarningReceiver setup values
    private int RWR_GENERATION = 0;
    private static final int RWR_MAX_DETECT = 16;
    private int RWR_KEEP_SECONDS = 4;
    private static final double RWR_RECEIVE_ELEVATION = 45.0D;
    private static final boolean RWR_DETECT_IRMIS = false;
    private static final boolean RWR_DETECT_ELEVATION = false;
    private boolean bRWR_Show_Text_Warning = true;

    private RadarWarningReceiverUtils rwrUtils;
    public boolean backfire;
    public boolean bRadarWarning;
    public boolean bMissileWarning;

    // TypeCountermeasure setup values
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;

    // TypeGuidedMissileCarrier and AI decision control value
    private GuidedMissileUtils guidedMissileUtils;

    // TypeLaserDesignator and laser guided weapon values
    public boolean bHasLaser;
    private Point3d laserSpotPos;
    private boolean bLaserOn;
    public long laserTimer;
    private boolean bLGBengaged;
    public boolean bHasPaveway;
    private boolean bHadLGweapons;
    private boolean bAILaserOn;
    private long lAILaserTimer;
    private long lAIGAskipTimer;
    private static float maxPavewayFOVfrom = 45.0F;
    private static double maxPavewayDistance = 20000D;
    private long lastLGBcheck;
    public boolean FLIR;
    public boolean hold;
    public boolean holdFollow;
    public Actor actorFollowing;


    private static final float gcenterScale[] = {
        0.0F, 0.0F, -0.7F, -1.4F, -2F
    };

    private static final float defaultelevtrimScale[] = {
        0.0F, 0.0F, 0.0F, -4F, -14F
    };

    private static final float cxmin0Scale[] = {
        0.032F, 0.029F, 0.026F, 0.023F, 0.021F
    };

    private static final float cy00Scale[] = {
        0.13F, 0.07F, 0.04F, 0.02F, 0.011F
    };

    private static final float cycrith0Scale[] = {
        1.7F, 1.45F, 1.2F, 1.15F, 1.1F
    };

    private static final float wingspanScale[] = {
        19.55F, 17.6F, 16.1F, 13.5F, 11.7F
    };

    private static final float wingsquredecScale[] = {
        0.0F, 0.4F, 1.0F, 2.0F, 2.8F
    };

    private static final float linecycoeffScale0007[] = {
        0.1500F, 0.1490F, 0.1480F, 0.1460F, 0.1440F, 0.1420F, 0.1400F, 0.1380F, 0.1355F, 0.1330F,
        0.1300F, 0.1220F, 0.1100F, 0.0960F, 0.0800F
    };

    static
    {
        Class class1 = com.maddox.il2.objects.air.F_14.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}