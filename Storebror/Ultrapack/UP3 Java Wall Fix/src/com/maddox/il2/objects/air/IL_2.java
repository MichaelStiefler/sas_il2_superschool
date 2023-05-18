package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.PylonKMB;
import com.maddox.rts.Property;

public abstract class IL_2 extends Scheme1
    implements TypeStormovikArmored
{

    public IL_2()
    {
    }

    public void update(float f)
    {
        super.update(f);
        World.cur();
        if(this == World.getPlayerAircraft() && FM.turret.length > 0 && FM.AS.astatePilotStates[1] < 90 && FM.turret[0].bIsAIControlled && (FM.getOverload() > 7F || FM.getOverload() < -0.7F))
            Voice.speakRearGunShake();
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        int i = (int)(f * 10F);
        float f1 = Math.max(-f * 1200F, -75F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -(GA[i][0] * (f - i / 10F) + GA[i][1]), 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, -(GA[i][2] * (f - i / 10F) + GA[i][3]));
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 55F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -(GA[i][0] * (f - i / 10F) + GA[i][1]), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, -(GA[i][2] * (f - i / 10F) + GA[i][3]));
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 55F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, f1, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC2_D0", 0.0F, f, 0.0F);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        xyz[2] = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.08F, 0.0F, 0.08F);
        hierMesh().chunkSetLocate("GearL9_D0", xyz, ypr);
        xyz[2] = cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.08F, 0.0F, 0.08F);
        hierMesh().chunkSetLocate("GearR9_D0", xyz, ypr);
    }

    protected void moveElevator(float f)
    {
        float f1 = -16F * f;
        if(f1 < 0.0F)
            f1 = (float)(f1 * 1.75D);
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, f1, 0.0F);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -20F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -20F * f, 0.0F);
        hierMesh().chunkSetAngles("FlettnerL_D0", 0.0F, 40F * f, 0.0F);
        hierMesh().chunkSetAngles("FlettnerR_D0", 0.0F, 40F * f, 0.0F);
        hierMesh().chunkSetAngles("FlettnerRodL_D0", 0.0F, -37F * f, 0.0F);
        hierMesh().chunkSetAngles("FlettnerRodR_D0", 0.0F, -37F * f, 0.0F);
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay1_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay3_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay5_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay6_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay7_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay8_D0", 0.0F, -90F * f, 0.0F);
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        if(af[0] < -60F)
        {
            af[0] = -60F;
            flag = false;
        } else
        if(af[0] > 60F)
        {
            af[0] = 60F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if(f < 20F)
        {
            if(af[1] < -10F)
            {
                af[1] = -10F;
                flag = false;
            }
        } else
        if(af[1] < -15F)
        {
            af[1] = -15F;
            flag = false;
        }
        if(af[1] > 45F)
        {
            af[1] = 45F;
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

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 1:
            if(FM.turret.length == 0)
                return;
            FM.turret[0].setHealth(f);
            hierMesh().chunkVisible("Turret1A_D0", false);
            hierMesh().chunkVisible("Turret1B_D0", false);
            hierMesh().chunkVisible("Turret1A_D1", true);
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            if(!FM.AS.bIsAboutToBailout)
            {
                hierMesh().chunkVisible("Gore1_D0", hierMesh().isChunkVisible("Blister1_D0"));
                hierMesh().chunkVisible("Gore2_D0", true);
            }
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            if(!FM.AS.bIsAboutToBailout)
                hierMesh().chunkVisible("Gore3_D0", true);
            if(hierMesh().chunkFindCheck("Helm_D0") != -1 && hierMesh().isChunkVisible("Helm_D0"))
            {
                hierMesh().chunkVisible("Helm_D0", false);
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Helm_D0"));
                wreckage.collide(false);
                Vector3d vector3d = new Vector3d();
                vector3d.set(FM.Vwld);
                wreckage.setSpeed(vector3d);
            }
            break;
        }
        if(i == 1)
        {
            if(FM.turret == null)
                return;
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            hierMesh().chunkVisible("Turret1A_D0", false);
            hierMesh().chunkVisible("Turret1B_D0", false);
        }
    }

    public void doRemoveBodyFromPlane(int i)
    {
        super.doRemoveBodyFromPlane(i);
        if(i == 1 && hierMesh().chunkFindCheck("Helm_D0") != -1 && hierMesh().isChunkVisible("Helm_D0"))
            hierMesh().chunkVisible("Helm_D0", false);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        Object aobj[] = pos.getBaseAttached();
        if(aobj == null)
            return;
        for(int i = 0; i < aobj.length; i++)
            if(aobj[i] instanceof PylonKMB)
            {
                for(int j = 1; j < 9; j++)
                    hierMesh().chunkVisible("Bay" + j + "_D0", false);

                return;
            }

        FM.AS.wantBeaconsNet(true);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        boolean flag = false;
        boolean flag1 = (this instanceof IL_2I) || (this instanceof IL_2MLate) || (this instanceof IL_2T) || (this instanceof IL_2Type3) || (this instanceof IL_2Type3M);
        boolean flag2 = !(this instanceof IL_2_1940Early) && !(this instanceof IL_2_1940Late);
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.startsWith("xxarmorp"))
                {
                    int i = s.charAt(8) - 48;
                    switch(i)
                    {
                    default:
                        break;

                    case 1:
                        getEnergyPastArmor(7.070000171661377D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                        shot.powerType = 0;
                        break;

                    case 2:
                    case 3:
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
                        shot.powerType = 0;
                        if(shot.power <= 0.0F && Math.abs(v1.x) > 0.86599999666213989D)
                            doRicochet(shot);
                        break;

                    case 4:
                        if(point3d.x > -1.35D)
                        {
                            getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                            shot.powerType = 0;
                            if(shot.power <= 0.0F && Math.abs(v1.x) > 0.86599999666213989D)
                                doRicochet(shot);
                        } else
                        {
                            getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                        }
                        break;

                    case 5:
                    case 6:
                        getEnergyPastArmor(20.2D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
                        if(shot.power > 0.0F)
                            break;
                        if(Math.abs(v1.x) > 0.86599999666213989D)
                            doRicochet(shot);
                        else
                            doRicochetBack(shot);
                        break;

                    case 7:
                        getEnergyPastArmor(20.2D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F)
                            doRicochetBack(shot);
                        break;
                    }
                }
                if(s.startsWith("xxarmorc1"))
                    getEnergyPastArmor(7.070000171661377D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                if(s.startsWith("xxarmort1"))
                    getEnergyPastArmor(6.059999942779541D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int j = s.charAt(10) - 48;
                if(s.endsWith("10"))
                    j = 10;
                switch(j)
                {
                default:
                    break;

                case 1:
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.33F)
                        {
                            debuggunnery("Controls: Throttle Controls Disabled..");
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        }
                        if(World.Rnd().nextFloat() < 0.33F)
                        {
                            debuggunnery("Controls: Prop Controls Disabled..");
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        }
                        if(World.Rnd().nextFloat() < 0.33F)
                        {
                            debuggunnery("Controls: Mix Controls Disabled..");
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 7);
                        }
                    }
                    break;

                case 2:
                    if(getEnergyPastArmor(1.1F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Control Column Hit, Controls Destroyed..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 3:
                    if(getEnergyPastArmor(0.25F / ((float)Math.sqrt(v1.y * v1.y + v1.z * v1.z) + 0.0001F), shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.05F)
                    {
                        debuggunnery("Controls: Elevator Wiring Hit, Elevator Controls Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(World.Rnd().nextFloat() < 0.75F)
                    {
                        debuggunnery("Controls: Rudder Wiring Hit, Rudder Controls Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;

                case 4:
                    if(getEnergyPastArmor(0.25F / ((float)Math.sqrt(v1.y * v1.y + v1.z * v1.z) + 0.0001F), shot) <= 0.0F)
                        break;
                    if(World.Rnd().nextFloat() < 0.03F)
                    {
                        debuggunnery("Controls: Elevator Wiring Hit, Elevator Controls Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    if(World.Rnd().nextFloat() < 0.15F)
                    {
                        debuggunnery("Controls: Rudder Wiring Hit, Rudder Controls Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;

                case 5:
                case 6:
                case 9:
                case 10:
                    if(getEnergyPastArmor(4D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        debuggunnery("Controls: Aileron Wiring Hit, Aileron Controls Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 7:
                case 8:
                    if(getEnergyPastArmor(5.25F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Aileron Cranks Hit, Aileron Controls Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;
                }
            }
            if(s.startsWith("xxspar"))
            {
                debuggunnery("Spar Construction: Hit..");
                if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && World.Rnd().nextFloat() > Math.abs(v1.x) + 0.1D && getEnergyPastArmor(3.4D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: Keel Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if(s.startsWith("xxsparlm"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingLMid") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingLMid Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingLMid_D" + chunkDamageVisible("WingLMid"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingLMid") > 2 && World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingLMid Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparrm"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingRMid") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingRMid Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingRMid_D" + chunkDamageVisible("WingRMid"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingRMid") > 2 && World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingRMid Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparlo"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingLOut") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingLOut Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingLOut_D" + chunkDamageVisible("WingLOut"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingLOut") > 2 && World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingLOut Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparro"))
                    if(flag1)
                    {
                        if(chunkDamageVisible("WingROut") > 0 && getEnergyPastArmor(2.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                        {
                            debuggunnery("Spar Construction: WingROut Spar Hit and Holed..");
                            nextDMGLevels(1, 2, "WingROut_D" + chunkDamageVisible("WingROut"), shot.initiator);
                        }
                    } else
                    if(chunkDamageVisible("WingROut") > 2 && World.Rnd().nextFloat() > Math.abs(v1.x) + 0.11999999731779099D && getEnergyPastArmor(6.96D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                    {
                        debuggunnery("Spar Construction: WingROut Spar Hit, Breaking in Half..");
                        nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                    }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(6.5D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "StabL_D2", shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(6.5D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot) > 0.0F)
                {
                    debuggunnery("Spar Construction: StabL Spar Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "StabR_D2", shot.initiator);
                }
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.86F / (float)Math.sqrt(v1.y * v1.y + v1.z * v1.z), shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    debuggunnery("Spar Construction: Tail1 Ribs Hit, Breaking in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
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
            if(s.startsWith("xxeng"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("prop"))
                {
                    if(getEnergyPastArmor(3.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F)
                        if(World.Rnd().nextFloat() < 0.5F)
                        {
                            debuggunnery("Engine Module: Prop Governor Hit, Disabled..");
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                        } else
                        {
                            debuggunnery("Engine Module: Prop Governor Hit, Oil Pipes Damaged..");
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        }
                } else
                if(s.endsWith("gear"))
                {
                    if(getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        debuggunnery("Engine Module: Reductor Hit, Bullet Jams Reductor Gear..");
                        FM.EI.engines[0].setEngineStuck(shot.initiator);
                    }
                } else
                if(s.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.01F, shot) > 0.0F)
                    {
                        debuggunnery("Engine Module: Supercharger Disabled..");
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                    }
                } else
                if(s.endsWith("feed"))
                {
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if(World.Rnd().nextFloat() < 0.05F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else
                if(s.endsWith("fue1"))
                {
                    if(getEnergyPastArmor(0.89F, shot) > 0.0F)
                    {
                        debuggunnery("Engine Module: Fuel Feed Line Pierced, Engine Fires..");
                        FM.AS.hitEngine(shot.initiator, 0, 100);
                    }
                } else
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(2.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 175000F)
                        {
                            debuggunnery("Engine Module: Crank Case Hit, Bullet Jams Ball Bearings..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F)
                        {
                            debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    debuggunnery("Engine Module: Crank Case Hit, Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                    getEnergyPastArmor(22.5F, shot);
                } else
                if(s.endsWith("cyl1"))
                {
                    if(getEnergyPastArmor(1.3F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.75F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                        debuggunnery("Engine Module: Cylinders Assembly Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Operating..");
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                        {
                            debuggunnery("Engine Module: Cylinders Assembly Hit, Engine Fires..");
                            FM.AS.hitEngine(shot.initiator, 0, 3);
                        }
                        if(World.Rnd().nextFloat() < 0.01F)
                        {
                            debuggunnery("Engine Module: Cylinders Assembly Hit, Bullet Jams Piston Head..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        getEnergyPastArmor(22.5F, shot);
                    }
                    if(Math.abs(point3d.y) < 0.1379999965429306D && getEnergyPastArmor(3.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Lines Hit, Engine Stalled..");
                            FM.EI.engines[0].setEngineStops(shot.initiator);
                        }
                        if(World.Rnd().nextFloat() < 0.05F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Engine Jams..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < 0.1F)
                        {
                            debuggunnery("Engine Module: Feed Gear Hit, Half Cylinder Feed Cut-Out..");
                            FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 6);
                        }
                    }
                } else
                if(s.startsWith("xxeng1mag"))
                {
                    int k = s.charAt(9) - 49;
                    debuggunnery("Engine Module: Magneto " + k + " Hit, Magneto " + k + " Disabled..");
                    FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, k);
                } else
                if(s.startsWith("xxeng1oil") && getEnergyPastArmor(0.5F, shot) > 0.0F)
                {
                    debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
                    FM.AS.hitOil(shot.initiator, 0);
                }
            }
            if(s.startsWith("xxw1"))
                if(FM.AS.astateEngineStates[0] == 0)
                {
                    debuggunnery("Engine Module: Water Radiator Pierced..");
                    FM.AS.hitEngine(shot.initiator, 0, 1);
                    FM.AS.doSetEngineState(shot.initiator, 0, 1);
                } else
                if(FM.AS.astateEngineStates[0] == 1)
                {
                    debuggunnery("Engine Module: Water Radiator Pierced..");
                    FM.AS.hitEngine(shot.initiator, 0, 1);
                    FM.AS.doSetEngineState(shot.initiator, 0, 2);
                }
            if(s.startsWith("xxtank"))
            {
                int l = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    if(FM.AS.astateTankStates[l] == 0)
                    {
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced..");
                        FM.AS.hitTank(shot.initiator, 2, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 1);
                    } else
                    if(FM.AS.astateTankStates[l] == 1)
                    {
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced..");
                        FM.AS.hitTank(shot.initiator, 2, 1);
                        FM.AS.doSetTankState(shot.initiator, l, 2);
                    }
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.hitTank(shot.initiator, 2, 2);
                        debuggunnery("Fuel System: Fuel Tank " + l + " Pierced, State Shifted..");
                    }
                }
            }
            if(s.startsWith("xxmgun"))
            {
                if(s.endsWith("01"))
                {
                    debuggunnery("Armament System: Left Machine Gun: Disabled..");
                    FM.AS.setJamBullets(0, 0);
                }
                if(s.endsWith("02"))
                {
                    debuggunnery("Armament System: Right Machine Gun: Disabled..");
                    FM.AS.setJamBullets(0, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.3F, 12.6F), shot);
            }
            if(s.startsWith("xxcannon"))
            {
                if(s.endsWith("01") && getEnergyPastArmor(0.25F, shot) > 0.0F)
                {
                    debuggunnery("Armament System: Left Cannon: Disabled..");
                    FM.AS.setJamBullets(1, 0);
                }
                if(s.endsWith("02") && getEnergyPastArmor(0.25F, shot) > 0.0F)
                {
                    debuggunnery("Armament System: Right Cannon: Disabled..");
                    FM.AS.setJamBullets(1, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            }
            if(s.startsWith("xxammo"))
            {
                if(s.startsWith("xxammol1") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Left Cannon: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(1, 0);
                }
                if(s.startsWith("xxammor1") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Right Cannon: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(1, 1);
                }
                if(s.startsWith("xxammol2") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Left Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(0, 0);
                }
                if(s.startsWith("xxammor2") && World.Rnd().nextFloat() < 0.023F)
                {
                    debuggunnery("Armament System: Right Machine Gun: Chain Feed Jammed, Gun Disabled..");
                    FM.AS.setJamBullets(0, 1);
                }
                getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 12.6F), shot);
            }
            if(s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.00345F && FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0].haveBullets())
            {
                debuggunnery("Armament System: Bomb Payload Detonated..");
                FM.AS.hitTank(shot.initiator, 0, 10);
                FM.AS.hitTank(shot.initiator, 1, 10);
                nextDMGLevels(3, 2, "CF_D0", shot.initiator);
            }
            if(s.startsWith("xxpnm") && getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
            {
                debuggunnery("Pneumo System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 1);
            }
            if(s.startsWith("xxhyd") && getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
            {
                debuggunnery("Hydro System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if(s.startsWith("xxins"))
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            return;
        }
        if(s.startsWith("xcockpit") || s.startsWith("xblister"))
            if(point3d.z > 0.473D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            else
            if(point3d.y > 0.0D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
            else
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
        if(s.startsWith("xcf"))
        {
            if(point3d.x < -1.94D)
            {
                if(chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", shot);
            } else
            {
                if(point3d.x <= 1.342D)
                    if(point3d.z < -0.591D || point3d.z > 0.40799999237060547D && point3d.x > 0.0D)
                    {
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F && Math.abs(v1.x) > 0.86599999666213989D)
                            doRicochet(shot);
                    } else
                    {
                        getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
                        if(shot.power <= 0.0F && Math.abs(v1.x) > 0.86599999666213989D)
                            doRicochet(shot);
                    }
                if(chunkDamageVisible("CF") < 3)
                    hitChunk("CF", shot);
            }
        } else
        if(s.startsWith("xoil"))
        {
            if(point3d.z < -0.981D)
            {
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochet(shot);
            } else
            if(point3d.x > 0.537D || point3d.x < -0.1D)
            {
                getEnergyPastArmor(0.2D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochetBack(shot);
            } else
            {
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
                if(shot.power <= 0.0F)
                    doRicochet(shot);
            }
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xeng"))
        {
            if(point3d.z > 0.159D)
                getEnergyPastArmor(1.25F * World.Rnd().nextFloat(0.95F, 1.12F) / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 1.335D && point3d.x < 2.386D && point3d.z > -0.06D && point3d.z < 0.064000000000000001D)
                getEnergyPastArmor(0.5D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 2.53D && point3d.x < 2.992D && point3d.z > -0.235D && point3d.z < 0.011D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 2.559D && point3d.z < -0.595D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 1.849D && point3d.x < 2.251D && point3d.z < -0.71D)
                getEnergyPastArmor(4.0399999618530273D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
            else
            if(point3d.x > 3.0030000000000001D)
                getEnergyPastArmor(World.Rnd().nextFloat(2.3F, 3.2F), shot);
            else
            if(point3d.z < -0.60600000619888306D)
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
            else
                getEnergyPastArmor(5.0500001907348633D / (Math.abs(v1.y) + 9.9999997473787516E-005D), shot);
            if(Math.abs(v1.x) > 0.86599999666213989D && (shot.power <= 0.0F || World.Rnd().nextFloat() < 0.1F))
                doRicochet(shot);
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(flag2)
            {
                if(chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", shot);
            } else
            {
                hitChunk("Tail1", shot);
            }
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
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
            if((s.endsWith("2a") || s.endsWith("2b")) && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
            {
                debuggunnery("Undercarriage: Stuck..");
                FM.AS.setInternalDamage(shot.initiator, 3);
            }
        } else
        if(s.startsWith("xturret"))
        {
            if(getEnergyPastArmor(0.25F, shot) > 0.0F)
            {
                debuggunnery("Armament System: Turret Machine Gun(s): Disabled..");
                FM.AS.setJamBullets(10, 0);
                FM.AS.setJamBullets(10, 1);
                getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 26.35F), shot);
            }
        } else
        if(s.startsWith("xhelm"))
        {
            getEnergyPastArmor(World.Rnd().nextFloat(2.0F, 3.56F), shot);
            if(shot.power <= 0.0F)
                doRicochetBack(shot);
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

    public void msgExplosion(Explosion explosion)
    {
        setExplosion(explosion);
        if(explosion.chunkName != null && explosion.power > 0.0F && explosion.chunkName.startsWith("Tail1"))
        {
            if(World.Rnd().nextFloat(0.0F, 0.038F) < explosion.power)
                FM.AS.setControlsDamage(explosion.initiator, 1);
            if(World.Rnd().nextFloat(0.0F, 0.042F) < explosion.power)
                FM.AS.setControlsDamage(explosion.initiator, 2);
        }
        super.msgExplosion(explosion);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 3; i++)
        {
            if(FM.getAltitude() < 3000F)
            {
                if(hierMesh().chunkFindCheck("HMask" + i + "_D0") != -1)
                    hierMesh().chunkVisible("HMask" + i + "_D0", false);
                continue;
            }
            if(hierMesh().chunkFindCheck("HMask" + i + "_D0") != -1)
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));
        }

    }

    private static float GA[][] = {
        {
            -68.1F, 0.0F, 134.4F, 0.0F
        }, {
            -74.7F, -6.81F, 119.6F, 13.44F
        }, {
            -88.4F, -14.29F, 109.7F, 25.41F
        }, {
            -112.7F, -23.13F, 85F, 36.39F
        }, {
            -142.4F, -34.39F, 58.6F, 44.89F
        }, {
            -166.3F, -48.64F, 17.8F, 50.76F
        }, {
            -164.8F, -65.27F, -28.9F, 52.55F
        }, {
            -118.2F, -81.75F, -65.1F, 49.66F
        }, {
            -63.1F, -93.57F, -91.7F, 43.14F
        }, {
            -18.5F, -99.87F, -108.3F, 33.96F
        }, {
            0.0F, -103F, 0.0F, 22F
        }
    };

    static 
    {
        Class class1 = IL_2.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
