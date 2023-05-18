// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 14/10/2015 09:57:21 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   F84G1.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            F84, PaintSchemeFMPar06, TypeFighter, TypeBNZFighter, 
//            TypeFighterAceMaker, Aircraft, Cockpit, NetAircraft

public class F84G1 extends F84
    implements TypeFighter, TypeBNZFighter, TypeFighterAceMaker
{

    public F84G1()
    {
        oldctl = -1F;
        curctl = -1F;
        arrestor2 = 0.0F;
        prevWing = 1.0F;
        AirBrakeControl = 0.0F;
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
        pk = 0;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(super.FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
        if(((FlightModelMain) (super.FM)).AP.way.isLanding() && (double)super.FM.getSpeed() > (double)((FlightModelMain) (super.FM)).VmaxFLAPS * 2D)
            ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 1.0F;
        else
        if(((FlightModelMain) (super.FM)).AP.way.isLanding() && (double)super.FM.getSpeed() < (double)((FlightModelMain) (super.FM)).VmaxFLAPS * 1.5D)
            ((FlightModelMain) (super.FM)).CT.AirBrakeControl = 0.0F;
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

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 70F), 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -70F), 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.7F, 0.0F, -100F), 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.1F, 0.0F, -80F), 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f2, 0.01F, 0.1F, 0.0F, 80F), 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.7F, 0.0F, 81F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.7F, 0.0F, -81F), 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.7F, 0.0F, 88F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.7F, 0.0F, -88F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.025F, 0.0F, -110F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f1, 0.01F, 0.025F, 0.0F, 110F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    public void moveWheelSink()
    {
        float f = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[2], 0.0F, 0.19075F, 0.0F, 1.0F);
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.19075F * f;
        hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, 30F * f, 0.0F);
        if(((FlightModelMain) (super.FM)).CT.GearControl > 0.5F)
            hierMesh().chunkSetAngles("GearC7_D0", 0.0F, -60F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        float f1 = 55F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 0.0F, f1);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 0.0F, f1);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 0.9F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void moveFan(float f)
    {
        pk = Math.abs((int)(((FlightModelMain) (super.FM)).Vwld.length() / 14D));
        if(pk >= 1)
            pk = 1;
        if(bDynamoRotary != (pk == 1))
        {
            bDynamoRotary = pk == 1;
            hierMesh().chunkVisible("Prop1_D0", !bDynamoRotary);
            hierMesh().chunkVisible("PropRot1_D0", bDynamoRotary);
        }
        dynamoOrient = bDynamoRotary ? (dynamoOrient - 17.987F) % 360F : (float)((double)dynamoOrient - ((FlightModelMain) (super.FM)).Vwld.length() * 1.5444015264511108D) % 360F;
        hierMesh().chunkSetAngles("Prop1_D0", 0.0F, dynamoOrient, 0.0F);
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
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 2);
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
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3: // '\003'
                case 4: // '\004'
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;
                }
            } else
            if(s.startsWith("xxeng1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("bloc"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(0.0F, 60F) / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999997473787516E-005D), shot);
                if(s.endsWith("cams") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersRatio() * 20F)
                {
                    ((FlightModelMain) (super.FM)).EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debuggunnery("Engine Module: Engine Cams Hit, " + ((FlightModelMain) (super.FM)).EI.engines[0].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 1);
                        debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
                    }
                }
                if(s.endsWith("eqpt") && World.Rnd().nextFloat() < shot.power / 24000F)
                {
                    ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, 0, 3);
                    debuggunnery("Engine Module: Hit - Engine Fires..");
                }
                if(!s.endsWith("exht"));
            } else
            if(s.startsWith("xxtank"))
            {
                int j = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(((FlightModelMain) (super.FM)).AS.astateTankStates[j] == 0)
                    {
                        debuggunnery("Fuel Tank (" + j + "): Pierced..");
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, j, 1);
                        ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.075F)
                    {
                        ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, j, 2);
                        debuggunnery("Fuel Tank (" + j + "): Hit..");
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
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
            else
            if(s.startsWith("xxpnm"))
                ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 1);
        } else
        {
            if(s.startsWith("xcockpit"))
            {
                ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
                getEnergyPastArmor(0.05F, shot);
            }
            if(s.startsWith("xxmgun1") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 0);
            if(s.startsWith("xxmgun2") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 1);
            if(s.startsWith("xxmgun3") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 2);
            if(s.startsWith("xxmgun4") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 3);
            if(s.startsWith("xxmgun5") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 4);
            if(s.startsWith("xxmgun6") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 5);
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
                    ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 0);
                }
                if(s.endsWith("2") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
                {
                    debuggunnery("Undercarriage: Stuck..");
                    ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 3);
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
            ((FlightModelMain) (super.FM)).EI.engines[0].setEngineDies(actor);
            return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f)
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster() && Config.isUSE_RENDER())
            if(((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 0.5F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 6)
            {
                if(((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 2.0F)
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 5);
                else
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 4);
            } else
            {
                ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 0);
            }
        super.update(f);
    }

    protected void moveAirBrake(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.25F);
        hierMesh().chunkSetLocate("Brake01_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("Brake02_D0", 0.0F, -70F * f, 0.0F);
    }

    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunBrowning50ki", 300);
        }
        catch(Exception exception) { }
        return a_lweaponslot;
    }

//    static Class _mthclass$(String s)
//    {
//        try
//        {
//            return Class.forName(s);
//        }
//        catch(ClassNotFoundException classnotfoundexception)
//        {
//            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
//        }
//    }
//
//    static Class _mthclass$(String s)
//    {
//        try
//        {
//            return Class.forName(s);
//        }
//        catch(ClassNotFoundException classnotfoundexception)
//        {
//            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
//        }
//    }

    public static boolean bChangedPit = false;
    private float oldctl;
    private float curctl;
    private float arrestor2;
    private float prevWing;
    public float AirBrakeControl;
    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    private float dynamoOrient;
    private boolean bDynamoRotary;
    private int pk;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.F84G1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F84G");
        Property.set(class1, "meshName", "3DO/Plane/F84G/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "meshName_us", "3DO/Plane/F84G(US)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946.9F);
        Property.set(class1, "yearExpired", 1955.3F);
        Property.set(class1, "FlightModel", "FlightModels/F84G.fmd:F84_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitF84G1.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 
            9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 
            3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", 
            "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", 
            "_ExternalBomb05", "_ExternalBomb03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", 
            "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", 
            "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 44;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = GenerateDefaultConfig(byte0);
            for(int i = 6; i < byte0; i++)
                a_lweaponslot[i] = null;

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x500lbs";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x500lbs+02x250lbs";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGun250lbsE", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun250lbsE", 1);
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x1000lbs";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x1000lbs+02x250lbs";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGun250lbsE", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun250lbsE", 1);
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x500lbs+08xHVAR";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x1000lbs+08xHVAR";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x75gal_napalm+08xHVAR";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x75gal_napalm+02x250lbs";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGun250lbsE", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGun250lbsE", 1);
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "20xHVAR";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02xTinyTim";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunTinyTim", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunTinyTim", 1);
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02xTinyTim+08xHVAR";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunTinyTim", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunTinyTim", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(2, "RocketGunHVARF84", 1);
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x230gal_tank";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonF84Bomb", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank230gal", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank230gal", 1);
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            for(int j = 0; j < byte0; j++)
                a_lweaponslot[j] = null;

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}
