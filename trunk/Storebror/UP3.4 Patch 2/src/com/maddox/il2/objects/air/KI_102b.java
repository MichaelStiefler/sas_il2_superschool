package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class KI_102b extends Scheme1
    implements TypeBNZFighter, TypeStormovik, TypeX4Carrier, TypeGuidedBombCarrier
{

    public KI_102b()
    {
        bGunUp = false;
        btme = -1L;
        fGunPos = 0.0F;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        isGuidingBomb = false;
    }

    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName) {
        hierMesh.chunkVisible("BatWingRack_D0", thisWeaponsName.endsWith("Igo_1_B"));
    }
        
    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        KI_102b.prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName);
    }

    public void update(float f)
    {
        super.update(f);
        if(!bGunUp)
        {
            if(fGunPos > 0.0F)
            {
                fGunPos -= 0.2F * f;
                this.FM.turret[0].bIsOperable = false;
                hierMesh().chunkVisible("RakkaO_D0", false);
                hierMesh().chunkVisible("RakkaC_D0", true);
            }
        } else
        if(fGunPos < 1.0F)
        {
            fGunPos += 0.2F * f;
            if(fGunPos > 0.8F && fGunPos < 0.9F)
            {
                this.FM.turret[0].bIsOperable = true;
                hierMesh().chunkVisible("RakkaO_D0", true);
                hierMesh().chunkVisible("RakkaC_D0", false);
            }
        }
        if(this.FM.turret[0].bIsAIControlled)
        {
            if(this.FM.turret[0].target != null && this.FM.AS.astatePilotStates[2] < 90)
                bGunUp = true;
            if(Time.current() > btme)
            {
                btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
                if(this.FM.turret[0].target == null && this.FM.AS.astatePilotStates[2] < 90)
                    bGunUp = false;
            }
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        float f1 = Math.max(-f * 800F, -80F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 70F * f);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 100F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 100F * f);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, -150F * f);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, -150F * f);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -25F * f);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 25F * f);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -25F * f);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("FlapL_D0", 0.0F, 0.0F, 40F * f);
        hierMesh().chunkSetAngles("FlapR_D0", 0.0F, 0.0F, 40F * f);
    }

    public void doKillPilot(int i)
    {
        switch(i)
        {
        case 1:
            this.FM.turret[0].bIsOperable = false;
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("Head" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("HMask" + (i + 1) + "_D0", false);
        hierMesh().chunkVisible("Pilot" + (i + 1) + "_D1", true);
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        case 0:
            if(f < -30F)
            {
                f = -30F;
                flag = false;
            }
            if(f > 30F)
            {
                f = 30F;
                flag = false;
            }
            if(f1 < -1F)
            {
                f1 = -1F;
                flag = false;
            }
            if(f1 > 30F)
            {
                f1 = 30F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(flag)
        {
            if(this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.0023F)
                this.FM.AS.hitTank(this, 0, 1);
            if(this.FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.0023F)
                this.FM.AS.hitTank(this, 1, 1);
            if(this.FM.AS.astateEngineStates[2] > 3 && World.Rnd().nextFloat() < 0.0023F)
                this.FM.AS.hitTank(this, 2, 1);
            if(this.FM.AS.astateEngineStates[3] > 3 && World.Rnd().nextFloat() < 0.0023F)
                this.FM.AS.hitTank(this, 3, 1);
        }
        super.rareAction(f, flag);
        for(int i = 1; i < 3; i++)
            if(this.FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                if(s.endsWith("p1"))
                    getEnergyPastArmor(12.7F, shot);
                if(s.endsWith("p2"))
                    getEnergyPastArmor(12.7F, shot);
                if(s.endsWith("p3"))
                    getEnergyPastArmor(12.7F, shot);
            } else
            if(s.startsWith("xxeng"))
            {
                int i = s.charAt(5) - 49;
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(1.7F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat(20000F, 140000F) < shot.power)
                            this.FM.AS.setEngineStuck(shot.initiator, i);
                        if(World.Rnd().nextFloat(10000F, 50000F) < shot.power)
                            this.FM.AS.hitEngine(shot.initiator, i, 2);
                    } else
                    if(World.Rnd().nextFloat() < 0.04F)
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, 1);
                    else
                        this.FM.EI.engines[i].setReadyness(shot.initiator, this.FM.EI.engines[i].getReadyness() - 0.02F);
                    getEnergyPastArmor(12F, shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[i].getCylindersRatio() * 0.9878F)
                    {
                        this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 19000F)));
                        if(World.Rnd().nextFloat() < shot.power / 48000F)
                            this.FM.AS.hitEngine(shot.initiator, 0, 2);
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("supc"))
                {
                    if(getEnergyPastArmor(0.05F, shot) > 0.0F)
                        this.FM.EI.engines[i].setKillCompressor(shot.initiator);
                    getEnergyPastArmor(2.0F, shot);
                }
                if(s.endsWith("oil1") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.25F, shot) > 0.0F)
                    this.FM.AS.hitOil(shot.initiator, 0);
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int j = s.charAt(6) - 49;
                if(getEnergyPastArmor(1.2F, shot) > 0.0F && World.Rnd().nextFloat() < 0.4F)
                {
                    if(this.FM.AS.astateTankStates[j] == 0)
                    {
                        this.FM.AS.hitTank(shot.initiator, j, 1);
                        this.FM.AS.doSetTankState(shot.initiator, j, 1);
                    }
                    if(World.Rnd().nextFloat() < 0.003F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.2F)
                        this.FM.AS.hitTank(shot.initiator, j, 4);
                }
                return;
            }
        }
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 3)
                hitChunk("CF", shot);
            if(World.Rnd().nextFloat() < 0.32F)
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            if(World.Rnd().nextFloat() < 0.32F)
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
            if(World.Rnd().nextFloat() < 0.32F)
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            if(World.Rnd().nextFloat() < 0.32F)
                this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
        } else
        if(s.startsWith("xcn"))
        {
            if(chunkDamageVisible("CN") < 3)
                hitChunk("CN", shot);
        } else
        if(s.startsWith("xtail1"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel1"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xrudder1"))
        {
            if(chunkDamageVisible("Rudder1") < 1)
                hitChunk("Rudder1", shot);
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
        if(s.startsWith("xgear"))
        {
            if(s.startsWith("xgear") && World.Rnd().nextFloat() < 0.1F && getEnergyPastArmor(World.Rnd().nextFloat(12.88F, 16.96F), shot) > 0.0F)
                this.FM.AS.setInternalDamage(shot.initiator, 3);
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int k;
            if(s.endsWith("a") || s.endsWith("a2"))
            {
                byte0 = 1;
                k = s.charAt(6) - 49;
            } else
            if(s.endsWith("b") || s.endsWith("b2"))
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

    public boolean typeGuidedBombCisMasterAlive()
    {
        return isMasterAlive;
    }

    public void typeGuidedBombCsetMasterAlive(boolean flag)
    {
        isMasterAlive = flag;
    }

    public boolean typeGuidedBombCgetIsGuiding()
    {
        return isGuidingBomb;
    }

    public void typeGuidedBombCsetIsGuiding(boolean flag)
    {
        isGuidingBomb = flag;
    }

    public void typeX4CAdjSidePlus()
    {
        deltaAzimuth = 0.002F;
    }

    public void typeX4CAdjSideMinus()
    {
        deltaAzimuth = -0.002F;
    }

    public void typeX4CAdjAttitudePlus()
    {
        deltaTangage = 0.002F;
    }

    public void typeX4CAdjAttitudeMinus()
    {
        deltaTangage = -0.002F;
    }

    public void typeX4CResetControls()
    {
        deltaAzimuth = deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth()
    {
        return deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage()
    {
        return deltaTangage;
    }

    boolean bGunUp;
    public long btme;
    public float fGunPos;
    private float deltaAzimuth;
    private float deltaTangage;
    private boolean isGuidingBomb;
    private boolean isMasterAlive;

    static 
    {
        Class class1 = KI_102b.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Randy");
        Property.set(class1, "meshName", "3DO/Plane/KI-102b(multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/KI102b.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitKI102b.class
        });
        Property.set(class1, "LOSElevation", 0.74185F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 1, 1, 10, 3, 3, 9, 9, 9, 9, 
            3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_MGUN01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", 
            "_ExternalBomb03"
        });
    }
}
