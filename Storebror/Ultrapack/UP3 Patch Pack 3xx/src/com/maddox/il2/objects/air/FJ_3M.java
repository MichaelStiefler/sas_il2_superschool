package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.War;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class FJ_3M extends F_86F
		implements TypeDockable, TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit {

	public FJ_3M() {
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
		bToFire = false;
		guidedMissileUtils = new GuidedMissileUtils(this);
		arrestor = 0.0F;
	}

	public GuidedMissileUtils getGuidedMissileUtils() {
		return this.guidedMissileUtils;
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
		long curTime = Time.current();
		if (curTime - lastCommonThreatActive > intervalCommonThreat) {
			lastCommonThreatActive = curTime;
			doDealCommonThreat();
		}
	}

	public void setRadarLockThreatActive() {
		long curTime = Time.current();
		if (curTime - lastRadarLockThreatActive > intervalRadarLockThreat) {
			lastRadarLockThreatActive = curTime;
			doDealRadarLockThreat();
		}
	}

	public void setMissileLaunchThreatActive() {
		long curTime = Time.current();
		if (curTime - lastMissileLaunchThreatActive > intervalMissileLaunchThreat) {
			lastMissileLaunchThreatActive = curTime;
			doDealMissileLaunchThreat();
		}
	}

	private void doDealCommonThreat() {
	}

	private void doDealRadarLockThreat() {
	}

	private void doDealMissileLaunchThreat() {
	}

	public void getGFactors(TypeGSuit.GFactors theGFactors) {
		theGFactors.setGFactors(NEG_G_TOLERANCE_FACTOR, NEG_G_TIME_FACTOR, NEG_G_RECOVERY_FACTOR,
				POS_G_TOLERANCE_FACTOR, POS_G_TIME_FACTOR, POS_G_RECOVERY_FACTOR);
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.guidedMissileUtils.onAircraftLoaded();
	}

	public void update(float f) {
		this.guidedMissileUtils.update();
		if (bNeedSetup)
			checkAsDrone();
		int i = aircIndex();
		if (this.FM instanceof Maneuver)
			if (typeDockableIsDocked()) {
				if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
					((Maneuver) this.FM).unblock();
					((Maneuver) this.FM).set_maneuver(48);
					for (int j = 0; j < i; j++)
						((Maneuver) this.FM).push(48);

					if (this.FM.AP.way.curr().Action != 3)
						this.FM.AP.way.setCur((((Aircraft) queen_)).FM.AP.way.Cur());
					((Pilot) this.FM).setDumbTime(3000L);
				}
				if (this.FM.M.fuel < this.FM.M.maxFuel)
					this.FM.M.fuel += 20F * f;
			} else if (!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) {
				if (this.FM.CT.GearControl == 0.0F && this.FM.EI.engines[0].getStage() == 0)
					this.FM.EI.setEngineRunning();
				if (dtime > 0L && ((Maneuver) this.FM).Group != null) {
					((Maneuver) this.FM).Group.leaderGroup = null;
					((Maneuver) this.FM).set_maneuver(22);
					((Pilot) this.FM).setDumbTime(3000L);
					if (Time.current() > dtime + 3000L) {
						dtime = -1L;
						((Maneuver) this.FM).clear_stack();
						((Maneuver) this.FM).set_maneuver(0);
						((Pilot) this.FM).setDumbTime(0L);
					}
				} else if (this.FM.AP.way.curr().Action == 0) {
					Maneuver maneuver = (Maneuver) this.FM;
					if (maneuver.Group != null && maneuver.Group.airc[0] == this && maneuver.Group.clientGroup != null)
						maneuver.Group.setGroupTask(2);
				}
			}
		if (this.FM.CT.getArrestor() > 0.2F)
			if (this.FM.Gears.arrestorVAngle != 0.0F) {
				float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
				arrestor = 0.8F * arrestor + 0.2F * f1;
				moveArrestorHook(arrestor);
			} else {
				float f2 = (-33F * this.FM.Gears.arrestorVSink) / 57F;
				if (f2 < 0.0F && this.FM.getSpeedKMH() > 60F)
					Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff",
							0.1F);
				if (f2 > 0.0F && this.FM.CT.getArrestor() < 0.95F)
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
	}

	public void msgCollisionRequest(Actor actor, boolean aflag[]) {
		super.msgCollisionRequest(actor, aflag);
		if (queen_last != null && queen_last == actor && (queen_time == 0L || Time.current() < queen_time + 5000L))
			aflag[0] = false;
		else
			aflag[0] = true;
	}

	public void missionStarting() {
		checkAsDrone();
	}

	private void checkAsDrone() {
		if (target_ == null) {
			if (this.FM.AP.way.curr().getTarget() == null)
				this.FM.AP.way.next();
			target_ = this.FM.AP.way.curr().getTarget();
			if (Actor.isValid(target_) && (target_ instanceof Wing)) {
				Wing wing = (Wing) target_;
				int i = aircIndex();
				if (Actor.isValid(wing.airc[i / 2]))
					target_ = wing.airc[i / 2];
				else
					target_ = null;
			}
		}
		if (Actor.isValid(target_) && (target_ instanceof TypeTankerDrogue)) {
			queen_last = target_;
			queen_time = Time.current();
			if (isNetMaster())
				((TypeDockable) target_).typeDockableRequestAttach(this, aircIndex() % 2, true);
		}
		bNeedSetup = false;
		target_ = null;
	}

	public int typeDockableGetDockport() {
		if (typeDockableIsDocked())
			return dockport_;
		else
			return -1;
	}

	public Actor typeDockableGetQueen() {
		return queen_;
	}

	public boolean typeDockableIsDocked() {
		return Actor.isValid(queen_);
	}

	public void typeDockableAttemptAttach() {
		if (this.FM.AS.isMaster() && !typeDockableIsDocked()) {
			Aircraft aircraft = War.getNearestFriend(this);
			if (aircraft instanceof TypeTankerDrogue)
				((TypeDockable) aircraft).typeDockableRequestAttach(this);
		}
	}

	public void typeDockableAttemptDetach() {
		if (this.FM.AS.isMaster() && typeDockableIsDocked() && Actor.isValid(queen_))
			((TypeDockable) queen_).typeDockableRequestDetach(this);
	}

	public void typeDockableRequestAttach(Actor actor) {
	}

	public void typeDockableRequestDetach(Actor actor) {
	}

	public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
	}

	public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
	}

	public void typeDockableDoAttachToDrone(Actor actor, int i) {
	}

	public void typeDockableDoDetachFromDrone(int i) {
	}

	public void typeDockableDoAttachToQueen(Actor actor, int i) {
		queen_ = actor;
		dockport_ = i;
		queen_last = queen_;
		queen_time = 0L;
		this.FM.EI.setEngineRunning();
		this.FM.CT.setGearAirborne();
		moveGear(0.0F);
		FlightModel queenFM = ((Aircraft) queen_).FM;
		if (aircIndex() == 0 && (this.FM instanceof Maneuver) && (queenFM instanceof Maneuver)) {
			Maneuver maneuver = (Maneuver) queenFM;
			Maneuver maneuver1 = (Maneuver) this.FM;
			if (maneuver.Group != null && maneuver1.Group != null
					&& maneuver1.Group.numInGroup(this) == maneuver1.Group.nOfAirc - 1) {
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

	public void typeDockableDoDetachFromQueen(int i) {
		if (dockport_ == i) {
			queen_last = queen_;
			queen_time = Time.current();
			queen_ = null;
			dockport_ = 0;
		}
	}

	public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		if (typeDockableIsDocked()) {
			netmsgguaranted.writeByte(1);
			com.maddox.il2.engine.ActorNet actornet = null;
			if (Actor.isValid(queen_)) {
				actornet = queen_.net;
				if (actornet.countNoMirrors() > 0)
					actornet = null;
			}
			netmsgguaranted.writeByte(dockport_);
			netmsgguaranted.writeNetObj(actornet);
		} else {
			netmsgguaranted.writeByte(0);
		}
	}

	public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		if (netmsginput.readByte() == 1) {
			dockport_ = netmsginput.readByte();
			NetObj netobj = netmsginput.readNetObj();
			if (netobj != null) {
				Actor actor = (Actor) netobj.superObj();
				((TypeDockable) actor).typeDockableDoAttachToDrone(this, dockport_);
			}
		}
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode() || !flag
				|| !(this.FM instanceof Pilot))
			return;
		if (flag && this.FM.AP.way.curr().Action == 3 && typeDockableIsDocked()
				&& Math.abs(((FlightModelMain) (((SndAircraft) ((Aircraft) queen_)).FM)).Or.getKren()) < 3F)
			if (this.FM.isPlayers()) {
				if ((this.FM instanceof RealFlightModel) && !((RealFlightModel) this.FM).isRealMode()) {
					typeDockableAttemptDetach();
					((Maneuver) this.FM).set_maneuver(22);
					((Maneuver) this.FM).setCheckStrike(false);
					this.FM.Vwld.z -= 5D;
					dtime = Time.current();
				}
			} else {
				typeDockableAttemptDetach();
				((Maneuver) this.FM).set_maneuver(22);
				((Maneuver) this.FM).setCheckStrike(false);
				this.FM.Vwld.z -= 5D;
				dtime = Time.current();
			}
	}

	protected void moveWingFold(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("WingLFold", 0.0F * f, 0.0F * f, -22F * f);
		hiermesh.chunkSetAngles("WingRFold", 0.0F * f, 0.0F * f, -22F * f);
		hiermesh.chunkSetAngles("WingLOut_D0", 0.0F * f, 90F * f, -22F * f);
		hiermesh.chunkSetAngles("WingROut_D0", 0.0F * f, -90F * f, -22F * f);
	}

	public void moveWingFold(float f) {
		if (f < 0.001F) {
			setGunPodsOn(true);
			hideWingWeapons(false);
		} else {
			setGunPodsOn(false);
			this.FM.CT.WeaponControl[0] = false;
			hideWingWeapons(true);
		}
		moveWingFold(hierMesh(), f);
	}

	public void moveArrestorHook(float f) {
		hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 45F * f, 0.0F);
		resetYPRmodifier();
		Aircraft.xyz[2] = 0.1385F * f;
		arrestor = f;
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
		case 19:
			this.FM.CT.bHasArrestorControl = false;
			break;
		}
		return super.cutFM(i, j, actor);
	}

	public void checkHydraulicStatus() {
		if (this.FM.EI.engines[0].getStage() < 6 && this.FM.Gears.nOfGearsOnGr > 0) {
			this.hasHydraulicPressure = false;
			this.FM.CT.bHasAileronControl = false;
			this.FM.CT.bHasElevatorControl = false;
			this.FM.CT.AirBrakeControl = 1.0F;
			this.FM.CT.bHasArrestorControl = false;
		} else if (!this.hasHydraulicPressure) {
			this.hasHydraulicPressure = true;
			this.FM.CT.bHasAileronControl = true;
			this.FM.CT.bHasElevatorControl = true;
			this.FM.CT.bHasAirBrakeControl = true;
			this.FM.CT.bHasArrestorControl = true;
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
	private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
	private static final float NEG_G_TIME_FACTOR = 1.5F;
	private static final float NEG_G_RECOVERY_FACTOR = 1F;
	private static final float POS_G_TOLERANCE_FACTOR = 2F;
	private static final float POS_G_TIME_FACTOR = 2F;
	private static final float POS_G_RECOVERY_FACTOR = 2F;
	public boolean bToFire;
	private float arrestor;

	static {
		Class localClass = FJ_3M.class;
		new NetAircraft.SPAWN(localClass);
		Property.set(localClass, "iconFar_shortClassName", "FJ-3M");
		Property.set(localClass, "meshName", "3DO/Plane/FJ_3M(Multi1)/hier.him");
		Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
		Property.set(localClass, "yearService", 1949.9F);
		Property.set(localClass, "yearExpired", 1960.3F);
		Property.set(localClass, "FlightModel", "FlightModels/FJ-3M.fmd");
		Property.set(localClass, "cockpitClass", new Class[] { CockpitF_86Flate.class });
		Property.set(localClass, "LOSElevation", 0.725F);
		Aircraft.weaponTriggersRegister(localClass, new int[] { 0, 0, 0, 0, 9, 9, 9, 9, 9, 3, 3, 9, 3, 3, 9, 2, 2, 9, 2,
				2, 9, 9, 9, 9, 9, 3, 3, 9, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
		Aircraft.weaponHooksRegister(localClass,
				new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02",
						"_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb01",
						"_ExternalDev06", "_ExternalBomb02", "_ExternalBomb02", "_ExternalDev07", "_ExternalRock01",
						"_ExternalRock01", "_ExternalDev08", "_ExternalRock02", "_ExternalRock02", "_ExternalDev09",
						"_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalBomb03",
						"_ExternalBomb03", "_ExternalDev14", "_ExternalBomb04", "_ExternalBomb04", "_ExternalDev17",
						"_ExternalDev18", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06",
						"_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10" });
	}
}
