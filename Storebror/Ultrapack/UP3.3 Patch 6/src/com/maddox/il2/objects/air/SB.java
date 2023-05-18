package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.Turret;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class SB extends Scheme2 implements TypeBomber
{

    public SB()
    {
        tme = 0L;
        topGunnerPosition = 0.0F;
        curTopGunnerPosition = 0.0F;
        topTurretPosition = 0.0F;
        curTurretPosition = 0.0F;
        radPosNum = 1;
        curTakeem = 0.0F;
        turretUp = 0.0F;
        prevAngle = 0.0F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
//        gun3 = getGunByHookName("_MGUN03");
//        gun4 = getGunByHookName("_MGUN04");
    }

    public void moveCockpitDoor(float f)
    {
        if(Config.isUSE_RENDER())
        {
            resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.7F);
            hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void setHumanControlledTurretAngels(Turret turret, float af[], HierMesh hiermesh, ActorHMesh actorhmesh)
    {
        super.setHumanControlledTurretAngels(turret, af, hiermesh, actorhmesh);
        if(turret == FM.turret[0])
            prevAngle = turret.tu[0];
    }

    public void update(float f)
    {
        super.update(f);
        float f1 = FM.turret[0].tu[0];
        if(FM.turret[0].bIsAIControlled && f1 == 0.0F && FM.turret[0].target == null)
            f1 = prevAngle;
        hierMesh().chunkSetAngles("Turret1C_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Turret1F_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Turret1D_D0", 0.0F, f1, 0.0F);
        if(FM.AS.astateTankStates[0] > 1 || FM.AS.astateTankStates[3] > 1)
            curTakeem += 0.02F * f;
        else
        if(FM.AS.astateTankStates[1] > 1 || FM.AS.astateTankStates[2] > 1)
            curTakeem += 0.05F * f;
        if(curTakeem > 0.5F)
            curTakeem = 0.5F;
        if(FM.turret.length != 0)
        {
            if(FM.AS.astatePilotStates[2] >= 100 && FM.AS.astatePilotStates[3] >= 100)
                return;
            FM.AS.astatePilotStates[2] = FM.AS.astatePilotStates[3] = (byte)Math.max(FM.AS.astatePilotStates[2], FM.AS.astatePilotStates[3]);
        }
        if(FM.turret.length != 0 && FM.turret[1].bIsAIControlled && FM.turret[2].bIsAIControlled)
        {
            if(Time.current() > tme)
            {
                tme = Time.current() + World.Rnd().nextLong(1000L, 5000L);
                if(FM.turret.length != 0)
                {
                    Actor actor = null;
                    for(int i = 0; i < 3; i++)
                        if(FM.turret[i].bIsOperable)
                            actor = FM.turret[i].target;

                    for(int j = 1; j < 3; j++)
                        FM.turret[j].target = actor;

                    if(actor != null)
                    {
                        if(Actor.isValid(actor))
                        {
                            pos.getAbs(Aircraft.tmpLoc2);
                            actor.pos.getAbs(Aircraft.tmpLoc3);
                            Aircraft.tmpLoc2.transformInv(Aircraft.tmpLoc3.getPoint());
                            if(Aircraft.tmpLoc3.getPoint().z > 0.0D)
                            {
                                setRadist(1);
                                topTurretPosition = 1.0F;
                                /*FM.turret[1].*/bMultiFunction = true;
                            } else
                            {
                                topGunnerPosition = 0.0F;
                            }
                        }
                    } else
                    {
                        topTurretPosition = 0.0F;
                        /*FM.turret[1].*/bMultiFunction = false;
                    }
                }
            }
            if(FM.AS.astatePilotStates[2] < 90)
            {
                if(topGunnerPosition == 0.0F && curTopGunnerPosition < 0.1F)
                    setRadist(2);
                if(curTopGunnerPosition > topGunnerPosition)
                    curTopGunnerPosition -= 0.3F * f;
                else
                    curTopGunnerPosition += 0.3F * f;
                if(curTopGunnerPosition > 0.1F)
                    FM.turret[2].bIsOperable = false;
                else
                    FM.turret[2].bIsOperable = radPosNum == 2;
                if(curTopGunnerPosition < 0.9F)
                    FM.turret[1].bIsOperable = false;
                else
                    FM.turret[1].bIsOperable = radPosNum == 1;
            }
            updateUpperTurret();
        } else
        {
            if(/*FM.turret[1].*/bMultiFunction)
                topTurretPosition = 1.0F;
            else
                topTurretPosition = 0.0F;
            if(!FM.turret[1].bIsAIControlled)
            {
                FM.turret[2].bIsOperable = false;
                FM.turret[2].bIsAIControlled = true;
                FM.turret[1].bIsOperable = true;
                setRadist(1);
            }
            if(!FM.turret[2].bIsAIControlled)
            {
                FM.turret[1].bIsOperable = false;
                FM.turret[1].bIsAIControlled = true;
                FM.turret[2].bIsOperable = true;
                setRadist(2);
            }
            if(curTopGunnerPosition > topGunnerPosition)
                curTopGunnerPosition -= 0.3F * f;
            else
                curTopGunnerPosition += 0.3F * f;
            updateUpperTurret();
        }
        if(curTurretPosition > topTurretPosition)
            curTurretPosition -= 0.3F * f;
        else
            curTurretPosition += 0.3F * f;
        kSAim = -FM.Or.getKren();
        tSAim = -FM.Or.getTangage();
        if(kSAim > 5F)
            kSAim = 5F;
        else
        if(kSAim < -5F)
            kSAim = -5F;
        if(tSAim > 5F)
            tSAim = 5F;
        else
        if(tSAim < -5F)
            tSAim = -5F;
    }

    private void updateUpperTurret()
    {
        if(curTopGunnerPosition > 0.01F)
        {
            resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(curTurretPosition, 0.01F, 0.5F, 0.0F, -1.16F);
            Aircraft.xyz[2] = Aircraft.cvt(curTurretPosition, 0.01F, 0.5F, 0.0F, 0.1F);
            float f = Math.abs(FM.turret[1].tu[0]);
            turretUp = Aircraft.cvt(curTurretPosition, 0.01F, 0.99F, 0.0F, 40F);
            if(f < 2.0F || !FM.turret[1].bIsAIControlled)
            {
                if((this instanceof SB_2M100A) || (this instanceof SB_2M103_96))
                {
                    float f1 = Aircraft.cvt(curTurretPosition, 0.5F, 0.9F, 0.0F, 40F);
                    hierMesh().chunkSetLocate("Blister3_D0", Aircraft.xyz, Aircraft.ypr);
                    hierMesh().chunkSetAngles("Turret2M1_D0", 0.0F, f1, 0.0F);
                    hierMesh().chunkSetAngles("Turret2M2_D0", 0.0F, -f1, 0.0F);
                } else
                {
                    float f2 = Aircraft.cvt(curTurretPosition, 0.5F, 0.9F, 0.0F, 90F);
                    hierMesh().chunkSetAngles("Turret2C_D0", 0.0F, 0.0F, -f2);
                    hierMesh().chunkSetAngles("Turret2D_D0", 0.0F, -f2, 0.0F);
                }
                if(FM.turret[1].target == null && FM.turret[1].bIsAIControlled)
                {
                    hierMesh().setCurChunk(FM.turret[1].indexA);
                    hierMesh().chunkSetAngles(FM.turret[1].tu);
                    hierMesh().setCurChunk(FM.turret[1].indexB);
                    hierMesh().chunkSetAngles(FM.turret[1].tu);
                }
            }
            if(curTopGunnerPosition > 0.6F && curTopGunnerPosition < 0.9F)
                FM.turret[1].target = null;
            resetYPRmodifier();
            if((this instanceof SB_2M100A) || (this instanceof SB_2M103_96))
            {
                Aircraft.xyz[2] = Aircraft.cvt(curTopGunnerPosition, 0.0F, 0.8F, -0.27F, 0.0F);
                Aircraft.xyz[1] = Aircraft.cvt(curTopGunnerPosition, 0.0F, 0.8F, 0.1F, 0.0F);
                Aircraft.ypr[0] = Aircraft.cvt(curTopGunnerPosition, 0.0F, 0.8F, 10F, 0.0F);
                Aircraft.ypr[2] = Aircraft.cvt(curTopGunnerPosition, 0.0F, 0.8F, 15F, 0.0F);
                hierMesh().chunkSetLocate("Pilot3_D0", Aircraft.xyz, Aircraft.ypr);
            } else
            {
                resetYPRmodifier();
                Aircraft.xyz[2] = Aircraft.cvt(curTopGunnerPosition, 0.6F, 0.9F, 0.0F, 0.3F);
                hierMesh().chunkSetLocate("Pilot3_D0", Aircraft.xyz, Aircraft.ypr);
            }
        }
    }

    private void setRadist(int i)
    {
        radPosNum = i;
        if(FM.AS.astatePilotStates[2] > 99)
            return;
        hierMesh().chunkVisible("Pilot3_D0", false);
        hierMesh().chunkVisible("Pilot4_D0", false);
        hierMesh().chunkVisible("HMask3_D0", false);
        hierMesh().chunkVisible("HMask4_D0", false);
        FM.turret[1].bIsOperable = false;
        FM.turret[2].bIsOperable = false;
        switch(i)
        {
        case 1:
            hierMesh().chunkVisible("Pilot3_D0", true);
            hierMesh().chunkVisible("HMask3_D0", FM.Loc.z > 3000D);
            FM.turret[1].bIsOperable = true;
            topGunnerPosition = 1.0F;
            break;

        case 2:
            hierMesh().chunkVisible("Pilot4_D0", true);
            hierMesh().chunkVisible("HMask4_D0", FM.Loc.z > 3000D);
            FM.turret[2].bIsOperable = true;
            topGunnerPosition = 0.0F;
            break;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -175F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -155F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, 70F), 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.05F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 100F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 5F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -175F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -155F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f1, 0.0F, 0.05F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f1, 0.0F, 0.05F, 0.0F, 70F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        moveGear(hiermesh, f, f, f);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f, f, f);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, 0.0F);
    }

    public void moveWheelSink()
    {
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
        {
            if(FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.39F)
                FM.AS.hitTank(this, 0, 1);
            if(FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.39F)
                FM.AS.hitTank(this, 1, 1);
            if(FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.08F)
                nextDMGLevel(FM.AS.astateEffectChunks[0] + "0", 0, this);
            if(FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.08F)
                nextDMGLevel(FM.AS.astateEffectChunks[1] + "0", 0, this);
            if(FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.08F)
                nextDMGLevel(FM.AS.astateEffectChunks[2] + "0", 0, this);
            if(FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.08F)
                nextDMGLevel(FM.AS.astateEffectChunks[3] + "0", 0, this);
            if(FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.16F)
                FM.AS.hitTank(this, 1, 1);
            if(FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.16F)
                FM.AS.hitTank(this, 1, 0);
            if(FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.16F)
                FM.AS.hitTank(this, 1, 3);
            if(FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.16F)
                FM.AS.hitTank(this, 1, 2);
        }
        for(int i = 1; i < 5; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("BayL_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("BayR_D0", 0.0F, 90F * f, 0.0F);
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
            if(f < -20F)
            {
                f = -20F;
                flag = false;
            }
            if(f > 20F)
            {
                f = 20F;
                flag = false;
            }
            if(f1 < -50F)
            {
                f1 = -50F;
                flag = false;
            }
            if(f1 > 45F)
            {
                f1 = 45F;
                flag = false;
            }
            break;

        case 1:
            if(!FM.turret[2].bIsAIControlled)
                flag = false;
            if((this instanceof SB_2M100A) || (this instanceof SB_2M103_96))
            {
                if(f < -100F)
                {
                    f = -100F;
                    flag = false;
                }
                if(f > 100F)
                {
                    f = 100F;
                    flag = false;
                }
                if(f1 < -10F)
                {
                    f1 = -10F;
                    flag = false;
                }
                if(f1 > 50F)
                {
                    f1 = 50F;
                    flag = false;
                }
                float f2 = Math.abs(f);
                if(f1 < 17F && f2 < 1.0F)
                {
                    f = 0.0F;
                    flag = false;
                }
                if(f2 < 30F)
                {
                    float f4 = 0.0F;
                    float f6 = 0.0F;
                    float f7 = 0.0F;
                    float f8 = 0.0F;
                    if(f2 < 1.0F)
                    {
                        f7 = -10F;
                        f8 = -10F;
                    } else
                    if(f2 < 5F)
                    {
                        f4 = 0.0F;
                        f6 = 5F;
                        f7 = 17F;
                        f8 = 16.3F;
                    } else
                    if(f2 < 10F)
                    {
                        f4 = 5F;
                        f6 = 10F;
                        f7 = 16.3F;
                        f8 = 14.5F;
                    } else
                    if(f2 < 15F)
                    {
                        f4 = 10F;
                        f6 = 15F;
                        f7 = 14.5F;
                        f8 = 11.5F;
                    } else
                    if(f2 < 20F)
                    {
                        f4 = 15F;
                        f6 = 20F;
                        f7 = 11.5F;
                        f8 = 5.8F;
                    } else
                    if(f2 < 25F)
                    {
                        f4 = 20F;
                        f6 = 25F;
                        f7 = 5.8F;
                        f8 = -2.2F;
                    } else
                    if(f2 < 30F)
                    {
                        f4 = 25F;
                        f6 = 30F;
                        f7 = -2.2F;
                        f8 = -10F;
                    }
                    float f9 = Aircraft.cvt(f2, f4, f6, f7, f8);
                    if(f1 < f9)
                    {
                        f1 = f9;
                        flag = false;
                    }
                }
                if(turretUp >= 30F)
                    break;
                f = 0.0F;
                if(f1 > 15F)
                {
                    f1 = 15F;
                    flag = false;
                }
                break;
            }
            if(f1 > 85F)
            {
                f1 = 85F;
                flag = false;
            } else
            if(f1 < -45F)
            {
                f1 = -45F;
                flag = false;
            }
            float f3 = Math.abs(f);
            float f5 = 0.0F;
            if(f3 < 50F)
            {
                if(f3 < 1.0F)
                {
                    if(f1 < 17F)
                    {
                        f1 = 17F;
                        flag = false;
                    }
                } else
                if(f3 < 5F)
                    f5 = Aircraft.cvt(f, 0.0F, 5F, 17F, 16.3F);
                else
                if(f3 < 10F)
                    f5 = Aircraft.cvt(f, 5F, 10F, 16.3F, 14.5F);
                else
                if(f3 < 15F)
                    f5 = Aircraft.cvt(f, 10F, 15F, 14.5F, 11.5F);
                else
                if(f3 < 20F)
                    f5 = Aircraft.cvt(f, 15F, 20F, 11.5F, 5.8F);
                else
                if(f3 < 25F)
                    f5 = Aircraft.cvt(f, 20F, 25F, 5.8F, -2.2F);
                else
                if(f3 < 30F)
                    f5 = Aircraft.cvt(f, 25F, 30F, -2.2F, -10F);
                else
                if(f3 < 50F)
                    f5 = Aircraft.cvt(f, 30F, 50F, -10F, -45F);
            } else
            if(f3 > 105F)
                if(f3 > 165F)
                    f5 = Aircraft.cvt(f, 165F, 180F, 25F, 30F);
                else
                if(f3 > 140F)
                    f5 = Aircraft.cvt(f, 140F, 165F, -20F, 25F);
                else
                if(f3 > 105F)
                    f5 = Aircraft.cvt(f, 105F, 140F, -45F, -20F);
            if(f1 < f5)
            {
                f1 = f5;
                flag = false;
            }
            break;

        case 2:
            if(!FM.turret[1].bIsAIControlled)
                flag = false;
            if(f < -25F)
            {
                f = -25F;
                flag = false;
            }
            if(f > 25F)
            {
                f = 25F;
                flag = false;
            }
            if(f1 < -70F)
            {
                f1 = -70F;
                flag = false;
            }
            if(f1 > 0.0F)
            {
                f1 = 0.0F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33:
            hitProp(0, j, actor);
            return super.cutFM(34, j, actor);

        case 36:
            hitProp(1, j, actor);
            return super.cutFM(37, j, actor);

        case 19:
            FM.AS.setJamBullets(12, 0);
            if(FM.turret.length > 0)
                FM.turret[2].bIsOperable = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
                if(s.endsWith("p1"))
                    getEnergyPastArmor(0.2F, shot);
                else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(0.2F, shot);
                else
                if(s.endsWith("p3"))
                    getEnergyPastArmor(0.2F, shot);
            if(s.startsWith("xxcontrols"))
            {
                int i = s.charAt(10) - 48;
                switch(i)
                {
                default:
                    break;

                case 1:
                    if(World.Rnd().nextFloat() < 0.05F || shot.mass > 0.092F && World.Rnd().nextFloat() < 0.1F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        if(World.Rnd().nextFloat() < 0.5F)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                    }
                    break;

                case 2:
                    if(World.Rnd().nextFloat() >= 0.05F && (shot.mass <= 0.092F || World.Rnd().nextFloat() >= 0.1F))
                        break;
                    if(World.Rnd().nextFloat() < 0.1F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                    if(World.Rnd().nextFloat() < 0.5F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                    break;

                case 3:
                    if(getEnergyPastArmor(1.0F, shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.12F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.12F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls Out..");
                    }
                    break;
                }
            }
            if(s.startsWith("xxspar"))
            {
                if(s.startsWith("xxspart") && World.Rnd().nextFloat() < 0.36F && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(6.8F, shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    msgCollision(this, "Tail1_D0", "Tail1_D0");
                }
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(14.8F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(14.8F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(12.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(12.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && (double)World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(9.1F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && (double)World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(9.1F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(5.2F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(5.2F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if(s.startsWith("xxspare1") && getEnergyPastArmor(28F, shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Engine1 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if(s.startsWith("xxspare2") && getEnergyPastArmor(28F, shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Engine2 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
            }
            if(s.startsWith("xxbmb") && World.Rnd().nextFloat() < 0.01F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets())
            {
                Aircraft.debugprintln(this, "*** Bomb Payload Detonates..");
                FM.AS.hitTank(shot.initiator, 0, 10);
                FM.AS.hitTank(shot.initiator, 1, 10);
                FM.AS.hitTank(shot.initiator, 2, 10);
                FM.AS.hitTank(shot.initiator, 3, 10);
                nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            if(s.startsWith("xxeng"))
            {
                int j = s.charAt(5) - 49;
                if(s.endsWith("prop") && getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 0.4F), shot) > 0.0F)
                {
                    FM.EI.engines[j].setKillPropAngleDevice(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Prop Governor Failed..");
                }
                if(s.endsWith("gear") && getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 1.1F), shot) > 0.0F)
                {
                    FM.EI.engines[j].setKillPropAngleDeviceSpeeds(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Prop Governor Damaged..");
                }
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 6.8F), shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 200000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, j);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            FM.AS.hitEngine(shot.initiator, j, 2);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Engine Damaged..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 28000F)
                        {
                            FM.EI.engines[j].setCyliderKnockOut(shot.initiator, 1);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Cylinder Feed Out, " + FM.EI.engines[j].getCylindersOperable() + "/" + FM.EI.engines[j].getCylinders() + " Left..");
                        }
                        if(World.Rnd().nextFloat() < 0.08F)
                        {
                            FM.EI.engines[j].setEngineStuck(shot.initiator);
                            Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Ball Bearing Jammed - Engine Stuck..");
                        }
                        FM.EI.engines[j].setReadyness(shot.initiator, FM.EI.engines[j].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Readyness Reduced to " + FM.EI.engines[j].getReadyness() + "..");
                    }
                    if(World.Rnd().nextFloat() < 0.01F)
                    {
                        FM.EI.engines[j].setEngineStops(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Engine Stalled..");
                    }
                    if(World.Rnd().nextFloat() < 0.01F)
                    {
                        FM.AS.hitEngine(shot.initiator, j, 10);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Crank Case Hit - Fuel Feed Hit - Engine Flamed..");
                    }
                    getEnergyPastArmor(6F, shot);
                }
                if((s.endsWith("cyl1") || s.endsWith("cyl2")) && getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.542F), shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[j].getCylindersRatio() * 1.72F)
                {
                    FM.EI.engines[j].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Cylinders Hit, " + FM.EI.engines[j].getCylindersOperable() + "/" + FM.EI.engines[j].getCylinders() + " Left..");
                    if(World.Rnd().nextFloat() < 0.01F)
                    {
                        FM.EI.engines[j].setEngineStuck(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Cylinder Case Broken - Engine Stuck..");
                    }
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        FM.AS.hitEngine(shot.initiator, j, 3);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Cylinders Hit - Engine Fires..");
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(3F, 46.7F), shot);
                }
                if(s.endsWith("supc") && getEnergyPastArmor(0.05F, shot) > 0.0F && World.Rnd().nextFloat() < 0.89F)
                {
                    FM.EI.engines[j].setKillCompressor(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Supercharger Out..");
                }
                if(s.endsWith("eqpt") && getEnergyPastArmor(World.Rnd().nextFloat(0.001F, 0.2F), shot) > 0.0F && World.Rnd().nextFloat() < 0.89F)
                {
                    if(World.Rnd().nextFloat() < 0.11F)
                    {
                        FM.EI.engines[j].setMagnetoKnockOut(shot.initiator, World.Rnd().nextInt(0, 1));
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Magneto Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.11F)
                    {
                        FM.EI.engines[j].setKillCompressor(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine" + (j + 1) + " Compressor Feed Out..");
                    }
                }
            }
            if(s.startsWith("xxoil"))
            {
                int k = 0;
                if(s.endsWith("2"))
                    k = 1;
                if(getEnergyPastArmor(0.21F, shot) > 0.0F)
                    FM.AS.hitOil(shot.initiator, k);
            }
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.03F, shot) > 0.0F)
                {
                    if(FM.AS.astateTankStates[l] == 0)
                    {
                        FM.AS.hitTank(shot.initiator, l, 2);
                        FM.AS.doSetTankState(shot.initiator, l, 2);
                    }
                    if(shot.powerType == 3)
                        if(shot.power < 14100F)
                        {
                            if(FM.AS.astateTankStates[l] < 4 && World.Rnd().nextFloat() < 0.1F)
                                FM.AS.hitTank(shot.initiator, l, 1);
                        } else
                        {
                            FM.AS.hitTank(shot.initiator, l, World.Rnd().nextInt(0, (int)(shot.power / 28200F)));
                        }
                }
            }
            if(s.startsWith("xxhyd"))
                FM.AS.setInternalDamage(shot.initiator, 3);
            if(s.startsWith("xxpnm"))
                FM.AS.setInternalDamage(shot.initiator, 1);
            if(s.equals("xxftonrglass"))
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            if(s.equals("xxcanopyleft"))
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
            if(s.equals("xxcanopyright"))
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
            if(s.equals("xxcanopytop"))
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
            if(s.equals("xxpanel"))
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            return;
        }
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(shot.power > 33000F && point3d.x > 1.0D)
            {
                FM.AS.hitPilot(shot.initiator, 0, World.Rnd().nextInt(30, 192));
                FM.AS.hitPilot(shot.initiator, 1, World.Rnd().nextInt(30, 192));
            }
        } else
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
            hitChunk("VatorL", shot);
        else
        if(s.startsWith("xvatorr"))
            hitChunk("VatorR", shot);
        else
        if(s.startsWith("xwinglin"))
        {
            if((FM.AS.astateTankStates[0] > 1 || FM.AS.astateTankStates[1] > 1) && shot.powerType == 3 && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.33F && World.Rnd().nextFloat() < curTakeem)
                FM.AS.hitTank(shot.initiator, World.Rnd().nextInt(0, 1), 3);
            if(chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
        } else
        if(s.startsWith("xwingrin"))
        {
            if((FM.AS.astateTankStates[2] > 1 || FM.AS.astateTankStates[3] > 1) && shot.powerType == 3 && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < 0.33F && World.Rnd().nextFloat() < curTakeem)
                FM.AS.hitTank(shot.initiator, World.Rnd().nextInt(2, 3), 3);
            if(chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
        } else
        if(s.startsWith("xwinglmid"))
        {
            if(chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
        } else
        if(s.startsWith("xwingrmid"))
        {
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
            FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 168000F));
            Aircraft.debugprintln(this, "*** Engine1 Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
        } else
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
            FM.EI.engines[1].setReadyness(shot.initiator, FM.EI.engines[1].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 168000F));
            Aircraft.debugprintln(this, "*** Engine2 Hit - Readyness Reduced to " + FM.EI.engines[1].getReadyness() + "..");
        } else
        if(s.startsWith("xgear"))
        {
            if(World.Rnd().nextFloat() < 0.1F)
            {
                Aircraft.debugprintln(this, "*** Gear Hydro Failed..");
                FM.Gears.setHydroOperable(false);
            }
        } else
        if(s.startsWith("xturret"))
        {
            if(s.startsWith("xturret1"))
            {
                FM.AS.setJamBullets(10, 0);
                FM.AS.setJamBullets(10, 1);
            }
            if(s.startsWith("xturret2"))
                FM.AS.setJamBullets(11, 0);
            if(s.startsWith("xturret3"))
                FM.AS.setJamBullets(12, 0);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int i1;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                i1 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                i1 = s.charAt(6) - 49;
            } else
            {
                i1 = s.charAt(5) - 49;
            }
            hitFlesh(i1, shot, byte0);
        }
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 1:
            FM.turret[0].setHealth(f);
            break;

        case 2:
        case 3:
            FM.turret[1].setHealth(f);
            FM.turret[2].setHealth(f);
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 3:
        default:
            break;

        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            if(World.cur().isHighGore())
            {
                hierMesh().chunkVisible("Gore2_D0", true);
                hierMesh().chunkVisible("Gore1_D0", hierMesh().isChunkVisible("Blister1_D0"));
            }
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            hierMesh().chunkVisible("HMask2_D0", false);
            break;

        case 2:
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            hierMesh().chunkVisible("HMask3_D0", false);
            break;
        }
    }

    public void doRemoveBodyFromPlane(int i)
    {
        super.doRemoveBodyFromPlane(i);
        if(i >= 3)
        {
            doRemoveBodyChunkFromPlane("Pilot4");
            doRemoveBodyChunkFromPlane("Head4");
        }
    }
    
    
    
    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
        this.fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus() {
        this.fSightCurForwardAngle += 0.2F;
        if (this.fSightCurForwardAngle > 75F) this.fSightCurForwardAngle = 75F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjDistanceMinus() {
        this.fSightCurForwardAngle -= 0.2F;
        if (this.fSightCurForwardAngle < -15F) this.fSightCurForwardAngle = -15F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) (this.fSightCurForwardAngle * 1.0F)) });
    }

    public void typeBomberAdjSideslipReset() {
        this.fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus() {
        this.fSightCurSideslip++;
        if (this.fSightCurSideslip > 45F) this.fSightCurSideslip = 45F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
    }

    public void typeBomberAdjSideslipMinus() {
        this.fSightCurSideslip--;
        if (this.fSightCurSideslip < -45F) this.fSightCurSideslip = -45F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Integer((int) (this.fSightCurSideslip * 1.0F)) });
    }

    public void typeBomberAdjAltitudeReset() {
        this.fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus() {
        this.fSightCurAltitude += 10F;
        if (this.fSightCurAltitude > 10000F) this.fSightCurAltitude = 10000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjAltitudeMinus() {
        this.fSightCurAltitude -= 10F;
        if (this.fSightCurAltitude < 300F) this.fSightCurAltitude = 300F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
    }

    public void typeBomberAdjSpeedReset() {
        this.fSightCurSpeed = 50F;
    }

    public void typeBomberAdjSpeedPlus() {
        this.fSightCurSpeed += 5F;
        if (this.fSightCurSpeed > 520F) this.fSightCurSpeed = 520F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberAdjSpeedMinus() {
        this.fSightCurSpeed -= 5F;
        if (this.fSightCurSpeed < 50F) this.fSightCurSpeed = 50F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
    }

    public void typeBomberUpdate(float f) {
        double d = this.fSightCurSpeed / 3.6D * Math.sqrt(this.fSightCurAltitude * 0.203873598D);
        d -= this.fSightCurAltitude * this.fSightCurAltitude * 1.419E-005D;
        this.fSightSetForwardAngle = (float) Math.toDegrees(Math.atan(d / this.fSightCurAltitude));
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeFloat(this.fSightCurAltitude);
        netmsgguaranted.writeFloat(this.fSightCurSpeed);
        netmsgguaranted.writeFloat(this.fSightCurForwardAngle);
        netmsgguaranted.writeFloat(this.fSightCurSideslip);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.fSightCurAltitude = netmsginput.readFloat();
        this.fSightCurSpeed = netmsginput.readFloat();
        this.fSightCurForwardAngle = netmsginput.readFloat();
        this.fSightCurSideslip = netmsginput.readFloat();
    }

    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurForwardAngle;
    public float fSightSetForwardAngle;
    public float fSightCurSideslip;



    private long tme;
    private float topGunnerPosition;
    private float curTopGunnerPosition;
    private float topTurretPosition;
    protected float curTurretPosition;
    private int radPosNum;
//    private Gun gun3;
//    private Gun gun4;
    private float curTakeem;
    public float turretUp;
    private float prevAngle;
    public boolean bMultiFunction = false;
    public float tSAim;
    public float kSAim;

    static 
    {
        Class class1 = SB.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
