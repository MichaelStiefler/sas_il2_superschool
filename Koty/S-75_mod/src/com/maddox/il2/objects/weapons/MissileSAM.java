// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 07.03.2020 18:21:57
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MissileSAM.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.util.HashMapExt;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Missile, MissileInterceptable, Rocket, MissilePhysics, 
//            GuidedMissileUtils, RocketFlare, RocketChaff, ActiveMissile

public class MissileSAM extends Missile
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
                out.writeFloat((float)((Tuple3d) (theMissilePoint3d)).x);
                out.writeFloat((float)((Tuple3d) (theMissilePoint3d)).y);
                out.writeFloat((float)((Tuple3d) (theMissilePoint3d)).z);
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

    private class MissileNavLight
    {

        public MissileNavLight nextNavLight;
        public Eff3DActor theNavLight;

        public MissileNavLight(Eff3DActor theEff3DActor)
        {
            theNavLight = theEff3DActor;
            nextNavLight = null;
        }
    }

    static class SPAWN
        implements NetSpawn
    {

        public void doSpawn(Actor actor1, NetChannel netchannel1, int j, Point3d point3d1, Orient orient1, float f1)
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


    public MissileSAM()
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
        maxFOVto = 0.0F;
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
        COMMAND_LEAD_TIMER0 = 0;
        COMMAND_LEAD_POS_CURR = new Point3d();
        COMMAND_LEAD_POS_OLD = new Point3d();
        COMMAND_LEAD_POINT = new Point3d();
        COMMAND_LEAD_POINT_LAG = new Point3d();
    	COMMAND_LEAD_POINT.x = 0;
    	COMMAND_LEAD_POINT.y = 0;
    	COMMAND_LEAD_POINT.z = 0;
    	COMMAND_LEAD_POINT_LAG.x = 0;
    	COMMAND_LEAD_POINT_LAG.y = 0;
    	COMMAND_LEAD_POINT_LAG.z = 0;
        MissileInit();
    }

    public MissileSAM(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f, Actor radartarget)
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
        maxFOVto = 0.0F;
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

        
        MissileSAMInit(actor, netchannel, i, point3d, orient, f, radartarget);
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
        super.pos.setAbs(missilePoint3d, missileOrient);
        if(Time.current() > startTime + 2L * trackDelay)
            if(Actor.isValid(victim))
            {
                Point3d futureVictimPos = new Point3d();
                victim.pos.getTime(Time.tickNext(), futureVictimPos);
                Point3d futureMissilePos = new Point3d();
                super.pos.getTime(Time.tickNext(), futureMissilePos);
                float f2 = (float)missilePoint3d.distance(victim.pos.getAbsPoint());
                float f2Future = (float)futureMissilePos.distance(futureVictimPos);
                if(f2Future < 0.0F)
                    f2Future = 0.0F;
                if(((victim instanceof Aircraft) || (victim instanceof MissileInterceptable)) && (f2Future > f2 * 5F || f2 > previousDistance) && previousDistance != 1000F && f2 < proxyDistance)
                {
                    doExplosionAir();
                    postDestroy();
                    collide(false);
                    drawing(false);
                }
                previousDistance = f2;
            } else
            {
                previousDistance = 1000F;
            }
        safeVictimOffset.set(victimOffsetPoint3d);
        return false;
    }

    private float computeMissileAccelleration()
    {
        victimOffsetPoint3d.set(safeVictimOffset);
        float missileSpeed = (float)getSpeed(null);
        if(getFailState() == 3)
            rocketMotorOperationTime = 0.0F;
        super.pos.getAbs(missilePoint3d, missileOrient);
        missileOrient.wrap();
        float theForce = missileForce;
        float millisecondsFromStart = Time.current() - startTime;
        if(millisecondsFromStart > rocketMotorOperationTime + rocketMotorSustainedOperationTime)
        {
            endSmoke();
            flameActive = false;
            smokeActive = false;
            spriteActive = false;
            missileMass = massaEnd;
            missileForce = 0.0F;
        } else
        if(millisecondsFromStart > rocketMotorOperationTime)
        {
            theForce = missileSustainedForce;
        } else
        {
            if(missileForceRunUpTime > 0.001F && millisecondsFromStart < missileForceRunUpTime)
            {
                float runUpTimeFactor = millisecondsFromStart / missileForceRunUpTime;
                if(runUpTimeFactor > 1.0F)
                    runUpTimeFactor = 1.0F;
                setAllSmokeIntensities(runUpTimeFactor);
                setAllSpriteIntensities(runUpTimeFactor);
                theForce *= (initialMissileForce + (100F - initialMissileForce) * runUpTimeFactor) / 100F;
            }
            missileMass -= massLossPerTick;
        }
        float forceAzimuth = MissilePhysics.getGForce(missileSpeed, oldDeltaAzimuth / Time.tickLenFs());
        float forceTangage = MissilePhysics.getGForce(missileSpeed, oldDeltaTangage / Time.tickLenFs());
        float turnForce = (float)Math.sqrt(forceAzimuth * forceAzimuth + forceTangage * forceTangage) * 9.80665F * missileMass * ((float)Math.sqrt(2D) - 1.0F);
        turnForce *= dragCoefficientTurn;
        float resForce = (float)Math.sqrt(Math.abs(theForce * theForce - turnForce * turnForce));
        if(turnForce > theForce)
            resForce *= -1F;
        float accelForce = resForce - MissilePhysics.getDragInGravity(frontSquare, dragCoefficient, (float)((Tuple3d) (missilePoint3d)).z, missileSpeed, missileOrient.getTangage(), missileMass);
        float accelleration = accelForce / missileMass;
        missileSpeed += accelleration * Time.tickLenFs();
        trajectoryVector3d.set(1.0D, 0.0D, 0.0D);
        missileOrient.transform(trajectoryVector3d);
        trajectoryVector3d.scale(missileSpeed);
        setSpeed(trajectoryVector3d);
        missilePoint3d.x += ((Tuple3d) (trajectoryVector3d)).x * (double)Time.tickLenFs();
        missilePoint3d.y += ((Tuple3d) (trajectoryVector3d)).y * (double)Time.tickLenFs();
        missilePoint3d.z += ((Tuple3d) (trajectoryVector3d)).z * (double)Time.tickLenFs();
        if(isNet() && isNetMirror())
        {
            super.pos.setAbs(missilePoint3d, missileOrient);
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
            return missileSpeed;
        }
    }
    
    public void setMaxG (float f) //TODO: dynamic max G depending on speed
    {
    	maxG = f;
    }

    private void computeMissilePath(float missileSpeed, float hTurn, float vTurn, float azimuth, float tangage)
    {
        float turnStepMax = MissilePhysics.getDegPerSec(missileSpeed, maxG) * Time.tickLenFs();
        float turnStepMaxSpeedFactor = 1.0F;
        float turnStepMaxAltitudeFactor = MissilePhysics.getAirDensityFactor((float)((Tuple3d) (missilePoint3d)).z);
        if(missileSpeed < 340F)
        {
            turnStepMaxSpeedFactor = missileSpeed / 340F;
        } else
        {
            turnStepMaxAltitudeFactor = (float)((double)turnStepMaxAltitudeFactor * Math.sqrt(missileSpeed / 340F));
            if(turnStepMaxAltitudeFactor > 1.0F)
                turnStepMaxAltitudeFactor = 1.0F;
        }
        turnStepMax *= turnStepMaxSpeedFactor * turnStepMaxAltitudeFactor;
        float newTurnDiffMax = turnStepMax / stepsForFullTurn;
        if(newTurnDiffMax > turnDiffMax)
            turnDiffMax = (turnDiffMax * stepsForFullTurn + newTurnDiffMax) / (stepsForFullTurn + 1.0F);
        if(getFailState() == 5)
        {
            if(ivanTimeLeft < Time.tickLenFs())
            {
                if(TrueRandom.nextFloat() < 0.5F)
                {
                    if(TrueRandom.nextFloat() < 0.5F)
                        deltaAzimuth = turnStepMax;
                    else
                        deltaAzimuth = -turnStepMax;
                    deltaTangage = TrueRandom.nextFloat(-turnStepMax, turnStepMax);
                } else
                {
                    if(TrueRandom.nextFloat() < 0.5F)
                        deltaTangage = turnStepMax;
                    else
                        deltaTangage = -turnStepMax;
                    deltaAzimuth = TrueRandom.nextFloat(-turnStepMax, turnStepMax);
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
                deltaAzimuth = turnStepMax;
            else
                deltaAzimuth = -turnStepMax;
            if(TrueRandom.nextFloat() < 0.5F)
                deltaTangage = turnStepMax;
            else
                deltaTangage = -turnStepMax;
        } else
        if(getFailState() == 6)
        {
            deltaAzimuth = oldDeltaAzimuth;
            deltaTangage = oldDeltaTangage;
        } else
        {
            if(stepMode == 0)
            {
                if(((Tuple3d) (targetPoint3d)).x > -10D)
                {
                    deltaAzimuth = -azimuth;
                    if(deltaAzimuth > turnStepMax)
                        deltaAzimuth = turnStepMax;
                    if(deltaAzimuth < -turnStepMax)
                        deltaAzimuth = -turnStepMax;
                    deltaTangage = tangage;
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
            } else
            if((stepMode == 1 && Math.abs(azimuth) <= 90F && Math.abs(tangage) <= 90F))
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
        missileOrient.increment(deltaAzimuth, deltaTangage, 0.0F);
        missileOrient.setYPR(missileOrient.getYaw(), missileOrient.getPitch(), getRoll());
    }

    private void computeNoTrackPath()
    {
        missileOrient.setYPR(missileOrient.getYaw(), missileOrient.getPitch(), getRoll());
    }

    private void createAdditionalFlames()
    {
        flames = new Actor[exhausts];
        flames[0] = super.flame;
        if(simFlame == null)
            return;
        com.maddox.il2.engine.Hook theHook = null;
        for(int i = 1; i < exhausts; i++)
        {
            theHook = findHook("_SMOKE" + i);
            flames[i] = new ActorSimpleMesh(simFlame);
            if(flames[i] != null)
            {
                ((ActorSimpleMesh)flames[i]).mesh().setScale(1.0F);
                flames[i].pos.setBase(this, theHook, false);
                flames[i].pos.changeHookToRel();
                flames[i].pos.resetAsBase();
            }
        }

    }

    private void createAdditionalSmokes()
    {
        smokes = new Eff3DActor[exhausts];
        smokes[0] = super.smoke;
        if(effSmoke == null)
            return;
        com.maddox.il2.engine.Hook theHook = null;
        for(int i = 1; i < exhausts; i++)
        {
            theHook = findHook("_SMOKE" + i);
            if(theHook == null)
            {
                smokes[i] = null;
            } else
            {
                smokes[i] = Eff3DActor.New(this, theHook, null, 1.0F, effSmoke, -1F);
                if(smokes[i] != null)
                    ((Actor) (smokes[i])).pos.changeHookToRel();
            }
        }

    }

    private void createAdditionalSprites()
    {
        sprites = new Eff3DActor[exhausts];
        sprites[0] = super.sprite;
        if(effSprite == null)
            return;
        com.maddox.il2.engine.Hook theHook = null;
        for(int i = 1; i < exhausts; i++)
        {
            theHook = findHook("_SMOKE" + i);
            if(theHook == null)
            {
                sprites[i] = null;
            } else
            {
                sprites[i] = Eff3DActor.New(this, theHook, null, missileKalibr, effSprite, -1F);
                if(sprites[i] != null)
                    ((Actor) (sprites[i])).pos.changeHookToRel();
            }
        }

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
        if(this instanceof MissileInterceptable)
            Engine.targets().remove(this);
        try
        {
            Engine.missiles().remove(this);
        }
        catch(Exception exception) { }
        super.destroy();
    }

    private void doStart(float f, Actor radartarget)
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
            setMissileVictim(radartarget);
            return;
        }
    }

    private void endAllFlame()
    {
        if(exhausts < 2)
        {
            if(super.flame != null)
                ObjState.destroy(super.flame);
        } else
        if(flames != null)
        {
            for(int i = 0; i < exhausts; i++)
                if(flames[i] != null)
                    ObjState.destroy(flames[i]);

        }
        if(super.light != null)
            super.light.light.setEmit(0.0F, 1.0F);
        stopSounds();
        endedFlame = true;
    }

    private void endAllSmoke()
    {
        if(exhausts < 2)
        {
            if(super.smoke != null)
                Eff3DActor.finish(super.smoke);
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
            if(super.sprite != null)
                Eff3DActor.finish(super.sprite);
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
        for(MissileNavLight theNavLight = firstNavLight; theNavLight != null; theNavLight = theNavLight.nextNavLight)
            Eff3DActor.finish(theNavLight.theNavLight);

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
        long millisecondsFromStart = Time.current() - startTime;
        if(millisecondsFromStart < timeToFailure)
            return 0;
        if(failState == 1)
        {
            float fRand = TrueRandom.nextFloat();
            if((double)fRand < 0.01D)
                return 7;
            if((double)fRand < 0.02D)
                return 4;
            if((double)fRand < 0.20000000000000001D)
                return 2;
            return (double)fRand >= 0.5D ? 5 : 6;
        } else
        {
            return failState;
        }
    }

    public double getLaunchPitch()
    {
        double launchPitchTimeFactor = getLaunchTimeFactor();
        double theLaunchPitch = 2D * ((Math.cos(launchPitchTimeFactor + 0.40000000000000002D) - 2.1759949269000001D) + launchPitchTimeFactor / 5D);
        theLaunchPitch *= Math.cos(launchKren);
        for(theLaunchPitch += launchPitch; theLaunchPitch > 180D; theLaunchPitch -= 360D);
        for(; theLaunchPitch < -180D; theLaunchPitch += 360D);
        return theLaunchPitch;
    }

    public double getLaunchTimeFactor()
    {
        return ((double)(Time.current() - startTime) / (double)trackDelay) * 6D;
    }

    public double getLaunchYaw()
    {
        double launchYawTimeFactor = getLaunchTimeFactor();
        double theLaunchYaw = 2D * ((Math.cos(launchYawTimeFactor + 0.40000000000000002D) - 2.1759949269000001D) + launchYawTimeFactor / 5D);
        theLaunchYaw *= Math.sin(launchKren);
        for(theLaunchYaw += launchYaw; theLaunchYaw > 180D; theLaunchYaw -= 360D);
        for(; theLaunchYaw < -180D; theLaunchYaw += 360D);
        return theLaunchYaw;
    }

    private void getMissileProperties()
    {
        Class localClass = super.getClass();
        launchType = Property.intValue(localClass, "launchType", 0);
        stepMode = Property.intValue(localClass, "stepMode", 0);
        maxFOVfrom = Property.floatValue(localClass, "maxFOVfrom", 180F);
        maxFOVto = Property.floatValue(localClass, "maxFOVto", 99.9F);
        maxLaunchG = Property.floatValue(localClass, "maxLockGForce", 99.9F);
        maxG = Property.floatValue(localClass, "maxGForce", 12F);
        stepsForFullTurn = Property.floatValue(localClass, "stepsForFullTurn", 10F);
        rocketMotorOperationTime = Property.floatValue(localClass, "timeFire", 2.2F) * 1000F;
        missileTimeLife = Property.floatValue(localClass, "timeLife", 30F) * 1000F;
        rocketMotorSustainedOperationTime = Property.floatValue(localClass, "timeSustain", 0.0F) * 1000F;
        super.timeFire = (long)(rocketMotorOperationTime + rocketMotorSustainedOperationTime);
        super.timeLife = (long)missileTimeLife;
        engineDelayTime = Property.longValue(getClass(), "engineDelayTime", 0L);
        missileForce = Property.floatValue(localClass, "force", 18712F);
        missileSustainedForce = Property.floatValue(localClass, "forceSustain", 0.0F);
        leadPercent = Property.floatValue(localClass, "leadPercent", 0.0F);
        missileKalibr = Property.floatValue(localClass, "kalibr", 0.2F);
        missileMass = Property.floatValue(localClass, "massa", 86.2F);
        massaEnd = Property.floatValue(localClass, "massaEnd", 80F);
        sunRayAngle = Property.floatValue(localClass, "sunRayAngle", 0.0F);
        groundTrackFactor = Property.floatValue(localClass, "groundTrackFactor", 0.0F);
        flareLockTime = Property.longValue(localClass, "flareLockTime", 1000L);
        trackDelay = Property.longValue(localClass, "trackDelay", 1000L);
        trackLag = Property.floatValue(localClass, "trackLag", 0.5F);
        proxyDistance = Property.floatValue(localClass, "proximityFuze", 50F);
        frontSquare = (3.141593F * missileKalibr * missileKalibr) / 4F;
        missileForceRunUpTime = Property.floatValue(localClass, "forceT1", 0.0F) * 1000F;
        initialMissileForce = Property.floatValue(localClass, "forceP1", 0.0F);
        dragCoefficient = Property.floatValue(localClass, "dragCoefficient", 0.3F);
        dragCoefficientTurn = Property.floatValue(localClass, "dragCoefficientTurn", dragCoefficient);
        attackMaxDistance = Property.floatValue(localClass, "PkDistMax", 5000F);
        exhausts = getNumExhausts();
        effSmoke = Property.stringValue(localClass, "smoke", null);
        effSprite = Property.stringValue(localClass, "sprite", null);
        simFlame = Property.stringValue(localClass, "flame", null);
        float failureRate = Property.floatValue(localClass, "failureRate", 10F);
        if(TrueRandom.nextFloat(0.0F, 100F) < failureRate)
        {
            setFailState();
            float randFail = TrueRandom.nextFloat();
            long baseFailTime = trackDelay;
            if(failState == 7)
                baseFailTime += baseFailTime;
            timeToFailure = baseFailTime + (long)((missileTimeLife - (float)baseFailTime) * randFail);
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
        if(super.mesh.hookFind("_SMOKE") == -1)
            return 0;
        int retVal;
        for(retVal = 1; super.mesh.hookFind("_SMOKE" + retVal) != -1; retVal++);
        return retVal;
    }

    private int getNumNavLights(String theNavLightHookName)
    {
        if(super.mesh.hookFind(theNavLightHookName) == -1)
            return 0;
        int retVal;
        for(retVal = 1; super.mesh.hookFind(theNavLightHookName + retVal) != -1; retVal++);
        return retVal;
    }

    public float getRoll()
    {
        if(Time.current() - releaseTime > trackDelay)
            return 0.0F;
        float fRollCalcAbscissa = ((float)(Time.current() - startTime) / (float)trackDelay) * 3.141593F;
        float fRollCalcOrdinate = ((float)Math.cos(fRollCalcAbscissa) + 1.0F) * 0.5F;
        float fRet;
        for(fRet = 360F - fRollCalcOrdinate * (360F - oldRoll); fRet > 180F; fRet -= 360F);
        for(; fRet < -180F; fRet += 360F);
        return fRet;
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
        doEvade();
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
        super.pos.getAbs(missilePoint3d, missileOrient);
        missileOrient.wrap();
        float ttG = missileOrient.getTangage() * -1F;
        float missileAlt = (float)(((Tuple3d) (missilePoint3d)).z - Engine.land().HQ_Air(((Tuple3d) (missilePoint3d)).x, ((Tuple3d) (missilePoint3d)).y));
        missileAlt /= 1000F;
        float groundFactor = ttG / (missileAlt * missileAlt);
        long lTimeCurrent = Time.current();
        if(lastTimeNoFlare == 0L)
            lastTimeNoFlare = lTimeCurrent;
        if(groundFactor > groundTrackFactor)
        {
            return lTimeCurrent >= lastTimeNoFlare + flareLockTime;
        } else
        {
            lastTimeNoFlare = lTimeCurrent;
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
        float sunAngle = GuidedMissileUtils.angleBetween(this, World.Sun().ToSun);
        return sunAngle < sunRayAngle;
    }

    public final void MissileSAMInit()
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

    public final void MissileSAMInit(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f, Actor radartarget)
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
        super.pos.setAbs(point3d, orient);
        super.pos.reset();
        super.pos.setBase(actor, null, true);
        doStart(-1F, radartarget);
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
        try
        {
            Engine.missiles().add(this);
        }
        catch(Exception exception) { }
        if(this instanceof MissileInterceptable)
            Engine.targets().add(this);
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
        netmsgspawn.writeFloat(orient.kren());
        float f = (float)getSpeed(null);
        netmsgspawn.writeFloat(f);
        return netmsgspawn;
    }

    private boolean ownerIsAI()
    {
        return true;
    }

    private boolean ownerIsAircraft()
    {
        return false;
    }

    public void runupEngine()
    {
        float millisecondsFromStart = Time.current() - startTime;
        float runUpTimeFactor = millisecondsFromStart / missileForceRunUpTime;
        if(runUpTimeFactor > 1.0F)
            runUpTimeFactor = 1.0F;
        setAllSmokeIntensities(runUpTimeFactor);
        setAllSpriteIntensities(runUpTimeFactor);
    }

    private void setAllSmokeIntensities(float theIntensity)
    {
        if(!engineRunning)
            return;
        if(exhausts < 2)
        {
            if(super.smoke != null)
                Eff3DActor.setIntesity(super.smoke, theIntensity);
        } else
        if(smokes != null)
        {
            for(int i = 0; i < exhausts; i++)
                if(smokes[i] != null)
                    Eff3DActor.setIntesity(smokes[i], theIntensity);

        }
    }

    private void setAllSpriteIntensities(float theIntensity)
    {
        if(!engineRunning)
            return;
        if(exhausts < 2)
        {
            if(super.sprite != null)
                Eff3DActor.setIntesity(super.sprite, theIntensity);
        } else
        if(sprites != null)
        {
            for(int i = 0; i < exhausts; i++)
                if(sprites[i] != null)
                    Eff3DActor.setIntesity(sprites[i], theIntensity);

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
            super.pos.setAbs(missilePoint3d, missileOrient);
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
        if(super.flame != null)
            ((ActorSimpleMesh)super.flame).mesh().setScale(1.0F);
        if(Config.isUSE_RENDER() && super.flame != null)
            super.flame.drawing(true);
    }

    private void setMissileStartParams()
    {
        previousDistance = 1000F;
        super.pos.getRelOrient().transformInv(super.speed);
        super.speed.y *= 3D;
        super.speed.z *= 3D;
        super.speed.x -= 198D;
        super.pos.getRelOrient().transform(super.speed);
        setStartTime();
        super.pos.getAbs(missilePoint3d, missileOrient);
        missileBaseSpeed = (float)getSpeed(null);
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
            if(iMotorType == 0 || iMotorType == 1 || iMotorType == 7)
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
        if(iMotorType == 0 || iMotorType == 1 || iMotorType == 7)
            iRetVal |= HEAT_SPREAD_360;
        return iRetVal;
    }

    private void setMissileVictim(Actor radartarget)
    {
        if(radartarget != null)
        {
            victim = radartarget;
        } else
        {
            super.pos.setAbs(missilePoint3d, missileOrient);
            victim = NearestTargetsAngle.getEnemyAngle(9, -1, missilePoint3d, attackMaxDistance, getArmy(), missileOrient, maxFOVfrom);
        }
    }

    private void setSmokeSpriteFlames()
    {
        if(exhausts > 1)
        {
            if(super.smoke != null)
                createAdditionalSmokes();
            if(super.sprite != null)
                createAdditionalSprites();
            if(super.flame != null)
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

    public void start(float f, Actor radartarget)
    {
        start(f, 0);
    }

    public void start(float f, int paramInt, Actor radartarget)
    {
        Actor actor = super.pos.base();
        try
        {
        if(!Actor.isValid(actor))
        {
        if(actor.isNetMirror())
        {
            destroy();
            return;
        }
            super.net = new Master(this);
        }
        }
        catch(Exception exception)
        {
            GuidedMissileUtils.LocalLog(getOwner(), "Missile launch cancelled (system error):" + exception.getMessage());
            destroy();
        }
        doStart(f, radartarget);
        return;
    }

    public void startEngine()
    {
        Class localClass = getClass();
        com.maddox.il2.engine.Hook localHook = null;
        String str = Property.stringValue(localClass, "sprite", null);
        if(str != null)
        {
            if(localHook == null)
                localHook = findHook("_SMOKE");
            super.sprite = Eff3DActor.New(this, localHook, null, 1.0F, str, -1F);
            if(super.sprite != null)
                ((Actor) (super.sprite)).pos.changeHookToRel();
        }
        createAdditionalSprites();
        str = Property.stringValue(localClass, "flame", null);
        if(str != null)
        {
            if(localHook == null)
                localHook = findHook("_SMOKE");
            super.flame = new ActorSimpleMesh(str);
            if(super.flame != null)
            {
                ((ActorSimpleMesh)super.flame).mesh().setScale(1.0F);
                super.flame.pos.setBase(this, localHook, false);
                super.flame.pos.changeHookToRel();
                super.flame.pos.resetAsBase();
            }
        }
        createAdditionalFlames();
        str = Property.stringValue(localClass, "smoke", null);
        if(str != null)
        {
            if(localHook == null)
                localHook = findHook("_SMOKE");
            super.smoke = Eff3DActor.New(this, localHook, null, 1.0F, str, -1F);
            if(super.smoke != null)
                ((Actor) (super.smoke)).pos.changeHookToRel();
        }
        createAdditionalSmokes();
        super.soundName = Property.stringValue(localClass, "sound", null);
        if(super.soundName != null)
            newSound(super.soundName, true);
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

    public void startMissile(float paramFloat, int paramInt)
    {
        Class localClass = getClass();
        float f1 = Property.floatValue(localClass, "kalibr", 0.082F);
        if(paramFloat <= 0.0F)
            paramFloat = Property.floatValue(localClass, "timeLife", 45F);
        float f2 = -1F + 2.0F * TrueRandom.nextFloat();
        f2 *= f2 * f2;
        float f3 = -1F + 2.0F * TrueRandom.nextFloat();
        f3 *= f3 * f3;
        init(f1, Property.floatValue(localClass, "massa", 6.8F), Property.floatValue(localClass, "massaEnd", 2.52F), Property.floatValue(localClass, "timeFire", 4F) / (1.0F + 0.1F * f2), Property.floatValue(localClass, "force", 500F) * (1.0F + 0.1F * f2), paramFloat + f3 * 0.1F);
        setOwner(super.pos.base(), false, false, false);
        super.pos.setBase(null, null, true);
        super.pos.setAbs(super.pos.getCurrent());
        super.pos.getAbs(Aircraft.tmpOr);
        float f4 = 0.68F * Property.floatValue(localClass, "maxDeltaAngle", 3F);
        f2 = -1F + 2.0F * TrueRandom.nextFloat();
        f3 = -1F + 2.0F * TrueRandom.nextFloat();
        f2 *= f2 * f2 * f4;
        f3 *= f3 * f3 * f4;
        Aircraft.tmpOr.increment(f2, f3, 0.0F);
        super.pos.setAbs(Aircraft.tmpOr);
        super.pos.getRelOrient().transformInv(super.speed);
        super.speed.z /= 3D;
        super.speed.x += 200D;
        super.pos.getRelOrient().transform(super.speed);
        collide(true);
        interpPut(new Rocket.Interpolater(), null, Time.current(), null);
        if(!Config.isUSE_RENDER())
            return;
        if(engineDelayTime <= 0L)
            startEngine();
        super.light = new LightPointActor(new LightPointWorld(), new Point3d());
        super.light.light.setColor((Color3f)Property.value(localClass, "emitColor", new Color3f(1.0F, 1.0F, 0.5F)));
        super.light.light.setEmit(Property.floatValue(localClass, "emitMax", 1.0F), Property.floatValue(localClass, "emitLen", 50F));
        super.draw.lightMap().put("light", super.light);
    }

    public boolean stepBeamRider()
    {
        float missileSpeed = computeMissileAccelleration();
        if(missileSpeed == -1F)
            return false;
        Actor myOwner = getOwner();
        if(victim != null && GuidedMissileUtils.angleBetween(myOwner, victim) > maxFOVfrom) //TODO:candidate1
            victim = null;
        if(Time.current() < startTime + trackDelay)
            computeNoTrackPath();
        else
        if(victim != null && myOwner != null)
        {
            float hTurn = 0.0F;
            float vTurn = 0.0F;
            
            //TODO:

            
            
            Point3d pointTarget = new Point3d();
            pointTarget.set(victim.pos.getAbsPoint());

            Orient orientTarget = new Orient();
            orientTarget.set(victim.pos.getAbsOrient());
            
            if(leadPercent != 0)
            {
            victim.getSpeed(victimSpeed);
            double victimDistance = GuidedMissileUtils.distanceBetween(this, victim);
             
            if(victimSpeed.length() > 10D)
            {
            	/*
                double speedRel = (double)missileSpeed / theVictimSpeed;
                double gamma = GuidedMissileUtils.angleActorBetween(victim, this);
                double alpha = Geom.RAD2DEG((float)Math.asin(Math.sin(Geom.DEG2RAD((float)gamma)) / speedRel));
                double beta = 180D - gamma - alpha;
                double victimAdvance = (victimDistance * Math.sin(Geom.DEG2RAD((float)alpha))) / Math.sin(Geom.DEG2RAD((float)beta));
                if(detectorType == 1)
                    victimAdvance -= 5D;*/
                double timeToTarget = victimDistance / (this.getSpeed(null));
                if(timeToTarget > (this.timeLife/1000)) timeToTarget = (this.timeLife/1000);
                victimSpeed.scale(timeToTarget * (double)(leadPercent / 100F));
                pointTarget.add(victimSpeed);
            }
            }
            
            

            Point3d pointAC = new Point3d();
            pointAC.set(myOwner.pos.getAbsPoint());              /** Position of owner*/
            
            
            //Orient orientAC = new Orient();
            //orientAC.set(myOwner.pos.getAbsOrient());
            Vector3d vectorOffset = new Vector3d();
            vectorOffset.set(victimOffsetPoint3d);
            //orientTarget.transform(vectorOffset);
            pointTarget.add(vectorOffset);
            
            
            Point3d pointMissile = new Point3d();
            pointMissile.set(super.pos.getAbsPoint());            /** Position of missile*/
            
            
            Point3d pointMissileAft = new Point3d();
            pointMissileAft.set(pointMissile);
            //Orient orientMissileAft = new Orient();
            //orientMissileAft.set(super.pos.getAbsOrient());
            Point3d pointACAft = new Point3d();
            pointACAft.set(pointAC);
            //pointTarget.sub(pointAC);
            //orientAC.transformInv(pointTarget);
            
            
            
            
            //Set 0-0-0
            pointMissile.sub(pointAC);
            pointTarget.sub(pointAC);
            
            
            float targetAzimuth = (float)Math.toDegrees(Math.atan2(pointTarget.x, pointTarget.y));
            float targetRange = (float)Math.sqrt(pointTarget.y*pointTarget.y + pointTarget.x*pointTarget.x);
            float targetElevation = (float) Math.toDegrees(Math.atan(pointTarget.z / targetRange));
            
            float missileAzimuth = (float)Math.toDegrees(Math.atan2(pointMissile.x, pointMissile.y));
            float missileRange = (float)Math.sqrt(pointMissile.y*pointMissile.y + pointMissile.x*pointMissile.x);
            float missileElevation = (float) Math.toDegrees(Math.atan(pointMissile.z / missileRange));
            
            float hCorrect = targetAzimuth - missileAzimuth;
            float vCorrect = targetElevation - missileElevation;
            
            if(hCorrect > 180) hCorrect -=360;
            if(hCorrect < -180) hCorrect +=360;
            
            float missileOffsetAzimuth = -hCorrect;
            float missileOffsetElevation = -vCorrect;
            
            float missileAzimuthAft = (float)Math.toDegrees(Math.atan2(pointACAft.x, pointACAft.y));
            float missileRangeAft = (float)Math.sqrt(pointACAft.y*pointACAft.y + pointACAft.x*pointACAft.x);
            float missileElevationAft = (float)Math.toDegrees(Math.atan(((Tuple3d) (pointACAft)).z / missileRangeAft));
            float missileTrackOffsetAzimuth = missileOffsetAzimuth - missileAzimuthAft;
            float missileTrackOffsetElevation = missileOffsetElevation - missileElevationAft;
            
            
            /*if(hCorrect > 0) hTurn = -1.0f;
            if(hCorrect > 5) hTurn = -3.0f;
            if(hCorrect < 0) hTurn =  1.0f;
            if(hCorrect <-5) hTurn =  3.0f;
            if(vCorrect > 0) vTurn =  1.0f;
            if(vCorrect > 5) vTurn =  3.0f;
            if(vCorrect < 0) vTurn = -1.0f;
            if(vCorrect <-5) vTurn = -3.0f;*/
            
            
        	
            //float targetAzimuth = (float)Math.toDegrees(Math.atan(-((Tuple3d) (pointTarget)).y / ((Tuple3d) (pointTarget)).x));
            //float targetElevation = (float)Math.toDegrees(Math.atan(((Tuple3d) (pointTarget)).z / Math.sqrt(((Tuple3d) (pointTarget)).x*((Tuple3d) (pointTarget)).x+((Tuple3d) (pointTarget)).y*((Tuple3d) (pointTarget)).y)));
            
            
            
            
            /*
            if(getFailState() == 4)
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
            float missileElevation = (float)Math.toDegrees(Math.atan(((Tuple3d) (pointMissile)).z / Math.sqrt(((Tuple3d) (pointMissile)).x*((Tuple3d) (pointMissile)).x+((Tuple3d) (pointMissile)).y*((Tuple3d) (pointMissile)).y)));
            float missileOffsetAzimuth = missileAzimuth - targetAzimuth;
            float missileOffsetElevation = missileElevation - targetElevation;
            pointACAft.sub(pointMissileAft);
            orientMissileAft.transformInv(pointACAft);
            float missileAzimuthAft = (float)Math.toDegrees(Math.atan(-((Tuple3d) (pointACAft)).y / ((Tuple3d) (pointACAft)).x));
            float missileElevationAft = (float)Math.toDegrees(Math.atan(((Tuple3d) (pointACAft)).z / Math.sqrt(((Tuple3d) (pointACAft)).x*((Tuple3d) (pointACAft)).x+((Tuple3d) (pointACAft)).y*((Tuple3d) (pointACAft)).y)));
            float missileTrackOffsetAzimuth = missileOffsetAzimuth - missileAzimuthAft;
            float missileTrackOffsetElevation = missileOffsetElevation - missileElevationAft;
            
            */
            float closingFactor = -5F;
            float maxClosing = 60F;
            float fastClosingMax = 3F;
            float turnNormal = 1.0F;
            float turnQuick = 1.5F;
            float turnSharp = 2.0F;
            
            //missileTrackOffsetElevation *=0.1f;
            //missileTrackOffsetAzimuth *=0.005f;
            if(missileOffsetAzimuth < 0.0F)
            {
                if(missileTrackOffsetAzimuth < 0.0F)
                    hTurn = turnSharp;
                else
                if(missileTrackOffsetAzimuth > maxClosing)
                    hTurn = -turnNormal;
                else
                if(missileTrackOffsetAzimuth > fastClosingMax && missileTrackOffsetAzimuth > closingFactor * missileOffsetAzimuth)
                    hTurn = -turnQuick;
                else
                    hTurn = turnNormal;
            } else
            if(missileTrackOffsetAzimuth > 0.0F)
                hTurn = -turnSharp;
            else
            if(missileTrackOffsetAzimuth < -maxClosing)
                hTurn = turnNormal;
            else
            if(missileTrackOffsetAzimuth < -fastClosingMax && missileTrackOffsetAzimuth < closingFactor * missileOffsetAzimuth)
                hTurn = turnQuick;
            else
                hTurn = -turnNormal;
            if(missileOffsetElevation < 0.0F)
            {
                if(missileTrackOffsetElevation < 0.0F)
                    vTurn = turnSharp;
                else
                if(missileTrackOffsetElevation > maxClosing)
                    vTurn = -turnNormal;
                else
                if(missileTrackOffsetElevation > fastClosingMax && missileTrackOffsetElevation > closingFactor * missileOffsetElevation)
                    vTurn = -turnQuick;
                else
                    vTurn = turnNormal;
            } else
            if(missileTrackOffsetElevation > 0.0F)
                vTurn = -turnSharp;
            else
            if(missileTrackOffsetElevation < -maxClosing)
                vTurn = turnNormal;
            else
            if(missileTrackOffsetElevation < -fastClosingMax && missileTrackOffsetElevation < closingFactor * missileOffsetElevation)
                vTurn = turnQuick;
            else
                vTurn = -turnNormal;
            /*
            Point3d pointAC = new Point3d();
            pointAC.set(myOwner.pos.getAbsPoint());
            Orient orientAC = new Orient();
            orientAC.set(myOwner.pos.getAbsOrient());
            
            Vector3d vectorOffset = new Vector3d();
            vectorOffset.set(victimOffsetPoint3d);
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
            if(getFailState() == 4)
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
            if(missileOffsetAzimuth < 0.0F)
            {
                if(missileTrackOffsetAzimuth < 0.0F)
                    hTurn = turnSharp;
                else
                if(missileTrackOffsetAzimuth > maxClosing)
                    hTurn = -turnNormal;
                else
                if(missileTrackOffsetAzimuth > fastClosingMax && missileTrackOffsetAzimuth > closingFactor * missileOffsetAzimuth)
                    hTurn = -turnQuick;
                else
                    hTurn = turnNormal;
            } else
            if(missileTrackOffsetAzimuth > 0.0F)
                hTurn = -turnSharp;
            else
            if(missileTrackOffsetAzimuth < -maxClosing)
                hTurn = turnNormal;
            else
            if(missileTrackOffsetAzimuth < -fastClosingMax && missileTrackOffsetAzimuth < closingFactor * missileOffsetAzimuth)
                hTurn = turnQuick;
            else
                hTurn = -turnNormal;
            if(missileOffsetElevation < 0.0F)
            {
                if(missileTrackOffsetElevation < 0.0F)
                    vTurn = turnSharp;
                else
                if(missileTrackOffsetElevation > maxClosing)
                    vTurn = -turnNormal;
                else
                if(missileTrackOffsetElevation > fastClosingMax && missileTrackOffsetElevation > closingFactor * missileOffsetElevation)
                    vTurn = -turnQuick;
                else
                    vTurn = turnNormal;
            } else
            if(missileTrackOffsetElevation > 0.0F)
                vTurn = -turnSharp;
            else
            if(missileTrackOffsetElevation < -maxClosing)
                vTurn = turnNormal;
            else
            if(missileTrackOffsetElevation < -fastClosingMax && missileTrackOffsetElevation < closingFactor * missileOffsetElevation)
                vTurn = turnQuick;
            else
                vTurn = -turnNormal;
            */
            
            
            
            targetAzimuth = 0;
            targetElevation = 0;
            //computeMissilePath(missileSpeed, -hTurn, vTurn, targetAzimuth, targetElevation);
            
            computeMissilePath(missileSpeed, -missileOffsetAzimuth*200*stepsForFullTurn, -missileOffsetElevation*200*stepsForFullTurn, targetAzimuth, targetElevation);
        }
        return computeFuzeState();
    }


    
    public boolean stepTargetHoming()
    {
        float missileSpeed = computeMissileAccelleration();
        if(missileSpeed == -1F)
            return false;
        if(Time.current() < startTime + trackDelay)
            computeNoTrackPath();
        else
        if(victim != null)
        {
            checkChaffFlareLock();
            victim.pos.getAbs(targetPoint3d, victimOffsetOrient);
            victim.getSpeed(victimSpeed);
            double victimDistance = GuidedMissileUtils.distanceBetween(this, victim);
            double theVictimSpeed = victimSpeed.length();
            if(theVictimSpeed > 10D)
            {
                double speedRel = (double)missileSpeed / theVictimSpeed;
                double gamma = GuidedMissileUtils.angleActorBetween(victim, this);
                double alpha = Geom.RAD2DEG((float)Math.asin(Math.sin(Geom.DEG2RAD((float)gamma)) / speedRel));
                double beta = 180D - gamma - alpha;
                double victimAdvance = (victimDistance * Math.sin(Geom.DEG2RAD((float)alpha))) / Math.sin(Geom.DEG2RAD((float)beta));
                if(detectorType == 1)
                    victimAdvance -= 5D;
                double timeToTarget = victimAdvance / theVictimSpeed;
                victimSpeed.scale(timeToTarget * (double)(leadPercent / 100F));
                targetPoint3d.add(victimSpeed);
            }
            Orient orientTarget = new Orient();
            orientTarget.set(victim.pos.getAbsOrient());
            trajectoryVector3d.set(victimOffsetPoint3d);
            orientTarget.transform(trajectoryVector3d);
            targetPoint3d.add(trajectoryVector3d);
            targetPoint3d.sub(missilePoint3d);
            missileOrient.transformInv(targetPoint3d);
            float angleAzimuth = (float)Math.toDegrees(Math.atan(((Tuple3d) (targetPoint3d)).y / ((Tuple3d) (targetPoint3d)).x));
            float angleTangage = (float)Math.toDegrees(Math.atan(((Tuple3d) (targetPoint3d)).z / ((Tuple3d) (targetPoint3d)).x));
            if(getFailState() == 4)
            {
                angleAzimuth += 180F;
                angleTangage += 180F;
                if(angleAzimuth > 180F)
                    angleAzimuth = 180F - angleAzimuth;
                if(angleTangage > 180F)
                    angleTangage = 180F - angleTangage;
            }
            computeMissilePath(missileSpeed, 0.0F, 0.0F, angleAzimuth, angleTangage);
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
        List theCountermeasures = null;
        try
        {
            theCountermeasures = Engine.countermeasures();
        }
        catch(Exception exception) { }
        if(theCountermeasures == null)
            return;
        int lockTime = TrueRandom.nextInt((int)flareLockTime + 1000);
        double flareDistance = 0.0D;
        int counterMeasureSize = theCountermeasures.size();
        for(int counterMeasureIndex = 0; counterMeasureIndex < counterMeasureSize; counterMeasureIndex++)
        {
            Actor flarechaff = (Actor)theCountermeasures.get(counterMeasureIndex);
            flareDistance = GuidedMissileUtils.distanceBetween(this, flarechaff);
            if(detectorType == 1 && (flarechaff instanceof RocketFlare) && flareLockTime < (long)lockTime && flareDistance < 200D && (double)GuidedMissileUtils.angleActorBetween(victim, this) > 30D)
                victim = flarechaff;
            else
            if((flarechaff instanceof RocketChaff) && flareLockTime < (long)lockTime && flareDistance < 500D && (double)GuidedMissileUtils.angleActorBetween(victim, this) > 30D)
                victim = flarechaff;
        }

    }

    private void doEvade()
    {
        List theTargets = Engine.targets();
        int TargetsSize = theTargets.size();
        for(int TargetsIndex = 0; TargetsIndex < TargetsSize; TargetsIndex++)
        {
            Actor prey = (Actor)theTargets.get(TargetsIndex);
            if(prey != World.getPlayerAircraft() && (prey instanceof Aircraft) && prey.getArmy() != getArmy())
            {
                double rnd = TrueRandom.nextDouble(0.0D, 80D);
                double rndDist = TrueRandom.nextDouble(0.0D, 1000D);
                double missileDistance = GuidedMissileUtils.distanceBetween(this, prey);
                int victimSkillDist = ((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).Skill * 1000;
                int victimSkill = ((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).Skill * 10;
                if(missileDistance < (double)(1000 + victimSkillDist) + rndDist && (double)victimSkill > rnd)
                {
                    if((prey instanceof TypeFighter) || (prey instanceof TypeStormovik) || (prey instanceof TypeStormovikArmored))
                        ((Pilot)((SndAircraft) ((Aircraft)prey)).FM).set_maneuver(100);
                    if(((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).CT.Weapons[7] != null && ((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).CT.Weapons[7][((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).CT.Weapons[7].length - 1] != null && ((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).CT.Weapons[7][((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).CT.Weapons[7].length - 1].haveBullets())
                        ((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).CT.WeaponControl[7] = true;
                    else
                        ((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).CT.WeaponControl[7] = false;
                    if(((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).CT.Weapons[8] != null && ((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).CT.Weapons[8][((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).CT.Weapons[8].length - 1] != null && ((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).CT.Weapons[8][((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).CT.Weapons[8].length - 1].haveBullets())
                        ((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).CT.WeaponControl[8] = true;
                    else
                        ((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)prey)).FM)).CT.WeaponControl[8] = false;
                }
            }
        }

    }

    public static int HEAT_SPREAD_NONE = 0;
    public static int HEAT_SPREAD_AFT = 1;
    public static int HEAT_SPREAD_360 = 2;
    private int detectorType;
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
    private float maxFOVto;
    private float maxG;
    private float maxLaunchG;
    private float missileBaseSpeed;
    private float missileForce;
    private float missileForceRunUpTime;
    private float missileKalibr;
    protected float missileMass;
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
    private float trackLag;
    private Vector3d trajectoryVector3d;
    private float turnDiffMax;
    private float attackMaxDistance;
    DecimalFormat twoPlaces;
    public Actor victim;
    private Orient victimOffsetOrient;
    private Point3f victimOffsetPoint3d;
    private Vector3d victimSpeed;
    private double COMMAND_LEAD_TIMER0;
    private double COMMAND_LEAD_TIMER1;
    private Point3d COMMAND_LEAD_POINT;
    private Point3d COMMAND_LEAD_POINT_LAG;
    private Point3d COMMAND_LEAD_POS_CURR;
    private Point3d COMMAND_LEAD_POS_OLD;
    
    private float proxyDistance;

}