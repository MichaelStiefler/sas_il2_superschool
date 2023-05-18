// Source File Name:   TU_95X.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.*;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme4, TypeTransport, Aircraft, PaintScheme

public abstract class TU_95X extends Scheme4
    implements TypeTransport
{

    public TU_95X()
    {
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1") || s.endsWith("p2"))
                    if(Math.abs(((Tuple3d) (Aircraft.v1)).x) > 0.5D)
                        getEnergyPastArmor(7.940000057220459D / Math.abs(((Tuple3d) (Aircraft.v1)).x), shot);
                    else
                        getEnergyPastArmor(9.5299997329711914D / (1.0D - Math.abs(((Tuple3d) (Aircraft.v1)).x)), shot);
                if(s.endsWith("p3"))
                    getEnergyPastArmor(7.940000057220459D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("p4"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("p5") || s.endsWith("p6"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("p7"))
                    getEnergyPastArmor(0.5D / (Math.abs(((Tuple3d) (Aircraft.v1)).z) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("p8"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("a1"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("a2"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(((Tuple3d) (Aircraft.v1)).y) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("a3"))
                    getEnergyPastArmor(6.3499999046325684D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("a4") || s.endsWith("a5"))
                    getEnergyPastArmor(12.699999809265137D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("a6") || s.endsWith("a7"))
                    getEnergyPastArmor(6.3499999046325684D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("r1"))
                    getEnergyPastArmor(3.1700000762939453D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("r2") || s.endsWith("r3"))
                    getEnergyPastArmor(9.5299997329711914D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
                if(s.endsWith("c1") || s.endsWith("c2"))
                    getEnergyPastArmor(8.7299995422363281D / (Math.abs(((Tuple3d) (Aircraft.v1)).x) + 9.9999999747524271E-007D), shot);
            } else
            if(s.startsWith("xxcontrols"))
            {
                int i = s.charAt(10) - 48;
                switch(i)
                {
                case 1: // '\001'
                    if(getEnergyPastArmor(3F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.12F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                            debuggunnery("Evelator Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.12F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                            debuggunnery("Rudder Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.12F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                            debuggunnery("Aileron Controls Out..");
                        }
                    }
                    break;

                case 2: // '\002'
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            debuggunnery("*** Engine1 Throttle Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            debuggunnery("*** Engine1 Prop Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.25F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                            debuggunnery("Ailerons Controls Out..");
                        }
                    }
                    break;

                case 3: // '\003'
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            debuggunnery("*** Engine2 Throttle Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            debuggunnery("*** Engine2 Prop Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.25F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                            debuggunnery("Ailerons Controls Out..");
                        }
                    }
                    break;

                case 4: // '\004'
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            debuggunnery("*** Engine3 Throttle Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            debuggunnery("*** Engine3 Prop Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.25F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                            debuggunnery("Ailerons Controls Out..");
                        }
                    }
                    break;

                case 5: // '\005'
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            debuggunnery("*** Engine4 Throttle Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.15F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            debuggunnery("*** Engine4 Prop Controls Out..");
                        }
                        if(World.Rnd().nextFloat() < 0.25F)
                        {
                            ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                            debuggunnery("Ailerons Controls Out..");
                        }
                    }
                    break;

                case 6: // '\006'
                case 7: // '\007'
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.12F)
                    {
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                        debuggunnery("Evelator Controls Out..");
                    }
                    break;

                case 8: // '\b'
                case 9: // '\t'
                    if(getEnergyPastArmor(1.5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.12F)
                    {
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                        debuggunnery("Rudder Controls Out..");
                    }
                    break;
                }
            } else
            if(s.startsWith("xxspar"))
            {
                if(s.startsWith("xxspart") && World.Rnd().nextFloat() < 0.1F && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(19.9F / (float)Math.sqrt(((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y + ((Tuple3d) (Aircraft.v1)).z * ((Tuple3d) (Aircraft.v1)).z), shot) > 0.0F)
                {
                    debuggunnery("*** Tail1 Spars Broken in Half..");
                    msgCollision(this, "Tail1_D0", "Tail1_D0");
                }
                if((s.endsWith("li1") || s.endsWith("li2")) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if((s.endsWith("ri1") || s.endsWith("ri2")) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if((s.endsWith("lm1") || s.endsWith("lm2")) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if((s.endsWith("rm1") || s.endsWith("rm2")) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if((s.endsWith("lo1") || s.endsWith("lo2")) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if((s.endsWith("ro1") || s.endsWith("ro2")) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(19.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(s.endsWith("e1") && getEnergyPastArmor(28F, shot) > 0.0F)
                {
                    debuggunnery("*** Engine1 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine1_D0", shot.initiator);
                }
                if(s.endsWith("e2") && getEnergyPastArmor(28F, shot) > 0.0F)
                {
                    debuggunnery("*** Engine2 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine2_D0", shot.initiator);
                }
                if(s.endsWith("e3") && getEnergyPastArmor(28F, shot) > 0.0F)
                {
                    debuggunnery("*** Engine1 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine3_D0", shot.initiator);
                }
                if(s.endsWith("e4") && getEnergyPastArmor(28F, shot) > 0.0F)
                {
                    debuggunnery("*** Engine2 Suspension Broken in Half..");
                    nextDMGLevels(3, 2, "Engine4_D0", shot.initiator);
                }
                if(s.startsWith("xxspark1") && chunkDamageVisible("Keel1") > 1 && getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** Keel1 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel1_D2", shot.initiator);
                }
                if(s.startsWith("xxspark2") && chunkDamageVisible("Keel2") > 1 && getEnergyPastArmor(9.6F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F)
                {
                    debuggunnery("*** Keel2 Spars Damaged..");
                    nextDMGLevels(1, 2, "Keel2_D2", shot.initiator);
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
            } else
            if(s.startsWith("xxbomb"))
            {
                if(World.Rnd().nextFloat() < 0.01F && ((FlightModelMain) (super.FM)).CT.Weapons[3] != null && ((FlightModelMain) (super.FM)).CT.Weapons[3][0].haveBullets())
                {
                    debuggunnery("*** Bomb Payload Detonates..");
                    ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 0, 100);
                    ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 1, 100);
                    ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 2, 100);
                    ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, 3, 100);
                    nextDMGLevels(3, 2, "CF_D0", shot.initiator);
                }
            } else
            if(s.startsWith("xxeng"))
            {
                byte byte0 = 0;
                if(s.startsWith("xxeng2"))
                    byte0 = 1;
                if(s.startsWith("xxeng3"))
                    byte0 = 2;
                if(s.startsWith("xxeng4"))
                    byte0 = 3;
                debuggunnery("Engine Module[" + byte0 + "]: Hit..");
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 280000F)
                        {
                            debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                            ((FlightModelMain) (super.FM)).AS.setEngineStuck(shot.initiator, byte0);
                        }
                        if(World.Rnd().nextFloat() < shot.power / 100000F)
                        {
                            debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, byte0, 2);
                        }
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < ((FlightModelMain) (super.FM)).EI.engines[byte0].getCylindersRatio() * 0.66F)
                    {
                        ((FlightModelMain) (super.FM)).EI.engines[byte0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 32200F)));
                        debuggunnery("Engine Module: Cylinders Hit, " + ((FlightModelMain) (super.FM)).EI.engines[byte0].getCylindersOperable() + "/" + ((FlightModelMain) (super.FM)).EI.engines[byte0].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 1000000F)
                        {
                            ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, byte0, 2);
                            debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("eqpt") || s.endsWith("cyls") && World.Rnd().nextFloat() < 0.01F)
                {
                    if(getEnergyPastArmor(0.5F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, byte0, 4);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, byte0, 0);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, byte0, 6);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, byte0, 1);
                    }
                    getEnergyPastArmor(2.0F, shot);
                }
                if(s.endsWith("gear"))
                {
                    if(getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        debuggunnery("Engine Module: Bullet Jams Reductor Gear..");
                        ((FlightModelMain) (super.FM)).EI.engines[byte0].setEngineStuck(shot.initiator);
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 12.44565F), shot);
                }
                if(s.endsWith("mag1") || s.endsWith("mag2"))
                {
                    debuggunnery("Engine Module: Magneto " + byte0 + " Destroyed..");
                    ((FlightModelMain) (super.FM)).EI.engines[byte0].setMagnetoKnockOut(shot.initiator, byte0);
                }
                if(s.endsWith("oil1"))
                {
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.25F, shot) > 0.0F)
                        debuggunnery("Engine Module: Oil Radiator Hit..");
                    ((FlightModelMain) (super.FM)).AS.hitOil(shot.initiator, byte0);
                }
                if(s.endsWith("prop") && getEnergyPastArmor(0.42F, shot) > 0.0F)
                    ((FlightModelMain) (super.FM)).EI.engines[byte0].setKillPropAngleDevice(shot.initiator);
                if(s.endsWith("supc") && getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 12F), shot) > 0.0F)
                {
                    debuggunnery("Engine Module: Turbine Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setEngineSpecificDamage(shot.initiator, byte0, 0);
                }
            } else
            if(s.startsWith("xxtank"))
            {
                int j = s.charAt(6) - 48;
                switch(j)
                {
                case 1: // '\001'
                case 2: // '\002'
                    doHitMeATank(shot, 1);
                    break;

                case 3: // '\003'
                    doHitMeATank(shot, 0);
                    break;

                case 4: // '\004'
                case 5: // '\005'
                    doHitMeATank(shot, 2);
                    break;

                case 6: // '\006'
                    doHitMeATank(shot, 3);
                    break;

                case 7: // '\007'
                    doHitMeATank(shot, 0);
                    doHitMeATank(shot, 1);
                    doHitMeATank(shot, 2);
                    doHitMeATank(shot, 3);
                    break;
                }
            } else
            if(s.startsWith("xxpnm"))
            {
                if(getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 1.22F), shot) > 0.0F)
                {
                    debuggunnery("Pneumo System: Disabled..");
                    ((FlightModelMain) (super.FM)).AS.setInternalDamage(shot.initiator, 1);
                }
            } else
            if(s.startsWith("xxmgun02"))
            {
                ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 0);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(s.startsWith("xxmgun07"))
            {
                ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 0);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(s.startsWith("xxmgun08"))
            {
                ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 1);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(s.startsWith("xxmgun09"))
            {
                ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 2);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(s.startsWith("xxmgun10"))
            {
                ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 3);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(s.startsWith("xxmgun13"))
            {
                ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 4);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(s.startsWith("xxmgun14"))
            {
                ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 5);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(s.startsWith("xxmgun15"))
            {
                ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 6);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(s.startsWith("xxmgun16"))
            {
                ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 7);
                getEnergyPastArmor(12.7F, shot);
            } else
            if(s.startsWith("xxcannon"))
            {
                ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 0);
                getEnergyPastArmor(44.7F, shot);
            } else
            if(s.startsWith("xxlock"))
            {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if(s.startsWith("xxlockr1") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockr2") && getEnergyPastArmor(6.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Rudder2 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder2_D" + chunkDamageVisible("Rudder2"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
            } else
            if(s.startsWith("xxammo0"))
            {
                int k = s.charAt(7) - 48;
                byte byte2;
                byte byte3;
                switch(k)
                {
                default:
                    byte2 = 0;
                    byte3 = 0;
                    break;

                case 2: // '\002'
                    byte2 = 10;
                    byte3 = 0;
                    break;

                case 3: // '\003'
                    byte2 = 11;
                    byte3 = 0;
                    break;

                case 4: // '\004'
                    byte2 = 11;
                    byte3 = 1;
                    break;

                case 5: // '\005'
                    byte2 = 12;
                    byte3 = 0;
                    break;

                case 6: // '\006'
                    byte2 = 12;
                    byte3 = 1;
                    break;

                case 7: // '\007'
                    byte2 = 0;
                    byte3 = 0;
                    break;

                case 8: // '\b'
                    byte2 = 0;
                    byte3 = 1;
                    break;

                case 9: // '\t'
                    byte2 = 0;
                    byte3 = 2;
                    break;

                case 10: // '\n'
                    byte2 = 0;
                    byte3 = 3;
                    break;
                }
                if(World.Rnd().nextFloat() < 0.125F)
                    ((FlightModelMain) (super.FM)).AS.setJamBullets(byte2, byte3);
                getEnergyPastArmor(4.7F, shot);
            }
        } else
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(World.Rnd().nextFloat() < 0.0575F)
                if(((Tuple3d) (point3d)).y > 0.0D)
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 8);
                else
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x20);
            if(((Tuple3d) (point3d)).x > 1.726D)
            {
                if(((Tuple3d) (point3d)).z > 0.44400000000000001D)
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 1);
                if(((Tuple3d) (point3d)).z > -0.28100000000000003D && ((Tuple3d) (point3d)).z < 0.44400000000000001D)
                    if(((Tuple3d) (point3d)).y > 0.0D)
                        ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 4);
                    else
                        ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x10);
                if(((Tuple3d) (point3d)).x > 2.774D && ((Tuple3d) (point3d)).x < 3.718D && ((Tuple3d) (point3d)).z > 0.42499999999999999D)
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 2);
                if(World.Rnd().nextFloat() < 0.12F)
                    ((FlightModelMain) (super.FM)).AS.setCockpitState(shot.initiator, ((FlightModelMain) (super.FM)).AS.astateCockpitState | 0x40);
            }
        } else
        if(s.startsWith("xnose"))
        {
            if(chunkDamageVisible("Nose1") < 2)
                hitChunk("Nose1", shot);
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
            if(chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
        } else
        if(s.startsWith("xvatorr"))
        {
            if(chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwinglin"))
        {
            if(chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
        } else
        if(s.startsWith("xwingrin"))
        {
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
        {
            if(chunkDamageVisible("AroneL") < 1)
                hitChunk("AroneL", shot);
        } else
        if(s.startsWith("xaroner"))
        {
            if(chunkDamageVisible("AroneR") < 1)
                hitChunk("AroneR", shot);
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
        if(s.startsWith("xengine3"))
        {
            if(chunkDamageVisible("Engine3") < 2)
                hitChunk("Engine3", shot);
        } else
        if(s.startsWith("xengine4"))
        {
            if(chunkDamageVisible("Engine4") < 2)
                hitChunk("Engine4", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(World.Rnd().nextFloat() < 0.1F)
            {
                debuggunnery("*** Gear Hydro Failed..");
                ((FlightModelMain) (super.FM)).Gears.setHydroOperable(false);
            }
        } else
        if(s.startsWith("xturret"))
        {
            if(s.startsWith("xturret1"))
                ((FlightModelMain) (super.FM)).AS.setJamBullets(10, 0);
            if(s.endsWith("2b1"))
                ((FlightModelMain) (super.FM)).AS.setJamBullets(11, 0);
            if(s.endsWith("2b2"))
                ((FlightModelMain) (super.FM)).AS.setJamBullets(11, 1);
            if(s.endsWith("3b1"))
                ((FlightModelMain) (super.FM)).AS.setJamBullets(12, 0);
            if(s.endsWith("3b2"))
                ((FlightModelMain) (super.FM)).AS.setJamBullets(12, 1);
            if(s.endsWith("4a"))
                ((FlightModelMain) (super.FM)).AS.setJamBullets(13, 1);
            if(s.endsWith("5a"))
                ((FlightModelMain) (super.FM)).AS.setJamBullets(14, 1);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte1 = 0;
            int l;
            if(s.endsWith("a"))
            {
                byte1 = 1;
                l = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte1 = 2;
                l = s.charAt(6) - 49;
            } else
            {
                l = s.charAt(5) - 49;
            }
            hitFlesh(l, shot, byte1);
        }
    }

    private final void doHitMeATank(Shot shot, int i)
    {
        if(getEnergyPastArmor(0.2F, shot) > 0.0F)
            if(shot.power < 14100F)
            {
                if(((FlightModelMain) (super.FM)).AS.astateTankStates[i] == 0)
                {
                    ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i, 1);
                    ((FlightModelMain) (super.FM)).AS.doSetTankState(shot.initiator, i, 1);
                }
                if(shot.powerType == 3 && ((FlightModelMain) (super.FM)).AS.astateTankStates[i] > 0 && World.Rnd().nextFloat() < 0.25F)
                    ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i, 2);
            } else
            {
                ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int)(shot.power / 56000F)));
            }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 91F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.01F, 0.06F, 0.0F, 93F));
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.01F, 0.06F, 0.0F, 93F));
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 54F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC7_D0", 0.0F, 108F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC8_D0", 0.0F, 59F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -81F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.12F, 0.0F, 67F), 0.0F);
        hiermesh.chunkSetAngles("GearL8_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.12F, 0.0F, -67F), 0.0F);
        hiermesh.chunkSetAngles("GearL9_D0", 0.0F, Aircraft.cvt(f, 0.01F, 1.0F, 0.0F, 84F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -81F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.12F, 0.0F, -67F), 0.0F);
        hiermesh.chunkSetAngles("GearR8_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.12F, 0.0F, 67F), 0.0F);
        hiermesh.chunkSetAngles("GearR9_D0", 0.0F, Aircraft.cvt(f, 0.01F, 1.0F, 0.0F, -84F), 0.0F);
        hiermesh.chunkSetAngles("GearR10_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 45F), 0.0F);
        hiermesh.chunkSetAngles("GearL10_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 45F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[2], 0.0F, 0.325F, 0.0F, 0.325F);
        hierMesh().chunkSetLocate("GearC3_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[0], 0.0F, 0.325F, 0.0F, -0.325F);
        hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(((FlightModelMain) (super.FM)).Gears.gWheelSinking[1], 0.0F, 0.325F, 0.0F, -0.325F);
        hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f)
    {
        super.moveRudder(f);
        if(((FlightModelMain) (super.FM)).CT.getGear() > 0.9F)
            hierMesh().chunkSetAngles("GearC33_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = -0.4436F * f;
        Aircraft.xyz[2] = 0.063F * f;
        Aircraft.ypr[1] = 30F * f;
        hierMesh().chunkSetLocate("Flap01_D0", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetLocate("Flap02_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 13: // '\r'
            killPilot(this, 2);
            return false;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("Bay01_D0", 0.0F, 0.0F, -90F * f);
        hierMesh().chunkSetAngles("Bay02_D0", 0.0F, 0.0F, -90F * f);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
        {
            if(((FlightModelMain) (super.FM)).AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.0023F)
                ((FlightModelMain) (super.FM)).AS.hitTank(this, 0, 1);
            if(((FlightModelMain) (super.FM)).AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.0023F)
                ((FlightModelMain) (super.FM)).AS.hitTank(this, 1, 1);
            if(((FlightModelMain) (super.FM)).AS.astateEngineStates[2] > 3 && World.Rnd().nextFloat() < 0.0023F)
                ((FlightModelMain) (super.FM)).AS.hitTank(this, 2, 1);
            if(((FlightModelMain) (super.FM)).AS.astateEngineStates[3] > 3 && World.Rnd().nextFloat() < 0.0023F)
                ((FlightModelMain) (super.FM)).AS.hitTank(this, 3, 1);
            if(((FlightModelMain) (super.FM)).AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(((FlightModelMain) (super.FM)).AS.astateEffectChunks[0] + "0", 0, this);
            if(((FlightModelMain) (super.FM)).AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(((FlightModelMain) (super.FM)).AS.astateEffectChunks[1] + "0", 0, this);
            if(((FlightModelMain) (super.FM)).AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(((FlightModelMain) (super.FM)).AS.astateEffectChunks[2] + "0", 0, this);
            if(((FlightModelMain) (super.FM)).AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.04F)
                nextDMGLevel(((FlightModelMain) (super.FM)).AS.astateEffectChunks[3] + "0", 0, this);
            if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
            {
                for(int i = 0; i < ((FlightModelMain) (super.FM)).EI.getNum(); i++)
                    if(((FlightModelMain) (super.FM)).AS.astateEngineStates[i] > 3 && World.Rnd().nextFloat() < 0.2F)
                        ((FlightModelMain) (super.FM)).EI.engines[i].setExtinguisherFire();

            }
        }
        for(int j = 1; j < 7; j++)
            if(super.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + j + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + j + "_D0", hierMesh().isChunkVisible("Pilot" + j + "_D0"));

    }

    public void update(float f)
    {
        if(super.FM.getSpeedKMH() > 700F && ((FlightModelMain) (super.FM)).CT.bHasFlapsControl)
        {
            ((FlightModelMain) (super.FM)).CT.FlapsControl = 0.0F;
            ((FlightModelMain) (super.FM)).CT.bHasFlapsControl = false;
        } else
        {
            ((FlightModelMain) (super.FM)).CT.bHasFlapsControl = true;
        }
        float f1 = super.FM.getSpeedKMH() - 700F;
        if(f1 < 0.0F)
            f1 = 0.0F;
        ((FlightModelMain) (super.FM)).CT.dvGear = 0.2F - f1 / 500F;
        if(((FlightModelMain) (super.FM)).CT.dvGear < 0.0F)
            ((FlightModelMain) (super.FM)).CT.dvGear = 0.0F;
        super.update(f);
        for(int i = 0; i < 4; i++)
        {
            float f2 = ((FlightModelMain) (super.FM)).EI.engines[i].getControlRadiator();
            if(Math.abs(flapps[i] - f2) > 0.01F)
            {
                flapps[i] = f2;
                hierMesh().chunkSetAngles("Water" + (i + 1) + "_D0", 0.0F, -10F * f2, 0.0F);
                for(int j = 0; j < 8; j++)
                    hierMesh().chunkSetAngles("Water" + (i * 8 + j + 5) + "_D0", 0.0F, 0.0F, -20F * f2);

            }
        }

    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 3: // '\003'
            super.FM.turret[0].setHealth(f);
            break;

        case 4: // '\004'
            super.FM.turret[1].setHealth(f);
            break;

        }
    }

    public void doMurderPilot(int i)
    {
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
        hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
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

        case 0: // '\0'
            if(f < -60F)
            {
                f = -60F;
                flag = false;
            }
            if(f > 60F)
            {
                f = 60F;
                flag = false;
            }
            if(f1 < -35F)
            {
                f1 = -35F;
                flag = false;
            }
            if(f1 > 5F)
            {
                f1 = 5F;
                flag = false;
            }
            break;

        case 1: // '\001'
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
            if(f1 < -45F)
            {
                f1 = -45F;
                flag = false;
            }
            if(f1 > 45F)
            {
                f1 = 45F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
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

    private float flapps[] = {
        0.0F, 0.0F, 0.0F, 0.0F
    };

    static 
    {
        Class class1 = com.maddox.il2.objects.air.TU_95X.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}