// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 02/11/2015 03:24:20 p.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AIM9BMissile.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Random;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket, AIM9BMissilePhysics, RocketAIM9BUtils

public class AIM9BMissile extends Rocket
{
    class Master extends ActorNet
        implements NetUpdate
    {

        public void msgNetNewChannel(NetChannel netchannel)
        {
            if(!Actor.isValid(actor()))
                return;
            if(netchannel.isMirrored(this))
                return;
            try
            {
                if(netchannel.userState == 0)
                {
                    NetMsgSpawn netmsgspawn = actor().netReplicate(netchannel);
                    if(netmsgspawn != null)
                    {
                        postTo(netchannel, netmsgspawn);
                        actor().netFirstUpdate(netchannel);
                    }
                }
            }
            catch(Exception exception)
            {
                NetObj.printDebug(exception);
            }
        }

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            return false;
        }

        public void netUpdate()
        {
            try
            {
                out.unLockAndClear();
                ((Actor) (theMissile)).pos.getAbs(theMissilePoint3d, theMissileOrient);
                out.writeFloat((float)((Tuple3d) (theMissilePoint3d)).x);
                out.writeFloat((float)((Tuple3d) (theMissilePoint3d)).y);
                out.writeFloat((float)((Tuple3d) (theMissilePoint3d)).z);
                theMissileOrient.wrap();
                int i = (int)((theMissileOrient.getYaw() * 32000F) / 180F);
                int j = (int)((theMissileOrient.tangage() * 32000F) / 90F);
                out.writeShort(i);
                out.writeShort(j);
                post(Time.current(), out);
            }
            catch(Exception exception)
            {
                NetObj.printDebug(exception);
            }
        }

        NetMsgFiltered out;
        AIM9BMissile theMissile;
        Point3d theMissilePoint3d;
        Orient theMissileOrient;

