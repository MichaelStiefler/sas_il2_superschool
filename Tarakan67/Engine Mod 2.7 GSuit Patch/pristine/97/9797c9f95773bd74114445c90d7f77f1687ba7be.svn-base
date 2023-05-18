// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 22.04.2015 20:58:11
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   KI_51.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme1, TypeStormovik, TypeScout, Aircraft, 
//            PaintScheme

public abstract class KI_51 extends Scheme1
    implements TypeStormovik, TypeScout
{

    public KI_51()
    {
        flapps = 0.0F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.AS.wantBeaconsNet(true);
        if(!thisWeaponsName.startsWith("1") && !thisWeaponsName.startsWith("2"))
        {
            hierMesh().chunkVisible("AtcL_D0", false);
            hierMesh().chunkVisible("AtcR_D0", false);
        }
    }

    protected void moveAileron(float f)
    {
        if(FM.CT.getWing() < 0.01F)
        {
            hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -19F * f, 0.0F);
            hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -19F * f, 0.0F);
        }
    }

    protected void moveFlap(float f)
    {
        float f1 = -45F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.getAltitude() < 3000F)
        {
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
        } else
        {
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
            hierMesh().chunkVisible("HMask2_D0", hierMesh().isChunkVisible("Pilot2_D0"));
        }
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 1: // '\001'
            FM.turret[0].setHealth(f);
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("HMask2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("t1"))
                {
                    getEnergyPastArmor((double)World.Rnd().nextFloat(21F, 42F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                } else
                if(s.endsWith("t2"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(12.2F, 12.9F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("t3"))
                    getEnergyPastArmor(8.9F, shot);
                else
                if(s.endsWith("t4"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(12.2F, 12.9F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("t5"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(12.2F, 12.9F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("t6"))
                    getEnergyPastArmor((double)World.Rnd().nextFloat(12.2F, 12.9F) / (Math.abs(Aircraft.v1.x) + 9.9999997473787516E-005D), shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                if(s.endsWith("s1") && getEnergyPastArmor(4.8F, shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                    FM.AS.setControlsDamage(shot.initiator, 2);
                    FM.AS.setControlsDamage(shot.initiator, 1);
                    FM.AS.setControlsDamage(shot.initiator, 0);
                }
                if(s.endsWith("s2") && getEnergyPastArmor(0.2F, shot) > 0.0F)
                {
                    FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                    Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine Controls Disabled..");
                }
                if(s.endsWith("s3") && getEnergyPastArmor(6.8F, shot) > 0.0F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Elevator Crank: Hit, Controls Destroyed..");
                }
                if((s.endsWith("s4") || s.endsWith("s5")) && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                }
                if((s.endsWith("s6") || s.endsWith("s7") || s.endsWith("s10") || s.endsWith("s11")) && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.35F, shot) > 0.0F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Aileron Controls Out..");
                }
                if((s.endsWith("s8") || s.endsWith("s9")) && getEnergyPastArmor(6.75F, shot) > 0.0F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Aileron Cranks Destroyed..");
                }
                return;
            }
            if(s.startsWith("xxeng1"))
            {
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 280000F)
                        {
                            FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 100000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                        }
                    } else
                    if(World.Rnd().nextFloat() < 0.01F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                    } else
                    {
                        FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - 0.002F);
                        Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                    }
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 0.66F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 32200F)));
                        Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 1000000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.startsWith("xxeng1mag"))
                {
                    int i = s.charAt(9) - 49;
                    debuggunnery("Engine Module: Magneto " + i + " Destroyed..");
                    FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, i);
                }
                if(s.endsWith("oil1"))
                {
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.25F, shot) > 0.0F)
                        debuggunnery("Engine Module: Oil Radiator Hit..");
                    FM.AS.hitOil(shot.initiator, 0);
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(13.5F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(16.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if((s.endsWith("lo1") || s.endsWith("lo2")) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(13.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if((s.endsWith("ro1") || s.endsWith("ro2")) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(13.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int j = s.charAt(6) - 49;
                if(getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 10.25F)
                {
                    FM.AS.hitTank(shot.initiator, j, 1);
                    if(World.Rnd().nextFloat() < 0.05F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.6F)
                        FM.AS.hitTank(shot.initiator, j, 2);
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
                return;
            }
            if(s.startsWith("xxmgun01"))
            {
                if(getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    FM.AS.setJamBullets(0, 0);
                    getEnergyPastArmor(11.98F, shot);
                }
                return;
            }
            if(s.startsWith("xxmgun02"))
            {
                if(getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F)
                {
                    FM.AS.setJamBullets(0, 1);
                    getEnergyPastArmor(11.98F, shot);
                }
                return;
            }
            if(s.startsWith("xxoil"))
                return;
            else
                return;
        }
        if(s.startsWith("xcf"))
        {
            hitChunk("CF", shot);
            if(point3d.x > 0.30499999999999999D && point3d.x < 1.597D)
            {
                if(point3d.x > 1.202D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
                if(Aircraft.v1.x < -0.80000001192092896D && World.Rnd().nextFloat() < 0.2F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                if(point3d.z > 0.57699999999999996D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                else
                if(point3d.y > 0.0D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                else
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
            }
        } else
        if(!s.startsWith("xblister"))
            if(s.startsWith("xeng"))
                hitChunk("Engine1", shot);
            else
            if(s.startsWith("xtail"))
            {
                if(chunkDamageVisible("Tail1") < 3)
                    hitChunk("Tail1", shot);
            } else
            if(s.startsWith("xkeel"))
                hitChunk("Keel1", shot);
            else
            if(s.startsWith("xrudder"))
            {
                if(chunkDamageVisible("Rudder1") < 2)
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
                if(s.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 2)
                    hitChunk("VatorL", shot);
                if(s.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 2)
                    hitChunk("VatorR", shot);
            } else
            if(s.startsWith("xwing"))
            {
                if(s.startsWith("xWingLIn") && chunkDamageVisible("WingLIn") < 3)
                    hitChunk("WingLIn", shot);
                if(s.startsWith("xWingRIn") && chunkDamageVisible("WingRIn") < 3)
                    hitChunk("WingRIn", shot);
                if(s.startsWith("xWingLMid"))
                {
                    if(chunkDamageVisible("WingLMid") < 3)
                        hitChunk("WingLMid", shot);
                    if(World.Rnd().nextFloat() < shot.mass + 0.02F)
                        FM.AS.hitOil(shot.initiator, 0);
                }
                if(s.startsWith("xWingRMid") && chunkDamageVisible("WingRMid") < 3)
                    hitChunk("WingRMid", shot);
                if(s.startsWith("xWingLOut") && chunkDamageVisible("WingLOut") < 3)
                    hitChunk("WingLOut", shot);
                if(s.startsWith("xWingROut") && chunkDamageVisible("WingROut") < 3)
                    hitChunk("WingROut", shot);
            } else
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
                hitFlesh(k, shot, byte0);
            }
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void update(float f)
    {
        super.update(f);
        float f1 = FM.EI.engines[0].getControlRadiator();
        if(Math.abs(flapps - f1) > 0.01F)
        {
            flapps = f1;
            hierMesh().chunkSetAngles("Oil_radiat_D0", 0.0F, -15F * f1, 0.0F);
            for(int i = 1; i < 11; i++)
                hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, 22F * f1, 0.0F);

        }
        super.onAircraftLoaded();
        if(super.FM.isPlayers())
            if(!Main3D.cur3D().isViewOutside())
            {
                hierMesh().chunkVisible("CF_D0", false);
                hierMesh().chunkVisible("Oil_radiat_D0", false);
                hierMesh().chunkVisible("Interior_D0", false);
                hierMesh().chunkVisible("Blister2_D0", false);
                hierMesh().chunkVisible("Blister3_D0", false);
            } else
            {
                hierMesh().chunkVisible("CF_D0", true);
                hierMesh().chunkVisible("Oil_radiat_D0", true);
                hierMesh().chunkVisible("Interior_D0", true);
                hierMesh().chunkVisible("Blister2_D0", true);
                hierMesh().chunkVisible("Blister3_D0", true);
            }
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

    private float flapps;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.KI_51.class;
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
    }
}
