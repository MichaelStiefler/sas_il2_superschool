// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 28.04.2015 17:05:45
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
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
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.bridges.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Conversion;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import java.util.*;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            RocketGun, GunNull, MissileInterceptable, RocketGunWithDelay, 
//            ActiveMissile, MissileGun, Missile

public class GuidedMissileUtils
{

    public GuidedMissileUtils(Actor actor)
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
        initParams(actor);
    }

    public GuidedMissileUtils(Actor actor, float f, float f1, float f2, float f3, float f4, float f5, 
            float f6, float f7, float f8, float f9, float f10, float f11, float f12, 
            float f13, long l, long l1, boolean flag, boolean flag1, 
            boolean flag2, String s, String s1, String s2, String s3)
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
        initParams(actor, f, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, l, l1, flag, flag1, flag2, s, s1, s2, s3);
    }

    public static void LocalLog(Actor actor, int i, String s)
    {
        if(actor == World.getPlayerAircraft() && !actor.isNetMirror())
            HUD.log(i, s);
    }

    public static void LocalLog(Actor actor, String s)
    {
        if(actor == World.getPlayerAircraft() && !actor.isNetMirror())
            HUD.log(s);
    }

    public void setMissileOwner(Actor actor)
    {
        missileOwner = actor;
    }

    public void setMissileMaxSpeedKmh(float f)
    {
        fMissileMaxSpeedKmh = f;
    }

    public void setLeadPercent(float f)
    {
        fLeadPercent = f;
    }

    public void setMaxG(float f)
    {
        fMaxG = f;
    }

    public void setStepsForFullTurn(float f)
    {
        fStepsForFullTurn = f;
    }

    public void setPkMaxAngle(float f)
    {
        fPkMaxAngle = f;
    }

    public void setPkMaxAngleAft(float f)
    {
        fPkMaxAngleAft = f;
    }

    public void setPkMinDist(float f)
    {
        fPkMinDist = f;
    }

    public void setPkOptDist(float f)
    {
        fPkOptDist = f;
    }

    public void setPkMaxDist(float f)
    {
        fPkMaxDist = f;
    }

    public void setPkMaxG(float f)
    {
        fPkMaxG = f;
    }

    public void setMaxPOVfrom(float f)
    {
        fMaxPOVfrom = f;
    }

    public void setMaxPOVto(float f)
    {
        fMaxPOVto = f;
    }

    public void setMaxDistance(float f)
    {
        fMaxDistance = f;
    }

    public void setFxMissileToneLock(String s)
    {
        fxMissileToneLock = missileOwner.newSound(s, false);
    }

    public void setFxMissileToneNoLock(String s)
    {
        fxMissileToneNoLock = missileOwner.newSound(s, false);
    }

    public void setSmplMissileLock(String s)
    {
        smplMissileLock = new Sample(s);
        smplMissileLock.setInfinite(true);
    }

    public void setSmplMissileNoLock(String s)
    {
        smplMissileNoLock = new Sample(s);
        smplMissileNoLock.setInfinite(true);
    }

    public void setLockTone(String s, String s1, String s2, String s3)
    {
        fxMissileToneLock = missileOwner.newSound(s, false);
        fxMissileToneNoLock = missileOwner.newSound(s1, false);
        setSmplMissileLock(s2);
        setSmplMissileNoLock(s3);
    }

    public void setMissileName(String s)
    {
        missileName = s;
    }

    public String getMissileName()
    {
        return missileName;
    }

    public void setMissileTarget(Actor actor)
    {
        trgtMissile = actor;
    }

    public void setAttackDecisionByAI(boolean flag)
    {
        attackDecisionByAI = flag;
    }

    public void setAttackDecisionByWaypoint(boolean flag)
    {
        attackDecisionByWaypoint = flag;
    }

    public void setMinPkForAttack(float f)
    {
        minPkForAttack = f;
    }

    public void setMillisecondsBetweenMissileLaunchAI(long l)
    {
        millisecondsBetweenMissileLaunchAI = l;
    }

    public void setTargetType(long l)
    {
        targetType = l;
    }

    public void setCanTrackSubs(boolean flag)
    {
        canTrackSubs = flag;
    }

    public void setMultiTrackingCapable(boolean flag)
    {
        multiTrackingCapable = flag;
    }

    public void setStartLastMissile(long l)
    {
        tStartLastMissile = l;
    }

    public void setMissileGrowl(int i)
    {
        iMissileTone = i;
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
        byte byte0 = 0;
        if(iDetectorMode == 1)
            byte0 = 1;
        else
        if(iDetectorMode == 3 || iDetectorMode == 2 || iDetectorMode == 4)
            byte0 = 2;
        return byte0;
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

    private void initParams(Actor actor, float f, float f1, float f2, float f3, float f4, float f5, 
            float f6, float f7, float f8, float f9, float f10, float f11, float f12, 
            float f13, long l, long l1, boolean flag, boolean flag1, 
            boolean flag2, String s, String s1, String s2, String s3)
    {
        initCommon();
        missileOwner = actor;
        fMissileMaxSpeedKmh = f;
        fLeadPercent = f1;
        fMaxG = f2;
        fStepsForFullTurn = f3;
        fPkMaxAngle = f4;
        fPkMaxAngleAft = f5;
        fPkMinDist = f6;
        fPkOptDist = f7;
        fPkMaxDist = f8;
        fPkMaxG = f9;
        fMaxPOVfrom = f10;
        fMaxPOVto = f11;
        fMaxDistance = f12;
        attackDecisionByAI = flag;
        minPkForAttack = f13;
        millisecondsBetweenMissileLaunchAI = l;
        targetType = l1;
        canTrackSubs = flag2;
        multiTrackingCapable = flag1;
        if(s == null)
            fxMissileToneLock = null;
        else
            fxMissileToneLock = missileOwner.newSound(s, false);
        if(s1 == null)
            fxMissileToneNoLock = null;
        else
            fxMissileToneNoLock = missileOwner.newSound(s1, false);
        if(s2 == null)
        {
            smplMissileLock = null;
        } else
        {
            smplMissileLock = new Sample(s2, 256, 65535);
            smplMissileLock.setInfinite(true);
        }
        if(s3 == null)
        {
            smplMissileNoLock = null;
        } else
        {
            smplMissileNoLock = new Sample(s3, 256, 65535);
            smplMissileNoLock.setInfinite(true);
        }
    }

    private void initParams(Actor actor)
    {
        initCommon();
        if(actor instanceof Aircraft)
            missileOwner = actor;
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

    public void createMissileList(ArrayList arraylist, Class class1)
    {
        Aircraft aircraft = (Aircraft)missileOwner;
        try
        {
            for(int i = 0; i < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons.length; i++)
                if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[i] != null)
                {
                    for(int j = 0; j < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[i].length; j++)
                    {
                        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[i][j] == null || !(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[i][j] instanceof RocketGun))
                            continue;
                        RocketGun rocketgun = (RocketGun)((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[i][j];
                        if(!rocketgun.haveBullets())
                            continue;
                        Class class2 = rocketgun.bulletClass();
                        if(class1 != null && !class2.getName().equals(class1.getName()) || !(com.maddox.il2.objects.weapons.Missile.class).isAssignableFrom(class2))
                            continue;
                        if(class1 == null)
                            class1 = class2;
                        arraylist.add(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[i][j]);
                    }

                }

        }
        catch(Exception exception)
        {
            EventLog.type("Exception in initParams: " + exception.getMessage());
        }
        if(class1 == null)
        {
            return;
        } else
        {
            getMissileProperties(class1);
            return;
        }
    }

    public void createMissileList(ArrayList arraylist)
    {
        createMissileList(arraylist, null);
    }

    public void getMissileProperties(Class class1)
    {
        fPkMaxG = Property.floatValue(class1, "maxLockGForce", 99.9F);
        fMaxPOVfrom = Property.floatValue(class1, "maxFOVfrom", 99.9F);
        fMaxPOVto = Property.floatValue(class1, "maxFOVto", 99.9F);
        fPkMaxAngle = Property.floatValue(class1, "PkMaxFOVfrom", 99.9F);
        fPkMaxAngleAft = Property.floatValue(class1, "PkMaxFOVto", 99.9F);
        fPkMinDist = Property.floatValue(class1, "PkDistMin", 99.9F);
        fPkOptDist = Property.floatValue(class1, "PkDistOpt", 99.9F);
        fPkMaxDist = Property.floatValue(class1, "PkDistMax", 99.9F);
        fMissileMaxSpeedKmh = Property.floatValue(class1, "maxSpeed", 99.9F);
        fLeadPercent = Property.floatValue(class1, "leadPercent", 99.9F);
        fMaxG = Property.floatValue(class1, "maxGForce", 99.9F);
        iDetectorMode = Property.intValue(class1, "detectorType", 0);
        attackDecisionByAI = Property.intValue(class1, "attackDecisionByAI", 0) == 1;
        attackDecisionByWaypoint = Property.intValue(class1, "attackDecisionByAI", 0) == 2;
        canTrackSubs = Property.intValue(class1, "canTrackSubs", 0) != 0;
        multiTrackingCapable = Property.intValue(class1, "multiTrackingCapable", 0) != 0;
        minPkForAttack = Property.floatValue(class1, "minPkForAI", 25F);
        millisecondsBetweenMissileLaunchAI = Property.longValue(class1, "timeForNextLaunchAI", 10000L);
        targetType = Property.longValue(class1, "targetType", 1L);
        String s = Property.stringValue(class1, "fxLock", "weapon.AIM9.lock");
        if(s == null)
            fxMissileToneLock = null;
        else
            fxMissileToneLock = missileOwner.newSound(s, false);
        s = Property.stringValue(class1, "fxNoLock", "weapon.AIM9.nolock");
        if(s == null)
            fxMissileToneNoLock = null;
        else
            fxMissileToneNoLock = missileOwner.newSound(s, false);
        s = Property.stringValue(class1, "smplLock", "AIM9_lock.wav");
        if(s == null)
        {
            smplMissileLock = null;
        } else
        {
            smplMissileLock = new Sample(s, 256, 65535);
            smplMissileLock.setInfinite(true);
        }
        s = Property.stringValue(class1, "smplNoLock", "AIM9_no_lock.wav");
        if(s == null)
        {
            smplMissileNoLock = null;
        } else
        {
            smplMissileNoLock = new Sample(s, 256, 65535);
            smplMissileNoLock.setInfinite(true);
        }
        missileName = Property.stringValue(class1, "friendlyName", "Missile");
    }

    public void changeMissileClass(Class class1)
    {
        cancelMissileGrowl();
        rocketsList.clear();
        createMissileList(rocketsList, class1);
    }

    public static float angleBetween(Actor actor, Actor actor1)
    {
        float f = 180.1F;
        double d1 = 0.0D;
        Loc loc = new Loc();
        Point3d point3d = new Point3d();
        Point3d point3d1 = new Point3d();
        Vector3d vector3d = new Vector3d();
        Vector3d vector3d1 = new Vector3d();
        actor.pos.getAbs(loc);
        loc.get(point3d);
        actor1.pos.getAbs(point3d1);
        vector3d.sub(point3d1, point3d);
        d1 = vector3d.length();
        vector3d.scale(1.0D / d1);
        vector3d1.set(1.0D, 0.0D, 0.0D);
        loc.transform(vector3d1);
        d1 = vector3d1.dot(vector3d);
        f = Geom.RAD2DEG((float)Math.acos(d1));
        return f;
    }

    public static float angleBetween(Actor actor, Vector3f vector3f)
    {
        return angleBetween(actor, new Vector3d(vector3f));
    }

    public static float angleBetween(Actor actor, Vector3d vector3d)
    {
        Vector3d vector3d1 = new Vector3d();
        vector3d1.set(vector3d);
        double d1 = 0.0D;
        Loc loc = new Loc();
        Point3d point3d = new Point3d();
        Vector3d vector3d2 = new Vector3d();
        actor.pos.getAbs(loc);
        loc.get(point3d);
        d1 = vector3d1.length();
        vector3d1.scale(1.0D / d1);
        vector3d2.set(1.0D, 0.0D, 0.0D);
        loc.transform(vector3d2);
        d1 = vector3d2.dot(vector3d1);
        return Geom.RAD2DEG((float)Math.acos(d1));
    }

    public static float angleActorBetween(Actor actor, Actor actor1)
    {
        float f = 180.1F;
        double d1 = 0.0D;
        Loc loc = new Loc();
        Point3d point3d = new Point3d();
        Point3d point3d1 = new Point3d();
        Vector3d vector3d = new Vector3d();
        Vector3d vector3d1 = new Vector3d();
        actor.pos.getAbs(loc);
        loc.get(point3d);
        actor1.pos.getAbs(point3d1);
        vector3d.sub(point3d1, point3d);
        d1 = vector3d.length();
        vector3d.scale(1.0D / d1);
        vector3d1.set(1.0D, 0.0D, 0.0D);
        loc.transform(vector3d1);
        d1 = vector3d1.dot(vector3d);
        f = Geom.RAD2DEG((float)Math.acos(d1));
        return f;
    }

    public static double distanceBetween(Actor actor, Actor actor1)
    {
        double d1 = 99999.998999999996D;
        if(!Actor.isValid(actor) || !Actor.isValid(actor1))
        {
            return d1;
        } else
        {
            Loc loc = new Loc();
            Point3d point3d = new Point3d();
            Point3d point3d1 = new Point3d();
            actor.pos.getAbs(loc);
            loc.get(point3d);
            actor1.pos.getAbs(point3d1);
            double d2 = point3d.distance(point3d1);
            return d2;
        }
    }

    public void setGunNullOwner()
    {
        if(!(missileOwner instanceof Aircraft))
            return;
        Aircraft aircraft = (Aircraft)missileOwner;
        try
        {
            for(int i = 0; i < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons.length; i++)
                if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[i] != null)
                {
                    for(int j = 0; j < ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[i].length; j++)
                        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[i][j] != null && (((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[i][j] instanceof GunNull))
                            ((GunNull)((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.Weapons[i][j]).setOwner(aircraft);

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
        Aircraft aircraft = (Aircraft)missileOwner;
        if(hasMissiles())
        {
            if(NetMissionTrack.isPlaying() || Mission.isNet())
                if(!(((SndAircraft) (aircraft)).FM instanceof RealFlightModel) || !((RealFlightModel)((SndAircraft) (aircraft)).FM).isRealMode())
                    ((RocketGun)rocketsList.get(0)).loadBullets(((RocketGun)rocketsList.get(0)).bullets() - 1);
                else
                if(World.cur().diffCur.Limited_Ammo)
                    ((RocketGun)rocketsList.get(0)).loadBullets(((RocketGun)rocketsList.get(0)).bullets() - 1);
            if((World.cur().diffCur.Limited_Ammo || aircraft != World.getPlayerAircraft()) && ((RocketGun)rocketsList.get(0)).bullets() == 1)
                rocketsList.remove(0);
            if(aircraft != World.getPlayerAircraft())
                Voice.speakAttackByRockets(aircraft);
        }
    }

    private float getMissilePk()
    {
        float f = 0.0F;
        if(Actor.isValid(getMissileTarget()))
            f = Pk(missileOwner, getMissileTarget());
        return f;
    }

    private void checkAIlaunchMissile()
    {
        if(!(missileOwner instanceof Aircraft))
            return;
        Aircraft aircraft = (Aircraft)missileOwner;
        if((((SndAircraft) (aircraft)).FM instanceof RealFlightModel) && ((RealFlightModel)((SndAircraft) (aircraft)).FM).isRealMode() || !(((SndAircraft) (aircraft)).FM instanceof Pilot))
            return;
        if(rocketsList.isEmpty())
            return;
        if(attackDecisionByAI || attackDecisionByWaypoint)
        {
            Autopilotage autopilotage = ((Aircraft)(Aircraft)missileOwner).FM.AP;
            if(attackDecisionByWaypoint && (autopilotage.way.curr().Action != 3 || autopilotage.way.curr().getTarget() == null))
                return;
            Pilot pilot = (Pilot)((SndAircraft) (aircraft)).FM;
            if(pilot.get_maneuver() != 27 && pilot.get_maneuver() != 62 && pilot.get_maneuver() != 63 || ((Maneuver) (pilot)).target == null)
                return;
            trgtAI = ((Interpolate) (((Maneuver) (pilot)).target)).actor;
            int i = 0;
            do
            {
                if(i >= getActiveMissilesSize())
                    break;
                ActiveMissile activemissile = getActiveMissile(i);
                if(activemissile.isAI() && aircraft.getArmy() == activemissile.getOwnerArmy() && activemissile.getVictim() == trgtAI)
                {
                    trgtAI = null;
                    break;
                }
                i++;
            } while(true);
            if((targetType & 1L) != 0L)
            {
                if(Actor.isValid(trgtAI) && ((trgtAI instanceof Aircraft) || (trgtAI instanceof MissileInterceptable)))
                {
                    setMissileTarget(trgtAI);
                    trgtPk = getMissilePk();
                } else
                {
                    trgtPk = 0.0F;
                    return;
                }
            } else
            if((targetType & 16L) != 0L || (targetType & 256L) != 0L)
            {
                if(Actor.isValid(trgtAI))
                {
                    setMissileTarget(trgtAI);
                    trgtPk = getMissilePk();
                } else
                {
                    trgtPk = 0.0F;
                    return;
                }
            } else
            if((targetType & 32L) != 0L)
            {
                setMissileTarget(trgtAI);
                trgtPk = getMissilePk();
            }
        }
        if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AP instanceof AutopilotAI)
            ((AutopilotAI)((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AP).setOverrideMissileControl(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT, false);
        if(trgtPk > getMinPkForAttack() && Actor.isValid(getMissileTarget()) && getMissileTarget().getArmy() != ((Interpolate) (((SndAircraft) (aircraft)).FM)).actor.getArmy() && Time.current() > tMissilePrev + getMillisecondsBetweenMissileLaunchAI())
        {
            tMissilePrev = Time.current();
            ((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT.WeaponControl[2] = true;
            if(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AP instanceof AutopilotAI)
                ((AutopilotAI)((FlightModelMain) (((SndAircraft) (aircraft)).FM)).AP).setOverrideMissileControl(((FlightModelMain) (((SndAircraft) (aircraft)).FM)).CT, true);
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

    private static boolean checkActiveMissileInit()
    {
        if(activeMissiles == null)
        {
            curMission = Mission.cur();
            activeMissiles = new ArrayList();
            return true;
        }
        if(Mission.cur() != curMission)
        {
            curMission = Mission.cur();
            if(activeMissiles != null)
                activeMissiles.clear();
            activeMissiles = new ArrayList();
            return true;
        } else
        {
            return false;
        }
    }

    public static ArrayList getActiveMissiles()
    {
        checkActiveMissileInit();
        return activeMissiles;
    }

    public static void setActiveMissiles(ArrayList arraylist)
    {
        activeMissiles = arraylist;
    }

    public static void addActiveMissile(ActiveMissile activemissile)
    {
        checkActiveMissileInit();
        addTargetHandledByAi(activemissile.getOwnerArmy(), activemissile.getVictim());
        activeMissiles.add(activemissile);
    }

    public static boolean removeActiveMissile(ActiveMissile activemissile)
    {
        if(checkActiveMissileInit())
            return true;
        if(!activeMissiles.contains(activemissile))
        {
            return false;
        } else
        {
            addTargetHandledByAi(activemissile.getOwnerArmy(), activemissile.getVictim());
            return activeMissiles.remove(activemissile);
        }
    }

    public static int getActiveMissilesSize()
    {
        if(checkActiveMissileInit())
            return 0;
        else
            return activeMissiles.size();
    }

    public static ActiveMissile getActiveMissile(int i)
    {
        if(checkActiveMissileInit())
            return null;
        if(i >= activeMissiles.size())
            return null;
        else
            return (ActiveMissile)activeMissiles.get(i);
    }

    public static boolean checkAllActiveMissilesValidity()
    {
        if(activeMissiles == null)
        {
            curMission = Mission.cur();
            activeMissiles = new ArrayList();
            return true;
        }
        boolean flag = true;
        for(int i = 0; i < activeMissiles.size(); i++)
        {
            ActiveMissile activemissile = (ActiveMissile)activeMissiles.get(i);
            if(!activemissile.isValidMissile())
            {
                activeMissiles.remove(i);
                flag = false;
            }
        }

        return flag;
    }

    public static boolean noLaunchSince(long l, int i)
    {
        if(checkActiveMissileInit())
            return true;
        checkAllActiveMissilesValidity();
        for(int j = 0; j < activeMissiles.size(); j++)
        {
            ActiveMissile activemissile = (ActiveMissile)activeMissiles.get(j);
            if(activemissile.getOwnerArmy() == i && Time.current() - activemissile.getLaunchTime() < l)
                return false;
        }

        return true;
    }

    public static boolean missilesLeft(BulletEmitter abulletemitter[])
    {
        for(int i = 0; i < abulletemitter.length; i++)
            if(abulletemitter[i] != null && (abulletemitter[i] instanceof MissileGun) && abulletemitter[i].haveBullets())
                return true;

        return false;
    }

    private static boolean checkTargetsHandledByAi()
    {
        if(targetsHandledByAiAlready == null)
        {
            targetsHandledByAiAlready = new HashMap();
            return true;
        } else
        {
            return false;
        }
    }

    public static boolean isTargetHandledByAi(int i, Actor actor)
    {
        if(actor == null)
            return true;
        checkTargetsHandledByAi();
        int j = actor.hashCode();
        Long long1 = new Long(Conversion.longFromTwoInts(i, j));
        long l = Time.current();
        if(targetsHandledByAiAlready.containsKey(long1))
        {
            long l1 = ((Long)targetsHandledByAiAlready.get(long1)).longValue();
            if(l < l1 || l > l1 + 1000L)
            {
                targetsHandledByAiAlready.remove(long1);
                return false;
            } else
            {
                return true;
            }
        } else
        {
            return false;
        }
    }

    public static long addTargetHandledByAi(int i, Actor actor)
    {
        if(actor == null)
            return 0L;
        checkTargetsHandledByAi();
        int j = actor.hashCode();
        Long long1 = new Long(Conversion.longFromTwoInts(i, j));
        Long long2 = new Long(Time.current());
        Long long3 = (Long)targetsHandledByAiAlready.put(long1, long2);
        if(long3 == null)
            return 0L;
        else
            return long3.longValue();
    }

    public void playMissileGrowlLock(boolean flag)
    {
        if(flag)
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

    private void changeMissileGrowl(int i)
    {
        if((missileOwner != World.getPlayerAircraft() || !((RealFlightModel)((SndAircraft) ((Aircraft)missileOwner)).FM).isRealMode()) && (((SndAircraft) ((Aircraft)missileOwner)).FM instanceof Pilot))
            return;
        setMissileGrowl(i);
        switch(i)
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
        int i = iMissileLockState;
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
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
                flag1 = true;
                if(trgtMissile.getArmy() != World.getPlayerAircraft().getArmy())
                    flag = true;
            }
            if(Actor.isValid(Main3D.cur3D().viewActor()) && Main3D.cur3D().viewActor() == missileOwner)
            {
                Actor actor = Selector.look(true, false, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], World.getPlayerAircraft().getArmy(), -1, World.getPlayerAircraft(), false);
                if(Actor.isValid(actor))
                    flag = true;
            }
            if(flag)
                tLastSeenEnemy = Time.current();
            else
            if(Time.current() - tLastSeenEnemy > 10000L)
                flag2 = true;
            if(flag1)
            {
                if(flag)
                    iMissileLockState = 2;
                else
                if(engageMode == 1)
                    iMissileLockState = 2;
                else
                if(flag2)
                    iMissileLockState = 0;
                else
                    iMissileLockState = 2;
            } else
            if(flag2)
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
                if(i != 1)
                    changeMissileGrowl(1);
                if(i == 0)
                    NetSafeLog.log(missileOwner, missileName + " engaged");
                break;

            case 2: // '\002'
                if(i != 2)
                    changeMissileGrowl(2);
                if(i == 0)
                    NetSafeLog.log(missileOwner, missileName + " engaged");
                break;

            case 0: // '\0'
                if(i != 0)
                {
                    changeMissileGrowl(0);
                    NetSafeLog.log(missileOwner, missileName + " disengaged");
                }
                break;

            default:
                if(i != 0)
                {
                    changeMissileGrowl(0);
                    NetSafeLog.log(missileOwner, missileName + " disengaged");
                }
                break;
            }
        }
        catch(Exception exception1) { }
    }

    public Missile getMissileFromRocketGun(RocketGun rocketgun)
    {
        return (Missile)rocketgun.rocket;
    }

    public Point3f getSelectedActorOffset()
    {
        return selectedActorOffset;
    }

    public float Pk(Actor actor, Actor actor1)
    {
        float f = 0.0F;
        float f1 = 0.0F;
        float f2 = angleBetween(actor, actor1);
        float f3 = 180F - angleBetween(actor1, actor);
        float f4 = (float)distanceBetween(actor, actor1);
        float f5 = ((SndAircraft) ((Aircraft)actor)).FM.getOverload();
        float f6 = fPkMaxG;
        if((actor instanceof Aircraft) && (!(((SndAircraft) ((Aircraft)actor)).FM instanceof RealFlightModel) || !((RealFlightModel)((SndAircraft) ((Aircraft)actor)).FM).isRealMode()))
            fMaxG *= 2.0F;
        f = 100F;
        if(f4 > fPkMaxDist || f4 < fPkMinDist || f2 > fPkMaxAngle || f3 > fPkMaxAngleAft || f5 > f6)
            return 0.0F;
        if(f4 > fPkOptDist)
        {
            f1 = f4 - fPkOptDist;
            f1 /= fPkMaxDist - fPkOptDist;
            f -= f1 * f1 * 20F;
        } else
        {
            f1 = fPkOptDist - f4;
            f1 /= fPkOptDist - fPkMinDist;
            f -= f1 * f1 * 60F;
        }
        f1 = f2 / fPkMaxAngle;
        f -= f1 * f1 * 30F;
        f1 = f3 / fPkMaxAngleAft;
        f -= f1 * f1 * 50F;
        f1 = f5 / f6;
        f -= f1 * f1 * 30F;
        if(f < 0.0F)
            f = 0.0F;
        return f;
    }

    private int engineHeatSpreadType(Actor actor)
    {
        if(!(actor instanceof Aircraft))
            return HEAT_SPREAD_360;
        EnginesInterface enginesinterface = ((FlightModelMain) (((SndAircraft)actor).FM)).EI;
        int i = HEAT_SPREAD_NONE;
        for(int j = 0; j < enginesinterface.getNum(); j++)
        {
            int k = enginesinterface.engines[j].getType();
            if(k == 2 || k == 3 || k == 4 || k == 6)
                i |= HEAT_SPREAD_AFT;
            if(k == 0 || k == 1 || k == 7 || k == 8)
                i |= HEAT_SPREAD_360;
        }

        return i;
    }

    private int engineHeatSpreadType(Motor motor)
    {
        int i = HEAT_SPREAD_NONE;
        int j = motor.getType();
        if(j == 2 || j == 3 || j == 4 || j == 6)
            i |= HEAT_SPREAD_AFT;
        if(j == 0 || j == 1 || j == 7 || j == 8)
            i |= HEAT_SPREAD_360;
        return i;
    }

    public Actor lookForGuidedMissileTarget(Actor actor, float f, float f1, double d1)
    {
        return lookForGuidedMissileTargetAircraft(actor, f, f1, d1);
    }

    public Actor lookForGuidedMissileTarget(Actor actor, float f, float f1, double d1, long l)
    {
        Actor actor1 = null;
        if((l & 1L) != 0L)
            actor1 = lookForGuidedMissileTargetAircraft(actor, f, f1, d1);
        else
        if((l & 16L) != 0L)
            actor1 = lookForGuidedMissileTargetGround(actor, f, f1, d1);
        else
        if((l & 256L) != 0L)
            actor1 = lookForGuidedMissileTargetShip(actor, f, f1, d1);
        else
        if((l & 32L) != 0L)
            actor1 = lookForGuidedMissileTargetLocate(actor, f, f1, d1);
        return actor1;
    }

    public Actor lookForGuidedMissileTargetAircraft(Actor actor, float f, float f1, double d1)
    {
        double d2 = 0.0D;
        float f2 = 0.0F;
        float f4 = 0.0F;
        float f6 = 0.0F;
        float f9 = 0.0F;
        Actor actor1 = null;
        Point3f point3f = new Point3f(0.0F, 0.0F, 0.0F);
        if(iDetectorMode == 0)
            return actor1;
        try
        {
            List list = Engine.targets();
            int i = list.size();
            for(int k = 0; k < i; k++)
            {
                Actor actor2 = (Actor)list.get(k);
                if((actor2 instanceof Aircraft) || (actor2 instanceof MissileInterceptable))
                {
                    double d3 = distanceBetween(actor, actor2);
                    if(d3 <= d1)
                    {
                        float f3 = angleBetween(actor, actor2);
                        if(f3 <= f)
                        {
                            float f5 = 180F - angleBetween(actor2, actor);
                            if(f5 <= f1 || iDetectorMode != 1 || (engineHeatSpreadType(actor2) & HEAT_SPREAD_360) != 0)
                                switch(iDetectorMode)
                                {
                                default:
                                    break;

                                case 1: // '\001'
                                    float f10 = 0.0F;
                                    int l = 0;
                                    EnginesInterface enginesinterface = null;
                                    if(actor2 instanceof Aircraft)
                                    {
                                        enginesinterface = ((FlightModelMain) (((SndAircraft)(SndAircraft)actor2).FM)).EI;
                                        int i1 = enginesinterface.getNum();
                                        for(int j1 = 0; j1 < i1; j1++)
                                        {
                                            Motor motor = enginesinterface.engines[j1];
                                            float f12 = motor.getEngineForce().length();
                                            if(engineHeatSpreadType(motor) == HEAT_SPREAD_NONE)
                                                f12 = 0.0F;
                                            if(engineHeatSpreadType(motor) == HEAT_SPREAD_360)
                                                f12 /= 10F;
                                            if(f12 > f10)
                                            {
                                                f10 = f12;
                                                l = j1;
                                            }
                                        }

                                    } else
                                    if(actor2 instanceof MissileInterceptable)
                                        f10 = Property.floatValue(actor2.getClass(), "force", 1000F);
                                    float f7 = f10 / f3 / (float)(d3 * d3);
                                    if(!actor2.isAlive())
                                        f7 /= 10F;
                                    if(f7 > f9)
                                    {
                                        f9 = f7;
                                        actor1 = actor2;
                                        if(actor2 instanceof Aircraft)
                                            point3f = enginesinterface.engines[l].getEnginePos();
                                    }
                                    break;

                                case 2: // '\002'
                                case 3: // '\003'
                                case 4: // '\004'
                                    float f11 = 0.0F;
                                    if(actor2 instanceof Aircraft)
                                    {
                                        Mass mass = ((FlightModelMain) (((SndAircraft)(SndAircraft)actor2).FM)).M;
                                        f11 = mass.getFullMass();
                                    } else
                                    if(actor2 instanceof MissileInterceptable)
                                        f11 = Property.floatValue(actor2.getClass(), "massa", 1000F);
                                    float f8 = f11 / f3 / (float)(d3 * d3);
                                    if(!actor2.isAlive())
                                        f8 /= 10F;
                                    if(f8 <= f9)
                                        break;
                                    f9 = f8;
                                    actor1 = actor2;
                                    if(actor2 instanceof Aircraft)
                                        point3f.set(0.0F, 0.0F, 0.0F);
                                    break;
                                }
                        }
                    }
                }
            }

        }
        catch(Exception exception)
        {
            EventLog.type("Exception in selectedActor");
            EventLog.type(exception.toString());
            EventLog.type(exception.getMessage());
        }
        if((actor instanceof Aircraft) && actor1 != null)
        {
            Aircraft aircraft = (Aircraft)actor;
            if(!aircraft.FM.isPlayers() || !(aircraft.FM instanceof RealFlightModel) || !((RealFlightModel)aircraft.FM).isRealMode())
            {
                checkAllActiveMissilesValidity();
                int j = 0;
                do
                {
                    if(j >= getActiveMissilesSize())
                        break;
                    ActiveMissile activemissile = getActiveMissile(j);
                    if(activemissile.isAI() && aircraft.FM.actor.getArmy() == activemissile.getOwnerArmy() && activemissile.getVictim() == actor1)
                    {
                        actor1 = null;
                        break;
                    }
                    j++;
                } while(true);
            }
        }
        selectedActorOffset.set(point3f);
        return actor1;
    }

    public Actor lookForGuidedMissileTargetGround(Actor actor, float f, float f1, double d1)
    {
        float f7;
        Object obj;
        Point3f point3f;
        double d2 = 0.0D;
        float f2 = 0.0F;
        float f5 = 0.0F;
        f7 = 0.0F;
        obj = null;
        point3f = new Point3f(0.0F, 0.0F, 0.0F);
        if(!(actor instanceof Aircraft) || iDetectorMode == 0)
            return ((Actor) (obj));
        BridgeSegment bridgesegment;
        double d5;
        List list;
        int l;
        int i1;
        ArrayList arraylist = World.cur().statics.bridges;
        int i = arraylist.size();
        LongBridge longbridge = null;
        bridgesegment = null;
        d5 = d1;
        for(int j = 0; j < i; j++)
        {
            LongBridge longbridge1 = (LongBridge)arraylist.get(j);
            if(!longbridge1.isAlive())
                continue;
            double d3 = distanceBetween(actor, longbridge1);
            if(d3 > d1)
                continue;
            float f3 = angleBetween(actor, longbridge1);
            if(f3 <= f && d3 < d5)
            {
                longbridge = longbridge1;
                d5 = d3;
            }
        }

        if(longbridge != null)
        {
            int k = longbridge.NumStateBits() / 2;
            bridgesegment = BridgeSegment.getByIdx(longbridge.bridgeIdx(), World.Rnd().nextInt(k));
        }
        list = Engine.targets();
        l = list.size();
        i1 = 0;
_L8:
        float f6;
        Actor actor1;
        int j1;
        if(i1 >= l)
            break MISSING_BLOCK_LABEL_476;
        actor1 = (Actor)list.get(i1);
        if(!(actor1 instanceof TgtFlak) && !(actor1 instanceof TgtTank) && !(actor1 instanceof TgtTrain) && !(actor1 instanceof TgtVehicle))
            break MISSING_BLOCK_LABEL_470;
        double d4 = distanceBetween(actor, actor1);
        if(d4 > d1)
            break MISSING_BLOCK_LABEL_470;
        float f4 = angleBetween(actor, actor1);
        if(f4 > f)
            break MISSING_BLOCK_LABEL_470;
        f6 = 1.0F / f4 / (float)(d4 * d4);
        if(!actor1.isAlive())
            f6 /= 10F;
        checkAllActiveMissilesValidity();
        j1 = 0;
_L7:
        if(j1 >= getActiveMissilesSize()) goto _L2; else goto _L1
_L1:
        ActiveMissile activemissile = getActiveMissile(j1);
        if(!actorIsAI(actor) || actor.getArmy() != activemissile.getOwnerArmy() || activemissile.getVictim() != actor1) goto _L4; else goto _L3
_L3:
        f6 = 0.0F;
          goto _L2
_L4:
        if(iDetectorMode == 1 || multiTrackingCapable || activemissile.getOwner() != actor || activemissile.getVictim() == null) goto _L6; else goto _L5
_L5:
        if(actorIsAI(actor))
            return null;
        return activemissile.getVictim();
_L6:
        j1++;
          goto _L7
_L2:
        if(f6 > f7)
        {
            f7 = f6;
            obj = actor1;
            point3f.set(0.0F, 0.0F, 0.0F);
        }
        i1++;
          goto _L8
        if(bridgesegment != null && d5 < distanceBetween(actor, ((Actor) (obj))))
        {
            obj = bridgesegment;
            point3f.set(0.0F, 0.0F, 3F);
        }
        break MISSING_BLOCK_LABEL_534;
        Exception exception;
        exception;
        EventLog.type("Exception in selectedActor");
        EventLog.type(exception.toString());
        EventLog.type(exception.getMessage());
        selectedActorOffset.set(point3f);
        return ((Actor) (obj));
    }

    public Actor lookForGuidedMissileTargetLocate(Actor actor, float f, float f1, double d1)
    {
        double d2 = 0.0D;
        float f2 = 0.0F;
        float f3 = 0.0F;
        float f4 = 0.0F;
        Actor actor1 = null;
        Point3f point3f = new Point3f(0.0F, 0.0F, 0.0F);
        if(!(actor instanceof Aircraft) || iDetectorMode == 0)
            return actor1;
        try
        {
            Autopilotage autopilotage = ((Aircraft)(Aircraft)actor).FM.AP;
            int i = autopilotage.way.Cur();
            boolean flag = false;
            Actor actor2 = null;
            do
            {
                if(autopilotage.way.curr().Action == 3 || autopilotage.way.curr().getTarget() != null)
                {
                    actor2 = autopilotage.way.curr().getTarget();
                    if(actor2.getSpeed(null) < 1.0D)
                    {
                        double d3 = distanceBetween(actor, actor2);
                        if(d3 <= d1)
                        {
                            flag = true;
                            break;
                        }
                    }
                }
                actor2 = null;
                if(autopilotage.way.isLast())
                    break;
                autopilotage.way.next();
            } while(true);
            autopilotage.way.setCur(i);
            if(flag)
                actor1 = actor2;
            if(actor1 instanceof Bridge)
                point3f = new Point3f(0.0F, 0.0F, 3F);
        }
        catch(Exception exception)
        {
            EventLog.type("Exception in selectedActor");
            EventLog.type(exception.toString());
            EventLog.type(exception.getMessage());
        }
        selectedActorOffset.set(point3f);
        return actor1;
    }

    private boolean actorIsAI(Actor actor)
    {
        if(!(actor instanceof Aircraft))
            return true;
        if(((SndAircraft) ((Aircraft)actor)).FM == null)
            return true;
        else
            return (actor != World.getPlayerAircraft() || !((RealFlightModel)((SndAircraft) ((Aircraft)actor)).FM).isRealMode()) && (((SndAircraft) ((Aircraft)actor)).FM instanceof Pilot);
    }

    public Actor lookForGuidedMissileTargetShip(Actor actor, float f, float f1, double d1)
    {
        float f6;
        Actor actor1;
        double d2 = 0.0D;
        float f2 = 0.0F;
        float f4 = 0.0F;
        f6 = 0.0F;
        actor1 = null;
        selectedActorOffset.set(new Point3f(0.0F, 0.0F, 0.0F));
        if(!(actor instanceof Aircraft) || iDetectorMode == 0)
            return actor1;
        List list;
        int i;
        int j;
        list = Engine.targets();
        i = list.size();
        j = 0;
_L8:
        float f5;
        Actor actor2;
        int k;
        if(j >= i)
            break MISSING_BLOCK_LABEL_397;
        actor2 = (Actor)list.get(j);
        if(!(actor2 instanceof TgtShip))
            break MISSING_BLOCK_LABEL_364;
        double d3 = distanceBetween(actor, actor2);
        if(d3 > d1)
            break MISSING_BLOCK_LABEL_364;
        float f3 = angleBetween(actor, actor2);
        if(f3 > f || actor2.pos.getAbsPoint().z < 0.0D && !canTrackSubs)
            break MISSING_BLOCK_LABEL_364;
        f5 = 1.0F / f3 / (float)(d3 * d3);
        if(!actor2.isAlive())
            f5 /= 10F;
        checkAllActiveMissilesValidity();
        k = 0;
_L7:
        if(k >= getActiveMissilesSize()) goto _L2; else goto _L1
_L1:
        ActiveMissile activemissile = getActiveMissile(k);
        if(!actorIsAI(actor) || actor.getArmy() != activemissile.getOwnerArmy() || activemissile.getVictim() != actor2) goto _L4; else goto _L3
_L3:
        f5 = 0.0F;
          goto _L2
_L4:
        if(iDetectorMode == 1 || multiTrackingCapable || activemissile.getOwner() != actor || activemissile.getVictim() == null) goto _L6; else goto _L5
_L5:
        if(actorIsAI(actor))
            return null;
        if(activemissile.getVictim().pos.getAbsPoint().z < 5D && !canTrackSubs)
            selectedActorOffset.set(new Point3f(0.0F, 0.0F, 5F));
        return activemissile.getVictim();
_L6:
        k++;
          goto _L7
_L2:
        if(f5 > f6)
        {
            f6 = f5;
            actor1 = actor2;
        }
        j++;
          goto _L8
        Exception exception;
        exception;
        EventLog.type("Exception in selectedActor");
        EventLog.type(exception.toString());
        EventLog.type(exception.getMessage());
        if(actor1 != null && actor1.pos.getAbsPoint().z < 5D && !canTrackSubs)
            selectedActorOffset.set(new Point3f(0.0F, 0.0F, 5F));
        return actor1;
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
