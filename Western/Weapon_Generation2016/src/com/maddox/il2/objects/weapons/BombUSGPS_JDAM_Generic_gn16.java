
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.ai.ground.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.bridges.LongBridge;
import com.maddox.rts.*;
import java.util.ArrayList;
import java.util.List;


public class BombUSGPS_JDAM_Generic_gn16 extends Bomb
{
    public BombUSGPS_JDAM_Generic_gn16()
    {
        bGPScatch = false;
        t1 = -1L;
    }

    public void start()
    {
        super.start();
        drawing(true);
        checkGPS();
        t1 = Time.current() + 1600L;
    }

    public void interpolateTick()
    {
        if(Time.current() > t1)
        {
            if(bGPScatch)
            {
                Point3d pTemp = new Point3d();
                pTemp.set(pT);
                float tlfs = Time.tickLenFs();
                float speedms = (float)getSpeed(null);
                speedms += (200F - speedms) * 0.1F * tlfs;
                super.pos.getAbs(p, or);
                if(p.z > Engine.land().HQ_Air(p.x, p.y))
                {
                    v.set(1.0D, 0.0D, 0.0D);
                    or.transform(v);
                    v.scale(speedms);
                    setSpeed(v);
                    p.x += ((Tuple3d) (v)).x * (double)tlfs;
                    p.y += ((Tuple3d) (v)).y * (double)tlfs;
                    p.z += ((Tuple3d) (v)).z * (double)tlfs - 0.011D;
                    pTemp.sub(p);
                    or.transformInv(pTemp);
                    float f2 = 0.05F;
                    if(p.distance(pTemp) > 0.0D)
                    {
                        if(((Tuple3d) (pTemp)).y > 0.10000000000000001D)
                           deltaAzimuth = -f2;
                        if(((Tuple3d) (pTemp)).y < -0.10000000000000001D)
                            deltaAzimuth = f2;
                        if(((Tuple3d) (pTemp)).z < -0.10000000000000001D)
                            deltaTangage = -f2;
                        if(((Tuple3d) (pTemp)).z > 0.10000000000000001D)
                            deltaTangage = f2;
                        or.increment(70F * f2 * deltaAzimuth, 70F * f2 * deltaTangage, 0.0F);
                        deltaAzimuth = deltaTangage = 0.0F;
                    }
                    super.pos.setAbs(p, or);
                    updateSound();
                }
            }
            else
            {
                super.curTm += Time.tickLenFs();
                Ballistics.updateBomb(this, super.M, super.S, super.J, super.DistFromCMtoStab);
                updateSound();
            }
        }
        else
        {
            super.curTm += Time.tickLenFs();
            Ballistics.updateBomb(this, super.M, super.S, super.J, super.DistFromCMtoStab);
            updateSound();
        }
    }

    private void checkGPS()
    {
        Point3d point3d = new Point3d();
        Aircraft aircraft = (Aircraft) getOwner();
        Actor actorTarget = null;
        // when the aircraft is under ground attacking , set GPS pos its target pos.
        if(aircraft.FM != null && ((Maneuver)aircraft.FM).target_ground != null)
        {
            point3d = ((Maneuver)aircraft.FM).target_ground.pos.getAbsPoint();
            if(aircraft.pos.getAbsPoint().distance(point3d) < maxDistance)
            {
                pT.set(point3d);
                bGPScatch = true;
                return;
            }
        }
        // when the mission waypoint is GATTACK , set GPS pos its target pos or waypoint pos.
        if(aircraft.FM != null && aircraft.FM.AP.way != null && aircraft.FM.AP.way.curr().Action == 3)
        {
            actorTarget = aircraft.FM.AP.way.curr().getTarget();
            if(actorTarget != null)
                actorTarget.pos.getAbs(point3d);
            else
                point3d = aircraft.FM.AP.way.curr().getP();
            if(aircraft.pos.getAbsPoint().distance(point3d) < maxDistance)
            {
                pT.set(point3d);
                bGPScatch = true;
                return;
            }
        }
        // Automatic stationary ground target search
        // search AAA
        actorTarget = nearestEnemyInRange(aircraft, com.maddox.il2.ai.ground.TgtFlak.class);
        if(actorTarget != null)
        {
            actorTarget.pos.getAbs(point3d);
            pT.set(point3d);
            bGPScatch = true;
            return;
        }
        // search Tank
        actorTarget = nearestEnemyInRange(aircraft, com.maddox.il2.ai.ground.TgtTank.class);
        if(actorTarget != null)
        {
            actorTarget.pos.getAbs(point3d);
            pT.set(point3d);
            bGPScatch = true;
            return;
        }
        // search Vehicle
        actorTarget = nearestEnemyInRange(aircraft, com.maddox.il2.ai.ground.TgtVehicle.class);
        if(actorTarget != null)
        {
            actorTarget.pos.getAbs(point3d);
            pT.set(point3d);
            bGPScatch = true;
            return;
        }
        // search Train
        actorTarget = nearestEnemyInRange(aircraft, com.maddox.il2.ai.ground.TgtTrain.class);
        if(actorTarget != null)
        {
            actorTarget.pos.getAbs(point3d);
            pT.set(point3d);
            bGPScatch = true;
            return;
        }
        // search Bridge
        actorTarget = nearestBridgeInRange(aircraft);
        if(actorTarget != null)
        {
            actorTarget.pos.getAbs(point3d);
            pT.set(point3d);
            bGPScatch = true;
            return;
        }
        // search Ship
        actorTarget = nearestEnemyInRange(aircraft, com.maddox.il2.ai.ground.TgtShip.class);
        if(actorTarget != null)
        {
            actorTarget.pos.getAbs(point3d);
            pT.set(point3d);
            bGPScatch = true;
            return;
        }
    }

