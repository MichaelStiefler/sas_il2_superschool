// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 31.03.2019 23:30:07
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Missile.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.vehicles.artillery.RocketryRocket;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.util.HashMapExt;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket, MissileInterceptable, ActiveMissile, RocketFlare, 
//            RocketChaff, MissilePhysics, GuidedMissileUtils

public class ZALOHA_Missile extends Rocket
    implements MsgExplosionListener, MsgShotListener
{
    static class SPAWN
        implements NetSpawn
    {

        public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
        {
        }

        public void netSpawn(int i, NetMsgInput netmsginput)
        {
            NetObj netobj = netmsginput.readNetObj();
            if(netobj == null)
                return;
            try
            {
                Actor actor = (Actor)netobj.superObj();
                Point3d point3d = new Point3d(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
                Orient orient = new Orient(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
                float f = netmsginput.readFloat();
                doSpawn(actor, netmsginput.channel(), i, point3d, orient, f);
                if(actor instanceof TypeGuidedMissileCarrier)
                    ((TypeGuidedMissileCarrier)actor).getGuidedMissileUtils().shotMissile();
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        }

        SPAWN()
        {
        }
    }

    private class MissileNavLight
    {

        public MissileNavLight nextNavLight;
        public Eff3DActor theNavLight;

        public MissileNavLight(Eff3DActor eff3dactor)
        {
            theNavLight = eff3dactor;
            nextNavLight = null;
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
            theMissilePoint3d.set(netmsginput.readFloat(), netmsginput.readFloat(), netmsginput.readFloat());
            int i = netmsginput.readShort();
            int j = netmsginput.readShort();
            int k = netmsginput.readShort();
            float f = -(((float)i * 180F) / 32000F);
            float f1 = ((float)j * 90F) / 32000F;
            float f2 = ((float)k * 90F) / 32000F;
            theMissileOrient.set(f, f1, f2);
            actor().pos.setAbs(theMissilePoint3d, theMissileOrient);
            return true;
        }

        NetMsgFiltered out;
        Orient theMissileOrient;
        Point3d theMissilePoint3d;

        public Mirror(Actor actor, NetChannel netchannel, int i)
        {
            super(actor, netchannel, i);
            theMissileOrient = new Orient();
            theMissilePoint3d = new Point3d();
            out = new NetMsgFiltered();
        }
    }

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
            return;
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
                actor().pos.getAbs(theMissilePoint3d, theMissileOrient);
                out.writeFloat((float)theMissilePoint3d.x);
                out.writeFloat((float)theMissilePoint3d.y);
                out.writeFloat((float)theMissilePoint3d.z);
                theMissileOrient.wrap();
                int i = (int)((theMissileOrient.getYaw() * 32000F) / 180F);
                int j = (int)((theMissileOrient.tangage() * 32000F) / 90F);
                int k = (int)((theMissileOrient.getKren() * 32000F) / 90F);
                out.writeShort(i);
                out.writeShort(j);
                out.writeShort(k);
                post(Time.current(), out);
            }
            catch(Exception exception)
            {
                NetObj.printDebug(exception);
            }
        }

        NetMsgFiltered out;
        Orient theMissileOrient;
        Point3d theMissilePoint3d;

        public Master(Actor actor)
        {
            super(actor);
            theMissileOrient = new Orient();
            theMissilePoint3d = new Point3d();
            out = new NetMsgFiltered();
        }
    }


    public ZALOHA_Missile()
    {
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        dragCoefficient = 0.3F;
        dragCoefficientTurn = 0.3F;
        dropFlightPathOrient = new Orient();
        effSmoke = null;
        effSprite = null;
        endedFlame = false;
        endedSmoke = false;
        endedSprite = false;
        engineDelayTime = 0L;
        engineRunning = false;
        exhausts = 1;
        failState = 0;
        firstNavLight = null;
        flameActive = true;
        flames = null;
        flareLockTime = 1000L;
        frontSquare = 1.0F;
        groundTrackFactor = 0.0F;
        initialMissileForce = 0.0F;
        ivanTimeLeft = 0.0F;
        lastNavLight = null;
        lastTimeNoFlare = 0L;
        launchKren = 0.0D;
        launchPitch = 0.0D;
        launchType = 0;
        launchYaw = 0.0D;
        leadPercent = 0.0F;
        massaEnd = 86.2F;
        massLossPerTick = 0.0F;
        maxFOVfrom = 0.0F;
        maxG = 12F;
        maxLaunchG = 2.0F;
        missileBaseSpeed = 0.0F;
        missileForce = 18712F;
        missileForceRunUpTime = 0.0F;
        missileKalibr = 0.2F;
        missileMass = 86.2F;
        missileOrient = new Orient();
        missilePoint3d = new Point3d();
        missileSustainedForce = 0.0F;
        missileTimeLife = 30F;
        myActiveMissile = null;
        oldRoll = 0.0F;
        previousDistance = 0.0F;
        releaseTime = 0L;
        rocketMotorOperationTime = 2.2F;
        rocketMotorSustainedOperationTime = 0.0F;
        safeVictimOffset = new Vector3d();
        simFlame = null;
        smokeActive = true;
        smokes = null;
        spriteActive = true;
        sprites = null;
        noSmokeFlameSustain = false;
        playingMotorSound = false;
        noSoundSustain = false;
        soundNameSustain = null;
        playingSustainMotorSound = false;
        effSmokeSustain = null;
        effSpriteSustain = null;
        simFlameSustain = null;
        showingSustainSmokeFlame = false;
        effSmokeTrail = null;
        showingTrailSmoke = false;
        mshFly = null;
        waitingMeshFly = false;
        mshSustain = null;
        waitingMeshSustain = false;
        mshDamage = null;
        startTime = 0L;
        startTimeIsSet = false;
        iDetectorType = 0;
        lTargetType = 0L;
        stepMode = 0;
        stepsForFullTurn = 10F;
        sunRayAngle = 0.0F;
        targetPoint3d = null;
        targetPoint3dAbs = null;
        timeToFailure = 0L;
        trackDelay = 1000L;
        trajectoryVector3d = null;
        turnDiffMax = 0.0F;
        attackMaxDistance = 5000F;
        missileProximityFuzeRadius = 50F;
        soundNameRadarPW = null;
        twoPlaces = new DecimalFormat("+000.00;-000.00");
        victim = null;
        lockingVictim = false;
        victimOffsetOrient = null;
        victimOffsetPoint3f = null;
        victimSpeed = null;
        bRocketFiring = false;
        bLaserHoming = false;
        laserOwner = null;
        bSACLOSHoming = false;
        saclosOwner = null;
        bRealisticRadarSelect = false;
        damage_MAT_TYPE = -1;
        damage_EXPL_TYPE = -1;
        damage_EFF_BODY_MATERIAL = -1;
        damage_PANZER = 0.001F;
        damage_MIN_TNT = 0.07F;
        damage_MAX_TNT = 0.071F;
        damage_PROBAB_DEATH_WHEN_EXPLOSION = 0.4F;
        bMirrorValueSet = false;
        mirrorAzimuth = 0.0F;
        mirrorTangage = 0.0F;
        iStatusCur_getArmy = 0;
        iStatusPrev_getArmy = 0;
        targetPoint3d = new Point3d();
        targetPoint3dAbs = new Point3d();
        trajectoryVector3d = new Vector3d();
        victimSpeed = new Vector3d();
        victimOffsetOrient = new Orient();
        victimOffsetPoint3f = new Point3f();
        MissileInit();
    }

    public ZALOHA_Missile(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        dragCoefficient = 0.3F;
        dragCoefficientTurn = 0.3F;
        dropFlightPathOrient = new Orient();
        effSmoke = null;
        effSprite = null;
        endedFlame = false;
        endedSmoke = false;
        endedSprite = false;
        engineDelayTime = 0L;
        engineRunning = false;
        exhausts = 1;
        failState = 0;
        firstNavLight = null;
        flameActive = true;
        flames = null;
        flareLockTime = 1000L;
        frontSquare = 1.0F;
        groundTrackFactor = 0.0F;
        initialMissileForce = 0.0F;
        ivanTimeLeft = 0.0F;
        lastNavLight = null;
        lastTimeNoFlare = 0L;
        launchKren = 0.0D;
        launchPitch = 0.0D;
        launchType = 0;
        launchYaw = 0.0D;
        leadPercent = 0.0F;
        massaEnd = 86.2F;
        massLossPerTick = 0.0F;
        maxFOVfrom = 0.0F;
        maxG = 12F;
        maxLaunchG = 2.0F;
        missileBaseSpeed = 0.0F;
        missileForce = 18712F;
        missileForceRunUpTime = 0.0F;
        missileKalibr = 0.2F;
        missileMass = 86.2F;
        missileOrient = new Orient();
        missilePoint3d = new Point3d();
        missileSustainedForce = 0.0F;
        missileTimeLife = 30F;
        myActiveMissile = null;
        oldRoll = 0.0F;
        previousDistance = 0.0F;
        releaseTime = 0L;
        rocketMotorOperationTime = 2.2F;
        rocketMotorSustainedOperationTime = 0.0F;
        safeVictimOffset = new Vector3d();
        simFlame = null;
        smokeActive = true;
        smokes = null;
        spriteActive = true;
        sprites = null;
        noSmokeFlameSustain = false;
        playingMotorSound = false;
        noSoundSustain = false;
        soundNameSustain = null;
        playingSustainMotorSound = false;
        effSmokeSustain = null;
        effSpriteSustain = null;
        simFlameSustain = null;
        showingSustainSmokeFlame = false;
        effSmokeTrail = null;
        showingTrailSmoke = false;
        mshFly = null;
        waitingMeshFly = false;
        mshSustain = null;
        waitingMeshSustain = false;
        mshDamage = null;
        startTime = 0L;
        startTimeIsSet = false;
        iDetectorType = 0;
        lTargetType = 0L;
        stepMode = 0;
        stepsForFullTurn = 10F;
        sunRayAngle = 0.0F;
        targetPoint3d = null;
        targetPoint3dAbs = null;
        timeToFailure = 0L;
        trackDelay = 1000L;
        trajectoryVector3d = null;
        turnDiffMax = 0.0F;
        attackMaxDistance = 5000F;
        missileProximityFuzeRadius = 50F;
        soundNameRadarPW = null;
        twoPlaces = new DecimalFormat("+000.00;-000.00");
        victim = null;
        lockingVictim = false;
        victimOffsetOrient = null;
        victimOffsetPoint3f = null;
        victimSpeed = null;
        bRocketFiring = false;
        bLaserHoming = false;
        laserOwner = null;
        bSACLOSHoming = false;
        saclosOwner = null;
        bRealisticRadarSelect = false;
        damage_MAT_TYPE = -1;
        damage_EXPL_TYPE = -1;
        damage_EFF_BODY_MATERIAL = -1;
        damage_PANZER = 0.001F;
        damage_MIN_TNT = 0.07F;
        damage_MAX_TNT = 0.071F;
        damage_PROBAB_DEATH_WHEN_EXPLOSION = 0.4F;
        bMirrorValueSet = false;
        mirrorAzimuth = 0.0F;
        mirrorTangage = 0.0F;
        iStatusCur_getArmy = 0;
        iStatusPrev_getArmy = 0;
        MissileInit(actor, netchannel, i, point3d, orient, f);
    }

    public float AttackMaxDistance()
    {
        return attackMaxDistance;
    }

    public int chooseBulletType(BulletProperties abulletproperties[])
    {
        if(abulletproperties.length == 1)
            return 0;
        if(abulletproperties.length <= 0)
            return -1;
        if(abulletproperties[0].power <= 0.0F)
            return 1;
        if(abulletproperties[1].power <= 0.0F)
            return 0;
        if(abulletproperties[0].powerType == 1)
            return 0;
        if(abulletproperties[1].powerType == 1)
            return 1;
        if(abulletproperties[0].powerType == 0)
            return 0;
        if(abulletproperties[1].powerType == 0)
            return 1;
        else
            return abulletproperties[0].powerType == 2 ? 1 : 0;
    }

    public int chooseShotpoint(BulletProperties bulletproperties)
    {
        return (this instanceof MissileInterceptable) ? 0 : -1;
    }

    private boolean computeFuzeState()
    {
        deltaAzimuth = deltaTangage = 0.0F;
        pos.setAbs(missilePoint3d, missileOrient);
        if(Time.current() > startTime + 2L * trackDelay)
            if(Actor.isValid(victim))
            {
                Point3d point3d = new Point3d();
                victim.pos.getTime(Time.tickNext(), point3d);
                Point3d point3d1 = new Point3d();
                pos.getTime(Time.tickNext(), point3d1);
                float f = (float)missilePoint3d.distance(victim.pos.getAbsPoint());
                float f1 = (float)point3d1.distance(point3d);
                if(f1 < 0.0F)
                    f1 = 0.0F;
                if(((victim instanceof Aircraft) || (victim instanceof RocketryRocket) || (victim instanceof MissileInterceptable)) && (f1 > f * 5F || f > previousDistance) && previousDistance != 1000F && f > 0.0F && f < missileProximityFuzeRadius)
                {
                    if(bLogDetail)
                    {
                        ArrayList arraylist = new ArrayList();
                        Point3d point3d2 = new Point3d();
                        pos.getAbs(point3d2);
                        Engine.collideEnv().getSphere(arraylist, point3d2, missileProximityFuzeRadius * 2.0F);
                        System.out.println("Missile(" + actorString(this) + ") - entering doExplosionAir(); - f2=" + f + " < FuzeRadius=" + missileProximityFuzeRadius + " , lst.size()=" + arraylist.size() + " , lst=" + arraylist);
                    }
                    doExplosionAir();
                    postDestroy();
                    collide(false);
                    drawing(false);
                }
                previousDistance = f;
            } else
            {
                previousDistance = 1000F;
            }
        safeVictimOffset.set(victimOffsetPoint3f);
        if(!Actor.isValid(getOwner()))
        {
            if(bLogDetail)
                System.out.println("Missile(" + actorString(this) + ") - entering doExplosionAir(); - Because of owner(" + actorString(getOwner()) + ") becomes not valid.");
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

    private float computeMissileAccelleration()
    {
        victimOffsetPoint3f.set(safeVictimOffset);
        float f = (float)getSpeed(null);
        if(getFailState() == 3)
        {
            rocketMotorOperationTime = 0.0F;
            rocketMotorSustainedOperationTime = 0.0F;
        }
        pos.getAbs(missilePoint3d, missileOrient);
        missileOrient.wrap();
        float f1 = missileForce;
        float f2 = Time.current() - startTime;
        if(f2 > rocketMotorOperationTime + rocketMotorSustainedOperationTime)
        {
            if(!noSmokeFlameSustain && (flameActive || smokeActive || spriteActive))
            {
                flameActive = false;
                smokeActive = false;
                spriteActive = false;
                endSmoke();
            }
            if(noSmokeFlameSustain && playingSustainMotorSound)
            {
                stopSounds();
                playingSustainMotorSound = false;
            }
            if(playingMotorSound)
            {
                stopSounds();
                playingMotorSound = false;
            }
            if(!showingTrailSmoke && effSmokeTrail != null)
            {
                afterFireEngineTrail();
                showingTrailSmoke = true;
            }
            missileMass = massaEnd;
            missileForce = 0.0F;
        } else
        if(f2 > rocketMotorOperationTime)
        {
            if(noSmokeFlameSustain && (flameActive || smokeActive || spriteActive))
            {
                flameActive = false;
                smokeActive = false;
                spriteActive = false;
                endSmoke();
            }
            f1 = missileSustainedForce;
            if(waitingMeshSustain)
            {
                setMesh(MeshShared.get(mshSustain));
                waitingMeshSustain = false;
            }
            if(!noSmokeFlameSustain && !showingSustainSmokeFlame && effSmokeSustain != null)
            {
                flameActive = false;
                smokeActive = false;
                spriteActive = false;
                endSmoke();
                sustainEngine();
                showingSustainSmokeFlame = true;
            }
            if(noSoundSustain && playingMotorSound)
            {
                stopSounds();
                playingMotorSound = false;
            }
            if(!noSmokeFlameSustain && !playingSustainMotorSound && soundNameSustain != null && playingMotorSound)
            {
                stopSounds();
                playingMotorSound = false;
            }
            if(!playingSustainMotorSound && soundNameSustain != null)
            {
                newSound(soundNameSustain, true);
                playingSustainMotorSound = true;
            }
        } else
        {
            if(missileForceRunUpTime > 0.001F && f2 < missileForceRunUpTime)
            {
                float f3 = f2 / missileForceRunUpTime;
                if(f3 > 1.0F)
                    f3 = 1.0F;
                setAllSmokeIntensities(f3);
                setAllSpriteIntensities(f3);
                f1 *= (initialMissileForce + (100F - initialMissileForce) * f3) / 100F;
            }
            missileMass -= massLossPerTick;
        }
        float f4 = MissilePhysics.getGForce(f, oldDeltaAzimuth / Time.tickLenFs());
        float f5 = MissilePhysics.getGForce(f, oldDeltaTangage / Time.tickLenFs());
        float f6 = (float)Math.sqrt(f4 * f4 + f5 * f5) * 9.80665F * missileMass * ((float)Math.sqrt(2D) - 1.0F);
        f6 *= dragCoefficientTurn;
        float f7 = (float)Math.sqrt(Math.abs(f1 * f1 - f6 * f6));
        if(f6 > f1)
            f7 *= -1F;
        float f8 = f7 - MissilePhysics.getDragInGravity(frontSquare, dragCoefficient, (float)missilePoint3d.z, f, missileOrient.getTangage(), missileMass);
        float f9 = f8 / missileMass;
        f += f9 * Time.tickLenFs();
        if(f < 3F)
            failState = 7;
        trajectoryVector3d.set(1.0D, 0.0D, 0.0D);
        if(Time.current() < startTime + trackDelay && launchType == 2)
            dropFlightPathOrient.transform(trajectoryVector3d);
        else
            missileOrient.transform(trajectoryVector3d);
        trajectoryVector3d.scale(f);
        setSpeed(trajectoryVector3d);
        missilePoint3d.x += trajectoryVector3d.x * (double)Time.tickLenFs();
        missilePoint3d.y += trajectoryVector3d.y * (double)Time.tickLenFs();
        missilePoint3d.z += trajectoryVector3d.z * (double)Time.tickLenFs();
        if(isNet() && isNetMirror())
        {
            pos.setAbs(missilePoint3d, missileOrient);
            return -1F;
        }
        if(getFailState() == 7)
        {
            if(bLogDetail)
                System.out.println("Missile(" + actorString(this) + ") - entering doExplosionAir(); - Because of getFailState() == FAIL_TYPE_WARHEAD.");
            doExplosionAir();
            postDestroy();
            collide(false);
            drawing(false);
            return -1F;
        } else
        {
            return f;
        }
    }

    private void computeMissilePath(float f, float f1, float f2, float f3, float f4)
    {
        float f5 = MissilePhysics.getDegPerSec(f, maxG) * Time.tickLenFs();
        float f6 = 1.0F;
        float f7 = MissilePhysics.getAirDensityFactor((float)missilePoint3d.z);
        if(f < 340F)
        {
            f6 = f / 340F;
        } else
        {
            f7 = (float)((double)f7 * Math.sqrt(f / 340F));
            if(f7 > 1.0F)
                f7 = 1.0F;
        }
        f5 *= f6 * f7;
        float f8 = f5 / stepsForFullTurn;
        if(f8 > turnDiffMax)
            turnDiffMax = (turnDiffMax * stepsForFullTurn + f8) / (stepsForFullTurn + 1.0F);
        if(getFailState() == 5)
        {
            if(ivanTimeLeft < Time.tickLenFs())
            {
                if(TrueRandom.nextFloat() < 0.5F)
                {
                    if(TrueRandom.nextFloat() < 0.5F)
                        deltaAzimuth = f5;
                    else
                        deltaAzimuth = -f5;
                    deltaTangage = TrueRandom.nextFloat(-f5, f5);
                } else
                {
                    if(TrueRandom.nextFloat() < 0.5F)
                        deltaTangage = f5;
                    else
                        deltaTangage = -f5;
                    deltaAzimuth = TrueRandom.nextFloat(-f5, f5);
                }
                ivanTimeLeft = TrueRandom.nextFloat(1.0F, 2.0F);
            } else
            {
                deltaAzimuth = oldDeltaAzimuth;
                deltaTangage = oldDeltaTangage;
                ivanTimeLeft -= Time.tickLenFs();
                if(ivanTimeLeft < Time.tickLenFs())
                {
                    failState = 0;
                    ivanTimeLeft = 0.0F;
                }
            }
        } else
        if(getFailState() == 2)
        {
            if(!bMirrorValueSet || RndB(0.02F))
            {
                if(RndB(0.5F))
                    deltaAzimuth = f5;
                else
                    deltaAzimuth = -f5;
                if(RndB(0.5F))
                    deltaTangage = f5;
                else
                    deltaTangage = -f5;
                bMirrorValueSet = true;
                mirrorAzimuth = deltaAzimuth;
                mirrorTangage = deltaTangage;
            } else
            {
                if(RndB(0.9F))
                    deltaAzimuth = mirrorAzimuth;
                if(RndB(0.9F))
                    deltaTangage = mirrorTangage;
            }
        } else
        if(getFailState() == 6)
        {
            deltaAzimuth = oldDeltaAzimuth;
            deltaTangage = oldDeltaTangage;
        } else
        {
            if(stepMode == 0)
            {
                if(targetPoint3d.x > -10D)
                {
                    deltaAzimuth = -f3;
                    if(deltaAzimuth > f5)
                        deltaAzimuth = f5;
                    if(deltaAzimuth < -f5)
                        deltaAzimuth = -f5;
                    deltaTangage = f4;
                    if(deltaTangage > f5)
                        deltaTangage = f5;
                    if(deltaTangage < -f5)
                        deltaTangage = -f5;
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
            } else
            if(stepMode == 1 && Math.abs(f3) <= 90F && Math.abs(f4) <= 90F)
            {
                if(f1 * oldDeltaAzimuth < 0.0F)
                {
                    deltaAzimuth = f1 * turnDiffMax;
                } else
                {
                    deltaAzimuth = oldDeltaAzimuth + f1 * turnDiffMax;
                    if(deltaAzimuth < -f5)
                        deltaAzimuth = -f5;
                    if(deltaAzimuth > f5)
                        deltaAzimuth = f5;
                }
                if(f2 * oldDeltaTangage < 0.0F)
                {
                    deltaTangage = f2 * turnDiffMax;
                } else
                {
                    deltaTangage = oldDeltaTangage + f2 * turnDiffMax;
                    if(deltaTangage < -f5)
                        deltaTangage = -f5;
                    if(deltaTangage > f5)
                        deltaTangage = f5;
                }
            }
            oldDeltaAzimuth = deltaAzimuth;
            oldDeltaTangage = deltaTangage;
        }
        missileOrient.increment(deltaAzimuth, deltaTangage, 0.0F);
        missileOrient.setYPR(missileOrient.getYaw(), missileOrient.getPitch(), getRoll());
    }

    private void computeNoTrackPath()
    {
        if(launchType == 2)
        {
            dropFlightPathOrient.setYPR((float)getLaunchYaw(), (float)getLaunchPitch(), getRoll());
            missileOrient.setYPR(dropFlightPathOrient.getYaw(), dropFlightPathOrient.getPitch(), getRoll());
        } else
        {
            missileOrient.setYPR(missileOrient.getYaw(), missileOrient.getPitch(), getRoll());
        }
    }

    private void createAdditionalFlames(int i)
    {
        flames = new Actor[exhausts];
        flames[0] = flame;
        if(i == 0 && simFlame == null)
            return;
        if(i == 1 && simFlameSustain == null)
            return;
        Object obj = null;
        for(int j = 1; j < exhausts; j++)
        {
            com.maddox.il2.engine.Hook hook = findHook("_SMOKE" + j);
            flames[j] = new ActorSimpleMesh(i != 0 ? simFlameSustain : simFlame);
            if(flames[j] == null)
                continue;
            ((ActorSimpleMesh)flames[j]).mesh().setScale(1.0F);
            flames[j].pos.setBase(this, hook, false);
            flames[j].pos.changeHookToRel();
            flames[j].pos.resetAsBase();
            if(Config.isUSE_RENDER())
                flames[j].drawing(true);
        }

    }

    private void createAdditionalSmokes(int i)
    {
        smokes = new Eff3DActor[exhausts];
        smokes[0] = smoke;
        if(i == 0 && effSmoke == null)
            return;
        if(i == 1 && effSmokeSustain == null)
            return;
        if(i == 2 && effSmokeTrail == null)
            return;
        Object obj = null;
        String s = null;
        switch(i)
        {
        case 0: // '\0'
        default:
            s = effSmoke;
            break;

        case 1: // '\001'
            s = effSmokeSustain;
            break;

        case 2: // '\002'
            s = effSmokeTrail;
            break;
        }
        for(int j = 1; j < exhausts; j++)
        {
            com.maddox.il2.engine.Hook hook = findHook("_SMOKE" + j);
            if(hook == null)
            {
                smokes[j] = null;
                continue;
            }
            smokes[j] = Eff3DActor.New(this, hook, null, 1.0F, s, -1F);
            if(smokes[j] != null)
                smokes[j].pos.changeHookToRel();
        }

    }

    private void createAdditionalSprites(int i)
    {
        sprites = new Eff3DActor[exhausts];
        sprites[0] = sprite;
        if(i == 0 && effSprite == null)
            return;
        if(i == 1 && effSpriteSustain == null)
            return;
        Object obj = null;
        for(int j = 1; j < exhausts; j++)
        {
            com.maddox.il2.engine.Hook hook = findHook("_SMOKE" + j);
            if(hook == null)
            {
                sprites[j] = null;
                continue;
            }
            sprites[j] = Eff3DActor.New(this, hook, null, missileKalibr, i != 0 ? effSpriteSustain : effSprite, -1F);
            if(sprites[j] != null)
                sprites[j].pos.changeHookToRel();
        }

    }

    private void createNamedNavLights(String s, String s1)
    {
        int i = getNumNavLights(s);
        if(i == 0)
            return;
        Object obj = null;
        for(int j = 0; j < i; j++)
        {
            com.maddox.il2.engine.Hook hook;
            if(j == 0)
                hook = findHook(s);
            else
                hook = findHook(s + j);
            MissileNavLight missilenavlight = new MissileNavLight(Eff3DActor.New(this, hook, null, 1.0F, s1, -1F));
            if(firstNavLight == null)
            {
                firstNavLight = missilenavlight;
                lastNavLight = missilenavlight;
            } else
            {
                lastNavLight.nextNavLight = missilenavlight;
                lastNavLight = missilenavlight;
            }
        }

    }

    private void createNavLights()
    {
        createNamedNavLights("_NavLightR", "3DO/Effects/Fireworks/FlareRed.eff");
        createNamedNavLights("_NavLightG", "3DO/Effects/Fireworks/FlareGreen.eff");
        createNamedNavLights("_NavLightW", "3DO/Effects/Fireworks/FlareWhite.eff");
        createNamedNavLights("_NavLightP", "3DO/Effects/Fireworks/PhosfourousBall.eff");
    }

    public void destroy()
    {
        if(isNet() && isNetMirror())
            doExplosionAir();
        endNavLights();
        flameActive = false;
        smokeActive = false;
        spriteActive = false;
        showingTrailSmoke = false;
        endSmoke();
        victim = null;
        lockingVictim = false;
        startTime = 0L;
        previousDistance = 1000F;
        if(myActiveMissile != null)
        {
            GuidedMissileUtils.removeActiveMissile(myActiveMissile);
            myActiveMissile = null;
        }
        if(this instanceof MissileInterceptable)
            Engine.targets().remove(this);
        Engine.missiles().remove(this);
        super.destroy();
    }

    private void doStart(float f)
    {
        if(waitingMeshFly)
        {
            setMesh(MeshShared.get(mshFly));
            waitingMeshFly = false;
        }
        startMissile(-1F, 0);
        setMissileEffects();
        setMissileStartParams();
        if(isNet() && isNetMirror())
            return;
        setMissileDropParams();
        setMissileVictim();
        //this.myActiveMissile = new ActiveMissile(this, getOwner(), victim, Actor.isValid(getOwner()) ? getOwner().getArmy() : 0x7fffffff, Actor.isValid(victim) ? victim.getArmy() : 0x7fffffff, ownerIsAI());
        GuidedMissileUtils.addActiveMissile(myActiveMissile);
        startEngineDone();
        
        
        //TODO: reset missile FOV
		
		System.out.println(
				"Missile FOV changing to: "
		        + this.maxFOVfrom_real 
		        + " /from: " 
		        + this.maxFOVfrom    
			);
	if(this.maxFOVfrom_real!=0.0F)
	this.maxFOVfrom = this.maxFOVfrom_real;
		//TODO: end of reset FOV
        
        
        if(bLogDetail && Actor.isValid(this))
            System.out.println("Missile(" + actorString(this) + ") - doStart(f), recording new ActiveMissile(): getOwner()=" + actorString(getOwner()) + "; " + (Actor.isValid(getOwner()) ? "Valid getOwner().getArmy()=" + getOwner().getArmy() : "Invalid") + " - collisionR()=" + mesh().collisionR() + " , visibilityR()=" + mesh().visibilityR());
        if(bLogDetail && Actor.isValid(this))
            iStatusCur_getArmy |= Integer.parseInt("0000000000000000000010000000000", 2);
    }

    private void endAllFlame()
    {
        if(exhausts < 2)
        {
            if(flame != null)
                ObjState.destroy(flame);
        } else
        if(flames != null)
        {
            for(int i = 0; i < exhausts; i++)
                if(flames[i] != null)
                    ObjState.destroy(flames[i]);

        }
        if(light != null)
            light.light.setEmit(0.0F, 1.0F);
        stopSounds();
        endedFlame = true;
        bRocketFiring = false;
    }

    private void endAllSmoke()
    {
        if(exhausts < 2)
        {
            if(smoke != null)
                Eff3DActor.finish(smoke);
        } else
        if(smokes != null)
        {
            for(int i = 0; i < exhausts; i++)
                if(smokes[i] != null)
                    Eff3DActor.finish(smokes[i]);

        }
        endedSmoke = true;
    }

    private void endAllSprites()
    {
        if(exhausts < 2)
        {
            if(sprite != null)
                Eff3DActor.finish(sprite);
        } else
        if(sprites != null)
        {
            for(int i = 0; i < exhausts; i++)
                if(sprites[i] != null)
                    Eff3DActor.finish(sprites[i]);

        }
        endedSprite = true;
    }

    private void endNavLights()
    {
        for(MissileNavLight missilenavlight = firstNavLight; missilenavlight != null; missilenavlight = missilenavlight.nextNavLight)
            Eff3DActor.finish(missilenavlight.theNavLight);

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

    public int getArmy()
    {
        if(bLogDetail)
        {
            iStatusCur_getArmy |= Integer.parseInt("0000000000000000000000000000010", 2);
            if(!Actor.isValid(getOwner()))
                iStatusCur_getArmy |= Integer.parseInt("0000000000000000000000000000100", 2);
            else
                iStatusCur_getArmy &= Integer.parseInt("1111111111111111111111111111011", 2);
            if(Actor.isValid(this) && iStatusCur_getArmy != iStatusPrev_getArmy)
                System.out.println("Missile(" + actorString(this) + ") - getArmy(): getOwner()=" + actorString(getOwner()) + "; " + (Actor.isValid(getOwner()) ? "Valid getOwner().getArmy()=" + getOwner().getArmy() : "Invalid") + " , iStatusCur_getArmy=" + iStatusCur_getArmy);
            iStatusPrev_getArmy = iStatusCur_getArmy;
        }
        if(Actor.isValid(getOwner()))
            return getOwner().getArmy();
        else
            return 0;
    }

    private int getFailState()
    {
        if(timeToFailure == 0L)
            return 0;
        long l = Time.current() - startTime;
        if(l < timeToFailure)
            return 0;
        if(myActiveMissile != null)
        {
            GuidedMissileUtils.removeActiveMissile(myActiveMissile);
            myActiveMissile = null;
        }
        if(failState == 1)
        {
            float f = TrueRandom.nextFloat();
            if(f < 0.01F)
                return 7;
            if(f < 0.02F)
                return 4;
            if(f < 0.2F)
                return 2;
            return f >= 0.5F ? 5 : 6;
        } else
        {
            return failState;
        }
    }

    private FlightModel getFM()
    {
        if(!ownerIsAircraft())
            return null;
        else
            return ((Aircraft)getOwner()).FM;
    }

    public double getLaunchPitch()
    {
        double d = getLaunchTimeFactor();
        double d1 = 2D * ((Math.cos(d + 0.40000000000000002D) - 2.1759949269000001D) + d / 5D);
        d1 *= Math.cos(launchKren);
        for(d1 += launchPitch; d1 > 180D; d1 -= 360D);
        for(; d1 < -180D; d1 += 360D);
        return d1;
    }

    public double getLaunchTimeFactor()
    {
        return ((double)(Time.current() - startTime) / (double)trackDelay) * 6D;
    }

    public double getLaunchYaw()
    {
        double d = getLaunchTimeFactor();
        double d1 = 2D * ((Math.cos(d + 0.40000000000000002D) - 2.1759949269000001D) + d / 5D);
        d1 *= Math.sin(launchKren);
        for(d1 += launchYaw; d1 > 180D; d1 -= 360D);
        for(; d1 < -180D; d1 += 360D);
        return d1;
    }

    private void getMissileProperties()
    {
        Class class1 = super.getClass();
        launchType = Property.intValue(class1, "launchType", 0);
        stepMode = Property.intValue(class1, "stepMode", 0);
        maxFOVfrom = Property.floatValue(class1, "maxFOVfrom", 180F);
		this.maxFOVfrom_real = Property.floatValue(class1, "maxFOVfrom_real", 0F);
        maxLaunchG = Property.floatValue(class1, "maxLockGForce", 99.9F);
        maxG = Property.floatValue(class1, "maxGForce", 12F);
        stepsForFullTurn = Property.floatValue(class1, "stepsForFullTurn", 10F);
        rocketMotorOperationTime = Property.floatValue(class1, "timeFire", 2.2F) * 1000F;
        missileTimeLife = Property.floatValue(class1, "timeLife", 30F) * 1000F;
        rocketMotorSustainedOperationTime = Property.floatValue(class1, "timeSustain", 0.0F) * 1000F;
        super.timeFire = (long)(rocketMotorOperationTime + rocketMotorSustainedOperationTime);
        super.timeLife = (long)missileTimeLife;
        engineDelayTime = Property.longValue(getClass(), "engineDelayTime", 0L);
        missileForce = Property.floatValue(class1, "force", 18712F);
        missileSustainedForce = Property.floatValue(class1, "forceSustain", 0.0F);
        leadPercent = Property.floatValue(class1, "leadPercent", 0.0F);
        missileKalibr = Property.floatValue(class1, "kalibr", 0.2F);
        missileMass = Property.floatValue(class1, "massa", 86.2F);
        massaEnd = Property.floatValue(class1, "massaEnd", 80F);
        sunRayAngle = Property.floatValue(class1, "sunRayAngle", 0.0F);
        groundTrackFactor = Property.floatValue(class1, "groundTrackFactor", 0.0F);
        flareLockTime = Property.longValue(class1, "flareLockTime", 1000L);
        trackDelay = Property.longValue(class1, "trackDelay", 1000L);
        frontSquare = (3.141593F * missileKalibr * missileKalibr) / 4F;
        missileForceRunUpTime = Property.floatValue(class1, "forceT1", 0.0F) * 1000F;
        initialMissileForce = Property.floatValue(class1, "forceP1", 0.0F);
        dragCoefficient = Property.floatValue(class1, "dragCoefficient", 0.3F);
        dragCoefficientTurn = Property.floatValue(class1, "dragCoefficientTurn", dragCoefficient);
        attackMaxDistance = Property.floatValue(class1, "PkDistMax", 5000F);
        exhausts = getNumExhausts();
        effSmoke = Property.stringValue(class1, "smoke", null);
        effSprite = Property.stringValue(class1, "sprite", null);
        simFlame = Property.stringValue(class1, "flame", null);
        float f = Property.floatValue(class1, "failureRate", 10F);
        noSmokeFlameSustain = Property.intValue(class1, "noSmokeFlameSustain", 0) == 1;
        noSoundSustain = Property.intValue(class1, "noSoundSustain", 0) == 1;
        soundNameSustain = Property.stringValue(class1, "soundSustain", null);
        effSmokeSustain = Property.stringValue(class1, "smokeSustain", null);
        effSpriteSustain = Property.stringValue(class1, "spriteSustain", null);
        simFlameSustain = Property.stringValue(class1, "flameSustain", null);
        effSmokeTrail = Property.stringValue(class1, "smokeTrail", null);
        mshFly = Property.stringValue(class1, "meshFly", null);
        mshDamage = Property.stringValue(class1, "meshDamage", null);
        if(mshFly != null)
            waitingMeshFly = true;
        mshSustain = Property.stringValue(class1, "meshSustain", null);
        if(mshSustain != null)
            waitingMeshSustain = true;
        if(TrueRandom.nextFloat(0.0F, 100F) < f)
        {
            setFailState();
            float f1 = TrueRandom.nextFloat();
            long l = trackDelay;
            if(failState == 7)
                l += l;
            timeToFailure = l + (long)((missileTimeLife - (float)l) * f1);
        } else
        {
            failState = 0;
            timeToFailure = 0L;
        }
        if(rocketMotorOperationTime > 0.0F)
            massLossPerTick = (missileMass - massaEnd) / (rocketMotorOperationTime / 1000F / Time.tickConstLenFs());
        else
            massLossPerTick = 0.0F;
        iDetectorType = Property.intValue(class1, "detectorType", 0);
        if(iDetectorType == 9)
            bLaserHoming = true;
        else
            bLaserHoming = false;
        if(iDetectorType == 10)
            bSACLOSHoming = true;
        else
            bSACLOSHoming = false;
        lTargetType = Property.longValue(class1, "targetType", 1L);
        missileProximityFuzeRadius = Property.floatValue(class1, "proximityFuzeRadius", 50F);
        soundNameRadarPW = Property.stringValue(class1, "soundRadarPW", null);
        bRealisticRadarSelect = Config.cur.ini.get("Mods", "RealisticRadarSelect", 0) != 0;
        int i = Config.cur.ini.get("Mods", "GuidedMissileDebugLog", 0);
        if((i & 0x80) == 128)
            bLogDetail = true;
        if((i & 0x100) == 256)
            bLogDetailDamage = true;
        if(this instanceof MissileInterceptable)
        {
            damage_MAT_TYPE = 2;
            damage_EXPL_TYPE = 0;
            damage_EFF_BODY_MATERIAL = 2;
            damage_PANZER = 0.001F;
            damage_MIN_TNT = 0.2F * (damage_PANZER / 0.002F);
            damage_MAX_TNT = damage_MIN_TNT * 1.7F;
            damage_PROBAB_DEATH_WHEN_EXPLOSION = 0.04F;
            flags &= 0xffffffdf;
        } else
        {
            damage_MAT_TYPE = 1;
            damage_EXPL_TYPE = 4;
            damage_EFF_BODY_MATERIAL = 4;
            damage_PANZER = 50F;
            damage_MIN_TNT = 300F * (damage_PANZER / 0.48F);
            damage_MAX_TNT = damage_MIN_TNT * 1.7F;
            damage_PROBAB_DEATH_WHEN_EXPLOSION = 0.001F;
        }
    }

    private int getNumExhausts()
    {
        if(mesh.hookFind("_SMOKE") == -1)
            return 0;
        int i;
        for(i = 1; mesh.hookFind("_SMOKE" + i) != -1; i++);
        return i;
    }

    private int getNumNavLights(String s)
    {
        if(mesh.hookFind(s) == -1)
            return 0;
        int i;
        for(i = 1; mesh.hookFind(s + i) != -1; i++);
        return i;
    }

    public float getRoll()
    {
        if(Time.current() - releaseTime > trackDelay)
            return 0.0F;
        float f = ((float)(Time.current() - startTime) / (float)trackDelay) * 3.141593F;
        float f1 = ((float)Math.cos(f) + 1.0F) * 0.5F;
        float f2;
        for(f2 = 360F - f1 * (360F - oldRoll); f2 > 180F; f2 -= 360F);
        for(; f2 < -180F; f2 += 360F);
        return f2;
    }

    public boolean getShotpointOffset(int i, Point3d point3d)
    {
        if(i != 0)
            return false;
        if(point3d != null)
            point3d.set(0.0D, 0.0D, 0.0D);
        return true;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public Actor getMissileTarget()
    {
        return victim;
    }

    public Point3d getMissileTargetPoint3d()
    {
        return targetPoint3d;
    }

    public Point3d getMissileTargetPoint3dAbs()
    {
        return targetPoint3dAbs;
    }

    public boolean getLockStatus()
    {
        return lockingVictim;
    }

    public int getDetectorType()
    {
        return iDetectorType;
    }

    public long getTargetType()
    {
        return lTargetType;
    }

    public boolean getRocketFiring()
    {
        return bRocketFiring;
    }

    public String getSoundstringRadarPW()
    {
        return soundNameRadarPW;
    }

    public int HitbyMask()
    {
        return -1;
    }

    public boolean interpolateStep()
    {
        if(engineDelayTime > 0L && !engineRunning)
        {
            if(Time.current() > releaseTime + engineDelayTime)
                startEngine();
            startTime = Time.current();
        }
        if(Time.current() > startTime + trackDelay)
        {
            if(isSunTracking() || isGroundTracking())
                victim = null;
            if(iDetectorType == 2 && victim != null)
                if(!Actor.isValid(getOwner()))
                {
                    victim = null;
                } else
                {
                    FlightModel flightmodel = ((Aircraft)getOwner()).FM;
                    if((getOwner() instanceof TypeSemiRadar) && (flightmodel instanceof RealFlightModel) && ((RealFlightModel)flightmodel).isRealMode() && (flightmodel instanceof Pilot))
                    {
                        if(bRealisticRadarSelect && (!((TypeSemiRadar)getOwner()).getSemiActiveRadarOn() || ((TypeSemiRadar)getOwner()).getSemiActiveRadarLockedActor() != victim))
                            lockingVictim = false;
                        else
                            lockingVictim = true;
                    } else
                    if(GuidedMissileUtils.angleBetween(getOwner(), victim) > 45F || GuidedMissileUtils.pitchBetween(getOwner(), victim) > 45F)
                        lockingVictim = false;
                    else
                        lockingVictim = true;
                }
        }
        switch(stepMode)
        {
        case 0: // '\0'
            return stepTargetHoming();

        case 1: // '\001'
            return stepBeamRider();
        }
        return true;
    }

    private boolean isGroundTracking()
    {
        if(groundTrackFactor == 0.0F)
            return false;
        pos.getAbs(missilePoint3d, missileOrient);
        missileOrient.wrap();
        float f = missileOrient.getTangage() * -1F;
        float f1 = (float)(missilePoint3d.z - Engine.land().HQ_Air(missilePoint3d.x, missilePoint3d.y));
        f1 /= 1000F;
        float f2 = f / (f1 * f1);
        long l = Time.current();
        if(lastTimeNoFlare == 0L)
            lastTimeNoFlare = l;
        if(f2 > groundTrackFactor)
        {
            return l >= lastTimeNoFlare + flareLockTime;
        } else
        {
            lastTimeNoFlare = l;
            return false;
        }
    }

    public boolean isReleased()
    {
        return releaseTime != 0L;
    }

    private boolean isSunTracking()
    {
        if(sunRayAngle == 0.0F)
            return false;
        float f = GuidedMissileUtils.angleBetween(this, World.Sun().ToSun);
        return f < sunRayAngle;
    }

    public final void MissileInit()
    {
        victim = null;
        lockingVictim = false;
        startTime = 0L;
        previousDistance = 1000F;
        deltaAzimuth = 0.0F;
        deltaTangage = 0.0F;
        oldDeltaAzimuth = 0.0F;
        oldDeltaTangage = 0.0F;
        endedFlame = false;
        endedSmoke = false;
        endedSprite = false;
        flameActive = true;
        smokeActive = true;
        spriteActive = true;
        getMissileProperties();
    }

    public final void MissileInit(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        if(Actor.isValid(actor) && !Actor.isValid(getOwner()))
            setOwner(actor);
        victim = null;
        lockingVictim = false;
        startTime = 0L;
        previousDistance = 1000F;
        oldDeltaAzimuth = 0.0F;
        oldDeltaTangage = 0.0F;
        endedFlame = false;
        endedSmoke = false;
        endedSprite = false;
        flameActive = true;
        smokeActive = true;
        spriteActive = true;
        net = new Mirror(this, netchannel, i);
        pos.setAbs(point3d, orient);
        pos.reset();
        pos.setBase(actor, null, true);
        trajectoryVector3d.set(1.0D, 0.0D, 0.0D);
        orient.transform(trajectoryVector3d);
        trajectoryVector3d.scale(f);
        setSpeed(trajectoryVector3d);
        collide(false);
        getMissileProperties();
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel)
        throws IOException
    {
        NetMsgSpawn netmsgspawn = super.netReplicate(netchannel);
        netmsgspawn.writeNetObj(getOwner().net);
        Point3d point3d = pos.getAbsPoint();
        netmsgspawn.writeFloat((float)point3d.x);
        netmsgspawn.writeFloat((float)point3d.y);
        netmsgspawn.writeFloat((float)point3d.z);
        Orient orient = pos.getAbsOrient();
        netmsgspawn.writeFloat(orient.azimut());
        netmsgspawn.writeFloat(orient.tangage());
        netmsgspawn.writeFloat(orient.kren());
        float f = (float)getSpeed(null);
        netmsgspawn.writeFloat(f);
        return netmsgspawn;
    }

    private boolean ownerIsAI()
    {
        if(getFM() == null)
            return true;
        return (getOwner() != World.getPlayerAircraft() || !((RealFlightModel)getFM()).isRealMode()) && (getFM() instanceof Pilot);
    }

    private boolean ownerIsAircraft()
    {
        return getOwner() instanceof Aircraft;
    }

    public void runupEngine()
    {
        float f = Time.current() - startTime;
        float f1 = f / missileForceRunUpTime;
        if(f1 > 1.0F)
            f1 = 1.0F;
        setAllSmokeIntensities(f1);
        setAllSpriteIntensities(f1);
    }

    private void setAllSmokeIntensities(float f)
    {
        if(!engineRunning)
            return;
        if(exhausts < 2)
        {
            if(smoke != null)
                Eff3DActor.setIntesity(smoke, f);
        } else
        if(smokes != null)
        {
            for(int i = 0; i < exhausts; i++)
                if(smokes[i] != null)
                    Eff3DActor.setIntesity(smokes[i], f);

        }
    }

    private void setAllSpriteIntensities(float f)
    {
        if(!engineRunning)
            return;
        if(exhausts < 2)
        {
            if(sprite != null)
                Eff3DActor.setIntesity(sprite, f);
        } else
        if(sprites != null)
        {
            for(int i = 0; i < exhausts; i++)
                if(sprites[i] != null)
                    Eff3DActor.setIntesity(sprites[i], f);

        }
    }

    private void setFailState()
    {
        if(failState == 0)
            failState = TrueRandom.nextInt(1, 7);
    }

    private void setMissileDropParams()
    {
        switch(launchType)
        {
        case 0: // '\0'
        default:
            oldRoll = missileOrient.getRoll();
            break;

        case 1: // '\001'
            missileBaseSpeed += 20F;
            trajectoryVector3d.set(1.0D, 0.0D, 0.0D);
            missileOrient.transform(trajectoryVector3d);
            trajectoryVector3d.scale(missileBaseSpeed);
            setSpeed(trajectoryVector3d);
            launchKren = Math.toRadians(missileOrient.getRoll());
            launchYaw = missileOrient.getYaw();
            launchPitch = missileOrient.getPitch();
            oldRoll = missileOrient.getRoll();
            missileOrient.setYPR((float)launchYaw, (float)launchPitch, oldRoll);
            pos.setAbs(missilePoint3d, missileOrient);
            break;

        case 2: // '\002'
            launchKren = Math.toRadians(getOwner().pos.getAbsOrient().getRoll());
            if(ownerIsAircraft())
            {
                launchYaw = missileOrient.getYaw() - getFM().getAOA() * (float)Math.sin(launchKren);
                launchPitch = missileOrient.getPitch() - getFM().getAOA() * (float)Math.cos(launchKren);
                oldRoll = missileOrient.getRoll();
            } else
            {
                launchYaw = getOwner().pos.getCurrentOrient().getYaw();
                launchPitch = getOwner().pos.getCurrentOrient().getPitch();
                oldRoll = getOwner().pos.getCurrentOrient().getRoll();
            }
            dropFlightPathOrient.setYPR((float)launchYaw + 0.5F * (float)Math.sin(launchKren), (float)launchPitch - 0.5F * (float)Math.cos(launchKren), oldRoll);
            break;
        }
    }

    public void setMissileEffects()
    {
        firstNavLight = null;
        lastNavLight = null;
        if(Config.isUSE_RENDER())
            createNavLights();
    }

    private void setMissileStartParams()
    {
        previousDistance = 1000F;
        pos.getRelOrient().transformInv(speed);
        speed.y *= 3D;
        speed.z *= 3D;
        speed.x -= 198D;
        pos.getRelOrient().transform(speed);
        setStartTime();
        pos.getAbs(missilePoint3d, missileOrient);
        missileBaseSpeed = (float)getSpeed((Vector3d)null);
    }

    private void setMissileVictim()
    {
        victim = null;
        lockingVictim = false;
        if(bLaserHoming)
        {
            if(!ownerIsAI() && getFM().getOverload() > maxLaunchG)
            {
                targetPoint3d = null;
                targetPoint3dAbs = null;
                laserOwner = null;
                return;
            }
            if((getOwner() instanceof TypeGuidedMissileCarrier) && (getOwner() instanceof TypeLaserDesignator) && ((TypeLaserDesignator)getOwner()).getLaserOn())
            {
                targetPoint3dAbs = ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().getMissileTargetPos();
                laserOwner = ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().getMissileTargetPosOwner();
                victimOffsetPoint3f = ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().getMissileTargetOffset();
                if(targetPoint3dAbs == null || laserOwner == null || victimOffsetPoint3f == null)
                    return;
                safeVictimOffset.set(victimOffsetPoint3f);
                targetPoint3d.set(targetPoint3dAbs);
                targetPoint3d.sub(missilePoint3d);
            }
            return;
        }
        if(bSACLOSHoming)
        {
            if(!ownerIsAI() && getFM().getOverload() > maxLaunchG)
            {
                targetPoint3d = null;
                targetPoint3dAbs = null;
                saclosOwner = null;
                return;
            }
            if((getOwner() instanceof TypeGuidedMissileCarrier) && (getOwner() instanceof TypeSACLOS) && ((TypeSACLOS)getOwner()).getSACLOSenabled())
            {
                targetPoint3dAbs = ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().getMissileTargetPos();
                saclosOwner = ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().getMissileTargetPosOwner();
                victimOffsetPoint3f = ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().getMissileTargetOffset();
                if(targetPoint3dAbs == null || saclosOwner == null || victimOffsetPoint3f == null)
                    return;
                safeVictimOffset.set(victimOffsetPoint3f);
                targetPoint3d.set(targetPoint3dAbs);
                targetPoint3d.sub(missilePoint3d);
            }
            return;
        }
        if(ownerIsAI())
        {
            if(getOwner() instanceof TypeGuidedMissileCarrier)
            {
                victim = ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().getMissileTarget();
                if(GuidedMissileUtils.distanceBetween(getOwner(), victim) < 1500D && getOwner().getArmy() == victim.getArmy())
                {
                    victim = null;
                } else
                {
                    lockingVictim = true;
                    victimOffsetPoint3f = ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().getMissileTargetOffset();
                    safeVictimOffset.set(victimOffsetPoint3f);
                }
            } else
            if((getOwner() instanceof TypeFighter) && getFM() != null)
            {
                victim = ((Pilot)getFM()).target.actor;
                lockingVictim = true;
            }
            //break;
        }
        if(getFM() != null && getFM().getOverload() > maxLaunchG)
        {
            victim = null;
            return;
        }
        try
        {
            if(getOwner() instanceof TypeGuidedMissileCarrier)
            {
                victim = ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().getMissileTarget();
                lockingVictim = true;
                safeVictimOffset.set(victimOffsetPoint3f);
            } else
            {
                victim = Selector.look(true, false, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], World.getPlayerAircraft().getArmy(), -1, World.getPlayerAircraft(), false);
                if(victim == null)
                {
                    victim = Main3D.cur3D().getViewPadlockEnemy();
                    lockingVictim = true;
                }
            }
        }
        catch(Exception exception) { }
    }

    private void setSmokeSpriteFlames(int i)
    {
        if(exhausts > 1)
        {
            if(smoke != null)
                createAdditionalSmokes(i);
            if(sprite != null)
                createAdditionalSprites(i);
            if(flame != null)
                createAdditionalFlames(i);
        }
    }

    public void setStartTime()
    {
        if(startTimeIsSet)
        {
            return;
        } else
        {
            startTime = Time.current();
            startTimeIsSet = true;
            return;
        }
    }

    public void start(float f)
    {
        start(f, 0);
    }

    public void start(float f, int i)
    {
        Actor actor = pos.base();
        try{
        if(Actor.isValid(actor))
        {
        if(actor.isNetMirror())
        {
            destroy();
            return;
        }
        
            net = new Master(this);
        }}
        catch(Exception exception)
        {
            GuidedMissileUtils.LocalLog(getOwner(), "Missile launch cancelled (system error):" + exception.getMessage());
            destroy();
        }
        doStart(f);
        return;
    }

    public void startEngine()
    {
        if(engineRunning)
            return;
        Class class1 = getClass();
        com.maddox.il2.engine.Hook hook = null;
        String s = effSprite;
        if(s != null)
        {
            if(hook == null)
                hook = findHook("_SMOKE");
            sprite = Eff3DActor.New(this, hook, null, 1.0F, s, -1F);
            if(sprite != null)
                sprite.pos.changeHookToRel();
        }
        s = simFlame;
        if(s != null)
        {
            if(hook == null)
                hook = findHook("_SMOKE");
            flame = new ActorSimpleMesh(s);
            if(flame != null)
            {
                ((ActorSimpleMesh)flame).mesh().setScale(1.0F);
                flame.pos.setBase(this, hook, false);
                flame.pos.changeHookToRel();
                flame.pos.resetAsBase();
                if(Config.isUSE_RENDER())
                    flame.drawing(true);
            }
            bRocketFiring = true;
        }
        s = effSmoke;
        if(s != null)
        {
            if(hook == null)
                hook = findHook("_SMOKE");
            smoke = Eff3DActor.New(this, hook, null, 1.0F, s, -1F);
            if(smoke != null)
                smoke.pos.changeHookToRel();
        }
        setSmokeSpriteFlames(0);
        soundName = Property.stringValue(class1, "sound", null);
        if(soundName != null)
        {
            newSound(soundName, true);
            playingMotorSound = true;
        }
        engineRunning = true;
    }

    public void startEngineDone()
    {
        releaseTime = Time.current();
        if(this instanceof MissileInterceptable)
        {
            Engine.targets().add(this);
            setName(getOwner().name() + "_" + ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().getMissileName() + hashCode());
        }
        Engine.missiles().add(this);
    }

    public void sustainEngine()
    {
        Class class1 = getClass();
        com.maddox.il2.engine.Hook hook = null;
        String s = effSpriteSustain;
        if(s != null)
        {
            if(hook == null)
                hook = findHook("_SMOKE");
            sprite = Eff3DActor.New(this, hook, null, 1.0F, s, -1F);
            if(sprite != null)
            {
                sprite.pos.changeHookToRel();
                spriteActive = true;
            }
        }
        s = simFlameSustain;
        if(s != null)
        {
            if(hook == null)
                hook = findHook("_SMOKE");
            flame = new ActorSimpleMesh(s);
            if(flame != null)
            {
                ((ActorSimpleMesh)flame).mesh().setScale(1.0F);
                flame.pos.setBase(this, hook, false);
                flame.pos.changeHookToRel();
                flame.pos.resetAsBase();
                flameActive = true;
                if(Config.isUSE_RENDER())
                    flame.drawing(true);
            }
            bRocketFiring = true;
        }
        s = effSmokeSustain;
        if(s != null)
        {
            if(hook == null)
                hook = findHook("_SMOKE");
            smoke = Eff3DActor.New(this, hook, null, 1.0F, s, -1F);
            if(smoke != null)
            {
                smoke.pos.changeHookToRel();
                smokeActive = true;
            }
        }
        setSmokeSpriteFlames(1);
    }

    public void afterFireEngineTrail()
    {
        Class class1 = getClass();
        com.maddox.il2.engine.Hook hook = null;
        String s = effSmokeTrail;
        if(s != null)
        {
            if(hook == null)
                hook = findHook("_SMOKE");
            smoke = Eff3DActor.New(this, hook, null, 1.0F, s, -1F);
            if(smoke != null)
            {
                smoke.pos.changeHookToRel();
                smokeActive = true;
            }
        }
        setSmokeSpriteFlames(2);
    }

    public void startMissile(float f, int i)
    {
        Class class1 = getClass();
        float f1 = Property.floatValue(class1, "kalibr", 0.082F);
        if(f <= 0.0F)
            f = Property.floatValue(class1, "timeLife", 45F);
        float f2 = -1F + 2.0F * TrueRandom.nextFloat();
        f2 *= f2 * f2;
        float f3 = -1F + 2.0F * TrueRandom.nextFloat();
        f3 *= f3 * f3;
        init(f1, Property.floatValue(class1, "massa", 6.8F), Property.floatValue(class1, "massaEnd", 2.52F), Property.floatValue(class1, "timeFire", 4F) / (1.0F + 0.1F * f2), Property.floatValue(class1, "force", 500F) * (1.0F + 0.1F * f2), f + f3 * 0.1F);
        setOwner(pos.base(), false, false, false);
        pos.setBase(null, null, true);
        pos.setAbs(pos.getCurrent());
        pos.getAbs(Aircraft.tmpOr);
        float f4 = 0.68F * Property.floatValue(class1, "maxDeltaAngle", 3F);
        f2 = -1F + 2.0F * TrueRandom.nextFloat();
        f3 = -1F + 2.0F * TrueRandom.nextFloat();
        f2 *= f2 * f2 * f4;
        f3 *= f3 * f3 * f4;
        Aircraft.tmpOr.increment(f2, f3, 0.0F);
        pos.setAbs(Aircraft.tmpOr);
        pos.getRelOrient().transformInv(speed);
        speed.z /= 3D;
        speed.x += 200D;
        pos.getRelOrient().transform(speed);
        collide(true);
        interpPut(new Rocket.Interpolater(), null, Time.current(), null);
        if(getOwner() == World.getPlayerAircraft())
            World.cur().scoreCounter.rocketsFire++;
        if(!Config.isUSE_RENDER())
        {
            if(engineDelayTime <= 0L)
                bRocketFiring = true;
            return;
        }
        if(engineDelayTime <= 0L)
            startEngine();
        light = new LightPointActor(new LightPointWorld(), new Point3d());
        light.light.setColor((Color3f)Property.value(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F)));
        light.light.setEmit(Property.floatValue(class1, "emitMax", 1.0F), Property.floatValue(class1, "emitLen", 50F));
        draw.lightMap().put("light", light);
    }

    public boolean stepBeamRider()
    {
        float f = computeMissileAccelleration();
        if(f == -1F)
            return false;
        Actor actor = getOwner();
        if(victim != null && GuidedMissileUtils.angleBetween(actor, victim) > maxFOVfrom)
            victim = null;
        if(Time.current() < startTime + trackDelay)
            computeNoTrackPath();
        else
        if(victim != null && actor != null)
        {
            float f1 = 0.0F;
            float f2 = 0.0F;
            Point3d point3d = new Point3d();
            point3d.set(actor.pos.getAbsPoint());
            Orient orient = new Orient();
            orient.set(actor.pos.getAbsOrient());
            Point3d point3d1 = new Point3d();
            point3d1.set(victim.pos.getAbsPoint());
            Vector3d vector3d = new Vector3d();
            Orient orient1 = new Orient();
            orient1.set(victim.pos.getAbsOrient());
            vector3d.set(victimOffsetPoint3f);
            orient1.transform(vector3d);
            point3d1.add(vector3d);
            Point3d point3d2 = new Point3d();
            point3d2.set(pos.getAbsPoint());
            Point3d point3d3 = new Point3d();
            point3d3.set(point3d2);
            Orient orient2 = new Orient();
            orient2.set(pos.getAbsOrient());
            Point3d point3d4 = new Point3d();
            point3d4.set(point3d);
            point3d1.sub(point3d);
            orient.transformInv(point3d1);
            float f3 = (float)Math.toDegrees(Math.atan(-point3d1.y / point3d1.x));
            float f4 = (float)Math.toDegrees(Math.atan(point3d1.z / point3d1.x));
            if(getFailState() == 4)
            {
                f3 += 180F;
                f4 += 180F;
                if(f3 > 180F)
                    f3 = 180F - f3;
                if(f4 > 180F)
                    f4 = 180F - f4;
            }
            point3d2.sub(point3d);
            orient.transformInv(point3d2);
            float f5 = (float)Math.toDegrees(Math.atan(-point3d2.y / point3d2.x));
            float f6 = (float)Math.toDegrees(Math.atan(point3d2.z / point3d2.x));
            float f7 = f5 - f3;
            float f8 = f6 - f4;
            point3d4.sub(point3d3);
            orient2.transformInv(point3d4);
            float f9 = (float)Math.toDegrees(Math.atan(-point3d4.y / point3d4.x));
            float f10 = (float)Math.toDegrees(Math.atan(point3d4.z / point3d4.x));
            float f11 = f7 - f9;
            float f12 = f8 - f10;
            float f13 = -5F;
            float f14 = 60F;
            float f15 = 3F;
            float f16 = 1.0F;
            float f17 = 1.5F;
            float f18 = 2.0F;
            if(f7 < 0.0F)
            {
                if(f11 < 0.0F)
                    f1 = f18;
                else
                if(f11 > f14)
                    f1 = -f16;
                else
                if(f11 > f15 && f11 > f13 * f7)
                    f1 = -f17;
                else
                    f1 = f16;
            } else
            if(f11 > 0.0F)
                f1 = -f18;
            else
            if(f11 < -f14)
                f1 = f16;
            else
            if(f11 < -f15 && f11 < f13 * f7)
                f1 = f17;
            else
                f1 = -f16;
            if(f8 < 0.0F)
            {
                if(f12 < 0.0F)
                    f2 = f18;
                else
                if(f12 > f14)
                    f2 = -f16;
                else
                if(f12 > f15 && f12 > f13 * f8)
                    f2 = -f17;
                else
                    f2 = f16;
            } else
            if(f12 > 0.0F)
                f2 = -f18;
            else
            if(f12 < -f14)
                f2 = f16;
            else
            if(f12 < -f15 && f12 < f13 * f8)
                f2 = f17;
            else
                f2 = -f16;
            computeMissilePath(f, f1, f2, f3, f4);
        }
        return computeFuzeState();
    }

    public boolean stepTargetHoming()
    {
        float f = computeMissileAccelleration();
        if(f == -1F)
            return false;
        if(Time.current() < startTime + trackDelay)
            computeNoTrackPath();
        else
        if(victim != null && !lockingVictim)
            computeNoTrackPath();
        else
        if(victim != null)
        {
            checkChaffFlareLock();
            victim.pos.getAbs(targetPoint3d, victimOffsetOrient);
            if((iDetectorType == 1 || iDetectorType == 7 || iDetectorType == 8) && Main.cur().clouds != null && Main.cur().clouds.getVisibility(victim.pos.getAbsPoint(), pos.getAbsPoint()) < 0.4F)
            {
                computeNoTrackPath();
                return computeFuzeState();
            }
            victim.getSpeed(victimSpeed);
            double d = GuidedMissileUtils.distanceBetween(this, victim);
            double d6 = victimSpeed.length();
            if(d6 > 10D)
            {
                double d7 = (double)f / d6;
                double d8 = GuidedMissileUtils.angleActorBetween(victim, this);
                double d9 = Geom.RAD2DEG((float)Math.asin(Math.sin(Geom.DEG2RAD((float)d8)) / d7));
                double d10 = 180D - d8 - d9;
                double d11 = (d * Math.sin(Geom.DEG2RAD((float)d9))) / Math.sin(Geom.DEG2RAD((float)d10));
                d11 -= 5D;
                double d12 = d11 / d6;
                victimSpeed.scale(d12 * (double)(leadPercent / 100F));
                targetPoint3d.add(victimSpeed);
            }
            Orient orient = new Orient();
            orient.set(victim.pos.getAbsOrient());
            trajectoryVector3d.set(victimOffsetPoint3f);
            orient.transform(trajectoryVector3d);
            targetPoint3d.add(trajectoryVector3d);
            targetPoint3d.sub(missilePoint3d);
            missileOrient.transformInv(targetPoint3d);
            float f11 = (float)Math.toDegrees(Math.atan(targetPoint3d.y / targetPoint3d.x));
            float f12 = (float)Math.toDegrees(Math.atan(targetPoint3d.z / targetPoint3d.x));
            if(getFailState() == 4)
            {
                f11 += 180F;
                f12 += 180F;
                if(f11 > 180F)
                    f11 = 180F - f11;
                if(f12 > 180F)
                    f12 = 180F - f12;
            }
            computeMissilePath(f, 0.0F, 0.0F, f11, f12);
        } else
        if(bLaserHoming)
        {
            double d1 = 0.0D;
            float f1 = 0.0F;
            float f6 = 0.0F;
            float f9 = 0.0F;
            if(laserOwner == null)
            {
                if((getOwner() instanceof TypeLaserDesignator) && ((TypeLaserDesignator)getOwner()).getLaserOn())
                {
                    Point3d point3d = new Point3d();
                    point3d.set(((TypeLaserDesignator)getOwner()).getLaserSpot());
                    if(point3d != null && (Main.cur().clouds == null || Main.cur().clouds.getVisibility(point3d, pos.getAbsPoint()) >= 1.0F && Main.cur().clouds.getVisibility(point3d, getOwner().pos.getAbsPoint()) >= 1.0F))
                    {
                        double d2 = pos.getAbsPoint().distance(point3d);
                        if(d2 <= (double)attackMaxDistance)
                        {
                            float f2 = GuidedMissileUtils.angleBetween(this, point3d);
                            if(f2 <= maxFOVfrom)
                                laserOwner = getOwner();
                        }
                    }
                }
                if(laserOwner == null)
                {
                    List list = Engine.targets();
                    int i = list.size();
                    for(int j = 0; j < i; j++)
                    {
                        Actor actor = (Actor)list.get(j);
                        if(!(actor instanceof TypeLaserDesignator) || !((TypeLaserDesignator)actor).getLaserOn() || actor.getArmy() != getOwner().getArmy())
                            continue;
                        Point3d point3d4 = new Point3d();
                        point3d4 = ((TypeLaserDesignator)actor).getLaserSpot();
                        if(point3d4 == null)
                            break;
                        if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d4, pos.getAbsPoint()) < 1.0F || Main.cur().clouds.getVisibility(point3d4, actor.pos.getAbsPoint()) < 1.0F)
                            continue;
                        double d3 = actor.pos.getAbsPoint().distance(point3d4);
                        if(d3 > (double)attackMaxDistance)
                            continue;
                        float f3 = GuidedMissileUtils.angleBetween(actor, point3d4);
                        if(f3 > maxFOVfrom)
                            continue;
                        float f7 = 1.0F / f3 / (float)(d3 * d3);
                        if(f7 > f9)
                        {
                            f9 = f7;
                            laserOwner = actor;
                        }
                    }

                }
            }
            if(laserOwner != null && ((TypeLaserDesignator)laserOwner).getLaserOn())
            {
                Point3d point3d1 = new Point3d();
                point3d1.set(((TypeLaserDesignator)laserOwner).getLaserSpot());
                if(point3d1 != null)
                {
                    targetPoint3d.set(point3d1);
                    targetPoint3d.sub(missilePoint3d);
                    missileOrient.transformInv(targetPoint3d);
                    float f13 = (float)Math.toDegrees(Math.atan(targetPoint3d.y / targetPoint3d.x));
                    float f15 = (float)Math.toDegrees(Math.atan(targetPoint3d.z / targetPoint3d.x));
                    if(getFailState() == 4)
                    {
                        f13 += 180F;
                        f15 += 180F;
                        if(f13 > 180F)
                            f13 = 180F - f13;
                        if(f15 > 180F)
                            f15 = 180F - f15;
                    }
                    if(Main.cur().clouds == null || Main.cur().clouds.getVisibility(targetPoint3d, pos.getAbsPoint()) >= 0.99F && Main.cur().clouds.getVisibility(targetPoint3d, laserOwner.pos.getAbsPoint()) >= 0.99F)
                        computeMissilePath(f, 0.0F, 0.0F, f13, f15);
                }
            }
        } else
        if(bSACLOSHoming)
        {
            double d4 = 0.0D;
            float f4 = 0.0F;
            float f8 = 0.0F;
            float f10 = 0.0F;
            if(saclosOwner == null && (getOwner() instanceof TypeSACLOS) && ((TypeSACLOS)getOwner()).getSACLOSenabled())
            {
                Point3d point3d2 = new Point3d();
                point3d2.set(((TypeSACLOS)getOwner()).getSACLOStarget());
                if(point3d2 != null && (Main.cur().clouds == null || Main.cur().clouds.getVisibility(point3d2, pos.getAbsPoint()) >= 1.0F && Main.cur().clouds.getVisibility(point3d2, getOwner().pos.getAbsPoint()) >= 1.0F))
                {
                    double d5 = pos.getAbsPoint().distance(point3d2);
                    if(d5 <= (double)attackMaxDistance)
                    {
                        float f5 = GuidedMissileUtils.angleBetween(this, point3d2);
                        if(f5 <= maxFOVfrom)
                            saclosOwner = getOwner();
                    }
                }
            }
            if(saclosOwner != null && ((TypeSACLOS)saclosOwner).getSACLOSenabled())
            {
                Point3d point3d3 = new Point3d();
                point3d3.set(((TypeSACLOS)saclosOwner).getSACLOStarget());
                if(point3d3 != null)
                {
                    targetPoint3d.set(point3d3);
                    targetPoint3d.sub(missilePoint3d);
                    missileOrient.transformInv(targetPoint3d);
                    float f14 = (float)Math.toDegrees(Math.atan(targetPoint3d.y / targetPoint3d.x));
                    float f16 = (float)Math.toDegrees(Math.atan(targetPoint3d.z / targetPoint3d.x));
                    if(getFailState() == 4)
                    {
                        f14 += 180F;
                        f16 += 180F;
                        if(f14 > 180F)
                            f14 = 180F - f14;
                        if(f16 > 180F)
                            f16 = 180F - f16;
                    }
                    if(Main.cur().clouds == null || Main.cur().clouds.getVisibility(saclosOwner.pos.getAbsPoint(), pos.getAbsPoint()) >= 0.99F && Main.cur().clouds.getVisibility(targetPoint3d, saclosOwner.pos.getAbsPoint()) >= 0.99F)
                        computeMissilePath(f, 0.0F, 0.0F, f14, f16);
                }
            }
        }
        return computeFuzeState();
    }

    public int WeaponsMask()
    {
        return -1;
    }

    private void checkChaffFlareLock()
    {
        if((victim instanceof RocketFlare) || (victim instanceof RocketChaff))
            return;
        List list = null;
        try
        {
            list = Engine.countermeasures();
        }
        catch(Exception exception) { }
        if(list == null)
            return;
        int i = TrueRandom.nextInt((int)flareLockTime + 1000);
        double d = 0.0D;
        int j = list.size();
        for(int k = 0; k < j; k++)
        {
            Actor actor = (Actor)list.get(k);
            double d1 = GuidedMissileUtils.distanceBetween(this, actor);
            if(iDetectorType == 1 && (actor instanceof RocketFlare) && flareLockTime < (long)i && d1 < 200D && GuidedMissileUtils.angleActorBetween(victim, this) > 30F)
            {
                victim = actor;
                continue;
            }
            if((iDetectorType == 2 || iDetectorType == 3 || iDetectorType == 4) && (actor instanceof RocketChaff) && flareLockTime < (long)i && d1 < 500D && GuidedMissileUtils.angleActorBetween(victim, this) > 30F)
            {
                victim = actor;
                continue;
            }
            if(iDetectorType == 8 && (actor instanceof RocketFlare) && flareLockTime < (long)i && d1 < 500D && GuidedMissileUtils.angleActorBetween(victim, this) > 30F && TrueRandom.nextFloat() < 0.1F)
                victim = actor;
        }

    }

    private static final void InitTablesOfEnergyToKill()
    {
        PenetrateEnergyToKill = new float[4][][];
        PenetrateThickness = new float[4][];
        PenetrateThickness[0] = (new float[] {
            0.025F, 0.15F
        });
        PenetrateEnergyToKill[0] = (new float[][] {
            new float[] {
                23400F, 131400F, 252000F
            }, new float[] {
                131400F, 369000F, 1224000F
            }
        });
        PenetrateThickness[1] = (new float[] {
            0.12F, 0.24F
        });
        PenetrateEnergyToKill[1] = (new float[][] {
            new float[] {
                131400F, 252000F, 3295500F
            }, new float[] {
                369000F, 3295500F, 5120000F
            }
        });
        PenetrateThickness[2] = (new float[] {
            0.002F, 0.008F
        });
        PenetrateEnergyToKill[2] = (new float[][] {
            new float[] {
                511.225F, 2453.88F, 23400F
            }, new float[] {
                2453.88F, 10140F, 23400F
            }
        });
        PenetrateThickness[3] = (new float[] {
            0.025F, 0.15F
        });
        PenetrateEnergyToKill[3] = (new float[][] {
            new float[] {
                23400F, 131400F, 252000F
            }, new float[] {
                131400F, 369000F, 1224000F
            }
        });
        for(int i = 0; i < 4; i++)
        {
            if(Math.abs(PenetrateThickness[i][0] - PenetrateThickness[i][1]) < 0.001F)
                internalerrror(1);
            for(int j = 0; j <= 1; j++)
            {
                float af[] = PenetrateEnergyToKill[i][j];
                if(af[1] - af[0] < 0.001F || af[2] - af[1] < 0.001F)
                    internalerrror(2);
            }

            float f = Math.min(PenetrateEnergyToKill[i][0][0], PenetrateEnergyToKill[i][1][0]);
            float f1 = Math.max(PenetrateEnergyToKill[i][0][2], PenetrateEnergyToKill[i][1][2]);
            for(int k = 0; k <= 100; k++)
            {
                float f2 = f + ((f1 - f) * (float)k) / 100F;
                float f3 = ComputeProbabOfPenetrateKill(i, 0, f2);
                float f4 = ComputeProbabOfPenetrateKill(i, 1, f2);
                if(f3 < f4)
                {
                    System.out.println(i + " i,e0,e1,e:" + k + " " + f + " " + f1 + " " + f2 + " prob0,1: " + f3 + " " + f4);
                    internalerrror(3);
                }
            }

        }

    }

    private static final float ComputeProbabOfPenetrateKill(int i, int j, float f)
    {
        float af[] = PenetrateEnergyToKill[i][j];
        float f1;
        float f2;
        if(f < af[1])
        {
            if(f < af[0])
                return 0.0F;
            f1 = 0.2F / (af[1] - af[0]);
            f2 = 0.1F - af[0] * f1;
            if(bLogDetailDamage)
                System.out.println("ComputeProbabOfPenetrateKill(i=" + i + ", j=" + j + ", f=" + f + ") - if (f < af[1]) - f1=" + f1 + ", f2=" + f2);
        } else
        {
            if(f >= af[2])
                return 1.0F;
            f1 = 0.7F / (af[2] - af[1]);
            f2 = 0.3F - af[1] * f1;
            if(bLogDetailDamage)
                System.out.println("ComputeProbabOfPenetrateKill(i=" + i + ", j=" + j + ", f=" + f + ") - if (f => af[1]) - f1=" + f1 + ", f2=" + f2);
        }
        if(bLogDetailDamage)
            System.out.println("ComputeProbabOfPenetrateKill(i=" + i + ", j=" + j + ", f=" + f + ") - return=" + (f * f1 + f2));
        return f * f1 + f2;
    }

    private final float ComputeProbabOfPenetrateKill(float f, int i)
    {
        if(i <= 0)
            return 0.0F;
        float f1 = ComputeProbabOfPenetrateKill(damage_MAT_TYPE, 0, f);
        float f2 = ComputeProbabOfPenetrateKill(damage_MAT_TYPE, 1, f);
        float af[] = PenetrateThickness[damage_MAT_TYPE];
        float f3 = (f2 - f1) / (af[1] - af[0]);
        float f4 = f1 - af[0] * f3;
        float f5 = damage_PANZER * f3 + f4;
        if(f5 < 0.1F)
            f5 = 0.0F;
        else
        if(f5 >= 1.0F)
            f5 = 1.0F;
        else
        if(i > 1)
            f5 = 1.0F - (float)Math.pow(1.0F - f5, i);
        if(bLogDetailDamage)
            System.out.println("ComputeProbabOfPenetrateKill(f=" + f + " ,i=" + i + ") - f1=" + f1 + ", f2=" + f2 + ", f3=" + f3 + ", f4=" + f4 + ", return=f5=" + f5);
        return f5;
    }

    public void msgShot(Shot shot)
    {
        if(bLogDetailDamage)
            System.out.println("Missile(" + actorString(this) + ") - msgShot(){} - shot.power=" + shot.power);
        shot.bodyMaterial = damage_EFF_BODY_MATERIAL;
        if(!isAlive())
            return;
        if(shot.power <= 0.0F)
            return;
        if(damage_MAT_TYPE == 3)
        {
            if(shot.powerType == 1)
            {
                die(shot.initiator, true);
                return;
            }
            if(shot.v.length() < 20D)
            {
                return;
            } else
            {
                die(shot.initiator, true);
                return;
            }
        }
        if(shot.powerType == 1)
        {
            if(damage_MAT_TYPE != 2)
                return;
            float f = shot.power * RndF(0.75F, 1.15F);
            float f2 = 0.256F * (float)Math.sqrt(Math.sqrt(f));
            if(damage_PANZER > f2)
            {
                return;
            } else
            {
                die(shot.initiator, true);
                return;
            }
        }
        float f1 = ComputeProbabOfPenetrateKill(shot.power, 1);
        if(!RndB(f1))
        {
            return;
        } else
        {
            die(shot.initiator, true);
            return;
        }
    }

    public void msgExplosion(Explosion explosion)
    {
        if(bLogDetailDamage || bLogDetail)
            System.out.println("Missile(" + actorString(this) + ") - msgExplosion(){} - explosion.power=" + explosion.power + " - receivedPower=" + explosion.receivedPower(this) + " - powerType=" + explosion.powerType);
        if(!isAlive())
            return;
        if(explosion.power <= 0.0F)
            return;
        if(damage_MAT_TYPE == 3)
        {
            if(Explosion.killable(this, explosion.receivedTNT_1meter(this), 0.005F, 0.1F, 0.0F))
                die(explosion.initiator, true);
            return;
        }
        Explosion explosion1 = explosion;
        if(explosion.powerType == 1)
        {
            float af[] = new float[6];
            mesh().getBoundBox(af);
            pos.getAbs(ppp);
            ppp.x = (ppp.x - (double)af[0]) + (double)(af[3] - af[0]);
            ppp.y = (ppp.y - (double)af[1]) + (double)(af[4] - af[1]);
            ppp.z = (ppp.z - (double)af[2]) + (double)(af[5] - af[2]);
            float af1[] = new float[2];
            explosion.computeSplintersHit(ppp, mesh().collisionR(), 0.7F, af1);
            float f = 0.015F * af1[1] * af1[1] * 0.5F;
            float f1 = ComputeProbabOfPenetrateKill(f, (int)(af1[0] + 0.5F));
            if(bLogDetailDamage || bLogDetail)
                System.out.println("msgExplosion(){} - if(powerType == 1) - af1[0]=" + af1[0] + " , af1[1]=" + af1[1] + " , ProbabOfKill=" + f1);
            if(RndB(f1))
                die(explosion.initiator, true);
            return;
        }
        if(explosion.powerType == 0)
        {
            if(Explosion.killable(this, explosion.receivedPower(this), damage_MIN_TNT, damage_MAX_TNT, damage_PROBAB_DEATH_WHEN_EXPLOSION))
                die(explosion.initiator, true);
            return;
        }
        if(damage_MAT_TYPE == 1)
        {
            return;
        } else
        {
            die(explosion.initiator, true);
            return;
        }
    }

    private void die(Actor actor, boolean flag)
    {
        if(bLogDetailDamage)
            System.out.println("Missile(" + actorString(this) + ") - die(actor=" + actorString(actor) + ", flag=" + flag + "){}");
        if(!isAlive())
            return;
        timeToFailure = 1000L;
        if(RndB(0.04F))
            failState = 7;
        else
        if((float)(Time.current() - startTime) < rocketMotorOperationTime + rocketMotorSustainedOperationTime)
        {
            if(RndB(0.25F))
                failState = 3;
            else
            if(failState == 0)
                if(RndB(0.7F))
                    failState = 2;
                else
                    failState = 6;
        } else
        if(failState == 0 || failState == 3)
            if(RndB(0.7F))
                failState = 2;
            else
                failState = 6;
        if(mshDamage != null)
            setMesh(MeshShared.get(mshDamage));
        if(bLogDetailDamage)
            System.out.println("Missile(" + actorString(this) + ") - die() - new failState=" + failState);
        if(bLogDetailDamage)
            System.out.println("die() - old Yaw=" + missileOrient.getYaw() + " , old Pitch=" + missileOrient.getPitch());
        missileOrient.setYPR(missileOrient.getYaw() + RndF(-5F, 5F), missileOrient.getPitch() + RndF(-5F, 5F), getRoll());
        if(bLogDetailDamage)
            System.out.println("die() - new Yaw=" + missileOrient.getYaw() + " , new Pitch=" + missileOrient.getPitch());
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        if(bLogDetail)
            System.out.println("Missile(" + actorString(this) + ") - entering msgCollisionRequest(actor, aflag[])");
        super.msgCollisionRequest(actor, aflag);
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(bLogDetail)
            System.out.println("Missile(" + actorString(this) + ") - entering msgCollision(actor, s, s1)");
        super.msgCollision(actor, s, s1);
    }

    private static final boolean RndB(float f)
    {
        return TrueRandom.nextFloat(0.0F, 1.0F) < f;
    }

    private static final float RndF(float f, float f1)
    {
        return TrueRandom.nextFloat(f, f1);
    }

    private static final void internalerrror(int i)
    {
        System.out.println("*** Internal error #" + i + " in Missile damage database ***");
        throw new RuntimeException("Can't initialize Missile");
    }

    private static String actorString(Actor actor)
    {
        if(!Actor.isValid(actor))
            return "(InvalidActor)";
        String s;
        try
        {
            s = actor.getClass().getName();
        }
        catch(Exception exception)
        {
            System.out.println("Missile - actorString(): Cannot resolve class name of " + actor);
            return "(NoClassnameActor)";
        }
        int i = s.lastIndexOf('.');
        String s1 = s.substring(i + 1);
        s1 = s1 + '@' + Integer.toHexString(actor.hashCode());
        return s1;
    }

    protected static final int DETECTOR_TYPE_MANUAL = 0;
    protected static final int DETECTOR_TYPE_INFRARED = 1;
    protected static final int DETECTOR_TYPE_RADAR_HOMING = 2;
    protected static final int DETECTOR_TYPE_RADAR_BEAMRIDING = 3;
    protected static final int DETECTOR_TYPE_RADAR_TRACK_VIA_MISSILE = 4;
    protected static final int DETECTOR_TYPE_ANTI_RADIATION = 5;
    protected static final int DETECTOR_TYPE_ANTI_RADIATION_LOCREC = 6;
    protected static final int DETECTOR_TYPE_IMAGE_EOTV = 7;
    protected static final int DETECTOR_TYPE_IMAGE_IR = 8;
    protected static final int DETECTOR_TYPE_LASER = 9;
    protected static final int DETECTOR_TYPE_SACLOS = 10;
    protected static final int FAIL_TYPE_CONTROL_BLOCKED = 6;
    protected static final int FAIL_TYPE_ELECTRONICS = 1;
    protected static final int FAIL_TYPE_ENGINE = 3;
    protected static final int FAIL_TYPE_IVAN = 5;
    protected static final int FAIL_TYPE_MIRROR = 2;
    protected static final int FAIL_TYPE_NONE = 0;
    protected static final int FAIL_TYPE_NUMBER = 7;
    protected static final int FAIL_TYPE_REFLEX = 4;
    protected static final int FAIL_TYPE_WARHEAD = 7;
    private static final float IVAN_TIME_MAX = 2F;
    private static final float IVAN_TIME_MIN = 1F;
    protected static final int LAUNCH_TYPE_DROP = 2;
    protected static final int LAUNCH_TYPE_QUICK = 1;
    protected static final int LAUNCH_TYPE_STRAIGHT = 0;
    protected static final int STEP_MODE_BEAMRIDER = 1;
    protected static final int STEP_MODE_HOMING = 0;
    protected static final int TARGET_AIR = 1;
    protected static final int TARGET_GROUND = 16;
    protected static final int TARGET_LOCATE = 32;
    protected static final int TARGET_SHIP = 256;
    protected static final int ATTACK_DECISION_BY_AI_NO = 0;
    protected static final int ATTACK_DECISION_BY_AI_YES = 1;
    protected static final int ATTACK_DECISION_BY_AI_WAYPOINT = 2;
    private float deltaAzimuth;
    private float deltaTangage;
    private float dragCoefficient;
    private float dragCoefficientTurn;
    private Orient dropFlightPathOrient;
    private String effSmoke;
    private String effSprite;
    private boolean endedFlame;
    private boolean endedSmoke;
    private boolean endedSprite;
    private long engineDelayTime;
    private boolean engineRunning;
    private int exhausts;
    private int failState;
    private MissileNavLight firstNavLight;
    private boolean flameActive;
    private Actor flames[];
    private long flareLockTime;
    private float frontSquare;
    private float groundTrackFactor;
    private float initialMissileForce;
    private float ivanTimeLeft;
    private MissileNavLight lastNavLight;
    private long lastTimeNoFlare;
    private double launchKren;
    private double launchPitch;
    private int launchType;
    private double launchYaw;
    private float leadPercent;
    private float massaEnd;
    private float massLossPerTick;
    private float maxFOVfrom;
	private float maxFOVfrom_real = 0.0F;
    private float maxG;
    private float maxLaunchG;
    private float missileBaseSpeed;
    private float missileForce;
    private float missileForceRunUpTime;
    private float missileKalibr;
    private float missileMass;
    private Orient missileOrient;
    private Point3d missilePoint3d;
    private float missileSustainedForce;
    private float missileTimeLife;
    private ActiveMissile myActiveMissile;
    private float oldDeltaAzimuth;
    private float oldDeltaTangage;
    private float oldRoll;
    private float previousDistance;
    private long releaseTime;
    private float rocketMotorOperationTime;
    private float rocketMotorSustainedOperationTime;
    private Vector3d safeVictimOffset;
    private String simFlame;
    private boolean smokeActive;
    private Eff3DActor smokes[];
    private boolean spriteActive;
    private Eff3DActor sprites[];
    private boolean noSmokeFlameSustain;
    private boolean playingMotorSound;
    private boolean noSoundSustain;
    private String soundNameSustain;
    private boolean playingSustainMotorSound;
    private String effSmokeSustain;
    private String effSpriteSustain;
    private String simFlameSustain;
    private boolean showingSustainSmokeFlame;
    private String effSmokeTrail;
    private boolean showingTrailSmoke;
    private String mshFly;
    private boolean waitingMeshFly;
    private String mshSustain;
    private boolean waitingMeshSustain;
    private String mshDamage;
    private long startTime;
    private boolean startTimeIsSet;
    private int iDetectorType;
    private long lTargetType;
    private int stepMode;
    private float stepsForFullTurn;
    private float sunRayAngle;
    private Point3d targetPoint3d;
    private Point3d targetPoint3dAbs;
    private long timeToFailure;
    private long trackDelay;
    private Vector3d trajectoryVector3d;
    private float turnDiffMax;
    private float attackMaxDistance;
    private float missileProximityFuzeRadius;
    private String soundNameRadarPW;
    DecimalFormat twoPlaces;
    private Actor victim;
    private boolean lockingVictim;
    private Orient victimOffsetOrient;
    private Point3f victimOffsetPoint3f;
    private Vector3d victimSpeed;
    private boolean bRocketFiring;
    private boolean bLaserHoming;
    private Actor laserOwner;
    private boolean bSACLOSHoming;
    private Actor saclosOwner;
    private boolean bRealisticRadarSelect;
    private static final int MAT_WOOD = 0;
    private static final int MAT_BRICK = 1;
    private static final int MAT_STEEL = 2;
    private static final int MAT_FLESH = 3;
    private static final int N_MAT_TYPES = 4;
    private static float PenetrateEnergyToKill[][][] = (float[][][])(float[][][])null;
    private static float PenetrateThickness[][] = (float[][])(float[][])null;
    private static final float KinEnergy_4 = 511.225F;
    private static final float KinEnergy_7_62 = 2453.88F;
    private static final float KinEnergy_12_7 = 10140F;
    private static final float KinEnergy_20 = 23400F;
    private static final float KinEnergy_37 = 131400F;
    private static final float KinEnergy_45 = 252000F;
    private static final float KinEnergy_50 = 369000F;
    private static final float KinEnergy_75 = 1224000F;
    private static final float KinEnergy_100 = 3295500F;
    private static final float KinEnergy_203 = 5120000F;
    private int damage_MAT_TYPE;
    private int damage_EXPL_TYPE;
    private int damage_EFF_BODY_MATERIAL;
    private float damage_PANZER;
    private float damage_MIN_TNT;
    private float damage_MAX_TNT;
    private float damage_PROBAB_DEATH_WHEN_EXPLOSION;
    private static Point3d ppp = new Point3d();
    private boolean bMirrorValueSet;
    private float mirrorAzimuth;
    private float mirrorTangage;
    private static boolean bLogDetail = false;
    private static boolean bLogDetailDamage = false;
    private int iStatusCur_getArmy;
    private int iStatusPrev_getArmy;

    static 
    {
        InitTablesOfEnergyToKill();
    }
}