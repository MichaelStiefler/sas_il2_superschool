package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.Property;

public abstract class SM79 extends Scheme6
{

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        hierMesh().chunkSetAngles("GearR7_D0", 0.0F, 1.0F, 0.0F);
        hierMesh().chunkSetAngles("GearL7_D0", 0.0F, 1.0F, 0.0F);
        hierMesh().chunkSetAngles("GearR6_D0", 0.0F, -1F, 0.0F);
        hierMesh().chunkSetAngles("GearL6_D0", 0.0F, -1F, 0.0F);
        if(thisWeaponsName.startsWith("12") || thisWeaponsName.startsWith("6"))
        {
            for(int i = 1; i <= 12; i++)
            {
                hierMesh().chunkVisible("BombRack" + i + "_D0", true);
                numBombsOld = 12;
                if(thisWeaponsName.startsWith("6"))
                    numBombsOld = 6;
            }

        }
        if(thisWeaponsName.startsWith("5"))
        {
            for(int j = 1; j <= 5; j++)
            {
                hierMesh().chunkVisible("BombRack250_" + j + "_D0", true);
                numBombsOld = 5;
            }

        }
        if(thisWeaponsName.startsWith("2"))
        {
            hierMesh().chunkVisible("BombRack500_1_D0", true);
            hierMesh().chunkVisible("BombRack500_2_D0", true);
            numBombsOld = 2;
        }
        if(thisWeaponsName.startsWith("1x"))
        {
            hierMesh().chunkVisible("Torpedo_Support_D0", true);
            numBombsOld = 0;
        }
    }

    public SM79()
    {
        fSightCurAltitude = 300F;
        fSightCurSpeed = 50F;
        fSightCurForwardAngle = 0.0F;
        fSightSetForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        bPitUnfocused = true;
        bPilot1Killed = false;
        bPilot2Killed = false;
        bPilot3Killed = false;
        bPilot4Killed = false;
        bPilot5Killed = false;
        bayDoorAngle = 0.0F;
        wasInTorpedoAttack = false;
        numEvasive = 0;
        timeEvasive = 0;
        timeTorpedoDrop = 0;
        bDynamoOperational = true;
        dynamoOrient = 0.0F;
        bDynamoRotary = false;
    }

    public void rareAction(float f, boolean flag)
    {
        if(hierMesh().isChunkVisible("Prop2_D1") && hierMesh().isChunkVisible("Prop3_D1") && (hierMesh().isChunkVisible("Prop1_D0") || hierMesh().isChunkVisible("PropRot1_D0")))
        {
            mydebuggunnery("!!!!!!!!!!!!!!!!!!! HIT PROP 1 !!!!!!!!!!!!!!!!!!!!!");
            hitProp(0, 0, Engine.actorLand());
        }
        if(hierMesh().isChunkVisible("Prop2_D1") && hierMesh().isChunkVisible("Prop1_D1") && (hierMesh().isChunkVisible("Prop3_D0") || hierMesh().isChunkVisible("PropRot3_D0")))
        {
            mydebuggunnery("!!!!!!!!!!!!!!!!!!! HIT PROP 3 !!!!!!!!!!!!!!!!!!!!!");
            hitProp(2, 0, Engine.actorLand());
        }
        super.rareAction(f, flag);
        if(flag)
        {
            if(FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.39F)
                FM.AS.hitTank(this, 0, 1);
            if(FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.39F)
                FM.AS.hitTank(this, 1, 1);
            if(FM.AS.astateEngineStates[2] > 3 && World.Rnd().nextFloat() < 0.39F)
                FM.AS.hitTank(this, 2, 1);
            if(FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(FM.AS.astateEffectChunks[0] + "0", 0, this);
            if(FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(FM.AS.astateEffectChunks[1] + "0", 0, this);
            if(FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(FM.AS.astateEffectChunks[2] + "0", 0, this);
            if(FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.1F)
                nextDMGLevel(FM.AS.astateEffectChunks[3] + "0", 0, this);
        }
        Actor actor = War.GetNearestEnemy(this, 16, 6000F);
        Aircraft aircraft = War.getNearestEnemy(this, 5000F);
        if((actor != null && !(actor instanceof BridgeSegment) || aircraft != null) && FM.CT.getCockpitDoor() < 0.01F)
            FM.AS.setCockpitDoor(this, 1);
        drawBombs();
        for(int i = 1; i <= 5; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("hmask" + i + "_d0", false);
            else
                hierMesh().chunkVisible("hmask" + i + "_d0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected void drawBombs()
    {
        if(!bPitUnfocused)
            return;
        if(thisWeaponsName.endsWith("drop"))
        {
            int i = 0;
            if(FM.CT.Weapons[3] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[3].length; j++)
                    if(FM.CT.Weapons[3][j] != null)
                        i += FM.CT.Weapons[3][j].countBullets();

                if(thisWeaponsName.startsWith("12x1"))
                {
                    for(int k = 2; k < i + 1; k++)
                        hierMesh().chunkVisible("Bomb100Kg" + k + "_D0", true);

                    for(int l = i + 1; l <= 12; l++)
                        hierMesh().chunkVisible("Bomb100Kg" + l + "_D0", false);

                }
                if(thisWeaponsName.startsWith("12x5"))
                {
                    for(int i1 = 2; i1 < i + 1; i1++)
                        hierMesh().chunkVisible("Bomb50Kg" + i1 + "_D0", true);

                    for(int j1 = i + 1; j1 <= 12; j1++)
                        hierMesh().chunkVisible("Bomb50Kg" + j1 + "_D0", false);

                }
                if(thisWeaponsName.startsWith("6"))
                {
                    for(int k1 = 2; k1 < i + 1; k1++)
                        hierMesh().chunkVisible("Bomb100Kg" + k1 + "_D0", true);

                    for(int l1 = i + 1; l1 <= 6; l1++)
                        hierMesh().chunkVisible("Bomb100Kg" + l1 + "_D0", false);

                }
                if(thisWeaponsName.startsWith("5"))
                {
                    for(int i2 = 2; i2 < i + 1; i2++)
                        hierMesh().chunkVisible("Bomb250Kg" + i2 + "_D0", true);

                    for(int j2 = i + 1; j2 <= 5; j2++)
                        hierMesh().chunkVisible("Bomb250Kg" + j2 + "_D0", false);

                }
                if(thisWeaponsName.startsWith("2"))
                    if(i == 2)
                        hierMesh().chunkVisible("Bomb500Kg2_D0", true);
                    else
                        hierMesh().chunkVisible("Bomb500Kg2_D0", false);
            }
        }
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
            hierMesh().chunkVisible("Dina_D0", !bDynamoRotary);
            hierMesh().chunkVisible("PropDina_D0", bDynamoRotary);
        }
        dynamoOrient = bDynamoRotary ? (dynamoOrient - 17.987F) % 360F : (float)(dynamoOrient - FM.Vwld.length() * 1.5444015264511108D) % 360F;
        hierMesh().chunkSetAngles("Dina_D0", 0.0F, dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 2:
            FM.turret[0].setHealth(f);
            break;

        case 3:
            FM.turret[1].setHealth(f);
            break;

        case 4:
            FM.turret[2].setHealth(f);
            FM.turret[3].setHealth(f);
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("hmask1_d0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            bPilot1Killed = true;
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("hmask2_d0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            bPilot2Killed = true;
            break;

        case 2:
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("hmask3_d0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            bPilot3Killed = true;
            break;

        case 3:
            hierMesh().chunkVisible("Pilot4_D0", false);
            hierMesh().chunkVisible("hmask4_d0", false);
            hierMesh().chunkVisible("Pilot4_D1", true);
            bPilot4Killed = true;
            break;

        case 4:
            hierMesh().chunkVisible("Pilot5_D0", false);
            hierMesh().chunkVisible("hmask5_d0", false);
            hierMesh().chunkVisible("Pilot5_D1", true);
            bPilot5Killed = true;
            break;
        }
    }

    public void update(float f)
    {
        for(int i = 0; i < 3; i++)
            if(FM.EI.engines[i].getControlProp() < 0.5F)
                FM.EI.engines[i].setControlProp(0.0F);
            else
                FM.EI.engines[i].setControlProp(1.0F);

        if(bPitUnfocused && Config.isUSE_RENDER())
        {
            com.maddox.il2.engine.Mat mat = hierMesh().material(hierMesh().materialFind("InteriorFake1"));
            hierMesh().materialReplace("InteriorFake", mat);
        }
        float f1 = 0.0F;
        f1 = Pitot.Indicator((float)FM.Loc.z, FM.getSpeedKMH());
        f1 = f1 * 0.05F + oldSpeed * 0.95F;
        Controls controls = FM.CT;
        float f2 = (210F - f1) / 70F;
        if(f2 > 1.0F)
            f2 = 1.0F;
        else
        if(f2 < 0.0F)
            f2 = 0.0F;
        f2 *= f2;
        oldSpeed = f1;
        float f4 = -35F * f2;
        hierMesh().chunkSetAngles("Flap_RearL_D0", 0.0F, f4, 0.0F);
        hierMesh().chunkSetAngles("Flap_RearR_D0", 0.0F, f4, 0.0F);
        f4 = -3F * f2;
        hierMesh().chunkSetAngles("Flap_FrontL_D0", 0.0F, f4, 0.0F);
        hierMesh().chunkSetAngles("Flap_FrontR_D0", 0.0F, f4, 0.0F);
        controls.forceFlaps(f2);
        controls.bHasFlapsControl = false;
        float f3 = controls.getAileron();
        float f5 = -(f3 * 30F + f2 * 17F);
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, f5, 0.0F);
        f5 = -(f3 * 30F - f2 * 17F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, f5, 0.0F);
        super.update(f);
    }

    protected void moveAileron(float f)
    {
        Controls controls = FM.CT;
        float f1 = controls.getFlap();
        float f2 = -(f * 30F + f1 * 17F);
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, f2, 0.0F);
        f2 = -(f * 30F - f1 * 17F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, f2, 0.0F);
    }

    public void moveCockpitDoor(float f)
    {
        boolean flag = isChunkAnyDamageVisible("Tail1_D");
        if(f < 0.99F)
        {
            if(!hierMesh().isChunkVisible("Tur2_DoorR_D0"))
            {
                hierMesh().chunkVisible("Tur2_DoorR_D0", true);
                hierMesh().chunkVisible("Tur2_DoorR_open_D0", false);
                hierMesh().chunkVisible("Tur2_DoorL_D0", true);
                hierMesh().chunkVisible("Tur2_DoorL_open_D0", false);
                hierMesh().chunkVisible("Tur1_DoorR_D0", true);
                hierMesh().chunkVisible("Tur1_DoorR_open_D0", false);
                hierMesh().chunkVisible("Tur1_DoorL_D0", true);
                hierMesh().chunkVisible("Tur1_DoorL_open_D0", false);
            }
            float f1 = 13.8F * f;
            hierMesh().chunkSetAngles("Tur2_Door1_D0", 0.0F, -f1, 0.0F);
            f1 = 8.8F * f;
            hierMesh().chunkSetAngles("Tur2_Door2_D0", 0.0F, -f1, 0.0F);
            f1 = 3.1F * f;
            hierMesh().chunkSetAngles("Tur2_Door3_D0", 0.0F, -f1, 0.0F);
            f1 = 14F * f;
            hierMesh().chunkSetAngles("Tur2_DoorL_D0", 0.0F, -f1, 0.0F);
            hierMesh().chunkSetAngles("Tur2_DoorR_D0", 0.0F, f1, 0.0F);
            hierMesh().chunkSetAngles("Tur1_DoorL_D0", 0.0F, f1, 0.0F);
            hierMesh().chunkSetAngles("Tur1_DoorR_D0", 0.0F, -f1, 0.0F);
        } else
        if(hierMesh().isChunkVisible("Tur2_DoorR_D0"))
        {
            hierMesh().chunkVisible("Tur2_DoorR_D0", false);
            hierMesh().chunkVisible("Tur2_DoorR_open_D0", true);
            hierMesh().chunkVisible("Tur2_DoorL_D0", false);
            hierMesh().chunkVisible("Tur2_DoorL_open_D0", true);
            hierMesh().chunkVisible("Tur1_DoorR_D0", false);
            hierMesh().chunkVisible("Tur1_DoorR_open_D0", flag);
            hierMesh().chunkVisible("Tur1_DoorL_D0", false);
            hierMesh().chunkVisible("Tur1_DoorL_open_D0", flag);
        }
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void hitDaSilk()
    {
        if(FM.CT.getCockpitDoor() < 0.01F)
            FM.AS.setCockpitDoor(this, 1);
        super.hitDaSilk();
    }

    protected void mydebuggunnery(String s)
    {
    }

    protected void setControlDamage(Shot shot, int i)
    {
        if(World.Rnd().nextFloat() < 0.002F && getEnergyPastArmor(4F, shot) > 0.0F)
        {
            FM.AS.setControlsDamage(shot.initiator, i);
            mydebuggunnery(i + " Controls Out... //0 = AILERON, 1 = ELEVATOR, 2 = RUDDER");
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        mydebuggunnery("HitBone called! " + s);
        mydebuggunnery("IN: " + shot.power);
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
                if(s.endsWith("p1"))
                {
                    if(Aircraft.v1.z > 0.5D)
                        getEnergyPastArmor(4D / Aircraft.v1.z, shot);
                    else
                    if(Aircraft.v1.x > 0.93969261646270752D)
                        getEnergyPastArmor((8D / Aircraft.v1.x) * World.Rnd().nextFloat(1.0F, 1.2F), shot);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(4D / Math.abs(Aircraft.v1.z), shot);
                else
                if(s.endsWith("p3"))
                    getEnergyPastArmor((7D / Math.abs(Aircraft.v1.x)) * World.Rnd().nextFloat(1.0F, 1.2F), shot);
                else
                if(s.endsWith("p4"))
                {
                    if(Aircraft.v1.x > 0.70710676908493042D)
                        getEnergyPastArmor((7D / Aircraft.v1.x) * World.Rnd().nextFloat(1.0F, 1.2F), shot);
                    else
                    if(Aircraft.v1.x > -0.70710676908493042D)
                        getEnergyPastArmor(5F, shot);
                } else
                if(s.endsWith("a1") || s.endsWith("a3") || s.endsWith("a4"))
                    if(Aircraft.v1.x > 0.70710676908493042D)
                        getEnergyPastArmor((0.8D / Aircraft.v1.x) * World.Rnd().nextFloat(1.0F, 1.2F), shot);
                    else
                        getEnergyPastArmor(0.6F, shot);
            if(s.startsWith("xxspar"))
            {
                getEnergyPastArmor(4F, shot);
                if((s.endsWith("cf1") || s.endsWith("cf2")) && World.Rnd().nextFloat() < 0.1F && chunkDamageVisible("CF") > 2 && getEnergyPastArmor(15.9F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
                {
                    mydebuggunnery("*** CF Spars Broken in Half..");
                    msgCollision(this, "Tail1_D0", "Tail1_D0");
                    msgCollision(this, "WingLIn_D0", "WingLIn_D0");
                    msgCollision(this, "WingRIn_D0", "WingRIn_D0");
                }
                if((s.endsWith("t1") || s.endsWith("t2")) && World.Rnd().nextFloat() < 0.1F && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(15.9F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
                {
                    mydebuggunnery("*** Tail1 Spars Broken in Half..");
                    msgCollision(this, "Tail1_D0", "Tail1_D0");
                }
                if((s.endsWith("li1") || s.endsWith("li2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(13.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D2", shot.initiator);
                }
                if((s.endsWith("ri1") || s.endsWith("ri2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D2", shot.initiator);
                }
                if((s.endsWith("lm1") || s.endsWith("lm2")) && World.Rnd().nextFloat() < 1.0D - 0.86D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D2", shot.initiator);
                }
                if((s.endsWith("rm1") || s.endsWith("rm2")) && World.Rnd().nextFloat() < 1.0D - 0.86D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(13.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D2", shot.initiator);
                }
                if((s.endsWith("lo1") || s.endsWith("lo2")) && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D2", shot.initiator);
                }
                if((s.endsWith("ro1") || s.endsWith("ro2")) && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D2", shot.initiator);
                }
                if(s.endsWith("e1") && (point3d.y > 2.79D || point3d.y < 2.32D) && getEnergyPastArmor(17F, shot) > 0.0F)
                {
                    mydebuggunnery("*** Engine1 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if(s.endsWith("e2") && (point3d.y < -2.79D || point3d.y > -2.32D) && getEnergyPastArmor(17F, shot) > 0.0F)
                {
                    mydebuggunnery("*** Engine2 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
                if(s.endsWith("e3") && (point3d.y < -2.79D || point3d.y > -2.32D) && getEnergyPastArmor(17F, shot) > 0.0F)
                {
                    mydebuggunnery("*** Engine3 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine3_D0", shot.initiator);
                }
                if((s.endsWith("k1") || s.endsWith("k2")) && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** Keel spars damaged..");
                    nextDMGLevels(1, 2, "Keel1_D0", shot.initiator);
                }
                if((s.endsWith("sr1") || s.endsWith("sr2")) && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** Right Stab spars damaged..");
                    nextDMGLevels(1, 2, "StabR_D0", shot.initiator);
                }
                if((s.endsWith("sl1") || s.endsWith("sl2")) && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    mydebuggunnery("*** Left Stab spars damaged..");
                    nextDMGLevels(1, 2, "StabL_D0", shot.initiator);
                }
            }
            if(s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.01F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets())
            {
                mydebuggunnery("*** Bomb Payload Detonates..");
                FM.AS.hitTank(shot.initiator, 0, 100);
                FM.AS.hitTank(shot.initiator, 1, 100);
                FM.AS.hitTank(shot.initiator, 2, 100);
                FM.AS.hitTank(shot.initiator, 3, 100);
                msgCollision(this, "CF_D0", "CF_D0");
            }
            if(s.startsWith("xxprop"))
            {
                byte byte0 = 0;
                if(s.endsWith("2"))
                    byte0 = 1;
                if(s.endsWith("3"))
                    byte0 = 2;
                if(getEnergyPastArmor(2.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.35F)
                {
                    FM.AS.setEngineSpecificDamage(shot.initiator, byte0, 3);
                    mydebuggunnery("*** Engine" + (byte0 + 1) + " Governor Failed..");
                }
            }
            if(s.startsWith("xxeng"))
            {
                byte byte1 = 0;
                if(s.startsWith("xxeng2"))
                    byte1 = 1;
                if(s.startsWith("xxeng3"))
                    byte1 = 2;
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(0.11F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 200000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, byte1);
                            mydebuggunnery("*** Engine" + (byte1 + 1) + " Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            FM.AS.hitEngine(shot.initiator, byte1, 2);
                            mydebuggunnery("*** Engine" + (byte1 + 1) + " Crank Case Hit - Engine Damaged..");
                        }
                    }
                } else
                if(s.endsWith("cyls"))
                {
                    mydebuggunnery("*** Engine" + (byte1 + 1) + " RATIO " + FM.EI.engines[byte1].getCylindersRatio());
                    if(getEnergyPastArmor(1.4F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[byte1].getCylindersRatio() * 0.6F)
                    {
                        FM.EI.engines[byte1].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 5000F)));
                        mydebuggunnery("*** Engine" + (byte1 + 1) + " Cylinders Hit, " + FM.EI.engines[byte1].getCylindersOperable() + "/" + FM.EI.engines[byte1].getCylinders() + " Left..");
                        if(FM.AS.astateEngineStates[byte1] < 1)
                        {
                            FM.AS.hitEngine(shot.initiator, byte1, 1);
                            FM.AS.doSetEngineState(shot.initiator, byte1, 1);
                        }
                        if(World.Rnd().nextFloat() < shot.power / 960000F)
                        {
                            FM.AS.hitEngine(shot.initiator, byte1, 3);
                            mydebuggunnery("*** Engine" + (byte1 + 1) + " Cylinders Hit - Engine Fires..");
                        }
                        mydebuggunnery("*** Engine" + (byte1 + 1) + " state " + FM.AS.astateEngineStates[byte1]);
                        getEnergyPastArmor(25F, shot);
                    }
                } else
                if(s.endsWith("supc") && getEnergyPastArmor(0.3F, shot) > 0.0F && World.Rnd().nextFloat() < 0.79F)
                {
                    FM.AS.setEngineSpecificDamage(shot.initiator, byte1, 0);
                    mydebuggunnery("*** Engine" + (byte1 + 1) + " Supercharger Out..");
                }
                if(s.endsWith("oil1") || s.endsWith("oil2") || s.endsWith("oil3"))
                {
                    if(getEnergyPastArmor(0.65F, shot) > 0.0F)
                        FM.AS.hitOil(shot.initiator, byte1);
                    getEnergyPastArmor(0.42F, shot);
                }
                mydebuggunnery("*** Engine" + (byte1 + 1) + " state = " + FM.AS.astateEngineStates[byte1]);
            }
            if(s.startsWith("xxtank"))
            {
                int i = s.charAt(6) - 49;
                if(getEnergyPastArmor(1.3F, shot) > 0.0F)
                    if(shot.power < 14100F)
                    {
                        if(FM.AS.astateTankStates[i] < 1)
                        {
                            mydebuggunnery("deterministic damage !!! ");
                            FM.AS.hitTank(shot.initiator, i, 1);
                        }
                        if(FM.AS.astateTankStates[i] < 4 && World.Rnd().nextFloat() < 0.1F)
                        {
                            mydebuggunnery("random damage !!! ");
                            FM.AS.hitTank(shot.initiator, i, 1);
                        }
                        if(shot.powerType == 3 && FM.AS.astateTankStates[i] > 0 && World.Rnd().nextFloat() < 0.07F)
                        {
                            mydebuggunnery("API round !!! ");
                            FM.AS.hitTank(shot.initiator, i, 10);
                        }
                    } else
                    {
                        mydebuggunnery("big shot !!! ");
                        FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int)(shot.power / 40000F)));
                    }
                mydebuggunnery("*** Tank " + (i + 1) + " state = " + FM.AS.astateTankStates[i]);
            }
            if(s.startsWith("xxlock"))
            {
                mydebuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && (s.startsWith("xxlockr1") || s.startsWith("xxlockr2")) && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    mydebuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    mydebuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    mydebuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    mydebuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
            }
        }
        if(s.startsWith("x12"))
            if(s.startsWith("x12,7_01"))
            {
                if(getEnergyPastArmor(5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    FM.AS.setJamBullets(0, 0);
                    getEnergyPastArmor(11.98F, shot);
                }
            } else
            if(s.startsWith("x12,7_00"))
            {
                if(getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    FM.AS.setJamBullets(0, 2);
                    getEnergyPastArmor(11.98F, shot);
                }
            } else
            if(s.startsWith("x12,7_02"))
            {
                if(getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    FM.AS.setJamBullets(0, 1);
                    getEnergyPastArmor(11.98F, shot);
                }
            } else
            if(s.startsWith("x12,7_03"))
            {
                if(getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    FM.AS.setJamBullets(0, 4);
                    getEnergyPastArmor(11.98F, shot);
                }
            } else
            if(s.startsWith("x12,7_04") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
            {
                FM.AS.setJamBullets(0, 3);
                getEnergyPastArmor(11.98F, shot);
            }
        if(s.startsWith("xcf"))
        {
            setControlDamage(shot, 0);
            setControlDamage(shot, 1);
            setControlDamage(shot, 2);
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(World.Rnd().nextFloat() < 0.1F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            if(World.Rnd().nextFloat() < 0.1F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
            if(World.Rnd().nextFloat() < 0.1F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            if(World.Rnd().nextFloat() < 0.1F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
            if(World.Rnd().nextFloat() < 0.1F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
            if(World.Rnd().nextFloat() > 0.8F)
                getEnergyPastArmor(5F, shot);
        } else
        if(s.startsWith("xtail"))
        {
            setControlDamage(shot, 1);
            setControlDamage(shot, 2);
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
            if(World.Rnd().nextFloat() < 0.1F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
            if(World.Rnd().nextFloat() < 0.1F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
            if(World.Rnd().nextFloat() > 0.8F)
                getEnergyPastArmor(3F, shot);
        } else
        if(s.startsWith("xkeel"))
        {
            setControlDamage(shot, 2);
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder"))
        {
            setControlDamage(shot, 2);
            hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xstabl"))
            hitChunk("StabL", shot);
        else
        if(s.startsWith("xstabr"))
            hitChunk("StabR", shot);
        else
        if(s.startsWith("xvatorl"))
            hitChunk("VatorL", shot);
        else
        if(s.startsWith("xvatorr"))
            hitChunk("VatorR", shot);
        else
        if(s.startsWith("xwinglin"))
        {
            setControlDamage(shot, 0);
            if(chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(World.Rnd().nextFloat() > 0.7F)
                getEnergyPastArmor(5F, shot);
        } else
        if(s.startsWith("xwingrin"))
        {
            setControlDamage(shot, 0);
            if(chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(World.Rnd().nextFloat() > 0.7F)
                getEnergyPastArmor(5F, shot);
        } else
        if(s.startsWith("xwinglmid"))
        {
            setControlDamage(shot, 0);
            if(chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
        } else
        if(s.startsWith("xwingrmid"))
        {
            setControlDamage(shot, 0);
            if(chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
        } else
        if(s.startsWith("xwinglout"))
        {
            if(chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
        } else
        if(s.startsWith("xwingrout"))
        {
            if(chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xaronel"))
            hitChunk("AroneL", shot);
        else
        if(s.startsWith("xaroner"))
            hitChunk("AroneR", shot);
        else
        if(s.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
        } else
        if(s.startsWith("xengine3"))
        {
            if(chunkDamageVisible("Engine3") < 2)
                hitChunk("Engine3", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(World.Rnd().nextFloat() < 0.1F)
            {
                mydebuggunnery("*** Gear Hydro Failed..");
                FM.Gears.setHydroOperable(false);
            }
        } else
        if(s.startsWith("xturret"))
        {
            if(s.startsWith("xturret1"))
                FM.AS.setJamBullets(10, 0);
            if(s.startsWith("xturret2"))
                FM.AS.setJamBullets(11, 0);
            if(s.startsWith("xturret3"))
                FM.AS.setJamBullets(12, 0);
            if(s.startsWith("xturret4"))
                FM.AS.setJamBullets(13, 0);
            if(s.startsWith("xturret5"))
                FM.AS.setJamBullets(14, 0);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead") || s.startsWith("xpilox") || s.startsWith("xheax"))
        {
            byte byte2 = 0;
            int j;
            if(s.endsWith("a"))
            {
                byte2 = 1;
                j = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte2 = 2;
                j = s.charAt(6) - 49;
            } else
            {
                j = s.charAt(5) - 49;
            }
            mydebuggunnery("call HitFlesh:  " + j + " " + byte2 + " " + shot.power);
            hitFlesh(j, shot, byte2);
        }
        mydebuggunnery("out:  " + shot.power);
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        if(i == 19)
            FM.Gears.hitCentreGear();
        return super.cutFM(i, j, actor);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Math.max(-f * 800F, -70F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -81F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -81F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 53F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 53F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, 1.2F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, 1.2F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, -1.2F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -1.2F * f1, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    protected void moveBayDoor(float f)
    {
        float f1 = f * 4F;
        hierMesh().chunkSetAngles("DoorL_D0", 0.0F, 23F * f1, 0.0F);
        hierMesh().chunkSetAngles("DoorR_D0", 0.0F, -23F * f1, 0.0F);
        f1 = -(f * 3.5F);
        hierMesh().chunkSetAngles("Gambali_D0", 0.0F, f1, 0.0F);
        bayDoorAngle = f;
    }

    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurForwardAngle;
    public float fSightSetForwardAngle;
    public float fSightCurSideslip;
    public boolean bPitUnfocused;
    public boolean bPilot1Killed;
    public boolean bPilot2Killed;
    public boolean bPilot3Killed;
    public boolean bPilot4Killed;
    public boolean bPilot5Killed;
    public int numBombsOld;
    public float bayDoorAngle;
    boolean wasInTorpedoAttack;
    int numEvasive;
    int timeEvasive;
    int timeTorpedoDrop;
    private boolean bDynamoOperational;
    private float dynamoOrient;
    private float oldSpeed;
    private boolean bDynamoRotary;
    private int pk;

    static 
    {
        Class class1 = SM79.class;
        Property.set(class1, "originCountry", PaintScheme.countryItaly);
    }
}
