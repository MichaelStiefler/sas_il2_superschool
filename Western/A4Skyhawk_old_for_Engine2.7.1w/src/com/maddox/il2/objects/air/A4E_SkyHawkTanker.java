
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;
import java.io.IOException;


public class A4E_SkyHawkTanker extends A4TX
    implements TypeCountermeasure, TypeTankerDrogue, TypeDockable
{

    public A4E_SkyHawkTanker()
    {
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        bDrogueExtended = true;
        bInRefueling = false;
        maxSendRefuel = 10.093F;      // max send rate = 200gal per 1minute 
          // 200gal == 757liter == 605kg JP-5 ---> 1 sec cycle = 10.093 kg
        drones = new Actor[1];
        waitRefuelTimer = 0L;
        ratdeg = 0F;
        bEmpty = false;
    }

    public boolean isDrogueExtended()
    {
        return bDrogueExtended;
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

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();

        ((FlightModelMain) FM).M.maxFuel += 880F;  // additional fuel 300gal in D-704 Refuel Store
        ((FlightModelMain) FM).M.fuel += 880F;
        ((FlightModelMain) FM).M.massEmpty += 370F;   // empty weight of D-704 Refuel Store
        ((FlightModelMain) FM).M.mass += 370F;
        ((FlightModelMain) FM).M.maxWeight += 1250F;

        bEmpty = false;
    }

    public void update(float f)
    {
        drogueRefuel(f);

        if(FM.getSpeedKMH() > 185F)
            RATrot();

        super.update(f);
    }

    public boolean typeDockableIsDocked()
    {
        return true;
    }

    public void typeDockableAttemptAttach()
    {
    }

    public void typeDockableAttemptDetach()
    {
        if(FM.AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
                if(Actor.isValid(drones[i]))
                    typeDockableRequestDetach(drones[i], i, true);

        }
    }

    public void typeDockableRequestAttach(Actor actor)
    {
        if(actor instanceof Aircraft)
        {
            Aircraft aircraft = (Aircraft)actor;
            if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AS.isMaster() && ((SndAircraft) (aircraft)).FM.getSpeedKMH() > 10F && super.FM.getSpeedKMH() > 10F && isDrogueExtended())
            {
                for(int i = 0; i < drones.length; i++)
                {
                    if(Actor.isValid(drones[i]))
                        continue;
                    Loc locQueen = new Loc();
                    Loc locDrone = new Loc();
                    super.pos.getAbs(locQueen);
                    actor.pos.getAbs(locDrone);
                    Loc locDockport = new Loc();
                    HookNamed hookdockport = new HookNamed(this, "_Dockport" + i);
                    hookdockport.computePos(this, locQueen, locDockport);
                    Loc locProbe = new Loc();
                    HookNamed hookprobe = new HookNamed((ActorMesh)actor, "_Probe");
                    hookprobe.computePos(actor, locDrone, locProbe);
                    if(locDockport.getPoint().distance(locProbe.getPoint()) >= 8.0D)
                        continue;
                    if(FM.AS.isMaster())
                        typeDockableRequestAttach(actor, i, true);
                    else
                        FM.AS.netToMaster(32, i, 0, actor);
                    break;
                }

            }
        }
    }

    public void typeDockableRequestDetach(Actor actor)
    {
        for(int i = 0; i < drones.length; i++)
            if(actor == drones[i])
            {
                Aircraft aircraft = (Aircraft)actor;
                if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AS.isMaster())
                    if(FM.AS.isMaster())
                        typeDockableRequestDetach(actor, i, true);
                    else
                        FM.AS.netToMaster(33, i, 1, actor);
            }

    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag)
    {
        if(i >= 0 && i <= 1)
            if(flag)
            {
                if(FM.AS.isMaster())
                {
                    FM.AS.netToMirrors(34, i, 1, actor);
                    typeDockableDoAttachToDrone(actor, i);
                } else
                {
                    FM.AS.netToMaster(34, i, 1, actor);
                }
            } else
            if(FM.AS.isMaster())
            {
                if(!Actor.isValid(drones[i]))
                {
                    FM.AS.netToMirrors(34, i, 1, actor);
                    typeDockableDoAttachToDrone(actor, i);
                }
            } else
            {
                FM.AS.netToMaster(34, i, 0, actor);
            }
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag)
    {
        if(flag)
            if(FM.AS.isMaster())
            {
                FM.AS.netToMirrors(35, i, 1, actor);
                typeDockableDoDetachFromDrone(i);
            } else
            {
                FM.AS.netToMaster(35, i, 1, actor);
            }
    }

    public void typeDockableDoAttachToDrone(Actor actor, int i)
    {
        if(!Actor.isValid(drones[i]))
        {
            Loc locQueen = new Loc();
            Loc locDrone = new Loc();
            super.pos.getAbs(locQueen);
            actor.pos.getAbs(locDrone);
            Loc locDockport = new Loc();
            HookNamed hookdockport = new HookNamed(this, "_Dockport" + i);
            hookdockport.computePos(this, locQueen, locDockport);
            Loc locProbe = new Loc();
            HookNamed hookprobe = new HookNamed((ActorMesh)actor, "_Probe");
            hookprobe.computePos(actor, locDrone, locProbe);
            Loc loctemp = new Loc();
            Loc loctemp2 = new Loc();
            loctemp = locDrone;
            loctemp.sub(locProbe);
            loctemp2 = locDockport;
            loctemp2.sub(locQueen);
            loctemp.add(loctemp2);
            loctemp.add(locQueen);
            actor.pos.setAbs(loctemp);
            actor.pos.setBase(this, null, true);
            actor.pos.resetAsBase();
            drones[i] = actor;
            ((TypeDockable)drones[i]).typeDockableDoAttachToQueen(this, i);
        }
    }

    public void typeDockableDoDetachFromDrone(int i)
    {
        if(Actor.isValid(drones[i]))
        {
            drones[i].pos.setBase(null, null, true);
            ((TypeDockable)drones[i]).typeDockableDoDetachFromQueen(i);
            drones[i] = null;
        }
    }

    public void typeDockableDoAttachToQueen(Actor actor1, int j)
    {
    }

    public void typeDockableDoDetachFromQueen(int j)
    {
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        for(int i = 0; i < drones.length; i++)
            if(Actor.isValid(drones[i]))
            {
                netmsgguaranted.writeByte(1);
                com.maddox.il2.engine.ActorNet actornet = drones[i].net;
                if(actornet.countNoMirrors() == 0)
                    netmsgguaranted.writeNetObj(actornet);
                else
                    netmsgguaranted.writeNetObj(null);
            } else
            {
                netmsgguaranted.writeByte(0);
            }

    }

    public void typeDockableReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        for(int i = 0; i < drones.length; i++)
            if(netmsginput.readByte() == 1)
            {
                NetObj netobj = netmsginput.readNetObj();
                if(netobj != null)
                    typeDockableDoAttachToDrone((Actor)netobj.superObj(), i);
            }

    }

    private void RATrot()
    {
        if(FM.getSpeedKMH() < 250F)
            ratdeg -= 10F;
        else if(FM.getSpeedKMH() < 400F)
            ratdeg -= 20F;
        else if(FM.getSpeedKMH() < 550F)
            ratdeg -= 25F;
        else
            ratdeg -= 31F;
        if(ratdeg < 720F) ratdeg += 1440F;

        hierMesh().chunkSetAngles("D704_rat", 0.0F, 0.0F, ratdeg);

        if(FM.getSpeedKMH() > 300F)
        {
            hierMesh().chunkVisible("D704_rat_rot", true);
            hierMesh().chunkVisible("D704_rat", false);
        }
        else
        {
            hierMesh().chunkVisible("D704_rat_rot", false);
            hierMesh().chunkVisible("D704_rat", true);
        }
    }

    private void drogueRefuel(float f)
    {
        float ias = Pitot.Indicator((float) (((Tuple3d) ((FlightModelMain)FM).Loc).z), FM.getSpeed()) * 3.6F;

        if(bEmpty || FM.getAltitude() < 1000F || FM.CT.getGear() > 0.0F || FM.CT.getArrestor() > 0.0F
           || ias > 580F || ias < 325F || (double)(FM.M.fuel) < (double)(FM.M.maxFuel) * 0.20000000000000001D)
        {
//            if(Time.current() > waitRefuelTimer)
//            {
                hierMesh().chunkVisible("D704_FuelLine1", false);
                hierMesh().chunkVisible("D704_Drogue1", false);
                hierMesh().chunkVisible("D704_Drogue1_Fold", true);
                if(bDrogueExtended == true)
                {
                    hierMesh().materialReplace("CYellowOff", "CYellowOff");
                }
                bDrogueExtended = false;
                waitRefuelTimer = Time.current() + 8000L;
                typeDockableAttemptDetach();
                if(bInRefueling == true)
                {
                    hierMesh().materialReplace("CGreenOff", "CGreenOff");
                    bInRefueling = false;
                }
//            }
        } else
        {
//            if(Time.current() > waitRefuelTimer)
//            {
                hierMesh().chunkVisible("D704_FuelLine1", true);
                hierMesh().chunkVisible("D704_Drogue1", true);
                hierMesh().chunkVisible("D704_Drogue1_Fold", false);
                if(bDrogueExtended == false && bInRefueling == false)
                {
                    hierMesh().materialReplace("CYellowOff", "CYellowOn");
                }
                bDrogueExtended = true;
                waitRefuelTimer = Time.current() + 8000L;
//            }
        }

        if(bDrogueExtended && ((FlightModelMain) FM).AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
                if(Actor.isValid(drones[i]))
                {
                    if(bInRefueling == false)
                    {
                        hierMesh().materialReplace("CGreenOff", "CGreenOn");
                        hierMesh().materialReplace("CYellowOff", "CYellowOff");
                        bInRefueling = true;
                    }
                }
                else
                {
                    if(bInRefueling == true)
                    {
                        hierMesh().materialReplace("CGreenOff", "CGreenOff");
                        hierMesh().materialReplace("CYellowOff", "CYellowOn");
                        bInRefueling = false;
                    }
                }
        }
    }

	public final float requestRefuel(Aircraft aircraft, float req, float f)
    {
        if(bDrogueExtended && ((FlightModelMain) FM).AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
                if(Actor.isValid(drones[i]) && drones[i] == (Actor)aircraft)
                {
                    if(req > maxSendRefuel)
                        req = maxSendRefuel;
                    if(FM.M.requestFuel(req * f))
                        return req * f;
                }
        }
        return 0.0F;
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        if(FM.AS.isMaster())
            switch(i)
            {
            case 33: // '!'
            case 34: // '"'
            case 35: // '#'
                typeDockableRequestDetach(drones[0], 0, true);
                break;
            }
        return super.cutFM(i, j, actor);
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

    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private boolean bDrogueExtended;
    private boolean bInRefueling;
    private Actor drones[];
    private float maxSendRefuel;
    private long waitRefuelTimer;
    private float ratdeg;
    private boolean bEmpty;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-4 Tanker");
        Property.set(class1, "meshName", "3DO/Plane/A4ESkyHawkTanker(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/A4E.fmd:Skyhawks_FM");
        Property.set(class1, "cockpitClass", new Class[] {
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 9, 9, 0, 0, 9, 9, 
            3, 3, 3, 3, 2, 2, 2, 2, 9, 2, 
            2, 2, 2, 9, 2, 2, 2, 2, 9, 9, 
            9, 3, 3, 3, 3, 3, 3, 9, 9, 9, 
            9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 2, 2, 9, 9, 9, 9, 2, 
            9, 9, 2, 2, 9, 9, 9, 9, 9, 9, 
            2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", 
            "_ExternalBomb07", "_ExternalBomb08", "_Bomb09", "_ExternalBomb10", "_Rock01", "_Rock02", "_MGUN01", "_MGUN02", "_ExternalRock03", "_ExternalRock04", 
            "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", 
            "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalDev01", "_ExternalDev02", 
            "_Dev03", "_ExternalBomb11", "_Bomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_Dev04", "_Dev05", "_Dev06", 
            "_Dev07", "_Bomb17", "_Bomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_Bomb23", "_ExternalBomb24", "_Bomb25", 
            "_Bomb26", "_Bomb27", "_Bomb28", "_ExternalRock23", "_ExternalRock24", "_Dev08", "_Dev09", "_Dev10", "_Dev11", "_ExternalRock25", 
            "_Dev12", "_Dev13", "_ExternalRock26", "_ExternalRock27", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalDev19", 
            "_Rock26", "_Rock27", "_Rock28", "_Rock29"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 84;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xDpt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[39] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}