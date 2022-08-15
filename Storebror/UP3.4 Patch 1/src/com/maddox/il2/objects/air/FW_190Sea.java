package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.PylonETC501FW190;
import com.maddox.rts.Property;

public abstract class FW_190Sea extends Scheme1
    implements TypeFighter, TypeBNZFighter
{

    public FW_190Sea()
    {
        arrestor = 0.0F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(World.cur().camouflage == 1)
        {
            hierMesh().chunkVisible("GearL5_D0", false);
            hierMesh().chunkVisible("GearR5_D0", false);
        }
        Object objects[] = this.pos.getBaseAttached();
        if(objects != null)
        {
            for(int i = 0; i < objects.length; i++)
            {
                if(!(objects[i] instanceof PylonETC501FW190))
                    continue;
                hierMesh().chunkVisible("GearL5_D0", false);
                hierMesh().chunkVisible("GearR5_D0", false);
                break;
            }
        }
        
        FW_190Sea.prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName);
    }

    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName) {

        boolean winter = Config.isUSE_RENDER() && (World.cur().camouflage == 1);
        hierMesh.chunkVisible("GearL5_D0", !winter);
        hierMesh.chunkVisible("GearR5_D0", !winter);

        String planeVersion = aircraftClass.getName().substring(33);
        
        _WeaponSlot[] weaponSlotsRegistered = Aircraft.getWeaponSlotsRegistered(aircraftClass, thisWeaponsName);
        
        if (planeVersion.startsWith("SeaFg")) {
            hierMesh.chunkVisible("7mmC_D0", weaponSlotsRegistered[0] != null);
            hierMesh.chunkVisible("7mmCowl_D0", weaponSlotsRegistered[0] == null);
            hierMesh.chunkVisible("20mmL1_D0", weaponSlotsRegistered[2] != null);
            hierMesh.chunkVisible("20mmR1_D0", weaponSlotsRegistered[3] != null);
            hierMesh.chunkVisible("20mmL_D0", weaponSlotsRegistered[4] != null);
            hierMesh.chunkVisible("20mmR_D0", weaponSlotsRegistered[5] != null);
            return;
        }
        
        if (planeVersion.startsWith("SeaDora")) {
            return;
        }
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -60F * f, 0.0F);
        arrestor = f;
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, 0.0F, -130F * f);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, 0.0F, 130F * f);
    }

    public void moveWingFold(float f)
    {
        if(f < 0.001F)
        {
            setGunPodsOn(true);
            hideWingWeapons(false);
        } else
        {
            setGunPodsOn(true);
            this.FM.CT.WeaponControl[0] = false;
            hideWingWeapons(false);
        }
        moveWingFold(hierMesh(), f);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.53F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        float f1 = (float)Math.sin(Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 3.141593F));
        hierMesh().chunkSetAngles("Pilot1_D0", 0.0F, 0.0F, 9F * f1);
        hierMesh().chunkSetAngles("Head1_D0", 12F * f1, 0.0F, 0.0F);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("FlaperonL_D0", 0.0F, -30F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -55F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -55F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap03_D0", 0.0F, -55F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap04_D0", 0.0F, -55F * f, 0.0F);
        hierMesh().chunkSetAngles("FlaperonR_D0", 0.0F, 30F * f, 0.0F);
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            break;
        }
    }

    public void rareAction(float f, boolean bool)
    {
        super.rareAction(f, bool);
        if(this.FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
    }

    public void update(float f)
    {
        if(this.FM.AS.bIsAboutToBailout)
            hierMesh().chunkVisible("Wire_D0", false);
        super.update(f);
    }

    public boolean cut(String string)
    {
        if(string.startsWith("Tail1"))
            this.FM.AS.hitTank(this, 2, 4);
        return super.cut(string);
    }

    protected boolean cutFM(int i, int i_1_, Actor actor)
    {
        switch(i)
        {
        case 33:
            return super.cutFM(34, i_1_, actor);

        case 36:
            return super.cutFM(37, i_1_, actor);

        case 34:
        case 35:
        default:
            return super.cutFM(i, i_1_, actor);
        }
    }

    protected void hitBone(String string, Shot shot, Point3d point3d)
    {
        if(string.startsWith("xx"))
        {
            if(string.startsWith("xxarmor"))
            {
                Aircraft.debugprintln(this, "*** Armor: Hit..");
                if(string.endsWith("p1"))
                {
                    getEnergyPastArmor(World.Rnd().nextFloat(50F, 50F), shot);
                    if(World.Rnd().nextFloat() < 0.15F)
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
                    Aircraft.debugprintln(this, "*** Armor Glass: Hit..");
                    if(shot.power <= 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** Armor Glass: Bullet Stopped..");
                        if(World.Rnd().nextFloat() < 0.5F)
                            doRicochetBack(shot);
                    }
                } else
                if(string.endsWith("p3"))
                {
                    if(point3d.z < -0.27D)
                        getEnergyPastArmor(4.1D / (Math.abs(Aircraft.v1.z) + 0.00001D), shot);
                    else
                        getEnergyPastArmor(8.1D / (Math.abs(Aircraft.v1.x) + 0.00001D), shot);
                } else
                if(string.endsWith("p6"))
                    getEnergyPastArmor(8D / (Math.abs(Aircraft.v1.x) + 0.00001D), shot);
            } else
            if(string.startsWith("xxcontrols"))
            {
                int i = string.charAt(10) - 48;
                switch(i)
                {
                case 1:
                case 4:
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Aileron Controls: Control Crank Destroyed..");
                    }
                    break;

                case 2:
                case 3:
                    if(getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                        Aircraft.debugprintln(this, "*** Aileron Controls: Disabled..");
                    }
                    break;

                case 5:
                    if(getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Elevator Controls: Disabled / Strings Broken..");
                    }
                    break;

                case 6:
                    if(getEnergyPastArmor(0.12F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls: Disabled / Strings Broken..");
                    }
                    break;

                case 8:
                    if(getEnergyPastArmor(3.2F, shot) > 0.0F)
                    {
                        Aircraft.debugprintln(this, "*** Control Column: Hit, Controls Destroyed..");
                        this.FM.AS.setControlsDamage(shot.initiator, 2);
                        this.FM.AS.setControlsDamage(shot.initiator, 1);
                        this.FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;
                }
            } else
            if(string.startsWith("xxspar"))
            {
                Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
                if(string.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(2.4F / (float)Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                if(string.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(18F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(string.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(18F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(string.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(string.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(string.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(string.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
            } else
            if(string.startsWith("xxlock"))
            {
                Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
                if(string.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(string.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(string.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
            } else
            if(string.startsWith("xxeng"))
            {
                Aircraft.debugprintln(this, "*** Engine Module: Hit..");
                if(string.endsWith("pipe"))
                {
                    if(World.Rnd().nextFloat() < 0.1F && this.FM.EI.engines[0].getType() == 0 && this.FM.CT.Weapons[1] != null && this.FM.CT.Weapons[1].length != 2)
                    {
                        this.FM.AS.setJamBullets(1, 0);
                        Aircraft.debugprintln(this, "*** Engine Module: Nose Nozzle Pipe Bent..");
                    }
                    getEnergyPastArmor(0.3F, shot);
                } else
                if(string.endsWith("prop"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.8F)
                        if(World.Rnd().nextFloat() < 0.5F)
                        {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Disabled..");
                        } else
                        {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                            Aircraft.debugprintln(this, "*** Engine Module: Prop Governor Hit, Damaged..");
                        }
                } else
                if(string.endsWith("gear"))
                {
                    if(getEnergyPastArmor(4.6F, shot) > 0.0F)
                        if(World.Rnd().nextFloat() < 0.5F)
                        {
                            this.FM.EI.engines[0].setEngineStuck(shot.initiator);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Reductor Gear..");
                        } else
                        {
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 3);
                            this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                            Aircraft.debugprintln(this, "*** Engine Module: Reductor Gear Damaged, Prop Governor Failed..");
                        }
                } else
                if(string.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        this.FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                        Aircraft.debugprintln(this, "*** Engine Module: Supercharger Disabled..");
                    }
                    getEnergyPastArmor(0.5F, shot);
                } else
                if(string.endsWith("feed"))
                {
                    if(getEnergyPastArmor(8.9F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F && this.FM.EI.engines[0].getPowerOutput() > 0.7F && this.FM.EI.engines[0].getType() == 0)
                    {
                        this.FM.AS.hitEngine(shot.initiator, 0, 100);
                        Aircraft.debugprintln(this, "*** Engine Module: Pressurized Fuel Line Pierced, Fuel Flamed..");
                    }
                    getEnergyPastArmor(1.0F, shot);
                } else
                if(string.endsWith("fuel"))
                {
                    if(getEnergyPastArmor(1.1F, shot) > 0.0F && this.FM.EI.engines[0].getType() == 0)
                    {
                        this.FM.EI.engines[0].setEngineStops(shot.initiator);
                        Aircraft.debugprintln(this, "*** Engine Module: Fuel Line Stalled, Engine Stalled..");
                    }
                    getEnergyPastArmor(1.0F, shot);
                } else
                if(string.endsWith("case"))
                {
                    if(getEnergyPastArmor(4.2F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 175000F)
                        {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 50000F && this.FM.EI.engines[0].getType() == 0)
                        {
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                        }
                        if(this.FM.EI.engines[0].getType() == 0)
                            this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
                        Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
                    }
                    getEnergyPastArmor(27.5F, shot);
                } else
                if(string.startsWith("xxeng1cyl"))
                {
                    if(getEnergyPastArmor(2.4F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * (this.FM.EI.engines[0].getType() != 0 ? 0.5F : 1.75F))
                    {
                        if(this.FM.EI.engines[0].getType() == 0)
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 4800F)));
                        else
                            this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19200F)));
                        Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 96000F && this.FM.EI.engines[0].getType() == 0)
                        {
                            this.FM.AS.hitEngine(shot.initiator, 0, 3);
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if(World.Rnd().nextFloat() < shot.power / 96000F && this.FM.EI.engines[0].getType() == 1)
                        {
                            this.FM.AS.hitEngine(shot.initiator, 0, 1);
                            Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
                        }
                        if(World.Rnd().nextFloat() < 0.01F)
                        {
                            this.FM.AS.setEngineStuck(shot.initiator, 0);
                            Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
                        }
                        getEnergyPastArmor(43.6F, shot);
                    }
                } else
                if(string.startsWith("xxeng1mag"))
                {
                    int i = string.charAt(9) - 49;
                    this.FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, i);
                    Aircraft.debugprintln(this, "*** Engine Module: Magneto " + i + " Destroyed..");
                } else
                if(string.endsWith("sync"))
                {
                    if(getEnergyPastArmor(2.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                        Aircraft.debugprintln(this, "*** Engine Module: Gun Synchronized Hit, Nose Guns Lose Authority..");
                } else
                if(string.endsWith("oil1") && getEnergyPastArmor(2.4F, shot) > 0.0F)
                {
                    this.FM.AS.hitOil(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
                }
            } else
            if(string.startsWith("xxtank"))
            {
                int i = string.charAt(6) - 48;
                switch(i)
                {
                case 1:
                    if(getEnergyPastArmor(0.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        if(this.FM.AS.astateTankStates[2] == 0)
                        {
                            Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                            this.FM.AS.hitTank(shot.initiator, 2, 1);
                            this.FM.AS.doSetTankState(shot.initiator, 2, 1);
                        } else
                        if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.9F || World.Rnd().nextFloat() < 0.03F)
                        {
                            this.FM.AS.hitTank(shot.initiator, 2, 2);
                            Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                        }
                        if(shot.power > 200000F)
                        {
                            this.FM.AS.hitTank(shot.initiator, 2, 99);
                            Aircraft.debugprintln(this, "*** Fuel Tank: Major Hit..");
                        }
                    }
                    break;

                case 2:
                    if(getEnergyPastArmor(1.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        if(this.FM.AS.astateTankStates[1] == 0)
                        {
                            Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                            this.FM.AS.hitTank(shot.initiator, 1, 1);
                            this.FM.AS.doSetTankState(shot.initiator, 1, 1);
                        } else
                        if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.8F || World.Rnd().nextFloat() < 0.03F)
                        {
                            this.FM.AS.hitTank(shot.initiator, 1, 2);
                            Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                        }
                        if(shot.power > 200000F)
                        {
                            this.FM.AS.hitTank(shot.initiator, 1, 99);
                            Aircraft.debugprintln(this, "*** Fuel Tank: Major Hit..");
                        }
                    }
                    break;

                case 3:
                    if(getEnergyPastArmor(1.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                    {
                        if(this.FM.AS.astateTankStates[0] == 0)
                        {
                            Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                            this.FM.AS.hitTank(shot.initiator, 0, 1);
                            this.FM.AS.doSetTankState(shot.initiator, 0, 1);
                        } else
                        if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.8F || World.Rnd().nextFloat() < 0.03F)
                        {
                            this.FM.AS.hitTank(shot.initiator, 0, 2);
                            Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                        }
                        if(shot.power > 200000F)
                        {
                            this.FM.AS.hitTank(shot.initiator, 0, 99);
                            Aircraft.debugprintln(this, "*** Fuel Tank: Major Hit..");
                        }
                    }
                    break;
                }
            } else
            if(string.startsWith("xxmw50"))
            {
                if(World.Rnd().nextFloat() < 0.05F)
                {
                    Aircraft.debugprintln(this, "*** MW50 Tank: Pierced..");
                    this.FM.AS.setInternalDamage(shot.initiator, 2);
                }
            } else
            if(string.startsWith("xxmgun"))
            {
                if(string.endsWith("01"))
                    this.FM.AS.setJamBullets(1, 0);
                if(string.endsWith("02"))
                    this.FM.AS.setJamBullets(1, 1);
                getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            } else
            if(string.startsWith("xxcannon"))
            {
                Aircraft.debugprintln(this, "*** Nose Cannon: Disabled..");
                this.FM.AS.setJamBullets(0, 0);
                getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), shot);
            } else
            if(string.startsWith("xxradiat"))
            {
                this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, 0.05F));
                Aircraft.debugprintln(this, "*** Engine Module: Radiator Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
            }
        } else
        if(string.startsWith("xcf") || string.startsWith("xcockpit"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(string.startsWith("xcockpit"))
            {
                if(point3d.z > 0.4D)
                {
                    if(World.Rnd().nextFloat() < 0.2F)
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
                    if(World.Rnd().nextFloat() < 0.2F)
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
                } else
                if(point3d.y > 0.0D)
                {
                    if(World.Rnd().nextFloat() < 0.2F)
                        this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
                } else
                if(World.Rnd().nextFloat() < 0.2F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
                if(point3d.x > 0.2D && World.Rnd().nextFloat() < 0.2F)
                    this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            }
        } else
        if(string.startsWith("xeng"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(string.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(string.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(string.startsWith("xrudder"))
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
        } else
        if(string.startsWith("xstab"))
        {
            if(string.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
            if(string.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
                hitChunk("StabR", shot);
        } else
        if(string.startsWith("xvator"))
        {
            if(string.startsWith("xvatorl") && chunkDamageVisible("VatorL") < 1)
                hitChunk("VatorL", shot);
            if(string.startsWith("xvatorr") && chunkDamageVisible("VatorR") < 1)
                hitChunk("VatorR", shot);
        } else
        if(string.startsWith("xwing"))
        {
            if(string.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(string.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(string.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(string.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            if(string.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(string.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(string.startsWith("xarone"))
        {
            if(string.startsWith("xaronel"))
                hitChunk("AroneL", shot);
            if(string.startsWith("xaroner"))
                hitChunk("AroneR", shot);
        } else
        if(string.startsWith("xpilot") || string.startsWith("xhead"))
        {
            int i = 0;
            int i_2_;
            if(string.endsWith("a"))
            {
                i = 1;
                i_2_ = string.charAt(6) - 49;
            } else
            if(string.endsWith("b"))
            {
                i = 2;
                i_2_ = string.charAt(6) - 49;
            } else
            {
                i_2_ = string.charAt(5) - 49;
            }
            hitFlesh(i_2_, shot, i);
        }
    }

    protected float arrestor;

    static 
    {
        Class class1 = FW_190Sea.class;
        Property.set(class1, "originCountry", PaintScheme.countryGermany);
    }
}