    private Actor nearestEnemyInRange(Aircraft aircraft, Class class1)
    {
        double targetDistance = 0.0D;
        float targetAngle = 0.0F;
        float targetBait = 0.0F;
        float maxTargetBait = 0.0F;
        Actor actorsave = null;

        List list = Engine.targets();
        int i = list.size();
        for(int j = 0; j < i; j++)
        {
            Actor actor = (Actor)list.get(j);
            if(class1.isInstance(actor) && actor.getArmy() != aircraft.getArmy())
            {
                Point3d point3d = actor.pos.getAbsPoint();
                targetDistance = aircraft.pos.getAbsPoint().distance(point3d);
                if (targetDistance > maxDistance)
                    continue;
                targetAngle = angleBetween(aircraft, point3d);
                if (targetAngle > maxFOVfrom)
                    continue;

                targetBait = 1 / targetAngle / (float) (targetDistance * targetDistance);
                if (targetBait <= maxTargetBait)
                    continue;

                maxTargetBait = targetBait;
                actorsave = actor;
            }
        }
        return actorsave;
    }

    private Actor nearestBridgeInRange(Aircraft aircraft)
    {
        double targetDistance = 0.0D;
        float targetAngle = 0.0F;
        float targetBait = 0.0F;
        float maxTargetBait = 0.0F;
        ArrayList arraylist = World.cur().statics.bridges;
        int j = arraylist.size();
        double d1 = maxDistance * maxDistance;
        LongBridge longbridge = null;
        for(int l = 0; l < j; l++)
        {
            LongBridge longbridge1 = (LongBridge)arraylist.get(l);
            if(!longbridge1.isAlive())
                continue;
            Point3d point3d = longbridge1.pos.getAbsPoint();
            targetDistance = aircraft.pos.getAbsPoint().distance(point3d);
            if (targetDistance > maxDistance)
                continue;
            targetAngle = angleBetween(aircraft, point3d);
            if (targetAngle > maxFOVfrom)
                continue;

            targetBait = 1 / targetAngle / (float) (targetDistance * targetDistance);
            if (targetBait <= maxTargetBait)
                continue;

            maxTargetBait = targetBait;
            longbridge = longbridge1;
        }

        if(longbridge == null)
        {
            return null;
        }
        else
        {
            int k = longbridge.NumStateBits() / 2;
            return BridgeSegment.getByIdx(longbridge.bridgeIdx(), World.Rnd().nextInt(k));
        }
    }

    public static float angleBetween(Actor actorFrom, Point3d pointTo) {
        float angleRetVal = 180.1F;
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

    private Orient or = new Orient();
    private Point3d p = new Point3d();
    private Point3d pT = new Point3d();
    private Vector3d v = new Vector3d();
    private float deltaAzimuth;
    private float deltaTangage;
    private float deltaX;
    private long t1;
    private boolean bGPScatch;
    private static float maxFOVfrom = 120.0F;
    private static double maxDistance = 27700D;

}
