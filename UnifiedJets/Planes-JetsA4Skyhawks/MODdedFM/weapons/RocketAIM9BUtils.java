// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 02/11/2015 03:26:59 p.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RocketAIM9BUtils.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeAIM9Carrier;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun

public class RocketAIM9BUtils
{

    public RocketAIM9BUtils(Actor owner)
    {
        twoPlaces = new DecimalFormat("+000.00;-000.00");
        selectedActorOffset = null;
        iMissileLockState = 0;
        iMissileTone = 0;
        tLastSeenEnemy = 0L;
        missileOwner = null;
        trgtMissile = null;
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
        tStart = 0L;
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
        initParams(owner);
    }

    public RocketAIM9BUtils(Actor theOwner, float theMissileMaxSpeedMeterPerSecond, float theLeadPercent, float theMaxG, float theStepsForFullTurn, float thePkMaxAngle, float thePkMaxAngleAft, 
            float thePkMinDist, float thePkOptDist, float thePkMaxDist, float thePkMaxG, float theMaxPOVfrom, float theMaxPOVto, float theMaxDistance, 
            String theLockFx, String theNoLockFx, String theLockSmpl, String theNoLockSmpl)
    {
        twoPlaces = new DecimalFormat("+000.00;-000.00");
        selectedActorOffset = null;
        iMissileLockState = 0;
        iMissileTone = 0;
        tLastSeenEnemy = 0L;
        missileOwner = null;
        trgtMissile = null;
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
        tStart = 0L;
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
        initParams(theOwner, theMissileMaxSpeedMeterPerSecond, theLeadPercent, theMaxG, theStepsForFullTurn, thePkMaxAngle, thePkMaxAngleAft, thePkMinDist, thePkOptDist, thePkMaxDist, thePkMaxG, theMaxPOVfrom, theMaxPOVto, theMaxDistance, theLockFx, theNoLockFx, theLockSmpl, theNoLockSmpl);
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
        smplMissileLock = new Sample(value, 256, 65535);
        smplMissileLock.setInfinite(true);
    }

    public void setSmplMissileNoLock(String value)
    {
        smplMissileNoLock = new Sample(value, 256, 65535);
        smplMissileNoLock.setInfinite(true);
    }

    public void setLockTone(String theLockPrs, String theNoLockPrs, String theLockWav, String theNoLockWav)
    {
        fxMissileToneLock = missileOwner.newSound(theLockPrs, false);
        fxMissileToneNoLock = missileOwner.newSound(theNoLockPrs, false);
        smplMissileLock = new Sample(theLockWav, 256, 65535);
        smplMissileLock.setInfinite(true);
        smplMissileNoLock = new Sample(theNoLockWav, 256, 65535);
        smplMissileNoLock.setInfinite(true);
    }

    public void setMissileName(String theMissileName)
    {
        missileName = theMissileName;
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

    public void setMissileTarget(Actor theTarget)
    {
        trgtMissile = theTarget;
    }

    public int getMissileLockState()
    {
        return iMissileLockState;
    }

    public int getMissileGrowl()
    {
        return iMissileTone;
    }

    private void initParams(Actor theOwner, float theMissileMaxSpeedMeterPerSecond, float theLeadPercent, float theMaxG, float theStepsForFullTurn, float thePkMaxAngle, float thePkMaxAngleAft, 
            float thePkMinDist, float thePkOptDist, float thePkMaxDist, float thePkMaxG, float theMaxPOVfrom, float theMaxPOVto, float theMaxDistance, 
            String theLockFx, String theNoLockFx, String theLockSmpl, String theNoLockSmpl)
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
        fxMissileToneLock = missileOwner.newSound(theLockFx, false);
        fxMissileToneNoLock = missileOwner.newSound(theNoLockFx, false);
        smplMissileLock = new Sample(theLockSmpl, 256, 65535);
        smplMissileLock.setInfinite(true);
        smplMissileNoLock = new Sample(theNoLockSmpl, 256, 65535);
        smplMissileNoLock.setInfinite(true);
    }

    private void initParams(Actor theOwner)
    {
        EventLog.type("GuidedMissileUtils initParams(Actor){}");
        initCommon();
        if(!(theOwner instanceof Aircraft))
        {
            EventLog.type("GuidedMissileUtils owner is not instanceof Aircraft!");
            EventLog.type("(Owner = " + theOwner.getClass().toString() + " )");
            return;
        } else
        {
            missileOwner = theOwner;
            return;
        }
    }

    private void initCommon()
    {
        selectedActorOffset = new Point3f();
        engageMode = 0;
        iMissileLockState = 0;
        iMissileTone = 0;
        tLastSeenEnemy = Time.current() - 20000L;
        oldBreakControl = true;
    }

