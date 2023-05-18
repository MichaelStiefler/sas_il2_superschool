
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;


public class KC_10A_boom extends KC_10
    implements TypeDockable, TypeTankerBoom
{

    public KC_10A_boom()
    {
        bBoomExtended = true;
        bInRefueling = false;
        maxSendRefuel = 64.052F;      // max send rate = 1270gal per 1minute
          // 1270gal == 4800liter == 3842kg JP-5 ---> 1 sec cycle = 64.052 kg
        drones = new Actor[1];
        waitRefuelTimer = 0L;
        bEmpty = false;
        bFirstTime = true;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(thisWeaponsName.startsWith("none"))
            bEmpty = true;
    }

    public boolean isBoomExtended()
    {
        return bBoomExtended;
    }

    public void missionStarting()
    {
        super.missionStarting();
        bFirstTime = true;
    }

    public void update(float f)
    {
        boomRefuel();

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
            if(aircraft.FM.AS.isMaster() && aircraft.FM.getSpeedKMH() > 100F && FM.getSpeedKMH() > 100F && bBoomExtended)
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
                    Loc locReceptacle = new Loc();
                    HookNamed hookreceptacle = new HookNamed((ActorMesh)actor, "_Receptacle");
                    hookreceptacle.computePos(actor, locDrone, locReceptacle);
                    if(locDockport.getPoint().distance(locReceptacle.getPoint()) >= 20.0D)
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
            boomRefuel();
        if(i >= 0 && i < 1 && bBoomExtended)
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
            Loc locReceptacle = new Loc();
            HookNamed hookreceptacle = new HookNamed((ActorMesh)actor, "_Receptacle");
            hookreceptacle.computePos(actor, locDrone, locReceptacle);
            Loc loctemp = new Loc();
            Loc loctemp2 = new Loc();
            loctemp = locDrone;
            loctemp.sub(locReceptacle);
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

    private void boomRefuel()
    {
        float ias = Pitot.Indicator((float) (((Tuple3d) ((FlightModelMain)FM).Loc).z), FM.getSpeed()) * 3.6F;

        if(bEmpty || (FM.getAltitude() < 950F && FM.getAltitude() != 0.0F && !bFirstTime) || FM.CT.getGear() > 0.0F || (ias < 300F && ias != 0.0F && !bFirstTime))
        {
            hierMesh().chunkSetAngles("Boom_Control", 0.0F, 0.0F, 0.0F);
        }
        else
        {
            hierMesh().chunkSetAngles("Boom_Control", 0.0F, 26.0F, 0.0F);
        }

        if(bEmpty || (FM.getAltitude() < 1000F && FM.getAltitude() != 0.0F && !bFirstTime) || FM.CT.getGear() > 0.0F
           || ias > 760F || (ias < 325F && ias != 0.0F && !bFirstTime) || FM.M.fuel < FM.M.maxFuel * 0.050F)
        {
//            if(Time.current() > waitRefuelTimer)
//            {
                hierMesh().chunkSetAngles("RefuelBoom1", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RefuelBoom_WingC", 0.0F, 0.0F, 0.0F);
                resetYPRmodifier();
                hierMesh().chunkSetLocate("RefuelBoom2", Aircraft.xyz, Aircraft.ypr);
                bBoomExtended = false;
                waitRefuelTimer = Time.current() + 8000L;
                typeDockableAttemptDetach();
//            }
        } else
        {
//            if(Time.current() > waitRefuelTimer)
//            {
                hierMesh().chunkSetAngles("RefuelBoom1", 0.0F, 40.0F, 0.0F);
                hierMesh().chunkSetAngles("RefuelBoom_WingC", 0.0F, -40.0F, 0.0F);
                resetYPRmodifier();
                Aircraft.xyz[0] = -6.8693F;
                hierMesh().chunkSetLocate("RefuelBoom2", Aircraft.xyz, Aircraft.ypr);
                bBoomExtended = true;
                waitRefuelTimer = Time.current() + 8000L;
//            }
        }

        if(bFirstTime && !(FM.getAltitude() == 0.0F && ias == 0.0F))
            bFirstTime = false;
    }

    public final float requestRefuel(Aircraft aircraft, float req, float f)
    {
        if(bBoomExtended && FM.AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
                if(Actor.isValid(drones[i]) && drones[i] == (Actor)aircraft)
                {
                  // fake codes for KC-10 fake FM all the weight is 1/4 than historical.
                    if(req > maxSendRefuel / 4F)
                        req = maxSendRefuel / 4F;
                    if(FM.M.requestFuel(req * f / 4F))
                        return req * f;
                }
        }
        return 0.0F;
    }

    private int dockport_;
    private boolean bFirstTime;
    private boolean bBoomExtended;
    private boolean bInRefueling;
    private Actor drones[];
    private float maxSendRefuel;
    private long waitRefuelTimer;
    private boolean bEmpty;


    static
    {
        Class class1 = KC_10A_boom.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "meshName", "3do/plane/KC-10(Multi1)/hier_boom.him");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitKC_10.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_BombSpawn01"
        });
        String s = "";
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            char c = 1;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[c];
            s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) {
        }
    }
}
