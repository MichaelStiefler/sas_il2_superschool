package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class MB_174xyz extends Scheme2
    implements TypeBomber
{

    public MB_174xyz()
    {
        bGunUp = false;
        btme = -1L;
        fGunPos = 0.0F;
        kl = 1.0F;
        kr = 1.0F;
    }

    public void update(float f)
    {
        super.update(f);
        if(!bGunUp)
        {
            if(fGunPos > 0.0F)
            {
                fGunPos -= 0.2F * f;
                this.FM.turret[0].bIsOperable = false;
            }
        } else
        if(fGunPos < 1.0F)
        {
            fGunPos += 0.2F * f;
            if(fGunPos > 0.8F && fGunPos < 0.9F)
                this.FM.turret[0].bIsOperable = true;
        }
        if(fGunPos < 0.6F)
        {
            resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(fGunPos, 0.0F, 0.6F, 0.0F, -0.65F);
            hierMesh().chunkSetLocate("Blister2_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if(this.FM.turret[0].bIsAIControlled)
        {
            if(this.FM.turret[0].target != null && this.FM.AS.astatePilotStates[2] < 90)
                bGunUp = true;
            if(Time.current() > btme)
            {
                btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
                if(this.FM.turret[0].target == null && this.FM.AS.astatePilotStates[2] < 90)
                    bGunUp = false;
            }
        }
        if (this.FM.AS.isMaster() && (!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && this.FM.Gears.onGround()) {
            if (this.FM.getSpeedKMH() < 20.0F) {
              this.FM.CT.cockpitDoorControl = 1.0F;
            } else {
              this.FM.CT.cockpitDoorControl = 0.0F;
            }
        }
        if(this.FM.isPlayers() && this.FM.Sq.squareElevators > 0.0F)
        {
            RealFlightModel realflightmodel = (RealFlightModel)this.FM;
            if(realflightmodel.RealMode && realflightmodel.indSpeed > 120F)
            {
                float f2 = 1.0F + 0.005F * (120F - realflightmodel.indSpeed);
                if(f2 < 0.0F)
                    f2 = 0.0F;
                this.FM.SensPitch = 0.45F * f2;
                if(realflightmodel.indSpeed > 120F)
                    this.FM.producedAM.y -= 1720F * (120F - realflightmodel.indSpeed);
            } else
            {
                this.FM.SensPitch = 0.6F;
            }
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Math.max(-f * 800F, -80F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 70F * f);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 100F * f * kl);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 100F * f * kr);
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.32F);
        hiermesh.chunkSetLocate("GearL2a_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetLocate("GearR2a_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, -150F * f * kl);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, -150F * f * kr);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -25F * f);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 25F * f);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
        hierMesh().chunkSetAngles("Rudder2_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("FlapWL_D0", 0.0F, 0.0F, 40F * f);
        hierMesh().chunkSetAngles("FlapWR_D0", 0.0F, 0.0F, 40F * f);
        hierMesh().chunkSetAngles("FlapCL_D0", 0.0F, 0.0F, 45F * f);
        hierMesh().chunkSetAngles("FlapCR_D0", 0.0F, 0.0F, 45F * f);
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("BayL_D0", 0.0F, 90F * f, 0.0F);
        hierMesh().chunkSetAngles("BayR_D0", 0.0F, -90F * f, 0.0F);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.58F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void doKillPilot(int i)
    {
        switch(i)
        {
        case 2:
            this.FM.turret[0].bIsOperable = false;
            this.FM.turret[1].bIsOperable = false;
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("Head1_D0", false);
        hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        default:
            break;

        case 0:
            if(f < -45F)
            {
                f = -45F;
                flag = false;
            }
            if(f > 45F)
            {
                f = 45F;
                flag = false;
            }
            if(f1 < -3F)
            {
                f1 = -3F;
                flag = false;
            }
            if(f1 > 45F)
            {
                f1 = 45F;
                flag = false;
            }
            break;

        case 1:
            if(f < -10F)
            {
                f = -10F;
                flag = false;
            }
            if(f > 10F)
            {
                f = 10F;
                flag = false;
            }
            if(f1 < -20F)
            {
                f1 = -20F;
                flag = false;
            }
            if(f1 > 2.0F)
            {
                f1 = 2.0F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 4; i++)
            if(this.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxArmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("p1"))
                    getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                else
                if(s.endsWith("p3"))
                    getEnergyPastArmor(12.7D / (Math.abs(Aircraft.v1.x) + 0.0001D), shot);
                return;
            }
            if(s.startsWith("xxcontrol"))
            {
                debuggunnery("Controls: Hit..");
                if(World.Rnd().nextFloat() >= 0.99F)
                {
                    int i = (new Integer(s.substring(9))).intValue();
                    switch(i)
                    {
                    case 4:
                    default:
                        break;

                    case 2:
                    case 3:
                        if(getEnergyPastArmor(3.5F, shot) > 0.0F)
                        {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            debuggunnery("Controls: Rudder Controls: Fuselage Line Destroyed..");
                        }
                        break;

                    case 0:
                    case 1:
                        if(getEnergyPastArmor(0.002F, shot) > 0.0F)
                        {
                            this.FM.AS.setControlsDamage(shot.initiator, 1);
                            debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        }
                        break;

                    case 5:
                        if(getEnergyPastArmor(0.1F, shot) <= 0.0F)
                            break;
                        if(World.Rnd().nextFloat() < 0.25F)
                        {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            debuggunnery("*** Engine1 Throttle Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            debuggunnery("*** Engine1 Prop Controls Out..");
                        }
                        break;

                    case 6:
                        if(getEnergyPastArmor(0.1F, shot) <= 0.0F)
                            break;
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            debuggunnery("*** Engine2 Throttle Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            debuggunnery("*** Engine2 Prop Controls Out..");
                        }
                        break;

                    case 7:
                        if(getEnergyPastArmor(1.5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.12F)
                        {
                            this.FM.AS.setControlsDamage(shot.initiator, 2);
                            debuggunnery("Rudder2 Controls Out..");
                        }
                        break;

                    case 8:
                        if(getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                        {
                            this.FM.AS.setControlsDamage(shot.initiator, 0);
                            debuggunnery("Controls: Aileron Controls: Disabled..");
                        }
                        break;
                    }
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.5F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    debuggunnery("Spar Construction: Tail1 Spars Broken in Half..");
                    nextDMGLevels(1, 2, "Tail1_D2", shot.initiator);
                }
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D2", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D2", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(13.2F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D2", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(13.2F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(11.2F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if(s.startsWith("xxspark1") && chunkDamageVisible("Keel1") > 1 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** Keel1 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D2" + chunkDamageVisible("Keel1"), shot.initiator);
                }
                if(s.startsWith("xxspark2") && chunkDamageVisible("Keel2") > 1 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** Keel2 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D2" + chunkDamageVisible("Keel2"), shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr"))
                {
                    int j = s.charAt(6) - 48;
                    if(getEnergyPastArmor(6.56F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                        if(j < 3)
                        {
                            debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                            nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                        } else
                        {
                            debuggunnery("Lock Construction: Rudder2 Lock Shot Off..");
                            nextDMGLevels(3, 2, "Rudder2_D" + chunkDamageVisible("Rudder2"), shot.initiator);
                        }
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxeng"))
            {
                int i = s.charAt(5) - 49;
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(1.7F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat(20000F, 140000F) < shot.power)
                            this.FM.AS.setEngineStuck(shot.initiator, i);
                        if(World.Rnd().nextFloat(10000F, 50000F) < shot.power)
                            this.FM.AS.hitEngine(shot.initiator, i, 2);
                    } else
                    if(World.Rnd().nextFloat() < 0.04F)
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
                    else
                        this.FM.EI.engines[i].setReadyness(shot.initiator, this.FM.EI.engines[i].getReadyness() - 0.02F);
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[i].getCylindersRatio() * 0.9878F)
                    {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.05F, shot) > 0.0F)
                        this.FM.EI.engines[i].setKillCompressor(shot.initiator);
                    getEnergyPastArmor(2.0F, shot);
                }
                if(s.endsWith("oil1") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.25F, shot) > 0.0F)
                    this.FM.AS.hitOil(shot.initiator, 0);
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int j = s.charAt(6) - 49;
                if(getEnergyPastArmor(1.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.4F)
                {
                    if(this.FM.AS.astateTankStates[j] == 0)
                    {
                        this.FM.AS.hitTank(shot.initiator, j, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if(World.Rnd().nextFloat() < 0.003F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.2F)
                        this.FM.AS.hitTank(shot.initiator, j, 4);
                }
                return;
            }
            if(s.startsWith("xxmgun"))
            {
                if(s.endsWith("l"))
                {
                    debuggunnery("Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 0);
                }
                if(s.endsWith("r"))
                {
                    debuggunnery("Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 1);
                }
                if(s.endsWith("3"))
                {
                    debuggunnery("Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 2);
                }
                if(s.endsWith("4"))
                {
                    debuggunnery("Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 3);
                }
                if(s.endsWith("5"))
                {
                    debuggunnery("Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 4);
                }
                if(s.endsWith("6"))
                {
                    debuggunnery("Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 5);
                }
                if(s.endsWith("7"))
                {
                    debuggunnery("Cowling Gun: Disabled..");
                    this.FM.AS.setJamBullets(0, 6);
                }
                return;
            }
            if(s.startsWith("xxoil1"))
            {
                if(getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    getEnergyPastArmor(0.22F, shot);
                    debuggunnery("Engine Module: Oil Radiator 1 Pierced..");
                }
                return;
            }
            if(s.startsWith("xxoil2"))
            {
                if(getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    this.FM.AS.hitOil(shot.initiator, 1);
                    getEnergyPastArmor(0.22F, shot);
                    debuggunnery("Engine Module: Oil Radiator 2 Pierced..");
                }
                return;
            }
        }
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 2)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xnose"))
        {
            if(chunkDamageVisible("Nose") < 2)
                hitChunk("Nose", shot);
        } else
        if(s.startsWith("xtail1"))
        {
            if(chunkDamageVisible("Tail1") < 2)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel1"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xkeel2"))
        {
            if(chunkDamageVisible("Keel2") < 2)
                hitChunk("Keel2", shot);
        } else
        if(s.startsWith("xrudder1"))
        {
            if(chunkDamageVisible("Rudder1") < 2)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xrudder2"))
        {
            if(chunkDamageVisible("Rudder2") < 2)
                hitChunk("Rudder2", shot);
        } else
        if(s.startsWith("xstabl"))
        {
            if(chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
        } else
        if(s.startsWith("xstabr"))
        {
            if(chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvatorl"))
        {
            if(chunkDamageVisible("VatorL") < 2)
                hitChunk("VatorL", shot);
        } else
        if(s.startsWith("xvatorr"))
        {
            if(chunkDamageVisible("VatorR") < 2)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwinglin"))
        {
            if(chunkDamageVisible("WingLIn") < 2)
                hitChunk("WingLIn", shot);
        } else
        if(s.startsWith("xwingrin"))
        {
            if(chunkDamageVisible("WingRIn") < 2)
                hitChunk("WingRIn", shot);
        } else
        if(s.startsWith("xwinglout"))
        {
            if(chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
        } else
        if(s.startsWith("xwingrout"))
        {
            if(chunkDamageVisible("WingROut") < 2)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xaronel"))
        {
            if(chunkDamageVisible("AroneL") < 2)
                hitChunk("AroneL", shot);
        } else
        if(s.startsWith("xaroner"))
        {
            if(chunkDamageVisible("AroneR") < 2)
                hitChunk("AroneR", shot);
        } else
        if(s.startsWith("xflapcl"))
        {
            if(chunkDamageVisible("FlapCL") < 2)
                hitChunk("FlapCL", shot);
        } else
        if(s.startsWith("xflapcr"))
        {
            if(chunkDamageVisible("FlapCR") < 2)
                hitChunk("FlapCR", shot);
        } else
        if(s.startsWith("xflapw"))
        {
            if(chunkDamageVisible("FlapWL") < 2)
                hitChunk("FlapWL", shot);
        } else
        if(s.startsWith("xflapwr"))
        {
            if(chunkDamageVisible("FlapWR") < 2)
                hitChunk("FlapWR", shot);
        } else
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
        if(s.startsWith("xgear"))
            gearDamageFX(s);
        else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int k;
            if(s.endsWith("a") || s.endsWith("a2"))
            {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else
            if(s.endsWith("b") || s.endsWith("b2"))
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

    public boolean typeBomberToggleAutomation()
    {
        return false;
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

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    private void gearDamageFX(String s)
    {
        if(s.startsWith("xgearl"))
        {
            if(this.FM.isPlayers())
                HUD.log("Left Gear:  Hydraulic system Failed");
            kl = World.Rnd().nextFloat();
            kr = World.Rnd().nextFloat() * kl;
        } else
        if(s.startsWith("xgearr"))
        {
            if(this.FM.isPlayers())
                HUD.log("Right Gear:  Hydraulic system Failed");
            kr = World.Rnd().nextFloat();
            kl = World.Rnd().nextFloat() * kr;
        }
        this.FM.CT.GearControl = 0.4F;
        this.FM.Gears.setHydroOperable(false);
    }

    boolean bGunUp;
    public long btme;
    public float fGunPos;
    private static float kl = 1.0F;
    private static float kr = 1.0F;
    public boolean bChangedPit;

    static 
    {
        Class class1 = MB_174xyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryFrance);
    }
}
