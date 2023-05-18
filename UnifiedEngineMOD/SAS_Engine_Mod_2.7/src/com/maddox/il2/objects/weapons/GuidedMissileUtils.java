// Source File Name:   GuidedMissileUtils.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.ai.ground.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.bridges.Bridge;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.bridges.LongBridge;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.maddox.sas1946.il2.util.Conversion;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun, GunNull, Missile, ActiveMissile, 
//            MissileInterceptable, RocketGunWithDelay

public class GuidedMissileUtils
{

    public GuidedMissileUtils(Actor owner)
    {
        selectedActorOffset = null;
        fxMissileToneLock = null;
        fxMissileToneNoLock = null;
        smplMissileLock = null;
        smplMissileNoLock = null;
        iMissileLockState = 0;
        iMissileTone = 0;
        tLastSeenEnemy = 0L;
        engageMode = 0;
        missileOwner = null;
        oldBreakControl = false;
        trgtMissile = null;
        missileName = null;
        fm = null;
        fl1 = null;
        fl2 = null;
        or = new Orient();
        p = new Point3d();
        orVictimOffset = null;
        pVictimOffset = null;
        pT = null;
        v = null;
        victimSpeed = null;
        tStartLastMissile = 0L;
        prevd = 0.0F;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        d = 0.0D;
        victim = null;
        fMissileMaxSpeedKmh = 2000F;
        fMissileBaseSpeedKmh = 0.0F;
        launchKren = 0.0D;
        launchPitch = 0.0D;
        launchYaw = 0.0D;
        oldDeltaAzimuth = 0.0F;
        oldDeltaTangage = 0.0F;
        rocketsList = null;
        trgtPk = 0.0F;
        trgtAI = null;
        tMissilePrev = 0L;
        attackDecisionByAI = false;
        attackDecisionByWaypoint = false;
        multiTrackingCapable = true;
        canTrackSubs = false;
        minPkForAttack = 25F;
        millisecondsBetweenMissileLaunchAI = 10000L;
        targetType = 1L;
        fLeadPercent = 0.0F;
        fPkMaxAngle = 30F;
        fPkMaxAngleAft = 70F;
        fPkMinDist = 400F;
        fPkOptDist = 1500F;
        fPkMaxDist = 4500F;
        fPkMaxG = 2.0F;
        fMaxG = 12F;
        fStepsForFullTurn = 10F;
        fMaxPOVfrom = 25F;
        fMaxPOVto = 60F;
        fMaxDistance = 4500F;
        iDetectorMode = 0;
        rocketSelected = 2;
        initParams(owner);
    }

