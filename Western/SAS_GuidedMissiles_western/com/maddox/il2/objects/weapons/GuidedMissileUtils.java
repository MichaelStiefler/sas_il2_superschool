// Source File Name: GuidedMissileUtils.java
// Author:	Storebror
// Edit:	western0221 on 21th/Feb./2019
package com.maddox.il2.objects.weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AutopilotAI;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.ai.ground.TgtFlak;
import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.ai.ground.TgtTank;
import com.maddox.il2.ai.ground.TgtTrain;
import com.maddox.il2.ai.ground.TgtVehicle;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeLaserDesignator;
import com.maddox.il2.objects.air.TypeSACLOS;
import com.maddox.il2.objects.air.TypeSemiRadar;
import com.maddox.il2.objects.air.TypeGroundRadar;
import com.maddox.il2.objects.air.TypeRadarWarningReceiver;
import com.maddox.il2.objects.bridges.Bridge;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.bridges.LongBridge;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.artillery.RocketryRocket;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Conversion;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class GuidedMissileUtils {

	private class MissileDataForPk {
		private int triggerNum;
		private float maxLaunchLoad;
		private float maxAngleToTarget;
		private float maxAngleFromTargetAft;
		private float minDist;
		private float optDist;
		private float maxDist;

		public int getTriggerNum() {
			return this.triggerNum;
		}
		public void setTriggerNum(int triggerNum) {
			this.triggerNum = triggerNum;
		}
		public float getMaxLaunchLoad() {
			return this.maxLaunchLoad;
		}
		public void setMaxLaunchLoad(float maxLaunchLoad) {
			this.maxLaunchLoad = maxLaunchLoad;
		}
		public float getMaxAngleToTarget() {
			return this.maxAngleToTarget;
		}
		public void setMaxAngleToTarget(float maxAngleToTarget) {
			this.maxAngleToTarget = maxAngleToTarget;
		}
		public float getMaxAngleFromTargetAft() {
			return this.maxAngleFromTargetAft;
		}
		public void setMaxAngleFromTargetAft(float maxAngleFromTargetAft) {
			this.maxAngleFromTargetAft = maxAngleFromTargetAft;
		}
		public float getMinDist() {
			return this.minDist;
		}
		public void setMinDist(float minDist) {
			this.minDist = minDist;
		}
		public float getOptDist() {
			return this.optDist;
		}
		public void setOptDist(float optDist) {
			this.optDist = optDist;
		}
		public float getMaxDist() {
			return this.maxDist;
		}
		public void setMaxDist(float maxDist) {
			this.maxDist = maxDist;
		}
		private MissileDataForPk() {
			this.triggerNum = 0;
			this.maxLaunchLoad = 99.9F;
			this.maxAngleToTarget = 99.9F;
			this.maxAngleFromTargetAft = 99.9F;
			this.minDist = 99.9F;
			this.optDist = 99.9F;
			this.maxDist = 99.9F;
		}
	}

	public static float angleActorBetween(Actor actorFrom, Actor actorTo) {
		float angleRetVal = 180.1F;
		double angleDoubleTemp = 0.0D;
		Loc angleActorLoc = new Loc();
		Point3d angleActorPos = new Point3d();
		Point3d angleTargetPos = new Point3d();
		Vector3d angleTargRayDir = new Vector3d();
		Vector3d angleNoseDir = new Vector3d();
		actorFrom.pos.getAbs(angleActorLoc);
		angleActorLoc.get(angleActorPos);
		actorTo.pos.getAbs(angleTargetPos);
		angleTargRayDir.sub(angleTargetPos, angleActorPos);
		angleDoubleTemp = angleTargRayDir.length();
		angleTargRayDir.scale(1.0D / angleDoubleTemp);
		angleNoseDir.set(1.0D, 0.0D, 0.0D);
		angleActorLoc.transform(angleNoseDir);
		angleDoubleTemp = angleNoseDir.dot(angleTargRayDir);
		angleRetVal = Geom.RAD2DEG((float) Math.acos(angleDoubleTemp));
		return angleRetVal;
	}

	public static float angleBetween(Actor actorFrom, Point3d pointTo) {
		float angleRetVal = 180.1F;
		if (actorFrom == null || !Actor.isValid(actorFrom) || pointTo == null) {
			return angleRetVal;
		}
		double angleDoubleTemp = 0.0D;
		Loc angleActorLoc = new Loc();
		Point3d angleActorPos = new Point3d();
		Vector3d angleTargRayDir = new Vector3d();
		Vector3d angleNoseDir = new Vector3d();
		actorFrom.pos.getAbs(angleActorLoc);
		angleActorLoc.get(angleActorPos);
		angleTargRayDir.sub(pointTo, angleActorPos);
		angleDoubleTemp = angleTargRayDir.length();
		angleTargRayDir.scale(1.0D / angleDoubleTemp);
		angleNoseDir.set(1.0D, 0.0D, 0.0D);
		angleActorLoc.transform(angleNoseDir);
		angleDoubleTemp = angleNoseDir.dot(angleTargRayDir);
		angleRetVal = Geom.RAD2DEG((float) Math.acos(angleDoubleTemp));
		return angleRetVal;
	}

	public static float angleBetween(Point3d pointFrom, Actor actorTo) {
		float angleRetVal = 180.1F;
		// if (!((actorFrom instanceof Aircraft) && (actorTo instanceof Aircraft))) {
		// return angleRetVal;
		// }
		double angleDoubleTemp = 0.0D;
		Loc angleActorLoc = new Loc();
		Point3d angleActorPos = new Point3d();
		Point3d angleTargetPos = new Point3d();
		Vector3d angleTargRayDir = new Vector3d();
		Vector3d angleNoseDir = new Vector3d();
		angleActorLoc.set(pointFrom);
		angleActorLoc.get(angleActorPos);
		actorTo.pos.getAbs(angleTargetPos);
		angleTargRayDir.sub(angleTargetPos, angleActorPos);
		angleDoubleTemp = angleTargRayDir.length();
		angleTargRayDir.scale(1.0D / angleDoubleTemp);
		angleNoseDir.set(1.0D, 0.0D, 0.0D);
		angleActorLoc.transform(angleNoseDir);
		angleDoubleTemp = angleNoseDir.dot(angleTargRayDir);
		angleRetVal = Geom.RAD2DEG((float) Math.acos(angleDoubleTemp));
		return angleRetVal;
	}

	public static float angleBetween(Actor actorFrom, Actor actorTo) {
		float angleRetVal = 180.1F;
		// if (!((actorFrom instanceof Aircraft) && (actorTo instanceof Aircraft))) {
		// return angleRetVal;
		// }
		double angleDoubleTemp = 0.0D;
		Loc angleActorLoc = new Loc();
		Point3d angleActorPos = new Point3d();
		Point3d angleTargetPos = new Point3d();
		Vector3d angleTargRayDir = new Vector3d();
		Vector3d angleNoseDir = new Vector3d();
		actorFrom.pos.getAbs(angleActorLoc);
		angleActorLoc.get(angleActorPos);
		actorTo.pos.getAbs(angleTargetPos);
		angleTargRayDir.sub(angleTargetPos, angleActorPos);
		angleDoubleTemp = angleTargRayDir.length();
		angleTargRayDir.scale(1.0D / angleDoubleTemp);
		angleNoseDir.set(1.0D, 0.0D, 0.0D);
		angleActorLoc.transform(angleNoseDir);
		angleDoubleTemp = angleNoseDir.dot(angleTargRayDir);
		angleRetVal = Geom.RAD2DEG((float) Math.acos(angleDoubleTemp));
		return angleRetVal;
	}

	public static float angleBetween(Actor actorFrom, Vector3d targetVector) {
		Vector3d theTargetVector = new Vector3d();
		theTargetVector.set(targetVector);
		double angleDoubleTemp = 0.0D;
		Loc angleActorLoc = new Loc();
		Point3d angleActorPos = new Point3d();
		Vector3d angleNoseDir = new Vector3d();
		actorFrom.pos.getAbs(angleActorLoc);
		angleActorLoc.get(angleActorPos);
		angleDoubleTemp = theTargetVector.length();
		theTargetVector.scale(1.0D / angleDoubleTemp);
		angleNoseDir.set(1.0D, 0.0D, 0.0D);
		angleActorLoc.transform(angleNoseDir);
		angleDoubleTemp = angleNoseDir.dot(theTargetVector);
		return Geom.RAD2DEG((float) Math.acos(angleDoubleTemp));
	}

	public static float angleBetween(Actor actorFrom, Vector3f targetVector) {
		return angleBetween(actorFrom, new Vector3d(targetVector));
	}

	public static float pitchBetween(Actor actorFrom, Actor actorTo) {
		float angleRetVal = 180.1F;
		double angleDoubleTemp = 0.0D;
		Loc angleActorLoc = new Loc();
		Point3d angleActorPos = new Point3d();
		Point3d angleTargetPos = new Point3d();
		Vector3d angleTargRayDir = new Vector3d();
		Vector3d angleNoseDir = new Vector3d();
		actorFrom.pos.getAbs(angleActorLoc);
		angleActorLoc.get(angleActorPos);
		actorTo.pos.getAbs(angleTargetPos);
		angleTargRayDir.sub(angleTargetPos, angleActorPos);
		angleDoubleTemp = angleTargRayDir.length();
		angleTargRayDir.scale(1.0D / angleDoubleTemp);
		angleNoseDir.set(1.0D, 0.0D, 0.0D);
		angleActorLoc.transform(angleNoseDir);
		angleDoubleTemp = angleTargRayDir.y;
		angleTargRayDir.y = angleTargRayDir.z;
		angleTargRayDir.z = angleDoubleTemp;
		angleDoubleTemp = angleNoseDir.dot(angleTargRayDir);
		angleRetVal = Geom.RAD2DEG((float) Math.acos(angleDoubleTemp));
		return angleRetVal;
	}

	public static double distanceBetween(Actor actorFrom, Actor actorTo) {
		double distanceRetVal = 99999.999D;
		if (!(Actor.isValid(actorFrom)) || !(Actor.isValid(actorTo))) return distanceRetVal;
		Loc distanceActorLoc = new Loc();
		Point3d distanceActorPos = new Point3d();
		Point3d distanceTargetPos = new Point3d();
		actorFrom.pos.getAbs(distanceActorLoc);
		distanceActorLoc.get(distanceActorPos);
		actorTo.pos.getAbs(distanceTargetPos);
		distanceRetVal = distanceActorPos.distance(distanceTargetPos);
		return distanceRetVal;
	}

	public static void LocalLog(Actor logActor, int i, String logLine) {
		if (logActor == World.getPlayerAircraft() && !logActor.isNetMirror()) {
			HUD.log(i, logLine);
		}
	}

	public static void LocalLog(Actor logActor, String logLine) {
		if (logActor == World.getPlayerAircraft() && !logActor.isNetMirror()) {
			HUD.log(logLine);
		}
	}

	public GuidedMissileUtils(Actor owner) {
		this.initParams(owner);
	}

	public GuidedMissileUtils(Actor theOwner, float theMissileMaxSpeedMeterPerSecond, float theLeadPercent, float theMaxG, float theStepsForFullTurn, float thePkMaxAngle, float thePkMaxAngleAft, float thePkMinDist, float thePkOptDist, float thePkMaxDist,
			float thePkMaxG, float theMaxPOVfrom, float theMaxPOVto, float theMaxDistance, float theMinPkForAttack, long theMillisecondsBetweenMissileLaunchAI, long theTargetType, boolean theAttackDecisionByAI, boolean theMultiTrackingCapable,
			boolean theCanTrackSubs, String theLockFx, String theNoLockFx, String theLockSmpl, String theNoLockSmpl) {
		this.initParams(theOwner, theMissileMaxSpeedMeterPerSecond, theLeadPercent, theMaxG, theStepsForFullTurn, thePkMaxAngle, thePkMaxAngleAft, thePkMinDist, thePkOptDist, thePkMaxDist, thePkMaxG, theMaxPOVfrom, theMaxPOVto, theMaxDistance,
				theMinPkForAttack, theMillisecondsBetweenMissileLaunchAI, theTargetType, theAttackDecisionByAI, theMultiTrackingCapable, theCanTrackSubs, theLockFx, theNoLockFx, theLockSmpl, theNoLockSmpl);
	}

	private boolean actorIsAI(Actor theActor) {
		if (!(theActor instanceof Aircraft)) return true;
		if (((Aircraft) theActor).FM == null) return true;
		if ((theActor != World.getPlayerAircraft() || !((RealFlightModel) ((Aircraft) theActor).FM).isRealMode()) && (((Aircraft) theActor).FM instanceof Pilot)) return true;
		return false;
	}

	public void cancelMissileGrowl() {
		if (this.missileOwner != World.getPlayerAircraft()) return;
		if (this.fxMissileToneLock != null) {
			this.fxMissileToneLock.setPlay(false);
			this.fxMissileToneLock.cancel();
		}
		if (this.fxMissileToneNoLock != null) {
			this.fxMissileToneNoLock.setPlay(false);
			this.fxMissileToneNoLock.cancel();
		}
	}

	public void changeMissileClass(Class theNewMissileClass) { // new function to switch missile types
		if ((this.iDebugLogLevel & 1) == 1)
			System.out.println("changeMissileClass to " + theNewMissileClass.getName());
		this.cancelMissileGrowl(); // stop
		this.rocketsList.clear();
		this.myMissileClass = theNewMissileClass;
		this.lockTonesInitialized = false;
		this.createMissileList(this.rocketsList, theNewMissileClass);
		this.setMissileGrowl(0);
		this.iMissileLockState = 0;
	}

	private void changeMissileGrowl(int iMode) {
		// if ((this.missileOwner != World.getPlayerAircraft() || !((RealFlightModel) ((Aircraft) this.missileOwner).FM).isRealMode()) && (((Aircraft) this.missileOwner).FM instanceof Pilot)) {
		// return;
		// }
		if (this.missileOwner != World.getPlayerAircraft()) return;
		this.setMissileGrowl(iMode);
		switch (iMode) {
		case 1: {
			this.playMissileGrowlLock(false);
			break;
		}
		case 2: {
			this.playMissileGrowlLock(true);
			break;
		}
		default: {
			this.cancelMissileGrowl();
			break;
		}
		}
	}

	private void checkAIlaunchMissile() {
		if (!(this.missileOwner instanceof Aircraft)) return;
		if (!this.attackDecisionByAI && !this.attackDecisionByWaypoint) return;
		Autopilotage AP = ((Aircraft)(this.missileOwner)).FM.AP;
		if (this.attackDecisionByWaypoint && (AP.way.curr().Action != 3 || AP.way.curr().getTarget() == null)) return;
		Aircraft ownerAircraft = (Aircraft) this.missileOwner;
		if ((((ownerAircraft.FM instanceof RealFlightModel)) && (((RealFlightModel) ownerAircraft.FM).isRealMode())) || (!(ownerAircraft.FM instanceof Pilot))) return;
		if (this.rocketsList.isEmpty()) return;
		// HUD.training("111");

		Pilot pilot = (Pilot) ownerAircraft.FM;
		// if (((pilot.get_maneuver() != 27) && (pilot.get_maneuver() != 62) && (pilot.get_maneuver() != 63))/* || (pilot.target == null)*/) return;

		if (pilot.target != null) {
			this.trgtAI = pilot.target.actor;
			if ((this.iDebugLogLevel & 3) == 3)
				System.out.println("AI is targetting victim " + this.trgtAI.hashCode());

			GuidedMissileUtils.checkAllActiveMissilesValidity();
			for (int activeMissileIndex = 0; activeMissileIndex < this.getActiveMissilesSize(); activeMissileIndex++) {
				ActiveMissile theActiveMissile = (ActiveMissile) this.getActiveMissile(activeMissileIndex);
				if ((this.iDebugLogLevel & 3) == 3)
					System.out.println("AI isAI=" + theActiveMissile.isAI()
					+ " owner army=" + ownerAircraft.FM.actor.getArmy()
					+ " missily army=" + theActiveMissile.getOwnerArmy()
					+ " victim=" + theActiveMissile.getVictim().hashCode()
					+ " theTarget1=" + this.trgtAI.hashCode());
				if (theActiveMissile.isAI()) {
					if (ownerAircraft.getArmy() == theActiveMissile.getOwnerArmy()) {
						if (theActiveMissile.getVictim() == this.trgtAI) {
							// HUD.training("victim is occupied!");
							this.trgtAI = null;
							break;
						}
					}
				}
			}
		} else {
			this.trgtAI = null;
		}

		if (this.trgtAI == null) {
			this.trgtAI = this.getMissileTarget();
			if ((this.iDebugLogLevel & 3) == 3)
				System.out.println("AI getMissileTarget victim=" + ((this.getMissileTarget()==null)?0:this.getMissileTarget().hashCode()));
		}
		if (this.trgtAI != null) {
			if (ownerAircraft.getArmy() == this.trgtAI.getArmy()) {
				this.trgtAI = null;
				this.trgtPk = 0F;
				return;
			}
		}

		// if ((!Actor.isValid(this.trgtAI)) || (!(this.trgtAI instanceof Aircraft))) return;
		// this.setMissileTarget(this.trgtAI);
		if ((targetType & Missile.TARGET_AIR) != 0) {
			if (Actor.isValid(this.trgtAI) && ((this.trgtAI instanceof Aircraft) || ((this.trgtAI instanceof RocketryRocket) && !((RocketryRocket)this.trgtAI).isOnRamp()) || ((this.trgtAI instanceof MissileInterceptable) && ((MissileInterceptable)this.trgtAI).isReleased()))) {
				this.setMissileTarget(this.trgtAI);
				this.trgtPk = this.getMissilePk();
			} else {
				this.trgtPk = 0F;
				return;
			}
		}
		else if (((targetType & Missile.TARGET_GROUND) != 0)||((targetType & Missile.TARGET_SHIP) != 0)) {
			if (Actor.isValid(this.trgtAI)) {
				this.setMissileTarget(this.trgtAI);
				this.trgtPk = this.getMissilePk();
			} else {
				this.trgtPk = 0F;
				return;
			}
		}
		else if ((targetType & Missile.TARGET_LOCATE) != 0) {
			this.setMissileTarget(this.trgtAI);
			this.trgtPk = this.getMissilePk();
		}

		if (ownerAircraft.FM.AP instanceof AutopilotAI) {
			((AutopilotAI) ownerAircraft.FM.AP).setOverrideMissileControl(ownerAircraft.FM.CT, false);
		}
//		if (ownerAircraft == World.getPlayerAircraft())
//			HUD.training("TRG=" + ownerAircraft.FM.CT.rocketHookSelected + " Pk="+trgtPk);
		if (this.trgtPk > this.getMinPkForAttack() && (this.iDetectorMode == Missile.DETECTOR_TYPE_RADAR_HOMING || this.iDetectorMode == Missile.DETECTOR_TYPE_RADAR_BEAMRIDING || this.iDetectorMode == Missile.DETECTOR_TYPE_RADAR_TRACK_VIA_MISSILE) && Actor.isValid(this.getMissileTarget())
			&& (this.getMissileTarget() instanceof TypeRadarWarningReceiver) && this.getMissileTarget().getArmy() != ownerAircraft.FM.actor.getArmy())
			((TypeRadarWarningReceiver) this.getMissileTarget()).myRadarLockYou(ownerAircraft.FM.actor, Property.stringValue(this.myMissileClass, "soundRadarPW", null));

		if (this.trgtPk > this.getMinPkForAttack() && (this.iDetectorMode == Missile.DETECTOR_TYPE_LASER || this.iDetectorMode == Missile.DETECTOR_TYPE_SACLOS) && this.getMissileTargetPos() != null
			&& Time.current() > this.tMissilePrev + this.getMillisecondsBetweenMissileLaunchAI() && GuidedMissileUtils.noLaunchSince(minTimeBetweenAIMissileLaunch, ownerAircraft.FM.actor.getArmy())
			&& missilesLeft(ownerAircraft.FM.CT.Weapons[ownerAircraft.FM.CT.rocketHookSelected])) {
			this.tMissilePrev = Time.current();
			// lastAIMissileLaunch = Time.current();
			ownerAircraft.FM.CT.WeaponControl[ownerAircraft.FM.CT.rocketHookSelected] = true;
			if (ownerAircraft.FM.AP instanceof AutopilotAI) {
				((AutopilotAI) ownerAircraft.FM.AP).setOverrideMissileControl(ownerAircraft.FM.CT, true);
			}
			if ((this.iDebugLogLevel & 3) == 3)
				System.out.println("Owner " + ownerAircraft.hashCode() + " missile launch against victim=" + this.getMissileTarget().hashCode() + " (" + this.getMissileTarget().getClass().getName() + ")");
			// HUD.log("AI missile launch (" + ownerAircraft.FM.CT.rocketHookSelected + "/" + Missile.getActiveMissilesSize() + ")");
		}
		else if (this.trgtPk > this.getMinPkForAttack() && Actor.isValid(this.getMissileTarget()) && this.getMissileTarget().getArmy() != ownerAircraft.FM.actor.getArmy()
				&& Time.current() > this.tMissilePrev + this.getMillisecondsBetweenMissileLaunchAI() && GuidedMissileUtils.noLaunchSince(minTimeBetweenAIMissileLaunch, ownerAircraft.FM.actor.getArmy())
				&& missilesLeft(ownerAircraft.FM.CT.Weapons[ownerAircraft.FM.CT.rocketHookSelected])) {
			if (isTargetHandledByAi(ownerAircraft.FM.actor.getArmy(), this.getMissileTarget())) return;
			addTargetHandledByAi(ownerAircraft.FM.actor.getArmy(), this.getMissileTarget());
			this.tMissilePrev = Time.current();
			// lastAIMissileLaunch = Time.current();
			ownerAircraft.FM.CT.WeaponControl[ownerAircraft.FM.CT.rocketHookSelected] = true;
			if (ownerAircraft.FM.AP instanceof AutopilotAI) {
				((AutopilotAI) ownerAircraft.FM.AP).setOverrideMissileControl(ownerAircraft.FM.CT, true);
			}
			if ((this.iDebugLogLevel & 3) == 3)
				System.out.println("Owner " + ownerAircraft.hashCode() + " missile launch against victim=" + this.getMissileTarget().hashCode() + " (" + this.getMissileTarget().getClass().getName() + ")");
			// HUD.log("AI missile launch (" + ownerAircraft.FM.CT.rocketHookSelected + "/" + Missile.getActiveMissilesSize() + ")");
		}
	}

	public void checkLockStatus() {
		int iOldLockState = this.iMissileLockState;
		boolean bEnemyInSight = false;
		boolean bSidewinderLocked = false;
		boolean bNoEnemyTimeout = false;
		try {
			if (((Aircraft) this.missileOwner).FM.CT.BrakeControl == 1.0F) {
				if (!this.oldBreakControl) {
					this.oldBreakControl = true;
					if (!((Aircraft) this.missileOwner).FM.Gears.onGround()) {
						this.engageMode--;
						if (this.engageMode < this.ENGAGE_OFF) {
							this.engageMode = this.ENGAGE_ON;
						}
						switch (this.engageMode) {
						case ENGAGE_OFF:
							if (this.missileName == null) {
							} else {
								LocalLog(this.missileOwner, this.missileName + " Engagement OFF");
							}
							break;
						case ENGAGE_AUTO:
							if (this.missileName == null) {
							} else {
								LocalLog(this.missileOwner, this.missileName + " Engagement AUTO");
							}
							break;
						case ENGAGE_ON:
							if (this.missileName == null) {
							} else {
								LocalLog(this.missileOwner, this.missileName + " Engagement ON");
							}
							break;
						}
					}
				}
			} else {
				this.oldBreakControl = false;
			}
		} catch (Exception exception) {
		}
		try {
			if (this.missileOwner != World.getPlayerAircraft()) {
				if (this.iMissileLockState != 0) {
					this.changeMissileGrowl(0);
					this.iMissileLockState = 0;
				}
				return;
			}
			// if (!((TypeGuidedMissileCarrier) this.missileOwner).hasMissiles()) {
			if (!this.hasMissiles()) {
				if (this.iMissileLockState != 0) {
					this.changeMissileGrowl(0);
					LocalLog(this.missileOwner, this.missileName + " missiles depleted");
					this.iMissileLockState = 0;
				}
				return;
			}
			if (this.engageMode == this.ENGAGE_OFF) {
				if (this.iMissileLockState != 0) {
					this.changeMissileGrowl(0);
					LocalLog(this.missileOwner, this.missileName + " disengaged");
					this.iMissileLockState = 0;
				}
				return;
			}

			if ((this.iDetectorMode == Missile.DETECTOR_TYPE_LASER || this.iDetectorMode == Missile.DETECTOR_TYPE_SACLOS) && this.trgtPosMissile != null) {
				bSidewinderLocked = true;
				bEnemyInSight = true;
			}
			else if (Actor.isValid(this.trgtMissile)) {
				bSidewinderLocked = true;
				if (this.trgtMissile.getArmy() != World.getPlayerAircraft().getArmy()) {
					bEnemyInSight = true;
				}
			}

			if (Actor.isValid(Main3D.cur3D().viewActor())) {
				if (Main3D.cur3D().viewActor() == this.missileOwner) {
					Actor theEnemy = Selector.look(true, false, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], World.getPlayerAircraft().getArmy(), -1, World.getPlayerAircraft(), false);
					if (Actor.isValid(theEnemy)) {
						bEnemyInSight = true;
					}
				}
			}

			if (bEnemyInSight) {
				this.tLastSeenEnemy = Time.current();
			} else {
				if (Time.current() - this.tLastSeenEnemy > 10000) {
					bNoEnemyTimeout = true;
				}
			}

			if (bSidewinderLocked) {
				if (bEnemyInSight) {
					this.iMissileLockState = 2;
				} else {
					if (this.engageMode == this.ENGAGE_ON) {
						this.iMissileLockState = 2;
					} else {
						if (bNoEnemyTimeout) {
							this.iMissileLockState = 0;
						} else {
							this.iMissileLockState = 2;
						}
					}
				}
			} else {
				if (bNoEnemyTimeout) {
					this.iMissileLockState = 0;
				} else {
					this.iMissileLockState = 1;
				}
			}

			if ((this.engageMode == this.ENGAGE_ON) && (this.iMissileLockState == 0)) {
				this.iMissileLockState = 1;
			}
			if ((((Aircraft) this.missileOwner).FM.getOverload() > this.fPkMaxG) && (this.iMissileLockState == 2)) {
				this.iMissileLockState = 1;
			}

			switch (this.iMissileLockState) {
			case 1: {
				if (iOldLockState != 1) {
					this.changeMissileGrowl(1);
				}
				if (iOldLockState == 0) {
					LocalLog(this.missileOwner, this.missileName + " engaged");
				}
				break;
			}
			case 2: {
				if (iOldLockState != 2) {
					this.changeMissileGrowl(2);
				}
				if (iOldLockState == 0) {
					LocalLog(this.missileOwner, this.missileName + " engaged");
				}
				break;
			}
			case 0: {
				if (iOldLockState != 0) {
					this.changeMissileGrowl(0);
					LocalLog(this.missileOwner, this.missileName + " disengaged");
				}
				break;
			}
			default: {
				if (iOldLockState != 0) {
					this.changeMissileGrowl(0);
					LocalLog(this.missileOwner, this.missileName + " disengaged");
				}
				break;
			}
			}

		} catch (Exception exception) {	}
	}

	private void checkPendingMissiles() {
		if (this.rocketsList.isEmpty()) {
			if ((this.iDebugLogLevel & 3) == 3)
				if (this.missileOwner == World.getPlayerAircraft()) System.out.println("checkPendingMissiles this.rocketsList.isEmpty()=true");
			return;
		}
		if (this.rocketsList.get(0) instanceof RocketGunWithDelay) {
			if ((this.iDebugLogLevel & 3) == 3)
				if (this.missileOwner == World.getPlayerAircraft()) System.out.println("checkPendingMissiles this.rocketsList.get(0) instanceof RocketGunWithDelay=true, hash=" + ((RocketGunWithDelay) this.rocketsList.get(0)).hashCode());
			((RocketGunWithDelay) this.rocketsList.get(0)).checkPendingWeaponRelease();
		} else {
			if ((this.iDebugLogLevel & 3) == 3)
				if (this.missileOwner == World.getPlayerAircraft()) System.out.println("checkPendingMissiles this.rocketsList.get(0) instanceof RocketGunWithDelay=false, hash=" + this.rocketsList.get(0).hashCode());
		}
	}

	public void createMissileList(ArrayList theMissileList) { // default missile init selects first available missile
		this.createMissileList(theMissileList, null);
	}

	// TODO: ++ Added/changed Code Multiple Missile Type Selection ++
	public void createMissileList(ArrayList theMissileList, Class theMissileClass) {
		Aircraft theMissileCarrier = (Aircraft) this.missileOwner;
		try {
			for (int l = 0; l < theMissileCarrier.FM.CT.Weapons.length; l++) {
				if (theMissileCarrier.FM.CT.Weapons[l] != null) {
					for (int l1 = 0; l1 < theMissileCarrier.FM.CT.Weapons[l].length; l1++) {
						if (theMissileCarrier.FM.CT.Weapons[l][l1] != null) {
							if (theMissileCarrier.FM.CT.Weapons[l][l1] instanceof RocketGun) {
								RocketGun theRocketGun = (RocketGun) theMissileCarrier.FM.CT.Weapons[l][l1];
								if (theRocketGun.haveBullets()) {
									Class theBulletClass = theRocketGun.bulletClass();
									if (theMissileClass != null) {
										if (!(theBulletClass.getName().equals(theMissileClass.getName()))) {
											continue; // Not the type of missile we're searching for.
										}
									}
									if (Missile.class.isAssignableFrom(theBulletClass)) { // We've found a missile!
										if (theMissileClass == null) {
											theMissileClass = theBulletClass;
										}
										theMissileList.add(theMissileCarrier.FM.CT.Weapons[l][l1]);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception exception) {
			EventLog.type("Exception in initParams: " + exception.getMessage());
		}
		if (theMissileClass == null) return;
		this.getMissileProperties(theMissileClass);
	}

	private int engineHeatSpreadType(Actor theActor) {
		if (theActor instanceof RocketryRocket) return HEAT_SPREAD_AFT;
		if (theActor instanceof MissileInterceptable) return HEAT_SPREAD_AFT;
		if (!(theActor instanceof Aircraft)) return HEAT_SPREAD_360;
		EnginesInterface checkEI = ((FlightModelMain) (((SndAircraft) (theActor)).FM)).EI;
		int iRetVal = HEAT_SPREAD_NONE;
		for (int i = 0; i < checkEI.getNum(); i++) {
			int iMotorType = checkEI.engines[i].getType();
			if (iMotorType == Motor._E_TYPE_JET || iMotorType == Motor._E_TYPE_ROCKET || iMotorType == Motor._E_TYPE_ROCKETBOOST || iMotorType == Motor._E_TYPE_PVRD) {
				iRetVal |= HEAT_SPREAD_AFT;
			}
			if (iMotorType == Motor._E_TYPE_INLINE || iMotorType == Motor._E_TYPE_RADIAL || iMotorType == Motor._E_TYPE_HELO_INLINE || iMotorType == Motor._E_TYPE_UNKNOWN) {
				iRetVal |= HEAT_SPREAD_360;
			}
		}
		return iRetVal;
	}

	private int engineHeatSpreadType(Motor theMotor) {
		int iRetVal = HEAT_SPREAD_NONE;
		int iMotorType = theMotor.getType();
		if (iMotorType == Motor._E_TYPE_JET || iMotorType == Motor._E_TYPE_ROCKET || iMotorType == Motor._E_TYPE_ROCKETBOOST || iMotorType == Motor._E_TYPE_PVRD) {
			iRetVal |= HEAT_SPREAD_AFT;
		}
		if (iMotorType == Motor._E_TYPE_INLINE || iMotorType == Motor._E_TYPE_RADIAL || iMotorType == Motor._E_TYPE_HELO_INLINE || iMotorType == Motor._E_TYPE_UNKNOWN) {
			iRetVal |= HEAT_SPREAD_360;
		}
		return iRetVal;
	}

	public boolean getAttackDecisionByAI() {
		return this.attackDecisionByAI;
	}

	public boolean getAttackDecisionByWaypoint() {
		return this.attackDecisionByWaypoint;
	}

	// TODO: For Countermeasures:
	public int getDetectorType() {
		return this.iDetectorMode;
	}

	public boolean getCanTrackSubs() {
		return this.canTrackSubs;
	}

	public SoundFX getFxMissileToneLock() {
		return this.fxMissileToneLock;
	}

	public SoundFX getFxMissileToneNoLock() {
		return this.fxMissileToneNoLock;
	}

	public float getLeadPercent() {
		return this.fLeadPercent;
	}

	public float getMaxDistance() {
		return this.fMaxDistance;
	}

	public float getMaxG() {
		return this.fMaxG;
	}

	public float getMaxPOVfrom() {
		return this.fMaxPOVfrom;
	}

	public float getMaxPOVto() {
		return this.fMaxPOVto;
	}

	public long getMillisecondsBetweenMissileLaunchAI() {
		return this.millisecondsBetweenMissileLaunchAI;
	}

	public float getMinPkForAttack() {
		return this.minPkForAttack;
	}

	public Missile getMissileFromRocketGun(RocketGun theRocketGun) {
		return (Missile) theRocketGun.rocket;
	}

	public int getMissileGrowl() {
		return this.iMissileTone;
	}

	public int getMissileLockState() {
		return this.iMissileLockState;
	}

	public float getMissileMaxSpeedKmh() {
		return this.fMissileMaxSpeedKmh;
	}

	public String getMissileName() {
		return this.missileName;
	}

	public Actor getMissileOwner() {
		return this.missileOwner;
	}

	private float getMissilePk() {
		float thePk = 0.0F;
		if (this.iDetectorMode == Missile.DETECTOR_TYPE_LASER || this.iDetectorMode == Missile.DETECTOR_TYPE_SACLOS) {
			if (this.getMissileTargetPos() != null)
				thePk = this.Pk(this.missileOwner, this.getMissileTargetPos());
		} else {
			if (Actor.isValid(this.getMissileTarget()))
				thePk = this.Pk(this.missileOwner, this.getMissileTarget());
		}

		return thePk;
	}

	public void getMissileProperties(Class theMissileClass) { // separate properties from missile list creation
		this.fPkMaxG = Property.floatValue(theMissileClass, "maxLockGForce", 99.9F);
		this.fMaxPOVfrom = Property.floatValue(theMissileClass, "maxFOVfrom", 99.9F);
		this.fMaxPOVto = Property.floatValue(theMissileClass, "maxFOVto", 99.9F);
		this.fPkMaxAngle = Property.floatValue(theMissileClass, "PkMaxFOVfrom", 99.9F);
		this.fPkMaxAngleAft = Property.floatValue(theMissileClass, "PkMaxFOVto", 99.9F);
		this.fPkMinDist = Property.floatValue(theMissileClass, "PkDistMin", 99.9F);
		this.fPkOptDist = Property.floatValue(theMissileClass, "PkDistOpt", 99.9F);
		this.fPkMaxDist = Property.floatValue(theMissileClass, "PkDistMax", 99.9F);
		this.fMissileMaxSpeedKmh = Property.floatValue(theMissileClass, "maxSpeed", 99.9F);
		this.fLeadPercent = Property.floatValue(theMissileClass, "leadPercent", 99.9F);
		this.fMaxG = Property.floatValue(theMissileClass, "maxGForce", 99.9F);
		this.iDetectorMode = Property.intValue(theMissileClass, "detectorType", Missile.DETECTOR_TYPE_MANUAL);
		this.attackDecisionByAI = Property.intValue(theMissileClass, "attackDecisionByAI", 0) == Missile.ATTACK_DECISION_BY_AI_YES;
		this.attackDecisionByWaypoint = Property.intValue(theMissileClass, "attackDecisionByAI", 0) == Missile.ATTACK_DECISION_BY_AI_WAYPOINT;
		this.canTrackSubs = Property.intValue(theMissileClass, "canTrackSubs", 0) != 0;
		this.multiTrackingCapable = Property.intValue(theMissileClass, "multiTrackingCapable", 0) != 0;
		this.minPkForAttack = Property.floatValue(theMissileClass, "minPkForAI", 25.0F);
		this.millisecondsBetweenMissileLaunchAI = Property.longValue(theMissileClass, "timeForNextLaunchAI", 10000L);
		this.targetType = Property.longValue(theMissileClass, "targetType", Missile.TARGET_AIR);
		this.fSunBrightThreshold = Property.floatValue(theMissileClass, "sunBrightThreshold", 0.03F);
		this.missileName = Property.stringValue(theMissileClass, "friendlyName", "Missile");
		this.myMissileClass = theMissileClass;
		this.initLockTones();
		this.iDebugLogLevel = Config.cur.ini.get("Mods", "GuidedMissileDebugLog", 0);
		this.bRealisticRadarSelect = Config.cur.ini.get("Mods", "RealisticRadarSelect", 0) != 0;
	}

	public Actor getMissileTarget() {
		return this.trgtMissile;
	}

	public Point3d getMissileTargetPos() {
		return this.trgtPosMissile;
	}

	public Actor getMissileTargetPosOwner() {
		return this.trgtPosOwner;
	}

	public Point3f getMissileTargetOffset() {
		return this.getSelectedActorOffset();
	}

	public boolean getMultiTrackingCapable() {
		return this.multiTrackingCapable;
	}

	public float getPkMaxAngle() {
		return this.fPkMaxAngle;
	}

	public float getPkMaxAngleAft() {
		return this.fPkMaxAngleAft;
	}

	public float getPkMaxDist() {
		return this.fPkMaxDist;
	}

	public float getPkMaxG() {
		return this.fPkMaxG;
	}

	public float getPkMinDist() {
		return this.fPkMinDist;
	}

	public float getPkOptDist() {
		return this.fPkOptDist;
	}

	public Point3f getSelectedActorOffset() {
		return this.selectedActorOffset;
	}

	public Sample getSmplMissileLock() {
		return this.smplMissileLock;
	}

	public Sample getSmplMissileNoLock() {
		return this.smplMissileNoLock;
	}

	public long getStartLastMissile() {
		return this.tStartLastMissile;
	}

	public float getStepsForFullTurn() {
		return this.fStepsForFullTurn;
	}

	public long getTargetType() {
		return this.targetType;
	}

	public boolean hasMissiles() {
		return !this.rocketsList.isEmpty();
	}

	private void initCommon() {
		this.selectedActorOffset = new Point3f();
		this.engageMode = this.ENGAGE_AUTO;
		this.iMissileLockState = 0;
		this.iMissileTone = 0;
		this.tLastSeenEnemy = Time.current() - 20000L;
		this.oldBreakControl = true;
		this.rocketsList = new ArrayList();
		this.tMissilePrev = 0L;
		this.attackDecisionByAI = false;
		this.attackDecisionByWaypoint = false;
		this.minPkForAttack = 25.0F;
		this.millisecondsBetweenMissileLaunchAI = 10000L;
		this.trgtPosMissile = new Point3d();
	}

	private void initLockTones() {
		if (this.lockTonesInitialized) return;
		if (this.myMissileClass == null) return;
		if (this.missileOwner == World.getPlayerAircraft()) {
			this.setFxMissileToneLock(Property.stringValue(this.myMissileClass, "fxLock", null), Property.floatValue(this.myMissileClass, "fxLockVolume", 1.0F));
			this.setFxMissileToneNoLock(Property.stringValue(this.myMissileClass, "fxNoLock", null), Property.floatValue(this.myMissileClass, "fxNoLockVolume", 1.0F));
			this.setSmplMissileLock(Property.stringValue(this.myMissileClass, "smplLock", null));
			this.setSmplMissileNoLock(Property.stringValue(this.myMissileClass, "smplNoLock", null));
		}
		this.lockTonesInitialized = true;
		if ((this.iDebugLogLevel & 3) == 3)
			System.out.println("initLockTones finished");
	}

	private void initParams(Actor theOwner) {
		if (theOwner instanceof Aircraft) {
			this.missileOwner = theOwner;
		}
		this.initCommon();
	}

	private void initParams(Actor theOwner, float theMissileMaxSpeedMeterPerSecond, float theLeadPercent, float theMaxG, float theStepsForFullTurn, float thePkMaxAngle, float thePkMaxAngleAft, float thePkMinDist, float thePkOptDist, float thePkMaxDist,
			float thePkMaxG, float theMaxPOVfrom, float theMaxPOVto, float theMaxDistance, float theMinPkForAttack, long theMillisecondsBetweenMissileLaunchAI, long theTargetType, boolean theAttackDecisionByAI, boolean theMultiTrackingCapable,
			boolean theCanTrackSubs, String theLockFx, String theNoLockFx, String theLockSmpl, String theNoLockSmpl) {
		this.initCommon();
		this.missileOwner = theOwner;
		this.fMissileMaxSpeedKmh = theMissileMaxSpeedMeterPerSecond;
		this.fLeadPercent = theLeadPercent;
		this.fMaxG = theMaxG;
		this.fStepsForFullTurn = theStepsForFullTurn;
		this.fPkMaxAngle = thePkMaxAngle;
		this.fPkMaxAngleAft = thePkMaxAngleAft;
		this.fPkMinDist = thePkMinDist;
		this.fPkOptDist = thePkOptDist;
		this.fPkMaxDist = thePkMaxDist;
		this.fPkMaxG = thePkMaxG;
		this.fMaxPOVfrom = theMaxPOVfrom;
		this.fMaxPOVto = theMaxPOVto;
		this.fMaxDistance = theMaxDistance;
		this.attackDecisionByAI = theAttackDecisionByAI;
		this.minPkForAttack = theMinPkForAttack;
		this.millisecondsBetweenMissileLaunchAI = theMillisecondsBetweenMissileLaunchAI;
		this.targetType = theTargetType;
		this.canTrackSubs = theCanTrackSubs;
		this.multiTrackingCapable = theMultiTrackingCapable;
		if (theLockFx == null) {
			this.fxMissileToneLock = null;
		} else {
			this.fxMissileToneLock = this.missileOwner.newSound(theLockFx, false);
		}
		if (theNoLockFx == null) {
			this.fxMissileToneNoLock = null;
		} else {
			this.fxMissileToneNoLock = this.missileOwner.newSound(theNoLockFx, false);
		}
		if (theLockSmpl == null) {
			this.smplMissileLock = null;
		} else {
			this.smplMissileLock = new Sample(theLockSmpl, 256, 65535);
			this.smplMissileLock.setInfinite(true);
		}
		if (theNoLockSmpl == null) {
			this.smplMissileNoLock = null;
		} else {
			this.smplMissileNoLock = new Sample(theNoLockSmpl, 256, 65535);
			this.smplMissileNoLock.setInfinite(true);
		}
	}

	public Actor lookForGuidedMissileTarget(Actor actor, float maxFOVfrom, float maxFOVto, double maxDistance) {
		return this.lookForGuidedMissileTargetAircraft(actor, maxFOVfrom, maxFOVto, maxDistance);
	}

	public Actor lookForGuidedMissileTarget(Actor actor, float maxFOVfrom, float maxFOVto, double maxDistance, long targetType) {
		Actor actorTarget = null;
		if ((targetType & Missile.TARGET_AIR) != 0) {
			actorTarget = this.lookForGuidedMissileTargetAircraft(actor, maxFOVfrom, maxFOVto, maxDistance);
		} else if ((targetType & Missile.TARGET_GROUND) != 0 && (targetType & Missile.TARGET_SHIP) != 0) {
			Actor actorTargetG = this.lookForGuidedMissileTargetGround(actor, maxFOVfrom, maxFOVto, maxDistance);
			Actor actorTargetS = this.lookForGuidedMissileTargetShip(actor, maxFOVfrom, maxFOVto, maxDistance);
			if (actorTargetG != null && actorTargetS == null) actorTarget = actorTargetG;
			if (actorTargetG == null && actorTargetS != null) actorTarget = actorTargetS;
			if (actorTargetG != null && actorTargetS != null) {
				double distanceG = distanceBetween(actor, actorTargetG);
				double distanceS = distanceBetween(actor, actorTargetS);
				if (distanceG < distanceS) actorTarget = actorTargetG;
				else actorTarget = actorTargetS;
			}
		} else if ((targetType & Missile.TARGET_GROUND) != 0) {
			actorTarget = this.lookForGuidedMissileTargetGround(actor, maxFOVfrom, maxFOVto, maxDistance);
		} else if ((targetType & Missile.TARGET_SHIP) != 0) {
			actorTarget = this.lookForGuidedMissileTargetShip(actor, maxFOVfrom, maxFOVto, maxDistance);
		} else if ((targetType & Missile.TARGET_LOCATE) != 0) {
			actorTarget = this.lookForGuidedMissileTargetLocate(actor, maxFOVfrom, maxFOVto, maxDistance);
		}
		return actorTarget;
	}

	public Actor lookForGuidedMissileTargetAircraft(Actor actor, float maxFOVfrom, float maxFOVto, double maxDistance) {
		double targetDistance = 0.0D;
		float targetAngle = 0.0F;
		float targetAngleAft = 0.0F;
		float targetBait = 0.0F;
		float maxTargetBait = 0.0F;
		Actor selectedActor = null;
		Point3f theSelectedActorOffset = new Point3f(0.0F, 0.0F, 0.0F);

		if (this.iDetectorMode == Missile.DETECTOR_TYPE_MANUAL) return selectedActor;

		FlightModelMain ownerfm = ((Aircraft)actor).FM;
		if (this.bRealisticRadarSelect && actor instanceof TypeSemiRadar &&
			(this.iDetectorMode == Missile.DETECTOR_TYPE_RADAR_HOMING || this.iDetectorMode == Missile.DETECTOR_TYPE_RADAR_TRACK_VIA_MISSILE) &&
			((ownerfm instanceof RealFlightModel) && ((RealFlightModel) ownerfm).isRealMode() && (ownerfm instanceof Pilot))) {
			if (!((TypeSemiRadar) actor).getSemiActiveRadarOn() || ((TypeSemiRadar) actor).getSemiActiveRadarLockedActor() == null) {
 				if ((this.iDebugLogLevel & 3) == 3)
					System.out.println("Semi-Active Radar is OFF. Missile target is made null.");
				return null;
			}
			else {
				Actor theTarget1 = ((TypeSemiRadar) actor).getSemiActiveRadarLockedActor();
				if ((theTarget1 instanceof Aircraft) || ((theTarget1 instanceof RocketryRocket) && !((RocketryRocket)theTarget1).isOnRamp()) || ((theTarget1 instanceof MissileInterceptable) && ((MissileInterceptable)theTarget1).isReleased())) {
					targetDistance = distanceBetween(actor, theTarget1);
					if (targetDistance > maxDistance) {
						return null;
					}
					targetAngle = angleBetween(actor, theTarget1);
					if (targetAngle > maxFOVfrom) {
						return null;
					}

					float fACMass = 0.0F;
					if (theTarget1 instanceof Aircraft) {
						Mass theM = ((FlightModelMain) (((SndAircraft) (theTarget1)).FM)).M;
						fACMass = theM.getFullMass();
					} else if ((theTarget1 instanceof RocketryRocket) || (theTarget1 instanceof MissileInterceptable)) {
						fACMass = Property.floatValue(theTarget1.getClass(), "massa", 1000F);
					}
					targetBait = fACMass / targetAngle / (float) (targetDistance * targetDistance);
					if (!theTarget1.isAlive()) {
						targetBait /= 10F;
					}
					if (targetBait > maxTargetBait) {
						maxTargetBait = targetBait;
						selectedActor = theTarget1;
						if (theTarget1 instanceof Aircraft) {
							theSelectedActorOffset.set(0.0F, 0.0F, 0.0F);
						}
						return selectedActor;
					}
					else
						return null;
				}

			}
		}

		try {
			List list = Engine.targets();
			int k = list.size();
			for (int i1 = 0; i1 < k; i1++) {
				Actor theTarget1 = (Actor) list.get(i1);
				if ((theTarget1 instanceof Aircraft) || ((theTarget1 instanceof RocketryRocket) && !((RocketryRocket)theTarget1).isOnRamp()) || ((theTarget1 instanceof MissileInterceptable) && ((MissileInterceptable)theTarget1).isReleased())) {
					targetDistance = distanceBetween(actor, theTarget1);
					if (targetDistance > maxDistance) {
						continue;
					}
					targetAngle = angleBetween(actor, theTarget1);
					if (targetAngle > maxFOVfrom) {
						continue;
					}
					targetAngleAft = 180.0F - angleBetween(theTarget1, actor);
					if (targetAngleAft > maxFOVto) {
						if (this.iDetectorMode == Missile.DETECTOR_TYPE_INFRARED) {
							if ((this.engineHeatSpreadType(theTarget1) & HEAT_SPREAD_360) == 0) {
								continue;
							}
						}
					}

					switch (this.iDetectorMode) {
					case Missile.DETECTOR_TYPE_INFRARED: {
						float maxEngineForce = 0.0F;
						int maxEngineForceEngineNo = 0;
						EnginesInterface theEI = null;
						if (theTarget1 instanceof Aircraft) {
							theEI = ((FlightModelMain) (((SndAircraft) (theTarget1)).FM)).EI;
							int iNumEngines = theEI.getNum();
							for (int i = 0; i < iNumEngines; i++) {
								Motor theMotor = theEI.engines[i];
								float theEngineForce = theMotor.getEngineForce().length();
								if (this.engineHeatSpreadType(theMotor) == HEAT_SPREAD_NONE) {
									theEngineForce = 0F;
								}
								if (this.engineHeatSpreadType(theMotor) == HEAT_SPREAD_360) {
									theEngineForce /= 10F;
								}
								if (theEngineForce > maxEngineForce) {
									maxEngineForce = theEngineForce;
									maxEngineForceEngineNo = i;
								}
							}
						} else if ((theTarget1 instanceof RocketryRocket) || (theTarget1 instanceof MissileInterceptable)) {
							maxEngineForce = Property.floatValue(theTarget1.getClass(), "force", 1000F);
						}
						targetBait = maxEngineForce / targetAngle / (float) (targetDistance * targetDistance);
						if (!theTarget1.isAlive()) {
							targetBait /= 10F;
						}
//						GuidedMissileUtils.checkAllActiveMissilesValidity();
//						for (int activeMissileIndex = 0; activeMissileIndex < this.getActiveMissilesSize(); activeMissileIndex++) {
//							ActiveMissile theActiveMissile = (ActiveMissile) this.getActiveMissile(activeMissileIndex);
//							if (theActiveMissile.isAI()) {
//								if (actor.getArmy() == theActiveMissile.getOwnerArmy()) {
//									if (theActiveMissile.getVictim() == theTarget1) {
//										targetBait = 0.0F;
//										break;
//									}
//								}
//							}
//						}
						if (targetBait > maxTargetBait) {
							maxTargetBait = targetBait;
							selectedActor = theTarget1;
							if (theTarget1 instanceof Aircraft) {
								theSelectedActorOffset = theEI.engines[maxEngineForceEngineNo].getEnginePos();
							}
						}
						break;
					}
					case Missile.DETECTOR_TYPE_RADAR_BEAMRIDING:
					case Missile.DETECTOR_TYPE_RADAR_HOMING:
					case Missile.DETECTOR_TYPE_RADAR_TRACK_VIA_MISSILE: {
						float fACMass = 0.0F;
						if (theTarget1 instanceof Aircraft) {
							Mass theM = ((FlightModelMain) (((SndAircraft) (theTarget1)).FM)).M;
							fACMass = theM.getFullMass();
						} else if ((theTarget1 instanceof RocketryRocket) || (theTarget1 instanceof MissileInterceptable)) {
							fACMass = Property.floatValue(theTarget1.getClass(), "massa", 1000F);
						}
						targetBait = fACMass / targetAngle / (float) (targetDistance * targetDistance);
						if (!theTarget1.isAlive()) {
							targetBait /= 10F;
						}

//						GuidedMissileUtils.checkAllActiveMissilesValidity();
//						for (int activeMissileIndex = 0; activeMissileIndex < this.getActiveMissilesSize(); activeMissileIndex++) {
//							ActiveMissile theActiveMissile = (ActiveMissile) this.getActiveMissile(activeMissileIndex);
//							if (this.actorIsAI(actor)) {
//								if (actor.getArmy() == theActiveMissile.getOwnerArmy()) {
//									if (theActiveMissile.getVictim() == theTarget1) {
//										targetBait = 0.0F;
//										break;
//									}
//								}
//							}
//							if (!this.multiTrackingCapable) {
//								if (theActiveMissile.getOwner() == actor) {
//									if (theActiveMissile.getVictim() != null) {
//										if (this.actorIsAI(actor)) return null;
//										else return theActiveMissile.getVictim();
//									}
//								}
//							}
//						}

						if (targetBait > maxTargetBait) {
							maxTargetBait = targetBait;
							selectedActor = theTarget1;
							if (theTarget1 instanceof Aircraft) {
								// float fGC = FlightModelMainEx.getFmGCenter((((SndAircraft) (theTarget1)).FM));
								// Arm Arms = (Arm) Reflection.genericGetFieldValue((FlightModelMain) (((SndAircraft) (theTarget1)).FM), "Arms");
								// float fGC = ((Float) Reflection.genericGetFieldValue(Arms, "GCENTER")).floatValue();
								// theSelectedActorOffset.set(fGC, 0, 0);
								theSelectedActorOffset.set(0.0F, 0.0F, 0.0F);
							}
						}
						break;
					}
					default:
						break;
					}

				}
			}
		} catch (Exception e) {
			EventLog.type("Exception in selectedActor");
			EventLog.type(e.toString());
			EventLog.type(e.getMessage());
		}
		if ((this.iDebugLogLevel & 3) == 3)
			if (selectedActor != null) System.out.println("Owner " + actor.hashCode() +" selected target " + selectedActor.hashCode() + "(" + selectedActor.getClass().getName() + ")");


		if (actor instanceof Aircraft && selectedActor != null) {
			Aircraft ownerAircraft = (Aircraft)actor;
			if (!ownerAircraft.FM.isPlayers() || !(ownerAircraft.FM instanceof RealFlightModel) || !((RealFlightModel)ownerAircraft.FM).isRealMode()) {
				if ((this.iDebugLogLevel & 3) == 3)
					System.out.println("Active Missiles before check: " + this.getActiveMissilesSize());
				GuidedMissileUtils.checkAllActiveMissilesValidity();
				if ((this.iDebugLogLevel & 3) == 3)
					System.out.println("Active Missiles after check: " + this.getActiveMissilesSize());
				for (int activeMissileIndex = 0; activeMissileIndex < this.getActiveMissilesSize(); activeMissileIndex++) {
					ActiveMissile theActiveMissile = (ActiveMissile) this.getActiveMissile(activeMissileIndex);
					if ((this.iDebugLogLevel & 3) == 3)
						System.out.println("isAI=" + theActiveMissile.isAI()
								+ " owner army=" + ownerAircraft.FM.actor.getArmy()
								+ " missily army=" + theActiveMissile.getOwnerArmy()
								+ " victim=" + theActiveMissile.getVictim().hashCode()
								+ " selectedActor=" + selectedActor.hashCode());
					if (theActiveMissile.isAI()) {
						if (ownerAircraft.FM.actor.getArmy() == theActiveMissile.getOwnerArmy()) {
							if (theActiveMissile.getVictim() == selectedActor) {
								if ((this.iDebugLogLevel & 3) == 3)
									System.out.println("Skipping target " + selectedActor.hashCode() + "(" + selectedActor.getClass().getName() + ")");
								selectedActor = null;
								break;
							}
						}
					}
				}
			}
		}

		this.selectedActorOffset.set(theSelectedActorOffset);
		return selectedActor;
	}

	public Actor lookForGuidedMissileTargetGround(Actor actor, float maxFOVfrom, float maxFOVto, double maxDistance) {
		double targetDistance = 0.0D;
		float targetAngle = 0.0F;
		float targetBait = 0.0F;
		float maxTargetBait = 0.0F;
		Actor selectedActor = null;
		Point3f theSelectedActorOffset = new Point3f(0.0F, 0.0F, 0.0F);

		if (!(actor instanceof Aircraft) || (this.iDetectorMode == Missile.DETECTOR_TYPE_MANUAL)) return selectedActor;

		// At Night or bad weather, EOTV (Visible Ray TV camera) doesn't work.
		if (this.iDetectorMode == Missile.DETECTOR_TYPE_IMAGE_EOTV &&
			  (World.Sun().ToSun.z < this.fSunBrightThreshold
			  || ((Mission.curCloudsType() > 3 || (Mission.curCloudsType() > 1 && World.Sun().ToSun.z < this.fSunBrightThreshold + 0.05F))
				  && Mission.curCloudsHeight() > actor.pos.getAbsPoint().z)))
			return selectedActor;

		// At too hot ground temperature, IR Imaging doesn't work.
		if (this.iDetectorMode == Missile.DETECTOR_TYPE_IMAGE_IR && Mission.curCloudsType() < 2 &&
			  World.getTimeofDay() > 11.0F && World.getTimeofDay() < 13.5F &&
			  Atmosphere.temperature((float)Engine.land().HQ_Air(actor.pos.getAbsPoint().x, actor.pos.getAbsPoint().y)) > 308.15F  // ground temperature is more than 35 deg Celsius!!
		   )
			return selectedActor;

		FlightModelMain ownerfm = ((Aircraft)actor).FM;
		if (this.bRealisticRadarSelect && actor instanceof TypeGroundRadar &&
			(this.iDetectorMode == Missile.DETECTOR_TYPE_IMAGE_EOTV || this.iDetectorMode == Missile.DETECTOR_TYPE_IMAGE_IR) &&
			(ownerfm instanceof RealFlightModel) && ((RealFlightModel) ownerfm).isRealMode() && (ownerfm instanceof Pilot)) {
			if (((TypeGroundRadar) actor).getGroundRadarOn() && ((TypeGroundRadar) actor).getGroundRadarLockedActor() != null) {
				Actor theTarget1 = ((TypeGroundRadar) actor).getGroundRadarLockedActor();
				if (((theTarget1 instanceof TgtFlak) || (theTarget1 instanceof TgtTank) || (theTarget1 instanceof TgtTrain) || (theTarget1 instanceof TgtVehicle)) &&
					(Main.cur().clouds == null || Main.cur().clouds.getVisibility(theTarget1.pos.getAbsPoint(), actor.pos.getAbsPoint()) >= 1.0F)) {
					targetDistance = distanceBetween(actor, theTarget1);
					targetAngle = angleBetween(actor, theTarget1);
					targetBait = 1 / targetAngle / (float) (targetDistance * targetDistance);
					if (!theTarget1.isAlive()) {
						targetBait /= 10F;
					}
					if (targetDistance <= maxDistance && targetAngle <= maxFOVfrom && targetBait <= maxTargetBait) {
						maxTargetBait = targetBait;
						selectedActor = theTarget1;
						this.selectedActorOffset.set(theSelectedActorOffset);
						return selectedActor;
					}
				}

			}
		}

		try {
			ArrayList arraylist = World.cur().statics.bridges;
			int jj = arraylist.size();
			LongBridge longbridge = null;
			Actor target_bridge = null;
			double longbridgeDistance = maxDistance;
			for (int ll = 0; ll < jj; ll++) {
				LongBridge longbridge1 = (LongBridge)arraylist.get(ll);
				if (!longbridge1.isAlive()) continue;
				// Not target about objects behind of clouds from the missile's seaker.
				if (Main.cur().clouds != null && Main.cur().clouds.getVisibility(longbridge1.pos.getAbsPoint(), actor.pos.getAbsPoint()) < 1.0F)
					continue;
				targetDistance = distanceBetween(actor, longbridge1);
				if (targetDistance > maxDistance) {
					continue;
				}
				targetAngle = angleBetween(actor, longbridge1);
				if (targetAngle > maxFOVfrom) {
					continue;
				}
				if (targetDistance < longbridgeDistance) {
					longbridge = longbridge1;
					longbridgeDistance = targetDistance;
				}
			}

			if (longbridge != null) {
				int kk = longbridge.NumStateBits() / 2;
				target_bridge = BridgeSegment.getByIdx(longbridge.bridgeIdx(), World.Rnd().nextInt(kk));
			}

			List list = Engine.targets();
			int k = list.size();
			for (int i1 = 0; i1 < k; i1++) {
				Actor theTarget1 = (Actor) list.get(i1);
				if ((theTarget1 instanceof TgtFlak) || (theTarget1 instanceof TgtTank) || (theTarget1 instanceof TgtTrain) || (theTarget1 instanceof TgtVehicle)) {
					// EventLog.type("Checking Target " + theTarget1.getClass().getName());
					// Not target about objects behind of clouds from the missile's seaker.
					if (Main.cur().clouds != null && Main.cur().clouds.getVisibility(theTarget1.pos.getAbsPoint(), actor.pos.getAbsPoint()) < 1.0F)
						continue;
					targetDistance = distanceBetween(actor, theTarget1);
					// EventLog.type("distance " + targetDistance + " max " + maxDistance);
					if (targetDistance > maxDistance) {
						continue;
					}
					targetAngle = angleBetween(actor, theTarget1);
					// EventLog.type("angle " + targetAngle + " max " + maxFOVfrom);
					if (targetAngle > maxFOVfrom) {
						continue;
					}

					targetBait = 1 / targetAngle / (float) (targetDistance * targetDistance);
					if (!theTarget1.isAlive()) {
						targetBait /= 10.0F;
					}
					GuidedMissileUtils.checkAllActiveMissilesValidity();
					for (int activeMissileIndex = 0; activeMissileIndex < this.getActiveMissilesSize(); activeMissileIndex++) {
						ActiveMissile theActiveMissile = (ActiveMissile) this.getActiveMissile(activeMissileIndex);
						if (this.actorIsAI(actor)) {
							if (actor.getArmy() == theActiveMissile.getOwnerArmy()) {
								if (theActiveMissile.getVictim() == theTarget1) {
									targetBait = 0.0F;
									break;
								}
							}
						}
						if (this.iDetectorMode != Missile.DETECTOR_TYPE_INFRARED) {
							if (!this.multiTrackingCapable) {
								if (theActiveMissile.getOwner() == actor) {
									if (theActiveMissile.getVictim() != null) {
										if (this.actorIsAI(actor)) return null;
										else return theActiveMissile.getVictim();
									}
								}
							}
						}
					}

					if (targetBait <= maxTargetBait) {
						continue;
					}
					maxTargetBait = targetBait;
					selectedActor = theTarget1;
					theSelectedActorOffset.set(0.0F, 0.0F, 0.0F);

				}

			}

			if (target_bridge != null){
				if (longbridgeDistance < distanceBetween(actor, selectedActor)){
					selectedActor = target_bridge;
					theSelectedActorOffset.set(0.0F, 0.0F, 3.0F);
				}
			}

		} catch (Exception e) {
			EventLog.type("Exception in selectedActor");
			EventLog.type(e.toString());
			EventLog.type(e.getMessage());
		}
		this.selectedActorOffset.set(theSelectedActorOffset);
		if (selectedActor != null)
			if ((this.iDebugLogLevel & 3) == 3) {
				System.out.print("Target=" + selectedActor.getClass().getName());
				System.out.print(" Army=" + selectedActor.getArmy());
				System.out.print(" Distance=" + distanceBetween(actor, selectedActor));
				System.out.println(" Angle=" + angleBetween(actor, selectedActor));
			}
//		EventLog.type("Chosen Target " + selectedActor.getClass().getName());
//		 else
//		 EventLog.type("Chosen Target none");
		return selectedActor;
	}

	public Actor lookForGuidedMissileTargetLocate(Actor actor, float maxFOVfrom, float maxFOVto, double maxDistance) {
		double targetDistance = 0.0D;
		float targetAngle = 0.0F;
		float targetBait = 0.0F;
		float maxTargetBait = 0.0F;
		Actor selectedActor = null;
		Point3f theSelectedActorOffset = new Point3f(0.0F, 0.0F, 0.0F);

		if (!(actor instanceof Aircraft) || (this.iDetectorMode == Missile.DETECTOR_TYPE_MANUAL)) return selectedActor;
		try {
			Autopilotage AP = ((Aircraft)(actor)).FM.AP;

			int wcu = AP.way.Cur();
			boolean flag = false;
			Actor target = null;
			do{
				if (AP.way.curr().Action == 3 || AP.way.curr().getTarget() != null) {
					target = AP.way.curr().getTarget();
					if (target.getSpeed(null) < 1D) {
						targetDistance = distanceBetween(actor, target);
						if (targetDistance <= maxDistance) {
							flag = true;
							break;
						}
					}
				}
				target = null;
				if (AP.way.isLast())
					break;
				AP.way.next();
			} while (true);
			AP.way.setCur(wcu);

			if (flag)
				selectedActor = target;
			if (selectedActor instanceof Bridge)
				theSelectedActorOffset = new Point3f(0.0F, 0.0F, 3.0F);

		} catch (Exception e) {
			EventLog.type("Exception in selectedActor");
			EventLog.type(e.toString());
			EventLog.type(e.getMessage());
		}
		this.selectedActorOffset.set(theSelectedActorOffset);
		return selectedActor;
	}

	public Actor lookForGuidedMissileTargetShip(Actor actor, float maxFOVfrom, float maxFOVto, double maxDistance) {
		double targetDistance = 0.0D;
		float targetAngle = 0.0F;
		float targetBait = 0.0F;
		float maxTargetBait = 0.0F;
		Actor selectedActor = null;
		this.selectedActorOffset.set(new Point3f(0.0F, 0.0F, 0.0F));

		if (!(actor instanceof Aircraft) || (this.iDetectorMode == Missile.DETECTOR_TYPE_MANUAL)) return selectedActor;

		// At Night or bad weather, EOTV (Visible Ray TV camera) doesn't work.
		if (this.iDetectorMode == Missile.DETECTOR_TYPE_IMAGE_EOTV &&
			  (World.Sun().ToSun.z < this.fSunBrightThreshold
			  || ((Mission.curCloudsType() > 3 || (Mission.curCloudsType() > 1 && World.Sun().ToSun.z < this.fSunBrightThreshold + 0.05F))
				  && Mission.curCloudsHeight() > actor.pos.getAbsPoint().z)))
			return selectedActor;

		// Even in the time of ground temperature is too hot , sea water maybe enough cool IR Imaging can work.
		// No denying code about temperature and DETECTOR_TYPE_IMAGE_IR.

		try {
			List list = Engine.targets();
			int k = list.size();
			for (int i1 = 0; i1 < k; i1++) {
				Actor theTarget1 = (Actor) list.get(i1);
				if (theTarget1 instanceof TgtShip) {
					// EventLog.type("Checking Target " + theTarget1.getClass().getName());
					// Not target about objects behind of clouds from the missile's seaker.
					if (Main.cur().clouds != null && Main.cur().clouds.getVisibility(theTarget1.pos.getAbsPoint(), actor.pos.getAbsPoint()) < 1.0F)
						continue;
					targetDistance = distanceBetween(actor, theTarget1);
					// EventLog.type("distance " + targetDistance + " max " + maxDistance);
					if (targetDistance > maxDistance) {
						continue;
					}
					targetAngle = angleBetween(actor, theTarget1);
					// EventLog.type("angle " + targetAngle + " max " + maxFOVfrom);
					if (targetAngle > maxFOVfrom) {
						continue;
					}
					if (theTarget1.pos.getAbsPoint().z < 0D) {
						if (!this.canTrackSubs) {
							continue;
						}
					}

					targetBait = 1 / targetAngle / (float) (targetDistance * targetDistance);
					if (!theTarget1.isAlive()) {
						targetBait /= 10.0F;
					}
					GuidedMissileUtils.checkAllActiveMissilesValidity();
					for (int activeMissileIndex = 0; activeMissileIndex < this.getActiveMissilesSize(); activeMissileIndex++) {
						ActiveMissile theActiveMissile = (ActiveMissile) this.getActiveMissile(activeMissileIndex);
						if (this.actorIsAI(actor)) {
							if (actor.getArmy() == theActiveMissile.getOwnerArmy()) {
								if (theActiveMissile.getVictim() == theTarget1) {
									// EventLog.type("blanked target " + theTarget1.getClass().getName() + " (already tracked)");
									targetBait = 0.0F;
									break;
								}
							}
						}
						if (this.iDetectorMode != Missile.DETECTOR_TYPE_INFRARED) {
							if (!this.multiTrackingCapable) {
								if (theActiveMissile.getOwner() == actor) {
									if (theActiveMissile.getVictim() != null) {
										if (this.actorIsAI(actor)) return null;
										else {
											if (theActiveMissile.getVictim().pos.getAbsPoint().z < 5D) { // don't hit small boats
												if (!this.canTrackSubs) {
													// HUD.log("Offset added");
													this.selectedActorOffset.set(new Point3f(0.0F, 0.0F, 5.0F));
												}
											}
											return theActiveMissile.getVictim();
										}
									}
								}
							}
						}
					}

					if (targetBait <= maxTargetBait) {
						continue;
					}
					maxTargetBait = targetBait;
					selectedActor = theTarget1;
				}

			}

		} catch (Exception e) {
			EventLog.type("Exception in selectedActor");
			EventLog.type(e.toString());
			EventLog.type(e.getMessage());
		}
		if (selectedActor != null) {
			if (selectedActor.pos.getAbsPoint().z < 5D) { // don't hit small boats
				if (!this.canTrackSubs) {
					// HUD.log("Offset added");
					this.selectedActorOffset.set(new Point3f(0.0F, 0.0F, 5.0F));
				}
			}
		}
		// if (selectedActor != null)
		// EventLog.type("Chosen Target " + selectedActor.getClass().getName());
		// else
		// EventLog.type("Chosen Target none");
		return selectedActor;
	}

	public Point3d lookForGuidedMissileTargetPos(Actor actor, float maxFOVfrom, float maxFOVto, double maxDistance, long targetType) {
		if (((targetType & Missile.TARGET_GROUND) != 0) || ((targetType & Missile.TARGET_SHIP) != 0) || ((targetType & Missile.TARGET_LOCATE) != 0)) {
			return this.lookForGuidedMissileTargetGroundPos(actor, maxFOVfrom, maxFOVto, maxDistance);
		}
		return null;
	}

	public Point3d lookForGuidedMissileTargetGroundPos(Actor actor, float maxFOVfrom, float maxFOVto, double maxDistance) {
		double targetDistance = 0.0D;
		float targetAngle = 0.0F;
		float targetBait = 0.0F;
		float maxTargetBait = 0.0F;
		Point3d selectedPos = new Point3d();
		Point3f theSelectedActorOffset = new Point3f(0.0F, 0.0F, 0.0F);
		boolean bFound = false;

		if (!(actor instanceof Aircraft) || ((this.iDetectorMode != Missile.DETECTOR_TYPE_LASER) && (this.iDetectorMode != Missile.DETECTOR_TYPE_SACLOS))) return null;

		if (this.iDetectorMode == Missile.DETECTOR_TYPE_LASER) {
			// superior the Laser spot of this missile's owner than others'
			while ((actor instanceof TypeLaserDesignator) && ((TypeLaserDesignator) actor).getLaserOn()) {
				Point3d point3d = new Point3d();
				point3d = ((TypeLaserDesignator)actor).getLaserSpot();
				if (Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, actor.pos.getAbsPoint()) < 1.0F)
					break;
				targetDistance = actor.pos.getAbsPoint().distance(point3d);
				if (targetDistance > maxDistance)
					break;
				targetAngle = angleBetween(actor, point3d);
				if (targetAngle > maxFOVfrom)
					break;

				selectedPos = point3d;
				bFound = true;
				this.trgtPosOwner = actor;
				break;
			}
			// seek other Laser designator spots when missle's owner doesn't spot Laser
			if (!bFound) {
				List list = Engine.targets();
				int i = list.size();
				for (int j = 0; j < i; j++) {
					Actor theOwner1 = (Actor)list.get(j);
					if ((theOwner1 instanceof TypeLaserDesignator) && ((TypeLaserDesignator) theOwner1).getLaserOn() && theOwner1.getArmy() == actor.getArmy()) {
						Point3d point3d = new Point3d();
						point3d = ((TypeLaserDesignator)theOwner1).getLaserSpot();
						// Not target about objects behind of clouds from the missile's seaker.
						if (Main.cur().clouds != null &&
							(   Main.cur().clouds.getVisibility(point3d, actor.pos.getAbsPoint()) < 1.0F
							 || Main.cur().clouds.getVisibility(point3d, theOwner1.pos.getAbsPoint()) < 1.0F))
							continue;
						targetDistance = actor.pos.getAbsPoint().distance(point3d);
						if (targetDistance > maxDistance)
							continue;
						targetAngle = angleBetween(actor, point3d);
						if (targetAngle > maxFOVfrom)
							continue;

						targetBait = 1 / targetAngle / (float) (targetDistance * targetDistance);
						GuidedMissileUtils.checkAllActiveMissilesValidity();
						for (int activeMissileIndex = 0; activeMissileIndex < this.getActiveMissilesSize(); activeMissileIndex++) {
							ActiveMissile theActiveMissile = (ActiveMissile) this.getActiveMissile(activeMissileIndex);
						}

						if (targetBait <= maxTargetBait)
							continue;

						maxTargetBait = targetBait;
						selectedPos = point3d;
						bFound = true;
						this.trgtPosOwner = theOwner1;
					}
				}
			}
			this.selectedActorOffset.set(theSelectedActorOffset);
			if ((this.iDebugLogLevel & 3) == 3) {
				if (bFound) {
					System.out.print("TargetPos=" + selectedPos);
					System.out.print(" Distance=" + actor.pos.getAbsPoint().distance(selectedPos));
					System.out.println(" Angle=" + angleBetween(actor, selectedPos));
				}
				else
					System.out.println("TargetPos=null");
			}
		}

		if (this.iDetectorMode == Missile.DETECTOR_TYPE_SACLOS) {
			while ((actor instanceof TypeSACLOS) && ((TypeSACLOS) actor).getSACLOSenabled()) {
				Point3d point3d = new Point3d();
				point3d = ((TypeSACLOS)actor).getSACLOStarget();
				if (Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, actor.pos.getAbsPoint()) < 1.0F)
					break;
				targetDistance = actor.pos.getAbsPoint().distance(point3d);
				if (targetDistance > maxDistance)
					break;
				targetAngle = angleBetween(actor, point3d);
				if (targetAngle > maxFOVfrom)
					break;

				selectedPos = point3d;
				selectedPos.z += 1.0D;
				bFound = true;
				this.trgtPosOwner = actor;
				break;
			}
		}
//		if (selectedActor != null)
//		EventLog.type("Chosen Target " + selectedActor.getClass().getName());
//		 else
//		 EventLog.type("Chosen Target none");
		if (bFound)
			return selectedPos;
		else {
			this.trgtPosOwner = null;
			return null;
		}
	}

	public void onAircraftLoaded() {
		this.rocketsList.clear();
		this.createMissileList(this.rocketsList);
		this.setGunNullOwner();
	}

	private void generateMissileDataForPk() {
		if (!(this.missileOwner instanceof Aircraft)) {
			this.missileDataForPk = null;
			return;
		}
		if (this.missileDataForPk == null)
			this.missileDataForPk = new ArrayList();
		else
			this.missileDataForPk.clear();

		Aircraft ownerAircraft = (Aircraft)this.missileOwner;
		BulletEmitter ownerAircraftEmitters [][] = ownerAircraft.FM.CT.Weapons;
		for (int weaponTrigger = 0; weaponTrigger < ownerAircraftEmitters.length; weaponTrigger++) {
			if (weaponTrigger < 2) continue;
			if (weaponTrigger > 7) continue;
			if (ownerAircraftEmitters[weaponTrigger] == null) continue;
			if (ownerAircraft.FM.isPlayers() && (ownerAircraft.FM instanceof RealFlightModel) && ((RealFlightModel)ownerAircraft.FM).isRealMode())
				if (weaponTrigger != ownerAircraft.FM.CT.rocketHookSelected) continue;

			for (int weaponIndex = 0; weaponIndex<ownerAircraftEmitters[weaponTrigger].length; weaponIndex++) {
				if (ownerAircraftEmitters[weaponTrigger][weaponIndex] == null) continue;
				if (!ownerAircraftEmitters[weaponTrigger][weaponIndex].haveBullets()) continue;
				if (!(ownerAircraftEmitters[weaponTrigger][weaponIndex] instanceof MissileGun)) continue;
				MissileGun theMissileGun = (MissileGun)ownerAircraftEmitters[weaponTrigger][weaponIndex];
				Class theMissileClass = theMissileGun.bulletClass();
				MissileDataForPk theMissileDataForPk = new MissileDataForPk();
				theMissileDataForPk.setTriggerNum(weaponTrigger);
				theMissileDataForPk.setMaxLaunchLoad(Property.floatValue(theMissileClass, "maxLockGForce", 99.9F));
				theMissileDataForPk.setMaxAngleToTarget(Property.floatValue(theMissileClass, "PkMaxFOVfrom", 99.9F));
				theMissileDataForPk.setMaxAngleFromTargetAft(Property.floatValue(theMissileClass, "PkMaxFOVto", 99.9F));
				theMissileDataForPk.setMinDist(Property.floatValue(theMissileClass, "PkDistMin", 99.9F));
				theMissileDataForPk.setOptDist(Property.floatValue(theMissileClass, "PkDistOpt", 99.9F));
				theMissileDataForPk.setMaxDist(Property.floatValue(theMissileClass, "PkDistMax", 99.9F));
				this.missileDataForPk.add(theMissileDataForPk);
				break;
			}
		}
	}

	public float Pk(Actor actorFrom, Actor actorTo) {
		float fPkRet = 0.0F;
		if (!(this.missileOwner instanceof Aircraft)) {
			return fPkRet;
		}
		this.generateMissileDataForPk();
		float angleToTarget = angleBetween(actorFrom, actorTo);
		float angleFromTarget = 180.0F - angleBetween(actorTo, actorFrom);
		float distanceToTarget = (float) distanceBetween(actorFrom, actorTo);
		float gForce = ((Aircraft) actorFrom).FM.getOverload();
		if (actorFrom instanceof Aircraft) {
			if (!(((Aircraft) actorFrom).FM instanceof RealFlightModel) || !((RealFlightModel) (((Aircraft) actorFrom).FM)).isRealMode()) {
				this.fMaxG *= 2; // double Max. Launch load for AI.
			}
		}

		Aircraft ownerAircraft = (Aircraft)this.missileOwner;
		int bestMissileTrigger = ownerAircraft.FM.CT.rocketHookSelected;

		for (int missileIndex = 0; missileIndex<this.missileDataForPk.size(); missileIndex++) {
			float fPkRetTemp = 100.0F;
			float fTemp = 0.0F;
			MissileDataForPk theMissileDataForPk = (MissileDataForPk)this.missileDataForPk.get(missileIndex);
			if ((this.iDebugLogLevel & 3) == 3)
				System.out.println("Checking Missile Data: Trigger=" + theMissileDataForPk.getTriggerNum() +
						", maxG=" + theMissileDataForPk.getMaxLaunchLoad() +
						", Angle=" + theMissileDataForPk.getMaxAngleToTarget() +
						", AngleAft=" + theMissileDataForPk.getMaxAngleFromTargetAft() +
						", minDist=" + theMissileDataForPk.getMinDist() +
						", optDist=" + theMissileDataForPk.getOptDist() +
						", maxDist=" + theMissileDataForPk.getMaxDist());
			if ((distanceToTarget > theMissileDataForPk.getMaxDist()) || (distanceToTarget < theMissileDataForPk.getMinDist()) || (angleToTarget > theMissileDataForPk.getMaxAngleToTarget()) || (angleFromTarget > theMissileDataForPk.getMaxAngleFromTargetAft()) || (gForce > theMissileDataForPk.getMaxLaunchLoad())) break;
			if (distanceToTarget > theMissileDataForPk.getOptDist()) {
				fTemp = (distanceToTarget - theMissileDataForPk.getOptDist());
				fTemp /= (theMissileDataForPk.getMaxDist() - theMissileDataForPk.getOptDist());
				fPkRetTemp -= (fTemp * fTemp * 20);
			} else {
				fTemp = (theMissileDataForPk.getOptDist() - distanceToTarget);
				fTemp /= (theMissileDataForPk.getOptDist() - theMissileDataForPk.getMinDist());
				fPkRetTemp -= (fTemp * fTemp * 60);
			}
			fTemp = angleToTarget / theMissileDataForPk.getMaxAngleToTarget();
			fPkRetTemp -= (fTemp * fTemp * 30);
			fTemp = angleFromTarget / theMissileDataForPk.getMaxAngleFromTargetAft();
			fPkRetTemp -= (fTemp * fTemp * 50);
			fTemp = gForce / theMissileDataForPk.getMaxLaunchLoad();
			fPkRetTemp -= (fTemp * fTemp * 30);
			if (fPkRetTemp < 0.0F) {
				fPkRetTemp = 0.0F;
			}
			if (fPkRetTemp > fPkRet) {
				bestMissileTrigger = theMissileDataForPk.getTriggerNum();
				fPkRet = fPkRetTemp;
			}
		}

		if (bestMissileTrigger != ownerAircraft.FM.CT.rocketHookSelected) {
			ownerAircraft.FM.CT.doSetRocketHook(bestMissileTrigger);
		}

//		HUD.training("" + bestMissileTrigger + "=" + fPkRet);

		return fPkRet;
	}

	public float Pk(Actor actorFrom, Point3d pointTo) {
		float fPkRet = 0.0F;
		if (!(this.missileOwner instanceof Aircraft) || pointTo == null) {
			return fPkRet;
		}
		this.generateMissileDataForPk();
		float angleToTarget = angleBetween(actorFrom, pointTo);
		float angleFromTarget = 180.0F - angleBetween(pointTo, actorFrom);
		float distanceToTarget = (float) actorFrom.pos.getAbsPoint().distance(pointTo);
		float gForce = ((Aircraft) actorFrom).FM.getOverload();
		if (actorFrom instanceof Aircraft) {
			if (!(((Aircraft) actorFrom).FM instanceof RealFlightModel) || !((RealFlightModel) (((Aircraft) actorFrom).FM)).isRealMode()) {
				this.fMaxG *= 2; // double Max. Launch load for AI.
			}
		}

		Aircraft ownerAircraft = (Aircraft)this.missileOwner;
		int bestMissileTrigger = ownerAircraft.FM.CT.rocketHookSelected;

		for (int missileIndex = 0; missileIndex<this.missileDataForPk.size(); missileIndex++) {
			float fPkRetTemp = 100.0F;
			float fTemp = 0.0F;
			MissileDataForPk theMissileDataForPk = (MissileDataForPk)this.missileDataForPk.get(missileIndex);
			if ((this.iDebugLogLevel & 3) == 3)
				System.out.println("Checking Missile Data: Trigger=" + theMissileDataForPk.getTriggerNum() +
						", maxG=" + theMissileDataForPk.getMaxLaunchLoad() +
						", Angle=" + theMissileDataForPk.getMaxAngleToTarget() +
						", AngleAft=" + theMissileDataForPk.getMaxAngleFromTargetAft() +
						", minDist=" + theMissileDataForPk.getMinDist() +
						", optDist=" + theMissileDataForPk.getOptDist() +
						", maxDist=" + theMissileDataForPk.getMaxDist());
			if ((distanceToTarget > theMissileDataForPk.getMaxDist()) || (distanceToTarget < theMissileDataForPk.getMinDist()) || (angleToTarget > theMissileDataForPk.getMaxAngleToTarget()) || (angleFromTarget > theMissileDataForPk.getMaxAngleFromTargetAft()) || (gForce > theMissileDataForPk.getMaxLaunchLoad())) break;
			if (distanceToTarget > theMissileDataForPk.getOptDist()) {
				fTemp = (distanceToTarget - theMissileDataForPk.getOptDist());
				fTemp /= (theMissileDataForPk.getMaxDist() - theMissileDataForPk.getOptDist());
				fPkRetTemp -= (fTemp * fTemp * 20);
			} else {
				fTemp = (theMissileDataForPk.getOptDist() - distanceToTarget);
				fTemp /= (theMissileDataForPk.getOptDist() - theMissileDataForPk.getMinDist());
				fPkRetTemp -= (fTemp * fTemp * 60);
			}
			fTemp = angleToTarget / theMissileDataForPk.getMaxAngleToTarget();
			fPkRetTemp -= (fTemp * fTemp * 30);
			fTemp = angleFromTarget / theMissileDataForPk.getMaxAngleFromTargetAft();
			fPkRetTemp -= (fTemp * fTemp * 50);
			fTemp = gForce / theMissileDataForPk.getMaxLaunchLoad();
			fPkRetTemp -= (fTemp * fTemp * 30);
			if (fPkRetTemp < 0.0F) {
				fPkRetTemp = 0.0F;
			}
			if (fPkRetTemp > fPkRet) {
				bestMissileTrigger = theMissileDataForPk.getTriggerNum();
				fPkRet = fPkRetTemp;
			}
		}

		if (bestMissileTrigger != ownerAircraft.FM.CT.rocketHookSelected) {
			ownerAircraft.FM.CT.doSetRocketHook(bestMissileTrigger);
		}

//		HUD.training("" + bestMissileTrigger + "=" + fPkRet);

		return fPkRet;
	}


	public float PkOld(Actor actorFrom, Actor actorTo) {
		float fPkRet = 0.0F;
		float fTemp = 0.0F;
		// if (!((actorFrom instanceof Aircraft) && (actorTo instanceof Aircraft))) {
		// return fPkRet;
		// }
		float angleToTarget = angleBetween(actorFrom, actorTo);
		float angleFromTarget = 180.0F - angleBetween(actorTo, actorFrom);
		float distanceToTarget = (float) distanceBetween(actorFrom, actorTo);
		float gForce = ((Aircraft) actorFrom).FM.getOverload();
//		float fMaxLaunchLoad = this.fPkMaxG;
		if (actorFrom instanceof Aircraft) {
			if (!(((Aircraft) actorFrom).FM instanceof RealFlightModel) || !((RealFlightModel) (((Aircraft) actorFrom).FM)).isRealMode()) {
				this.fMaxG *= 2; // double Max. Launch load for AI.
			}
		}
		fPkRet = 100.0F;
		if ((distanceToTarget > this.fPkMaxDist) || (distanceToTarget < this.fPkMinDist) || (angleToTarget > this.fPkMaxAngle) || (angleFromTarget > this.fPkMaxAngleAft) || (gForce > this.fPkMaxG)) return 0.0F;
		if (distanceToTarget > this.fPkOptDist) {
			fTemp = (distanceToTarget - this.fPkOptDist);
			fTemp /= (this.fPkMaxDist - this.fPkOptDist);
			fPkRet -= (fTemp * fTemp * 20);
		} else {
			fTemp = (this.fPkOptDist - distanceToTarget);
			fTemp /= (this.fPkOptDist - this.fPkMinDist);
			fPkRet -= (fTemp * fTemp * 60);
		}
		fTemp = angleToTarget / this.fPkMaxAngle;
		fPkRet -= (fTemp * fTemp * 30);
		fTemp = angleFromTarget / this.fPkMaxAngleAft;
		fPkRet -= (fTemp * fTemp * 50);
		fTemp = gForce / this.fPkMaxG;
		fPkRet -= (fTemp * fTemp * 30);
		if (fPkRet < 0.0F) {
			fPkRet = 0.0F;
		}
		return fPkRet;
	}

	public void playMissileGrowlLock(boolean isLocked) {
		if (isLocked) {
			if (this.fxMissileToneNoLock != null) {
				this.fxMissileToneNoLock.setPlay(false);
			}
			if (this.fxMissileToneLock != null && this.smplMissileLock != null) {
				this.fxMissileToneLock.play(this.smplMissileLock);
			}
			this.fxMissileToneLock.setPlay(true);
		} else {
			if (this.fxMissileToneLock != null) {
				this.fxMissileToneLock.setPlay(false);
			}
			if (this.fxMissileToneNoLock != null && this.smplMissileNoLock != null) {
				this.fxMissileToneNoLock.play(this.smplMissileNoLock);
			}
			this.fxMissileToneNoLock.setPlay(true);
		}
		// System.out.println("playMissileGrowlLock " + isLocked + " " + (fxMissileToneLock != null) + " " + (smplMissileLock != null) + " " + fxMissileToneLock.isPlaying() + " " + fxMissileToneNoLock.isPlaying());
	}

	public void setAttackDecisionByAI(boolean theAttackDecisionByAI) {
		this.attackDecisionByAI = theAttackDecisionByAI;
	}

	public void setAttackDecisionByWaypoint(boolean theAttackDecisionByWaypoint) {
		this.attackDecisionByWaypoint = theAttackDecisionByWaypoint;
	}

	public void setCanTrackSubs(boolean theCanTrackSubs) {
		this.canTrackSubs = theCanTrackSubs;
	}

	public void setFxMissileToneLock(String value) {
		if (this.missileOwner != World.getPlayerAircraft()) return;
		if (value == null) {
			this.fxMissileToneLock = null;
		}
		// System.out.println("setFxMissileToneLock " + value);
		this.fxMissileToneLock = this.missileOwner.newSound(value, false);
		// if (missileOwner.getRootFX() != null)
		// this.fxMissileToneLock.setParent(missileOwner.getRootFX());
		// this.fxMissileToneLock.setPosition(1, 0, 0);
	}

	public void setFxMissileToneLock(String fxName, float theVolume) {
		if (this.missileOwner != World.getPlayerAircraft()) return;
		this.setFxMissileToneLock(fxName);
		this.setFxMissileToneLockVolume(theVolume);
	}

	public void setFxMissileToneLockVolume(float value) {
		if (this.missileOwner != World.getPlayerAircraft()) return;
		if (this.fxMissileToneLock == null) return;
		this.fxMissileToneLock.setVolume(value);
	}

	// TODO: -- Added/changed Code Multiple Missile Type Selection --

	public void setFxMissileToneNoLock(String value) {
		if (this.missileOwner != World.getPlayerAircraft()) return;
		if (value == null) {
			this.fxMissileToneNoLock = null;
		}
		// System.out.println("setFxMissileToneNoLock " + value);
		this.fxMissileToneNoLock = this.missileOwner.newSound(value, false);
		// if (missileOwner.getRootFX() != null)
		// this.fxMissileToneNoLock.setParent(missileOwner.getRootFX());
		// this.fxMissileToneLock.setPosition(1, 0, 0);
	}

	public void setFxMissileToneNoLock(String fxName, float theVolume) {
		if (this.missileOwner != World.getPlayerAircraft()) return;
		this.setFxMissileToneNoLock(fxName);
		this.setFxMissileToneNoLockVolume(theVolume);
	}

	public void setFxMissileToneNoLockVolume(float value) {
		if (this.missileOwner != World.getPlayerAircraft()) return;
		if (this.fxMissileToneNoLock == null) return;
		this.fxMissileToneNoLock.setVolume(value);
	}

	public void setGunNullOwner() {
		if (!(this.missileOwner instanceof Aircraft)) return;
		Aircraft ownerAircraft = (Aircraft) this.missileOwner;
		try {
			for (int l = 0; l < ownerAircraft.FM.CT.Weapons.length; l++)
				if (ownerAircraft.FM.CT.Weapons[l] != null) {
					for (int l1 = 0; l1 < ownerAircraft.FM.CT.Weapons[l].length; l1++) {
						if ((ownerAircraft.FM.CT.Weapons[l][l1] == null) || (!(ownerAircraft.FM.CT.Weapons[l][l1] instanceof GunNull))) {
							continue;
						}
						((GunNull) ownerAircraft.FM.CT.Weapons[l][l1]).setOwner(ownerAircraft);
					}
				}
		} catch (Exception localException) {
		}
	}

	public void setLeadPercent(float value) {
		this.fLeadPercent = value;
	}

	public void setLockTone(String theLockPrs, String theNoLockPrs, String theLockWav, String theNoLockWav) {
		if (this.missileOwner != World.getPlayerAircraft()) return;
		this.setFxMissileToneLock(theLockPrs);
		this.setFxMissileToneNoLock(theNoLockPrs);
		// TODO: ++ Added/changed Code Multiple Missile Type Selection ++
		this.setSmplMissileLock(theLockWav);
		this.setSmplMissileNoLock(theNoLockWav);
		// TODO: -- Added/changed Code Multiple Missile Type Selection --
	}

	public void setMaxDistance(float value) {
		this.fMaxDistance = value;
	}

	public void setMaxG(float value) {
		this.fMaxG = value;
	}

	public void setMaxPOVfrom(float value) {
		this.fMaxPOVfrom = value;
	}

	public void setMaxPOVto(float value) {
		this.fMaxPOVto = value;
	}

	public void setMillisecondsBetweenMissileLaunchAI(long theMillisecondsBetweenMissileLaunchAI) {
		this.millisecondsBetweenMissileLaunchAI = theMillisecondsBetweenMissileLaunchAI;
	}

	public void setMinPkForAttack(float theMinPkForAttack) {
		this.minPkForAttack = theMinPkForAttack;
	}

	public void setMissileGrowl(int growl) {
		this.iMissileTone = growl;
	}

	public void setMissileMaxSpeedKmh(float value) {
		this.fMissileMaxSpeedKmh = value;
	}

	public void setMissileName(String theMissileName) {
		this.missileName = theMissileName;
	}

	// public void playMissileGrowlLock(boolean isLocked) {
	// if (isLocked) {
	// if (fxMissileToneNoLock != null)
	// fxMissileToneNoLock.cancel();
	// if ((fxMissileToneLock != null) && (smplMissileLock != null))
	// fxMissileToneLock.play(smplMissileLock);
	// } else {
	// if (fxMissileToneLock != null)
	// fxMissileToneLock.cancel();
	// if ((fxMissileToneNoLock != null) && (smplMissileNoLock != null))
	// fxMissileToneNoLock.play(smplMissileNoLock);
	// }
	// }
	//
	// public void cancelMissileGrowl() {
	// if (fxMissileToneLock != null)
	// fxMissileToneLock.cancel();
	// if (fxMissileToneNoLock != null)
	// fxMissileToneNoLock.cancel();
	// }

	public void setMissileOwner(Actor value) {
		this.missileOwner = value;
		this.initLockTones();
	}

	public void setMissileTarget(Actor theTarget) {
		this.trgtMissile = theTarget;
	}

	public void setMissileTargetPos(Point3d p3d) {
		this.trgtPosMissile = p3d;
	}

	public void setMissileTargetPosOwner(Actor theOwner) {
		this.trgtPosOwner = theOwner;
	}

	public void setMultiTrackingCapable(boolean theMultiTrackingCapable) {
		this.multiTrackingCapable = theMultiTrackingCapable;
	}

	public void setPkMaxAngle(float value) {
		this.fPkMaxAngle = value;
	}

	public void setPkMaxAngleAft(float value) {
		this.fPkMaxAngleAft = value;
	}

	public void setPkMaxDist(float value) {
		this.fPkMaxDist = value;
	}

	public void setPkMaxG(float value) {
		this.fPkMaxG = value;
	}

	public void setPkMinDist(float value) {
		this.fPkMinDist = value;
	}

	public void setPkOptDist(float value) {
		this.fPkOptDist = value;
	}

	public void setSmplMissileLock(String value) {
		if (this.missileOwner != World.getPlayerAircraft()) return;
		if (value == null) {
			this.smplMissileLock = null;
			return;
		}
		// System.out.println("setSmplMissileLock " + value);
		// TODO: ++ Added/changed Code Multiple Missile Type Selection ++
		this.smplMissileLock = new Sample(value, 256, 65535);
		// TODO: -- Added/changed Code Multiple Missile Type Selection --
		this.smplMissileLock.setInfinite(true);
	}

	public void setSmplMissileNoLock(String value) {
		if (this.missileOwner != World.getPlayerAircraft()) return;
		if (value == null) {
			this.smplMissileNoLock = null;
			return;
		}
		// System.out.println("setSmplMissileNoLock " + value);
		// TODO: ++ Added/changed Code Multiple Missile Type Selection ++
		this.smplMissileNoLock = new Sample(value, 256, 65535);
		// TODO: -- Added/changed Code Multiple Missile Type Selection --
		this.smplMissileNoLock.setInfinite(true);
	}

	public void setStartLastMissile(long theStartTime) {
		this.tStartLastMissile = theStartTime;
	}

	public void setStepsForFullTurn(float value) {
		this.fStepsForFullTurn = value;
	}

	public void setTargetType(long theTargetType) {
		this.targetType = theTargetType;
	}

	public void shootRocket() {
		if (this.rocketsList.isEmpty()) return;
		// com.maddox.il2.game.HUD.log("Shoot!");
		((RocketGun) this.rocketsList.get(0)).shots(1);
		// this.rocketsList.remove(0);
	}

	public void shotMissile() {
		if (!(this.missileOwner instanceof Aircraft)) return;
//		Exception testException = new Exception("shotMissile");
//		testException.printStackTrace();
		Aircraft ownerAircraft = (Aircraft) this.missileOwner;
		if (this.hasMissiles()) {
			if (NetMissionTrack.isPlaying() || Mission.isNet()) {
				if ((!(ownerAircraft.FM instanceof RealFlightModel)) || (!((RealFlightModel) ownerAircraft.FM).isRealMode())) {
					((RocketGun) this.rocketsList.get(0)).loadBullets(((RocketGun) this.rocketsList.get(0)).bullets() - 1);
				} else if (World.cur().diffCur.Limited_Ammo && this.missileOwner.isNetMirror()) {
					((RocketGun) this.rocketsList.get(0)).loadBullets(((RocketGun) this.rocketsList.get(0)).bullets() - 1);
				}
			}

			if ((World.cur().diffCur.Limited_Ammo) || (ownerAircraft != World.getPlayerAircraft())) {
				 if (((RocketGun) this.rocketsList.get(0)).bullets() == 1) {
					this.rocketsList.remove(0);
				}
			}
			if (ownerAircraft != World.getPlayerAircraft()) {
				Voice.speakAttackByRockets(ownerAircraft);
			}
		}
	}

	public void update() {
		Aircraft ownerAircraft = (Aircraft) this.missileOwner;
		if (ownerAircraft != null) {
			if (ownerAircraft.FM.CT.Weapons[ownerAircraft.FM.CT.rocketHookSelected] == null) ownerAircraft.FM.CT.toggleRocketHook();
		}

		if (this.iDetectorMode == Missile.DETECTOR_TYPE_LASER || this.iDetectorMode == Missile.DETECTOR_TYPE_SACLOS)
			this.setMissileTargetPos(this.lookForGuidedMissileTargetPos(this.missileOwner, this.getMaxPOVfrom(), this.getMaxPOVto(), this.getPkMaxDist(), this.targetType));
		else
			this.setMissileTarget(this.lookForGuidedMissileTarget(this.missileOwner, this.getMaxPOVfrom(), this.getMaxPOVto(), this.getPkMaxDist(), this.targetType));
		this.trgtPk = this.getMissilePk();
		this.checkAIlaunchMissile();
		this.checkPendingMissiles();
		this.checkLockStatus();
		// if(!this.lockTonesInitialized)
		// System.out.println("update before initLockTones finished");
	}

	private static boolean checkActiveMissileInit() {
		if (activeMissiles == null) {
			curMission = Mission.cur();
			activeMissiles = new ArrayList();
			return true;
		}
		if (Mission.cur() != curMission) {
			curMission = Mission.cur();
			if (activeMissiles != null) activeMissiles.clear();
			activeMissiles = new ArrayList();
			return true;
		}
		return false;
	}

	public static ArrayList getActiveMissiles() {
		checkActiveMissileInit();
		return activeMissiles;
	}

	public static void setActiveMissiles(ArrayList activeMissiles) {
		GuidedMissileUtils.activeMissiles = activeMissiles;
	}

	public static void addActiveMissile(ActiveMissile theNewActiveMissile) {
		checkActiveMissileInit();
		addTargetHandledByAi(theNewActiveMissile.getOwnerArmy(), theNewActiveMissile.getVictim());
		activeMissiles.add(theNewActiveMissile);
	}

	public static boolean removeActiveMissile(ActiveMissile theActiveMissile) {
		if (checkActiveMissileInit()) return true;
		if (!activeMissiles.contains(theActiveMissile)) return false;
		addTargetHandledByAi(theActiveMissile.getOwnerArmy(), theActiveMissile.getVictim());
		return activeMissiles.remove(theActiveMissile);
	}

	public static int getActiveMissilesSize() {
		if (checkActiveMissileInit()) return 0;
		return activeMissiles.size();
	}

	public static ActiveMissile getActiveMissile(int index) {
		if (checkActiveMissileInit()) return null;
		if (index >= activeMissiles.size()) return null;
		return (ActiveMissile)activeMissiles.get(index);
	}

	public static boolean checkAllActiveMissilesValidity() {
		if (activeMissiles == null) {
			curMission = Mission.cur();
			activeMissiles = new ArrayList();
			return true;
		}
		boolean retVal = true;
		for (int index = 0; index<activeMissiles.size(); index++) {
			ActiveMissile theActiveMissile = (ActiveMissile)activeMissiles.get(index);
			if (!theActiveMissile.isValidMissile()) {
				activeMissiles.remove(index);
				retVal = false;
			}
		}
		return retVal;
	}

	public static boolean noLaunchSince(long timeMilliseconds, int army) {
		if (checkActiveMissileInit()) return true;
		checkAllActiveMissilesValidity();
		for (int index = 0; index<activeMissiles.size(); index++) {
			ActiveMissile theActiveMissile = (ActiveMissile)activeMissiles.get(index);
			if (theActiveMissile.getOwnerArmy() == army) {
				if (Time.current() - theActiveMissile.getLaunchTime() < timeMilliseconds) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean missilesLeft(BulletEmitter[] missileHook) {
		for (int index=0; index<missileHook.length; index++) {
			if (missileHook[index] != null) {
				if (missileHook[index] instanceof MissileGun) {
					if (missileHook[index].haveBullets()) return true;
				}
			}
		}
		return false;
	}

	private static boolean checkTargetsHandledByAi() {
		if (targetsHandledByAiAlready == null) {
			targetsHandledByAiAlready = new HashMap();
			return true;
		}
		return false;
	}

	public static boolean isTargetHandledByAi(int LauncherArmy, Actor victim) {
		if (victim == null) return true;
		checkTargetsHandledByAi();
		int victimHash = victim.hashCode();
		Long hashKey = new Long(Conversion.longFromTwoInts(LauncherArmy, victimHash));
		long curTime = Time.current();
		if (targetsHandledByAiAlready.containsKey(hashKey)) {
			long hashValue = ((Long)targetsHandledByAiAlready.get(hashKey)).longValue();
			if ((curTime < hashValue)
					|| (curTime > hashValue + minTimeBetweenAIMissileLaunch) ){
				targetsHandledByAiAlready.remove(hashKey);
				return false;
			}
			return true;
		}
		return false;
	}

	public static long addTargetHandledByAi(int LauncherArmy, Actor victim) {
		if (victim==null) return 0;
		checkTargetsHandledByAi();
		int victimHash = victim.hashCode();
		Long hashKey = new Long(Conversion.longFromTwoInts(LauncherArmy, victimHash));
		Long curTime = new Long(Time.current());
		Long retVal = (Long)targetsHandledByAiAlready.put(hashKey, curTime);
		if (retVal == null) return 0;
		return retVal.longValue();
	}

	private static Mission curMission = null;
	private static ArrayList activeMissiles = null;
	private static HashMap targetsHandledByAiAlready = null;

	public static int HEAT_SPREAD_360 = 2; // Engine emits heat in all directions, i.e. Piston engines
	public static int HEAT_SPREAD_AFT = 1; // Engine emits heat aft of A/C, i.e. Jet/Rocket engines
	public static int HEAT_SPREAD_NONE = 0; // Engine produces no heat, i.e. Tow Gliders

	private boolean attackDecisionByAI = false;
	private boolean attackDecisionByWaypoint = false;
	private boolean canTrackSubs = false;
	protected double d = 0.0D;
	protected float deltaAzimuth = 0.0F;
	protected float deltaTangage = 0.0F;
	private final int ENGAGE_AUTO = 0;
	private final int ENGAGE_OFF = -1;
	private final int ENGAGE_ON = 1;
	private int engageMode = 0;
	protected Eff3DActor fl1 = null;
	protected Eff3DActor fl2 = null;
	private float fLeadPercent = 0.0F; // 0 means tail chasing, 100 means full lead tracking
	protected FlightModel fm = null;
	private float fMaxDistance = 4500.0F; // maximum Distance for lockon
	private float fMaxG = 12F; // maximum G-Force during flight
	private float fMaxPOVfrom = 25.0F; // maximum Angle (from launching A/C POV) for lockon
	private float fMaxPOVto = 60.0F; // maximum Angle (from target back POV) for lockon
	protected float fMissileBaseSpeedKmh = 0.0F;
	protected float fMissileMaxSpeedKmh = 2000.0F;
	private float fPkMaxAngle = 30.0F; // maximum Angle (from launching A/C POV) for Pk calculation
	private float fPkMaxAngleAft = 70.0F; // maximum Angle (from target back POV) for Pk calculation
	private float fPkMaxDist = 4500.0F; // maximum Distance for Pk calculation
	private float fPkMaxG = 2.0F; // maximum G-Force for Pk calculation
	private float fPkMinDist = 400.0F; // minimum Distance for Pk calculation
	private float fPkOptDist = 1500.0F; // optimum Distance for Pk calculation
	private float fStepsForFullTurn = 10F; // update steps for maximum control surface output, higher value means slower reaction and smoother flight
	private float fSunBrightThreshold = 0.03F; // EOTV seaker need Sun light brightness higher to work, Sun.z value bigger than this value.
	private SoundFX fxMissileToneLock = null;
	private SoundFX fxMissileToneNoLock = null;
	private int iDetectorMode = 0;
	private int iMissileLockState = 0;
	private int iMissileTone = 0;
	protected double launchKren = 0.0D;
	protected double launchPitch = 0.0D;
	protected double launchYaw = 0.0D;
	private boolean lockTonesInitialized = false;
	private long millisecondsBetweenMissileLaunchAI = 10000L;
	private float minPkForAttack = 25.0F;
	private String missileName = null;
	private Actor missileOwner = null;
	private boolean multiTrackingCapable = true;
	private Class myMissileClass = null;
	private boolean oldBreakControl = false;
	protected float oldDeltaAzimuth = 0.0F;
	protected float oldDeltaTangage = 0.0F;
	protected Orient or = new Orient();
	protected Orient orVictimOffset = null;
	protected Point3d p = new Point3d();
	protected float prevd = 0.0F;
	protected Point3d pT = null;
	protected Point3f pVictimOffset = null;
	public int rocketSelected = 2;
	private ArrayList rocketsList = null;
	private Point3f selectedActorOffset = null;
	private Sample smplMissileLock = null;
	private Sample smplMissileNoLock = null;
	private long targetType = Missile.TARGET_AIR;
	private long tLastSeenEnemy = 0L;
	private long tMissilePrev = 0L;

	private Actor trgtAI = null;

	private Actor trgtMissile = null;
	private Point3d trgtPosMissile = null;
	private Actor trgtPosOwner = null;

	private float trgtPk = 0.0F;

	protected long tStartLastMissile = 0L;

	protected Vector3d v = null;

	protected Actor victim = null;

	protected Vector3d victimSpeed = null;

	private ArrayList missileDataForPk = null;

//	private static long lastAIMissileLaunch[] = new long[16];
	private static final long minTimeBetweenAIMissileLaunch = 1000L;


	private int iDebugLogLevel = 0;
	private boolean bRealisticRadarSelect = false;
}
