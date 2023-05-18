
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;


public class MI_8TERMINATOR extends MI8xyz
    implements TypeSACLOS
{

    public MI_8TERMINATOR()
    {
        soldiers = null;
        saclosSpotPos = new Point3d();
        bSACLOSenabled = false;
        tLastSaclosUpdate = -1L;
        bHasSturm = false;
        tLastSturmCheck = -1L;
    }

    private void checkCargos()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j].haveBullets())
                    {
                        if(FM.CT.Weapons[i][j] instanceof BombGunPara)
                        {
                            soldiers = (BombGunPara) (FM.CT.Weapons[i][j]);
                            break;
                        }
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunSturmV_gn16)
                            bHasSturm = true;
                    }
                }
            }

    }

// ---- TypeSACLOS implements .... begin ----
    public Point3d getSACLOStarget()
    {
        return saclosSpotPos;
    }

    public boolean setSACLOStarget(Point3d p3d)
    {
        saclosSpotPos.set(p3d);

        return true;
    }

    public boolean getSACLOSenabled()
    {
        return bSACLOSenabled;
    }

    public boolean setSACLOSenable(boolean flag)
    {
        return bSACLOSenabled = flag;
    }
// ---- TypeSACLOS implements .... end ----

    private void saclosUpdate()
    {
        if(Time.current() == tLastSaclosUpdate)
            return;

        tLastSaclosUpdate = Time.current();
        Orient orient2 = new Orient();
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d, orient2);

        super.pos.setUpdateEnable(true);
        super.pos.getRender(Actor._tmpLoc);
        saclosHook = new HookNamed(this, "_MGUN01");
        saclosLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        saclosHook.computePos(this, Actor._tmpLoc, saclosLoc1);
        saclosLoc1.get(saclosP1);
        saclosLoc1.set(30000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
        saclosHook.computePos(this, Actor._tmpLoc, saclosLoc1);
        saclosLoc1.get(saclosP2);
        if(Landscape.rayHitHQ(saclosP1, saclosP2, saclosPL))
        {
            saclosPL.z -= 0.95D;
            saclosP2.interpolate(saclosP1, saclosPL, 1.0F);
            setSACLOStarget(saclosP2);
            bSACLOSenabled = true;
        }
        else
            bSACLOSenabled = false;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(soldiers != null)
        {
            if(soldiers.countBullets() < 6)
            {
                soldiers = null;
                hierMesh().chunkVisible("SoldiersB", false);
            }
            else if(soldiers.countBullets() < 15)
                hierMesh().chunkVisible("SoldiersA", false);
        }

        if(bHasSturm && Time.current() > tLastSturmCheck + 120000L)
        {
            int sturmnum = 0;
            for(int i = 0; i < FM.CT.Weapons.length; i++)
                if(FM.CT.Weapons[i] != null)
                {
                    for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                    {
                        if(FM.CT.Weapons[i][j].haveBullets())
                        {
                            if(FM.CT.Weapons[i][j] instanceof RocketGunSturmV_gn16)
                                sturmnum += FM.CT.Weapons[i][j].countBullets();
                        }
                    }
                }

            if(sturmnum == 0)
                bHasSturm = false;
        }
    }

    // Both "Cockpit door control" and "BombBay door control" move the same front-portside slide door.
    // Avoiding conflicting.
    public void moveCockpitDoor(float f)
    {
        if(FM.CT.getBayDoor() > 0.05F)
            return;

        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.8F);
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.042F);
        hierMesh().chunkSetLocate("Door1_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveBayDoor(float f)
    {
        if(FM.CT.getCockpitDoor() > 0.05F)
            return;
        FM.CT.bHasCockpitDoorControl = (f < 0.05F);

        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.8F);
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.042F);
        hierMesh().chunkSetLocate("Door1_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxmainrotor"))
            {
                if(getEnergyPastArmor(0.11F, shot) > 0.0F && World.Rnd().nextFloat() < 0.33F)
                    hitProp(0, 2, shot.initiator);
            }
            if(s.startsWith("xxtailrotor"))
            {
                if(getEnergyPastArmor(0.063F, shot) > 0.0F && World.Rnd().nextFloat() < 0.3F)
                    hitProp(1, 2, shot.initiator);
            }
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1") || s.endsWith("p2"))
                    getEnergyPastArmor(16.65F / (1E-005F + (float)Math.abs(Aircraft.v1.x)), shot);
                return;
            }
            if(s.startsWith("xxcontrols"))
            {
                if(s.endsWith("1"))
                {
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                } else
                if(s.endsWith("2"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < 0.33F)
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                            FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
                            Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine 1 Controls Disabled..");
                        }
                        if(World.Rnd().nextFloat() < 0.33F)
                        {
                            FM.AS.setEngineSpecificDamage(shot.initiator, 1, 1);
                            FM.AS.setEngineSpecificDamage(shot.initiator, 1, 6);
                            FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
                            Aircraft.debugprintln(this, "*** Throttle Quadrant: Hit, Engine 2 Controls Disabled..");
                        }
                    }
                } else
                if(s.endsWith("3") || s.endsWith("4"))
                {
                    if(World.Rnd().nextFloat() < 0.12F)
                    {
                        FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Evelator Controls Out..");
                    }
                } else
                if(World.Rnd().nextFloat() < 0.12F)
                {
                    FM.AS.setControlsDamage(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Arone Controls Out..");
                }
                return;
            }
            if(s.startsWith("xxspar"))
            {
                if((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.5F / (float)Math.sqrt(((Tuple3d) (Aircraft.v1)).y * ((Tuple3d) (Aircraft.v1)).y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if((s.endsWith("lo1") || s.endsWith("lo2")) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if((s.endsWith("ro1") || s.endsWith("ro2")) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxeng"))
            {
                int i = s.charAt(5) - 49;
                Aircraft.debugprintln(this, "*** Engine" + i + " Hit..");
                if(s.endsWith("case") && getEnergyPastArmor(0.1F, shot) > 0.0F)
                {
                    if(World.Rnd().nextFloat() < shot.power / 200000F)
                    {
                        FM.AS.setEngineStuck(shot.initiator, i);
                        Aircraft.debugprintln(this, "*** Engine Case Hit - Engine Stucks..");
                    }
                    if(World.Rnd().nextFloat() < shot.power / 50000F)
                    {
                        FM.AS.hitEngine(shot.initiator, i, 2);
                        Aircraft.debugprintln(this, "*** Engine Case Hit - Engine Damaged..");
                    }
                    FM.EI.engines[i].setReadyness(shot.initiator, FM.EI.engines[i].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                    Aircraft.debugprintln(this, "*** Engine Case Hit - Readyness Reduced to " + FM.EI.engines[i].getReadyness() + "..");
                } else
                if(s.endsWith("oil") && getEnergyPastArmor(0.5F, shot) > 0.0F)
                {
                    debuggunnery("Engine Module " + i + ": Oil Radiator Hit, Oil Radiator Pierced..");
                    FM.AS.hitOil(shot.initiator, i);
                }
                return;
            }
            if(s.startsWith("xxtank") && getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
            {
                int j = s.charAt(6) - 49;
                doHitMeATank(shot, j);
                return;
            }
            if(s.startsWith("xxw1") || s.startsWith("xxoil1"))
            {
                if(World.Rnd().nextFloat() < 0.12F && getEnergyPastArmor(2.25F, shot) > 0.0F)
                {
                    FM.AS.hitOil(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Oil Radiator L Hit..");
                }
                return;
            }
            if(s.startsWith("xxw2") || s.startsWith("xxoil1"))
            {
                if(World.Rnd().nextFloat() < 0.12F && getEnergyPastArmor(2.25F, shot) > 0.0F)
                {
                    FM.AS.hitOil(shot.initiator, 1);
                    Aircraft.debugprintln(this, "*** Oil Radiator R Hit..");
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
            if(s.startsWith("xxmgun0"))
            {
                int k = s.charAt(7) - 49;
                if(getEnergyPastArmor(0.75F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Machine Gun (" + k + ") Disabled..");
                    FM.AS.setJamBullets(0, k);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if(s.startsWith("xxcannon0"))
            {
                int l = s.charAt(9) - 49;
                if(getEnergyPastArmor(6.29F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Cannon (" + l + ") Disabled..");
                    FM.AS.setJamBullets(1, l);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            } else
            {
                return;
            }
        }
        if(s.startsWith("xcf") || s.startsWith("xcockpit"))
        {
            hitChunk("CF", shot);
            if(point3d.x > 0.0D)
            {
                if(World.Rnd().nextFloat() < 0.1F)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
                if(point3d.z > 0.40D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            }
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
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
            hitChunk("Keel1", shot);
        else
        if(s.startsWith("xrudder"))
            hitChunk("Rudder1", shot);
        else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl"))
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr"))
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xvator"))
        {
            if(s.startsWith("xvatorl"))
                hitChunk("VatorL", shot);
            if(s.startsWith("xvatorr"))
                hitChunk("VatorR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xwinglmid"))
            {
                if(chunkDamageVisible("WingLMid") < 3)
                    hitChunk("WingLMid", shot);
                if(World.Rnd().nextFloat() < shot.mass + 0.02F)
                    FM.AS.hitOil(shot.initiator, 0);
            }
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
            if(s.endsWith("1"))
            {
                if(World.Rnd().nextFloat() < 0.05F)
                {
                    debuggunnery("Hydro System: Disabled..");
                    FM.AS.setInternalDamage(shot.initiator, 0);
                }
            } else
            if(World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(1.2F, 3.435F), shot) > 0.0F)
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
                    FM.AS.hitTank(shot.initiator, i, 2);
            } else
            {
                FM.AS.hitTank(shot.initiator, i, World.Rnd().nextInt(0, (int)(shot.power / 56000F)));
            }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            bObserverKilled = true;
            break;

        default:
            break;
        }
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        checkCargos();
        if(soldiers != null)
        {
            hierMesh().chunkVisible("SoldiersA", true);
            hierMesh().chunkVisible("SoldiersB", true);
        }
    }

    public void update(float f)
    {
        if(bHasSturm)
            saclosUpdate();
        super.update(f);
        if(this == World.getPlayerAircraft() && FM.turret.length > 0 && FM.AS.astatePilotStates[1] < 90 && FM.turret[0].bIsAIControlled && (FM.getOverload() > 3F || FM.getOverload() < -0.7F))
            Voice.speakRearGunShake();
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
        if(af[1] < -60F)
        {
            af[1] = -60F;
            flag = false;
        }
        if(af[1] > 60F)
        {
            af[1] = 60F;
            flag = false;
        }
        if(!flag)
            return false;
        float f1 = af[1];
        if(f < 1.2F && f1 < 13.3F)
            return false;
        else
            return f1 >= -3.1F || f1 <= -4.6F;
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

    private BombGunPara soldiers;

    private Point3d saclosSpotPos;
    private boolean bSACLOSenabled;
    private long tLastSaclosUpdate;
    private Hook saclosHook;
    private Loc saclosLoc1 = new Loc();
    private Point3d saclosP1 = new Point3d();
    private Point3d saclosP2 = new Point3d();
    private Point3d saclosPL = new Point3d();
    private boolean bHasSturm;
    private long tLastSturmCheck;

    static
    {
        Class class1 = com.maddox.il2.objects.air.MI_8TERMINATOR.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mi-8");
        Property.set(class1, "meshName", "3DO/Plane/Mi-8_Terminator/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 2001F);
        Property.set(class1, "yearExpired", 2020F);
        Property.set(class1, "FlightModel", "FlightModels/Mi-8MTV1.fmd:MI");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitMi8.class, com.maddox.il2.objects.air.CockpitMi8_TGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 3, 7, 7, 8, 8, 9, 9, 9, 9,
             9, 9, 3, 3, 3, 3, 3, 3, 2, 2,
             2, 2, 2, 2, 5, 5, 5, 5, 5, 5,
             5, 5, 4, 4, 4, 4, 4, 4, 4, 4,
             1, 1, 1, 1, 1, 1, 0, 0, 0, 0,
             0, 0, 0, 0, 9, 9, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01",          "_BombSpawn01",     "_Flare01",         "_Flare02",         "_Chaff01",         "_Chaff02",         "_ExternalDev01",   "_ExternalDev02",   "_ExternalDev03",   "_ExternalDev04",
            "_ExternalDev05",   "_ExternalDev06",   "_ExternalBomb01",  "_ExternalBomb02",  "_ExternalBomb03",  "_ExternalBomb04",  "_ExternalBomb05",  "_ExternalBomb06",  "_Rock01",          "_Rock02",
            "_Rock03",          "_Rock04",          "_Rock05",          "_Rock06",          "_ExternalRock07",  "_ExternalRock07",  "_ExternalRock08",  "_ExternalRock08",  "_ExternalRock09",  "_ExternalRock09",
            "_ExternalRock10",  "_ExternalRock10",  "_Sturm01",         "_Sturm01",         "_Sturm02",         "_Sturm02",         "_Sturm03",         "_Sturm03",         "_Sturm04",         "_Sturm04",
            "_CANNON01",        "_CANNON02",        "_CANNON03",        "_CANNON04",        "_CANNON05",        "_CANNON06",        "_MGUN11",          "_MGUN12",          "_MGUN13",          "_MGUN14",
            "_MGUN15",          "_MGUN16",          "_MGUN17",          "_MGUN18",          "_InternalDev05",   "_InternalDev06",   "_ExternalDev07",   "_ExternalDev08"
        });
    }
}