    public GuidedMissileUtils(Actor theOwner, float theMissileMaxSpeedMeterPerSecond, float theLeadPercent, float theMaxG, float theStepsForFullTurn, float thePkMaxAngle, float thePkMaxAngleAft, 
            float thePkMinDist, float thePkOptDist, float thePkMaxDist, float thePkMaxG, float theMaxPOVfrom, float theMaxPOVto, float theMaxDistance, 
            float theMinPkForAttack, long theMillisecondsBetweenMissileLaunchAI, long theTargetType, boolean theAttackDecisionByAI, boolean theMultiTrackingCapable, 
            boolean theCanTrackSubs, String theLockFx, String theNoLockFx, String theLockSmpl, String theNoLockSmpl)
    {
        selectedActorOffset = null;
        fxMissileToneLock = null;
        fxMissileToneNoLock = null;
        smplMissileLock = null;
        smplMissileNoLock = null;
        iMissileLockState = 0;
        iMissileTone = 0;
        tLastSeenEnemy = 0L;
        engageMode = 0;
        missileOwner = null;
        oldBreakControl = false;
        trgtMissile = null;
        missileName = null;
        fm = null;
        fl1 = null;
        fl2 = null;
        or = new Orient();
        p = new Point3d();
        orVictimOffset = null;
        pVictimOffset = null;
        pT = null;
        v = null;
        victimSpeed = null;
        tStartLastMissile = 0L;
        prevd = 0.0F;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        d = 0.0D;
        victim = null;
        fMissileMaxSpeedKmh = 2000F;
        fMissileBaseSpeedKmh = 0.0F;
        launchKren = 0.0D;
        launchPitch = 0.0D;
        launchYaw = 0.0D;
        oldDeltaAzimuth = 0.0F;
        oldDeltaTangage = 0.0F;
        rocketsList = null;
        trgtPk = 0.0F;
        trgtAI = null;
        tMissilePrev = 0L;
        attackDecisionByAI = false;
        attackDecisionByWaypoint = false;
        multiTrackingCapable = true;
        canTrackSubs = false;
        minPkForAttack = 25F;
        millisecondsBetweenMissileLaunchAI = 10000L;
        targetType = 1L;
        fLeadPercent = 0.0F;
        fPkMaxAngle = 30F;
        fPkMaxAngleAft = 70F;
        fPkMinDist = 400F;
        fPkOptDist = 1500F;
        fPkMaxDist = 4500F;
        fPkMaxG = 2.0F;
        fMaxG = 12F;
        fStepsForFullTurn = 10F;
        fMaxPOVfrom = 25F;
        fMaxPOVto = 60F;
        fMaxDistance = 4500F;
        iDetectorMode = 0;
        rocketSelected = 2;
        initParams(theOwner, theMissileMaxSpeedMeterPerSecond, theLeadPercent, theMaxG, theStepsForFullTurn, thePkMaxAngle, thePkMaxAngleAft, thePkMinDist, thePkOptDist, thePkMaxDist, thePkMaxG, theMaxPOVfrom, theMaxPOVto, theMaxDistance, theMinPkForAttack, theMillisecondsBetweenMissileLaunchAI, theTargetType, theAttackDecisionByAI, theMultiTrackingCapable, theCanTrackSubs, theLockFx, theNoLockFx, theLockSmpl, theNoLockSmpl);
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

    public void setMissileOwner(Actor value)
    {
        missileOwner = value;
    }

    public void setMissileMaxSpeedKmh(float value)
    {
        fMissileMaxSpeedKmh = value;
    }

    public void setLeadPercent(float value)
    {
        fLeadPercent = value;
    }

    public void setMaxG(float value)
    {
        fMaxG = value;
    }

    public void setStepsForFullTurn(float value)
    {
        fStepsForFullTurn = value;
    }

    public void setPkMaxAngle(float value)
    {
        fPkMaxAngle = value;
    }

    public void setPkMaxAngleAft(float value)
    {
        fPkMaxAngleAft = value;
    }

    public void setPkMinDist(float value)
    {
        fPkMinDist = value;
    }

    public void setPkOptDist(float value)
    {
        fPkOptDist = value;
    }

    public void setPkMaxDist(float value)
    {
        fPkMaxDist = value;
    }

    public void setPkMaxG(float value)
    {
        fPkMaxG = value;
    }

    public void setMaxPOVfrom(float value)
    {
        fMaxPOVfrom = value;
    }

    public void setMaxPOVto(float value)
    {
        fMaxPOVto = value;
    }

    public void setMaxDistance(float value)
    {
        fMaxDistance = value;
    }

    public void setFxMissileToneLock(String value)
    {
        fxMissileToneLock = missileOwner.newSound(value, false);
    }

    public void setFxMissileToneNoLock(String value)
    {
        fxMissileToneNoLock = missileOwner.newSound(value, false);
    }

    public void setSmplMissileLock(String value)
    {
        smplMissileLock = new Sample(value);
        smplMissileLock.setInfinite(true);
    }

    public void setSmplMissileNoLock(String value)
    {
        smplMissileNoLock = new Sample(value);
        smplMissileNoLock.setInfinite(true);
    }

    public void setLockTone(String theLockPrs, String theNoLockPrs, String theLockWav, String theNoLockWav)
    {
        fxMissileToneLock = missileOwner.newSound(theLockPrs, false);
        fxMissileToneNoLock = missileOwner.newSound(theNoLockPrs, false);
        setSmplMissileLock(theLockWav);
        setSmplMissileNoLock(theNoLockWav);
    }

    public void setMissileName(String theMissileName)
    {
        missileName = theMissileName;
    }

    public String getMissileName()
    {
        return missileName;
    }

    public void setMissileTarget(Actor theTarget)
    {
        trgtMissile = theTarget;
    }

    public void setAttackDecisionByAI(boolean theAttackDecisionByAI)
    {
        attackDecisionByAI = theAttackDecisionByAI;
    }

    public void setAttackDecisionByWaypoint(boolean theAttackDecisionByWaypoint)
    {
        attackDecisionByWaypoint = theAttackDecisionByWaypoint;
    }

    public void setMinPkForAttack(float theMinPkForAttack)
    {
        minPkForAttack = theMinPkForAttack;
    }

    public void setMillisecondsBetweenMissileLaunchAI(long theMillisecondsBetweenMissileLaunchAI)
    {
        millisecondsBetweenMissileLaunchAI = theMillisecondsBetweenMissileLaunchAI;
    }

    public void setTargetType(long theTargetType)
    {
        targetType = theTargetType;
    }

    public void setCanTrackSubs(boolean theCanTrackSubs)
    {
        canTrackSubs = theCanTrackSubs;
    }

    public void setMultiTrackingCapable(boolean theMultiTrackingCapable)
    {
        multiTrackingCapable = theMultiTrackingCapable;
    }

    public void setStartLastMissile(long theStartTime)
    {
        tStartLastMissile = theStartTime;
    }

    public void setMissileGrowl(int growl)
    {
        iMissileTone = growl;
    }

    public Actor getMissileOwner()
    {
        return missileOwner;
    }

    public float getMissileMaxSpeedKmh()
    {
        return fMissileMaxSpeedKmh;
    }

    public float getLeadPercent()
    {
        return fLeadPercent;
    }

    public float getMaxG()
    {
        return fMaxG;
    }

    public float getStepsForFullTurn()
    {
        return fStepsForFullTurn;
    }

    public float getPkMaxAngle()
    {
        return fPkMaxAngle;
    }

    public float getPkMaxAngleAft()
    {
        return fPkMaxAngleAft;
    }

    public float getPkMinDist()
    {
        return fPkMinDist;
    }

    public float getPkOptDist()
    {
        return fPkOptDist;
    }

    public float getPkMaxDist()
    {
        return fPkMaxDist;
    }

    public float getPkMaxG()
    {
        return fPkMaxG;
    }

    public float getMaxPOVfrom()
    {
        return fMaxPOVfrom;
    }

    public float getMaxPOVto()
    {
        return fMaxPOVto;
    }

    public float getMaxDistance()
    {
        return fMaxDistance;
    }

    public SoundFX getFxMissileToneLock()
    {
        return fxMissileToneLock;
    }

    public SoundFX getFxMissileToneNoLock()
    {
        return fxMissileToneNoLock;
    }

    public Sample getSmplMissileLock()
    {
        return smplMissileLock;
    }

    public Sample getSmplMissileNoLock()
    {
        return smplMissileNoLock;
    }

    public Actor getMissileTarget()
    {
        return trgtMissile;
    }

    public int getMissileLockState()
    {
        return iMissileLockState;
    }

    public boolean getAttackDecisionByAI()
    {
        return attackDecisionByAI;
    }

    public boolean getAttackDecisionByWaypoint()
    {
        return attackDecisionByWaypoint;
    }

    public float getMinPkForAttack()
    {
        return minPkForAttack;
    }

    public long getMillisecondsBetweenMissileLaunchAI()
    {
        return millisecondsBetweenMissileLaunchAI;
    }

    public long getTargetType()
    {
        return targetType;
    }

    public int getDetectorType()
    {
        int lockType = 0;
        if(iDetectorMode == 1)
            lockType = 1;
        else
        if(iDetectorMode == 3 || iDetectorMode == 2 || iDetectorMode == 4)
            lockType = 2;
        return lockType;
    }

    public boolean getCanTrackSubs()
    {
        return canTrackSubs;
    }

    public boolean getMultiTrackingCapable()
    {
        return multiTrackingCapable;
    }

    public long getStartLastMissile()
    {
        return tStartLastMissile;
    }

    public int getMissileGrowl()
    {
        return iMissileTone;
    }

    private void initParams(Actor theOwner, float theMissileMaxSpeedMeterPerSecond, float theLeadPercent, float theMaxG, float theStepsForFullTurn, float thePkMaxAngle, float thePkMaxAngleAft, 
            float thePkMinDist, float thePkOptDist, float thePkMaxDist, float thePkMaxG, float theMaxPOVfrom, float theMaxPOVto, float theMaxDistance, 
            float theMinPkForAttack, long theMillisecondsBetweenMissileLaunchAI, long theTargetType, boolean theAttackDecisionByAI, boolean theMultiTrackingCapable, 
            boolean theCanTrackSubs, String theLockFx, String theNoLockFx, String theLockSmpl, String theNoLockSmpl)
    {
        initCommon();
        missileOwner = theOwner;
        fMissileMaxSpeedKmh = theMissileMaxSpeedMeterPerSecond;
        fLeadPercent = theLeadPercent;
        fMaxG = theMaxG;
        fStepsForFullTurn = theStepsForFullTurn;
        fPkMaxAngle = thePkMaxAngle;
        fPkMaxAngleAft = thePkMaxAngleAft;
        fPkMinDist = thePkMinDist;
        fPkOptDist = thePkOptDist;
        fPkMaxDist = thePkMaxDist;
        fPkMaxG = thePkMaxG;
        fMaxPOVfrom = theMaxPOVfrom;
        fMaxPOVto = theMaxPOVto;
        fMaxDistance = theMaxDistance;
        attackDecisionByAI = theAttackDecisionByAI;
        minPkForAttack = theMinPkForAttack;
        millisecondsBetweenMissileLaunchAI = theMillisecondsBetweenMissileLaunchAI;
        targetType = theTargetType;
        canTrackSubs = theCanTrackSubs;
        multiTrackingCapable = theMultiTrackingCapable;
        if(theLockFx == null)
            fxMissileToneLock = null;
        else
            fxMissileToneLock = missileOwner.newSound(theLockFx, false);
        if(theNoLockFx == null)
            fxMissileToneNoLock = null;
        else
            fxMissileToneNoLock = missileOwner.newSound(theNoLockFx, false);
        if(theLockSmpl == null)
        {
            smplMissileLock = null;
        } else
        {
            smplMissileLock = new Sample(theLockSmpl, 256, 65535);
            smplMissileLock.setInfinite(true);
        }
        if(theNoLockSmpl == null)
        {
            smplMissileNoLock = null;
        } else
        {
            smplMissileNoLock = new Sample(theNoLockSmpl, 256, 65535);
            smplMissileNoLock.setInfinite(true);
        }
    }

    private void initParams(Actor theOwner)
    {
        initCommon();
        if(theOwner instanceof Aircraft)
            missileOwner = theOwner;
    }

    private void initCommon()
    {
        selectedActorOffset = new Point3f();
        engageMode = 0;
        iMissileLockState = 0;
        iMissileTone = 0;
        tLastSeenEnemy = Time.current() - 20000L;
        oldBreakControl = true;
        rocketsList = new ArrayList();
        tMissilePrev = 0L;
        attackDecisionByAI = false;
        attackDecisionByWaypoint = false;
        minPkForAttack = 25F;
        millisecondsBetweenMissileLaunchAI = 10000L;
    }

    public void createMissileList(ArrayList theMissileList, Class theMissileClass)
    {
        Aircraft theMissileCarrier = (Aircraft)missileOwner;
        try
        {
            for(int l = 0; l < ((FlightModelMain) (((SndAircraft) (theMissileCarrier)).FM)).CT.Weapons.length; l++)
                if(((FlightModelMain) (((SndAircraft) (theMissileCarrier)).FM)).CT.Weapons[l] != null)
                {
                    for(int l1 = 0; l1 < ((FlightModelMain) (((SndAircraft) (theMissileCarrier)).FM)).CT.Weapons[l].length; l1++)
                        if(((FlightModelMain) (((SndAircraft) (theMissileCarrier)).FM)).CT.Weapons[l][l1] != null && (((FlightModelMain) (((SndAircraft) (theMissileCarrier)).FM)).CT.Weapons[l][l1] instanceof RocketGun))
                        {
                            RocketGun theRocketGun = (RocketGun)((FlightModelMain) (((SndAircraft) (theMissileCarrier)).FM)).CT.Weapons[l][l1];
                            if(theRocketGun.haveBullets())
                            {
                                Class theBulletClass = theRocketGun.bulletClass();
                                if((theMissileClass == null || theBulletClass.getName().equals(theMissileClass.getName())) && com.maddox.il2.objects.weapons.Missile.class.isAssignableFrom(theBulletClass))
                                {
                                    if(theMissileClass == null)
                                        theMissileClass = theBulletClass;
                                    theMissileList.add(((FlightModelMain) (((SndAircraft) (theMissileCarrier)).FM)).CT.Weapons[l][l1]);
                                }
                            }
                        }

                }

        }
        catch(Exception exception)
        {
            EventLog.type("Exception in initParams: " + exception.getMessage());
        }
        if(theMissileClass == null)
        {
            return;
        } else
        {
            getMissileProperties(theMissileClass);
            return;
        }
    }

    public void createMissileList(ArrayList theMissileList)
    {
        createMissileList(theMissileList, null);
    }

    public void getMissileProperties(Class theMissileClass)
    {
        fPkMaxG = Property.floatValue(theMissileClass, "maxLockGForce", 99.9F);
        fMaxPOVfrom = Property.floatValue(theMissileClass, "maxFOVfrom", 99.9F);
        fMaxPOVto = Property.floatValue(theMissileClass, "maxFOVto", 99.9F);
        fPkMaxAngle = Property.floatValue(theMissileClass, "PkMaxFOVfrom", 99.9F);
        fPkMaxAngleAft = Property.floatValue(theMissileClass, "PkMaxFOVto", 99.9F);
        fPkMinDist = Property.floatValue(theMissileClass, "PkDistMin", 99.9F);
        fPkOptDist = Property.floatValue(theMissileClass, "PkDistOpt", 99.9F);
        fPkMaxDist = Property.floatValue(theMissileClass, "PkDistMax", 99.9F);
        fMissileMaxSpeedKmh = Property.floatValue(theMissileClass, "maxSpeed", 99.9F);
        fLeadPercent = Property.floatValue(theMissileClass, "leadPercent", 99.9F);
        fMaxG = Property.floatValue(theMissileClass, "maxGForce", 99.9F);
        iDetectorMode = Property.intValue(theMissileClass, "detectorType", 0);
        attackDecisionByAI = Property.intValue(theMissileClass, "attackDecisionByAI", 0) == 1;
        attackDecisionByWaypoint = Property.intValue(theMissileClass, "attackDecisionByAI", 0) == 2;
        canTrackSubs = Property.intValue(theMissileClass, "canTrackSubs", 0) != 0;
        multiTrackingCapable = Property.intValue(theMissileClass, "multiTrackingCapable", 0) != 0;
        minPkForAttack = Property.floatValue(theMissileClass, "minPkForAI", 25F);
        millisecondsBetweenMissileLaunchAI = Property.longValue(theMissileClass, "timeForNextLaunchAI", 10000L);
        targetType = Property.longValue(theMissileClass, "targetType", 1L);
        String strBuf = Property.stringValue(theMissileClass, "fxLock", "weapon.AIM9.lock");
        if(strBuf == null)
            fxMissileToneLock = null;
        else
            fxMissileToneLock = missileOwner.newSound(strBuf, false);
        strBuf = Property.stringValue(theMissileClass, "fxNoLock", "weapon.AIM9.nolock");
        if(strBuf == null)
            fxMissileToneNoLock = null;
        else
            fxMissileToneNoLock = missileOwner.newSound(strBuf, false);
        strBuf = Property.stringValue(theMissileClass, "smplLock", "AIM9_lock.wav");
        if(strBuf == null)
        {
            smplMissileLock = null;
        } else
        {
            smplMissileLock = new Sample(strBuf, 256, 65535);
            smplMissileLock.setInfinite(true);
        }
        strBuf = Property.stringValue(theMissileClass, "smplNoLock", "AIM9_no_lock.wav");
        if(strBuf == null)
        {
            smplMissileNoLock = null;
        } else
        {
            smplMissileNoLock = new Sample(strBuf, 256, 65535);
            smplMissileNoLock.setInfinite(true);
        }
        missileName = Property.stringValue(theMissileClass, "friendlyName", "Missile");
    }

    public void changeMissileClass(Class theNewMissileClass)
    {
        cancelMissileGrowl();
        rocketsList.clear();
        createMissileList(rocketsList, theNewMissileClass);
    }

    public static float angleBetween(Actor actorFrom, Actor actorTo)
    {
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
        angleRetVal = Geom.RAD2DEG((float)Math.acos(angleDoubleTemp));
        return angleRetVal;
    }

    public static float angleBetween(Actor actorFrom, Vector3f targetVector)
    {
        return angleBetween(actorFrom, new Vector3d(targetVector));
    }

    public static float angleBetween(Actor actorFrom, Vector3d targetVector)
    {
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
        return Geom.RAD2DEG((float)Math.acos(angleDoubleTemp));
    }

    public static float angleActorBetween(Actor actorFrom, Actor actorTo)
    {
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
        angleRetVal = Geom.RAD2DEG((float)Math.acos(angleDoubleTemp));
        return angleRetVal;
    }

    public static double distanceBetween(Actor actorFrom, Actor actorTo)
    {
        double distanceRetVal = 99999.998999999996D;
        if(!Actor.isValid(actorFrom) || !Actor.isValid(actorTo))
        {
            return distanceRetVal;
        } else
        {
            Loc distanceActorLoc = new Loc();
            Point3d distanceActorPos = new Point3d();
            Point3d distanceTargetPos = new Point3d();
            actorFrom.pos.getAbs(distanceActorLoc);
            distanceActorLoc.get(distanceActorPos);
            actorTo.pos.getAbs(distanceTargetPos);
            distanceRetVal = distanceActorPos.distance(distanceTargetPos);
            return distanceRetVal;
        }
    }

    public void setGunNullOwner()
    {
        if(!(missileOwner instanceof Aircraft))
            return;
        Aircraft ownerAircraft = (Aircraft)missileOwner;
        try
        {
            for(int l = 0; l < ((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).CT.Weapons.length; l++)
                if(((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).CT.Weapons[l] != null)
                {
                    for(int l1 = 0; l1 < ((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).CT.Weapons[l].length; l1++)
                        if(((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).CT.Weapons[l][l1] != null && (((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).CT.Weapons[l][l1] instanceof GunNull))
                            ((GunNull)((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).CT.Weapons[l][l1]).setOwner(ownerAircraft);

                }

        }
        catch(Exception exception) { }
    }

    public Point3f getMissileTargetOffset()
    {
        return getSelectedActorOffset();
    }

    public boolean hasMissiles()
    {
        return !rocketsList.isEmpty();
    }

    public void shotMissile()
    {
        if(!(missileOwner instanceof Aircraft))
            return;
        Aircraft ownerAircraft = (Aircraft)missileOwner;
        if(hasMissiles())
        {
            if(NetMissionTrack.isPlaying() || Mission.isNet())
                if(!(((SndAircraft) (ownerAircraft)).FM instanceof RealFlightModel) || !((RealFlightModel)((SndAircraft) (ownerAircraft)).FM).isRealMode())
                    ((RocketGun) this.rocketsList.get(0)).loadBullets(((RocketGun) this.rocketsList.get(0)).bullets() - 1);
                else
                if(World.cur().diffCur.Limited_Ammo)
                    ((RocketGun) this.rocketsList.get(0)).loadBullets(((RocketGun) this.rocketsList.get(0)).bullets() - 1);
            if ((World.cur().diffCur.Limited_Ammo) || (ownerAircraft != World.getPlayerAircraft())) {
                if (((RocketGun) this.rocketsList.get(0)).bullets() == 1) {
                    this.rocketsList.remove(0);
                }
            }
            if(ownerAircraft != World.getPlayerAircraft())
                Voice.speakAttackByRockets(ownerAircraft);
        }
    }

    private float getMissilePk()
    {
        float thePk = 0.0F;
        if(Actor.isValid(getMissileTarget()))
            thePk = Pk(missileOwner, getMissileTarget());
        return thePk;
    }

    private void checkAIlaunchMissile()
    {
        if(!(missileOwner instanceof Aircraft))
            return;
        Aircraft ownerAircraft = (Aircraft)missileOwner;
        if((((SndAircraft) (ownerAircraft)).FM instanceof RealFlightModel) && ((RealFlightModel)((SndAircraft) (ownerAircraft)).FM).isRealMode() || !(((SndAircraft) (ownerAircraft)).FM instanceof Pilot))
            return;
        if(rocketsList.isEmpty())
            return;
        if(attackDecisionByAI || attackDecisionByWaypoint)
        {
            Autopilotage AP = ((Aircraft)(this.missileOwner)).FM.AP;
            if (attackDecisionByWaypoint && (AP.way.curr().Action != 3 || AP.way.curr().getTarget() == null)) return;
            Pilot pilot = (Pilot)((SndAircraft) (ownerAircraft)).FM;
            if(pilot.get_maneuver() != 27 && pilot.get_maneuver() != 62 && pilot.get_maneuver() != 63 || ((Maneuver) (pilot)).target == null)
                return;
            trgtAI = ((Interpolate) (((Maneuver) (pilot)).target)).actor;
            for(int activeMissileIndex = 0; activeMissileIndex < GuidedMissileUtils.getActiveMissilesSize(); activeMissileIndex++)
            {
                ActiveMissile theActiveMissile = (ActiveMissile) GuidedMissileUtils.getActiveMissile(activeMissileIndex);
                if(!theActiveMissile.isAI() || ownerAircraft.getArmy() != theActiveMissile.getOwnerArmy() || theActiveMissile.getVictim() != trgtAI)
                    continue;
                trgtAI = null;
                break;
            }

            if ((targetType & Missile.TARGET_AIR) != 0) {
                if(Actor.isValid(trgtAI) && ((trgtAI instanceof Aircraft) || (trgtAI instanceof MissileInterceptable)))
                {
                    setMissileTarget(trgtAI);
                    trgtPk = getMissilePk();
                } else {
                    this.trgtPk = 0F;
                    return;
                }
            }
            else if ((((targetType & Missile.TARGET_GROUND) != 0)||((targetType & Missile.TARGET_SHIP) != 0))
                     && ((((SndAircraft) (ownerAircraft)).FM instanceof Pilot)||(AP.way.curr().Action == 3))) {
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
        }
        if(((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).AP instanceof AutopilotAI)
            ((AutopilotAI)((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).AP).setOverrideMissileControl(((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).CT, false);
        if(trgtPk > getMinPkForAttack() && Actor.isValid(getMissileTarget()) && getMissileTarget().getArmy() != ((Interpolate) (((SndAircraft) (ownerAircraft)).FM)).actor.getArmy() && Time.current() > tMissilePrev + getMillisecondsBetweenMissileLaunchAI())
        {
            tMissilePrev = Time.current();
            ((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).CT.WeaponControl[2] = true;
            if(((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).AP instanceof AutopilotAI)
                ((AutopilotAI)((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).AP).setOverrideMissileControl(((FlightModelMain) (((SndAircraft) (ownerAircraft)).FM)).CT, true);
        }
    }

    public void shootRocket()
    {
        if(rocketsList.isEmpty())
        {
            return;
        } else
        {
            ((RocketGun)rocketsList.get(0)).shots(1);
            return;
        }
    }

    private void checkPendingMissiles()
    {
        if(rocketsList.isEmpty())
            return;
        if(rocketsList.get(0) instanceof RocketGunWithDelay)
            ((RocketGunWithDelay)rocketsList.get(0)).checkPendingWeaponRelease();
    }

    public void onAircraftLoaded()
    {
        rocketsList.clear();
        createMissileList(rocketsList);
        setGunNullOwner();
    }

    public void update()
    {
        setMissileTarget(lookForGuidedMissileTarget(missileOwner, getMaxPOVfrom(), getMaxPOVto(), getPkMaxDist(), targetType));
        trgtPk = getMissilePk();
        checkAIlaunchMissile();
        checkPendingMissiles();
        checkLockStatus();
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
		for(int index=0; index<missileHook.length; index++) {
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
	
    public void playMissileGrowlLock(boolean isLocked)
    {
        if(isLocked)
        {
            if(fxMissileToneNoLock != null)
                fxMissileToneNoLock.cancel();
            if(fxMissileToneLock != null && smplMissileLock != null)
                fxMissileToneLock.play(smplMissileLock);
        } else
        {
            if(fxMissileToneLock != null)
                fxMissileToneLock.cancel();
            if(fxMissileToneNoLock != null && smplMissileNoLock != null)
                fxMissileToneNoLock.play(smplMissileNoLock);
        }
    }

    public void cancelMissileGrowl()
    {
        if(fxMissileToneLock != null)
            fxMissileToneLock.cancel();
        if(fxMissileToneNoLock != null)
            fxMissileToneNoLock.cancel();
    }

    private void changeMissileGrowl(int iMode)
    {
        if((missileOwner != World.getPlayerAircraft() || !((RealFlightModel)((SndAircraft) ((Aircraft)missileOwner)).FM).isRealMode()) && (((SndAircraft) ((Aircraft)missileOwner)).FM instanceof Pilot))
            return;
        setMissileGrowl(iMode);
        switch(iMode)
        {
        case 1: // '\001'
            playMissileGrowlLock(false);
            break;

        case 2: // '\002'
            playMissileGrowlLock(true);
            break;

        default:
            cancelMissileGrowl();
            break;
        }
    }

    public void checkLockStatus()
    {
        int iOldLockState;
        boolean bEnemyInSight;
        boolean bSidewinderLocked;
        boolean bNoEnemyTimeout;
        iOldLockState = iMissileLockState;
        bEnemyInSight = false;
        bSidewinderLocked = false;
        bNoEnemyTimeout = false;
        try
        {
            if(((FlightModelMain) (((SndAircraft) ((Aircraft)missileOwner)).FM)).CT.BrakeControl == 1.0F)
            {
                if(!oldBreakControl)
                {
                    oldBreakControl = true;
                    if(!((FlightModelMain) (((SndAircraft) ((Aircraft)missileOwner)).FM)).Gears.onGround())
                    {
                        engageMode--;
                        if(engageMode < -1)
                            engageMode = 1;
                        switch(engageMode)
                        {
                        case -1: 
                            if(missileName != null)
                                NetSafeLog.log(missileOwner, missileName + " Engagement OFF");
                            break;

                        case 0: // '\0'
                            if(missileName != null)
                                NetSafeLog.log(missileOwner, missileName + " Engagement AUTO");
                            break;

                        case 1: // '\001'
                            if(missileName != null)
                                NetSafeLog.log(missileOwner, missileName + " Engagement ON");
                            break;
                        }
                    }
                }
            } else
            {
                oldBreakControl = false;
            }
        }
        catch(Exception exception) { }
        if(missileOwner != World.getPlayerAircraft())
        {
            if(iMissileLockState != 0)
            {
                changeMissileGrowl(0);
                iMissileLockState = 0;
            }
            return;
        }
        if(!hasMissiles())
        {
            if(iMissileLockState != 0)
            {
                changeMissileGrowl(0);
                NetSafeLog.log(missileOwner, missileName + " missiles depleted");
                iMissileLockState = 0;
            }
            return;
        }
        if(engageMode == -1)
        {
            if(iMissileLockState != 0)
            {
                changeMissileGrowl(0);
                NetSafeLog.log(missileOwner, missileName + " disengaged");
                iMissileLockState = 0;
            }
            return;
        }
        try
        {
            if(Actor.isValid(trgtMissile))
            {
                bSidewinderLocked = true;
                if(trgtMissile.getArmy() != World.getPlayerAircraft().getArmy())
                    bEnemyInSight = true;
            }
            if(Actor.isValid(Main3D.cur3D().viewActor()) && Main3D.cur3D().viewActor() == missileOwner)
            {
                Actor theEnemy = Selector.look(true, false, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], World.getPlayerAircraft().getArmy(), -1, World.getPlayerAircraft(), false);
                if(Actor.isValid(theEnemy))
                    bEnemyInSight = true;
            }
            if(bEnemyInSight)
                tLastSeenEnemy = Time.current();
            else
            if(Time.current() - tLastSeenEnemy > 10000L)
                bNoEnemyTimeout = true;
            if(bSidewinderLocked)
            {
                if(bEnemyInSight)
                    iMissileLockState = 2;
                else
                if(engageMode == 1)
                    iMissileLockState = 2;
                else
                if(bNoEnemyTimeout)
                    iMissileLockState = 0;
                else
                    iMissileLockState = 2;
            } else
            if(bNoEnemyTimeout)
                iMissileLockState = 0;
            else
                iMissileLockState = 1;
            if(engageMode == 1 && iMissileLockState == 0)
                iMissileLockState = 1;
            if(((SndAircraft) ((Aircraft)missileOwner)).FM.getOverload() > fPkMaxG && iMissileLockState == 2)
                iMissileLockState = 1;
            switch(iMissileLockState)
            {
            case 1: // '\001'
                if(iOldLockState != 1)
                    changeMissileGrowl(1);
                if(iOldLockState == 0)
                    NetSafeLog.log(missileOwner, missileName + " engaged");
                break;

            case 2: // '\002'
                if(iOldLockState != 2)
                    changeMissileGrowl(2);
                if(iOldLockState == 0)
                    NetSafeLog.log(missileOwner, missileName + " engaged");
                break;

            case 0: // '\0'
                if(iOldLockState != 0)
                {
                    changeMissileGrowl(0);
                    NetSafeLog.log(missileOwner, missileName + " disengaged");
                }
                break;

            default:
                if(iOldLockState != 0)
                {
                    changeMissileGrowl(0);
                    NetSafeLog.log(missileOwner, missileName + " disengaged");
                }
                break;
            }
        }
        catch(Exception exception1) { }
        return;
    }

    public Missile getMissileFromRocketGun(RocketGun theRocketGun)
    {
        return (Missile)theRocketGun.rocket;
    }

    public Point3f getSelectedActorOffset()
    {
        return selectedActorOffset;
    }

    public float Pk(Actor actorFrom, Actor actorTo)
    {
        float fPkRet = 0.0F;
        float fTemp = 0.0F;
        float angleToTarget = angleBetween(actorFrom, actorTo);
        float angleFromTarget = 180F - angleBetween(actorTo, actorFrom);
        float distanceToTarget = (float)distanceBetween(actorFrom, actorTo);
        float gForce = ((SndAircraft) ((Aircraft)actorFrom)).FM.getOverload();
        float fMaxLaunchLoad = fPkMaxG;
        if((actorFrom instanceof Aircraft) && (!(((SndAircraft) ((Aircraft)actorFrom)).FM instanceof RealFlightModel) || !((RealFlightModel)((SndAircraft) ((Aircraft)actorFrom)).FM).isRealMode()))
            fMaxG *= 2.0F;
        fPkRet = 100F;
        if(distanceToTarget > fPkMaxDist || distanceToTarget < fPkMinDist || angleToTarget > fPkMaxAngle || angleFromTarget > fPkMaxAngleAft || gForce > fMaxLaunchLoad)
            return 0.0F;
        if(distanceToTarget > fPkOptDist)
        {
            fTemp = distanceToTarget - fPkOptDist;
            fTemp /= fPkMaxDist - fPkOptDist;
            fPkRet -= fTemp * fTemp * 20F;
        } else
        {
            fTemp = fPkOptDist - distanceToTarget;
            fTemp /= fPkOptDist - fPkMinDist;
            fPkRet -= fTemp * fTemp * 60F;
        }
        fTemp = angleToTarget / fPkMaxAngle;
        fPkRet -= fTemp * fTemp * 30F;
        fTemp = angleFromTarget / fPkMaxAngleAft;
        fPkRet -= fTemp * fTemp * 50F;
        fTemp = gForce / fMaxLaunchLoad;
        fPkRet -= fTemp * fTemp * 30F;
        if(fPkRet < 0.0F)
            fPkRet = 0.0F;
        return fPkRet;
    }

    private int engineHeatSpreadType(Actor theActor)
    {
        if(!(theActor instanceof Aircraft))
            return HEAT_SPREAD_360;
        EnginesInterface checkEI = ((FlightModelMain) (((SndAircraft)theActor).FM)).EI;
        int iRetVal = HEAT_SPREAD_NONE;
        for(int i = 0; i < checkEI.getNum(); i++)
        {
            int iMotorType = checkEI.engines[i].getType();
            if(iMotorType == 2 || iMotorType == 3 || iMotorType == 4 || iMotorType == 6)
                iRetVal |= HEAT_SPREAD_AFT;
            if(iMotorType == 0 || iMotorType == 1 || iMotorType == 7 || iMotorType == 8)
                iRetVal |= HEAT_SPREAD_360;
        }

        return iRetVal;
    }

    private int engineHeatSpreadType(Motor theMotor)
    {
        int iRetVal = HEAT_SPREAD_NONE;
        int iMotorType = theMotor.getType();
        if(iMotorType == 2 || iMotorType == 3 || iMotorType == 4 || iMotorType == 6)
            iRetVal |= HEAT_SPREAD_AFT;
        if(iMotorType == 0 || iMotorType == 1 || iMotorType == 7 || iMotorType == 8)
            iRetVal |= HEAT_SPREAD_360;
        return iRetVal;
    }

    public Actor lookForGuidedMissileTarget(Actor actor, float maxFOVfrom, float maxFOVto, double maxDistance)
    {
        return lookForGuidedMissileTargetAircraft(actor, maxFOVfrom, maxFOVto, maxDistance);
    }

    public Actor lookForGuidedMissileTarget(Actor actor, float maxFOVfrom, float maxFOVto, double maxDistance, long targetType)
    {
        Actor actorTarget = null;
        if ((targetType & Missile.TARGET_AIR) != 0) {
            actorTarget = this.lookForGuidedMissileTargetAircraft(actor, maxFOVfrom, maxFOVto, maxDistance);
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
						} else if (theTarget1 instanceof MissileInterceptable) {
							maxEngineForce = Property.floatValue(theTarget1.getClass(), "force", 1000F);
						}
						targetBait = maxEngineForce / targetAngle / (float) (targetDistance * targetDistance);
						if (!theTarget1.isAlive()) {
							targetBait /= 10F;
						}
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
						} else if (theTarget1 instanceof MissileInterceptable) {
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
								// float fGC = FlightModelMainEx.getFmGCenter((((SndAircraft) (theTarget1)).FM));
								// Arm Arms = (Arm) Reflection.genericGetFieldValue((FlightModelMain) (((SndAircraft) (theTarget1)).FM), "Arms");
								// float fGC = ((Float) Reflection.genericGetFieldValue(Arms, "GCENTER")).floatValue();
								// theSelectedActorOffset.set(fGC, 0, 0);
								theSelectedActorOffset.set(0, 0, 0);
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

		
		if (actor instanceof Aircraft && selectedActor != null) {
			Aircraft ownerAircraft = (Aircraft)actor;
	        if(!ownerAircraft.FM.isPlayers() || !(ownerAircraft.FM instanceof RealFlightModel) || !((RealFlightModel)ownerAircraft.FM).isRealMode()) {
	        	GuidedMissileUtils.checkAllActiveMissilesValidity();
				for (int activeMissileIndex = 0; activeMissileIndex < GuidedMissileUtils.getActiveMissilesSize(); activeMissileIndex++) {
					ActiveMissile theActiveMissile = (ActiveMissile) GuidedMissileUtils.getActiveMissile(activeMissileIndex);
					if (theActiveMissile.isAI()) {
						if (ownerAircraft.FM.actor.getArmy() == theActiveMissile.getOwnerArmy()) {
							if (theActiveMissile.getVictim() == selectedActor) {
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
		try {
			ArrayList arraylist = World.cur().statics.bridges;
			int jj = arraylist.size();
			LongBridge longbridge = null;
			Actor target_bridge = null;
			double longbridgeDistance = maxDistance;
			for(int ll = 0; ll < jj; ll++) {
				LongBridge longbridge1 = (LongBridge)arraylist.get(ll);
				if(!longbridge1.isAlive()) continue;
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
			
			if(longbridge != null) {
				int kk = longbridge.NumStateBits() / 2;
				target_bridge = BridgeSegment.getByIdx(longbridge.bridgeIdx(), World.Rnd().nextInt(kk));
			}
			
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
					GuidedMissileUtils.checkAllActiveMissilesValidity();
					for (int activeMissileIndex = 0; activeMissileIndex < GuidedMissileUtils.getActiveMissilesSize(); activeMissileIndex++) {
						ActiveMissile theActiveMissile = (ActiveMissile) GuidedMissileUtils.getActiveMissile(activeMissileIndex);
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

			if(target_bridge != null){
				if(longbridgeDistance < distanceBetween(actor, selectedActor)){
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
				if (AP.way.curr().Action == 3 || AP.way.curr().getTarget() != null)
				{
					target = AP.way.curr().getTarget();
					if (target.getSpeed(null) < 1D)
					{
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
			} while(true);
			AP.way.setCur(wcu);
			
			if(flag)
				selectedActor = target;
			if(selectedActor instanceof Bridge)
				theSelectedActorOffset = new Point3f(0.0F, 0.0F, 3.0F);

		} catch (Exception e) {
			EventLog.type("Exception in selectedActor");
			EventLog.type(e.toString());
			EventLog.type(e.getMessage());
		}
		this.selectedActorOffset.set(theSelectedActorOffset);
		return selectedActor;
	}

    private boolean actorIsAI(Actor theActor)
    {
        if(!(theActor instanceof Aircraft))
            return true;
        if(((SndAircraft) ((Aircraft)theActor)).FM == null)
            return true;
        return (theActor != World.getPlayerAircraft() || !((RealFlightModel)((SndAircraft) ((Aircraft)theActor)).FM).isRealMode()) && (((SndAircraft) ((Aircraft)theActor)).FM instanceof Pilot);
    }

	public Actor lookForGuidedMissileTargetShip(Actor actor, float maxFOVfrom, float maxFOVto, double maxDistance) {
		double targetDistance = 0.0D;
		float targetAngle = 0.0F;
		float targetBait = 0.0F;
		float maxTargetBait = 0.0F;
		Actor selectedActor = null;
		this.selectedActorOffset.set(new Point3f(0.0F, 0.0F, 0.0F));

		if (!(actor instanceof Aircraft) || (this.iDetectorMode == Missile.DETECTOR_TYPE_MANUAL)) return selectedActor;

		try {
			List list = Engine.targets();
			int k = list.size();
			for (int i1 = 0; i1 < k; i1++) {
				Actor theTarget1 = (Actor) list.get(i1);
				if (theTarget1 instanceof TgtShip) {
					targetDistance = distanceBetween(actor, theTarget1);
					if (targetDistance > maxDistance) {
						continue;
					}
					targetAngle = angleBetween(actor, theTarget1);
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
					for (int activeMissileIndex = 0; activeMissileIndex < GuidedMissileUtils.getActiveMissilesSize(); activeMissileIndex++) {
						ActiveMissile theActiveMissile = (ActiveMissile) GuidedMissileUtils.getActiveMissile(activeMissileIndex);
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
					this.selectedActorOffset.set(new Point3f(0.0F, 0.0F, 5.0F));
				}
			}
		}
		return selectedActor;
	}

    private Point3f selectedActorOffset;
    private SoundFX fxMissileToneLock;
    private SoundFX fxMissileToneNoLock;
    private Sample smplMissileLock;
    private Sample smplMissileNoLock;
    private int iMissileLockState;
    private int iMissileTone;
    private long tLastSeenEnemy;
    private final int ENGAGE_OFF = -1;
    private final int ENGAGE_AUTO = 0;
    private final int ENGAGE_ON = 1;
    private int engageMode;
    private Actor missileOwner;
    private boolean oldBreakControl;
    private Actor trgtMissile;
    private String missileName;
    protected FlightModel fm;
    protected Eff3DActor fl1;
    protected Eff3DActor fl2;
    protected Orient or;
    protected Point3d p;
    protected Orient orVictimOffset;
    protected Point3f pVictimOffset;
    protected Point3d pT;
    protected Vector3d v;
    protected Vector3d victimSpeed;
    protected long tStartLastMissile;
    protected float prevd;
    protected float deltaAzimuth;
    protected float deltaTangage;
    protected double d;
    protected Actor victim;
    protected float fMissileMaxSpeedKmh;
    protected float fMissileBaseSpeedKmh;
    protected double launchKren;
    protected double launchPitch;
    protected double launchYaw;
    protected float oldDeltaAzimuth;
    protected float oldDeltaTangage;
    private ArrayList rocketsList;
    private float trgtPk;
    private Actor trgtAI;
    private long tMissilePrev;
    private boolean attackDecisionByAI;
    private boolean attackDecisionByWaypoint;
    private boolean multiTrackingCapable;
    private boolean canTrackSubs;
    private float minPkForAttack;
    private long millisecondsBetweenMissileLaunchAI;
    private long targetType;
    private float fLeadPercent;
    private float fPkMaxAngle;
    private float fPkMaxAngleAft;
    private float fPkMinDist;
    private float fPkOptDist;
    private float fPkMaxDist;
    private float fPkMaxG;
    private float fMaxG;
    private float fStepsForFullTurn;
    private float fMaxPOVfrom;
    private float fMaxPOVto;
    private float fMaxDistance;
    private int iDetectorMode;
    private static Mission curMission = null;
    private static ArrayList activeMissiles = null;
    private static HashMap targetsHandledByAiAlready = null;
    public static int HEAT_SPREAD_NONE = 0;
    public static int HEAT_SPREAD_AFT = 1;
    public static int HEAT_SPREAD_360 = 2;
    public int rocketSelected;
    private static final long minTimeBetweenAIMissileLaunch = 1000L;

}