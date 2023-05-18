package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;
import java.io.IOException;

public class A1H_Tanker extends AD_Tanker
    implements TypeDockable
{

    public A1H_Tanker()
    {
        drones = new Actor[1];
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
        if(((FlightModelMain) (super.FM)).AS.isMaster())
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
                    if(locDockport.getPoint().distance(locProbe.getPoint()) >= 7.5D)
                        continue;
                    if(((FlightModelMain) (super.FM)).AS.isMaster())
                        typeDockableRequestAttach(actor, i, true);
                    else
                        ((FlightModelMain) (super.FM)).AS.netToMaster(32, i, 0, actor);
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
                    if(((FlightModelMain) (super.FM)).AS.isMaster())
                        typeDockableRequestDetach(actor, i, true);
                    else
                        ((FlightModelMain) (super.FM)).AS.netToMaster(33, i, 1, actor);
            }

    }

    public void typeDockableRequestAttach(Actor actor, int i, boolean flag)
    {
        if(i >= 0 && i <= 1)
            if(flag)
            {
                if(((FlightModelMain) (super.FM)).AS.isMaster())
                {
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(34, i, 1, actor);
                    typeDockableDoAttachToDrone(actor, i);
                } else
                {
                    ((FlightModelMain) (super.FM)).AS.netToMaster(34, i, 1, actor);
                }
            } else
            if(((FlightModelMain) (super.FM)).AS.isMaster())
            {
                if(!Actor.isValid(drones[i]))
                {
                    ((FlightModelMain) (super.FM)).AS.netToMirrors(34, i, 1, actor);
                    typeDockableDoAttachToDrone(actor, i);
                }
            } else
            {
                ((FlightModelMain) (super.FM)).AS.netToMaster(34, i, 0, actor);
            }
    }

    public void typeDockableRequestDetach(Actor actor, int i, boolean flag)
    {
        if(flag)
            if(((FlightModelMain) (super.FM)).AS.isMaster())
            {
                ((FlightModelMain) (super.FM)).AS.netToMirrors(35, i, 1, actor);
                typeDockableDoDetachFromDrone(i);
            } else
            {
                ((FlightModelMain) (super.FM)).AS.netToMaster(35, i, 1, actor);
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

    protected boolean cutFM(int i, int j, Actor actor)
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster())
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

    private Actor drones[];

    static 
    {
        Class class1 = com.maddox.il2.objects.air.A1H_Tanker.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A1H");
        Property.set(class1, "meshName", "3DO/Plane/A1H_Tanker(multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1945F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/A1H.fmd");
        Property.set(class1, "LOSElevation", 1.0585F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            1, 1, 1, 1, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev17", "_ExternalDev18"
        });
        String as[] = new String[6];
        as[0] = "MGunHispanoMkIk 200";
        as[1] = "MGunHispanoMkIk 200";
        as[2] = "MGunHispanoMkIk 200";
        as[3] = "MGunHispanoMkIk 200";
        Aircraft.weaponsRegister(class1, "default", as);
        Aircraft.weaponsRegister(class1, "2x DT", new String[] {
            "MGunHispanoMkIk 200", "MGunHispanoMkIk 200", "MGunHispanoMkIk 200", "MGunHispanoMkIk 200", "FuelTankGun_TankAD4 1", "FuelTankGun_TankAD4 1"
        });
        Aircraft.weaponsRegister(class1, "none", new String[6]);
    }
}