
package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;


public class F9F8 extends F9F_Cougar
    implements TypeDockable
{

    public F9F8()
    {
    }

    public void update(float f)
    {
        if(bNeedSetup)
            checkAsDrone();
        if(super.FM instanceof Maneuver)
            receivingRefuel(f);
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
        super.missionStarting();
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
            if(FM.M.fuel < FM.M.maxFuel - 10F)
            {
                float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, 9.893F, f);
                FM.M.fuel += getFuel;
            }
            else if(fuelTanks.length > 0 && fuelTanks[0] != null && !FM.M.bFuelTanksDropped)
            {
                float freeTankSum = 0F;
                for(int num = 0; num < fuelTanks.length; num++)
                    freeTankSum += fuelTanks[num].checkFreeTankSpace();
                if(freeTankSum < 10F)
                {
                    typeDockableAttemptDetach();
                    return;
                }
                float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, 9.893F, f);
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

    static 
    {
        Class class1 = com.maddox.il2.objects.air.F9F8.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F9F-8");
        Property.set(class1, "meshName", "3DO/Plane/F9F8/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946.9F);
        Property.set(class1, "yearExpired", 1955.3F);
        Property.set(class1, "FlightModel", "FlightModels/F9F8.fmd:F9F");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitF9F_Cougar.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 
            9, 9, 3, 3, 3, 3, 3, 3, 9, 9, 
            2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 
            9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 
            3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", 
            "_ExternalDev07", "_ExternalDev08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev09", "_ExternalDev10", 
            "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalDev11", "_ExternalDev12", 
            "_ExternalDev13", "_ExternalDev14", "_ExternalRock09", "_ExternalRock09", "_ExternalRock10", "_ExternalRock10", "_ExternalRock11", "_ExternalRock11", "_ExternalRock12", "_ExternalRock12", 
            "_ExternalBomb07"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 79;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "Default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM57";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xM57";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM57 + 2xM64";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM57 + 2xDTs";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Cougar150gal", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "FuelTankGun_Cougar150gal", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM57 + 2xM117";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM57 + 2xMk83";
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM57 + 2xM65";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM57 + 2xZuni";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM64";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xM64";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM64 + 2xDTs";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Cougar150gal", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "FuelTankGun_Cougar150gal", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM64 + 2xM117";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM64 + 2xMk83";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM64 + 2xM65";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM64 + 2xZuni";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuni", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM57 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM57 + 2xAim-9 + 2xDTs";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Cougar150gal", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "FuelTankGun_Cougar150gal", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM57 + 2xM117 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM57 + 2xMK83 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM57 + 2xM65 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xM64 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM64 + 2xAim-9 + 2xDTs";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Cougar150gal", 1);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "FuelTankGun_Cougar150gal", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM64 + 2xM117 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM64 + 2xMK83 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM64 + 2xM65 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk81 + 2xMK82 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk81 + 2xM117 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk81 + 2xMK83 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk81 + 2xM65 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMK82 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82 + 2xM117 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82 + 2xMK83 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82 + 2xM65 + 2xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "PylonAero15A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM57 + 4xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun250lbs", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk81 + 4xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM64 + 4xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun500lbs", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82 + 4xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM117 + 4xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun750lbsM117", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83 + 4xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xM65 + 4xAim-9";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIk", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonAero65A", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(3, "BombGun1000lbs", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}