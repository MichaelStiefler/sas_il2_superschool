
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


public class MI8MT extends MI8xyz
    implements TypeSACLOS
{

    public MI8MT()
    {
        soldiers = null;
        cargoboxes = null;
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
                        else if(FM.CT.Weapons[i][j] instanceof BombGunCargoA)
                        {
                            cargoboxes = (BombGunCargoA) (FM.CT.Weapons[i][j]);
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
        if(cargoboxes != null)
        {
            if(cargoboxes.countBullets() < 4)
            {
                cargoboxes = null;
                hierMesh().chunkVisible("CargoBox", false);
                hierMesh().chunkVisible("CargoBox4a", false);
                hierMesh().chunkVisible("CargoBox4b", false);
            }
            else if(cargoboxes.countBullets() < 6)
            {
                hierMesh().chunkVisible("CargoBox", false);
                hierMesh().chunkVisible("CargoBox4a", true);
                hierMesh().chunkVisible("CargoBox4b", false);
            }
            else if(cargoboxes.countBullets() < 8)
            {
                hierMesh().chunkVisible("CargoBox", true);
                hierMesh().chunkVisible("CargoBox4a", false);
                hierMesh().chunkVisible("CargoBox4b", false);
            }
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
        if(thisWeaponsName.endsWith("mm"))
        {
            hierMesh().chunkVisible("Turret1BB_D0", true);
            hierMesh().chunkVisible("Leather_Window", true);
        }
        if(thisWeaponsName.indexOf("UB-32-57") > 0 || thisWeaponsName.indexOf("B-8") > 0 || thisWeaponsName.indexOf("GUV-") > 0 || thisWeaponsName.indexOf("UPK-23") > 0 ||
           thisWeaponsName.indexOf("R-60M") > 0 || thisWeaponsName.indexOf("FAB-") > 0 || thisWeaponsName.indexOf("Sturm-V") > 0 || thisWeaponsName.indexOf("PTB450L") > 0)
        {
            hierMesh().chunkVisible("PylonL_D0", true);
            hierMesh().chunkVisible("PylonR_D0", true);
        }
        checkCargos();
        if(cargoboxes != null)
        {
            if(cargoboxes.countBullets() == 4)
            {
                hierMesh().chunkVisible("CargoBox", false);
                hierMesh().chunkVisible("CargoBox4a", true);
                hierMesh().chunkVisible("CargoBox4b", false);
            }
            else if(cargoboxes.countBullets() == 6)
            {
                hierMesh().chunkVisible("CargoBox", true);
                hierMesh().chunkVisible("CargoBox4a", false);
                hierMesh().chunkVisible("CargoBox4b", false);
            }
            else
            {
                hierMesh().chunkVisible("CargoBox", false);
                hierMesh().chunkVisible("CargoBox4a", true);
                hierMesh().chunkVisible("CargoBox4b", true);
            }
        }
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
    private BombGunCargoA cargoboxes;

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
        Class class1 = com.maddox.il2.objects.air.MI8MT.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mi-8");
        Property.set(class1, "meshName", "3DO/Plane/Mi-8MT/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1978F);
        Property.set(class1, "yearExpired", 2020F);
        Property.set(class1, "FlightModel", "FlightModels/Mi-8MT.fmd:MI");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitMi8.class, com.maddox.il2.objects.air.CockpitMi8_TGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 3, 7, 7, 9, 9, 9, 9, 9, 9,
             3, 3, 3, 3, 3, 3, 2, 2, 2, 2,
             2, 2, 5, 5, 5, 5, 4, 4, 4, 4,
             4, 4, 4, 4, 1, 1, 1, 1, 1, 1,
             0, 0, 0, 0, 0, 0, 0, 0, 9, 9,
             9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01",          "_BombSpawn01",     "_Flare01",         "_Flare02",         "_ExternalDev01",   "_ExternalDev02",   "_ExternalDev03",   "_ExternalDev04",   "_ExternalDev05",   "_ExternalDev06",
            "_ExternalBomb01",  "_ExternalBomb02",  "_ExternalBomb03",  "_ExternalBomb04",  "_ExternalBomb05",  "_ExternalBomb06",  "_Rock01",          "_Rock02",          "_Rock03",          "_Rock04",
            "_Rock05",          "_Rock06",          "_ExternalRock07",  "_ExternalRock07",  "_ExternalRock08",  "_ExternalRock08",  "_Sturm01",         "_Sturm01",         "_Sturm02",         "_Sturm02",
            "_Sturm03",         "_Sturm03",         "_Sturm04",         "_Sturm04",         "_CANNON01",        "_CANNON02",        "_CANNON03",        "_CANNON04",        "_CANNON05",        "_CANNON06",
            "_MGUN11",          "_MGUN12",          "_MGUN13",          "_MGUN14",          "_MGUN15",          "_MGUN16",          "_MGUN17",          "_MGUN18",          "_InternalDev05",   "_InternalDev06",
            "_ExternalDev07",   "_ExternalDev08"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 52;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xUB-32-57";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xUB-32-57+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xUB-32-57+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xUB-32-57+ExtTank+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(9, "FuelTankGun_MilInternal450L_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "FuelTankGun_MilInternal450L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+2xGUV-8700+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunYakB", 750);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunYakB", 750);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+2xR-60M+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_APU60I_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_APU60I_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(5, "RocketGunR60M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(5, "RocketGunR60M_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB-8+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xB-8+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xB-8+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB-8+ExtTank+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(9, "FuelTankGun_MilInternal450L_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "FuelTankGun_MilInternal450L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xB-8+ExtTank+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(9, "FuelTankGun_MilInternal450L_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "FuelTankGun_MilInternal450L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB-8+2xR-60M+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_APU60I_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_APU60I_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(5, "RocketGunR60M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(5, "RocketGunR60M_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB-8+4xGUV-8700+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunYakB", 750);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunYakB", 750);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunYakB", 750);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunYakB", 750);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xB-8+2xGUV-1(AGS-17)+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700AGS_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700AGS_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGun30mmAGS17", 300);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGun30mmAGS17", 300);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB-8+2xUPK-23+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_UPK23_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_UPK23_gn16", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 250);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 250);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-250+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB-250+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xFAB-250+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB-100+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-500+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunFAB500M46_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-250+2xUB-32-57+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB-250+2xUB-32-57+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-250+2xUB-32-57+ExtTank+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(9, "FuelTankGun_MilInternal450L_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "FuelTankGun_MilInternal450L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB-250+2xB-8+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-250+2xB-8+ExtTank+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(9, "FuelTankGun_MilInternal450L_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "FuelTankGun_MilInternal450L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-250+2xGUV-1(AGS-17)+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700AGS_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700AGS_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGun30mmAGS17", 300);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGun30mmAGS17", 300);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xFAB-100+2xGUV-8700+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunFAB100M46_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunYakB", 750);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunYakB", 750);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-250+2xUPK-23+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_UPK23_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_UPK23_gn16", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 250);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 250);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGUV-1(AGS-17)+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700AGS_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700AGS_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGun30mmAGS17", 300);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGun30mmAGS17", 300);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGUV-8700+2xR-60M+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(1, "MGunYakB", 750);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(1, "MGunYakB", 750);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_APU60I_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_APU60I_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(5, "RocketGunR60M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(5, "RocketGunR60M_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xCargo+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunCargoA", 6);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xCargo+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunCargoA", 8);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xPTB450L+6xCargo+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunCargoA", 6);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_Mi24PTB450L_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_Mi24PTB450L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "16xSoldiers+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunPara", 16);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24xSoldiers+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunPara", 24);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xPTB450L+16xSoldiers+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunPara", 16);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_Mi24PTB450L_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_Mi24PTB450L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xUB-32-57+4xCargo+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunCargoA", 4);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xUB-32-57+6xCargo+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunCargoA", 6);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+6xCargo+2xR-60M+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunCargoA", 6);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_APU60I_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_APU60I_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(5, "RocketGunR60M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(5, "RocketGunR60M_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xUB-32-57+16xSoldiers+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunPara", 16);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xUB-32-57+24xSoldiers+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunPara", 24);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB-8+16xSoldiers+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunPara", 16);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGUV-1(AGS-17)+24xSoldiers+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunPara", 24);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700AGS_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700AGS_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGun30mmAGS17", 300);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGun30mmAGS17", 300);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+16xSoldiers+2xPTB450L+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunPara", 16);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_Mi24PTB450L_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_Mi24PTB450L_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+16xSoldiers+2xGUV-8700+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunPara", 16);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_GUV8700_gn16", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(1, "MGunYakB", 750);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(1, "MGunYakB", 750);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(0, "MGunGShG7_62", 900);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUB-32-57+16xSoldiers+2xR-60M+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(3, "BombGunPara", 16);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_UB32_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS5M_gn16", 32);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_APU60I_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_APU60I_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(5, "RocketGunR60M_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(5, "RocketGunR60M_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xSturm-V+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_rackSturm_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_rackSturm_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "Pylon_Sturm_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_Sturm_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB-8+4xSturm-V+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_B8V20_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunS8_gn16", 20);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_rackSturm_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_rackSturm_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "Pylon_Sturm_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_Sturm_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xFAB-250+4xSturm-V+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(3, "BombGunFAB250M46_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_rackSturm_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_rackSturm_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "Pylon_Sturm_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_Sturm_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xUPK-23+4xSturm-V+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_UPK23_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_UPK23_gn16", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 250);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(1, "MGunGSH23ki", 250);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_rackSturm_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_rackSturm_gn16", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "Pylon_Sturm_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_Sturm_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "RocketGunSturmV_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xPTB450L+ExtTank+1x12.7mm";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(10, "MGunDHK", 500);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 35);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_Mi24PTB450L_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_Mi24PTB450L_gn16", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(9, "FuelTankGun_MilInternal450L_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "FuelTankGun_MilInternal450L_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}