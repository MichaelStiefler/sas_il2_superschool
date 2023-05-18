package com.maddox.il2.objects.air;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Orientation;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.Missile;
import com.maddox.il2.objects.weapons.RocketBombGun;
import com.maddox.il2.objects.weapons.RocketGunAGM65;
import com.maddox.il2.objects.weapons.RocketGunAGM65Ds;
import com.maddox.il2.objects.weapons.RocketGunAIM9L;
import com.maddox.il2.objects.weapons.RocketGunFlare;
import com.maddox.rts.CLASS;
import com.maddox.rts.Finger;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.util.HashMapInt;

public class F_18D extends F_18 implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, TypeStormovikArmored, TypeDockable {

	public F_18D() {
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
		windFoldValue = 0.0F;
		IR = false;
		missilesList = new ArrayList();
		backfireList = new ArrayList();
		tX4Prev = 0L;
	}

	private void checkAmmo() {
		missilesList.clear();
		for (int i = 0; i < FM.CT.Weapons.length; i++) {
			if (FM.CT.Weapons[i] == null)
				continue;
			for (int j = 0; j < FM.CT.Weapons[i].length; j++) {
				if (!FM.CT.Weapons[i][j].haveBullets())
					continue;
				if (FM.CT.Weapons[i][j] instanceof RocketGunAGM65Ds) {
					IR = true;
					//missilesList.add(FM.CT.Weapons[i][j]);
				}
				if (FM.CT.Weapons[i][j] instanceof RocketGunFlare) {
					backfireList.add(FM.CT.Weapons[i][j]);
				} else {
					missilesList.add(FM.CT.Weapons[i][j]);
				}
				if (FM.CT.Weapons[i][j] instanceof RocketBombGun) {
					IR = true;
					//missilesList.add(FM.CT.Weapons[i][j]);
				}
			}
		}

	}

	public void launchMsl() {
		if (missilesList.isEmpty()) {
			return;
		} else {
			((RocketGunAGM65) missilesList.remove(0)).shots(1);
			return;
		}
	}

	public void launchbmb() {
		if (missilesList.isEmpty()) {
			return;
		} else {
			((RocketBombGun) missilesList.remove(0)).shots(1);
			return;
		}
	}

	public void backFire() {
		if (backfireList.isEmpty()) {
			return;
		} else {
			((RocketGunFlare) backfireList.remove(0)).shots(3);
			return;
		}
	}

	public float getFlowRate() {
		return FlowRate;
	}

	public float getFuelReserve() {
		return FuelReserve;
	}

	public void moveArrestorHook(float f) {
		hierMesh().chunkSetAngles("Tailhook_D0", 0.0F, 0.0F, 70F * f);
	}

	public void moveRefuel(float f) {
		hierMesh().chunkSetAngles("fueldoor_D0", 0.0F, Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, 35F), 0.0F);
		hierMesh().chunkSetAngles("fueldoor2_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.0F, 0.2F, 0.0F, 90F));
		hierMesh().chunkSetAngles("fueldoor3_D0", 0.0F, 0.0F, Aircraft.cvt(f, 0.8F, 1.0F, 0.0F, -90F));
		hierMesh().chunkSetAngles("rod2", 0.0F, Aircraft.cvt(f, 0.2F, 0.8F, 0.0F, -85F), 0.0F);
	}

