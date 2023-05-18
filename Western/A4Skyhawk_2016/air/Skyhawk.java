
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import java.io.IOException;
import java.util.List;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, TypeSupersonic, TypeFighter, TypeBNZFighter,
//            TypeFighterAceMaker, TypeRadarGunsight, TypeStormovik, TypeGSuit,
//            TypeZBReceiver, TypeFuelDump, TypeFastJet, AircraftLH,
//            Aircraft, PaintScheme, EjectionSeat

public class Skyhawk extends Scheme1
    implements TypeSupersonic, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeRadarGunsight, TypeStormovik, TypeBomber, TypeX4Carrier, TypeGSuit, TypeZBReceiver, TypeFuelDump, TypeFastJet, TypeRadarWarningReceiver
{

    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
    }

    public void getGFactors(TypeGSuit.GFactors theGFactors)
    {
        theGFactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
    }

    public RadarWarningReceiverUtils getRadarWarningReceiverUtils()
    {
        return rwrUtils;
    }

    public void myRadarSearchYou(Actor actor, String soundpreset)
    {
        if(bHasRWR)
            rwrUtils.recordRadarSearched(actor, soundpreset);
    }

    public void myRadarLockYou(Actor actor, String soundpreset)
    {
        if(bHasRWR)
            rwrUtils.recordRadarLocked(actor, soundpreset);
    }

    private static final float toMeters(float f)
    {
        return 0.3048F * f;
    }

    private static final float toMetersPerSecond(float f)
    {
        return 0.4470401F * f;
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

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        if(k14Mode == 0)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Caged");
        } else
        if(k14Mode == 1)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Mode: Uncaged");
        } else
        if(k14Mode == 2 && FM.actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Sight Off");
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
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
        if(k14WingspanType == 0)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
        } else
        if(k14WingspanType == 1)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
        } else
        if(k14WingspanType == 2)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
        } else
        if(k14WingspanType == 3)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
        } else
        if(k14WingspanType == 4)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
        } else
        if(k14WingspanType == 5)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
        } else
        if(k14WingspanType == 6)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
        } else
        if(k14WingspanType == 7)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
        } else
        if(k14WingspanType == 8)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
        } else
        if(k14WingspanType == 9 && FM.actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        if(k14WingspanType == 0)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-17/19/21");
        } else
        if(k14WingspanType == 1)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: MiG-15");
        } else
        if(k14WingspanType == 2)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Me-262");
        } else
        if(k14WingspanType == 3)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Pe-2");
        } else
        if(k14WingspanType == 4)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: 60ft");
        } else
        if(k14WingspanType == 5)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Canberra Bomber");
        } else
        if(k14WingspanType == 6)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Yak-28/Il-28");
        } else
        if(k14WingspanType == 7)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: C-47");
        } else
        if(k14WingspanType == 8)
        {
            if(FM.actor == World.getPlayerAircraft())
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-16");
        } else
        if(k14WingspanType == 9 && FM.actor == World.getPlayerAircraft())
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Wingspan Selected: Tu-4");
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

    public void typeFighterAceMakerRangeFinder()
    {
        if(k14Mode == 2)
            return;
        if(!Config.isUSE_RENDER())
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

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
    }

    public void typeBomberAdjDistanceMinus()
    {
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
    }

    public void typeBomberAdjSideslipMinus()
    {
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
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
            if(fSightCurDistance < toMetersPerSecond(fSightCurSpeed) * Math.sqrt(toMeters(fSightCurAltitude) * 0.2038736F))
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

    public Skyhawk()
    {
        lastUpdateTime = -1L;
        lastRareActionTime = -1L;
        critSpeed = 0.0F;
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
        bTwoSeat = false;
        bObserverKilled = false;
        bNoSpoiler = false;
        SpoilerBrakeControl = 0.0F;
        spoilerBrake = 0.0F;
        overrideBailout = false;
        ejectComplete = false;
        lTimeNextEject = 0L;
        fSteer = 0.0F;
        APmode1 = false;
        APmode2 = false;
        timeCounterRud9 = 0.0F;
        timeCounterHyd9 = 0.0F;
        timeRudder9 = 600F;
        timeHydro9 = 600F;
        timeCounterCrash9 = 0.0F;
        timeCrash9 = 3F;
        timeBrake19 = 10F;
        timeBrake29 = 17F;
        timeBrake39 = 2400F;
        timeCounterBrake9 = 0.0F;
        timeCounterBrake29 = 0.0F;
        timeCounterBoost = 0.0F;
        isHydraulicAlive = false;
        isGeneratorAlive = false;
        isBatteryOn = false;
        lLastHydraulicLost = -1L;
        lLastHydraulicGot = -1L;
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 3000F;
        fSightCurSpeed = 200F;
        fSightCurReadyness = 0.0F;
        tX4Prev = 0L;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        antiColLight = new Eff3DActor[6];
        oldAntiColLight = false;
        arrestor = 0.0F;
        fSlat = 0.0F;
        iSlatControl = 0;
        bHasRWR = false;
        misslebrg = 0.0F;
        aircraftbrg = 0.0F;
        stockSquareWing = 24.11F;
        stockLiftWingLMid = 4.60F;
        stockLiftWingRMid = 4.60F;
        stockCy0 = 0.009F;
        stockCy1 = 0.5F;
        stockDragAirbrake = 0.095F;

        if((this instanceof SkyhawkA4E) || (this instanceof SkyhawkA4E_tanker) || (this instanceof SkyhawkA4M) ||
           (this instanceof SkyhawkA4F) || (this instanceof SkyhawkA4F_late))
            bHasRWR = true;

        if(Config.cur.ini.get("Mods", "RWRTextStop", 0) > 0) bRWR_Show_Text_Warning = false;
        if(bHasRWR)
            rwrUtils = new RadarWarningReceiverUtils(this, RWR_GENERATION, RWR_MAX_DETECT, RWR_KEEP_SECONDS, RWR_RECEIVE_ELEVATION, RWR_DETECT_IRMIS, RWR_DETECT_ELEVATION, bRWR_Show_Text_Warning);
//          rwrUtils = new RadarWarningReceiverUtils(this, RWR_GENERATION, RWR_MAX_DETECT, RWR_KEEP_SECONDS, RWR_RECEIVE_ELEVATION, RWR_DETECT_IRMIS, RWR_DETECT_ELEVATION, bRWR_Show_Text_Warning, 12, "A4- ");
        else
            rwrUtils = null;
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

        FM.CT.bHasBombSelect = true;
        FM.CT.bHasAntiColLights = true;

        if(bHasRWR)
        {
            rwrUtils.onAircraftLoaded();
            rwrUtils.setLockTone("aircraft.APR25AAA", "aircraft.APR25S75EbandLock", "aircraft.APR25S75EbandLock", "APR25AAA.wav", "APR25S75EbandLock.wav", "APR25S75EbandLock.wav");
        }

        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        stockSquareWing = FM.Sq.squareWing;
        stockLiftWingLMid = FM.Sq.liftWingLMid;
        stockLiftWingRMid = FM.Sq.liftWingRMid;
        stockCy0 = polares.Cy0_0;
        stockCy1 = polares.Cy0_1;
        stockDragAirbrake = FM.Sq.dragAirbrakeCx;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(lastRareActionTime != Time.current())
        {
            if(bTwoSeat)
                if(!bObserverKilled)
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
                        } else
                        {
                            obsAzimuth = World.Rnd().nextFloat() * 140F - 70F;
                            obsElevation = World.Rnd().nextFloat() * 50F - 20F;
                        }
                    } else
                    {
                        obsLookTime--;
                    }

            if ((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver))
                if (FM.AP.way.isLanding() && FM.getSpeed() > FM.VmaxFLAPS && FM.getSpeed() > FM.AP.way.curr().getV() * 1.4F)
                {
                    if (FM.CT.AirBrakeControl != 1.0F)
                        FM.CT.AirBrakeControl = 1.0F;
                }
                else if (((Maneuver) FM).get_maneuver() == 25 && FM.AP.way.isLanding() && FM.getSpeed() < FM.VminFLAPS * 1.16F)
                {
                    if (FM.getSpeed() > FM.VminFLAPS * 0.5F && FM.Gears.onGround())
                    {
                        if (FM.CT.AirBrakeControl != 1.0F)
                            FM.CT.AirBrakeControl = 1.0F;
                    }
                    else if (FM.CT.AirBrakeControl != 0.0F)
                        FM.CT.AirBrakeControl = 0.0F;
                }
                else if (((Maneuver) FM).get_maneuver() == 66)
                {
                    if (FM.CT.AirBrakeControl != 0.0F)
                        FM.CT.AirBrakeControl = 0.0F;
                }
                else if (((Maneuver) FM).get_maneuver() == 7)
                {
                    if (FM.CT.AirBrakeControl != 1.0F)
                        FM.CT.AirBrakeControl = 1.0F;
                }
                else if (FM.CT.AirBrakeControl != 0.0F)
                    FM.CT.AirBrakeControl = 0.0F;
            if((FM.Gears.nearGround() || FM.Gears.onGround()) && FM.CT.getCockpitDoor() == 1.0F)
            {
                hierMesh().chunkVisible("HMask1_D0", false);
                if(bTwoSeat)
                    hierMesh().chunkVisible("HMask2_D0", false);
            }
            else
            {
                hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
                if(bTwoSeat)
                    hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
            }
            if(!FM.isPlayers())
                FM.CT.bAntiColLights = FM.AS.bNavLightsOn;
            anticollights();
            lastRareActionTime = Time.current();
        }
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
            if(bTwoSeat)
            {
                hierMesh().chunkVisible("Pilot2_D0", false);
                hierMesh().chunkVisible("Head2_D0", false);
                hierMesh().chunkVisible("HMask2_D0", false);
                hierMesh().chunkVisible("Pilot2_D1", true);
            }
            break;
        }
    }

    public static void moveGear(HierMesh hiermesh, float fL, float fR, float fC)
    {
        float fc2 = Aircraft.cvt(fC, 0.25F, 0.9F, 0.0F, -113F);
        float fc4 = Aircraft.cvt(fC, 0.0F, 0.2F, 0.0F, -90F);
        float fc5 = Aircraft.cvt(fC, 0.25F, 0.9F, 0.0F, -43F);
        float fc7 = Aircraft.cvt(fC, 0.25F, 0.9F, 0.0F, -45F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, fc2, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, fc4, 0.0F);
        if(fC < 0.2F)
            hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, fc5, 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, -fc7, 0.0F);

        float fl2 = Aircraft.cvt(fL, 0.25F, 0.9F, 0.0F, -94F);
        float fl3 = Aircraft.cvt(fL, 0.25F, 0.9F, 0.0F, -42F);
        float fl3b = Aircraft.cvt(fL, 0.32F, 0.90F, 0.0F, 172F);
        float fl4 = Aircraft.cvt(fL, 0.0F, 0.2F, 0.0F, -83F);
        float fl5 = Aircraft.cvt(fL, 0.3F, 0.7F, 0.0F, -90F);
        float fl6 = Aircraft.cvt(fL, 0.0F, 0.2F, 0.0F, -90F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, fl2, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, fl6, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, fl4, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", fl5, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, fl3, 0.0F);
        hiermesh.chunkSetAngles("GearL3b_D0", 0.0F, fl3b, 0.0F);

        float fr2 = Aircraft.cvt(fR, 0.25F, 0.9F, 0.0F, -94F);
        float fr3 = Aircraft.cvt(fR, 0.25F, 0.9F, 0.0F, -42F);
        float fr3b = Aircraft.cvt(fR, 0.32F, 0.90F, 0.0F, 172F);
        float fr4 = Aircraft.cvt(fR, 0.0F, 0.2F, 0.0F, -83F);
        float fr5 = Aircraft.cvt(fR, 0.3F, 0.7F, 0.0F, -90F);
        float fr6 = Aircraft.cvt(fR, 0.0F, 0.2F, 0.0F, -90F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -fr2, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, fr6, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, fr4, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", -fr5, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, fr3, 0.0F);
        hiermesh.chunkSetAngles("GearR3b_D0", 0.0F, fr3b, 0.0F);
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    // ************************************************************************************************
    // Gear code for backward compatibility, older base game versions don't indepently move their gears
    public static void moveGear(HierMesh hiermesh, float f)
    {
        moveGear(hiermesh, f, f, f); // re-route old style function calls to new code
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        //By PAL, Central
        //float f = Aircraft.cvt(FM.Gears.gWheelSinking[2], 0.0F, 0.19075F, 0.0F, 1.0F);
        resetYPRmodifier();
        float f60 = cvt(FM.Gears.gWheelSinking[2], 0.0F, /*fSinkF*/0.25F, 0.0F, -0.4F); //2405F); //-0.19075F * f;
        xyz[0] = f60;
        xyz[2] = 0.5F * f60; //By PAL, 0.5F component in the other axis to make it move aligned
        hierMesh().chunkSetLocate("GearC2b_D0", Aircraft.xyz, Aircraft.ypr);
        //hierMesh().chunkSetAngles("GearC3_D0", 0.0F, 0.0F, fSteer);
        //By PAL, Main Gears:
        resetYPRmodifier();
        float f6 = Aircraft.cvt(FM.CT.getGear(), 0.2F, 0.6F, 0.0F, 90F); //By PAL, originally between 0 an 0.8
        xyz[2] = cvt(FM.Gears.gWheelSinking[0], 0.0F, /*fSinkR*/0.25F, 0.0F, -0.35F); //44F);
        ypr[0] = -f6; //-90.0F; //-90F;
        hierMesh().chunkSetLocate("GearL5_D0", xyz, ypr);
        resetYPRmodifier();
        xyz[2] = cvt(FM.Gears.gWheelSinking[1], 0.0F, /*fSinkR*/0.25F, 0.0F, -0.35F); //44F);
        ypr[0] = f6; //+90.0F; //90F;
        hierMesh().chunkSetLocate("GearR5_D0", xyz, ypr);
    }

    public void moveSteering(float f)
    {
        //By PAL, interpolate it
        fSteer += 0.075F * (f - fSteer);
        //By PAL, Limit on -30...30
        if(fSteer < -maxSteer) fSteer = -maxSteer;
            else
        if(fSteer > maxSteer) fSteer = maxSteer;
        FM.Gears.steerAngle = fSteer;
        //By PAL, to line up Gear when Up
            hierMesh().chunkSetAngles("GearC3_D0", 0.0F, 0.0F, fSteer * FM.CT.GearControl);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        updateFlapsAndAirbrakesVisuals();
    }

    protected void moveFan(float f)
    {
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -70F * f, 0.0F);
        arrestor = f;
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 45F * f, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    private void updateFlapsAndAirbrakesVisuals()
    {
        if(lLastHydraulicGot > 0L && Time.current() > 3000L)
        {
            if(Time.current() - lLastHydraulicGot < 3000L)
            {
                float f1 = cvt((float)(Time.current() - lLastHydraulicGot), 800F, 2700F, 1.0F, 0.0F);
                float deg = Math.max(4F * f1, 45F * FM.CT.getAirBrake());
                hierMesh().chunkSetAngles("FuBrake01_D0", -deg, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("FuBrake02_D0", deg, 0.0F, 0.0F);

                f1 = cvt((float)(Time.current() - lLastHydraulicGot), 300F, 2900F, 1.0F, 0.0F);
                deg = Math.max(45F * f1, 55F * FM.CT.getFlap());
                hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -deg, 0.0F);
                hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -deg, 0.0F);
            }
            else
            {
                hierMesh().chunkSetAngles("FuBrake01_D0", -45F * FM.CT.getAirBrake(), 0.0F, 0.0F);
                hierMesh().chunkSetAngles("FuBrake02_D0", 45F * FM.CT.getAirBrake(), 0.0F, 0.0F);
                hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -55F * FM.CT.getFlap(), 0.0F);
                hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -55F * FM.CT.getFlap(), 0.0F);
            }
        }
        if(lLastHydraulicLost > 0L)
        {
            if(Time.current() - lLastHydraulicLost < 30000L)
            {
                float f1 = cvt((float)(Time.current() - lLastHydraulicLost), 9000F, 27000F, 0.0F, 1.0F);
                float deg = Math.max(4F * f1, 45F * FM.CT.getAirBrake());
                hierMesh().chunkSetAngles("FuBrake01_D0", -deg, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("FuBrake02_D0", deg, 0.0F, 0.0F);

                f1 = cvt((float)(Time.current() - lLastHydraulicLost), 4000F, 29500F, 0.0F, 1.0F);
                deg = Math.max(45F * f1, 55F * FM.CT.getFlap());
                hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -deg, 0.0F);
                hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -deg, 0.0F);
            }
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
                    getEnergyPastArmor(13.35D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
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
            } else
            if(s.startsWith("xxmgun0"))
            {
                int j = s.charAt(7) - 49;
                if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Machine Gun (" + j + ") Disabled..");
                    FM.AS.setJamBullets(0, j);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else
            if(s.startsWith("xxtank"))
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
            } else
            if(s.startsWith("xxspar"))
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
                hitChunk("CF", shot);
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
                int l;
                if(s.endsWith("a"))
                {
                    byte0 = 1;
                    l = s.charAt(6) - 49;
                } else
                if(s.endsWith("b"))
                {
                    byte0 = 2;
                    l = s.charAt(6) - 49;
                } else
                {
                    l = s.charAt(5) - 49;
                }
                hitFlesh(l, shot, byte0);
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

    public void soundbarier()
    {
    }

    public float calculateMach()
    {
        return FM.getSpeedKMH() / getMachForAlt(FM.getAltitude());
    }

    protected void moveAirBrake(float f)
    {
        updateFlapsAndAirbrakesVisuals();
    }

    public void checkSpolers(float f)
    {
        if(FM.Gears.nOfGearsOnGr > 1 && FM.CT.getPowerControl() < 0.70F)
        {
            SpoilerBrakeControl = FM.CT.AirBrakeControl;
        }
        else
        {
            SpoilerBrakeControl = 0.0F;
        }

        spoilerBrake = filter(f, SpoilerBrakeControl, spoilerBrake, 999.9F, FM.CT.dvAirbrake);
        FM.Sq.dragAirbrakeCx = stockDragAirbrake + 0.20F * spoilerBrake;

        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 0.0F, -45F * spoilerBrake);
        hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 0.0F, -45F * spoilerBrake);
    }

    private void bailout()
    {
        if(overrideBailout)
            if(FM.AS.astateBailoutStep >= 0 && FM.AS.astateBailoutStep < 2)
            {
                if(FM.CT.cockpitDoorControl > 0.5F && FM.CT.getCockpitDoor() > 0.5F && FM.getSpeedKMH() < 15F)
                {
                    FM.AS.astateBailoutStep = 11;
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
                    if(FM.CT.cockpitDoorControl < 0.5F || FM.CT.getCockpitDoor() < 0.5F || FM.getSpeedKMH() > 15F)
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
                if((FM instanceof Maneuver) && ((Maneuver)FM).get_maneuver() != 44)
                {
                    if(FM.AS.actor != World.getPlayerAircraft())
                        ((Maneuver)FM).set_maneuver(44);
                }
                doRemoveBodyFromPlane(byte0 - 10);
                if(FM.getSpeedKMH() > 15F)
                {
                    if(byte0 == 11 || (byte0 == 12 && bTwoSeat))
                        doEjectCatapult(byte0 - 10);
                    if(byte0 == 11)
                        lTimeNextEject = Time.current() + 1000L;
                    if (!bTwoSeat || byte0 > 11)
                    {
                        FM.AS.astateBailoutStep = -1;
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
                              Hook localHook = findHook("_ExternalBail0" + (byte0 - 10));

                              if (localHook != null)
                              {
                                  Loc localLoc = new Loc(0.0D, 0.0D, 0.0D, World.Rnd().nextFloat(-45.0F, 45.0F), 0.0F, 0.0F);

                                  localHook.computePos(this, pos.getAbs(), localLoc);

                                  new Paratrooper(this, getArmy(), byte0 - 11, localLoc, FM.Vwld);

                                  if ((byte0 > 10) && (byte0 <= 19))
                                  {
                                      EventLog.onBailedOut(this, byte0 - 11);
                                  }
                              }
                            }
                            catch (Exception localException)
                            {
                            }
                            finally
                            {
                            }
                            if ((FM.AS.astatePilotStates[byte0 - 11] == 19) && (this == World.getPlayerAircraft()) && (!World.isPlayerGunner()) && (FM.brakeShoe))
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
                        if(!bTwoSeat || byte0 > 11)
                        {
                            FM.AS.astateBailoutStep = -1;
                            overrideBailout = false;
                            FM.AS.bIsAboutToBailout = true;
                            ejectComplete = true;
                            if ((this == World.getPlayerAircraft()) && (!World.isPlayerGunner()) && (FM.brakeShoe))
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

    public void doEjectCatapult(final int i)
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
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 40D - (double) i * 7D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat0" + i);
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(6, loc, vector3d, aircraft);
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

    public void update(float f)
    {
        computeEngine();
        computeSlat();
        if(!bNoSpoiler)
            checkSpolers(f);
        if(lastUpdateTime != Time.current())
        {
            computeLift();
            if(Config.isUSE_RENDER() && FM.AS.isMaster())
                if(FM.EI.engines[0].getPowerOutput() > 0.8F && FM.EI.engines[0].getStage() == 6)
                {
                    if(FM.EI.engines[0].getPowerOutput() > 0.95F)
                        FM.AS.setSootState(this, 0, 3);
                    else
                        FM.AS.setSootState(this, 0, 2);
                } else
                {
                    FM.AS.setSootState(this, 0, 0);
                }
            if(calculateMach() < 0.780F)
                critSpeed = 0.0F;
            if(FM.getAltitude() > 0.0F && calculateMach() >= 0.935F && FM.isTick(44, 0))  //  speed limiter, added by Vega
                FM.Sq.dragParasiteCx += 0.002F;
            if((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete)
            {
                overrideBailout = true;
                FM.AS.bIsAboutToBailout = false;
                if(Time.current() > lTimeNextEject)
                    bailout();
            }
            soundbarier();
            if(bHasRWR)
            {
                rwrUtils.update();
                backfire = rwrUtils.getBackfire();
                bRadarWarning = rwrUtils.getRadarLockedWarning();
                bMissileWarning = rwrUtils.getMissileWarning();
            }
            if(FM.CT.getArrestor() > 0.2F)
                calculateArrestor();
            lastUpdateTime = Time.current();
        }
        HydroGearCounter(f);
        super.update(f);
    }

    public void computeSlat()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");

        if(FM.getSpeedKMH() < 300F || FM.getAOA() > 5.8F)
            iSlatControl = 1;
        if(FM.getSpeedKMH() > 330F && FM.getAOA() < 5.1F)
            iSlatControl = 0;

        if(FM.getSpeedKMH() < 70F)
            fSlat = 1.0F;
        else if(Math.abs(fSlat - (float)iSlatControl) < 0.02F)
            fSlat = (float)iSlatControl;
        else
            fSlat = (fSlat * 24F + (float)iSlatControl) / 25F;

        FM.Sq.squareWing = stockSquareWing + SquareSlat * 2F * fSlat;
        FM.Sq.liftWingLMid = stockLiftWingLMid + SquareSlat * fSlat;
        FM.Sq.liftWingRMid = stockLiftWingRMid + SquareSlat * fSlat;
        polares.Cy0_0 = (stockCy0 + Cy0AddSlat * fSlat) * (1.0F - spoilerBrake * 0.9F);
        polares.Cy0_1 = stockCy1 * (1.0F - spoilerBrake * 0.8F);

        resetYPRmodifier();
        xyz[0] = -0.008F * Aircraft.cvt(fSlat, 0.4F, 1.0F, 0.0F, 1.0F);
        xyz[1] = -0.297F * fSlat;
        ypr[0] = -29.06F * fSlat;
        hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        xyz[0] = -0.008F * Aircraft.cvt(fSlat, 0.4F, 1.0F, 0.0F, 1.0F);
        xyz[1] = 0.297F * fSlat;
        ypr[0] = 29.06F * fSlat;
        hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void computeLift() // added by Vega instead old method computeCy
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

    public void computeEngine()
    {
//     broken original code do nothing, by bad decompiled
//        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() > 5)
//            if(calculateMach() >= 0.0F);
        if(FM.EI.engines[0].getStage() < 6 || calculateMach() < 0.0F)
            return;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(f > 13.5F)
        {
            f1 = 11F;
        } else
        {
            float f2 = f * f;
            float f3 = f2 * f;
            float f4 = f3 * f;
            f1 = ((134F * f3 - 2381F * f2) + 14946F * f) / 22680F;
        }
        FM.producedAF.x -= f1 * 1000F;
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log("Autopilot Mode: Altitude ON");
                FM.AP.setStabAltitude(FM.getAltitude());
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
    }

    private void HydroGearCounter(float f)
    {
        if(FM.EI.engines[0].getStage() < 6 && FM.getSpeedKMH() < 160F)
        {
            if(FM.Gears.nOfGearsOnGr > 0)
            {
                FM.CT.bHasFlapsControl = false;
                FM.CT.bHasAileronControl = false;
                FM.CT.bHasElevatorControl = false;
                FM.CT.bHasAirBrakeControl = false;
                FM.CT.bHasRudderControl = true;
                FM.CT.bHasGearControl = false;
                isGeneratorAlive = false;
                isHydraulicAlive = false;
                if(lLastHydraulicLost == -1L)
                    lLastHydraulicLost = Time.current();
                lLastHydraulicGot = -1L;
            } else
            if(FM.Gears.nOfGearsOnGr == 0)
            {
                timeCounterHyd9 += f;
                if(timeCounterHyd9 > timeHydro9)
                {
                    timeHydro9 = 0.0F;
                    FM.CT.bHasFlapsControl = false;
                    FM.CT.bHasAileronControl = false;
                    FM.CT.bHasElevatorControl = false;
                    FM.CT.bHasAirBrakeControl = false;
                    FM.CT.bHasRudderControl = false;
                    FM.CT.bHasGearControl = false;
                    isGeneratorAlive = false;
                    isHydraulicAlive = false;
                    if(lLastHydraulicLost == -1L)
                        lLastHydraulicLost = Time.current();
                    lLastHydraulicGot = -1L;
                }
            }
        } else
        {
            timeCounterHyd9 = 0.0F;
            FM.CT.bHasFlapsControl = true;
            FM.CT.bHasAileronControl = true;
            FM.CT.bHasElevatorControl = true;
            FM.CT.bHasAirBrakeControl = true;
            FM.CT.bHasRudderControl = true;
            FM.CT.bHasGearControl = true;
            isGeneratorAlive = true;
            isHydraulicAlive = true;
            lLastHydraulicLost = -1L;
            if(lLastHydraulicGot == -1L)
                lLastHydraulicGot = Time.current();
        }
        updateFlapsAndAirbrakesVisuals();

        if(FM.EI.engines[0].getStage() == 6 && FM.getSpeedKMH() < 5F && FM.Loc.z - Engine.land().HQ_Air(FM.Loc.x, FM.Loc.y) < 40D && FM.Gears.nOfGearsOnGr < 3 && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
        {
            timeCounterCrash9 += f;
            if(timeCounterCrash9 > timeCrash9)
            {
                timeCounterCrash9 = 0.0F;
                FM.AS.hitEngine(this, 0, 100);
                FM.EI.engines[0].setEngineDies(this);
            }
        } else
        {
            timeCounterCrash9 = 0.0F;
        }
        if(FM.Gears.nOfGearsOnGr > 0 && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
            if(FM.CT.bHasBrakeControl && FM.CT.BrakeControl > 0.2F)
            {
                if(FM.getSpeedKMH() >= 180F)
                {
                    timeCounterBrake9 += f;
                    if(timeCounterBrake9 > timeBrake19)
                    {
                        FM.CT.BrakeControl = 0.0F;
                        FM.CT.bHasBrakeControl = false;
                    }
                } else
                if(FM.getSpeedKMH() < 180F && FM.getSpeedKMH() > 10F)
                {
                    timeCounterBrake9 += f;
                    if(timeCounterBrake9 > timeBrake29)
                    {
                        FM.CT.BrakeControl = 0.0F;
                        FM.CT.bHasBrakeControl = false;
                    }
                }
            } else
            {
                timeCounterBrake9 = 0.0F;
            }
        if(!FM.CT.bHasBrakeControl && (FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode())
        {
            timeCounterBrake29 += f;
            if(timeCounterBrake29 > timeBrake39)
                FM.CT.bHasBrakeControl = true;
        } else
        {
            timeCounterBrake29 = 0.0F;
        }
    }

    private void calculateArrestor()
    {
        if(FM.Gears.arrestorVAngle != 0.0F)
        {
            float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
            arrestor = 0.8F * arrestor + 0.2F * f1;
            moveArrestorHook(arrestor);
            if(FM.Gears.arrestorVAngle < -0.2F)// && FM.Gears.nOfGearsOnGr >= 1)
            {
                //By PAL, to Stabilize plane on landing when catched by arrestor cables
                Orient or = new Orient();
                FM.actor.pos.getAbs(or);
                float fRoll = or.getRoll() - 360.0F;
                float fPitch = or.getPitch() - 360.0F;
                if(fRoll > -20F && fRoll < 20F)
                    FM.producedAM.x += 6000F * fRoll;
                if(fPitch > -20F && fPitch < 20F)
                    FM.producedAM.y += 4500F * fPitch;
            }
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
                        antiColLight[i] = Eff3DActor.New(this, findHook("_AntiColLight" + Integer.toString(i + 1)), new Loc(), 1.0F, "3DO/Effects/Fireworks/FlareRedFlash_" + String.valueOf(postfix) + ".eff", -1.0F, false);
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

    public static void forceParkingStyle(HierMesh hiermesh) {
        // flaps parking position without Hydraulic pressuer.
        hiermesh.chunkSetAngles("Flap01_D0", 0.0F, -45F, 0.0F);
        hiermesh.chunkSetAngles("Flap02_D0", 0.0F, -45F, 0.0F);

        // fuselage airbrakes parking position without Hydraulic pressuer.
        hiermesh.chunkSetAngles("FuBrake01_D0", -4F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("FuBrake02_D0", 4F, 0.0F, 0.0F);

        // slats parking position.
        xyz[0] = -0.008F;
        xyz[1] = -0.297F;
        xyz[2] = 0.0F;
        ypr[0] = -29.06F;
        ypr[1] = 0.0F;
        ypr[2] = 0.0F;
        hiermesh.chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
        xyz[0] = -0.008F;
        xyz[1] = 0.297F;
        xyz[2] = 0.0F;
        ypr[0] = 29.06F;
        ypr[1] = 0.0F;
        ypr[2] = 0.0F;
        hiermesh.chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
    }

    private float filter(float f, float f1, float f2, float f3, float f4) {
        float f5 = (float)Math.exp(-f / f3);
        float f6 = f1 + (f2 - f1) * f5;
        if (f6 < f1)
        {
            f6 += f4 * f;
            if (f6 > f1)
                f6 = f1;
        }
        else if (f6 > f1)
        {
            f6 -= f4 * f;
            if (f6 < f1)
                f6 = f1;
        }
        return f6;
    }

    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    Actor hunted = null;

    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;

    //By PAL
    private float arrestor;
    //By PAL, fSteer added
    private float fSteer;
    private static final float maxSteer = 45.0F;
    //By PAL; from Vega
    public boolean APmode1;
    public boolean APmode2;
    //By PAL, added for type FuelDump
    public static float FlowRate = 8.5F;
    public static float FuelReserve = 1000F;
    //By PAL, for Hydraulics
    private float timeCounterRud9;
    private float timeCounterHyd9;
    private float timeCounterBoost;
    private float timeBoost;
    private float timeRudder9;
    private float timeHydro9;
    private float timeCounterCrash9;
    private float timeCrash9;
    private float timeBrake19;
    private float timeBrake29;
    private float timeBrake39;
    private float timeCounterBrake9;
    private float timeCounterBrake29;
    public boolean isHydraulicAlive;
    public boolean isGeneratorAlive;
    public boolean isBatteryOn;
    private long lLastHydraulicLost;
    private long lLastHydraulicGot;
    private long lastUpdateTime;
    private long lastRareActionTime;

    private float critSpeed;

    // for AGM-12 remote control
    private long tX4Prev;
    private float deltaAzimuth;
    private float deltaTangage;

    private boolean overrideBailout;
    private boolean ejectComplete;
    private long lTimeNextEject;

    // for Two seat (TA-4, OA-4) variants and observer looking around
    private int obsLookTime;
    private float obsLookAzimuth;
    private float obsLookElevation;
    private float obsAzimuth;
    private float obsElevation;
    private float obsAzimuthOld;
    private float obsElevationOld;
    private float obsMove;
    private float obsMoveTot;
    public boolean bTwoSeat;
    private boolean bObserverKilled;

    //By western0221, Anti collision light
    private Eff3DActor antiColLight[];
    private boolean oldAntiColLight;

    //By western0221, classfy has Radar Warning Receiver (A-4E or later)
    private boolean bHasRWR;
    private RadarWarningReceiverUtils rwrUtils;
    public boolean bRadarWarning;
    public boolean bMissileWarning;
    public float misslebrg;
    public float aircraftbrg;
    public boolean backfire;

    private static final int RWR_GENERATION = 0;
    private static final int RWR_MAX_DETECT = 8;
    private static final int RWR_KEEP_SECONDS = 2;
    private static final double RWR_RECEIVE_ELEVATION = 45.0D;
    private static final boolean RWR_DETECT_IRMIS = false;
    private static final boolean RWR_DETECT_ELEVATION = false;
    private boolean bRWR_Show_Text_Warning = true;

    //By western0221, classfy working ground spoiler (A-4F or later) or not (A-4E or earlier)
    public boolean bNoSpoiler;
    private float stockDragAirbrake;
    private float SpoilerBrakeControl;
    private float spoilerBrake;

    //By western0221, slat value: 0.00=close, 1.00=full down
    private float fSlat;
    private int iSlatControl;
    private float stockSquareWing;
    private float stockLiftWingLMid;
    private float stockLiftWingRMid;
    private final float SquareSlat = 1.08F;
    private float stockCy0;
    private float stockCy1;
    private final float Cy0AddSlat = 0.002F;

    // G Suit values
    private static final float NEG_G_TOLERANCE_FACTOR = 1F;
    private static final float NEG_G_TIME_FACTOR = 1F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 4.5F;
    private static final float POS_G_TIME_FACTOR = 1.5F;
    private static final float POS_G_RECOVERY_FACTOR = 1F;

    static
    {
        Class class1 = CLASS.THIS();
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}