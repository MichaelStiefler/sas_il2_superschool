package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;

import java.io.IOException;

public class F_100F extends F_100
    implements TypeGuidedMissileCarrier, TypeDockable, TypeCountermeasure, TypeThreatDetector
{

    public F_100F()
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
        arrestor = 0.0F;
        overrideBailout = false;
        ejectComplete = false;
        super.isTrainer = true;
        lTimeNextEject = 0;
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

   public GuidedMissileUtils getGuidedMissileUtils() {
     return this.guidedMissileUtils;
   }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f)
    {
        if(bNeedSetup)
            checkAsDrone();
        int i = aircIndex();
        if(super.FM instanceof Maneuver)
            if(typeDockableIsDocked())
            {
                if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
                {
                    ((Maneuver)super.FM).unblock();
                    ((Maneuver)super.FM).set_maneuver(48);
                    for(int j = 0; j < i; j++)
                        ((Maneuver)super.FM).push(48);

                    if(((FlightModelMain) (super.FM)).AP.way.curr().Action != 3)
                        ((FlightModelMain) ((Maneuver)super.FM)).AP.way.setCur(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).AP.way.Cur());
                    ((Pilot)super.FM).setDumbTime(3000L);
                }
                if(((FlightModelMain) (super.FM)).M.fuel < ((FlightModelMain) (super.FM)).M.maxFuel)
                    ((FlightModelMain) (super.FM)).M.fuel += 20F * f;
            } else
            if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
            {
                if(FM.CT.GearControl == 0.0F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 0)
                    ((FlightModelMain) (super.FM)).EI.setEngineRunning();
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
                if(((FlightModelMain) (super.FM)).AP.way.curr().Action == 0)
                {
                    Maneuver maneuver = (Maneuver)super.FM;
                    if(maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
                        maneuver.Group.setGroupTask(2);
                }
            }
        if(FM.CT.getArrestor() > 0.2F)
            if(FM.Gears.arrestorVAngle != 0.0F)
            {
                float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
                arrestor = 0.8F * arrestor + 0.2F * f1;
                moveArrestorHook(arrestor);
            } else
            {
                float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
                if(f2 < 0.0F && FM.getSpeedKMH() > 60F)
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
        if ((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete
				&& FM.getSpeedKMH() > 15.0F) {
			overrideBailout = true;
			FM.AS.bIsAboutToBailout = false;
			if (Time.current() > lTimeNextEject) bailout();
		}
        super.update(f);
        this.guidedMissileUtils.update();        
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
            if(((FlightModelMain) (super.FM)).AP.way.curr().getTarget() == null)
                ((FlightModelMain) (super.FM)).AP.way.next();
            target_ = ((FlightModelMain) (super.FM)).AP.way.curr().getTarget();
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
        if(((FlightModelMain) (super.FM)).AS.isMaster() && !typeDockableIsDocked())
        {
            Aircraft aircraft = War.getNearestFriend(this);
            if(aircraft instanceof TypeTankerDrogue)
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
        }
    }

    public void typeDockableAttemptDetach()
    {
        if(((FlightModelMain) (super.FM)).AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
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
        ((FlightModelMain) (super.FM)).EI.setEngineRunning();
        ((FlightModelMain) (super.FM)).CT.setGearAirborne();
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
        if ((FM.Gears.nearGround() || FM.Gears.onGround())
                && FM.EI.engines[0].getStage() != 6) {
          this.hierMesh().chunkVisible("HMask1_D0", false);
          this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
          this.hierMesh().chunkVisible("HMask1_D0",
          this.hierMesh().isChunkVisible("Pilot1_D0"));
          this.hierMesh().chunkVisible("HMask2_D0",
          this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
        if((super.FM instanceof RealFlightModel) && ((RealFlightModel)super.FM).isRealMode() || !flag || !(super.FM instanceof Pilot))
            return;
        if(flag && ((FlightModelMain) (super.FM)).AP.way.curr().Action == 3 && typeDockableIsDocked() && Math.abs(((FlightModelMain) (((SndAircraft) ((Aircraft)queen_)).FM)).Or.getKren()) < 3F)
            if(super.FM.isPlayers())
            {
                if((super.FM instanceof RealFlightModel) && !((RealFlightModel)super.FM).isRealMode())
                {
                    typeDockableAttemptDetach();
                    ((Maneuver)super.FM).set_maneuver(22);
                    ((Maneuver)super.FM).setCheckStrike(false);
                    ((FlightModelMain) (super.FM)).Vwld.z -= 5D;
                    dtime = Time.current();
                }
            } else
            {
                typeDockableAttemptDetach();
                ((Maneuver)super.FM).set_maneuver(22);
                ((Maneuver)super.FM).setCheckStrike(false);
                ((FlightModelMain) (super.FM)).Vwld.z -= 5D;
                dtime = Time.current();
            }
    }
    
    public void doEjectCatapultBack() {
        new MsgAction(false, this) {

            public void doAction(Object paramObject) {
                Aircraft localAircraft = (Aircraft) paramObject;
                if (Actor.isValid(localAircraft)) {
                    Loc localLoc1 = new Loc();
                    Loc localLoc2 = new Loc();
                    Vector3d localVector3d = new Vector3d(0.0, 0.0, 20.0);
                    HookNamed localHookNamed = new HookNamed(localAircraft,
                            "_ExternalSeat01");
                    localAircraft.pos.getAbs(localLoc2);
                    localHookNamed.computePos(localAircraft, localLoc2,
                            localLoc1);
                    localLoc1.transform(localVector3d);
                    localVector3d.x += localAircraft.FM.Vwld.x;
                    localVector3d.y += localAircraft.FM.Vwld.y;
                    localVector3d.z += localAircraft.FM.Vwld.z;
                    new EjectionSeat(6, localLoc1, localVector3d, localAircraft);
                }
            }
        };
        this.hierMesh().chunkVisible("Seat_D0", false);
    }

    public void doEjectCatapultFront() {
        new MsgAction(false, this) {

            public void doAction(Object paramObject) {
                Aircraft localAircraft = (Aircraft) paramObject;
                if (Actor.isValid(localAircraft)) {
                    Loc localLoc1 = new Loc();
                    Loc localLoc2 = new Loc();
                    Vector3d localVector3d = new Vector3d(0.0, 0.0, 20.0);
                    HookNamed localHookNamed = new HookNamed(localAircraft,
                            "_ExternalSeat02");
                    localAircraft.pos.getAbs(localLoc2);
                    localHookNamed.computePos(localAircraft, localLoc2,
                            localLoc1);
                    localLoc1.transform(localVector3d);
                    localVector3d.x += localAircraft.FM.Vwld.x;
                    localVector3d.y += localAircraft.FM.Vwld.y;
                    localVector3d.z += localAircraft.FM.Vwld.z;
                    new EjectionSeat(6, localLoc1, localVector3d, localAircraft);
                }
            }
        };
        this.hierMesh().chunkVisible("Seat2_D0", false);
    }
    
    private void bailout() {
        if (overrideBailout) {
            if (FM.AS.astateBailoutStep >= 0 && FM.AS.astateBailoutStep < 2) {
                if (FM.CT.cockpitDoorControl > 0.5F
                        && FM.CT.getCockpitDoor() > 0.5F) {
                    FM.AS.astateBailoutStep = (byte) 11;
                    doRemoveBlisters();
                } else {
                    FM.AS.astateBailoutStep = (byte) 2;
                }
            } else if (FM.AS.astateBailoutStep >= 2
                    && FM.AS.astateBailoutStep <= 3) {
                switch (FM.AS.astateBailoutStep) {
                    case 2:
                        if (FM.CT.cockpitDoorControl >= 0.5F) {
                            break;
                        }
                        doRemoveBlister1();
                        break;
                    case 3:
                        doRemoveBlisters();
                        lTimeNextEject = Time.current() + 1000;
                        break;
                }
                if (FM.AS.isMaster()) {
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                }
                FM.AS.astateBailoutStep = (byte) (FM.AS.astateBailoutStep + 1);
                if (FM.AS.astateBailoutStep == 4) {
                    FM.AS.astateBailoutStep = (byte) 11;
                }
            } else if (FM.AS.astateBailoutStep >= 11 && FM.AS.astateBailoutStep <= 19) {
                int i = FM.AS.astateBailoutStep;
                if (FM.AS.isMaster()) {
                    FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
                }
                FM.AS.astateBailoutStep = (byte) (FM.AS.astateBailoutStep + 1);
                if (FM instanceof Maneuver && ((Maneuver) FM).get_maneuver() != 44) {
                    World.cur();
                    if (FM.AS.actor != World.getPlayerAircraft()) {
                        ((Maneuver) FM).set_maneuver(44);
                    }
                }
                
                if (FM.AS.astatePilotStates[i - 11] < 99) {
                    if (i == 11) {
                        this.doRemoveBodyFromPlane(2);
                        doEjectCatapultFront();
                        lTimeNextEject = Time.current() + 1000;
                    } else if (i == 12) {
                        this.doRemoveBodyFromPlane(1);
                        doEjectCatapultBack();
                        FM.AS.astateBailoutStep = (byte) 51;
                        FM.setTakenMortalDamage(true, null);
                        FM.CT.WeaponControl[0] = false;
                        FM.CT.WeaponControl[1] = false;
                        FM.AS.astateBailoutStep = (byte) -1;
                        overrideBailout = false;
                        FM.AS.bIsAboutToBailout = true;
                        ejectComplete = true;
                    }
                    FM.AS.astatePilotStates[(i - 11)] = 99;
                } else {
                    EventLog.type("astatePilotStates[" + (i-11) + "]=" + FM.AS.astatePilotStates[i - 11]);
                }                    
            }
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        default:
            break;

        case 0: // '\0'
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1: // '\001'
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            break;
        }
    }
    
    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33: // '!'
            return super.cutFM(34, j, actor);

        case 36: // '$'
            return super.cutFM(37, j, actor);

        case 11: // '\013'
            cutFM(17, j, actor);
            super.FM.cut(17, j, actor);
            cutFM(18, j, actor);
            super.FM.cut(18, j, actor);
            return super.cutFM(i, j, actor);
        }
        return super.cutFM(i, j, actor);
    }
    
    private final void doRemoveBlister1() {
        if (this.hierMesh().chunkFindCheck("Blister1_D0") != -1
                && FM.AS.getPilotHealth(0) > 0.0F) {
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            localWreckage.collide(false);
            Vector3d localVector3d = new Vector3d();
            localVector3d.set(FM.Vwld);
            localWreckage.setSpeed(localVector3d);
        }
    }
    
    private final void doRemoveBlisters() {
        for (int i = 2; i < 10; i++) {
            if (this.hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1
                    && FM.AS.getPilotHealth(i - 1) > 0.0F) {
                this.hierMesh().hideSubTrees("Blister" + i + "_D0");
                Wreckage localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister" + i + "_D0"));
                localWreckage.collide(false);
                Vector3d localVector3d = new Vector3d();
                localVector3d.set(FM.Vwld);
                localWreckage.setSpeed(localVector3d);
            }
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
    private float arrestor;
    public static boolean bChangedPit = false;
    private boolean overrideBailout;
    private boolean ejectComplete;
    private long lTimeNextEject = 0;

    static 
    {
    	Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-100");
        Property.set(class1, "meshName", "3DO/Plane/F-100F(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1957.9F);
        Property.set(class1, "yearExpired", 1985.3F);
        Property.set(class1, "FlightModel", "FlightModels/F-100F.fmd:F100D");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitF_100F.class
        });
        Property.set(class1, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
                0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 
                9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 
                3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 
                3, 3, 3, 3, 9, 9, 3, 3, 3, 3, 
                3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 
                2, 2, 2, 9, 9, 2, 2, 2, 2, 2, 
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 
                2, 2, 2
            });
            Aircraft.weaponHooksRegister(class1, new String[] {
                    "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", 
                    "_ExternalDev07", "_ExternalDev08", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", 
                    "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08","_ExternalDev09", "_ExternalDev10", 
                    "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalDev11", "_ExternalDev12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", 
                    "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalDev13", "_ExternalDev14", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", 
                    "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalDev15", "_ExternalDev16", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", 
                    "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", 
                    "_ExternalRock27", "_ExternalRock28", "_ExternalRock28"
                });
        }
}