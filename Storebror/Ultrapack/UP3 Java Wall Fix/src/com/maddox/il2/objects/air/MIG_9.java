package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class MIG_9 extends Scheme2
    implements TypeFighter, TypeBNZFighter
{

    public MIG_9()
    {
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

    protected void moveFlap(float f)
    {
        float f1 = -45F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    public void update(float f)
    {
        super.update(f);
        if(FM.AS.isMaster())
        {
            float f1 = FM.EI.engines[0].getThrustOutput() * FM.EI.engines[0].getControlThrottle();
            if(FM.EI.engines[0].getStage() == 6 && f1 > 0.75F)
            {
                if(f1 > 0.92F)
                    FM.AS.setSootState(this, 0, 3);
                else
                    FM.AS.setSootState(this, 0, 2);
            } else
            {
                FM.AS.setSootState(this, 0, 0);
            }
            f1 = FM.EI.engines[1].getThrustOutput() * FM.EI.engines[1].getControlThrottle();
            if(FM.EI.engines[1].getStage() == 6 && f1 > 0.75F)
            {
                if(f1 > 0.92F)
                    FM.AS.setSootState(this, 1, 3);
                else
                    FM.AS.setSootState(this, 1, 2);
            } else
            {
                FM.AS.setSootState(this, 1, 0);
            }
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, cvt(f, 0.05F, 0.75F, 0.0F, -70F), 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, cvt(f, 0.05F, 0.75F, 0.0F, -95F), 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, cvt(f, 0.05F, 0.15F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, cvt(f, 0.05F, 0.15F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.02F, 0.12F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.05F, 0.75F, 0.0F, -87F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f, 0.22F, 0.32F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f, 0.25F, 0.95F, 0.0F, -87F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC8_D0", 0.0F, -cvt(f, -40F, 40F, -40F, 40F), 0.0F);
    }

    public void moveWheelSink()
    {
        float f = cvt(FM.Gears.gWheelSinking[2], 0.0F, 0.11295F, 0.0F, -25F);
        hierMesh().chunkSetAngles("GearC4_D0", 0.0F, f, 0.0F);
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, f, 0.0F);
        f = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.1293F, 0.0F, -26F);
        hierMesh().chunkSetAngles("GearL3_D0", 0.0F, f, 0.0F);
        f = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.1293F, 0.0F, -30F);
        hierMesh().chunkSetAngles("GearL5_D0", 0.0F, f, 0.0F);
        f = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.1293F, 0.0F, -4F);
        hierMesh().chunkSetAngles("GearL8_D0", 0.0F, f, 0.0F);
        f = cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.1293F, 0.0F, -26F);
        hierMesh().chunkSetAngles("GearR3_D0", 0.0F, f, 0.0F);
        f = cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.1293F, 0.0F, -30F);
        hierMesh().chunkSetAngles("GearR5_D0", 0.0F, f, 0.0F);
        f = cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.1293F, 0.0F, -4F);
        hierMesh().chunkSetAngles("GearR8_D0", 0.0F, f, 0.0F);
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
            break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        boolean flag = false;
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxammo"))
            {
                if(World.Rnd().nextFloat(0.0F, 20000F) < shot.power && World.Rnd().nextFloat() < 0.25F)
                    FM.AS.setJamBullets(0, World.Rnd().nextInt(0, 1));
                getEnergyPastArmor(11.4F, shot);
                return;
            }
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.startsWith("xxarmorg"))
                {
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    getEnergyPastArmor(56.259998321533203D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    if(shot.power <= 0.0F)
                        if(Math.abs(v1.x) > 0.86599999666213989D)
                            doRicochet(shot);
                        else
                            doRicochetBack(shot);
                }
                if(s.startsWith("xxarmorp"))
                    getEnergyPastArmor(12.88D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                return;
            }
            if(s.startsWith("xxcannon"))
            {
                if(s.endsWith("01"))
                {
                    debuggunnery("Armament System: Left Cannon: Disabled..");
                    FM.AS.setJamBullets(0, 0);
                }
                if(s.endsWith("02"))
                {
                    debuggunnery("Armament System: Right Cannon: Disabled..");
                    FM.AS.setJamBullets(0, 1);
                }
                if(s.endsWith("03"))
                {
                    debuggunnery("Armament System: Central Cannon: Disabled..");
                    FM.AS.setJamBullets(1, 0);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(6.98F, 24.35F), shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                int i = s.charAt(10) - 48;
                switch(i)
                {
                default:
                    break;

                case 1:
                case 4:
                    if(getEnergyPastArmor(4.1F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** Aileron Controls Crank: Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 2:
                case 3:
                    if(getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debugprintln(this, "*** Aileron Controls: Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 5:
                case 6:
                    if(getEnergyPastArmor(0.3F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** Rudder Controls: Disabled / Strings Broken..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;

                case 7:
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 8:
                    if(getEnergyPastArmor(0.1F, shot) <= 0.0F)
                        break;
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                    if(World.Rnd().nextFloat() < 0.5F)
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    else
                        FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                    debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                    break;
                }
                return;
            }
            if(s.startsWith("xxEng"))
            {
                int j = s.charAt(5) - 49;
                if(point3d.x > 1.2030000000000001D)
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        debugprintln(this, "*** Engine Module(s): Supercharger Disabled..");
                        FM.AS.setEngineSpecificDamage(shot.initiator, j, 0);
                    }
                } else
                {
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F)
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    if(World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass && FM.EI.engines[1].getStage() == 6)
                        FM.AS.hitEngine(shot.initiator, 0, 1);
                    getEnergyPastArmor(14.296F, shot);
                }
                return;
            }
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
            }
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLIn Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRIn Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float)Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
            }
            if(s.startsWith("xxTank"))
            {
                int k = s.charAt(6) - 48;
                switch(k)
                {
                case 1:
                    doHitMeATank(shot, 0);
                    break;

                case 2:
                    doHitMeATank(shot, 1);
                    break;

                case 3:
                    doHitMeATank(shot, 2);
                    break;

                case 4:
                    doHitMeATank(shot, 3);
                    break;

                case 5:
                    doHitMeATank(shot, 2);
                    break;

                case 6:
                    doHitMeATank(shot, 3);
                    break;

                case 7:
                    doHitMeATank(shot, 2);
                    break;

                case 8:
                    doHitMeATank(shot, 3);
                    break;
                }
                return;
            } else
            {
                return;
            }
        }
        if(s.startsWith("xcockpit"))
        {
            if(point3d.z > 0.5D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            else
            if(point3d.y > 0.0D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
            else
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
            if(World.Rnd().nextFloat() < 0.067F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
        }
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
            hitChunk("Keel1", shot);
        else
        if(s.startsWith("xNose"))
            hitChunk("Nose1", shot);
        else
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
        } else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl"))
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr"))
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
            if((s.endsWith("2a") || s.endsWith("2b")) && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
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

    private final void doHitMeATank(Shot shot, int i)
    {
        if(getEnergyPastArmor(0.2F, shot) > 0.0F)
            if(shot.power < 14100F)
            {
                if(FM.AS.astateTankStates[i] == 0)
                {
                    FM.AS.hitTank(shot.initiator, i, 1);
                    FM.AS.doSetTankState(shot.initiator, i, 1);
                }
                if(FM.AS.astateTankStates[i] > 0 && (World.Rnd().nextFloat() < 0.02F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F))
                    FM.AS.hitTank(shot.initiator, i, 1);
            } else
            {
                FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int)(shot.power / 58899F)));
            }
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        if(f < 0.1F)
            xyz[1] = cvt(f, 0.01F, 0.08F, 0.0F, -0.1F);
        else
            xyz[1] = cvt(f, 0.17F, 0.99F, -0.1F, -0.6F);
        hierMesh().chunkSetLocate("Blister1_D0", xyz, ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public static boolean bChangedPit = false;

    static 
    {
        Class class1 = MIG_9.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
