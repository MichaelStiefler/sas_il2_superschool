
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapExt;
import com.maddox.sas1946.il2.util.Reflection;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;


public class AV_8 extends Scheme1
    implements TypeFighter, TypeFighterAceMaker, TypeBomber, TypeStormovikArmored, TypeGSuit, TypeSupersonic, TypeFastJet, TypeFuelDump
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

    public AV_8()
    {
        lLightHook = new Hook[4];
        SonicBoom = 0.0F;
        k14Mode = 2;
        k14WingspanType = 0;
        k14Distance = 200F;
        overrideBailout = false;
        ejectComplete = false;
        lightTime = 0.0F;
        ft = 0.0F;
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 3000F;
        fSightCurSpeed = 200F;
        fSightCurReadyness = 0.0F;
        gearTargetAngle = -1F;
        gearCurrentAngle = -1F;
        vectorthrustx = 0.0F;
        vectorthrustz = 0.0F;
        Bingofuel = 1000;
        Nvision = false;
        bDynamoOperational = true;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        APmode1 = false;
        antiColLight = new Eff3DActor[6];
        oldAntiColLight = false;
        isHydraulicAlive = false;
        isGeneratorAlive = false;
        isBatteryOn = false;
        vtolSlipX = 0;
        vtolSlipY = 0;
        lastUpdateTime = -1L;
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

    public void auxPressed(int i)
    {
        super.auxPressed(i);
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
        if(i == 25)
        {
            Bingofuel += 500;
            if(Bingofuel > 6000)
                Bingofuel = 1000;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Bingofuel " + Bingofuel);
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
    }

    public boolean typeBomberToggleAutomation()
    {
        return true;
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR, POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.AS.wantBeaconsNet(true);
        FM.CT.bHasBombSelect = true;
        FM.CT.bHasAntiColLights = true;
        FM.CT.bHasFormationLights = true;
        if(thisWeaponsName.endsWith("_NoGun"))
        {
            hierMesh().chunkVisible("CF1_D0", false);
            hierMesh().chunkVisible("CF1nogun_D0", true);
        }
        else
        {
            hierMesh().chunkVisible("CF1_D0", true);
            hierMesh().chunkVisible("CF1nogun_D0", false);
        }
    }

    public void missionStarting()
    {
        super.missionStarting();
//        checkDroptanks();
    }

    public void checkHydraulicStatus()
    {
        if(FM.EI.engines[0].getStage() < 6 && FM.Gears.nOfGearsOnGr > 0)
        {
            gearTargetAngle = 90F;
            isHydraulicAlive = false;
            FM.CT.bHasAileronControl = false;
            FM.CT.bHasElevatorControl = false;
            FM.CT.AirBrakeControl = 0.0F;
        }
        else if(!isHydraulicAlive)
        {
            gearTargetAngle = 0.0F;
            isHydraulicAlive = true;
            FM.CT.bHasAileronControl = true;
            FM.CT.bHasElevatorControl = true;
            FM.CT.bHasAirBrakeControl = true;
        }
    }

    public void moveHydraulics(float f)
    {
        if(gearTargetAngle >= 0.0F)
            if(gearCurrentAngle < gearTargetAngle)
            {
                gearCurrentAngle += 90F * f * 0.8F;
                if(gearCurrentAngle >= gearTargetAngle)
                {
                    gearCurrentAngle = gearTargetAngle;
                    gearTargetAngle = -1F;
                }
            }
            else
            {
                gearCurrentAngle -= 90F * f * 0.8F;
                if(gearCurrentAngle <= gearTargetAngle)
                {
                    gearCurrentAngle = gearTargetAngle;
                    gearTargetAngle = -1F;
                }
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
        if(FM.EI.engines[0].getRPM() > 200F || FM.getSpeedKMH() > 160F)
        {
            isGeneratorAlive = true;
            isHydraulicAlive = true;
        }
        else
        {
            isGeneratorAlive = false;
            isHydraulicAlive = false;
        }
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            ft = World.getTimeofDay() % 0.01F;
            if(ft == 0.0F)
                UpdateLightIntensity();
        }
        if(FM.Gears.onGround() && FM.CT.getCockpitDoor() == 1.0F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
        if((!FM.isPlayers() || !(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver))
            AIAirBrake();
        if(!FM.isPlayers() && isGeneratorAlive)
            if(((Maneuver)super.FM).get_maneuver() == 21 && FM.AP.way.isLanding())
                FM.CT.FlapsControlSwitch = 2;
            else if(((Maneuver)super.FM).get_maneuver() == 25 || ((Maneuver)super.FM).get_maneuver() == 26)
                FM.CT.FlapsControlSwitch = 2;
            else if(calculateMach() > 0.7F || FM.getSpeedKMH() > 680F)
                FM.CT.FlapsControlSwitch = 0;
            else
                FM.CT.FlapsControlSwitch = 1;
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
            k14Distance = (float)(FM.actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint()));
            if(k14Distance > 800F)
                k14Distance = 800F;
            else if(k14Distance < 200F)
                k14Distance = 200F;
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
        }
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
                    if(shot.power <= -7F)
                        doRicochetBack(shot);
                }
                else if(s.endsWith("p2"))
                    getEnergyPastArmor(8.770001F, shot);
                else if(s.endsWith("g1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(40F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    if(shot.power <= -7F)
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
                    if(World.Rnd().nextFloat() < -10.5F && getEnergyPastArmor(1.1F, shot) > 200F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3: // '\003'
                case 4: // '\004'
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 200F && World.Rnd().nextFloat() < -10.25F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 200F && World.Rnd().nextFloat() < -10.25F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;
                }
            }
            else if(s.startsWith("xxengine1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 20F)
                {
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 5800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 44000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 44000F)
                {
                    FM.AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                s.endsWith("exht");
            }
            else if(s.startsWith("xxmgun0"))
            {
                int k = s.charAt(7) - 49;
                if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                {
                    debuggunnery("Armament: mnine Gun (" + k + ") Disabled..");
                    FM.AS.setJamBullets(0, k);
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
            else if(s.startsWith("xnose"))
            {
                if(chunkDamageVisible("Nose") < 2)
                    hitChunk("Nose", shot);
                if(chunkDamageVisible("Tail2") < 2)
                    hitChunk("Tail2", shot);
            }
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
            else if(s.startsWith("xrudder"))
                hitChunk("Rudder1", shot);
            else if(s.startsWith("xvator"))
            {
                if(s.startsWith("xvatorl"))
                    hitChunk("VatorL", shot);
                if(s.startsWith("xvatorr"))
                    hitChunk("VatorR", shot);
            }
            else if(s.startsWith("xwing"))
            {
                if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 2)
                    hitChunk("WingLIn", shot);
                if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 2)
                    hitChunk("WingRIn", shot);
                if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                {
                    hitChunk("WingLMid", shot);
                    hitChunk("Flap1", shot);
                    hitChunk("Flap12", shot);
                }
                if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                {
                    hitChunk("WingRMid", shot);
                    hitChunk("Flap2", shot);
                    hitChunk("Flap22", shot);
                }
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
            float f = World.Rnd().nextFloat();
            if(f < 0.1F)
                FM.AS.hitEngine(this, 0, 100);
            FM.EI.engines[0].setEngineDies(actor);

            break;

        case 34: // '"'
            FM.Gears.lgear = false;
            break;

        case 37: // '%'
            FM.Gears.rgear = false;
            break;

        case 19: // '\023'
            FM.CT.bHasAirBrakeControl = false;
            FM.EI.engines[0].setEngineDies(actor);
            break;

        case 11: // '\013'
            FM.CT.bHasElevatorControl = false;
            FM.CT.bHasRudderControl = false;
            FM.CT.bHasRudderTrim = false;
            FM.CT.bHasElevatorTrim = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.2F, 1.0F, 0.0F, 0.6F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    // no more needed 4.12.2m or later games, but keep backward compatibility
    public static void moveGear(HierMesh hiermesh, float f)
    {
        moveGear(hiermesh, f, f, f);
    }

    public static void moveGear(HierMesh hiermesh, float fgl, float fgr, float fgc)
    {
        float fy = fgc > 0.5F ? Aircraft.cvt(fgc, 0.8F, 1.0F, 90F, 0.0F) : Aircraft.cvt(fgc, 0.01F, 0.11F, 0.0F, 90F);
        float fx = fgc > 0.5F ? Aircraft.cvt(fgc, 0.8F, 1.0F, 6F, 0.0F) : Aircraft.cvt(fgc, 0.01F, 0.11F, 0.0F, 6F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, Aircraft.cvt(fgc, 0.3F, 0.8F, 0.0F, -95F));
        hiermesh.chunkSetAngles("GearC7_D0", fx, -fy, 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", -fx, fy, 0.0F);
        hiermesh.chunkSetAngles("GearC9_D0", 0.0F, Aircraft.cvt(fgc, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearC10_D0", 0.0F, Aircraft.cvt(fgc, 0.01F, 0.11F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC11_D0", 0.0F, 0.0F, Aircraft.cvt(fgc, 0.0F, 0.8F, 0.0F, -90F));

        hiermesh.chunkSetAngles("GearB2_D0", 0.0F, 0.0F, Aircraft.cvt(fgc, 0.0F, 0.7F, 0.0F, 115F));
        hiermesh.chunkSetAngles("GearB5_D0", 0.0F, Aircraft.cvt(fgc, 0.0F, 0.7F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearB6_D0", 0.0F, Aircraft.cvt(fgc, 0.0F, 0.7F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearB7_D0", 0.0F, fy, 0.0F);
        hiermesh.chunkSetAngles("GearB8_D0", 0.0F, fy, 0.0F);

        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, Aircraft.cvt(fgl, 0.0F, 0.8F, 0.0F, 85F));
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, Aircraft.cvt(fgr, 0.0F, 0.8F, 0.0F, 85F));
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(fgl, 0.0F, 0.8F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(fgr, 0.0F, 0.8F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(fgl, 0.0F, 0.8F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(fgr, 0.0F, 0.8F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 0.0F, Aircraft.cvt(fgl, 0.0F, 0.8F, 0.0F, 45F));
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 0.0F, Aircraft.cvt(fgr, 0.0F, 0.8F, 0.0F, 45F));
    }

    // no more needed 4.12.2m or later games, but keep backward compatibility
    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    protected void moveGear(float fgl, float fgr, float fgc)
    {
        moveGear(hierMesh(), fgl, fgr, fgc);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        float f = FM.Gears.gWheelSinking[2];
        Aircraft.xyz[2] = -f;
        hierMesh().chunkSetLocate("GearC211_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -20F * f, 0.0F);
        resetYPRmodifier();
        f = FM.Gears.gWheelSinking[0] + FM.Gears.gWheelSinking[1];
        Aircraft.xyz[1] = -f / 2.0F;
        hierMesh().chunkSetLocate("GearB21_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("GearB3_D0", 0.0F, 20F * f, 0.0F);
    }

    protected void moveFan(float f)
    {
        if(bDynamoOperational)
        {
            pk = Math.abs((int)(FM.Vwld.length() / 14D));
            if(pk >= 1)
                pk = 1;
        }
        if(bDynamoRotary != (pk == 1))
        {
            bDynamoRotary = pk == 1;
            hierMesh().chunkVisible("Prop1_D0", !bDynamoRotary);
            hierMesh().chunkVisible("PropRot1_D0", bDynamoRotary);
        }
        dynamoOrient = bDynamoRotary ? (dynamoOrient - 17.987F) % 360F : (float)((double)dynamoOrient - FM.Vwld.length() * 1.5444015264511111D) % 360F;
        hierMesh().chunkSetAngles("Prop1_D0", 0.0F, dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder_D0", 30F * f, 0.0F, 0.0F);
    }

    public void moveSteering(float f)
    {
        if(FM.CT.GearControl > 0.5F && FM.Gears.onGround())
            hierMesh().chunkSetAngles("GearC21_D0", -1.2F * f, 0.0F, 0.0F);
        if(FM.CT.GearControl < 0.5F)
            hierMesh().chunkSetAngles("GearC21_D0", 0.0F, 0.0F, 0.0F);
    }

    protected void moveElevator(float f)
    {
        updateControlsVisuals();
    }

    private final void updateControlsVisuals()
    {
        if(FM.getSpeedKMH() > 590F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -30F * FM.CT.getElevator() + 17F * FM.CT.getAileron());
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -30F * FM.CT.getElevator() - 17F * FM.CT.getAileron());
        }
        else
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -17F * FM.CT.getElevator() + 10F * FM.CT.getAileron());
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -17F * FM.CT.getElevator() - 10F * FM.CT.getAileron());
        }
    }

    protected void moveAileron(float f)
    {
        updateControlsVisuals();
        if(bUseDroopAileron)
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 15F);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, 15F);
        }
        else if(FM.getSpeedKMH() > 570F)
        {
            float f1 = 2.0F * f;
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 20F * f1);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -20F * f1);
        }
        else
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 20F * f);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -20F * f);
        }
    }

    protected void moveFlap(float f)
    {
        float f1 = 35F * f;
        float f2 = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 18F);
        hierMesh().chunkSetAngles("Flap1_D0", 0.0F, 0.0F, f1);
        hierMesh().chunkSetAngles("Flap2_D0", 0.0F, 0.0F, f1);
        hierMesh().chunkSetAngles("Flap12_D0", 0.0F, 0.0F, f2);
        hierMesh().chunkSetAngles("Flap22_D0", 0.0F, 0.0F, f2);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 0.8F, 0.0F, 0.2F);
        hierMesh().chunkSetLocate("Flap21_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("Flap11_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveAirBrake(float f)
    {
        float f1 = FM.CT.GearControl <= 0.5F ? 50F : 25F;
        hierMesh().chunkSetAngles("Airbrake_D0", 0.0F, 0.0F, f1 * f);
        hierMesh().chunkSetAngles("Airbrake2_D0", 0.0F, 0.0F, -70F * f);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.14F);
        hierMesh().chunkSetLocate("Airbrake21_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveRefuel(float f)
    {
        resetYPRmodifier();
        hierMesh().chunkSetAngles("Refillrod1", Aircraft.cvt(f, 0.0F, 0.5F, 0.0F, 15F), 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Refillrod3", 0.0F, Aircraft.cvt(f, 0.0F, 0.5F, 0.0F, 75F), 0.0F);
        hierMesh().chunkSetAngles("Refillrod4", 0.0F, Aircraft.cvt(f, 0.0F, 0.5F, 0.0F, -165F), 0.0F);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.9F);
        hierMesh().chunkSetLocate("Refillrod2", Aircraft.xyz, Aircraft.ypr);
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
        if(calculateMach() > 1.0F)
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
            for(int i = 0; i < 1; i++)
            {
                if(curthrl[i] == -1F)
                {
                    curthrl[i] = oldthrl[i] = FM.EI.engines[i].getControlThrottle();
                }
                else
                {
                    curthrl[i] = FM.EI.engines[i].getControlThrottle();
                    if(curthrl[i] < 1.05F)
                    {
                        if((curthrl[i] - oldthrl[i]) / f > 20F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                        {
                            if(this == World.getPlayerAircraft())
                                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage[i] += 0.01F * (FM.EI.engines[i].getRPM() / 1000F);
                            FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage[i]);
                            if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                                FM.AS.hitEngine(this, i, 100);
                            if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                                FM.EI.engines[i].setEngineDies(this);
                        }
                        if((curthrl[i] - oldthrl[i]) / f < -20F && (curthrl[i] - oldthrl[i]) / f > -100F && FM.EI.engines[i].getRPM() < 3200F && FM.EI.engines[i].getStage() == 6)
                        {
                            super.playSound("weapon.MGunMk108s", true);
                            engineSurgeDamage[i] += 0.001F * (FM.EI.engines[i].getRPM() / 1000F);
                            FM.EI.engines[i].doSetReadyness(FM.EI.engines[i].getReadyness() - engineSurgeDamage[i]);
                            if(World.Rnd().nextFloat() < 0.4F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
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
            FM.CT.forceFlaps(flapsMovement(f, FM.CT.FlapsControl, FM.CT.getFlap(), 999F, Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.08F)));
        else
            FM.CT.forceFlaps(flapsMovement(f, FM.CT.FlapsControl, FM.CT.getFlap(), 999F, Aircraft.cvt(FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.7F)));
        if(lastUpdateTime != Time.current())
        {
            if((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && FM.getSpeedKMH() > 15F)
            {
                overrideBailout = true;
                FM.AS.bIsAboutToBailout = false;
                bailout();
            }
            float f2 = FM.getSpeedKMH() - 1000F;
            if(f2 < 0.0F)
                f2 = 0.0F;
            FM.CT.dvGear = 0.2F - f2 / 1000F;
            if(FM.CT.dvGear < 0.0F)
                FM.CT.dvGear = 0.0F;
            if((!FM.isPlayers() || !(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver))
            {
                if(FM.AP.way.isLanding() && FM.Gears.onGround() && FM.getSpeed() > 40F)
                {
                    FM.CT.AirBrakeControl = 1.0F;
                }
                if(FM.AP.way.isLanding() && FM.Gears.onGround() && FM.getSpeed() < 40F)
                {
                    FM.CT.AirBrakeControl = 0.0F;
                }
            }
            if(FM.AS.isMaster() && Config.isUSE_RENDER())
            {
                for(int i = 0; i < 1; i++)
                    if(FM.EI.engines[i].getPowerOutput() > 0.25F && FM.EI.engines[i].getStage() == 6)
                    {
                        if(FM.EI.engines[i].getPowerOutput() > 0.85F)
                        {
                            if(nozzlemode == 1)
                                FM.AS.setSootState(this, i, 5);
                            else
                                FM.AS.setSootState(this, i, 6);
                        }
                        else if(FM.EI.engines[i].getPowerOutput() > 0.55F && FM.EI.engines[i].getPowerOutput() < 0.85F)
                        {
                            if(nozzlemode == 1)
                                FM.AS.setSootState(this, i, 3);
                            else
                                FM.AS.setSootState(this, i, 4);
                        }
                        else
                        {
                            FM.AS.setSootState(this, i, 2);
                        }
                    }
                    else
                    {
                        FM.AS.setSootState(this, i, 0);
                    }
            }
            typeFighterAceMakerRangeFinder();
            checkHydraulicStatus();
            soundbarier();
            computeLift();
        }
        engineSurge(f);
        moveHydraulics(f);
        computeLimiter();
        computeVerticalThrust();

        super.update(f);

        if(lastUpdateTime != Time.current())
        {
            pullingvapor();
            if(FM.getSpeed() > 300F)
                FM.CT.cockpitDoorControl = 0.0F;
            for(int i = 1; i < 15; i++)
            {
                if(FM.EI.engines[0].getStage() > 0)
                    hierMesh().chunkSetAngles("Eflap" + i, 0.0F, 0.0F, Aircraft.cvt(150F - FM.getSpeedKMH(), -200F, 0.0F, 0.0F, 30F));
                else
                    hierMesh().chunkSetAngles("Eflap" + i, 0.0F, 0.0F, 0.0F);
            }
            formationlights();
            if(!FM.isPlayers())
                FM.CT.bAntiColLights = FM.AS.bNavLightsOn;
            anticollights();
            lastUpdateTime = Time.current();
        }

        VTOL();
        computeflightmodel();
    }

    private void computeflightmodel()
    {
        FM.setGCenter(Aircraft.cvt(FM.getSpeedKMH(), 480F, 940F, 0.00F, 2.50F));
        if(FM.CT.FlapsControlSwitch == 0)
        {
            FM.CT.FlapsControl = 5F / FM.CT.FlapStageMax;
            bUseDroopAileron = false;
        }
        else if(FM.CT.FlapsControlSwitch == 1)
        {
            if(FM.CT.getGear() < 0.01F)
            {
                float fl = Aircraft.cvt(FM.getAOA(), 5F, 15F, 5F, 25F);
                float limitmach = Aircraft.cvt(calculateMach(), 5.5F, 9.5F, 25F, 0.0F);
                float limitTAS = Aircraft.cvt(FM.getSpeedKMH(), 557F, 1002F, 25F, 5F);
                if(fl > limitmach)
                    fl = limitmach;
                if(fl > limitTAS)
                    fl = limitTAS;
                FM.CT.FlapsControl = fl / FM.CT.FlapStageMax;
                bUseDroopAileron = false;
            }
            else
            {
                FM.CT.FlapsControl = 25F / FM.CT.FlapStageMax;
                bUseDroopAileron = false;
            }
        }
        else if(FM.CT.FlapsControlSwitch == 2)
            if(FM.Gears.nOfGearsOnGr > 0 || FM.getSpeedKMH() < 306F)
            {
                if(FM.CT.VarWingControl < 25F / FM.CT.VarWingStageMax)
                {
                    FM.CT.FlapsControl = 25F / FM.CT.FlapStageMax;
                    bUseDroopAileron = FM.Gears.nOfGearsOnGr > 0;
                }
                else
                {
                    FM.CT.FlapsControl = Aircraft.cvt(FM.CT.VarWingControl * FM.CT.VarWingStageMax, 25F, 50F, 25F, FM.CT.FlapStageMax) / FM.CT.FlapStageMax;
                    bUseDroopAileron = true;
                }
            }
            else
            {
                FM.CT.FlapsControl = 25F / FM.CT.FlapStageMax;
                bUseDroopAileron = false;
            }
        if(bUseDroopAileron != oldbUseDroopAileron)
        {
            moveAileron(FM.CT.getAileron());
            oldbUseDroopAileron = bUseDroopAileron;
        }
    }

    private void pullingvapor()
    {
        if(FM.getSpeed() > 7F && World.Rnd().nextFloat() < getAirDensityFactor(FM.getAltitude()))
        {
            if(FM.getOverload() > 6.5F + getAirDensityFactor(FM.getAltitude()) * 0.10F)
            {
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.1F)
                    pull1 = Eff3DActor.New(this, findHook("_Pull1"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull1);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.1F)
                    pull2 = Eff3DActor.New(this, findHook("_Pull2"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull2);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.6F)
                    pull3 = Eff3DActor.New(this, findHook("_Pull3"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull3);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.3F)
                    pull4 = Eff3DActor.New(this, findHook("_Pull4"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull4);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.3F)
                    pull5 = Eff3DActor.New(this, findHook("_Pull5"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull5);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.4F)
                    pull6 = Eff3DActor.New(this, findHook("_Pull6"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull6);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.5F)
                    pull7 = Eff3DActor.New(this, findHook("_Pull7"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull7);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.6F)
                    pull8 = Eff3DActor.New(this, findHook("_Pull8"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull8);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.4F)
                    pull9 = Eff3DActor.New(this, findHook("_Pull9"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull9);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.3F)
                    pull10 = Eff3DActor.New(this, findHook("_Pull10"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull10);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.2F)
                    pull11 = Eff3DActor.New(this, findHook("_Pull11"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull11);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.1F)
                    pull12 = Eff3DActor.New(this, findHook("_Pull12"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull12);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.1F)
                    pull13 = Eff3DActor.New(this, findHook("_Pull13"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull13);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.1F)
                    pull14 = Eff3DActor.New(this, findHook("_Pull14"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull14);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.1F)
                    pull15 = Eff3DActor.New(this, findHook("_Pull15"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull15);
                if(World.Rnd().nextFloat(0.1F, 1.5F) > 0.1F)
                    pull16 = Eff3DActor.New(this, findHook("_Pull16"), null, 1.0F, "3DO/Effects/Aircraft/Pullingvaporyak.eff", -1F);
                else
                    Eff3DActor.finish(pull16);
            }
            if(FM.getOverload() <= 6.5F)
            {
                Eff3DActor.finish(pull1);
                Eff3DActor.finish(pull2);
                Eff3DActor.finish(pull3);
                Eff3DActor.finish(pull4);
                Eff3DActor.finish(pull5);
                Eff3DActor.finish(pull6);
                Eff3DActor.finish(pull7);
                Eff3DActor.finish(pull8);
                Eff3DActor.finish(pull9);
                Eff3DActor.finish(pull10);
                Eff3DActor.finish(pull11);
                Eff3DActor.finish(pull12);
                Eff3DActor.finish(pull13);
                Eff3DActor.finish(pull14);
                Eff3DActor.finish(pull15);
                Eff3DActor.finish(pull16);
            }
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
        int ws = Mission.cur().curCloudsType();
        float we = Mission.cur().curCloudsHeight() + 500F;
        if((World.getTimeofDay() <= 6.5F || World.getTimeofDay() > 18F || ws > 4 && FM.getAltitude() < we) && !FM.isPlayers())
            FM.CT.bFormationLights = true;
        if((World.getTimeofDay() > 6.5F && World.getTimeofDay() <= 18F && ws <= 4 || World.getTimeofDay() > 6.5F && World.getTimeofDay() <= 18F && FM.getAltitude() > we) && !FM.isPlayers())
            FM.CT.bFormationLights = false;
        hierMesh().chunkVisible("SlightNose", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightTail", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightWTopL", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightWTopR", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightKeel", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightWTipL", FM.CT.bFormationLights);
        hierMesh().chunkVisible("SlightWTipR", FM.CT.bFormationLights);
    }

    private void VTOL()
    {
        if((!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver) && !FM.AP.way.isLanding())
        {
            if(FM.CT.getGear() > 0.2F)
                FM.CT.VarWingControl = 0.2F;
            if(FM.CT.getGear() < 0.2F)
                FM.CT.VarWingControl = 0.0F;
        }
        if(FM.CT.getBrake() > 0.5F && FM.Gears.onGround())
        {
            if(FM.getSpeedKMH() > 15F)
                FM.producedAF.x -= FM.getSpeedKMH() * 2.0F;
            if(FM.getSpeedKMH() < -15F)
                FM.producedAF.x += FM.getSpeedKMH() * 2.0F;
        }
        if(nozzlemode == 1)
        {
            if((FM instanceof RealFlightModel) && ((RealFlightModel)FM).isRealMode() || !(FM instanceof Pilot))
            {
                if(FM.CT.getAirBrake() > 0.10F)
                {
                    if(FM.getAltitude() > 0.0F && FM.getSpeedKMH() >= 80F && FM.EI.engines[0].getStage() > 5)
                        FM.producedAF.x -= 3200D;
                    if(FM.getAltitude() > 0.0F && FM.getSpeedKMH() >= 200F && FM.EI.engines[0].getStage() > 5)
                        FM.producedAF.x -= 5300D;
                    if(FM.getAltitude() > 0.0F && FM.getSpeedKMH() >= 300F && FM.EI.engines[0].getStage() > 5)
                        FM.producedAF.x -= 10000D;
                }
                if(FM.getAltitude() > 0.0F && FM.EI.engines[0].getStage() > 5)
                    FM.producedAF.z -= FM.getAltitude() * 3F * FM.EI.engines[0].getPowerOutput();
                float avW = FM.EI.engines[0].getw() / 2.0F;
                if(avW > 60F)
                {
                    Vector3f eVect = new Vector3f();
                    eVect.x = -(FM.CT.getElevator() + FM.CT.getTrimElevatorControl()) * 0.6F + (1.0F - vtolvect);
                    eVect.y = -(FM.CT.getAileron() + FM.CT.getTrimRudderControl()) * 0.6F;
                    eVect.z = 1.0F * vtolvect;
                    eVect.normalize();
                    FM.EI.engines[0].setVector(eVect);
                    FM.Or.increment((FM.CT.getRudder() + FM.CT.getTrimRudderControl()) * 0.4F, (FM.CT.getElevator() + FM.CT.getTrimElevatorControl()) * 0.4F, (FM.CT.getAileron() + FM.CT.getTrimAileronControl()) * 0.4F);
                    if(hierMesh().isChunkVisible("WingROut_CAP") || hierMesh().isChunkVisible("WingROut_CAP") || hierMesh().isChunkVisible("WingROut_CAP"))
                        FM.Or.increment(0.0F, 0.0F, -1F);
                    if(hierMesh().isChunkVisible("WingLOut_CAP") || hierMesh().isChunkVisible("WingLOut_CAP") || hierMesh().isChunkVisible("WingLOut_CAP"))
                        FM.Or.increment(0.0F, 0.0F, 1.0F);
                    if(hierMesh().isChunkVisible("Tail1_CAP"))
                        FM.Or.increment(0.0F, 3F, 0.0F);
                    if(hierMesh().isChunkVisible("Nose_CAP"))
                        FM.Or.increment(0.0F, -3F, 0.0F);
                    FM.getW().scale(0.80D);
                    if(FM.Gears.nOfGearsOnGr < 1)
                    {
                        FM.producedAF.x += (double)vtolSlipX * 200D;
                        FM.producedAF.y -= (double)vtolSlipY * 200D;
                    }
                }
                else
                {
                    if(FM.Gears.nOfGearsOnGr < 2)
                        FM.producedAF.z -= (100D - (double)avW) * 300D + 15000D;
                    Vector3d vector3d = new Vector3d();
                    getSpeed(vector3d);
                    Point3d point3d = new Point3d();
                    super.pos.getAbs(point3d);
                    float f1 = FM.getAltitude() - (float)World.land().HQ(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y);
                    if(f1 < 10F && FM.getSpeedKMH() < 60F && ((Tuple3d) (vector3d)).z < -1D)
                    {
                        vector3d.z *= 0.70D;
                        setSpeed(vector3d);
                    }
                }
            }
            else
            {
                Vector3f eVect = new Vector3f();
                eVect.x = 2.0F;
                eVect.y = 0.0F;
                eVect.z = 0.5F;
                eVect.normalize();
                FM.EI.engines[0].setVector(eVect);
                FM.CT.FlapsControl = 0.6F;
                FM.producedAF.z += 2000D;
                FM.getW().scale(0.70D);
                flapswitch = true;
            }
        }
        else if(nozzlemode == 0)
        {
            float t = (tnozzle - Time.current()) / 10000L;
            if(t < 0.0F)
                t = 0.0F;
            Vector3f eVect = new Vector3f();
            eVect.x = 1.0F - vectorthrustx;
            eVect.y = 0.0F;
            eVect.z = vectorthrustz + t;
            eVect.normalize();
            FM.EI.engines[0].setVector(eVect);
            if(nozzleswitch && FM.EI.engines[0].getStage() > 5 && FM.EI.engines[0].getPowerOutput() > 0.7F && !FM.Gears.onGround() && flapswitch)
            {
                FM.CT.FlapsControl = 0.0F;
                flapswitch = false;
            }
            if(FM.getAltitude() > 0.0F && calculateMach() > 0.0F && FM.EI.engines[0].getStage() > 5)
                FM.producedAF.x -= Math.pow(FM.getSpeedKMH(), 2D) / 20D;
        }
    }

    protected void moveVarWing(float f)
    {
        hierMesh().chunkSetAngles("nozzole3", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 98F));
        hierMesh().chunkSetAngles("nozzole4", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 98F));
        hierMesh().chunkSetAngles("nozzole1", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 98F));
        hierMesh().chunkSetAngles("nozzole2", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 98F));
        if(f > 0.105F)
        {
            nozzlemode = 1;
            nozzleswitch = true;
            vtolvect = f;
        }
        else
        {
            nozzlemode = 0;
            vtolSlipX = 0;
            vtolSlipY = 0;
        }
        if(f < 0.105F)
        {
            nozzleswitch = false;
            tnozzle = Time.current() + 8000L;
        }
        float f1 = f * 4F;
        if(f1 > 0.5F)
            f1 = 0.5F;
        vectorthrustz = f1;
        vectorthrustx = 0.5F * f;
    }

    public void doSetSootState(int i, int j)
    {
        for(int k = 0; k < 2; k++)
        {
            if(FM.AS.astateSootEffects[i][k] != null)
                Eff3DActor.finish(FM.AS.astateSootEffects[i][k]);
            FM.AS.astateSootEffects[i][k] = null;
        }

        float f = FM.EI.engines[0].getPowerOutput();
        switch(j)
        {
        case 1: // '\001'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 3: // '\003'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 0.5F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 0.5F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 2: // '\002'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 0.1F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 0.1F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 5: // '\005'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 0.7F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 0.7F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
            break;

        case 6: // '\006'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 0.5F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 0.5F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            break;

        case 4: // '\004'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_01"), null, 0.2F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "ES_02"), null, 0.2F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
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
                    doRemoveBlisters();
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
                        doRemoveBlister1();
                    break;

                case 3: // '\003'
                    doRemoveBlisters();
                    break;
                }
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                AircraftState tmp178_177 = FM.AS;
                tmp178_177.astateBailoutStep = (byte)(tmp178_177.astateBailoutStep + 1);
                if(FM.AS.astateBailoutStep == 4)
                    FM.AS.astateBailoutStep = 11;
            }
            else if(FM.AS.astateBailoutStep >= 11 && FM.AS.astateBailoutStep <= 19)
            {
                int i = FM.AS.astateBailoutStep;
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                AircraftState tmp383_382 = FM.AS;
                tmp383_382.astateBailoutStep = (byte)(tmp383_382.astateBailoutStep + 1);
                if(i == 11)
                {
                    FM.setTakenMortalDamage(true, null);
                    if((super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_maneuver() != 44)
                    {
                        if(FM.AS.actor != World.cur().getPlayerAircraft())
                            ((Maneuver)super.FM).set_maneuver(44);
                    }
                }
                if(FM.AS.astatePilotStates[i - 11] < 99)
                {
                    doRemoveBodyFromPlane(i - 10);
                    if(i == 11)
                    {
                        doEjectCatapult();
                        FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                        if(i > 10 && i <= 19)
                            EventLog.onBailedOut(this, i - 11);
                    }
                }
            }
    }

    private final void doRemoveBlister1()
    {
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1 && FM.AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(false);
            Vector3d vector3d = new Vector3d();
            vector3d.set(FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    protected final void doRemoveBlisters()
    {
        for(int i = 2; i < 10; i++)
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
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 60D);
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

    private void AIAirBrake()
    {
        if(FM.AP.way.isLanding() && FM.getSpeed() > FM.VmaxFLAPS && Pitot.Indicator((float)FM.getAltitude(), FM.getSpeed()) > FM.AP.way.curr().Speed * 1.25F)
        {
            if(FM.CT.AirBrakeControl != 1.0F)
                FM.CT.AirBrakeControl = 1.0F;
        }
        else if(((Maneuver)super.FM).get_maneuver() == 25 && FM.AP.way.isLanding() && FM.getSpeed() < FM.VmaxFLAPS * 1.2F)
        {
            if(FM.getSpeed() > FM.VminFLAPS * 0.5F && FM.Gears.nearGround())
            {
                if(FM.Gears.onGround())
                {
                    if(FM.CT.AirBrakeControl != 1.0F)
                        FM.CT.AirBrakeControl = 1.0F;
                }
                else if(FM.CT.AirBrakeControl != 1.0F)
                    FM.CT.AirBrakeControl = 1.0F;
            }
            else if(FM.CT.AirBrakeControl != 0.0F)
                FM.CT.AirBrakeControl = 0.0F;
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
        else if(((Maneuver)super.FM).get_maneuver() == 21 && Pitot.Indicator((float)FM.getAltitude(), FM.getSpeed()) > FM.AP.way.curr().Speed * 1.25F && FM.getAltitude() > FM.AP.way.curr().z() + 20F
           && FM.EI.engines[0].getControlThrottle() < 0.88F)
        {
            if(FM.CT.AirBrakeControl != 1.0F)
                FM.CT.AirBrakeControl = 1.0F;
        }
        else if(((Maneuver)super.FM).get_maneuver() == 21 && Pitot.Indicator((float)FM.getAltitude(), FM.getSpeed()) > FM.AP.way.curr().Speed * 1.05F && FM.getAltitude() > FM.AP.way.curr().z()
           && FM.EI.engines[0].getControlThrottle() < 0.95F)
        {
            ;   // Not to do anything in neutral condition
        }
        else if(isHydraulicAlive && FM.CT.AirBrakeControl != 0.0F)
            FM.CT.AirBrakeControl = 0.0F;
    }

    public void computeLift()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float x = cvt(calculateMach(), 0.9F, 1.3F, 0.08F, 0.04F);
        float y = cvt(calculateMach(), 1.3F, 2.3F, 0.04F, 0.016F);
        if(calculateMach() < 1.3F)
            polares.lineCyCoeff = x;
        else
            polares.lineCyCoeff = y;
    }

    public void computeLimiter()
    {
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && FM.EI.engines[0].getStage() > 5)
        {
            double x = (double)cvt(calculateMach(), 0.5F, 1.0F, 0F, 5000F);
            double y = (double)cvt(FM.getAltitude(), 0.0F, 11000F, 1F, -5.9F);
            FM.producedAF.x -= (x * y);
        }
    }

    public void computeVerticalThrust()
    {
        boolean isControlledByAI = true;
        if(FM.isPlayers() && (FM instanceof RealFlightModel))
        {
            isControlledByAI = !((RealFlightModel)FM).RealMode;
        }
        float x = FM.EI.engines[0].getThrustOutput();
        float ias = Pitot.Indicator((float)FM.Loc.z, FM.getSpeed()) * 3.6F;
        float alt = FM.getAltitude() - (float)Engine.land().HQ_Air(FM.Loc.x, FM.Loc.y);
        float y = cvt(ias, 0.0F, 150F, 1F, 0.9F);
        float z = cvt(ias, 150F, 320F, 0.9F, 0.0F);
        float t = cvt(alt, 0.0F, 20F, 1.0F, 1.0F);
        float k = cvt(alt, 20F, 30F, 1.0F, 0.0F);
        float Thrust1 = 0.0F;
        if(FM.EI.engines[0].getStage() > 5 && FM.EI.getPowerOutput() > 0.2F && isControlledByAI) //&& ias < 15F) // AI
        {
            Thrust1 = x * 1.1F;
        }
        FM.producedAF.z += Thrust1 * (10F * FM.M.massEmpty  + 7F * FM.M.fuel) * y * z * t * k; // (10F * FM.M.massEmpty + 10F * FM.M.fuel)
    }

    public float getFlowRate()
    {
        return FlowRate;
    }

    public float getFuelReserve()
    {
        return FuelReserve;
    }

    public int Bingofuel;
    public boolean Nvision;
    public boolean APmode1;
    public boolean ILS;
    private float oldthrl[] = { -1.0F };
    private float curthrl[] = { -1.0F };
    private float engineSurgeDamage[] = { 0.0F };
    private boolean overrideBailout;
    private boolean ejectComplete;
    public int nozzlemode;
    public long tvect;
    private boolean bDynamoOperational;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int pk;
    float vtolvect;
    private long tnozzle;
    private boolean nozzleswitch;
    private boolean flapswitch;
    private float vectorthrustz;
    private float vectorthrustx;
    private long twait;
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
    private float gearTargetAngle;
    private float gearCurrentAngle;

    private Eff3DActor pull1;
    private Eff3DActor pull2;
    private Eff3DActor pull3;
    private Eff3DActor pull4;
    private Eff3DActor pull5;
    private Eff3DActor pull6;
    private Eff3DActor pull7;
    private Eff3DActor pull8;
    private Eff3DActor pull9;
    private Eff3DActor pull10;
    private Eff3DActor pull11;
    private Eff3DActor pull12;
    private Eff3DActor pull13;
    private Eff3DActor pull14;
    private Eff3DActor pull15;
    private Eff3DActor pull16;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    private Eff3DActor jet1;
    private Eff3DActor jet2;
    private boolean bUseDroopAileron;
    private boolean oldbUseDroopAileron;
    public int vtolSlipX;
    public int vtolSlipY;
    private Eff3DActor antiColLight[];
    private boolean oldAntiColLight;
    public boolean isHydraulicAlive;
    public boolean isGeneratorAlive;
    public boolean isBatteryOn;
    private long lastUpdateTime;

    // TypeFuelDump setup values
    public static float FlowRate = 10F;
    public static float FuelReserve = 1500F;

    // TypeGSuit setup values
    private static final float NEG_G_TOLERANCE_FACTOR = 3.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 2.5F;
    private static final float POS_G_TOLERANCE_FACTOR = 8.2F;
    private static final float POS_G_TIME_FACTOR = 3F;
    private static final float POS_G_RECOVERY_FACTOR = 3.5F;


    static
    {
        Class class1 = com.maddox.il2.objects.air.AV_8.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}