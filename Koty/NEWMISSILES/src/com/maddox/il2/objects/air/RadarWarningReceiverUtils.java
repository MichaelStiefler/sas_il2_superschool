// Source File Name: RadarWarningReceiverUtils.java
// Author:           western0221
// Last Modified by: western0221 on 13th/Feb./2019
package com.maddox.il2.objects.air;
import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.artillery.ArtilleryGeneric;
import com.maddox.il2.objects.vehicles.stationary.StationaryGeneric;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.il2.objects.weapons.Missile;
//import com.maddox.il2.objects.weapons.MissileSAM;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.sas1946.il2.util.TrueRandom;

import java.util.*;
public class RadarWarningReceiverUtils {
    public class RWRdata implements Comparable {
        Actor actor;
        double distance;
        long firstDetectTime;
        long lastDetectTime;
        double lastDetectDistance;
        float lastDetectDirection;
        SoundFX fxRwrToneGen0;
        boolean bFinalWarned;
        public RWRdata() {
            actor = null;
            distance = -1.0D;
            firstDetectTime = -1L;
            lastDetectTime = -1L;
            lastDetectDistance = -1.0D;
            lastDetectDirection = 360.1F;
            fxRwrToneGen0 = null;
            bFinalWarned = false;
        }
        public int compareTo(Object other) {
            RWRdata otherRWR = (RWRdata)other;
            int r = 0;
            if(this.distance - otherRWR.distance > 0.0D) r = 1;
            else if(this.distance - otherRWR.distance < 0.0D) r = -1;
            return r;
        }
    }
    public static float angleBetween(Actor actorFrom, Point3d pointTo) {
        float angleRetVal = 180.1F;
        if(!(Actor.isValid(actorFrom))) return angleRetVal;
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
        if(!(Actor.isValid(actorTo))) return angleRetVal;
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
        if(!(Actor.isValid(actorFrom)) || !(Actor.isValid(actorTo))) return angleRetVal;
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
    public static float angle360Between(Actor actorFrom, Actor actorTo) {
        float angleRetVal = 360.1F;
        if(!(Actor.isValid(actorFrom)) || !(Actor.isValid(actorTo))) return angleRetVal;
        double angleDoubleTemp = 0.0D;
        double yaw = -actorFrom.pos.getAbsOrient().getYaw() + 90D;
        if(yaw < 0D) yaw += 360D;
        double x = actorTo.pos.getAbsPoint().x - actorFrom.pos.getAbsPoint().x;
        double y = actorTo.pos.getAbsPoint().y - actorFrom.pos.getAbsPoint().y;
        double deg = Math.toDegrees(Math.atan2(y, -x)) - 90D;
        if(deg < 0D) deg += 360D;
        double degNew = deg - yaw;
        if(degNew < 0D) degNew += 360D;
        angleRetVal = (float)degNew;
        return angleRetVal;
    }
    public static float angleBetween(Actor actorFrom, Vector3d targetVector) {
        float angleRetVal = 180.1F;
        if(!(Actor.isValid(actorFrom))) return angleRetVal;
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
    public static float pitchBetweenPnM(Actor actorFrom, Actor actorTo) {
        float angleRetVal = 360.1F;
        if(!(Actor.isValid(actorFrom)) || !(Actor.isValid(actorTo))) return angleRetVal;
        double angleDoubleTemp = 0.0D;
        double pitch = actorFrom.pos.getAbsOrient().getPitch();
        if(pitch < 0D) pitch += 360D;
        if(pitch > 360D) pitch -= 360D;
        double x = actorTo.pos.getAbsPoint().x - actorFrom.pos.getAbsPoint().x;
        double y = actorTo.pos.getAbsPoint().y - actorFrom.pos.getAbsPoint().y;
        double xx = Math.sqrt(x * x + y * y);
        double z = actorTo.pos.getAbsPoint().z - actorFrom.pos.getAbsPoint().z;
        double deg = Math.toDegrees(Math.atan2(z, xx));
        if(deg < 0D) deg += 360D;
        if(deg > 360D) deg -= 360D;
        double degNew = deg - pitch;
        if(degNew < -180D) degNew += 360D;
        if(degNew > 180D) degNew -= 360D;
        angleRetVal = (float)degNew;
        return angleRetVal;
    }
    public static double distanceBetween(Actor actorFrom, Actor actorTo) {
        double distanceRetVal = 99999.999D;
        if(!(Actor.isValid(actorFrom)) || !(Actor.isValid(actorTo))) return distanceRetVal;
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
        if(logActor == World.getPlayerAircraft() && !logActor.isNetMirror()) {
            HUD.log(i, logLine);
        }
    }
    public static void LocalLog(Actor logActor, String logLine) {
        if(logActor == World.getPlayerAircraft() && !logActor.isNetMirror()) {
            HUD.log(logLine);
        }
    }
    private static String actorString(Actor actor) {
        String s = actor.getClass().getName();
        int i = s.lastIndexOf('.');
        String strSection = s.substring(i + 1);
        strSection = strSection + '@' + Integer.toHexString(actor.hashCode());
        return strSection;
    }
    public RadarWarningReceiverUtils(Actor theOwner) {
        this.initParams(theOwner);
    }
    public RadarWarningReceiverUtils(Actor theOwner, int theMaxDetectRadarNum, int theKeepSeconds, double theReceiveElevationDegrees,
                                     boolean theAbleDetectIRmissiles, boolean theAbleDetectElevation, boolean theShowTextWarning) {
        this.initParams(theOwner, 0, theMaxDetectRadarNum, theKeepSeconds, theReceiveElevationDegrees, true, theAbleDetectIRmissiles, theAbleDetectElevation, 12, theShowTextWarning);
    }
    public RadarWarningReceiverUtils(Actor theOwner, int theGen, int theMaxDetectRadarNum, int theKeepSeconds, double theReceiveElevationDegrees,
                                     boolean theAbleDetectIRmissiles, boolean theAbleDetectElevation, boolean theShowTextWarning) {
        this.initParams(theOwner, theGen, theMaxDetectRadarNum, theKeepSeconds, theReceiveElevationDegrees, true, theAbleDetectIRmissiles, theAbleDetectElevation, 12, theShowTextWarning);
    }
    public RadarWarningReceiverUtils(Actor theOwner, int theMaxDetectRadarNum, int theKeepSeconds, double theReceiveElevationDegrees,
                                     boolean theAbleDetectRHmissiles, boolean theAbleDetectIRmissiles, boolean theAbleDetectElevation,  int theSectorNum, boolean theShowTextWarning) {
        this.initParams(theOwner, 0, theMaxDetectRadarNum, theKeepSeconds, theReceiveElevationDegrees, theAbleDetectRHmissiles, theAbleDetectIRmissiles, theAbleDetectElevation, theSectorNum, theShowTextWarning);
    }
    public RadarWarningReceiverUtils(Actor theOwner, int theGen, int theMaxDetectRadarNum, int theKeepSeconds, double theReceiveElevationDegrees,
                                     boolean theAbleDetectRHmissiles, boolean theAbleDetectIRmissiles, boolean theAbleDetectElevation,  int theSectorNum, boolean theShowTextWarning) {
        this.initParams(theOwner, theGen, theMaxDetectRadarNum, theKeepSeconds, theReceiveElevationDegrees, theAbleDetectRHmissiles, theAbleDetectIRmissiles, theAbleDetectElevation, theSectorNum, theShowTextWarning);
    }
    public RadarWarningReceiverUtils(Actor theOwner, int theMaxDetectRadarNum, int theKeepSeconds, double theReceiveElevationDegrees,
                                     boolean theAbleDetectRHmissiles, boolean theAbleDetectIRmissiles, boolean theAbleDetectElevation, int theSectorNum,
                                     boolean theShowTextWarning, int theDebugLogLevel, String theDebugPlaneName) {
        this.initParams(theOwner, 0, theMaxDetectRadarNum, theKeepSeconds, theReceiveElevationDegrees, theAbleDetectRHmissiles, theAbleDetectIRmissiles, theAbleDetectElevation, theSectorNum, theShowTextWarning, theDebugLogLevel, theDebugPlaneName);
    }
    public RadarWarningReceiverUtils(Actor theOwner, int theGen, int theMaxDetectRadarNum, int theKeepSeconds, double theReceiveElevationDegrees,
                                     boolean theAbleDetectRHmissiles, boolean theAbleDetectIRmissiles, boolean theAbleDetectElevation, int theSectorNum,
                                     boolean theShowTextWarning, int theDebugLogLevel, String theDebugPlaneName) {
        this.initParams(theOwner, theGen, theMaxDetectRadarNum, theKeepSeconds, theReceiveElevationDegrees, theAbleDetectRHmissiles, theAbleDetectIRmissiles, theAbleDetectElevation, theSectorNum, theShowTextWarning, theDebugLogLevel, theDebugPlaneName);
    }
    private boolean actorIsAI(Actor theActor) {
        if(!(theActor instanceof Aircraft)) return true;
        if(((Aircraft) theActor).FM == null) return true;
        if((theActor != World.getPlayerAircraft() || !((RealFlightModel) ((Aircraft) theActor).FM).isRealMode()) && (((Aircraft) theActor).FM instanceof Pilot)) return true;
        return false;
    }
    private void initCommon() {
        this.IRmissiles = new ArrayList();
        this.RHmissiles = new ArrayList();
        this.radars = new ArrayList();
        this.radarsLock = new ArrayList();
        this.ignoreMissileList = new ArrayList();
        this.rwrOwner = null;
        this.rwrGeneration = 0;
        this.maxDetectRadarNum = 16;
        this.keepMiliseconds = 7000L;
        this.receiveElevationDegrees = 45.0D;
        this.bAbleDetectIRmissiles = false;
    }
//delete
/*    public void initLockTones(String sFxRwrToneLock, String sFxRwrToneSearch, String sFxRwrToneMissile,
                              String sSmplRwrLock, String sSmplRwrSearch, String sSmplRwrMissile) {
        this.initLockTones(sFxRwrToneLock, sFxRwrToneSearch, sFxRwrToneMissile, (String) null, sSmplRwrLock, sSmplRwrSearch, sSmplRwrMissile, (String) null);
    }
    public void initLockTones(String sFxRwrToneLock, String sFxRwrToneSearch, String sFxRwrToneMissile, String sFxRwrToneThreatNew,
                              String sSmplRwrLock, String sSmplRwrSearch, String sSmplRwrMissile, String sSmplRwrThreatNew) {
        if(this.lockTonesInitialized) return;
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: initLockTones() entering .... this.rwrOwner=" + this.rwrOwner);
        if(this.rwrOwner == World.getPlayerAircraft()) {
            this.setFxRwrToneLock(sFxRwrToneLock, 1.0F);
            this.setFxRwrToneSearch(sFxRwrToneSearch, 1.0F);
            this.setFxRwrToneMissile(sFxRwrToneMissile, 1.0F);
            this.setFxRwrToneThreatNew(sFxRwrToneThreatNew, 1.0F);
//delete            this.setSmplRwrLock(sSmplRwrLock);
//delete            this.setSmplRwrSearch(sSmplRwrSearch);
//delete            this.setSmplRwrMissile(sSmplRwrMissile);
//delete            this.setSmplRwrThreatNew(sSmplRwrThreatNew);
        }
        this.lockTonesInitialized = true;
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "initLockTones finished");
    }
*/
//delete
    private void initParams(Actor theOwner) {
        this.initCommon();
        if(theOwner instanceof Aircraft && Actor.isValid(theOwner) && theOwner instanceof TypeRadarWarningReceiver) {
            this.rwrOwner = theOwner;
            if(((Aircraft) theOwner).FM != null)
                this.FM = ((Aircraft) theOwner).FM;
        }
    }
/*
    private void initParams(Actor theOwner, int theGen, int theMaxDetectRadarNum, int theKeepSeconds, double theReceiveElevationDegrees,
                            boolean theAbleDetectIRmissiles, boolean theAbleDetectElevation, boolean theShowTextWarning) {
        this.initCommon();
        if(theOwner instanceof Aircraft && Actor.isValid(theOwner) && theOwner instanceof TypeRadarWarningReceiver) {
            this.rwrOwner = theOwner;
            this.rwrGeneration = theGen;
            if(((Aircraft) theOwner).FM != null)
                this.FM = ((Aircraft) theOwner).FM;
            this.maxDetectRadarNum = theMaxDetectRadarNum;
            this.keepMiliseconds = (long) theKeepSeconds * 1000L;
            this.receiveElevationDegrees = theReceiveElevationDegrees;
            this.bAbleDetectIRmissiles = theAbleDetectIRmissiles;
            this.bAbleDetectElevation = theAbleDetectElevation;
            this.bShowTextWarning = theShowTextWarning;
        }
    }
    private void initParams(Actor theOwner, int theGen, int theMaxDetectRadarNum, int theKeepSeconds, double theReceiveElevationDegrees,
                            boolean theAbleDetectIRmissiles, boolean theAbleDetectElevation, boolean theShowTextWarning,
                            int theDebugLogLevel, String theDebugPlaneName) {
        this.initParams(theOwner, theGen, theMaxDetectRadarNum, theKeepSeconds, theReceiveElevationDegrees, theAbleDetectIRmissiles, theAbleDetectElevation, theShowTextWarning);
        iDebugLogLevel = theDebugLogLevel;
        sDebugPlaneName = theDebugPlaneName;
    }
*/
    private void initParams(Actor theOwner, int theGen, int theMaxDetectRadarNum, int theKeepSeconds, double theReceiveElevationDegrees,
                            boolean theAbleDetectRHmissiles, boolean theAbleDetectIRmissiles, boolean theAbleDetectElevation, int theSectorNum,
                            boolean theShowTextWarning) {
        this.initCommon();
        if(theOwner instanceof Aircraft && Actor.isValid(theOwner) && theOwner instanceof TypeRadarWarningReceiver) {
            this.rwrOwner = theOwner;
            this.rwrGeneration = theGen;
            if(((Aircraft) theOwner).FM != null)
                this.FM = ((Aircraft) theOwner).FM;
            this.maxDetectRadarNum = theMaxDetectRadarNum;
            this.keepMiliseconds = (long) theKeepSeconds * 1000L;
            this.receiveElevationDegrees = theReceiveElevationDegrees;
            this.bAbleDetectRHmissiles = theAbleDetectRHmissiles;
            this.bAbleDetectIRmissiles = theAbleDetectIRmissiles;
            this.bAbleDetectElevation = theAbleDetectElevation;
            if(theSectorNum < 5) this.detectSectorNum = 4;
            else if(theSectorNum < 9) this.detectSectorNum = 8;
            else this.detectSectorNum = 12;
            this.bShowTextWarning = theShowTextWarning;
        }
    }
    private void initParams(Actor theOwner, int theGen, int theMaxDetectRadarNum, int theKeepSeconds, double theReceiveElevationDegrees,
                            boolean theAbleDetectRHmissiles, boolean theAbleDetectIRmissiles, boolean theAbleDetectElevation, int theSectorNum,
                            boolean theShowTextWarning, int theDebugLogLevel, String theDebugPlaneName) {
        this.initParams(theOwner, theGen, theMaxDetectRadarNum, theKeepSeconds, theReceiveElevationDegrees, theAbleDetectRHmissiles, theAbleDetectIRmissiles, theAbleDetectElevation, theSectorNum, theShowTextWarning);
        iDebugLogLevel = theDebugLogLevel;
        sDebugPlaneName = theDebugPlaneName;
    }
    public void onAircraftLoaded() {
    }
    public ArrayList getIRmissileList() {
        return this.IRmissiles;
    }
    public ArrayList getRHmissileList() {
        return this.RHmissiles;
    }
    public ArrayList getRadarList() {
        return this.radars;
    }
    public ArrayList getRadarLockList() {
        return this.radarsLock;
    }
    public boolean getRadarSearchedWarning() {
        return this.bRadarSearchedWarning;
    }
    public boolean getRadarLockedWarning() {
        return this.bRadarLockedWarning;
    }
    public boolean getMissileWarning() {
        return this.bMissileWarning;
    }
    public boolean getBackfire() {
        return this.backfire;
    }
    public boolean getAIEmergency() {
        return this.bAIEmergency;
    }
    public void setRwrOwner(Actor value) {
        if(value instanceof Aircraft && value instanceof TypeRadarWarningReceiver) {
            this.rwrOwner = value;
        }
    }
    public void setLockTone(String theSearchPrs, String theLockPrs, String theMissilePrs, String theSearchWav, String theLockWav, String theMissileWav) {
        this.setLockTone(theSearchPrs, theLockPrs, theMissilePrs, (String) null, theSearchWav, theLockWav, theMissileWav, (String) null);
    }
    public void setLockTone(String theSearchPrs, String theLockPrs, String theMissilePrs, String theThreatNewPrs, String theSearchWav, String theLockWav, String theMissileWav, String theThreatNewWav) {
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: setLockTone() entering .... this.rwrOwner=" + this.rwrOwner);
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        this.setFxRwrToneSearch(theSearchPrs);
        this.setFxRwrToneLock(theLockPrs);
        this.setFxRwrToneMissile(theMissilePrs);
        this.setFxRwrToneThreatNew(theThreatNewPrs);
//delete        this.setSmplRwrSearch(theSearchWav);
//delete        this.setSmplRwrLock(theLockWav);
//delete        this.setSmplRwrMissile(theMissileWav);
//delete        this.setSmplRwrThreatNew(theThreatNewWav);
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: setLockTone() finish!");
    }
    public void setLockTone(String theSearchPrs, String theLockPrs, String theMissilePrs, String theThreatNewPrs) {
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: setLockTone() entering .... this.rwrOwner=" + this.rwrOwner);
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        this.setFxRwrToneSearch(theSearchPrs);
        this.setFxRwrToneLock(theLockPrs);
        this.setFxRwrToneMissile(theMissilePrs);
        this.setFxRwrToneThreatNew(theThreatNewPrs);
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: setLockTone() finish!");
    }
    public void setFxRwrToneSearch(String value) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        if(value == null || value == "") {
            this.fxRwrToneSearch = null;
            return;
        }
        // System.out.println("setFxMissileToneLock " + value);
        this.fxRwrToneSearch = this.rwrOwner.newSound(value, false);
        // if(rwrOwner.getRootFX() != null)
        // this.fxRwrToneSearch.setParent(rwrOwner.getRootFX());
        // this.fxRwrToneSearch.setPosition(1, 0, 0);
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: setFxRwrToneSearch() finish!");
    }
    public void setFxRwrToneSearch(String fxName, float theVolume) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        this.setFxRwrToneSearch(fxName);
        this.setFxRwrToneSearchVolume(theVolume);
    }
    public void setFxRwrToneSearchVolume(float value) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        if(this.fxRwrToneSearch == null) return;
        this.fxRwrToneSearch.setVolume(value);
    }
    public void setFxRwrToneLock(String value) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        if(value == null || value == "") {
            this.fxRwrToneLock = null;
            return;
        }
        // System.out.println("setFxMissileToneLock " + value);
        this.fxRwrToneLock = this.rwrOwner.newSound(value, false);
        // if(rwrOwner.getRootFX() != null)
        // this.fxRwrToneLock.setParent(rwrOwner.getRootFX());
        // this.fxRwrToneLock.setPosition(1, 0, 0);
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: setFxRwrToneLock() finish!");
    }
    public void setFxRwrToneLock(String fxName, float theVolume) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        this.setFxRwrToneLock(fxName);
        this.setFxRwrToneLockVolume(theVolume);
    }
    public void setFxRwrToneLockVolume(float value) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        if(this.fxRwrToneLock == null) return;
        this.fxRwrToneLock.setVolume(value);
    }
    public void setFxRwrToneMissile(String value) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        if(value == null || value == "") {
            this.fxRwrToneMissile = null;
            return;
        }
        // System.out.println("setFxMissileToneLock " + value);
        this.fxRwrToneMissile = this.rwrOwner.newSound(value, false);
        // if(rwrOwner.getRootFX() != null)
        // this.fxRwrToneMissile.setParent(rwrOwner.getRootFX());
        // this.fxRwrToneMissile.setPosition(1, 0, 0);
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: setFxRwrToneMissile() finish!");
    }
    public void setFxRwrToneMissile(String fxName, float theVolume) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        this.setFxRwrToneMissile(fxName);
        this.setFxRwrToneMissileVolume(theVolume);
    }
    public void setFxRwrToneMissileVolume(float value) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        if(this.fxRwrToneMissile == null) return;
        this.fxRwrToneMissile.setVolume(value);
    }
    public void setFxRwrToneThreatNew(String value) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        if(value == null || value == "") {
            this.fxRwrToneThreatNew = null;
            return;
        }
        // System.out.println("setFxMissileToneThreatNew " + value);
        this.fxRwrToneThreatNew = this.rwrOwner.newSound(value, false);
        // if(rwrOwner.getRootFX() != null)
        // this.fxRwrToneThreatNew.setParent(rwrOwner.getRootFX());
        // this.fxRwrToneThreatNew.setPosition(1, 0, 0);
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: setFxRwrToneThreatNew() finish!");
    }
    public void setFxRwrToneThreatNew(String fxName, float theVolume) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        this.setFxRwrToneThreatNew(fxName);
        this.setFxRwrToneThreatNewVolume(theVolume);
    }
    public void setFxRwrToneThreatNewVolume(float value) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        if(this.fxRwrToneThreatNew == null) return;
        this.fxRwrToneThreatNew.setVolume(value);
    }
