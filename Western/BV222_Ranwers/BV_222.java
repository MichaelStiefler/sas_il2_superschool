
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.Property;


public abstract class BV_222 extends Scheme7
    implements TypeSeaPlane, TypeTransport
{

    public BV_222()
    {
    }

    public void update(float f)
    {
        super.update(f);
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 2; j++)
                if(FM.Gears.clpGearEff[i][j] != null)
                {
                    tmpp.set(FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    tmpp.z = 0.01D;
                    FM.Gears.clpGearEff[i][j].pos.setAbs(tmpp);
                    FM.Gears.clpGearEff[i][j].pos.reset();
                }

        }

    }

    protected void moveFlap(float f)
    {
        float f1 = -45.5F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        boolean flag = false;
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxammo"))
            {
                int i = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.15F)
                    FM.AS.setJamBullets(10 + i, 0);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                int j = s.charAt(10) - 48;
                switch(j)
                {
                default:
                    break;

                case 1: // '\001'
                case 2: // '\002'
                case 3: // '\003'
                    if(getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Aileron Controls Out..");
                    }
                    break;

                case 6: // '\006'
                    if(getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    break;

                case 4: // '\004'
                case 5: // '\005'
                    if(getEnergyPastArmor(1.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    break;
                }
                return;
            }
            if(s.startsWith("xxengine"))
            {
                int k = s.charAt(8) - 49;
                if(s.endsWith("base"))
                {
                    if(getEnergyPastArmor(0.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 140000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, k);
                            Aircraft.debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 85000F)
                        {
                            FM.AS.hitEngine(shot.initiator, k, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Engine Damaged..");
                        }
                    } else
                    if(World.Rnd().nextFloat() < 0.005F)
                    {
                        FM.EI.engines[k].setCyliderKnockOut(shot.initiator, 1);
                    } else
                    {
                        FM.EI.engines[k].setReadyness(shot.initiator, FM.EI.engines[k].getReadyness() - 0.00082F);
                        Aircraft.debugprintln(this, "*** Engine (" + k + ") Crank Case Hit - Readyness Reduced to " + FM.EI.engines[k].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyl"))
                {
                    if(getEnergyPastArmor(5.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[k].getCylindersRatio() * 0.75F)
                    {
                        FM.EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        Aircraft.debugprintln(this, "*** Engine (" + k + ") Cylinders Hit, " + FM.EI.engines[k].getCylindersOperable() + "/" + FM.EI.engines[k].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 18000F)
                        {
                            FM.AS.hitEngine(shot.initiator, k, 2);
                            Aircraft.debugprintln(this, "*** Engine (" + k + ") Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("sup") && getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                {
                    FM.EI.engines[k].setKillCompressor(shot.initiator);
                    Aircraft.debugprintln(this, "*** Engine (" + k + ") Module: Compressor Stops..");
                    getEnergyPastArmor(2.6F, shot);
                }
                return;
            }
            if(s.startsWith("xxgun"))
            {
                int l = s.charAt(5) - 49;
                if(getEnergyPastArmor(5.7F, shot) > 0.0F && World.Rnd().nextFloat() < 0.96F)
                {
                    FM.AS.setJamBullets(10 + l, 0);
                    getEnergyPastArmor(29.95F, shot);
                }
                return;
            }
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
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
            if(s.startsWith("xxoil"))
            {
                int i1 = s.charAt(5) - 49;
                if(getEnergyPastArmor(0.21F, shot) > 0.0F && World.Rnd().nextFloat() < 0.2435F)
                    FM.AS.hitOil(shot.initiator, i1);
                Aircraft.debugprintln(this, "*** Engine (" + i1 + ") Module: Oil Tank Pierced..");
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.6F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.8F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F && World.Rnd().nextFloat() < 0.125F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(s.startsWith("xxspark") && chunkDamageVisible("Keel1") > 1 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F)
                {
                    Aircraft.debugprintln(this, "*** Keel1 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D" + chunkDamageVisible("Keel1"), shot.initiator);
                }
                if(s.startsWith("xxsparsl") && chunkDamageVisible("StabL") > 1 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F)
                {
                    Aircraft.debugprintln(this, "*** StabL: Spars Damaged..");
                    nextDMGLevels(1, 2, "StabL_D" + chunkDamageVisible("StabL"), shot.initiator);
                }
                if(s.startsWith("xxsparsr") && chunkDamageVisible("StabR") > 1 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F)
                {
                    Aircraft.debugprintln(this, "*** StabR: Spars Damaged..");
                    nextDMGLevels(1, 2, "StabR_D" + chunkDamageVisible("StabR"), shot.initiator);
                }
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(16.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.125F)
                {
                    Aircraft.debugprintln(this, "*** Tail1: Spars Damaged..");
                    nextDMGLevels(1, 2, "Tail1_D" + chunkDamageVisible("Tail1"), shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int j1 = s.charAt(6) - 48;
                if(s.length() == 8)
                    j1 = 10;
                switch(j1)
                {
                case 1: // '\001'
                case 2: // '\002'
                case 3: // '\003'
                    doHitMeATank(shot, 2);
                    break;

                case 4: // '\004'
                case 5: // '\005'
                case 6: // '\006'
                    doHitMeATank(shot, 3);
                    break;

                case 7: // '\007'
                case 8: // '\b'
                case 9: // '\t'
                case 10: // '\n'
                    doHitMeATank(shot, World.Rnd().nextInt(0, 1));
                    break;
                }
                return;
            } else
            {
                return;
            }
        }
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            return;
        }
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
            return;
        }
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
            return;
        }
        if(s.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
            return;
        }
        if(s.startsWith("xstabl"))
        {
            if(chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
            return;
        }
        if(s.startsWith("xstabr"))
        {
            if(chunkDamageVisible("StabR") < 2)
                hitChunk("StabR", shot);
            return;
        }
        if(s.startsWith("xvatorl"))
        {
            if(chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
            return;
        }
        if(s.startsWith("xvatorr"))
        {
            if(chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
            return;
        }
        if(s.startsWith("xwinglin"))
        {
            if(chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            return;
        }
        if(s.startsWith("xwingrin"))
        {
            if(chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            return;
        }
        if(s.startsWith("xwinglmid"))
        {
            if(chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            return;
        }
        if(s.startsWith("xwingrmid"))
        {
            if(chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            return;
        }
        if(s.startsWith("xwinglout"))
        {
            if(chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            return;
        }
        if(s.startsWith("xwingrout"))
        {
            if(chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
            return;
        }
        if(s.startsWith("xaronel"))
        {
            if(chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
            return;
        }
        if(s.startsWith("xaroner"))
        {
            if(chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR", shot);
            return;
        }
        if(s.startsWith("xengine1"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
            return;
        }
        if(s.startsWith("xengine2"))
        {
            if(chunkDamageVisible("Engine2") < 2)
                hitChunk("Engine2", shot);
            return;
        }
        if(s.startsWith("xengine3"))
        {
            if(chunkDamageVisible("Engine3") < 2)
                hitChunk("Engine3", shot);
            return;
        }
        if(s.startsWith("xengine4"))
        {
            if(chunkDamageVisible("Engine4") < 2)
                hitChunk("Engine4", shot);
            return;
        }
        if(s.startsWith("xengine5"))
        {
            if(chunkDamageVisible("Engine5") < 2)
                hitChunk("Engine5", shot);
            return;
        }
        if(s.startsWith("xengine6"))
        {
            if(chunkDamageVisible("Engine6") < 2)
                hitChunk("Engine6", shot);
            return;
        }
        if(s.startsWith("xnose"))
        {
            if(chunkDamageVisible("Nose") < 2)
                hitChunk("Nose", shot);
            return;
        }
        if(s.startsWith("xgearr"))
        {
            hitChunk("GearR2", shot);
            return;
        }
        if(s.startsWith("xgearl"))
        {
            hitChunk("GearL2", shot);
            return;
        }
        if(s.startsWith("xturret"))
            return;
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int k1;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                k1 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                k1 = s.charAt(6) - 49;
            } else
            {
                k1 = s.charAt(5) - 49;
            }
            hitFlesh(k1, shot, byte0);
            return;
        } else
        {
            return;
        }
    }

    private final void doHitMeATank(Shot shot, int i)
    {
        if(getEnergyPastArmor(0.2F, shot) > 0.0F)
            if(shot.power < 14100F)
            {
                if(shot.powerType == 3 && FM.AS.astateTankStates[i] > 0 && World.Rnd().nextFloat() < 0.25F)
                    FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(1, 2));
                else
                if(FM.AS.astateTankStates[i] == 0)
                {
                    FM.AS.hitTank(shot.initiator, i, 1);
                    FM.AS.doSetTankState(shot.initiator, i, 1);
                }
            } else
            {
                FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int)(shot.power / 56000F)));
            }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19: // '\023'
            killPilot(this, 5);
            break;

        case 13: // '\r'
            killPilot(this, 2);
            break;

        case 33: // '!'
            hitProp(1, j, actor);
            // fall through

        case 34: // '"'
            hitProp(0, j, actor);
            break;

        case 35: // '#'
            FM.AS.hitTank(actor, 0, World.Rnd().nextInt(0, 5));
            break;

        case 36: // '$'
            hitProp(2, j, actor);
            // fall through

        case 37: // '%'
            hitProp(3, j, actor);
            break;

        case 38: // '&'
            FM.AS.hitTank(actor, 3, World.Rnd().nextInt(0, 5));
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 10; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public static void moveGear(HierMesh hiermesh, float fc, float fl, float fr)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 90F * fl);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, -90F * fl);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, -90F * fr);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 90F * fr);
    }

    protected void moveGear(float f, float f1, float f2)
    {
        moveGear(hierMesh(), f, f1, f2);
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private static Point3d tmpp = new Point3d();

    static 
    {
        Class class1 = com.maddox.il2.objects.air.BV_222.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}