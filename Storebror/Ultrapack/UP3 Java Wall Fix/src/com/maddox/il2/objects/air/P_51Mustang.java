package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public abstract class P_51Mustang extends Scheme1
    implements TypeFighter, TypeBNZFighter, TypeStormovik
{

    public P_51Mustang()
    {
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = f <= 0.5F ? Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 95F) : Aircraft.cvt(f, 0.8F, 1.0F, 95F, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, -105F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f1, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 0.4F, 0.0F, -0.1F);
        hierMesh().chunkSetLocate("GearL9_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("GearR9_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void moveSteering(float f)
    {
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.2F, 0.0F, 0.2F);
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.2F, 0.0F, 0.2F);
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    public void doKillPilot(int i)
    {
        int _tmp = i;
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        }
    }

    protected void moveFlap(float f)
    {
        float f1 = -52F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("p1"))
                    getEnergyPastArmor(World.Rnd().nextFloat(5F, 28.096F), shot);
                else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(13.35D / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("p3"))
                {
                    getEnergyPastArmor(World.Rnd().nextFloat(20F, 40F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                }
            } else
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int i = s.charAt(10) - 48;
                if(s.endsWith("10"))
                    i = 10;
                switch(i)
                {
                case 1:
                    if(getEnergyPastArmor(2.2F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Control Column: Hit, Controls Destroyed..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        if(World.Rnd().nextFloat() < 0.25F)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        if(World.Rnd().nextFloat() < 0.25F)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                    }
                    break;

                case 2:
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
                    break;

                case 3:
                case 4:
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Out..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 5:
                case 6:
                case 8:
                case 9:
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    break;

                case 7:
                case 10:
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 2.93F), shot) > 0.0F)
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
                if(s.endsWith("prop") && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                if(s.endsWith("case") && getEnergyPastArmor(0.1F, shot) > 0.0F)
                {
                    if(World.Rnd().nextFloat() < shot.power / 200000F)
                    {
                        FM.AS.setEngineStuck(shot.initiator, 0);
                        debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                    }
                    if(World.Rnd().nextFloat() < shot.power / 50000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 2);
                        debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                    }
                    if(World.Rnd().nextFloat() < shot.power / 28000F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                        debuggunnery("Engine Module: Engine Crank Case Hit - Cylinder Feed Out, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    }
                    FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    debuggunnery("Engine Module: Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                }
                if(s.endsWith("cyls") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.75F)
                {
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debuggunnery("Engine Module: Engine Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    if(FM.AS.astateEngineStates[0] < 1)
                        FM.AS.hitEngine(shot.initiator, 0, 1);
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 3);
                        debuggunnery("Engine Module: Engine Cylinders Hit - Engine Fires..");
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("supc") && getEnergyPastArmor(0.05F, shot) > 0.0F)
                    FM.EI.engines[0].setKillCompressor(shot.initiator);
                if(s.endsWith("eqpt"))
                {
                    if(World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
                        FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator);
                    if(World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
                        FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 0);
                    if(World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
                        FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, 1);
                    if(World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
                        FM.EI.engines[0].setEngineStuck(shot.initiator);
                    if(World.Rnd().nextFloat() < 0.3F && getEnergyPastArmor(0.05F, shot) > 0.0F)
                        FM.EI.engines[0].setEngineStops(shot.initiator);
                }
                if(s.endsWith("oil1"))
                    FM.AS.hitOil(shot.initiator, 0);
                if(s.endsWith("wat1") && World.Rnd().nextFloat() < 0.05F)
                    FM.AS.hitOil(shot.initiator, 0);
                if(s.startsWith("xxeng1mag"))
                {
                    int j = s.charAt(9) - 49;
                    FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, j);
                    debuggunnery("Engine Module: Magneto " + j + " Destroyed..");
                }
            } else
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
            } else
            if(s.startsWith("xxmgun0"))
            {
                int k = s.charAt(7) - 49;
                if(getEnergyPastArmor(0.5F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Machine Gun (" + k + ") Disabled..");
                    FM.AS.setJamBullets(0, k);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
            } else
            if(s.startsWith("xxoil"))
            {
                if(getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    FM.AS.hitOil(shot.initiator, 0);
                    getEnergyPastArmor(0.22F, shot);
                    debuggunnery("Engine Module: Oil Tank Pierced..");
                }
            } else
            if(s.startsWith("xxtank"))
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
        } else
        {
            if(s.startsWith("xblister"))
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            if(s.startsWith("xcf"))
                hitChunk("CF", shot);
            else
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
            if(s.startsWith("xkeel"))
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
                if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
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
                if(s.startsWith("xaronel") && chunkDamageVisible("AroneL") < 1)
                    hitChunk("AroneL", shot);
                if(s.startsWith("xaroner") && chunkDamageVisible("AroneR") < 1)
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
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 35:
            FM.AS.setJamBullets(0, 0);
            FM.AS.setJamBullets(1, 0);
            FM.AS.setJamBullets(1, 2);
            break;

        case 38:
            FM.AS.setJamBullets(0, 1);
            FM.AS.setJamBullets(1, 1);
            FM.AS.setJamBullets(1, 3);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f)
    {
        float f1 = FM.EI.engines[0].getControlRadiator();
        hierMesh().chunkSetAngles("Oil_D0", 0.0F, 15F * f1, 0.0F);
        hierMesh().chunkSetAngles("Water_D0", 0.0F, -12F + 15F * f1, 0.0F);
        super.update(f);
    }

    static 
    {
        Class class1 = P_51Mustang.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