//delete
/*    public void setSmplRwrSearch(String value) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        if(value == null || value == "") {
            this.smplRwrSearch = null;
            return;
        }
        // System.out.println("setSmplMissileNoLock " + value);
        this.smplRwrSearch = new Sample(value, 256, 65535);
        this.smplRwrSearch.setInfinite(true);
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: setSmplRwrSearch() finish!");
    }
    public void setSmplRwrLock(String value) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        if(value == null || value == "") {
            this.smplRwrLock = null;
            return;
        }
        // System.out.println("setSmplMissileLock " + value);
        this.smplRwrLock = new Sample(value, 256, 65535);
        this.smplRwrLock.setInfinite(true);
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: setSmplRwrLock() finish!");
    }
    public void setSmplRwrMissile(String value) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        if(value == null || value == "") {
            this.smplRwrMissile = null;
            return;
        }
        // System.out.println("setSmplMissileLock " + value);
        this.smplRwrMissile = new Sample(value, 256, 65535);
        this.smplRwrMissile.setInfinite(true);
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: setSmplRwrMissile() finish!");
    }
    public void setSmplRwrThreatNew(String value) {
        if(this.rwrOwner != World.getPlayerAircraft()) return;
        if(value == null || value == "") {
            this.smplRwrThreatNew = null;
            return;
        }
        // System.out.println("setSmplThreatNew " + value);
        this.smplRwrThreatNew = new Sample(value, 256, 65535);
        this.smplRwrThreatNew.setInfinite(true);
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: setSmplRwrThreatNew() finish!");
    }
*/
//delete
    public void update() {
        if(lastRWRUpdateTime == Time.current()) return;
        if(this.rwrOwner == null) {
            try {
                System.out.println("ERROR - RadarWarningReceiverUtils.class: rwrOwner value is not set till update() method is called.");
                throw new Exception();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        if(!Actor.isValid(this.rwrOwner)) return;
        Aircraft ownerAircraft = (Aircraft) this.rwrOwner;
        if(this.FM == null && ownerAircraft.FM != null)
            this.FM = ownerAircraft.FM;
        if(this.FM == null) return;
        RWRMissileLaunchWarning();
        RWRRadarLockWarning();
        RWRRadarSearchWarning();
        lastRWRUpdateTime = Time.current();
    }
    private boolean RWRMissileLaunchWarning() {
        List mislist = Engine.missiles();
        if(mislist.size() == 0) {
            if((this.iDebugLogLevel & 3) > 0) {
                if(IRmissiles.size() > 0 || this.RHmissiles.size() > 0)
                    System.out.println(sDebugPlaneName + "RWRUtils: mislist.size() == 0 , make lists clear()");
            }
            if(IRmissiles.size() > 0)
                IRmissiles.clear();
            if(RHmissiles.size() > 0) {
                if(this.rwrGeneration == 0) {
                    RWRdata tempRrwrdata = null;
                    for(int i = 0; i < this.RHmissiles.size(); i++) {
                        tempRrwrdata = (RWRdata)this.RHmissiles.get(i);
                        if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                            tempRrwrdata.fxRwrToneGen0.stop();
                    }
                }
                RHmissiles.clear();
            }
            if(bMissileWarning || this.backfire) {
                bMissileWarning = false;
                playRWRWarning();
                this.backfire = false;
                this.bAIEmergency = false;
                if((this.iDebugLogLevel & 3) > 0)
                    System.out.println(sDebugPlaneName + "RWRUtils: mislist.size() == 0, backfire = false;");
            }
            return false;
        }
        Vector3d vector3d = new Vector3d();
        ArrayList missileHuntMe = new ArrayList();
        Actor actor = null;
        for(int i = 0; i < mislist.size(); i++) {
            actor = (Actor)mislist.get(i);
            if((actor instanceof Missile) && actor.getSpeed(vector3d) > 20D && !this.ignoreMissileList.contains(actor)
               && ((Missile)actor).getMissileTarget() == this.rwrOwner) {
                missileHuntMe.add(actor);
            }
        }
        Point3d point3d = new Point3d();
        this.rwrOwner.pos.getAbs(point3d);
        if((this.iDebugLogLevel & 1) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: before record this.RHmissiles, RHmissiles.size()=" + this.RHmissiles.size());
        if((this.iDebugLogLevel & 2) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: before record this.IRmissiles, IRmissiles.size()=" + this.IRmissiles.size());
        int dt = 0;
        RWRdata tempNrwrdata = null;
        RWRdata tempRrwrdata = null;
        double tempDistance = 0.0D;
        for(int i = 0; i < missileHuntMe.size(); i++) {
            Missile missile = (Missile)missileHuntMe.get(i);
            dt = missile.getDetectorType();
                // DETECTOR_TYPE_RADAR_HOMING = 2
                // DETECTOR_TYPE_RADAR_BEAMRIDING = 3
                // DETECTOR_TYPE_RADAR_TRACK_VIA_MISSILE = 4
            // Pure RWR can detect only Active Radar Homing missile like AIM-54, AIM-120 ..... not Semi-Active ones
            if((this.bAbleDetectIRmissiles && (dt == 2 || dt == 3)) || dt == 4) {
                tempDistance = distanceBetween(this.rwrOwner, missile);
                double zDiff = 0.0D;
                double elevDegree = 0.0D;
                if(tempDistance > 0D) {
                    zDiff = missile.pos.getAbsPoint().z - this.rwrOwner.pos.getAbsPoint().z;
                    elevDegree = Math.toDegrees(Math.atan(Math.abs(zDiff) / tempDistance));
                    if((this.iDebugLogLevel & 1) > 0)
                        System.out.println(sDebugPlaneName + "RWRUtils: " + actorString(missile) + " 's elevation=" + Math.floor(elevDegree * 1000D) * 0.001D + " deg.");
                    if(elevDegree > this.receiveElevationDegrees) continue;
                }
                boolean bInserted = false;
                for(int j = 0; j < this.RHmissiles.size(); j++) {
                    if((this.iDebugLogLevel & 1) > 0)
                        System.out.println(sDebugPlaneName + "RWRUtils: ((RWRdata)this.RHmissiles.get(" + j + ")).actor=" + actorString(((RWRdata)this.RHmissiles.get(j)).actor) + " , (Actor)missile=" + actorString((Actor)missile));
                    if(((RWRdata)this.RHmissiles.get(j)).actor == (Actor)missile) {
                        tempRrwrdata = (RWRdata)this.RHmissiles.get(j);
                        if(tempDistance < tempRrwrdata.distance) {
                            tempRrwrdata.lastDetectTime = Time.current();  // when missile passing away, no update lDT
                            tempRrwrdata.lastDetectDistance = tempDistance;
                            tempRrwrdata.lastDetectDirection = angle360Between(this.rwrOwner, actor);
                        }
                        tempRrwrdata.distance = tempDistance;
                        this.RHmissiles.set(j, tempRrwrdata);
                        bInserted = true;
                        break;
                    }
                }
                if(!bInserted) {
                    tempNrwrdata = new RWRdata();
                    tempNrwrdata.actor = (Actor)missile;
                    tempNrwrdata.distance = tempDistance;
                    tempNrwrdata.firstDetectTime = Time.current();
                    tempNrwrdata.lastDetectTime = Time.current();
                    tempNrwrdata.lastDetectDistance = tempDistance;
                    tempNrwrdata.lastDetectDirection = angle360Between(this.rwrOwner, actor);
                    this.RHmissiles.add(tempNrwrdata);
                    if((this.iDebugLogLevel & 1) > 0)
                        System.out.println(sDebugPlaneName + "RWRUtils: new this.RHmissiles.add(" + actorString((Actor)missile) + ") , size()=" + this.RHmissiles.size());
                    if(this.bShowTextWarning && this.bAbleDetectRHmissiles && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode() || !(this.FM instanceof Pilot)) {
                        String sElev = "";
                        if(this.bAbleDetectElevation) {
                            if(elevDegree > 15.0D) {
                                if(zDiff > 0D) sElev = "Above ";
                                else sElev = "Below ";
                            }
                            else sElev = "Level ";
                        }
//                        String sAngl = Integer.toString((int)angle360Between(this.rwrOwner, missile));
//                        LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New RH-MISSILE detected " + sAngl + "deg. " + sElev + "!!!");
                        if(detectSectorNum == 4) {
                            String sector = DEG2sector4(angle360Between(this.rwrOwner, missile));
                            LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New RH-MISSILE detected " + sector + " " + sElev + "!!!");
                        } else if(detectSectorNum == 8) {
                            String sector = DEG2sector8(angle360Between(this.rwrOwner, missile));
                            LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New RH-MISSILE detected " + sector + " " + sElev + "!!!");
                        } else if(detectSectorNum == 12) {
                            String sOclock = Integer.toString(DEG2OCLOCK(angle360Between(this.rwrOwner, missile)));
                            LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New RH-MISSILE detected " + sOclock + " o'clock " + sElev + "!!!");
                        }
                    }
                }
            } else {
                if(!this.bAbleDetectIRmissiles && (dt == 2 || dt == 3)) {
                    // Semi-Active Radar Homing missiles' (AIM-7 etc.) Radar Lock information
                    if(missile.getLockStatus())
                        recordRadarLocked(missile.getOwner(), "");
                }
                tempDistance = distanceBetween(this.rwrOwner, missile);
                double zDiff = 0.0D;
                double elevDegree = 0.0D;
                if(tempDistance > 0D) {
                    zDiff = missile.pos.getAbsPoint().z - this.rwrOwner.pos.getAbsPoint().z;
                    elevDegree = Math.toDegrees(Math.atan(Math.abs(zDiff) / tempDistance));
                    if((this.iDebugLogLevel & 2) > 0)
                        System.out.println(sDebugPlaneName + "RWRUtils: " + actorString(missile) + " 's elevation=" + Math.floor(elevDegree * 1000D) * 0.001D + " deg. , " + (int)tempDistance + " meters. RocketFiring=" + missile.getRocketFiring());
                    // Not using elevation about IR missile detecting.
                    // But using distance and Rocket motor firing.
                    if(tempDistance > 7200D || !missile.getRocketFiring())
                        continue;
                }
                boolean bInserted = false;
                for(int j = 0; j < this.IRmissiles.size(); j++) {
                    if((this.iDebugLogLevel & 2) > 0)
                        System.out.println(sDebugPlaneName + "RWRUtils: ((RWRdata)this.IRmissiles.get(" + j + ")).actor=" + actorString(((RWRdata)this.IRmissiles.get(j)).actor) + " , (Actor)missile=" + actorString((Actor)missile));
                    if(((RWRdata)this.IRmissiles.get(j)).actor == (Actor)missile) {
                        tempRrwrdata = (RWRdata)this.IRmissiles.get(j);
                        if(tempDistance < tempRrwrdata.distance) {
                            tempRrwrdata.lastDetectTime = Time.current();  // when missile passing away, no update lDT
                            tempRrwrdata.lastDetectDistance = tempDistance;
                            tempRrwrdata.lastDetectDirection = angle360Between(this.rwrOwner, actor);
                        }
                        tempRrwrdata.distance = tempDistance;
                        this.IRmissiles.set(j, tempRrwrdata);
                        bInserted = true;
                        break;
                    }
                }
                if(!bInserted) {
                    tempNrwrdata = new RWRdata();
                    tempNrwrdata.actor = (Actor)missile;
                    tempNrwrdata.distance = tempDistance;
                    tempNrwrdata.firstDetectTime = Time.current();
                    tempNrwrdata.lastDetectTime = Time.current();
                    tempNrwrdata.lastDetectDistance = tempDistance;
                    tempNrwrdata.lastDetectDirection = angle360Between(this.rwrOwner, actor);
                    this.IRmissiles.add(tempNrwrdata);
                    if((this.iDebugLogLevel & 2) > 0)
                        System.out.println(sDebugPlaneName + "RWRUtils: new this.IRmissiles.add(" + actorString((Actor)missile) + ") , size()=" + this.IRmissiles.size());
                    if(this.bShowTextWarning && this.bAbleDetectIRmissiles && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode() || !(this.FM instanceof Pilot)) {
                        String sElev = "";
                        if(this.bAbleDetectElevation) {
                            if(elevDegree > 15.0D) {
                                if(zDiff > 0D) sElev = "Above ";
                                else sElev = "Below ";
                            }
                            else sElev = "Level ";
                        }
//                        String sAngl = Integer.toString((int)angle360Between(this.rwrOwner, missile));
//                        LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New MISSILE detected " + sAngl + "deg. " + sElev + "!!!");
                        if(detectSectorNum == 4) {
                            String sector = DEG2sector4(angle360Between(this.rwrOwner, missile));
                            LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New MISSILE detected " + sector + " " + sElev + "!!!");
                        } else if(detectSectorNum == 8) {
                            String sector = DEG2sector8(angle360Between(this.rwrOwner, missile));
                            LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New MISSILE detected " + sector + " " + sElev + "!!!");
                        } else if(detectSectorNum == 12) {
                            String sOclock = Integer.toString(DEG2OCLOCK(angle360Between(this.rwrOwner, missile)));
                            LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New MISSILE detected " + sOclock + " o'clock " + sElev + "!!!");
                        }
                    }
                }
            }
        }
        for(int j = 0; j < this.RHmissiles.size(); j++) {
            tempRrwrdata = (RWRdata)this.RHmissiles.get(j);
            if(tempRrwrdata.lastDetectTime != Time.current() && Actor.isValid(tempRrwrdata.actor)) {
                tempRrwrdata.distance = distanceBetween(this.rwrOwner, tempRrwrdata.actor);
                this.RHmissiles.set(j, tempRrwrdata);
            }
        }
        Collections.sort(this.RHmissiles);
        for(int j = 0; j < this.IRmissiles.size(); j++) {
            tempRrwrdata = (RWRdata)this.IRmissiles.get(j);
            if(tempRrwrdata.lastDetectTime != Time.current() && Actor.isValid(tempRrwrdata.actor)) {
                tempRrwrdata.distance = distanceBetween(this.rwrOwner, tempRrwrdata.actor);
                this.IRmissiles.set(j, tempRrwrdata);
            }
        }
        Collections.sort(this.IRmissiles);
        missileListExpire();
        if(bShowTextWarning && this.bAbleDetectRHmissiles && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode() && !(this.FM instanceof Pilot) && this.FM.isTick(12, 0)) {
            for(int j = 0; j < this.RHmissiles.size(); j++) {
                tempRrwrdata = (RWRdata)this.RHmissiles.get(j);
                if(!tempRrwrdata.bFinalWarned && tempRrwrdata.distance < 1800D && tempRrwrdata.distance > 0D) {
                    String sElev = "";
                    double zDiff = tempRrwrdata.actor.pos.getAbsPoint().z - this.rwrOwner.pos.getAbsPoint().z;
                    double elevDegree = Math.toDegrees(Math.atan(Math.abs(zDiff) / tempRrwrdata.distance));
                    if(this.bAbleDetectElevation) {
                        if(elevDegree > 15.0D) {
                            if(zDiff > 0D) sElev = "Above ";
                            else sElev = "Below ";
                        }
                        else sElev = "Level ";
                    }
//                    String sAngl = Integer.toString((int)angle360Between(this.rwrOwner, tempRrwrdata.actor));
//                    LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "RH-Missile nearly Coming " + sAngl + "deg. " + sElev + "!!!");
                    if(detectSectorNum == 4) {
                        String sector = DEG2sector4(angle360Between(this.rwrOwner, tempRrwrdata.actor));
                        LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "RH-MISSILE nearly Coming " + sector + " " + sElev + "!!!");
                    } else if(detectSectorNum == 8) {
                        String sector = DEG2sector8(angle360Between(this.rwrOwner, tempRrwrdata.actor));
                        LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "RH-MISSILE nearly Coming " + sector + " " + sElev + "!!!");
                    } else if(detectSectorNum == 12) {
                        String sOclock = Integer.toString(DEG2OCLOCK(angle360Between(this.rwrOwner, tempRrwrdata.actor)));
                        LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "RH-Missile nearly Coming " + sOclock + " o'clock " + sElev + "!!!");
                    }
                    this.bAIEmergency = true;
                    tempRrwrdata.bFinalWarned = true;
                    this.RHmissiles.set(j, tempRrwrdata);
                }
            }
        }
        if((this.iDebugLogLevel & 1) > 0) {
            for(int ii = 0; ii < this.RHmissiles.size(); ii++)
                System.out.println(sDebugPlaneName + "RWRUtils: this.RHmissiles(" + ii + "): " + actorString(((RWRdata)this.RHmissiles.get(ii)).actor) + ", d=" + ((RWRdata)this.RHmissiles.get(ii)).distance + ", lDT=" + ((RWRdata)this.RHmissiles.get(ii)).lastDetectTime);
        }
        if((this.iDebugLogLevel & 2) > 0) {
            for(int ii = 0; ii < this.IRmissiles.size(); ii++)
                System.out.println(sDebugPlaneName + "RWRUtils: this.IRmissiles(" + ii + "): " + actorString(((RWRdata)this.IRmissiles.get(ii)).actor) + ", d=" + ((RWRdata)this.IRmissiles.get(ii)).distance + ", lDT=" + ((RWRdata)this.IRmissiles.get(ii)).lastDetectTime);
        }
        if(this.bAbleDetectRHmissiles) {
            if(this.RHmissiles.size() > 0) {
                this.bMissileWarning = true;
                playRWRWarning();
                if((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && (this.FM instanceof Maneuver))
                    this.backfire = true;
            }
        } else {
            if(this.RHmissiles.size() > 0 && (!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && (this.FM instanceof Maneuver)) {
                boolean btemp = false;
                int j = 0;
                for(; j < this.RHmissiles.size(); j++) {
                    tempRrwrdata = (RWRdata)this.RHmissiles.get(j);
                    if(((Missile)tempRrwrdata.actor).getRocketFiring()) {
                        btemp = true;
                        break;
                    }
                }
                if(btemp) {
                    if(((RWRdata)this.RHmissiles.get(j)).distance < 1800D && TrueRandom.nextFloat() < 0.20F * CrewHealthSummaryContainPilot()) {
                        this.backfire = true;
                        this.bAIEmergency = true;
                    }
                }
            }
        }
        if(this.bAbleDetectIRmissiles) {
            if(this.IRmissiles.size() > 0) {
                boolean btemp = false;
                int j = 0;
                for(; j < this.IRmissiles.size(); j++) {
                    tempRrwrdata = (RWRdata)this.IRmissiles.get(j);
                    if(((Missile)tempRrwrdata.actor).getRocketFiring()) {
                        btemp = true;
                        break;
                    }
                }
                if(btemp) {
                    this.bMissileWarning = true;
                    playRWRWarning();
                    if((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && (this.FM instanceof Maneuver)) {
                        this.backfire = true;
                        if(((RWRdata)this.IRmissiles.get(j)).distance < 1800D)
                            this.bAIEmergency = true;
                    }
                }
            }
        } else {
            if(this.IRmissiles.size() > 0 && (!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && (this.FM instanceof Maneuver)) {
                boolean btemp = false;
                int j = 0;
                for(; j < this.IRmissiles.size(); j++) {
                    tempRrwrdata = (RWRdata)this.IRmissiles.get(j);
                    if(((Missile)tempRrwrdata.actor).getRocketFiring()) {
                        btemp = true;
                        break;
                    }
                }
                if(btemp) {
                    if(((RWRdata)this.IRmissiles.get(j)).distance < 1800D && TrueRandom.nextFloat() < 0.20F * CrewHealthSummaryContainPilot()) {
                        this.backfire = true;
                        this.bAIEmergency = true;
                    }
                }
            }
        }
        // RIO message is always shown in ignoring bAbleDetectRHmissiles / bAbleDetectIRmissiles flag.
        if(this.RHmissiles.size() > 0 && this.FM != null && (this.FM.isPlayers() && (this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode()) || !(this.FM instanceof Maneuver) && this.FM.crew > 1 && this.FM.isTick(24, 18)) {
            for(int i = 0; i < this.RHmissiles.size(); i++) {
                tempRrwrdata = (RWRdata)this.RHmissiles.get(i);
                if(((Missile)tempRrwrdata.actor).getRocketFiring()) break;
            }
            if(tempRrwrdata.distance < 1800D && ((Missile)tempRrwrdata.actor).getRocketFiring() && tempRrwrdata.firstDetectTime < Time.current() && TrueRandom.nextFloat() < 0.20F * CrewHealthSummaryWithoutPilot()) {
                String sElev = "";
                double zDiff = tempRrwrdata.actor.pos.getAbsPoint().z - this.rwrOwner.pos.getAbsPoint().z;
                double elevDegree = Math.toDegrees(Math.atan(Math.abs(zDiff) / tempRrwrdata.distance));
                if(elevDegree > 15.0D) {
                    if(zDiff > 0D) sElev = "Above ";
                    else sElev = "Below ";
                }
                else sElev = "Level ";
                String sCoPil = "WSO:";
                Regiment regi = ((Wing)this.rwrOwner.getOwner()).regiment();
                if (regi != null && regi.country() == PaintScheme.countryUSA && ("un".equals(regi.branch()) || "um".equals(regi.branch())))
                    sCoPil = "RIO:";
//                String sAngl = Integer.toString((int)angle360Between(this.rwrOwner, tempRrwrdata.actor));
//                LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, sCoPil + " MISSILE seen " + sAngl + "deg. " + sElev + "!!!");
                String sOclock = Integer.toString(DEG2OCLOCK(angle360Between(this.rwrOwner, tempRrwrdata.actor)));
                LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, sCoPil + " MISSILE seen " + sOclock + " o'clock " + sElev + "!!!");
            }
        }
        if(this.IRmissiles.size() > 0 && this.FM != null && (this.FM.isPlayers() && (this.FM instanceof RealFlightModel) && ((RealFlightModel)this.FM).isRealMode()) || !(this.FM instanceof Maneuver) && this.FM.crew > 1 && this.FM.isTick(24, 6)) {
            for(int i = 0; i < this.IRmissiles.size(); i++) {
                tempRrwrdata = (RWRdata)this.IRmissiles.get(i);
                if(((Missile)tempRrwrdata.actor).getRocketFiring()) break;
            }
            if(tempRrwrdata.distance < 1800D && ((Missile)tempRrwrdata.actor).getRocketFiring() && tempRrwrdata.firstDetectTime < Time.current() && TrueRandom.nextFloat() < 0.20F * CrewHealthSummaryWithoutPilot()) {
                String sElev = "";
                double zDiff = tempRrwrdata.actor.pos.getAbsPoint().z - this.rwrOwner.pos.getAbsPoint().z;
                double elevDegree = Math.toDegrees(Math.atan(Math.abs(zDiff) / tempRrwrdata.distance));
                if(elevDegree > 15.0D) {
                    if(zDiff > 0D) sElev = "Above ";
                    else sElev = "Below ";
                }
                else sElev = "Level ";
                String sCoPil = "WSO:";
                Regiment regi = ((Wing)this.rwrOwner.getOwner()).regiment();
                if (regi != null && regi.country() == PaintScheme.countryUSA && ("un".equals(regi.branch()) || "um".equals(regi.branch())))
                    sCoPil = "RIO:";
//                String sAngl = Integer.toString((int)angle360Between(this.rwrOwner, tempRrwrdata.actor));
//                LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, sCoPil + " MISSILE seen " + sAngl + "deg. " + sElev + "!!!");
                String sOclock = Integer.toString(DEG2OCLOCK(angle360Between(this.rwrOwner, tempRrwrdata.actor)));
                LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, sCoPil + " MISSILE seen " + sOclock + " o'clock " + sElev + "!!!");
            }
        }
        if(this.IRmissiles.size() == 0 && this.RHmissiles.size() == 0 && (this.bMissileWarning || this.backfire || this.bAIEmergency)) {
            this.bMissileWarning = false;
            playRWRWarning();
            this.backfire = false;
            this.bAIEmergency = false;
            if((this.iDebugLogLevel & 3) > 0)
                System.out.println(sDebugPlaneName + "RWRUtils: IRmissiles.size() == 0 && this.RHmissiles.size() == 0, backfire = false;, bAIEmergency = false;");
            return false;
        }
        return true;
    }
    private boolean RWRRadarLockWarning() {
        List list = Engine.targets();
        Vector3d vector3d = new Vector3d();
        ArrayList radarLockMe = new ArrayList();
        Actor actor = null;
        for(int i = 0; i < list.size(); i++) {
            boolean bRecorded = false;
            actor = (Actor)list.get(i);
            if((actor instanceof TypeGuidedMissileCarrier) && actor.getArmy() != this.rwrOwner.getArmy()) {
                if((this.iDebugLogLevel & 4) > 0)
                    System.out.println(sDebugPlaneName + "RWRUtils: RWRRadarLockWarning() - enemy TypeGuidedMissileCarrier " + actorString(actor) + " found");
                GuidedMissileUtils gmu = ((TypeGuidedMissileCarrier)actor).getGuidedMissileUtils();
                // DETECTOR_TYPE_RADAR_HOMING = 2
                // DETECTOR_TYPE_RADAR_BEAMRIDING = 3
                // DETECTOR_TYPE_RADAR_TRACK_VIA_MISSILE = 4
                if(gmu.getMissileTarget() == this.rwrOwner && gmu.getMissileLockState() == 2
                   && (gmu.getDetectorType() == 2 || gmu.getDetectorType() == 3 ||gmu.getDetectorType() == 4)) {
                    radarLockMe.add(actor);
                    bRecorded = true;
                    if((this.iDebugLogLevel & 4) > 0)
                        System.out.println(sDebugPlaneName + "RWRUtils: RWRRadarLockWarning() - enemy TypeGuidedMissileCarrier " + actorString(actor) + " lock me !");
                    if((gmu.getDetectorType() == 2 || gmu.getDetectorType() == 3) && !this.bAbleDetectIRmissiles) {
                        if((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel)this.FM).isRealMode()) && (this.FM instanceof Maneuver))
                            this.backfire = true;
                    }
                }
            }
            if(!bRecorded && actor.getArmy() != this.rwrOwner.getArmy() && distanceBetween(actor, this.rwrOwner) < 3000D) {
                boolean bEquipRadar = false;
                if((actor instanceof TypeRadar) && !(actor instanceof TypeSemiRadar))
                    bEquipRadar = true;
                else if((actor instanceof TypeSemiRadar) && ((TypeSemiRadar)actor).getSemiActiveRadarOn())
                    bEquipRadar = true;
                if(bEquipRadar) {
                    if((this.iDebugLogLevel & 4) > 0)
                        System.out.println(sDebugPlaneName + "RWRUtils: RWRRadarLockWarning() - enemy TypeRadar or TypeSemiRadar " + actorString(actor) + " found in 3000m. (angle=" + (int)angleBetween(actor, this.rwrOwner) + ", pitch=" + (int)pitchBetweenPnM(actor, this.rwrOwner) + ")");
                    if(angleBetween(actor, this.rwrOwner) < 30F && Math.abs(pitchBetweenPnM(actor, this.rwrOwner)) < 30F) {
                        radarLockMe.add(actor);
                        bRecorded = true;
                        if((this.iDebugLogLevel & 4) > 0)
                            System.out.println(sDebugPlaneName + "RWRUtils: RWRRadarLockWarning() - enemy TypeRadar " + actorString(actor) + " may lock me (dest=" + (int)distanceBetween(actor, this.rwrOwner) + ", angle=" + (int)angleBetween(actor, this.rwrOwner) + ", pitch=" + (int)pitchBetweenPnM(actor, this.rwrOwner) + ") !");
                    }
                }
            }
        }
        Point3d point3d = new Point3d();
        this.rwrOwner.pos.getAbs(point3d);
        if((this.iDebugLogLevel & 4) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: RWRRadarLockWarning() - before record this.radarsLock, radarsLock.size()=" + this.radarsLock.size());
        RWRdata tempNrwrdata = null;
        RWRdata tempRrwrdata = null;
        double tempDistance = 0.0D;
        for(int i = 0; i < radarLockMe.size(); i++) {
            Actor tact = (Actor)radarLockMe.get(i);
            tempDistance = distanceBetween(this.rwrOwner, tact);
            double zDiff = 0.0D;
            double elevDegree = 0.0D;
            if(tempDistance > 0D) {
                zDiff = tact.pos.getAbsPoint().z - this.rwrOwner.pos.getAbsPoint().z;
                elevDegree = Math.toDegrees(Math.atan(Math.abs(zDiff) / tempDistance));
                if((this.iDebugLogLevel & 4) > 0)
                    System.out.println(sDebugPlaneName + "RWRUtils: " + actorString(tact) + " 's elevation=" + Math.floor(elevDegree * 1000D) * 0.001D + " deg.");
                if(elevDegree > this.receiveElevationDegrees) continue;
            }
            boolean bInserted = false;
            for(int j = 0; j < this.radarsLock.size(); j++) {
                if((this.iDebugLogLevel & 4) > 0)
                    System.out.println(sDebugPlaneName + "RWRUtils: ((RWRdata)this.radarsLock.get(" + j + ")).actor=" + actorString(((RWRdata)this.radarsLock.get(j)).actor) + " , (Actor)tact=" + actorString(tact));
                if(((RWRdata)this.radarsLock.get(j)).actor == tact) {
                    tempRrwrdata = (RWRdata)this.radarsLock.get(j);
                    if(Actor.isValid(tact)) {
                        tempRrwrdata.lastDetectTime = Time.current();
                        tempRrwrdata.lastDetectDistance = tempDistance;
                        tempRrwrdata.lastDetectDirection = angle360Between(this.rwrOwner, actor);
                    }
                    tempRrwrdata.distance = tempDistance;
                    this.radarsLock.set(j, tempRrwrdata);
                    bInserted = true;
                    break;
                }
            }
            if(!bInserted) {
                tempNrwrdata = new RWRdata();
                tempNrwrdata.actor = tact;
                tempNrwrdata.distance = tempDistance;
                tempNrwrdata.firstDetectTime = Time.current();
                tempNrwrdata.lastDetectTime = Time.current();
                tempNrwrdata.lastDetectDistance = tempDistance;
                tempNrwrdata.lastDetectDirection = angle360Between(this.rwrOwner, actor);
                this.radarsLock.add(tempNrwrdata);
                if((this.iDebugLogLevel & 4) > 0)
                    System.out.println(sDebugPlaneName + "RWRUtils: new this.radarsLock.add(" + actorString(tact) + ") , size()=" + this.radarsLock.size());
                if(this.bSoundEnabled && this.rwrGeneration != 0 && fxRwrToneThreatNew != null) {
                    if(!fxRwrToneThreatNew.isPlaying())
                        fxRwrToneThreatNew.start();
                }
                if(this.bShowTextWarning && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode() || !(this.FM instanceof Pilot)) {
                    String sElev = "";
                    if(this.bAbleDetectElevation) {
                        if(elevDegree > 15.0D) {
                            if(zDiff > 0D) sElev = "Above ";
                            else sElev = "Below ";
                        }
                        else sElev = "Level ";
                    }
//                    String sAngl = Integer.toString((int)angle360Between(this.rwrOwner, tact));
//                    LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New Radar LOCK detected " + sAngl + "deg. " + sElev + "!!!");
                    if(detectSectorNum == 4) {
                        String sector = DEG2sector4(angle360Between(this.rwrOwner, tact));
                        LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New Radar LOCK detected " + sector + " " + sElev + "!!!");
                    } else if(detectSectorNum == 8) {
                        String sector = DEG2sector8(angle360Between(this.rwrOwner, tact));
                        LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New Radar LOCK detected " + sector + " " + sElev + "!!!");
                    } else if(detectSectorNum == 12) {
                        String sOclock = Integer.toString(DEG2OCLOCK(angle360Between(this.rwrOwner, tact)));
                        LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New Radar LOCK detected " + sOclock + " o'clock " + sElev + "!!!");
                    }
                }
            }
        }
        for(int j = 0; j < this.radarsLock.size(); j++) {
            tempRrwrdata = (RWRdata)this.radarsLock.get(j);
            if(tempRrwrdata.lastDetectTime != Time.current() && Actor.isValid(tempRrwrdata.actor)) {
                tempRrwrdata.distance = distanceBetween(this.rwrOwner, tempRrwrdata.actor);
                this.radarsLock.set(j, tempRrwrdata);
            }
        }
        Collections.sort(this.radarsLock);
        radarLockListExpire();
        if(this.radarsLock.size() > 0 && bShowTextWarning && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode() && !(this.FM instanceof Pilot) && this.FM.isTick(600, 10)) {
            LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "Total " + this.radarsLock.size() + " Radars Locking Me !");
        }
        if((this.iDebugLogLevel & 4) > 0) {
            for(int ii = 0; ii < this.radarsLock.size(); ii++)
                System.out.println(sDebugPlaneName + "RWRUtils: this.radarsLock(" + ii + "): " + actorString(((RWRdata)this.radarsLock.get(ii)).actor) + ", d=" + ((RWRdata)this.radarsLock.get(ii)).distance + ", lDT=" + ((RWRdata)this.radarsLock.get(ii)).lastDetectTime);
        }
        if(this.radarsLock.size() > 0) {
            this.bRadarLockedWarning = true;
            playRWRWarning();
        } else {
            this.bRadarLockedWarning = false;
            playRWRWarning();
        }
        return true;
    }
    private boolean RWRRadarSearchWarning() {
        RWRdata tempRrwrdata = null;
        for(int j = 0; j < this.radars.size(); j++) {
            tempRrwrdata = (RWRdata)this.radars.get(j);
            if(tempRrwrdata.lastDetectTime != Time.current() && Actor.isValid(tempRrwrdata.actor)) {
                tempRrwrdata.distance = distanceBetween(this.rwrOwner, tempRrwrdata.actor);
                this.radars.set(j, tempRrwrdata);
            }
        }
        Collections.sort(this.radars);
        radarListExpire();
        if(this.radars.size() > 0 && bShowTextWarning && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode() && !(this.FM instanceof Pilot) && this.FM.isTick(600, 310)) {
            LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "Total " + this.radars.size() + " Radars Searching Me !");
        }
        if((this.iDebugLogLevel & 8) > 0) {
            for(int ii = 0; ii < this.radars.size(); ii++)
                System.out.println(sDebugPlaneName + "RWRUtils: this.radars(" + ii + "): " + actorString(((RWRdata)this.radars.get(ii)).actor) + ", d=" + ((RWRdata)this.radars.get(ii)).distance + ", lDT=" + ((RWRdata)this.radars.get(ii)).lastDetectTime);
        }
        if(this.radars.size() > 0) {
            this.bRadarSearchedWarning = true;
            playRWRWarning();
        } else {
            this.bRadarSearchedWarning = false;
            playRWRWarning();
        }
        return true;
    }
    public void recordRadarLocked(Actor actor, String soundpreset) {
        if((this.iDebugLogLevel & 4) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: entering recordRadarLocked(" + actorString(actor) + ")");
        if(!((actor instanceof TypeGuidedMissileCarrier)
             || ((actor instanceof ArtilleryGeneric) && ((ArtilleryGeneric)actor).getHasRadar()) || ((actor instanceof TankGeneric) && ((TankGeneric)actor).getHasRadar()) || (actor instanceof StationaryGeneric))
           || actor.getArmy() == this.rwrOwner.getArmy())
            return;
        if(actor instanceof TypeGuidedMissileCarrier) {
            GuidedMissileUtils gmu = ((TypeGuidedMissileCarrier)actor).getGuidedMissileUtils();
            if(!(gmu.getDetectorType() == 2 || gmu.getDetectorType() == 3 ||gmu.getDetectorType() == 4))
                return;
        }
        Point3d point3d = new Point3d();
        this.rwrOwner.pos.getAbs(point3d);
        RWRdata tempNrwrdata = null;
        RWRdata tempRrwrdata = null;
        double tempDistance = distanceBetween(this.rwrOwner, actor);
        double zDiff = 0.0D;
        double elevDegree = 0.0D;
        if(tempDistance > 0D) {
            zDiff = actor.pos.getAbsPoint().z - this.rwrOwner.pos.getAbsPoint().z;
            elevDegree = Math.toDegrees(Math.atan(Math.abs(zDiff) / tempDistance));
            if((this.iDebugLogLevel & 4) > 0)
                System.out.println(sDebugPlaneName + "RWRUtils: " + actorString(actor) + " 's elevation=" + Math.floor(elevDegree * 1000D) * 0.001D + " deg.");
            if(elevDegree > this.receiveElevationDegrees) return;
        }
        boolean bInserted = false;
        for(int j = 0; j < this.radarsLock.size(); j++) {
            if((this.iDebugLogLevel & 4) > 0)
                System.out.println(sDebugPlaneName + "RWRUtils: ((RWRdata)this.radarsLock.get(" + j + ")).actor=" + actorString(((RWRdata)this.radarsLock.get(j)).actor) + " , (Actor)actor=" + actorString(actor));
            if(((RWRdata)this.radarsLock.get(j)).actor == actor) {
                tempRrwrdata = (RWRdata)this.radarsLock.get(j);
                if(Actor.isValid(actor)) {
                    tempRrwrdata.lastDetectTime = Time.current();
                    tempRrwrdata.lastDetectDistance = tempDistance;
                    tempRrwrdata.lastDetectDirection = angle360Between(this.rwrOwner, actor);
                }
                tempRrwrdata.distance = tempDistance;
                this.radarsLock.set(j, tempRrwrdata);
                bInserted = true;
                break;
            }
        }
        if(!bInserted) {
            tempNrwrdata = new RWRdata();
            tempNrwrdata.actor = actor;
            tempNrwrdata.distance = tempDistance;
            tempNrwrdata.firstDetectTime = Time.current();
            tempNrwrdata.lastDetectTime = Time.current();
            tempNrwrdata.lastDetectDistance = tempDistance;
            tempNrwrdata.lastDetectDirection = angle360Between(this.rwrOwner, actor);
            if(this.rwrGeneration == 0 && soundpreset != null)
                tempNrwrdata.fxRwrToneGen0 = this.rwrOwner.newSound(soundpreset, false);
            else
                tempNrwrdata.fxRwrToneGen0 = null;
            this.radarsLock.add(tempNrwrdata);
            if((this.iDebugLogLevel & 4) > 0)
                System.out.println(sDebugPlaneName + "RWRUtils: new this.radarsLock.add(" + actorString(actor) + ") , size()=" + this.radarsLock.size());
            if(this.bSoundEnabled && this.rwrGeneration != 0 && fxRwrToneThreatNew != null) {
                if(!fxRwrToneThreatNew.isPlaying())
                    fxRwrToneThreatNew.start();
            }
            if(this.bShowTextWarning && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode() || !(this.FM instanceof Pilot)) {
                String sElev = "";
                if(this.bAbleDetectElevation) {
                    if(elevDegree > 15.0D) {
                        if(zDiff > 0D) sElev = "Above ";
                        else sElev = "Below ";
                    }
                    else sElev = "Level ";
                }
//                String sAngl = Integer.toString((int)angle360Between(this.rwrOwner, actor));
//                LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New Radar LOCK detected " + sAngl + "deg. " + sElev + "!!!");
                if(detectSectorNum == 4) {
                    String sector = DEG2sector4(angle360Between(this.rwrOwner, actor));
                    LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New Radar LOCK detected " + sector + " " + sElev + "!!!");
                } else if(detectSectorNum == 8) {
                    String sector = DEG2sector8(angle360Between(this.rwrOwner, actor));
                    LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New Radar LOCK detected " + sector + " " + sElev + "!!!");
                } else if(detectSectorNum == 12) {
                    String sOclock = Integer.toString(DEG2OCLOCK(angle360Between(this.rwrOwner, actor)));
                    LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New Radar LOCK detected " + sOclock + " o'clock " + sElev + "!!!");
                }
            }
        }
        Collections.sort(this.radarsLock);
    }
    public void recordRadarSearched(Actor actor, String soundpreset) {
        if((this.iDebugLogLevel & 8) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: entering recordRadarSearched(" + actorString(actor) + ")");
        if(actor.getArmy() == this.rwrOwner.getArmy())
            return;
        if(!((actor instanceof TypeGuidedMissileCarrier) || (actor instanceof TypeRadar) || (actor instanceof TypeSemiRadar)
            || ((actor instanceof ArtilleryGeneric) && ((ArtilleryGeneric)actor).getHasRadar()) || ((actor instanceof TankGeneric) && ((TankGeneric)actor).getHasRadar())))
            return;
        Point3d point3d = new Point3d();
        this.rwrOwner.pos.getAbs(point3d);
        RWRdata tempNrwrdata = null;
        RWRdata tempRrwrdata = null;
        double tempDistance = distanceBetween(this.rwrOwner, actor);
        double zDiff = 0.0D;
        double elevDegree = 0.0D;
        if(tempDistance > 0D) {
            zDiff = actor.pos.getAbsPoint().z - this.rwrOwner.pos.getAbsPoint().z;
            elevDegree = Math.toDegrees(Math.atan(Math.abs(zDiff) / tempDistance));
            if((this.iDebugLogLevel & 8) > 0)
                System.out.println(sDebugPlaneName + "RWRUtils: " + actorString(actor) + " 's elevation=" + Math.floor(elevDegree * 1000D) * 0.001D + " deg.");
            if(elevDegree > this.receiveElevationDegrees) return;
        }
        boolean bInserted = false;
        for(int j = 0; j < this.radars.size(); j++) {
            if((this.iDebugLogLevel & 8) > 0)
                System.out.println(sDebugPlaneName + "RWRUtils: ((RWRdata)this.radars.get(" + j + ")).actor=" + actorString(((RWRdata)this.radars.get(j)).actor) + " , (Actor)actor=" + actorString(actor));
            if(((RWRdata)this.radars.get(j)).actor == actor) {
                tempRrwrdata = (RWRdata)this.radars.get(j);
                if(Actor.isValid(actor)) {
                    tempRrwrdata.lastDetectTime = Time.current();
                    tempRrwrdata.lastDetectDistance = tempDistance;
                    tempRrwrdata.lastDetectDirection = angle360Between(this.rwrOwner, actor);
                }
                tempRrwrdata.distance = tempDistance;
                this.radars.set(j, tempRrwrdata);
                bInserted = true;
                break;
            }
        }
        if(!bInserted) {
            tempNrwrdata = new RWRdata();
            tempNrwrdata.actor = actor;
            tempNrwrdata.distance = tempDistance;
            tempNrwrdata.firstDetectTime = Time.current();
            tempNrwrdata.lastDetectTime = Time.current();
            tempNrwrdata.lastDetectDistance = tempDistance;
            tempNrwrdata.lastDetectDirection = angle360Between(this.rwrOwner, actor);
            if(this.rwrGeneration == 0 && soundpreset != null)
                tempNrwrdata.fxRwrToneGen0 = this.rwrOwner.newSound(soundpreset, false);
            else
                tempNrwrdata.fxRwrToneGen0 = null;
            this.radars.add(tempNrwrdata);
            if((this.iDebugLogLevel & 8) > 0)
                System.out.println(sDebugPlaneName + "RWRUtils: new this.radars.add(" + actorString(actor) + ") , size()=" + this.radars.size());
            if(this.bSoundEnabled && this.rwrGeneration != 0 && fxRwrToneThreatNew != null) {
                if(!fxRwrToneThreatNew.isPlaying())
                    fxRwrToneThreatNew.start();
            }
            if(this.bShowTextWarning && (this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode() || !(this.FM instanceof Pilot)) {
                String sElev = "";
                if(this.bAbleDetectElevation) {
                    if(elevDegree > 15.0D) {
                        if(zDiff > 0D) sElev = "Above ";
                        else sElev = "Below ";
                    }
                    else sElev = "Level ";
                }
//                String sAngl = Integer.toString((int)angle360Between(this.rwrOwner, actor));
//                LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New Radar search detected " + sAngl + "deg. " + sElev + "!!!");
                if(detectSectorNum == 4) {
                    String sector = DEG2sector4(angle360Between(this.rwrOwner, actor));
                    LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New Radar search detected " + sector + " " + sElev + "!!!");
                } else if(detectSectorNum == 8) {
                    String sector = DEG2sector8(angle360Between(this.rwrOwner, actor));
                    LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New Radar search detected " + sector + " " + sElev + "!!!");
                } else if(detectSectorNum == 12) {
                    String sOclock = Integer.toString(DEG2OCLOCK(angle360Between(this.rwrOwner, actor)));
                    LocalLog(this.rwrOwner, AircraftHotKeys.hudLogWeaponId, "New Radar search detected " + sOclock + " o'clock " + sElev + "!!!");
                }
            }
        }
        Collections.sort(this.radars);
    }
    public void playRWRWarning() {
        if(!this.bSoundEnabled) return;
        if(this.rwrGeneration == 0) {
            for(int j = 0; j < this.radars.size(); j++) {
                if(((RWRdata)this.radars.get(j)).fxRwrToneGen0 != null && !((RWRdata)this.radars.get(j)).fxRwrToneGen0.isPlaying())
                    ((RWRdata)this.radars.get(j)).fxRwrToneGen0.start();
            }
            for(int j = 0; j < this.radarsLock.size(); j++) {
                if(((RWRdata)this.radarsLock.get(j)).fxRwrToneGen0 != null && !((RWRdata)this.radarsLock.get(j)).fxRwrToneGen0.isPlaying())
                    ((RWRdata)this.radarsLock.get(j)).fxRwrToneGen0.start();
            }
            return;
        }
        if(fxRwrToneSearch != null) {
            if(bRadarSearchedWarning && !fxRwrToneSearch.isPlaying())
                fxRwrToneSearch.start();
            else if(!bRadarSearchedWarning && fxRwrToneSearch.isPlaying())
                fxRwrToneSearch.stop();
        }
        if(fxRwrToneLock != null) {
            if(bRadarLockedWarning && !fxRwrToneLock.isPlaying()) {
                if((this.iDebugLogLevel & 128) > 0)
                    System.out.println(sDebugPlaneName + "RWRUtils: playRWRWarning() - bRadarLockedWarning=" + bRadarLockedWarning + " , fxRwrToneLock.isPlaying()=" + fxRwrToneLock.isPlaying());
                fxRwrToneLock.start();
                if((this.iDebugLogLevel & 128) > 0)
                    System.out.println(sDebugPlaneName + "RWRUtils: fxRwrToneLock.start(); in playRWRWarning()");
            } else if(!bRadarLockedWarning && fxRwrToneLock.isPlaying()) {
                if((this.iDebugLogLevel & 128) > 0)
                    System.out.println(sDebugPlaneName + "RWRUtils: playRWRWarning() - bRadarLockedWarning=" + bRadarLockedWarning + " , fxRwrToneLock.isPlaying()=" + fxRwrToneLock.isPlaying());
                fxRwrToneLock.stop();
                if((this.iDebugLogLevel & 128) > 0)
                    System.out.println(sDebugPlaneName + "RWRUtils: fxRwrToneLock.stop(); in playRWRWarning()");
            }
        }
        if(fxRwrToneMissile != null) {
            if(bMissileWarning && !fxRwrToneMissile.isPlaying()) {
                if((this.iDebugLogLevel & 128) > 0)
                    System.out.println(sDebugPlaneName + "RWRUtils: playRWRWarning() - bMissileWarning=" + bMissileWarning + " , fxRwrToneMissile.isPlaying()=" + fxRwrToneMissile.isPlaying());
                fxRwrToneMissile.start();
                if((this.iDebugLogLevel & 128) > 0)
                    System.out.println(sDebugPlaneName + "RWRUtils: fxRwrToneMissile.start(); in playRWRWarning()");
            } else if(!bMissileWarning && fxRwrToneMissile.isPlaying()) {
                if((this.iDebugLogLevel & 128) > 0)
                    System.out.println(sDebugPlaneName + "RWRUtils: playRWRWarning() - bMissileWarning=" + bMissileWarning + " , fxRwrToneMissile.isPlaying()=" + fxRwrToneMissile.isPlaying());
                fxRwrToneMissile.stop();
                if((this.iDebugLogLevel & 128) > 0)
                    System.out.println(sDebugPlaneName + "RWRUtils: fxRwrToneMissile.stop(); in playRWRWarning()");
            }
        }
    }
    public void stopAllRWRSounds() {
        this.bSoundEnabled = false;
        if(fxRwrToneSearch != null && fxRwrToneSearch.isPlaying())
            fxRwrToneSearch.stop();
        if(fxRwrToneLock != null && fxRwrToneLock.isPlaying())
            fxRwrToneLock.stop();
        if(fxRwrToneMissile != null && fxRwrToneMissile.isPlaying())
            fxRwrToneMissile.stop();
        if(fxRwrToneThreatNew != null && fxRwrToneThreatNew.isPlaying())
            fxRwrToneThreatNew.stop();
        if(this.rwrGeneration == 0) {
            RWRdata tempRrwrdata = null;
            for(int i = 0; i < this.RHmissiles.size(); i++) {
                tempRrwrdata = (RWRdata)this.RHmissiles.get(i);
                if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                    tempRrwrdata.fxRwrToneGen0.stop();
            }
            for(int i = 0; i < this.radars.size(); i++) {
                tempRrwrdata = (RWRdata)this.radars.get(i);
                if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                    tempRrwrdata.fxRwrToneGen0.stop();
            }
            for(int i = 0; i < this.radarsLock.size(); i++) {
                tempRrwrdata = (RWRdata)this.radarsLock.get(i);
                if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                    tempRrwrdata.fxRwrToneGen0.stop();
            }
        }
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: stopAllRWRSounds(); --- done.");
    }
    public void setSoundEnable(boolean flag) {
        bSoundEnabled = flag;
        if((this.iDebugLogLevel & 128) > 0)
            System.out.println(sDebugPlaneName + "RWRUtils: setSoundEnable( " + flag + " ) --- bSoundEnabled=" + flag);
    }
    private void missileListExpire() {
        RWRdata tempRrwrdata = null;
        if(this.ignoreMissileList.size() > 0) {
            for(int i = 0; i < this.ignoreMissileList.size(); i++) {
                if(!(Actor.isValid((Actor)this.ignoreMissileList.get(i)))) {
                    this.ignoreMissileList.remove(i);
                    continue;
                }
            }
        }
        if(this.IRmissiles.size() > 0) {
            for(int i = 0; i < this.IRmissiles.size(); i++) {
                if(!(Actor.isValid(((RWRdata)this.IRmissiles.get(i)).actor))) {
                    this.IRmissiles.remove(i);
                    continue;
                }
                if(Time.current() > ((RWRdata)this.IRmissiles.get(i)).lastDetectTime + this.keepMiliseconds) {
                    this.ignoreMissileList.add(((RWRdata)this.IRmissiles.get(i)).actor);
                    this.IRmissiles.remove(i);
                    continue;
                }
            }
        }
        if(this.RHmissiles.size() > 0) {
            for(int i = 0; i < this.RHmissiles.size(); i++) {
                if(!(Actor.isValid(((RWRdata)this.RHmissiles.get(i)).actor))) {
                    if(this.rwrGeneration == 0) {
                        tempRrwrdata = (RWRdata)this.RHmissiles.get(i);
                        if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                            tempRrwrdata.fxRwrToneGen0.stop();
                    }
                    this.RHmissiles.remove(i);
                    continue;
                }
                if(Time.current() > ((RWRdata)this.RHmissiles.get(i)).lastDetectTime + this.keepMiliseconds) {
                    if(this.rwrGeneration == 0) {
                        tempRrwrdata = (RWRdata)this.RHmissiles.get(i);
                        if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                            tempRrwrdata.fxRwrToneGen0.stop();
                    }
                    this.ignoreMissileList.add(((RWRdata)this.RHmissiles.get(i)).actor);
                    this.RHmissiles.remove(i);
                    continue;
                }
            }
        }
        if(this.RHmissiles.size() > maxDetectRadarNum) {
            int nDone = 0;
            for(int i = this.RHmissiles.size() - 1; i >= 0 && this.RHmissiles.size() - nDone > maxDetectRadarNum; i--) {
                if(Time.current() > ((RWRdata)this.RHmissiles.get(i)).lastDetectTime) {
                    if(this.rwrGeneration == 0) {
                        tempRrwrdata = (RWRdata)this.RHmissiles.get(i);
                        if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                            tempRrwrdata.fxRwrToneGen0.stop();
                    }
                    this.RHmissiles.remove(i);
                    nDone++;
                }
            }
        }
        if(this.RHmissiles.size() > maxDetectRadarNum) {
            for(int i = this.RHmissiles.size() - 1; i >= maxDetectRadarNum; i--) {
                if(this.rwrGeneration == 0) {
                    tempRrwrdata = (RWRdata)this.RHmissiles.get(i);
                    if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                        tempRrwrdata.fxRwrToneGen0.stop();
                }
                this.RHmissiles.remove(i);
            }
        }
    }
    private void radarListExpire() {
        RWRdata tempRrwrdata = null;
        if(this.radars.size() > 0) {
            for(int i = 0; i < this.radars.size(); i++) {
                if(!(Actor.isValid(((RWRdata)this.radars.get(i)).actor))) {
                    if(this.rwrGeneration == 0) {
                        tempRrwrdata = (RWRdata)this.radars.get(i);
                        if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                            tempRrwrdata.fxRwrToneGen0.stop();
                    }
                    this.radars.remove(i);
                    continue;
                }
                if(Time.current() > ((RWRdata)this.radars.get(i)).lastDetectTime + keepMiliseconds) {
                    if(this.rwrGeneration == 0) {
                        tempRrwrdata = (RWRdata)this.radars.get(i);
                        if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                            tempRrwrdata.fxRwrToneGen0.stop();
                    }
                    this.radars.remove(i);
                    continue;
                }
            }
        }
        if(this.radars.size() > maxDetectRadarNum) {
            int nDone = 0;
            for(int i = this.radars.size() - 1; i >= 0 && this.radars.size() - nDone > maxDetectRadarNum; i--) {
                if(Time.current() > ((RWRdata)this.radars.get(i)).lastDetectTime) {
                    if(this.rwrGeneration == 0) {
                        tempRrwrdata = (RWRdata)this.radars.get(i);
                        if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                            tempRrwrdata.fxRwrToneGen0.stop();
                    }
                    this.radars.remove(i);
                    nDone++;
                }
            }
        }
        if(this.radars.size() > maxDetectRadarNum) {
            for(int i = this.radars.size() - 1; i >= maxDetectRadarNum; i--) {
                if(this.rwrGeneration == 0) {
                    tempRrwrdata = (RWRdata)this.radars.get(i);
                    if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                        tempRrwrdata.fxRwrToneGen0.stop();
                }
                this.radars.remove(i);
            }
        }
    }
    private void radarLockListExpire() {
        RWRdata tempRrwrdata = null;
        if(this.radarsLock.size() > 0) {
            for(int i = 0; i < this.radarsLock.size(); i++) {
                if(!(Actor.isValid(((RWRdata)this.radarsLock.get(i)).actor))) {
                    if(this.rwrGeneration == 0) {
                        tempRrwrdata = (RWRdata)this.radarsLock.get(i);
                        if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                            tempRrwrdata.fxRwrToneGen0.stop();
                    }
                    this.radarsLock.remove(i);
                    continue;
                }
                if(Time.current() > ((RWRdata)this.radarsLock.get(i)).lastDetectTime + keepMiliseconds) {
                    if(this.rwrGeneration == 0) {
                        tempRrwrdata = (RWRdata)this.radarsLock.get(i);
                        if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                            tempRrwrdata.fxRwrToneGen0.stop();
                    }
                    this.radarsLock.remove(i);
                    continue;
                }
            }
        }
        if(this.radarsLock.size() > maxDetectRadarNum) {
            int nDone = 0;
            for(int i = this.radarsLock.size() - 1; i >= 0 && this.radarsLock.size() - nDone > maxDetectRadarNum; i--) {
                if(Time.current() > ((RWRdata)this.radarsLock.get(i)).lastDetectTime) {
                    if(this.rwrGeneration == 0) {
                        tempRrwrdata = (RWRdata)this.radarsLock.get(i);
                        if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                            tempRrwrdata.fxRwrToneGen0.stop();
                    }
                    this.radarsLock.remove(i);
                    nDone++;
                }
            }
        }
        if(this.radarsLock.size() > maxDetectRadarNum) {
            for(int i = this.radarsLock.size() - 1; i >= maxDetectRadarNum; i--) {
                if(this.rwrGeneration == 0) {
                    tempRrwrdata = (RWRdata)this.radarsLock.get(i);
                    if(tempRrwrdata.fxRwrToneGen0 != null && tempRrwrdata.fxRwrToneGen0.isPlaying())
                        tempRrwrdata.fxRwrToneGen0.stop();
                }
                this.radarsLock.remove(i);
            }
        }
    }
    private String DEG2sector4(double degree) {
        while(degree < 0.0D)
            degree += 360.0D;
        String sr = "";
        if(degree > 355.0D || degree < 5.0D) sr = "Fw-L/Fw-R";
        else if(degree < 85.0D) sr = "Fw-R";
        else if(degree < 95.0D) sr = "Fw-R/Bw-R";
        else if(degree < 175.0D) sr = "Bw-R";
        else if(degree < 185.0D) sr = "Bw-L/Bw-R";
        else if(degree < 265.0D) sr = "Bw-L";
        else if(degree < 275.0D) sr = "Fw-L/Bw-L";
        else sr = "Fw-L";
        return sr;
    }
    private String DEG2sector8(double degree) {
        while(degree < 0.0D)
            degree += 360.0D;
        String sr = "";
        if(degree > 337.5D || degree < 22.5D) sr = "Fw";
        else if(degree < 67.5D) sr = "Fw-R";
        else if(degree < 112.5D) sr = "Right";
        else if(degree < 157.5D) sr = "Bw-R";
        else if(degree < 202.5D) sr = "Bw";
        else if(degree < 247.5D) sr = "Bw-L";
        else if(degree < 292.5D) sr = "Left";
        else sr = "Fw-L";
        return sr;
    }
    private int DEG2OCLOCK(double degree) {
        while(degree < 0.0D)
            degree += 360.0D;
        int oclock = (int)(Math.ceil((degree + 15.0D) / 30.0D) - 1.0D);
        if(oclock < 1)
            oclock = 12;
        return oclock;
    }
    private float CrewHealthSummary(int iStart) {
        float health = 0.0F;
        for(int i = iStart; i < Math.min(this.FM.crew, 4); i++) {
            float f = (float)this.FM.AS.astatePilotStates[i] * 0.01F;
            if(f > 1.0F) f = 1.0F;
            health += (1.0F - f);
        }
        return health;
    }
    private float CrewHealthSummaryContainPilot() {
        return CrewHealthSummary(0);
    }
    private float CrewHealthSummaryWithoutPilot() {
        return CrewHealthSummary(1);
    }
    private boolean lockTonesInitialized = false;
    private SoundFX fxRwrToneLock = null;
    private SoundFX fxRwrToneSearch = null;
    private SoundFX fxRwrToneMissile = null;
    private SoundFX fxRwrToneThreatNew = null;
//delete    private Sample smplRwrLock = null;
//delete    private Sample smplRwrSearch = null;
//delete    private Sample smplRwrMissile = null;
//delete    private Sample smplRwrThreatNew = null;
    private String missileName = null;
    private Actor rwrOwner = null;
    // rwrGeneration: 0= 1950-80 era, sounds only puls-wave modulation; 1= 1990-2020 era, detect and identify enemy types and classes
    private int rwrGeneration = 0;
    private int maxDetectRadarNum = 0;
    private long keepMiliseconds = 0L;
    private double receiveElevationDegrees = 0.0D;
    private boolean bAbleDetectRHmissiles = false;
    private boolean bAbleDetectIRmissiles = false;
    private boolean bAbleDetectElevation = true;
    private int detectSectorNum = 12;
    private boolean bShowTextWarning = true;
    private ArrayList IRmissiles;
    private ArrayList RHmissiles;
    private ArrayList radars;
    private ArrayList radarsLock;
    private ArrayList ignoreMissileList;
    private boolean RWRSearchedSoundPlaying = false;
    private boolean RWRLockedSoundPlaying = false;
    private boolean MissileSoundPlaying = false;
    private boolean bRadarSearchedWarning = false;
    private boolean bRadarLockedWarning = false;
    private boolean bMissileWarning = false;
    private FlightModelMain FM = null;
    private long lastRWRUpdateTime = -1L;
    private boolean backfire = false;
    private boolean bAIEmergency = false;
    private boolean bSoundEnabled = false;
    private String sDebugPlaneName = "";
    private int iDebugLogLevel = 0;
        // 1:RH missile, 2:IR missile, 4:radarLock, 8:radarSearch, 128:sound
}