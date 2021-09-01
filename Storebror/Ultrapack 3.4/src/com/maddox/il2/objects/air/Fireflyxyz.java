package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class Fireflyxyz extends Scheme1
    implements TypeFighter, TypeTNBFighter, TypeStormovik
{

    public Fireflyxyz()
    {
        arrestor = 0.0F;
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("Wspong_L", 75F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("Wspong_R", -75F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("Wspong_L2", 0.0F, 15F * f, 0.0F);
        hiermesh.chunkSetAngles("Wspong_R2", 0.0F, -15F * f, 0.0F);
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, 0.0F, 60F * f);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, 0.0F, 60F * f);
    }

    public void moveWingFold(float f)
    {
        if(f < 0.001F)
        {
            setGunPodsOn(true);
            hideWingWeapons(false);
        } else
        {
            setGunPodsOn(false);
            FM.CT.WeaponControl[0] = false;
            hideWingWeapons(true);
        }
        moveWingFold(hierMesh(), f);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Math.max(-f * 800F, -95F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 55F * f);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 82F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -82F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 35F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", -35F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 99F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -99F * f, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.5F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }
    
    public void update(float f)
    {
        super.update(f);
        if(FM.CT.getArrestor() > 0.001F)
        {
            if(FM.Gears.arrestorVAngle != 0.0F)
            {
                float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -73.5F, 3.5F, 1.0F, 0.0F);
                arrestor = 0.8F * arrestor + 0.2F * f1;
                moveArrestorHook(arrestor);
            } else
            {
                float f2 = -1.224F * FM.Gears.arrestorVSink;
                if(f2 < 0.0F && this.FM.getSpeedKMH() > 60F)
                    Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                if(f2 > 0.2F)
                    f2 = 0.2F;
                if(f2 > 0.0F)
                    arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
                else
                    arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
                if(arrestor < 0.0F)
                    arrestor = 0.0F;
                else
                if(arrestor > FM.CT.getArrestor())
                    arrestor = FM.CT.getArrestor();
                moveArrestorHook(arrestor);
            }
        } else
        {
            hierMesh().chunkSetAngles("Scoop", 0.0F, 0.0F, 20F * FM.EI.engines[0].getControlRadiator());
        }
        if(this.FM instanceof RealFlightModel) {
            RealFlightModel realFlightModel = (RealFlightModel)this.FM;
            if (realFlightModel.isRealMode()) {
                float rpm = FM.EI.engines[0].getRPM();
                if (rpm > 30F) {
                    if(rpm < 300F)
                        realFlightModel.producedShakeLevel = (1500F - rpm) / 3000F;
                    else if(rpm < 1000F)
                        realFlightModel.producedShakeLevel = (1500F - rpm) / 8000F;
                    else if(rpm < 1500F)
                        realFlightModel.producedShakeLevel = 0.07F;
                    else if(rpm < 2000F)
                        realFlightModel.producedShakeLevel = 0.05F;
                    
                    // TODO: By SAS~Storebror: Get rid of this constant shaking when engine is revved up!
//                    else if(rpm < 2500F)
//                        realFlightModel.producedShakeLevel = 0.04F;
//                    else
//                        realFlightModel.producedShakeLevel = 0.03F;
                }
            }
        }
        // TODO: By SAS~Storebror: FUCK JAVA FM MESSUP! FUCK IT! DAMN... FUCK IT! WTF?
//        if(this.FM.getSpeedKMH() > 250F && this.FM.getVertSpeed() > 0.0F && this.FM.getAltitude() < 5000F)
//            FM.producedAF.x += 20F * (250F - this.FM.getSpeedKMH());
//        if(this.FM.isPlayers() && FM.Sq.squareElevators > 0.0F)
//        {
//            RealFlightModel realFlightModel = (RealFlightModel)this.FM;
//            if(realFlightModel.isRealMode() && realFlightModel.indSpeed > 120F)
//            {
//                float f5 = 1.0F + 0.005F * (120F - realFlightModel.indSpeed);
//                if(f5 < 0.0F)
//                    f5 = 0.0F;
//                this.FM.SensPitch = 0.59F * f5;
//                if(realFlightModel.indSpeed > 120F)
//                    FM.producedAM.y -= 1720F * (120F - realFlightModel.indSpeed);
//            } else
//            {
//                this.FM.SensPitch = 0.63F;
//            }
//        }
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
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("FlapL_D0", 0.0F, 0.0F, 35F * f);
        hierMesh().chunkSetAngles("FlapR_D0", 0.0F, 0.0F, 35F * f);
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook_D0", 0.0F, 0.0F, 75F * arrestor);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 3; i++)
            if(this.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void doMurderPilot(int i)
    {
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1"))
                    getEnergyPastArmor(14.2F / (1E-005F + (float)Math.abs(Aircraft.v1.x)), shot);
                return;
            }
            if(s.startsWith("xxeng1"))
            {
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(0.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 140000F)
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        if(World.Rnd().nextFloat() < shot.power / 85000F)
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                    } else
                    if(World.Rnd().nextFloat() < 0.01F)
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    else
                        FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - 0.002F);
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(6.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 0.75F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("supc") && getEnergyPastArmor(0.05F, shot) > 0.0F && World.Rnd().nextFloat() < 0.89F)
                    FM.EI.engines[0].setKillCompressor(shot.initiator);
                if(s.endsWith("oil1") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.25F, shot) > 0.0F)
                    FM.AS.hitOil(shot.initiator, 0);
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int i = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    FM.AS.hitTank(shot.initiator, i, 1);
                    if(World.Rnd().nextFloat() < 0.05F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.6F)
                        FM.AS.hitTank(shot.initiator, i, 2);
                }
                return;
            }
        }
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(World.Rnd().nextFloat() < 0.19F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            if(World.Rnd().nextFloat() < 0.19F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
        } else
        if(s.startsWith("xeng"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel1"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel") && chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner") && chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR", shot);
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

    private float arrestor;

    static 
    {
        Class class1 = Fireflyxyz.class;
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
    }
}
