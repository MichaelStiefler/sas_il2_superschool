package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;
import java.io.IOException;

public class A1H_Tanker extends AD_Tanker
    implements TypeDockable, TypeTankerDrogue
{

    public A1H_Tanker()
    {
        bDrogueExtended = true;
        bInRefueling = false;
        maxSendRefuel = 9.588F;      // max send rate = 190gal per 1minute 
          // 190gal == 719liter == 575kg JP-5 ---> 1 sec cycle = 9.588 kg
        drones = new Actor[1];
        waitRefuelTimer = 0L;
        ratdeg = 0F;
        bEmpty = false;
    }

    public boolean isDrogueExtended()
    {
        return bDrogueExtended;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();

        ((FlightModelMain) FM).M.maxFuel += 880F;  // additional fuel 300gal in D-704 Refuel Store
        ((FlightModelMain) FM).M.fuel += 880F;
        ((FlightModelMain) FM).M.massEmpty += 370F;   // empty weight of D-704 Refuel Store
        ((FlightModelMain) FM).M.mass += 370F;
        ((FlightModelMain) FM).M.maxWeight += 1250F;

        if(thisWeaponsName.startsWith("none"))
            bEmpty = true;
    }

    public void update(float f)
    {
        drogueRefuel(f);

        if(FM.CT.getArrestor() > 0.2F)
            calculateArrestor();

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

    private boolean bDrogueExtended;
    private boolean bInRefueling;
    private Actor drones[];
    private float maxSendRefuel;
    private long waitRefuelTimer;
    private float ratdeg;
    private boolean bEmpty;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.A1H_Tanker.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "A-1H");
        Property.set(class1, "meshName", "3DO/Plane/A1H_Tanker(multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1968F);
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