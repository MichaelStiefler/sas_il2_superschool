// Source File Name: GuidedMissileUtils.java
// Author:           Storebror
package com.maddox.il2.objects.weapons;

// <editor-fold defaultstate="collapsed" desc="Imports">
import java.util.ArrayList;
import java.util.List;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.JGP.Vector3f;
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
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.FlightModelMainEx;
import com.maddox.il2.fm.Mass;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.NetSafeLog;
import com.maddox.il2.game.Selector;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class GuidedMissileUtils {
	// <editor-fold defaultstate="collapsed" desc="Parameter Fields">
	private Point3f selectedActorOffset = null;
	private SoundFX fxMissileToneLock = null;
	private SoundFX fxMissileToneNoLock = null;
	private Sample smplMissileLock = null;
	private Sample smplMissileNoLock = null;
	private int iMissileLockState = 0;
	private int iMissileTone = 0;
	private long tLastSeenEnemy = 0L;
	private final int ENGAGE_OFF = -1;
	private final int ENGAGE_AUTO = 0;
	private final int ENGAGE_ON = 1;
	private int engageMode = 0;
	private Actor missileOwner = null;
	private boolean oldBreakControl = false;
	private Actor trgtMissile = null;
	private String missileName = null;
	protected FlightModel fm = null;
	protected Eff3DActor fl1 = null;
	protected Eff3DActor fl2 = null;
	protected Orient or = new Orient();
	protected Point3d p = new Point3d();
	protected Orient orVictimOffset = null;
	protected Point3f pVictimOffset = null;
	protected Point3d pT = null;
	protected Vector3d v = null;
	protected Vector3d victimSpeed = null;
	protected long tStartLastMissile = 0L;
	protected float prevd = 0.0F;
	protected float deltaAzimuth = 0.0F;
	protected float deltaTangage = 0.0F;
	protected double d = 0.0D;
	protected Actor victim = null;
	protected float fMissileMaxSpeedKmh = 2000.0F;
	protected float fMissileBaseSpeedKmh = 0.0F;
	protected double launchKren = 0.0D;
	protected double launchPitch = 0.0D;
	protected double launchYaw = 0.0D;
	protected float oldDeltaAzimuth = 0.0F;
	protected float oldDeltaTangage = 0.0F;
	private ArrayList rocketsList = null;
	private float trgtPk = 0.0F;
	private Actor trgtAI = null;
	private long tMissilePrev = 0L;
	private boolean attackDecisionByAI = false;
	private boolean multiTrackingCapable = true;
	private boolean canTrackSubs = false;
	private float minPkForAttack = 25.0F;
	private long millisecondsBetweenMissileLaunchAI = 10000L;
	private long targetType = Missile.TARGET_AIR;
	private float fLeadPercent = 0.0F; // 0 means tail chasing, 100 means full lead tracking
	private float fPkMaxAngle = 30.0F; // maximum Angle (from launching A/C POV) for Pk calculation
	private float fPkMaxAngleAft = 70.0F; // maximum Angle (from target back POV) for Pk calculation
	private float fPkMinDist = 400.0F; // minimum Distance for Pk calculation
	private float fPkOptDist = 1500.0F; // optimum Distance for Pk calculation
	private float fPkMaxDist = 4500.0F; // maximum Distance for Pk calculation
	private float fPkMaxG = 2.0F; // maximum G-Force for Pk calculation
	private float fMaxG = 12F; // maximum G-Force during flight
	private float fStepsForFullTurn = 10F; // update steps for maximum control surface output, higher value means slower reaction and smoother flight
	private float fMaxPOVfrom = 25.0F; // maximum Angle (from launching A/C POV) for lockon
	private float fMaxPOVto = 60.0F; // maximum Angle (from target back POV) for lockon
	private float fMaxDistance = 4500.0F; // maximum Distance for lockon
	private int iDetectorMode = 0;
	public static int HEAT_SPREAD_NONE = 0; // Engine produces no heat, i.e. Tow Gliders
	public static int HEAT_SPREAD_AFT = 1; // Engine emits heat aft of A/C, i.e. Jet/Rocket engines
	public static int HEAT_SPREAD_360 = 2; // Engine emits heat in all directions, i.e. Piston engines
	public int rocketSelected = 2;

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Constructors">

	public GuidedMissileUtils(Actor owner) {
		this.initParams(owner);
	}

	public GuidedMissileUtils(Actor theOwner, float theMissileMaxSpeedMeterPerSecond, float theLeadPercent, float theMaxG, float theStepsForFullTurn, float thePkMaxAngle, float thePkMaxAngleAft,
			float thePkMinDist, float thePkOptDist, float thePkMaxDist, float thePkMaxG, float theMaxPOVfrom, float theMaxPOVto, float theMaxDistance, float theMinPkForAttack,
			long theMillisecondsBetweenMissileLaunchAI, long theTargetType, boolean theAttackDecisionByAI, boolean theMultiTrackingCapable, boolean theCanTrackSubs, String theLockFx,
			String theNoLockFx, String theLockSmpl, String theNoLockSmpl) {
		this.initParams(theOwner, theMissileMaxSpeedMeterPerSecond, theLeadPercent, theMaxG, theStepsForFullTurn, thePkMaxAngle, thePkMaxAngleAft, thePkMinDist, thePkOptDist, thePkMaxDist, thePkMaxG,
				theMaxPOVfrom, theMaxPOVto, theMaxDistance, theMinPkForAttack, theMillisecondsBetweenMissileLaunchAI, theTargetType, theAttackDecisionByAI, theMultiTrackingCapable, theCanTrackSubs,
				theLockFx, theNoLockFx, theLockSmpl, theNoLockSmpl);
	}// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="Parameter Set Functions">

	public void setMissileOwner(Actor value) {
		this.missileOwner = value;
	}

	public void setMissileMaxSpeedKmh(float value) {
		this.fMissileMaxSpeedKmh = value;
	}

	public void setLeadPercent(float value) {
		this.fLeadPercent = value;
	}

	public void setMaxG(float value) {
		this.fMaxG = value;
	}

	public void setStepsForFullTurn(float value) {
		this.fStepsForFullTurn = value;
	}

	public void setPkMaxAngle(float value) {
		this.fPkMaxAngle = value;
	}

	public void setPkMaxAngleAft(float value) {
		this.fPkMaxAngleAft = value;
	}

	public void setPkMinDist(float value) {
		this.fPkMinDist = value;
	}

	public void setPkOptDist(float value) {
		this.fPkOptDist = value;
	}

	public void setPkMaxDist(float value) {
		this.fPkMaxDist = value;
	}

	public void setPkMaxG(float value) {
		this.fPkMaxG = value;
	}

	public void setMaxPOVfrom(float value) {
		this.fMaxPOVfrom = value;
	}

	public void setMaxPOVto(float value) {
		this.fMaxPOVto = value;
	}

	public void setMaxDistance(float value) {
		this.fMaxDistance = value;
	}

	public void setFxMissileToneLock(String value) {
		this.fxMissileToneLock = missileOwner.newSound(value, false);
	}

	public void setFxMissileToneNoLock(String value) {
		this.fxMissileToneNoLock = missileOwner.newSound(value, false);
	}

	public void setSmplMissileLock(String value) {
        // TODO: ++ Added/changed Code Multiple Missile Type Selection ++
		this.smplMissileLock = new Sample(value);
        // TODO: -- Added/changed Code Multiple Missile Type Selection --
		this.smplMissileLock.setInfinite(true);
	}

	public void setSmplMissileNoLock(String value) {
        // TODO: ++ Added/changed Code Multiple Missile Type Selection ++
		this.smplMissileNoLock = new Sample(value);
        // TODO: -- Added/changed Code Multiple Missile Type Selection --
		this.smplMissileNoLock.setInfinite(true);
	}

	public void setLockTone(String theLockPrs, String theNoLockPrs, String theLockWav, String theNoLockWav) {
		this.fxMissileToneLock = missileOwner.newSound(theLockPrs, false);
		this.fxMissileToneNoLock = missileOwner.newSound(theNoLockPrs, false);
        // TODO: ++ Added/changed Code Multiple Missile Type Selection ++
		this.setSmplMissileLock(theLockWav);
		this.setSmplMissileNoLock(theNoLockWav);
        // TODO: -- Added/changed Code Multiple Missile Type Selection --
	}

	public void setMissileName(String theMissileName) {
		this.missileName = theMissileName;
	}

	public String getMissileName() {
		return this.missileName;
	}

	public void setMissileTarget(Actor theTarget) {
		this.trgtMissile = theTarget;
	}

	public void setAttackDecisionByAI(boolean theAttackDecisionByAI) {
		this.attackDecisionByAI = theAttackDecisionByAI;
	}

	public void setMinPkForAttack(float theMinPkForAttack) {
		this.minPkForAttack = theMinPkForAttack;
	}

	public void setMillisecondsBetweenMissileLaunchAI(long theMillisecondsBetweenMissileLaunchAI) {
		this.millisecondsBetweenMissileLaunchAI = theMillisecondsBetweenMissileLaunchAI;
	}

	public void setTargetType(long theTargetType) {
		this.targetType = theTargetType;
	}

	public void setCanTrackSubs(boolean theCanTrackSubs) {
		this.canTrackSubs = theCanTrackSubs;
	}

	public void setMultiTrackingCapable(boolean theMultiTrackingCapable) {
		this.multiTrackingCapable = theMultiTrackingCapable;
	}

	public void setStartLastMissile(long theStartTime) {
		this.tStartLastMissile = theStartTime;
	}

	public void setMissileGrowl(int growl) {
		this.iMissileTone = growl;
	}// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="Parameter Get Functions">

	public Actor getMissileOwner() {
		return this.missileOwner;
	}

	public float getMissileMaxSpeedKmh() {
		return this.fMissileMaxSpeedKmh;
	}

	public float getLeadPercent() {
		return this.fLeadPercent;
	}

	public float getMaxG() {
		return this.fMaxG;
	}

	public float getStepsForFullTurn() {
		return this.fStepsForFullTurn;
	}

	public float getPkMaxAngle() {
		return this.fPkMaxAngle;
	}

	public float getPkMaxAngleAft() {
		return this.fPkMaxAngleAft;
	}

	public float getPkMinDist() {
		return this.fPkMinDist;
	}

	public float getPkOptDist() {
		return this.fPkOptDist;
	}

	public float getPkMaxDist() {
		return this.fPkMaxDist;
	}

	public float getPkMaxG() {
		return this.fPkMaxG;
	}

	public float getMaxPOVfrom() {
		return this.fMaxPOVfrom;
	}

	public float getMaxPOVto() {
		return this.fMaxPOVto;
	}

	public float getMaxDistance() {
		return this.fMaxDistance;
	}

	public SoundFX getFxMissileToneLock() {
		return this.fxMissileToneLock;
	}

	public SoundFX getFxMissileToneNoLock() {
		return this.fxMissileToneNoLock;
	}

	public Sample getSmplMissileLock() {
		return this.smplMissileLock;
	}

	public Sample getSmplMissileNoLock() {
		return this.smplMissileNoLock;
	}

	public Actor getMissileTarget() {
		return this.trgtMissile;
	}

	public int getMissileLockState() {
		return this.iMissileLockState;
	}

	public boolean getAttackDecisionByAI() {
		return this.attackDecisionByAI;
	}

	public float getMinPkForAttack() {
		return this.minPkForAttack;
	}

	public long getMillisecondsBetweenMissileLaunchAI() {
		return this.millisecondsBetweenMissileLaunchAI;
	}

	public long getTargetType() {
		return this.targetType;
	}

//TODO: For Countermeasures:
	
/*	public int getDetectorType() {
		return iDetectorMode;
	}
*/	
	public int getDetectorType() {
		int lockType = 0;
		if (this.iDetectorMode == Missile.DETECTOR_TYPE_INFRARED) {
			lockType = 1;
		} else { if (this.iDetectorMode == Missile.DETECTOR_TYPE_RADAR_BEAMRIDING || this.iDetectorMode == Missile.DETECTOR_TYPE_RADAR_HOMING || this.iDetectorMode == Missile.DETECTOR_TYPE_RADAR_TRACK_VIA_MISSILE)
			lockType = 2;
		}
		return lockType;
	}
	

	public boolean getCanTrackSubs() {
		return this.canTrackSubs;
	}

	public boolean getMultiTrackingCapable() {
		return this.multiTrackingCapable;
	}

	public long getStartLastMissile() {
		return this.tStartLastMissile;
	}

	public int getMissileGrowl() {
		return this.iMissileTone;
	}// </editor-fold>
		// <editor-fold defaultstate="collapsed" desc="Class Init">

	private void initParams(Actor theOwner, float theMissileMaxSpeedMeterPerSecond, float theLeadPercent, float theMaxG, float theStepsForFullTurn, float thePkMaxAngle, float thePkMaxAngleAft,
			float thePkMinDist, float thePkOptDist, float thePkMaxDist, float thePkMaxG, float theMaxPOVfrom, float theMaxPOVto, float theMaxDistance, float theMinPkForAttack,
			long theMillisecondsBetweenMissileLaunchAI, long theTargetType, boolean theAttackDecisionByAI, boolean theMultiTrackingCapable, boolean theCanTrackSubs, String theLockFx,
			String theNoLockFx, String theLockSmpl, String theNoLockSmpl) {
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
		if (theLockFx == null)
			this.fxMissileToneLock = null;
		else
			this.fxMissileToneLock = missileOwner.newSound(theLockFx, false);
		if (theNoLockFx == null)
			this.fxMissileToneNoLock = null;
		else
			this.fxMissileToneNoLock = missileOwner.newSound(theNoLockFx, false);
		if (theLockSmpl == null)
			this.smplMissileLock = null;
		else {
			this.smplMissileLock = new Sample(theLockSmpl, 256, 65535);
			this.smplMissileLock.setInfinite(true);
		}
		if (theNoLockSmpl == null)
			this.smplMissileNoLock = null;
		else {
			this.smplMissileNoLock = new Sample(theNoLockSmpl, 256, 65535);
			this.smplMissileNoLock.setInfinite(true);
		}
	}

	private void initParams(Actor theOwner) {
		this.initCommon();
		if (theOwner instanceof Aircraft) {
			this.missileOwner = theOwner;
		}
	}

	private void initCommon() {
		this.selectedActorOffset = new Point3f();
		this.engageMode = ENGAGE_AUTO;
		this.iMissileLockState = 0;
		this.iMissileTone = 0;
		this.tLastSeenEnemy = Time.current() - 20000;
		this.oldBreakControl = true;
		this.rocketsList = new ArrayList();
		this.tMissilePrev = 0L;
		this.attackDecisionByAI = false;
		this.minPkForAttack = 25.0F;
		this.millisecondsBetweenMissileLaunchAI = 10000L;
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
										if (!(theBulletClass.getName().equals(theMissileClass.getName())))
											continue; // Not the type of missile we're searching for.
									}
									if (Missile.class.isAssignableFrom(theBulletClass)) { // We've found a missile!
										if (theMissileClass == null)
											theMissileClass = theBulletClass;
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
		if (theMissileClass == null) {
			return;
		}
		this.getMissileProperties(theMissileClass);
	}

	public void createMissileList(ArrayList theMissileList) { // default missile init selects first available missile
		this.createMissileList(theMissileList, null);
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
		this.attackDecisionByAI = Property.intValue(theMissileClass, "attackDecisionByAI", 0) != 0;
		this.canTrackSubs = Property.intValue(theMissileClass, "canTrackSubs", 0) != 0;
		this.multiTrackingCapable = Property.intValue(theMissileClass, "multiTrackingCapable", 0) != 0;
		this.minPkForAttack = Property.floatValue(theMissileClass, "minPkForAI", 25.0F);
		this.millisecondsBetweenMissileLaunchAI = Property.longValue(theMissileClass, "timeForNextLaunchAI", 10000L);
		this.targetType = Property.longValue(theMissileClass, "targetType", Missile.TARGET_AIR);

		String strBuf = Property.stringValue(theMissileClass, "fxLock", "weapon.AIM9.lock");
		if (strBuf == null)
			this.fxMissileToneLock = null;
		else
			this.fxMissileToneLock = missileOwner.newSound(strBuf, false);

		strBuf = Property.stringValue(theMissileClass, "fxNoLock", "weapon.AIM9.nolock");
		if (strBuf == null)
			this.fxMissileToneNoLock = null;
		else
			this.fxMissileToneNoLock = missileOwner.newSound(strBuf, false);

		strBuf = Property.stringValue(theMissileClass, "smplLock", "AIM9_lock.wav");
		if (strBuf == null)
			this.smplMissileLock = null;
		else {
			this.smplMissileLock = new Sample(strBuf, 256, 65535);
			this.smplMissileLock.setInfinite(true);
		}

		strBuf = Property.stringValue(theMissileClass, "smplNoLock", "AIM9_no_lock.wav");
		if (strBuf == null)
			this.smplMissileNoLock = null;
		else {
			this.smplMissileNoLock = new Sample(strBuf, 256, 65535);
			this.smplMissileNoLock.setInfinite(true);
		}

		this.missileName = Property.stringValue(theMissileClass, "friendlyName", "Missile");
	}

	public void changeMissileClass(Class theNewMissileClass) { // new function to switch missile types
		this.cancelMissileGrowl(); // stop 
		this.rocketsList.clear();
		this.createMissileList(this.rocketsList, theNewMissileClass);
	}
	// TODO: -- Added/changed Code Multiple Missile Type Selection --

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Static Methods">
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

	public static float angleBetween(Actor actorFrom, Vector3f targetVector) {
		return angleBetween(actorFrom, new Vector3d(targetVector));
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

	public static double distanceBetween(Actor actorFrom, Actor actorTo) {
		double distanceRetVal = 99999.999D;
		if (!(Actor.isValid(actorFrom)) || !(Actor.isValid(actorTo))) {
			return distanceRetVal;
		}
		Loc distanceActorLoc = new Loc();
		Point3d distanceActorPos = new Point3d();
		Point3d distanceTargetPos = new Point3d();
		actorFrom.pos.getAbs(distanceActorLoc);
		distanceActorLoc.get(distanceActorPos);
		actorTo.pos.getAbs(distanceTargetPos);
		distanceRetVal = distanceActorPos.distance(distanceTargetPos);
		return distanceRetVal;
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Method Members">

	public void setGunNullOwner() {
		if (!(this.missileOwner instanceof Aircraft))
			return;
		Aircraft ownerAircraft = (Aircraft) this.missileOwner;
		try {
			for (int l = 0; l < ownerAircraft.FM.CT.Weapons.length; l++)
				if (ownerAircraft.FM.CT.Weapons[l] != null)
					for (int l1 = 0; l1 < ownerAircraft.FM.CT.Weapons[l].length; l1++) {
						if ((ownerAircraft.FM.CT.Weapons[l][l1] == null) || (!(ownerAircraft.FM.CT.Weapons[l][l1] instanceof GunNull)))
							continue;
						((GunNull) ownerAircraft.FM.CT.Weapons[l][l1]).setOwner(ownerAircraft);
					}
		} catch (Exception localException) {
		}
	}

	public Point3f getMissileTargetOffset() {
		return this.getSelectedActorOffset();
	}

	public boolean hasMissiles() {
		return !this.rocketsList.isEmpty();
	}

	public void shotMissile() {
		if (!(this.missileOwner instanceof Aircraft))
			return;
		Aircraft ownerAircraft = (Aircraft) this.missileOwner;
		if (hasMissiles()) {
			if (NetMissionTrack.isPlaying() || Mission.isNet()) {
				if ((!(ownerAircraft.FM instanceof RealFlightModel)) || (!((RealFlightModel) ownerAircraft.FM).isRealMode())) {
					((RocketGun) this.rocketsList.get(0)).loadBullets(0);
				} else if (World.cur().diffCur.Limited_Ammo) {
					((RocketGun) this.rocketsList.get(0)).loadBullets(0);
				}
			}

			if ((World.cur().diffCur.Limited_Ammo) || (ownerAircraft != World.getPlayerAircraft())) {
				this.rocketsList.remove(0);
			}
			if (ownerAircraft != World.getPlayerAircraft()) {
				Voice.speakAttackByRockets(ownerAircraft);
			}
		}
	}

	private float getMissilePk() {
		float thePk = 0.0F;
		if (Actor.isValid(this.getMissileTarget())) {
			thePk = this.Pk(this.missileOwner, this.getMissileTarget());
		}

		return thePk;
	}

	private void checkAIlaunchMissile() {
		if (!(this.missileOwner instanceof Aircraft))
			return;
		Aircraft ownerAircraft = (Aircraft) this.missileOwner;
		if ((((ownerAircraft.FM instanceof RealFlightModel)) && (((RealFlightModel) ownerAircraft.FM).isRealMode())) || (!(ownerAircraft.FM instanceof Pilot))) {
			return;
		}
		if (this.rocketsList.isEmpty()) {
			return;
		}

		if (this.attackDecisionByAI) {
			Pilot pilot = (Pilot) ownerAircraft.FM;
			if (((pilot.get_maneuver() != 27) && (pilot.get_maneuver() != 62) && (pilot.get_maneuver() != 63)) || (pilot.target == null))
				return;
			this.trgtAI = pilot.target.actor;

			for (int activeMissileIndex = 0; activeMissileIndex < Missile.activeMissiles.size(); activeMissileIndex++) {
				ActiveMissile theActiveMissile = (ActiveMissile) Missile.activeMissiles.get(activeMissileIndex);
				if (theActiveMissile.isAI) {
					if (ownerAircraft.getArmy() == theActiveMissile.theOwnerArmy) {
						if (theActiveMissile.theVictim == this.trgtAI) {
							this.trgtAI = null;
							break;
						}
					}
				}
			}

			// if ((!Actor.isValid(this.trgtAI)) || (!(this.trgtAI instanceof Aircraft))) return;
			// this.setMissileTarget(this.trgtAI);
			if (Actor.isValid(this.trgtAI) && ((this.trgtAI instanceof Aircraft) || (this.trgtAI instanceof MissileInterceptable))) {
				this.setMissileTarget(this.trgtAI);
				this.trgtPk = getMissilePk();
			}

		}

		if (ownerAircraft.FM.AP instanceof AutopilotAI) {
			((AutopilotAI) ownerAircraft.FM.AP).setOverrideMissileControl(ownerAircraft.FM.CT, false);
		}
		// com.maddox.il2.game.HUD.training("trgtPk = " + this.trgtPk);
		// HUD.training("trgtPk="+trgtPk);
		if ((this.trgtPk > this.getMinPkForAttack()) && (Actor.isValid(this.getMissileTarget())) && (this.getMissileTarget().getArmy() != ownerAircraft.FM.actor.getArmy())
				&& (Time.current() > this.tMissilePrev + this.getMillisecondsBetweenMissileLaunchAI())) {
			this.tMissilePrev = Time.current();
			ownerAircraft.FM.CT.WeaponControl[2] = true;
			if (ownerAircraft.FM.AP instanceof AutopilotAI) {
				((AutopilotAI) ownerAircraft.FM.AP).setOverrideMissileControl(ownerAircraft.FM.CT, true);
			}
			// HUD.log("AI missile launch");
		}
	}

	public void shootRocket() {
		if (this.rocketsList.isEmpty()) {
			return;
		}
		// com.maddox.il2.game.HUD.log("Shoot!");
		((RocketGun) this.rocketsList.get(0)).shots(1);
		// this.rocketsList.remove(0);
	}

	private void checkPendingMissiles() {
		if (this.rocketsList.isEmpty())
			return;
		if (this.rocketsList.get(0) instanceof RocketGunWithDelay) {
			((RocketGunWithDelay) this.rocketsList.get(0)).checkPendingWeaponRelease();
		}
	}

	public void onAircraftLoaded() {
		this.rocketsList.clear();
		this.createMissileList(this.rocketsList);
		this.setGunNullOwner();
	}

	public void update() {
		this.setMissileTarget(this.lookForGuidedMissileTarget(this.missileOwner, this.getMaxPOVfrom(), this.getMaxPOVto(), this.getPkMaxDist(), this.targetType));
		this.trgtPk = getMissilePk();
		this.checkAIlaunchMissile();
		this.checkPendingMissiles();
		this.checkLockStatus();
	}

	public void playMissileGrowlLock(boolean isLocked) {
		if (isLocked) {
			if (fxMissileToneNoLock != null)
				fxMissileToneNoLock.cancel();
			if ((fxMissileToneLock != null) && (smplMissileLock != null))
				fxMissileToneLock.play(smplMissileLock);
		} else {
			if (fxMissileToneLock != null)
				fxMissileToneLock.cancel();
			if ((fxMissileToneNoLock != null) && (smplMissileNoLock != null))
				fxMissileToneNoLock.play(smplMissileNoLock);
		}
	}

	public void cancelMissileGrowl() {
		if (fxMissileToneLock != null)
			fxMissileToneLock.cancel();
		if (fxMissileToneNoLock != null)
			fxMissileToneNoLock.cancel();
	}

	private void changeMissileGrowl(int iMode) {
		if ((this.missileOwner != World.getPlayerAircraft() || !((RealFlightModel) ((Aircraft) this.missileOwner).FM).isRealMode()) && (((Aircraft) this.missileOwner).FM instanceof Pilot)) {
			return;
		}
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
						if (this.engageMode < ENGAGE_OFF) {
							this.engageMode = ENGAGE_ON;
						}
						switch (this.engageMode) {
						case ENGAGE_OFF:
							if (missileName == null) {
							} else {
								NetSafeLog.log(this.missileOwner, missileName + " Engagement OFF");
							}
							break;
						case ENGAGE_AUTO:
							if (missileName == null) {
							} else {
								NetSafeLog.log(this.missileOwner, missileName + " Engagement AUTO");
							}
							break;
						case ENGAGE_ON:
							if (missileName == null) {
							} else {
								NetSafeLog.log(this.missileOwner, missileName + " Engagement ON");
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
					changeMissileGrowl(0);
					this.iMissileLockState = 0;
				}
				return;
			}
			// if (!((TypeGuidedMissileCarrier) this.missileOwner).hasMissiles()) {
			if (!this.hasMissiles()) {
				if (this.iMissileLockState != 0) {
					changeMissileGrowl(0);
					NetSafeLog.log(this.missileOwner, missileName + " missiles depleted");
					this.iMissileLockState = 0;
				}
				return;
			}
			if (this.engageMode == ENGAGE_OFF) {
				if (this.iMissileLockState != 0) {
					changeMissileGrowl(0);
					NetSafeLog.log(this.missileOwner, missileName + " disengaged");
					this.iMissileLockState = 0;
				}
				return;
			}

			if (Actor.isValid(this.trgtMissile)) {
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
					if (this.engageMode == ENGAGE_ON) {
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

			if ((this.engageMode == ENGAGE_ON) && (this.iMissileLockState == 0)) {
				this.iMissileLockState = 1;
			}
			if ((((Aircraft) this.missileOwner).FM.getOverload() > this.fPkMaxG) && (this.iMissileLockState == 2)) {
				this.iMissileLockState = 1;
			}

			switch (this.iMissileLockState) {
			case 1: {
				if (iOldLockState != 1) {
					changeMissileGrowl(1);
				}
				if (iOldLockState == 0) {
					NetSafeLog.log(this.missileOwner, missileName + " engaged");
				}
				break;
			}
			case 2: {
				if (iOldLockState != 2) {
					changeMissileGrowl(2);
				}
				if (iOldLockState == 0) {
					NetSafeLog.log(this.missileOwner, missileName + " engaged");
				}
				break;
			}
			case 0: {
				if (iOldLockState != 0) {
					changeMissileGrowl(0);
					NetSafeLog.log(this.missileOwner, missileName + " disengaged");
				}
				break;
			}
			default: {
				if (iOldLockState != 0) {
					changeMissileGrowl(0);
					NetSafeLog.log(this.missileOwner, missileName + " disengaged");
				}
				break;
			}
			}

		} catch (Exception exception) {
		}
	}

	public Missile getMissileFromRocketGun(RocketGun theRocketGun) {
		return (Missile) theRocketGun.rocket;
	}

	public Point3f getSelectedActorOffset() {
		return this.selectedActorOffset;
	}

	public float Pk(Actor actorFrom, Actor actorTo) {
		float fPkRet = 0.0F;
		float fTemp = 0.0F;
		// if (!((actorFrom instanceof Aircraft) && (actorTo instanceof Aircraft))) {
		// return fPkRet;
		// }
		float angleToTarget = angleBetween(actorFrom, actorTo);
		float angleFromTarget = 180.0F - angleBetween(actorTo, actorFrom);
		float distanceToTarget = (float) distanceBetween(actorFrom, actorTo);
		float gForce = ((Aircraft) actorFrom).FM.getOverload();
		float fMaxLaunchLoad = this.fPkMaxG;
		if (actorFrom instanceof Aircraft) {
			if (!(((Aircraft) actorFrom).FM instanceof RealFlightModel) || !((RealFlightModel) (((Aircraft) actorFrom).FM)).isRealMode()) {
				fMaxG *= 2; // double Max. Launch load for AI.
			}
		}
		fPkRet = 100.0F;
		if ((distanceToTarget > fPkMaxDist) || (distanceToTarget < fPkMinDist) || (angleToTarget > fPkMaxAngle) || (angleFromTarget > fPkMaxAngleAft) || (gForce > fMaxLaunchLoad)) {
			return 0.0F;
		}
		if (distanceToTarget > fPkOptDist) {
			fTemp = (distanceToTarget - fPkOptDist);
			fTemp /= (fPkMaxDist - fPkOptDist);
			fPkRet -= (fTemp * fTemp * 20);
		} else {
			fTemp = (fPkOptDist - distanceToTarget);
			fTemp /= (fPkOptDist - fPkMinDist);
			fPkRet -= (fTemp * fTemp * 60);
		}
		fTemp = angleToTarget / fPkMaxAngle;
		fPkRet -= (fTemp * fTemp * 30);
		fTemp = angleFromTarget / fPkMaxAngleAft;
		fPkRet -= (fTemp * fTemp * 50);
		fTemp = gForce / fMaxLaunchLoad;
		fPkRet -= (fTemp * fTemp * 30);
		if (fPkRet < 0.0F) {
			fPkRet = 0.0F;
		}
		return fPkRet;
	}

	private int engineHeatSpreadType(Actor theActor) {
		if (!(theActor instanceof Aircraft))
			return HEAT_SPREAD_360;
		EnginesInterface checkEI = ((FlightModelMain) (((SndAircraft) (theActor)).FM)).EI;
		int iRetVal = HEAT_SPREAD_NONE;
		for (int i = 0; i < checkEI.getNum(); i++) {
			int iMotorType = checkEI.engines[i].getType();
			if (iMotorType == Motor._E_TYPE_JET || iMotorType == Motor._E_TYPE_ROCKET || iMotorType == Motor._E_TYPE_ROCKETBOOST || iMotorType == Motor._E_TYPE_PVRD) {
				iRetVal |= HEAT_SPREAD_AFT;
			}
			if (iMotorType == Motor._E_TYPE_INLINE || iMotorType == Motor._E_TYPE_RADIAL || iMotorType == Motor._E_TYPE_HELO_INLINE || iMotorType == Motor._E_TYPE_UNIDENTIFIED) {
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
		if (iMotorType == Motor._E_TYPE_INLINE || iMotorType == Motor._E_TYPE_RADIAL || iMotorType == Motor._E_TYPE_HELO_INLINE || iMotorType == Motor._E_TYPE_UNIDENTIFIED) {
			iRetVal |= HEAT_SPREAD_360;
		}
		return iRetVal;
	}

	public Actor lookForGuidedMissileTarget(Actor actor, float maxFOVfrom, float maxFOVto, double maxDistance) {
		return lookForGuidedMissileTargetAircraft(actor, maxFOVfrom, maxFOVto, maxDistance);
	}

	public Actor lookForGuidedMissileTarget(Actor actor, float maxFOVfrom, float maxFOVto, double maxDistance, long targetType) {
		Actor actorTarget = null;
		if ((targetType & Missile.TARGET_AIR) != 0) {
			actorTarget = lookForGuidedMissileTargetAircraft(actor, maxFOVfrom, maxFOVto, maxDistance);
		} else if ((targetType & Missile.TARGET_GROUND) != 0) {
			actorTarget = lookForGuidedMissileTargetGround(actor, maxFOVfrom, maxFOVto, maxDistance);
		} else if ((targetType & Missile.TARGET_SHIP) != 0) {
			actorTarget = lookForGuidedMissileTargetShip(actor, maxFOVfrom, maxFOVto, maxDistance);
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

		if (this.iDetectorMode == Missile.DETECTOR_TYPE_MANUAL) {
			return selectedActor;
		}
		

		try {
			List list = Engine.targets();
			int k = list.size();
			for (int i1 = 0; i1 < k; i1++) {
				Actor theTarget1 = (Actor) list.get(i1);
				if ((theTarget1 instanceof Aircraft) || (theTarget1 instanceof MissileInterceptable)) {
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
							if ((engineHeatSpreadType(theTarget1) & HEAT_SPREAD_360) == 0) {
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
								if (engineHeatSpreadType(theMotor) == HEAT_SPREAD_NONE) {
									theEngineForce = 0F;
								}
								if (engineHeatSpreadType(theMotor) == HEAT_SPREAD_360) {
									theEngineForce /= 10F;
								}
								if (theEngineForce > maxEngineForce) {
									maxEngineForce = theEngineForce;
									maxEngineForceEngineNo = i;
								}
							}
						} else if (theTarget1 instanceof MissileInterceptable) {
							maxEngineForce = Property.floatValue(theTarget1.getClass(), "force", 1000F);
						}
						targetBait = maxEngineForce / targetAngle / (float) (targetDistance * targetDistance);
						if (!theTarget1.isAlive()) {
							targetBait /= 10F;
						}
						for (int activeMissileIndex = 0; activeMissileIndex < Missile.activeMissiles.size(); activeMissileIndex++) {
							ActiveMissile theActiveMissile = (ActiveMissile) Missile.activeMissiles.get(activeMissileIndex);
							if (theActiveMissile.isAI) {
								if (actor.getArmy() == theActiveMissile.theOwnerArmy) {
									if (theActiveMissile.theVictim == theTarget1) {
										targetBait = 0.0F;
										break;
									}
								}
							}
						}
						if (targetBait > maxTargetBait) {
							maxTargetBait = targetBait;
							selectedActor = theTarget1;
							if (theTarget1 instanceof Aircraft)
								theSelectedActorOffset = theEI.engines[maxEngineForceEngineNo].getEnginePos();
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
						} else if (theTarget1 instanceof MissileInterceptable) {
							fACMass = Property.floatValue(theTarget1.getClass(), "massa", 1000F);
						}
						targetBait = fACMass / targetAngle / (float) (targetDistance * targetDistance);
						if (!theTarget1.isAlive()) {
							targetBait /= 10F;
						}

						for (int activeMissileIndex = 0; activeMissileIndex < Missile.activeMissiles.size(); activeMissileIndex++) {
							ActiveMissile theActiveMissile = (ActiveMissile) Missile.activeMissiles.get(activeMissileIndex);
							if (actorIsAI(actor)) {
								if (actor.getArmy() == theActiveMissile.theOwnerArmy) {
									if (theActiveMissile.theVictim == theTarget1) {
										targetBait = 0.0F;
										break;
									}
								}
							}
							if (!this.multiTrackingCapable) {
								if (theActiveMissile.theOwner == actor) {
									if (theActiveMissile.theVictim != null) {
										if (actorIsAI(actor))
											return null;
										else
											return theActiveMissile.theVictim;
									}
								}
							}
						}

						if (targetBait > maxTargetBait) {
							maxTargetBait = targetBait;
							selectedActor = theTarget1;
							if (theTarget1 instanceof Aircraft) {
								float fGC = FlightModelMainEx.getFmGCenter((FlightModelMain) (((SndAircraft) (theTarget1)).FM));
								theSelectedActorOffset.set(fGC, 0, 0);
							}
						}
						break;
					}
					default: {
						break;
					}
					}

				}
			}
		} catch (Exception e) {
			EventLog.type("Exception in selectedActor");
			EventLog.type(e.toString());
			EventLog.type(e.getMessage());
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

		if (!(actor instanceof Aircraft) || (this.iDetectorMode == Missile.DETECTOR_TYPE_MANUAL)) {
			return selectedActor;
		}
		
		try {
			List list = Engine.targets();
			int k = list.size();
			for (int i1 = 0; i1 < k; i1++) {
				Actor theTarget1 = (Actor) list.get(i1);
				if ((theTarget1 instanceof TgtFlak) || (theTarget1 instanceof TgtTank) || (theTarget1 instanceof TgtTrain) || (theTarget1 instanceof TgtVehicle)) {
					// EventLog.type("Checking Target " + theTarget1.getClass().getName());
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
					for (int activeMissileIndex = 0; activeMissileIndex < Missile.activeMissiles.size(); activeMissileIndex++) {
						ActiveMissile theActiveMissile = (ActiveMissile) Missile.activeMissiles.get(activeMissileIndex);
						if (actorIsAI(actor)) {
							if (actor.getArmy() == theActiveMissile.theOwnerArmy) {
								if (theActiveMissile.theVictim == theTarget1) {
									targetBait = 0.0F;
									break;
								}
							}
						}
						if (this.iDetectorMode != Missile.DETECTOR_TYPE_INFRARED) {
							if (!this.multiTrackingCapable) {
								if (theActiveMissile.theOwner == actor) {
									if (theActiveMissile.theVictim != null) {
										if (actorIsAI(actor))
											return null;
										else
											return theActiveMissile.theVictim;
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

		} catch (Exception e) {
			EventLog.type("Exception in selectedActor");
			EventLog.type(e.toString());
			EventLog.type(e.getMessage());
		}
		this.selectedActorOffset.set(theSelectedActorOffset);
		// if (selectedActor != null)
		// EventLog.type("Chosen Target " + selectedActor.getClass().getName());
		// else
		// EventLog.type("Chosen Target none");
		return selectedActor;
	}

	private boolean actorIsAI(Actor theActor) {
		if (!(theActor instanceof Aircraft))
			return true;
		if (((Aircraft) theActor).FM == null)
			return true;
		if ((theActor != World.getPlayerAircraft() || !((RealFlightModel) ((Aircraft) theActor).FM).isRealMode()) && (((Aircraft) theActor).FM instanceof Pilot))
			return true;
		return false;
	}

	public Actor lookForGuidedMissileTargetShip(Actor actor, float maxFOVfrom, float maxFOVto, double maxDistance) {
		double targetDistance = 0.0D;
		float targetAngle = 0.0F;
		float targetBait = 0.0F;
		float maxTargetBait = 0.0F;
		Actor selectedActor = null;
		this.selectedActorOffset.set(new Point3f(0.0F, 0.0F, 0.0F));

		if (!(actor instanceof Aircraft) || (this.iDetectorMode == Missile.DETECTOR_TYPE_MANUAL)) {
			return selectedActor;
		}

		try {
			List list = Engine.targets();
			int k = list.size();
			for (int i1 = 0; i1 < k; i1++) {
				Actor theTarget1 = (Actor) list.get(i1);
				if (theTarget1 instanceof TgtShip) {
					// EventLog.type("Checking Target " + theTarget1.getClass().getName());
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
					for (int activeMissileIndex = 0; activeMissileIndex < Missile.activeMissiles.size(); activeMissileIndex++) {
						ActiveMissile theActiveMissile = (ActiveMissile) Missile.activeMissiles.get(activeMissileIndex);
						if (actorIsAI(actor)) {
							if (actor.getArmy() == theActiveMissile.theOwnerArmy) {
								if (theActiveMissile.theVictim == theTarget1) {
									// EventLog.type("blanked target " + theTarget1.getClass().getName() + " (already tracked)");
									targetBait = 0.0F;
									break;
								}
							}
						}
						if (this.iDetectorMode != Missile.DETECTOR_TYPE_INFRARED) {
							if (!this.multiTrackingCapable) {
								if (theActiveMissile.theOwner == actor) {
									if (theActiveMissile.theVictim != null) {
										if (actorIsAI(actor))
											return null;
										else {
											if (theActiveMissile.theVictim.pos.getAbsPoint().z < 5D) { // don't hit small boats
												if (!this.canTrackSubs) {
													// HUD.log("Offset added");
													this.selectedActorOffset.set(new Point3f(0.0F, 0.0F, 5.0F));
												}
											}
											return theActiveMissile.theVictim;
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
	// </editor-fold>
}
