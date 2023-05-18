
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;


public class HY6D extends Tu16
    implements TypeTransport, TypeDockable, TypeTankerDrogue
{

    public HY6D()
    {
        APmode1 = false;
        APmode2 = false;
        APmode3 = false;
        bDrogueExtended = false;
        maxSendRefuel = 10.093F;      // max send rate = 200gal per 1minute 
          // 200gal == 757liter == 605kg jet fuel ---> 1 sec cycle = 10.093 kg
        drones = new Actor[2];
        waitRefuelTimer = 0L;
        bEmpty = false;
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        return "HY6_";
    }

    public boolean isDrogueExtended()
    {
        return bDrogueExtended;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.CT.bHasDragChuteControl = true;
        bHasDeployedDragChute = false;
        if(thisWeaponsName.startsWith("none"))
            bEmpty = true;
        else
        {
            bEmpty = false;
            FM.M.maxFuel += 2000F;  // additional fuel 2000kg for Refueling
            FM.M.fuel += 2000F;
            FM.M.massEmpty += 370F;   // empty weight of Refuel kit
            FM.M.mass += 370F;
            FM.M.maxWeight += 2370F;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33: // '!'
        case 34: // '"'
        case 35: // '#'
        case 36: // '$'
        case 37: // '%'
        case 38: // '&'
        default:
            return super.cutFM(i, j, actor);
        }
    }

    public void update(float f)
    {
        drogueRefuel();

        if(FM.getSpeedKMH() > 185F)
            RATrot();

        super.update(f);
        if(FM.CT.DragChuteControl > 0.0F && !bHasDeployedDragChute)
        {
            chute = new Chute(this);
            chute.setMesh("3do/plane/ChuteF86/mono.sim");
            chute.collide(true);
            chute.mesh().setScale(1.0F);
            ((Actor) (chute)).pos.setRel(new Point3d(-23.0D, 0.0D, 0.70D), new Orient(0.0F, 90F, 0.0F));
            bHasDeployedDragChute = true;
        } else
        if(bHasDeployedDragChute && FM.CT.bHasDragChuteControl)
            if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() > 600F || FM.CT.DragChuteControl < 1.0F)
            {
                if(chute != null)
                {
                    chute.tangleChute(this);
                    ((Actor) (chute)).pos.setRel(new Point3d(-10D, 0.0D, 1.0D), new Orient(0.0F, 80F, 0.0F));
                }
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 250L;
            } else
            if(FM.CT.DragChuteControl == 1.0F && FM.getSpeedKMH() < 20F)
            {
                if(chute != null)
                    chute.tangleChute(this);
                ((Actor) (chute)).pos.setRel(new Orient(0.0F, 100F, 0.0F));
                FM.CT.DragChuteControl = 0.0F;
                FM.CT.bHasDragChuteControl = false;
                FM.Sq.dragChuteCx = 0.0F;
                removeChuteTimer = Time.current() + 10000L;
            }
        if(removeChuteTimer > 0L && !FM.CT.bHasDragChuteControl && Time.current() > removeChuteTimer)
            chute.destroy();
        if(FM.AS.isMaster() && Config.isUSE_RENDER())
        {
            for(int en = 0; en < 2; en++)
            {
                if(FM.EI.engines[en].getPowerOutput() > 0.8F && FM.EI.engines[en].getStage() == 6)
                {
                    if(FM.EI.engines[en].getPowerOutput() > 0.95F)
                        FM.AS.setSootState(this, en, 3);
                    else
                        FM.AS.setSootState(this, en, 2);
                } else
                {
                    FM.AS.setSootState(this, en, 0);
                }
            }
        }
        float f2 = FM.getSpeedKMH() - 700F;
        if(f2 < 0.0F)
            f2 = 0.0F;
        FM.CT.dvGear = 0.2F - f2 / 700F;
        if(FM.CT.dvGear < 0.0F)
            FM.CT.dvGear = 0.0F;
    }

    public void missionStarting()
    {
        super.missionStarting();

        bFirstTime = true;
    }

    private void RATrot()
    {
        for(int i=0; i<2; i++)
        {
            if(FM.getSpeedKMH() < 250F)
                ratdeg[i] -= 10F + World.Rnd().nextFloat(-0.3F, 0.3F);
            else if(FM.getSpeedKMH() < 400F)
                ratdeg[i] -= 20F + World.Rnd().nextFloat(-0.8F, 0.8F);
            else if(FM.getSpeedKMH() < 550F)
                ratdeg[i] -= 25F + World.Rnd().nextFloat(-0.9F, 0.9F);
            else
                ratdeg[i] -= 31F + World.Rnd().nextFloat(-1.0F, 1.0F);
            if(ratdeg[i] < -720F) ratdeg[i] += 1440F;
        }

        hierMesh().chunkSetAngles("HY6_rat1", 0.0F, 0.0F, ratdeg[0]);
        hierMesh().chunkSetAngles("HY6_rat2", 0.0F, 0.0F, ratdeg[1]);

        if(FM.getSpeedKMH() > 300F)
        {
            hierMesh().chunkVisible("HY6_rat_rot1", true);
            hierMesh().chunkVisible("HY6_rat_rot2", true);
            hierMesh().chunkVisible("HY6_rat1", false);
            hierMesh().chunkVisible("HY6_rat2", false);
        }
        else
        {
            hierMesh().chunkVisible("HY6_rat_rot1", false);
            hierMesh().chunkVisible("HY6_rat_rot2", false);
            hierMesh().chunkVisible("HY6_rat1", true);
            hierMesh().chunkVisible("HY6_rat2", true);
        }
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

    private void drogueRefuel()
    {
        float ias = Pitot.Indicator((float) (((Tuple3d) ((FlightModelMain)FM).Loc).z), FM.getSpeed()) * 3.6F;
        Aircraft enemy1 = War.GetNearestEnemyAircraft(this, 5000F, 9);
        Aircraft enemy2 = War.GetNearestEnemyAircraft(this, 6000F, 9);

        if(bEmpty || (FM.getAltitude() < 1000F && FM.getAltitude() != 0.0F && !bFirstTime) || FM.CT.getGear() > 0.0F
           || ias > 700F || (ias < 325F && ias != 0.0F && !bFirstTime) || FM.M.fuel < FM.M.maxFuel * 0.40F || enemy1 != null)
        {
//            if(Time.current() > waitRefuelTimer)
//            {
                hierMesh().chunkVisible("HY6_Drogue1_D0", false);
                hierMesh().chunkVisible("HY6_Drogue2_D0", false);
                hierMesh().chunkVisible("HY6_FuelLine1_D0", false);
                hierMesh().chunkVisible("HY6_FuelLine2_D0", false);
                hierMesh().chunkVisible("HY6_Drogue1_Fold_D0", true);
                hierMesh().chunkVisible("HY6_Drogue2_Fold_D0", true);

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
//            }
        } else
        {
//            if(Time.current() > waitRefuelTimer)
//            {
                hierMesh().chunkVisible("HY6_Drogue1_D0", true);
                hierMesh().chunkVisible("HY6_FuelLine1_D0", true);
                hierMesh().chunkVisible("HY6_Drogue2_D0", true);
                hierMesh().chunkVisible("HY6_FuelLine2_D0", true);
                hierMesh().chunkVisible("HY6_Drogue1_Fold_D0", false);
                hierMesh().chunkVisible("HY6_Drogue2_Fold_D0", false);
                bDrogueExtended = true;
                waitRefuelTimer = Time.current() + 8000L;
//            }
        }

        if(bDrogueExtended && FM.AS.isMaster())
        {
            for(int i = 0; i < drones.length; i++)
                if(Actor.isValid(drones[i]))
                {
                    if(bInRefueling[i] == false)
                    {
                        bInRefueling[i] = true;
                    }
                }
                else
                {
                    if(bInRefueling[i] == true)
                    {
                        bInRefueling[i] = false;
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

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap2_D0", 0.0F, -35F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap3_D0", 0.0F, -35F * f, 0.0F);
        if(FM.CT.Weapons[2] != null)
        {
            hierMesh().chunkSetAngles("Flap1_D0", 0.0F, -25F * f, 0.0F);
            hierMesh().chunkSetAngles("Flap4_D0", 0.0F, -25F * f, 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("Flap1_D0", 0.0F, -35F * f, 0.0F);
            hierMesh().chunkSetAngles("Flap4_D0", 0.0F, -35F * f, 0.0F);
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 7; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        default:
            break;

        case 0: // '\0'
            if(f1 < -1F)
            {
                f1 = -1F;
                flag = false;
            }
            if(f1 > 80F)
            {
                f1 = 80F;
                flag = false;
            }
            break;

        case 1: // '\001'
            if(f1 < -80F)
            {
                f1 = -80F;
                flag = false;
            }
            if(f1 > 1.0F)
            {
                f1 = 1.0F;
                flag = false;
            }
            break;

        case 2: // '\002'
            if(f < -35F)
            {
                f = -35F;
                flag = false;
            }
            if(f > 35F)
            {
                f = 35F;
                flag = false;
            }
            if(f1 < -45F)
            {
                f1 = -45F;
                flag = false;
            }
            if(f1 > 45F)
            {
                f1 = 45F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void auxPressed(int i)
    {
        super.auxPressed(i);
        if(i == 20)
            if(!APmode1)
            {
                APmode1 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude ON");
                FM.AP.setStabAltitude((float)FM.Loc.z);
            } else
            if(APmode1)
            {
                APmode1 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Altitude OFF");
                FM.AP.setStabAltitude(false);
            }
        if(i == 21)
            if(!APmode2)
            {
                APmode2 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction ON");
                FM.AP.setStabDirection(true);
                FM.CT.bHasRudderControl = false;
            } else
            if(APmode2)
            {
                APmode2 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Direction OFF");
                FM.AP.setStabDirection(false);
                FM.CT.bHasRudderControl = true;
            }
        if(i == 22)
        {
            FM.AP.way.prev();
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Navigator: Previous Waypoint");
        }
        if(i == 23)
        {
            FM.AP.way.next();
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Navigator: Next Waypoint");
        }
        if(i == 24)
        {
            FM.CT.setTrimElevatorControl(0.3125F);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Co-Pilot: Take Off Position");
        }
        if(i == 25)
        {
            FM.CT.DiffBrakesType++;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Co-Pilot: Take Off Position");
            if(FM.CT.DiffBrakesType > 1)
                FM.CT.DiffBrakesType = 0;
            if(FM.CT.DiffBrakesType == 0)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Front Wheel Steer: Active");
            if(FM.CT.DiffBrakesType == 1)
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Front Wheel Steer: Passive");
        }
    }

    public boolean APmode1;
    public boolean APmode2;
    public boolean APmode3;
    private boolean bHasDeployedDragChute;
    private Chute chute;
    private long removeChuteTimer;
    public static boolean bChangedPit = false;
    private boolean bFirstTime = true;
    private boolean bDrogueExtended;
    private boolean bInRefueling[] = { false, false };
    private Actor drones[];
    private float maxSendRefuel;
    private long waitRefuelTimer;
    private float ratdeg[] = { 0.0F, 0.0F };
    private boolean bEmpty;

    static
    {
        Class class1 = com.maddox.il2.objects.air.HY6D.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "HY-6");
        Property.set(class1, "meshName", "3DO/Plane/Tu16A/hier_hy6d.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1990F);
        Property.set(class1, "yearExpired", 2030F);
        Property.set(class1, "FlightModel", "FlightModels/Tu_16A.fmd:TU_16");
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            12, 12
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN06", "_MGUN07"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 2;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(12, "MGunVYakNS23", 315);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(12, "MGunVYakNS23", 315);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            for(int j = 0; j < byte0; j++)
                a_lweaponslot[j] = null;

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}