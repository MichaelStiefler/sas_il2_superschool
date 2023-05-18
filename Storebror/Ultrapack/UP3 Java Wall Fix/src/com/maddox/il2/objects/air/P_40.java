package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public abstract class P_40 extends Scheme1
    implements TypeFighter
{

    public P_40()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(FM.CT.Weapons[3] != null && FM.CT.Weapons[3][0] != null)
            hierMesh().chunkVisible("Pilon_D0", true);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Math.max(-f * 1100F, -90F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 92F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -99F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 92F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -99F * f, 0.0F);
    }

    protected void moveGear(float f)
    {
        float f1 = Math.max(-f * 1100F, -90F);
        HierMesh hiermesh = hierMesh();
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 90F * f, 0.0F);
        float f2 = cvt(f, 0.0F, 0.7F, 0.0F, 1.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 92F * f2, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -45F * f2, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -99F * f2, 0.0F);
        f2 = cvt(f, 0.2F, 1.0F, 0.0F, 1.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 92F * f2, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -45F * f2, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -99F * f2, 0.0F);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC99_D0", f, 0.0F, 0.0F);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        xyz[1] = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.17F, 0.0F, -0.1689F);
        ypr[1] = -92F * FM.CT.getGear();
        hierMesh().chunkSetLocate("GearL5_D0", xyz, ypr);
        xyz[1] = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.17F, 0.0F, 0.1689F);
        ypr[1] = -92F * FM.CT.getGear();
        hierMesh().chunkSetLocate("GearR5_D0", xyz, ypr);
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        default:
            break;

        case 0:
            if(!FM.AS.bIsAboutToBailout)
            {
                hierMesh().chunkVisible("Pilot1_D0", false);
                hierMesh().chunkVisible("Head1_D0", false);
                hierMesh().chunkVisible("Pilot1_D1", true);
            }
            break;
        }
    }

    protected void moveFlap(float f)
    {
        float f1 = -60F * f;
        hierMesh().chunkSetAngles("Flap1_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap2_D0", 0.0F, f1, 0.0F);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
    }

    public void msgExplosion(Explosion explosion)
    {
        setExplosion(explosion);
        if(explosion.chunkName != null)
        {
            if(explosion.chunkName.startsWith("WingL"))
            {
                if(World.Rnd().nextFloat() < 0.02F)
                    FM.AS.setJamBullets(0, 0);
                if(World.Rnd().nextFloat() < 0.02F)
                    FM.AS.setJamBullets(0, 1);
                if(World.Rnd().nextFloat() < 0.02F)
                    FM.AS.setJamBullets(0, 2);
            }
            if(explosion.chunkName.startsWith("WingR"))
            {
                if(World.Rnd().nextFloat() < 0.02F)
                    FM.AS.setJamBullets(0, 3);
                if(World.Rnd().nextFloat() < 0.02F)
                    FM.AS.setJamBullets(0, 4);
                if(World.Rnd().nextFloat() < 0.02F)
                    FM.AS.setJamBullets(0, 5);
            }
        }
        super.msgExplosion(explosion);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
                if(s.endsWith("p1"))
                {
                    getEnergyPastArmor(15F / (1E-005F + (float)Math.abs(v1.x)), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(4F / (1E-005F + (float)Math.abs(v1.x)), shot);
                else
                if(s.endsWith("p3"))
                    getEnergyPastArmor(2.0F / (1E-005F + (float)Math.abs(v1.x)), shot);
            if(s.startsWith("xxcontrols"))
                if(s.endsWith("1"))
                {
                    if(World.Rnd().nextFloat() < 0.3F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        debugprintln(this, "*** Engine Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.3F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        debugprintln(this, "*** Engine Controls Out..");
                    }
                } else
                if(s.endsWith("2"))
                {
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        debugprintln(this, "*** Evelator Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 0);
                        debugprintln(this, "*** Ailerones Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        debugprintln(this, "*** Rudder Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.3F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        debugprintln(this, "*** Engine Controls Out..");
                    }
                    if(World.Rnd().nextFloat() < 0.3F)
                    {
                        FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                        debugprintln(this, "*** Engine Controls Out..");
                    }
                } else
                if(s.endsWith("3"))
                {
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        debugprintln(this, "*** Rudder Controls Out..");
                    }
                } else
                if((s.endsWith("4") || s.endsWith("5")) && World.Rnd().nextFloat() < 0.3F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 0);
                    debugprintln(this, "*** Ailerones Controls Out..");
                }
            if(s.startsWith("xxeng1"))
            {
                if(s.endsWith("prp") && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                if(s.endsWith("cas") && getEnergyPastArmor(0.7F, shot) > 0.0F)
                {
                    if(World.Rnd().nextFloat(20000F, 200000F) < shot.power)
                    {
                        FM.AS.setEngineStuck(shot.initiator, 0);
                        debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
                    }
                    if(World.Rnd().nextFloat(10000F, 50000F) < shot.power)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 2);
                        debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
                    }
                    if(World.Rnd().nextFloat(8000F, 28000F) < shot.power)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
                        debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    }
                    FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
                }
                if(s.endsWith("cyl") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.75F)
                {
                    FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                    debugprintln(this, "*** Engine Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                    if(FM.AS.astateEngineStates[0] < 1)
                        FM.AS.hitEngine(shot.initiator, 0, 1);
                    if(World.Rnd().nextFloat() < shot.power / 24000F)
                    {
                        FM.AS.hitEngine(shot.initiator, 0, 3);
                        debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("sup") && getEnergyPastArmor(0.05F, shot) > 0.0F)
                    FM.EI.engines[0].setKillCompressor(shot.initiator);
                if(s.endsWith("sup"))
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
                if(s.endsWith("oil"))
                    FM.AS.hitOil(shot.initiator, 0);
            }
            if(s.startsWith("xxtank"))
            {
                int i = s.charAt(6) - 49;
                if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    FM.AS.hitTank(shot.initiator, i, 1);
                    if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.11F)
                        FM.AS.hitTank(shot.initiator, i, 2);
                }
            }
            if(s.startsWith("xxmgun"))
            {
                int j = s.charAt(6) - 49;
                FM.AS.setJamBullets(0, j);
            }
            if(s.startsWith("xxammo"))
            {
                int k = s.charAt(6) - 48;
                if(World.Rnd().nextFloat(0.0F, 20000F) < shot.power)
                    switch(k)
                    {
                    default:
                        break;

                    case 1:
                        if(World.Rnd().nextFloat() < 0.5F)
                            FM.AS.setJamBullets(0, 2);
                        else
                            FM.AS.setJamBullets(0, 1);
                        break;

                    case 2:
                        if(World.Rnd().nextFloat() < 0.5F)
                            FM.AS.setJamBullets(0, 0);
                        else
                            FM.AS.setJamBullets(0, 1);
                        break;

                    case 3:
                        if(World.Rnd().nextFloat() < 0.5F)
                            FM.AS.setJamBullets(0, 5);
                        else
                            FM.AS.setJamBullets(0, 4);
                        break;

                    case 4:
                        if(World.Rnd().nextFloat() < 0.5F)
                            FM.AS.setJamBullets(0, 3);
                        else
                            FM.AS.setJamBullets(0, 4);
                        break;
                    }
            }
            return;
        }
        if(s.startsWith("xcf") || s.startsWith("xcockpit"))
            hitChunk("CF", shot);
        if(s.startsWith("xcockpit"))
        {
            if(point3d.z > 0.75D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            if(point3d.x > -1.1D && World.Rnd().nextFloat() < 0.1F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            if(World.Rnd().nextFloat() < 0.25F)
                if(point3d.y > 0.0D)
                {
                    if(point3d.x > -1.1D)
                        FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                    else
                        FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                } else
                if(point3d.x > -1.1D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
                else
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
        } else
        if(s.startsWith("xeng"))
            hitChunk("Engine1", shot);
        else
        if(s.startsWith("xtail"))
            hitChunk("Tail1", shot);
        else
        if(s.startsWith("xkeel"))
            hitChunk("Keel1", shot);
        else
        if(s.startsWith("xrudder"))
            hitChunk("Rudder1", shot);
        else
        if(s.startsWith("xstabl"))
            hitChunk("StabL", shot);
        else
        if(s.startsWith("xstabr"))
            hitChunk("StabR", shot);
        else
        if(s.startsWith("xvatorl"))
            hitChunk("VatorL", shot);
        else
        if(s.startsWith("xvatorr"))
            hitChunk("VatorR", shot);
        else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglin"))
                hitChunk("WingLIn", shot);
            if(s.startsWith("xwingrin"))
                hitChunk("WingRIn", shot);
            if(s.startsWith("xwinglmid"))
                hitChunk("WingLMid", shot);
            if(s.startsWith("xwingrmid"))
                hitChunk("WingRMid", shot);
            if(s.startsWith("xwinglout"))
                hitChunk("WingLOut", shot);
            if(s.startsWith("xwingrout"))
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xarone"))
        {
            if(s.startsWith("xaronel"))
                hitChunk("AroneL", shot);
            if(s.startsWith("xaroner"))
                hitChunk("AroneR", shot);
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

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 11:
            hierMesh().chunkVisible("Wire_D0", false);
            break;

        case 35:
            FM.AS.setJamBullets(0, 0);
            FM.AS.setJamBullets(0, 1);
            break;

        case 38:
            FM.AS.setJamBullets(0, 4);
            FM.AS.setJamBullets(0, 5);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f)
    {
        float f1 = cvt(FM.EI.engines[0].getControlRadiator(), 0.0F, 1.0F, 27F, -11F);
        hierMesh().chunkSetAngles("Water2_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Water3_D0", 0.0F, f1, 0.0F);
        f1 = Math.min(f1, 10.5F);
        hierMesh().chunkSetAngles("Water1_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Water4_D0", 0.0F, f1, 0.0F);
        super.update(f);
    }

    static 
    {
        Property.set(CLASS.THIS(), "originCountry", PaintScheme.countryUSA);
    }
}
