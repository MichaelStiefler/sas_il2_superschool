package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.weapons.BombGunPara1;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class U_2xyz extends Scheme1
    implements TypeScout, TypeBomber, TypeTransport
{

    public U_2xyz()
    {
        gunnerDead = false;
        gunnerEjected = false;
        AroneROn = true;
        AroneLOn = true;
        bChangedPit = false;
        gunnerAnimation = 0.0F;
        tme0 = 0L;
        bMultiFunction = false;
    }

    public boolean hasIntervalometer()
    {
        return false;
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

    public void update(float f)
    {
        updatef = f;
        if(!hierMesh().isChunkVisible("Rudder1_D0")) 
            hierMesh().materialReplace("Overlay8", "Null");
        super.update(f);
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 12:
        case 13:
        case 14:
        case 16:
        case 20:
        case 21:
        case 22:
        case 23:
        case 24:
        case 25:
        case 26:
        case 27:
        case 28:
        case 29:
        case 30:
        case 33:
        case 35:
        case 36:
        default:
            break;

        case 17:
        case 31:
            hierMesh().chunkVisible("VatorLrodN_D0", false);
            hierMesh().chunkVisible("VatorLrodV_D0", false);
            break;

        case 18:
        case 32:
            hierMesh().chunkVisible("VatorRrodN_D0", false);
            hierMesh().chunkVisible("VatorRrodV_D0", false);
            break;

        case 11:
        case 15:
            hierMesh().chunkVisible("Rudder1RodL_D0", false);
            hierMesh().chunkVisible("Rudder1RodR_D0", false);
            break;

        case 19:
            FM.Gears.hitCentreGear();
            hierMesh().chunkVisible("VatorLrodN_D0", false);
            hierMesh().chunkVisible("VatorLrodV_D0", false);
            hierMesh().chunkVisible("VatorRrodN_D0", false);
            hierMesh().chunkVisible("VatorRrodV_D0", false);
            hierMesh().chunkVisible("Rudder1RodL_D0", false);
            hierMesh().chunkVisible("Rudder1RodR_D0", false);
            break;

        case 1:
        case 37:
            hierMesh().chunkVisible("AroneRrodN_D0", false);
            AroneROn = false;
            break;

        case 0:
        case 34:
            hierMesh().chunkVisible("AroneLrodN_D0", false);
            AroneLOn = false;
            break;

        case 9:
            if(hierMesh().chunkFindCheck("GearL2_D0") != -1)
            {
                hierMesh().hideSubTrees("GearL2_D0");
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("GearL2_D0"));
                wreckage.collide(true);
                FM.Gears.hitLeftGear();
            }
            break;

        case 10:
            if(hierMesh().chunkFindCheck("GearR2_D0") != -1)
            {
                hierMesh().hideSubTrees("GearR2_D0");
                Wreckage wreckage1 = new Wreckage(this, hierMesh().chunkFind("GearR2_D0"));
                wreckage1.collide(true);
                FM.Gears.hitRightGear();
            }
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        if(af[0] < -35F)
        {
            af[0] = -35F;
            flag = false;
        } else
        if(af[0] > 35F)
        {
            af[0] = 35F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if(f < 10F)
        {
            if(af[1] < -5F)
            {
                af[1] = -5F;
                flag = false;
            }
        } else
        if(af[1] < -15F)
        {
            af[1] = -15F;
            flag = false;
        }
        if(af[1] > 30F)
        {
            af[1] = 30F;
            flag = false;
        }
        if(!flag)
            return false;
        float f1 = af[1];
        if(f < 2.0F && f1 < 17F)
            return false;
        if(f1 > -5F)
            return true;
        if(f1 > -12F)
        {
            f1 += 12F;
            return f > 12F + f1 * 2.571429F;
        } else
        {
            f1 = -f1;
            return f > f1;
        }
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 20F * f, 0.0F, 0.0F);
        resetYPRmodifier();
        if(f > 0.0F)
        {
            Aircraft.xyz[0] = -Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.11F);
            hierMesh().chunkSetLocate("Rudder1RodDR_D0", Aircraft.xyz, Aircraft.ypr);
            Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.08F);
            hierMesh().chunkSetLocate("Rudder1RodDL_D0", Aircraft.xyz, Aircraft.ypr);
        } else
        {
            Aircraft.xyz[0] = Aircraft.cvt(-f, 0.01F, 0.99F, 0.0F, 0.08F);
            hierMesh().chunkSetLocate("Rudder1RodDR_D0", Aircraft.xyz, Aircraft.ypr);
            Aircraft.xyz[0] = -Aircraft.cvt(-f, 0.01F, 0.99F, 0.0F, 0.11F);
            hierMesh().chunkSetLocate("Rudder1RodDL_D0", Aircraft.xyz, Aircraft.ypr);
        }
    }

    protected void moveElevator(float f)
    {
        float f1 = 0.0F;
        if(f > 0.0F)
            f1 = 28F;
        else
            f1 = 13F;
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -f1 * f);
        hierMesh().chunkSetAngles("VatorLRodV_D0", 0.0F, f1 * f, 0.0F);
        hierMesh().chunkSetAngles("VatorLRodN_D0", 0.0F, f1 * f, 0.0F);
        hierMesh().chunkSetAngles("VatorLRodR_D0", 0.0F, f1 * f, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -f1 * f);
        hierMesh().chunkSetAngles("VatorRRodV_D0", 0.0F, f1 * f, 0.0F);
        hierMesh().chunkSetAngles("VatorRRodN_D0", 0.0F, f1 * f, 0.0F);
        hierMesh().chunkSetAngles("VatorRRodR_D0", 0.0F, f1 * f, 0.0F);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 20F * f);
        if(AroneLOn)
            hierMesh().chunkSetAngles("AroneLn_D0", 0.0F, 0.0F, 20F * f);
        else
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 15F);
        hierMesh().chunkSetAngles("aronelrodN_d0", 0.0F, -20F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -20F * f);
        if(AroneROn)
            hierMesh().chunkSetAngles("AroneRn_D0", 0.0F, 0.0F, -20F * f);
        else
            hierMesh().chunkSetAngles("AroneRn_D0", 0.0F, 0.0F, -15F);
        hierMesh().chunkSetAngles("aronerrodN_d0", 0.0F, 20F * f, 0.0F);
        hierMesh().chunkSetAngles("aronerrodV_d0", 0.0F, 20F * f, 0.0F);
        hierMesh().chunkSetAngles("aronelrodV_d0", 0.0F, -20F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxcontrols"))
            {
                if((s.endsWith("5") || s.endsWith("6")) && World.Rnd().nextFloat() < 0.2F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 2);
                    Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#5/#6)");
                }
                if(s.endsWith("2"))
                {
                    if(World.Rnd().nextFloat() < 0.2F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#2)");
                    }
                    if(World.Rnd().nextFloat() < 0.2F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Arone Controls Out.. (#2)");
                    }
                }
                if((s.endsWith("7") || s.endsWith("8") || s.endsWith("3") || s.endsWith("4")) && World.Rnd().nextFloat() < 0.5F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#3/#4/#7/#8)");
                }
                if((s.endsWith("1") || s.endsWith("9")) && World.Rnd().nextFloat() < 0.5F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 2);
                    Aircraft.debugprintln(this, "*** Arone Controls Out.. (#1/#9)");
                }
                return;
            }
            if(s.startsWith("xxeng") || s.startsWith("xxEng"))
            {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if(s.endsWith("prop"))
                    Aircraft.debugprintln(this, "*** Prop hit");
                else
                if(s.startsWith("xxeng1cyl") && getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 4.4F), shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.12F)
                {
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < shot.power / 48000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 3);
                        Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                    }
                    if(World.Rnd().nextFloat() < 0.005F)
                    {
                        FM.AS.setEngineStuck(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                    }
                    getEnergyPastArmor(22.5F, shot);
                }
                if(s.startsWith("xxEng1Mag"))
                {
                    int i = s.charAt(9) - 49;
                    debuggunnery("Engine Module: Magneto " + i + " Destroyed..");
                    FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, i);
                }
                if(s.startsWith("xxEng1Oil"))
                {
                    FM.AS.hitOil(shot.initiator, 0);
                    getEnergyPastArmor(0.22F, shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x80);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
                }
                if(s.endsWith("gear"))
                {
                    Aircraft.debugprintln(this, "*** Engine Module: Gear Hit..");
                    if(getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.05F)
                    {
                        FM.AS.setEngineStuck(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Engine Module: gear hit, engine stuck..");
                    }
                }
                return;
            }
            if(s.startsWith("xxTank"))
            {
                int j = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.4F, shot) > 0.0F && World.Rnd().nextFloat() < 0.99F)
                {
                    if(FM.AS.astateTankStates[j] == 0)
                    {
                        Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                        FM.AS.hitTank(shot.initiator, j, 2);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)
                    {
                        FM.AS.hitTank(shot.initiator, j, 2);
                        Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                    }
                }
            }
            if(s.startsWith("xxlock"))
            {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if(s.startsWith("xxlockR") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockVL") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockVR") && getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockAVL") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockAVR") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
                if(s.startsWith("xxlockANL") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneLn Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneLn_D" + chunkDamageVisible("AroneLn"), shot.initiator);
                }
                if(s.startsWith("xxlockANR") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneRn Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneRn_D" + chunkDamageVisible("AroneRn"), shot.initiator);
                }
            }
            if(s.startsWith("xxMgun"))
            {
                Aircraft.debugprintln(this, "*** Rear Gun Disabled..");
                FM.AS.setJamBullets(10, 0);
                getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
            }
            if(s.startsWith("xxspar"))
            {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if(s.startsWith("xxsparT") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(9.5F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if(s.startsWith("xxsparLI") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLin_D" + chunkDamageVisible("WingLin"), shot.initiator);
                }
                if(s.startsWith("xxsparRI") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRin_D" + chunkDamageVisible("WingRin"), shot.initiator);
                }
                if(s.startsWith("xxsparLO") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
                }
                if(s.startsWith("xxsparRO") && World.Rnd().nextFloat() < 0.25F && getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
                }
                if(s.startsWith("xxsparK") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor((double)(6.8F * World.Rnd().nextFloat(1.0F, 1.5F)) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Keel Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D" + chunkDamageVisible("Keel1"), shot.initiator);
                }
                if(s.startsWith("xxsparSL") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor((double)(6.8F * World.Rnd().nextFloat(1.0F, 1.5F)) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D" + chunkDamageVisible("StabL"), shot.initiator);
                }
                if(s.startsWith("xxsparSR") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor((double)(6.8F * World.Rnd().nextFloat(1.0F, 1.5F)) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D" + chunkDamageVisible("StabR"), shot.initiator);
                }
            }
            return;
        }
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
            if(k == 2)
                k = 1;
            Aircraft.debugprintln(this, "*** hitFlesh..");
            hitFlesh(k, shot, byte0);
        }
        if(s.startsWith("xcf") || s.startsWith("xcockpit"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(s.startsWith("xcockpit"))
            {
                if(World.Rnd().nextFloat() < 0.07F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                if(World.Rnd().nextFloat() < 0.07F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                if(World.Rnd().nextFloat() < 0.07F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
                if(World.Rnd().nextFloat() < 0.07F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                if(World.Rnd().nextFloat() < 0.07F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
            }
        }
        if(s.startsWith("xEng") && chunkDamageVisible("Engine1") < 3)
            hitChunk("Engine1", shot);
        if(s.startsWith("xTail") && chunkDamageVisible("Tail1") < 3)
            hitChunk("Tail1", shot);
        if(s.startsWith("xKeel") && chunkDamageVisible("Keel1") < 3)
            hitChunk("Keel1", shot);
        if(s.startsWith("xRudder") && chunkDamageVisible("Rudder1") < 1)
            hitChunk("Rudder1", shot);
        if(s.startsWith("xStab"))
        {
            if(s.startsWith("xStabL") && chunkDamageVisible("StabL") < 3)
                hitChunk("StabL", shot);
            if(s.startsWith("xStabR") && chunkDamageVisible("StabR") < 3)
                hitChunk("StabR", shot);
        }
        if(s.startsWith("xVator"))
        {
            if(s.startsWith("xVatorL") && chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
            if(s.startsWith("xVatorR") && chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        }
        if(s.startsWith("xWing"))
        {
            Aircraft.debugprintln(this, "*** xWing: " + s);
            if(s.startsWith("xWingLIn") && chunkDamageVisible("WingLin") < 3)
                hitChunk("WingLin", shot);
            if(s.startsWith("xWingRIn") && chunkDamageVisible("WingRin") < 3)
                hitChunk("WingRin", shot);
            if(s.startsWith("xWingLMid") && chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(s.startsWith("xWingRMid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            if(s.startsWith("xWingLOut") && chunkDamageVisible("WingLout") < 3)
                hitChunk("WingLout", shot);
            if(s.startsWith("xwingROut") && chunkDamageVisible("WingRout") < 3)
                hitChunk("WingRout", shot);
        }
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaroneLV") && chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroneRV") && chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR", shot);
            if(s.startsWith("xaroneLN") && chunkDamageVisible("AroneLn") < 1)
                hitChunk("AroneLn", shot);
            if(s.startsWith("xaroneRN") && chunkDamageVisible("AroneRn") < 1)
                hitChunk("AroneRn", shot);
        }
    }

    public boolean cut(String s)
    {
        boolean flag = super.cut(s);
        if(s.equalsIgnoreCase("WingLIn"))
            hierMesh().chunkVisible("WingLMid_CAP", true);
        else
        if(s.equalsIgnoreCase("WingRIn"))
            hierMesh().chunkVisible("WingRMid_CAP", true);
        return flag;
    }

    public float getGunnerAnimation()
    {
        return gunnerAnimation;
    }

    public void gunnerTarget()
    {
        if(gunnerDead || gunnerEjected)
        {
            FM.turret[0].bIsOperable = false;
            return;
        }
        if(!FM.turret[0].bIsAIControlled)
        {
            if(!bMultiFunction && !this.isPlayerMannedTurret())
            {
                FM.turret[0].tu[0] = FM.turret[0].tu[1] = 0.0F;
                hierMesh().setCurChunk(FM.turret[0].indexA);
                hierMesh().chunkSetAngles(FM.turret[0].tu);
                hierMesh().setCurChunk(FM.turret[0].indexB);
                hierMesh().chunkSetAngles(FM.turret[0].tu);
                if((double)gunnerAnimation <= 0.0D)
                    FM.turret[0].bIsOperable = false;
            } else
            if((double)gunnerAnimation >= 1.0D)
                FM.turret[0].bIsOperable = true;
        } else
        if(Time.current() > tme0)
        {
            tme0 = Time.current() + 2000L;
            if(FM.turret.length != 0)
            {
                Actor actor = War.GetNearestEnemy(this, 16, 7000F);
                Aircraft aircraft = War.getNearestEnemy(this, 6000F);
                if(actor != null && !(actor instanceof BridgeSegment) || aircraft != null)
                {
                    if(FM.turret[0].tu[0] > 35F)
                        FM.turret[0].tu[0] = 35F;
                    bMultiFunction = true;
                    if((double)gunnerAnimation >= 1.0D)
                        FM.turret[0].bIsOperable = true;
                    FM.turret[0].bIsAIControlled = true;
                } else
                {
                    if (!isPlayerMannedTurret()) bMultiFunction = false;
                    FM.turret[0].tu[0] = FM.turret[0].tu[1] = 0.0F;
                    hierMesh().setCurChunk(FM.turret[0].indexA);
                    hierMesh().chunkSetAngles(FM.turret[0].tu);
                    hierMesh().setCurChunk(FM.turret[0].indexB);
                    hierMesh().chunkSetAngles(FM.turret[0].tu);
                    if((double)gunnerAnimation <= 0.0D)
                        FM.turret[0].bIsOperable = false;
                    FM.turret[0].bIsAIControlled = true;
                }
            }
        }
    }

    public void gunnerAiming()
    {
        if(FM.CT.Weapons[3] != null && (FM.CT.Weapons[3][0] instanceof BombGunPara1))
            return;
        if(bMultiFunction || isPlayerMannedTurret())
        {
            if((double)gunnerAnimation < 1.0D)
            {
                gunnerAnimation += 0.0125F;
                moveGunner();
            }
        } else
        if((double)gunnerAnimation > 0.0D)
        {
            gunnerAnimation -= 0.0125F;
            moveGunner();
        }
    }

    public void moveGunner()
    {
        if(gunnerDead || gunnerEjected)
        {
            FM.turret[0].bIsOperable = false;
            return;
        }
        if((double)gunnerAnimation < 1.0D)
        {
            FM.turret[0].bIsOperable = false;
            hierMesh().chunkVisible("Turret1A_D0", false);
            hierMesh().chunkVisible("Turret1B_D0", false);
            hierMesh().chunkVisible("Turret1A_FAKE", true);
            hierMesh().chunkVisible("Turret1B_FAKE", true);
        } else
        {
            FM.turret[0].bIsOperable = true;
            hierMesh().chunkVisible("Turret1A_D0", !isPlayerMannedTurret());
            hierMesh().chunkVisible("Turret1B_D0", !isPlayerMannedTurret());
            hierMesh().chunkVisible("Turret1A_FAKE", false);
            hierMesh().chunkVisible("Turret1B_FAKE", false);
        }
        if((double)gunnerAnimation > 0.5D)
        {
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot3_D0", true);
            hierMesh().chunkVisible("Turret1A_D0", !isPlayerMannedTurret());
            hierMesh().chunkVisible("Turret1B_D0", !isPlayerMannedTurret());
            hierMesh().chunkVisible("Turret1A_FAKE", false);
            hierMesh().chunkVisible("Turret1B_FAKE", false);
        } else
        if((double)gunnerAnimation > 0.25D)
        {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = (gunnerAnimation - 0.5F) * 0.5F;
            Aircraft.ypr[0] = -120F + 480F * (gunnerAnimation - 0.25F);
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            hierMesh().chunkSetLocate("Pilot3_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkVisible("Pilot3_D0", true);
            hierMesh().chunkVisible("Pilot2_D0", false);
        } else
        {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = gunnerAnimation * 0.5F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            hierMesh().chunkSetLocate("Pilot2_D0", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Pilot2_D0", true);
        }
    }
    
    public boolean isPlayerMannedTurret() {
        if (!Config.isUSE_RENDER()) return false;
        if (this != World.getPlayerAircraft()) return false;
        if (Main3D.cur3D() == null) return false;
        if (Main3D.cur3D().cockpitCurIndx() == 2) return true;
        return false;
    }

    public abstract void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException;

    public abstract void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException;

    public abstract void typeBomberUpdate(float f);

    public abstract void typeBomberAdjSpeedMinus();

    public abstract void typeBomberAdjSpeedPlus();

    public abstract void typeBomberAdjSpeedReset();

    public abstract void typeBomberAdjAltitudeMinus();

    public abstract void typeBomberAdjAltitudePlus();

    public abstract void typeBomberAdjAltitudeReset();

    public abstract void typeBomberAdjSideslipMinus();

    public abstract void typeBomberAdjSideslipPlus();

    public abstract void typeBomberAdjSideslipReset();

    public abstract void typeBomberAdjDistanceMinus();

    public abstract void typeBomberAdjDistancePlus();

    public abstract void typeBomberAdjDistanceReset();

    public abstract boolean typeBomberToggleAutomation();

    private boolean AroneROn;
    private boolean AroneLOn;
    public boolean gunnerDead;
    public boolean gunnerEjected;
    public static boolean bChangedPit;
    public static float updatef;
    private float gunnerAnimation;
    private long tme0;
    public boolean bMultiFunction;
    public float fSightCurForwardAngle;
    public static boolean Spare1;
    public static boolean Spare2;
    public static boolean Spare3;
    public static boolean Spare4;


    static 
    {
        Class class1 = U_2xyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