    public void createMissileList(ArrayList theMissileList)
    {
        Aircraft theMissileCarrier = (Aircraft)missileOwner;
        Class theMissileClass = null;
        try
        {
            for(int l = 0; l < ((FlightModelMain) (((SndAircraft) (theMissileCarrier)).FM)).CT.Weapons.length; l++)
                if(((FlightModelMain) (((SndAircraft) (theMissileCarrier)).FM)).CT.Weapons[l] != null)
                {
                    for(int l1 = 0; l1 < ((FlightModelMain) (((SndAircraft) (theMissileCarrier)).FM)).CT.Weapons[l].length; l1++)
                        if(((FlightModelMain) (((SndAircraft) (theMissileCarrier)).FM)).CT.Weapons[l][l1] != null && (((FlightModelMain) (((SndAircraft) (theMissileCarrier)).FM)).CT.Weapons[l][l1] instanceof RocketGun))
                        {
                            Class theBulletClass = ((RocketGun)((FlightModelMain) (((SndAircraft) (theMissileCarrier)).FM)).CT.Weapons[l][l1]).bulletClass();
                            if(com.maddox.il2.objects.weapons.AIM9BMissile.class.isAssignableFrom(theBulletClass))
                            {
                                theMissileClass = theBulletClass;
                                theMissileList.add(((FlightModelMain) (((SndAircraft) (theMissileCarrier)).FM)).CT.Weapons[l][l1]);
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
            fxMissileToneLock = missileOwner.newSound(Property.stringValue(theMissileClass, "fxLock", "weapon.AIM9.lock"), false);
            fxMissileToneNoLock = missileOwner.newSound(Property.stringValue(theMissileClass, "fxNoLock", "weapon.AIM9.nolock"), false);
            smplMissileLock = new Sample(Property.stringValue(theMissileClass, "smplLock", "AIM9_lock.wav"), 256, 65535);
            smplMissileLock.setInfinite(true);
            smplMissileNoLock = new Sample(Property.stringValue(theMissileClass, "smplNoLock", "AIM9_no_lock.wav"), 256, 65535);
            smplMissileNoLock.setInfinite(true);
            missileName = Property.stringValue(theMissileClass, "friendlyName", "Missile");
            return;
        }
    }

    public static float angleBetween(Actor actorFrom, Actor actorTo)
    {
        float angleRetVal = 180.1F;
        if(!(actorFrom instanceof Aircraft) || !(actorTo instanceof Aircraft))
        {
            return angleRetVal;
        } else
        {
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

    public void playMissileGrowlLock(boolean isLocked)
    {
        if(isLocked)
        {
            fxMissileToneNoLock.cancel();
            fxMissileToneLock.play(smplMissileLock);
        } else
        {
            fxMissileToneLock.cancel();
            fxMissileToneNoLock.play(smplMissileNoLock);
        }
    }

    public void cancelMissileGrowl()
    {
        fxMissileToneLock.cancel();
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
        int iOldLockState = iMissileLockState;
        boolean bEnemyInSight = false;
        boolean bSidewinderLocked = false;
        boolean bNoEnemyTimeout = false;
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
                                AIM9BNetSafeLog.log(missileOwner, missileName + " Engagement OFF");
                            break;

                        case 0: // '\0'
                            if(missileName != null)
                                AIM9BNetSafeLog.log(missileOwner, missileName + " Engagement AUTO");
                            break;

                        case 1: // '\001'
                            if(missileName != null)
                                AIM9BNetSafeLog.log(missileOwner, missileName + " Engagement ON");
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
        if(!((TypeAIM9Carrier)missileOwner).hasMissiles())
        {
            if(iMissileLockState != 0)
            {
                changeMissileGrowl(0);
                AIM9BNetSafeLog.log(missileOwner, missileName + " missiles depleted");
                iMissileLockState = 0;
            }
            return;
        }
        if(engageMode == -1)
        {
            if(iMissileLockState != 0)
            {
                changeMissileGrowl(0);
                AIM9BNetSafeLog.log(missileOwner, missileName + " disengaged");
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
                    AIM9BNetSafeLog.log(missileOwner, missileName + " engaged");
                break;

            case 2: // '\002'
                if(iOldLockState != 2)
                    changeMissileGrowl(2);
                if(iOldLockState == 0)
                    AIM9BNetSafeLog.log(missileOwner, missileName + " engaged");
                break;

            case 0: // '\0'
                if(iOldLockState != 0)
                {
                    changeMissileGrowl(0);
                    AIM9BNetSafeLog.log(missileOwner, missileName + " disengaged");
                }
                break;

            default:
                if(iOldLockState != 0)
                {
                    changeMissileGrowl(0);
                    AIM9BNetSafeLog.log(missileOwner, missileName + " disengaged");
                }
                break;
            }
        }
        catch(Exception exception1) { }
    }

    public Point3f getSelectedActorOffset()
    {
        return selectedActorOffset;
    }

    public float Pk(Actor actorFrom, Actor actorTo)
    {
        float fPkRet = 0.0F;
        float fTemp = 0.0F;
        if(!(actorFrom instanceof Aircraft) || !(actorTo instanceof Aircraft))
            return fPkRet;
        float angleToTarget = angleBetween(actorFrom, actorTo);
        float angleFromTarget = 180F - angleBetween(actorTo, actorFrom);
        float distanceToTarget = (float)distanceBetween(actorFrom, actorTo);
        float gForce = ((SndAircraft) ((Aircraft)actorFrom)).FM.getOverload();
        float fMaxLaunchLoad = fPkMaxG;
        if(!(((SndAircraft) ((Aircraft)actorFrom)).FM instanceof RealFlightModel) || !((RealFlightModel)((SndAircraft) ((Aircraft)actorFrom)).FM).isRealMode())
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
        double targetDistance = 0.0D;
        float targetAngle = 0.0F;
        float targetAngleAft = 0.0F;
        float targetBait = 0.0F;
        float maxTargetBait = 0.0F;
        Actor selectedActor = null;
        Point3f theSelectedActorOffset = new Point3f(0.0F, 0.0F, 0.0F);
        if(!(actor instanceof Aircraft) || iDetectorMode == 0)
            return selectedActor;
        try
        {
            List list = Engine.targets();
            int k = list.size();
            for(int i1 = 0; i1 < k; i1++)
            {
                Actor theTarget1 = (Actor)list.get(i1);
                if(theTarget1 instanceof Aircraft)
                {
                    targetDistance = distanceBetween(actor, theTarget1);
                    if(targetDistance <= maxDistance)
                    {
                        targetAngle = angleBetween(actor, theTarget1);
                        if(targetAngle <= maxFOVfrom)
                        {
                            targetAngleAft = 180F - angleBetween(theTarget1, actor);
                            if(targetAngleAft <= maxFOVto || iDetectorMode != 1 || (engineHeatSpreadType(theTarget1) & HEAT_SPREAD_360) != 0)
                                switch(iDetectorMode)
                                {
                                default:
                                    break;

                                case 1: // '\001'
                                    EnginesInterface theEI = ((FlightModelMain) (((SndAircraft)theTarget1).FM)).EI;
                                    int iNumEngines = theEI.getNum();
                                    float maxEngineForce = 0.0F;
                                    int maxEngineForceEngineNo = 0;
                                    for(int i = 0; i < iNumEngines; i++)
                                    {
                                        Motor theMotor = theEI.engines[i];
                                        float theEngineForce = theMotor.getEngineForce().length();
                                        if(engineHeatSpreadType(theMotor) == HEAT_SPREAD_NONE)
                                            theEngineForce = 0.0F;
                                        if(engineHeatSpreadType(theMotor) == HEAT_SPREAD_360)
                                            theEngineForce /= 10F;
                                        if(theEngineForce > maxEngineForce)
                                        {
                                            maxEngineForce = theEngineForce;
                                            maxEngineForceEngineNo = i;
                                        }
                                    }

                                    targetBait = maxEngineForce / targetAngle / (float)(targetDistance * targetDistance);
                                    if(!theTarget1.isAlive())
                                        targetBait /= 10F;
                                    if(targetBait > maxTargetBait)
                                    {
                                        maxTargetBait = targetBait;
                                        selectedActor = theTarget1;
                                        theSelectedActorOffset = theEI.engines[maxEngineForceEngineNo].getEnginePos();
                                    }
                                    break;

                                case 2: // '\002'
                                case 3: // '\003'
                                case 4: // '\004'
                                    Mass theM = ((FlightModelMain) (((SndAircraft)theTarget1).FM)).M;
                                    float fACMass = theM.getFullMass();
                                    targetBait = fACMass / targetAngle / (float)(targetDistance * targetDistance);
                                    if(!theTarget1.isAlive())
                                        targetBait /= 10F;
                                    if(targetBait > maxTargetBait)
                                    {
                                        maxTargetBait = targetBait;
                                        selectedActor = theTarget1;
                                        float fGC = FlightModelMainAIM9B.getFmGCenter(((SndAircraft)theTarget1).FM);
                                        theSelectedActorOffset.set(fGC, 0.0F, 0.0F);
                                    }
                                    break;
                                }
                        }
                    }
                }
            }

        }
        catch(Exception e)
        {
            EventLog.type("Exception in selectedActor");
            EventLog.type(e.toString());
            EventLog.type(e.getMessage());
        }
        selectedActorOffset.set(theSelectedActorOffset);
        return selectedActor;
    }

    private DecimalFormat twoPlaces;
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
    protected long tStart;
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
    public static int HEAT_SPREAD_NONE = 0;
    public static int HEAT_SPREAD_AFT = 1;
    public static int HEAT_SPREAD_360 = 2;

}
