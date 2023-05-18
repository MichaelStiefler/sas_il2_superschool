// MiG-21 Parent Class File

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
import com.maddox.sound.*;
import java.io.IOException;

public class MIG_21 extends Scheme1
    implements TypeSupersonic, TypeFastJet, TypeFighter, TypeBNZFighter, TypeFighterAceMaker, TypeGSuit
{
    private float oldthrl;
    private float curthrl;
    private float engineSurgeDamage;
    private float SonicBoom;
    private Eff3DActor shockwave;
    private boolean isSonic;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private boolean pylonOccupied;
    private Bomb booster[];
    protected boolean bHasBoosters;
    protected long boosterFireOutTime;
    private SoundFX fxSirena;
    private Sample smplSirena;
    private boolean sirenaSoundPlaying;
    private boolean bRadarWarning;
    protected boolean bHasSK1Seat;
    private static Actor hunted = null;
    private Eff3DActor gearLightsEffects[];
    private LightPointActor gearLightsLights[];
    private boolean bGearLightsOn;
    private boolean bGearLightState;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;

    public MIG_21()
    {
        SonicBoom = 0.0F;
        fxSirena = newSound("aircraft.Sirena2", false);
        smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
        sirenaSoundPlaying = false;
        bHasSK1Seat = true;
        gearLightsEffects = new Eff3DActor[3];
        gearLightsLights = new LightPointActor[3];
        bGearLightsOn = false;
        bGearLightState = false;
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        overrideBailout = false;
        ejectComplete = false;
        oldthrl = -1F;
        curthrl = -1F;
        pylonOccupied = false;
        booster = new Bomb[2];
        bHasBoosters = true;
        boosterFireOutTime = -1L;
        smplSirena.setInfinite(true);
    }

    public void getGFactors(TypeGSuit.GFactors theGFactors)
    {
        theGFactors.setGFactors(1.0F, 1.0F, 1.0F, 1.8F, 1.5F, 1.0F);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if((FM.Gears.nearGround() || FM.Gears.onGround()) && FM.CT.getCockpitDoor() == 1.0F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
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
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
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
        if(k14Mode == 0)
            return;
        hunted = Main3D.cur3D().getViewPadlockEnemy();
        if(hunted == null)
            hunted = War.GetNearestEnemyAircraft(((Interpolate) (super.FM)).actor, 2000F, 9);
        if(hunted != null)
        {
            k14Distance = (float)((Interpolate) (super.FM)).actor.pos.getAbsPoint().distance(hunted.pos.getAbsPoint());
            if(k14Distance > 1700F)
                k14Distance = 1700F;
            else
            if(k14Distance < 200F)
                k14Distance = 200F;
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
            hierMesh().chunkVisible("Glass_Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
       hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -125F), 0.0F);
       hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, -90F));
       hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 90F));
       hiermesh.chunkSetAngles("GearL27_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -90F));
       hiermesh.chunkSetAngles("GearR27_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -90F));
       hiermesh.chunkSetAngles("GearL22_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, 115F), 0.0F);
       hiermesh.chunkSetAngles("GearR22_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, 115F), 0.0F);
       hiermesh.chunkSetAngles("GearL23_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.11F, 0.2F, 0.0F, -55F));
       hiermesh.chunkSetAngles("GearR23_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.11F, 0.2F, 0.0F, -55F));
       hiermesh.chunkSetAngles("GearL3_D0", Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, 45.0F), 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -40.3F));
       hiermesh.chunkSetAngles("GearR3_D0", Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, 47.0F), 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -42.3F));
       hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, 145F));
       hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.1F, 0.0F, -145F));
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.12F, 0.3F, 0.0F, 0.31F);
        hierMesh().chunkSetLocate("GearL21_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.12F, 0.3F, 0.0F, 0.31F);
        hierMesh().chunkSetLocate("GearR21_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveWheelSink()
    {	
        float f = FM.Gears.gWheelSinking[2];
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, 20.0F * f, 0.0F);
        resetYPRmodifier();
        xyz[1] = FM.Gears.gWheelSinking[0];
        hierMesh().chunkSetLocate("GearL212_D0", xyz, ypr);
        resetYPRmodifier();
        xyz[1] = FM.Gears.gWheelSinking[1];
        hierMesh().chunkSetLocate("GearR212_D0", xyz, ypr);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -25F * f, 0.0F);
    }

    protected void moveElevator(float f)
    {
        if(f > 0.0F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -16.5F * FM.CT.getElevator(), 0.0F);

            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -16.5F * FM.CT.getElevator(), 0.0F);
        } else
        if(f < 0.0F)
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -7F * FM.CT.getElevator(), 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -7F * FM.CT.getElevator(), 0.0F);
        }
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 20F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -20F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        float f1 = -44.5F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("AirbrakeL", -35F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("AirbrakeR", 35F * f, 0.0F, 0.0F);
        if(!pylonOccupied)
        {
            hierMesh().chunkSetAngles("AirbrakeRear", 0.0F, 40F * f, 0.0F);
            hierMesh().chunkSetAngles("AirbrakeTelescope", 0.0F, -40F * f, 0.0F);
        }
    }

    protected void moveFan(float f1)
    {
    }

    public void moveSteering(float f)
    {
    	if(FM.CT.GearControl > 0.5F)
        hierMesh().chunkSetAngles("GearC_D0", 30F * f, 0.0F, 0.0F);
    	if(FM.CT.GearControl < 0.5F)
            hierMesh().chunkSetAngles("GearC_D0", 0.0F, 0.0F, 0.0F);
    }

    public void moveShockCone()
    {
        float m = calculateMach();
        Aircraft.xyz[0] = Aircraft.cvt(m / 1.9F, 0.0F, 1.0F, 0.0F, 1.2F);
        hierMesh().chunkSetLocate("Cone", Aircraft.xyz, Aircraft.ypr);
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
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                switch(i)
                {
                default:
                    break;

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
                return;
            }
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
                if(!s.endsWith("exht"));
                return;
            }
            if(s.startsWith("xxmgun0"))
            {
                Aircraft.debugprintln(this, "Armament: Gunpod Disabled..");
                FM.AS.setJamBullets(0, 0);
                FM.AS.setJamBullets(0, 1);
                return;
            }
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
                return;
            }
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
                return;
            }
            if(s.startsWith("xxhyd"))
            {
                FM.AS.setInternalDamage(shot.initiator, 3);
                return;
            }
            if(s.startsWith("xxpnm"))
            {
                FM.AS.setInternalDamage(shot.initiator, 1);
                return;
            } else
            {
                return;
            }
        }
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
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel"))
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner"))
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xflap"))
        {
            if(s.startsWith("xflap1"))
                hitChunk("Flap01", shot);
            if(s.startsWith("xflap2"))
                hitChunk("Flap02", shot);
            if(World.Rnd().nextFloat() < 0.4F)
                FM.CT.bHasFlapsControlRed = true;
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

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19: // '\023'
            FM.EI.engines[0].setEngineDies(actor);
            // fall through

        case 33: // '!'
        case 34: // '"'
        case 35: // '#'
        case 36: // '$'
        case 37: // '%'
        case 38: // '&'
            doCutBoosters();
            FM.AS.setGliderBoostOff();
            bHasBoosters = false;
            // fall through

        default:
            return super.cutFM(i, j, actor);
        }
    }

    public void destroy()
    {
        doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters()
    {
        Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 15F);
        Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 6F);
        Eff3DActor.New(this, findHook("_Booster1"), null, 1.0F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 6F);
        Eff3DActor.New(this, findHook("_Booster1"), null, 0.25F, "3DO/Effects/Aircraft/TurboHWK109D.eff", 6F);
        Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", 15F);
        Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", 6F);
        Eff3DActor.New(this, findHook("_Booster2"), null, 1.0F, "3DO/Effects/Aircraft/SPRD_Flame.eff", 6F);
        Eff3DActor.New(this, findHook("_Booster2"), null, 0.25F, "3DO/Effects/Aircraft/TurboHWK109D.eff", 6F);
    }

    public void doCutBoosters()
    {
        for(int i = 0; i < 2; i++)
            if(booster[i] != null)
            {
                booster[i].start();
                booster[i] = null;
            }

    }

    public float getAirPressure(float theAltitude)
    {
        float fBase = 1.0F - (0.0065F * theAltitude) / 288.15F;
        float fExponent = 5.255781F;
        return 101325F * (float)Math.pow(fBase, fExponent);
    }

    public float getAirPressureFactor(float theAltitude)
    {
        return getAirPressure(theAltitude) / 101325F;
    }

    public float getAirDensity(float theAltitude)
    {
        return (getAirPressure(theAltitude) * 0.0289644F) / (8.31447F * (288.15F - 0.0065F * theAltitude));
    }

    public float getAirDensityFactor(float theAltitude)
    {
        return getAirDensity(theAltitude) / 1.225F;
    }

    public float getMachForAlt(float theAltValue)
    {
        theAltValue /= 1000F;
        int i = 0;
        for(i = 0; i < TypeSupersonic.fMachAltX.length; i++)
            if(TypeSupersonic.fMachAltX[i] > theAltValue)
                break;

        if(i == 0)
        {
            return TypeSupersonic.fMachAltY[0];
        } else
        {
            float baseMach = TypeSupersonic.fMachAltY[i - 1];
            float spanMach = TypeSupersonic.fMachAltY[i] - baseMach;
            float baseAlt = TypeSupersonic.fMachAltX[i - 1];
            float spanAlt = TypeSupersonic.fMachAltX[i] - baseAlt;
            float spanMult = (theAltValue - baseAlt) / spanAlt;
            return baseMach + spanMach * spanMult;
        }
    }


    public float getMpsFromKmh(float kilometersPerHour)
    {
        return kilometersPerHour / 3.6F;
    }

    public float calculateMach()
    {
        return super.FM.getSpeedKMH() / getMachForAlt(super.FM.getAltitude());
    }

    public void soundbarier()
    {
        float f = getMachForAlt(super.FM.getAltitude()) - super.FM.getSpeedKMH();
        if(f < 0.5F)
            f = 0.5F;
        float f_0_ = super.FM.getSpeedKMH() - getMachForAlt(super.FM.getAltitude());
        if(f_0_ < 0.5F)
            f_0_ = 0.5F;
        if((double)calculateMach() <= 1.0D)
        {
            super.FM.VmaxAllowed = super.FM.getSpeedKMH() + f;
            SonicBoom = 0.0F;
            isSonic = false;
        }
        if((double)calculateMach() >= 1.0D)
        {
            super.FM.VmaxAllowed = super.FM.getSpeedKMH() + f_0_;
            isSonic = true;
        }
        if(FM.VmaxAllowed > 1500F)
            super.FM.VmaxAllowed = 1500F;
        if(isSonic && SonicBoom < 1.0F)
        {
            super.playSound("aircraft.SonicBoom", true);
            super.playSound("aircraft.SonicBoomInternal", true);
            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                HUD.log("Mach 1 Exceeded!");
            if(Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(super.FM.getAltitude()))
                shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
            SonicBoom = 1.0F;
        }
        if((double)calculateMach() > 1.01D || (double)calculateMach() < 1.0D)
            Eff3DActor.finish(shockwave);
    }

    public void engineSurge(float f)
    {
        if(FM.AS.isMaster())
            if(curthrl == -1F)
            {
                curthrl = oldthrl = FM.EI.engines[0].getControlThrottle();
            } else
            {
                curthrl = FM.EI.engines[0].getControlThrottle();
                if(curthrl < 1.05F)
                {
                    if((curthrl - oldthrl) / f > 20F && FM.EI.engines[0].getRPM() < 3200F && FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.4F)
                    {
                        if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                            HUD.log("Compressor Stall!");
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.01D * (double)(FM.EI.engines[0].getRPM() / 1000F);
                        FM.EI.engines[0].doSetReadyness(FM.EI.engines[0].getReadyness() - engineSurgeDamage);
                        if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            FM.AS.hitEngine(this, 0, 100);
                        if(World.Rnd().nextFloat() < 0.05F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            FM.EI.engines[0].setEngineDies(this);
                    }
                    if((curthrl - oldthrl) / f < -20F && (curthrl - oldthrl) / f > -100F && FM.EI.engines[0].getRPM() < 3200F && FM.EI.engines[0].getStage() == 6)
                    {
                        super.playSound("weapon.MGunMk108s", true);
                        engineSurgeDamage += 0.001D * (double)(FM.EI.engines[0].getRPM() / 1000F);
                        FM.EI.engines[0].doSetReadyness(FM.EI.engines[0].getReadyness() - engineSurgeDamage);
                        if(World.Rnd().nextFloat() < 0.4F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                        {
                            if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                                HUD.log("Engine Flameout!");
                            FM.EI.engines[0].setEngineStops(this);
                        } else
                        if(((Interpolate) (super.FM)).actor == World.getPlayerAircraft())
                            HUD.log("Compressor Stall!");
                    }
                }
                oldthrl = curthrl;
            }
    }

    private boolean sirenaWarning()
    {
    	if ((Aircraft)this != World.getPlayerAircraft()) return false; // MiG-21 Hotfix: Skip Player only functions for AI
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
        Actor actor = War.getNearestEnemy(this, 4000F);
        if((actor instanceof Aircraft) && actor.getArmy() != World.getPlayerArmy() && (actor instanceof TypeFighterAceMaker) && ((actor instanceof TypeSupersonic) || (actor instanceof TypeFastJet)) && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
        {
            super.pos.getAbs(point3d);
            double d3 = Main3D.cur3D().land2D.worldOfsX() + ((Tuple3d) (actor.pos.getAbsPoint())).x;
            double d4 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).y;
            double d5 = Main3D.cur3D().land2D.worldOfsY() + ((Tuple3d) (actor.pos.getAbsPoint())).z;
            new String();
            new String();
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
            int k1 = (int)(Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
            float f1 = 57.32484F * (float)Math.atan2(k1, d11);
            int l1 = (int)(Math.floor((int)f1) - 90D);
            if(l1 < 0)
                l1 += 360;
            int i2 = l1 - j;
            int j2 = (int)(Math.ceil(((double)k1 * 3.2808399000000001D) / 100D) * 100D);
            if(j2 >= 5280)
                j2 = (int)Math.floor(j2 / 5280);
            bRadarWarning = (double)k1 <= 3000D && (double)k1 >= 50D && i2 >= 195 && i2 <= 345 && Math.sqrt(j1 * j1) >= 120D;
            playSirenaWarning(bRadarWarning);
        } else
        {
            bRadarWarning = false;
            playSirenaWarning(bRadarWarning);
        }
        return true;
    }

    public void playSirenaWarning(boolean isThreatened)
    {
    	if ((Aircraft)this != World.getPlayerAircraft()) return; // MiG-21 Hotfix: Skip Player only functions for AI
        if(isThreatened && !sirenaSoundPlaying)
        {
            fxSirena.play(smplSirena);
            sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-2: Enemy on Six!");
        } else
        if(!isThreatened && sirenaSoundPlaying)
        {
            fxSirena.cancel();
            sirenaSoundPlaying = false;
        }
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.AS.wantBeaconsNet(true);
        if((getBulletEmitterByHookName("_ExternalDev01") instanceof PylonGP9) || (getBulletEmitterByHookName("_ExternalDev02") instanceof MiG21Pylon))
        {
            pylonOccupied = true;
            FM.Sq.dragAirbrakeCx = FM.Sq.dragAirbrakeCx / 2.0F;
        }
        if(super.thisWeaponsName.endsWith("RATO"))
        {
            for(int i = 0; i < 2; i++)
                try
                {
                    booster[i] = new BombSPRD99();
                    ((Actor) (booster[i])).pos.setBase(this, findHook("_BoosterH" + (i + 1)), false);
                    ((Actor) (booster[i])).pos.resetAsBase();
                    booster[i].drawing(true);
                }
                catch(Exception exception)
                {
                    debugprintln("Structure corrupt - can't hang SPRD-99..");
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
        } else
        if(f6 > f1)
        {
            f6 -= f4 * f;
            if(f6 < f1)
                f6 = f1;
        }
        return f6;
    }

    public void update(float f)
    {
        super.update(f); // MiG-21 Bugfix: Put super.update(f) in front (super should always be called before any other method).
        if(super.FM.getSpeedKMH() > 700F && FM.CT.bHasFlapsControl)
        {
            FM.CT.FlapsControl = 0.0F;
            FM.CT.bHasFlapsControl = false;
        } else
        {
            FM.CT.bHasFlapsControl = true;
        }
        if (FM.CT.FlapsControl < 0) FM.CT.FlapsControl = 0; // MiG-21 Bugfix: Limit Flaps Control to 0-100%, don't allow negative flaps values.
        if (FM.CT.FlapsControl > 1) FM.CT.FlapsControl = 1; // MiG-21 Bugfix: Limit Flaps Control to 0-100%, don't allow excessive flaps values.
        if(FM.CT.getFlap() < FM.CT.FlapsControl) {
//            System.out.println("Flaps 1 " + EventLog.name(this) + " getFlap()=" + FM.CT.getFlap() + " FlapsControl=" + FM.CT.FlapsControl);
            FM.CT.forceFlaps(flapsMovement(f, FM.CT.FlapsControl, FM.CT.getFlap(), 999F, Aircraft.cvt(super.FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.08F)));
        } else if(FM.CT.getFlap() > FM.CT.FlapsControl) {
//            System.out.println("Flaps 2 " + EventLog.name(this) + " getFlap()=" + FM.CT.getFlap() + " FlapsControl=" + FM.CT.FlapsControl);
            FM.CT.forceFlaps(flapsMovement(f, FM.CT.FlapsControl, FM.CT.getFlap(), 999F, Aircraft.cvt(super.FM.getSpeedKMH(), 0.0F, 700F, 0.5F, 0.7F)));
        }
        for(int i = 1; i < 19; i++)
            hierMesh().chunkSetAngles("EngineExhaustFlap" + i, 0.0F, -38F * FM.CT.getPowerControl(), 0.0F);

        resetYPRmodifier();
        float f1 = FM.CT.getPowerControl() * 1.5F;
        Aircraft.xyz[0] = Aircraft.cvt(f1, 0.0F, 1.5F, 0.0F, 1.5F);
        hierMesh().chunkSetLocate("EffectBox", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(calculateMach() / 1.9F, 0.0F, 1.0F, 0.0F, 0.15F);
        hierMesh().chunkSetLocate("Cone", Aircraft.xyz, Aircraft.ypr);
        float f2 = super.FM.getSpeedKMH() - 1000F;
        if(f2 < 0.0F)
            f2 = 0.0F;
        FM.CT.dvGear = 0.2F - f2 / 1000F;
        if(FM.CT.dvGear < 0.0F)
            FM.CT.dvGear = 0.0F;
        try
        {
            sirenaWarning();
        }
        catch(NullPointerException nullpointerexception) { }
        engineSurge(f);
        typeFighterAceMakerRangeFinder();
        soundbarier();
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
            if(FM.EI.engines[0].getThrustOutput() > 0.5F && FM.EI.engines[0].getStage() == 6)
            {
                if(FM.EI.engines[0].getThrustOutput() > 0.5F)
                    if(FM.EI.engines[0].getThrustOutput() > 1.001F)
                        FM.AS.setSootState(this, 0, 5);
                    else
                        FM.AS.setSootState(this, 0, 3);
            } else
            {
                FM.AS.setSootState(this, 0, 0);
            }
        if((!super.FM.isPlayers() || !(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver))
        {
            if(FM.AP.way.isLanding() && FM.Gears.onGround() && super.FM.getSpeed() > 40F)
            {
                FM.CT.AirBrakeControl = 1.0F;
                if(FM.CT.bHasDragChuteControl)
                    FM.CT.DragChuteControl = 1.0F;
            }
            if(FM.AP.way.isLanding() && FM.Gears.onGround() && super.FM.getSpeed() < 40F)
            {
                FM.CT.AirBrakeControl = 0.0F;
                if(super.FM.getSpeed() < 20F)
                    FM.CT.DragChuteControl = 0.0F;
            }
        }
        if((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete && super.FM.getSpeedKMH() > 15F)
        {
            overrideBailout = true;
            FM.AS.bIsAboutToBailout = false;
            bailout();
        }
        if((super.FM instanceof Pilot) && bHasBoosters && super.thisWeaponsName.endsWith("RATO"))
        {
            if(super.FM.getAltitude() > 300F && boosterFireOutTime == -1L && ((Tuple3d) (FM.Loc)).z != 0.0D && World.Rnd().nextFloat() < 0.05F)
            {
                doCutBoosters();
                FM.AS.setGliderBoostOff();
                bHasBoosters = false;
            }
            if(bHasBoosters && boosterFireOutTime == -1L && FM.Gears.onGround() && FM.EI.getPowerOutput() > 0.8F && super.FM.getSpeedKMH() > 20F)
            {
                boosterFireOutTime = Time.current() + 6000L;
                doFireBoosters();
                FM.AS.setGliderBoostOn();
            }
            if(bHasBoosters && boosterFireOutTime > 0L)
            {
                if(Time.current() < boosterFireOutTime)
                    FM.producedAF.x += 20000D;
                if(Time.current() > boosterFireOutTime + 20000L)
                {
                    doCutBoosters();
                    FM.AS.setGliderBoostOff();
                    bHasBoosters = false;
                }
            }
        }
        if(FM.AS.bNavLightsOn && FM.CT.getGear() >= 1.0F)
            bGearLightsOn = true;
        else
            bGearLightsOn = false;
        if(bGearLightState != bGearLightsOn)
            doSetGearLightsState(bGearLightsOn);	
    }

    private void doSetGearLightsState(boolean flag)
    {
        for(int i = 0; i < gearLightsEffects.length; i++)
        {
            if(gearLightsEffects[i] != null)
            {
                Eff3DActor.finish(gearLightsEffects[i]);
                gearLightsLights[i].light.setEmit(0.0F, 0.0F);
            }
            gearLightsEffects[i] = null;
        }

        if(flag)
        {
            gearLightsEffects[0] = Eff3DActor.New(this, findHook("_GearLightC"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhiteStatic.eff", -1F, false);
            gearLightsEffects[1] = Eff3DActor.New(this, findHook("_GearLightL"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhiteStatic.eff", -1F, false);
            gearLightsEffects[2] = Eff3DActor.New(this, findHook("_GearLightR"), null, 1.0F, "3DO/Effects/Fireworks/FlareWhiteStatic.eff", -1F, false);
            for(int j = 0; j < gearLightsEffects.length; j++)
            {
                Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                Loc loc1 = new Loc();
                loc1.set(1.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                if(j == 0)
                    findHook("_GearLightC").computePos(this, loc, loc1);
                if(j == 1)
                    findHook("_GearLightL").computePos(this, loc, loc1);
                if(j == 2)
                    findHook("_GearLightR").computePos(this, loc, loc1);
                Point3d point3d = loc1.getPoint();
                gearLightsLights[j] = new LightPointActor(new LightPoint(), point3d);
                gearLightsLights[j].light.setColor(0.5F, 0.6F, 0.7F);
                gearLightsLights[j].light.setEmit(1.0F, 8F);
                super.draw.lightMap().put("_GearLightC", gearLightsLights[0]);
                super.draw.lightMap().put("_GearLightL", gearLightsLights[1]);
                super.draw.lightMap().put("_GearLightR", gearLightsLights[2]);
            }

            bGearLightState = true;
        } else
        {
            bGearLightState = flag;
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
        case 2: // '\002'
        case 3: // '\003'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("HolyGrail02"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            break;

        case 5: // '\005'
            FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, findHook("HolyGrail02"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
            FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, findHook("HolyGrail01"), null, 2.5F, "3DO/Effects/Aircraft/afterburner.eff", -1F);
            // fall through

        case 4: // '\004'
        default:
            return;
        }
    }

    public void doEjectCatapult()
    {
        new MsgAction(false, this) {

            public void doAction(Object paramObject)
            {
                Aircraft localAircraft = (Aircraft)paramObject;
                if(Actor.isValid(localAircraft))
                {
                    Loc localLoc1 = new Loc();
                    Loc localLoc2 = new Loc();
                    Vector3d localVector3d = new Vector3d(0.0D, 0.0D, 20D);
                    HookNamed localHookNamed = new HookNamed(localAircraft, "_ExternalSeat01");
                    ((Actor) (localAircraft)).pos.getAbs(localLoc2);
                    localHookNamed.computePos(localAircraft, localLoc2, localLoc1);
                    localLoc1.transform(localVector3d);
                    localVector3d.x += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).x;
                    localVector3d.y += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).y;
                    localVector3d.z += ((Tuple3d) (((FlightModelMain) (((SndAircraft) (localAircraft)).FM)).Vwld)).z;
                    new EjectionSeat(9, localLoc1, localVector3d, localAircraft);
                }
            }

        }
;
        hierMesh().chunkVisible("Seat_D0", false);
        super.FM.setTakenMortalDamage(true, null);
        FM.CT.WeaponControl[0] = false;
        FM.CT.WeaponControl[1] = false;
        FM.CT.bHasAileronControl = false;
        FM.CT.bHasRudderControl = false;
        FM.CT.bHasElevatorControl = false;
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
            } else
            if(FM.AS.astateBailoutStep >= 11 && FM.AS.astateBailoutStep <= 19)
            {
                int i = FM.AS.astateBailoutStep;
                if(FM.AS.isMaster())
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                AircraftState tmp383_382 = FM.AS;
                tmp383_382.astateBailoutStep = (byte)(tmp383_382.astateBailoutStep + 1);
                if(i == 11)
                {
                    super.FM.setTakenMortalDamage(true, null);
                    if((super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_maneuver() != 44)
                    {
                        World.cur();
                        if(FM.AS.actor != World.getPlayerAircraft())
                            ((Maneuver)super.FM).set_maneuver(44);
                    }
                }
                if(FM.AS.astatePilotStates[i - 11] < 99)
                {
                    doRemoveBodyFromPlane(i - 10);
                    if(i == 11)
                    {
                        doEjectCatapult();
                        hierMesh().chunkVisible("Glass_Head1_D0", false);
                        super.FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        FM.AS.astateBailoutStep = -1;
                        overrideBailout = false;
                        FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                }
            }
    }

    private final void doRemoveBlister1()
    {
        if(hierMesh().chunkFindCheck("Blister1_D0") != -1 && FM.AS.getPilotHealth(0) > 0.0F)
        {
            hierMesh().hideSubTrees("Blister1_D0");
            if(!bHasSK1Seat)
            {
                Wreckage localWreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
                localWreckage.collide(false);
                Vector3d localVector3d = new Vector3d();
                localVector3d.set(FM.Vwld);
                localWreckage.setSpeed(localVector3d);
            }
        }
    }

    private final void doRemoveBlisters()
    {
        for(int i = 2; i < 10; i++)
            if(hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1 && FM.AS.getPilotHealth(i - 1) > 0.0F)
            {
                hierMesh().hideSubTrees("Blister" + i + "_D0");
                if(!bHasSK1Seat)
                {
                    Wreckage localWreckage = new Wreckage(this, hierMesh().chunkFind("Blister" + i + "_D0"));
                    localWreckage.collide(false);
                    Vector3d localVector3d = new Vector3d();
                    localVector3d.set(FM.Vwld);
                    localWreckage.setSpeed(localVector3d);
                }
            }

    }
	
    public void doRemoveBodyFromPlane(int i)
    {
    	super.doRemoveBodyFromPlane(i);
        doRemoveBodyChunkFromPlane("Glass_Head1_D0");
    }
    
    static 
    {
        Class class1 = com.maddox.il2.objects.air.MIG_21.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}