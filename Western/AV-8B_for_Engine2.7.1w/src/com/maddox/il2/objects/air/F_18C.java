
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

// Referenced classes of package com.maddox.il2.objects.air:
//            F_18, Aircraft, TypeTankerDrogue, TypeDockable, 
//            PaintSchemeFMPar05, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, 
//            TypeGSuit, TypeAcePlane, TypeFuelDump, TypeStormovikArmored, 
//            NetAircraft

public class F_18C extends F_18
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeDockable
{

    public F_18C()
    {
        guidedMissileUtils = null;
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
        guidedMissileUtils = new GuidedMissileUtils(this);
        bulletEmitters = null;
        wingFoldValue = 0.0F;
        IR = false;
        missilesList = new ArrayList();
        counterFlareList = new ArrayList();
        counterChaffList = new ArrayList();
        tX4Prev = 0L;
    }

    private void checkAmmo()
    {
        missilesList.clear();
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                    if(FM.CT.Weapons[i][j].haveBullets())
                    {
                        if(FM.CT.Weapons[i][j] instanceof RocketGunAGM65Ds)
                            IR = true;
                        if(FM.CT.Weapons[i][j] instanceof RocketGunFlare_gn16)
                            counterFlareList.add(FM.CT.Weapons[i][j]);
                        else if(FM.CT.Weapons[i][j] instanceof RocketGunChaff_gn16)
                            counterChaffList.add(FM.CT.Weapons[i][j]);
                        else if(FM.CT.Weapons[i][j] instanceof BombGunGBU10_Mk84LGB_gn16 ||
                                FM.CT.Weapons[i][j] instanceof BombGunGBU12_Mk82LGB_gn16 ||
                                FM.CT.Weapons[i][j] instanceof BombGunGBU16_Mk83LGB_gn16)
                            super.bHasPaveway = true;
                        else
                            missilesList.add(FM.CT.Weapons[i][j]);
                        if(FM.CT.Weapons[i][j] instanceof RocketBombGun)
                            IR = true;
                    }

            }

    }

    public void launchMsl()
    {
        if(missilesList.isEmpty())
        {
            return;
        }
        else
        {
            ((RocketGunAGM65L)missilesList.remove(0)).shots(1);
            return;
        }
    }

    public void launchbmb()
    {
        if(missilesList.isEmpty())
        {
            return;
        }
        else
        {
            ((RocketBombGun)missilesList.remove(0)).shots(1);
            return;
        }
    }

    public void backFire()
    {
        if(counterFlareList.isEmpty())
            hasFlare = false;
        else
        {
            if(Time.current() > lastFlareDeployed + 700L)
            {
                ((RocketGunFlare_gn16)counterFlareList.get(0)).shots(1);
                hasFlare = true;
                lastFlareDeployed = Time.current();
                if(!((RocketGunFlare_gn16)counterFlareList.get(0)).haveBullets())
                    counterFlareList.remove(0);
            }
        }
        if(counterChaffList.isEmpty())
            hasChaff = false;
        else
        {
            if(Time.current() > lastChaffDeployed + 900L)
            {
                ((RocketGunChaff_gn16)counterChaffList.get(0)).shots(1);
                hasChaff = true;
                lastChaffDeployed = Time.current();
                if(!((RocketGunChaff_gn16)counterChaffList.get(0)).haveBullets())
                    counterChaffList.remove(0);
            }
        }
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

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
        bulletEmitters = new BulletEmitter[weaponHookArray.length];
        for(int i = 0; i < weaponHookArray.length; i++)
            bulletEmitters[i] = getBulletEmitterByHookName(weaponHookArray[i]);
        FM.turret[0].bIsAIControlled = false;
    }

    public void update(float f)
    {
        if(bNeedSetup)
            checkAsDrone();
        guidedMissileUtils.update();
        computeF404_AB();
        computeLift();
        computeEnergy();
        computeEngine();
        if(super.FM instanceof Maneuver)
            receivingRefuel(f);
        super.update(f);
        if(super.backfire)
            backFire();
        if((!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (super.FM instanceof Maneuver) && ((Maneuver)super.FM).get_task() == 7 && !FM.AP.way.isLanding())
            if(missilesList.isEmpty() && !((Maneuver)super.FM).hasBombs())
            {
                Pilot pilot = (Pilot)FM;
                Vector3d vector3d = new Vector3d();
                getSpeed(vector3d);
                Point3d point3d1 = new Point3d();
                super.pos.getAbs(point3d1);
                float f3 = (float)((double)FM.getAltitude() - World.land().HQ(point3d1.x, point3d1.y));
                if(f3 < 55F && vector3d.z < 0.0D)
                    vector3d.z = 0.0D;
                else
                if(pilot != null && Actor.isAlive(((Maneuver) (pilot)).target_ground))
                {
                    Point3d point3d2 = new Point3d();
                    ((Maneuver) (pilot)).target_ground.pos.getAbs(point3d2);
                    pilot.set_maneuver(43);
                    if(super.pos.getAbsPoint().distance(point3d2) < 2000D)
                    {
                        point3d2.sub(FM.Loc);
                        FM.Or.transformInv(point3d2);
                        FM.CT.PowerControl = 0.55F;
                    }
                }
                setSpeed(vector3d);
            }
            else
            if(!missilesList.isEmpty() && Time.current() > tX4Prev + 500L + (IR ? 10000L : 0L))
            {
                Pilot pilot1 = (Pilot)FM;
                if(pilot1.get_maneuver() == 43 && ((Maneuver) (pilot1)).target_ground != null)
                {
                    Point3d point3d = new Point3d();
                    ((Maneuver) (pilot1)).target_ground.pos.getAbs(point3d);
                    point3d.sub(FM.Loc);
                    FM.Or.transformInv(point3d);
                    if(point3d.x > 1000D && point3d.x < (IR ? 2250D : 1250D) + 250D * (double)FM.Skill)
                    {
                        if(!IR)
                            point3d.x /= 2 - FM.Skill / 3;
                        if(point3d.y < point3d.x && point3d.y > -point3d.x && point3d.z * 1.5D < point3d.x && point3d.z * 1.5D > -point3d.x)
                        {
                            launchMsl();
                            tX4Prev = Time.current();
                            Voice.speakAttackByRockets(this);
                        }
                    }
                }
            }
    }

    public void computeF404_AB()
    {
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() > 5)
            FM.producedAF.x += 25000D;
        if(FM.EI.engines[1].getThrustOutput() > 1.001F && FM.EI.engines[1].getStage() > 5)
            FM.producedAF.x += 25000D;
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() > 1.001F && FM.EI.engines[0].getStage() == 6 && FM.EI.engines[1].getThrustOutput() > 1.001F && FM.EI.engines[1].getStage() == 6)
            if(f > 17.3F)
            {
                f1 = 20F;
            }
            else
            {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                float f5 = f4 * f;
                f1 = ((0.00221147F * f4 - 0.0498509F * f3) + 0.319277F * f2) - 0.121025F * f;
            }
        FM.producedAF.x -= f1 * 1000F;
    }

    public void computeLift()
    {
        Polares polares = (Polares)Reflection.getValue(FM, "Wing");
        float f = calculateMach();
        if(calculateMach() < 0.0F);
        float f1 = 0.0F;
        if(f > 2.25F)
        {
            f1 = 0.12F;
        }
        else
        {
            float f2 = f * f;
            float f3 = f2 * f;
            float f4 = f3 * f;
            float f5 = f4 * f;
            float f6 = f5 * f;
            float f7 = f6 * f;
            float f8 = f7 * f;
            float f9 = f8 * f;
            f1 = ((((((0.00152131F * f8 + 0.0351945F * f7) - 0.403687F * f6) + 1.58931F * f5) - 3.09189F * f4) + 3.21415F * f3) - 1.73844F * f2) + 0.364213F * f + 0.078F;
        }
        polares.lineCyCoeff = f1;
    }

    public void computeEnergy()
    {
        float f = FM.getOverload();
        if(FM.getOverload() < 4.5F);
        float f1 = 0.0F;
        if(f >= 10F)
        {
            f1 = 0.085F;
        }
        else
        {
            float f2 = f * f;
            float f3 = f2 * f;
            float f4 = f3 * f;
            float f5 = f4 * f;
            float f6 = f5 * f;
            f1 = ((6.734E-007F * f5 + 9.876539E-007F * f4) - 7.57583E-006F * f3) + 2.22222E-006F * f2 + 1.70512E-005F * f;
        }
        FM.Sq.dragParasiteCx += f1;
    }

    public void computeEngine()
    {
        float f = FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if(FM.EI.engines[0].getThrustOutput() < 1.001F && FM.EI.engines[0].getStage() > 5 && FM.EI.engines[1].getThrustOutput() < 1.001F && FM.EI.engines[1].getStage() > 5)
            if(calculateMach() <= 0.0F);
        if(f > 13.5F)
        {
            f1 = 1.5F;
        }
        else
        {
            float f2 = f * f;
            float f3 = f2 * f;
            f1 = 0.0130719F * f2 - 0.0653595F * f;
        }
        FM.producedAF.x -= f1 * 1000F;
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
        checkAmmo();
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
            if((aircraft instanceof TypeTankerDrogue) && FM.CT.getRefuel() > 0.95F)
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
        }
        else
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

    public void updateHook()
    {
        for(int i = 0; i < weaponHookArray.length; i++)
            try
            {
               if(bulletEmitters[i] instanceof RocketGunAIM9L_gn16)
                    ((RocketGunAIM9L_gn16)bulletEmitters[i]).updateHook(weaponHookArray[i]);
            }
            catch(Exception exception) { }
    }

    public void moveWingFold(float f)
    {
        moveWingFold(hierMesh(), f);
        super.moveWingFold(f);
        if(wingFoldValue != f)
        {
            wingFoldValue = f;
            super.needUpdateHook = true;
        }
    }

    private void receivingRefuel(float f)
    {
        int i = aircIndex();

        if(typeDockableIsDocked())
        {
            if(FM.CT.getRefuel() < 0.9F)
            {
                typeDockableAttemptDetach();
            }
            else
            {
                if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
                {
                    ((Maneuver)super.FM).unblock();
                    ((Maneuver)super.FM).set_maneuver(48);
                    for(int j = 0; j < i; j++)
                        ((Maneuver)super.FM).push(48);

                    if(FM.AP.way.curr().Action != 3)
                        ((FlightModelMain) ((Maneuver)super.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).AP.way.Cur());
                    ((Pilot)FM).setDumbTime(3000L);
                }
                FuelTank fuelTanks[];
                fuelTanks = FM.CT.getFuelTanks();
                if(FM.M.fuel < FM.M.maxFuel - 12F)
                {
                    float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, 11.101F, f);
                    FM.M.fuel += getFuel;
                }
                else if(fuelTanks.length > 0 && fuelTanks[0] != null && !FM.M.bFuelTanksDropped)
                {
                    float freeTankSum = 0F;
                    for(int num = 0; num < fuelTanks.length; num++)
                        freeTankSum += fuelTanks[num].checkFreeTankSpace();
                    if(freeTankSum < 12F)
                        typeDockableAttemptDetach();
                    float getFuel = ((TypeTankerDrogue) (queen_)).requestRefuel((Aircraft) this, 11.101F, f);
                    for(int num = 0; num < fuelTanks.length; num++)
                        fuelTanks[num].doRefuel(getFuel * (fuelTanks[num].checkFreeTankSpace() / freeTankSum));
                }
                else
                    typeDockableAttemptDetach();
            }
        }
        else if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
        {
            if(FM.CT.GearControl == 0.0F && FM.EI.engines[0].getStage() == 0)
                FM.EI.setEngineRunning();
            if(dtime > 0L && ((Maneuver)super.FM).Group != null)
            {
                ((Maneuver)super.FM).Group.leaderGroup = null;
                ((Maneuver)super.FM).set_maneuver(22);
                ((Pilot)FM).setDumbTime(3000L);
                if(Time.current() > dtime + 3000L)
                {
                    dtime = -1L;
                    ((Maneuver)super.FM).clear_stack();
                    ((Maneuver)super.FM).set_maneuver(0);
                    ((Pilot)FM).setDumbTime(0L);
                }
            }
            else if(FM.AP.way.curr().Action == 0)
            {
                Maneuver maneuver = (Maneuver)super.FM;
                if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                    maneuver.Group.setGroupTask(2);
            }
        }
    }

    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(byte bt)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[bt];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - F_18C : Default loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] GenerateCenterPylonConfig(byte bt)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[bt];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_SUU62_F18C_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - F_18C : Center Pylon loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] GenerateCenterTankConfig(byte bt)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[bt];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_SUU62_F18C_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - F_18C : Center Tank loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static Aircraft._WeaponSlot[] GenerateNoPylonConfig(byte bt)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[bt];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[34] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(7, "RocketGunFlare_gn16", 30);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(8, "RocketGunChaff_gn16", 30);
        }
        catch(Exception exception) {
            System.out.println("Weapon register error - F_18C : No Pylon loadout Generator method");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        return a_lweaponslot;
    }

    private static String weaponHookArray[] = {
            "_CANNON01",        "_ExternalDev01",   "_ExternalDev02",   "_ExternalDev03",   "_ExternalDev04",   "_ExternalDev05",   "_ExternalDev06",   "_ExternalDev07",   "_ExternalDev08",   "_ExternalDev09",
            "_ExternalDev10",   "_ExternalDev11",   "_ExternalDev12",   "_ExternalDev13",   "_ExternalDev14",   "_ExternalDev15",   "_ExternalDev16",   "_ExternalDev17",   "_ExternalDev18",   "_ExternalBomb01",
            "_ExternalBomb02",  "_ExternalBomb03",  "_ExternalBomb04",  "_ExternalBomb05",  "_ExternalBomb06",  "_ExternalBomb07",  "_ExternalBomb08",  "_ExternalBomb09",  "_ExternalBomb10",  "_ExternalBomb11",
            "_ExternalBomb12",  "_ExternalBomb13",  "_ExternalMis01",   "_ExternalMis01",   "_ExternalMis02",   "_ExternalMis02",   "_ExternalMis03",   "_ExternalFLIR",    "_ExternalMis04",   "_ExternalLASER",
            "_ExternalMis05",   "_ExternalMis05",   "_ExternalMis06",   "_ExternalMis06",   "_ExternalMis07",   "_ExternalMis07",   "_ExternalMis08",   "_ExternalMis08",   "_ExternalMis09",   "_ExternalMis09",
            "_ExternalMis10",   "_ExternalMis10",   "_ExternalMis11",   "_ExternalMis11",   "_ExternalMis12",   "_ExternalMis12",   "_ExternalMis13",   "_ExternalMis13",   "_ExternalMis14",   "_ExternalMis14",
            "_ExternalMis15",   "_ExternalMis15",   "_ExternalMis16",   "_ExternalMis16",   "_ExternalRock01",  "_ExternalRock01",  "_ExternalRock02",  "_ExternalRock02",  "_ExternalRock03",  "_ExternalRock03",
            "_ExternalRock04",  "_ExternalRock04",  "_ExternalRock05",  "_ExternalRock05",  "_ExternalRock06",  "_ExternalRock06",  "_ExternalRock07",  "_ExternalRock07",  "_ExternalRock08",  "_ExternalRock08",
            "_ExternalRock09",  "_ExternalRock09",  "_ExternalRock10",  "_ExternalRock10",  "_ExternalRock11",  "_ExternalRock11",  "_ExternalRock12",  "_ExternalRock12",  "_Rock13",          "_Rock14",
            "_Rock15",          "_Rock16",          "_Rock17",          "_Rock18",          "_Rock19",          "_Rock20",          "_Rock21",          "_Rock22",          "_Rock23",          "_Rock24",
            "_Rock25",          "_Rock26",          "_Rock27",          "_Rock28",          "_Flare01",         "_Flare02",         "_Chaff01",         "_Chaff02"
    };
    private BulletEmitter bulletEmitters[];
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
    public boolean bToFire;
    private float wingFoldValue;
    private long tX4Prev;
    private boolean IR;
    private ArrayList missilesList;
    private ArrayList counterFlareList;
    private ArrayList counterChaffList;
    private Actor queen_last;
    private long queen_time;
    private boolean bNeedSetup;
    private long dtime;
    private Actor target_;
    private Actor queen_;
    private int dockport_;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-18C");
        Property.set(class1, "meshName", "3DO/Plane/F-18C/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1989F);
        Property.set(class1, "yearExpired", 2050F);
        Property.set(class1, "FlightModel", "FlightModels/F-18C.fmd:F18_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitF_18C.class, com.maddox.il2.objects.air.CockpitF18FLIR.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 9, 9, 9, 9, 9, 9, 9, 9, 9,
            9, 9, 9, 9, 9, 9, 9, 9, 9, 2,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            2, 2, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 7, 7, 8, 8
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01",        "_ExternalDev01",   "_ExternalDev02",   "_ExternalDev03",   "_ExternalDev04",   "_ExternalDev05",   "_ExternalDev06",   "_ExternalDev07",   "_ExternalDev08",   "_ExternalDev09",
            "_ExternalDev10",   "_ExternalDev11",   "_ExternalDev12",   "_ExternalDev13",   "_ExternalDev14",   "_ExternalDev15",   "_ExternalDev16",   "_ExternalDev17",   "_ExternalDev18",   "_ExternalBomb01",
            "_ExternalBomb02",  "_ExternalBomb03",  "_ExternalBomb04",  "_ExternalBomb05",  "_ExternalBomb06",  "_ExternalBomb07",  "_ExternalBomb08",  "_ExternalBomb09",  "_ExternalBomb10",  "_ExternalBomb11",
            "_ExternalBomb12",  "_ExternalBomb13",  "_ExternalMis01",   "_ExternalMis01",   "_ExternalMis02",   "_ExternalMis02",   "_ExternalMis03",   "_ExternalFLIR",    "_ExternalMis04",   "_ExternalLASER",
            "_ExternalMis05",   "_ExternalMis05",   "_ExternalMis06",   "_ExternalMis06",   "_ExternalMis07",   "_ExternalMis07",   "_ExternalMis08",   "_ExternalMis08",   "_ExternalMis09",   "_ExternalMis09",
            "_ExternalMis10",   "_ExternalMis10",   "_ExternalMis11",   "_ExternalMis11",   "_ExternalMis12",   "_ExternalMis12",   "_ExternalMis13",   "_ExternalMis13",   "_ExternalMis14",   "_ExternalMis14",
            "_ExternalMis15",   "_ExternalMis15",   "_ExternalMis16",   "_ExternalMis16",   "_ExternalRock01",  "_ExternalRock01",  "_ExternalRock02",  "_ExternalRock02",  "_ExternalRock03",  "_ExternalRock03",
            "_ExternalRock04",  "_ExternalRock04",  "_ExternalRock05",  "_ExternalRock05",  "_ExternalRock06",  "_ExternalRock06",  "_ExternalRock07",  "_ExternalRock07",  "_ExternalRock08",  "_ExternalRock08",
            "_ExternalRock09",  "_ExternalRock09",  "_ExternalRock10",  "_ExternalRock10",  "_ExternalRock11",  "_ExternalRock11",  "_ExternalRock12",  "_ExternalRock12",  "_Rock13",          "_Rock14",
            "_Rock15",          "_Rock16",          "_Rock17",          "_Rock18",          "_Rock19",          "_Rock20",          "_Rock21",          "_Rock22",          "_Rock23",          "_Rock24",
            "_Rock25",          "_Rock26",          "_Rock27",          "_Rock28",          "_Flare01",         "_Flare02",         "_Chaff01",         "_Chaff02"
        });
        String s = "undefined";
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 108;
            s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = GenerateNoPylonConfig(byte0);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM7+2xAIM9";
            a_lweaponslot = GenerateNoPylonConfig(byte0);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM7+2xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM7+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM7+6xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM7+4xAIM9+ALQ167+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM7+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM7+6xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM7+4xAIM9+ALQ167+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM7+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM7+6xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM7+4xAIM9+ALQ167+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM120+2xAIM9";
            a_lweaponslot = GenerateNoPylonConfig(byte0);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM120+6xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "10xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM120+4xAIM9+ALQ167+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM120+6xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM120+2xAIM9+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM120+2xAIM9+ALQ167+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM120+4xAIM9+ALQ167+2xDt";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM120+6xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xAIM120+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM120+2xAIM9+ALQ167+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM120+4xAIM9+ALQ167+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAIM120+2xAIM7+6xAIM9";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xMk82+ALQ167+2xAIM120+4xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82+ALQ167+2xAIM120+4xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82HD+ALQ167+4xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82+4xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82HD+4xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82+2xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82HD+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82+2xAIM120+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82HD+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xMk83+ALQ167+4xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xMk83+4xMk82+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xMk83HD+4xMk82HD+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "7xMk83+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83+2xMk82+ALQ167+2xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83+4xMk82+2xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83HD+4xMk82HD+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk83+2xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk83HD+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xMk83+2xMk82+ALQ167+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xMk83+4xMk82+2xAIM120+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xMk83HD+4xMk82HD+2xAIM120+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xMk83+2xAIM120+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xMk83HD+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83+2xAIM120+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83HD+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xMk84+4xMk83+ALQ167+4xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xMk84+8xMk83+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk84+1xMk83+1xMk82+ALQ167+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk84HD+1xMk83HD+1xMk82HD+ALQ167+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk84+1xMk83+4xMk82+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xMk84+2xMk82+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xMk84+ALQ167+1xAIM7+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk84+2xMk82+ALQ167+2xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk84+4xMk82+2xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk84HD+4xMk82HD+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk84+2xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk84HD+2xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xMk84+2xMk82+ALQ167+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xMk84+4xMk82+2xAIM120+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xMk84HD+4xMk82HD+2xAIM120+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xMk84+2xMk83+2xAIM120+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xMk84+2xAIM120+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xMk84HD+2xAIM120+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk84+2xAIM120+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk84HD+2xAIM120+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk20+1xMk84+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+4xMk82+1xMk83+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+5xMk83+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk20+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+4xMk82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+2xMk82+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+4xMk83+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+2xMk83+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+1xMk83+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+2xMk84+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+1xMk83+2xAIM120+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+1xMk84+2xAIM120+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+2xAIM120+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xMk77+2xMk82HD+ALQ167+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xMk77+2xMk83HD+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xMk77+2xMk82HD+2xMk20+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+4xMk82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+2xMk82+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+4xMk82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+2xMk82HD+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+2xMk82HD+2xMk20+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+2xMk83+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk77+2xMk83+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+2xMk83HD+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+1xMk83+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xMk77+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+2xAIM120+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82+1xMk83+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82+Zuni+2xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk82HD+Zuni+2xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82+Zuni+ALQ167+2xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82HD+Zuni+ALQ167+2xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(6, "RocketGunAIM120A_gn16", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk83HD+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+1xMk84+Zuni+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+1xMk84HD+Zuni+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk20+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk20+2xMk82+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+1xMk84+Zuni+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+1xMk84HD+Zuni+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk77+2xMk82HD+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xMk77+Zuni+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Zuni+1xMk84+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Zuni+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+6xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+4xAIM9+ALQ167+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+4xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+4xMk20+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+2xMk20+2xMk82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+2xMk82+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+Zuni+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xGBU12+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+2xMk77+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunMk77NapalmMod1_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xGBU12+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xGBU12+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+1xMk84+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU12+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU16+6xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU16+4xAIM9+ALQ167+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU16+4xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU16+4xMk20+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU16+2xMk20+2xMk82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU16+2xMk82+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU16+Zuni+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU16+1xGBU12+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU16+4xGBU12+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xGBU16+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xGBU16+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU16+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU10+6xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU10+4xAIM9+ALQ167+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU10+4xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU10+4xMk83+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU10+4xMk20+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU10+2xMk20+2xMk82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk20RockeyeII_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU10+1xGBU12+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU10+Zuni+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU10+4xGBU12+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU10+4xGBU16+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xGBU10+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xGBU10+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU10_Mk84LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM82+6xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM82+2xAIM7+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM82+4xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM82+4xAIM9+ALQ167+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM82+1xMk83+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM82+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "5xJDAM82+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xJDAM82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM83+6xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM83+2xAIM7+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU115C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(5, "RocketGunAIM7Mhl_gn16", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM83+4xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM83+4xAIM9+ALQ167+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM83+1xJDAM82+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM83+1xMk83+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM83+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xJDAM83+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[24] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[25] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[26] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGunGBU32_Mk83JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM84+6xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM84+4xAIM9+ALQ167+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM84+4xAIM120+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_TwinLAU127_115C_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(6, "RocketGunAIM120Ahl_gn16", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM84+4xMk83+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM84+1xJDAM82+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM84+4xJDAM82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM84+1xMk84+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xJDAM84+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xJDAM84+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunGBU31_Mk84JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65B+2xMk82+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65B+2xMk82HD+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65B+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65B+1xMk83+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65B+1xMk83HD+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65B_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAGM65E+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xMk82+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xMk82HD+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xGBU12+2xMk82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+4xGBU12+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xJDAM82+2xMk82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+4xJDAM82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xMk83+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xMk83HD+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xMk84+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+1xMk84HD+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65E+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAGM65F+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65F+2xMk82+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65F+2xMk82HD+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65F+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71AP_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65F+2xGBU12+2xMk82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65F+4xGBU12+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65F+2xJDAM82+2xMk82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65F+4xJDAM82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU38_Mk82JDAM_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65F+1xMk83+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65F+1xMk83HD+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65F+1xMk84+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65F+1xMk84HD+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84Ballute_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM65F+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAGM123+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM123+4xMk82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM123+2xGBU12+2xMk82+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM123+2xGBU12+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunGBU12_Mk82LGB_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xAGM123+1xGBU16+2xMk82+Zuni+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_BRU33A_CVER_gn16", 1);
            a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(9, "Pylon_LAU10_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunGBU16_Mk83LGB_gn16", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGunMk82_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71_gn16", 4);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(5, "RocketGun5inchZuniMk71_gn16", 4);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM123+1xMk83+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk83_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM123+1xMk84+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunMk84_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM123+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAGM84D+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+2xAGM65E+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+2xAGM65F+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84D+2xAGM123+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(5, "RocketGunAGM84D_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAGM123A_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAGM84J+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+2xAGM65E+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65E_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84J+2xAGM65F+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_LAU117_gn16", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(5, "RocketGunAGM84J_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunAGM65F_gn16", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "4xAGM84E+2xAIM9";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_AN_AWW13_gn16", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM84E_gn16", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAGM84E_gn16", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAGM84E_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAGM84E_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM84E+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "Pylon_AN_AWW13_gn16", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAGM84E_gn16", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAGM84E_gn16", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xB43Nuke70kt+2xAIM9+2xDt";
            a_lweaponslot = GenerateNoPylonConfig(byte0);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "Pylon_SUU63_F18W_gn16", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "Pylon_SUU62_F18C_gn16", 1);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunB43nuke70kt_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB57Nuke10kt+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunB57nuke10kt_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunB57nuke10kt_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB57Nuke10ktPara+2xAIM9+3xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(3, "BombGunB57nuke10ktpara_gn16", 1);
            a_lweaponslot[22] = new Aircraft._WeaponSlot(3, "BombGunB57nuke10ktpara_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB61Nuke03kt+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunB61nuke03kt_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunB61nuke03kt_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xB61Nuke5ktPara+ALQ167+2xAIM9+1xDt";
            a_lweaponslot = GenerateCenterTankConfig(byte0);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "BombGunB61nuke5ktpara_gn16", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunB61nuke5ktpara_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xB61Nuke80kt+ALQ167+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunB61nuke80kt_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xB61Nuke80ktPara+ALQ167+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunB61nuke80ktpara_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xB83Nuke200kt+ALQ167+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunB83nuke200kt_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xB83Nuke500ktPara+ALQ167+2xAIM9+2xDt";
            a_lweaponslot = GenerateCenterPylonConfig(byte0);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankF18C_gn16", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon_AN_ALQ167_gn16", 1);
            a_lweaponslot[23] = new Aircraft._WeaponSlot(3, "BombGunB83nuke500ktpara_gn16", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception)
        {
            System.out.println("Weapons register error - F_18C : " + s);
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}