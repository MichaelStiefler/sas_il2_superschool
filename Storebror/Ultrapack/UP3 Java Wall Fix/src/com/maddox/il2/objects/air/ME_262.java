package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public class ME_262 extends Scheme2
    implements TypeFighter, TypeBNZFighter
{

    public ME_262()
    {
        trimElevator = 0.0F;
        bHasElevatorControl = true;
        cockpitDoor_ = 0.0F;
        fMaxKMHSpeedForOpenCanopy = 200F;
        bHasBlister = true;
        X = 1.0F;
        s17 = s18 = 0.1F;
        s31 = s32 = 0.4F;
    }

    protected void moveElevator(float f)
    {
        f -= trimElevator;
        if(!(this instanceof ME_262HGII))
        {
            hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
            hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
        } else
        {
            super.moveElevator(f);
        }
    }

    private void cutOp(int i)
    {
        this.FM.Operate &= ~(1L << i);
    }

    protected boolean getOp(int i)
    {
        return (this.FM.Operate & 1L << i) != 0L;
    }

    private float Op(int i)
    {
        return getOp(i) ? 1.0F : 0.0F;
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        if(!getOp(i))
            return false;
        if(!(this instanceof ME_262HGII))
            switch(i)
            {
            case 33:
                return super.cutFM(34, j, actor);

            case 36:
                return super.cutFM(37, j, actor);

            case 11:
                cutFM(17, j, actor);
                this.FM.cut(17, j, actor);
                cutFM(18, j, actor);
                this.FM.cut(18, j, actor);
                return super.cutFM(i, j, actor);

            case 17:
                cut("StabL");
                cutOp(17);
                this.FM.setCapableOfACM(false);
                if(World.Rnd().nextInt(-1, 8) < this.FM.Skill)
                    this.FM.setReadyToReturn(true);
                if(World.Rnd().nextInt(-1, 16) < this.FM.Skill)
                    this.FM.setReadyToDie(true);
                this.FM.Sq.liftStab *= 0.5F * Op(18) + 0.1F;
                this.FM.Sq.liftWingLIn *= 1.05F;
                this.FM.Sq.liftWingRIn *= 0.95F;
                this.FM.Sq.dragProducedCx -= 0.06F;
                if(Op(18) == 0.0F)
                {
                    this.FM.SensPitch = 0.0F;
                    this.FM.setGCenter(0.2F);
                } else
                {
                    this.FM.setGCenter(0.15F);
                    s17 = 0.0F;
                    this.FM.SensPitch *= s17 + s18 + s31 + s32;
                    X = 1.0F / (s17 + s18 + s31 + s32);
                    s18 *= X;
                    s31 *= X;
                    s32 *= X;
                }
                // fall through

            case 31:
                if(Op(31) == 0.0F)
                    return false;
                cut("VatorL");
                cutOp(31);
                if(Op(32) == 0.0F)
                {
                    bHasElevatorControl = false;
                    this.FM.setCapableOfACM(false);
                    if(Op(18) == 0.0F)
                        this.FM.setReadyToDie(true);
                }
                this.FM.Sq.squareElevators *= 0.5F * Op(32);
                this.FM.Sq.dragProducedCx += 0.06F;
                s31 = 0.0F;
                this.FM.SensPitch *= s17 + s18 + s31 + s32;
                X = 1.0F / (s17 + s18 + s31 + s32);
                s17 *= X;
                s18 *= X;
                s32 *= X;
                return false;

            case 18:
                cut("StabR");
                cutOp(18);
                this.FM.setCapableOfACM(false);
                if(World.Rnd().nextInt(-1, 8) < this.FM.Skill)
                    this.FM.setReadyToReturn(true);
                if(World.Rnd().nextInt(-1, 16) < this.FM.Skill)
                    this.FM.setReadyToDie(true);
                this.FM.Sq.liftStab *= 0.5F * Op(17) + 0.1F;
                this.FM.Sq.liftWingLIn *= 0.95F;
                this.FM.Sq.liftWingRIn *= 1.05F;
                this.FM.Sq.dragProducedCx -= 0.06F;
                if(Op(17) == 0.0F)
                {
                    this.FM.SensPitch = 0.0F;
                    this.FM.setGCenter(0.2F);
                } else
                {
                    this.FM.setGCenter(0.15F);
                    s18 = 0.0F;
                    this.FM.SensPitch *= s17 + s18 + s31 + s32;
                    X = 1.0F / (s17 + s18 + s31 + s32);
                    s17 *= X;
                    s31 *= X;
                    s32 *= X;
                }
                // fall through

            case 32:
                if(Op(32) == 0.0F)
                    return false;
                cut("VatorR");
                cutOp(32);
                if(Op(31) == 0.0F)
                {
                    bHasElevatorControl = false;
                    this.FM.setCapableOfACM(false);
                    if(Op(17) == 0.0F)
                        this.FM.setReadyToDie(true);
                }
                this.FM.Sq.squareElevators *= 0.5F * Op(31);
                this.FM.Sq.dragProducedCx += 0.06F;
                s32 = 0.0F;
                this.FM.SensPitch *= s17 + s18 + s31 + s32;
                X = 1.0F / (s17 + s18 + s31 + s32);
                s17 *= X;
                s18 *= X;
                s31 *= X;
                return false;
            }
        return super.cutFM(i, j, actor);
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            if(!this.FM.AS.bIsAboutToBailout)
            {
                if(hierMesh().isChunkVisible("Blister1_D0"))
                    hierMesh().chunkVisible("Gore1_D0", true);
                hierMesh().chunkVisible("Gore2_D0", true);
            }
            break;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 111F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC21_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 73F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 73F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 88F * f, 0.0F);
        float f1 = Math.max(-f * 1500F, -90F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, f1, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        float f = this.FM.Gears.gWheelSinking[2];
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 0.19F, 0.0F, 0.19F);
        hierMesh().chunkSetLocate("GearC22_D0", Aircraft.xyz, Aircraft.ypr);
        f = Aircraft.cvt(f, 0.0F, 19F, 0.0F, 30F);
        hierMesh().chunkSetAngles("GearC7_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("GearC8_D0", 0.0F, 2.0F * f, 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        if(this.FM.CT.getGear() > 0.75F)
            hierMesh().chunkSetAngles("GearC21_D0", 0.0F, 40F * f, 0.0F);
    }

    public void moveCockpitDoor(float f)
    {
        if(bHasBlister)
        {
            resetYPRmodifier();
            hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 100F * f, 0.0F);
            if(Config.isUSE_RENDER())
            {
                if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                    Main3D.cur3D().cockpits[0].onDoorMoved(f);
                setDoorSnd(f);
            }
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(point3d.x > 1.7D)
            {
                if(World.Rnd().nextFloat() < 0.07F)
                    this.FM.AS.setJamBullets(0, 0);
                if(World.Rnd().nextFloat() < 0.07F)
                    this.FM.AS.setJamBullets(0, 1);
                if(World.Rnd().nextFloat() < 0.12F)
                    this.FM.AS.setJamBullets(1, 0);
                if(World.Rnd().nextFloat() < 0.12F)
                    this.FM.AS.setJamBullets(1, 1);
            }
            if(point3d.x > -0.999D && point3d.x < 0.535D && point3d.z > -0.224D)
            {
                if(World.Rnd().nextFloat() < 0.1F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                if(World.Rnd().nextFloat() < 0.1F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                if(World.Rnd().nextFloat() < 0.1F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                if(World.Rnd().nextFloat() < 0.1F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
                if(World.Rnd().nextFloat() < 0.1F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
                if(World.Rnd().nextFloat() < 0.1F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                if(World.Rnd().nextFloat() < 0.1F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
            }
            if(point3d.x > 0.8D && point3d.x < 1.58D && World.Rnd().nextFloat() < 0.25F && (shot.powerType == 3 && getEnergyPastArmor(0.4F, shot) > 0.0F || shot.powerType == 0))
                this.FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(1, (int)(shot.power / 4000F)));
            if(point3d.x > -2.485D && point3d.x < -1.6D && World.Rnd().nextFloat() < 0.25F && (shot.powerType == 3 && getEnergyPastArmor(0.4F, shot) > 0.0F || shot.powerType == 0))
                this.FM.AS.hitTank(shot.initiator, 1, World.Rnd().nextInt(1, (int)(shot.power / 4000F)));
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
            hitChunk("Keel1", shot);
        else
        if(s.startsWith("xstabl"))
            hitChunk("StabL", shot);
        else
        if(s.startsWith("xstabr"))
            hitChunk("StabR", shot);
        else
        if(s.startsWith("xwing"))
        {
            if(s.endsWith("lin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.endsWith("rin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.endsWith("lmid") && chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(s.endsWith("rmid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            if(s.endsWith("lout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.endsWith("rout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xengine"))
        {
            int i = s.charAt(7) - 49;
            if(point3d.x > 0.0D && point3d.x < 0.697D)
                this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, 6));
            if(World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass)
                this.FM.AS.hitEngine(shot.initiator, i, 5);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int j;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                j = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                j = s.charAt(6) - 49;
            } else
            {
                j = s.charAt(5) - 49;
            }
            hitFlesh(j, shot, byte0);
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag && World.Rnd().nextFloat() < 0.2F)
        {
            if(this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.12F)
            {
                this.FM.AS.explodeEngine(this, 0);
                msgCollision(this, "WingLIn_D0", "WingLIn_D0");
                if(World.Rnd().nextBoolean())
                    this.FM.AS.hitTank(this, 0, World.Rnd().nextInt(1, 8));
                else
                    this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(1, 8));
            }
            if(this.FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.12F)
            {
                this.FM.AS.explodeEngine(this, 1);
                msgCollision(this, "WingRIn_D0", "WingRIn_D0");
                if(World.Rnd().nextBoolean())
                    this.FM.AS.hitTank(this, 0, World.Rnd().nextInt(1, 8));
                else
                    this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(1, 8));
            }
        }
        if(this.FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Head1_D0"));
        if(this.FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask2_D0", false);
        else
            hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Head2_D0"));
    }

    public void engineSurge(float f)
    {
        for(int i = 0; i < 2; i++)
            if(curthrl[i] == -1F)
            {
                curthrl[i] = oldthrl[i] = this.FM.EI.engines[i].getControlThrottle();
            } else
            {
                curthrl[i] = this.FM.EI.engines[i].getControlThrottle();
                if((curthrl[i] - oldthrl[i]) / f > 8F && this.FM.EI.engines[i].getRPM() < 3200F && this.FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.5F)
                {
                    if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                        HUD.log("Compressor Stall!");
                    this.playSound("weapon.MGunMk108s", true);
                    engineSurgeDamage[i] += 0.01D * (this.FM.EI.engines[i].getRPM() / 1000F);
                    this.FM.EI.engines[i].doSetReadyness(this.FM.EI.engines[i].getReadyness() - engineSurgeDamage[i]);
                    if(World.Rnd().nextFloat() < 0.2F && (this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
                        this.FM.AS.hitEngine(this, i, 100);
                    if(World.Rnd().nextFloat() < 0.2F && (this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
                        this.FM.EI.engines[i].setEngineDies(this);
                }
                if((curthrl[i] - oldthrl[i]) / f < -8F && (curthrl[i] - oldthrl[i]) / f > -100F && this.FM.EI.engines[i].getRPM() < 3200F && this.FM.EI.engines[i].getStage() == 6)
                {
                    this.playSound("weapon.MGunMk108s", true);
                    engineSurgeDamage[i] += 0.001D * (this.FM.EI.engines[i].getRPM() / 1000F);
                    this.FM.EI.engines[i].doSetReadyness(this.FM.EI.engines[i].getReadyness() - engineSurgeDamage[i]);
                    if(World.Rnd().nextFloat() < 0.5F && (this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode())
                    {
                        if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                            HUD.log("Engine Flameout!");
                        this.FM.EI.engines[i].setEngineStops(this);
                    } else
                    {
                        if(((Interpolate) (this.FM)).actor == World.getPlayerAircraft())
                            HUD.log("Compressor Stall!");
                        this.FM.EI.engines[i].setKillCompressor(this);
                    }
                }
                oldthrl[i] = curthrl[i];
            }

    }

    public void update(float f)
    {
        if(this.FM.getSpeed() > 5F)
        {
            hierMesh().chunkSetAngles("SlatL0_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, -1F), 0.0F);
            hierMesh().chunkSetAngles("SlatL1_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, -1F), 0.0F);
            hierMesh().chunkSetAngles("SlatR0_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, -1F), 0.0F);
            hierMesh().chunkSetAngles("SlatR1_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, -1F), 0.0F);
            hierMesh().chunkSetAngles("SlatL2_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, -1F), 0.0F);
            hierMesh().chunkSetAngles("SlatR2_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, -1F), 0.0F);
            if(!(this instanceof ME_262HGII))
            {
                hierMesh().chunkSetAngles("SlatL2_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, -1F), 0.0F);
                hierMesh().chunkSetAngles("SlatR2_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, -1F), 0.0F);
            }
        }
        if(this.FM.AS.isMaster())
        {
            engineSurge(f);
            if(Config.isUSE_RENDER())
            {
                if(this.FM.EI.engines[0].getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6)
                {
                    if(this.FM.EI.engines[0].getPowerOutput() > 0.95F)
                        this.FM.AS.setSootState(this, 0, 3);
                    else
                        this.FM.AS.setSootState(this, 0, 2);
                } else
                {
                    this.FM.AS.setSootState(this, 0, 0);
                }
                if(this.FM.EI.engines[1].getPowerOutput() > 0.8F && this.FM.EI.engines[1].getStage() == 6)
                {
                    if(this.FM.EI.engines[1].getPowerOutput() > 0.95F)
                        this.FM.AS.setSootState(this, 1, 3);
                    else
                        this.FM.AS.setSootState(this, 1, 2);
                } else
                {
                    this.FM.AS.setSootState(this, 1, 0);
                }
            }
        }
        super.update(f);
        if(!(this instanceof ME_262HGII))
        {
            if(!getOp(31) || !getOp(32))
                this.FM.CT.trimAileron = ((this.FM.CT.ElevatorControl * (s32 - s31) + this.FM.CT.trimElevator * (s18 - s17)) * this.FM.SensPitch) / 3F;
            if(!bHasElevatorControl)
                this.FM.CT.ElevatorControl = 0.0F;
            if(trimElevator != this.FM.CT.trimElevator)
            {
                trimElevator = this.FM.CT.trimElevator;
                hierMesh().chunkSetAngles("StabL_D0", 0.0F, -14F * trimElevator, 0.0F);
                hierMesh().chunkSetAngles("StabR_D0", 0.0F, -14F * trimElevator, 0.0F);
            }
        }
        if(this.FM.CT.getCockpitDoor() > 0.2D && bHasBlister && this.FM.getSpeedKMH() > fMaxKMHSpeedForOpenCanopy && hierMesh().chunkFindCheck("Blister1_D0") != -1 && !this.FM.AS.bIsAboutToBailout)
        {
            try
            {
                if(this == World.getPlayerAircraft())
                    ((CockpitME_262)Main3D.cur3D().cockpitCur).removeCanopy();
            }
            catch(Exception exception) { }
            hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
            bHasBlister = false;
            this.FM.CT.bHasCockpitDoorControl = false;
            this.FM.setGCenter(-0.5F);
        }
    }

    private float trimElevator;
    private boolean bHasElevatorControl;
    public float cockpitDoor_;
    private float fMaxKMHSpeedForOpenCanopy;
    public boolean bHasBlister;
    private float X;
    private float s17;
    private float s18;
    private float s31;
    private float s32;
    private float oldthrl[] = {
        -1F, -1F
    };
    private float curthrl[] = {
        -1F, -1F
    };
    private float engineSurgeDamage[] = {
        0.0F, 0.0F
    };

    static 
    {
        Class class1 = ME_262.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
