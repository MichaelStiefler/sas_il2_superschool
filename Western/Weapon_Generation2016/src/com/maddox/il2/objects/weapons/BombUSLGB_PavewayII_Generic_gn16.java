
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.AirGroup;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.TypeLaserDesignator;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            Bomb, Ballistics

public class BombUSLGB_PavewayII_Generic_gn16 extends Bomb
{

    public BombUSLGB_PavewayII_Generic_gn16()
    {
        popped = false;
        laseron = false;
        bCatchCarrier = false;
        bCatchExternal = false;
        actorLaserDesig = null;
        t1 = -1L;
        meshopen = (String) null;
    }

    public void start()
    {
        super.start();
        drawing(true);
        t1 = Time.current() + 1000L;
    }

    public void interpolateTick()
    {
        checklaser();
        if(!popped && Time.current() > t1)
        {
            if(meshopen != null)
                setMesh(meshopen);
            popped = true;
        }
        if(Time.current() > t1 + 1000L)
        {
            float tlfs = Time.tickLenFs();
            float speedms = (float)getSpeed(null);
            speedms += (200F - speedms) * 0.1F * tlfs;
            super.pos.getAbs(p, or);
            if(p.z > Engine.land().HQ_Air(p.x, p.y))
            {
                v.set(1.0D, 0.0D, 0.0D);
                or.transform(v);
                v.scale(speedms);
                if(laseron)
                {
                    setSpeed(v);
                    p.x += ((Tuple3d) (v)).x * (double)tlfs;
                    p.y += ((Tuple3d) (v)).y * (double)tlfs;
                    p.z += ((Tuple3d) (v)).z * (double)tlfs - 0.011D;
// commented out by western0221; what necessity ?
//                    if(isNet() && isNetMirror())
//                        super.pos.setAbs(p, or);
                    pT.sub(p);
                    or.transformInv(pT);
                    float f2 = 0.05F;
                    if(p.distance(pT) > 0.0D)
                    {
                        if(((Tuple3d) (pT)).y > 0.10D)
                            deltaAzimuth = -f2;
                        if(((Tuple3d) (pT)).y < -0.10D)
                            deltaAzimuth = f2;
                        if(((Tuple3d) (pT)).z < -0.10D)
                            deltaTangage = -f2;
                        if(((Tuple3d) (pT)).z > 0.10D)
                            deltaTangage = f2;
                        or.increment(70F * f2 * deltaAzimuth, 70F * f2 * deltaTangage, 0.0F);
                        deltaAzimuth = deltaTangage = 0.0F;
                    }
                    super.pos.setAbs(p, or);
                    updateSound();
                }
                else
                {
                    double pitch = or.getPitch();
                    for(;pitch > 180.0D; pitch -= 360.0D) ;
                    for(;pitch < -180.0D; pitch += 360.0D) ;
                    if(bCatchCarrier || bCatchExternal || pitch < -8.0D)
                    {
                        setSpeed(v);
                        p.x += ((Tuple3d) (v)).x * (double)tlfs;
                        p.y += ((Tuple3d) (v)).y * (double)tlfs;
                        p.z += ((Tuple3d) (v)).z * (double)tlfs - 0.011D;
                    }
                    super.curTm += Time.tickLenFs();
                    Ballistics.updateBomb(this, super.M, super.S, super.J, super.DistFromCMtoStab);
                    updateSound();
                }
            }
        }
        else
        {
            super.curTm += Time.tickLenFs();
            Ballistics.updateBomb(this, super.M, super.S, super.J, super.DistFromCMtoStab);
            updateSound();
        }
    }