	protected void moveWingFold(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 92F), 0.0F);
		hiermesh.chunkSetAngles("WingROut_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -92F), 0.0F);
		moveFlap(FM.CT.getFlap());
	}

	public long getChaffDeployed() {
		if (hasChaff)
			return lastChaffDeployed;
		else
			return 0L;
	}

	public long getFlareDeployed() {
		if (hasFlare)
			return lastFlareDeployed;
		else
			return 0L;
	}

	public void setCommonThreatActive() {
		long l = Time.current();
		if (l - lastCommonThreatActive > intervalCommonThreat) {
			lastCommonThreatActive = l;
			doDealCommonThreat();
		}
	}

	public void setRadarLockThreatActive() {
		long l = Time.current();
		if (l - lastRadarLockThreatActive > intervalRadarLockThreat) {
			lastRadarLockThreatActive = l;
			doDealRadarLockThreat();
		}
	}

	public void setMissileLaunchThreatActive() {
		long l = Time.current();
		if (l - lastMissileLaunchThreatActive > intervalMissileLaunchThreat) {
			lastMissileLaunchThreatActive = l;
			doDealMissileLaunchThreat();
		}
	}

	private void doDealCommonThreat() {
	}

	private void doDealRadarLockThreat() {
	}

	private void doDealMissileLaunchThreat() {
	}

	public GuidedMissileUtils getGuidedMissileUtils() {
		return guidedMissileUtils;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		checkAmmo();
		guidedMissileUtils.onAircraftLoaded();
		FM.Skill = 3;
		FM.CT.bHasDragChuteControl = true;
		FM.turret[0].bIsAIControlled = false;
		bulletEmitters = new BulletEmitter[weponHookArray.length];
		for (int i = 0; i < weponHookArray.length; i++)
			bulletEmitters[i] = getBulletEmitterByHookName(weponHookArray[i]);
	}

	public void update(float f) {
		if (bNeedSetup)
			checkAsDrone();
		guidedMissileUtils.update();
		int i = aircIndex();
    	if(super.FM instanceof Maneuver)
            if(typeDockableIsDocked())
            {
                if(((FlightModelMain) (super.FM)).CT.getRefuel() < 0.90F)
                    typeDockableAttemptDetach();
                else
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
                }
            } else
            if(!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode())
            {
                if(((FlightModelMain) (super.FM)).CT.GearControl == 0.0F && ((FlightModelMain) (super.FM)).EI.engines[0].getStage() == 0)
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
		if (FM.CT.getArrestor() > 0.2F)
			if (FM.Gears.arrestorVAngle != 0.0F) {
				float f1 = Aircraft.cvt(FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
				arrestor = 0.8F * arrestor + 0.2F * f1;
				moveArrestorHook(arrestor);
			} else {
				float f2 = (-33F * FM.Gears.arrestorVSink) / 57F;
				if (f2 < 0.0F && FM.getSpeedKMH() > 60F)
					Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
				if (f2 > 0.0F && FM.CT.getArrestor() < 0.95F)
					f2 = 0.0F;
				if (f2 > 0.2F)
					f2 = 0.2F;
				if (f2 > 0.0F)
					arrestor = 0.7F * arrestor + 0.3F * (arrestor + f2);
				else
					arrestor = 0.3F * arrestor + 0.7F * (arrestor + f2);
				if (arrestor < 0.0F)
					arrestor = 0.0F;
				else if (arrestor > 1.0F)
					arrestor = 1.0F;
				moveArrestorHook(arrestor);
			}
		super.update(f);
		if(this.backfire)
			backFire();
		if((!(super.FM instanceof RealFlightModel) || !((RealFlightModel)super.FM).isRealMode()) && (FM instanceof Maneuver) && ((Maneuver)FM).get_task() == 7 && !((FlightModelMain) (super.FM)).AP.way.isLanding()) //TODO straft
        {           
        	if(missilesList.isEmpty() && !((Maneuver)super.FM).hasBombs())
        	{        	
        	Pilot pilot = (Pilot)FM;           
            Vector3d vector3d = new Vector3d();
            getSpeed(vector3d);
            Point3d point3d = new Point3d();
            pos.getAbs(point3d);
            float f1 = (float)((double)FM.getAltitude() - World.land().HQ(point3d.x, point3d.y));
            if(f1 < 55F && vector3d.z < 0.0D){
                vector3d.z = 0.0D;} else               	
            if(pilot != null && isAlive(pilot.target_ground))
            {
                Point3d point3d2 = new Point3d();
                pilot.target_ground.pos.getAbs(point3d2);
                pilot.set_maneuver(43);
                if(pos.getAbsPoint().distance(point3d2) < 2000D)
                {
                    point3d2.sub(FM.Loc);
                    FM.Or.transformInv(point3d2);
                    ((FlightModelMain) (super.FM)).CT.PowerControl = 0.55F;                                                                             	
                }
            }
            setSpeed(vector3d);
        } else
        	if(!missilesList.isEmpty() && Time.current() > tX4Prev + 500L + (IR ? 10000L : 0L))
            {       		
        		Pilot pilot = (Pilot)FM;
                if(pilot.get_maneuver() == 43 && pilot.target_ground != null)
                {
                    Point3d point3d = new Point3d();
                    pilot.target_ground.pos.getAbs(point3d);
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
            if(aircraft instanceof TypeTankerDrogue && ((FlightModelMain) (super.FM)).CT.getRefuel() > 0.95F)
                ((TypeDockable)aircraft).typeDockableRequestAttach(this);
      /*          ((FlightModelMain) (super.FM)).CT.RefuelControl = 1F;
            } else  
            {
                ((FlightModelMain) (super.FM)).CT.RefuelControl = 0F;
            }	*/
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
        com.maddox.il2.fm.FlightModel flightmodel = ((SndAircraft) ((Aircraft)queen_)).FM;
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
            com.maddox.il2.engine.ActorNet actornet = null;
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

	public void rareAction(float f, boolean flag) {		
		super.rareAction(f, flag);
		
	}

	public void updateHook() {
		for (int i = 0; i < weponHookArray.length; i++)
			try {
				if (bulletEmitters[i] instanceof RocketGunAIM9L)
					((RocketGunAIM9L) bulletEmitters[i]).updateHook(weponHookArray[i]);
			} catch (Exception exception) {
			}

	}

	public void moveWingFold(float f) {
		moveWingFold(hierMesh(), f);
		super.moveWingFold(f);
		if (windFoldValue != f) {
			windFoldValue = f;
			super.needUpdateHook = true;
		}
	}

	static String weponHookArray[] = { "_CANNON01", "_Extmis05", "_Extmis06", "_Extmis07", "_Extmis08", "_Extmis10", "_Extmis11", "_Extmis12", "_Extmis13", "_ExtDev05", "_ExtTank03", "_ExtDev01", "_ExtDev02", "_ExtDev03", "_ExtDev04", "_ExtTank01",
			"_ExtTank02", "_Extmis14", "_Extmis15", "_Extmis16", "_Extmis17", "_Extmis01", "_Extmis02", "_Extmis03", "_Extmis04", "_Extmis18", "_Extmis19", "_Extmis20", "_Extmis21", "_ExtDev06", "_ExtDev07", "_ExtDev08", "_ExtDev09", "_ExtBomb01",
			"_ExtBomb02", "_ExtBomb03", "_ExtBomb04", "_ExtBomb05", "_ExtBomb06", "_ExtBomb07", "_ExtBomb08", "_ExtPlchd1", "_Extmis22", "_Extmis23", "_Extmis24", "_Extmis25", "_Extmis26", "_Extmis27", "_Extmis28", "_Extmis29", "_Extmis30", "_Extmis31",
			"_Extmis32", "_Extmis33" };
	BulletEmitter bulletEmitters[];
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
	public static float FlowRate = 10F;
	public static float FuelReserve = 1500F;
	public boolean bToFire;
	private float arrestor;
	float windFoldValue;
	private long tX4Prev;
	private boolean IR;
	private ArrayList missilesList;
	private ArrayList backfireList;
	private Actor queen_last;
	private long queen_time;
	private boolean bNeedSetup;
	private long dtime;
	private Actor target_;
	private Actor queen_;
	private int dockport_;

	static {
		Class class1 = CLASS.THIS();
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "F-18D");
		Property.set(class1, "meshName", "3DO/Plane/F-18D/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "yearService", 1989F);
		Property.set(class1, "yearExpired", 2050F);
		Property.set(class1, "FlightModel", "FlightModels/F-18D.fmd:F18_FM");
		Property.set(class1, "cockpitClass", new Class[] { CockpitF_18C.class, CockpitF18FLIR.class });
		Property.set(class1, "LOSElevation", 0.965F);
		Aircraft.weaponTriggersRegister(class1, new int[] { 0, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 9, 3, 3, 7, 7, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
				2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 8, 8 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_Extmis05", "_Extmis06", "_Extmis07", "_Extmis08", "_Extmis10", "_Extmis11", "_Extmis12", "_Extmis13", "_ExtDev05", "_ExtTank03", "_ExtDev01", "_ExtDev02", "_ExtDev03",
				"_ExtDev04", "_ExtTank01", "_ExtTank02", "_Extmis14", "_Extmis15", "_Extmis16", "_Extmis17", "_Extmis01", "_Extmis02", "_Extmis03", "_Extmis04", "_Extmis18", "_Extmis19", "_Extmis20", "_Extmis21", "_ExtDev06", "_ExtDev07", "_ExtDev08",
				"_ExtDev09", "_ExtBomb01", "_ExtBomb02", "_ExtBomb03", "_ExtBomb04", "_ExtBomb05", "_ExtBomb06", "_ExtBomb07", "_ExtBomb08", "_ExtPlchd1", "_Extmis22", "_Extmis23", "_Extmis24", "_Extmis25", "_Extmis26", "_Extmis27", "_Extmis28",
				"_Extmis29", "_Extmis30", "_Extmis31", "_Extmis32", "_Extmis33", "_Flare01", "_Flare02", "_Extmis34", "_Extmis35", "_Extmis36", "_Extmis37", "_Extmis38", "_Extmis39", "_Extmis40", "_Extmis41", "_Extmis42", "_Extmis43", "_Extmis44",
				"_Extmis45", "_Extmis46", "_Extmis47", "_Extmis48", "_Extmis49", "_ExtBomb09", "_ExtBomb10", "_ExtBomb11", "_ExtBomb12", "_ExtBomb13", "_ExtBomb14", "_ExtBomb15", "_ExtBomb16", "_ExtBomb17", "_ExtBomb18", "_ExtBomb19", "_ExtBomb20",
				"_Chaff01", "_Chaff02" });
		String s = "undefined";
		try {
			ArrayList arraylist = new ArrayList();
			Property.set(class1, "weaponsList", arraylist);
			HashMapInt hashmapint = new HashMapInt();
			Property.set(class1, "weaponsMap", hashmapint);
			byte byte0 = 86;
			Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
			s = "Default";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
			a_lweaponslot[85] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 6xAIM-7";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
			a_lweaponslot[85] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 6xAIM-7 +DT";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
			a_lweaponslot[85] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 4xAIM-7 +2xDT";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAIM7M", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[84] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
			a_lweaponslot[85] = new Aircraft._WeaponSlot(8, "BombGunChaffF", 20);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xAIM-7 +3xDT";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = null;
			a_lweaponslot[14] = null;
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[84] = new Aircraft._WeaponSlot(7, "BombGunChaffF", 20);
			a_lweaponslot[85] = new Aircraft._WeaponSlot(7, "BombGunChaffF", 20);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xAIM-7 +2xDT";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = null;
			a_lweaponslot[14] = null;
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 4xAIM-7 +3xDT";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 8xAIM-7";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[33] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[34] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[35] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);			
			s = "2xAIM-9 + 6xAIM-120 + 2xDT";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 10xAIM-120";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 10xAIM-120 + 1xDT";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[65] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[66] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[67] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[68] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[70] = new Aircraft._WeaponSlot(2, "RocketGunAIM120A", 1);
			a_lweaponslot[71] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "6xAIM-9 + 6xAIM-120";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[64] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[65] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[66] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[67] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[68] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[69] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[70] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[71] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "6xAIM-9 + 6xAIM-120 + 1xDT";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[64] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[65] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[66] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[67] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[68] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[69] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[70] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[71] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "6xAIM-9 + 4xAIM-120 + 2xAIM-7";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[64] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[65] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[66] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[67] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[68] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[69] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[70] = new Aircraft._WeaponSlot(5, "RocketGunAIM120A", 1);
			a_lweaponslot[71] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "6xAIM-9 + 2xAIM-7 + 2xDT";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "6xAIM-9 + 2xAIM-7 + 3xDT";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-7 + 4xMk82LGB + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 4xAIM-7 + 4xJDAM84";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(6, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "3xDT + 2xAGM65E + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xDT + 2xAGM65E + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xDT + 6xAGM65E + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "3xDT + 6xAGM65E + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xDT + 2xAGM65E + 2xAGM65D + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[46] = new Aircraft._WeaponSlot(6, "RocketGunAGM65L", 1);
			a_lweaponslot[47] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
			a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[50] = new Aircraft._WeaponSlot(6, "RocketGunAGM65L", 1);
			a_lweaponslot[51] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
			a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "3xDT + 2xAGM65E + 2xAGM65D + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[46] = new Aircraft._WeaponSlot(6, "RocketGunAGM65L", 1);
			a_lweaponslot[47] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
			a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[50] = new Aircraft._WeaponSlot(6, "RocketGunAGM65L", 1);
			a_lweaponslot[51] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
			a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAGM-84 + 2xAGM65E + 2xAGM65D + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
			a_lweaponslot[17] = new Aircraft._WeaponSlot(6, "RocketGunAGM84B", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(6, "RocketGunAGM84B", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[46] = new Aircraft._WeaponSlot(6, "RocketGunAGM65L", 1);
			a_lweaponslot[47] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
			a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[50] = new Aircraft._WeaponSlot(6, "RocketGunAGM65L", 1);
			a_lweaponslot[51] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
			a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xMk82LGB + 2xAGM65E + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xJDAM84 + 2xAGM65E + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xMk82LGB + 2xAGM65E + 1xDT + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xAIM-7 + 2xAGM65E + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[33] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[34] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[35] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAGM65E + 6xMk83 + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[72] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[77] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[78] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[79] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[80] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xAIM-7 + 2xZuni + 6xMk83";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[72] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[77] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[78] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[79] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[80] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAGM65E + 6xMk82Snakeyes + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(2, "RocketGunAGM65L", 1);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[72] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[77] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[78] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[79] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[80] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xZuni + 6xMk82Snakeyes + 2xAIM-9 + 2xAIM-7";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[72] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[77] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[78] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[79] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[80] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xZuni + 6xMk82Snakeyes + 2xAIM-9 + 2xAIM-7";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[42] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[44] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[50] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[52] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[72] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[77] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[78] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[79] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[80] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAGM65D + 2xAGM65E + 6xMk82Snakeyes + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[46] = new Aircraft._WeaponSlot(6, "RocketGunAGM65L", 1);
			a_lweaponslot[47] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
			a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[50] = new Aircraft._WeaponSlot(6, "RocketGunAGM65L", 1);
			a_lweaponslot[51] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
			a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[72] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[77] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[78] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[79] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[80] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk82SnakeEye", 1);
			a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAGM65D + 2xAGM65E + 6xMk83 + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MTer", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[46] = new Aircraft._WeaponSlot(6, "RocketGunAGM65L", 1);
			a_lweaponslot[47] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
			a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[50] = new Aircraft._WeaponSlot(6, "RocketGunAGM65L", 1);
			a_lweaponslot[51] = new Aircraft._WeaponSlot(6, "RocketGunNull", 1);
			a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunAGM65Ds", 1);
			a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[72] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[73] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[74] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[75] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[76] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[77] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[78] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[79] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[80] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[81] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[82] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
			a_lweaponslot[83] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xAIM-7 + 2xDT + 6xZuni";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[42] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[44] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[46] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[48] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[50] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[52] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xAIM-7 + 2xAGM-84 + 6xZuni";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[42] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[44] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[46] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[48] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[50] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[52] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xMk82LGB + 2xZuni + 1xDT + 2xAIM-9";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xZuni + 4xMk82LGB + 2xAIM-9 + 2xAIM-7 + 1xDT";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[42] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[44] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[50] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[52] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "4xJDAM84 + 2xZuni + 1 DT + 2xAIM-9 + 2xAIM-7";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twin", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[34] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[35] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[36] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[37] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[39] = new Aircraft._WeaponSlot(3, "BombGunNull", 1);
			a_lweaponslot[40] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xAIM-7 + 2xDT + 2xZuni";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 4xAIM-7 +2xAGM-84";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 6xAIM-7 +2xAGM-84";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[33] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[34] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[35] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xAIM-7 + 2xDT +2xAGM-84";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xAIM-7 + 3xDT +2xAGM-84";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(9, "RocketGunAIM7M", 1);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xAIM-7 + 4xAGM-84";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "6xAIM-9 + 2xAIM-7 + 2xAGM-84";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(2, "RocketGunAGM84B", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[56] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[57] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[58] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[59] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[60] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[61] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[62] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[63] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);			
			s = "2xAIM-9 + 2xAIM-7 + 2xJDAM84 + 6xZuni";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = null;
			a_lweaponslot[6] = null;
			a_lweaponslot[7] = null;
			a_lweaponslot[8] = null;
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18BTer", 1);
			a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[42] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[43] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[44] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[45] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[46] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[48] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[50] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[51] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[52] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[53] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 6xAIM-7 +2xJDAM84";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[33] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[34] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[35] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xAIM-7 +2xDT + 2xJDAM84";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xAIM-7 + 2xJDAM84 + 2xZuni";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xAIM-7 + 2xJDAM84 + 2xAGM-65";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunAGM65L", 1);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(4, "RocketGunAGM65L", 1);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "6xAIM-9 + 2xAIM-7 + 2xJDAM84";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xAIM-7 + 4xJDAM84";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[1] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[3] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "RocketGunJDAM84", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 4xAIM-7 +2xLGB-12";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[29] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
			a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "Pylon18Twinm", 1);
			a_lweaponslot[31] = null;
			a_lweaponslot[32] = null;
			a_lweaponslot[33] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[34] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[35] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[36] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[37] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[38] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[39] = new Aircraft._WeaponSlot(5, "RocketGunAIM7M", 1);
			a_lweaponslot[40] = new Aircraft._WeaponSlot(5, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 +2xDT + 2xLGB-12";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 +3xDT + 2xLGB-12";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[9] = new Aircraft._WeaponSlot(9, "Pylon18CT", 1);
			a_lweaponslot[10] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[15] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "FuelTankGun_Tank18C", 1);
			a_lweaponslot[17] = null;
			a_lweaponslot[18] = null;
			a_lweaponslot[19] = null;
			a_lweaponslot[20] = null;
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xLGB-12 + 2xZuni";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(1, "CannonRocketSimpleHYDRA", 4);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "Pylon_Zuni", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 2xLGB-12 + 2xAGM-65";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18M", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(4, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[25] = new Aircraft._WeaponSlot(4, "RocketGunAGM65L", 1);
			a_lweaponslot[26] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[27] = new Aircraft._WeaponSlot(4, "RocketGunAGM65L", 1);
			a_lweaponslot[28] = new Aircraft._WeaponSlot(4, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "6xAIM-9 + 2xLGB-12";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18MD", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[58] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[59] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[60] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[61] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[62] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
			s = "2xAIM-9 + 4xLGB-12";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunM61A1", 450);
			a_lweaponslot[2] = new Aircraft._WeaponSlot(9, "FLIRPOD", 1);
			a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "LAZERPOD", 1);
			a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[6] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[7] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[8] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[9] = null;
			a_lweaponslot[10] = null;
			a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "Pylon18T", 1);
			a_lweaponslot[15] = null;
			a_lweaponslot[16] = null;
			a_lweaponslot[17] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[18] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[19] = new Aircraft._WeaponSlot(3, "RocketGunNull", 1);
			a_lweaponslot[20] = new Aircraft._WeaponSlot(3, "BombGunMk82LGB", 1);
			a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[22] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[23] = new Aircraft._WeaponSlot(2, "RocketGunAIM9L", 1);
			a_lweaponslot[24] = new Aircraft._WeaponSlot(2, "RocketGunNull", 1);
			a_lweaponslot[41] = new Aircraft._WeaponSlot(10, "MGunNull", 10);
			a_lweaponslot[54] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			a_lweaponslot[55] = new Aircraft._WeaponSlot(7, "RocketGunFlare", 35);
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);			
			s = "none";
			a_lweaponslot = new Aircraft._WeaponSlot[byte0];
			a_lweaponslot[0] = null;
			arraylist.add(s);
			hashmapint.put(Finger.Int(s), a_lweaponslot);
		} catch (Exception exception) {
			System.out.println("Weapons Error in " + class1.getName() + " on Entry " + s);
		}
	}
}