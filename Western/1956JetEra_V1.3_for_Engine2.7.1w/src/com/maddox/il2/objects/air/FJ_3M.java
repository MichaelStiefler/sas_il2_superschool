
package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            F_86F, Aircraft, TypeTankerDrogue, TypeDockable, 
//            PaintSchemeFMPar06, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, 
//            TypeGSuit, NetAircraft

public class FJ_3M extends F_86F
    implements TypeDockable, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit
{

    public FJ_3M()
    {
        guidedMissileUtils = new GuidedMissileUtils(this);
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        lastCommonThreatActive = 0L;
        intervalCommonThreat = 1000L;
        lastRadarLockThreatActive = 0L;
        intervalRadarLockThreat = 1000L;
        lastMissileLaunchThreatActive = 0L;
        intervalMissileLaunchThreat = 1000L;
        arrestor = 0.0F;
    }

    public long getChaffDeployed()
    {
        if(hasChaff)
            return lastChaffDeployed;
        else
            return 0L;
    }

    public long getFlareDeployed()
    {
        if(hasFlare)
            return lastFlareDeployed;
        else
            return 0L;
    }

    public void setCommonThreatActive()
    {
        long l = Time.current();
        if(l - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = l;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long l = Time.current();
        if(l - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = l;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long l = Time.current();
        if(l - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = l;
            doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat()
    {
    }

    private void doDealRadarLockThreat()
    {
    }

    private void doDealMissileLaunchThreat()
    {
    }

    public void getGFactors(TypeGSuit.GFactors gfactors)
    {
        gfactors.setGFactors(1.5F, 1.5F, 1.0F, 2.0F, 2.0F, 2.0F);
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f)
    {
        if(bNeedSetup)
            checkAsDrone();
        guidedMissileUtils.update();
        if(super.FM instanceof Maneuver)
            receivingRefuel(f);
        if(FM.CT.getArrestor() > 0.2F)
            calculateArrestor();
        super.update(f);
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        super.msgCollisionRequest(actor, aflag);
        if(queen_last != null && queen_last == actor && (queen_time == 0L || Time.current() < queen_time + 5000L))
            aflag[0] = false;
        else
            aflag[0] = true;
    }

    public void missionStarting()
    {
        checkAsDrone();
    }

    private void checkAsDrone()
    {
        if(target_ == null)
        {
            if(FM.AP.way.curr().getTarget() == null)
                FM.AP.way.next();
            target_ = FM.AP.way.curr().getTarget();
            if(Actor.isValid(target_) && (target_ instanceof Wing))
            {
                Wing wing = (Wing)target_;
                int i = aircIndex();
                if(Actor.isValid(wing.airc[i / 2]))
                    target_ = wing.airc[i / 2];
                else
                    target_ = null;
            }
        }
        if(Actor.isValid(target_) && (target_ instanceof TypeTankerDrogue))
        {
            queen_last = target_;
            queen_time = Time.current();
            if(isNetMaster())
                ((TypeDockable)target_).typeDockableRequestAttach(this, aircIndex() % 2, true);
        }
        bNeedSetup = false;
        target_ = null;
    }

    public int typeDockableGetDockport()
    {
        if(typeDockableIsDocked())
            return dockport_;
        else
            return -1;
    }

    public Actor typeDockableGetQueen()
    {
        return queen_;
    }

    public boolean typeDockableIsDocked()
    {
        return Actor.isValid(queen_);
    }

    public void typeDockableAttemptAttach()
    {
        if(FM.AS.isMaster() && !typeDockableIsDocked())
        {
            Aircraft aircraft = War.getNearestFriend(this);
            if(aircraft instanceof TypeTankerDrogue)
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach()
    {
        if(FM.AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
            ((TypeDockable)queen_).typeDockableRequestDetach(this);
    }

    public void typeDockableRequestAttach(Actor actor)
    {
    }

    public void typeDockableRequestDetach(Actor actor)
    {
    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag)
    {
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag)
    {
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i)
    {
    }

    public void typeDockableDoDetachFromDrone(int i)
    {
    }

    public void typeDockableDoAttachToQueen(Actor actor, int i)
    {
        queen_ = actor;
        dockport_ = i;
        queen_last = queen_;
        queen_time = 0L;
        FM.EI.setEngineRunning();
        FM.CT.setGearAirborne();
        moveGear(0.0F);
        FlightModel flightmodel = ((SndAircraft) ((Aircraft)queen_)).FM;
        if(aircIndex() == 0 && (super.FM instanceof Maneuver) && (flightmodel instanceof Maneuver))
        {
            Maneuver maneuver = (Maneuver)flightmodel;
            Maneuver maneuver1 = (Maneuver)super.FM;
            if(maneuver.Group != null && maneuver1.Group != null && maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1)
            {
                AirGroup airgroup = new AirGroup(maneuver1.Group);
                maneuver1.Group.delAircraft(this);
                airgroup.addAircraft(this);
                airgroup.attachGroup(maneuver.Group);
                airgroup.rejoinGroup = null;
                airgroup.leaderGroup = null;
                airgroup.clientGroup = maneuver.Group;
            }
        }
    }

    public void typeDockableDoDetachFromQueen(int i)
    {
        if(dockport_ == i)
        {
            queen_last = queen_;
            queen_time = Time.current();
            queen_ = null;
            dockport_ = 0;
        }
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        if(typeDockableIsDocked())
        {
            netmsgguaranted.writeByte(1);
            ActorNet actornet = null;
            if(Actor.isValid(queen_))
            {
                actornet = queen_.net;
                if(actornet.countNoMirrors() > 0)
                    actornet = null;
            }
            netmsgguaranted.writeByte(dockport_);
            netmsgguaranted.writeNetObj(actornet);
        } else
        {
            netmsgguaranted.writeByte(0);
        }
    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        if(netmsginput.readByte() == 1)
        {
            dockport_ = netmsginput.readByte();
            NetObj netobj = netmsginput.readNetObj();
            if(netobj != null)
            {
                Actor actor = (Actor)netobj.superObj();
                ((TypeDockable)actor).typeDockableDoAttachToDrone(this, dockport_);
            }
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !flag || !(super.FM instanceof Pilot))
            return;
        if(flag && FM.AP.way.curr().Action == 3 && typeDockableIsDocked() && Math.abs(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).Or.getKren()) < 3F)
            if(super.FM.isPlayers())
            {
                if((super.FM instanceof RealFlightModel) && !((RealFlightModel)super.FM).isRealMode())
                {
                    typeDockableAttemptDetach();
                    ((Maneuver)super.FM).set_maneuver(22);
                    ((Maneuver)super.FM).setCheckStrike(false);
                    FM.Vwld.z -= 5D;
                    dtime = Time.current();
                }
            } else
            {
                typeDockableAttemptDetach();
                ((Maneuver)super.FM).set_maneuver(22);
                ((Maneuver)super.FM).setCheckStrike(false);
                FM.Vwld.z -= 5D;
                dtime = Time.current();
            }
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLFold", 0.0F * f, 0.0F * f, -22F * f);
        hiermesh.chunkSetAngles("WingRFold", 0.0F * f, 0.0F * f, -22F * f);
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F * f, 90F * f, -22F * f);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F * f, -90F * f, -22F * f);
    }

    public void moveWingFold(float f)
    {
        if(f < 0.001F)
        {
            setGunPodsOn(true);
            hideWingWeapons(false);
        } else
        {
            setGunPodsOn(false);
            FM.CT.WeaponControl[0] = false;
            hideWingWeapons(true);
        }
        moveWingFold(hierMesh(), f);
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 45F * f, 0.0F);
        resetYPRmodifier();
        Aircraft.xyz[2] = 0.1385F * f;
        arrestor = f;
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19: // '\023'
            FM.CT.bHasArrestorControl = false;
            break;
        }
        return super.cutFM(i, j, actor);
    }

    public void checkHydraulicStatus()
    {
        if(FM.EI.engines[0].getStage() < 6 && FM.Gears.nOfGearsOnGr > 0)
        {
            super.hasHydraulicPressure = false;
            FM.CT.bHasAileronControl = false;
            FM.CT.bHasElevatorControl = false;
            FM.CT.AirBrakeControl = 1.0F;
            FM.CT.bHasArrestorControl = false;
        } else
        if(!super.hasHydraulicPressure)
        {
            super.hasHydraulicPressure = true;
            FM.CT.bHasAileronControl = true;
            FM.CT.bHasElevatorControl = true;
            FM.CT.bHasAirBrakeControl = true;
            FM.CT.bHasArrestorControl = true;
        }
    }

    private void receivingRefuel(float f)
    {
        int i = aircIndex();
        if(typeDockableIsDocked())
        {
            if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
            {
                ((Maneuver)super.FM).unblock();
                ((Maneuver)super.FM).set_maneuver(48);
                for(int j = 0; j < i; j++)
                    ((Maneuver)super.FM).push(48);

                if(FM.AP.way.curr().Action != 3)
                    ((FlightModelMain) ((Maneuver)super.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).AP.way.Cur());
                ((Pilot)super.FM).setDumbTime(3000L);
            }
            FuelTank fuelTanks[];
            fuelTanks = FM.CT.getFuelTanks();
            if(FM.M.fuel < FM.M.maxFuel - 8F)
            {
                        // "7.57F" means fuel receiving rate 150gal per 1minute
                        // 150gal == 567.75liter == 453.75kg JP-5 ---> 1 sec cycle = 7.57 kg
                float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, 7.57F, f);
                FM.M.fuel += getFuel;
            }
            else if(fuelTanks.length > 0 && fuelTanks[0] != null && !FM.M.bFuelTanksDropped)
            {
                float freeTankSum = 0F;
                for(int num = 0; num < fuelTanks.length; num++)
                    freeTankSum += fuelTanks[num].checkFreeTankSpace();
                if(freeTankSum < 8F)
                {
                    typeDockableAttemptDetach();
                    return;
                }
                float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, 7.57F, f);
                for(int num = 0; num < fuelTanks.length; num++)
                    fuelTanks[num].doRefuel(getFuel * (fuelTanks[num].checkFreeTankSpace() / freeTankSum));
            }
            else
            {
                typeDockableAttemptDetach();
                return;
            }
        } else
        if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
        {
            if(FM.CT.GearControl == 0.0F && FM.EI.engines[0].getStage() == 0)
                FM.EI.setEngineRunning();
            if(dtime > 0L && ((Maneuver)super.FM).Group != null)
            {
                ((Maneuver)super.FM).Group.leaderGroup = null;
                ((Maneuver)super.FM).set_maneuver(22);
                ((Pilot)super.FM).setDumbTime(3000L);
                if(Time.current() > dtime + 3000L)
                {
                    dtime = -1L;
                    ((Maneuver)super.FM).clear_stack();
                    ((Maneuver)super.FM).set_maneuver(0);
                    ((Pilot)super.FM).setDumbTime(0L);
                }
            } else
            if(FM.AP.way.curr().Action == 0)
            {
                Maneuver maneuver = (Maneuver)super.FM;
                if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                    maneuver.Group.setGroupTask(2);
            }
        }
    }

    private void calculateArrestor()
    {
        if(FM.Gears.arrestorVAngle != 0.0F)
        {
            float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
            arrestor = 0.8F * arrestor + 0.2F * f1;
            moveArrestorHook(arrestor);
        } else
        {
            float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
            if(f2 < 0.0F && super.FM.getSpeedKMH() > 60F)
                Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if(f2 > 0.0F && FM.CT.getArrestor() < 0.95F)
                f2 = 0.0F;
            if(f2 > 0.2F)
                f2 = 0.2F;
            if(f2 > 0.0F)
                arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
            else
                arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
            if(arrestor < 0.0F)
                arrestor = 0.0F;
            else
            if(arrestor > 1.0F)
                arrestor = 1.0F;
            moveArrestorHook(arrestor);
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

    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;
    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private long lastCommonThreatActive;
    private long intervalCommonThreat;
    private long lastRadarLockThreatActive;
    private long intervalRadarLockThreat;
    private long lastMissileLaunchThreatActive;
    private long intervalMissileLaunchThreat;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR = 1F;
    private static final float POS_G_TOLERANCE_FACTOR = 2F;
    private static final float POS_G_TIME_FACTOR = 2F;
    private static final float POS_G_RECOVERY_FACTOR = 2F;
    public boolean bToFire;
    private float arrestor;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.FJ_3M.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FJ-3M");
        Property.set(class1, "meshName", "3DO/Plane/FJ_3M(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1949.9F);
        Property.set(class1, "yearExpired", 1960.3F);
        Property.set(class1, "FlightModel", "FlightModels/FJ-3M.fmd:JETERA");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitF_86Flate.class
        });
        Property.set(class1, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 9, 9, 9, 9, 9, 3, 
            3, 9, 3, 3, 9, 2, 2, 9, 2, 2, 
            9, 9, 9, 9, 9, 3, 3, 9, 3, 3, 
            9, 9, 2, 2, 2, 2, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", 
            "_ExternalBomb01", "_ExternalDev06", "_ExternalBomb02", "_ExternalBomb02", "_ExternalDev07", "_ExternalRock01", "_ExternalRock01", "_ExternalDev08", "_ExternalRock02", "_ExternalRock02", 
            "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalBomb03", "_ExternalBomb03", "_ExternalDev14", "_ExternalBomb04", "_ExternalBomb04", 
            "_ExternalDev17", "_ExternalDev18", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 40;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x120dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x75nap";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x75nap+2x120Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x500+2x120Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x500+2x750";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "BombGun500lbs", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "BombGun500lbs", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun750lbs", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun750lbs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x500+2xM117";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "BombGun500lbs", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "BombGun500lbs", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4x500";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "BombGun500lbs", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "BombGun500lbs", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x750";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun750lbs", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun750lbs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x750+2x120Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun750lbs", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun750lbs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM117";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM117+2x120Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x1000";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x1000+2x120Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM114";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 132);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun1000lbs_M114", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun1000lbs_M114", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM114+2x120Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGun1000lbs_M114", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGun1000lbs_M114", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunnNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xLAU10";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xLAU10+2x120Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9B";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9B+2x120Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9D";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9D+2x120Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunColtMk12ki", 162);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonF86_Outboard", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galL", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_120galR", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9D", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}