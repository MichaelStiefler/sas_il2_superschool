// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 14/10/2015 11:54:16 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MeteorF3.java

package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.Property;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.air:
//            Scheme2, TypeFighter, TypeBNZFighter, PaintSchemeFMPar04, 
//            PaintScheme, Aircraft, Cockpit, AircraftLH, 
//            NetAircraft

public class MeteorF3 extends Scheme2
    implements TypeFighter, TypeBNZFighter
{

    public MeteorF3()
    {
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.48F);
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.05F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(super.FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
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
            if(!((FlightModelMain) (super.FM)).AS.bIsAboutToBailout && hierMesh().isChunkVisible("Blister1_D0"))
                hierMesh().chunkVisible("Gore1_D0", true);
            break;
        }
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 110F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, 90F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        float f1 = Math.max(-f * 1000F, -90F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f1, 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -20F * f, 0.0F);
        if(((FlightModelMain) (super.FM)).CT.getGear() > 0.75F)
            hierMesh().chunkSetAngles("GearC22_D0", 0.0F, -40F * f, 0.0F);
    }

    protected void moveAirBrake(float f)
    {
        hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 90F * f, 0.0F);
        hierMesh().chunkSetAngles("Brake03_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Brake04_D0", 0.0F, 90F * f, 0.0F);
    }

    protected void moveFlap(float f)
    {
        float f1 = -45F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xcf"))
        {
            if(chunkDamageVisible("CF") < 2)
                hitChunk("CF", shot);
        } else
        if(s.startsWith("xxtank1"))
        {
            int i = s.charAt(6) - 49;
            if(getEnergyPastArmor(0.4F, shot) > 0.0F && World.Rnd().nextFloat() < 0.99F)
            {
                if(((FlightModelMain) (super.FM)).AS.astateTankStates[i] == 0)
                {
                    Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                    ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i, 2);
                }
                if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F)
                {
                    ((FlightModelMain) (super.FM)).AS.hitTank(shot.initiator, i, 2);
                    Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
                }
            }
        } else
        if(s.startsWith("xxmgun"))
        {
            if(s.endsWith("01"))
            {
                Aircraft.debugprintln(this, "*** Cannon #1: Disabled..");
                ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 0);
            }
            if(s.endsWith("02"))
            {
                Aircraft.debugprintln(this, "*** Cannon #2: Disabled..");
                ((FlightModelMain) (super.FM)).AS.setJamBullets(0, 1);
            }
            if(s.endsWith("03"))
            {
                Aircraft.debugprintln(this, "*** Cannon #3: Disabled..");
                ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 0);
            }
            if(s.endsWith("04"))
            {
                Aircraft.debugprintln(this, "*** Cannon #4: Disabled..");
                ((FlightModelMain) (super.FM)).AS.setJamBullets(1, 1);
            }
            getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
        } else
        if(s.startsWith("xxcontrols"))
        {
            int j = s.charAt(10) - 48;
            switch(j)
            {
            case 1: // '\001'
                if(getEnergyPastArmor(World.Rnd().nextFloat(0.1F, 2.3F), shot) > 0.0F)
                {
                    if(World.Rnd().nextFloat() < 0.25F)
                    {
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 2);
                        Aircraft.debugprintln(this, "*** Rudder Controls: Disabled..");
                    }
                    if(World.Rnd().nextFloat() < 0.25F)
                    {
                        ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 1);
                        Aircraft.debugprintln(this, "*** Elevator Controls: Disabled..");
                    }
                }
                // fall through

            case 2: // '\002'
            case 3: // '\003'
                if(getEnergyPastArmor(1.5F, shot) > 0.0F)
                {
                    ((FlightModelMain) (super.FM)).AS.setControlsDamage(shot.initiator, 0);
                    Aircraft.debugprintln(this, "*** Aileron Controls: Disabled..");
                }
                break;
            }
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
            hitChunk("Keel1", shot);
        else
        if(s.startsWith("xstabl"))
            hitChunk("StabL", shot);
        else
        if(s.startsWith("xstabr"))
            hitChunk("StabR", shot);
        else
        if(s.startsWith("xwing"))
        {
            if(s.endsWith("lin") && chunkDamageVisible("WingLIn") < 2)
                hitChunk("WingLIn", shot);
            if(s.endsWith("rin") && chunkDamageVisible("WingRIn") < 2)
                hitChunk("WingRIn", shot);
            if(s.endsWith("lmid") && chunkDamageVisible("WingLMid") < 2)
                hitChunk("WingLMid", shot);
            if(s.endsWith("rmid") && chunkDamageVisible("WingRMid") < 2)
                hitChunk("WingRMid", shot);
            if(s.endsWith("lout") && chunkDamageVisible("WingLOut") < 2)
                hitChunk("WingLOut", shot);
            if(s.endsWith("rout") && chunkDamageVisible("WingROut") < 2)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xengine"))
        {
            int k = s.charAt(7) - 49;
            if(((Tuple3d) (point3d)).x > 0.0D && ((Tuple3d) (point3d)).x < 0.69699999999999995D)
                ((FlightModelMain) (super.FM)).EI.engines[k].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, 6));
            if(World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass)
                ((FlightModelMain) (super.FM)).AS.hitEngine(shot.initiator, k, 5);
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
            if(l == 2)
                l = 1;
            Aircraft.debugprintln(this, "*** hitFlesh..");
            hitFlesh(l, shot, byte0);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33: // '!'
            return super.cutFM(34, j, actor);

        case 36: // '$'
            return super.cutFM(37, j, actor);

        case 11: // '\013'
            cutFM(17, j, actor);
            super.FM.cut(17, j, actor);
            cutFM(18, j, actor);
            super.FM.cut(18, j, actor);
            return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f)
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster())
        {
            for(int i = 0; i < 2; i++)
                if(curctl[i] == -1F)
                {
                    curctl[i] = oldctl[i] = ((FlightModelMain) (super.FM)).EI.engines[i].getControlThrottle();
                } else
                {
                    curctl[i] = ((FlightModelMain) (super.FM)).EI.engines[i].getControlThrottle();
                    if((curctl[i] - oldctl[i]) / f > 3F && ((FlightModelMain) (super.FM)).EI.engines[i].getRPM() < 2400F && ((FlightModelMain) (super.FM)).EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.25F)
                        ((FlightModelMain) (super.FM)).AS.hitEngine(this, i, 100);
                    if((curctl[i] - oldctl[i]) / f < -3F && ((FlightModelMain) (super.FM)).EI.engines[i].getRPM() < 2400F && ((FlightModelMain) (super.FM)).EI.engines[i].getStage() == 6)
                    {
                        if(World.Rnd().nextFloat() < 0.25F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            ((FlightModelMain) (super.FM)).EI.engines[i].setEngineStops(this);
                        if(World.Rnd().nextFloat() < 0.75F && (super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode())
                            ((FlightModelMain) (super.FM)).EI.engines[i].setKillCompressor(this);
                    }
                    oldctl[i] = curctl[i];
                }

            if(Config.isUSE_RENDER())
            {
                if(((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 0.8F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 6)
                {
                    if(((FlightModelMain) (super.FM)).EI.engines[0].getPowerOutput() > 0.95F)
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 3);
                    else
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 2);
                } else
                {
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 0, 0);
                }
                if(((FlightModelMain) (super.FM)).EI.engines[1].getPowerOutput() > 0.8F && ((FlightModelMain) (super.FM)).EI.engines[1].getStage() == 6)
                {
                    if(((FlightModelMain) (super.FM)).EI.engines[1].getPowerOutput() > 0.95F)
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, 1, 3);
                    else
                        ((FlightModelMain) (super.FM)).AS.setSootState(this, 1, 2);
                } else
                {
                    ((FlightModelMain) (super.FM)).AS.setSootState(this, 1, 0);
                }
            }
        }
        super.update(f);
    }

    private float oldctl[] = {
        -1F, -1F
    };
    private float curctl[] = {
        -1F, -1F
    };

    static 
    {
        Class class1 = com.maddox.il2.objects.air.MeteorF3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Meteor");
        Property.set(class1, "meshName", "3DO/Plane/MeteorF3/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/MeteorF3.fmd:Meteors");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitMeteor.class
        });
        Property.set(class1, "LOSElevation", 0.6731F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 1, 9, 9, 9, 9, 9, 3, 
            3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
            2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 
            9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 
            2, 2, 2, 9, 9, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", 
            "_ExternalBomb02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", 
            "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", 
            "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", 
            "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17"
        });
        String as[] = new String[47];
        as[0] = "MGunHispanoMkIkpzl 200";
        as[1] = "MGunHispanoMkIkpzl 200";
        as[2] = "MGunHispanoMkIkpzl 190";
        as[3] = "MGunHispanoMkIkpzl 190";
        Aircraft.weaponsRegister(class1, "default", as);
        String as1[] = new String[47];
        as1[0] = "MGunHispanoMkIkpzl 200";
        as1[1] = "MGunHispanoMkIkpzl 200";
        as1[2] = "MGunHispanoMkIkpzl 190";
        as1[3] = "MGunHispanoMkIkpzl 190";
        as1[4] = "FuelTankGun_TankMeteor 1";
        Aircraft.weaponsRegister(class1, "droptank", as1);
        String as2[] = new String[47];
        as2[0] = "MGunHispanoMkIkpzl 200";
        as2[1] = "MGunHispanoMkIkpzl 200";
        as2[2] = "MGunHispanoMkIkpzl 190";
        as2[3] = "MGunHispanoMkIkpzl 190";
        as2[4] = "FuelTankGun_TankMeteor 1";
        as2[7] = "FuelTankGun_TankTempest 1";
        as2[8] = "FuelTankGun_TankTempest 1";
        Aircraft.weaponsRegister(class1, "3xdroptank", as2);
        String as3[] = new String[47];
        as3[0] = "MGunHispanoMkIkpzl 200";
        as3[1] = "MGunHispanoMkIkpzl 200";
        as3[2] = "MGunHispanoMkIkpzl 190";
        as3[3] = "MGunHispanoMkIkpzl 190";
        as3[5] = "PylonTEMPESTPLN1 1";
        as3[6] = "PylonTEMPESTPLN1 1";
        as3[9] = "BombGun500lbsMC 1";
        as3[10] = "BombGun500lbsMC 1";
        Aircraft.weaponsRegister(class1, "2x500", as3);
        String as4[] = new String[47];
        as4[0] = "MGunHispanoMkIkpzl 200";
        as4[1] = "MGunHispanoMkIkpzl 200";
        as4[2] = "MGunHispanoMkIkpzl 190";
        as4[3] = "MGunHispanoMkIkpzl 190";
        as4[4] = "FuelTankGun_TankMeteor 1";
        as4[5] = "PylonTEMPESTPLN1 1";
        as4[6] = "PylonTEMPESTPLN1 1";
        as4[9] = "BombGun500lbsMC 1";
        as4[10] = "BombGun500lbsMC 1";
        Aircraft.weaponsRegister(class1, "2x500_droptank", as4);
        String as5[] = new String[47];
        as5[0] = "MGunHispanoMkIkpzl 200";
        as5[1] = "MGunHispanoMkIkpzl 200";
        as5[2] = "MGunHispanoMkIkpzl 190";
        as5[3] = "MGunHispanoMkIkpzl 190";
        as5[5] = "PylonTEMPESTPLN1 1";
        as5[6] = "PylonTEMPESTPLN1 1";
        as5[9] = "BombGun500lbsMC 1";
        as5[10] = "BombGun500lbsMC 1";
        Aircraft.weaponsRegister(class1, "2x1000", as5);
        String as6[] = new String[47];
        as6[0] = "MGunHispanoMkIkpzl 200";
        as6[1] = "MGunHispanoMkIkpzl 200";
        as6[2] = "MGunHispanoMkIkpzl 190";
        as6[3] = "MGunHispanoMkIkpzl 190";
        as6[4] = "FuelTankGun_TankMeteor 1";
        as6[5] = "PylonTEMPESTPLN1 1";
        as6[6] = "PylonTEMPESTPLN1 1";
        as6[9] = "BombGun1000lbsMC 1";
        as6[10] = "BombGun1000lbsMC 1";
        Aircraft.weaponsRegister(class1, "2x1000_droptank", as6);
        String as7[] = new String[47];
        as7[0] = "MGunHispanoMkIkpzl 200";
        as7[1] = "MGunHispanoMkIkpzl 200";
        as7[2] = "MGunHispanoMkIkpzl 190";
        as7[3] = "MGunHispanoMkIkpzl 190";
        as7[11] = "RocketGunHVAR5BEAU 1";
        as7[12] = "RocketGunHVAR5BEAU 1";
        as7[15] = "RocketGunHVAR5BEAU 1";
        as7[16] = "RocketGunHVAR5BEAU 1";
        as7[19] = "RocketGunHVAR5BEAU 1";
        as7[20] = "RocketGunHVAR5BEAU 1";
        as7[23] = "RocketGunHVAR5BEAU 1";
        as7[24] = "RocketGunHVAR5BEAU 1";
        as7[27] = "PylonSpitROCK 1";
        as7[28] = "PylonSpitROCK 1";
        as7[29] = "PylonSpitROCK 1";
        as7[30] = "PylonSpitROCK 1";
        as7[31] = "PylonSpitROCK 1";
        as7[32] = "PylonSpitROCK 1";
        as7[33] = "PylonSpitROCK 1";
        as7[34] = "PylonSpitROCK 1";
        Aircraft.weaponsRegister(class1, "8xRP3", as7);
        String as8[] = new String[47];
        as8[0] = "MGunHispanoMkIkpzl 200";
        as8[1] = "MGunHispanoMkIkpzl 200";
        as8[2] = "MGunHispanoMkIkpzl 190";
        as8[3] = "MGunHispanoMkIkpzl 190";
        as8[4] = "FuelTankGun_TankMeteor 1";
        as8[11] = "RocketGunHVAR5BEAUBEAU 1";
        as8[12] = "RocketGunHVAR5BEAU 1";
        as8[15] = "RocketGunHVAR5BEAU 1";
        as8[16] = "RocketGunHVAR5BEAU 1";
        as8[19] = "RocketGunHVAR5BEAU 1";
        as8[20] = "RocketGunHVAR5BEAU 1";
        as8[23] = "RocketGunHVAR5BEAU 1";
        as8[24] = "RocketGunHVAR5BEAU 1";
        as8[27] = "PylonSpitROCK 1";
        as8[28] = "PylonSpitROCK 1";
        as8[29] = "PylonSpitROCK 1";
        as8[30] = "PylonSpitROCK 1";
        as8[31] = "PylonSpitROCK 1";
        as8[32] = "PylonSpitROCK 1";
        as8[33] = "PylonSpitROCK 1";
        as8[34] = "PylonSpitROCK 1";
        Aircraft.weaponsRegister(class1, "8xRP3_droptank", as8);
        String as9[] = new String[47];
        as9[0] = "MGunHispanoMkIkpzl 200";
        as9[1] = "MGunHispanoMkIkpzl 200";
        as9[2] = "MGunHispanoMkIkpzl 190";
        as9[3] = "MGunHispanoMkIkpzl 190";
        as9[11] = "RocketGunHVAR5BEAU 1";
        as9[12] = "RocketGunHVAR5BEAU 1";
        as9[13] = "RocketGunHVAR5BEAU 1";
        as9[14] = "RocketGunHVAR5BEAU 1";
        as9[15] = "RocketGunHVAR5BEAU 1";
        as9[16] = "RocketGunHVAR5BEAU 1";
        as9[17] = "RocketGunHVAR5BEAU 1";
        as9[18] = "RocketGunHVAR5BEAU 1";
        as9[19] = "RocketGunHVAR5BEAU 1";
        as9[20] = "RocketGunHVAR5BEAU 1";
        as9[21] = "RocketGunHVAR5BEAU 1";
        as9[22] = "RocketGunHVAR5BEAU 1";
        as9[23] = "RocketGunHVAR5BEAU 1";
        as9[24] = "RocketGunHVAR5BEAU 1";
        as9[25] = "RocketGunHVAR5BEAU 1";
        as9[26] = "RocketGunHVAR5BEAU 1";
        as9[27] = "PylonSpitROCK 1";
        as9[28] = "PylonSpitROCK 1";
        as9[29] = "PylonSpitROCK 1";
        as9[30] = "PylonSpitROCK 1";
        as9[31] = "PylonSpitROCK 1";
        as9[32] = "PylonSpitROCK 1";
        as9[33] = "PylonSpitROCK 1";
        as9[34] = "PylonSpitROCK 1";
        Aircraft.weaponsRegister(class1, "16xRP3", as9);
        String as10[] = new String[47];
        as10[0] = "MGunHispanoMkIkpzl 200";
        as10[1] = "MGunHispanoMkIkpzl 200";
        as10[2] = "MGunHispanoMkIkpzl 190";
        as10[3] = "MGunHispanoMkIkpzl 190";
        as10[4] = "FuelTankGun_TankMeteor 1";
        as10[11] = "RocketGunHVAR5BEAU 1";
        as10[12] = "RocketGunHVAR5BEAU 1";
        as10[13] = "RocketGunHVAR5BEAU 1";
        as10[14] = "RocketGunHVAR5BEAU 1";
        as10[15] = "RocketGunHVAR5BEAU 1";
        as10[16] = "RocketGunHVAR5BEAU 1";
        as10[17] = "RocketGunHVAR5BEAU 1";
        as10[18] = "RocketGunHVAR5BEAU 1";
        as10[19] = "RocketGunHVAR5BEAU 1";
        as10[20] = "RocketGunHVAR5BEAU 1";
        as10[21] = "RocketGunHVAR5BEAU 1";
        as10[22] = "RocketGunHVAR5BEAU 1";
        as10[23] = "RocketGunHVAR5BEAU 1";
        as10[24] = "RocketGunHVAR5BEAU 1";
        as10[25] = "RocketGunHVAR5BEAU 1";
        as10[26] = "RocketGunHVAR5BEAU 1";
        as10[27] = "PylonSpitROCK 1";
        as10[28] = "PylonSpitROCK 1";
        as10[29] = "PylonSpitROCK 1";
        as10[30] = "PylonSpitROCK 1";
        as10[31] = "PylonSpitROCK 1";
        as10[32] = "PylonSpitROCK 1";
        as10[33] = "PylonSpitROCK 1";
        as10[34] = "PylonSpitROCK 1";
        Aircraft.weaponsRegister(class1, "16xRP3_droptank", as10);
        String as11[] = new String[47];
        as11[0] = "MGunHispanoMkIkpzl 200";
        as11[1] = "MGunHispanoMkIkpzl 200";
        as11[2] = "MGunHispanoMkIkpzl 190";
        as11[3] = "MGunHispanoMkIkpzl 190";
        as11[11] = "RocketGunHVAR5BEAU25AP 1";
        as11[12] = "RocketGunHVAR5BEAU25AP 1";
        as11[15] = "RocketGunHVAR5BEAU25AP 1";
        as11[16] = "RocketGunHVAR5BEAU25AP 1";
        as11[19] = "RocketGunHVAR5BEAU25AP 1";
        as11[20] = "RocketGunHVAR5BEAU25AP 1";
        as11[23] = "RocketGunHVAR5BEAU25AP 1";
        as11[24] = "RocketGunHVAR5BEAU25AP 1";
        as11[27] = "PylonSpitROCK 1";
        as11[28] = "PylonSpitROCK 1";
        as11[29] = "PylonSpitROCK 1";
        as11[30] = "PylonSpitROCK 1";
        as11[31] = "PylonSpitROCK 1";
        as11[32] = "PylonSpitROCK 1";
        as11[33] = "PylonSpitROCK 1";
        as11[34] = "PylonSpitROCK 1";
        Aircraft.weaponsRegister(class1, "8xRP3_25", as11);
        String as12[] = new String[47];
        as12[0] = "MGunHispanoMkIkpzl 200";
        as12[1] = "MGunHispanoMkIkpzl 200";
        as12[2] = "MGunHispanoMkIkpzl 190";
        as12[3] = "MGunHispanoMkIkpzl 190";
        as12[4] = "FuelTankGun_TankMeteor 1";
        as12[11] = "RocketGunHVAR5BEAU25APBEAU 1";
        as12[12] = "RocketGunHVAR5BEAU25AP 1";
        as12[15] = "RocketGunHVAR5BEAU25AP 1";
        as12[16] = "RocketGunHVAR5BEAU25AP 1";
        as12[19] = "RocketGunHVAR5BEAU25AP 1";
        as12[20] = "RocketGunHVAR5BEAU25AP 1";
        as12[23] = "RocketGunHVAR5BEAU25AP 1";
        as12[24] = "RocketGunHVAR5BEAU25AP 1";
        as12[27] = "PylonSpitROCK 1";
        as12[28] = "PylonSpitROCK 1";
        as12[29] = "PylonSpitROCK 1";
        as12[30] = "PylonSpitROCK 1";
        as12[31] = "PylonSpitROCK 1";
        as12[32] = "PylonSpitROCK 1";
        as12[33] = "PylonSpitROCK 1";
        as12[34] = "PylonSpitROCK 1";
        Aircraft.weaponsRegister(class1, "8xRP3_25_droptank", as12);
        String as13[] = new String[47];
        as13[0] = "MGunHispanoMkIkpzl 200";
        as13[1] = "MGunHispanoMkIkpzl 200";
        as13[2] = "MGunHispanoMkIkpzl 190";
        as13[3] = "MGunHispanoMkIkpzl 190";
        as13[11] = "RocketGunHVAR5BEAU25AP 1";
        as13[12] = "RocketGunHVAR5BEAU25AP 1";
        as13[13] = "RocketGunHVAR5BEAU25AP 1";
        as13[14] = "RocketGunHVAR5BEAU25AP 1";
        as13[15] = "RocketGunHVAR5BEAU25AP 1";
        as13[16] = "RocketGunHVAR5BEAU25AP 1";
        as13[17] = "RocketGunHVAR5BEAU25AP 1";
        as13[18] = "RocketGunHVAR5BEAU25AP 1";
        as13[19] = "RocketGunHVAR5BEAU25AP 1";
        as13[20] = "RocketGunHVAR5BEAU25AP 1";
        as13[21] = "RocketGunHVAR5BEAU25AP 1";
        as13[22] = "RocketGunHVAR5BEAU25AP 1";
        as13[23] = "RocketGunHVAR5BEAU25AP 1";
        as13[24] = "RocketGunHVAR5BEAU25AP 1";
        as13[25] = "RocketGunHVAR5BEAU25AP 1";
        as13[26] = "RocketGunHVAR5BEAU25AP 1";
        as13[27] = "PylonSpitROCK 1";
        as13[28] = "PylonSpitROCK 1";
        as13[29] = "PylonSpitROCK 1";
        as13[30] = "PylonSpitROCK 1";
        as13[31] = "PylonSpitROCK 1";
        as13[32] = "PylonSpitROCK 1";
        as13[33] = "PylonSpitROCK 1";
        as13[34] = "PylonSpitROCK 1";
        Aircraft.weaponsRegister(class1, "16xRP3_25", as13);
        String as14[] = new String[47];
        as14[0] = "MGunHispanoMkIkpzl 200";
        as14[1] = "MGunHispanoMkIkpzl 200";
        as14[2] = "MGunHispanoMkIkpzl 190";
        as14[3] = "MGunHispanoMkIkpzl 190";
        as14[4] = "FuelTankGun_TankMeteor 1";
        as14[11] = "RocketGunHVAR5BEAU25AP 1";
        as14[12] = "RocketGunHVAR5BEAU25AP 1";
        as14[13] = "RocketGunHVAR5BEAU25AP 1";
        as14[14] = "RocketGunHVAR5BEAU25AP 1";
        as14[15] = "RocketGunHVAR5BEAU25AP 1";
        as14[16] = "RocketGunHVAR5BEAU25AP 1";
        as14[17] = "RocketGunHVAR5BEAU25AP 1";
        as14[18] = "RocketGunHVAR5BEAU25AP 1";
        as14[19] = "RocketGunHVAR5BEAU25AP 1";
        as14[20] = "RocketGunHVAR5BEAU25AP 1";
        as14[21] = "RocketGunHVAR5BEAU25AP 1";
        as14[22] = "RocketGunHVAR5BEAU25AP 1";
        as14[23] = "RocketGunHVAR5BEAU25AP 1";
        as14[24] = "RocketGunHVAR5BEAU25AP 1";
        as14[25] = "RocketGunHVAR5BEAU25AP 1";
        as14[26] = "RocketGunHVAR5BEAU25AP 1";
        as14[27] = "PylonSpitROCK 1";
        as14[28] = "PylonSpitROCK 1";
        as14[29] = "PylonSpitROCK 1";
        as14[30] = "PylonSpitROCK 1";
        as14[31] = "PylonSpitROCK 1";
        as14[32] = "PylonSpitROCK 1";
        as14[33] = "PylonSpitROCK 1";
        as14[34] = "PylonSpitROCK 1";
        Aircraft.weaponsRegister(class1, "16xRP3_25_droptank", as14);
        Aircraft.weaponsRegister(class1, "none", new String[47]);
    }
}
