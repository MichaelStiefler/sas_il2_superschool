
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.ai.ground.NearestEnemies;
import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapExt;
import com.maddox.sas1946.il2.util.Reflection;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;


public class F_18 extends Scheme2
    implements TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeBomber, TypeStormovikArmored, TypeSupersonic, TypeFastJet, TypeLaserDesignator, TypeRadar, TypeSemiRadar, TypeGroundRadar, TypeGSuit, TypeFuelDump, TypeGuidedMissileCarrier, TypeCountermeasure, TypeRadarWarningReceiver, TypeDockable
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

    public F_18()
    {
        tflap = 0L;
        tflapSw = -1L;
        bForceTakeoffElTrim = false;
        bForceFlapmodeAuto = false;
        oldTrimElevator = 0.0F;
        elevatorsField = null;
        lLightHook = new Hook[4];
        needUpdateHook = false;
        SonicBoom = 0.0F;
        bSlatsOff = false;
        k14Mode = 2;
        k14WingspanType = 0;
        k14Distance = 200F;
        overrideBailout = false;
        ejectComplete = false;
        lTimeNextEject = 0L;
        lightTime = 0.0F;
        ft = 0.0F;
        super.bWantBeaconKeys = true;
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
        trimauto = false;
        bHasDeployedDragChute = false;
        Chute chute = null;
        removeChuteTimer = -1L;
        queen_last = null;
        queen_time = 0L;
        bNeedSetup = true;
        dtime = -1L;
        target_ = null;
        queen_ = null;
        dockport_ = -1;
        fuelReceiveRate = 11.101F;
        // max receive rate = 220gal per 1minute (Probe and Drogue system can send/receive only small amount.)
        // 220gal == 833liter == 666kg JP-5 ---> 1 sec cycle = 11.101 kg
        APmode1 = false;
        radartoggle = false;
        radarvrt = 0.0F;
        radarhol = 0.0F;
        lockmode = 0;
        radargunsight = 0;
        radarmode = 0;
        radarrange = 1;
        targetnum = 0;
        lockrange = 0.04F;
        leftscreen = 2;
        tf = 0L;
        tangate = 0.0F;
        azimult = 0.0F;
        Bingofuel = 1000;
        Nvision = false;
        bHasCenterTank = false;
        bHasWingTank = false;
        antiColLight = new Eff3DActor[6];
        oldAntiColLight = false;
        isHydraulicAlive = false;
        isGeneratorAlive = false;
        isBatteryOn = false;
        lastUpdateTime = -1L;
        arrestor = 0.0F;
        stockCy0_0 = 0.05F;
        stockCy0_1 = 0.40F;
        stockCxMin_0 = 0.021F;
        stockCxMin_1 = 0.028F;
        stockCyCritH_0 = 1.4F;
        stockSqFlaps = 5.726F;
        stockSqAileron = 4.646F;
        stockSqliftStab = 3.20F;
        stockSqElevators = 3.00F;
        guidedMissileUtils = new GuidedMissileUtils(this);
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        counterFlareList = new ArrayList();
        counterChaffList = new ArrayList();
        backfire = false;
        bHasAGM = false;
        bHasLAGM = false;
        bHasAShM = false;
        bHasUGR = false;
        lastAGMcheck = -1L;
        bHasLaser = false;
        FLIR = false;
        bLaserOn = false;
        laserSpotPos = new Point3d();
        laserTimer = -1L;
        bLGBengaged = false;
        bHasPaveway = false;
        bHadLGweapons = false;
        bAILaserOn = false;
        lAILaserTimer = -1L;
        lAIGAskipTimer = -1L;
        lastLGBcheck = -1L;
        lastAIMissileSelect = -1L;
        hold = false;
        holdFollow = false;
        actorFollowing = null;
        t1 = 0L;
        semiradartarget = null;
        groundradartarget = null;
        if(Config.cur.ini.get("Mods", "RWRTextStop", 0) > 0) bRWR_Show_Text_Warning = false;
        rwrUtils = new RadarWarningReceiverUtils(this, RWR_GENERATION, RWR_MAX_DETECT, RWR_KEEP_SECONDS, RWR_RECEIVE_ELEVATION, RWR_DETECT_IRMIS, RWR_DETECT_ELEVATION, bRWR_Show_Text_Warning);
//        rwrUtils = new RadarWarningReceiverUtils(this, RWR_GENERATION, RWR_MAX_DETECT, RWR_KEEP_SECONDS, RWR_RECEIVE_ELEVATION, RWR_DETECT_IRMIS, RWR_DETECT_ELEVATION, bRWR_Show_Text_Warning, 12, "F18- ");
        iDebugLogLevel = 0;
    }

    private static final float toMeters(float f)
    {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f)
    {
        return 0.4470401F * f;
    }

    public float checkfuel(int i)
    {
        FuelTank afueltank[] = FM.CT.getFuelTanks();

        if(afueltank.length == 0 || FM.M.bFuelTanksDropped)
            return 0.0F;

        return afueltank[i].checkFuel();
    }

    private void checkDroptanks()
    {
        FuelTank afueltank[] = FM.CT.getFuelTanks();
        if(afueltank.length == 1 || afueltank.length == 3)
            bHasCenterTank = true;
        if(afueltank.length == 2 || afueltank.length == 3)
            bHasWingTank = true;
    }

    private void checkAmmo()
    {
        counterFlareList.clear();
        counterChaffList.clear();
        bHasLaser = false;
        bHasPaveway = false;
        bHadLGweapons = false;
        bHasLAGM = false;
        bHasAGM = false;
        bHasAShM = false;
        bHasUGR = false;
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
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunAGM65B_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM65D_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM65F_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM65K_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM84E_gn16)
                            bHasAGM = true;
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunAGM65E_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM123A_gn16)
                        {
                            bHasLAGM = true;
                            bHasAGM = true;
                            bHadLGweapons = true;
                        }
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunAGM84A_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM84D_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM84J_gn16)
                            bHasAShM = true;
                        else if(FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71AP_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71WPFAC_gn16)
                            bHasUGR = true;
                    }
                    else if(FM.CT.Weapons[i][j] instanceof LAZERPOD)
                        bHasLaser = true;
            }
    }

    private void checkAIAGMrest()
    {
        bHasLAGM = false;
        bHasAGM = false;
        bHasAShM = false;
        bHasUGR = false;
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                    if(FM.CT.Weapons[i][j].haveBullets())
                    {
                        if(FM.CT.Weapons[i][j] instanceof RocketGunAGM65B_gn16 ||
                           FM.CT.Weapons[i][j] instanceof RocketGunAGM65D_gn16 ||
                           FM.CT.Weapons[i][j] instanceof RocketGunAGM65F_gn16 ||
                           FM.CT.Weapons[i][j] instanceof RocketGunAGM65K_gn16 ||
                           FM.CT.Weapons[i][j] instanceof RocketGunAGM84E_gn16)
                            bHasAGM = true;
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunAGM65E_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM123A_gn16)
                        {
                            bHasLAGM = true;
                            bHasAGM = true;
                        }
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunAGM84A_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM84D_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM84J_gn16)
                            bHasAShM = true;
                        else if(FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71AP_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71WPFAC_gn16)
                            bHasUGR = true;
                    }
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
            if(Time.current() > lastChaffDeployed + 900L)
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

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    private void checkHydraulicControls(boolean flag)
    {
        FM.CT.bHasWingControl = flag;
        FM.CT.bHasArrestorControl = flag;
        FM.CT.bHasFlapsControl = flag;
        FM.CT.bHasAileronControl = flag;
        FM.CT.bHasRudderControl = flag;
        FM.CT.bHasAirBrakeControl = flag;
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
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
            switch(radargunsight)
            {
            case 0:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: funnel");
                break;
            case 1:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Radar ranging");
                break;
            case 2:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Unguided Rocket");
                break;
            case 3:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight: Ground");
                break;
            default:
                break;
            }
        }
        if(i == 24)
        {
            leftscreen++;
            if(leftscreen > 2)
                leftscreen = 0;
            switch(leftscreen)
            {
            case 0:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: Fuel");
                break;
            case 1:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: FPAS");
                break;
            case 2:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Left screen: Engine");
                break;
            default:
                break;
            }
        }
        if(i == 25)
        {
            Bingofuel += 500;
            if(Bingofuel > 6000)
                Bingofuel = 1000;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Bingofuel " + Bingofuel);
        }
        if(i == 26 && bHasLaser)
        {
            if(hold && Time.current() > t1 + 200L && FLIR)
            {
                hold = false;
                holdFollow = false;
                actorFollowing = null;
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
        if(i == 27 && bHasLaser)
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
        if(i == 28)
            if(!ILS)
            {
                ILS = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ILS ON");
            }
            else
            {
                ILS = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "ILS OFF");
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

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
        if(FLIR)
            if(!APmode1)
            {
                APmode1 = true;
                FM.AP.setStabAltitude((float)FM.Loc.z);
                if(this == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Engaged");
            }
            else if(APmode1)
            {
                APmode1 = false;
                FM.AP.setStabAltitude(false);
                if(this == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Altitude Hold Released");
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
    }

    public void typeBomberAdjSpeedMinus()
    {
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
        else if(bSightAutomation)
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

    public boolean typeBomberToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        if(this == World.getPlayerAircraft())
        {
            switch(k14Mode)
            {
            case 0:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Bomb");
                break;
            case 1:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Gunnery");
                break;
            case 2:
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Navigation");
                break;
            default:
                break;
            }
        }
        return true;
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
            if(((actor instanceof Aircraft) || (actor instanceof ArtilleryGeneric) || (actor instanceof CarGeneric) || (actor instanceof TankGeneric)) && !(actor instanceof StationaryGeneric) && !(actor instanceof TypeLaserDesignator) && actor.pos.getAbsPoint().distance(super.pos.getAbsPoint()) < 30000D)
            {
                Point3d point3d = new Point3d();
                Orient orient = new Orient();
                actor.pos.getAbs(point3d, orient);
//                flirloc.set(point3d, orient);
                Eff3DActor eff3dactor = Eff3DActor.New(actor, null, new Loc(), 1.0F, "effects/Explodes/Air/Zenitka/Germ_88mm/Glow.eff", 1.0F);
                eff3dactor.postDestroy(Time.current() + 1500L);
                LightPointActor lightpointactor = new LightPointActor(new LightPointWorld(), new Point3d());
                lightpointactor.light.setColor(1.0F, 0.9F, 0.5F);
                if(actor instanceof Aircraft)
                    lightpointactor.light.setEmit(8F, 50F);
                else if(!(actor instanceof ArtilleryGeneric))
                    lightpointactor.light.setEmit(5F, 30F);
                else
                    lightpointactor.light.setEmit(3F, 10F);
                ((Actor) (eff3dactor)).draw.lightMap().put("light", lightpointactor);
            }
        }
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
                if(this == World.getPlayerAircraft())
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: ON");
            }
            else
            {
                if(this == World.getPlayerAircraft())
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
        if(bLGBengaged != flag && this == World.getPlayerAircraft())
        {
            if(bLGBengaged == false)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser Bomb: Engaged");
            else
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser Bomb: Disengaged");
        }

        return bLGBengaged = flag;
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
        if((bHasLAGM || bHasPaveway) && FM.AP.way.curr().Action == 3 && ((Maneuver)FM).target_ground != null && !bAILaserOn)
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


    // +++ Implementing TypeSemiRadar
    public Actor getSemiActiveRadarLockedActor()
    {
        if(getSemiActiveRadarOn())
            return semiradartarget;

        return (Actor) null;
    }

    public Actor setSemiActiveRadarLockedActor(Actor actor)
    {
        if(getSemiActiveRadarOn())
        {
            if(this.iDebugLogLevel > 2)
            {
            // debugging
                if(actor == null)
                    HUD.log("Semi-Active Radar lock-off.");
                else
                {
                    String classnameFull = actor.getClass().getName();
                    int idot = classnameFull.lastIndexOf('.');
                    int idol = classnameFull.lastIndexOf('$');
                    if(idot < idol) idot = idol;
                    String classnameSection = classnameFull.substring(idot + 1);
                    HUD.log("Semi-Active Radar lock-on " + classnameSection);
                }
            }

            semiradartarget = actor;
            return actor;
        }
        else
            semiradartarget = null;

        return (Actor) null;
    }

    public boolean getSemiActiveRadarOn()
    {
        if(radartoggle && (radarmode == 0 || radarmode == 1))
            return true;
        else
            return false;
    }

    public boolean setSemiActiveRadarOn(boolean flag)
    {
        if(flag)
        {
            radartoggle = true;
            if(radarmode == 2)
                radarmode = 0;
        }
        else
        {
            radartoggle = false;
        }

        return flag;
    }
    // --- Implementing TypeSemiRadar

    // +++ Implementing TypeGroundRadar
    public Actor getGroundRadarLockedActor()
    {
        if(getGroundRadarOn())
            return groundradartarget;

        return (Actor) null;
    }

    public Actor setGroundRadarLockedActor(Actor actor)
    {
        if(getGroundRadarOn())
        {
            if(this.iDebugLogLevel > 2)
            {
            // debugging
                if(actor == null)
                    HUD.log("Ground Radar lock-off.");
                else
                {
                    String classnameFull = actor.getClass().getName();
                    int idot = classnameFull.lastIndexOf('.');
                    int idol = classnameFull.lastIndexOf('$');
                    if(idot < idol) idot = idol;
                    String classnameSection = classnameFull.substring(idot + 1);
                    HUD.log("Ground Radar lock-on " + classnameSection);
                }
            }

            groundradartarget = actor;
            return actor;
        }
        else
            groundradartarget = null;

        return (Actor) null;
    }

    public boolean getGroundRadarOn()
    {
        if(radartoggle && radarmode == 2)
            return true;
        else
            return false;
    }

    public boolean setGroundRadarOn(boolean flag)
    {
        if(flag)
        {
            radartoggle = true;
            if(radarmode != 2)
                radarmode = 2;
        }
        else
        {
            radartoggle = false;
        }

        return flag;
    }
    // --- Implementing TypeGroundRadar


    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.AS.wantBeaconsNet(true);
        guidedMissileUtils.onAircraftLoaded();
        FM.CT.bHasDragChuteControl = true;
        FM.Sq.dragChuteCx = 6F;
        bHasDeployedDragChute = false;
        FM.turret[0].bIsAIControlled = false;
        FM.CT.bHasBombSelect = true;
        FM.CT.bHasAntiColLights = true;
        FM.CT.bHasFormationLights = true;
        t1 = Time.current();
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        stockCy0_0 = polares.Cy0_0;
        stockCy0_1 = polares.Cy0_1;
        stockCxMin_0 = polares.CxMin_0;
        stockCxMin_1 = polares.CxMin_1;
        stockCyCritH_0 = polares.CyCritH_0;
        stockSqFlaps = FM.Sq.squareFlaps;
        stockSqAileron = FM.Sq.squareAilerons;
        stockSqliftStab = FM.Sq.liftStab;
        stockSqElevators = FM.Sq.squareElevators;
//        if(FM instanceof RealFlightModel)
//            Reflection.invokeMethod(FM, "init_G_Limits");
//        if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))
//        {
//            FM.Sq.squareElevators += 2.0F;
//            FM.Sq.liftStab += 2.0F;
//        }
        rwrUtils.onAircraftLoaded();
        rwrUtils.setLockTone("aircraft.usRWRScan", "aircraft.usRWRLock", "aircraft.usRWRLaunchWarningMissileMissile", "aircraft.usRWRThreatNew");
    }

    public void missionStarting()
    {
        super.missionStarting();
        checkAsDrone();
        checkDroptanks();
        checkAmmo();
        FM.CT.toggleRocketHook();

        laserTimer = -1L;
        bLaserOn = false;
        FLIR = false;
        isBatteryOn = true;
        iDebugLogLevel = Config.cur.ini.get("Mods", "GuidedMissileDebugLog", 0);

        for(int i = 0; i < 2; i++)
        {
            oldthrl[i] = -1.0F;
            curthrl[i] = -1.0F;
            engineSurgeDamage[i] = 0.0F;
        }
    }

    private void checkAsDrone()
    {
        if(target_ == null)
        {
            if(FM.AP.way.curr().getTarget() == null)
                FM.AP.way.next();
            target_ = FM.AP.way.curr().getTarget();
            if(Actor.isValid(target_) && (target_ instanceof Wing))
            {
                Wing wing = (Wing)target_;
                int i = aircIndex();
                if(Actor.isValid(wing.airc[i / 2]))
                    target_ = wing.airc[i / 2];
                else
                    target_ = null;
            }
        }
        if(Actor.isValid(target_) && (target_ instanceof TypeTankerDrogue))
        {
            queen_last = target_;
            queen_time = Time.current();
            if(isNetMaster())
                ((TypeDockable)target_).typeDockableRequestAttach(this, aircIndex() % 2, true);
        }
        bNeedSetup = false;
        target_ = null;
    }

    public int typeDockableGetDockport()
    {
        if(typeDockableIsDocked())
            return dockport_;
        else
            return -1;
    }

    public Actor typeDockableGetQueen()
    {
        return queen_;
    }

    public boolean typeDockableIsDocked()
    {
        return Actor.isValid(queen_);
    }

    public void typeDockableAttemptAttach()
    {
        if(FM.AS.isMaster() && !typeDockableIsDocked())
        {
            Aircraft aircraft = War.getNearestFriend(this);
            if((aircraft instanceof TypeTankerDrogue) && FM.CT.getRefuel() > 0.95F)
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach()
    {
        if(FM.AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
            ((TypeDockable)queen_).typeDockableRequestDetach(this);
    }

    public void typeDockableRequestAttach(Actor actor)
    {
    }

    public void typeDockableRequestDetach(Actor actor)
    {
    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag)
    {
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag)
    {
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i)
    {
    }

    public void typeDockableDoDetachFromDrone(int i)
    {
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i)
    {
        queen_ = actor;
        dockport_ = i;
        queen_last = queen_;
        queen_time = 0L;
        FM.EI.setEngineRunning();
        FM.CT.setGearAirborne();
        moveGear(0.0F, 0.0F, 0.0F);
        FlightModel flightmodel = ((SndAircraft) ((Aircraft)queen_)).FM;
        if(aircIndex() == 0 && (super.FM instanceof Maneuver) && (flightmodel instanceof Maneuver))
        {
            Maneuver maneuver = (Maneuver)flightmodel;
            Maneuver maneuver1 = (Maneuver)super.FM;
            if(maneuver.Group != null && maneuver1.Group != null && maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1)
            {
                AirGroup airgroup = new AirGroup(maneuver1.Group);
                maneuver1.Group.delAircraft(this);
                airgroup.addAircraft(this);
                airgroup.attachGroup(maneuver.Group);
                airgroup.rejoinGroup = null;
                airgroup.leaderGroup = null;
                airgroup.clientGroup = maneuver.Group;
            }
        }
    }

    public void typeDockableDoDetachFromQueen(int i)
    {
        if(dockport_ == i)
        {
            queen_last = queen_;
            queen_time = Time.current();
            queen_ = null;
            dockport_ = 0;
        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        if(typeDockableIsDocked())
        {
            netmsgguaranted.writeByte(1);
            ActorNet actornet = null;
            if(Actor.isValid(queen_))
            {
                actornet = queen_.net;
                if(actornet.countNoMirrors() > 0)
                    actornet = null;
            }
            netmsgguaranted.writeByte(dockport_);
            netmsgguaranted.writeNetObj(actornet);
        }
        else
        {
            netmsgguaranted.writeByte(0);
        }
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        if(netmsginput.readByte() == 1)
        {
            dockport_ = netmsginput.readByte();
            NetObj netobj = netmsginput.readNetObj();
            if(netobj != null)
            {
                Actor actor = (Actor)netobj.superObj();
                ((TypeDockable)actor).typeDockableDoAttachToDrone(this, dockport_);
            }
        }
    }

    private void receivingRefuel(float f)
    {
        int i = aircIndex();

        if(typeDockableIsDocked())
        {
            if(FM.CT.getRefuel() < 0.9F)
            {
                typeDockableAttemptDetach();
                return;
            }
            else
            {
                if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
                {
                    ((Maneuver)super.FM).unblock();
                    ((Maneuver)super.FM).set_maneuver(48);
                    for(int j = 0; j < i; j++)
                        ((Maneuver)super.FM).push(48);

                    if(FM.AP.way.curr().Action != 3)
                        ((FlightModelMain) ((Maneuver)super.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).AP.way.Cur());
                    ((Pilot)super.FM).setDumbTime(3000L);
                }
                FuelTank fuelTanks[];
                fuelTanks = FM.CT.getFuelTanks();
                if(FM.M.fuel < FM.M.maxFuel - fuelReceiveRate - 1.1F)
                {
                    float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, fuelReceiveRate, f);
                    FM.M.fuel += getFuel;
                }
                else if(fuelTanks.length > 0 && fuelTanks[0] != null && !FM.M.bFuelTanksDropped)
                {
                    float freeTankSum = 0F;
                    for(int num = 0; num < fuelTanks.length; num++)
                        freeTankSum += fuelTanks[num].checkFreeTankSpace();
                    if(freeTankSum < fuelReceiveRate + 1.1F)
                    {
                        typeDockableAttemptDetach();
                        return;
                    }
                    float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, fuelReceiveRate, f);
                    for(int num = 0; num < fuelTanks.length; num++)
                        fuelTanks[num].doRefuel(getFuel * (fuelTanks[num].checkFreeTankSpace() / freeTankSum));
                }
                else
                {
                    typeDockableAttemptDetach();
                    return;
                }
            }
        }
        else if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
        {
            if(FM.CT.GearControl == 0.0F && FM.EI.engines[0].getStage() == 0)
                FM.EI.setEngineRunning();
            if(dtime > 0L && ((Maneuver)super.FM).Group != null)
            {
                ((Maneuver)super.FM).Group.leaderGroup = null;
                ((Maneuver)super.FM).set_maneuver(22);
                ((Pilot)super.FM).setDumbTime(3000L);
                if(Time.current() > dtime + 3000L)
                {
                    dtime = -1L;
                    ((Maneuver)super.FM).clear_stack();
                    ((Maneuver)super.FM).set_maneuver(0);
                    ((Pilot)super.FM).setDumbTime(0L);
                }
            }
            else if(FM.AP.way.curr().Action == 0)
            {
                Maneuver maneuver = (Maneuver)super.FM;
                if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                    maneuver.Group.setGroupTask(2);
            }
        }
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        super.msgCollisionRequest(actor, aflag);
        if(queen_last != null && queen_last == actor && (queen_time == 0L || Time.current() < queen_time + 5000L))
            aflag[0] = false;
        else
            aflag[0] = true;
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
                    Engine.land();
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
                }
                else if(lLight[j].getR() != 0.0F)
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
        if(FM.EI.engines[0].getRPM() > 200F || FM.EI.engines[1].getRPM() > 200F || FM.getSpeedKMH() > 160F)
        {
            isGeneratorAlive = true;
            isHydraulicAlive = true;
        }
        else
        {
            isGeneratorAlive = false;
            isHydraulicAlive = false;
        }
        checkHydraulicControls(isHydraulicAlive);
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
        if((FM.Gears.nearGround() || FM.Gears.onGround()) && FM.CT.getCockpitDoor() == 1.0F)
        {
            hierMesh().chunkVisible("HMask1_D0", false);
            if(FM.crew > 1)
                hierMesh().chunkVisible("HMask2_D0", false);
        }
        else
        {
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
            if(FM.crew > 1)
                hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
        }
        if(bHasLaser && this == World.getPlayerAircraft())
        {
            if(FLIR)
                FLIR();
        }

        if((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver))
        {
            if(FM.AP.way.curr().Action == 3)
            {
                Actor tempActor = null;
                NearestEnemies.set(WeaponsMask(), 25F, 2000F, 20F, 20000F, false, true, (String)null);
                tempActor = NearestEnemies.getAFoundFlyingPlane(this.pos.getAbsPoint(), 30000D, this.getArmy(), 1F);
                if(tempActor == null && FM.AP.way.curr().getTarget() != null && Time.current() > lastAIMissileSelect + 8000L)
                {
                    if(bHasAShM && (FM.AP.way.curr().getTarget() instanceof TgtShip) &&
                       FM.CT.rocketNameSelected != "AGM-84A" && FM.CT.rocketNameSelected != "AGM-84D" && FM.CT.rocketNameSelected != "AGM-84J")
                    {
                        for(int i = 0; i < 4; i++)
                        {
                            if(FM.CT.rocketNameSelected == "AGM-84A" || FM.CT.rocketNameSelected == "AGM-84D" || FM.CT.rocketNameSelected == "AGM-84J")
                                break;
                            FM.CT.toggleRocketHook();
                        }
                    }
                    if((!bHasAShM || !(FM.AP.way.curr().getTarget() instanceof TgtShip)) && bHasAGM &&
                       FM.CT.rocketNameSelected != "AGM-65B" && FM.CT.rocketNameSelected != "AGM-65D" && FM.CT.rocketNameSelected != "AGM-65E" && FM.CT.rocketNameSelected != "AGM-65F" && FM.CT.rocketNameSelected != "AGM-65K" && FM.CT.rocketNameSelected != "AGM-123" && FM.CT.rocketNameSelected != "AGM-84E")
                    {
                        for(int i = 0; i < 4; i++)
                        {
                            if(FM.CT.rocketNameSelected == "AGM-65B" || FM.CT.rocketNameSelected == "AGM-65D" || FM.CT.rocketNameSelected == "AGM-65E" || FM.CT.rocketNameSelected == "AGM-65F" || FM.CT.rocketNameSelected == "AGM-65K" || FM.CT.rocketNameSelected == "AGM-123" || FM.CT.rocketNameSelected == "AGM-84E")
                                break;
                            FM.CT.toggleRocketHook();
                        }
                    }
                    if(!bHasAGM && bHasUGR && !bHasAShM && FM.CT.rocketNameSelected != "Zuni" && FM.CT.rocketNameSelected != "ZuniFAC" && FM.CT.rocketNameSelected != "ZuniFO" &&
                       FM.CT.rocketNameSelected != "Hydra" && FM.CT.rocketNameSelected != "HydraFAC" && FM.CT.rocketNameSelected != "HydraFO")
                    {
                        for(int i = 0; i < 4; i++)
                        {
                            if(FM.CT.rocketNameSelected == "Zuni" || FM.CT.rocketNameSelected == "ZuniFAC" || FM.CT.rocketNameSelected == "ZuniFO" ||
                               FM.CT.rocketNameSelected == "Hydra" || FM.CT.rocketNameSelected == "HydraFAC" || FM.CT.rocketNameSelected == "HydraFO")
                                break;
                            FM.CT.toggleRocketHook();
                        }
                    }
                    lastAIMissileSelect = Time.current();
                }
                if((!((Maneuver)super.FM).hasBombs() && !bHasAGM && !bHasAShM && !bHasUGR) ||
                   (!bHasAGM && !bHasUGR && bHasAShM && (FM.AP.way.curr().getTarget() == null || !(FM.AP.way.curr().getTarget() instanceof TgtShip))))
                {
                    if((lAIGAskipTimer > 0L && Time.current() > lAIGAskipTimer) || !bHadLGweapons || !bHasLaser)
                    {
                        FM.AP.way.next();
                        FM.bSkipGroundAttack = true;
                        ((Maneuver)FM).target_ground = null;
                        ((Maneuver)FM).set_maneuver(0);
                        FM.CT.toggleRocketHook();
                    }
                    else if(lAIGAskipTimer == -1L)
                        lAIGAskipTimer = Time.current() + 20000L + (long)Math.min(FM.getAltitude(), 10000F) * 5L;
                }

                if((bHasAGM || bHasAShM || bHasUGR) && Time.current() > lastAGMcheck + 30000L)
                {
                    checkAIAGMrest();
                    lastAGMcheck = Time.current();
                }
            }

            if(isGeneratorAlive)
            {
                if((((Maneuver)FM).get_maneuver() == 21 || ((Maneuver)FM).get_maneuver() == 25 || ((Maneuver)FM).get_maneuver() == 2 || ((Maneuver)FM).get_maneuver() == 84)
                   && FM.AP.way.isLanding() && (FM.Gears.nOfGearsOnGr < 3 || FM.getSpeedKMH() > 80F))
                    FM.CT.FlapsControlSwitch = 2;
                else if(((Maneuver)FM).get_maneuver() == 26)
                    FM.CT.FlapsControlSwitch = 1;
                else
                    FM.CT.FlapsControlSwitch = 0;
            }

            FM.CT.bAntiColLights = FM.AS.bNavLightsOn;
        }

        if((bHasPaveway || FM.bNoDiveBombing) && Time.current() > lastLGBcheck + 30000L && (FM instanceof Maneuver))
        {
            checkGuidedBombRest();
            lastLGBcheck = Time.current();
        }

        formationlights();
        anticollights();
        if(!FM.isPlayers())
            if(((Maneuver)FM).get_maneuver() == 25 || ((Maneuver)FM).get_maneuver() == 26
               || ((Maneuver)FM).get_maneuver() == 64 || ((Maneuver)FM).get_maneuver() == 66 || ((Maneuver)FM).get_maneuver() == 102)
            {
                // LANDING, TAKEOFF, PARKED_STARTUP, TAXI, TAXI_TO_TO
                radartoggle = false;
            }
            else
            {
                radartoggle = true;
                radarmode = 0;
            }
    }

    private final void UpdateLightIntensity()
    {
        if(World.getTimeofDay() >= 6F && World.getTimeofDay() < 7F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 6F, 7F, 1.0F, 0.1F);
        else if(World.getTimeofDay() >= 18F && World.getTimeofDay() < 19F)
            lightTime = Aircraft.cvt(World.getTimeofDay(), 18F, 19F, 0.1F, 1.0F);
        else if(World.getTimeofDay() >= 7F && World.getTimeofDay() < 18F)
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
            k14Distance = (float)(FM.actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint()));
            if(k14Distance > 1500F)
                k14Distance = 1500F;
            else if(k14Distance < 20F)
                k14Distance = 20F;
        }
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
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        case 1: // '\1'
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
            }
            else if(s.startsWith("xxeng1"))
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
            else if(s.startsWith("xxeng2"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[1].getCylindersRatio() * 20F)
                {
                    FM.EI.engines[1].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + FM.EI.engines[1].getCylindersOperable() + "/" + FM.EI.engines[1].getCylinders() + " Left..");
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
            else if(s.startsWith("xxmgun0"))
            {
                int j = s.charAt(7) - 49;
                if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                {
                    debuggunnery("Armament: mnine Gun (" + j + ") Disabled..");
                    FM.AS.setJamBullets(0, j);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            }
            else if(s.startsWith("xxtank"))
            {
                int k = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(FM.AS.astateTankStates[k] == 0)
                    {
                        debuggunnery("Fuel Tank (" + k + "): Pierced..");
                        FM.AS.hitTank(shot.initiator, k, 1);
                        FM.AS.doSetTankState(shot.initiator, k, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        FM.AS.hitTank(shot.initiator, k, 2);
                        debuggunnery("Fuel Tank (" + k + "): Hit..");
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
            {
                hitChunk("CF", shot);
                hitChunk("CF2", shot);
            }
            else if(s.startsWith("xnose"))
                hitChunk("Nose1", shot);
            else if(s.startsWith("xBody"))
                hitChunk("Body", shot);
            else if(s.startsWith("xtail"))
            {
                if(chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", shot);
            }
            else if(s.startsWith("xkeel"))
            {
                if(chunkDamageVisible("Keel1") < 2)
                    hitChunk("Keel1", shot);
            }
            else if(s.startsWith("xrudderl"))
                hitChunk("RudderL", shot);
            else if(s.startsWith("xrudderr"))
                hitChunk("RudderR", shot);
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
                if(s.startsWith("xflap1"))
                    hitChunk("Flap1", shot);
                if(s.startsWith("xflap2"))
                    hitChunk("Flap2", shot);
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
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        default:
            break;

        case 13: // '\r'
            FM.Gears.cgear = false;
            float f = World.Rnd().nextFloat(0.0F, 1.0F);
            if(f < 0.1F)
            {
                if(World.Rnd().nextFloat(0.0F, 1.0F) < 0.5F)
                {
                    FM.AS.hitEngine(this, 0, 100);
                    FM.EI.engines[0].setEngineDies(actor);
                }
                else
                {
                    FM.AS.hitEngine(this, 1, 100);
                    FM.EI.engines[1].setEngineDies(actor);
                }
            }
            else if(f > 0.55F)
                FM.EI.engines[0].setEngineDies(actor);
            else
                FM.EI.engines[1].setEngineDies(actor);
            break;

        case 19: // '\023'
            FM.EI.engines[0].setEngineDies(actor);
            FM.EI.engines[1].setEngineDies(actor);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void moveCockpitDoor(float f)
    {
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 20F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public static void moveGear(HierMesh hiermesh, float fgl, float fgr, float fgc)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, Aircraft.cvt(fgc, 0.275F, 0.75F, 0.0F, -95F));
        hiermesh.chunkSetAngles("GearC24_D0", 0.0F, 0.0F, Aircraft.cvt(fgc, 0.275F, 0.75F, 0.0F, -76F));
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(fgc, 0.01F, 0.275F, 0.0F, -75F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(fgc, 0.01F, 0.275F, 0.0F, 75F), 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, Aircraft.cvt(fgc, 0.01F, 0.275F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearC9_D0", 0.0F, 0.0F, Aircraft.cvt(fgc, 0.275F, 0.75F, 0.0F, -33.4F));
        hiermesh.chunkSetAngles("GearR252_D0", 0.0F, Aircraft.cvt(fgr, 0.275F, 0.325F, 0.0F, 20F), 0.0F);
        hiermesh.chunkSetAngles("GearL252_D0", 0.0F, Aircraft.cvt(fgl, 0.275F, 0.325F, 0.0F, 20F), 0.0F);
        hiermesh.chunkSetAngles("GearR25_D0", Aircraft.cvt(fgr, 0.275F, 0.75F, 0.0F, -93.84F), Aircraft.cvt(fgr, 0.275F, 0.75F, 0.0F, -19.1F), Aircraft.cvt(fgr, 0.75F, 0.875F, 0.0F, 35F));
        hiermesh.chunkSetAngles("GearL25_D0", Aircraft.cvt(fgl, 0.275F, 0.75F, 0.0F, -93.84F), Aircraft.cvt(fgl, 0.275F, 0.75F, 0.0F, -19.1F), Aircraft.cvt(fgl, 0.75F, 0.875F, 0.0F, 35F));
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, Aircraft.cvt(fgr, 0.75F, 0.875F, 0.0F, -20.45F));
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, Aircraft.cvt(fgl, 0.75F, 0.875F, 0.0F, -20.45F));
        hiermesh.chunkSetAngles("GearR221_D0", 0.0F, 0.0F, Aircraft.cvt(fgr, 0.75F, 0.875F, 0.0F, 39.06F));
        hiermesh.chunkSetAngles("GearL221_D0", 0.0F, 0.0F, Aircraft.cvt(fgl, 0.75F, 0.875F, 0.0F, 39.06F));
        hiermesh.chunkSetAngles("GearR32_D0", Aircraft.cvt(fgr, 0.75F, 0.99F, 0.0F, 43.53F), 0.0F, Aircraft.cvt(fgr, 0.75F, 0.99F, 0.0F, -17.53F));
        hiermesh.chunkSetAngles("GearL32_D0", Aircraft.cvt(fgl, 0.75F, 0.99F, 0.0F, 43.53F), 0.0F, Aircraft.cvt(fgl, 0.75F, 0.99F, 0.0F, -18.53F));
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(fgl, 0.01F, 0.275F, 0.0F, -55F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(fgr, 0.01F, 0.275F, 0.0F, 55F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(fgl, 0.01F, 0.275F, 0.0F, -92F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(fgr, 0.01F, 0.275F, 0.0F, 92F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(fgl, 0.01F, 0.275F, 0.0F, 100F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(fgr, 0.01F, 0.275F, 0.0F, -100F), 0.0F);
    }

    protected void moveGear(float fgl, float fgr, float fgc)
    {
        moveGear(hierMesh(), fgl, fgr, fgc);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(fgc, 0.275F, 0.75F, 0.0F, 0.73F);
        hierMesh().chunkSetLocate("GearC25_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(fgc, 0.3F, 0.99F, 0.0F, -0.39F);
        hierMesh().chunkSetLocate("GearC3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        float f = FM.Gears.gWheelSinking[2];
        xyz[1] = Aircraft.cvt(2.9F * f, 0.0F, 0.4F, 0.0F, 0.39F);
        hierMesh().chunkSetLocate("GearC32_D0", xyz, ypr);
        resetYPRmodifier();
        xyz[1] = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, 0.39F);
        hierMesh().chunkSetLocate("GearC31_D0", xyz, ypr);
        resetYPRmodifier();
        f = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.5F, 0.0F, -0.5F);
        xyz[2] = f;
        hierMesh().chunkSetLocate("GearR31_D0", xyz, ypr);
        resetYPRmodifier();
        f = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.5F, 0.0F, 0.5F);
        xyz[2] = f;
        hierMesh().chunkSetLocate("GearL31_D0", xyz, ypr);
        f = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.4F, 0.0F, 1.0F);
        hierMesh().chunkSetAngles("GearR21_D0", 0.0F, 0.0F, 55.5F * f);
        hierMesh().chunkSetAngles("GearR211_D0", 0.0F, 0.0F, 17.6F * f);
        hierMesh().chunkSetAngles("GearR212_D0", 0.0F, 0.0F, 17.51F * f);
        hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 14.51F * f);
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.4F, 0.0F, -0.18F);
        hierMesh().chunkSetLocate("GearR213_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.4F, 0.0F, -0.17F);
        hierMesh().chunkSetLocate("GearR214_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        f = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.4F, 0.0F, 1.0F);
        hierMesh().chunkSetAngles("GearL21_D0", 0.0F, 0.0F, 55.5F * f);
        hierMesh().chunkSetAngles("GearL211_D0", 0.0F, 0.0F, 17.6F * f);
        hierMesh().chunkSetAngles("GearL212_D0", 0.0F, 0.0F, 17.51F * f);
        hierMesh().chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 14.51F * f);
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.4F, 0.0F, 0.18F);
        hierMesh().chunkSetLocate("GearL213_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.4F, 0.0F, 0.17F);
        hierMesh().chunkSetLocate("GearL214_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f)
    {
        updateControlsVisuals2();
    }

    public void moveSteering(float f)
    {
        if(FM.CT.getGear() > 0.8F)
            hierMesh().chunkSetAngles("GearC33_D0", 0.0F, 1.2F * f, 0.0F);
        else
            hierMesh().chunkSetAngles("GearC33_D0", 0.0F, 0.0F, 0.0F);
    }

    private final void updateControlsVisuals()
    {
        if(FM.getSpeedKMH() > 590F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * FM.CT.getElevator() + 27F * FM.CT.getAileron(), 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * FM.CT.getElevator() - 27F * FM.CT.getAileron(), 0.0F);
        }
        else
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -17F * FM.CT.getElevator() + 10F * FM.CT.getAileron(), 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -17F * FM.CT.getElevator() - 10F * FM.CT.getAileron(), 0.0F);
        }
    }

    private final void updateControlsVisuals2()
    {
        if(FM.getSpeedKMH() > 590F)
        {
            hierMesh().chunkSetAngles("RudderL_D0", 35F * FM.CT.getRudder() - 35F * FM.CT.getAileron(), 0.0F, 0.0F);
            hierMesh().chunkSetAngles("RudderR_D0", 35F * FM.CT.getRudder() - 35F * FM.CT.getAileron(), 0.0F, 0.0F);
        }
        else
        {
            hierMesh().chunkSetAngles("RudderL_D0", 25F * FM.CT.getRudder() - 20F * FM.CT.getAileron(), 0.0F, 0.0F);
            hierMesh().chunkSetAngles("RudderR_D0", 25F * FM.CT.getRudder() - 20F * FM.CT.getAileron(), 0.0F, 0.0F);
        }
    }

    protected void moveElevator(float f)
    {
        updateControlsVisuals();
    }

    protected void moveAileron(float f)
    {
        updateControlsVisuals();
        updateControlsVisuals2();
        if(FM.getSpeedKMH() > 590F)
        {
            hierMesh().chunkSetAngles("AroneL2_D0", 0.0F, 40F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneR2_D0", 0.0F, -40F * f, 0.0F);
        }
        else
        {
            hierMesh().chunkSetAngles("AroneL2_D0", 0.0F, 20F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneR2_D0", 0.0F, -20F * f, 0.0F);
        }
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap1_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 45F), 0.0F);
        hierMesh().chunkSetAngles("Flap2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 45F), 0.0F);
        if(FM.CT.getWing() < 0.01F)
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, Aircraft.cvt(f, 0.25F, 0.29F, 0.0F, 15F), 0.0F);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, Aircraft.cvt(f, 0.25F, 0.29F, 0.0F, 15F), 0.0F);
            hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, Aircraft.cvt(f, 0.29F, 0.99F, 0.0F, 30F), 0.0F);
            hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, Aircraft.cvt(f, 0.29F, 0.99F, 0.0F, 30F), 0.0F);
        }
        else
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, 0.0F, 0.0F);
            hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, 0.0F, 0.0F);
        }
    }

    private void updateSlatsVisual()
    {
        float fSlatDegree = 0.0F;
        if(FM.Gears.onGround())
        {
            fSlatDegree = Aircraft.cvt(FM.CT.getFlap(), 0.02F, 0.12F, 0.0F, 14.5F);
        }
        else
        {
            if(FM.getSpeed() > 20F)
                fSlatDegree = Aircraft.cvt(FM.getAOA(), 6.8F, 15F, 0.0F, 30.5F);
            else if(FM.getSpeed() > 10F)
                fSlatDegree = Aircraft.cvt(FM.getAOA(), 6.8F, 15F, 0.0F, 20.5F);
            else if(FM.getSpeed() > 5F)
                fSlatDegree = Aircraft.cvt(FM.getAOA(), 6.8F, 15F, 0.0F, 14.5F);

            if(FM.CT.FlapsControlSwitch > 0 && !bForceFlapmodeAuto && fSlatDegree < 14.5F)
                fSlatDegree = Math.max(fSlatDegree, Aircraft.cvt(FM.CT.getFlap(), 0.02F, 0.12F, 0.0F, 14.5F));
        }
        hierMesh().chunkSetAngles("SlatLIn_D0", 0.0F, -fSlatDegree, 0.0F);
        hierMesh().chunkSetAngles("SlatRIn_D0", 0.0F, -fSlatDegree, 0.0F);
        hierMesh().chunkSetAngles("SlatLOut_D0", 0.0F, -fSlatDegree, 0.0F);
        hierMesh().chunkSetAngles("SlatROut_D0", 0.0F, -fSlatDegree, 0.0F);

        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        polares.Cy0_0 = stockCy0_0 + 0.0036F * fSlatDegree;
        polares.CxMin_0 = stockCxMin_0 + 5.256E-4F * fSlatDegree;
    }

    private void updateDragChute()
    {
        if(FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteF18/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(0.8F);
            ((Actor) (chute)).pos.setRel(new Point3d(-8D, 0.0D, 0.5D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        }
        else if(bHasDeployedDragChute && FM.CT.bHasDragChuteControl)
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
            }
            else if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() < 20F)
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

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("Airbrake_D0", 0.0F, -60F * f, 0.0F);
        hierMesh().chunkSetAngles("Airbrake3_D0", 0.0F, 80F * f, 0.0F);
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -5.5F);
        hierMesh().chunkSetLocate("Airbrake2_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Tailhook_D0", 0.0F, 0.0F, 70F * f);
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 92F), 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -92F), 0.0F);
        moveFlap(FM.CT.getFlap());
    }

    public void moveRefuel(float f)
    {
        hierMesh().chunkSetAngles("fueldoor_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, 35F), 0.0F);
        hierMesh().chunkSetAngles("rod2", 0.0F, Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -85F), 0.0F);
        if(f < 0.5F)
            hierMesh().chunkSetAngles("fueldoor2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.18F, 0.0F, 90F));
        else
            hierMesh().chunkSetAngles("fueldoor2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.82F, 1.0F, 90F, 0.0F));
    }

    protected void moveCatLaunchBar(float f)
    {
        hierMesh().chunkSetAngles("GearC24_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, -76F, -121F));
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
            if(this == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
            if(Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(FM.getAltitude()))
                shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            SonicBoom = 1.0F;
        }
        if(calculateMach() > 1.01F || calculateMach() < 1.0F)
            Eff3DActor.finish(shockwave);
    }

    private void engineSurge(float f)
    {
        if(FM.AS.isMaster())
        {
            for(int i = 0; i < 2; i++)
            {
                if(curthrl[i] == -1F)
                {
                    curthrl[i] = oldthrl[i] = FM.EI.engines[i].getControlThrottle();
                    continue;
                }
                curthrl[i] = FM.EI.engines[i].getControlThrottle();
                if(curthrl[i] < 1.05F)
                {
                    if((curthrl[i] - oldthrl[i]) / f > 35F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                    {
                        if(this == World.getPlayerAircraft())
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
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
                            if(this == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
                            FM.EI.engines[i].setEngineStops(this);
                        }
                        else if(this == World.getPlayerAircraft())
                            HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
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

    public void update(float f)
    {
        if(FM.CT.getFlap() < FM.CT.FlapsControl)
            FM.CT.forceFlaps(flapsMovement(f, FM.CT.FlapsControl, FM.CT.getFlap(), 999F, Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 500F, 0.5F, 0.08F)));
        else
            FM.CT.forceFlaps(flapsMovement(f, FM.CT.FlapsControl, FM.CT.getFlap(), 999F, Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 500F, 0.5F, 0.7F)));
        if(lastUpdateTime != Time.current())
        {
            if(bNeedSetup)
                checkAsDrone();
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
                    if(FM.EI.engines[en].getPowerOutput() > 1.001F && FM.EI.engines[en].getStage() == 6)
                    {
                        if(World.getTimeofDay() >= 18F || World.getTimeofDay() <= 6F)
                            FM.AS.setSootState(this, en, 5);
                        else
                            FM.AS.setSootState(this, en, 3);
                    }
                    else
                    {
                        FM.AS.setSootState(this, en, 0);
                    }
                    setExhaustFlame(Math.round(Aircraft.cvt(FM.EI.engines[en].getThrustOutput(), 0.7F, 0.87F, 0.0F, 12F)), en);
                }
            }
            if(bHasLaser && this == World.getPlayerAircraft())
            {
                if(FLIR)
                    laser(getLaserSpot());
                updatecontrollaser();
            }
            if(bHasLaser && this != World.getPlayerAircraft() && (FM instanceof Maneuver))
                AILaserControl();
            typeFighterAceMakerRangeFinder();
            soundbarier();
            rwrUtils.update();
            backfire = rwrUtils.getBackfire();
            bRadarWarning = rwrUtils.getRadarLockedWarning();
            bMissileWarning = rwrUtils.getMissileWarning();
            if(backfire)
                backFire();
            if(FM.crew > 1 && obsMove < obsMoveTot && !bObserverKilled && !FM.AS.isPilotParatrooper(1))
            {
                if(obsMove < 0.2F || obsMove > obsMoveTot - 0.2F)
                    obsMove += 0.3F * f;
                else
                if(obsMove < 0.1F || obsMove > obsMoveTot - 0.1F)
                    obsMove += 0.15F;
                else
                    obsMove += 1.2F * f;
                obsLookAzimuth = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsAzimuthOld, obsAzimuth);
                obsLookElevation = Aircraft.cvt(obsMove, 0.0F, obsMoveTot, obsElevationOld, obsElevation);
                hierMesh().chunkSetAngles("Head2_D0", 0.0F, obsLookAzimuth, obsLookElevation);
            }
            gearlimit();
            if(needUpdateHook)
            {
                updateHook();
                needUpdateHook = false;
            }
        }

        engineSurge(f);
        if(FM.CT.getArrestor() > 0.2F)
            calculateArrestor();
        if(super.FM instanceof Maneuver)
            receivingRefuel(f);

        super.update(f);

        if(lastUpdateTime != Time.current())
        {
            for(int i = 1; i < 33; i++)
            {
                fNozzleOpenL = FM.EI.engines[0].getPowerOutput() > 0.92F ? cvt(FM.EI.engines[0].getPowerOutput(), 0.92F, 1.1F, 0.0F, -9F) : cvt(FM.EI.engines[0].getPowerOutput(), 0.0F, 0.92F, -9F, 0.0F);
                hierMesh().chunkSetAngles("Eflap" + i, fNozzleOpenL, 0.0F, 0.0F);
            }

            for(int j = 33; j > 32 && j < 65; j++)
            {
                fNozzleOpenR = FM.EI.engines[1].getPowerOutput() > 0.92F ? cvt(FM.EI.engines[1].getPowerOutput(), 0.92F, 1.1F, 0.0F, -9F) : cvt(FM.EI.engines[1].getPowerOutput(), 0.0F, 0.92F, -9F, 0.0F);
                hierMesh().chunkSetAngles("Eflap" + j, fNozzleOpenR, 0.0F, 0.0F);
            }

            float f1 = cvt(FM.getSpeedKMH(), 500F, 1000F, 0.999F, 0.601F);
            if(FM.getSpeed() > 7F && World.Rnd().nextFloat() < getAirDensityFactor(FM.getAltitude()))
            {
                if(FM.getOverload() > 5.7F)
                {
                    pull01 = Eff3DActor.New(this, findHook("_Pull01"), null, f1, "3DO/Effects/Aircraft/Pullingvapor.eff", -1F);
                    pull02 = Eff3DActor.New(this, findHook("_Pull02"), null, f1, "3DO/Effects/Aircraft/Pullingvapor.eff", -1F);
                }
                else
                {
                    Eff3DActor.finish(pull01);
                    Eff3DActor.finish(pull02);
                }
            }
            if(FM.getSpeedKMH() > 300F)
                FM.CT.cockpitDoorControl = 0.0F;
            updateSlatsVisual();
            FlapAssistTakeoff();
            updateDragChute();
            computeLift();
            lastUpdateTime = Time.current();
        }
        computeFlightmodel();
        computeElevators();
        computeSupersonicLimiter();
    }

    private void computeFlightmodel()
    {
        if(FM.CT.FlapsControlSwitch == 0 || FM.CT.FlapsControlSwitch == 2)
            tflapSw = -1L;
        else
        {
            if(tflapSw == -1L)
                tflapSw = Time.current();
        }
        float f = (cvt(FM.getAOA(), 0.0F, 5F, 0.0F, 1.0F) * FM.getAOA()) * 0.025F * cvt(FM.getSpeedKMH(), 0.0F, 500F, 0.0F, 1.0F);
        if(FM.getSpeedKMH() > 465F || FM.CT.FlapsControlSwitch == 0)
        {
            if(FM.CT.FlapsControlSwitch > 0)
                bForceFlapmodeAuto = true;
            else
                bForceFlapmodeAuto = false;
            FM.CT.FlapsControl = cvt(f, 0.0F, 0.2F, 0.0F, 0.2F);
            if(Time.current() > tflap + 2700L && bForceTakeoffElTrim)
            {
                FM.CT.setTrimElevatorControl(0.0F);
                bForceTakeoffElTrim = false;
            }
        }
        else
        {
            bForceFlapmodeAuto = false;
            float f1 = cvt(FM.getSpeedKMH(), 330F, 465F, 1.0F, 0.0F);
            if(FM.CT.FlapsControlSwitch == 1 && f1 > FM.CT.FlapStage[1])
                f1 = FM.CT.FlapStage[1];
            FM.CT.FlapsControl = f1;
            if(FM.CT.FlapsControlSwitch != 1 && bForceTakeoffElTrim)
            {
                tflap = 0L;
                FM.CT.setTrimElevatorControl(0.0F);
                bForceTakeoffElTrim = false;
            }
            if(FM.CT.FlapsControlSwitch == 1 && tflapSw > 0L && Time.current() > tflapSw + 400L && FM.CT.FlapsControl > 0.6F && ((FM.Or.getPitch() > 180F)? FM.Or.getPitch() - 360F : FM.Or.getPitch()) < (FM.isPlayers() ? 6F : 9F) && FM.Gears.onGround())
            {
                tflap = Time.current();
                FM.CT.setTrimElevatorControl(0.5F);
                bForceTakeoffElTrim = true;
            }
            else if(Time.current() > tflap + 100L && bForceTakeoffElTrim)
                FM.CT.setTrimElevatorControl(cvt(((FM.Or.getPitch() > 180F)? FM.Or.getPitch() - 360F : FM.Or.getPitch()), 5F, 13F, 0.3F, -0.4F));
        }
        if(FM.getAOA() > 28F || FM.getSpeedKMH() < 469F && FM.CT.FlapsControl > 0.16F && FM.CT.getGear() < 0.8F || FM.getOverload() >= 6F)
            FM.CT.AirBrakeControl = 0.0F;
        if((!(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode()) && (FM instanceof Maneuver))
        {
            if(FM.Gears.onGround() && ((Maneuver)FM).get_maneuver() == 25 && FM.AP.way.isLanding() && !FM.AP.way.isLandingOnShip())
            {
                if(FM.getSpeedKMH() > 120F)
                    FM.CT.AirBrakeControl = 1.0F;
                else
                    FM.CT.AirBrakeControl = 0.0F;
            }
            if(((Maneuver)FM).get_maneuver() == 21)
            {
                if(Pitot.Indicator((float)FM.getAltitude(), FM.getSpeed()) > FM.AP.way.curr().Speed * 1.2F && FM.getAltitude() > FM.AP.way.curr().z() + 20F
                   && FM.EI.engines[0].getControlThrottle() < 0.88F && FM.EI.engines[1].getControlThrottle() < 0.88F)
                    FM.CT.AirBrakeControl = 1.0F;
                if(Pitot.Indicator((float)FM.getAltitude(), FM.getSpeed()) < FM.AP.way.curr().Speed * 1.05F || FM.getAltitude() < FM.AP.way.curr().z()
                   || FM.EI.engines[0].getControlThrottle() > 0.96F || FM.EI.engines[1].getControlThrottle() > 0.96F)
                    FM.CT.AirBrakeControl = 0.0F;
            }
        }
        restoreElevatorControl();
    }

    private void computeLift()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float x = cvt(calculateMach(), 0.9F, 1.3F, 0.08F, 0.035F);
        float y = cvt(calculateMach(), 1.3F, 2.2F, 0.035F, 0.012F);
        if(calculateMach() < 1.3F)
            polares.lineCyCoeff = x;
        else
            polares.lineCyCoeff = y;
    }

    private void computeSupersonicLimiter()
    {
        if(FM.getAltitude() < 10000F)
        {
            float x = cvt(FM.getAltitude(), 0.0F, 10000F, 1.16F, 1.78F);
            float xx = cvt(FM.getAltitude(), 0.0F, 10000F, 1.22F, 1.82F);
            if(calculateMach() > x)
                FM.producedAF.x -= cvt(calculateMach(), x, xx, 0F, 30000F);
        }
        else if(FM.getAltitude() < 16000F)
        {
            float y = cvt(FM.getAltitude(), 10000F, 16000F, 1.76F, 1.54F);
            float yy = cvt(FM.getAltitude(), 10000F, 16000F, 1.82F, 1.58F);
            if(calculateMach() > y)
                FM.producedAF.x -= cvt(calculateMach(), y, yy, 0F, 27500F);
        }
        else
        {
            float z = cvt(FM.getAltitude(), 16000F, 18000F, 1.53F, 1.34F);
            float zz = cvt(FM.getAltitude(), 16000F, 18000F, 1.58F, 1.38F);
            if(calculateMach() > z)
                FM.producedAF.x -= cvt(calculateMach(), z, zz, 0F, 27500F);
        }
    }

    public void restoreElevatorControl()
    {
        FM.CT.bHasElevatorControl = true;
    }

    private float clamp11(float f)
    {
        if(f < -1F)
            f = -1F;
        else if(f > 1.0F)
            f = 1.0F;
        return f;
    }

    public void netUpdateWayPoint()
    {
        super.netUpdateWayPoint();
        if(!(FM instanceof RealFlightModel))
        {
            return;
        }
        else
        {
            computeElevators();
            return;
        }
    }

    private void computeElevators()
    {
        if(!isHydraulicAlive)
            return;
        if(FM.CT.StabilizerControl)
            return;
        if(FM.Gears.onGround() && FM.CT.FlapsControl > 0.68F)
            return;
        if(FM.getSpeedKMH() < 30F)
            return;
        float f = FM.CT.getElevator();
        float f1 = 0.0F;
        f = f - oldTrimElevator * 0.01333F + FM.CT.getTrimElevatorControl() * 0.01666F;
        oldTrimElevator = FM.CT.getTrimElevatorControl();
        float fsave = f;
        if(FM.CT.ElevatorControl > 0.0F)
            f1 = (FM.getLimitLoad() * 0.9F - 1.0F) * FM.CT.ElevatorControl + 1.0F;
        else
            f1 = 1.0F - (FM.Negative_G_Limit * 0.9F - 1.0F) * FM.CT.ElevatorControl;
        float f2 = FM.getOverload() - f1;
        f -= f2 / Math.max(FM.getSpeedKMH() / 0.7F, 200F);
        f = clamp11(f);
        float pitch = (FM.Or.getPitch() > 180F)? FM.Or.getPitch() - 360F : FM.Or.getPitch();
        if((!(FM instanceof RealFlightModel) || !((RealFlightModel)FM).isRealMode())
           && ((Maneuver)FM).get_maneuver() != 25 && ((Maneuver)FM).get_maneuver() != 26)
        {
            float alt = (float) (this.pos.getAbsPoint().z - World.land().HQ(this.pos.getAbsPoint().x, this.pos.getAbsPoint().y));
            float tgtpitchmin = cvt(alt, 200F, 600F, -4.0F, -30.0F);
            float tgtpitchmaxWPz = cvt(FM.getAltitude() - FM.AP.way.curr().z(), 100F, 300F, 30.0F, 4.0F);
            float tgtpitchmaxWPs = cvt(FM.getSpeed() - FM.AP.way.curr().Speed, -30F, 30F, 12.0F, 30.0F);
            float tgtpitchmax = 30F;
            if(FM.getSpeedKMH() < 600F)
                tgtpitchmax = cvt(FM.getSpeedKMH(), 400F, 600F, 6.0F, 20.0F);
            else
                tgtpitchmax = cvt(FM.getSpeedKMH(), 600F, 700F, 20.0F, 30.0F);
            float tgtpitchminWPz = cvt(FM.getAltitude() - FM.AP.way.curr().z(), -200F, 100F, 0.0F, -30.0F);

            tgtpitchmax = Math.min(tgtpitchmax, tgtpitchmaxWPz);
            tgtpitchmax = Math.min(tgtpitchmax, tgtpitchmaxWPs);
            tgtpitchmin = Math.max(tgtpitchmin, tgtpitchminWPz);
            if(tgtpitchmax == 0.0F) tgtpitchmax = 0.001F;
            if(tgtpitchmin == 0.0F) tgtpitchmin = -0.001F;

            if(tgtpitchmin > tgtpitchmax)
            {
                float ftemp = tgtpitchmax;
                tgtpitchmax = tgtpitchmin;
                tgtpitchmin = ftemp;
            }

            if(pitch > tgtpitchmax * 1.1F)
            {
                if(f > cvt(pitch - tgtpitchmax * 1.1F, 0F, 50F, -0.2F, -1.0F))
                    f = cvt(pitch - tgtpitchmax * 1.1F, 0F, 50F, -0.2F, -1.0F);
            }
            else if(pitch > tgtpitchmax)
            {
                f = Math.min(f, cvt(pitch, tgtpitchmax, tgtpitchmax * 1.1F, -0.1F, -0.2F));
            }
            else if(pitch > tgtpitchmax * 0.9F)
            {
                f = Math.min(f, cvt(pitch, tgtpitchmax * 0.9F, tgtpitchmax, 0.0F, -0.1F));
            }
            else if(pitch > tgtpitchmax * 0.75F)
            {
                if(f > 0.0F) f *= cvt(pitch / tgtpitchmax, 0.75F, 0.9F, 1.0F, 0.8F);
            }
        }
        FM.CT.bHasElevatorControl = false;
        if(elevatorsField == null)
        {
            elevatorsField = Reflection.getField(FM.CT, "Elevators");
            elevatorsField.setAccessible(true);
        }
        try
        {
            elevatorsField.setFloat(FM.CT, f);
        }
        catch(IllegalAccessException illegalaccessexception) { }
        updateControlsVisuals();
    }

    private void FlapAssistTakeoff()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");

        if(FM.CT.getWing() < 0.01F && FM.CT.getFlap() > 0.25F)
        {
            if(FM.EI.engines[0].getThrustOutput() > 0.97F && FM.EI.engines[1].getThrustOutput() > 0.97F && FM.getSpeedKMH() > 220F && (calculateMach() < 0.32F || FM.Gears.nearGround()))
            {
                polares.Cy0_1 = stockCy0_1 + 1.0F;
                polares.CxMin_1 = stockCxMin_1 + 0.010F;
                FM.Sq.liftStab = stockSqliftStab + 1.2F;
                FM.Sq.squareElevators = stockSqElevators + 2.4F;
            }
            else
            {
                polares.Cy0_1 = stockCy0_1 + 0.1F;
                polares.CxMin_1 = stockCxMin_1 + 0.007F;
                FM.Sq.liftStab = stockSqliftStab;
                FM.Sq.squareElevators = stockSqElevators;
            }
            FM.Sq.squareFlaps = stockSqFlaps + fSqFlaperon;
            FM.Sq.squareAilerons = stockSqAileron - fSqFlaperon * 0.25F;
        }
        else
        {
            polares.Cy0_1 = stockCy0_1;
            polares.CxMin_1 = stockCxMin_1;
            FM.Sq.squareFlaps = stockSqFlaps;
            FM.Sq.squareAilerons = stockSqAileron;
            FM.Sq.liftStab = stockSqliftStab;
            FM.Sq.squareElevators = stockSqElevators;
        }
        return;
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
            else if(arrestor > 1.0F)
                arrestor = 1.0F;
            moveArrestorHook(arrestor);
        }
    }

    private void anticollights()
    {
        if(FM.CT.bAntiColLights && isGeneratorAlive && !oldAntiColLight)
        {
            char postfix = (char)('A' + World.Rnd().nextInt(0, 5));
            for(int i = 0; i < 6; i++)
            {
                if(antiColLight[i] == null)
                {
                    try
                    {
                        antiColLight[i] = Eff3DActor.New(this, findHook("_AntiColLight" + Integer.toString(i + 1)), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRedFlash2_" + String.valueOf(postfix) + ".eff", -1.0F, false);
                    } catch(Exception excp) {}
                }
            }
            oldAntiColLight = true;
        }
        else if((!FM.CT.bAntiColLights || !isGeneratorAlive) && oldAntiColLight)
        {
            for(int i = 0; i < 6; i++)
                if(antiColLight[i] != null)
                {
                    Eff3DActor.finish(antiColLight[i]);
                    antiColLight[i] = null;
                }
            oldAntiColLight = false;
        }
    }

    private void formationlights()
    {
        int i = Mission.cur().curCloudsType();
        float f = Mission.cur().curCloudsHeight() + 500F;
        if((World.getTimeofDay() <= 6.5F || World.getTimeofDay() > 18F || i > 4 && FM.getAltitude() < f) && !FM.isPlayers())
            FM.CT.bFormationLights = true;
        if((World.getTimeofDay() > 6.5F && World.getTimeofDay() <= 18F && i <= 4 || World.getTimeofDay() > 6.5F && World.getTimeofDay() <= 18F && FM.getAltitude() > f) && !FM.isPlayers())
            FM.CT.bFormationLights = false;
        hierMesh().chunkVisible("SSlightstabr", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SSlightstabl", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SSlightnose", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SSlighttail", FM.CT.bFormationLights);
    }

    public void updateHook()
    {
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
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 3: // '\003'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.5F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 4F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            break;

        case 2: // '\002'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.8F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.5F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;

        case 5: // '\005'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurnerF18.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 2.5F, "3DO/Effects/Aircraft/AfterBurnerF18A.eff", -1F);
            break;

        case 4: // '\004'
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;
        }
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
                // fall through

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
                {
                    FM.AS.astateBailoutStep = 11;
                    if(FM.crew < 2)
                        doRemoveBlisters(2, 10);
                }
                else
                {
                    FM.AS.astateBailoutStep = 2;
                }
            }
            else if(FM.AS.astateBailoutStep >= 2 && FM.AS.astateBailoutStep <= 3)
            {
                switch(FM.AS.astateBailoutStep)
                {
                case 2: // '\002'
                    if(FM.CT.cockpitDoorControl < 0.5F)
                        if(FM.crew > 1)
                            doRemoveBlisters(1, 10);
                        else
                            doRemoveBlisters(1, 2);
                    break;

                case 3: // '\003'
                    if(FM.crew > 1)
                        lTimeNextEject = Time.current() + 1000L;
                    else
                        doRemoveBlisters(2, 10);
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
                if(FM.crew < 2)
                {
                    if(byte0 == 1)
                    {
                        FM.setTakenMortalDamage(true, null);
                        if((FM instanceof Maneuver) && ((Maneuver)FM).get_maneuver() != 44 && FM.AS.actor != World.getPlayerAircraft())
                            ((Maneuver)FM).set_maneuver(44);
                    }
                }
                else if((FM instanceof Maneuver) && ((Maneuver)FM).get_maneuver() != 44 && FM.AS.actor != World.getPlayerAircraft())
                    ((Maneuver)FM).set_maneuver(44);
                if(FM.AS.astatePilotStates[byte0 - 11] < 99)
                {
                    if(FM.crew < 2)
                        doRemoveBodyFromPlane(byte0 - 10);
                    if(byte0 == 11)
                    {
                        if(FM.crew > 1)
                        {
                            doRemoveBodyFromPlane(2);
                            doEjectCatapultStudent();
                            lTimeNextEject = Time.current() + 1000L;
                        }
                        else
                        {
                            doEjectCatapult();
                            FM.setTakenMortalDamage(true, null);
                            FM.CT.WeaponControl[0] = false;
                            FM.CT.WeaponControl[1] = false;
                            FM.AS.astateBailoutStep = -1;
                            overrideBailout = false;
                            FM.AS.bIsAboutToBailout = true;
                            ejectComplete = true;
                        }
                    }
                    else if(FM.crew > 1 && byte0 == 12)
                    {
                        doRemoveBodyFromPlane(1);
                        doEjectCatapult();
                        FM.AS.astateBailoutStep = 51;
                        FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                    if(FM.crew > 1)
                        FM.AS.astatePilotStates[byte0 - 11] = 99;
                }
                else
                {
                    EventLog.type("astatePilotStates[" + (byte0 - 11) + "]=" + FM.AS.astatePilotStates[byte0 - 11]);
                }
            }
    }

    private final void doRemoveBlisters(int i, int j)
    {
        for(int k = i; k < j; k++)
            if(hierMesh().chunkFindCheck("Blister" + k + "_D0") != -1 && FM.AS.getPilotHealth(k - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + k + "_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + k + "_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(FM.Vwld);
                wreckage.setSpeed(vector3d);
            }

    }

    public void doEjectCatapultStudent()
    {
        new MsgAction(false, this) {

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
                    new EjectionSeat(10, loc, vector3d, aircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat2_D0", false);
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
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 30D);
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

    public void typeRadarGainPlus()
    {
    }

    public void typeRadarGainMinus()
    {
    }

    public void typeRadarRangePlus()
    {
        radarrange++;
        if(radarrange > 4)
            radarrange = 4;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar range " + radarrange);
    }

    public void typeRadarRangeMinus()
    {
        radarrange--;
        if(radarrange < 1)
            radarrange = 1;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar range " + radarrange);
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
        HUD.log(AircraftHotKeys.hudLogWeaponId, "Radar mode " + radarmode);
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

    private static String actorString(Actor actor) {
		if (!Actor.isValid(actor)) return "(InvalidActor)";
		String s;
		try {
			s = actor.getClass().getName();
		} catch(Exception e) {
			System.out.println("F_18 - actorString(): Cannot resolve class name of " + actor);
			return "(NoClassnameActor)";
		}
        int i = s.lastIndexOf('.');
        String strSection = s.substring(i + 1);
        strSection = strSection + '@' + Integer.toHexString(actor.hashCode());
        return strSection;
    }


    public boolean radartoggle;
    public int radarmode;
    public int radarrange;
    public int radargunsight;
    public int lockmode;
    public float lockrange;
    public int targetnum;
    public float radarvrt;
    public float radarhol;
    public float azimult;
    public float tangate;
    public long tf;
    public int leftscreen;
    public int Bingofuel;
    public boolean Nvision;
    private boolean APmode1;
    public boolean ILS;
    protected boolean bSlatsOff;
    private float oldthrl[] = { -1.0F, -1.0F };
    private float curthrl[] = { -1.0F, -1.0F };
    private float engineSurgeDamage[] = { 0.0F, 0.0F };
    private boolean overrideBailout;
    private boolean ejectComplete;
    private long lTimeNextEject;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
//    public static int LockState = 0;
    Actor hunted = null;
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
    private boolean bObserverKilled;
    public boolean needUpdateHook;
    private Eff3DActor pull01;
    private Eff3DActor pull02;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    public boolean trimauto;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    public float fNozzleOpenL;
    public float fNozzleOpenR;
    private Field elevatorsField;
    private long tflap;
    private long tflapSw;
    private boolean bForceTakeoffElTrim;
    public boolean bForceFlapmodeAuto;
    private float oldTrimElevator;
    public boolean bHasCenterTank;
    public boolean bHasWingTank;
    private float arrestor;
    private Eff3DActor antiColLight[];
    private boolean oldAntiColLight;
    public boolean isHydraulicAlive;
    public boolean isGeneratorAlive;
    public boolean isBatteryOn;

    private float stockCy0_0;
    private float stockCy0_1;
    private float stockCxMin_0;
    private float stockCxMin_1;
    private float stockCyCritH_0;
    private float stockSqFlaps;
    private float stockSqAileron;
    private float stockSqliftStab;
    private float stockSqElevators;
    private static float fSqFlaperon = 2.646F;
    private long lastUpdateTime;

    // TypeDockable and Refuel values
    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;
    private float fuelReceiveRate;

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

    // TypeCountermeasure setup values
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;

    // TypeGuidedMissileCarrier and AI decision control value
    private GuidedMissileUtils guidedMissileUtils;
    private boolean bHasLAGM;
    private boolean bHasAGM;
    private boolean bHasAShM;
    private boolean bHasUGR;
    private long lastAGMcheck;

    // TypeRadarWarningReceiver setup values
    private RadarWarningReceiverUtils rwrUtils;
    public float misslebrg;
    public float aircraftbrg;
    private boolean backfire;
    public boolean bRadarWarning;
    public boolean bMissileWarning;

    private static final int RWR_GENERATION = 1;
    private static final int RWR_MAX_DETECT = 16;
    private static final int RWR_KEEP_SECONDS = 7;
    private static final double RWR_RECEIVE_ELEVATION = 45.0D;
    private static final boolean RWR_DETECT_IRMIS = false;
    private static final boolean RWR_DETECT_ELEVATION = false;
    private boolean bRWR_Show_Text_Warning = true;

    // TypeLaserDesignator and laser guided weapon values
    public boolean FLIR;
    private boolean bLaserOn;
    public long laserTimer;
    private Point3d laserSpotPos;
    private boolean bLGBengaged;
    private boolean bHasPaveway;
    private boolean bHadLGweapons;
    private boolean bAILaserOn;
    private long lAILaserTimer;
    private long lAIGAskipTimer;
    private static float maxPavewayFOVfrom = 45.0F;
    private static double maxPavewayDistance = 20000D;
    private long lastLGBcheck;
    private long lastAIMissileSelect;
    public boolean hold;
    public boolean holdFollow;
    public Actor actorFollowing;
    public boolean bHasLaser;
    private long t1;

    private Actor semiradartarget;
    private Actor groundradartarget;

    private int iDebugLogLevel = 0;

    static
    {
        Class class1 = com.maddox.il2.objects.air.F_18.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}