        public Master(Actor actor)
        {
            super(actor);
            theMissilePoint3d = new Point3d();
            theMissileOrient = new Orient();
            out = new NetMsgFiltered();
            if(actor instanceof AIM9BMissile)
                theMissile = (AIM9BMissile)actor;
        }
    }

    class Mirror extends ActorNet
    {

        public void msgNetNewChannel(NetChannel netchannel)
        {
            if(!Actor.isValid(actor()))
                return;
            if(netchannel.isMirrored(this))
                return;
            try
            {
                if(netchannel.userState == 0)
                {
                    NetMsgSpawn netmsgspawn = actor().netReplicate(netchannel);
                    if(netmsgspawn != null)
                    {
                        postTo(netchannel, netmsgspawn);
                        actor().netFirstUpdate(netchannel);
                    }
                }
            }
            catch(Exception exception)
            {
                NetObj.printDebug(exception);
            }
        }

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            if(netmsginput.isGuaranted())
                return false;
            if(isMirrored())
            {
                out.unLockAndSet(netmsginput, 0);
                postReal(Message.currentTime(true), out);
            }
            theMissile = (AIM9BMissile)actor();
            theMissilePoint3d.set(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
            int i = netmsginput.readShort();
            int j = netmsginput.readShort();
            float f = -(((float)i * 180F) / 32000F);
            float f1 = ((float)j * 90F) / 32000F;
            theMissileOrient.set(f, f1, 0.0F);
            ((Actor) (theMissile)).pos.setAbs(theMissilePoint3d, theMissileOrient);
            return true;
        }

        NetMsgFiltered out;
        AIM9BMissile theMissile;
        Point3d theMissilePoint3d;
        Orient theMissileOrient;

        public Mirror(Actor actor, NetChannel netchannel, int i)
        {
            super(actor, netchannel, i);
            theMissilePoint3d = new Point3d();
            theMissileOrient = new Orient();
            out = new NetMsgFiltered();
        }
    }

    private class MissileNavLight
    {

        public Eff3DActor theNavLight;
        public MissileNavLight nextNavLight;

        public MissileNavLight(Eff3DActor theEff3DActor)
        {
            theNavLight = theEff3DActor;
            nextNavLight = null;
        }
    }

    static class SPAWN
        implements NetSpawn
    {

        public void netSpawn(int i, NetMsgInput netmsginput)
        {
            NetObj netobj = netmsginput.readNetObj();
            if(netobj == null)
                return;
            try
            {
                Actor actor = (Actor)netobj.superObj();
                Point3d point3d = new Point3d(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
                Orient orient = new Orient(netmsginput.readFloat(), netmsginput.readFloat(), 0.0F);
                float f = netmsginput.readFloat();
                doSpawn(actor, netmsginput.channel(), i, point3d, orient, f);
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
            new AIM9BMissile(actor, netchannel, i, point3d, orient, f);
        }

        SPAWN()
        {
        }
    }


    private void DebugLog(String s1)
    {
    }

    public double getLaunchTimeFactor()
    {
        return ((double)(Time.current() - tStart) / 1000D) * 6D;
    }

    public double getLaunchYaw()
    {
        double launchYawTimeFactor = getLaunchTimeFactor();
        double theLaunchYaw = 2D * ((Math.cos(launchYawTimeFactor + 0.40000000000000002D) - 1.02D) + launchYawTimeFactor / 5D);
        theLaunchYaw *= Math.sin(launchKren);
        theLaunchYaw += launchYaw;
        return theLaunchYaw;
    }

    public double getLaunchPitch()
    {
        double launchPitchTimeFactor = getLaunchTimeFactor();
        double theLaunchPitch = 2D * ((Math.cos(launchPitchTimeFactor + 0.40000000000000002D) - 1.02D) + launchPitchTimeFactor / 5D);
        theLaunchPitch *= Math.cos(launchKren);
        theLaunchPitch += launchPitch;
        return theLaunchPitch;
    }

    private float KD(float paramFloat)
    {
        return 1.0F + paramFloat * (-9.59387E-005F + paramFloat * (3.53118E-009F + paramFloat * -5.83556E-014F));
    }

    private float KF(float paramFloat)
    {
        return (608.5F + (-1.81327F + 0.0016511F * paramFloat) * paramFloat) * paramFloat;
    }

    private void endAllSmoke()
    {
        if(iExhausts < 2)
        {
            Eff3DActor.finish(super.smoke);
        } else
        {
            for(int i = 0; i < iExhausts; i++)
                if(smokes[i] != null)
                    Eff3DActor.finish(smokes[i]);

        }
        endedSmoke = true;
    }

    private void endAllFlame()
    {
        if(iExhausts < 2)
        {
            ObjState.destroy(super.flame);
        } else
        {
            for(int i = 0; i < iExhausts; i++)
                if(flames[i] != null)
                    ObjState.destroy(flames[i]);

        }
        if(super.light != null)
            super.light.light.setEmit(0.0F, 1.0F);
        stopSounds();
        endedFlame = true;
    }

    private void endAllSprites()
    {
        if(iExhausts < 2)
        {
            Eff3DActor.finish(super.sprite);
        } else
        {
            for(int i = 0; i < iExhausts; i++)
                if(sprites[i] != null)
                    Eff3DActor.finish(sprites[i]);

        }
        endedSprite = true;
    }

    protected void endSmoke()
    {
        if(!smokeActive && !endedSmoke)
            endAllSmoke();
        if(!spriteActive && !endedSprite)
            endAllSprites();
        if(!flameActive && !endedFlame)
            endAllFlame();
    }

    public boolean stepTargetHoming()
    {
        float fTick = Time.tickLenFs();
        int theFailState = getFailState();
        float fSpeed = (float)getSpeed(null);
        if(theFailState == 3)
            fTimeFire = 0.0F;
        super.pos.getAbs(p, or);
        or.wrap();
        float theForce = fForce;
        float millisecondsFromStart = Time.current() - tStart;
        if(millisecondsFromStart > fTimeFire)
        {
            flameActive = false;
            smokeActive = false;
            endSmoke();
            fMassa = fMassaEnd;
            fForce = 0.0F;
        } else
        {
            if(fT1 > 0.001F && millisecondsFromStart < fT1)
                theForce *= (fP1 + ((100F - fP1) * millisecondsFromStart) / fT1) / 100F;
            float millisecondsToEnd = fTimeFire - millisecondsFromStart;
            if(fT2 > 0.001F && millisecondsToEnd < fT2)
                theForce *= (fP2 + ((100F - fP2) * (fT2 - millisecondsToEnd)) / fT2) / 100F;
            fMassa -= fDiffM;
        }
        float fForceAzimuth = AIM9BMissilePhysics.getGForce(fSpeed, oldDeltaAzimuth / fTick);
        float fForceTangage = AIM9BMissilePhysics.getGForce(fSpeed, oldDeltaTangage / fTick);
        float fTurnForce = (float)Math.sqrt(fForceAzimuth * fForceAzimuth + fForceTangage * fForceTangage) * 9.80665F * fMassa;
        fTurnForce *= fCw;
        float fResForce = (float)Math.sqrt(Math.abs(theForce * theForce - fTurnForce * fTurnForce));
        if(fTurnForce > theForce)
            fResForce *= -1F;
        float fAccelForce = fResForce - AIM9BMissilePhysics.getDragInGravity(fSquare, fCw, (float)((Tuple3d) (p)).z, fSpeed, or.getTangage(), fMassa);
        float fAccel = fAccelForce / fMassa;
        fSpeed += fAccel * fTick;
        if(fSpeed < 3F)
            theFailState = 7;
        v.set(1.0D, 0.0D, 0.0D);
        or.transform(v);
        v.scale(fSpeed);
        setSpeed(v);
        p.x += ((Tuple3d) (v)).x * (double)fTick;
        p.y += ((Tuple3d) (v)).y * (double)fTick;
        p.z += ((Tuple3d) (v)).z * (double)fTick;
        if(isNet() && isNetMirror())
        {
            super.pos.setAbs(p, or);
            return false;
        }
        if(theFailState == 7)
        {
            doExplosionAir();
            postDestroy();
            collide(false);
            drawing(false);
            return false;
        }
        if(victim != null)
        {
            victim.pos.getAbs(pT, orVictimOffset);
            victim.getSpeed(victimSpeed);
            double victimDistance = RocketAIM9BUtils.distanceBetween(this, victim);
            double theVictimSpeed = victimSpeed.length();
            double speedRel = (double)fSpeed / theVictimSpeed;
            double gamma = RocketAIM9BUtils.angleActorBetween(victim, this);
            double alpha = Geom.RAD2DEG((float)Math.asin(Math.sin(Geom.DEG2RAD((float)gamma)) / speedRel));
            double beta = 180D - gamma - alpha;
            double victimAdvance = (victimDistance * Math.sin(Geom.DEG2RAD((float)alpha))) / Math.sin(Geom.DEG2RAD((float)beta));
            victimAdvance -= 5D;
            double timeToTarget = victimAdvance / theVictimSpeed;
            victimSpeed.scale(timeToTarget * (double)(fLeadPercent / 100F));
            pT.add(victimSpeed);
            Orient orientTarget = new Orient();
            orientTarget.set(victim.pos.getAbsOrient());
            v.set(pVictimOffset);
            orientTarget.transform(v);
            pT.add(v);
            pT.sub(p);
            or.transformInv(pT);
            double angleAzimuth = Math.toDegrees(Math.atan(((Tuple3d) (pT)).y / ((Tuple3d) (pT)).x));
            double angleTangage = Math.toDegrees(Math.atan(((Tuple3d) (pT)).z / ((Tuple3d) (pT)).x));
            if(theFailState == 4)
            {
                angleAzimuth += 180D;
                angleTangage += 180D;
                if(angleAzimuth > 180D)
                    angleAzimuth = 180D - angleAzimuth;
                if(angleTangage > 180D)
                    angleTangage = 180D - angleTangage;
            }
            if(Time.current() > tStart + lTrackDelay)
            {
                float turnStepMax = AIM9BMissilePhysics.getDegPerSec(fSpeed, fMaxG) * fTick * AIM9BMissilePhysics.getAirDensityFactor((float)((Tuple3d) (p)).z);
                float turnDiffMax = turnStepMax / fStepsForFullTurn;
                if(theFailState == 5)
                {
                    if(fIvanTimeLeft < fTick)
                    {
                        if(theRangeRandom.nextFloat() < 0.5F)
                        {
                            if(theRangeRandom.nextFloat() < 0.5F)
                                deltaAzimuth = turnStepMax;
                            else
                                deltaAzimuth = -turnStepMax;
                            deltaTangage = theRangeRandom.nextFloat(-turnStepMax, turnStepMax);
                        } else
                        {
                            if(theRangeRandom.nextFloat() < 0.5F)
                                deltaTangage = turnStepMax;
                            else
                                deltaTangage = -turnStepMax;
                            deltaAzimuth = theRangeRandom.nextFloat(-turnStepMax, turnStepMax);
                        }
                        fIvanTimeLeft = theRangeRandom.nextFloat(1.0F, 2.0F);
                    } else
                    {
                        deltaAzimuth = oldDeltaAzimuth;
                        deltaTangage = oldDeltaTangage;
                        fIvanTimeLeft -= fTick;
                        if(fIvanTimeLeft < fTick)
                        {
                            iFailState = 0;
                            fIvanTimeLeft = 0.0F;
                        }
                    }
                } else
                if(theFailState == 2)
                {
                    if(theRangeRandom.nextFloat() < 0.5F)
                        deltaAzimuth = turnStepMax;
                    else
                        deltaAzimuth = -turnStepMax;
                    if(theRangeRandom.nextFloat() < 0.5F)
                        deltaTangage = turnStepMax;
                    else
                        deltaTangage = -turnStepMax;
                } else
                if(theFailState == 6)
                {
                    deltaAzimuth = oldDeltaAzimuth;
                    deltaTangage = oldDeltaTangage;
                } else
                {
                    if(((Tuple3d) (pT)).x > -10D)
                    {
                        deltaAzimuth = (float)(-angleAzimuth);
                        if(deltaAzimuth > turnStepMax)
                            deltaAzimuth = turnStepMax;
                        if(deltaAzimuth < -turnStepMax)
                            deltaAzimuth = -turnStepMax;
                        deltaTangage = (float)angleTangage;
                        if(deltaTangage > turnStepMax)
                            deltaTangage = turnStepMax;
                        if(deltaTangage < -turnStepMax)
                            deltaTangage = -turnStepMax;
                    }
                    if(Math.abs(oldDeltaAzimuth - deltaAzimuth) > turnDiffMax)
                        if(oldDeltaAzimuth < deltaAzimuth)
                            deltaAzimuth = oldDeltaAzimuth + turnDiffMax;
                        else
                            deltaAzimuth = oldDeltaAzimuth - turnDiffMax;
                    if(Math.abs(oldDeltaTangage - deltaTangage) > turnDiffMax)
                        if(oldDeltaTangage < deltaTangage)
                            deltaTangage = oldDeltaTangage + turnDiffMax;
                        else
                            deltaTangage = oldDeltaTangage - turnDiffMax;
                    oldDeltaAzimuth = deltaAzimuth;
                    oldDeltaTangage = deltaTangage;
                }
                or.increment(deltaAzimuth, deltaTangage, 0.0F);
                or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
            } else
            if(iLaunchType == 2)
                or.setYPR((float)getLaunchYaw(), (float)getLaunchPitch(), 0.0F);
            else
                or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        } else
        if(Time.current() < tStart + lTrackDelay && iLaunchType == 2)
            or.setYPR((float)getLaunchYaw(), (float)getLaunchPitch(), 0.0F);
        else
            or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        deltaAzimuth = deltaTangage = 0.0F;
        super.pos.setAbs(p, or);
        if(Time.current() > tStart + 2L * lTrackDelay)
            if(Actor.isValid(victim))
            {
                float f2 = (float)p.distance(victim.pos.getAbsPoint());
                if((victim instanceof Aircraft) && f2 > prevd && prevd != 1000F)
                {
                    if(f2 < 30F)
                    {
                        doExplosionAir();
                        postDestroy();
                        collide(false);
                        drawing(false);
                    }
                    if(getSpeed(null) > victim.getSpeed(null))
                        victim = null;
                }
                prevd = f2;
            } else
            {
                prevd = 1000F;
            }
        if(!Actor.isValid(getOwner()) || !(getOwner() instanceof Aircraft))
        {
            doExplosionAir();
            postDestroy();
            collide(false);
            drawing(false);
            return false;
        } else
        {
            return false;
        }
    }

    public boolean stepBeamRider()
    {
        float fTick = Time.tickLenFs();
        float fSpeed = (float)getSpeed(null);
        int theFailState = getFailState();
        if(theFailState == 3)
            fTimeFire = 0.0F;
        super.pos.getAbs(p, or);
        or.wrap();
        float theForce = fForce;
        float millisecondsFromStart = Time.current() - tStart;
        if(millisecondsFromStart > fTimeFire)
        {
            flameActive = false;
            smokeActive = false;
            endSmoke();
            fMassa = fMassaEnd;
            fForce = 0.0F;
        } else
        {
            if(fT1 > 0.001F && millisecondsFromStart < fT1)
                theForce *= (fP1 + ((100F - fP1) * millisecondsFromStart) / fT1) / 100F;
            float millisecondsToEnd = fTimeFire - millisecondsFromStart;
            if(fT2 > 0.001F && millisecondsToEnd < fT2)
                theForce *= (fP2 + ((100F - fP2) * (fT2 - millisecondsToEnd)) / fT2) / 100F;
            fMassa -= fDiffM;
        }
        float fForceAzimuth = AIM9BMissilePhysics.getGForce(fSpeed, oldDeltaAzimuth / fTick);
        float fForceTangage = AIM9BMissilePhysics.getGForce(fSpeed, oldDeltaTangage / fTick);
        float fTurnForce = (float)Math.sqrt(fForceAzimuth * fForceAzimuth + fForceTangage * fForceTangage) * 9.80665F * fMassa;
        fTurnForce *= fCw;
        float fResForce = (float)Math.sqrt(Math.abs(theForce * theForce - fTurnForce * fTurnForce));
        if(fTurnForce > theForce)
            fResForce *= -1F;
        float fAccelForce = fResForce - AIM9BMissilePhysics.getDragInGravity(fSquare, fCw, (float)((Tuple3d) (p)).z, fSpeed, or.getTangage(), fMassa);
        float fAccel = fAccelForce / fMassa;
        fSpeed += fAccel * fTick;
        if(fSpeed < 3F)
            theFailState = 7;
        v.set(1.0D, 0.0D, 0.0D);
        or.transform(v);
        v.scale(fSpeed);
        setSpeed(v);
        p.x += ((Tuple3d) (v)).x * (double)fTick;
        p.y += ((Tuple3d) (v)).y * (double)fTick;
        p.z += ((Tuple3d) (v)).z * (double)fTick;
        if(isNet() && isNetMirror())
        {
            super.pos.setAbs(p, or);
            return false;
        }
        if(theFailState == 7)
        {
            doExplosionAir();
            postDestroy();
            collide(false);
            drawing(false);
            return false;
        }
        Actor myOwner = getOwner();
        if(victim != null && RocketAIM9BUtils.angleBetween(myOwner, victim) > fMaxFOVfrom)
            victim = null;
        if(victim != null)
        {
            if(myOwner != null)
            {
                float hTurn = 0.0F;
                float vTurn = 0.0F;
                Point3d pointAC = new Point3d();
                pointAC.set(myOwner.pos.getAbsPoint());
                Orient orientAC = new Orient();
                orientAC.set(myOwner.pos.getAbsOrient());
                Point3d pointTarget = new Point3d();
                pointTarget.set(victim.pos.getAbsPoint());
                Vector3d vectorOffset = new Vector3d();
                Orient orientTarget = new Orient();
                orientTarget.set(victim.pos.getAbsOrient());
                vectorOffset.set(pVictimOffset);
                orientTarget.transform(vectorOffset);
                pointTarget.add(vectorOffset);
                Point3d pointMissile = new Point3d();
                pointMissile.set(super.pos.getAbsPoint());
                Point3d pointMissileAft = new Point3d();
                pointMissileAft.set(pointMissile);
                Orient orientMissileAft = new Orient();
                orientMissileAft.set(super.pos.getAbsOrient());
                Point3d pointACAft = new Point3d();
                pointACAft.set(pointAC);
                pointTarget.sub(pointAC);
                orientAC.transformInv(pointTarget);
                float targetAzimuth = (float)Math.toDegrees(Math.atan(-((Tuple3d) (pointTarget)).y / ((Tuple3d) (pointTarget)).x));
                float targetElevation = (float)Math.toDegrees(Math.atan(((Tuple3d) (pointTarget)).z / ((Tuple3d) (pointTarget)).x));
                if(theFailState == 4)
                {
                    targetAzimuth += 180F;
                    targetElevation += 180F;
                    if(targetAzimuth > 180F)
                        targetAzimuth = 180F - targetAzimuth;
                    if(targetElevation > 180F)
                        targetElevation = 180F - targetElevation;
                }
                pointMissile.sub(pointAC);
                orientAC.transformInv(pointMissile);
                float missileAzimuth = (float)Math.toDegrees(Math.atan(-((Tuple3d) (pointMissile)).y / ((Tuple3d) (pointMissile)).x));
                float missileElevation = (float)Math.toDegrees(Math.atan(((Tuple3d) (pointMissile)).z / ((Tuple3d) (pointMissile)).x));
                float missileOffsetAzimuth = missileAzimuth - targetAzimuth;
                float missileOffsetElevation = missileElevation - targetElevation;
                pointACAft.sub(pointMissileAft);
                orientMissileAft.transformInv(pointACAft);
                float missileAzimuthAft = (float)Math.toDegrees(Math.atan(-((Tuple3d) (pointACAft)).y / ((Tuple3d) (pointACAft)).x));
                float missileElevationAft = (float)Math.toDegrees(Math.atan(((Tuple3d) (pointACAft)).z / ((Tuple3d) (pointACAft)).x));
                float missileTrackOffsetAzimuth = missileOffsetAzimuth - missileAzimuthAft;
                float missileTrackOffsetElevation = missileOffsetElevation - missileElevationAft;
                float closingFactor = -5F;
                float maxClosing = 60F;
                float fastClosingMax = 3F;
                float turnNormal = 1.0F;
                float turnQuick = 1.5F;
                float turnSharp = 2.0F;
                String missileCurse = "";
                if(missileOffsetAzimuth < 0.0F)
                {
                    if(missileTrackOffsetAzimuth < 0.0F)
                    {
                        hTurn = turnSharp;
                        missileCurse = missileCurse + "L:RS";
                    } else
                    if(missileTrackOffsetAzimuth > maxClosing)
                    {
                        hTurn = -turnNormal;
                        missileCurse = missileCurse + "L:L ";
                    } else
                    if(missileTrackOffsetAzimuth > fastClosingMax && missileTrackOffsetAzimuth > closingFactor * missileOffsetAzimuth)
                    {
                        hTurn = -turnQuick;
                        missileCurse = missileCurse + "L:LQ";
                    } else
                    {
                        hTurn = turnNormal;
                        missileCurse = missileCurse + "L:R ";
                    }
                } else
                if(missileTrackOffsetAzimuth > 0.0F)
                {
                    hTurn = -turnSharp;
                    missileCurse = missileCurse + "R:LS";
                } else
                if(missileTrackOffsetAzimuth < -maxClosing)
                {
                    hTurn = turnNormal;
                    missileCurse = missileCurse + "R:R ";
                } else
                if(missileTrackOffsetAzimuth < -fastClosingMax && missileTrackOffsetAzimuth < closingFactor * missileOffsetAzimuth)
                {
                    hTurn = turnQuick;
                    missileCurse = missileCurse + "R:RQ";
                } else
                {
                    hTurn = -turnNormal;
                    missileCurse = missileCurse + "R:L ";
                }
                missileCurse = missileCurse + " # ";
                if(missileOffsetElevation < 0.0F)
                {
                    if(missileTrackOffsetElevation < 0.0F)
                    {
                        vTurn = turnSharp;
                        missileCurse = missileCurse + "B:US";
                    } else
                    if(missileTrackOffsetElevation > maxClosing)
                    {
                        vTurn = -turnNormal;
                        missileCurse = missileCurse + "B:D ";
                    } else
                    if(missileTrackOffsetElevation > fastClosingMax && missileTrackOffsetElevation > closingFactor * missileOffsetElevation)
                    {
                        vTurn = -turnQuick;
                        missileCurse = missileCurse + "B:DQ";
                    } else
                    {
                        vTurn = turnNormal;
                        missileCurse = missileCurse + "B:U ";
                    }
                } else
                if(missileTrackOffsetElevation > 0.0F)
                {
                    vTurn = -turnSharp;
                    missileCurse = missileCurse + "A:DS";
                } else
                if(missileTrackOffsetElevation < -maxClosing)
                {
                    vTurn = turnNormal;
                    missileCurse = missileCurse + "A:U ";
                } else
                if(missileTrackOffsetElevation < -fastClosingMax && missileTrackOffsetElevation < closingFactor * missileOffsetElevation)
                {
                    vTurn = turnQuick;
                    missileCurse = missileCurse + "A:UQ";
                } else
                {
                    vTurn = -turnNormal;
                    missileCurse = missileCurse + "A:D ";
                }
                iCounter++;
                if(Time.current() > tStart + lTrackDelay)
                {
                    float turnStepMax = AIM9BMissilePhysics.getDegPerSec(fSpeed, fMaxG) * fTick * AIM9BMissilePhysics.getAirDensityFactor((float)((Tuple3d) (p)).z);
                    float turnDiffMax = turnStepMax / fStepsForFullTurn;
                    if(theFailState == 5)
                    {
                        if(fIvanTimeLeft < fTick)
                        {
                            if(theRangeRandom.nextFloat() < 0.5F)
                            {
                                if(theRangeRandom.nextFloat() < 0.5F)
                                    deltaAzimuth = turnStepMax;
                                else
                                    deltaAzimuth = -turnStepMax;
                                deltaTangage = theRangeRandom.nextFloat(-turnStepMax, turnStepMax);
                            } else
                            {
                                if(theRangeRandom.nextFloat() < 0.5F)
                                    deltaTangage = turnStepMax;
                                else
                                    deltaTangage = -turnStepMax;
                                deltaAzimuth = theRangeRandom.nextFloat(-turnStepMax, turnStepMax);
                            }
                            fIvanTimeLeft = theRangeRandom.nextFloat(1.0F, 2.0F);
                        } else
                        {
                            deltaAzimuth = oldDeltaAzimuth;
                            deltaTangage = oldDeltaTangage;
                            fIvanTimeLeft -= fTick;
                            if(fIvanTimeLeft < fTick)
                            {
                                iFailState = 0;
                                fIvanTimeLeft = 0.0F;
                            }
                        }
                    } else
                    if(theFailState == 2)
                    {
                        if(theRangeRandom.nextFloat() < 0.5F)
                            deltaAzimuth = turnStepMax;
                        else
                            deltaAzimuth = -turnStepMax;
                        if(theRangeRandom.nextFloat() < 0.5F)
                            deltaTangage = turnStepMax;
                        else
                            deltaTangage = -turnStepMax;
                    } else
                    if(theFailState == 6)
                    {
                        deltaAzimuth = oldDeltaAzimuth;
                        deltaTangage = oldDeltaTangage;
                    } else
                    {
                        if(Math.abs(targetAzimuth) <= 90F && Math.abs(targetElevation) <= 90F)
                        {
                            if(hTurn * oldDeltaAzimuth < 0.0F)
                            {
                                deltaAzimuth = hTurn * turnDiffMax;
                            } else
                            {
                                deltaAzimuth = oldDeltaAzimuth + hTurn * turnDiffMax;
                                if(deltaAzimuth < -turnStepMax)
                                    deltaAzimuth = -turnStepMax;
                                if(deltaAzimuth > turnStepMax)
                                    deltaAzimuth = turnStepMax;
                            }
                            if(vTurn * oldDeltaTangage < 0.0F)
                            {
                                deltaTangage = vTurn * turnDiffMax;
                            } else
                            {
                                deltaTangage = oldDeltaTangage + vTurn * turnDiffMax;
                                if(deltaTangage < -turnStepMax)
                                    deltaTangage = -turnStepMax;
                                if(deltaTangage > turnStepMax)
                                    deltaTangage = turnStepMax;
                            }
                        }
                        oldDeltaAzimuth = deltaAzimuth;
                        oldDeltaTangage = deltaTangage;
                    }
                    or.increment(deltaAzimuth, deltaTangage, 0.0F);
                    or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
                } else
                if(iLaunchType == 2)
                    or.setYPR((float)getLaunchYaw(), (float)getLaunchPitch(), 0.0F);
                else
                    or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
            }
        } else
        if(Time.current() < tStart + lTrackDelay && iLaunchType == 2)
            or.setYPR((float)getLaunchYaw(), (float)getLaunchPitch(), 0.0F);
        else
            or.setYPR(or.getYaw(), or.getPitch(), 0.0F);
        deltaAzimuth = deltaTangage = 0.0F;
        super.pos.setAbs(p, or);
        if(Time.current() > tStart + 2L * lTrackDelay)
            if(Actor.isValid(victim))
            {
                float f2 = (float)p.distance(victim.pos.getAbsPoint());
                if((victim instanceof Aircraft) && f2 > prevd && prevd != 1000F)
                {
                    if(f2 < 30F)
                    {
                        doExplosionAir();
                        postDestroy();
                        collide(false);
                        drawing(false);
                    }
                    if(getSpeed(null) > victim.getSpeed(null))
                        victim = null;
                }
                prevd = f2;
            } else
            {
                prevd = 1000F;
            }
        if(!Actor.isValid(getOwner()) || !(getOwner() instanceof Aircraft))
        {
            doExplosionAir();
            postDestroy();
            collide(false);
            drawing(false);
            return false;
        } else
        {
            return false;
        }
    }

    public boolean interpolateStep()
    {
        Class localClass = super.getClass();
        int iStepMode = Property.intValue(localClass, "stepMode", 0);
        if(Time.current() > tStart + lTrackDelay && (isSunTracking() || isGroundTracking()))
            victim = null;
        switch(iStepMode)
        {
        case 0: // '\0'
            return stepTargetHoming();

        case 1: // '\001'
            return stepBeamRider();
        }
        return true;
    }

    private int getFailState()
    {
        if(lTimeToFailure == 0L)
            return 0;
        long millisecondsFromStart = Time.current() - tStart;
        if(millisecondsFromStart < lTimeToFailure)
            return 0;
        if(iFailState == 1)
        {
            float fRand = theRangeRandom.nextFloat();
            if((double)fRand < 0.01D)
                return 7;
            if((double)fRand < 0.02D)
                return 4;
            if((double)fRand < 0.20000000000000001D)
                return 2;
            else
                return (double)fRand < 0.5D ? 6 : 5;
        } else
        {
            return iFailState;
        }
    }

    private void setFailState()
    {
        if(iFailState == 0)
            iFailState = theRangeRandom.nextInt(1, 7);
    }

    public AIM9BMissile()
    {
        twoPlaces = new DecimalFormat("+000.00;-000.00");
        fm = null;
        or = new Orient();
        p = new Point3d();
        orVictimOffset = null;
        pVictimOffset = null;
        pT = null;
        pOwnerOffset = null;
        v = null;
        victimSpeed = null;
        tStart = 0L;
        prevd = 0.0F;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        d = 0.0D;
        victim = null;
        fMissileBaseSpeed = 0.0F;
        launchKren = 0.0D;
        launchPitch = 0.0D;
        launchYaw = 0.0D;
        dPi = 3.1415926535897931D;
        endedFlame = false;
        endedSmoke = false;
        endedSprite = false;
        flameActive = true;
        smokeActive = true;
        spriteActive = true;
        iCounter = 0;
        bTest = false;
        bTest2 = false;
        fMaxFOVfrom = 0.0F;
        fLeadPercent = 0.0F;
        fMaxG = 12F;
        fStepsForFullTurn = 10F;
        fTimeLife = 30F;
        fTimeFire = 2.2F;
        fForce = 18712F;
        lTimeToFailure = 0L;
        fSunRayAngle = 0.0F;
        fGroundTrackFactor = 0.0F;
        fMaxLaunchG = 2.0F;
        iLaunchType = 0;
        fKalibr = 0.2F;
        fMassa = 86.2F;
        fMassaEnd = 86.2F;
        fSquare = 1.0F;
        fCw = 0.3F;
        lTrackDelay = 1000L;
        lLastTimeNoFlare = 0L;
        lFlareLockTime = 1000L;
        fDiffM = 0.0F;
        fT1 = 0.0F;
        fP1 = 0.0F;
        fT2 = 0.0F;
        fP2 = 0.0F;
        iFailState = 0;
        fIvanTimeLeft = 0.0F;
        smokes = null;
        sprites = null;
        flames = null;
        sEffSmoke = null;
        sEffSprite = null;
        sSimFlame = null;
        iExhausts = 1;
        firstNavLight = null;
        lastNavLight = null;
        MissileInit();
    }

    public final void MissileInit()
    {
        d = 0.10000000000000001D;
        victim = null;
        fm = null;
        tStart = 0L;
        prevd = 1000F;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        pT = new Point3d();
        v = new Vector3d();
        victimSpeed = new Vector3d();
        oldDeltaAzimuth = 0.0F;
        oldDeltaTangage = 0.0F;
        orVictimOffset = new Orient();
        pVictimOffset = new Point3f();
        pOwnerOffset = new Point3d();
        endedFlame = false;
        endedSmoke = false;
        endedSprite = false;
        flameActive = true;
        smokeActive = true;
        spriteActive = true;
        theRangeRandom = new RangeRandom(System.currentTimeMillis());
    }

    public AIM9BMissile(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        twoPlaces = new DecimalFormat("+000.00;-000.00");
        fm = null;
        or = new Orient();
        p = new Point3d();
        orVictimOffset = null;
        pVictimOffset = null;
        pT = null;
        pOwnerOffset = null;
        v = null;
        victimSpeed = null;
        tStart = 0L;
        prevd = 0.0F;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        d = 0.0D;
        victim = null;
        fMissileBaseSpeed = 0.0F;
        launchKren = 0.0D;
        launchPitch = 0.0D;
        launchYaw = 0.0D;
        dPi = 3.1415926535897931D;
        endedFlame = false;
        endedSmoke = false;
        endedSprite = false;
        flameActive = true;
        smokeActive = true;
        spriteActive = true;
        iCounter = 0;
        bTest = false;
        bTest2 = false;
        fMaxFOVfrom = 0.0F;
        fLeadPercent = 0.0F;
        fMaxG = 12F;
        fStepsForFullTurn = 10F;
        fTimeLife = 30F;
        fTimeFire = 2.2F;
        fForce = 18712F;
        lTimeToFailure = 0L;
        fSunRayAngle = 0.0F;
        fGroundTrackFactor = 0.0F;
        fMaxLaunchG = 2.0F;
        iLaunchType = 0;
        fKalibr = 0.2F;
        fMassa = 86.2F;
        fMassaEnd = 86.2F;
        fSquare = 1.0F;
        fCw = 0.3F;
        lTrackDelay = 1000L;
        lLastTimeNoFlare = 0L;
        lFlareLockTime = 1000L;
        fDiffM = 0.0F;
        fT1 = 0.0F;
        fP1 = 0.0F;
        fT2 = 0.0F;
        fP2 = 0.0F;
        iFailState = 0;
        fIvanTimeLeft = 0.0F;
        smokes = null;
        sprites = null;
        flames = null;
        sEffSmoke = null;
        sEffSprite = null;
        sSimFlame = null;
        iExhausts = 1;
        firstNavLight = null;
        lastNavLight = null;
        MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    public final void MissileInit(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        d = 0.10000000000000001D;
        victim = null;
        fm = null;
        tStart = 0L;
        prevd = 1000F;
        pT = new Point3d();
        v = new Vector3d();
        victimSpeed = new Vector3d();
        orVictimOffset = new Orient();
        pVictimOffset = new Point3f();
        pOwnerOffset = new Point3d();
        oldDeltaAzimuth = 0.0F;
        oldDeltaTangage = 0.0F;
        endedFlame = false;
        endedSmoke = false;
        endedSprite = false;
        flameActive = true;
        smokeActive = true;
        spriteActive = true;
        theRangeRandom = new RangeRandom(System.currentTimeMillis());
        super.net = new Mirror(this, netchannel, i);
        super.pos.setAbs(point3d, orient);
        super.pos.reset();
        super.pos.setBase(actor, null, true);
        doStart(-1F);
        v.set(1.0D, 0.0D, 0.0D);
        orient.transform(v);
        v.scale(f);
        setSpeed(v);
        collide(false);
        doStart(-1F);
    }

    public void start(float f)
    {
        start(f, 0);
    }

    public void start(float f, int paramInt)
    {
        Actor actor = super.pos.base();
        if(!Actor.isValid(actor) || !(actor instanceof Aircraft))
            return; //By PAL, what happened here: break MISSING_BLOCK_LABEL_66;
        if(actor.isNetMirror())
        {
            destroy();
            return;
        }
        try
        {
            super.net = new Master(this);
        }
        catch(Exception exception)
        {
            AIM9BNetSafeLog.log(getOwner(), "Missile launch cancelled (system error)");
            destroy();
        }
        doStart(f);
        return;
    }

    private void getMissileProperties()
    {
        Class localClass = super.getClass();
        float f = Time.tickLenFs();
        iLaunchType = Property.intValue(localClass, "launchType", 0);
        fMaxFOVfrom = Property.floatValue(localClass, "maxFOVfrom", 180F);
        fMaxLaunchG = Property.floatValue(localClass, "maxLockGForce", 99.9F);
        fMaxG = Property.floatValue(localClass, "maxGForce", 12F);
        fStepsForFullTurn = Property.floatValue(localClass, "stepsForFullTurn", 10F);
        fTimeFire = Property.floatValue(localClass, "timeFire", 2.2F) * 1000F;
        fTimeLife = Property.floatValue(localClass, "timeLife", 30F) * 1000F;
        super.timeFire = (long)fTimeFire;
        super.timeLife = (long)fTimeLife;
        fForce = Property.floatValue(localClass, "force", 18712F);
        fLeadPercent = Property.floatValue(localClass, "leadPercent", 0.0F);
        fKalibr = Property.floatValue(localClass, "kalibr", 0.2F);
        fMassa = Property.floatValue(localClass, "massa", 86.2F);
        fMassaEnd = Property.floatValue(localClass, "massaEnd", 80F);
        fSunRayAngle = Property.floatValue(localClass, "sunRayAngle", 0.0F);
        fGroundTrackFactor = Property.floatValue(localClass, "groundTrackFactor", 0.0F);
        lFlareLockTime = Property.longValue(localClass, "flareLockTime", 1000L);
        lTrackDelay = Property.longValue(localClass, "trackDelay", 1000L);
        fSquare = (3.141593F * fKalibr * fKalibr) / 4F;
        fT1 = Property.floatValue(localClass, "forceT1", 0.0F) * 1000F;
        fP1 = Property.floatValue(localClass, "forceP1", 0.0F);
        fT2 = Property.floatValue(localClass, "forceT2", 0.0F) * 1000F;
        fP2 = Property.floatValue(localClass, "forceP2", 0.0F);
        fCw = Property.floatValue(localClass, "dragCoefficient", 0.3F);
        iExhausts = getNumExhausts();
        sEffSmoke = Property.stringValue(localClass, "smoke", null);
        sEffSprite = Property.stringValue(localClass, "sprite", null);
        sSimFlame = Property.stringValue(localClass, "flame", null);
        float fFailureRate = Property.floatValue(localClass, "failureRate", 10F);
        theRangeRandom.setSeed(Time.current());
        if(theRangeRandom.nextFloat(0.0F, 100F) < fFailureRate)
        {
            setFailState();
            float fRand = theRangeRandom.nextFloat();
            fRand = fRand * fRand * fRand * fRand;
            long lBaseFailTime = lTrackDelay;
            if(iFailState == 7)
                lBaseFailTime += lBaseFailTime;
            lTimeToFailure = lBaseFailTime + (long)((fTimeLife - (float)lBaseFailTime) * fRand);
        } else
        {
            iFailState = 0;
            lTimeToFailure = 0L;
        }
        if(fTimeFire > 0.0F)
            fDiffM = (fMassa - fMassaEnd) / (fTimeFire / 1000F / Time.tickConstLenFs());
        else
            fDiffM = 0.0F;
    }

    private void createAdditionalSmokes()
    {
        smokes = new Eff3DActor[iExhausts];
        smokes[0] = super.smoke;
        if(sEffSmoke == null)
            return;
        com.maddox.il2.engine.Hook theHook = null;
        for(int i = 1; i < iExhausts; i++)
        {
            theHook = findHook("_SMOKE" + i);
            if(theHook == null)
            {
                smokes[i] = null;
            } else
            {
                smokes[i] = Eff3DActor.New(this, theHook, null, 1.0F, sEffSmoke, -1F);
                if(smokes[i] != null)
                    ((Actor) (smokes[i])).pos.changeHookToRel();
            }
        }

    }

    private void createAdditionalSprites()
    {
        sprites = new Eff3DActor[iExhausts];
        sprites[0] = super.sprite;
        if(sEffSprite == null)
            return;
        com.maddox.il2.engine.Hook theHook = null;
        for(int i = 1; i < iExhausts; i++)
        {
            theHook = findHook("_SMOKE" + i);
            if(theHook == null)
            {
                sprites[i] = null;
            } else
            {
                sprites[i] = Eff3DActor.New(this, theHook, null, fKalibr, sEffSprite, -1F);
                if(sprites[i] != null)
                    ((Actor) (sprites[i])).pos.changeHookToRel();
            }
        }

    }

    private void createAdditionalFlames()
    {
        flames = new Actor[iExhausts];
        flames[0] = super.flame;
        if(sSimFlame == null)
            return;
        com.maddox.il2.engine.Hook theHook = null;
        for(int i = 1; i < iExhausts; i++)
        {
            theHook = findHook("_SMOKE" + i);
            flames[i] = new ActorSimpleMesh(sSimFlame);
            if(flames[i] != null)
            {
                ((ActorSimpleMesh)flames[i]).mesh().setScale(1.0F);
                flames[i].pos.setBase(this, theHook, false);
                flames[i].pos.changeHookToRel();
                flames[i].pos.resetAsBase();
            }
        }

    }

    private void endNavLights()
    {
        for(MissileNavLight theNavLight = firstNavLight; theNavLight != null; theNavLight = theNavLight.nextNavLight)
            Eff3DActor.finish(theNavLight.theNavLight);

    }

    private void createNavLights()
    {
        createNamedNavLights("_NavLightR", "3DO/Effects/Fireworks/FlareRed.eff");
        createNamedNavLights("_NavLightG", "3DO/Effects/Fireworks/FlareGreen.eff");
        createNamedNavLights("_NavLightW", "3DO/Effects/Fireworks/FlareWhite.eff");
        createNamedNavLights("_NavLightP", "3DO/Effects/Fireworks/PhosfourousBall.eff");
    }

    private void createNamedNavLights(String theNavLightHookName, String theEffectName)
    {
        int numNavLights = getNumNavLights(theNavLightHookName);
        if(numNavLights == 0)
            return;
        com.maddox.il2.engine.Hook theHook = null;
        for(int i = 0; i < numNavLights; i++)
        {
            if(i == 0)
                theHook = findHook(theNavLightHookName);
            else
                theHook = findHook(theNavLightHookName + i);
            MissileNavLight theNavLight = new MissileNavLight(Eff3DActor.New(this, theHook, null, 1.0F, theEffectName, -1F));
            if(firstNavLight == null)
            {
                firstNavLight = theNavLight;
                lastNavLight = theNavLight;
            } else
            {
                lastNavLight.nextNavLight = theNavLight;
                lastNavLight = theNavLight;
            }
        }

    }

    private int getNumNavLights(String theNavLightHookName)
    {
        if(super.mesh.hookFind(theNavLightHookName) == -1)
            return 0;
        int retVal;
        for(retVal = 1; super.mesh.hookFind(theNavLightHookName + retVal) != -1; retVal++);
        return retVal;
    }

    private int getNumExhausts()
    {
        if(super.mesh.hookFind("_SMOKE") == -1)
            return 0;
        int retVal;
        for(retVal = 1; super.mesh.hookFind("_SMOKE" + retVal) != -1; retVal++);
        return retVal;
    }

    private void doStart(float f)
    {
        super.start(-1F, 0);
        getMissileProperties();
        if(iExhausts > 1)
        {
            createAdditionalSmokes();
            createAdditionalSprites();
            createAdditionalFlames();
        }
        firstNavLight = null;
        lastNavLight = null;
        if(Config.isUSE_RENDER())
            createNavLights();
        ((ActorSimpleMesh)super.flame).mesh().setScale(1.0F);
        prevd = 1000F;
        super.pos.getRelOrient().transformInv(super.speed);
        super.speed.y *= 3D;
        super.speed.z *= 3D;
        super.speed.x -= 198D;
        super.pos.getRelOrient().transform(super.speed);
        tStart = Time.current();
        if(Config.isUSE_RENDER())
            super.flame.drawing(true);
        super.pos.getAbs(p, or);
        Point3d ownerPos = new Point3d();
        getOwner().pos.getAbs(ownerPos);
        pOwnerOffset.set(p);
        pOwnerOffset.sub(ownerPos);
        fMissileBaseSpeed = (float)getSpeed(null);
        if(isNet() && isNetMirror())
            return;
        fm = ((SndAircraft) ((Aircraft)getOwner())).FM;
        switch(iLaunchType)
        {
        case 1: // '\001'
            fMissileBaseSpeed += 20F;
            v.set(1.0D, 0.0D, 0.0D);
            or.transform(v);
            v.scale(fMissileBaseSpeed);
            setSpeed(v);
            launchKren = Math.toRadians(or.getKren());
            launchYaw = or.getYaw();
            launchPitch = or.getPitch();
            or.setYPR((float)launchYaw, (float)launchPitch, 0.0F);
            super.pos.setAbs(p, or);
            break;

        case 2: // '\002'
            launchKren = Math.toRadians(or.getKren());
            launchYaw = or.getYaw() + fm.getAOA() * (float)Math.sin(launchKren);
            launchPitch = or.getPitch() - fm.getAOA() * (float)Math.cos(launchKren);
            or.setYPR((float)launchYaw + 0.5F * (float)Math.sin(launchKren), (float)launchPitch - 0.5F * (float)Math.cos(launchKren), 0.0F);
            super.pos.setAbs(p, or);
            break;
        }
        victim = null;
        if((getOwner() != World.getPlayerAircraft() || !((RealFlightModel)fm).isRealMode()) && (fm instanceof Pilot))
        {
            if(getOwner() instanceof TypeAIM9Carrier)
            {
                victim = ((TypeAIM9Carrier)getOwner()).getMissileTarget();
                pVictimOffset = ((TypeAIM9Carrier)getOwner()).getMissileTargetOffset();
            } else
            if(getOwner() instanceof TypeFighter)
            {
                Pilot pilot = (Pilot)fm;
                victim = ((Interpolate) (((Maneuver) (pilot)).target)).actor;
            }
            return; //By PAL, what happened here: break MISSING_BLOCK_LABEL_809;
        }
        if(fm.getOverload() > fMaxLaunchG)
        {
            victim = null;
            return;
        }
        try
        {
            if(getOwner() instanceof TypeAIM9Carrier)
            {
                victim = ((TypeAIM9Carrier)getOwner()).getMissileTarget();
                pVictimOffset = ((TypeAIM9Carrier)getOwner()).getMissileTargetOffset();
            } else
            {
                victim = Selector.look(true, false, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], World.getPlayerAircraft().getArmy(), -1, World.getPlayerAircraft(), false);
                if(victim == null)
                    victim = Main3D.cur3D().getViewPadlockEnemy();
            }
        }
        catch(Exception exception)
        {
            EventLog.type("Missile doStart Exception: " + exception.getMessage());
        }
    }

    public void destroy()
    {
        endNavLights();
        flameActive = false;
        smokeActive = false;
        spriteActive = false;
        endSmoke();
        victim = null;
        fm = null;
        tStart = 0L;
        prevd = 1000F;
        super.destroy();
    }

    protected void doExplosion(Actor actor, String s)
    {
        super.doExplosion(actor, s);
    }

    protected void doExplosionAir()
    {
        super.doExplosionAir();
    }

    private boolean isSunTracking()
    {
        if(fSunRayAngle == 0.0F)
            return false;
        float sunAngle = RocketAIM9BUtils.angleBetween(this, World.Sun().ToSun);
        return sunAngle < fSunRayAngle;
    }

    private boolean isGroundTracking()
    {
        if(fGroundTrackFactor == 0.0F)
            return false;
        super.pos.getAbs(p, or);
        or.wrap();
        float ttG = or.getTangage() * -1F;
        float missileAlt = (float)(((Tuple3d) (p)).z - Engine.land().HQ_Air(((Tuple3d) (p)).x, ((Tuple3d) (p)).y));
        missileAlt /= 1000F;
        float groundFactor = ttG / (missileAlt * missileAlt);
        long lTimeCurrent = Time.current();
        if(lLastTimeNoFlare == 0L)
            lLastTimeNoFlare = lTimeCurrent;
        if(groundFactor > fGroundTrackFactor)
        {
            return lTimeCurrent >= lLastTimeNoFlare + lFlareLockTime;
        } else
        {
            lLastTimeNoFlare = lTimeCurrent;
            return false;
        }
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel)
        throws IOException
    {
        NetMsgSpawn netmsgspawn = super.netReplicate(netchannel);
        netmsgspawn.writeNetObj(getOwner().net);
        Point3d point3d = super.pos.getAbsPoint();
        netmsgspawn.writeFloat((float)((Tuple3d) (point3d)).x);
        netmsgspawn.writeFloat((float)((Tuple3d) (point3d)).y);
        netmsgspawn.writeFloat((float)((Tuple3d) (point3d)).z);
        Orient orient = super.pos.getAbsOrient();
        netmsgspawn.writeFloat(orient.azimut());
        netmsgspawn.writeFloat(orient.tangage());
        float f = (float)getSpeed(null);
        netmsgspawn.writeFloat(f);
        return netmsgspawn;
    }

    DecimalFormat twoPlaces;
    private FlightModel fm;
    private Orient or;
    private Point3d p;
    private Orient orVictimOffset;
    private Point3f pVictimOffset;
    private Point3d pT;
    private Point3d pOwnerOffset;
    private Vector3d v;
    private static Vector3d dir = new Vector3d();
    private Vector3d victimSpeed;
    private long tStart;
    private float prevd;
    private float deltaAzimuth;
    private float deltaTangage;
    private double d;
    private Actor victim;
    private float fMissileBaseSpeed;
    private double launchKren;
    private double launchPitch;
    private double launchYaw;
    private double dPi;
    private float oldDeltaAzimuth;
    private float oldDeltaTangage;
    private boolean endedFlame;
    private boolean endedSmoke;
    private boolean endedSprite;
    private boolean flameActive;
    private boolean smokeActive;
    private boolean spriteActive;
    private int iCounter;
    private boolean bTest;
    private boolean bTest2;
    private float fMaxFOVfrom;
    private float fLeadPercent;
    private float fMaxG;
    private float fStepsForFullTurn;
    private float fTimeLife;
    private float fTimeFire;
    private float fForce;
    private long lTimeToFailure;
    private float fSunRayAngle;
    private float fGroundTrackFactor;
    private float fMaxLaunchG;
    private int iLaunchType;
    private float fKalibr;
    private float fMassa;
    private float fMassaEnd;
    private float fSquare;
    private float fCw;
    private long lTrackDelay;
    private long lLastTimeNoFlare;
    private long lFlareLockTime;
    private float fDiffM;
    private float fT1;
    private float fP1;
    private float fT2;
    private float fP2;
    private int iFailState;
    private float fIvanTimeLeft;
    private Eff3DActor smokes[];
    private Eff3DActor sprites[];
    private Actor flames[];
    private String sEffSmoke;
    private String sEffSprite;
    private String sSimFlame;
    private int iExhausts;
    private MissileNavLight firstNavLight;
    private MissileNavLight lastNavLight;
    protected static final int LAUNCH_TYPE_STRAIGHT = 0;
    protected static final int LAUNCH_TYPE_QUICK = 1;
    protected static final int LAUNCH_TYPE_DROP = 2;
    protected static final int STEP_MODE_HOMING = 0;
    protected static final int STEP_MODE_BEAMRIDER = 1;
    protected static final int DETECTOR_TYPE_MANUAL = 0;
    protected static final int DETECTOR_TYPE_INFRARED = 1;
    protected static final int DETECTOR_TYPE_RADAR_HOMING = 2;
    protected static final int DETECTOR_TYPE_RADAR_BEAMRIDING = 3;
    protected static final int DETECTOR_TYPE_RADAR_TRACK_VIA_MISSILE = 4;
    protected static final int FAIL_TYPE_NONE = 0;
    protected static final int FAIL_TYPE_ELECTRONICS = 1;
    protected static final int FAIL_TYPE_MIRROR = 2;
    protected static final int FAIL_TYPE_ENGINE = 3;
    protected static final int FAIL_TYPE_REFLEX = 4;
    protected static final int FAIL_TYPE_IVAN = 5;
    protected static final int FAIL_TYPE_CONTROL_BLOCKED = 6;
    protected static final int FAIL_TYPE_WARHEAD = 7;
    protected static final int FAIL_TYPE_NUMBER = 7;
    private static final float IVAN_TIME_MIN = 1F;
    private static final float IVAN_TIME_MAX = 2F;
    private static final boolean MISSILE_DEBUG = false;
    private RangeRandom theRangeRandom;

}
