package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;


public class KA_6D extends A_6
    implements TypeDockable, TypeTankerDrogue
{

    public KA_6D()
    {
        bDrogueExtended = true;
        bInRefueling = false;
        maxSendRefuel = 10.093F;      // max send rate = 200gal per 1minute 
          // 200gal == 757liter == 605kg JP-5 ---> 1 sec cycle = 10.093 kg
        drones = new Actor[1];
        waitRefuelTimer = 0L;
        bD704 = false;
        bUseD704 = false;
        ratdeg = 0F;
        bEmpty = false;
    }

    public boolean isDrogueExtended()
    {
        return bDrogueExtended;
    }

    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof Pylon_USTER_gn16)
                        ((Pylon_USTER_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_USMERfw_gn16)
                        ((Pylon_USMERfw_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_gn16)
                        ((Pylon_LAU10_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU10_Cap_gn16)
                        ((Pylon_LAU10_Cap_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU7_gn16)
                        ((Pylon_LAU7_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof Pylon_LAU118_gn16)
                        ((Pylon_LAU118_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof BombGunCBU24_gn16)
                        ((BombGunCBU24_gn16)FM.CT.Weapons[i][j]).matGray();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkU1_gn16)
                        ((FuelTankGun_TankSkyhawkU1_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkNF_gn16)
                        ((FuelTankGun_TankSkyhawkNF_gn16)FM.CT.Weapons[i][j]).matHighvis();
                    else if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawkV2_gn16)
                        ((FuelTankGun_TankSkyhawkV2_gn16)FM.CT.Weapons[i][j]).matHighvis();
                }
            }
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();

        if(thisWeaponsName.startsWith("D704"))
            bD704 = true;
        if(thisWeaponsName.startsWith("D704use"))
            bUseD704 = true;

        if(bD704)
        {
            ((FlightModelMain) FM).M.maxFuel += 880F;  // additional fuel 300gal in D704 Refuel Store
            ((FlightModelMain) FM).M.fuel += 880F;
            ((FlightModelMain) FM).M.massEmpty += 370F;   // empty weight of D704 Refuel Store
            ((FlightModelMain) FM).M.mass += 370F;
            ((FlightModelMain) FM).M.maxWeight += 1250F;
            hierMesh().chunkVisible("D704_RefuelStore", true);
            hierMesh().chunkVisible("D704_Drogue1_Fold", true);
            hierMesh().chunkVisible("D704_rat", true);
            hierMesh().chunkVisible("D704_iGreen", true);
            hierMesh().chunkVisible("D704_iYellow", true);
        }

        if(thisWeaponsName.startsWith("none"))
            bEmpty = true;
    }

    public void update(float f)
    {
        drogueRefuel(f);

        if(bD704 && FM.getSpeedKMH() > 185F)
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
            if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AS.isMaster() && ((SndAircraft) (aircraft)).FM.getSpeedKMH() > 10F && FM.getSpeedKMH() > 10F && isDrogueExtended())
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

    public void missionStarting()
    {
        super.missionStarting();

        checkChangeWeaponColors();
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
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
           || ias > 580F || ias < 325F || FM.M.fuel < FM.M.maxFuel * 0.20F)
        {
//            if(Time.current() > waitRefuelTimer)
//            {
                hierMesh().chunkVisible("KA_Drogue1", false);
                if(bUseD704)
                {
                    hierMesh().chunkVisible("D704_FuelLine1", false);
                    hierMesh().chunkVisible("D704_Drogue1_Fold", true);
                    if(bDrogueExtended == true)
                    {
                        hierMesh().materialReplace("CDYellowOff", "CDYellowOff");
                    }
                }
                else
                {
                    hierMesh().chunkVisible("KA_FuelLine1", false);
                    hierMesh().chunkVisible("KA_Drogue1_Fold", true);
                    if(bDrogueExtended == true)
                    {
                        hierMesh().materialReplace("CYellowOff", "CYellowOff");
                    }
                }
                bDrogueExtended = false;
                waitRefuelTimer = Time.current() + 8000L;
                typeDockableAttemptDetach();
                if(bInRefueling == true)
                {
                    if(bUseD704)
                        hierMesh().materialReplace("CDGreenOff", "CDGreenOff");
                    else
                        hierMesh().materialReplace("CGreenOff", "CGreenOff");
                    bInRefueling = false;
                }
//            }
        } else
        {
//            if(Time.current() > waitRefuelTimer)
//            {
                hierMesh().chunkVisible("KA_Drogue1", true);
                if(bUseD704)
                {
                    hierMesh().chunkVisible("D704_FuelLine1", true);
                    hierMesh().chunkVisible("D704_Drogue1_Fold", false);
                    if(bDrogueExtended == false && bInRefueling == false)
                    {
                        hierMesh().materialReplace("CDYellowOff", "CDYellowOn");
                    }
                }
                else
                {
                    hierMesh().chunkVisible("KA_FuelLine1", true);
                    hierMesh().chunkVisible("KA_Drogue1_Fold", false);
                    if(bDrogueExtended == false && bInRefueling == false)
                    {
                        hierMesh().materialReplace("CYellowOff", "CYellowOn");
                    }
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
                        if(bUseD704)
                        {
                            hierMesh().materialReplace("CDGreenOff", "CDGreenOn");
                            hierMesh().materialReplace("CDYellowOff", "CDYellowOff");
                        }
                        else
                        {
                            hierMesh().materialReplace("CGreenOff", "CGreenOn");
                            hierMesh().materialReplace("CYellowOff", "CYellowOff");
                        }
                        bInRefueling = true;
                    }
                }
                else
                {
                    if(bInRefueling == true)
                    {
                        if(bUseD704)
                        {
                            hierMesh().materialReplace("CDGreenOff", "CDGreenOff");
                            hierMesh().materialReplace("CDYellowOff", "CDYellowOn");
                        }
                        else
                        {
                            hierMesh().materialReplace("CGreenOff", "CGreenOff");
                            hierMesh().materialReplace("CYellowOff", "CYellowOn");
                        }
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

    private boolean bDrogueExtended;
    private boolean bInRefueling;
    private Actor drones[];
    private float maxSendRefuel;
    private long waitRefuelTimer;
    private float ratdeg;
    private boolean bD704;
    private boolean bUseD704;
    private boolean bEmpty;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "KA-6D");
        Property.set(class1, "meshName", "3DO/Plane/KA-6D/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1970F);
        Property.set(class1, "yearExpired", 1980F);
        Property.set(class1, "FlightModel", "FlightModels/A6A.fmd:A6");
//        Property.set(class1, "cockpitClass", new Class[] {     // AI only
//            com.maddox.il2.objects.air.CockpitA_6.class
//        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
            9, 9, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 2, 2, 2, 2, 2, 2, 2, 2,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 9, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2, 2, 7, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_ExternalDev06",  "_ExternalDev01",  "_ExternalDev02",  "_ExternalDev03",  "_ExternalDev04",  "_ExternalDev05",  "_ExternalDev01",  "_ExternalDev02",  "_ExternalDev03",  "_ExternalDev04", 
            "_ExternalDev05",  "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb01", "_Bomb06",          "_ExternalBomb07", "_Bomb08",        "_ExternalBomb09", 
            "_Bomb10",         "_ExternalBomb11", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", 
            "_Bomb12",         "_Bomb13",         "_ExternalBomb14", "_ExternalBomb15", "_Bomb16",         "_ExternalBomb17", "_ExternalBomb18", "_Bomb19NOUSE",    "_ExternalBomb20", "_Bomb21",
            "_Bomb22NOUSE",    "_ExternalBomb23", "_Bomb24",         "_Bomb25",         "_ExternalBomb26", "_ExternalBomb27", "_Bomb28",         "_Bomb29",         "_ExternalBomb30", "_ExternalBomb31",
            "_Bomb32",         "_Bomb33",         "_ExternalBomb34", "_ExternalBomb35", "_ExternalBomb36", "_ExternalBomb37", "_ExternalBomb38", "_ExternalBomb39", "_ExternalBomb40", "_ExternalBomb41",
            "_ExternalBomb42", "_ExternalBomb43", "_ExternalBomb44", "_ExternalBomb45", "_ExternalBomb46", "_ExternalBomb47", "_ExternalBomb48", "_ExternalDev07",  "_ExternalDev08",  "_ExternalDev09",
            "_ExternalDev10",  "_ExternalDev11",  "_ExternalDev12",  "_ExternalDev13",  "_ExternalDev14",  "_ExternalDev15",  "_ExternalDev16",  "_ExternalDev07",  "_ExternalDev08",  "_ExternalDev09",
            "_ExternalDev10",  "_ExternalDev11",  "_ExternalDev12",  "_ExternalDev13",  "_ExternalDev14",  "_ExternalDev15",  "_ExternalDev16",  "_Rock05",         "_Rock06",         "_Rock07",
            "_Rock08",         "_Rock09",         "_Rock10",         "_Rock11",         "_Rock12"       ,  "_Rock13",         "_Rock14"       ,  "_Rock15",         "_Rock16",         "_Rock17",
            "_Rock18",         "_Rock19",         "_Rock20",         "_Rock21",         "_Rock22"       ,  "_Rock23",         "_Rock24",         "_Rock25",         "_Rock26",         "_Rock27",
            "_Rock28",         "_Rock29",         "_Rock30",         "_ExternalRock31", "_ExternalRock31", "_ExternalRock32", "_ExternalRock32", "_Rock33",         "_Rock34",         "_ExternalRock35",
            "_ExternalRock35", "_ExternalRock36", "_ExternalRock36", "_ExternalRock37", "_ExternalRock37", "_ExternalRock38", "_ExternalRock38", "_Flare01",        "_Chaff01"
        });
        String s = "";
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            char c = 129;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[c];
            s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[127] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[128] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[127] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[128] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[127] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[128] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkNF_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[127] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[128] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "D704";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[127] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[128] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "D704+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[127] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[128] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "D704+4x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[127] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[128] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "D704use";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[127] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[128] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "D704use+2x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[127] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[128] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "D704use+4x300Dt";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(9, "Pylon_F100_Outboard_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawkU1_gn16", 1);
            a_lweaponslot[127] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[128] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "empty";
            a_lweaponslot = new Aircraft._WeaponSlot[c];
            a_lweaponslot[0] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - KA_6D : " + s);
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}