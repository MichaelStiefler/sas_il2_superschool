
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;


public class KC_130H extends C_130X
    implements TypeDockable, TypeTankerDrogue
{

    public KC_130H()
    {
        bDrogueExtended = true;
        maxSendRefuel = 10.093F;      // max send rate = 200gal per 1minute 
          // 200gal == 757liter == 605kg JP-5 ---> 1 sec cycle = 10.093 kg
        drones = new Actor[2];
        waitRefuelTimer = 0L;
        bEmpty = false;
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        return "KC_";
    }

    public boolean isDrogueExtended()
    {
        return bDrogueExtended;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();

        if(thisWeaponsName.startsWith("none"))
            bEmpty = true;
    }

    public void missionStarting()
    {
        super.missionStarting();
        bWingBroken[0] = bWingBroken[1] = false;
        bFirstTime = true;
    }

    public void update(float f)
    {
        drogueRefuel();

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
            if(aircraft.FM.AS.isMaster() && aircraft.FM.getSpeedKMH() > 100F && FM.getSpeedKMH() > 100F && isDrogueExtended())
            {
                for(int i = 0; i < drones.length; i++)
                {
                    if(Actor.isValid(drones[i]))
                        continue;
                    Loc locQueen = new Loc();
                    Loc locDrone = new Loc();
                    this.pos.getAbs(locQueen);
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
                    {
                        typeDockableRequestAttach(actor, i, true);
                        return;
                    }
                    else
                    {
                        FM.AS.netToMaster(32, i, 0, actor);
                        return;
                    }
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
                if(aircraft.FM.AS.isMaster())
                    if(FM.AS.isMaster())
                        typeDockableRequestDetach(actor, i, true);
                    else
                        FM.AS.netToMaster(33, i, 1, actor);
            }

    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag)
    {
        if(bFirstTime)
            drogueRefuel();
        if(i >= 0 && i <= 1 && bDrogueExtended)
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
            this.pos.getAbs(locQueen);
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

    public void typeDockableDoAttachToQueen(Actor actor, int i)
    {
    }

    public void typeDockableDoDetachFromQueen(int i)
    {
    }

    public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        for(int i = 0; i < drones.length; i++)
            if(Actor.isValid(drones[i]))
            {
                netmsgguaranted.writeByte(1);
                ActorNet actornet = drones[i].net;
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

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19: // '\023'
            killPilot(this, 4);
            break;
        case 33: // '!'
        case 34: // '"'
            if(FM.AS.isMaster())
            {
                typeDockableRequestDetach(drones[0], 0, true);
                bWingBroken[0] = true;
            }
            break;
        case 36: // '$'
        case 37: // '%'
            if(FM.AS.isMaster())
            {
                typeDockableRequestDetach(drones[1], 0, true);
                bWingBroken[1] = true;
            }
            break;

        }
        return super.cutFM(i, j, actor);
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 3; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

        for(int i = 0; i < 2; i++)
        {
            if(bWingBroken[i])
            {
                hierMesh().chunkVisible("Rfp_Drogue" + (i + 1) + "_Fold", false);
                hierMesh().chunkVisible("Rfp_iGreen" + (i + 1), false);
                hierMesh().chunkVisible("Rfp_iRed" + (i + 1), false);
                hierMesh().chunkVisible("Rfp_iYellow" + (i + 1), false);
                hierMesh().chunkVisible("Rfp_FuelLine" + (i + 1), false);
                hierMesh().chunkVisible("Rfp_Drogue" + (i + 1), false);
            }
        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    private void drogueRefuel()
    {
        float ias = Pitot.Indicator((float) (((Tuple3d) ((FlightModelMain)FM).Loc).z), FM.getSpeed()) * 3.6F;

        if(bEmpty || (FM.getAltitude() < 1000F && FM.getAltitude() != 0.0F && !bFirstTime) || FM.CT.getGear() > 0.0F
           || ias > 580F || (ias < 250F && ias != 0.0F && !bFirstTime) || FM.M.fuel < FM.M.maxFuel * 0.120F)
        {
//            if(Time.current() > waitRefuelTimer)
//            {
                hierMesh().chunkVisible("Rfp_Drogue1", false);
                hierMesh().chunkVisible("Rfp_Drogue2", false);
                hierMesh().chunkVisible("Rfp_FuelLine1", false);
                hierMesh().chunkVisible("Rfp_FuelLine2", false);
                if(!bWingBroken[0])
                    hierMesh().chunkVisible("Rfp_Drogue1_Fold", true);
                if(!bWingBroken[1])
                    hierMesh().chunkVisible("Rfp_Drogue2_Fold", true);
                if(bDrogueExtended == true)
                {
                    hierMesh().materialReplace("CYellowOff1", "CYellowOff1");
                    hierMesh().materialReplace("CYellowOff2", "CYellowOff2");
                }

                bDrogueExtended = false;
                waitRefuelTimer = Time.current() + 8000L;
                typeDockableAttemptDetach();

                boolean bTempFlag = false;
                for(int i=0; i<2; i++)
                {
                    if(bInRefueling[i] == true)
                    {
                        bInRefueling[i] = false;
                        bTempFlag = true;
                    }
                }
                if(bTempFlag)
                    hierMesh().materialReplace("CGreenOff1", "CGreenOff1");
                    hierMesh().materialReplace("CGreenOff2", "CGreenOff2");
//            }
        } else
        {
//            if(Time.current() > waitRefuelTimer)
//            {
                if(!bWingBroken[0])
                {
                    hierMesh().chunkVisible("Rfp_Drogue1", true);
                    hierMesh().chunkVisible("Rfp_FuelLine1", true);
                }
                if(!bWingBroken[1])
                {
                    hierMesh().chunkVisible("Rfp_Drogue2", true);
                    hierMesh().chunkVisible("Rfp_FuelLine2", true);
                }
                hierMesh().chunkVisible("Rfp_Drogue1_Fold", false);
                hierMesh().chunkVisible("Rfp_Drogue2_Fold", false);
                if(bDrogueExtended == false && bInRefueling[0] == false)
                {
                    hierMesh().materialReplace("CYellowOff1", "CYellowOn1");
                    hierMesh().materialReplace("CYellowOff2", "CYellowOn2");
                }
                bDrogueExtended = true;
                waitRefuelTimer = Time.current() + 8000L;
//            }
        }

        if(bDrogueExtended && FM.AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
            {
                if(Actor.isValid(drones[i]))
                {
                    if(bInRefueling[i] == false)
                    {
                        hierMesh().materialReplace("CGreenOff" + String.valueOf(i+1), "CGreenOn" + String.valueOf(i+1));
                        hierMesh().materialReplace("CYellowOff" + String.valueOf(i+1), "CYellowOff" + String.valueOf(i+1));
                        bInRefueling[i] = true;
                    }
                }
                else
                {
                    if(bInRefueling[i] == true)
                    {
                        hierMesh().materialReplace("CGreenOff" + String.valueOf(i+1), "CGreenOff" + String.valueOf(i+1));
                        hierMesh().materialReplace("CYellowOff" + String.valueOf(i+1), "CYellowOn" + String.valueOf(i+1));
                        bInRefueling[i] = false;
                    }
                }
            }
        }

        if(bFirstTime && !(FM.getAltitude() == 0.0F && ias == 0.0F))
            bFirstTime = false;
    }

    public final float requestRefuel(Aircraft aircraft, float req, float f)
    {
        if(bDrogueExtended && FM.AS.isMaster())
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


    public static boolean bChangedPit = false;
    private boolean bFirstTime = true;
    private boolean bDrogueExtended;
    private boolean bInRefueling[] = { false, false };
    private Actor drones[];
    private float maxSendRefuel;
    private long waitRefuelTimer;
    private boolean bEmpty;
    private boolean bWingBroken[] = { false, false };

    static 
    {
        Class class1 = com.maddox.il2.objects.air.KC_130H.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "KC-130H");
        Property.set(class1, "meshName", "3DO/Plane/C-130/hierKC.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 2080F);
        Property.set(class1, "FlightModel", "FlightModels/LockheedC-130.fmd:C130_FM");
//        Property.set(class1, "cockpitClass", new Class[] {     // AI only
//            com.maddox.il2.objects.air.CockpitC_130H.class
//        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            14, 3, 3, 3, 2, 2, 2, 2, 2, 2, 
            3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN11", "_MGUN11", "_BombSpawn01", "_BombSpawn02", "_Rock01", "_Rock02", "_Rock03", "_Rock04", "_Rock05", "_Rock06", 
            "_InternalBomb01"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 11;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}