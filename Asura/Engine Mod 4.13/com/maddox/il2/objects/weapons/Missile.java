// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 28.08.2015 13:18:28
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Missile.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.ScoreCounter;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Selector;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.*;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.util.HashMapExt;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Rocket, MissileInterceptable, ActiveMissile, RocketFlare, 
//            RocketChaff, MissilePhysics, GuidedMissileUtils

public class Missile extends Rocket
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


    public Missile()
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
        startTime = 0L;
        startTimeIsSet = false;
        stepMode = 0;
        stepsForFullTurn = 10F;
        sunRayAngle = 0.0F;
        targetPoint3d = null;
        timeToFailure = 0L;
        trackDelay = 1000L;
        trajectoryVector3d = null;
        turnDiffMax = 0.0F;
        attackMaxDistance = 5000F;
        twoPlaces = new DecimalFormat("+000.00;-000.00");
        victim = null;
        victimOffsetOrient = null;
        victimOffsetPoint3d = null;
        victimSpeed = null;
        targetPoint3d = new Point3d();
        trajectoryVector3d = new Vector3d();
        victimSpeed = new Vector3d();
        victimOffsetOrient = new Orient();
        victimOffsetPoint3d = new Point3f();
        MissileInit();
    }

    public Missile(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
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
        startTime = 0L;
        startTimeIsSet = false;
        stepMode = 0;
        stepsForFullTurn = 10F;
        sunRayAngle = 0.0F;
        targetPoint3d = null;
        timeToFailure = 0L;
        trackDelay = 1000L;
        trajectoryVector3d = null;
        turnDiffMax = 0.0F;
        attackMaxDistance = 5000F;
        twoPlaces = new DecimalFormat("+000.00;-000.00");
        victim = null;
        victimOffsetOrient = null;
        victimOffsetPoint3d = null;
        victimSpeed = null;
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
        return -1;
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
                if(((victim instanceof Aircraft) || (victim instanceof MissileInterceptable)) && (f1 > f * 5F || f > previousDistance) && previousDistance != 1000F && f < 50F)
                {
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
        safeVictimOffset.set(victimOffsetPoint3d);
        if(!Actor.isValid(getOwner()))
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

    private float computeMissileAccelleration()
    {
        victimOffsetPoint3d.set(safeVictimOffset);
        float f = (float)getSpeed(null);
        if(getFailState() == 3)
            rocketMotorOperationTime = 0.0F;
        pos.getAbs(missilePoint3d, missileOrient);
        missileOrient.wrap();
        float f1 = missileForce;
        float f2 = Time.current() - startTime;
        if(f2 > rocketMotorOperationTime + rocketMotorSustainedOperationTime)
        {
            endSmoke();
            flameActive = false;
            smokeActive = false;
            spriteActive = false;
            missileMass = massaEnd;
            missileForce = 0.0F;
        } else
        if(f2 > rocketMotorOperationTime)
        {
            f1 = missileSustainedForce;
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
            if(TrueRandom.nextFloat() < 0.5F)
                deltaAzimuth = f5;
            else
                deltaAzimuth = -f5;
            if(TrueRandom.nextFloat() < 0.5F)
                deltaTangage = f5;
            else
                deltaTangage = -f5;
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

    private void createAdditionalFlames()
    {
        flames = new Actor[exhausts];
        flames[0] = flame;
        if(simFlame == null)
            return;
        Object obj = null;
        for(int i = 1; i < exhausts; i++)
        {
            com.maddox.il2.engine.Hook hook = findHook("_SMOKE" + i);
            flames[i] = new ActorSimpleMesh(simFlame);
            if(flames[i] != null)
            {
                ((ActorSimpleMesh)flames[i]).mesh().setScale(1.0F);
                flames[i].pos.setBase(this, hook, false);
                flames[i].pos.changeHookToRel();
                flames[i].pos.resetAsBase();
            }
        }

    }

    private void createAdditionalSmokes()
    {
        smokes = new Eff3DActor[exhausts];
        smokes[0] = smoke;
        if(effSmoke == null)
            return;
        Object obj = null;
        for(int i = 1; i < exhausts; i++)
        {
            com.maddox.il2.engine.Hook hook = findHook("_SMOKE" + i);
            if(hook == null)
            {
                smokes[i] = null;
                continue;
            }
            smokes[i] = Eff3DActor.New(this, hook, null, 1.0F, effSmoke, -1F);
            if(smokes[i] != null)
                smokes[i].pos.changeHookToRel();
        }

    }

    private void createAdditionalSprites()
    {
        sprites = new Eff3DActor[exhausts];
        sprites[0] = sprite;
        if(effSprite == null)
            return;
        Object obj = null;
        for(int i = 1; i < exhausts; i++)
        {
            com.maddox.il2.engine.Hook hook = findHook("_SMOKE" + i);
            if(hook == null)
            {
                sprites[i] = null;
                continue;
            }
            sprites[i] = Eff3DActor.New(this, hook, null, missileKalibr, effSprite, -1F);
            if(sprites[i] != null)
                sprites[i].pos.changeHookToRel();
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
        endSmoke();
        flameActive = false;
        smokeActive = false;
        spriteActive = false;
        victim = null;
        startTime = 0L;
        previousDistance = 1000F;
        if(myActiveMissile != null)
        {
            GuidedMissileUtils.removeActiveMissile(myActiveMissile);
            myActiveMissile = null;
        }
        if(this instanceof MissileInterceptable)
            Engine.targets().remove(this);
        try
        {
            Engine.missiles().remove(this);
        }
        catch(Exception exception) { }
        super.destroy();
    }

    private void doStart(float f)
    {
        startEngineDone();
        setMesh(MeshShared.get(Property.stringValue(getClass(), "meshFly", Property.stringValue(getClass(), "mesh", null))));
        startMissile(-1F, 0);
        setMissileEffects();
        setMissileStartParams();
        if(isNet() && isNetMirror())
        {
            return;
        } else
        {
            setMissileDropParams();
            setMissileVictim();
            myActiveMissile = new ActiveMissile(this, getOwner(), victim, Actor.isValid(getOwner()) ? getOwner().getArmy() : 0x7fffffff, Actor.isValid(victim) ? victim.getArmy() : 0x7fffffff, ownerIsAI());
            GuidedMissileUtils.addActiveMissile(myActiveMissile);
            return;
        }
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
            if((double)f < 0.01D)
                return 7;
            if((double)f < 0.02D)
                return 4;
            if((double)f < 0.20000000000000001D)
                return 2;
            return (double)f >= 0.5D ? 5 : 6;
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
        if(Time.current() > startTime + trackDelay && (isSunTracking() || isGroundTracking()))
            victim = null;
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
        doStart(-1F);
        trajectoryVector3d.set(1.0D, 0.0D, 0.0D);
        orient.transform(trajectoryVector3d);
        trajectoryVector3d.scale(f);
        setSpeed(trajectoryVector3d);
        collide(false);
        getMissileProperties();
        startTime = Time.current();
        releaseTime = Time.current();
        if(engineDelayTime < 0L)
            startTime += engineDelayTime;
        if(this instanceof MissileInterceptable)
        {
            try
            {
                Engine.missiles().add(this);
            }
            catch(Exception exception) { }
            Engine.targets().add(this);
        }
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
        if(engineDelayTime <= 0L)
            setSmokeSpriteFlames();
        firstNavLight = null;
        lastNavLight = null;
        if(Config.isUSE_RENDER())
            createNavLights();
        if(flame != null)
            ((ActorSimpleMesh)flame).mesh().setScale(1.0F);
        if(Config.isUSE_RENDER() && flame != null)
            flame.drawing(true);
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
      this.victim = null;
      try
      {
        if (ownerIsAI())
        {
          if ((getOwner() instanceof TypeGuidedMissileCarrier))
          {
            this.victim = ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().getMissileTarget();
            this.victimOffsetPoint3d = ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().getMissileTargetOffset();
            this.safeVictimOffset.set(this.victimOffsetPoint3d);
          }
          else if (((getOwner() instanceof TypeFighter)) && 
            (getFM() != null))
          {
            this.victim = ((Pilot)getFM()).target.actor;
          }
        }
        else
        {
          if ((getFM() != null) && 
            (getFM().getOverload() > this.maxLaunchG))
          {
            this.victim = null;
            return;
          }
          if ((getOwner() instanceof TypeGuidedMissileCarrier))
          {
            this.victim = ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().getMissileTarget();
            this.safeVictimOffset.set(this.victimOffsetPoint3d);
          }
          else
          {
            this.victim = Selector.look(true, false, Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()], World.getPlayerAircraft().getArmy(), -1, World.getPlayerAircraft(), false);
            if (this.victim == null) {
              this.victim = Main3D.cur3D().getViewPadlockEnemy();
            }
          }
        }
      }
      catch (Exception localException) {}
    }
    

    private void setSmokeSpriteFlames()
    {
        if(exhausts > 1)
        {
            if(smoke != null)
                createAdditionalSmokes();
            if(sprite != null)
                createAdditionalSprites();
            if(flame != null)
                createAdditionalFlames();
        }
        engineRunning = true;
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

    public void start(float paramFloat, int paramInt)
    {
      Actor localActor = this.pos.base();
      try
      {
        if (Actor.isValid(localActor))
        {
          if (localActor.isNetMirror())
          {
            destroy();
            return;
          }
          this.net = new Master(this);
        }
      }
      catch (Exception localException)
      {
        GuidedMissileUtils.LocalLog(getOwner(), "Missile launch cancelled (system error):" + localException.getMessage());
        destroy();
      }
      doStart(paramFloat);
    }

    public void startEngine()
    {
        Class class1 = getClass();
        com.maddox.il2.engine.Hook hook = null;
        String s = Property.stringValue(class1, "sprite", null);
        if(s != null)
        {
            if(hook == null)
                hook = findHook("_SMOKE");
            sprite = Eff3DActor.New(this, hook, null, 1.0F, s, -1F);
            if(sprite != null)
                sprite.pos.changeHookToRel();
        }
        createAdditionalSprites();
        s = Property.stringValue(class1, "flame", null);
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
            }
        }
        createAdditionalFlames();
        s = Property.stringValue(class1, "smoke", null);
        if(s != null)
        {
            if(hook == null)
                hook = findHook("_SMOKE");
            smoke = Eff3DActor.New(this, hook, null, 1.0F, s, -1F);
            if(smoke != null)
                smoke.pos.changeHookToRel();
        }
        createAdditionalSmokes();
        soundName = Property.stringValue(class1, "sound", null);
        if(soundName != null)
            newSound(soundName, true);
        engineRunning = true;
    }

    public void startEngineDone()
    {
        releaseTime = Time.current();
        if(this instanceof MissileInterceptable)
            Engine.targets().add(this);
        try
        {
            Engine.missiles().add(this);
        }
        catch(Exception exception) { }
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
            return;
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
            vector3d.set(victimOffsetPoint3d);
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
        if(victim != null)
        {
            checkChaffFlareLock();
            victim.pos.getAbs(targetPoint3d, victimOffsetOrient);
            victim.getSpeed(victimSpeed);
            double d = GuidedMissileUtils.distanceBetween(this, victim);
            double d1 = victimSpeed.length();
            if(d1 > 10D)
            {
                double d2 = (double)f / d1;
                double d3 = GuidedMissileUtils.angleActorBetween(victim, this);
                double d4 = Geom.RAD2DEG((float)Math.asin(Math.sin(Geom.DEG2RAD((float)d3)) / d2));
                double d5 = 180D - d3 - d4;
                double d6 = (d * Math.sin(Geom.DEG2RAD((float)d4))) / Math.sin(Geom.DEG2RAD((float)d5));
                d6 -= 5D;
                double d7 = d6 / d1;
                victimSpeed.scale(d7 * (double)(leadPercent / 100F));
                targetPoint3d.add(victimSpeed);
            }
            Orient orient = new Orient();
            orient.set(victim.pos.getAbsOrient());
            trajectoryVector3d.set(victimOffsetPoint3d);
            orient.transform(trajectoryVector3d);
            targetPoint3d.add(trajectoryVector3d);
            targetPoint3d.sub(missilePoint3d);
            missileOrient.transformInv(targetPoint3d);
            float f1 = (float)Math.toDegrees(Math.atan(targetPoint3d.y / targetPoint3d.x));
            float f2 = (float)Math.toDegrees(Math.atan(targetPoint3d.z / targetPoint3d.x));
            if(getFailState() == 4)
            {
                f1 += 180F;
                f2 += 180F;
                if(f1 > 180F)
                    f1 = 180F - f1;
                if(f2 > 180F)
                    f2 = 180F - f2;
            }
            computeMissilePath(f, 0.0F, 0.0F, f1, f2);
        }
        return computeFuzeState();
    }

    public int WeaponsMask()
    {
        return -1;
    }

    public Actor getMissileTarget()
    {
        return victim;
    }

    private void checkChaffFlareLock()
    {
        List list = null;
        try
        {
            list = Engine.countermeasures();
        }
        catch(Exception exception) { }
        if(list == null)
            return;
        int i = ((TypeGuidedMissileCarrier)getOwner()).getGuidedMissileUtils().getDetectorType();
        int j = TrueRandom.nextInt((int)flareLockTime + 1000);
        double d = 0.0D;
        int k = list.size();
        for(int l = 0; l < k; l++)
        {
            Actor actor = (Actor)list.get(l);
            double d1 = GuidedMissileUtils.distanceBetween(this, actor);
            if(i == 1 && (actor instanceof RocketFlare) && flareLockTime < (long)j && d1 < 200D && (double)GuidedMissileUtils.angleActorBetween(victim, this) > 30D)
            {
                victim = actor;
                continue;
            }
            if((actor instanceof RocketChaff) && flareLockTime < (long)j && d1 < 500D && (double)GuidedMissileUtils.angleActorBetween(victim, this) > 30D)
                victim = actor;
        }

    }

    protected static final int DETECTOR_TYPE_INFRARED = 1;
    protected static final int DETECTOR_TYPE_MANUAL = 0;
    protected static final int DETECTOR_TYPE_RADAR_BEAMRIDING = 3;
    protected static final int DETECTOR_TYPE_RADAR_HOMING = 2;
    protected static final int DETECTOR_TYPE_RADAR_TRACK_VIA_MISSILE = 4;
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
    private long startTime;
    private boolean startTimeIsSet;
    private int stepMode;
    private float stepsForFullTurn;
    private float sunRayAngle;
    private Point3d targetPoint3d;
    private long timeToFailure;
    private long trackDelay;
    private Vector3d trajectoryVector3d;
    private float turnDiffMax;
    private float attackMaxDistance;
    DecimalFormat twoPlaces;
    public Actor victim;
    private Orient victimOffsetOrient;
    private Point3f victimOffsetPoint3d;
    private Vector3d victimSpeed;
}