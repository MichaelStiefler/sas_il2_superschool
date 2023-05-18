
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.ai.ground.NearestEnemies;
import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.cars.CarGeneric;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.*;

public class AV_8B extends AV_8
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeDockable, TypeRadarWarningReceiver, TypeLaserDesignator, TypeRadar, TypeSemiRadar, TypeGroundRadar
{

    public AV_8B()
    {
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        queen_last = null;
        queen_time = 0L;
        bNeedSetup = true;
        dtime = -1L;
        target_ = null;
        queen_ = null;
        dockport_ = -1;
        fuelReceiveRate = 11.101F;
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
        counter = 0;
        error = 0;
        raretimer = 0L;
        semiradartarget = null;
        groundradartarget = null;
        if(Config.cur.ini.get("Mods", "RWRTextStop", 0) > 0) bRWR_Show_Text_Warning = false;
        rwrUtils = new RadarWarningReceiverUtils(this, RWR_GENERATION, RWR_MAX_DETECT, RWR_KEEP_SECONDS, RWR_RECEIVE_ELEVATION, RWR_DETECT_IRMIS, RWR_DETECT_ELEVATION, bRWR_Show_Text_Warning);
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
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunAGM84E_gn16)
                            bHasAGM = true;
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunAGM65E_gn16)
                        {
                            bHasLAGM = true;
                            bHasAGM = true;
                            bHadLGweapons = true;
                        }
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunAGM84D_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM84J_gn16)
                            bHasAShM = true;
                        else if(FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71AP_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71WPFAC_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71WPFO_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunHYDRA_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunHYDRA_WPFAC_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunHYDRA_WPFO_gn16)
                            bHasUGR = true;
                    }
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_AN_AAQ28_gn16)
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
                        if(FM.CT.Weapons[i][j] instanceof RocketGunAGM84E_gn16)
                            bHasAGM = true;
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunAGM65E_gn16)
                        {
                            bHasLAGM = true;
                            bHasAGM = true;
                        }
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunAGM84D_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunAGM84J_gn16)
                            bHasAShM = true;
                        else if(FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71AP_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGun5inchZuniMk71WPFAC_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunHYDRA_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunHYDRA_WPFAC_gn16 ||
                                FM.CT.Weapons[i][j] instanceof RocketGunHYDRA_WPFO_gn16)
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
        super.vtolSlipY = 0;
    }

    public void typeBomberAdjSideslipPlus()
    {
        if(nozzlemode == 1 && vtolvect > 0.74F)
        {
            super.vtolSlipY += 10;
            if(super.vtolSlipY > 100)
                super.vtolSlipY = 100;
            if(this == World.getPlayerAircraft())
            {
                if(super.vtolSlipY == 0)
                    HUD.log("Side Slip Thrust: Neutral");
                else if(super.vtolSlipY > 0)
                    HUD.log("Side Slip Thrust: Right" + Math.abs(super.vtolSlipY));
                else
                    HUD.log("Side Slip Thrust: Left" + Math.abs(super.vtolSlipY));
            }
        }
        else if(FLIR)
        {
            tangate++;
            tf = Time.current();
        }
        else if(radartoggle && lockmode == 0)
            radarvrt += 0.0035F;
    }

    public void typeBomberAdjSideslipMinus()
    {
        if(nozzlemode == 1 && vtolvect > 0.74F)
        {
            super.vtolSlipY -= 10;
            if(super.vtolSlipY < -100)
                super.vtolSlipY = -100;
            if(this == World.getPlayerAircraft())
            {
                if(super.vtolSlipY == 0)
                    HUD.log("Side Slip Thrust: Neutral");
                else if(super.vtolSlipY > 0)
                    HUD.log("Side Slip Thrust: Right" + Math.abs(super.vtolSlipY));
                else
                    HUD.log("Side Slip Thrust: Left" + Math.abs(super.vtolSlipY));
            }
        }
        else if(FLIR)
        {
            tangate--;
            tf = Time.current();
        }
        else if(radartoggle && lockmode == 0)
            radarvrt -= 0.0035F;
    }

    public void typeBomberAdjAltitudeReset()
    {
        super.vtolSlipX = 0;
    }

    public void typeBomberAdjAltitudePlus()
    {
        if(nozzlemode == 1 && vtolvect > 0.74F)
        {
            super.vtolSlipX += 10;
            if(super.vtolSlipX > 100)
                super.vtolSlipX = 100;
            if(this == World.getPlayerAircraft())
            {
                if(super.vtolSlipX == 0)
                    HUD.log("Forward Slip Thrust: Neutral");
                else if(super.vtolSlipX > 0)
                    HUD.log("Forward Slip Thrust: " + Math.abs(super.vtolSlipX));
                else
                    HUD.log("Backward Slip Thrust: " + Math.abs(super.vtolSlipX));
            }
        }
        else if(FLIR)
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
        if(nozzlemode == 1 && vtolvect > 0.74F)
        {
            super.vtolSlipX -= 10;
            if(super.vtolSlipX < -100)
                super.vtolSlipX = -100;
            if(this == World.getPlayerAircraft())
            {
                if(super.vtolSlipX == 0)
                    HUD.log("Forward Slip Thrust: Neutral");
                else if(super.vtolSlipX > 0)
                    HUD.log("Forward Slip Thrust: " + Math.abs(super.vtolSlipX));
                else
                    HUD.log("Backward Slip Thrust: " + Math.abs(super.vtolSlipX));
            }
        }
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


    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        FM.turret[0].bIsAIControlled = false;
        t1 = Time.current();
        rwrUtils.onAircraftLoaded();
        rwrUtils.setLockTone("aircraft.usRWRScan", "aircraft.usRWRLock", "aircraft.usRWRLaunchWarningMissileMissile", "aircraft.usRWRThreatNew");
    }

    public void missionStarting()
    {
        super.missionStarting();
        checkAmmo();
        checkAsDrone();
        FM.CT.toggleRocketHook();
        FM.CT.FlapsControlSwitch = 1;
        laserTimer = -1L;
        bLaserOn = false;
        FLIR = false;
        iDebugLogLevel = Config.cur.ini.get("Mods", "GuidedMissileDebugLog", 0);
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

    public void startCockpitSounds()
    {
        rwrUtils.setSoundEnable(true);
    }

    public void stopCockpitSounds()
    {
        rwrUtils.stopAllRWRSounds();
    }

    public void rareAction(float f, boolean flag)
    {
        if(raretimer != Time.current() && this == World.getPlayerAircraft())
        {
            counter++;
            if(counter % 12 == 9)
                InertialNavigation();
        }
        raretimer = Time.current();

        super.rareAction(f, flag);

        if(bHasLaser && this == World.getPlayerAircraft())
        {
            if(FLIR)
                FLIR();
            if(!FLIR)
                FM.AP.setStabAltitude(false);
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
                       FM.CT.rocketNameSelected != "AGM-65E")
                    {
                        for(int i = 0; i < 4; i++)
                        {
                            if(FM.CT.rocketNameSelected == "AGM-65E")
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
        }

        if((bHasPaveway || FM.bNoDiveBombing) && Time.current() > lastLGBcheck + 30000L && (FM instanceof Maneuver))
        {
            checkGuidedBombRest();
            lastLGBcheck = Time.current();
        }

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

    private boolean InertialNavigation()
    {
        Point3d point3d = new Point3d();
        pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if(aircraft.getSpeed(vector3d) > 20D && aircraft.pos.getAbsPoint().z >= 150D)
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
            double d1 = ((Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft)).pos.getAbsPoint().x) / 1000D) / 10D;
            double d2 = ((Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().y) / 1000D) / 10D;
            char c = (char)(int)(65D + Math.floor((d1 / 676D - Math.floor(d1 / 676D)) * 26D));
            char c1 = (char)(int)(65D + Math.floor((d1 / 26D - Math.floor(d1 / 26D)) * 26D));
            String s = "";
            if(d > 260D)
                s = "" + c + c1;
            else
                s = "" + c1;
            int l = (int)Math.ceil(d2);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "INS: " + s + "-" + l);
        }
        return true;
    }

    public void update(float f)
    {
        if(bNeedSetup)
            checkAsDrone();
        guidedMissileUtils.update();
        if(super.FM instanceof Maneuver)
            receivingRefuel(f);
        rwrUtils.update();
        backfire = rwrUtils.getBackfire();
        bRadarWarning = rwrUtils.getRadarLockedWarning();
        bMissileWarning = rwrUtils.getMissileWarning();
        if(bHasLaser && this == World.getPlayerAircraft())
        {
            if(FLIR)
                laser(getLaserSpot());
            updatecontrollaser();
        }
        if(bHasLaser && this != World.getPlayerAircraft() && (FM instanceof Maneuver))
            AILaserControl();

        super.update(f);
        if(backfire)
            backFire();
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        super.msgCollisionRequest(actor, aflag);
        if(queen_last != null && queen_last == actor && (queen_time == 0L || Time.current() < queen_time + 5000L))
            aflag[0] = false;
        else
            aflag[0] = true;
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


    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(byte bt)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[bt];
        try
        {
            a_lweaponslot[92] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - AV_8B : Default loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
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

    private int counter;
    private int error;
    private long raretimer;

    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;

    // TypeDockable and Refuel values
    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;
    private float fuelReceiveRate;

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
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Harrier");
        Property.set(class1, "meshName", "3DO/Plane/AV-8B/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1997F);
        Property.set(class1, "yearExpired", 2014F);
        Property.set(class1, "FlightModel", "FlightModels/AV-8B.fmd:AV8B");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitAV_8B.class, com.maddox.il2.objects.air.CockpitAV8FLIR.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 9, 9, 9, 9, 9, 9, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 9, 9, 9, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 7, 7, 8, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01",       "_ExternalDev01",  "_ExternalDev02",  "_ExternalDev03",  "_ExternalDev04",  "_ExternalDev05",  "_ExternalDev06",  "_ExternalDev07",  "_ExternalDev08",  "_ExternalDev09",
            "_ExternalDev10",  "_ExternalDev11",  "_ExternalDev12",  "_ExternalDev13",  "_ExternalDev14",  "_ExternalDev15",  "_ExternalDev16",  "_ExternalDev17",  "_ExternalDev18",  "_ExternalBomb01",
            "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11",
            "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalMis01",  "_ExternalMis01",  "_ExternalMis02",  "_ExternalMis02",
            "_ExternalMis03",  "_ExternalMis03",  "_ExternalMis04",  "_ExternalMis04",  "_ExternalMis05",  "_ExternalMis05",  "_ExternalMis06",  "_ExternalMis06",  "_ExternalRock01", "_ExternalRock01",
            "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalRock06",
            "_ExternalRock07", "_ExternalRock07", "_ExternalRock08", "_ExternalRock08", "_Rock09",         "_Rock10",         "_Rock11",         "_Rock12",         "_Rock13",         "_Rock14",
            "_Rock15",         "_Rock16",         "_Rock17",         "_Rock18",         "_Rock19",         "_Rock20",         "_Rock21",         "_Rock22",         "_Rock23",         "_Rock24",
            "_Rock25",         "_Rock26",         "_Rock27",         "_Rock28",         "_Rock29",         "_Rock30",         "_Rock31",         "_Rock32",         "_Rock33",         "_Rock34",
            "_Rock35",         "_Rock36",         "_FlareL",         "_FlareR",         "_ChaffL",         "_ChaffR"
        });
        String s = "empty";
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 96;
            s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM120+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM120+4xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM120+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM120";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM120+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM120+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM120+2xAIM9+2xDt+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM120+2xDt+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU127_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9+4xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82HD+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xMk82_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xMk82HD_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82HD+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83HD+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83HD+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82HD+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xMk82_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xMk82HD_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk83";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk83HD";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk20+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "10xMk20_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk20+2xMk82+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk20+2xMk83+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+2xMk20+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83+2xMk20+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk77_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+2xMk77+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+2xMk82HD+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+2xMk83HD+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82+2xZuni+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82HD+2xZuni+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83+2xZuni+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83HD+2xZuni+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk20+2xZuni+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk77+2xZuni+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+2xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+2xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+4xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+4xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83+4xZuniAP";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83HD+4xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk20+4xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+4xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xZuni+2xMk82+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xZuniAP+2xMk83+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xZuni+2xMk20+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xZuni+2xMk77+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+2xHydra70+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+2xHydra70+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk20+4xHydra70";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+4xHydra70";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xHydra70+2xMk82";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xHydra70+2xMk82HD";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xHydra70+2xMk20";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xHydra70+2xM77";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82HD+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83HD+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk20+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk77+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xZuni+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xHydra70+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82HD+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xZuni+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xHydra70+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xGBU12+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+1xGBU12+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU16_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "7xGBU12";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+6xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xGBU12+2xMk82_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xGBU12+2xMk20_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+2xZuni_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xGBU12+2xZuni_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xGBU12+4xHydra70_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU130_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunHYDRA_gn16", 19);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+1xMk82+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+1xMk20+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+1xZuni+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xGBU12+4xZuni+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU12+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU16+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU12+2xMk20+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xGBU16+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU12+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xGBU12+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xGBU12+3xMk82+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xGBU12+3xZuni+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xGBU12+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM82+1xMk82+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM83+1xMk82+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM82+1xMk20+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM83+1xMk20+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM82+1xGBU12+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM83+1xGBU12+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM82+4xMk20_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM83+2xMk83_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM82+2xAIM9_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM82+2xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM82+2xZuni_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM82+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM83+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM82+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM82+Mk20+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM82+Zuni+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM82+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM83+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM82+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xJDAM83+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xJDAM82+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xMk82";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xMk20";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+4xZuni_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xMk83_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+6xMk82_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+6xMk20_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xGBU12+2xAIM9_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xGBU16+2xAIM9_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+6xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xJDAM82+2xAIM9_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xJDAM83+2xAIM9_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+6xJDAM82_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xMk83+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xZuni+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xGBU12+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xGBU16+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+2xMk83+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+6xMk82+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+6xMk20+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+2xGBU12+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+2xGBU16+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+6xGBU12+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+3xGBU12+3xMk20+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+2xJDAM82+2xAIM9+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+2xJDAM83+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+6xJDAM82+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xGBU12+2xZuni+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xGBU16+2xZuni+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xMk82+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xMk83+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xMk20+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xMk77+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xGBU12+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+1xGBU16+2xAIM9+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+2xZuni+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_USTER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM65E+2xAIM9+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+2xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+2xZuni";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+1xAGM65E";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+2xAGM65E_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+1xAGM65E+1xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+1xAGM65E+1xGBU16_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+1xAGM65E";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+2xAGM65E_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+1xAGM65E+1xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+1xAGM65E+1xGBU16_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84D+2xAGM65E";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84D+1xAGM65E+1xGBU12";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84D+1xAGM65E+1xGBU16";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84J+2xAGM65E";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84J+1xAGM65E+1xGBU12";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84J+1xAGM65E+1xGBU16";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84D+1xAGM65E+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84D+1xGBU12+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84D+1xGBU16+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84J+1xAGM65E+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84J+1xGBU12+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM84J+1xGBU16+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_2xMk82+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_2xMk77+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_2xMk77+ALQ164_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_2xAGM65E_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_1xAGM65E+1xMk77_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_1xAGM65E+1xGBU12_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_1xMk77+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunGAU12U", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU7_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_1xMk77+ALQ164+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ164_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_1xAGM65E+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(5, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "FAC_1xGBU12+2xDt_NoGun";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkL1_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkR1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_AN_AAQ28_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71WPFAC_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - AV_8B : " + s);
            System.out.println(exception.getMessage());
            exception.printStackTrace();
 }
    }
}