    private void checklaser()
    {
        laseron = false;
        double targetDistance = 0.0D;
        float targetAngle = 0.0F;
        float targetBait = 0.0F;
        float maxTargetBait = 0.0F;
        Aircraft aircraft = (Aircraft) getOwner();
        // superior the Laser spot of this Paveway's owner than others'
        // but ignoring owner's laser after once catch external designator's
        while(!bCatchExternal && (aircraft instanceof TypeLaserDesignator) && ((TypeLaserDesignator) aircraft).getLaserOn())
        {
            Point3d point3d = new Point3d();
            point3d = ((TypeLaserDesignator)aircraft).getLaserSpot();
            if(point3d == null)
                break;
            if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, aircraft.pos.getAbsPoint()) < 0.98F)
                break;
            if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) < 0.96F)
                break;
            targetDistance = aircraft.pos.getAbsPoint().distance(point3d);
            if(targetDistance > maxDistance)
                break;
            if(this.pos.getAbsPoint().distance(point3d) > maxDistance)
                break;
            targetAngle = angleBetween(this, point3d);
            if(targetAngle > maxFOVfrom)
                break;

            pT.set(point3d);
            laseron = true;
            bCatchCarrier = true;
            actorLaserDesig = (Actor) aircraft;
            if(bLogDetail) System.out.println("BombUSLGB_(" + actorString(this) + ") - Carrier's LaserOn, laserSpotPos=" + point3d);
            break;
        }
        if(bCatchCarrier) return;

        // after once catch external designator's , follow only its laser
        while(bCatchExternal && actorLaserDesig != null && (actorLaserDesig instanceof TypeLaserDesignator) && ((TypeLaserDesignator) actorLaserDesig).getLaserOn())
        {
            Point3d point3d = new Point3d();
            point3d = ((TypeLaserDesignator)actorLaserDesig).getLaserSpot();
            if(point3d == null)
                break;
            if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, actorLaserDesig.pos.getAbsPoint()) < 0.98F)
                break;
            if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) < 0.96F)
                break;
            targetDistance = actorLaserDesig.pos.getAbsPoint().distance(point3d);
            if(targetDistance > maxDistance)
                break;
            if(this.pos.getAbsPoint().distance(point3d) > maxDistance)
                break;
            targetAngle = angleBetween(this, point3d);
            if(targetAngle > maxFOVfrom)
                break;

            pT.set(point3d);
            laseron = true;
            if(bLogDetail) System.out.println("BombUSLGB_(" + actorString(this) + ") - get external " + actorString(actorLaserDesig) + "'s laser, laserSpotPos=" + point3d);
            break;
        }
        if(bCatchExternal) return;

        // seak other Laser designator spots when Paveway's owner doesn't spot Laser
        if(!laseron)
        {
            Actor actor = null;
            Point3d point3d = new Point3d();
            List list = Engine.targets();
            int i = list.size();
            for(int j = 0; j < i; j++)
            {
                actor = (Actor)list.get(j);
                if(actor != aircraft && (actor instanceof TypeLaserDesignator) && ((TypeLaserDesignator) actor).getLaserOn() && actor.getArmy() == aircraft.getArmy())
                {
                    point3d = ((TypeLaserDesignator)actor).getLaserSpot();
                    if(point3d == null)
                        continue;
                    // Not target about objects behind of clouds from the Paveway's seaker.
                    if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, actor.pos.getAbsPoint()) < 0.98F)
                        continue;
                    if(Main.cur().clouds != null && Main.cur().clouds.getVisibility(point3d, this.pos.getAbsPoint()) < 0.96F)
                        continue;
                    targetDistance = actor.pos.getAbsPoint().distance(point3d);
                    if(targetDistance > maxDistance)
                        continue;
                    if(this.pos.getAbsPoint().distance(point3d) > maxDistance)
                        continue;
                    targetAngle = angleBetween(this, point3d);
                    if(targetAngle > maxFOVfrom)
                        continue;

                    targetBait = 1 / targetAngle / (float) (targetDistance * targetDistance);
                    if(targetBait <= maxTargetBait)
                        continue;

                    maxTargetBait = targetBait;
                    pT.set(point3d);
                    laseron = true;
                    bCatchExternal = true;
                    actorLaserDesig = actor;
                }
            }
            if(bLogDetail && laseron) System.out.println("BombUSLGB_(" + actorString(this) + ") - get external " + actorString(actor) + "'s laser, laserSpotPos=" + point3d + " as First!");
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

    private static String actorString(Actor actor) {
        if(!Actor.isValid(actor)) return "(InvalidActor)";
        String s;
        try {
            s = actor.getClass().getName();
        } catch(Exception e) {
            System.out.println("LaserDesignatorGeneric - actorString(): Cannot resolve class name of " + actor);
            return "(NoClassnameActor)";
        }
        int i = s.lastIndexOf('.');
        String strSection = s.substring(i + 1);
        strSection = strSection + '@' + Integer.toHexString(actor.hashCode());
        return strSection;
    }

    private Orient or = new Orient();
    private Point3d p = new Point3d();
    private Point3d pT = new Point3d();
    private Vector3d v = new Vector3d();
    private float deltaAzimuth;
    private float deltaTangage;
    private float deltaX;
    private long t1;
    private boolean popped;
    private boolean laseron;
    private boolean bCatchCarrier;
    private boolean bCatchExternal;
    private Actor actorLaserDesig;
    public String meshopen;
    private static float maxFOVfrom = 45.0F;
    private static double maxDistance = 20000D;

    private static boolean bLogDetail = false;
}
